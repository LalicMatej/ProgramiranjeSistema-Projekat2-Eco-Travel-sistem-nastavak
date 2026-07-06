package smestajmikroservis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacilityDto {
    private Long id;
    private String name;
    private Double extraCost;
}
