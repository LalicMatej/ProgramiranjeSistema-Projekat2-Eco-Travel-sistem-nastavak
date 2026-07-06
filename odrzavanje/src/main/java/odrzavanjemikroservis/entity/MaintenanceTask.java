package odrzavanjemikroservis.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
@Data

@Entity
public class MaintenanceTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String taskName;
    private Integer estimatedDurationHours;


    @OneToMany(mappedBy = "maintenanceTask") @JsonIgnore
    private List<WorkOrder> workOrders;
}
