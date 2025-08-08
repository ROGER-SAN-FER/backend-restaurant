package backend_restaurant.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = false)
public class TipoRequestDto {

    @NotBlank(message = "{TipoRequestDto.nombre.NotBlank}")
    @Size(min = 4, max = 25, message = "{TipoRequestDto.nombre.Size}")
    @NotNull(message = "{TipoRequestDto.nombre.NotNull}")
    private String nombre;
}
