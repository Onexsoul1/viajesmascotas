package com.viajesmascotas.microservicio_usuarios;


import com.viajesmascotas.microservicio_usuarios.controller.UsuarioController;
import com.viajesmascotas.microservicio_usuarios.model.Usuario;
import com.viajesmascotas.microservicio_usuarios.repository.UsuariosRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuariosRepository usuariosRepository;

    @Test
    void deberiaRetornarUsuarioPorId() throws Exception {
        Usuario usuario = new Usuario(1, "Juan", "juan@correo.com", "1234", "dueño");

        Mockito.when(usuariosRepository.findById(1)).thenReturn(Optional.of(usuario));

        mockMvc.perform(get("/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    @Test
    void deberiaRetornarLoginExitoso() throws Exception {
        Mockito.when(usuariosRepository.findByCorreoAndClave("juan@correo.com", "1234"))
                .thenReturn(Optional.of(new Usuario()));

        mockMvc.perform(get("/usuarios/login")
                .param("correo", "juan@correo.com")
                .param("clave", "1234"))
                .andExpect(status().isOk())
                .andExpect(content().string("Login exitoso"));
    }
    @Test
    void deberiaRetornarNotFoundSiUsuarioNoExiste() throws Exception {
        Mockito.when(usuariosRepository.findById(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/usuarios/999"))
                .andExpect(status().isNotFound());
    }
    @Test
    void deberiaRetornarUnauthorizedSiLoginFalla() throws Exception {
        Mockito.when(usuariosRepository.findByCorreoAndClave("falso@correo.com", "wrong"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/usuarios/login")
                .param("correo", "falso@correo.com")
                .param("clave", "wrong"))
                .andExpect(status().isUnauthorized());
    }


}
