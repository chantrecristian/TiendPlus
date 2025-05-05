package com.tiendplus.views.admin;

// Importación de clases necesarias para crear la interfaz y manejar eventos
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
        // Define que el menú lateral es la parte principal del diseño
        setPrimarySection(Section.DRAWER);

        // Agrega el encabezado (título, avatar y botón de menú) al diseño
        addToNavbar(true, createHeaderContent());

        // Crea el menú lateral y lo agrega al diseño
        menu = createMenu();
        addToDrawer(createDrawerContent(menu));
    }

    // ==========================
    // ENCABEZADO SUPERIOR (HEADER)
    // ==========================
    private Component createHeaderContent() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setId("header");
        layout.setWidthFull();  // Usa todo el ancho
        layout.setPadding(true);
        layout.setSpacing(true);
        layout.setAlignItems(FlexComponent.Alignment.CENTER); // Alinea los elementos verticalmente
        layout.getStyle().set("background", "var(--lumo-base-color)");
        layout.getStyle().set("box-shadow", "0 2px 4px rgba(0,0,0,0.1)");

        // Botón para mostrar/ocultar el menú lateral
        DrawerToggle toggle = new DrawerToggle();
        layout.add(toggle);

        // Título que se ve en la parte superior
        viewTitle = new H1("TiendPluss");
        viewTitle.getStyle()
            .set("font-size", "1.8em")
            .set("margin", "0")
            .set("font-weight", "bold");

        // Centra el título horizontalmente
        HorizontalLayout titleWrapper = new HorizontalLayout(viewTitle);
        titleWrapper.setWidthFull();
        titleWrapper.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        titleWrapper.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.add(titleWrapper);

        // Imagen del avatar del usuario (circular)
        Avatar avatar = new Avatar("Usuario");
        avatar.setImage("icons/user.png");
        avatar.setHeight("36px");
        avatar.setWidth("36px");

        // Menú que se despliega al hacer clic en el avatar
        ContextMenu userMenu = new ContextMenu(avatar);
        userMenu.setOpenOnClick(true);
        userMenu.addItem("Provedor", e -> {
            getUI().ifPresent(ui -> ui.navigate("proveedor/productos"));
        });
        userMenu.addItem("Cajero", e -> {
            getUI().ifPresent(ui -> ui.navigate("En uso proximamente"));
        });
        
        userMenu.addItem("Cerrar sesión", e -> {
            // Cierra la sesión actual y redirige a la página de login
            VaadinSession.getCurrent().close();
            getUI().ifPresent(ui -> ui.getPage().setLocation("login"));
        });

        layout.add(avatar); // Se agrega el avatar al encabezado
        return layout;      // Se devuelve el encabezado completo
    }

    // ==========================
    // MENÚ LATERAL (DRAWER)
    // ==========================
    private Component createDrawerContent(Tabs menu) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setPadding(true);
        layout.setSpacing(true);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH); // Que se estire según el tamaño

        // Logo de la aplicación (imagen de la tienda)
        Image logo = new Image("images/tienda1.jpeg", "Logo");
        logo.setHeight("60px");
        logo.getStyle()
            .set("border-radius", "8px")
            .set("box-shadow", "0 2px 6px rgba(0,0,0,0.15)")
            .set("margin-right", "8px");

        // Título al lado del logo
        Span title = new Span("TiendPlus Admin");
        title.getStyle()
            .set("font-weight", "bold")
            .set("font-size", "1.3em");

        // Agrupación del logo y el título
        HorizontalLayout logoLayout = new HorizontalLayout(logo, title);
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        logoLayout.setSpacing(true);
        logoLayout.setPadding(true);
        logoLayout.setWidthFull();

        // Agrega el logo y el menú al layout vertical
        layout.add(logoLayout, menu);
        return layout; // Devuelve el contenido del menú lateral
    }

    // ==========================
    // OPCIONES DEL MENÚ
    // ==========================
    private Tabs createMenu() {
        Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL); // Menú vertical
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL); // Estilo minimalista
        tabs.setWidthFull();
        tabs.setId("tabs");

        // Se agregan las secciones disponibles
        tabs.add(createTab("Inicio", MenuAdminView.class));
        tabs.add(createTab("Inventario", InventarioView.class));
        tabs.add(createTab("Ventas", VentasView.class));
        tabs.add(createTab("Reportes", ReportesView.class));

        return tabs;
    }

    // Crea una pestaña del menú con un enlace a una vista
    private static Tab createTab(String text, Class<? extends Component> navigationTarget) {
        RouterLink link = new RouterLink(text, navigationTarget); // Crea el enlace a la vista
        Tab tab = new Tab(link); // Crea la pestaña que contiene ese enlace
        ComponentUtil.setData(tab, Class.class, navigationTarget); // Guarda la clase de destino
        return tab;
    }

    // ==========================
    // CUANDO EL USUARIO NAVEGA ENTRE VISTAS
    // ==========================
    @Override
    protected void afterNavigation() {
        super.afterNavigation();

        // Marca como seleccionada la pestaña correspondiente a la vista actual
        getTabForComponent(getContent()).ifPresent(menu::setSelectedTab);

        // Cambia el título de la parte superior por el título de la vista actual
        viewTitle.setText(getCurrentPageTitle());
    }

    // Busca cuál pestaña del menú corresponde a la vista actual
    private Optional<Tab> getTabForComponent(Component component) {
        return menu.getChildren()
                .filter(tab -> ComponentUtil.getData(tab, Class.class).equals(component.getClass()))
                .findFirst()
                .map(Tab.class::cast);
    }

    // Obtiene el título de la vista actual (desde la anotación @PageTitle)
    private String getCurrentPageTitle() {
        PageTitle annotation = getContent().getClass().getAnnotation(PageTitle.class);
        return annotation != null ? annotation.value() : "TiendPlus";
    }
}
