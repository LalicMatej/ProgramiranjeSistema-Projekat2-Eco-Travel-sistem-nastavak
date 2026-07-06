package org.example.financeservice.mapper;

import org.example.financeservice.dto.finance.InvoiceResponse;
import org.example.financeservice.dto.finance.InvoiceTotalResponse;
import org.example.financeservice.entity.Invoice;
import org.springframework.stereotype.Component;

@Component
public class InvoiceMapper {

    public InvoiceResponse toResponse(Invoice invoice) {
        return InvoiceResponse.builder()
                .id(invoice.getId())
                .bookingId(invoice.getBookingId())
                .taxRateId(invoice.getTaxRate() != null ? invoice.getTaxRate().getId() : null)
                .subtotal(invoice.getSubtotal())
                .totalAmount(invoice.getTotalAmount())
                .status(invoice.getStatus())
                .build();
    }

    public InvoiceTotalResponse toTotalResponse(Invoice invoice) {
        return InvoiceTotalResponse.builder()
                .invoiceId(invoice.getId())
                .subtotal(invoice.getSubtotal())
                .totalAmount(invoice.getTotalAmount())
                .build();
    }
}
