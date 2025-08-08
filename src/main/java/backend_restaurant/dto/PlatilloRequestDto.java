package backend_restaurant.dto;

import lombok.Data;

import java.util.List;

@Data
public class PlatilloRequestDto {
    private String nombre;
    private Double precio;
    private List<String> insumos;
    private Long tipoId;
}
