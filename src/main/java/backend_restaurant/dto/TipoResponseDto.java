package backend_restaurant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TipoResponseDto {
    private Long id;
    private String nombre;
}