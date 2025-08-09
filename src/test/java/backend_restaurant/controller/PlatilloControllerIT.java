package backend_restaurant.controller;

import backend_restaurant.dto.PlatilloRequestDto;
import backend_restaurant.dto.TipoRequestDto;
import backend_restaurant.repository.PlatilloRepository;
import backend_restaurant.repository.TipoRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PlatilloControllerIT {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired PlatilloRepository platilloRepository;
    @Autowired TipoRepository tipoRepository;

    @AfterEach
    void clean() {
        platilloRepository.deleteAll();
        tipoRepository.deleteAll();
    }

    private long crearTipo(String nombre) throws Exception {
        var req = new TipoRequestDto();
        req.setNombre(nombre);
        var res = mockMvc.perform(post("/api/tipos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        JsonNode node = objectMapper.readTree(res);
        return node.get("id").asLong();
    }

    @Test
    void flujo_completo_platillo() throws Exception {
        long tipoId = crearTipo("Postres");

        // Crear platillo
        var p = new PlatilloRequestDto();
        p.setNombre("Tarta de queso");
        p.setPrecio(4.5);
        p.setInsumos(List.of("Queso", "Huevo", "Azúcar"));
        p.setTipoId(tipoId);

        var createResult = mockMvc.perform(post("/api/platillos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(p)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.nombre", is("Tarta de queso")))
                .andExpect(jsonPath("$.precio", is(4.5)))
                .andExpect(jsonPath("$.insumos", hasSize(3)))
                .andExpect(jsonPath("$.tipoId", is((int) tipoId)))
                .andExpect(jsonPath("$.tipoNombre", is("Postres")))
                .andReturn();

        long createdId = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asLong();

        // Listar
        mockMvc.perform(get("/api/platillos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        // Obtener por id
        mockMvc.perform(get("/api/platillos/{id}", createdId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) createdId)))
                .andExpect(jsonPath("$.tipoNombre", is("Postres")));

        // Actualizar (cambiar tipo y datos)
        long tipoId2 = crearTipo("Bebidas");
        var upd = new PlatilloRequestDto();
        upd.setNombre("Tarta de queso fría");
        upd.setPrecio(5.0);
        upd.setInsumos(List.of("Queso", "Huevo"));
        upd.setTipoId(tipoId2);

        mockMvc.perform(put("/api/platillos/{id}", createdId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(upd)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Tarta de queso fría")))
                .andExpect(jsonPath("$.precio", is(5.0)))
                .andExpect(jsonPath("$.insumos", hasSize(2)))
                .andExpect(jsonPath("$.tipoNombre", is("Bebidas")));

        // Eliminar
        mockMvc.perform(delete("/api/platillos/{id}", createdId))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("eliminado")));
    }

    @Test
    void validar_creacion_platillo_invalido() throws Exception {
        long tipoId = crearTipo("Entrantes");

        var bad = new PlatilloRequestDto();
        bad.setNombre("abc");          // min 4
        bad.setPrecio(-1.0);           // debe ser positivo
        bad.setInsumos(List.of());
        bad.setTipoId(tipoId);

        mockMvc.perform(post("/api/platillos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bad)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nombre", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.precio", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.nombre[0]").value("El nombre debe tener entre 4 y 35 caracteres"))
                .andExpect(jsonPath("$.precio[0]").value("El precio tiene que ser positivo"));
    }
}
