package smestajmikroservis.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;


import java.time.LocalDate;
import java.time.LocalDateTime;

@Data

public class WorkOrderDto {

    private Long id;
    private Long unitId;
    private String status;
    private LocalDateTime scheduledFor;
    private Long workerId;
    private Long maintenanceTaskId;
}