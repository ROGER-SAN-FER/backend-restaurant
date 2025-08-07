package backend_restaurant.controller;


import backend_restaurant.dto.PlatilloDto;
import backend_restaurant.dto.TipoDto;
import backend_restaurant.mapper.TipoMapper;
import backend_restaurant.model.Tipo;
import backend_restaurant.service.TipoService;
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
    public ResponseEntity<List<TipoDto>> listarTodosDTO() {
        List<TipoDto> listaTiposDto = tipoService.listarTodos().stream().map(tipoMapper::toDto).collect(Collectors.toList());
        return ResponseEntity
                .ok(listaTiposDto);
    }

    // mostrar tipo por id
    @GetMapping("/{id}")
    public ResponseEntity<TipoDto> obtenerPorId(@PathVariable Long id) {
        TipoDto tipoDto =  tipoMapper.toDto(tipoService.obtenerPorId(id));
        return ResponseEntity
                .ok(tipoDto);
    }

    // crear tipo
    @PostMapping
    public ResponseEntity<TipoDto> crear(@RequestBody Tipo tipo) {
        Tipo tipoCreado = tipoService.crear(tipo);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(tipoMapper.toDto(tipoCreado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tipo> actualizar(
            @PathVariable Long id,
            @RequestBody Tipo tipoDatos) {
        Tipo actualizado = tipoService.actualizar(id, tipoDatos);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        tipoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
