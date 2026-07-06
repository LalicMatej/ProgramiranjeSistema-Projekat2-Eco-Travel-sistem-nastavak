package smestajmikroservis.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceTierDto {

    @NotBlank(message = "Naziv cenovnika je obavezan!")
    @Size(min = 3, max = 50, message = "Naziv mora imati između 3 i 50 karaktera!")
    private String name;

    @NotNull(message = "Multiplikator je obavezan!")
    @DecimalMin(value = "0.5", message = "Multiplikator ne može biti manji od 0.5!")
    @DecimalMax(value = "10.0", message = "Multiplikator ne može biti veći od 10.0!")
    private Double multiplier;

    @NotNull(message = "Datum početka je obavezan!")
    @FutureOrPresent(message = "Datum početka ne može biti u prošlosti!")
    private LocalDate startDate;

    @NotNull(message = "Datum kraja je obavezan!")
    @Future(message = "Datum kraja mora biti u budućnosti!")
    private LocalDate endDate;

    @NotNull(message = "ID jedinice je obavezan!")
    @Positive(message = "ID jedinice mora biti pozitivan broj!")
    private Long unitId;
}