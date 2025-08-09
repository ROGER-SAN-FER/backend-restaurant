package backend_restaurant.controller;

import backend_restaurant.dto.TipoRequestDto;
import backend_restaurant.repository.TipoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TipoControllerIT {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired TipoRepository tipoRepository;

    @AfterEach
    void clean() { tipoRepository.deleteAll(); }

    @Test
    void crear_listar_obtener_actualizar_eliminar_tipo() throws Exception {
        // Crear
        var req = new TipoRequestDto();
        req.setNombre("Entrantes");

        var createResult = mockMvc.perform(post("/api/tipos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.nombre", is("Entrantes")))
                .andReturn();

        var createdJson = createResult.getResponse().getContentAsString();
        var createdId = objectMapper.readTree(createdJson).get("id").asLong();

        // Listar
        mockMvc.perform(get("/api/tipos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre", is("Entrantes")));

        // Obtener por id
        mockMvc.perform(get("/api/tipos/{id}", createdId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) createdId)))
                .andExpect(jsonPath("$.nombre", is("Entrantes")));

        // Actualizar
        var upd = new TipoRequestDto();
        upd.setNombre("Principales");

        mockMvc.perform(put("/api/tipos/{id}", createdId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(upd)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Principales")));

        // Eliminar
        mockMvc.perform(delete("/api/tipos/{id}", createdId))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("eliminado")));

        // Verificar que ya no existe
        mockMvc.perform(get("/api/tipos/{id}", createdId))
                .andExpect(status().is4xxClientError()); // tu service lanza RuntimeException → traducirá a 500/404 según tu advice (si lo añades)
    }

    @Test
    void validar_creacion_tipo_invalido() throws Exception {
        var req = new TipoRequestDto();
        req.setNombre("ab"); // demasiado corto (min 4)

        mockMvc.perform(post("/api/tipos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nombre", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.nombre[0]").value("El nombre debe tener entre 4 y 35 caracteres"));
    }
}
