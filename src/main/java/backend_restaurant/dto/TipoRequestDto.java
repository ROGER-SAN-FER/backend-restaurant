package backend_restaurant.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = false)
public class TipoRequestDto {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 4, max = 25, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String nombre;
}
