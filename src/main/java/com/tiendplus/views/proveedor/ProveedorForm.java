package com.tiendplus.views.proveedor;

import java.util.function.Consumer;

import com.tiendplus.models.Proveedor;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class ProveedorForm extends FormLayout {
    private final TextField nombre = new TextField("Nombre");
    private final TextField correo = new TextField("Correo");
    private final TextField telefono = new TextField("Teléfono");
    private final TextField empresa = new TextField("Empresa");

    private final Button guardar = new Button("Guardar");
    private final Button cancelar = new Button("Cancelar");
    private final Button eliminar = new Button("Eliminar");

    private Proveedor proveedor;

    private Consumer<Proveedor> onSave;
    private Consumer<Proveedor> onDelete;

    public ProveedorForm() {
        HorizontalLayout botones = new HorizontalLayout(guardar, cancelar, eliminar);
        add(nombre, correo, telefono, empresa, botones);

        guardar.addClickListener(e -> guardar());
        cancelar.addClickListener(e -> {
            setProveedor(null);
            getElement().executeJs("console.log('Edición cancelada')");
            Notification.show("Edición cancelada");
        });
        eliminar.addClickListener(e -> eliminar());

        setVisible(false);
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
        if (proveedor != null) {
            nombre.setValue(proveedor.getNombre() != null ? proveedor.getNombre() : "");
            correo.setValue(proveedor.getCorreo() != null ? proveedor.getCorreo() : "");
            telefono.setValue(proveedor.getTelefono() != null ? proveedor.getTelefono() : "");
            empresa.setValue(proveedor.getEmpresa() != null ? proveedor.getEmpresa() : "");
            setVisible(true);
            getElement().executeJs("console.log('Formulario cargado para proveedor: ' + $0)", proveedor.getNombre());
        } else {
            setVisible(false);
            getElement().executeJs("console.log('Formulario ocultado')");
        }
    }

    public void setOnSave(Consumer<Proveedor> onSave) {
        this.onSave = onSave;
    }

    private void guardar() {
        if (proveedor != null) {
            proveedor.setNombre(nombre.getValue());
            proveedor.setCorreo(correo.getValue());
            proveedor.setTelefono(telefono.getValue());
            proveedor.setEmpresa(empresa.getValue());

            getElement().executeJs("console.log('Guardando proveedor: ' + $0)", proveedor.getNombre());
            Notification.show("Guardando proveedor: " + proveedor.getNombre());

            if (onSave != null) {
                onSave.accept(proveedor);
            }
        }
    }

    private void eliminar() {
        if (proveedor != null && onDelete != null) {
            getElement().executeJs("console.log('Eliminando proveedor: ' + $0)", proveedor.getNombre());
            Notification.show("Eliminando proveedor: " + proveedor.getNombre());
            onDelete.accept(proveedor);
        }
    }

    public void setOnDelete(Consumer<Proveedor> handler) {
        this.onDelete = handler;
    }
}
