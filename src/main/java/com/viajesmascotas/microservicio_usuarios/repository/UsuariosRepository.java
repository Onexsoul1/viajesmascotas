package com.viajesmascotas.microservicio_usuarios.repository;

import com.viajesmascotas.microservicio_usuarios.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuariosRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByCorreoAndClave(String correo, String clave);
}
