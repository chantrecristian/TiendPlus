package com.tiendplus.views.proveedor.grids;

import java.util.List;

import com.tiendplus.models.Proveedor;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ProveedorGrid extends VerticalLayout {
    private final Grid<Proveedor> grid;

    public ProveedorGrid() {
        grid = new Grid<>(Proveedor.class);
        grid.setColumns("id", "nombre", "correo", "telefono", "empresa");
        add(grid);
        setSizeFull();
    }

    public void setItems(List<Proveedor> items) {
        grid.setItems(items);
    }

    public Grid<Proveedor> getGrid() {
        return grid;
    }
    
}
