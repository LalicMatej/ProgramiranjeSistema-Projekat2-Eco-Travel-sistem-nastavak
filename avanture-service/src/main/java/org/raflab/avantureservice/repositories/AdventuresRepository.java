package org.raflab.avantureservice.repositories;

import org.raflab.avantureservice.model.Adventures;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdventuresRepository extends JpaRepository<Adventures,Long> {
    @Query("select base_price from Adventures")
    List<Double> getTotalPrice();

    Optional<Adventures> findByTitle(String title);
}
