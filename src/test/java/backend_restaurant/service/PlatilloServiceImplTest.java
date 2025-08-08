package backend_restaurant.service;

import backend_restaurant.model.Platillo;
import backend_restaurant.model.Tipo;
import backend_restaurant.repository.PlatilloRepository;
import backend_restaurant.repository.TipoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlatilloServiceImplTest {

    @Mock
    private PlatilloRepository platilloRepo;

    @Mock
    private TipoRepository tipoRepo;

    @InjectMocks
    private PlatilloServiceImpl service;

    // -------- listarTodos --------
    @Test
    void listarTodos_debeRetornarLista() {
        Platillo p1 = new Platillo(); p1.setId(1L); p1.setNombre("A");
        Platillo p2 = new Platillo(); p2.setId(2L); p2.setNombre("B");
        when(platilloRepo.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Platillo> res = service.listarTodos();

        assertEquals(2, res.size());
        assertEquals("A", res.get(0).getNombre());
        verify(platilloRepo, times(1)).findAll();
    }

    // -------- obtenerPorId --------
    @Test
    void obtenerPorId_debeRetornarCuandoExiste() {
        Platillo p = new Platillo(); p.setId(10L); p.setNombre("Taco");
        when(platilloRepo.findById(10L)).thenReturn(Optional.of(p));

        Platillo res = service.obtenerPorId(10L);

        assertNotNull(res);
        assertEquals("Taco", res.getNombre());
        verify(platilloRepo).findById(10L);
    }

    @Test
    void obtenerPorId_debeLanzarCuandoNoExiste() {
        when(platilloRepo.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.obtenerPorId(99L));
        assertEquals("Platillo no encontrado", ex.getMessage());
        verify(platilloRepo).findById(99L);
    }

    // -------- crear --------
    @Test
    void crear_debeValidarTipoYGuardar() {
        // Platillo entrante con referencia a Tipo (solo id)
        Tipo refTipo = new Tipo(); refTipo.setId(5L);
        Platillo nuevo = new Platillo();
        nuevo.setNombre("Ceviche");
        nuevo.setTipo(refTipo);
        nuevo.setInsumos(Arrays.asList("Pescado", "Limón"));

        // Tipo real existente en BD
        Tipo tipoReal = new Tipo(); tipoReal.setId(5L); tipoReal.setNombre("Marino");

        when(tipoRepo.findById(5L)).thenReturn(Optional.of(tipoReal));
        when(platilloRepo.save(any(Platillo.class))).thenAnswer(i -> i.getArgument(0));

        Platillo res = service.crear(nuevo);

        assertEquals("Ceviche", res.getNombre());
        assertEquals("Marino", res.getTipo().getNombre());
        assertEquals(2, res.getInsumos().size());

        // Verificamos que se guardó con el tipo *real*
        ArgumentCaptor<Platillo> captor = ArgumentCaptor.forClass(Platillo.class);
        verify(platilloRepo).save(captor.capture());
        assertSame(tipoReal, captor.getValue().getTipo());

        verify(tipoRepo).findById(5L);
    }

    @Test
    void crear_debeLanzarSiTipoNoExiste() {
        Tipo refTipo = new Tipo(); refTipo.setId(404L);
        Platillo nuevo = new Platillo();
        nuevo.setNombre("Sopa");
        nuevo.setTipo(refTipo);

        when(tipoRepo.findById(404L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.crear(nuevo));
        assertEquals("Tipo no encontrado", ex.getMessage());
        verify(tipoRepo).findById(404L);
        verify(platilloRepo, never()).save(any());
    }

    // -------- actualizar --------
    @Test
    void actualizar_debeActualizarCamposYGuardar() {
        // existente en BD
        Platillo existente = new Platillo();
        existente.setId(1L);
        existente.setNombre("Antiguo");
        existente.setInsumos(Arrays.asList("I1"));

        // datos entrantes
        Tipo refTipo = new Tipo(); refTipo.setId(7L);
        Platillo datos = new Platillo();
        datos.setNombre("Nuevo");
        datos.setTipo(refTipo);
        datos.setInsumos(Arrays.asList("A", "B"));

        // tipo real en BD
        Tipo tipoReal = new Tipo(); tipoReal.setId(7L); tipoReal.setNombre("Especial");

        when(platilloRepo.findById(1L)).thenReturn(Optional.of(existente));
        when(tipoRepo.findById(7L)).thenReturn(Optional.of(tipoReal));
        when(platilloRepo.save(any(Platillo.class))).thenAnswer(i -> i.getArgument(0));

        Platillo res = service.actualizar(1L, datos);

        assertEquals("Nuevo", res.getNombre());
        assertEquals("Especial", res.getTipo().getNombre());
        assertEquals(Arrays.asList("A", "B"), res.getInsumos());

        verify(platilloRepo).findById(1L);
        verify(tipoRepo).findById(7L);
        verify(platilloRepo).save(existente); // se guarda el *existente* modificado
    }

    @Test
    void actualizar_debeLanzarSiPlatilloNoExiste() {
        when(platilloRepo.findById(2L)).thenReturn(Optional.empty());

        Platillo datos = new Platillo();
        Tipo refTipo = new Tipo(); refTipo.setId(1L);
        datos.setTipo(refTipo);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.actualizar(2L, datos));
        assertEquals("Platillo no encontrado", ex.getMessage());

        verify(platilloRepo).findById(2L);
        verify(tipoRepo, never()).findById(anyLong());
        verify(platilloRepo, never()).save(any());
    }

    @Test
    void actualizar_debeLanzarSiTipoNoExiste() {
        // existente
        Platillo existente = new Platillo(); existente.setId(3L);

        // datos con tipo inexistente
        Tipo refTipo = new Tipo(); refTipo.setId(123L);
        Platillo datos = new Platillo(); datos.setTipo(refTipo);

        when(platilloRepo.findById(3L)).thenReturn(Optional.of(existente));
        when(tipoRepo.findById(123L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.actualizar(3L, datos));
        assertEquals("Tipo no encontrado", ex.getMessage());

        verify(platilloRepo).findById(3L);
        verify(tipoRepo).findById(123L);
        verify(platilloRepo, never()).save(any());
    }

    // -------- eliminar --------
    @Test
    void eliminar_debeEliminarPorId() {
        service.eliminar(9L);
        verify(platilloRepo).deleteById(9L);
    }
}
