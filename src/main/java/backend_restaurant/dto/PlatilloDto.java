package backend_restaurant.dto;

import lombok.Data;

@Data
public class PlatilloDto {
    private Long id;
    private String nombre;
    private Double precio;

    // Solo llevamos el ID y nombre del tipo
    private Long tipoId;
    private String tipoNombre;
}
