package backend_restaurant.repository;

import backend_restaurant.model.Tipo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoRepository extends JpaRepository<Tipo, Long> {
}
