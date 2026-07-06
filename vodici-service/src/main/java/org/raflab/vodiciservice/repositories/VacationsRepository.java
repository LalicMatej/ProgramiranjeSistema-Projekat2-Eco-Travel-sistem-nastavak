package org.raflab.vodiciservice.repositories;

import org.raflab.vodiciservice.model.Certifications;
import org.raflab.vodiciservice.model.Vacations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VacationsRepository extends JpaRepository<Vacations,Long> {
    @Query("select va from Vacations va where va.guides.first_name like :ime and va.guides.last_name like :prezime")
    List<Vacations> findVacationsByImeAndPrezime(String ime, String prezime);

}
