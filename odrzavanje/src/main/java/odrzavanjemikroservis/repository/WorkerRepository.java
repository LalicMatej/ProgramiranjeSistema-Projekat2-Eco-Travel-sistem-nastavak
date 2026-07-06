package odrzavanjemikroservis.repository;

import odrzavanjemikroservis.dtos.WorkerStatisticsDto;
import odrzavanjemikroservis.entity.WorkOrder;
import odrzavanjemikroservis.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long> {

    @Query("select w from Worker w where w.speciality = :speciality")
    List<Worker> findBySpeciality(String speciality);


    @Query("select w from Worker w where w.id = :workerId")
    Worker findWorkerById(Long workerId);

    @Query("SELECT new odrzavanjemikroservis.dtos.WorkerStatisticsDto(" +
            "w.speciality, " +
            "CAST(COUNT(DISTINCT w) AS long), " +
            "CAST((SELECT COUNT(wo2) FROM WorkOrder wo2 WHERE wo2.worker.speciality = w.speciality) AS long), " +
            "CAST((SELECT COUNT(wo3) FROM WorkOrder wo3 WHERE wo3.worker.speciality = w.speciality AND wo3.status = odrzavanjemikroservis.enums.WorkOrderStatus.COMPLETED) AS int)) " +
            "FROM Worker w " +
            "GROUP BY w.speciality")
    List<WorkerStatisticsDto> getWorkerStatisticsBySpeciality();

    // U WorkOrderRepository.java


}
