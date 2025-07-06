package backend_restaurant.mapper;


import backend_restaurant.dto.PlatilloDto;
import backend_restaurant.model.Platillo;
import backend_restaurant.model.Tipo;
import org.springframework.stereotype.Component;

import java.util.Base64;

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
//
//        // Conversión de byte[] a Base64
//        if (entity.getFoto() != null) {
//            dto.setFoto(Base64.getEncoder().encodeToString(entity.getFoto()));
//        }

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
//
//        // Conversión de Base64 a byte[]
//        if (dto.getFoto() != null && !dto.getFoto().isEmpty()) {
//            entity.setFoto(Base64.getDecoder().decode(dto.getFoto()));
//        }

        if (dto.getTipoId() != null) {
            Tipo tipo = new Tipo();
            tipo.setId(dto.getTipoId());
            entity.setTipo(tipo);
        }
        return entity;
    }
}
