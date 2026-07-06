package smestajmikroservis.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smestajmikroservis.dtos.PriceTierDto;
import smestajmikroservis.entity.PriceTier;
import smestajmikroservis.entity.Unit;
import smestajmikroservis.entityMapper.MapperFactory;
import smestajmikroservis.repository.PriceTierRepository;
import smestajmikroservis.repository.UnitRepository;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class PriceTierService {

    @Autowired
    PriceTierRepository priceTierRepository;
    @Autowired
    private MapperFactory mapperFactory;
    @Autowired
    private UnitRepository unitRepository;

    public PriceTierDto savePriceTier(PriceTierDto priceTierDto) {

        // imamo provere 3.
        Unit unit = unitRepository.findById(priceTierDto.getUnitId())
                .orElseThrow(() -> new EntityNotFoundException("Jedinica sa ID " + priceTierDto.getUnitId() + " nije pronađena!"));

        //Validacija datuma (ako anotacije nisu dovoljne)
        if (priceTierDto.getStartDate().isAfter(priceTierDto.getEndDate())) {
            throw new IllegalArgumentException("Datum početka mora biti pre datuma kraja!");
        }

        // da li se cenovnik preklapa sa postojećim
        List<PriceTier> overlappingTiers = priceTierRepository.findOverlappingByUnitAndDates(
                unit.getId(),
                priceTierDto.getStartDate(),
                priceTierDto.getEndDate()
        );

        if (!overlappingTiers.isEmpty()) {
            throw new IllegalStateException(
                    String.format("Cenovnik se preklapa sa postojećim! Period: %s - %s, postojeći: %s - %s",
                            priceTierDto.getStartDate(),
                            priceTierDto.getEndDate(),
                            overlappingTiers.get(0).getStartDate(),
                            overlappingTiers.get(0).getEndDate())
            );
        }

        // Da li je period validan (ne duži od 365 dana)
        long days = ChronoUnit.DAYS.between(priceTierDto.getStartDate(), priceTierDto.getEndDate());
        if (days > 365) {
            throw new IllegalArgumentException("Cenovnik ne može važiti duže od 365 dana!");
        }


        PriceTier priceTier = mapperFactory.getMapper(PriceTier.class).toEntity(priceTierDto);
        priceTier.setUnit(unit);

        PriceTier savedPriceTier = priceTierRepository.save(priceTier);

        System.out.println("Kreiran cenovnik '" + priceTierDto.getName() +
                "' za jedinicu ID " + unit.getId() +
                " | Period: " + priceTierDto.getStartDate() + " do " + priceTierDto.getEndDate() +
                " | Multiplikator: " + priceTierDto.getMultiplier());

        return (PriceTierDto) mapperFactory.getMapper(PriceTier.class).toDto(savedPriceTier);
    }

}
