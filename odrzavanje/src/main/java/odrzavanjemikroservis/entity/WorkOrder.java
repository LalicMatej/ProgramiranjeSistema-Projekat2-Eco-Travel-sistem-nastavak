package odrzavanjemikroservis.entity;

import jakarta.persistence.*;
import lombok.Data;
import odrzavanjemikroservis.enums.WorkOrderStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data

@Entity
public class WorkOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long unitId;
    private WorkOrderStatus status;
    private LocalDateTime scheduledFor;

    @ManyToOne
    private Worker worker;

    @ManyToOne
    private MaintenanceTask maintenanceTask;
}
