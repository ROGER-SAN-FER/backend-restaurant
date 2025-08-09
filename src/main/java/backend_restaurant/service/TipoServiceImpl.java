package backend_restaurant.service;

import backend_restaurant.model.Tipo;
import backend_restaurant.repository.TipoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TipoServiceImpl implements TipoService {

    private final TipoRepository tipoRepo;

    @Override
    @Transactional(readOnly = true)
    public List<Tipo> listarTodos() {
        return tipoRepo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Tipo obtenerPorId(Long id) {
        return tipoRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipo no encontrado"));
    }

    @Override
    @Transactional
    public Tipo crear(Tipo tipo) {
        return tipoRepo.save(tipo);
    }

    @Override
    @Transactional
    public Tipo actualizar(Long id, Tipo datos) {
        Tipo existente = obtenerPorId(id);
        existente.setNombre(datos.getNombre());
        return tipoRepo.save(existente);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        tipoRepo.deleteById(id);
    }
}

