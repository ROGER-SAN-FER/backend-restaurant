package backend_restaurant.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.List;

@Data
public class PlatilloRequestDto {

    @NotEmpty(message = "{message.nombre.NotEmpty}")
    @NotBlank(message = "{message.nombre.NotBlank}")
    @Size(min = 4, max = 35, message = "{message.nombre.Size}")
    private String nombre;

    @NotNull(message = "{message.precio.NotNull}")
    @Positive(message = "{message.precio.Positive}")
    private Double precio;

    @Valid
    private List<String> insumos;

    @NotNull(message = "{message.tipoId.NotNull}")
    @Positive(message = "{message.tipoId.Positive}")
    private Long tipoId;
}
