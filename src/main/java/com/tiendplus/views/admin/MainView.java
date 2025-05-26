package com.tiendplus.views.admin;

// Importación de clases necesarias para crear la interfaz y manejar eventos
import java.util.Optional;

import com.tiendplus.alertas.LoggerUI;
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
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;

/**
 * Esta clase representa la vista principal del sistema para el rol de administrador.
 * Contiene el encabezado superior, el menú lateral (drawer) y se encarga de mostrar
 * el título de cada sección según la vista actual.
 */
public class MainView extends AppLayout {

    private final Tabs menu;       // Menú lateral con las secciones del sistema
    private H1 viewTitle;          // Título que se muestra en la parte superior

    // Constructor: se llama automáticamente al abrir esta vista
    public MainView() {
        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        menu = createMenu();
        addToDrawer(createDrawerContent(menu));
        LoggerUI.logInfo("Vista principal de administrador cargada.");
    }

    // ==========================
    // ENCABEZADO SUPERIOR (HEADER)
    // ==========================
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
        viewTitle.getStyle()
            .set("font-size", "1.8em")
            .set("margin", "0")
            .set("font-weight", "bold");

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

        userMenu.addItem("Provedor", e -> {
            LoggerUI.logInfo("Navegando a vista de productos del proveedor.");
            getUI().ifPresent(ui -> ui.navigate("proveedor/productos"));
        });

        userMenu.addItem("Cajero", e -> {
            LoggerUI.logInfo("Navegando a vista de cajero.");
            getUI().ifPresent(ui -> ui.navigate("registrar-venta"));
        });

        userMenu.addItem("Cerrar sesión", e -> {
            LoggerUI.logInfo("Sesión cerrada por el usuario.");
            VaadinSession.getCurrent().close();
            getUI().ifPresent(ui -> ui.getPage().setLocation("login"));
        });

        layout.add(avatar);
        return layout;
    }

    // ==========================
    // MENÚ LATERAL (DRAWER)
    // ==========================
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
        title.getStyle()
            .set("font-weight", "bold")
            .set("font-size", "1.3em");

        HorizontalLayout logoLayout = new HorizontalLayout(logo, title);
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        logoLayout.setSpacing(true);
        logoLayout.setPadding(true);
        logoLayout.setWidthFull();

        layout.add(logoLayout, menu);
        return layout;
    }

    // ==========================
    // OPCIONES DEL MENÚ
    // ==========================
    private Tabs createMenu() {
        Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        tabs.setWidthFull();
        tabs.setId("tabs");

        tabs.add(createTab("Inicio", MenuAdminView.class));
        tabs.add(createTab("Inventario", InventarioView.class));
        tabs.add(createTab("Ventas", VentasView.class));
        tabs.add(createTab("Reportes", ReportesView.class));
        tabs.add(createTab("clientes", ClientesView.class));

        return tabs;
    }

    private static Tab createTab(String text, Class<? extends Component> navigationTarget) {
        RouterLink link = new RouterLink(text, navigationTarget);
        Tab tab = new Tab(link);
        ComponentUtil.setData(tab, Class.class, navigationTarget);
        return tab;
    }

    // ==========================
    // CAMBIO DE VISTA (NAVEGACIÓN)
    // ==========================
    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        getTabForComponent(getContent()).ifPresent(menu::setSelectedTab);
        String title = getCurrentPageTitle();
        viewTitle.setText(title);
        LoggerUI.logInfo("Vista actual: " + title);
    }

    private Optional<Tab> getTabForComponent(Component component) {
        return menu.getChildren()
                .filter(tab -> ComponentUtil.getData(tab, Class.class).equals(component.getClass()))
                .findFirst()
                .map(Tab.class::cast);
    }

    private String getCurrentPageTitle() {
        PageTitle annotation = getContent().getClass().getAnnotation(PageTitle.class);
        return annotation != null ? annotation.value() : "TiendPlus";
    }
}
