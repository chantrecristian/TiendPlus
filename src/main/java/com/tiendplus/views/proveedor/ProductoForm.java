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

public class ProductoForm extends FormLayout{//Formulario visual para crear, editar o eliminar un producto.
    private final ComboBox<Proveedor> proveedorComboBox = new ComboBox<>("Proveedor");// ComboBox para seleccionar proveedor
    // Campos del producto
    private final TextField nombre = new TextField("Nombre del producto");
    private final TextField descripcion = new TextField("Descripcion");
    private IntegerField precio = new IntegerField("Precio");
    private IntegerField cantidad = new IntegerField("Cantidad");


    private final Button guardar = new Button("Guardar");
    private final Button cancelar = new Button("Cancelar");
    private final Button eliminar = new Button("Eliminar");

    private Producto producto;

    private Consumer<Producto> onSave;
    private Consumer<Producto> onDelete;

    public ProductoForm() {
        // Configurar layout
        HorizontalLayout botones = new HorizontalLayout(guardar, cancelar, eliminar);
        add(proveedorComboBox, nombre, descripcion, precio, cantidad, botones);
        setWidth("400px"); // ancho visible   

        // Listeners de los botones
        guardar.addClickListener(e -> guardar());
        cancelar.addClickListener(e -> setProducto(null)); // Ocultar el formulario
        eliminar.addClickListener(e -> eliminar());
    }
    //proveedores disponibles en el ComboBox.
    public void setProveedores(List<Proveedor> proveedores) {
        proveedorComboBox.setItems(proveedores);
        proveedorComboBox.setItemLabelGenerator(Proveedor::getNombre); // Muestra nombre
    }     
    //Establece el producto a editar o crea uno nuevo, También llena los campos con los datos del producto.
    public void setProducto(Producto producto) {
        this.producto = producto;
        if (producto != null) {
            // Carga los valores del producto en el formulario
            nombre.setValue(producto.getNombre() != null ? producto.getNombre() : "");
            descripcion.setValue(producto.getDescripcion() != null ? producto.getDescripcion() : "");
            precio.setValue(producto.getPrecio() != null ? producto.getPrecio() : 0);
            cantidad.setValue(producto.getCantidad() != null ? producto.getCantidad() : 0);
            proveedorComboBox.setValue(producto.getProveedor());
    
            setVisible(true);//muestra el formulario
        } else {
            setVisible(false);//oculta el formulario
        }
    }    
    //metodo al presionar guardar y actualizar
    private void guardar() {
        if (producto == null) return;
    
        if (proveedorComboBox.getValue() == null) {
            Notification.show("Debe seleccionar un proveedor");
            return;
        }
        // Actualiza el objeto producto con los valores del formulario
        producto.setNombre(nombre.getValue());
        producto.setDescripcion(descripcion.getValue());
        producto.setPrecio(precio.getValue());
        producto.setCantidad(cantidad.getValue());
        producto.setProveedor(proveedorComboBox.getValue());
        // Llama al guardado
        if (onSave != null) {
            onSave.accept(producto);
        }
    
        setProducto(null);//Limpia y oculta el formulario
        Notification.show("Producto guardado");
    }   
    //metodo al presionar eliminar 
    private void eliminar() {
        if (producto != null && onDelete != null) {
            onDelete.accept(producto);
        }
    }
    // Setter para el callback que se ejecuta al guardar un producto
    public void setOnSave(Consumer<Producto> handler) {
        this.onSave = handler;
    }
    // Setter para el callback que se ejecuta al eliminar un producto
    public void setOnDelete(Consumer<Producto> handler) {
        this.onDelete = handler;
    }
}