package backend_restaurant.service;

import backend_restaurant.model.Tipo;
import java.util.List;

public interface TipoService {
    List<Tipo> listarTodos();
    Tipo obtenerPorId(Long id);
    Tipo crear(Tipo tipo);
    Tipo actualizar(Long id, Tipo datosActualizados);
    void eliminar(Long id);
}
