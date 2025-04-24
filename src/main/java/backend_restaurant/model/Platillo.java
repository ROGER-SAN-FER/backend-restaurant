package backend_restaurant.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "platillos")
public class Platillo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nombre;
    private Double precio;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tipo_id", nullable = false)
    private Tipo tipo;
}
