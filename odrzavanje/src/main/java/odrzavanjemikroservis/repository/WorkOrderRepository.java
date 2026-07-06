package odrzavanjemikroservis.repository;

import odrzavanjemikroservis.entity.WorkOrder;
import odrzavanjemikroservis.enums.WorkOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {


    List<WorkOrder> findByWorkerIdAndStatus(Long workerId, WorkOrderStatus status);

    @Query("select w from WorkOrder w where w.unitId= :unitId")
    List<WorkOrder> findByUnitId(Long unitId);

    @Query("select w from WorkOrder w where w.status = :status")
    List<WorkOrder> getAllWithStatus(WorkOrderStatus status);

    @Query("SELECT wo FROM WorkOrder wo WHERE wo.worker.id = :workerId AND CAST(wo.scheduledFor AS date) = CAST(:scheduledFor AS date)")
    List<WorkOrder> findByWorkerIdAndScheduledFor(@Param("workerId") Long workerId, @Param("scheduledFor") LocalDateTime scheduledFor);
}
