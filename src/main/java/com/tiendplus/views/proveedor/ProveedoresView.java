package com.tiendplus.views.proveedor;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("proveedor/proveedores")
public class ProveedoresView extends VerticalLayout {
    public ProveedoresView() {
        Grid<String> grid = new Grid<>();
        grid.setItems("Proveedor X", "Proveedor Y", "Proveedor Z");
        add(grid);
    }
}