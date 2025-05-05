package com.tiendplus.repositories;

import com.tiendplus.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Cliente findByIdentificacion(String identificacion);
}
