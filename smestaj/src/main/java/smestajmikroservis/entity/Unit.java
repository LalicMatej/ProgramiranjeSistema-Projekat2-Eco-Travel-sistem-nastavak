package smestajmikroservis.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import smestajmikroservis.UnitApplication;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Unit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String unit_type;
    private Double basePricePerNight;


    @ManyToMany //ovde se ispisuju fasilitiji
    private List<Facility> facilities = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

}
