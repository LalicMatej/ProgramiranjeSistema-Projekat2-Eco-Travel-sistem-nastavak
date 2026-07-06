package org.raflab.authservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "roles")
public class Role {

    @Id
    private Long id;

    @Column(unique = true, nullable = false)
    private String role;

}
