package backend_restaurant.security.repository;

import backend_restaurant.security.model.RoleEntity;
import backend_restaurant.security.model.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByRoleEnum(RoleEnum roleEnum);
}
