//    package backend_restaurant.mapper;
//
//
//    import backend_restaurant.dto.PlatilloDto;
//    import backend_restaurant.model.Platillo;
//    import backend_restaurant.model.Tipo;
//    import org.mapstruct.Mapper;
//    import org.mapstruct.Mapping;
//
//    @Mapper(componentModel = "spring", uses = {TipoMapperHelper.class/*, FotoMapperHelper.class*/})
//    public interface PlatilloMapper {
//
//        @Mapping(source = "tipo.id", target = "tipoId")
//        @Mapping(source = "tipo.nombre", target = "tipoNombre")
////        @Mapping(source = "foto",      target = "foto")       // mapea byte[] → String (Base64)
//        PlatilloDto toDTO(Platillo platillo);
//
//        @Mapping(source = "tipoId", target = "tipo")
//        @Mapping(target = "tipoNombre", ignore = true)
////        @Mapping(source = "foto",    target = "foto")        // mapea String (Base64) → byte[]
//        Platillo toEntity(PlatilloDto platilloDto);
//
//        // Metodo auxiliar para convertir el tipoId a un objeto Tipo
//    //    default Tipo map(Long tipoId) {
//    //        if (tipoId == null) return null;
//    //        Tipo tipo = new Tipo();
//    //        tipo.setId(tipoId);
//    //        return tipo;
//    //    }
//    }
