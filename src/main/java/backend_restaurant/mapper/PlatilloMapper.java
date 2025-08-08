package backend_restaurant.mapper;

import backend_restaurant.dto.PlatilloRequestDto;
import backend_restaurant.dto.PlatilloResponseDto;
import backend_restaurant.model.Platillo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlatilloMapper {

    @Mapping(source = "tipo.id", target = "tipoId")
    @Mapping(source = "tipo.nombre", target = "tipoNombre")
    PlatilloResponseDto toDto(Platillo platillo);

    @Mapping(source = "tipoId", target = "tipo.id")
    Platillo toEntity(PlatilloRequestDto platilloRequestDto);
}
