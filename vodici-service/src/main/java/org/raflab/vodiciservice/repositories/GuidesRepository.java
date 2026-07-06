package org.raflab.vodiciservice.repositories;

import org.raflab.vodiciservice.model.Guides;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuidesRepository extends JpaRepository<Guides,Long> {
    @Query("select rating from Guides")
    List<Double> getSumRating();
    @Query("select gr from Guides gr where gr.first_name like :firstName and gr.last_name like :lastName")
    Guides getByFirstNameAndLastName(String firstName,String lastName);
}
