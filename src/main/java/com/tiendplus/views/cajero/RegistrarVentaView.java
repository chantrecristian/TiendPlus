package com.tiendplus.views.cajero;

import com.tiendplus.complements.MainLayout;
import com.tiendplus.models.Cliente;
import com.tiendplus.models.DetalleVenta;
import com.tiendplus.models.Producto;
import com.tiendplus.models.Venta;
import com.tiendplus.models.VentaFiada;
import com.tiendplus.repositories.ClienteRepository;
import com.tiendplus.repositories.DetalleVentaRepository;
import com.tiendplus.repositories.ProductoRepository;
import com.tiendplus.repositories.VentaFiadaRepository;
import com.tiendplus.repositories.VentaRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Route(value = "registrar-venta", layout = MainLayout.class)
public class RegistrarVentaView extends VerticalLayout {

    private final ProductoRepository productoRepo;
    private final VentaRepository ventaRepo;
    private final DetalleVentaRepository detalleRepo;
    private final ClienteRepository clienteRepo;
    private final VentaFiadaRepository ventaFiadaRepo;

    private final Grid<DetalleVenta> grid = new Grid<>(DetalleVenta.class);
    private final List<DetalleVenta> detalles = new ArrayList<>();
    private double totalVenta = 0;

    private final TextField codigoProducto = new TextField("Id");
    private final TextField nombreProducto = new TextField("Nombre");
    private final NumberField precioUnitario = new NumberField("Precio Unitario");
    private final NumberField cantidadField = new NumberField("Cantidad");
    private final NumberField subtotalField = new NumberField("Subtotal");

    private final Button agregarBtn = new Button("Agregar Producto");
    private final Button cancelarBtn = new Button("Cancelar Venta");
    private final Button pagarBtn = new Button("Pagar Venta");
    private final Button pagarFiadoBtn = new Button("Pagar Venta Fiada");

    private final H3 totalVentaLabel = new H3("Total: $0");
    private final ComboBox<Producto> comboBoxProducto = new ComboBox<>("Producto");

    @Autowired
    public RegistrarVentaView(ProductoRepository productoRepo, VentaRepository ventaRepo,
                               DetalleVentaRepository detalleRepo, ClienteRepository clienteRepo,
                               VentaFiadaRepository ventaFiadaRepo) {
        this.productoRepo = productoRepo;
        this.ventaRepo = ventaRepo;
        this.detalleRepo = detalleRepo;
        this.clienteRepo = clienteRepo;
        this.ventaFiadaRepo = ventaFiadaRepo;

        add(new H1("Registrar Venta"));

        comboBoxProducto.setItems(productoRepo.findAll());
        comboBoxProducto.setItemLabelGenerator(Producto::getNombre);
        comboBoxProducto.setClearButtonVisible(true);

        comboBoxProducto.addValueChangeListener(event -> {
            Producto producto = event.getValue();
            if (producto != null) {
                nombreProducto.setValue(producto.getNombre());
                precioUnitario.setValue(producto.getPrecio());
                calcularSubtotal();
            } else {
                limpiarCamposProducto();
            }
        });

        cantidadField.setValue(1d);
        nombreProducto.setEnabled(false);
        precioUnitario.setEnabled(false);
        subtotalField.setEnabled(false);

        cantidadField.addValueChangeListener(event -> calcularSubtotal());
        precioUnitario.addValueChangeListener(event -> calcularSubtotal());

        HorizontalLayout inputs = new HorizontalLayout(
                comboBoxProducto, nombreProducto, precioUnitario, cantidadField, subtotalField, agregarBtn);

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        DecimalFormat formato = new DecimalFormat("#,##0", symbols);

        grid.removeAllColumns();
        grid.addColumn(detalle -> detalle.getProducto().getNombre()).setHeader("Producto");
        grid.addColumn(detalle -> detalle.getCantidad()).setHeader("Cantidad");
        grid.addColumn(detalle -> "$" + formato.format(detalle.getPrecioUnitario())).setHeader("Precio Unitario");
        grid.addColumn(detalle -> "$" + formato.format(detalle.getSubtotal())).setHeader("Subtotal");

        agregarBtn.addClickListener(e -> agregarProducto());
        cancelarBtn.addClickListener(e -> cancelarVenta());
        pagarBtn.addClickListener(e -> mostrarOpcionesPago());
        pagarFiadoBtn.addClickListener(e -> pagarVentaFiada());

        HorizontalLayout botonesPago = new HorizontalLayout(pagarBtn, pagarFiadoBtn, cancelarBtn);

        add(inputs, grid, totalVentaLabel, botonesPago);

        pagarBtn.setEnabled(false);
    }

    private void calcularSubtotal() {
        if (precioUnitario.getValue() != null && cantidadField.getValue() != null) {
            double subtotal = precioUnitario.getValue() * cantidadField.getValue();
            subtotalField.setValue(subtotal);
        }
    }

    private void agregarProducto() {
        Producto productoSeleccionado = comboBoxProducto.getValue();

        if (productoSeleccionado == null || precioUnitario.getValue() == null) {
            Notification.show("Debe seleccionar un producto válido");
            getElement().executeJs("console.log('⚠️ Producto no válido o precio nulo')");
            return;
        }

        int cantidad = cantidadField.getValue().intValue();
        if (cantidad <= 0) {
            Notification.show("La cantidad debe ser mayor a 0");
            getElement().executeJs("console.log('⚠️ Cantidad inválida')");
            return;
        }

        double subtotal = precioUnitario.getValue() * cantidad;

        DetalleVenta detalle = new DetalleVenta();
        detalle.setProducto(productoSeleccionado);
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(precioUnitario.getValue());

        detalles.add(detalle);
        grid.setItems(detalles);
        totalVenta += subtotal;
        totalVentaLabel.setText("Total: $" + totalVenta);

        getElement().executeJs("console.log('✅ Producto agregado: $' + $0)", subtotal);

        limpiarCamposProducto();

        pagarBtn.setEnabled(true);
        pagarFiadoBtn.setEnabled(true);
    }

