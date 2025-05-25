package com.tiendplus.views.cajero;

import com.tiendplus.models.Cliente;
import com.tiendplus.models.DetalleVenta;
import com.tiendplus.models.Producto;
import com.tiendplus.models.Venta;
import com.tiendplus.repositories.ClienteRepository;
import com.tiendplus.repositories.DetalleVentaRepository;
import com.tiendplus.repositories.ProductoRepository;
import com.tiendplus.repositories.VentaRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Route("registrar-venta")
public class RegistrarVentaView extends VerticalLayout {

    private final ProductoRepository productoRepo;
    private final VentaRepository ventaRepo;
    private final DetalleVentaRepository detalleRepo;
    private final ClienteRepository clienteRepo;

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

    @Autowired
    public RegistrarVentaView(ProductoRepository productoRepo, VentaRepository ventaRepo,
                              DetalleVentaRepository detalleRepo, ClienteRepository clienteRepo) {
        this.productoRepo = productoRepo;
        this.ventaRepo = ventaRepo;
        this.detalleRepo = detalleRepo;
        this.clienteRepo = clienteRepo;

        add(new H1("Registrar Venta"));

        codigoProducto.setClearButtonVisible(true);
        cantidadField.setValue(1d);
        nombreProducto.setEnabled(false);
        precioUnitario.setEnabled(false);
        subtotalField.setEnabled(false);

        codigoProducto.addValueChangeListener(event -> buscarProducto());
        cantidadField.addValueChangeListener(event -> calcularSubtotal());
        precioUnitario.addValueChangeListener(event -> calcularSubtotal());

        HorizontalLayout inputs = new HorizontalLayout(
            codigoProducto, nombreProducto, precioUnitario, cantidadField, subtotalField, agregarBtn
        );

        grid.setColumns("producto.nombre", "cantidad", "precioUnitario", "subtotal");

        agregarBtn.addClickListener(e -> agregarProducto());
        cancelarBtn.addClickListener(e -> cancelarVenta());
        pagarBtn.addClickListener(e -> mostrarOpcionesPago());
        pagarFiadoBtn.addClickListener(e -> pagarVentaFiada());

        HorizontalLayout botonesPago = new HorizontalLayout(pagarBtn, pagarFiadoBtn, cancelarBtn);
        add(inputs, grid, totalVentaLabel, botonesPago);

        pagarBtn.setEnabled(false);
    }

    private void buscarProducto() {
        String idText = codigoProducto.getValue();
        if (idText.isEmpty()) return;

        try {
            Long id = Long.parseLong(idText);
            Optional<Producto> productoOpt = productoRepo.findById(id);
            if (productoOpt.isPresent()) {
                Producto producto = productoOpt.get();
                nombreProducto.setValue(producto.getNombre());
                precioUnitario.setValue(producto.getPrecio());
                calcularSubtotal();

                getElement().executeJs("console.log('✅ Producto encontrado: " + producto.getNombre() + "')");
            } else {
                limpiarCamposProducto();
                Notification.show("Producto no encontrado");
                getElement().executeJs("console.log('❌ Producto no encontrado con ID: " + idText + "')");
            }
        } catch (NumberFormatException e) {
            Notification.show("ID inválido");
            getElement().executeJs("console.log('❌ ID ingresado no es válido: " + idText + "')");
        }
    }

    private void calcularSubtotal() {
        if (precioUnitario.getValue() != null && cantidadField.getValue() != null) {
            double subtotal = precioUnitario.getValue() * cantidadField.getValue();
            subtotalField.setValue(subtotal);
        }
    }

    private void agregarProducto() {
        if (codigoProducto.getValue().isEmpty() || nombreProducto.getValue().isEmpty() || precioUnitario.getValue() == null) {
            Notification.show("Debe ingresar un producto válido");
            getElement().executeJs("console.log('⚠️ Intento fallido de agregar producto')");
            return;
        }

        int cantidad = cantidadField.getValue().intValue();
        if (cantidad <= 0) {
            Notification.show("La cantidad debe ser mayor a 0");
            getElement().executeJs("console.log('⚠️ Cantidad inválida: " + cantidad + "')");
            return;
        }

        double subtotal = precioUnitario.getValue() * cantidad;
        DetalleVenta detalle = new DetalleVenta();
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(precioUnitario.getValue());

        Producto producto = productoRepo.findById(Long.parseLong(codigoProducto.getValue())).orElse(null);
        if (producto != null) {
            detalle.setProducto(producto);
        }

        detalles.add(detalle);
        grid.setItems(detalles);
        totalVenta += subtotal;
        totalVentaLabel.setText("Total: $" + totalVenta);

        getElement().executeJs("console.log('🛒 Producto agregado: " + producto.getNombre() + " - Cantidad: " + cantidad + "')");

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

        Button pagarTarjetaBtn = new Button("Pagar con Tarjeta", event -> {
            pagarVenta("TARJETA");
            dialog.close();
        });

        Button pagarDigitalBtn = new Button("Pagar Digital", event -> {
            pagarVenta("DIGITAL");
            dialog.close();
        });

        Button fiarVentaBtn = new Button("Fiar Venta", event -> {
            pedirIdClienteParaFiado();
            dialog.close();
        });

        dialog.add(new VerticalLayout(pagarEfectivoBtn, pagarTarjetaBtn, pagarDigitalBtn, fiarVentaBtn));
        dialog.open();
    }

