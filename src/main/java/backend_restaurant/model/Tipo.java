package backend_restaurant.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "tipos")
public class Tipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // La BD genera automáticamente el valor del id al insertar un nuevo Tipo
    private long id;

    private String nombre;

    @OneToMany(
            mappedBy = "tipo",            // El campo en Platillo que referencia a este Tipo
            cascade = CascadeType.ALL,    // Todas las operaciones (persist, merge, remove, etc.) se propagan a los Platillos
            orphanRemoval = true,         // Si un Platillo se elimina de esta lista, también se borra de la BD
            fetch = FetchType.LAZY        // No carga la lista de Platillos hasta que se acceda a ella
    )
    @JsonManagedReference            // Lado “padre” en la serialización JSON para romper ciclos
    private List<Platillo> platillos = new ArrayList<>(); // No crea columna extra en la tabla, solo maneja la relación
}
