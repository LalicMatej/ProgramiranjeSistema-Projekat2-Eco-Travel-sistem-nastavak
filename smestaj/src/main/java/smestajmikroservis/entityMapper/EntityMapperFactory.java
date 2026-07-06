package smestajmikroservis.entityMapper;

public interface EntityMapperFactory <T,D>{
    T toEntity(D d);
    D toDto(T t);
}
