package org.example.bookingservice.service;

import lombok.RequiredArgsConstructor;
import org.example.bookingservice.config.RabbitMQConfig;
import org.example.bookingservice.dto.booking.BookingResponse;
import org.example.bookingservice.dto.booking.BookingRevenueSummaryResponse;
import org.example.bookingservice.dto.booking.BookingSearchResponse;
import org.example.bookingservice.dto.booking.BookingSummaryResponse;
import org.example.bookingservice.dto.booking.CreateBookingRequest;
import org.example.bookingservice.dto.booking.PricePreviewResponse;
import org.example.bookingservice.dto.booking.UpdateBookingStatusRequest;
import org.example.bookingservice.dto.integration.UnitBookingSummaryResponse;
import org.example.bookingservice.dto.integration.UnitOccupancyResponse;
import org.example.bookingservice.entity.AddOnItem;
import org.example.bookingservice.entity.Booking;
import org.example.bookingservice.entity.BookingStatusLog;
import org.example.bookingservice.entity.CancellationPolicy;
import org.example.bookingservice.entity.Guest;
import org.example.bookingservice.entity.enums.BookingStatus;
import org.example.bookingservice.exception.BadRequestException;
import org.example.bookingservice.exception.ResourceNotFoundException;
import org.example.bookingservice.mapper.BookingMapper;
import org.example.bookingservice.repository.AddOnItemRepository;
import org.example.bookingservice.repository.BookingRepository;
import org.example.bookingservice.repository.BookingStatusLogRepository;
import org.example.bookingservice.repository.CancellationPolicyRepository;
import org.example.bookingservice.repository.GuestRepository;
import org.raflab.sharedevents.BookingCreatedEvent;
import org.raflab.sharedevents.BookingInvoiceRequestedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private static final Logger log = LoggerFactory.getLogger(BookingService.class);


    private final AddOnItemRepository addOnItemRepository;
    private final BookingRepository bookingRepository;
    private final GuestRepository guestRepository;
    private final CancellationPolicyRepository cancellationPolicyRepository;
    private final BookingStatusLogRepository bookingStatusLogRepository;
    private final BookingMapper bookingMapper;
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public void createBooking(CreateBookingRequest request) {
        validateBookingDates(request);

        Guest guest = guestRepository.findById(request.getGuestId())
                .orElseThrow(() -> new ResourceNotFoundException("Guest with id " + request.getGuestId() + " was not found."));

        CancellationPolicy policy = resolvePolicy(request.getPolicyId());

        Booking booking = Booking.builder()
                .unitId(request.getUnitId())
                .guest(guest)
                .policy(policy)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .totalPrice(request.getTotalPrice())
                .status(BookingStatus.PENDING)
                .build();

        Booking savedBooking = bookingRepository.save(booking);

        BookingStatusLog statusLog = BookingStatusLog.builder()
                .booking(savedBooking)
                .oldStatus(null)
                .newStatus(BookingStatus.PENDING)
                .changedAt(Instant.now())
                .note("Initial booking state")
                .build();

        BookingCreatedEvent event = BookingCreatedEvent.builder()
                .bookingId(savedBooking.getId())
                .unitId(savedBooking.getUnitId())
                .guestId(guest.getId())
                .policyId(policy != null ? policy.getId() : null)
                .startDate(savedBooking.getStartDate())
                .endDate(savedBooking.getEndDate())
                .totalPrice(savedBooking.getTotalPrice())
                .build();

        BookingInvoiceRequestedEvent invoiceRequestedEvent = BookingInvoiceRequestedEvent.builder()
                .bookingId(savedBooking.getId())
                .guestId(guest.getId())
                .unitId(savedBooking.getUnitId())
                .startDate(savedBooking.getStartDate())
                .endDate(savedBooking.getEndDate())
                .subtotal(savedBooking.getTotalPrice())
                .build();

        bookingStatusLogRepository.save(statusLog);
        publishBookingCreatedAfterCommit(event);
        publishBookingInvoiceRequestedAfterCommit(invoiceRequestedEvent);
    }

    @Transactional(readOnly = true)
    public BookingResponse getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking with id " + id + " was not found."));

        return bookingMapper.toResponse(booking);
    }

    @Transactional(readOnly = true)
    public BookingSummaryResponse getBookingSummary(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking with id " + id + " was not found."));

        return bookingMapper.toSummaryResponse(booking);
    }

    @Transactional
    public void updateStatus(Long bookingId, UpdateBookingStatusRequest request) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking with id " + bookingId + " was not found."));

        if (booking.getStatus() == request.getStatus()) {
            throw new BadRequestException("Booking already has status " + request.getStatus() + ".");
        }

        BookingStatus previousStatus = booking.getStatus();
        booking.setStatus(request.getStatus());
        bookingRepository.save(booking);

        BookingStatusLog statusLog = BookingStatusLog.builder()
                .booking(booking)
                .oldStatus(previousStatus)
                .newStatus(request.getStatus())
                .changedAt(Instant.now())
                .note(request.getNote())
                .build();

        bookingStatusLogRepository.save(statusLog);
    }

    @Transactional(readOnly = true)
    public PricePreviewResponse getPricePreview(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking with id " + bookingId + " was not found."));

        List<AddOnItem> addOnItems = addOnItemRepository.findByBookingId(bookingId);
        BigDecimal basePrice = scaleAmount(booking.getTotalPrice());
        BigDecimal addOnTotal = addOnItems.stream()
                .map(AddOnItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal finalTotal = scaleAmount(basePrice.add(addOnTotal));

        return PricePreviewResponse.builder()
                .bookingId(booking.getId())
                .basePrice(basePrice)
                .addOnTotal(scaleAmount(addOnTotal))
                .totalPrice(finalTotal)
                .build();
    }

    @Transactional(readOnly = true)
    public List<BookingSearchResponse> searchBookings(BookingStatus status,
                                                      String guestEmail,
                                                      LocalDate startFrom,
                                                      LocalDate endTo) {
        validateSearchDates(startFrom, endTo);
        return bookingRepository.searchBookings(status, normalizeSearchText(guestEmail), startFrom, endTo);
    }

    @Transactional(readOnly = true)
    public List<BookingRevenueSummaryResponse> getRevenueSummaryByStatus() {
        return bookingRepository.summarizeRevenueByStatus();
    }

    @Transactional(readOnly = true)
    public UnitBookingSummaryResponse getUnitBookingSummary(Long unitId) {
        long activeBookingCount = bookingRepository.countByUnitIdAndStatusIn(unitId, activeStatuses());
        return UnitBookingSummaryResponse.builder()
                .unitId(unitId)
                .activeBookingCount(activeBookingCount)
                .hasActiveBookings(activeBookingCount > 0)
                .build();
    }

    @Transactional(readOnly = true)
    public UnitOccupancyResponse getUnitOccupancy(Long unitId, LocalDate date) {
        return bookingRepository.findFirstByUnitIdAndStatusInAndStartDateLessThanEqualAndEndDateGreaterThan(
                        unitId,
                        activeStatuses(),
                        date,
                        date
                )
                .map(booking -> UnitOccupancyResponse.builder()
                        .unitId(unitId)
                        .date(date)
                        .occupied(true)
                        .bookingId(booking.getId())
                        .bookingStatus(booking.getStatus())
                        .build())
                .orElseGet(() -> UnitOccupancyResponse.builder()
                        .unitId(unitId)
                        .date(date)
                        .occupied(false)
                        .build());
    }

    private void validateBookingDates(CreateBookingRequest request) {
        if (!request.getEndDate().isAfter(request.getStartDate())) {
            throw new BadRequestException("Booking end date must be after start date.");
        }
    }

    private void validateSearchDates(LocalDate startFrom, LocalDate endTo) {
        if (startFrom != null && endTo != null && endTo.isBefore(startFrom)) {
            throw new BadRequestException("Search end date must not be before search start date.");
        }
    }

    private String normalizeSearchText(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }

    private CancellationPolicy resolvePolicy(Long policyId) {
        if (policyId == null) {
            return null;
        }

        return cancellationPolicyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException("Cancellation policy with id " + policyId + " was not found."));
    }

    private BigDecimal scaleAmount(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP);
    }

    private List<BookingStatus> activeStatuses() {
        return List.of(BookingStatus.PENDING, BookingStatus.CONFIRMED);
    }

    private void publishBookingCreatedAfterCommit(BookingCreatedEvent event) {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            publishBookingCreated(event);
            return;
        }

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                publishBookingCreated(event);
            }
        });
    }

    private void publishBookingInvoiceRequestedAfterCommit(BookingInvoiceRequestedEvent event) {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            publishBookingInvoiceRequested(event);
            return;
        }

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                publishBookingInvoiceRequested(event);
            }
        });
    }

    private void publishBookingCreated(BookingCreatedEvent event) {
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.SMESTAJ_EXCHANGE,
                    RabbitMQConfig.BOOKING_CREATED_ROUTING_KEY,
                    event
            );
            log.info("[RabbitMQ] Objavljen dogadjaj booking.created za booking id={}", event.getBookingId());
        } catch (AmqpException e) {
            log.warn("[RabbitMQ] Nije moguce objaviti dogadjaj booking.created: {}", e.getMessage());
        }
    }

    private void publishBookingInvoiceRequested(BookingInvoiceRequestedEvent event) {
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.FINANCE_EXCHANGE,
                    RabbitMQConfig.BOOKING_INVOICE_REQUESTED_ROUTING_KEY,
                    event
            );
            log.info("[RabbitMQ] Objavljen dogadjaj booking.invoice-requested za booking id={}", event.getBookingId());
        } catch (AmqpException e) {
            log.warn("[RabbitMQ] Nije moguce objaviti dogadjaj booking.invoice-requested: {}", e.getMessage());
        }
    }
}
