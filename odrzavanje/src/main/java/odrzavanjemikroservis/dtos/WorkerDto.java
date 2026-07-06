package odrzavanjemikroservis.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(description = "DTO za radnika - osoba koja izvodi radne naloge")
public class WorkerDto {

    @Schema(description = "ID radnika", example = "5", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "Ime radnika je obavezno")
    @Size(min = 2, max = 50, message = "Ime mora imati između 2 i 50 karaktera")
    @Pattern(regexp = "^[A-Za-zŠĐŽČĆšđžčć\\s-]+$",
            message = "Ime može sadržati samo slova, razmake i crtice")
    @Schema(description = "Ime radnika",
            example = "Marko",
            required = true)
    private String firstName;

    @NotBlank(message = "Prezime radnika je obavezno")
    @Size(min = 2, max = 50, message = "Prezime mora imati između 2 i 50 karaktera")
    @Pattern(regexp = "^[A-Za-zŠĐŽČĆšđžčć\\s-]+$",
            message = "Prezime može sadržati samo slova, razmake i crtice")
    @Schema(description = "Prezime radnika",
            example = "Marković",
            required = true)
    private String lastName;

    @NotBlank(message = "Specijalnost radnika je obavezna")
    @Size(min = 3, max = 100, message = "Specijalnost mora imati između 3 i 100 karaktera")
    @Schema(description = "Specijalnost radnika (oblast stručnosti)",
            example = "Elektrotehničar",
            required = true)
    private String speciality;
}