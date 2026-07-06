package smestajmikroservis.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smestajmikroservis.dtos.UnitDto;
import smestajmikroservis.entity.Unit;
import smestajmikroservis.entityMapper.MapperFactory;
import smestajmikroservis.entityMapper.UnitMapper;
import smestajmikroservis.repository.AddressRepository;
import smestajmikroservis.entityMapper.EntityMapper;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private MapperFactory mapperFactory;

    public List<UnitDto> findByCity(String city) {

       List<Unit> units = addressRepository.findUnitsByCity(city);
       List<UnitDto> unitDtos = new ArrayList<>();
       for (Unit unit : units) {

           unitDtos.add( (UnitDto) mapperFactory.getMapper(Unit.class).toDto(unit) );//EntityMapper.fromUnitToDto(unit));
       }
       return unitDtos;
   }
}
