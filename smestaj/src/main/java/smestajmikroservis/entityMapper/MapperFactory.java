package smestajmikroservis.entityMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import smestajmikroservis.dtos.FacilityDto;
import smestajmikroservis.dtos.PriceTierDto;
import smestajmikroservis.dtos.UnitDto;
import smestajmikroservis.entity.Facility;
import smestajmikroservis.entity.PriceTier;
import smestajmikroservis.entity.Unit;

import java.util.HashMap;
import java.util.Map;

@Component
public class MapperFactory {
    private final Map<Class<?>, EntityMapperFactory<?, ?>> mappers = new HashMap<>();

    @Autowired
    public MapperFactory(UnitMapper unitMapper, PriceTierMapper priceTierMapper, FacilityMapper facilityMapper) {

        mappers.put(Unit.class, unitMapper);
        mappers.put(UnitDto.class,unitMapper);

        mappers.put(PriceTier.class,priceTierMapper);
        mappers.put(PriceTierDto.class,priceTierMapper);

        mappers.put(Facility.class,facilityMapper);
        mappers.put(FacilityDto.class,facilityMapper);


    }
    @SuppressWarnings("unchecked")
    public <T, D> EntityMapperFactory<T, D> getMapper(Class<T> entityClass) {
        return (EntityMapperFactory<T, D>) mappers.get(entityClass);
    }
}
