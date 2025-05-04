package com.tiendplus.repositories;

import com.tiendplus.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Buscar usuario por nombre de usuario
    Optional<Usuario> findByNombre(String nombre);

    // Buscar usuario por correo
    Optional<Usuario> findByCorreo(String correo);
}