    private void mostrarOpcionesPago() {
        Dialog dialog = new Dialog();
        dialog.setWidth("400px");

        Button pagarEfectivoBtn = new Button("Pagar en Efectivo", event -> {
            pagarVenta("EFECTIVO");
            dialog.close();
        });

        Button fiarVentaBtn = new Button("Fiar Venta", event -> {
            pedirIdClienteParaFiado();
            dialog.close();
        });

        pagarEfectivoBtn.setWidth("48%");
        fiarVentaBtn.setWidth("48%");

        HorizontalLayout botonesLayout = new HorizontalLayout(pagarEfectivoBtn, fiarVentaBtn);
        botonesLayout.setSpacing(true);
        botonesLayout.setWidthFull();
        botonesLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        VerticalLayout contenido = new VerticalLayout(botonesLayout);
        contenido.setAlignItems(FlexComponent.Alignment.STRETCH);
        contenido.setPadding(true);
        contenido.setSpacing(true);

        dialog.add(contenido);
        dialog.open();
    }

    private boolean pagarVenta(String metodo) {
        try {
            Venta venta = new Venta();
            venta.setFechaVenta(LocalDate.now());
            venta.setTotal(totalVenta);
            venta.setMetodoPago(metodo);

            ventaRepo.save(venta);
            for (DetalleVenta d : detalles) {
                d.setVenta(venta);
                d.setSubtotal(d.getCantidad() * d.getPrecioUnitario());
                detalleRepo.save(d);
            }

            Notification.show("Venta registrada con "+metodo);
            getElement().executeJs("console.log('✅ Venta pagada con $0')", metodo);

            cancelarVenta();
            return true;

        } catch (Exception e) {
            Notification.show("Error al registrar venta: " + e.getMessage());
            getElement().executeJs("console.log('❌ Error: $0')", e.getMessage());
            return false;
        }
    }

    private void pagarVentaFiada() {
        getUI().ifPresent(ui -> ui.navigate("pagos-fiados"));
        getElement().executeJs("console.log('🔁 Redirigiendo a pagos fiados')");
    }

    private void cancelarVenta() {
        detalles.clear();
        grid.setItems(detalles);
        totalVenta = 0;
        totalVentaLabel.setText("Total: $0");

        limpiarCamposProducto();

        pagarBtn.setEnabled(false);
        pagarFiadoBtn.setEnabled(true);

        getElement().executeJs("console.log('❌ Venta cancelada.')");
    }

    private void limpiarCamposProducto() {
        codigoProducto.clear();
        nombreProducto.clear();
        precioUnitario.clear();
        cantidadField.setValue(1d);
        subtotalField.clear();
        comboBoxProducto.clear();
    }

    private void pedirIdClienteParaFiado() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Registrar Cliente para Venta Fiada");

        TextField idClienteField = new TextField("Numero de Documento");
        TextField nombreClienteField = new TextField("Nombre");

        Button confirmarBtn = new Button("Confirmar", event -> {
            String idCliente = idClienteField.getValue();
            String nombreCliente = nombreClienteField.getValue();

            if (idCliente.isEmpty() || nombreCliente.isEmpty()) {
                Notification.show("Debe ingresar un ID y nombre válido.");
                getElement().executeJs("console.log('⚠️ Datos cliente incompletos')");
                return;
            }

            Cliente cliente = clienteRepo.findByNDocumento(idCliente);
            if (cliente == null) {
                cliente = new Cliente();
                cliente.setIdentificacion(idCliente);
                cliente.setNombre(nombreCliente);
                clienteRepo.save(cliente);
                Notification.show("Cliente nuevo registrado");
                getElement().executeJs("console.log('🆕 Cliente registrado: $0')", nombreCliente);
            } else {
                Notification.show("Cliente encontrado: " + cliente.getNombre());
                getElement().executeJs("console.log('📌 Cliente existente: $0')", cliente.getNombre());
            }

            registrarVentaFiada(cliente);
            dialog.close();
        });

        dialog.add(new VerticalLayout(idClienteField, nombreClienteField, confirmarBtn));
        dialog.open();
    }

    private void registrarVentaFiada(Cliente cliente) {
        if (cliente == null) {
            Notification.show("Error: Cliente no válido.");
            getElement().executeJs("console.log('❌ Cliente inválido en venta fiada')");
            return;
        }

        venta = new Venta();
        venta.setFechaVenta(LocalDate.now());
        venta.setTotal(totalVenta);
        venta.setMetodoPago("FIADO");
        venta.setCliente(cliente);

        venta = ventaRepo.save(venta);
        for (DetalleVenta d : detalles) {
            d.setVenta(venta);
            detalleRepo.save(d);
        }

        VentaFiada ventaFiada = new VentaFiada();
        ventaFiada.setFechaVenta(LocalDate.now());
        ventaFiada.setIdCliente(cliente.getId());
        ventaFiada.setMontoFiado(totalVenta);
        ventaFiadaRepo.save(ventaFiada);

        Notification.show("Venta fiada registrada. Total: $" + totalVenta);
        getElement().executeJs("console.log('✅ Venta fiada registrada')");

        cancelarVenta();
    }

    private Venta venta;
}
