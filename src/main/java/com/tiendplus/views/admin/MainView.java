package com.tiendplus.views.admin;

import java.util.Optional;

import com.tiendplus.views.cajero.VentasView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.router.Route;

@Route("menu-admin")
public class MainView extends AppLayout {

    private final Tabs menu;
    private H1 viewTitle;

    public MainView() {
        setPrimarySection(Section.DRAWER);

        // Header y Drawer
        addToNavbar(true, createHeaderContent());
        menu = createMenu();
        addToDrawer(createDrawerContent(menu));
    }

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

        viewTitle = new H1("TiendPlus");
        viewTitle.getStyle().set("font-size", "1.5em");
        viewTitle.getStyle().set("margin", "0");

        layout.add(viewTitle);
        layout.expand(viewTitle);

        Image avatar = new Image("images/user.svg", "Usuario");
        avatar.setHeight("32px");
        layout.add(avatar);

        return layout;
    }

    private Component createDrawerContent(Tabs menu) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setPadding(true);
        layout.setSpacing(true);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);

        // Logo
        Image logo = new Image("images/logo.png", "Logo");
        logo.setHeight("60px");

        Span title = new Span("TiendPlus Admin");
        title.getStyle().set("font-weight", "bold");
        title.getStyle().set("font-size", "1.2em");

        HorizontalLayout logoLayout = new HorizontalLayout(logo, title);
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        logoLayout.setSpacing(true);
        logoLayout.setPadding(true);
        logoLayout.setWidthFull();

        layout.add(logoLayout, menu);
        return layout;
    }

    private Tabs createMenu() {
        Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        tabs.setWidthFull();
        tabs.setId("tabs");
        tabs.add(createMenuItems());
        return tabs;
    }

    private Component[] createMenuItems() {
        return new Tab[] {
            createTab("Inventario", InventarioView.class),
            createTab("Ventas", VentasView.class),
            createTab("Reportes", ReportesView.class)
        };
    }

    private static Tab createTab(String text, Class<? extends Component> navigationTarget) {
        RouterLink link = new RouterLink(text, navigationTarget);
        // Removed invalid HighlightCondition field usage
        Tab tab = new Tab(link);
        ComponentUtil.setData(tab, Class.class, navigationTarget);
        return tab;
    }

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
