package odrzavanjemikroservis.repository;

import odrzavanjemikroservis.entity.MaintenanceTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MaintenanceTaskRepository extends JpaRepository<MaintenanceTask, Long> {


    @Query("select t from MaintenanceTask t where t.id = :id")
    MaintenanceTask findTaskById(Long id);
}
