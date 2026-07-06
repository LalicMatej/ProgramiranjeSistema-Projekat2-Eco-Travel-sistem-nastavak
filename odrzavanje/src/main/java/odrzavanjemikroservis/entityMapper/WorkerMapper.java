package odrzavanjemikroservis.entityMapper;

import odrzavanjemikroservis.dtos.WorkerDto;
import odrzavanjemikroservis.entity.Worker;
import org.springframework.stereotype.Component;

@Component

public class WorkerMapper implements EntityMapperFactory<Worker, WorkerDto> {
    @Override
    public Worker toEntity(WorkerDto dto) {
        Worker worker = new Worker();
        worker.setFirstName(dto.getFirstName());
        worker.setLastName(dto.getLastName());
        worker.setSpeciality(dto.getSpeciality().toLowerCase());

        return worker;
    }

    @Override
    public WorkerDto toDto(Worker worker) {
        WorkerDto workerDto = new WorkerDto();
        workerDto.setFirstName(worker.getFirstName());
        workerDto.setLastName(worker.getLastName());
        workerDto.setSpeciality(worker.getSpeciality());
        workerDto.setId(worker.getId());
        return workerDto;
    }
}
