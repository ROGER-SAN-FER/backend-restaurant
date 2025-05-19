package backend_restaurant.mapper;

import backend_restaurant.dto.TipoDto;
import backend_restaurant.model.Tipo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TipoMapper {

    TipoDto toDto(Tipo tipo);

    @Mapping(target = "platillos", ignore = true)
    Tipo toTipo(TipoDto tipoDto);
}
