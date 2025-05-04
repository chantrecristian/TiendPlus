package com.tiendplus.views.admin;

import java.util.Optional;

import com.tiendplus.views.cajero.VentasView;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;

@Route("menu-admin")
@PageTitle("Menú")
public class MainView extends AppLayout {

    private final Tabs menu;
    private H1 viewTitle;

    public MainView() {
        setPrimarySection(Section.DRAWER);

        // Header
        addToNavbar(true, createHeaderContent());

        // Menú lateral
        menu = createMenu();
        addToDrawer(createDrawerContent(menu));

        // Pantalla de bienvenida con accesos rápidos
        setContent(createWelcomeContent());
    }

    // HEADER
    private Component createHeaderContent() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setId("header");
        layout.setWidthFull();
        layout.setPadding(true);
        layout.setSpacing(true);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.getStyle().set("background", "var(--lumo-base-color)");
        layout.getStyle().set("box-shadow", "0 2px 4px rgba(0,0,0,0.1)");

        DrawerToggle toggle = new DrawerToggle();
        layout.add(toggle);

        viewTitle = new H1("TiendPluss");
        viewTitle.getStyle().set("font-size", "1.8em").set("margin", "0").set("font-weight", "bold");

        HorizontalLayout titleWrapper = new HorizontalLayout(viewTitle);
        titleWrapper.setWidthFull();
        titleWrapper.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        titleWrapper.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.add(titleWrapper);

        Avatar avatar = new Avatar("Usuario");
        avatar.setImage("icons/user.png");
        avatar.setHeight("36px");
        avatar.setWidth("36px");

        ContextMenu userMenu = new ContextMenu(avatar);
        userMenu.setOpenOnClick(true);
        userMenu.addItem("Perfil", e -> Notification.show("Funcionalidad de perfil próximamente..."));
        userMenu.addItem("Cerrar sesión", e -> {
            VaadinSession.getCurrent().close();
            getUI().ifPresent(ui -> ui.getPage().setLocation("login"));
        });

        layout.add(avatar);
        return layout;
    }

    // DRAWER (Menú lateral)
    private Component createDrawerContent(Tabs menu) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setPadding(true);
        layout.setSpacing(true);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);

        Image logo = new Image("images/tienda1.jpeg", "Logo");
        logo.setHeight("60px");
        logo.getStyle()
            .set("border-radius", "8px")
            .set("box-shadow", "0 2px 6px rgba(0,0,0,0.15)")
            .set("margin-right", "8px");

        Span title = new Span("TiendPlus Admin");
        title.getStyle().set("font-weight", "bold").set("font-size", "1.3em");

        HorizontalLayout logoLayout = new HorizontalLayout(logo, title);
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        logoLayout.setSpacing(true);
        logoLayout.setPadding(true);
        logoLayout.setWidthFull();

        layout.add(logoLayout, menu);
        return layout;
    }

    // MENÚ LATERAL
    private Tabs createMenu() {
        Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        tabs.setWidthFull();
        tabs.setId("tabs");

        tabs.add(createTab("Inventario", InventarioView.class));
        tabs.add(createTab("Ventas", VentasView.class));
        tabs.add(createTab("Reportes", ReportesView.class));

        return tabs;
    }

    private static Tab createTab(String text, Class<? extends Component> navigationTarget) {
        RouterLink link = new RouterLink(text, navigationTarget);
        Tab tab = new Tab(link);
        ComponentUtil.setData(tab, Class.class, navigationTarget);
        return tab;
    }

    // BIENVENIDA + ACCESOS RÁPIDOS
    private Component createWelcomeContent() {
        VerticalLayout contentLayout = new VerticalLayout();
        contentLayout.setSizeFull();
        contentLayout.setPadding(true);
        contentLayout.setAlignItems(FlexComponent.Alignment.CENTER);

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

        contentLayout.add(bienvenida, subtitulo, accesos);
        return contentLayout;
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

    // Actualiza el título según navegación
    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        getTabForComponent(getContent()).ifPresent(menu::setSelectedTab);
        viewTitle.setText(getCurrentPageTitle());
    }

    private Optional<Tab> getTabForComponent(Component component) {
        return menu.getChildren()
                .filter(tab -> ComponentUtil.getData(tab, Class.class).equals(component.getClass()))
                .findFirst().map(Tab.class::cast);
    }

    private String getCurrentPageTitle() {
        PageTitle annotation = getContent().getClass().getAnnotation(PageTitle.class);
        return annotation != null ? annotation.value() : "TiendPlus";
    }
}
