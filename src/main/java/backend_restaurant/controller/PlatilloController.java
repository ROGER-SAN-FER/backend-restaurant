package backend_restaurant.controller;

import backend_restaurant.dto.PlatilloRequestDto;
import backend_restaurant.dto.PlatilloResponseDto;
import backend_restaurant.mapper.PlatilloMapper;
import backend_restaurant.model.Platillo;
import backend_restaurant.service.PlatilloService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;


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
    public ResponseEntity<PlatilloResponseDto> crear(@RequestBody @Valid PlatilloRequestDto dto) {
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

    // 1) Subir/actualizar foto - Devuelve Url
    @PostMapping(
            value = "/{id}/foto",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Map<String, String>> subirFoto(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file) {

        Platillo p = platilloService.subirFoto(id, file);      // actualiza BD y caché
        String publicUrl = platilloService.obtenerFotoPublica(p.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("url", publicUrl));
    }

//    // Devuelve platilloDto:
//    @PostMapping(
//            value = "/{id}/foto",
//            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
//    )
//    public ResponseEntity<PlatilloResponseDto> subirFoto(
//            @PathVariable Long id,
//            @RequestPart("file") MultipartFile file) {
//
//        Platillo actualizado = platilloService.subirFoto(id, file);
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(mapper.toDto(actualizado));
//    }


    // 2) Obtener URL pública de la foto
    @GetMapping("/{id}/foto")
    public ResponseEntity<Map<String, String>> obtenerFotoUrl(@PathVariable Long id) {
        String url = platilloService.obtenerFotoPublica(id);
        return ResponseEntity.ok(Map.of("url", url));
    }

    // 3) Borrar foto
    @DeleteMapping("/{id}/foto")
    public ResponseEntity<Void> borrarFoto(@PathVariable Long id) {
        platilloService.eliminarFoto(id);
        return ResponseEntity.noContent().build();
    }
}
