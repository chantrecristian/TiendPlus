package com.tiendplus.repositories;
import com.tiendplus.models.Producto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByProveedorId(Long proveedorId);
}
