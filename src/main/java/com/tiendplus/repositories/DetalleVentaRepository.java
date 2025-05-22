package com.tiendplus.repositories;

import com.tiendplus.models.DetalleVenta;
import com.tiendplus.models.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {
    List<DetalleVenta> findByVenta(Venta venta);
}