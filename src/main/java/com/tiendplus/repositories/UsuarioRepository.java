package com.tiendplus.repositories;

import com.tiendplus.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByNombreAndContraseña(String nombre, String contraseña);
}