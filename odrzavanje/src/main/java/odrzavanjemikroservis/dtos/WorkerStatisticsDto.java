package odrzavanjemikroservis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkerStatisticsDto {
    private String speciality;
    private Long workerCount;
    private Long totalWorkOrders;
    private int completedWorkOrders;
}
