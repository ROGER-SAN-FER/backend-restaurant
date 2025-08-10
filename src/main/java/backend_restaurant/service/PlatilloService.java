package backend_restaurant.service;

import backend_restaurant.model.Platillo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PlatilloService {
    List<Platillo> listarTodos();
    Platillo obtenerPorId(Long id);
    Platillo crear(Platillo platillo);
    Platillo actualizar(Long id, Platillo datosActualizados);
    void eliminar(Long id);

    // Para fotos
    Platillo subirFoto(Long platilloId, MultipartFile file); // devuelve URL pública
    String obtenerFotoPublica(Long platilloId);            // URL pública
    void eliminarFoto(Long platilloId);                    // borra de Supabase y limpia campo
}
