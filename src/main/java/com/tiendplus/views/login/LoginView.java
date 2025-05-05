package com.tiendplus.views.login;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
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
        getStyle().set("background", "linear-gradient(135deg, #f3f4f6, #e0e7ff)");

        // Logo (opcional, coméntalo si no lo usas)
        Image logo = new Image("images/tienda1.jpeg", "TiendPlus");
        logo.setHeight("100px");
        logo.getStyle().set("border-radius", "8px")
                       .set("box-shadow", "0 4px 8px rgba(0,0,0,0.1)");

        // Título estilizado
        H1 titulo = new H1("TiendPlus");
        titulo.getStyle().set("color", "#1e293b")
                         .set("font-size", "2em")
                         .set("font-weight", "bold");

        // Campo usuario
        TextField nombreField = new TextField("Usuario");
        nombreField.setPlaceholder("Ingresa tu usuario");
        nombreField.setWidth("100%");
        nombreField.getStyle().set("margin-bottom", "15px");

        // Campo contraseña
        PasswordField contraseñaField = new PasswordField("Contraseña");
        contraseñaField.setPlaceholder("********");
        contraseñaField.setWidth("100%");
        contraseñaField.getStyle().set("margin-bottom", "15px");

        // Mensaje de error
        Div mensajeError = new Div();
        mensajeError.getStyle().set("color", "red");
        mensajeError.setVisible(false);

        // Botón estilizado
        Button loginButton = new Button("Iniciar sesión");
        loginButton.setWidth("100%");
        loginButton.getStyle()
            .set("background-color", "#1e293b")  // Azul oscuro
            .set("color", "white")
            .set("border-radius", "8px");

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

        // Contenedor visual
        VerticalLayout formLayout = new VerticalLayout(
            logo, titulo, nombreField, contraseñaField, loginButton, mensajeError
        );
        formLayout.setPadding(true);
        formLayout.setSpacing(true);
        formLayout.setAlignItems(Alignment.CENTER);
        formLayout.setWidth("350px");
        formLayout.getStyle()
            .set("background-color", "#ffffff")
            .set("border-radius", "12px")
            .set("box-shadow", "0 6px 12px rgba(0,0,0,0.15)")
            .set("padding", "32px");

        add(formLayout);
    }

    private boolean validarLogin(String nombre, String contraseña) {
        return usuarioService.validarLogin(nombre, contraseña);
    }
}
