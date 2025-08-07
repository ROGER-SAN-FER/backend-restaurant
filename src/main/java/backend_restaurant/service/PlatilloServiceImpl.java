package backend_restaurant.service;

import backend_restaurant.model.Platillo;
import backend_restaurant.model.Tipo;
import backend_restaurant.repository.PlatilloRepository;
import backend_restaurant.repository.TipoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlatilloServiceImpl implements PlatilloService {

    private final PlatilloRepository platilloRepo;
    private final TipoRepository tipoRepo;


    @Override
    @Transactional(readOnly = true)
    public List<Platillo> listarTodos() {
        return platilloRepo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Platillo obtenerPorId(Long id) {
        return platilloRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Platillo no encontrado"));
    }

    @Override
    @Transactional
    public Platillo crear(Platillo platillo) {
        // opcional: validar que el tipo exista
        Tipo tipo = tipoRepo.findById(platillo.getTipo().getId())
                .orElseThrow(() -> new RuntimeException("Tipo no encontrado"));
        platillo.setTipo(tipo);
        return platilloRepo.save(platillo);
    }

    @Override
    @Transactional
    public Platillo actualizar(Long id, Platillo platillo) {
        Platillo existente = obtenerPorId(id);
        existente.setNombre(platillo.getNombre());
        existente.setPrecio(platillo.getPrecio());
        Tipo tipo = tipoRepo.findById(platillo.getTipo().getId())
                .orElseThrow(() -> new RuntimeException("Tipo no encontrado"));
        existente.setTipo(tipo);
        existente.setInsumos(platillo.getInsumos());
        return platilloRepo.save(existente);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        platilloRepo.deleteById(id);
    }
}