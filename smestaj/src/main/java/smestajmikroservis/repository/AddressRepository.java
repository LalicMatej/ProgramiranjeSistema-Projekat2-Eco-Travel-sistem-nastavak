package smestajmikroservis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import smestajmikroservis.entity.Address;
import smestajmikroservis.entity.Unit;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    @Query("select a from Address a where a.city = :city")
    List<Address> findByCity(String city);

    List<Address> findByZipCode(String zipCode);

    @Query("SELECT u FROM Unit u WHERE u.address.city = :city")
    List<Unit> findUnitsByCity(@Param("city") String city);

    @Query("select a from Address a where a.unit.id= :unitId")
    Address findByUnitId(Long unitId);

    Address findByStreetAddressAndCity(String streetAddr, String city);
}
