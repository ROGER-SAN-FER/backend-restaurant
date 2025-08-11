package backend_restaurant.controller;

import backend_restaurant.dto.PlatilloRequestDto;
import backend_restaurant.dto.PlatilloResponseDto;
import backend_restaurant.mapper.PlatilloMapper;
import backend_restaurant.model.Platillo;
import backend_restaurant.service.PlatilloService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Platillos", description = "Endpoints para gestionar platillos del restaurante")
@RestController
@RequestMapping("/api/platillos")
@RequiredArgsConstructor
public class PlatilloController {

    private final PlatilloMapper mapper;
    private final PlatilloService platilloService;

    /**
     * Lista todos los platillos disponibles.
     */
    @Operation(
            summary = "Listar todos los platillos",
            description = "Obtiene una lista con todos los platillos registrados en el sistema.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista obtenida con éxito",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PlatilloResponseDto.class)
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<PlatilloResponseDto>> listarTodos() {
        List<PlatilloResponseDto> listaPlatillosDto = platilloService.listarTodos()
                .stream()
                .map(mapper::toDto)
                .toList();
        return ResponseEntity
                .ok(listaPlatillosDto);
    }

    /**
     * Obtiene un platillo específico por su ID.
     */
    @Operation(
            summary = "Obtener platillo por ID",
            description = "Devuelve la información de un platillo a partir de su identificador.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Platillo encontrado"),
                    @ApiResponse(responseCode = "404", description = "Platillo no encontrado")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<PlatilloResponseDto> obtenerPorId(@PathVariable Long id) {
        PlatilloResponseDto platilloResponseDto = mapper.toDto(platilloService.obtenerPorId(id));
        return ResponseEntity
                .ok(platilloResponseDto);
    }

    /**
     * Crea un nuevo platillo.
     */
    @Operation(
            summary = "Crear un platillo",
            description = "Crea un nuevo platillo con los datos enviados en el cuerpo de la solicitud.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Platillo creado con éxito"),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos")
            }
    )
    @PostMapping
    public ResponseEntity<PlatilloResponseDto> crear(@RequestBody @Valid PlatilloRequestDto dto) {
        Platillo platillo = mapper.toEntity(dto);
        Platillo platilloCreado = platilloService.crear(platillo);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.toDto(platilloCreado));
    }

    /**
     * Actualiza un platillo existente.
     */
    @Operation(
            summary = "Actualizar platillo",
            description = "Actualiza un platillo existente con los datos enviados en el cuerpo de la solicitud.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Platillo actualizado con éxito"),
                    @ApiResponse(responseCode = "404", description = "Platillo no encontrado")
            }
    )
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

    /**
     * Elimina un platillo por su ID.
     */
    @Operation(
            summary = "Eliminar platillo",
            description = "Elimina un platillo del sistema por su identificador.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Platillo eliminado con éxito"),
                    @ApiResponse(responseCode = "404", description = "Platillo no encontrado")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        platilloService.eliminar(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Platillo con id = " + id + " eliminado");
    }

    /**
     * Sube o actualiza la foto de un platillo y devuelve su URL pública.
     */
    @Operation(
            summary = "Subir o actualizar foto",
            description = "Sube o actualiza la foto de un platillo y devuelve su URL pública.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Foto subida con éxito"),
                    @ApiResponse(responseCode = "404", description = "Platillo no encontrado")
            }
    )
    @PostMapping(
            value = "/{id}/foto",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Map<String, String>> subirFoto(
            @Parameter(
                    description = "ID del platillo al que se le subirá o actualizará la foto",
                    example = "1",
                    required = true
            )
            @PathVariable Long id,

            @Parameter(
                    description = "Archivo de imagen en formato JPG o PNG",
                    required = true
            )
            @RequestPart("file") MultipartFile file
    ) {
        Platillo p = platilloService.subirFoto(id, file);
        String publicUrl = platilloService.obtenerFotoPublica(p.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("url", publicUrl));
    }

    /**
     * Obtiene la URL pública de la foto de un platillo.
     */
    @Operation(
            summary = "Obtener URL de la foto",
            description = "Devuelve la URL pública de la foto asociada a un platillo.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "URL obtenida con éxito"),
                    @ApiResponse(responseCode = "404", description = "Foto o platillo no encontrado")
            }
    )
    @GetMapping("/{id}/foto")
    public ResponseEntity<Map<String, String>> obtenerFotoUrl(
            @Parameter(
                    description = "ID del platillo cuya foto pública se desea obtener",
                    example = "1",
                    required = true
            )
            @PathVariable Long id
    ) {
        String url = platilloService.obtenerFotoPublica(id);
        return ResponseEntity.ok(Map.of("url", url));
    }

    /**
     * Borra la foto asociada a un platillo.
     */
    @Operation(
            summary = "Borrar foto de platillo",
            description = "Elimina la foto asociada a un platillo.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Foto eliminada con éxito"),
                    @ApiResponse(responseCode = "404", description = "Foto o platillo no encontrado")
            }
    )
    @DeleteMapping("/{id}/foto")
    public ResponseEntity<Void> borrarFoto(
            @Parameter(
                    description = "ID del platillo cuya foto se desea eliminar",
                    example = "1",
                    required = true
            )
            @PathVariable Long id
    ) {
        platilloService.eliminarFoto(id);
        return ResponseEntity.noContent().build();
    }

}
