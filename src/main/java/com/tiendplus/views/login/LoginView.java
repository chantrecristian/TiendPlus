package com.tiendplus.views.login;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.tiendplus.models.Usuario;
import com.tiendplus.services.UsuarioService;

@Route("login")
public class LoginView extends VerticalLayout {

    private final UsuarioService usuarioService;

    public LoginView(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;

        setSizeFull();
        setHeight("calc(80vh - 64px)");
        setPadding(false);
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        getStyle().set("background", "linear-gradient(135deg, #f3f4f6, #e0e7ff)");

        Image logo = new Image("images/tienda1.jpeg", "TiendPlus");
        logo.setHeight("100px");
        logo.getStyle().set("border-radius", "8px")
                       .set("box-shadow", "0 4px 8px rgba(0,0,0,0.1)");

        H1 titulo = new H1("TiendPlus");
        titulo.getStyle().set("color", "#1e293b")
                         .set("font-size", "2em")
                         .set("font-weight", "bold");

        TextField nombreField = new TextField("Usuario");
        nombreField.setPlaceholder("Ingresa tu usuario");
        nombreField.setWidth("100%");
        nombreField.getStyle().set("margin-bottom", "15px");

        PasswordField contraseñaField = new PasswordField("Contraseña");
        contraseñaField.setPlaceholder("********");
        contraseñaField.setWidth("100%");
        contraseñaField.getStyle().set("margin-bottom", "15px");

        Div mensajeError = new Div();
        mensajeError.getStyle().set("color", "red");
        mensajeError.setVisible(false);

        Button loginButton = new Button("Iniciar sesión");
        loginButton.setWidth("100%");
        loginButton.getStyle()
            .set("background-color", "#1e293b")
            .set("color", "white")
            .set("border-radius", "8px");

        loginButton.addClickListener(e -> {
            String nombre = nombreField.getValue();
            String contraseña = contraseñaField.getValue();

            try {
                Usuario usuario = usuarioService.validarLogin(nombre, contraseña);

                if (usuario != null) {
                    mensajeError.setVisible(false);
                    String rol = usuario.getRol().toLowerCase();

                    UI.getCurrent().getPage().executeJs("console.log('✅ Usuario autenticado correctamente: " + nombre + "')");

                    switch (rol) {
                        case "administrador":
                            UI.getCurrent().getPage().executeJs("console.log('👤 Rol administrador detectado. Redirigiendo al menú.')");
                            UI.getCurrent().navigate("menu-admin");
                            break;
                        case "cajero":
                            UI.getCurrent().getPage().executeJs("console.log('👤 Rol cajero detectado. Redirigiendo a ventas.')");
                            UI.getCurrent().navigate("registrar-venta");
                            break;
                        case "proveedor":
                            UI.getCurrent().getPage().executeJs("console.log('👤 Rol proveedor detectado. Redirigiendo a productos.')");
                            UI.getCurrent().navigate("proveedor/productos");
                            break;
                        default:
                            mensajeError.setText("Rol no reconocido: " + rol);
                            mensajeError.setVisible(true);
                            UI.getCurrent().getPage().executeJs("console.error('❌ Rol no reconocido: " + rol + "')");
                    }

                } else {
                    mensajeError.setText("Credenciales incorrectas");
                    mensajeError.setVisible(true);
                    UI.getCurrent().getPage().executeJs("console.error('❌ Fallo en login: credenciales incorrectas para el usuario " + nombre + "')");
                }

            } catch (Exception ex) {
                mensajeError.setText("Error inesperado en el sistema");
                mensajeError.setVisible(true);
                UI.getCurrent().getPage().executeJs("console.error('🔥 Error inesperado al intentar iniciar sesión: ' + $0)", ex.getMessage());
            }
        });

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
}
