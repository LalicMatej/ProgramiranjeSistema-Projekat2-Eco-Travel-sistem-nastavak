package org.raflab.avantureservice.repositories;

import org.raflab.avantureservice.model.AdventureCategories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdventureCategoriesRepository extends JpaRepository<AdventureCategories, Long> {
    Optional<AdventureCategories> findByName(String name);
}
