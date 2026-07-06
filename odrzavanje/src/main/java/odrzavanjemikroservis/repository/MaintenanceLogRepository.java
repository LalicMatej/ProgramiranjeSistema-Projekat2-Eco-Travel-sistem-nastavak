package odrzavanjemikroservis.repository;

import odrzavanjemikroservis.entity.MaintenanceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaintenanceLogRepository extends JpaRepository<MaintenanceLog, Long> {


    @Query("select l from MaintenanceLog l where l.unit_id = :unitId")
    List<MaintenanceLog> findByUnitId(Long unitId);
}
