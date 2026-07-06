package odrzavanjemikroservis.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data

@Entity
public class MaintenanceLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long unit_id;
    private String taskDescription;
    private String workerName;
    private LocalDateTime completedAt;
    private String notes;

    @OneToOne
    private WorkOrder workOrder; //dodao sam ovo kako bih povezao log sa orderom da bih mogao da vidim vise podataka
}
