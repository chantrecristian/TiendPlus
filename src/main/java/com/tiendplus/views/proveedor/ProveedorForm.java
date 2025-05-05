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

    
    // Proveedor actual que se está editando (puede ser nuevo o existente)
    private Proveedor proveedor;

    // Evento que se ejecuta al guardar (lo asignaremos desde la vista)
    private Consumer<Proveedor> onSave;
    private Consumer<Proveedor> onDelete;

    public ProveedorForm() {
        HorizontalLayout botones = new HorizontalLayout(guardar, cancelar, eliminar);
        add(nombre, correo, telefono, empresa, botones);
    
        guardar.addClickListener(e -> guardar());
        cancelar.addClickListener(e -> setProveedor(null));
        eliminar.addClickListener(e -> eliminar());
    
        setVisible(false); // Ocultar por defecto
    }

    /**
     * Método para asignar un proveedor al formulario (usado para edición)
     */
    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
        if (proveedor != null) {
            nombre.setValue(proveedor.getNombre() != null ? proveedor.getNombre() : "");
            correo.setValue(proveedor.getCorreo() != null ? proveedor.getCorreo() : "");
            telefono.setValue(proveedor.getTelefono() != null ? proveedor.getTelefono() : "");
            empresa.setValue(proveedor.getEmpresa() != null ? proveedor.getEmpresa() : "");
            setVisible(true);
        } else {
            setVisible(false);
        }
    }

    /**
     * Método para asignar el evento de guardado (se usará desde la vista)
     */
    public void setOnSave(Consumer<Proveedor> onSave) {
        this.onSave = onSave;
    }

    /**
     * Lógica cuando se presiona el botón Guardar
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
    private void eliminar() {
        if (proveedor != null && onDelete != null) {
            onDelete.accept(proveedor);
        }
    }    
    
    public void setOnDelete(Consumer<Proveedor> handler) {
        this.onDelete = handler;
    }
}
