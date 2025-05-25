package com.tiendplus.views.admin;

import com.tiendplus.models.Producto;
import com.tiendplus.repositories.ProductoRepository;
import com.tiendplus.repositories.ProveedorRepository;
import com.tiendplus.alertas.LoggerUI;
import com.tiendplus.views.proveedor.ProductoForm;
import com.tiendplus.views.proveedor.grids.ProductoGrid;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "inventario", layout = MainView.class)
@PageTitle("Inventario")
public class InventarioView extends VerticalLayout {

    private final ProductoRepository productoRepository;
    private final ProveedorRepository proveedorRepository;

    private final ProductoGrid productoGrid = new ProductoGrid();
    private final ProductoForm productoForm = new ProductoForm();

    @Autowired
    public InventarioView(ProductoRepository productoRepository, ProveedorRepository proveedorRepository) {
        this.productoRepository = productoRepository;
        this.proveedorRepository = proveedorRepository;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        Button volverBtn = new Button("Volver al Menú", e -> {
            LoggerUI.logInfo("Navegando al menú de administrador.");
            getUI().ifPresent(ui -> ui.navigate("menu-admin"));
        });

        // Botón para crear nuevo producto
        Button nuevoProducto = new Button("Nuevo Producto", e -> {
            try {
                LoggerUI.logInfo("Creando nuevo producto.");
                Producto nuevo = new Producto();
                var proveedores = proveedorRepository.findAll();
                if (proveedores.isEmpty()) {
                    LoggerUI.logError("No hay proveedores disponibles para asignar al nuevo producto.");
                }
                productoForm.setProveedores(proveedores);
                productoForm.setProducto(nuevo);
                productoForm.setVisible(true);
            } catch (Exception ex) {
                LoggerUI.logError("Error al preparar el formulario de nuevo producto: " + ex.getMessage());
            }
        });

        // Listeners del formulario
        productoForm.setOnSave(producto -> {
            try {
                productoRepository.save(producto);
                LoggerUI.logInfo("Producto guardado correctamente: " + producto.getNombre());
                actualizarProductos();
                productoForm.setVisible(false);
            } catch (Exception ex) {
                LoggerUI.logError("Error al guardar producto: " + ex.getMessage());
            }
        });

        productoForm.setOnDelete(producto -> {
            try {
                productoRepository.delete(producto);
                LoggerUI.logInfo("Producto eliminado correctamente: " + producto.getNombre());
                actualizarProductos();
                productoForm.setVisible(false);
            } catch (Exception ex) {
                LoggerUI.logError("Error al eliminar producto: " + ex.getMessage());
            }
        });

        productoGrid.getGrid().asSingleSelect().addValueChangeListener(event -> {
            Producto seleccionado = event.getValue();
            if (seleccionado != null) {
                try {
                    LoggerUI.logInfo("Producto seleccionado: " + seleccionado.getNombre());
                    var proveedores = proveedorRepository.findAll();
                    if (proveedores.isEmpty()) {
                        LoggerUI.logError("No hay proveedores disponibles al seleccionar un producto.");
                    }
                    productoForm.setProveedores(proveedores);
                    productoForm.setProducto(seleccionado);
                    productoForm.setVisible(true);
                } catch (Exception ex) {
                    LoggerUI.logError("Error al seleccionar producto: " + ex.getMessage());
                }
            } else {
                productoForm.setVisible(false);
                LoggerUI.logInfo("Selección de producto cancelada o nula.");
            }
        });

        // Layout de tabla y formulario
        HorizontalLayout contenido = new HorizontalLayout(productoGrid, productoForm);
        contenido.setWidthFull();
        contenido.setFlexGrow(1, productoGrid);
        contenido.setFlexGrow(1, productoForm);

        add(new HorizontalLayout(volverBtn, nuevoProducto), contenido);

        actualizarProductos();
        productoForm.setVisible(false);
    }

    private void actualizarProductos() {
        try {
            List<Producto> productos = productoRepository.findAll();
            productoGrid.setItems(productos);
            LoggerUI.logInfo("Productos cargados: " + productos.size());
            if (productos.isEmpty()) {
                LoggerUI.logError("No hay productos registrados en el inventario.");
            }
        } catch (Exception ex) {
            LoggerUI.logError("Error al cargar productos: " + ex.getMessage());
        }
    }
}
