package org.raflab.avantureservice.repositories;

import org.raflab.avantureservice.model.GearRequirements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GearRequirementsRepository extends JpaRepository<GearRequirements,Long> {
}
