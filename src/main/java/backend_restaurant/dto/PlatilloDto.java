package backend_restaurant.dto;

import lombok.Data;

import java.util.List;

@Data
public class PlatilloDto {
    private Long id;
    private String nombre;
    private List<String> insumos;
    private Double precio;

    // Solo llevamos el ID y nombre del tipo
    private Long tipoId;
    private String tipoNombre;
}
