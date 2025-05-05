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
// Esta vista de la ruta "/proveedor/productos" usando el layout MainLayout2
@Route(value = "proveedor/productos", layout = MainLayout2.class)
public class ProductosView extends VerticalLayout {
    // Repositorios para acceder a productos y proveedores en la base de datos
    private final ProductoRepository productoRepository;
    private final ProveedorRepository proveedorRepository;
    // Tabla para mostrar productos
    private final ProductoGrid productoGrid = new ProductoGrid();
    // Formulario para crear o editar productos
    private final ProductoForm productoForm = new ProductoForm();
    // Constructor de la vista. se arma la interfaz
    @Autowired
    public ProductosView(ProductoRepository productoRepository, ProveedorRepository proveedorRepository) {
        this.productoRepository = productoRepository;
        this.proveedorRepository = proveedorRepository;
        // Botón agregar un nuevo producto
        Button agregar = new Button("Agregar producto", e -> {
            Producto nuevo = new Producto();
            productoForm.setProveedores(proveedorRepository.findAll());
            productoForm.setProducto(nuevo);
            productoForm.setVisible(true);
        });
        // despues de guardar
        productoForm.setOnSave(producto -> {
            productoRepository.save(producto);
            actualizarProductos();
            productoForm.setVisible(false);
        });
        //despues de eliminar
        productoForm.setOnDelete(producto -> {
            productoRepository.delete(producto);
            actualizarProductos();
            productoForm.setVisible(false);
        });

        productoGrid.getGrid().asSingleSelect().addValueChangeListener(event -> {
            Producto seleccionado = event.getValue();
            if (seleccionado != null) {
                productoForm.setProveedores(proveedorRepository.findAll());// Carga los proveedores
                productoForm.setProducto(seleccionado);// Muestra ese producto en el formulario
                productoForm.setVisible(true);// Muestra el formulario
            } else {
                productoForm.setVisible(false);//oculta si no hay un producto seleccionado
            }
        });        

        //layout horizontal que contiene la tabla y el formulario
        HorizontalLayout contenido = new HorizontalLayout(productoGrid, productoForm);
        contenido.setWidthFull(); //todo el ancho
        contenido.setFlexGrow(1, productoGrid);// Ambos elementos crecen por igual
        contenido.setFlexGrow(1, productoForm);
        // Agrega el botón y el contenido a la vista
        add(agregar, contenido);
        actualizarProductos();// Cargar productos desde la base al iniciar
        productoForm.setVisible(false);// Oculta el formulario al inicio
    }
    // Método que actualiza la tabla de productos desde la base de datos
    private void actualizarProductos() {
        List<Producto> productos = productoRepository.findAll();
        productoGrid.setItems(productos);
    }
}