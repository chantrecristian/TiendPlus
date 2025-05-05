package com.tiendplus.views.proveedor;

import com.tiendplus.views.proveedor.grids.ProveedorGrid;
import com.tiendplus.models.Proveedor;
import com.tiendplus.repositories.ProveedorRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "proveedor/proveedores", layout = MainLayout2.class)
public class ProveedoresView extends VerticalLayout {

    private final ProveedorRepository proveedorRepository;
    private final ProveedorGrid proveedorGrid = new ProveedorGrid();
    private final ProveedorForm proveedorForm = new ProveedorForm();

    @Autowired
    public ProveedoresView(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;

        Button agregar = new Button("Agregar proveedor", e -> {
            proveedorForm.setProveedor(new Proveedor());
            proveedorForm.setVisible(true);
        });

        proveedorForm.setOnSave(proveedor -> {
            proveedorRepository.save(proveedor);
            actualizarProveedores();
            proveedorForm.setVisible(false);
        });     
        
        proveedorForm.setOnDelete(proveedor -> {
            proveedorRepository.delete(proveedor);
            actualizarProveedores();
            proveedorForm.setVisible(false);
        });

        proveedorGrid.getGrid().asSingleSelect().addValueChangeListener(event -> {
            Proveedor seleccionado = event.getValue();
            if (seleccionado != null) {
                proveedorForm.setProveedor(seleccionado);
                proveedorForm.setVisible(true);
            }
        }); 

        // Contenedor horizontal
        HorizontalLayout contenido = new HorizontalLayout(proveedorGrid, proveedorForm);
        contenido.setWidthFull();

        // Controlar tamaños
        proveedorGrid.setWidthFull();
        proveedorForm.setWidth("350px"); // ancho del formulario
        contenido.setFlexGrow(1, proveedorGrid);
        contenido.setFlexGrow(0, proveedorForm); // evita que el form crezca demasiado

        add(agregar, contenido);
        actualizarProveedores();
        proveedorForm.setVisible(false);
    }

    private void actualizarProveedores() {
        List<Proveedor> lista = proveedorRepository.findAll();
        proveedorGrid.setItems(lista);
    }    
}