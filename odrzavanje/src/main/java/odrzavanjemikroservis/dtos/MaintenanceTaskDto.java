package odrzavanjemikroservis.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(description = "DTO za zadatak održavanja - definiše tip održavanja")
public class MaintenanceTaskDto {

    @Schema(description = "ID zadatka održavanja",
            example = "23",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "Naziv zadatka je obavezan")
    @Size(min = 3, max = 100, message = "Naziv zadatka mora imati između 3 i 100 karaktera")
    @Pattern(regexp = "^[A-Za-z0-9ŠĐŽČĆšđžčć\\s-]+$",
            message = "Naziv može sadržati slova, brojeve, razmake i crtice")
    @Schema(description = "Naziv zadatka održavanja",
            example = "Servisiranje električnih instalacija",
            required = true)
    private String taskName;

    @NotNull(message = "Procenjeno trajanje je obavezno")
    @Min(value = 1, message = "Procenjeno trajanje mora biti najmanje 1 sat")
    @Max(value = 168, message = "Procenjeno trajanje ne može biti više od 168 sati (7 dana)")
    @Schema(description = "Procenjeno trajanje zadatka u satima",
            example = "4",
            minimum = "1",
            maximum = "168",
            required = true)
    private Integer estimatedDurationHours;
}