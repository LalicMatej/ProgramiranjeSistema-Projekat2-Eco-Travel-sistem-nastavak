package smestajmikroservis.consumer;

import com.rabbitmq.client.Channel;
import org.raflab.sharedevents.WorkOrderCreatedEvent;
import org.raflab.sharedevents.WorkOrderStatusChangedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import smestajmikroservis.config.RabbitMQConfig;
import smestajmikroservis.services.AvailabilityCalendarService;

import java.io.IOException;

@Component
public class OdrzavanjeEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(OdrzavanjeEventConsumer.class);

    @Autowired
    private AvailabilityCalendarService availabilityCalendarService;

    @RabbitListener(queues = RabbitMQConfig.SMESTAJ_WORKORDER_CREATED_QUEUE)
    public void onWorkOrderCreated(WorkOrderCreatedEvent event, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        log.info("[RabbitMQ] Primljen dogadjaj workorder.created za nalog id={}, jedinica id={}", event.getWorkOrderId(), event.getUnitId());
        try {
            availabilityCalendarService.blockForMaintenance(event);
            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.error("[RabbitMQ] Greska pri obradi workorder.created za nalog id={}: {}",
                    event.getWorkOrderId(), e.getMessage());
            channel.basicNack(tag, false, false);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.SMESTAJ_WORKORDER_COMPLETED_QUEUE)
    public void onWorkOrderCompleted(WorkOrderStatusChangedEvent event, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        log.info("[RabbitMQ] Primljen dogadjaj workorder.completed za nalog id={}, jedinica id={}",
                event.getWorkOrderId(), event.getUnitId());
        try {
            availabilityCalendarService.removeMaintenanceBlock(event.getWorkOrderId());
            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.error("[RabbitMQ] Greska pri obradi workorder.completed za nalog id={}: {}",
                    event.getWorkOrderId(), e.getMessage());
            channel.basicNack(tag, false, false);
        }
    }
}
