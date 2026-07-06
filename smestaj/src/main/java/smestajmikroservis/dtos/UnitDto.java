package smestajmikroservis.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.*;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Schema(description = "DTO za jedinicu smeštaja")
public class UnitDto {

    @Schema(description = "Id smestajne jedinice", example = "1")
    private Long id;
    
    @NotBlank(message = "Naziv jedinice je obavezan")
    @Size(min = 2, max = 100, message = "Naziv mora imati između 2 i 100 karaktera")
    @Schema(description = "Naziv smeštajne jedinice",
            example = "Apartman Centar",
            required = true)
    private String name;

    @NotBlank(message = "Tip jedinice je obavezan")
//    @Pattern(regexp = "^(APARTMAN|SOBA|STUDIO|KUCA)$",
//            message = "Tip mora biti: APARTMAN, SOBA, STUDIO ili KUCA")
    @Schema(description = "Tip smeštajne jedinice",
            example = "APARTMAN",
//            allowableValues = {"APARTMAN", "SOBA", "STUDIO", "KUCA"},
            required = true)
    private String unit_type;

    @NotNull(message = "Cena po noćenju je obavezna")
    @DecimalMin(value = "0.01", message = "Cena mora biti veća od 0")
    @DecimalMax(value = "10000", message = "Cena ne može biti veća od 10000")
    @Schema(description = "Osnovna cena po noćenju",
            example = "89.99",
            minimum = "0.01",
            maximum = "10000",
            required = true)
    private Double basePricePerNight;

    @NotBlank(message = "Ulica je obavezna")
    @Size(min = 3, max = 100, message = "Ulica mora imati između 3 i 100 karaktera")
    @Schema(description = "Ulica i broj",
            example = "Knez Mihailova 25",
            required = true)
    private String streetAddress;

    @NotBlank(message = "Grad je obavezan")
    @Size(min = 2, max = 50, message = "Grad mora imati između 2 i 50 karaktera")
    @Pattern(regexp = "^[A-Za-zŠĐŽČĆšđžčć\\s-]+$",
            message = "Grad može sadržati samo slova, razmake i crtice")
    @Schema(description = "Grad",
            example = "Beograd",
            required = true)
    private String city;

    @NotBlank(message = "Poštanski broj je obavezan")
    @Pattern(regexp = "^[0-9]{5}$", message = "Poštanski broj mora imati tačno 5 cifara")
    @Schema(description = "Poštanski broj",
            example = "11000",
            pattern = "^[0-9]{5}$",
            required = true)
    private String zip_code;
}