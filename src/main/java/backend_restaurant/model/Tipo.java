package backend_restaurant.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "tipos")
public class Tipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nombre;
}
