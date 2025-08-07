package backend_restaurant.mapper;

import backend_restaurant.dto.TipoRequestDto;
import backend_restaurant.dto.TipoResponseDto;
import backend_restaurant.model.Tipo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TipoMapper {

    TipoResponseDto toDto(Tipo tipo);

    @Mapping(target = "platillos", ignore = true)
    Tipo toEntity(TipoRequestDto tipoRequestDto);
}
