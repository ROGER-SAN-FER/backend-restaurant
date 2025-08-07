package backend_restaurant.controller;

import backend_restaurant.dto.TipoRequestDto;
import backend_restaurant.dto.TipoResponseDto;
import backend_restaurant.mapper.TipoMapper;
import backend_restaurant.model.Tipo;
import backend_restaurant.service.TipoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tipos")
@RequiredArgsConstructor
public class TipoController {

    private final TipoService tipoService;
    private final TipoMapper tipoMapper;

    // mostrar todos los tipos
    @GetMapping
    public ResponseEntity<List<TipoResponseDto>> listarTodosDTO() {
        List<TipoResponseDto> listaTiposDto = tipoService.listarTodos().stream().map(tipoMapper::toDto).collect(Collectors.toList());
        return ResponseEntity
                .ok(listaTiposDto);
    }

    // mostrar tipo por id
    @GetMapping("/{id}")
    public ResponseEntity<TipoResponseDto> obtenerPorId(@PathVariable Long id) {
        TipoResponseDto tipoRequestDto =  tipoMapper.toDto(tipoService.obtenerPorId(id));
        return ResponseEntity
                .ok(tipoRequestDto);
    }

    // crear tipo
    @PostMapping
    public ResponseEntity<TipoResponseDto> crear(@RequestBody @Valid TipoRequestDto tipoRequestDto) {
        Tipo tipoEntity = tipoMapper.toEntity(tipoRequestDto);
        Tipo tipoCreado = tipoService.crear(tipoEntity);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(tipoMapper.toDto(tipoCreado));
    }

    // actualizar tipo
    @PutMapping("/{id}")
    public ResponseEntity<TipoResponseDto> actualizar(
            @PathVariable Long id,
            @RequestBody TipoRequestDto tipoRequestDto) {
        Tipo tipo = tipoMapper.toEntity(tipoRequestDto);
        Tipo tipoActualizado = tipoService.actualizar(id, tipo);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tipoMapper.toDto(tipoActualizado));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        tipoService.eliminar(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Tipo con id = " + id + " eliminado");
    }
}
