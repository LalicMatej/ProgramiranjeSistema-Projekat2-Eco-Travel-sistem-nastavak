package smestajmikroservis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class CityStatisticsDto {
    private String city;
    private Long unitCount;
    private Double avgPrice;
    private Double minPrice;
    private Double maxPrice;
}
