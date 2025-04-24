package backend_restaurant.service;

import backend_restaurant.model.Tipo;
import backend_restaurant.repository.TipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class TipoServiceImpl implements TipoService {

    @Autowired
    private TipoRepository tipoRepo;

//    private final TipoRepository tipoRepo;
//
//    public TipoServiceImpl(TipoRepository tipoRepo) {
//        this.tipoRepo = tipoRepo;
//    }

    @Override
    public List<Tipo> listarTodos() {
        return tipoRepo.findAll();
    }

    @Override
    public Tipo obtenerPorId(Long id) {
        return tipoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Tipo no encontrado"));
    }

    @Override
    public Tipo crear(Tipo tipo) {
        return tipoRepo.save(tipo);
    }

    @Override
    public Tipo actualizar(Long id, Tipo datos) {
        Tipo existente = obtenerPorId(id);
        existente.setNombre(datos.getNombre());
        return tipoRepo.save(existente);
    }

    @Override
    public void eliminar(Long id) {
        tipoRepo.deleteById(id);
    }
}

