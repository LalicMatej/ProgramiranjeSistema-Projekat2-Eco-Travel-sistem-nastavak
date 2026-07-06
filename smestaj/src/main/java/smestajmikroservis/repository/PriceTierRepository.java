package smestajmikroservis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import smestajmikroservis.entity.PriceTier;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PriceTierRepository extends JpaRepository<PriceTier, Long> {
    @Query("SELECT pt FROM PriceTier pt WHERE " +
            "pt.startDate <= :date AND pt.endDate >= :date and pt.unit.id= :unitId")
    List<PriceTier> findActiveForDate(@Param("date") LocalDate date, @Param("unitId") Long unitId);

    void deleteByUnitId(Long id);

    @Query("SELECT pt FROM PriceTier pt WHERE pt.unit.id = :unitId " +
            "AND pt.startDate <= :endDate AND pt.endDate >= :startDate")
    List<PriceTier> findOverlappingByUnitAndDates(
            @Param("unitId") Long unitId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );}
