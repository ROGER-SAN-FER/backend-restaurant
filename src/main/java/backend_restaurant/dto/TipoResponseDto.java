package backend_restaurant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Respuesta con los datos de un tipo de platillo")
public class TipoResponseDto {

    @Schema(description = "Identificador del tipo de platillo", example = "1")
    private Long id;

    @Schema(description = "Nombre del tipo de platillo", example = "Ensaladas")
    private String nombre;
}
