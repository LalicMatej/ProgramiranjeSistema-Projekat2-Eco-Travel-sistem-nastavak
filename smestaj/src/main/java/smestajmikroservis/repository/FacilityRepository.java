package smestajmikroservis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import smestajmikroservis.entity.Facility;
import smestajmikroservis.entity.Unit;

import java.util.List;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {
    @Query("SELECT u FROM Unit u JOIN u.facilities f WHERE f.id = :facilityId")
    List<Unit> findUnitsByFacility(@Param("facilityId") Long facilityId);
}
