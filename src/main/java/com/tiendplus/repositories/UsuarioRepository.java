package com.tiendplus.repositories;

import com.tiendplus.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// Interfaz que extiende de JpaRepository para realizar operaciones CRUD sobre la entidad Usuario
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Método para buscar un usuario por su nombre de usuario.
    // El retorno es un Optional porque el usuario podría no existir.
    Optional<Usuario> findByNombre(String nombre);

    // Método para buscar un usuario por su correo electrónico.
    // El retorno es un Optional porque el usuario podría no existir.
    Optional<Usuario> findByCorreo(String correo);
}
