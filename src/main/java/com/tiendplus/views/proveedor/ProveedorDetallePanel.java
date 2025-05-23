package com.tiendplus.views.proveedor;

import java.util.List;

import com.tiendplus.models.Producto;
import com.tiendplus.models.Proveedor;
import com.tiendplus.repositories.ProductoRepository;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ProveedorDetallePanel extends VerticalLayout {
    private final ProductoRepository productoRepository;
    private final Paragraph nombre = new Paragraph();
    private final Paragraph correo = new Paragraph();
    private final Paragraph telefono = new Paragraph();
    private final Paragraph empresa = new Paragraph();
    private final Grid<Producto> productosGrid = new Grid<>(Producto.class);

        public ProveedorDetallePanel(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;

        add(new H3("Información del proveedor"));
        add(nombre, correo, telefono, empresa);

        productosGrid.setColumns("nombre", "descripcion", "precio", "stock");
        productosGrid.setSizeFull();
        add(new H3("Productos asociados"), productosGrid);
        setVisible(false);
        setWidth("500px"); // Puedes ajustar el tamaño según tu diseño
    }

    public void mostrarDetalles(Proveedor proveedor) {
        if (proveedor != null) {
            nombre.setText("Nombre: " + proveedor.getNombre());
            correo.setText("Correo: " + proveedor.getCorreo());
            telefono.setText("Teléfono: " + proveedor.getTelefono());
            empresa.setText("Empresa: " + proveedor.getEmpresa());

            List<Producto> productos = productoRepository.findByProveedorId(proveedor.getId());
            productosGrid.setItems(productos);

            setVisible(true);
        } else {
            setVisible(false);
        }
    }
}
