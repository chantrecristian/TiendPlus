package com.tiendplus.views.admin;

import com.tiendplus.models.Producto;
import com.tiendplus.services.ProductoService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("inventario")
public class InventarioView extends VerticalLayout {

    private Grid<Producto> grid = new Grid<>(Producto.class, false);
    private Producto productoSeleccionado = null;

    private final ProductoService productoService;

    // Campos del formulario
    private TextField nombre = new TextField("Nombre");
    private TextArea descripcion = new TextArea("Descripción");
    private NumberField precio = new NumberField("Precio");
    private NumberField cantidad = new NumberField("Cantidad");

    @Autowired
    public InventarioView(ProductoService productoService) {
        this.productoService = productoService;

        setSizeFull();
        setPadding(true);

        Button volverBtn = new Button("Volver al Menú");
        volverBtn.addClickListener(e -> volverBtn.getUI().ifPresent(ui -> ui.navigate("menu-admin")));

        // Configurar Grid
        grid.addColumn(Producto::getId).setHeader("ID");
        grid.addColumn(Producto::getNombre).setHeader("Nombre");
        grid.addColumn(Producto::getDescripcion).setHeader("Descripción");
        grid.addColumn(Producto::getPrecio).setHeader("Precio");
        grid.addColumn(Producto::getCantidad).setHeader("Cantidad");

        grid.setItems(productoService.findAll()); // Cargar productos desde el servicio
        grid.asSingleSelect().addValueChangeListener(e -> {
            productoSeleccionado = e.getValue();
            if (productoSeleccionado != null) {
                llenarFormulario(productoSeleccionado);
            }
        });

        FormLayout formulario = new FormLayout(nombre, descripcion, precio, cantidad);
        Button guardarBtn = new Button("Guardar", e -> guardarProducto());
        Button eliminarBtn = new Button("Eliminar", e -> eliminarProducto());
        Button limpiarBtn = new Button("Limpiar", e -> limpiarFormulario());
        HorizontalLayout botones = new HorizontalLayout(guardarBtn, eliminarBtn, limpiarBtn);

        add(volverBtn, grid, formulario, botones);
    }

    private void guardarProducto() {
        if (nombre.isEmpty() || precio.isEmpty() || cantidad.isEmpty()) {
            Notification.show("Completa todos los campos requeridos");
            return;
        }

        if (productoSeleccionado == null) {
            Producto nuevo = new Producto();
            nuevo.setNombre(nombre.getValue());
            nuevo.setDescripcion(descripcion.getValue());
            nuevo.setPrecio(precio.getValue());
            nuevo.setCantidad(cantidad.getValue().intValue());
            productoService.save(nuevo);
        } else {
            productoSeleccionado.setNombre(nombre.getValue());
            productoSeleccionado.setDescripcion(descripcion.getValue());
            productoSeleccionado.setPrecio(precio.getValue());
            productoSeleccionado.setCantidad(cantidad.getValue().intValue());
            productoService.save(productoSeleccionado);
        }

        grid.setItems(productoService.findAll()); // Refrescar
        limpiarFormulario();
    }

    private void eliminarProducto() {
        if (productoSeleccionado != null) {
            productoService.delete(productoSeleccionado);
            grid.setItems(productoService.findAll()); // Refrescar
            limpiarFormulario();
        }
    }

    private void limpiarFormulario() {
        productoSeleccionado = null;
        nombre.clear();
        descripcion.clear();
        precio.clear();
        cantidad.clear();
        grid.deselectAll();
    }

    private void llenarFormulario(Producto producto) {
        nombre.setValue(producto.getNombre());
        descripcion.setValue(producto.getDescripcion());
        precio.setValue(producto.getPrecio());
        cantidad.setValue((double) producto.getCantidad());
    }
}
