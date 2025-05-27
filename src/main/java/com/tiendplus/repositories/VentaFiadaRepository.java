package com.tiendplus.repositories;

import com.tiendplus.models.VentaFiada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VentaFiadaRepository extends JpaRepository<VentaFiada, Integer> {

    List<VentaFiada> findAll();
    List<VentaFiada> findByDocumentoCliente(String documentoCliente);
    List<VentaFiada> findByDocumentoClienteOrderByFechaVentaDesc(String documentoCliente);
    List<VentaFiada> findByDocumentoClienteAndMetodoPago(String documentoCliente, String metodoPago);

    // ✅ Agregando el método de validación de existencia
    boolean existsByDocumentoCliente(String documentoCliente); 
}
