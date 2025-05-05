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
//ruta con el mainLayout2
@Route(value = "proveedor/proveedores", layout = MainLayout2.class)
public class ProveedoresView extends VerticalLayout {
    // Repositorio para acceder a los datos de los proveedores
    private final ProveedorRepository proveedorRepository;
    private final ProveedorGrid proveedorGrid = new ProveedorGrid();//muestra la lista
    private final ProveedorForm proveedorForm = new ProveedorForm();//formulario
    // Constructor de la vista, se arma toda la interfaz
    @Autowired
    public ProveedoresView(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;

        Button agregar = new Button("Agregar proveedor", e -> {
            proveedorForm.setProveedor(new Proveedor()); // Crea un proveedor vacío
            proveedorForm.setVisible(true);             // Muestra el formulario
        });
        // guardar un proveedor
        proveedorForm.setOnSave(proveedor -> {
            proveedorRepository.save(proveedor); // Guarda en la bd
            actualizarProveedores();             // Actualiza la tabla
            proveedorForm.setVisible(false);// Oculta el formulario
        });     
        //eliminar un proveedor
        proveedorForm.setOnDelete(proveedor -> {
            proveedorRepository.delete(proveedor); // Elimina de la bd
            actualizarProveedores();               // Actualiza la tabla
            proveedorForm.setVisible(false);// Oculta el formulario
        });
        //cuando se selecciona un proveedor
        proveedorGrid.getGrid().asSingleSelect().addValueChangeListener(event -> {
            Proveedor seleccionado = event.getValue();
            if (seleccionado != null) {
                proveedorForm.setProveedor(seleccionado); // Muestra proveedor en el formulario
                proveedorForm.setVisible(true);// Muestra el formulario
            }
        }); 

        //contiene la tabla y el formulario
        HorizontalLayout contenido = new HorizontalLayout(proveedorGrid, proveedorForm);
        contenido.setWidthFull();//ancho

        // Controlar tamaños
        proveedorGrid.setWidthFull();
        proveedorForm.setWidth("350px"); // ancho del formulario
        contenido.setFlexGrow(1, proveedorGrid);// La tabla puede crecer
        contenido.setFlexGrow(0, proveedorForm); // evita que el formulario crezca
        //agrega el boton y el contenido
        add(agregar, contenido);
        actualizarProveedores();// Carga inicial de la tabla de proveedores
        proveedorForm.setVisible(false);//oculta el formulario
    }
    // Método que actualiza los datos en la tabla desde la base de datos
    private void actualizarProveedores() {
        List<Proveedor> lista = proveedorRepository.findAll();// Obtener proveedores
        proveedorGrid.setItems(lista);// Mostrar en la tabla
    }    
}