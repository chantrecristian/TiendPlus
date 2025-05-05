package com.tiendplus.views.admin;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "menu-admin", layout = MainView.class)
@PageTitle("Menú")
public class MenuAdminView extends VerticalLayout {

    public MenuAdminView() {
        setSizeFull();
        setPadding(true);
        setAlignItems(Alignment.CENTER);

        Span bienvenida = new Span("Bienvenido al menú");
        bienvenida.getStyle().set("font-size", "1.8em").set("font-weight", "bold");

        Span subtitulo = new Span("Gestiona tu tienda de manera rápida y sencilla.");
        subtitulo.getStyle().set("font-size", "1.1em").set("color", "gray");

        HorizontalLayout accesos = new HorizontalLayout();
        accesos.setSpacing(true);
        accesos.setPadding(true);

        VerticalLayout inventarioCard = createCard("📦 Inventario", "Administra tus productos", "inventario");
        VerticalLayout ventasCard = createCard("💵 Ventas", "Revisa tus transacciones", "ventas");
        VerticalLayout reportesCard = createCard("📊 Reportes", "Visualiza tus informes", "reportes");

        accesos.add(inventarioCard, ventasCard, reportesCard);

        add(bienvenida, subtitulo, accesos);
    }

    private VerticalLayout createCard(String title, String description, String route) {
        VerticalLayout card = new VerticalLayout();
        card.getStyle().set("border", "1px solid #ccc");
        card.getStyle().set("border-radius", "12px");
        card.getStyle().set("padding", "16px");
        card.getStyle().set("width", "200px");
        card.getStyle().set("cursor", "pointer");
        card.getStyle().set("box-shadow", "2px 2px 6px rgba(0,0,0,0.1)");

        Span titleLabel = new Span(title);
        titleLabel.getStyle().set("font-weight", "bold");

        Span descLabel = new Span(description);
        descLabel.getStyle().set("font-size", "0.9em").set("color", "gray");

        card.add(titleLabel, descLabel);
        card.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(route)));

        return card;
    }
}
