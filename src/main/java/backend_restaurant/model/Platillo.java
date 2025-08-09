package backend_restaurant.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Type;

import java.util.List;

@Entity
@Data
@Table(name = "platillos")
public class Platillo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // La base de datos asigna automáticamente el valor de id al insertar un nuevo Platillo.
    private long id;

    private String nombre;
    private Double precio;
    private List<String> insumos;

    @ManyToOne(
            fetch = FetchType.LAZY, // No carga el Tipo hasta que se llame a getTipo()
            optional = false        // Obliga a que todo Platillo tenga siempre un Tipo asociado
    )
    @JoinColumn(
            name = "tipo_id",      // Nombre de la columna FK en la tabla platillos
            nullable = false       // Impide valores NULL en esta columna
    )
    @JsonBackReference        // Lado “hijo” en la serialización JSON; evita ciclos con Tipo
    private Tipo tipo;        // Tipo al que pertenece este Platillo (entrante, principal, postre…)
}
