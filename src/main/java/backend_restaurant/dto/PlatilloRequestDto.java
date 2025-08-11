package backend_restaurant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Datos para crear o actualizar un platillo en el restaurante")
public class PlatilloRequestDto {

    @Schema(description = "Nombre del platillo", example = "Ensalada César")
    @NotEmpty(message = "{message.nombre.NotEmpty}")
    @NotBlank(message = "{message.nombre.NotBlank}")
    @Size(min = 4, max = 35, message = "{message.nombre.Size}")
    private String nombre;

    @Schema(description = "Precio del platillo en euros", example = "12.50")
    @NotNull(message = "{message.precio.NotNull}")
    @Positive(message = "{message.precio.Positive}")
    private Double precio;

    @Schema(description = "Lista de insumos o ingredientes del platillo", example = "[\"Lechuga\", \"Pollo\", \"Queso parmesano\"]")
    @Valid
    private List<String> insumos;

    @Schema(description = "Identificador del tipo de platillo", example = "1")
    @NotNull(message = "{message.tipoId.NotNull}")
    @Positive(message = "{message.tipoId.Positive}")
    private Long tipoId;
}
