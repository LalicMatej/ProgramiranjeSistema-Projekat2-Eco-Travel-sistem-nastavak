package org.raflab.vodiciservice.repositories;

import org.raflab.vodiciservice.model.Certifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertificationsRepository extends JpaRepository<Certifications,Long> {
    @Query("select ce from Certifications ce where ce.guides.first_name like :ime and ce.guides.last_name like :prezime")
    List<Certifications> findCertificationsByImeAndPrezime(String ime, String prezime);
}
