package odrzavanjemikroservis.entityMapper;

import odrzavanjemikroservis.dtos.MaintenanceTaskDto;
import odrzavanjemikroservis.dtos.WorkOrderDto;
import odrzavanjemikroservis.dtos.WorkerDto;
import odrzavanjemikroservis.entity.MaintenanceTask;
import odrzavanjemikroservis.entity.WorkOrder;
import odrzavanjemikroservis.entity.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MapperFactory {
    private final Map<Class<?>, EntityMapperFactory<?, ?>> mappers = new HashMap<>();

    @Autowired
    public MapperFactory(
            WorkerMapper workerMapper,
            MaintenanceTaskMapper taskMapper,
            WorkOrderMapper workOrderMapper) {

        mappers.put(Worker.class, workerMapper);
        mappers.put(WorkerDto.class, workerMapper);
        mappers.put(MaintenanceTask.class, taskMapper);
        mappers.put(MaintenanceTaskDto.class, taskMapper);
        mappers.put(WorkOrder.class, workOrderMapper);
        mappers.put(WorkOrderDto.class, workOrderMapper);
    }

    @SuppressWarnings("unchecked")
    public <T, D> EntityMapperFactory<T, D> getMapper(Class<T> entityClass) {
        return (EntityMapperFactory<T, D>) mappers.get(entityClass);
    }
}
