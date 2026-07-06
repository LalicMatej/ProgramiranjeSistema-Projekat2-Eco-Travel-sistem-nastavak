package odrzavanjemikroservis.client;

import odrzavanjemikroservis.dtos.AvailabilityCalendarDto;
import odrzavanjemikroservis.dtos.UnitDto;
import odrzavanjemikroservis.exception.ServiceUnavailableException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SmestajClientFallback implements SmestajClient {

    @Override
    public List<UnitDto> getAllUnits() {
        throw new ServiceUnavailableException("Smestaj servis trenutno nije dostupan.");
    }

    @Override
    public UnitDto getUnitById(Long id) {
        throw new ServiceUnavailableException("Smestaj servis trenutno nije dostupan.");
    }

    @Override
    public List<UnitDto> searchUnitsByName(String name) {
        throw new ServiceUnavailableException("Smestaj servis trenutno nije dostupan.");
    }

    @Override
    public List<UnitDto> findByCity(String city) {
        throw new ServiceUnavailableException("Smestaj servis trenutno nije dostupan.");
    }

    @Override
    public List<UnitDto> findByParameters(String type, Double minPrice, Double maxPrice) {
        throw new ServiceUnavailableException("Smestaj servis trenutno nije dostupan.");
    }

    @Override
    public Double calculatePrice(Long unitId, String startDate, String endDate) {
        throw new ServiceUnavailableException("Smestaj servis trenutno nije dostupan.");
    }

    @Override
    public AvailabilityCalendarDto addReservation(AvailabilityCalendarDto reservation) {
        throw new ServiceUnavailableException("Smestaj servis trenutno nije dostupan.");
    }

    @Override
    public Boolean unitExists(Long id) {
        throw new ServiceUnavailableException("Smestaj servis trenutno nije dostupan.");
    }
}
