package com.tiendplus.views.proveedor;

import java.util.function.Consumer;

import com.tiendplus.models.Producto;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;

import com.vaadin.flow.component.combobox.ComboBox;
import com.tiendplus.models.Proveedor;
import java.util.List;

public class ProductoForm extends FormLayout {
    private final ComboBox<Proveedor> proveedorComboBox = new ComboBox<>("Proveedor");
    private final TextField nombre = new TextField("Nombre del producto");
    private final TextField descripcion = new TextField("Descripcion");
    private TextField precio = new TextField("Precio");
    private IntegerField cantidad = new IntegerField("Cantidad");

    private final Button guardar = new Button("Guardar");
    private final Button cancelar = new Button("Cancelar");
    private final Button eliminar = new Button("Eliminar");

    private Producto producto;

    private Consumer<Producto> onSave;
    private Consumer<Producto> onDelete;

    public ProductoForm() {
        HorizontalLayout botones = new HorizontalLayout(guardar, cancelar, eliminar);
        add(proveedorComboBox, nombre, descripcion, precio, cantidad, botones);
        setWidth("400px");

        guardar.addClickListener(e -> guardar());
        cancelar.addClickListener(e -> setProducto(null));
        eliminar.addClickListener(e -> eliminar());
    }

    public void setProveedores(List<Proveedor> proveedores) {
        proveedorComboBox.setItems(proveedores);
        proveedorComboBox.setItemLabelGenerator(Proveedor::getNombre);
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
        if (producto != null) {
            nombre.setValue(producto.getNombre() != null ? producto.getNombre() : "");
            descripcion.setValue(producto.getDescripcion() != null ? producto.getDescripcion() : "");
            precio.setValue(producto.getPrecio() != 0.0 ? String.valueOf(producto.getPrecio()) : "0");
            cantidad.setValue(producto.getCantidad() != null ? producto.getCantidad() : 0);
            proveedorComboBox.setValue(producto.getProveedor());
            setVisible(true);
        } else {
            setVisible(false);
        }
    }

    private void guardar() {
        if (producto == null) return;

        if (proveedorComboBox.getValue() == null) {
            Notification.show("Debe seleccionar un proveedor");
            getElement().executeJs("console.error('Error: No se seleccionó proveedor')");
            return;
        }

        try {
            producto.setNombre(nombre.getValue());
            producto.setDescripcion(descripcion.getValue());
            producto.setPrecio(Double.parseDouble(precio.getValue()));
            producto.setCantidad(cantidad.getValue());
            producto.setProveedor(proveedorComboBox.getValue());

            if (onSave != null) {
                onSave.accept(producto);
            }

            Notification.show("Producto guardado");
            getElement().executeJs("console.log('Producto guardado: ' + $0)", producto.getNombre());
            setProducto(null);
        } catch (NumberFormatException ex) {
            Notification.show("El precio debe ser un número válido");
            getElement().executeJs("console.error('Error: Precio inválido')");
        }
    }

    private void eliminar() {
        if (producto != null && onDelete != null) {
            onDelete.accept(producto);
            Notification.show("Producto eliminado");
            getElement().executeJs("console.log('Producto eliminado: ' + $0)", producto.getNombre());
            setProducto(null);
        }
    }

    public void setOnSave(Consumer<Producto> handler) {
        this.onSave = handler;
    }

    public void setOnDelete(Consumer<Producto> handler) {
        this.onDelete = handler;
    }
}
