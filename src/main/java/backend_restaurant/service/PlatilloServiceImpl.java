package backend_restaurant.service;

import backend_restaurant.model.Platillo;
import backend_restaurant.model.Tipo;
import backend_restaurant.repository.PlatilloRepository;
import backend_restaurant.repository.TipoRepository;
import backend_restaurant.service.PlatilloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class PlatilloServiceImpl implements PlatilloService {

    @Autowired
    private PlatilloRepository platilloRepo;

    @Autowired
    private TipoRepository tipoRepo;

//    private final PlatilloRepository platilloRepo;
//    private final TipoRepository tipoRepo;

//    public PlatilloServiceImpl(PlatilloRepository platilloRepo, TipoRepository tipoRepo) {
//        this.platilloRepo = platilloRepo;
//        this.tipoRepo = tipoRepo;
//    }

    @Override
    public List<Platillo> listarTodos() {
        return platilloRepo.findAll();
    }

    @Override
    public Platillo obtenerPorId(Long id) {
        return platilloRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Platillo no encontrado"));
    }

    @Override
    public Platillo crear(Platillo platillo) {
        // opcional: validar que el tipo exista
        Tipo tipo = tipoRepo.findById(platillo.getTipo().getId())
                .orElseThrow(() -> new RuntimeException("Tipo no encontrado"));
        platillo.setTipo(tipo);
        return platilloRepo.save(platillo);
    }

    @Override
    public Platillo actualizar(Long id, Platillo datos) {
        Platillo existente = obtenerPorId(id);
        existente.setNombre(datos.getNombre());
        existente.setPrecio(datos.getPrecio());
        // actualizar tipo si es distinto
        Tipo tipo = tipoRepo.findById(datos.getTipo().getId())
                .orElseThrow(() -> new RuntimeException("Tipo no encontrado"));
        existente.setTipo(tipo);
        return platilloRepo.save(existente);
    }

    @Override
    public void eliminar(Long id) {
        platilloRepo.deleteById(id);
    }

    @Override
    public List<Platillo> listarPorTipo(Long tipoId) {
        return platilloRepo.findAllByTipo_Id(tipoId);
    }
}
