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

@Route("login") // Define la ruta para acceder a esta vista
public class LoginView extends VerticalLayout {

    private final UsuarioService usuarioService;  // Inyección del servicio para validar el login

    public LoginView(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;  // Inicializa el servicio de usuario

        // Configura el diseño de la página
        setSizeFull();  // Establece que el layout ocupe toda la pantalla
        setHeight("calc(80vh - 64px)"); // Ajusta la altura, restando el alto de la barra
        setPadding(false);
        setAlignItems(Alignment.CENTER);  // Centra los elementos verticalmente
        setJustifyContentMode(JustifyContentMode.CENTER);  // Centra los elementos horizontalmente
        getStyle().set("background", "linear-gradient(135deg, #f3f4f6, #e0e7ff)");  // Establece un fondo con gradiente

        // Configura el logo (opcional)
        Image logo = new Image("images/tienda1.jpeg", "TiendPlus");
        logo.setHeight("100px");  // Establece el tamaño del logo
        logo.getStyle().set("border-radius", "8px")  // Aplica borde redondeado
                       .set("box-shadow", "0 4px 8px rgba(0,0,0,0.1)");  // Añade sombra al logo

        // Configura el título de la página
        H1 titulo = new H1("TiendPlus");
        titulo.getStyle().set("color", "#1e293b")  // Establece el color del texto
                         .set("font-size", "2em")  // Establece el tamaño de la fuente
                         .set("font-weight", "bold");  // Establece el peso de la fuente

        // Configura el campo para ingresar el nombre de usuario
        TextField nombreField = new TextField("Usuario");
        nombreField.setPlaceholder("Ingresa tu usuario");  // Placeholder cuando el campo está vacío
        nombreField.setWidth("100%");  // Establece el ancho del campo
        nombreField.getStyle().set("margin-bottom", "15px");  // Añade espacio debajo del campo

        // Configura el campo para ingresar la contraseña
        PasswordField contraseñaField = new PasswordField("Contraseña");
        contraseñaField.setPlaceholder("********");  // Placeholder para la contraseña
        contraseñaField.setWidth("100%");  // Establece el ancho del campo
        contraseñaField.getStyle().set("margin-bottom", "15px");  // Añade espacio debajo del campo

        // Configura el mensaje de error que se muestra cuando las credenciales son incorrectas
        Div mensajeError = new Div();
        mensajeError.getStyle().set("color", "red");  // Establece el color rojo para el mensaje de error
        mensajeError.setVisible(false);  // Inicialmente no se muestra el mensaje

        // Configura el botón de inicio de sesión
        Button loginButton = new Button("Iniciar sesión");
        loginButton.setWidth("100%");  // Establece el ancho del botón
        loginButton.getStyle()
            .set("background-color", "#1e293b")  // Color de fondo del botón
            .set("color", "white")  // Color del texto
            .set("border-radius", "8px");  // Borde redondeado en el botón

        // Acción al hacer clic en el botón de inicio de sesión
        loginButton.addClickListener(e -> {
            String nombre = nombreField.getValue();  // Obtiene el valor del nombre de usuario
            String contraseña = contraseñaField.getValue();  // Obtiene el valor de la contraseña
            if (validarLogin(nombre, contraseña)) {  // Valida las credenciales
                mensajeError.setVisible(false);  // Si es válido, oculta el mensaje de error
                getUI().ifPresent(ui -> ui.navigate("menu-admin"));  // Navega al menú de administrador
            } else {
                mensajeError.setText("Credenciales incorrectas");  // Si no es válido, muestra el mensaje de error
                mensajeError.setVisible(true);
            }
        });

        // Configura el layout principal que contiene todos los elementos visuales
        VerticalLayout formLayout = new VerticalLayout(
            logo, titulo, nombreField, contraseñaField, loginButton, mensajeError
        );
        formLayout.setPadding(true);  // Añade relleno alrededor de los elementos
        formLayout.setSpacing(true);  // Añade espacio entre los elementos
        formLayout.setAlignItems(Alignment.CENTER);  // Centra los elementos en el layout
        formLayout.setWidth("350px");  // Establece el ancho del formulario
        formLayout.getStyle()
            .set("background-color", "#ffffff")  // Fondo blanco para el formulario
            .set("border-radius", "12px")  // Bordes redondeados
            .set("box-shadow", "0 6px 12px rgba(0,0,0,0.15)")  // Sombra del formulario
            .set("padding", "32px");  // Añade relleno dentro del formulario

        add(formLayout);  // Añade el formulario al layout principal
    }

    // Método que valida las credenciales del usuario
    private boolean validarLogin(String nombre, String contraseña) {
        return usuarioService.validarLogin(nombre, contraseña);  // Llama al servicio para validar las credenciales
    }
}
