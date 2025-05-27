package com.tiendplus.views.cajero;

import com.tiendplus.models.VentaFiada;
import com.tiendplus.repositories.VentaFiadaRepository;
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

@Route("pagos-fiados")  
public class PagosFiadosView extends VerticalLayout {

    @Autowired
    private VentaFiadaRepository ventaFiadaRepository;

    private TextField documentoClienteField;
    private Button buscarButton;
    private Grid<VentaFiada> ventasGrid;
    private Button pagarEfectivoBtn;
    private Button pagarTarjetaBtn;
    private Button pagarDigitalBtn;
    private Button abonarButton;
    private NumberField abonoField;
    private VentaFiada ventaSeleccionada;

    @PostConstruct
    public void init() {
        documentoClienteField = new TextField("Número Documento del Cliente");
        buscarButton = new Button("Buscar Ventas Fiadas", e -> buscarVentasFiadas());

        ventasGrid = new Grid<>(VentaFiada.class);
        ventasGrid.setColumns("idFiado", "fechaVenta", "montoFiado");
        ventasGrid.asSingleSelect().addValueChangeListener(e -> ventaSeleccionada = e.getValue());

        pagarEfectivoBtn = new Button("Pagar en Efectivo", e -> pagarVenta("Efectivo"));
        pagarTarjetaBtn = new Button("Pagar con Tarjeta", e -> pagarVenta("Tarjeta"));
        pagarDigitalBtn = new Button("Pagar Digital", e -> pagarVenta("Digital"));
        abonoField = new NumberField("Monto a Abonar"); 
        abonarButton = new Button("Abonar", e -> abonarAFiado());

        add(documentoClienteField, buscarButton, ventasGrid, abonoField, abonarButton, pagarEfectivoBtn, pagarTarjetaBtn, pagarDigitalBtn);
    }

    /** 🔥 Método para buscar las ventas fiadas de un cliente por número de documento */
    private void buscarVentasFiadas() {
    String documentoClienteStr = documentoClienteField.getValue().trim();
    
    if (documentoClienteStr.isEmpty()) {
        Notification.show("❌ Ingrese un número de documento válido");
        return;
    }

    // ✅ Verificar si el documento existe en la BD
    boolean clienteExiste = ventaFiadaRepository.existsByDocumentoCliente(documentoClienteStr);
    if (!clienteExiste) {
        Notification.show("❌ No existe un cliente con el documento " + documentoClienteStr);
        return;
    }

    // ✅ Buscar las ventas fiadas asociadas al documento
    List<VentaFiada> ventasFiadas = ventaFiadaRepository.findByDocumentoClienteAndMetodoPago(documentoClienteStr, "Fiado");

    if (ventasFiadas.isEmpty()) {
        Notification.show("⚠️ No hay ventas fiadas registradas para este cliente.");
        return;
    }

    ventasGrid.setItems(ventasFiadas);
    Notification.show("✅ Ventas fiadas cargadas correctamente.");
}


    /** 🔥 Método para pagar una venta fiada con el método de pago seleccionado */
    private void pagarVenta(String metodo) {
        if (ventaSeleccionada == null) {
            Notification.show("❌ Seleccione una venta");
            return;
        }

        ventaSeleccionada.setMontoFiado(0.0);
        ventaFiadaRepository.save(ventaSeleccionada); // 🔹 Se guarda el cambio en la BD
        Notification.show("✅ Venta pagada con éxito. Método de pago: " + metodo);

        buscarVentasFiadas();
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

        Double nuevoTotal = ventaSeleccionada.getMontoFiado() - abono;

        if (nuevoTotal <= 0) {
            ventaSeleccionada.setMontoFiado(0.0);
            Notification.show("✅ Abono cubre toda la deuda, venta marcada como pagada");
        } else {
            ventaSeleccionada.setMontoFiado(nuevoTotal);
            Notification.show("✅ Abono registrado. Nuevo total: " + nuevoTotal);
        }

        ventaFiadaRepository.save(ventaSeleccionada); // 🔹 Se guarda el cambio en la BD
        buscarVentasFiadas();
    }
}
