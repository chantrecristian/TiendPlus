package com.tiendplus.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tiendplus.models.Proveedor;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long>{
    @EntityGraph(attributePaths = "productos")
    Optional<Proveedor> findById(Long id);
}