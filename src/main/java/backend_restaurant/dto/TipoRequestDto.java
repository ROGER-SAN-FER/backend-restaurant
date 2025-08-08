package backend_restaurant.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TipoRequestDto {

    @NotBlank(message = "{message.nombre.NotBlank}")
    @Size(min = 4, max = 25, message = "{message.nombre.Size}")
    @NotEmpty(message = "{message.nombre.NotNull}")
    private String nombre;
}
