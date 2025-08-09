package backend_restaurant.controller;

import backend_restaurant.model.Platillo;
import backend_restaurant.model.Tipo;
import backend_restaurant.repository.PlatilloRepository;
import backend_restaurant.repository.TipoRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class PlatilloPersistenceIT { //(JPA puro)

    @Autowired PlatilloRepository platilloRepo;
    @Autowired TipoRepository tipoRepo;
    @Autowired EntityManager em;

    @Test
    void persiste_y_relee_insumos() {
        // Crear tipo (FK NOT NULL)
        Tipo t = new Tipo();
        t.setNombre("Postres");
        t = tipoRepo.save(t);

        // Guardar platillo con insumos
        Platillo p = new Platillo();
        p.setNombre("Tarta");
        p.setPrecio(4.5);
        p.setInsumos(List.of("Queso", "Huevo", "Azúcar"));
        p.setTipo(t);

        Platillo saved = platilloRepo.save(p);

        // Forzar escritura y limpiar caché
        em.flush();
        em.clear();

        // Leer desde BD
        Platillo found = platilloRepo.findById(saved.getId()).orElseThrow();
        assertThat(found.getNombre()).isEqualTo("Tarta");
        assertThat(found.getTipo().getId()).isEqualTo(t.getId());
        assertThat(found.getInsumos()).containsExactlyInAnyOrder("Queso","Huevo","Azúcar");

        // Actualizar insumos
        found.setInsumos(List.of("Queso","Huevo"));
        platilloRepo.save(found);
        em.flush();
        em.clear();

        Platillo reread = platilloRepo.findById(saved.getId()).orElseThrow();
        assertThat(reread.getInsumos()).containsExactlyInAnyOrder("Queso","Huevo");
    }
}
