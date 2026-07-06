package odrzavanjemikroservis.entityMapper;

import odrzavanjemikroservis.dtos.WorkOrderDto;
import odrzavanjemikroservis.entity.MaintenanceTask;
import odrzavanjemikroservis.entity.WorkOrder;
import odrzavanjemikroservis.entity.Worker;
import org.springframework.stereotype.Component;

@Component
public class WorkOrderMapper implements EntityMapperFactory<WorkOrder, WorkOrderDto> {
    @Override
    public WorkOrder toEntity(WorkOrderDto dto) {
        return null;
    }

    @Override
    public WorkOrderDto toDto(WorkOrder workOrder) {
        WorkOrderDto dto = new WorkOrderDto();
        dto.setId(workOrder.getId());
        dto.setStatus(workOrder.getStatus());
        dto.setUnitId(workOrder.getUnitId());
        dto.setScheduledFor(workOrder.getScheduledFor());
        if(workOrder.getMaintenanceTask() != null)
            dto.setMaintenanceTaskId(workOrder.getMaintenanceTask().getId());
        if(workOrder.getWorker() != null)
            dto.setWorkerId(workOrder.getWorker().getId());

        return dto;
    }

    public WorkOrder toEntityWithDependencies(WorkOrderDto dto, MaintenanceTask task, Worker worker) {
        WorkOrder workOrder = new WorkOrder();
        if(task != null)
            workOrder.setMaintenanceTask(task);
        if(worker != null)
            workOrder.setWorker(worker);

        workOrder.setStatus(dto.getStatus());
        workOrder.setUnitId(dto.getUnitId());
        workOrder.setScheduledFor(dto.getScheduledFor());

        return workOrder;
    }
}
