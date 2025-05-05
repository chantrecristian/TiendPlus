package com.tiendplus.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

// La clase Producto está mapeada a la tabla "inventario" en la base de datos.
@Entity // Marca esta clase como una entidad JPA para que se mapee a una tabla en la base de datos
@Table(name = "inventario") // Define el nombre de la tabla en la base de datos, que será "inventario"
public class Producto {
    
    // La propiedad 'id' es la clave primaria de la entidad
    @Id // Marca el campo como la clave primaria de la tabla
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Establece que el ID será generado automáticamente y se utilizará una estrategia de identidad (auto-incremento)
    private Long id;
    
    private String nombre; // Nombre del producto
    private String descripcion; // Descripción del producto
    private double precio; // Precio del producto
    private int cantidad; // Cantidad disponible en el inventario

    // Constructores
    public Producto() {} // Constructor vacío (sin parámetros), necesario para JPA
    public Producto(Long id, String nombre, String descripcion, double precio, int cantidad) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    // Getters y Setters para los atributos

    public Long getId() { return id; } // Retorna el ID del producto
    public void setId(Long id) { this.id = id; } // Establece el ID del producto

    public String getNombre() { return nombre; } // Retorna el nombre del producto
    public void setNombre(String nombre) { this.nombre = nombre; } // Establece el nombre del producto

    public String getDescripcion() { return descripcion; } // Retorna la descripción del producto
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; } // Establece la descripción del producto

    public double getPrecio() { return precio; } // Retorna el precio del producto
    public void setPrecio(double precio) { this.precio = precio; } // Establece el precio del producto

    public int getCantidad() { return cantidad; } // Retorna la cantidad del producto en inventario
    public void setCantidad(int cantidad) { this.cantidad = cantidad; } // Establece la cantidad del producto en inventario
}
