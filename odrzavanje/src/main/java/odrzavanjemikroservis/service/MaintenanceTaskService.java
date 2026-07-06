package odrzavanjemikroservis.service;

import odrzavanjemikroservis.dtos.MaintenanceTaskDto;
import odrzavanjemikroservis.entity.MaintenanceTask;
import odrzavanjemikroservis.entityMapper.MaintenanceTaskMapper;
import odrzavanjemikroservis.entityMapper.MapperFactory;
import odrzavanjemikroservis.repository.MaintenanceTaskRepository;
import odrzavanjemikroservis.entityMapper.EntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MaintenanceTaskService {

    @Autowired
    private  MaintenanceTaskRepository maintenanceTaskRepository;
    @Autowired
    private MaintenanceTaskMapper maintenanceTaskMapper;
    @Autowired
    private MapperFactory mapperFactory;


    public MaintenanceTaskDto createMaintenanceTask(MaintenanceTaskDto maintenanceTaskDto) {
        MaintenanceTask maintenanceTask = mapperFactory.getMapper(MaintenanceTask.class).toEntity(maintenanceTaskDto); //EntityMapper.fromMaintenanceTaskDtoToEntity(maintenanceTaskDto);
        return (MaintenanceTaskDto) mapperFactory.getMapper(MaintenanceTask.class).toDto(maintenanceTaskRepository.save(maintenanceTask));//EntityMapper.fromMaintenanceTaskToDto(maintenanceTaskRepository.save(maintenanceTask));
    }

    public List<MaintenanceTaskDto> getAllMaintenanceTasks() {
        List<MaintenanceTask> maintenanceTasks = maintenanceTaskRepository.findAll();
        List<MaintenanceTaskDto> maintenanceTaskDtos = new ArrayList<>();
        for (MaintenanceTask maintenanceTask : maintenanceTasks) {
            maintenanceTaskDtos.add((MaintenanceTaskDto) mapperFactory.getMapper(MaintenanceTask.class).toDto(maintenanceTask));//EntityMapper.fromMaintenanceTaskToDto(maintenanceTask));
        }
        return maintenanceTaskDtos;
    }

    //todo dodati da za task id mogu da izlistam work_orders
}
