package backend_restaurant.repository;

import backend_restaurant.model.Platillo;
import backend_restaurant.model.Tipo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PlatilloRepository extends JpaRepository<Platillo, Long> {
    List<Platillo> findAllByTipo_Id(long tipoId);
    // Métodos para filtrar por Tipo
    //List<Platillo> findAllByTipo(Tipo tipo);
    //List<Platillo> findAllByTipoId(Long tipoId);
}