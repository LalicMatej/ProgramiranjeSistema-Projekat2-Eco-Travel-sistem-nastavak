// odrzavanjemikroservis/dtos/MaintenanceStatisticsDto.java
package smestajmikroservis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaintenanceStatisticsDto {
    private Long unitId;
    private Integer totalWorkOrders;      // ukupno radnih naloga
    private Integer completedWorkOrders;  // završenih
    private Double avgDurationHours;      // prosečno trajanje (sati)
    private String lastMaintenanceDate;   // datum poslednjeg održavanja
    private String mostCommonIssue;       // najčešći kvar

    // getteri, setteri, konstruktori...
}