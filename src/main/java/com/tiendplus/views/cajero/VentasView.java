package com.tiendplus.views.cajero;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("cajero/ventas")
public class VentasView extends VerticalLayout {
    public VentasView() {
        Grid<String> grid = new Grid<>();
        grid.setItems("Venta #1", "Venta #2", "Venta #3");
        add(grid);
    }
}