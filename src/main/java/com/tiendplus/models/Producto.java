package com.tiendplus.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
<<<<<<< HEAD

@Entity
@Table(name = "inventario")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String descripcion;

    private double precio;

    private Integer cantidad;

    @ManyToOne
    private Proveedor proveedor;

    public Producto() {}

=======
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
>>>>>>> b00e0011af1d2b76535b6abae1a91dbdc0bd4528
    public Producto(Long id, String nombre, String descripcion, Integer precio, Integer cantidad, Proveedor proveedor) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.cantidad = cantidad;
        this.proveedor = proveedor;
    }

<<<<<<< HEAD
=======
    // Getters y Setters

>>>>>>> b00e0011af1d2b76535b6abae1a91dbdc0bd4528
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
