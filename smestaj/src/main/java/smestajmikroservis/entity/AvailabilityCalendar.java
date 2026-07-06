package smestajmikroservis.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class AvailabilityCalendar { //todo ### OVO PRATI KADA JE JEDINICA ZAUZETA !!!
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;  

    @ManyToOne
    private Unit unit;

    // null za regularne rezervacije, popunjeno za blokade od odrzavanja
    private Long workOrderId;

    // null za regularne rezervacije, popunjeno za blokade iz avanture-service termina
    private Long timeSlotId;
}
