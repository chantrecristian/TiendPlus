package com.tiendplus.views.proveedor.grids;

import java.util.List;

import com.tiendplus.models.Proveedor;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ProveedorGrid extends VerticalLayout {
    private final Grid<Proveedor> grid; // Grid para mostrar proveedores

    public ProveedorGrid() {
        grid = new Grid<>(Proveedor.class);
        grid.setColumns("id", "nombre", "correo", "telefono", "empresa"); //columnas 
        add(grid);// Se agrega el grid al layout vertical
        setSizeFull();
    }
    //Método para establecer los datos que se mostrarán en el grid.
    public void setItems(List<Proveedor> items) {
        grid.setItems(items);
    }
    //Devuelve el grid interno por si se necesita manipular desde afuera.
    public Grid<Proveedor> getGrid() {
        return grid;
    }
    
}
