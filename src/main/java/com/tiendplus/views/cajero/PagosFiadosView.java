package com.tiendplus.views.cajero;

import com.tiendplus.alertas.LoggerUI;
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
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route("pagos-fiados")  // Define la vista en la aplicación
public class PagosFiadosView extends VerticalLayout {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    private TextField idClienteField;
    private Button buscarButton;
    private Grid<Venta> ventasGrid;
    private Button pagarEfectivoBtn;
    private Button pagarTarjetaBtn;
    private Button pagarDigitalBtn;
    private Button abonarButton;
    private NumberField abonoField;
    private Venta ventaSeleccionada;

    @PostConstruct
    public void init() {
        idClienteField = new TextField("Numero Documento del Cliente");

        buscarButton = new Button("Buscar Ventas Fiadas", e -> {
            LoggerUI.logInfo("Botón 'Buscar Ventas Fiadas' presionado");
            buscarVentasFiadas();
        });

        ventasGrid = new Grid<>(Venta.class);
        ventasGrid.setColumns("id", "fechaVenta", "total", "metodoPago");
        ventasGrid.asSingleSelect().addValueChangeListener(e -> {
            ventaSeleccionada = e.getValue();
            if (ventaSeleccionada != null) {
                LoggerUI.logInfo("Venta seleccionada: ID=" + ventaSeleccionada.getId());
            }
        });

        pagarEfectivoBtn = new Button("Pagar en Efectivo", e -> pagarVenta("EFECTIVO"));
        pagarTarjetaBtn = new Button("Pagar con Tarjeta", e -> pagarVenta("TARJETA"));
        pagarDigitalBtn = new Button("Pagar Digital", e -> pagarVenta("DIGITAL"));
        abonoField = new NumberField("Monto a Abonar");
        abonarButton = new Button("Abonar", e -> abonarAFiado());

        add(idClienteField, buscarButton, ventasGrid, abonoField, abonarButton, pagarEfectivoBtn, pagarTarjetaBtn, pagarDigitalBtn);
    }

    private void buscarVentasFiadas() {
        LoggerUI.logInfo("Iniciando búsqueda de ventas fiadas");

        String idClienteStr = idClienteField.getValue();
        LoggerUI.logInfo("ID ingresado: " + idClienteStr);

        if (idClienteStr == null || idClienteStr.isEmpty()) {
            Notification.show("❌ Por favor ingrese el ID del cliente");
            LoggerUI.logInfo("ID del cliente no ingresado");
            return;
        }

        try {
            Long idCliente = Long.parseLong(idClienteStr);
            Cliente cliente = clienteRepository.findById(idCliente).orElse(null);

            if (cliente == null) {
                Notification.show("❌ Cliente no encontrado");
                LoggerUI.logInfo("Cliente no encontrado con ID: " + idCliente);
                return;
            }

            List<Venta> ventasFiadas = ventaRepository.findByClienteAndMetodoPago(cliente, "Fiado");
            ventasGrid.setItems(ventasFiadas);

            LoggerUI.logInfo("Ventas fiadas encontradas: " + ventasFiadas.size());
            Notification.show("✅ Ventas fiadas cargadas correctamente!");

        } catch (NumberFormatException ex) {
            Notification.show("❌ ID inválido");
            LoggerUI.logInfo("Error al convertir ID: " + idClienteStr);
        }
    }

    private void pagarVenta(String metodo) {
        if (ventaSeleccionada == null) {
            Notification.show("❌ Seleccione una venta");
            LoggerUI.logInfo("No se ha seleccionado ninguna venta para pagar");
            return;
        }

        ventaSeleccionada.setMetodoPago(metodo);
        ventaRepository.save(ventaSeleccionada);
        Notification.show("✅ Venta pagada con éxito. Método de pago: " + metodo);
        LoggerUI.logInfo("Venta ID=" + ventaSeleccionada.getId() + " pagada con método: " + metodo);

        buscarVentasFiadas();
    }

    private void abonarAFiado() {
        if (ventaSeleccionada == null) {
            Notification.show("❌ Seleccione una venta");
            LoggerUI.logInfo("No se ha seleccionado ninguna venta para abonar");
            return;
        }

        Double abono = abonoField.getValue();
        if (abono == null || abono <= 0) {
            Notification.show("❌ Ingrese un monto válido");
            LoggerUI.logInfo("Monto de abono inválido: " + abono);
            return;
        }

        Double nuevoTotal = ventaSeleccionada.getTotal() - abono;

        if (nuevoTotal <= 0) {
            ventaSeleccionada.setMetodoPago("Efectivo");
            ventaSeleccionada.setTotal(0.0);
            Notification.show("✅ Abono cubre toda la deuda, venta marcada como pagada");
            LoggerUI.logInfo("Venta ID=" + ventaSeleccionada.getId() + " abonada totalmente");
        } else {
            ventaSeleccionada.setTotal(nuevoTotal);
            Notification.show("✅ Abono registrado. Nuevo total: " + nuevoTotal);
            LoggerUI.logInfo("Venta ID=" + ventaSeleccionada.getId() + " abonada. Nuevo total: " + nuevoTotal);
        }

        ventaRepository.save(ventaSeleccionada);
        buscarVentasFiadas();
    }
}
