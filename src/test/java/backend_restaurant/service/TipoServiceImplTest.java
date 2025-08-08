package backend_restaurant.service;

import backend_restaurant.model.Tipo;
import backend_restaurant.repository.TipoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TipoServiceImplTest {

    @Mock
    private TipoRepository tipoRepo;

    @InjectMocks
    private TipoServiceImpl tipoService;


    @Test
    void listarTodos_debeRetornarListaDeTipos() {
        // Arrange
        Tipo tipo1 = new Tipo();
        tipo1.setId(1L);
        tipo1.setNombre("Vegano");

        Tipo tipo2 = new Tipo();
        tipo2.setId(2L);
        tipo2.setNombre("Tradicional");

        List<Tipo> tipos = Arrays.asList(tipo1, tipo2);
        when(tipoRepo.findAll()).thenReturn(tipos);

        // Act
        List<Tipo> resultado = tipoService.listarTodos();

        // Assert
        assertEquals(2, resultado.size());
        assertEquals("Vegano", resultado.get(0).getNombre());
        verify(tipoRepo, times(1)).findAll();
    }

    @Test
    void obtenerPorId_debeRetornarTipoCuandoExiste() {
        // Arrange
        Tipo tipo = new Tipo();
        tipo.setId(1L);
        tipo.setNombre("Vegano");

        when(tipoRepo.findById(1L)).thenReturn(Optional.of(tipo));

        // Act
        Tipo resultado = tipoService.obtenerPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals("Vegano", resultado.getNombre());
        verify(tipoRepo, times(1)).findById(1L);
    }

    @Test
    void obtenerPorId_debeLanzarExcepcionCuandoNoExiste() {
        // Arrange
        when(tipoRepo.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> tipoService.obtenerPorId(1L));
        assertEquals("Tipo no encontrado", ex.getMessage());
        verify(tipoRepo, times(1)).findById(1L);
    }

    @Test
    void crear_debeGuardarYRetornarTipo() {
        // Arrange
        Tipo tipo = new Tipo();
        tipo.setNombre("Nuevo");

        when(tipoRepo.save(tipo)).thenReturn(tipo);

        // Act
        Tipo resultado = tipoService.crear(tipo);

        // Assert
        assertEquals("Nuevo", resultado.getNombre());
        verify(tipoRepo, times(1)).save(tipo);
    }

    @Test
    void actualizar_debeActualizarYRetornarTipo() {
        // Arrange
        Tipo existente = new Tipo();
        existente.setId(1L);
        existente.setNombre("Viejo");

        Tipo datos = new Tipo();
        datos.setNombre("Actualizado");

        when(tipoRepo.findById(1L)).thenReturn(Optional.of(existente));
        when(tipoRepo.save(any(Tipo.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Tipo resultado = tipoService.actualizar(1L, datos);

        // Assert
        assertEquals("Actualizado", resultado.getNombre());
        verify(tipoRepo, times(1)).findById(1L);
        verify(tipoRepo, times(1)).save(existente);
    }

    @Test
    void eliminar_debeEliminarPorId() {
        // Arrange
        Long id = 1L;

        // Act
        tipoService.eliminar(id);

        // Assert
        verify(tipoRepo, times(1)).deleteById(id);
    }
}
