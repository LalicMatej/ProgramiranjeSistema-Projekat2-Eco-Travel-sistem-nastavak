// odrzavanjemikroservis/dtos/MaintenanceStatisticsDto.java
package odrzavanjemikroservis.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaintenanceStatisticsDto {
    private Long unitId;
    private String unitName;
    private String city;
    private Integer totalWorkOrders;      // ukupno radnih naloga
    private Integer completedWorkOrders;  // završenih
    private Double avgDurationHours;      // prosečno trajanje (sati)
    private String lastMaintenanceDate;   // datum poslednjeg održavanja

}