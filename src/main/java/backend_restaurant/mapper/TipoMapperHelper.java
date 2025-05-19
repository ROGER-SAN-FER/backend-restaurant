package backend_restaurant.mapper;

import backend_restaurant.model.Tipo;

public class TipoMapperHelper {
    public Tipo fromId(Long id) {
        if (id == null) return null;
        Tipo tipo = new Tipo();
        tipo.setId(id);
        return tipo;
    }
}