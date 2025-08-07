package backend_restaurant.mapper;

import backend_restaurant.dto.PlatilloDto;
import backend_restaurant.model.Platillo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlatilloMapper {

    @Mapping(source = "tipo.id", target = "tipoId")
    @Mapping(source = "tipo.nombre", target = "tipoNombre")
    PlatilloDto toDto(Platillo platillo);

    @Mapping(source = "tipoId", target = "tipo.id")
    @Mapping(source = "tipoNombre", target = "tipo.nombre")
    Platillo toEntity(PlatilloDto platilloDto);
}
