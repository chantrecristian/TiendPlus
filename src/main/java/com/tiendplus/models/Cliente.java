package com.tiendplus.models;

import jakarta.persistence.*;

@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(name = "n_documento")
    private String nDocumento;

    public Cliente() {}

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIdentificacion() {
        return nDocumento;
    }

    public void setIdentificacion(String identificacion) {
        this.nDocumento = identificacion;
    }
}
