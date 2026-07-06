package smestajmikroservis.entityMapper;

import org.springframework.stereotype.Component;
import smestajmikroservis.dtos.PriceTierDto;
import smestajmikroservis.entity.PriceTier;

@Component
public class PriceTierMapper implements EntityMapperFactory<PriceTier, PriceTierDto>{
    @Override
    public PriceTier toEntity(PriceTierDto priceTierDto) {
        PriceTier priceTier =  new PriceTier();
        priceTier.setName(priceTierDto.getName());
        priceTier.setMultiplier(priceTierDto.getMultiplier());
        priceTier.setStartDate(priceTierDto.getStartDate());
        priceTier.setEndDate(priceTierDto.getEndDate());
    //todo posle pozivanja ovoga povezati sa unitom
    return priceTier;
    }

    @Override
    public PriceTierDto toDto(PriceTier priceTier) {
        PriceTierDto priceTierDto = new PriceTierDto();
        priceTierDto.setName(priceTier.getName());
        priceTierDto.setMultiplier(priceTier.getMultiplier());
        priceTierDto.setStartDate(priceTier.getStartDate());
        priceTierDto.setEndDate(priceTier.getEndDate());
        priceTierDto.setUnitId(priceTier.getUnit().getId());
        return priceTierDto;
    }
}
