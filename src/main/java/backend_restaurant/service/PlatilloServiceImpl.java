package backend_restaurant.service;

import backend_restaurant.model.Platillo;
import backend_restaurant.model.Tipo;
import backend_restaurant.repository.PlatilloRepository;
import backend_restaurant.repository.TipoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlatilloServiceImpl implements PlatilloService {

    private final PlatilloRepository platilloRepo;
    private final TipoRepository tipoRepo;

    @Cacheable(value = "platillos:list", unless = "#result == null || #result.isEmpty()")
    @Override
    public List<Platillo> listarTodos() {
        return platilloRepo.findAll();
    }

    @Cacheable(value = "platillos", key = "#id", unless = "#result == null")
    @Override
    public Platillo obtenerPorId(Long id) {
        return platilloRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Platillo no encontrado"));
    }

    @Caching(
            put = {
                    @CachePut(value = "platillos", key = "#result.id", unless = "#result == null")
            },
            evict = {
                    @CacheEvict(value = "platillos:list",  allEntries = true)
            }
    )
    @Override
    @Transactional
    public Platillo crear(Platillo platillo) {
        // opcional: validar que el tipo exista
        Tipo tipo = tipoRepo.findById(platillo.getTipo().getId())
                .orElseThrow(() -> new RuntimeException("Tipo no encontrado"));
        platillo.setTipo(tipo);
        return platilloRepo.save(platillo);
    }

    @Caching(
            put = {
                    @CachePut(value = "platillos", key = "#result.id", unless = "#result == null")
            },
            evict = {
                    @CacheEvict(value = "platillos:list",   allEntries = true)
            }
    )
    @Override
    @Transactional
    public Platillo actualizar(Long id, Platillo platillo) {
        Platillo existente = platilloRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Platillo no encontrado"));
        existente.setNombre(platillo.getNombre());
        existente.setPrecio(platillo.getPrecio());
        Tipo tipo = tipoRepo.findById(platillo.getTipo().getId())
                .orElseThrow(() -> new RuntimeException("Tipo no encontrado"));
        existente.setTipo(tipo);
        existente.setInsumos(platillo.getInsumos());
        return platilloRepo.save(existente);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "platillos", key = "#id"),
                    @CacheEvict(value = "platillos:list", allEntries = true)
            }
    )
    @Override
    @Transactional
    public void eliminar(Long id) {
        platilloRepo.deleteById(id);
    }
}