package com.tiendplus.views.admin;

import com.tiendplus.models.Cliente;
import com.tiendplus.repositories.ClienteRepository;
import com.vaadin.flow.component.grid.Grid;
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

        // Obtener datos desde la base de datos
        List<Cliente> clientes = clienteRepository.findAll();
        grid.setItems(clientes);

        // Agregar el grid al layout
        add(grid);
    }
}
