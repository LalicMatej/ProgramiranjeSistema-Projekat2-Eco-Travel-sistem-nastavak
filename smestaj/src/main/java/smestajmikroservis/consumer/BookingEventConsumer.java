package smestajmikroservis.consumer;

import com.rabbitmq.client.Channel;
import org.raflab.sharedevents.BookingCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import smestajmikroservis.config.RabbitMQConfig;
import smestajmikroservis.dtos.AvailabilityCalendarDto;
import smestajmikroservis.entity.Unit;
import smestajmikroservis.repository.UnitRepository;
import smestajmikroservis.services.AvailabilityCalendarService;

import java.io.IOException;

@Component
public class BookingEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(BookingEventConsumer.class);

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private AvailabilityCalendarService availabilityCalendarService;

    @RabbitListener(queues = RabbitMQConfig.BOOKING_CREATED_QUEUE)
    public void onBookingCreated(BookingCreatedEvent event, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        log.info("[RabbitMQ] Primljen dogadjaj booking.created za booking id={}, jedinica id={}", event.getBookingId(), event.getUnitId());
        try {
            Unit unit = unitRepository.findById(event.getUnitId())
                    .orElseThrow(() -> new IllegalArgumentException("Unit not found"));

            AvailabilityCalendarDto availabilityCalendarDto = AvailabilityCalendarDto.builder()
                    .startDate(event.getStartDate())
                    .endDate(event.getEndDate())
                    .reason("Rezervacija je kreirana.")
                    .unitName(unit.getName())
                    .unitType(unit.getUnit_type())
                    .build();
            availabilityCalendarService.addReservation(availabilityCalendarDto);
            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.error("[RabbitMQ] Greska pri obradi booking.created za booking id={}: {}",
                    event.getBookingId(), e.getMessage());
            channel.basicNack(tag, false, false);
        }
    }
}
