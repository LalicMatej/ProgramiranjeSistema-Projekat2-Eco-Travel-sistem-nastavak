package smestajmikroservis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import smestajmikroservis.entity.AvailabilityCalendar;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AvailabilityCalendarRepository extends JpaRepository<AvailabilityCalendar, Long> {

    List<AvailabilityCalendar> findByUnitName(String name);

    @Query("select c from AvailabilityCalendar c where c.id= :id")
    AvailabilityCalendar findByUnitId(Long id);


    void deleteById(Long id);

    //void update(AvailabilityCalendar availabilityCalendar);

    @Query("select a from AvailabilityCalendar a where a.unit.id = :unitId")
    List<AvailabilityCalendar> findByUnit(Long unitId);

    @Query("select a from AvailabilityCalendar a where :date between a.startDate and a.endDate")
    List<AvailabilityCalendar> findByDate(LocalDate date);

    void deleteByUnitId(Long unitId);

    List<AvailabilityCalendar> findByWorkOrderId(Long workOrderId);

    Optional<AvailabilityCalendar> findByTimeSlotId(Long timeSlotId);

    // Opcija 1: Osnovni query
    @Query("SELECT a FROM AvailabilityCalendar a WHERE " +
            "a.unit.name = :unitName AND " +
            "a.unit.address.streetAddress = :unitAddress AND " +
            "a.startDate < :endDate AND a.endDate > :startDate")
    List<AvailabilityCalendar> findOccupiedByDateAndUnitNameAndAddress(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("unitName") String unitName,
            @Param("unitAddress") String unitAddress
    );

    @Query("SELECT a FROM AvailabilityCalendar a WHERE " +
            "a.unit.id = :unitId AND " +
            "a.id <> :calendarId AND " +
            "a.startDate < :endDate AND a.endDate > :startDate")
    List<AvailabilityCalendar> findOverlappingForUnitExcludingCalendar(
            @Param("unitId") Long unitId,
            @Param("calendarId") Long calendarId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
