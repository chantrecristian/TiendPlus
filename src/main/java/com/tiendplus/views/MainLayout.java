package com.tiendplus.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Image;

public class MainLayout extends AppLayout {

    public MainLayout() {
        // Configura el diseño principal de la aplicación

        // Crea un objeto Image que carga el logo de la aplicación
        Image logo = new Image("src/main/resources/META-INF/resources/images/\r\n" + //
                        "", "Logo");

        // Añade el logo a la barra de navegación
        addToNavbar(logo);
    }
}
