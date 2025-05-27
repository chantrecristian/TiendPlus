package com.tiendplus.complements;

import com.tiendplus.alertas.LoggerUI;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.server.VaadinSession;

public class MainLayout extends AppLayout {

    public MainLayout() {
        setPrimarySection(Section.NAVBAR);
        addToNavbar(createNavbar()); // Agrega la barra de navegación personalizada
    }

    private Component createNavbar() {
        // Contenedor principal de la barra de navegación
        HorizontalLayout navbar = new HorizontalLayout();
        navbar.setWidthFull();
        navbar.setPadding(true);
        navbar.setSpacing(true);
        navbar.setAlignItems(FlexComponent.Alignment.CENTER);
        navbar.getStyle()
            .set("background-color", "#f8f9fa")
            .set("box-shadow", "0 2px 4px rgba(0,0,0,0.1)");

        // Título de la aplicación
        H1 title = new H1("TiendPlus");
        title.getStyle().set("margin", "0").set("font-size", "1.5em");

        // Avatar con menú
        Avatar avatar = new Avatar("Usuario");
        avatar.setImage("icons/user.png");
        avatar.setHeight("36px");
        avatar.setWidth("36px");

        ContextMenu userMenu = new ContextMenu(avatar);
        userMenu.setOpenOnClick(true);
        
        userMenu.addItem("Administrador", e -> {
            LoggerUI.logInfo("Navegando a vista de productos del proveedor.");
            getUI().ifPresent(ui -> ui.navigate("menu-admin"));
        });

        userMenu.addItem("Proveedor", e -> {
            LoggerUI.logInfo("Navegando a vista de productos del proveedor.");
            getUI().ifPresent(ui -> ui.navigate("proveedor/productos"));
        });

        userMenu.addItem("Cerrar sesión", e -> {
            LoggerUI.logInfo("Sesión cerrada por el usuario.");
            VaadinSession.getCurrent().close();
            getUI().ifPresent(ui -> ui.getPage().setLocation("login"));
        });

        // Añadimos título y avatar al navbar
        navbar.add(title, avatar);

        // Expandir el título para que quede a la izquierda y avatar a la derecha
        navbar.expand(title);

        // Justificar contenido entre ellos (espacio entre)
        navbar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        return navbar;
    }
}
