package smestajmikroservis.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Facility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double extraCost;


    @ManyToMany(mappedBy = "facilities")@JsonIgnore
    private List<Unit> units = new ArrayList<>();
}