    private boolean pagarVenta(String metodo) {
        try {
            Venta venta = new Venta();
            venta.setFechaVenta(LocalDate.now());
            venta.setTotal(totalVenta);
            venta.setMetodoPago(metodo);

            ventaRepo.save(venta);
            detalles.forEach(d -> {
                d.setVenta(venta);
                d.setSubtotal(d.getCantidad() * d.getPrecioUnitario());
                detalleRepo.save(d);
            });

            Notification.show("Venta registrada con éxito. Pago con: " + metodo);
            getElement().executeJs("console.log('✅ Venta pagada con " + metodo + " por $" + totalVenta + "')");

            cancelarVenta();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            Notification.show("Error al registrar la venta: " + e.getMessage());
            getElement().executeJs("console.log('❌ Error al registrar la venta: " + e.getMessage() + "')");
            return false;
        }
    }

    private Venta venta;

    private void registrarVentaFiada(Cliente cliente) {
        if (cliente == null) {
            Notification.show("Error: Cliente no válido.");
            getElement().executeJs("console.log('❌ Cliente no válido en venta fiada')");
            return;
        }

        Notification.show("Registrando venta fiada para el cliente: " + cliente.getNombre());
        getElement().executeJs("console.log('📦 Registrando venta fiada para cliente: " + cliente.getNombre() + "')");

        double totalVenta = detalles.stream().mapToDouble(DetalleVenta::getSubtotal).sum();

        venta = new Venta();
        venta.setFechaVenta(LocalDate.now());
        venta.setTotal(totalVenta);
        venta.setMetodoPago("FIADO");

        venta = ventaRepo.save(venta);

        detalles.forEach(d -> {
            d.setVenta(venta);
            detalleRepo.save(d);
        });

        Notification.show("Venta fiada registrada con éxito. Total: $" + totalVenta);
        getElement().executeJs("console.log('✅ Venta fiada registrada con éxito. Total: $" + totalVenta + "')");

        cancelarVenta();
    }

    private void pedirIdClienteParaFiado() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Registrar Cliente para Venta Fiada");

        TextField idClienteField = new TextField("ID Cliente");
        TextField nombreClienteField = new TextField("Nombre Cliente");

        Button confirmarBtn = new Button("Confirmar", event -> {
            String idCliente = idClienteField.getValue();
            String nombreCliente = nombreClienteField.getValue();

            if (idCliente.isEmpty() || nombreCliente.isEmpty()) {
                Notification.show("Debe ingresar un ID y nombre de cliente válido.");
                getElement().executeJs("console.log('⚠️ Datos incompletos para cliente fiado')");
                return;
            }

            Cliente cliente = clienteRepo.findByNDocumento(idCliente);

            if (cliente == null) {
                cliente = new Cliente();
                cliente.setIdentificacion(idCliente);
                cliente.setNombre(nombreCliente);
                clienteRepo.save(cliente);
                Notification.show("Cliente nuevo registrado: " + cliente.getNombre());
                getElement().executeJs("console.log('🆕 Cliente nuevo registrado: " + cliente.getNombre() + "')");
            } else {
                Notification.show("Cliente encontrado: " + cliente.getNombre());
                getElement().executeJs("console.log('📌 Cliente existente: " + cliente.getNombre() + "')");
            }

            registrarVentaFiada(cliente);
            dialog.close();
        });

        dialog.add(new VerticalLayout(idClienteField, nombreClienteField, confirmarBtn));
        dialog.open();
    }

    private void pagarVentaFiada() {
        getUI().ifPresent(ui -> ui.navigate("pagos-fiados"));
        getElement().executeJs("console.log('🔁 Redirigiendo a vista de pagos fiados')");
    }

    private void cancelarVenta() {
        detalles.clear();
        grid.setItems(detalles);
        totalVenta = 0;
        totalVentaLabel.setText("Total: $0");

        limpiarCamposProducto();

        pagarBtn.setEnabled(false);
        pagarFiadoBtn.setEnabled(false);

        getElement().executeJs("console.log('❌ Venta cancelada. Productos limpiados.')");
    }

    private void limpiarCamposProducto() {
        codigoProducto.clear();
        nombreProducto.clear();
        precioUnitario.clear();
        cantidadField.setValue(1d);
        subtotalField.clear();
    }
}
