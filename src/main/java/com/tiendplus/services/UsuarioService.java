package com.tiendplus.services;

import com.tiendplus.models.Usuario;
import com.tiendplus.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service  // Marca la clase como un servicio de Spring, gestionado por el contenedor de Spring
@Transactional // Asegura que las operaciones en esta clase se realicen dentro de una transacción
public class UsuarioService {

    // Inyección de dependencias del repositorio UsuarioRepository
    private final UsuarioRepository usuarioRepository;

    // Constructor que inyecta el UsuarioRepository
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Método para validar las credenciales de login
    public boolean validarLogin(String nombre, String contraseña) {
        // Busca el usuario por su nombre en la base de datos
        Usuario usuario = usuarioRepository.findByNombre(nombre).orElse(null);
        
        // Si el usuario existe y la contraseña es válida, retorna true
        return usuario != null && usuario.validarContraseña(contraseña);
    }
}
