package com.tiendplus.models;

import jakarta.persistence.*;

@Entity
@Table(name = "cliente") // 🔧 tabla en singular
public class Cliente {

    @Id
    @Column(name = "n_documento", nullable = false)
    private String documentoCliente;  // 🔑 clave primaria

    @Column(name = "nombre")
    private String nombre;

    public Cliente() {}

    // Getters y Setters
    public String getDocumentoCliente() {
        return documentoCliente;
    }

    public void setDocumentoCliente(String documentoCliente) {
        this.documentoCliente = documentoCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
