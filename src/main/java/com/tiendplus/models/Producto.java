package com.tiendplus.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
// Indica que esta clase es una entidad JPA, es decir, se mapeará a una tabla en la base de datos
@Entity

// Define el nombre de la tabla en la base de datos como "inventario"
@Table(name = "productos")
public class Producto {

    @Id // clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //campos
    private String nombre;

    // Descripción del producto
    private String descripcion;

    // Precio del producto
    private double precio;

    // Cantidad disponible del producto en inventario
    private Integer cantidad;

    // Relación muchos-a-uno con la entidad Proveedor (varios productos pueden tener el mismo proveedor)
    @ManyToOne
    private Proveedor proveedor;

    // Constructor
    public Producto() {}

    // Constructor con parametros
    public Producto(Long id, String nombre, String descripcion, Integer precio, Integer cantidad, Proveedor proveedor) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.cantidad = cantidad;
        this.proveedor = proveedor;
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }
}
