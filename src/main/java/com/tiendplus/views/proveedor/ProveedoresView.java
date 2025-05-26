package com.tiendplus.views.proveedor;

import com.tiendplus.views.proveedor.grids.ProveedorGrid;
import com.tiendplus.models.Producto;
import com.tiendplus.models.Proveedor;
import com.tiendplus.repositories.ProductoRepository;
import com.tiendplus.repositories.ProveedorRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Route(value = "proveedor/proveedores", layout = MainLayout2.class)
public class ProveedoresView extends VerticalLayout {
    private final ProductoRepository productoRepository;
    private final ProveedorRepository proveedorRepository;
    private final ProveedorGrid proveedorGrid = new ProveedorGrid();
    private final ProveedorForm proveedorForm = new ProveedorForm();
    private final HorizontalLayout detallesProveedorLayout = new HorizontalLayout();
    private final Grid<Producto> productosGrid = new Grid<>(Producto.class, false);

    @Autowired
    public ProveedoresView(ProveedorRepository proveedorRepository, ProductoRepository productoRepository) {
        this.proveedorRepository = proveedorRepository;
        this.productoRepository = productoRepository;

        Button agregar = new Button("Agregar proveedor", e -> {
            getElement().executeJs("console.log('Botón agregar proveedor presionado')");
            Notification.show("Preparando formulario para nuevo proveedor...");
            proveedorForm.setProveedor(new Proveedor());
            proveedorForm.setVisible(true);
        });

        proveedorForm.setOnSave(proveedor -> {
            proveedorRepository.save(proveedor);
            getElement().executeJs("console.log('Proveedor guardado: ' + $0)", proveedor.getNombre());
            Notification.show("Proveedor guardado: " + proveedor.getNombre());
            actualizarProveedores();
            proveedorForm.setVisible(false);
        });

        proveedorForm.setOnDelete(proveedor -> {
            proveedorRepository.delete(proveedor);
            getElement().executeJs("console.log('Proveedor eliminado: ' + $0)", proveedor.getNombre());
            Notification.show("Proveedor eliminado: " + proveedor.getNombre());
            actualizarProveedores();
            proveedorForm.setVisible(false);
        });

        proveedorGrid.getGrid().asSingleSelect().addValueChangeListener(event -> {
            Proveedor seleccionado = event.getValue();
            if (seleccionado != null) {
                getElement().executeJs("console.log('Proveedor seleccionado: ' + $0)", seleccionado.getNombre());
                Notification.show("Mostrando detalles de: " + seleccionado.getNombre());
                proveedorForm.setProveedor(seleccionado);
                proveedorForm.setVisible(true);

                List<Producto> productos = productoRepository.findByProveedorId(seleccionado.getId());
                mostrarDetalleProveedor(seleccionado, productos);
            }
        });

        HorizontalLayout contenido = new HorizontalLayout(proveedorGrid, proveedorForm);
        contenido.setWidthFull();

        proveedorGrid.setWidthFull();
        proveedorForm.setWidth("350px");
        contenido.setFlexGrow(1, proveedorGrid);
        contenido.setFlexGrow(0, proveedorForm);
        detallesProveedorLayout.setWidthFull();

        add(agregar, contenido, detallesProveedorLayout);
        actualizarProveedores();
        proveedorForm.setVisible(false);
    }

    private void actualizarProveedores() {
        List<Proveedor> lista = proveedorRepository.findAll();
        proveedorGrid.setItems(lista);
        getElement().executeJs("console.log('Tabla de proveedores actualizada con ' + $0 + ' registros')", lista.size());
    }

    private void mostrarDetalleProveedor(Proveedor proveedor, List<Producto> productos) {
        detallesProveedorLayout.removeAll();

        VerticalLayout infoProveedor = new VerticalLayout();
        infoProveedor.add(new H3("Detalles del Proveedor"));
        infoProveedor.add(new Span("Nombre: " + proveedor.getNombre()));
        infoProveedor.add(new Span("Teléfono: " + proveedor.getTelefono()));
        infoProveedor.add(new Span("Correo: " + proveedor.getCorreo()));
        infoProveedor.add(new Span("Empresa: " + proveedor.getEmpresa()));
        infoProveedor.setWidth("300px");

        productosGrid.setItems(productos);
        productosGrid.removeAllColumns();
        productosGrid.addColumn(Producto::getNombre).setHeader("Nombre");
        productosGrid.addColumn(Producto::getDescripcion).setHeader("Descripción");
        productosGrid.addColumn(Producto::getCantidad).setHeader("Cantidad");
        productosGrid.addColumn(producto -> {
            NumberFormat formato = NumberFormat.getNumberInstance(new Locale("es", "CO"));
            return formato.format(producto.getPrecio());
        }).setHeader("Precio");
        productosGrid.setWidthFull();

        detallesProveedorLayout.add(infoProveedor, productosGrid);

        getElement().executeJs("console.log('Mostrando detalles y productos para proveedor: ' + $0)", proveedor.getNombre());
        Notification.show("Detalles del proveedor cargados");
    }
}
