package odrzavanjemikroservis.entityMapper;

import odrzavanjemikroservis.dtos.MaintenanceTaskDto;
import odrzavanjemikroservis.dtos.WorkOrderDto;
import odrzavanjemikroservis.dtos.WorkerDto;
import odrzavanjemikroservis.entity.MaintenanceTask;
import odrzavanjemikroservis.entity.WorkOrder;
import odrzavanjemikroservis.entity.Worker;

public class EntityMapper {
//    public static Worker fromWorkerDtoToWorker(WorkerDto workerDto) {
//        Worker worker = new Worker();
//        worker.setFirstName(workerDto.getFirstName());
//        worker.setLastName(workerDto.getLastName());
//        worker.setSpeciality(workerDto.getSpeciality().toLowerCase());
//
//        return worker;
//    }
//
//    public static WorkerDto fromWorkerToDto(Worker worker) {
//        WorkerDto workerDto = new WorkerDto();
//        workerDto.setFirstName(worker.getFirstName());
//        workerDto.setLastName(worker.getLastName());
//        workerDto.setSpeciality(worker.getSpeciality());
//        workerDto.setId(worker.getId());
//        return workerDto;
//    }
//
//    public static MaintenanceTask fromMaintenanceTaskDtoToEntity(MaintenanceTaskDto dto) {
//        MaintenanceTask maintenanceTask = new MaintenanceTask();
//        maintenanceTask.setTaskName(dto.getTaskName());
//        maintenanceTask.setEstimatedDurationHours(dto.getEstimatedDurationHours());
//        return maintenanceTask;
//    }
//
//    public static MaintenanceTaskDto fromMaintenanceTaskToDto(MaintenanceTask maintenanceTask) {
//        MaintenanceTaskDto maintenanceTaskDto = new MaintenanceTaskDto();
//        maintenanceTaskDto.setTaskName(maintenanceTask.getTaskName());
//        maintenanceTaskDto.setEstimatedDurationHours(maintenanceTask.getEstimatedDurationHours());
//        maintenanceTaskDto.setId(maintenanceTask.getId());
//        return maintenanceTaskDto;
//    }
//
//
//    public static WorkOrder fromWorkOrderDtoToEntity(WorkOrderDto dto, MaintenanceTask task, Worker worker) {
//        WorkOrder workOrder = new WorkOrder();
//        if(task != null)
//            workOrder.setMaintenanceTask(task);
//        if(worker != null)
//            workOrder.setWorker(worker);
//
//        workOrder.setStatus(dto.getStatus());
//        workOrder.setUnitId(dto.getUnitId());
//        workOrder.setScheduledFor(dto.getScheduledFor());
//
//        return workOrder;
//    }
//
//    public static WorkOrderDto fromWorkOrderToDto(WorkOrder workOrder) {
//        WorkOrderDto dto = new WorkOrderDto();
//        dto.setId(workOrder.getId());
//        dto.setStatus(workOrder.getStatus());
//        dto.setUnitId(workOrder.getUnitId());
//        dto.setScheduledFor(workOrder.getScheduledFor());
//        if(workOrder.getMaintenanceTask() != null)
//            dto.setMaintenanceTaskId(workOrder.getMaintenanceTask().getId());
//        if(workOrder.getWorker() != null)
//            dto.setWorkerId(workOrder.getWorker().getId());
//
//        return dto;
//    }
}
