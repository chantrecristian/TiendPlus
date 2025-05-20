package com.tiendplus.views.proveedor;

import com.tiendplus.views.proveedor.grids.ProveedorGrid;
import com.tiendplus.models.Producto;
import com.tiendplus.models.Proveedor;
import com.tiendplus.repositories.ProductoRepository;
import com.tiendplus.repositories.ProveedorRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.List;
//ruta con el mainLayout2
@Route(value = "proveedor/proveedores", layout = MainLayout2.class)
public class ProveedoresView extends VerticalLayout {
    // Repositorio para acceder a los datos de los proveedores
    private final ProductoRepository productoRepository;
    private final ProveedorRepository proveedorRepository;
    private final ProveedorGrid proveedorGrid = new ProveedorGrid();//muestra la lista
    private final ProveedorForm proveedorForm = new ProveedorForm();//formulario
    private final HorizontalLayout detallesProveedorLayout = new HorizontalLayout();
    private final Grid<Producto> productosGrid = new Grid<>(Producto.class, false);

    // Constructor de la vista, se arma toda la interfaz
    @Autowired
    public ProveedoresView(ProveedorRepository proveedorRepository, ProductoRepository productoRepository) {
        this.proveedorRepository = proveedorRepository;
    this.productoRepository = productoRepository;

        Button agregar = new Button("Agregar proveedor", e -> {
            proveedorForm.setProveedor(new Proveedor()); // Crea un proveedor vacío
            proveedorForm.setVisible(true);             // Muestra el formulario
        });
        // guardar un proveedor
        proveedorForm.setOnSave(proveedor -> {
            proveedorRepository.save(proveedor); // Guarda en la bd
            actualizarProveedores();             // Actualiza la tabla
            proveedorForm.setVisible(false);// Oculta el formulario
        });     
        //eliminar un proveedor
        proveedorForm.setOnDelete(proveedor -> {
            proveedorRepository.delete(proveedor); // Elimina de la bd
            actualizarProveedores();               // Actualiza la tabla
            proveedorForm.setVisible(false);// Oculta el formulario
        });
        //cuando se selecciona un proveedor
        proveedorGrid.getGrid().asSingleSelect().addValueChangeListener(event -> {
            Proveedor seleccionado = event.getValue();
            if (seleccionado != null) {
                proveedorForm.setProveedor(seleccionado); // Muestra proveedor en el formulario
                proveedorForm.setVisible(true);// Muestra el formulario

                        // Obtener productos del proveedor
                List<Producto> productos = productoRepository.findByProveedorId(seleccionado.getId());

                // Mostrar menú de detalles del proveedor + productos
                mostrarDetalleProveedor(seleccionado, productos);
            }
        }); 

        //contiene la tabla y el formulario
        HorizontalLayout contenido = new HorizontalLayout(proveedorGrid, proveedorForm);
        contenido.setWidthFull();//ancho

        // Controlar tamaños
        proveedorGrid.setWidthFull();
        proveedorForm.setWidth("350px"); // ancho del formulario
        contenido.setFlexGrow(1, proveedorGrid);// La tabla puede crecer
        contenido.setFlexGrow(0, proveedorForm); // evita que el formulario crezca
        detallesProveedorLayout.setWidthFull();
        //agrega el boton y el contenido
        add(agregar, contenido, detallesProveedorLayout);
        actualizarProveedores();// Carga inicial de la tabla de proveedores
        proveedorForm.setVisible(false);//oculta el formulario
    }
    // Método que actualiza los datos en la tabla desde la base de datos
    private void actualizarProveedores() {
        List<Proveedor> lista = proveedorRepository.findAll();// Obtener proveedores
        proveedorGrid.setItems(lista);// Mostrar en la tabla
    }   
    private void mostrarDetalleProveedor(Proveedor proveedor, List<Producto> productos) {
        detallesProveedorLayout.removeAll(); // Limpia contenido anterior

        // Panel con detalles del proveedor
        VerticalLayout infoProveedor = new VerticalLayout();
        infoProveedor.add(new H3("Detalles del Proveedor"));
        infoProveedor.add(new Span("Nombre: " + proveedor.getNombre()));
        infoProveedor.add(new Span("Teléfono: " + proveedor.getTelefono()));
        infoProveedor.add(new Span("Correo: " + proveedor.getCorreo()));
        infoProveedor.add(new Span("Empresa: " + proveedor.getEmpresa()));
        infoProveedor.setWidth("300px");

        // Configurar grid de productos
        productosGrid.setItems(productos);
        productosGrid.removeAllColumns(); // Limpia columnas previas (si las hay)
        productosGrid.addColumn(producto -> producto.getNombre()).setHeader("Nombre");
        productosGrid.addColumn(producto -> producto.getDescripcion()).setHeader("Descripción");
        productosGrid.addColumn(producto -> producto.getCantidad()).setHeader("Cantidad");
        productosGrid.addColumn(producto -> {
            NumberFormat formato = NumberFormat.getNumberInstance(new Locale("es", "CO"));
            return formato.format(producto.getPrecio());
        }).setHeader("Precio");
        productosGrid.setWidthFull();

        // Agregar a layout horizontal
        detallesProveedorLayout.add(infoProveedor, productosGrid);
    }

}