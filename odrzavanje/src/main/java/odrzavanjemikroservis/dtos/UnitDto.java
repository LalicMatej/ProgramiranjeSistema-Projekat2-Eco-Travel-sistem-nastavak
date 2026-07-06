package odrzavanjemikroservis.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "DTO za jedinicu smeštaja")
@JsonIgnoreProperties(ignoreUnknown = true)  // ← DODAJ OVO!

public class UnitDto {


    private Long id;

    private String name;

    private String unit_type;

    private Double basePricePerNight;

    private String streetAddress;

    private String city;

    private String zip_code;
}