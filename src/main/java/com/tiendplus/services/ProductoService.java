package com.tiendplus.services;

import com.tiendplus.models.Producto;
import com.tiendplus.repositories.ProductoRepository;

import java.util.List;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service  // Indica que esta clase es un servicio de Spring, gestionado por el contenedor de Spring
@Transactional // Asegura que las operaciones en esta clase se gestionan como transacciones
public class ProductoService {

    // Inyección de dependencias del repositorio ProductoRepository
    private final ProductoRepository productoRepository;

    // Constructor que inyecta el ProductoRepository
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // Método para guardar un producto en la base de datos
    public void save(Producto producto) {
        productoRepository.save(producto);  // Llama al repositorio para guardar el producto
    }

    // Método para eliminar un producto de la base de datos
    public void delete(Producto producto) {
        productoRepository.delete(producto);  // Llama al repositorio para eliminar el producto
    }

    // Método para obtener todos los productos desde la base de datos
    public List<Producto> findAll() {
        return productoRepository.findAll();  // Llama al repositorio para obtener todos los productos
    }
}
