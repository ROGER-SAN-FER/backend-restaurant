package backend_restaurant.service;

import backend_restaurant.model.Tipo;
import backend_restaurant.repository.TipoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TipoServiceImpl implements TipoService {

    private final TipoRepository tipoRepo;

    @Cacheable(value = "tipos:list", unless = "#result == null || #result.isEmpty()")
    @Override
    public List<Tipo> listarTodos() {
        return tipoRepo.findAll();
    }

    @Cacheable(value = "tipos", key = "#id", unless = "#result == null")
    @Override
    public Tipo obtenerPorId(Long id) {
        return tipoRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipo no encontrado"));
    }


    @Caching(
            put = {
                    @CachePut(value = "tipos", key = "#result.id", unless = "#result == null")
            },
            evict = {
                    @CacheEvict(value = "tipos:list",  allEntries = true)
            }
    )
    @Override
    @Transactional
    public Tipo crear(Tipo tipo) {
        return tipoRepo.save(tipo);
    }

    @Caching(
            put = {
                    @CachePut(value = "tipos", key = "#result.id", unless = "#result == null")
            },
            evict = {
                    @CacheEvict(value = "tipos:list", allEntries = true)
            }
    )
    @Override
    @Transactional
    public Tipo actualizar(Long id, Tipo datos) {
        //Tipo existente = obtenerPorId(id);
        Tipo existente = tipoRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipo no encontrado"));
        existente.setNombre(datos.getNombre());
        return tipoRepo.save(existente);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "tipos", key = "#id"),
                    @CacheEvict(value = "tipos:list", allEntries = true)
            }
    )
    @Override
    @Transactional
    public void eliminar(Long id) {
        tipoRepo.deleteById(id);
    }
}