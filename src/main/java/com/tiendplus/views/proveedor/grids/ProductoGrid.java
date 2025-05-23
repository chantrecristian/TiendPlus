package com.tiendplus.views.proveedor.grids;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;

import com.tiendplus.models.Producto;
import com.tiendplus.models.Proveedor;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ProductoGrid extends VerticalLayout {
    // Grid para mostrar productos
    private final Grid<Producto> grid;

    public ProductoGrid() {
        grid = new Grid<>(Producto.class);  // Inicializa el grid
        grid.removeAllColumns(); 

        // columnas personalizadas
        grid.addColumn(Producto::getId).setHeader("ID");
        grid.addColumn(Producto::getNombre).setHeader("Nombre");
        grid.addColumn(Producto::getDescripcion).setHeader("Descripción");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');

        DecimalFormat formato = new DecimalFormat("#,##0", symbols); // sin decimales

        grid.addColumn(producto -> formato.format(producto.getPrecio()))
        .setHeader("Precio");
        grid.addColumn(Producto::getCantidad).setHeader("Cantidad");

        // Columna para el nombre del proveedor asociado
        grid.addColumn(producto -> {
            Proveedor proveedor = producto.getProveedor();
            return proveedor != null ? proveedor.getNombre() : "Sin proveedor";
        }).setHeader("Proveedor");

        add(grid);
        setSizeFull();
    }
    //Método para actualizar los productos que se muestran en el grid.
    public void setItems(List<Producto> items) {
        grid.setItems(items);
    }

    public Grid<Producto> getGrid() {
        return grid;
    }
}
