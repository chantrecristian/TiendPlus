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
import java.math.BigDecimal;
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
            Notification.show("🔍 Buscando ventas fiadas...");
            buscarVentasFiadas();
        });

        ventasGrid = new Grid<>(Venta.class);
        ventasGrid.setColumns("idVenta", "fechaVenta", "total", "metodoPago");
        ventasGrid.asSingleSelect().addValueChangeListener(e -> ventaSeleccionada = e.getValue());

        pagarEfectivoBtn = new Button("Pagar en Efectivo", e -> pagarVenta("Efectivo"));
        pagarTarjetaBtn = new Button("Pagar con Tarjeta", e -> pagarVenta("Tarjeta"));
        pagarDigitalBtn = new Button("Pagar Digital", e -> pagarVenta("Digital"));
        abonoField = new NumberField("Monto a Abonar");
        abonarButton = new Button("Abonar", e -> abonarAFiado());

        add(idClienteField, buscarButton, ventasGrid, abonoField, abonarButton, pagarEfectivoBtn, pagarTarjetaBtn, pagarDigitalBtn);
    }

    private void buscarVentasFiadas() {
        String idClienteStr = idClienteField.getValue();
        if (idClienteStr == null || idClienteStr.isEmpty()) {
            Notification.show("❌ Por favor ingrese el ID del cliente");
            return;
        }

        try {
            Long idCliente = Long.parseLong(idClienteStr);
            Cliente cliente = clienteRepository.findById(idCliente).orElse(null);
            if (cliente == null) {
                Notification.show("❌ Cliente no encontrado");
                return;
            }

            // 🔍 Verificación en consola antes de la consulta
            System.out.println("Ejecutando consulta de ventas fiadas...");
            System.out.println("Cliente ID: " + cliente.getId());

            List<Venta> ventasFiadas = ventaRepository.findByClienteAndMetodoPago(cliente.getId(), "Fiado");

            // 🔍 Confirmación en consola
            System.out.println("Ventas fiadas encontradas: " + ventasFiadas.size());

            ventasGrid.setItems(ventasFiadas);
            ventasGrid.getDataProvider().refreshAll();

            Notification.show("✅ Ventas fiadas cargadas correctamente!");
        } catch (NumberFormatException ex) {
            Notification.show("❌ ID inválido");
        }
    }

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

        BigDecimal nuevoTotal = ventaSeleccionada.getTotal().subtract(BigDecimal.valueOf(abono));

        if (nuevoTotal.compareTo(BigDecimal.ZERO) <= 0) {
            ventaSeleccionada.setMetodoPago("Efectivo");
            ventaSeleccionada.setTotal(BigDecimal.ZERO);
            Notification.show("✅ Abono cubre toda la deuda, venta marcada como pagada");
        } else {
            ventaSeleccionada.setTotal(nuevoTotal);
            Notification.show("✅ Abono registrado. Nuevo total: " + nuevoTotal);
        }

        ventaRepository.save(ventaSeleccionada);
        buscarVentasFiadas();
    }
}
