package backend_restaurant.mapper;


import backend_restaurant.dto.TipoDto;
import backend_restaurant.model.Tipo;
import org.springframework.stereotype.Component;

@Component
public class TipoMapper {

    public TipoDto toDto(Tipo entity) {
        if (entity == null) return null;
        TipoDto dto = new TipoDto();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        return dto;
    }

    public Tipo toEntity(TipoDto dto) {
        if (dto == null) return null;
        Tipo entity = new Tipo();
        entity.setId(dto.getId());
        entity.setNombre(dto.getNombre());
        return entity;
    }
}
