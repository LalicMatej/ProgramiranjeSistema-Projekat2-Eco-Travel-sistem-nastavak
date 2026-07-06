package smestajmikroservis.entityMapper;

import org.springframework.stereotype.Component;
import smestajmikroservis.dtos.UnitDto;
import smestajmikroservis.entity.Unit;

@Component
public class UnitMapper implements EntityMapperFactory<Unit, UnitDto> {
    @Override
    public Unit toEntity(UnitDto unitDto) {
        if(unitDto == null) return null;
        Unit unit = new Unit();
        unit.setName(unitDto.getName());
        unit.setUnit_type(unitDto.getUnit_type().toUpperCase());
        unit.setBasePricePerNight(unitDto.getBasePricePerNight());
        return unit;    }

    @Override
    public UnitDto toDto(Unit unit) {
        if(unit == null) return null;

        String adr="nema adresu";
        String city="Nema grada";
        String zip="Nema zipa";
        if(unit.getAddress()!=null)
        {
            adr = unit.getAddress().getStreetAddress();
            city = unit.getAddress().getCity();
            zip = unit.getAddress().getZipCode();
        }
        return new UnitDto(unit.getId(),unit.getName(),unit.getUnit_type() ,unit.getBasePricePerNight(), adr, city,zip);
    }
}
