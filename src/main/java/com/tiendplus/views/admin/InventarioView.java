package com.tiendplus.views.admin;

import com.tiendplus.models.Producto;
import com.tiendplus.repositories.ProductoRepository;
import com.tiendplus.repositories.ProveedorRepository;
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

        Button volverBtn = new Button("Volver al Menú", e -> getUI().ifPresent(ui -> ui.navigate("menu-admin")));

        // Botón para crear nuevo producto
        Button nuevoProducto = new Button("Nuevo Producto", e -> {
            Producto nuevo = new Producto();
            productoForm.setProveedores(proveedorRepository.findAll());
            productoForm.setProducto(nuevo);
            productoForm.setVisible(true);
        });

        // Listeners del formulario
        productoForm.setOnSave(producto -> {
            productoRepository.save(producto);
            actualizarProductos();
            productoForm.setVisible(false);
        });

        productoForm.setOnDelete(producto -> {
            productoRepository.delete(producto);
            actualizarProductos();
            productoForm.setVisible(false);
        });

        productoGrid.getGrid().asSingleSelect().addValueChangeListener(event -> {
            Producto seleccionado = event.getValue();
            if (seleccionado != null) {
                productoForm.setProveedores(proveedorRepository.findAll());
                productoForm.setProducto(seleccionado);
                productoForm.setVisible(true);
            } else {
                productoForm.setVisible(false);
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
        List<Producto> productos = productoRepository.findAll();
        productoGrid.setItems(productos);
    }
}
