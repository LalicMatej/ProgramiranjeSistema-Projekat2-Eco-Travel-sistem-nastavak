package org.example.financeservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.financeservice.dto.finance.CreateInvoiceRequest;
import org.example.financeservice.dto.finance.InvoiceResponse;
import org.example.financeservice.dto.finance.InvoiceTotalResponse;
import org.example.financeservice.dto.finance.RegisterTransactionRequest;
import org.example.financeservice.dto.integration.RemoteBookingPricePreviewResponse;
import org.example.financeservice.dto.integration.RemoteBookingResponse;
import org.example.financeservice.dto.integration.RemoteBookingSummaryResponse;
import org.example.financeservice.dto.integration.enums.RemoteBookingStatus;
import org.example.financeservice.entity.Invoice;
import org.example.financeservice.entity.PaymentMethod;
import org.example.financeservice.entity.TaxRate;
import org.example.financeservice.entity.Transaction;
import org.example.financeservice.entity.enums.InvoiceStatus;
import org.example.financeservice.exception.BadRequestException;
import org.example.financeservice.exception.ResourceNotFoundException;
import org.example.financeservice.exception.ServiceUnavailableException;
import org.example.financeservice.integration.BookingClientResult;
import org.example.financeservice.integration.BookingClientFacade;
import org.example.financeservice.integration.IntegrationResultStatus;
import org.example.financeservice.mapper.InvoiceMapper;
import org.example.financeservice.repository.InvoiceRepository;
import org.example.financeservice.repository.PaymentMethodRepository;
import org.example.financeservice.repository.TaxRateRepository;
import org.example.financeservice.repository.TransactionRepository;
import org.raflab.sharedevents.BookingInvoiceRequestedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceService {

    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");
    private static final String AUTO_INVOICE_TAX_NAME = "VAT";

    private final InvoiceRepository invoiceRepository;
    private final TaxRateRepository taxRateRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final TransactionRepository transactionRepository;
    private final InvoiceMapper invoiceMapper;
    private final BookingClientFacade bookingClientFacade;

    @Transactional
    public void createInvoice(CreateInvoiceRequest request) {
        RemoteBookingResponse booking = requireSuccessfulBooking(
                bookingClientFacade.getBooking(request.getBookingId())
        );
        RemoteBookingSummaryResponse bookingSummary = requireSuccessfulBookingSummary(
                bookingClientFacade.getBookingSummary(request.getBookingId())
        );
        RemoteBookingPricePreviewResponse pricePreview = requireSuccessfulBookingPricePreview(
                bookingClientFacade.getBookingPricePreview(request.getBookingId())
        );

        validateBookableBooking(bookingSummary);
        validateBookingResponseConsistency(booking, bookingSummary, pricePreview);
        validateSubtotalMatchesBookingPreview(request.getSubtotal(), pricePreview);

        TaxRate taxRate = taxRateRepository.findById(request.getTaxRateId())
                .orElseThrow(() -> new ResourceNotFoundException("Tax rate with id " + request.getTaxRateId() + " was not found."));

        BigDecimal totalAmount = calculateTotalAmount(request.getSubtotal(), taxRate.getPercentage());

        Invoice invoice = Invoice.builder()
                .bookingId(request.getBookingId())
                .taxRate(taxRate)
                .subtotal(scaleAmount(request.getSubtotal()))
                .totalAmount(totalAmount)
                .status(request.getStatus())
                .build();

        invoiceRepository.save(invoice);
    }

    @Transactional
    public void createInvoiceFromBookingEvent(BookingInvoiceRequestedEvent event) {
        if (invoiceRepository.existsByBookingId(event.getBookingId())) {
            log.info("Invoice already exists for bookingId={}. Skipping automatic invoice creation.",
                    event.getBookingId());
            return;
        }

        TaxRate taxRate = taxRateRepository.findByTaxNameIgnoreCase(AUTO_INVOICE_TAX_NAME)
                .orElseThrow(() -> new ResourceNotFoundException("Tax rate " + AUTO_INVOICE_TAX_NAME + " was not found."));

        BigDecimal subtotal = scaleAmount(event.getSubtotal());
        BigDecimal totalAmount = calculateTotalAmount(subtotal, taxRate.getPercentage());

        Invoice invoice = Invoice.builder()
                .bookingId(event.getBookingId())
                .taxRate(taxRate)
                .subtotal(subtotal)
                .totalAmount(totalAmount)
                .status(InvoiceStatus.ISSUED)
                .build();

        invoiceRepository.save(invoice);
        log.info("Automatically created invoice for bookingId={} with status={}",
                event.getBookingId(),
                InvoiceStatus.ISSUED);
    }

    @Transactional(readOnly = true)
    public InvoiceResponse getInvoiceById(Long id) {
        Invoice invoice = findInvoiceById(id);
        return invoiceMapper.toResponse(invoice);
    }

    @Transactional(readOnly = true)
    public InvoiceTotalResponse getInvoiceTotal(Long id) {
        Invoice invoice = findInvoiceById(id);
        return invoiceMapper.toTotalResponse(invoice);
    }

    @Transactional
    public void registerTransaction(Long invoiceId, RegisterTransactionRequest request) {
        Invoice invoice = findInvoiceById(invoiceId);
        validateInvoiceAcceptsPayment(invoice);

        PaymentMethod paymentMethod = paymentMethodRepository.findById(request.getPaymentMethodId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment method with id " + request.getPaymentMethodId() + " was not found."));

        if (!paymentMethod.isActive()) {
            throw new BadRequestException("Payment method with id " + paymentMethod.getId() + " is not active.");
        }

        BigDecimal requestedAmount = scaleAmount(request.getAmount());
        if (requestedAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Transaction amount must be greater than zero.");
        }

        BigDecimal paidAmount = transactionRepository.sumAmountsByInvoiceId(invoiceId);
        BigDecimal remainingAmount = invoice.getTotalAmount().subtract(paidAmount);

        if (requestedAmount.compareTo(remainingAmount) > 0) {
            throw new BadRequestException("Transaction amount exceeds the remaining invoice balance.");
        }

        Transaction transaction = Transaction.builder()
                .invoice(invoice)
                .paymentMethod(paymentMethod)
                .amount(requestedAmount)
                .transactionDate(Instant.now())
                .build();

        transactionRepository.save(transaction);

        BigDecimal newPaidAmount = paidAmount.add(requestedAmount);
        if (newPaidAmount.compareTo(invoice.getTotalAmount()) >= 0) {
            invoice.setStatus(InvoiceStatus.PAID);
        } else if (invoice.getStatus() == InvoiceStatus.DRAFT) {
            invoice.setStatus(InvoiceStatus.ISSUED);
        }

        invoiceRepository.save(invoice);
    }

    private Invoice findInvoiceById(Long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice with id " + id + " was not found."));
    }

    // Validacije za payload i da status mora biti SUCCESS
    private RemoteBookingResponse requireSuccessfulBooking(BookingClientResult<RemoteBookingResponse> result) {
        if (result.isSuccess() && result.getPayload() != null) {
            return result.getPayload();
        }

        throw mapRemoteBookingFailure(result);
    }

    private RemoteBookingSummaryResponse requireSuccessfulBookingSummary(BookingClientResult<RemoteBookingSummaryResponse> result) {
        if (result.isSuccess() && result.getPayload() != null) {
            return result.getPayload();
        }

        throw mapRemoteBookingFailure(result);
    }

    private RemoteBookingPricePreviewResponse requireSuccessfulBookingPricePreview(BookingClientResult<RemoteBookingPricePreviewResponse> result) {
        if (result.isSuccess() && result.getPayload() != null) {
            return result.getPayload();
        }

        throw mapRemoteBookingFailure(result);
    }

    private void validateInvoiceAcceptsPayment(Invoice invoice) {
        if (invoice.getStatus() == InvoiceStatus.CANCELED) {
            throw new BadRequestException("Canceled invoices cannot receive payments.");
        }

        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new BadRequestException("Invoice with id " + invoice.getId() + " is already fully paid.");
        }
    }

    // Business logic validacije..
    private void validateBookableBooking(RemoteBookingSummaryResponse bookingSummary) {
        if (bookingSummary.getStatus() == RemoteBookingStatus.CANCELED) {
            throw new BadRequestException("Invoice cannot be created for a canceled booking.");
        }

        if (bookingSummary.getStatus() == RemoteBookingStatus.COMPLETED) {
            throw new BadRequestException("Invoice cannot be created for a completed booking.");
        }

        if (bookingSummary.getStatus() != RemoteBookingStatus.PENDING
                && bookingSummary.getStatus() != RemoteBookingStatus.CONFIRMED) {
            throw new BadRequestException("Invoice cannot be created for booking status " + bookingSummary.getStatus() + ".");
        }
    }

    private void validateBookingResponseConsistency(RemoteBookingResponse booking,
                                                    RemoteBookingSummaryResponse summary,
                                                    RemoteBookingPricePreviewResponse pricePreview) {
        if (!booking.getId().equals(summary.getId()) || !booking.getId().equals(pricePreview.getBookingId())) {
            throw new BadRequestException("Booking service returned inconsistent identifiers across integration calls.");
        }

        if (!booking.getGuestId().equals(summary.getGuestId())) {
            throw new BadRequestException("Booking service returned inconsistent guest identifiers.");
        }

        if (booking.getStatus().name().equals(summary.getStatus().name())) {
            return;
        }

        throw new BadRequestException("Booking service returned inconsistent booking status values.");
    }

    private void validateSubtotalMatchesBookingPreview(BigDecimal subtotal, RemoteBookingPricePreviewResponse pricePreview) {
        BigDecimal scaledSubtotal = scaleAmount(subtotal);
        BigDecimal expectedSubtotal = scaleAmount(pricePreview.getTotalPrice());

        if (scaledSubtotal.compareTo(expectedSubtotal) != 0) {
            throw new BadRequestException(
                    "Invoice subtotal must match booking preview total price. Expected " + expectedSubtotal + "."
            );
        }
    }

    private RuntimeException mapRemoteBookingFailure(BookingClientResult<?> result) {
        if (result.getStatus() == IntegrationResultStatus.NOT_FOUND) {
            return new ResourceNotFoundException(result.getMessage());
        }

        if (result.getStatus() == IntegrationResultStatus.INVALID_RESPONSE) {
            return new BadRequestException(result.getMessage());
        }

        log.warn("Booking integration failed for bookingId={} status={} message={}",
                result.getBookingId(),
                result.getStatus(),
                result.getMessage());
        return new ServiceUnavailableException(result.getMessage());
    }

    private BigDecimal calculateTotalAmount(BigDecimal subtotal, BigDecimal percentage) {
        BigDecimal scaledSubtotal = scaleAmount(subtotal);
        BigDecimal taxAmount = scaledSubtotal
                .multiply(percentage)
                .divide(ONE_HUNDRED, 2, RoundingMode.HALF_UP);

        return scaleAmount(scaledSubtotal.add(taxAmount));
    }

    private BigDecimal scaleAmount(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP);
    }
}
