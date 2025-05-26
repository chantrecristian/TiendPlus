package com.tiendplus.repositories;

import com.tiendplus.models.VentaFiada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VentaFiadaRepository extends JpaRepository<VentaFiada, Integer> {  
    // Método para obtener todas las ventas fiadas
    List<VentaFiada> findAll();

    // Método para buscar ventas fiadas por ID de cliente
    List<VentaFiada> findByIdCliente(Long idCliente);
    
    // Método para buscar ventas fiadas de un cliente ordenadas por fecha
    List<VentaFiada> findByIdClienteOrderByFechaVentaDesc(Integer idCliente);
}
