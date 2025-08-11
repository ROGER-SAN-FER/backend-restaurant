package backend_restaurant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Respuesta con los datos de un platillo")
public class PlatilloResponseDto {

    @Schema(description = "Identificador del platillo", example = "10")
    private Long id;

    @Schema(description = "Nombre del platillo", example = "Ensalada César")
    private String nombre;

    @Schema(description = "Precio del platillo en euros", example = "12.50")
    private Double precio;

    @Schema(description = "Lista de insumos o ingredientes", example = "[\"Lechuga\", \"Pollo\", \"Queso parmesano\"]")
    private List<String> insumos;

    @Schema(description = "Ruta de la foto asociada al platillo", example = "fotos/ensalada_cesar.jpg")
    private String fotoPath;

    @Schema(description = "ID del tipo de platillo", example = "1")
    private Long tipoId;

    @Schema(description = "Nombre del tipo de platillo", example = "Ensaladas")
    private String tipoNombre;
}
