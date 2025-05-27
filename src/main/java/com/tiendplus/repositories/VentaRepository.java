package com.tiendplus.repositories;

import com.tiendplus.models.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    // ✅ Correcto: accede al campo 'documentoCliente' dentro de 'cliente'
    List<Venta> findByCliente_DocumentoCliente(String documentoCliente);

    List<Venta> findByMetodoPago(String metodoPago);

    List<Venta> findByCliente_DocumentoClienteOrderByFechaVentaDesc(String documentoCliente);
}
