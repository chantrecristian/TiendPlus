package com.tiendplus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tiendplus.models.Usuario;
import com.tiendplus.repositories.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Valida el login del usuario buscando por nombre y contraseña.
     * Si las credenciales son correctas, retorna el objeto Usuario (incluye su rol).
     * Si no, retorna null.
     */
    public Usuario validarLogin(String nombre, String contraseña) {
        return usuarioRepository.findByNombreAndContraseña(nombre, contraseña);
    }
}
