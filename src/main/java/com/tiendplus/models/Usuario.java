package com.tiendplus.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "usuarios") // Asegúrate de que la tabla se llame "usuarios" en la base de datos
@Entity
public class Usuario {

    @Id
    @GeneratedValue // Esto genera el ID automáticamente (puedes ajustar el tipo de generación según lo necesites)
    private Long id;
    private String nombre;
    private String correo;
    private String contraseña;
    private String rol;

    // Getters y setters

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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    // Lógica de validación de contraseña
    public boolean validarContraseña(String contraseña) {
        return this.contraseña.equals(contraseña); // Compara la contraseña con la almacenada en la base de datos
    }
}
