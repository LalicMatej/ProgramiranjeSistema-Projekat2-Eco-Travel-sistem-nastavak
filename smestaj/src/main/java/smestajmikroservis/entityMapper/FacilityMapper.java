package smestajmikroservis.entityMapper;

import org.springframework.stereotype.Component;
import smestajmikroservis.dtos.FacilityDto;
import smestajmikroservis.entity.Facility;

@Component
public class FacilityMapper implements EntityMapperFactory<Facility, FacilityDto> {
    @Override
    public Facility toEntity(FacilityDto facilityDto) {
        Facility facility = new Facility();
        facility.setName(facilityDto.getName());
        facility.setExtraCost(facilityDto.getExtraCost());
        return facility;
    }

    @Override
    public FacilityDto toDto(Facility facility) {
        FacilityDto dto=new FacilityDto();
        dto.setName(facility.getName());
        dto.setExtraCost(facility.getExtraCost());
        return dto;
    }
}
