package com.tiendplus.controllers;

import com.tiendplus.models.Cliente;
import com.tiendplus.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    // 📝 Registrar cliente
    @PostMapping("/registrar")
    public Cliente registrarCliente(@RequestBody Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    // 🔍 Buscar cliente por documento
    @GetMapping("/buscar/{documento}")
    public Cliente buscarCliente(@PathVariable String documento) {
        return clienteRepository.findByIdentificacion(documento);
    }
}
