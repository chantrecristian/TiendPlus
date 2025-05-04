package com.tiendplus.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Image;

public class MainLayout extends AppLayout {
    public MainLayout() {
        // Configuración del diseño principal
        Image logo = new Image("src/main/resources/META-INF/resources/images/\r\n" + //
                        "", "Logo");
        addToNavbar(logo);
    }
}