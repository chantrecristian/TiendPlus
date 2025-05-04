package com.tiendplus.services;

import com.tiendplus.models.Producto;
import com.tiendplus.repositories.ProductoRepository;

import java.util.List;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
@Transactional // Asegúrate de que esté aquí
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public void save(Producto producto) {
        productoRepository.save(producto);
    }

    public void delete(Producto producto) {
        productoRepository.delete(producto);
    }

    public List<Producto> findAll() {
        return productoRepository.findAll();
    }
}
