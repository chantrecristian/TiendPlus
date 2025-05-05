package com.tiendplus.views.admin;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;

@PageTitle("Reportes")
@Route(value = "reportes", layout = MainView.class)
public class ReportesView extends VerticalLayout {

    public ReportesView() {
        // Ocupa toda la pantalla y centra el contenido
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        getStyle().set("background", "linear-gradient(135deg, #f3f4f6, #e0e7ff)");

        // Contenedor estilo tarjeta
        VerticalLayout card = new VerticalLayout();
        card.setWidth("80%");
        card.setPadding(true);
        card.setSpacing(true);
        card.setAlignItems(Alignment.CENTER);
        card.getStyle()
            .set("background-color", "#ffffff")
            .set("border-radius", "12px")
            .set("box-shadow", "0 6px 12px rgba(0,0,0,0.1)")
            .set("padding", "32px");

        // Título
        H2 titulo = new H2("📊 Reporte de Ventas del Día");
        titulo.getStyle().set("color", "#1e293b");

        // Fecha actual
        Span fecha = new Span("Fecha: " + LocalDate.now());
        fecha.getStyle().set("color", "gray");

        // Datos simulados
        int totalVentas = 15;
        int productosVendidos = 42;
        double totalGenerado = 328000; // en pesos

        Span ventas = new Span("🧾 Ventas realizadas: " + totalVentas);
        Span productos = new Span("📦 Productos vendidos: " + productosVendidos);
        Span total = new Span("💰 Total generado: $" + totalGenerado);

        // Estilos básicos
        ventas.getStyle().set("font-size", "1.1em");
        productos.getStyle().set("font-size", "1.1em");
        total.getStyle().set("font-size", "1.2em").set("font-weight", "bold").set("color", "#1e40af");

        // Agregar al contenedor
        card.add(titulo, fecha, ventas, productos, total);
        add(card);
    }
}
