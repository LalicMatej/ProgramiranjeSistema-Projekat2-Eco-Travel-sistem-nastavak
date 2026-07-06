package odrzavanjemikroservis.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import odrzavanjemikroservis.enums.WorkOrderStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Schema(description = "DTO za radni nalog - predstavlja zahtev za održavanje")
public class WorkOrderDto {

    @Schema(description = "ID radnog naloga", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "ID jedinice (opreme) je obavezan")
    @Positive(message = "ID jedinice mora biti pozitivan broj")
    @Schema(description = "ID opreme/jedinice kojoj je potrebno održavanje",
            example = "101", required = true)
    private Long unitId;


    @NotNull(message = "Status radnog naloga je obavezan")
    @Schema(description = "Trenutni status radnog naloga",
            example = "PENDING",
            allowableValues = {"PENDING", "IN_PROGRESS", "COMPLETED", "CANCELLED"})
    private WorkOrderStatus status;

    @NotNull(message = "Datum i vreme zakazanog održavanja su obavezni")
    @Future(message = "Datum održavanja mora biti u budućnosti")
    @Schema(description = "Datum i vreme kada je održavanje zakazano",
            example = "2026-04-15T10:30:00",
            required = true)
    private LocalDateTime scheduledFor;

    @Positive(message = "ID radnika mora biti pozitivan broj")
    @Schema(description = "ID radnika zaduženog za održavanje",
            example = "5")
    private Long workerId;

    @Positive(message = "ID zadatka mora biti pozitivan broj")
    @Schema(description = "ID zadatka održavanja koji treba izvršiti",
            example = "23")
    private Long maintenanceTaskId;
}