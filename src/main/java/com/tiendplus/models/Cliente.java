package com.tiendplus.models;

import jakarta.persistence.*;

@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(name = "n_documento") // Se asegura de que este campo se mapee correctamente con la BD
    private String identificacion;

    public Cliente() {}

    // Getters y setters

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
        return identificacion;
    }

    public void setIdentificacion(String identificacion) { 
        this.identificacion = identificacion;
    }
}
