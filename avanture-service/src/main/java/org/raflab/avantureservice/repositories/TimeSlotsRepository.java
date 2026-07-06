package org.raflab.avantureservice.repositories;

import org.raflab.avantureservice.model.TimeSlots;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TimeSlotsRepository extends JpaRepository<TimeSlots,Long> {
    Optional<TimeSlots> findByTermMark(String termMark);
}
