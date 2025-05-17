package com.tiendplus.repositories;

import com.tiendplus.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para gestionar clientes en la base de datos.
 */
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Cliente findByNDocumento(String nDocumento); 
}