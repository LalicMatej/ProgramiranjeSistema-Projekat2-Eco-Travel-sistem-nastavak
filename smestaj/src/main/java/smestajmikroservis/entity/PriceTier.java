package smestajmikroservis.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class PriceTier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double multiplier;
    private LocalDate startDate;
    private LocalDate endDate;

    @ManyToOne
    private Unit unit;
}
