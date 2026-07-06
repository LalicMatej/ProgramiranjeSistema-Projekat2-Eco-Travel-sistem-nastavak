package org.raflab.avantureservice.repositories;

import org.raflab.avantureservice.model.AdventureReviews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdventureReviewsRepository extends JpaRepository<AdventureReviews,Long> {
}
