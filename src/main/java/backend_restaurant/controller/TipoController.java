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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Tipos", description = "Endpoints para gestionar tipos de platillos")
@RestController
@RequestMapping("/api/tipos")
@RequiredArgsConstructor
public class TipoController {

    private final TipoService tipoService;
    private final TipoMapper tipoMapper;

    /**
     * Lista todos los tipos de platillo disponibles.
     */
    @Operation(
            summary = "Listar todos los tipos",
            description = "Obtiene una lista con todos los tipos de platillo registrados en el sistema.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista obtenida con éxito",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TipoResponseDto.class)
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<TipoResponseDto>> listarTodosDTO() {
        List<TipoResponseDto> listaTiposDto = tipoService.listarTodos()
                .stream()
                .map(tipoMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity
                .ok(listaTiposDto);
    }

    /**
     * Obtiene un tipo de platillo específico por su ID.
     */
    @Operation(
            summary = "Obtener tipo por ID",
            description = "Devuelve la información de un tipo de platillo a partir de su identificador.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tipo encontrado"),
                    @ApiResponse(responseCode = "404", description = "Tipo no encontrado")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<TipoResponseDto> obtenerPorId(@PathVariable Long id) {
        TipoResponseDto tipoRequestDto = tipoMapper.toDto(tipoService.obtenerPorId(id));
        return ResponseEntity
                .ok(tipoRequestDto);
    }

    /**
     * Crea un nuevo tipo de platillo.
     */
    @Operation(
            summary = "Crear tipo",
            description = "Crea un nuevo tipo de platillo con los datos enviados en el cuerpo de la solicitud.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Tipo creado con éxito"),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos")
            }
    )
    @PostMapping
    public ResponseEntity<TipoResponseDto> crear(@RequestBody @Valid TipoRequestDto tipoRequestDto) {
        Tipo tipoEntity = tipoMapper.toEntity(tipoRequestDto);
        Tipo tipoCreado = tipoService.crear(tipoEntity);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(tipoMapper.toDto(tipoCreado));
    }

    /**
     * Actualiza un tipo de platillo existente.
     */
    @Operation(
            summary = "Actualizar tipo",
            description = "Actualiza un tipo de platillo existente con los datos enviados en el cuerpo de la solicitud.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tipo actualizado con éxito"),
                    @ApiResponse(responseCode = "404", description = "Tipo no encontrado")
            }
    )
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

    /**
     * Elimina un tipo de platillo por su ID.
     */
    @Operation(
            summary = "Eliminar tipo",
            description = "Elimina un tipo de platillo del sistema por su identificador.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tipo eliminado con éxito"),
                    @ApiResponse(responseCode = "404", description = "Tipo no encontrado")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        tipoService.eliminar(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Tipo con id = " + id + " eliminado");
    }
}
