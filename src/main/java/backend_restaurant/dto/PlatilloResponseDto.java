package backend_restaurant.dto;

import lombok.Data;
import java.util.List;

@Data
public class PlatilloResponseDto {
    private Long id;
    private String nombre;
    private Double precio;
    private List<String> insumos;

    // Solo muestro el ID y nombre del tipo del objeto "tipo"
    private Long tipoId;
    private String tipoNombre;
}
