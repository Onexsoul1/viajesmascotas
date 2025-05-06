package com.viajesmascotas.microservicio_usuarios.controller;

import com.viajesmascotas.microservicio_usuarios.model.Usuario;
import com.viajesmascotas.microservicio_usuarios.repository.UsuariosRepository;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuariosRepository usuarioRepository;

    @GetMapping
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable int id) {
        return usuarioRepository.findById(id)
            .map(usuario -> {
                EntityModel<Usuario> model = EntityModel.of(usuario);
                model.add(linkTo(methodOn(UsuarioController.class).obtenerPorId(id)).withSelfRel());
                model.add(linkTo(methodOn(UsuarioController.class).obtenerTodos()).withRel("todos-usuarios"));
                return ResponseEntity.ok(model);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/login")
    public String login(@RequestParam String correo, @RequestParam String clave) {
        return usuarioRepository.findByCorreoAndClave(correo, clave).isPresent()
            ? "Login exitoso"
            : "Credenciales inválidas";
    }

    @PostMapping
    public ResponseEntity<?> crearUsuario(@Valid @RequestBody Usuario usuario, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errores = result.getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
            return ResponseEntity.badRequest().body(errores);
        }

        return ResponseEntity.ok(usuarioRepository.save(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable int id, @Valid @RequestBody Usuario usuarioActualizado, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errores = result.getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
            return ResponseEntity.badRequest().body(errores);
        }
    
        return usuarioRepository.findById(id)
            .map(usuario -> {
                usuario.setNombre(usuarioActualizado.getNombre());
                usuario.setCorreo(usuarioActualizado.getCorreo());
                usuario.setClave(usuarioActualizado.getClave());
                usuario.setRol(usuarioActualizado.getRol());
                return ResponseEntity.ok(usuarioRepository.save(usuario));
            })
            .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable int id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return ResponseEntity.ok("Usuario eliminado correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
    }
}
