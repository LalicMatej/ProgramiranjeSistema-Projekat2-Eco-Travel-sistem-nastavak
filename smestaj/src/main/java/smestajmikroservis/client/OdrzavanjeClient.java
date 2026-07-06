// smestajmikroservis/client/OdrzavanjeClient.java
package smestajmikroservis.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import smestajmikroservis.dtos.WorkOrderDto;
import smestajmikroservis.dtos.WorkOrderDto;

import java.util.List;

@FeignClient(
        name = "odrzavanje-service",
        url = "${smestaj.odrzavanje.url}",
        fallback = OdrzavanjeClientFallback.class,
        configuration = OdrzavanjeClientConfig.class
)
public interface OdrzavanjeClient {


    @GetMapping("/api/workOrder/byUnit/{unitId}")
    List<WorkOrderDto> getWorkOrdersByUnitId(@PathVariable("unitId") Long unitId);

    @GetMapping("/api/workOrder/all")
    List<WorkOrderDto> getAllWorkOrders();

    // string
    @GetMapping("/api/workOrder/status/{id}")
    String getWorkOrderStatus(@PathVariable("id") Long id);

    @GetMapping("/api/workOrder/inProgress")
    List<WorkOrderDto> getInProgressWorkOrders();


}