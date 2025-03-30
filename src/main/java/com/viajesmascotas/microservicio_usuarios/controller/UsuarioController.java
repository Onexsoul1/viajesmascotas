package com.viajesmascotas.microservicio_usuarios.controller;
import com.viajesmascotas.microservicio_usuarios.model.Usuario;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private List<Usuario> usuarios = new ArrayList<>(List.of(
        new Usuario(1, "Ana Pérez", "ana@mail.com", "1234", "dueño de mascota"),
        new Usuario(2, "Carlos Soto", "carlos@mail.com", "abcd", "conductor pet-friendly"),
        new Usuario(3, "Lucía Vega", "lucia@mail.com", "pass", "dueño de mascota")
    ));

    @GetMapping
    public List<Usuario> obtenerTodos() {
        return usuarios;
    }

    @GetMapping("/{id}")
    public Usuario obtenerPorId(@PathVariable int id) {
        return usuarios.stream().filter(u -> u.getId() == id).findFirst().orElse(null);
    }

    @GetMapping("/login")
    public String login(@RequestParam String correo, @RequestParam String clave) {
        return usuarios.stream().anyMatch(u -> u.getCorreo().equals(correo) && u.getClave().equals(clave))
            ? "Login exitoso"
            : "Credenciales inválidas";
    }
}
