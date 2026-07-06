package odrzavanjemikroservis.entityMapper;

import odrzavanjemikroservis.dtos.MaintenanceTaskDto;
import odrzavanjemikroservis.entity.MaintenanceTask;
import org.springframework.stereotype.Component;

@Component

public class MaintenanceTaskMapper implements EntityMapperFactory<MaintenanceTask, MaintenanceTaskDto> {
    @Override
    public MaintenanceTaskDto toDto(MaintenanceTask maintenanceTask) {
        MaintenanceTaskDto maintenanceTaskDto = new MaintenanceTaskDto();
        maintenanceTaskDto.setTaskName(maintenanceTask.getTaskName());
        maintenanceTaskDto.setEstimatedDurationHours(maintenanceTask.getEstimatedDurationHours());
        maintenanceTaskDto.setId(maintenanceTask.getId());
        return maintenanceTaskDto;
    }

    @Override
    public MaintenanceTask toEntity(MaintenanceTaskDto dto) {
        MaintenanceTask maintenanceTask = new MaintenanceTask();
        maintenanceTask.setTaskName(dto.getTaskName());
        maintenanceTask.setEstimatedDurationHours(dto.getEstimatedDurationHours());
        return maintenanceTask;    }
}
