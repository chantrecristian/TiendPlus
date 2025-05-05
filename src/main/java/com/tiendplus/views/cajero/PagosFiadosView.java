package com.tiendplus.views.cajero;

import com.tiendplus.models.Cliente;
import com.tiendplus.models.Venta;
import com.tiendplus.repositories.ClienteRepository;
import com.tiendplus.repositories.VentaRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.annotation.PostConstruct;
import java.util.List;

@Route("pagos-fiados")  // Define la vista en la aplicación
public class PagosFiadosView extends VerticalLayout {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    private TextField idClienteField;  // Campo para ID de cliente
    private Button buscarButton;  // Botón para buscar ventas fiadas
    private Grid<Venta> ventasGrid;  // Tabla de ventas fiadas
    private Button pagarEfectivoBtn;  // Botón para pagar en efectivo
    private Button pagarTarjetaBtn;  // Botón para pagar con tarjeta
    private Button pagarDigitalBtn;  // Botón para pagar digitalmente
    private Button abonarButton;  // Botón para abonar
    private NumberField abonoField;  // Campo de abono
    private Venta ventaSeleccionada;  // Venta seleccionada

    @PostConstruct
    public void init() {
        idClienteField = new TextField("ID del Cliente");
        
        buscarButton = new Button("Buscar Ventas Fiadas", e -> {
            Notification.show("Botón presionado! 🔍"); // 🔥 Confirma que el botón funciona
            buscarVentasFiadas();
        });

        ventasGrid = new Grid<>(Venta.class);
        ventasGrid.setColumns("id", "fechaVenta", "total", "metodoPago"); 
        ventasGrid.asSingleSelect().addValueChangeListener(e -> ventaSeleccionada = e.getValue());

        pagarEfectivoBtn = new Button("Pagar en Efectivo", e -> pagarVenta("EFECTIVO"));
        pagarTarjetaBtn = new Button("Pagar con Tarjeta", e -> pagarVenta("TARJETA"));
        pagarDigitalBtn = new Button("Pagar Digital", e -> pagarVenta("DIGITAL"));
        abonoField = new NumberField("Monto a Abonar");
        abonarButton = new Button("Abonar", e -> abonarAFiado());

        add(idClienteField, buscarButton, ventasGrid, abonoField, abonarButton, pagarEfectivoBtn, pagarTarjetaBtn, pagarDigitalBtn);
    }

    /** 🔥 Método para buscar las ventas fiadas de un cliente */
    private void buscarVentasFiadas() {
        Notification.show("Buscando ventas fiadas... 🔍");
    
        String idClienteStr = idClienteField.getValue();
        System.out.println("ID ingresado: " + idClienteStr); // 🔥 Verificación en consola
    
        if (idClienteStr == null || idClienteStr.isEmpty()) {
            Notification.show("❌ Por favor ingrese el ID del cliente");
            return;
        }
    
        try {
            Long idCliente = Long.parseLong(idClienteStr);
            Cliente cliente = clienteRepository.findById(idCliente).orElse(null);
    
            // 🔥 Confirmación en consola para verificar si el cliente se encuentra
            System.out.println("Cliente encontrado: " + cliente);
    
            if (cliente == null) {
                Notification.show("❌ Cliente no encontrado");
                return;
            }
    
            List<Venta> ventasFiadas = ventaRepository.findByClienteAndMetodoPago(cliente, "Fiado");
            System.out.println("Ventas fiadas encontradas: " + ventasFiadas);
            ventasGrid.setItems(ventasFiadas);
    
            Notification.show("✅ Ventas fiadas cargadas correctamente!");
    
        } catch (NumberFormatException ex) {
            Notification.show("❌ ID inválido");
        }
    }
    

    /** 🔥 Método para pagar una venta fiada con el método de pago seleccionado */
    private void pagarVenta(String metodo) {
        if (ventaSeleccionada == null) {
            Notification.show("❌ Seleccione una venta");
            return;
        }

        ventaSeleccionada.setMetodoPago(metodo);
        ventaRepository.save(ventaSeleccionada);
        Notification.show("✅ Venta pagada con éxito. Método de pago: " + metodo);

        buscarVentasFiadas(); // 🔄 Refrescar tabla de ventas fiadas
    }

    /** 🔥 Método para abonar a una venta fiada */
    private void abonarAFiado() {
        if (ventaSeleccionada == null) {
            Notification.show("❌ Seleccione una venta");
            return;
        }

        Double abono = abonoField.getValue();
        if (abono == null || abono <= 0) {
            Notification.show("❌ Ingrese un monto válido");
            return;
        }

        Double nuevoTotal = ventaSeleccionada.getTotal() - abono;

        if (nuevoTotal <= 0) {
            ventaSeleccionada.setMetodoPago("Efectivo");
            ventaSeleccionada.setTotal(0.0);
            Notification.show("✅ Abono cubre toda la deuda, venta marcada como pagada");
        } else {
            ventaSeleccionada.setTotal(nuevoTotal);
            Notification.show("✅ Abono registrado. Nuevo total: " + nuevoTotal);
        }

        ventaRepository.save(ventaSeleccionada);
        buscarVentasFiadas(); // 🔄 Refrescar tabla de ventas fiadas
    }
}
