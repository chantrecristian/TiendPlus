package com.tiendplus.views.cajero;

import com.tiendplus.alertas.LoggerUI;
import com.tiendplus.models.Cliente;
import com.tiendplus.models.VentaFiada;
import com.tiendplus.repositories.ClienteRepository;
import com.tiendplus.repositories.VentaFiadaRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import jakarta.annotation.PostConstruct;

import org.apache.xmlbeans.impl.store.Locale;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;

@Route("pagos-fiados")
public class PagosFiadosView extends VerticalLayout {

    @Autowired
    private VentaFiadaRepository ventaFiadaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    private TextField numeroDocumentoField;
    private Button buscarButton;
    private Grid<VentaFiada> ventasGrid;
    private Button pagarEfectivoBtn;
    private Button abonarButton;
    private NumberField abonoField;
    private VentaFiada ventaSeleccionada;

    @PostConstruct
public void init() {
    numeroDocumentoField = new TextField("Número Documento del Cliente");

    buscarButton = new Button("Buscar Ventas Fiadas", e -> {
        LoggerUI.logInfo("Botón 'Buscar Ventas Fiadas' presionado");
        buscarVentasFiadas();
    });

    // Agrupar númeroDocumentoField y buscarButton en una sola fila
    HorizontalLayout busquedaLayout = new HorizontalLayout(numeroDocumentoField, buscarButton);
    busquedaLayout.setAlignItems(Alignment.END); // Opcional: alinear verticalmente

    ventasGrid = new Grid<>(VentaFiada.class);
    ventasGrid.removeAllColumns();
    ventasGrid.addColumn(VentaFiada::getIdFiado).setHeader("ID");
    ventasGrid.addColumn(VentaFiada::getFechaVenta).setHeader("Fecha Venta");

    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
    symbols.setGroupingSeparator('.');
    symbols.setDecimalSeparator(',');

    DecimalFormat formato = new DecimalFormat("#,##0", symbols);

    ventasGrid.addColumn(venta -> "$" + formato.format(venta.getMontoFiado()))
            .setHeader("Monto Fiado");

    ventasGrid.asSingleSelect().addValueChangeListener(e -> {
        ventaSeleccionada = e.getValue();
        if (ventaSeleccionada != null) {
            LoggerUI.logInfo("Venta seleccionada: ID=" + ventaSeleccionada.getIdFiado());
        }
    });

    abonoField = new NumberField("Monto a Abonar");
    abonarButton = new Button("Abonar", e -> abonarAFiado());
    pagarEfectivoBtn = new Button("Pagar en Efectivo", e -> pagarVenta("EFECTIVO"));

    // Agrupar abonoField, abonarButton y pagarEfectivoBtn en una fila
    HorizontalLayout abonoLayout = new HorizontalLayout(abonoField, abonarButton, pagarEfectivoBtn);
    abonoLayout.setAlignItems(Alignment.END); // Opcional: alinear verticalmente

    // Agregar todo al layout principal
    add(busquedaLayout, ventasGrid, abonoLayout);
}


    private void buscarVentasFiadas() {
        LoggerUI.logInfo("Iniciando búsqueda de ventas fiadas");

        String numeroDocumento = numeroDocumentoField.getValue();

        if (numeroDocumento == null || numeroDocumento.isEmpty()) {
            Notification.show("❌ Por favor ingrese el número de documento del cliente");
            return;
        }

        Cliente cliente = clienteRepository.findByNDocumento(numeroDocumento);

        if (cliente == null) {
            Notification.show("❌ Cliente no encontrado");
            return;
        }

        List<VentaFiada> ventasFiadas = ventaFiadaRepository.findByIdCliente(cliente.getId());
        ventasGrid.setItems(ventasFiadas);

        LoggerUI.logInfo("Ventas fiadas encontradas: " + ventasFiadas.size());
        Notification.show("✅ Ventas fiadas cargadas correctamente!");
    }

    private void pagarVenta(String metodo) {
        if (ventaSeleccionada == null) {
            Notification.show("❌ Seleccione una venta");
            return;
        }

        // Aquí podrías guardar el método de pago en otra entidad o hacer lógica adicional.
        ventaFiadaRepository.delete(ventaSeleccionada);
        Notification.show("✅ Venta pagada con éxito. Método de pago: " + metodo);
        buscarVentasFiadas();
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

        Double nuevoTotal = ventaSeleccionada.getMontoFiado() - abono;

        if (nuevoTotal <= 0) {
            ventaFiadaRepository.delete(ventaSeleccionada);
            Notification.show("✅ Abono cubre toda la deuda, venta eliminada");
        } else {
            ventaSeleccionada.setMontoFiado(nuevoTotal);
            ventaFiadaRepository.save(ventaSeleccionada);
            Notification.show("✅ Abono registrado. Nuevo total: " + nuevoTotal);
        }

        buscarVentasFiadas();
    }
}
