package odrzavanjemikroservis.consumer;

import com.rabbitmq.client.Channel;
import odrzavanjemikroservis.config.RabbitMQConfig;
import odrzavanjemikroservis.service.WorkOrderService;
import org.raflab.sharedevents.UnitDeletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SmestajEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(SmestajEventConsumer.class);

    @Autowired
    private WorkOrderService workOrderService;

    @RabbitListener(queues = RabbitMQConfig.ODRZAVANJE_UNIT_DELETED_QUEUE)
    public void onUnitDeleted(UnitDeletedEvent event, Channel channel,
                              @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        log.info("[RabbitMQ] Primljen dogadjaj unit.deleted za jedinicu id={} ({})",
                event.getUnitId(), event.getUnitName());
        try {
            workOrderService.cancelAllForUnit(event.getUnitId());
            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.error("[RabbitMQ] Greska pri obradi unit.deleted za jedinicu id={}: {}",
                    event.getUnitId(), e.getMessage());
            channel.basicNack(tag, false, false);
        }
    }
}
