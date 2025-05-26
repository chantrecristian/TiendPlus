package com.tiendplus.views.admin;

import com.tiendplus.models.Producto;
import com.tiendplus.repositories.ProductoRepository;
import com.tiendplus.alertas.LoggerUI;
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

    private final ProductoGrid productoGrid = new ProductoGrid();

    @Autowired
    public InventarioView(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        Button volverBtn = new Button("Volver al Menú", e -> {
            LoggerUI.logInfo("Navegando al menú de administrador.");
            getUI().ifPresent(ui -> ui.navigate("menu-admin"));
        });

        // Layout solo con el grid
        HorizontalLayout botonesLayout = new HorizontalLayout(volverBtn);
        botonesLayout.setWidthFull();

        add(botonesLayout, productoGrid);
        actualizarProductos();
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
