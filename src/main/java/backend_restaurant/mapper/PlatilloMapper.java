package backend_restaurant.mapper;


import backend_restaurant.dto.PlatilloDto;
import backend_restaurant.model.Platillo;
import backend_restaurant.model.Tipo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlatilloMapper {

    @Autowired
    private TipoMapper tipoMapper;
//    private final TipoMapper tipoMapper;
//
//    public PlatilloMapper(TipoMapper tipoMapper) {
//        this.tipoMapper = tipoMapper;
//    }

    public PlatilloDto toDto(Platillo entity) {
        if (entity == null) return null;
        PlatilloDto dto = new PlatilloDto();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setPrecio(entity.getPrecio());
        if (entity.getTipo() != null) {
            dto.setTipoId(entity.getTipo().getId());
            dto.setTipoNombre(entity.getTipo().getNombre());
        }
        return dto;
    }

    public Platillo toEntity(PlatilloDto dto) {
        if (dto == null) return null;
        Platillo entity = new Platillo();
        entity.setId(dto.getId());
        entity.setNombre(dto.getNombre());
        entity.setPrecio(dto.getPrecio());
        if (dto.getTipoId() != null) {
            Tipo tipo = new Tipo();
            tipo.setId(dto.getTipoId());
            // No seteamos nombre; el service lo obtendrá de BD
            entity.setTipo(tipo);
        }
        return entity;
    }
}
