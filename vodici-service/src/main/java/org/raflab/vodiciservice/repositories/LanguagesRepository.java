package org.raflab.vodiciservice.repositories;

import org.raflab.vodiciservice.model.Languages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguagesRepository extends JpaRepository<Languages,Long> {
}
