package backend_restaurant.controller;


import backend_restaurant.model.Tipo;
import backend_restaurant.service.TipoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipos")
public class TipoController {

    private final TipoService tipoService;

    public TipoController(TipoService tipoService) {
        this.tipoService = tipoService;
    }

    @GetMapping
    public List<Tipo> listarTodos() {
        return tipoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tipo> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(tipoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Tipo> crear(@RequestBody Tipo tipo) {
        Tipo creado = tipoService.crear(tipo);
        return ResponseEntity
                .created(null)              // podrías usar URI de ubicación: URI.create("/api/tipos/" + creado.getId())
                .body(creado);
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
