package com.tiendplus.views.admin;

// Importación de componentes necesarios para la vista
import com.tiendplus.alertas.LoggerUI;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/**
 * Esta clase representa la vista del menú principal para el administrador.
 * Contiene los accesos a las diferentes secciones del sistema como Inventario,
 * Ventas y Reportes, así como los mensajes de bienvenida.
 */
@Route(value = "menu-admin", layout = MainView.class) // Ruta que se asocia con la clase MainView
@PageTitle("Menú") // Título que se muestra en la barra de navegación
public class MenuAdminView extends VerticalLayout {

    // Constructor que se llama automáticamente cuando se carga la vista
    public MenuAdminView() {
        setSizeFull(); // Hace que el layout ocupe todo el espacio disponible
        setPadding(true); // Añade padding a la vista
        setAlignItems(Alignment.CENTER); // Alinea los elementos verticalmente al centro

        // Mensaje de bienvenida
        Span bienvenida = new Span("Bienvenido al menú");
        bienvenida.getStyle().set("font-size", "1.8em") // Estilo para el tamaño y grosor del texto
                           .set("font-weight", "bold");

        // Subtítulo que indica el propósito de la vista
        Span subtitulo = new Span("Gestiona tu tienda de manera rápida y sencilla.");
        subtitulo.getStyle().set("font-size", "1.1em")  // Estilo para tamaño de fuente
                           .set("color", "gray"); // Cambia el color del texto

        // Layout horizontal para alinear las tarjetas de acceso a las secciones
        HorizontalLayout accesos = new HorizontalLayout();
        accesos.getStyle()
        .set("flex-wrap", "wrap")
        .set("justify-content", "center");
        accesos.setSpacing(true); // Espaciado entre los elementos
        accesos.setPadding(true); // Padding para los elementos dentro del layout

        // Crear tarjetas para las secciones: Inventario, Ventas y Reportes
        VerticalLayout inventarioCard = createCard("📦 Inventario", "Ver tus productos", "inventario");
        VerticalLayout ventasCard = createCard("💵 Ventas", "Ver las ventas", "ventas");
        VerticalLayout clientesCard = createCard("👤 Clientes", "Visualiza tus clientes", "clientes");

        // Agregar las tarjetas al layout horizontal
        accesos.add(inventarioCard, ventasCard, clientesCard);

        // Agregar los componentes al layout principal (bienvenida, subtítulo y accesos)
        add(bienvenida, subtitulo, accesos);
    }

    /**
     * Este método crea una tarjeta con un título, descripción y enlace a la ruta correspondiente.
     * @param title El título de la tarjeta.
     * @param description Descripción corta de la tarjeta.
     * @param route La ruta de la vista a la que redirige al hacer clic.
     * @return Un layout vertical que representa la tarjeta.
     */
    private VerticalLayout createCard(String title, String description, String route) {
        // Layout vertical para cada tarjeta
        VerticalLayout card = new VerticalLayout();
        card.getStyle().set("border", "1px solid #ccc") // Estilo de borde
                     .set("border-radius", "12px") // Bordes redondeados
                     .set("padding", "16px") // Padding interior
                     .set("width", "200px") // Ancho de la tarjeta
                     .set("cursor", "pointer") // Cursor en forma de mano para indicar que es interactivo
                     .set("box-shadow", "2px 2px 6px rgba(0,0,0,0.1)"); // Sombra para el efecto visual

        // Título de la tarjeta
        Span titleLabel = new Span(title);
        titleLabel.getStyle().set("font-weight", "bold"); // Estilo en negrita para el título

        // Descripción debajo del título
        Span descLabel = new Span(description);
        descLabel.getStyle().set("font-size", "0.9em") // Estilo para el tamaño del texto
                   .set("color", "gray"); // Color gris para la descripción

        // Agregar el título y la descripción al layout de la tarjeta
        card.add(titleLabel, descLabel);

        // Añadir un evento de clic para navegar a la ruta correspondiente cuando se hace clic en la tarjeta
        card.addClickListener(e -> {
            LoggerUI.logInfo("Navegando a la sección: " + title.replace("📦", "").replace("💵", "").replace("📊", "").trim());
            getUI().ifPresent(ui -> ui.navigate(route));
        });

        return card; // Devuelve la tarjeta lista
    }
}
