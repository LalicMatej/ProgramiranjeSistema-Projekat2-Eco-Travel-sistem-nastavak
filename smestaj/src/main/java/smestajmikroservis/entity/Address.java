package smestajmikroservis.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String city;
    private String zipCode;
    private String streetAddress;

    @OneToOne(mappedBy = "address",cascade = CascadeType.ALL)  @JsonIgnore
    private Unit unit;

}
