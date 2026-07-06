package smestajmikroservis.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.persistence.EntityNotFoundException;
import org.raflab.sharedevents.UnitDeletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smestajmikroservis.client.OdrzavanjeClient;
import smestajmikroservis.config.RabbitMQConfig;
import smestajmikroservis.dtos.CityStatisticsDto;
import smestajmikroservis.dtos.UnitDto;
import smestajmikroservis.dtos.UnitWithWorkOrdersDto;
import smestajmikroservis.dtos.WorkOrderDto;
import smestajmikroservis.entity.Address;
import smestajmikroservis.entity.Facility;
import smestajmikroservis.entity.PriceTier;
import smestajmikroservis.entity.Unit;
import smestajmikroservis.entityMapper.MapperFactory;
import smestajmikroservis.exception.ServiceUnavailableException;
import smestajmikroservis.repository.AddressRepository;
import smestajmikroservis.repository.AvailabilityCalendarRepository;
import smestajmikroservis.repository.PriceTierRepository;
import smestajmikroservis.repository.UnitRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UnitService {

    private static final Logger log = LoggerFactory.getLogger(UnitService.class);

    @Autowired
    private UnitRepository unitRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private PriceTierRepository priceTierRepository;
    @Autowired
    private AvailabilityCalendarRepository availabilityCalendarRepository;
    @Qualifier("smestajmikroservis.client.OdrzavanjeClient")
    @Autowired
    private OdrzavanjeClient odrzavanjeClient;
    @Autowired
    private MapperFactory mapperFactory;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Transactional
    public UnitDto add(UnitDto unit) {
        Address a = new Address();
        a.setCity(unit.getCity());
        a.setStreetAddress(unit.getStreetAddress());
        a.setZipCode(unit.getZip_code());
        a = addressRepository.save(a);

        Unit u = mapperFactory.getMapper(Unit.class).toEntity(unit);
        u.setAddress(a);

        if (alreadyExists(u)) {
            throw new IllegalStateException("Jedinica sa tim imenom, tipom i adresom već postoji.");
        }
        a.setUnit(u);
        u = unitRepository.save(u);
        return (UnitDto) mapperFactory.getMapper(Unit.class).toDto(u);
    }

    private boolean alreadyExists(Unit u) {
        Unit p = unitRepository.findByNameAndType(u.getName(), u.getUnit_type());
        if (p == null) {
            return false;
        }
        Address a = p.getAddress();
        return Objects.equals(a.getZipCode(), u.getAddress().getZipCode())
                && Objects.equals(a.getStreetAddress(), u.getAddress().getStreetAddress());
    }

    public List<UnitDto> findByName(String name) {
        List<Unit> units = unitRepository.findByNameContainingIgnoreCase(name);
        List<UnitDto> unitDtos = new ArrayList<>();
        for (Unit unit : units) {
            unitDtos.add((UnitDto) mapperFactory.getMapper(Unit.class).toDto(unit));
        }
        return unitDtos;
    }

    public UnitDto getUnitById(Long id) {
        Unit unit = unitRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Jedinica nije pronađena sa id: " + id));
        return (UnitDto) mapperFactory.getMapper(Unit.class).toDto(unit);
    }

    public List<UnitDto> findAll() {
        List<Unit> units = unitRepository.findAll();
        List<UnitDto> unitDtos = new ArrayList<>();
        for (Unit unit : units) {
            unitDtos.add((UnitDto) mapperFactory.getMapper(Unit.class).toDto(unit));
        }
        return unitDtos;
    }

    @Transactional
    public void delete(Long id) {
        Unit unit = unitRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Jedinica nije pronađena sa id: " + id));
        String unitName = unit.getName();

        availabilityCalendarRepository.deleteByUnitId(id);
        priceTierRepository.deleteByUnitId(id);
        unitRepository.deleteById(id);

        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.SMESTAJ_EXCHANGE, "unit.deleted", new UnitDeletedEvent(id, unitName, LocalDateTime.now()));
            log.info("[RabbitMQ] Objavljen dogadjaj unit.deleted za jedinicu id={}", id);
        } catch (AmqpException e) {
            log.warn("[RabbitMQ] Nije moguce objaviti dogadjaj unit.deleted za id={}: {}", id, e.getMessage());
        }
    }

    public Double calculatePrice(Long unitId, LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Datum početka mora biti pre datuma završetka.");
        }
        if (startDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Datum početka mora biti u budućnosti.");
        }
        Unit unit = unitRepository.findById(unitId)
                .orElseThrow(() -> new EntityNotFoundException("Jedinica nije pronađena sa id: " + unitId));

        long brDana = ChronoUnit.DAYS.between(startDate, endDate);
        double ukupno = unit.getBasePricePerNight() * brDana;
        log.debug("Osnovna cena za {} dana: {}", brDana, ukupno);

        if (unit.getFacilities() != null) {
            for (Facility f : unit.getFacilities()) {
                if (f.getExtraCost() != null && f.getExtraCost() > 0) {
                    ukupno += f.getExtraCost() * brDana;
                }
            }
        }
        log.debug("Cena sa amenitijima: {}", ukupno);

        double multiplikator = 1;
        List<PriceTier> aktivniCenovnik = priceTierRepository.findActiveForDate(startDate, unitId);
        if (aktivniCenovnik != null && !aktivniCenovnik.isEmpty()) {
            multiplikator = aktivniCenovnik.getFirst().getMultiplier();
        }
        log.debug("Konačna cena (multiplikator {}): {}", multiplikator, ukupno * multiplikator);
        return ukupno * multiplikator;
    }

    public List<UnitDto> searchByTypeAndPrice(String unitType, Double minPrice, Double maxPrice) {
        List<Unit> units = unitRepository.findUnitsByFilters(unitType.toUpperCase(), minPrice, maxPrice);
        List<UnitDto> unitDtos = new ArrayList<>();
        for (Unit u : units) {
            unitDtos.add((UnitDto) mapperFactory.getMapper(Unit.class).toDto(u));
        }
        return unitDtos;
    }

    public boolean existsById(Long id) {
        return unitRepository.existsById(id);
    }

    @Retry(name = "odrzavanjeService")
    @CircuitBreaker(name = "odrzavanjeService", fallbackMethod = "getUnitWithOrdersFallback")
    public UnitWithWorkOrdersDto getUnitWithOrders(Long unitId) {
        log.info("Dohvatam jedinicu sa radnim nalozima, unitId={}", unitId);
        UnitDto unit = getUnitById(unitId);
        List<WorkOrderDto> workOrders = odrzavanjeClient.getWorkOrdersByUnitId(unitId);
        return new UnitWithWorkOrdersDto(unit, workOrders);
    }

    @Retry(name = "odrzavanjeService")
    @CircuitBreaker(name = "odrzavanjeService", fallbackMethod = "getUnitsWithWorksInProgressFallback")
    public List<UnitDto> getUnitsWithWorksInProgress() {
        List<WorkOrderDto> workOrders = odrzavanjeClient.getInProgressWorkOrders();
        if (workOrders.isEmpty()) return List.of();
        List<UnitDto> unitDtos = new ArrayList<>();
        List<Long> unitIds = new ArrayList<>();
        for (WorkOrderDto w : workOrders) {
            if (!unitIds.contains(w.getUnitId()) && unitRepository.existsById(w.getUnitId())) {
                unitIds.add(w.getUnitId());
                unitDtos.add((UnitDto) mapperFactory.getMapper(Unit.class)
                        .toDto(unitRepository.findById(w.getUnitId()).get()));
            }
        }
        return unitDtos;
    }

    public UnitWithWorkOrdersDto getUnitWithOrdersFallback(Long unitId, Throwable t) {
        Throwable cause = (t instanceof java.lang.reflect.UndeclaredThrowableException) ? t.getCause() : t;
        if (cause instanceof EntityNotFoundException e)     throw e;
        if (cause instanceof IllegalArgumentException e)    throw e;
        if (cause instanceof IllegalStateException e)       throw e;
        if (cause instanceof ServiceUnavailableException e) throw e;
        if (cause.getClass().getSimpleName().equals("CallNotPermittedException")) {
            log.warn("[CB OPEN] Odrzavanje servis CB je otvoren za unitId={}", unitId);
            throw new ServiceUnavailableException("Odrzavanje servis nije dostupan (Circuit Breaker je otvoren).");
        }
        log.warn("[CIRCUIT BREAKER] Odrzavanje servis nije dostupan za unitId={}: {}", unitId, cause.getMessage());
        throw new ServiceUnavailableException("Odrzavanje servis trenutno nije dostupan.");
    }

    public List<UnitDto> getUnitsWithWorksInProgressFallback(Throwable t) {
        Throwable cause = (t instanceof java.lang.reflect.UndeclaredThrowableException) ? t.getCause() : t;
        if (cause instanceof EntityNotFoundException e)     throw e;
        if (cause instanceof IllegalArgumentException e)    throw e;
        if (cause instanceof IllegalStateException e)       throw e;
        if (cause instanceof ServiceUnavailableException e) throw e;
        if (cause.getClass().getSimpleName().equals("CallNotPermittedException")) {
            log.warn("[CB OPEN] Odrzavanje servis CB je otvoren");
            throw new ServiceUnavailableException("Odrzavanje servis nije dostupan (Circuit Breaker je otvoren).");
        }
        log.warn("[CIRCUIT BREAKER] Odrzavanje servis nije dostupan: {}", cause.getMessage());
        throw new ServiceUnavailableException("Odrzavanje servis trenutno nije dostupan.");
    }

    public List<CityStatisticsDto> getCityStatistics() {
        return unitRepository.getCityStatistics();
    }
}
