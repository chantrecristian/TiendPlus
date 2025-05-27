package com.tiendplus.views.admin;

import com.tiendplus.models.Cliente;
import com.tiendplus.repositories.ClienteRepository;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "clientes", layout = MainView.class)
public class ClientesView extends VerticalLayout {

    private final ClienteRepository clienteRepository;
    private final Grid<Cliente> grid = new Grid<>(Cliente.class);

    public ClientesView(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;

        setPadding(true);
        setSpacing(true);

        // Configurar columnas manualmente
        grid.removeAllColumns();
        grid.addColumn(Cliente::getId).setHeader("ID");
        grid.addColumn(Cliente::getNombre).setHeader("Nombre");
        grid.addColumn(Cliente::getIdentificacion).setHeader("N° Documento");

        try {
            // Obtener datos desde la base de datos
            List<Cliente> clientes = clienteRepository.findAll();
            grid.setItems(clientes);
            add(grid);

            // Notificación visual y en consola si carga exitosamente
            Notification.show("Clientes cargados correctamente", 3000, Notification.Position.BOTTOM_START)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            getUI().ifPresent(ui ->
                ui.getPage().executeJs("console.log('Clientes cargados correctamente. Total: ' + $0)", clientes.size())
            );

        } catch (Exception e) {
            // Notificación visual y en consola si falla la carga
            Notification.show("Error al cargar los clientes", 3000, Notification.Position.BOTTOM_START)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            getUI().ifPresent(ui ->
                ui.getPage().executeJs("console.error('Error al cargar clientes: ' + $0)", e.getMessage())
            );
        }
    }
}
