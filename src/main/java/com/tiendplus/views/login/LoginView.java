package com.tiendplus.views.login;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.tiendplus.services.UsuarioService;

@Route("login")
public class LoginView extends VerticalLayout {

    private final UsuarioService usuarioService;

    public LoginView(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        // Título
        H1 titulo = new H1("TiendPlus");

        // Campos
        TextField nombreField = new TextField("Usuario");
        nombreField.setPlaceholder("Ingresa tu usuario");
        nombreField.setWidth("300px");

        PasswordField contraseñaField = new PasswordField("Contraseña");
        contraseñaField.setPlaceholder("********");
        contraseñaField.setWidth("300px");

        // Mensaje de error
        Div mensajeError = new Div();
        mensajeError.getStyle().set("color", "red");
        mensajeError.setVisible(false);

        // Botón
        Button loginButton = new Button("Iniciar sesión");
        loginButton.getStyle().set("background-color", "#4CAF50")
                              .set("color", "white");
        loginButton.addClickListener(e -> {
            String nombre = nombreField.getValue();
            String contraseña = contraseñaField.getValue();
            if (validarLogin(nombre, contraseña)) {
                mensajeError.setVisible(false);
                getUI().ifPresent(ui -> ui.navigate("menu-admin"));
            } else {
                mensajeError.setText("Credenciales incorrectas");
                mensajeError.setVisible(true);
            }
        });

        // Contenedor del formulario
        VerticalLayout formLayout = new VerticalLayout(
            titulo, nombreField, contraseñaField, loginButton, mensajeError
        );
        formLayout.setPadding(true);
        formLayout.setSpacing(true);
        formLayout.setAlignItems(Alignment.CENTER);
        formLayout.getStyle()
            .set("border", "1px solid #ccc")
            .set("border-radius", "12px")
            .set("padding", "30px")
            .set("box-shadow", "0 4px 10px rgba(0,0,0,0.1)")
            .set("background-color", "#fff");

        add(formLayout);
    }

    private boolean validarLogin(String nombre, String contraseña) {
        return usuarioService.validarLogin(nombre, contraseña);
    }
}
