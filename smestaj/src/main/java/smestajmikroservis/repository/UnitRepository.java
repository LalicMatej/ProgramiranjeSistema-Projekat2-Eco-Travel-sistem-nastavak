package smestajmikroservis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import smestajmikroservis.dtos.CityStatisticsDto;
import smestajmikroservis.entity.Unit;

import java.math.BigDecimal;
import java.util.List;


@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {

    //todo DODATI ENDPOINT ZA OVO !!!
    @Query("SELECT u FROM Unit u WHERE " +
            "(:type IS NULL OR u.unit_type = :type) AND " +
            "(:minPrice IS NULL OR u.basePricePerNight >= :minPrice) AND " +
            "(:maxPrice IS NULL OR u.basePricePerNight <= :maxPrice)")
    List<Unit> findUnitsByFilters(@Param("type") String type,
                                  @Param("minPrice") Double minPrice,
                                  @Param("maxPrice") Double maxPrice);



    List<Unit> findByNameContainingIgnoreCase(String naziv);

    @Query("select u from Unit u where u.unit_type= :type")
    List<Unit> findByUnit_type(String type);


    void deleteById(Long id);

    @Query("select u from Unit u where u.unit_type= :unitType and u.name= :unitName")
    Unit findByNameAndType(String unitName, String unitType);
    // List<Unit> findByBasePricePerNightBetween(BigDecimal min, BigDecimal max);

    @Query("SELECT new smestajmikroservis.dtos.CityStatisticsDto(u.address.city, COUNT(u), AVG(u.basePricePerNight), MIN(u.basePricePerNight), MAX(u.basePricePerNight)) FROM Unit u GROUP BY u.address.city")
    List<CityStatisticsDto> getCityStatistics();
}
