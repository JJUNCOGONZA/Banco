package com.banco.usuarios;

import com.banco.usuarios.service.model.Persona;
import com.banco.usuarios.service.repository.PersonaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PersonaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonaRepository personaRepository;

    @BeforeEach
    void setUp() {
        // Limpiar la tabla de personas para evitar duplicados
        personaRepository.deleteAll();
    }

    @Test
    void testCreatePersona() throws Exception {
        String personaJson = "{\"nombre\":\"Jeisson Junco\",\"genero\":\"Masculino\",\"edad\":30,\"identificacion\":\"123456789\",\"direccion\":\"Calle 123\",\"telefono\":\"987654321\"}";

        mockMvc.perform(post("/personas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(personaJson))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("Jeisson Junco"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.identificacion").value("123456789"));
    }

    @Test
    void testGetAllPersonas() throws Exception {
        Persona persona = new Persona();
        persona.setNombre("Jeisson Junco");
        persona.setGenero("Masculino");
        persona.setEdad(30);
        persona.setIdentificacion("123456787");
        persona.setDireccion("Calle Falsa 123");
        persona.setTelefono("555-1234");
        personaRepository.save(persona);

        mockMvc.perform(get("/personas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nombre").value("Jeisson Junco"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].identificacion").value("123456787"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].direccion").value("Calle Falsa 123"));
    }
}