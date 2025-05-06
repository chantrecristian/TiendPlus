// Paquete donde se encuentra esta vista
package com.tiendplus.views.admin;

// Importación de clases necesarias para la vista
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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

// Esta clase muestra la página de inventario, accesible desde la ruta "inventario"
@Route(value = "inventario", layout = MainView.class)
@PageTitle("Inventario")
public class InventarioView extends VerticalLayout {

    // Tabla para mostrar la lista de productos
    private Grid<Producto> grid = new Grid<>(Producto.class, false);
    
    // Este campo guarda el producto que se selecciona en la tabla
    private Producto productoSeleccionado = null;

    // Servicio que se encarga de guardar, buscar y eliminar productos
    private final ProductoService productoService;

    // Campos del formulario donde se escribe la información del producto
    private TextField nombre = new TextField("Nombre");
    private TextArea descripcion = new TextArea("Descripción");
    private NumberField precio = new NumberField("Precio");
    private NumberField cantidad = new NumberField("Cantidad");

    // Constructor de la vista
    @Autowired
    public InventarioView(ProductoService productoService) {
        this.productoService = productoService;

        // Ajusta el tamaño de la vista para que ocupe toda la pantalla
        setSizeFull();
        setPadding(true);

        // Botón para volver al menú principal
        Button volverBtn = new Button("Volver al Menú");
        volverBtn.addClickListener(e -> volverBtn.getUI().ifPresent(ui -> ui.navigate("menu-admin")));

        // Configuración de las columnas de la tabla
        grid.addColumn(Producto::getId).setHeader("ID");
        grid.addColumn(Producto::getNombre).setHeader("Nombre");
        grid.addColumn(Producto::getDescripcion).setHeader("Descripción");
        grid.addColumn(Producto::getPrecio).setHeader("Precio");
        grid.addColumn(Producto::getCantidad).setHeader("Cantidad");

        // Carga los productos desde el servicio y los muestra en la tabla
        grid.setItems(productoService.findAll());

        // Escucha cuando el usuario selecciona un producto en la tabla
        grid.asSingleSelect().addValueChangeListener(e -> {
            productoSeleccionado = e.getValue();
            if (productoSeleccionado != null) {
                llenarFormulario(productoSeleccionado); // Rellena el formulario con los datos del producto
            }
        });

        // Crea el formulario con los campos de nombre, descripción, precio y cantidad
        FormLayout formulario = new FormLayout(nombre, descripcion, precio, cantidad);

        // Botones para guardar, eliminar o limpiar el formulario
        Button guardarBtn = new Button("Guardar", e -> guardarProducto());
        Button eliminarBtn = new Button("Eliminar", e -> eliminarProducto());
        Button limpiarBtn = new Button("Limpiar", e -> limpiarFormulario());

        // Organiza los botones horizontalmente
        HorizontalLayout botones = new HorizontalLayout(guardarBtn, eliminarBtn, limpiarBtn);

        // Agrega los componentes a la vista en orden: botón volver, tabla, formulario y botones
        add(volverBtn, grid, formulario, botones);
    }

    // Método para guardar un producto nuevo o actualizar uno existente
    private void guardarProducto() {
        // Verifica que los campos obligatorios no estén vacíos
        if (nombre.isEmpty() || precio.isEmpty() || cantidad.isEmpty()) {
            Notification.show("Completa todos los campos requeridos");
            return;
        }

        // Si no hay un producto seleccionado, se crea uno nuevo
        if (productoSeleccionado == null) {
            Producto nuevo = new Producto();
            nuevo.setNombre(nombre.getValue());
            nuevo.setDescripcion(descripcion.getValue());
            nuevo.setPrecio(precio.getValue());
            nuevo.setCantidad(cantidad.getValue().intValue());
            productoService.save(nuevo); // Se guarda en la base de datos
        } else {
            // Si hay un producto seleccionado, se actualiza con los nuevos valores
            productoSeleccionado.setNombre(nombre.getValue());
            productoSeleccionado.setDescripcion(descripcion.getValue());
            productoSeleccionado.setPrecio(precio.getValue());
            productoSeleccionado.setCantidad(cantidad.getValue().intValue());
            productoService.save(productoSeleccionado); // Se guarda la actualización
        }

        // Se recarga la tabla con todos los productos
        grid.setItems(productoService.findAll());
        limpiarFormulario(); // Limpia el formulario después de guardar
    }

    // Método para eliminar el producto que está seleccionado
    private void eliminarProducto() {
        if (productoSeleccionado != null) {
            productoService.delete(productoSeleccionado); // Lo elimina del sistema
            grid.setItems(productoService.findAll()); // Se actualiza la tabla
            limpiarFormulario(); // Se limpia el formulario
        }
    }

    // Método que limpia todos los campos del formulario y la selección en la tabla
    private void limpiarFormulario() {
        productoSeleccionado = null;
        nombre.clear();
        descripcion.clear();
        precio.clear();
        cantidad.clear();
        grid.deselectAll(); // Deselecciona cualquier fila en la tabla
    }

    // Método que llena el formulario con la información de un producto seleccionado
    private void llenarFormulario(Producto producto) {
        nombre.setValue(producto.getNombre());
        descripcion.setValue(producto.getDescripcion());
        precio.setValue(producto.getPrecio());
        cantidad.setValue((double) producto.getCantidad());
    }
}
