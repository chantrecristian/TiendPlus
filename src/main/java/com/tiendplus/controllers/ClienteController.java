package com.tiendplus.controllers;

import com.tiendplus.models.Cliente;
import com.tiendplus.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    // 📝 Registrar cliente con validación
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarCliente(@RequestBody Cliente cliente) {
        if (cliente.getDocumentoCliente() == null || cliente.getDocumentoCliente().isBlank()) {
            return ResponseEntity.badRequest().body("❌ Error: El documento del cliente no puede estar vacío.");
        }
        Cliente nuevoCliente = clienteRepository.save(cliente);
        return ResponseEntity.ok(nuevoCliente);
    }

    // 🔍 Buscar cliente por documento con manejo de errores
    @GetMapping("/buscar/{documentoCliente}")
    public ResponseEntity<?> buscarCliente(@PathVariable String documentoCliente) {
        Optional<Cliente> clienteEncontrado = Optional.ofNullable(clienteRepository.findByDocumentoCliente(documentoCliente));
       if (clienteEncontrado.isPresent()) {
    return ResponseEntity.ok(clienteEncontrado.get());
    } else {
        return ResponseEntity.badRequest().body("⚠️ Cliente no encontrado con documento: " + documentoCliente);
    }
    }
}
