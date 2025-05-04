package com.tiendplus.services;

import com.tiendplus.models.Usuario;
import com.tiendplus.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
@Transactional // Asegúrate de que esté aquí para manejar las transacciones correctamente.
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public boolean validarLogin(String nombre, String contraseña) {
        Usuario usuario = usuarioRepository.findByNombre(nombre).orElse(null);
        return usuario != null && usuario.validarContraseña(contraseña);
    }
}
