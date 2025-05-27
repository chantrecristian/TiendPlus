package com.tiendplus.views.cajero;

import com.tiendplus.models.Cliente;
import com.tiendplus.models.DetalleVenta;
import com.tiendplus.models.Producto;
import com.tiendplus.models.Venta;
import com.tiendplus.repositories.ClienteRepository;
import com.tiendplus.repositories.DetalleVentaRepository;
import com.tiendplus.repositories.ProductoRepository;
import com.tiendplus.repositories.VentaRepository;
import com.tiendplus.services.VentaService;
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

    // Repositorios y servicios
    private final ProductoRepository productoRepo;
    private final VentaRepository ventaRepo;
    private final DetalleVentaRepository detalleRepo;
    private final ClienteRepository clienteRepo;
    private final VentaService ventaService;

    // Componentes UI
    private final Grid<DetalleVenta> grid = new Grid<>(DetalleVenta.class);
    private final List<DetalleVenta> detalles = new ArrayList<>();
    private double totalVenta = 0;

    // Campos de formulario
    private final TextField codigoProducto = new TextField("Id");
    private final TextField nombreProducto = new TextField("Nombre");
    private final NumberField precioUnitario = new NumberField("Precio Unitario");
    private final NumberField cantidadField = new NumberField("Cantidad");
    private final NumberField subtotalField = new NumberField("Subtotal");

    // Botones
    private final Button agregarBtn = new Button("Agregar Producto");
    private final Button cancelarBtn = new Button("Cancelar Venta");
    private final Button pagarBtn = new Button("Pagar Venta");
    private final Button pagarFiadoBtn = new Button("Pagar Venta Fiada");

    // Etiquetas
    private final H3 totalVentaLabel = new H3("Total: $0");

    @Autowired
    public RegistrarVentaView(ProductoRepository productoRepo, 
                            VentaRepository ventaRepo,
                            DetalleVentaRepository detalleRepo, 
                            ClienteRepository clienteRepo,
                            VentaService ventaService) {
        this.productoRepo = productoRepo;
        this.ventaRepo = ventaRepo;
        this.detalleRepo = detalleRepo;
        this.clienteRepo = clienteRepo;
        this.ventaService = ventaService;

        inicializarUI();
    }

    private void inicializarUI() {
        setPadding(true);
        setSpacing(true);

        add(new H1("Registrar Venta"));

        // Configuración de campos
        codigoProducto.setClearButtonVisible(true);
        cantidadField.setValue(1d);
        nombreProducto.setEnabled(false);
        precioUnitario.setEnabled(false);
        subtotalField.setEnabled(false);

        // Listeners
        codigoProducto.addValueChangeListener(event -> buscarProducto());
        cantidadField.addValueChangeListener(event -> calcularSubtotal());
        precioUnitario.addValueChangeListener(event -> calcularSubtotal());

        // Layouts
        HorizontalLayout inputs = new HorizontalLayout(
            codigoProducto, nombreProducto, precioUnitario, cantidadField, subtotalField, agregarBtn
        );
        inputs.setAlignItems(Alignment.BASELINE);

        // Configuración del grid
        grid.setColumns("producto.nombre", "cantidad", "precioUnitario", "subtotal");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        // Configuración de botones
        agregarBtn.addClickListener(e -> agregarProducto());
        cancelarBtn.addClickListener(e -> cancelarVenta());
        pagarBtn.addClickListener(e -> mostrarOpcionesPago());
        pagarFiadoBtn.addClickListener(e -> pagarVentaFiada());

        HorizontalLayout botonesPago = new HorizontalLayout(pagarBtn, pagarFiadoBtn, cancelarBtn);
        botonesPago.setSpacing(true);

        // Agregar componentes a la vista
        add(inputs, grid, totalVentaLabel, botonesPago);

        // Estado inicial
        pagarBtn.setEnabled(true);
        pagarFiadoBtn.setEnabled(true);
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
            } else {
                limpiarCamposProducto();
                Notification.show("Producto no encontrado");
            }
        } catch (NumberFormatException e) {
            Notification.show("ID inválido");
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
            return;
        }

        int cantidad = cantidadField.getValue().intValue();
        if (cantidad <= 0) {
            Notification.show("La cantidad debe ser mayor a 0");
            return;
        }

        double subtotal = precioUnitario.getValue() * cantidad;
        DetalleVenta detalle = new DetalleVenta();
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(precioUnitario.getValue());
        detalle.setSubtotal(subtotal);

        Producto producto = productoRepo.findById(Long.parseLong(codigoProducto.getValue())).orElse(null);
        if (producto != null) {
            detalle.setProducto(producto);
        }

        detalles.add(detalle);
        grid.setItems(detalles);
        totalVenta += subtotal;
        totalVentaLabel.setText(String.format("Total: $%.2f", totalVenta));

        limpiarCamposProducto();
        pagarBtn.setEnabled(true);
        pagarFiadoBtn.setEnabled(true);
    }

    private void mostrarOpcionesPago() {
        Dialog dialog = new Dialog();
        dialog.setWidth("400px");

        Button pagarEfectivoBtn = new Button("Pagar en Efectivo", event -> {
            registrarVenta("EFECTIVO");
            dialog.close();
        });

        Button pagarTarjetaBtn = new Button("Pagar con Tarjeta", event -> {
            registrarVenta("TARJETA");
            dialog.close();
        });

        Button pagarDigitalBtn = new Button("Pagar Digital", event -> {
            registrarVenta("DIGITAL");
            dialog.close();
        });

        Button fiarVentaBtn = new Button("Fiar Venta", event -> {
            pedirIdClienteParaFiado();
            dialog.close();
        });

        VerticalLayout opcionesLayout = new VerticalLayout(
            pagarEfectivoBtn, pagarTarjetaBtn, pagarDigitalBtn, fiarVentaBtn
        );
        opcionesLayout.setSpacing(true);
        dialog.add(opcionesLayout);
        dialog.open();
    }

    private void registrarVenta(String metodoPago) {
        try {
            Venta venta = new Venta();
            venta.setFechaVenta(LocalDate.now());
            venta.setTotal(totalVenta);
            venta.setMetodoPago(metodoPago);

            // Asignar venta a cada detalle
            detalles.forEach(detalle -> {
                detalle.setVenta(venta);
                detalle.setSubtotal(detalle.getCantidad() * detalle.getPrecioUnitario());
            });

            venta.setDetalles(detalles);
            ventaService.registrarVenta(venta);

            Notification.show("Venta registrada exitosamente", 3000, Notification.Position.MIDDLE);
            cancelarVenta();
        } catch (Exception e) {
            Notification.show("Error al registrar venta: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
        }
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
                return;
            }

           Cliente cliente = clienteRepo.findByDocumentoCliente(idCliente); // ✅ Buscando con documentoCliente

            if (cliente == null) {
                cliente = new Cliente();
                cliente.setDocumentoCliente(idCliente); // ✅ Guardando con el nombre correcto en código
                cliente.setNombre(nombreCliente);
                clienteRepo.save(cliente);
                Notification.show("Cliente nuevo registrado: " + cliente.getNombre());
            }


            registrarVentaFiada(cliente);
            dialog.close();
        });

        VerticalLayout dialogLayout = new VerticalLayout(idClienteField, nombreClienteField, confirmarBtn);
        dialogLayout.setSpacing(true);
        dialog.add(dialogLayout);
        dialog.open();
    }

    private void registrarVentaFiada(Cliente cliente) {
        try {
            Venta venta = new Venta();
            venta.setFechaVenta(LocalDate.now());
            venta.setTotal(totalVenta);
            venta.setMetodoPago("FIADO");
            venta.setCliente(cliente);

            // Asignar venta a cada detalle
            detalles.forEach(detalle -> {
                detalle.setVenta(venta);
                detalle.setSubtotal(detalle.getCantidad() * detalle.getPrecioUnitario());
            });

            venta.setDetalles(detalles);
            ventaService.registrarVenta(venta);

            Notification.show("Venta fiada registrada exitosamente", 3000, Notification.Position.MIDDLE);
            cancelarVenta();
        } catch (Exception e) {
            Notification.show("Error al registrar venta fiada: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }

    private void pagarVentaFiada() {
        getUI().ifPresent(ui -> ui.navigate("pagos-fiados"));
    }

    private void cancelarVenta() {
        detalles.clear();
        grid.setItems(detalles);
        totalVenta = 0;
        totalVentaLabel.setText("Total: $0");
        limpiarCamposProducto();
        pagarBtn.setEnabled(false);
        pagarFiadoBtn.setEnabled(false);
    }

    private void limpiarCamposProducto() {
        codigoProducto.clear();
        nombreProducto.clear();
        precioUnitario.clear();
        cantidadField.setValue(1d);
        subtotalField.clear();
    }
}