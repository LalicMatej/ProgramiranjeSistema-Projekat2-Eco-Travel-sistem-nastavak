package smestajmikroservis.entityMapper;

import smestajmikroservis.dtos.UnitDto;
import smestajmikroservis.entity.Unit;

public class EntityMapper {

//    public static UnitDto fromUnitToDto(Unit unit) {
//        if(unit == null) return null;
//
//        String adr="nema adresu";
//        String city="Nema grada";
//        String zip="Nema zipa";
//        if(unit.getAddress()!=null)
//        {
//            adr = unit.getAddress().getStreetAddress();
//            city = unit.getAddress().getCity();
//            zip = unit.getAddress().getZipCode();
//        }
//       return new UnitDto(unit.getId(),unit.getName(),unit.getUnit_type() ,unit.getBasePricePerNight(), adr, city,zip);
//    }
//
//    public static Unit fromDtoToUnit(UnitDto unitDto) {
//        if(unitDto == null) return null;
//        Unit unit = new Unit();
//        unit.setName(unitDto.getName());
//        unit.setUnit_type(unitDto.getUnit_type().toUpperCase());
//        unit.setBasePricePerNight(unitDto.getBasePricePerNight());
//        return unit;
//    }
}
