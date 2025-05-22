package com.tiendplus.repositories;

import com.tiendplus.models.Cliente;
import com.tiendplus.models.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByClienteAndMetodoPago(Cliente cliente, String metodoPago);
}