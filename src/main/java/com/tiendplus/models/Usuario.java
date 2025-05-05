package com.tiendplus.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// La clase Usuario está mapeada a una tabla "usuarios" en la base de datos.
@Table(name = "usuarios") // Define el nombre de la tabla en la base de datos, que será "usuarios"
@Entity // Marca esta clase como una entidad JPA para que se mapee a una tabla en la base de datos
public class Usuario {

    // El ID es la clave primaria de la entidad
    @Id
    @GeneratedValue // Esto genera el ID automáticamente (puedes ajustar el tipo de generación según lo necesites)
    private Long id;
    
    private String nombre; // Nombre del usuario
    private String correo; // Correo electrónico del usuario
    private String contraseña; // Contraseña del usuario
    private String rol; // Rol del usuario (por ejemplo: "admin", "cajero", etc.)

    // Getters y setters para los atributos

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

    // Método para validar la contraseña
    public boolean validarContraseña(String contraseña) {
        return this.contraseña.equals(contraseña); // Compara la contraseña proporcionada con la almacenada en la base de datos
    }
}
