package odrzavanjemikroservis.entityMapper;

public interface EntityMapperFactory <T,D>{
    T toEntity(D dto);
    D toDto(T entity);
}
