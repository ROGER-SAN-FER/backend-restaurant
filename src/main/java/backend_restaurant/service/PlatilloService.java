package backend_restaurant.service;

import backend_restaurant.model.Platillo;
import java.util.List;

public interface PlatilloService {
    List<Platillo> listarTodos();
    Platillo obtenerPorId(Long id);
    Platillo crear(Platillo platillo);
    Platillo actualizar(Long id, Platillo datosActualizados);
    void eliminar(Long id);
    List<Platillo> listarPorTipo(Long tipoId);
}
