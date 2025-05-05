package com.tiendplus.views.admin;

// Importación de componentes necesarios para la vista
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;

/**
 * Esta clase representa la vista de los reportes en el sistema.
 * Muestra un reporte de ventas del día con información como la cantidad de ventas,
 * productos vendidos y el total generado.
 */
@PageTitle("Reportes") // Título que se muestra en la barra de navegación
@Route(value = "reportes", layout = MainView.class) // Ruta asociada a esta vista en la aplicación
public class ReportesView extends VerticalLayout {

    // Constructor que se llama cuando se carga la vista
    public ReportesView() {
        // Configuración del layout principal
        setSizeFull(); // Hace que el layout ocupe todo el espacio disponible
        setAlignItems(Alignment.CENTER); // Centra los elementos verticalmente
        setJustifyContentMode(JustifyContentMode.CENTER); // Centra los elementos horizontalmente
        getStyle().set("background", "linear-gradient(135deg, #f3f4f6, #e0e7ff)"); // Fondo con un gradiente de colores

        // Contenedor estilo tarjeta para el reporte
        VerticalLayout card = new VerticalLayout();
        card.setWidth("80%"); // Ancho del contenedor en porcentaje
        card.setPadding(true); // Añadir padding dentro del contenedor
        card.setSpacing(true); // Espaciado entre los elementos dentro del contenedor
        card.setAlignItems(Alignment.CENTER); // Alinea los elementos dentro de la tarjeta al centro
        card.getStyle() // Estilo de la tarjeta
            .set("background-color", "#ffffff") // Fondo blanco
            .set("border-radius", "12px") // Bordes redondeados
            .set("box-shadow", "0 6px 12px rgba(0,0,0,0.1)") // Sombra suave para el contorno
            .set("padding", "32px"); // Padding interior del contenedor

        // Título del reporte
        H2 titulo = new H2("📊 Reporte de Ventas del Día");
        titulo.getStyle().set("color", "#1e293b"); // Color del texto del título

        // Fecha actual para mostrar en el reporte
        Span fecha = new Span("Fecha: " + LocalDate.now());
        fecha.getStyle().set("color", "gray"); // Color gris para la fecha

        // Datos simulados del reporte
        int totalVentas = 15; // Total de ventas realizadas
        int productosVendidos = 42; // Total de productos vendidos
        double totalGenerado = 328000; // Total generado en pesos

        // Mostrar la información del reporte en spans
        Span ventas = new Span("🧾 Ventas realizadas: " + totalVentas);
        Span productos = new Span("📦 Productos vendidos: " + productosVendidos);
        Span total = new Span("💰 Total generado: $" + totalGenerado);

        // Estilos para cada dato del reporte
        ventas.getStyle().set("font-size", "1.1em"); // Tamaño de fuente para las ventas
        productos.getStyle().set("font-size", "1.1em"); // Tamaño de fuente para los productos vendidos
        total.getStyle().set("font-size", "1.2em") // Tamaño de fuente más grande para el total generado
                        .set("font-weight", "bold") // Estilo en negrita
                        .set("color", "#1e40af"); // Color azul para el total generado

        // Agregar todos los componentes al contenedor de la tarjeta
        card.add(titulo, fecha, ventas, productos, total);

        // Finalmente, agregar la tarjeta al layout principal
        add(card);
    }
}
