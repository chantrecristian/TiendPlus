package com.tiendplus.services;

import com.tiendplus.models.Producto;
import com.tiendplus.repositories.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository repository;

    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }

    public List<Producto> findAll() {
        return repository.findAll();
    }

    public Producto save(Producto producto) {
        return repository.save(producto);
    }

    public void delete(Producto producto) {
        repository.delete(producto);
    }
}
