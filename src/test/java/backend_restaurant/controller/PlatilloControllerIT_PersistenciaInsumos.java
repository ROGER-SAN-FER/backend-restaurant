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
class PlatilloControllerIT_PersistenciaInsumos { // GET /api/platillos/{id}

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
    void crea_actualiza_y_lee_insumos_desde_bd() throws Exception {
        long tipoId = crearTipo("Postres");

        // Crear con 3 insumos
        var p = new PlatilloRequestDto();
        p.setNombre("Tarta de queso");
        p.setPrecio(4.5);
        p.setInsumos(List.of("Queso", "Huevo", "Azúcar"));
        p.setTipoId(tipoId);

        var create = mockMvc.perform(post("/api/platillos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(p)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.insumos", hasSize(3)))
                .andReturn();

        long id = objectMapper.readTree(create.getResponse().getContentAsString()).get("id").asLong();

        // GET por id: debe venir de la BD con los mismos insumos
        mockMvc.perform(get("/api/platillos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.insumos", hasSize(3)))
                .andExpect(jsonPath("$.insumos", containsInAnyOrder("Queso","Huevo","Azúcar")));

        // Actualizar con 2 insumos
        long tipoId2 = crearTipo("Bebidas");
        var upd = new PlatilloRequestDto();
        upd.setNombre("Tarta de queso fría");
        upd.setPrecio(5.0);
        upd.setInsumos(List.of("Queso", "Huevo"));
        upd.setTipoId(tipoId2);

        mockMvc.perform(put("/api/platillos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(upd)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.insumos", hasSize(2)));

        // GET por id otra vez: la BD debe reflejar los 2 insumos
        mockMvc.perform(get("/api/platillos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.insumos", hasSize(2)))
                .andExpect(jsonPath("$.insumos", containsInAnyOrder("Queso","Huevo")));
    }
}
