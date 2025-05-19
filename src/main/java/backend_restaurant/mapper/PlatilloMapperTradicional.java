package backend_restaurant.mapper;


import backend_restaurant.dto.PlatilloDto;
import backend_restaurant.model.Platillo;
import backend_restaurant.model.Tipo;
import org.springframework.stereotype.Component;

@Component
public class PlatilloMapperTradicional {
//
//    @Autowired
//    private TipoMapper tipoMapper;
    private final TipoMapperTradicional tipoMapper;

    public PlatilloMapperTradicional(TipoMapperTradicional tipoMapper) {
        this.tipoMapper = tipoMapper;
    }

    public PlatilloDto toDto(Platillo entity) {
        if (entity == null) return null;
        PlatilloDto dto = new PlatilloDto();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setPrecio(entity.getPrecio());
        dto.setInsumos(entity.getInsumos());
        if (entity.getTipo() != null) {
            dto.setTipoId(entity.getTipo().getId());
            dto.setTipoNombre(entity.getTipo().getNombre());
        }
        return dto;
    }

    public Platillo toEntity(PlatilloDto dto) {
        if (dto == null) return null;
        Platillo entity = new Platillo();
        // Sólo setea el ID si el DTO lo trae (en actualizaciones)
        if (dto.getId() != null) {
            entity.setId(dto.getId());
        }
        entity.setNombre(dto.getNombre());
        entity.setPrecio(dto.getPrecio());
        entity.setInsumos(dto.getInsumos());
        if (dto.getTipoId() != null) {
            Tipo tipo = new Tipo();
            tipo.setId(dto.getTipoId());
            entity.setTipo(tipo);
        }
        return entity;
    }
}
