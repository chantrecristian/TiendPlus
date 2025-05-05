package com.tiendplus.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "proveedores")
public class Proveedor {

    @Id //clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Atributos del proveedor
    private String nombre;
    private String correo;
    private String telefono;
    private String empresa;

    @OneToMany(mappedBy = "proveedor", fetch = FetchType.LAZY) // o EAGER si prefieres
    private List<Producto> productos;

    // Getters y Setters
    public Long getId() { return id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmpresa() { return empresa; }
    public void setEmpresa(String empresa) { this.empresa = empresa; }

    public List<Producto> getProductos() { return productos; }
    public void setProductos(List<Producto> productos) { this.productos = productos; }
}