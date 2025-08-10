package backend_restaurant.service;

import backend_restaurant.model.Platillo;
import backend_restaurant.model.Tipo;
import backend_restaurant.repository.PlatilloRepository;
import backend_restaurant.repository.TipoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlatilloServiceImpl implements PlatilloService {

    private final PlatilloRepository platilloRepo;
    private final TipoRepository tipoRepo;
    private final SupabaseStorageService supabaseStorageService;

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

    // Para Fotos
    @Caching(
            put   = { @CachePut(value = "platillos", key = "#result.id", unless = "#result == null") },
            evict = { @CacheEvict(value = "platillos:list", allEntries = true) }
    )
    @Transactional
    @Override
    public Platillo subirFoto(Long platilloId, MultipartFile file) {
        Platillo platillo = platilloRepo.findById(platilloId)
                .orElseThrow(() -> new RuntimeException("Platillo no encontrado"));

        String oldPath = platillo.getFotoPath();

        // 1) Subir nueva (carpeta única por platillo)
        String newPath = supabaseStorageService.upload(file, "platillos/" + platilloId);

        // 2) Intentar borrar la anterior (si había)
        if (oldPath != null && !oldPath.isBlank()) {
            try {
                supabaseStorageService.delete(oldPath);
            } catch (Exception e) {
                log.warn("No se pudo borrar la foto anterior {}: {}", oldPath, e.getMessage());
            }
        }

        // 3) Guardar referencia y devolver la entidad persistida
        platillo.setFotoPath(newPath);
        return platilloRepo.save(platillo); // <-- esto alimenta el @CachePut("platillos")
    }


    @Override
    public String obtenerFotoPublica(Long platilloId) {
        Platillo platillo = platilloRepo.findById(platilloId)
                .orElseThrow(() -> new RuntimeException("Platillo no encontrado"));
        if (platillo.getFotoPath() == null) {
            throw new RuntimeException("El platillo no tiene foto");
        }
        return supabaseStorageService.buildPublicUrl(platillo.getFotoPath());
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "platillos", key = "#platilloId"),
                    @CacheEvict(value = "platillos:list", allEntries = true)
            }
    )
    @Transactional
    @Override
    public void eliminarFoto(Long platilloId) {
        Platillo p = platilloRepo.findById(platilloId)
                .orElseThrow(() -> new RuntimeException("Platillo no encontrado"));

        if (p.getFotoPath() != null && !p.getFotoPath().isBlank()) {
            try {
                supabaseStorageService.delete(p.getFotoPath());
            } catch (Exception e) {
                log.warn("No se pudo borrar en Supabase {}: {}", p.getFotoPath(), e.getMessage());
            }
            p.setFotoPath(null);
            platilloRepo.save(p);
        }
    }

}