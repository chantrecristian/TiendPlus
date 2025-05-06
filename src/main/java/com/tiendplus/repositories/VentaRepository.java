package com.tiendplus.repositories;

import com.tiendplus.models.Venta;
import com.tiendplus.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {

    @Query("SELECT v FROM Venta v WHERE v.cliente.id = :clienteId AND v.metodoPago = :metodoPago")
List<Venta> findByClienteAndMetodoPago(@Param("clienteId") Long clienteId, @Param("metodoPago") String metodoPago);

}
