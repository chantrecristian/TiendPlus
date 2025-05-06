package com.tiendplus.views.proveedor;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLink;

public class MainLayout2 extends AppLayout {//nav
    //agrega la barra de navegación al layout.
    public MainLayout2() {
        setPrimarySection(Section.NAVBAR);
        addToNavbar(createNavbar());// Agrega la barra de navegación personalizada
    }

    private Component createNavbar() {
        HorizontalLayout navbar = new HorizontalLayout();
        navbar.setWidthFull();// ancho
        navbar.setPadding(true);//padding
        navbar.setSpacing(true);//espacios
        navbar.setAlignItems(FlexComponent.Alignment.CENTER);
        navbar.getStyle()// Estilo visual del navbar: color de fondo y sombra
            .set("background-color", "#f8f9fa")
            .set("box-shadow", "0 2px 4px rgba(0,0,0,0.1)");

        H1 title = new H1("TiendPlus");
        title.getStyle().set("margin", "0").set("font-size", "1.5em");
        // Enlaces de navegación a las vistas de productos y proveedores
        RouterLink productosLink = new RouterLink("Productos", ProductosView.class);
        RouterLink proveedoresLink = new RouterLink("Proveedores", ProveedoresView.class);
        // Agrupa los enlaces horizontalmente
        HorizontalLayout links = new HorizontalLayout(productosLink, proveedoresLink);
        links.setSpacing(true);
        // Agrega el título y los enlaces al navbar
        navbar.add(title);
        navbar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        navbar.expand(title);
        navbar.add(links);

        return navbar;// Retorna el navbar
    }
}