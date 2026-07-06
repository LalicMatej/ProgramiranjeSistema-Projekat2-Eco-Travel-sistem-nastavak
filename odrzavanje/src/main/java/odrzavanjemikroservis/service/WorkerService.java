package odrzavanjemikroservis.service;

import jakarta.persistence.EntityNotFoundException;
import odrzavanjemikroservis.dtos.WorkerDto;
import odrzavanjemikroservis.dtos.WorkerStatisticsDto;
import odrzavanjemikroservis.entity.WorkOrder;
import odrzavanjemikroservis.entity.Worker;
import odrzavanjemikroservis.entityMapper.MapperFactory;
import odrzavanjemikroservis.repository.WorkOrderRepository;
import odrzavanjemikroservis.repository.WorkerRepository;
import odrzavanjemikroservis.entityMapper.EntityMapper;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorkerService {

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private WorkOrderRepository workOrderRepository;
    @Autowired
    private MapperFactory mapperFactory;


    public WorkerDto addWorker(WorkerDto workerDto) {
        Worker worker = mapperFactory.getMapper(Worker.class).toEntity(workerDto); //EntityMapper.fromWorkerDtoToWorker(workerDto);

        return (WorkerDto) mapperFactory.getMapper(Worker.class).toDto(workerRepository.save(worker));//EntityMapper.fromWorkerToDto(workerRepository.save(worker));
    }

    public List<WorkerDto> getWorkersBySpeciality(String specijalnost) {
        //ako je prazan string, vracaju se svi radnici


        List<WorkerDto> dtos = new ArrayList<>();
        if(specijalnost == null){
            List<Worker> workers = workerRepository.findAll();
            for(Worker worker : workers){
                dtos.add( (WorkerDto) mapperFactory.getMapper(Worker.class).toDto(worker) ); //EntityMapper.fromWorkerToDto(worker));
            }
            return dtos;
        }
        String speciality = specijalnost.toLowerCase();

        List<Worker> workers = workerRepository.findBySpeciality(speciality);
        for(Worker worker : workers){
            dtos.add( (WorkerDto) mapperFactory.getMapper(Worker.class).toDto(worker) );//EntityMapper.fromWorkerToDto(worker));
        }
        return dtos;
    }


    public void deleteWorker(Long id){
        Worker w = workerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Radnik sa prosledjenim id-jem nije pronadjen"));
        workerRepository.deleteById(id);
    }

    public List<WorkerStatisticsDto> getWorkerStatisticsBySpeciality() {
        return workerRepository.getWorkerStatisticsBySpeciality();
    }

    // provera da li se poklapa sa nekim terminima i 1 sat lufta da bi stigao
    public void checkWorkerAvailability(Long workerId, LocalDateTime scheduledFor) {
        // Proveri da li radnik postoji
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new EntityNotFoundException("Radnik sa id " + workerId + " ne postoji"));


        List<WorkOrder> existingOrders = workOrderRepository.findByWorkerIdAndScheduledFor(workerId, scheduledFor);

        if (!existingOrders.isEmpty()) {
            for (WorkOrder existing : existingOrders) {
                LocalDateTime existingStart = existing.getScheduledFor();
                LocalDateTime existingEnd = existingStart.plusHours(
                        existing.getMaintenanceTask() != null ? existing.getMaintenanceTask().getEstimatedDurationHours() : 1
                );

                if (scheduledFor.isBefore(existingEnd) &&
                        scheduledFor.plusHours(1).isAfter(existingStart)) {
                    System.out.println("Radnik vec ima nesto zakazano tada");
                    throw new IllegalStateException(
                            "Radnik " + worker.getFirstName() + " " + worker.getLastName() +
                                    " vec ima zakazan task u terminu " + existingStart
                    );
                }
            }
        }
    }
}
