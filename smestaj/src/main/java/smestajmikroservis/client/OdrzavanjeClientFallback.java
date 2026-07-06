package smestajmikroservis.client;

import org.springframework.stereotype.Component;
import smestajmikroservis.dtos.WorkOrderDto;
import smestajmikroservis.exception.ServiceUnavailableException;

import java.util.List;

@Component
public class OdrzavanjeClientFallback implements OdrzavanjeClient {

    @Override
    public List<WorkOrderDto> getWorkOrdersByUnitId(Long unitId) {
        throw new ServiceUnavailableException("Odrzavanje servis trenutno nije dostupan.");
    }

    @Override
    public List<WorkOrderDto> getAllWorkOrders() {
        throw new ServiceUnavailableException("Odrzavanje servis trenutno nije dostupan.");
    }

    @Override
    public String getWorkOrderStatus(Long id) {
        throw new ServiceUnavailableException("Odrzavanje servis trenutno nije dostupan.");
    }

    @Override
    public List<WorkOrderDto> getInProgressWorkOrders() {
        throw new ServiceUnavailableException("Odrzavanje servis trenutno nije dostupan.");
    }
}
