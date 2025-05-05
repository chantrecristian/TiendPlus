package com.tiendplus.views.proveedor;

import java.util.function.Consumer;

import com.tiendplus.models.Proveedor;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class ProveedorForm extends FormLayout {
    // Campos del formulario
    private final TextField nombre = new TextField("Nombre");
    private final TextField correo = new TextField("Correo");
    private final TextField telefono = new TextField("Teléfono");
    private final TextField empresa = new TextField("Empresa");

    // Botones
    private final Button guardar = new Button("Guardar");
    private final Button cancelar = new Button("Cancelar");
    private final Button eliminar = new Button("Eliminar");
    // Objeto proveedor que se está editando o creando
    private Proveedor proveedor;

    // Evento que se ejecuta al guardar o eliminar
    private Consumer<Proveedor> onSave;
    private Consumer<Proveedor> onDelete;

    // Constructor del formulario
    public ProveedorForm() {
        HorizontalLayout botones = new HorizontalLayout(guardar, cancelar, eliminar);// agrupacion de botones
        add(nombre, correo, telefono, empresa, botones);// se añaden al formulario
    
        // Asignamos las acciones a los botones
        guardar.addClickListener(e -> guardar());
        cancelar.addClickListener(e -> setProveedor(null));// Cancela y oculta el formulario
        eliminar.addClickListener(e -> eliminar());
    
        setVisible(false); // Ocultar por defecto
    }
    //Método para asignar un proveedor para editar
    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
        if (proveedor != null) {
            // Rellena los campos con los datos del proveedor, o vacío si hay nulls
            nombre.setValue(proveedor.getNombre() != null ? proveedor.getNombre() : "");
            correo.setValue(proveedor.getCorreo() != null ? proveedor.getCorreo() : "");
            telefono.setValue(proveedor.getTelefono() != null ? proveedor.getTelefono() : "");
            empresa.setValue(proveedor.getEmpresa() != null ? proveedor.getEmpresa() : "");
            setVisible(true);
        } else {
            setVisible(false);
        }
    }

    //Método para asignar el evento de guardado
    public void setOnSave(Consumer<Proveedor> onSave) {
        this.onSave = onSave;
    }
    /**
     * Lógica cuando se presiona el botón "Guardar":
     * Actualiza los datos del proveedor con lo escrito en los campos
     * y ejecuta la acción onSave si está definida.
     */
    private void guardar() {
        if (proveedor != null) {
            proveedor.setNombre(nombre.getValue());
            proveedor.setCorreo(correo.getValue());
            proveedor.setTelefono(telefono.getValue());
            proveedor.setEmpresa(empresa.getValue());
    
            if (onSave != null) {
                onSave.accept(proveedor);
            }
        }
    }
    //al presionar eliminar
    private void eliminar() {
        if (proveedor != null && onDelete != null) {
            onDelete.accept(proveedor);
        }
    }    
    //comportamiento eliminar
    public void setOnDelete(Consumer<Proveedor> handler) {
        this.onDelete = handler;
    }
}
