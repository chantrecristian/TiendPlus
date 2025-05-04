package com.tiendplus.views.proveedor;

import com.tiendplus.views.proveedor.grids.ProductoGrid;
import com.tiendplus.models.Producto;
import com.tiendplus.repositories.ProductoRepository;
import com.tiendplus.repositories.ProveedorRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route("proveedor/productos")
public class ProductosView extends VerticalLayout {

    private final ProductoRepository productoRepository;
    private final ProveedorRepository proveedorRepository;

    private final ProductoGrid productoGrid = new ProductoGrid();
    private final ProductoForm productoForm = new ProductoForm();

    @Autowired
    public ProductosView(ProductoRepository productoRepository, ProveedorRepository proveedorRepository) {
        this.productoRepository = productoRepository;
        this.proveedorRepository = proveedorRepository;

        Button agregar = new Button("Agregar producto", e -> {
            Producto nuevo = new Producto();
            productoForm.setProveedores(proveedorRepository.findAll());
            productoForm.setProducto(nuevo);
            productoForm.setVisible(true);
        });

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

        // Nuevo layout horizontal que contiene la tabla y el formulario
        HorizontalLayout contenido = new HorizontalLayout(productoGrid, productoForm);
        contenido.setWidthFull();
        contenido.setFlexGrow(1, productoGrid);
        contenido.setFlexGrow(1, productoForm);

        add(agregar, contenido);
        actualizarProductos();
        productoForm.setVisible(false);
    }

    private void actualizarProductos() {
        List<Producto> productos = productoRepository.findAll();
        productoGrid.setItems(productos);
    }
}