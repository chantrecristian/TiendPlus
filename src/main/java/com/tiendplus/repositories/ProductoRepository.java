package com.tiendplus.repositories;
import com.tiendplus.models.Producto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

// Interfaz que extiende de JpaRepository para realizar operaciones CRUD sobre la entidad Producto
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // JpaRepository proporciona métodos listos para usar como save(), findAll(), findById(), delete(), etc.
    List<Producto> findByProveedorId(Long proveedorId);

}
