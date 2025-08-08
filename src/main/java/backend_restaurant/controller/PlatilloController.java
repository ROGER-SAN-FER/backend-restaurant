package backend_restaurant.controller;

import backend_restaurant.dto.PlatilloRequestDto;
import backend_restaurant.dto.PlatilloResponseDto;
import backend_restaurant.mapper.PlatilloMapper;
import backend_restaurant.model.Platillo;
import backend_restaurant.service.PlatilloService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/platillos")
@RequiredArgsConstructor
public class PlatilloController {

    private final PlatilloMapper mapper;
    private final PlatilloService platilloService;

    // mostrar todos los platillos
    @GetMapping
    public ResponseEntity<List<PlatilloResponseDto>> listarTodos() {
        List<PlatilloResponseDto> listaPlatillosDto= platilloService.listarTodos()
                .stream()
                .map(mapper::toDto)
                .toList();
        return ResponseEntity
                .ok(listaPlatillosDto);
    }

    // mostrar platillo por id
    @GetMapping("/{id}")
    public ResponseEntity<PlatilloResponseDto> obtenerPorId(@PathVariable Long id) {
        PlatilloResponseDto platilloResponseDto =  mapper.toDto(platilloService.obtenerPorId(id));
        return ResponseEntity
                .ok(platilloResponseDto);
    }

    // crear platillo
    @PostMapping
    public ResponseEntity<PlatilloResponseDto> crear(@RequestBody PlatilloRequestDto dto) {
        Platillo platillo = mapper.toEntity(dto);
        Platillo platilloCreado = platilloService.crear(platillo);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.toDto(platilloCreado));
    }

    // actualizar platillo
    @PutMapping("/{id}")
    public ResponseEntity<PlatilloResponseDto> actualizar(
            @PathVariable Long id,
            @RequestBody PlatilloRequestDto platilloRequestDto) {
        Platillo platillo = mapper.toEntity(platilloRequestDto);
        Platillo platilloActualizado = platilloService.actualizar(id, platillo);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mapper.toDto(platilloActualizado));
    }

    // eliminar platillo
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        platilloService.eliminar(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Platillo con id = " + id + " eliminado");
    }
}
