package backend_restaurant.controller;

import backend_restaurant.dto.PlatilloDto;
import backend_restaurant.mapper.PlatilloMapper;
import backend_restaurant.model.Platillo;
import backend_restaurant.service.PlatilloService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/platillos")
@RequiredArgsConstructor
public class PlatilloController {

    private final PlatilloMapper mapper;
    private final PlatilloService platilloService;

    // mostrar todos los platillos
    @GetMapping
    public ResponseEntity<List<PlatilloDto>> listarTodos() {
        List<PlatilloDto> listaPlatillosDto= platilloService.listarTodos()
                .stream()
                .map(mapper::toDto)
                .toList();
        return ResponseEntity
                .ok(listaPlatillosDto);
    }

    // mostrar platillo por id
    @GetMapping("/{id}")
    public ResponseEntity<PlatilloDto> obtenerPorId(@PathVariable Long id) {
        PlatilloDto platilloDto =  mapper.toDto(platilloService.obtenerPorId(id));
        return ResponseEntity
                .ok(platilloDto);
    }

    // crear platillo
    @PostMapping
    public ResponseEntity<PlatilloDto> crear(@RequestBody PlatilloDto dto) {
        Platillo platillo = mapper.toEntity(dto);
        Platillo platilloCreado = platilloService.crear(platillo);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.toDto(platilloCreado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlatilloDto> actualizar(
            @PathVariable Long id,
            @RequestBody PlatilloDto platilloDatos) {
        Platillo entidad = mapper.toEntity(platilloDatos);
        Platillo actualizado = platilloService.actualizar(id, entidad);
        return ResponseEntity.ok(mapper.toDto(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        platilloService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
