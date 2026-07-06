package org.example.financeservice.consumer;

import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.financeservice.config.RabbitMQConfig;
import org.example.financeservice.service.InvoiceService;
import org.raflab.sharedevents.BookingInvoiceRequestedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingInvoiceRequestedConsumer {

    private final InvoiceService invoiceService;

    @RabbitListener(queues = RabbitMQConfig.BOOKING_INVOICE_REQUESTED_QUEUE)
    public void onBookingInvoiceRequested(BookingInvoiceRequestedEvent event,
                                          Channel channel,
                                          @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        log.info("[RabbitMQ] Primljen dogadjaj booking.invoice-requested za booking id={}", event.getBookingId());
        try {
            invoiceService.createInvoiceFromBookingEvent(event);
            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.error("[RabbitMQ] Greska pri obradi booking.invoice-requested za booking id={}: {}",
                    event.getBookingId(),
                    e.getMessage());
            channel.basicNack(tag, false, false);
        }
    }
}
