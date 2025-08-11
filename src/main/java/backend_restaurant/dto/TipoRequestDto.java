package backend_restaurant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Datos para crear o actualizar un tipo de platillo")
public class TipoRequestDto {

    @Schema(description = "Nombre del tipo de platillo", example = "Ensaladas")
    @NotBlank(message = "{message.nombre.NotBlank}")
    @Size(min = 4, max = 35, message = "{message.nombre.Size}")
    @NotEmpty(message = "{message.nombre.NotEmpty}")
    private String nombre;
}
