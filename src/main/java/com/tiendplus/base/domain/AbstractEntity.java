package com.tiendplus.base.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.MappedSuperclass;
import org.jspecify.annotations.Nullable;
import org.springframework.data.util.ProxyUtils;

// La clase AbstractEntity es una clase base abstracta que puede ser heredada por otras entidades JPA.
// Se usa como base para otras clases que necesitan tener un identificador (ID) común.
@MappedSuperclass // Indica que esta clase no será una entidad en sí misma, sino una clase base que será heredada por otras entidades.
public abstract class AbstractEntity<ID> {

    // El método getId() debe ser implementado por las clases que hereden de AbstractEntity.
    // El @JsonIgnore indica que este método no debe ser serializado en JSON.
    @JsonIgnore
    public abstract @Nullable ID getId(); // Método abstracto para obtener el ID de la entidad.

    // Método para representar la entidad como una cadena, incluyendo su nombre de clase y el ID.
    @Override
    public String toString() {
        // La cadena de formato devuelve el nombre de la clase y el ID de la entidad.
        return "%s{id=%s}".formatted(getClass().getSimpleName(), getId());
    }

    // Método para calcular el hashCode de la entidad.
    @Override
    public int hashCode() {
        // El hashCode no debe cambiar durante la vida del objeto.
        // En lugar de usar getId(), que podría cambiar, se utiliza ProxyUtils para obtener el hashCode de la clase.
        return ProxyUtils.getUserClass(getClass()).hashCode();
    }

    // Método para comparar dos entidades y verificar si son iguales.
    @Override
    public boolean equals(Object obj) {
        if (obj == null) { // Si el objeto es nulo, no es igual.
            return false;
        } else if (obj == this) { // Si el objeto es el mismo, son iguales.
            return true;
        }

        // Comparamos las clases de las entidades (ignorando la posible clase proxy de JPA).
        var thisUserClass = ProxyUtils.getUserClass(getClass());
        var otherUserClass = ProxyUtils.getUserClass(obj);
        if (thisUserClass != otherUserClass) { // Si las clases son diferentes, no son iguales.
            return false;
        }

        // Comparamos los ID de las dos entidades, si el ID es nulo, no son iguales.
        var id = getId();
        return id != null && id.equals(((AbstractEntity<?>) obj).getId());
    }
}
