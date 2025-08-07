package backend_restaurant.controller;

import backend_restaurant.dto.PlatilloDto;
import backend_restaurant.model.Platillo;
import backend_restaurant.service.PlatilloService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/platillos")
@RequiredArgsConstructor
public class PlatilloController {

    private final PlatilloMapperTradicional mapper;
    private final PlatilloService platilloService;


    @GetMapping
    public List<PlatilloDto> listarTodos() {
        return platilloService.listarTodos()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Platillo> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(platilloService.obtenerPorId(id));
    }

    @GetMapping("/tipo/{tipoId}")
    public List<PlatilloDto> listarPorTipo(@PathVariable Long tipoId) {
        return platilloService.listarPorTipo(tipoId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @PostMapping
    public ResponseEntity<PlatilloDto> crear(@RequestBody PlatilloDto dto) {
        Platillo entidad = mapper.toEntity(dto);
        Platillo creado = platilloService.crear(entidad);
        URI location = URI.create("/api/platillos/" + creado.getId());
        return ResponseEntity
                .created(location)
                .body(mapper.toDto(creado));
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
