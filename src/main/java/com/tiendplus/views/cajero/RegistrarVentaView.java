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

    // Repositorios para interactuar con la base de datos
    private final ProductoRepository productoRepo;
    private final VentaRepository ventaRepo;
    private final DetalleVentaRepository detalleRepo;
    private final ClienteRepository clienteRepo;

    // Grid para mostrar los detalles de la venta
    private final Grid<DetalleVenta> grid = new Grid<>(DetalleVenta.class);
    private final List<DetalleVenta> detalles = new ArrayList<>();  // Lista de detalles de la venta
    private double totalVenta = 0;  // Variable para almacenar el total de la venta

    // Campos de entrada para capturar la información del producto
    private final TextField codigoProducto = new TextField("Id");
    private final TextField nombreProducto = new TextField("Nombre");
    private final NumberField precioUnitario = new NumberField("Precio Unitario");
    private final NumberField cantidadField = new NumberField("Cantidad");
    private final NumberField subtotalField = new NumberField("Subtotal");

    // Botones para agregar productos, cancelar la venta y pagar
    private final Button agregarBtn = new Button("Agregar Producto");
    private final Button cancelarBtn = new Button("Cancelar Venta");
    private final Button pagarBtn = new Button("Pagar Venta");
    private final Button pagarFiadoBtn = new Button("Pagar Venta Fiada");

    // Etiqueta para mostrar el total de la venta
    private final H3 totalVentaLabel = new H3("Total: $0");

    // Constructor con inyección de dependencias
    @Autowired
    
    public RegistrarVentaView(ProductoRepository productoRepo, VentaRepository ventaRepo,
                              DetalleVentaRepository detalleRepo, ClienteRepository clienteRepo) {
        this.productoRepo = productoRepo;
        this.ventaRepo = ventaRepo;
        this.detalleRepo = detalleRepo;
        this.clienteRepo = clienteRepo;

        add(new H1("Registrar Venta"));  // Título de la vista

        // Configuración de campos
        codigoProducto.setClearButtonVisible(true);  // Botón para limpiar el campo de texto
        cantidadField.setValue(1d);  // Valor inicial de la cantidad
        nombreProducto.setEnabled(false);  // Deshabilitar el campo de nombre, ya que se llena automáticamente
        precioUnitario.setEnabled(false);  // Deshabilitar el campo de precio, se llena automáticamente
        subtotalField.setEnabled(false);  // Deshabilitar el campo de subtotal, ya que es calculado

        // Listener para buscar el producto cuando se cambia el código
        codigoProducto.addValueChangeListener(event -> buscarProducto());

        // Layout para los campos de entrada
        HorizontalLayout inputs = new HorizontalLayout(codigoProducto, nombreProducto, precioUnitario, cantidadField, subtotalField, agregarBtn);
        
        // Configuración de las columnas del grid
        grid.setColumns("producto.nombre", "cantidad", "precioUnitario", "subtotal");

        // Añadir los listeners a los botones
        agregarBtn.addClickListener(e -> agregarProducto());
        cancelarBtn.addClickListener(e -> cancelarVenta());
        pagarBtn.addClickListener(e -> mostrarOpcionesPago());
        pagarFiadoBtn.addClickListener(e -> pagarVentaFiada());

        // Layout para los botones de pago
        HorizontalLayout botonesPago = new HorizontalLayout(pagarBtn, pagarFiadoBtn, cancelarBtn);
        
        // Añadir todos los componentes a la vista
        add(inputs, grid, totalVentaLabel, botonesPago);

        // Deshabilitar los botones de pago hasta que haya productos en la venta
        pagarBtn.setEnabled(false);
    }

    // Método para buscar un producto por su ID
    private void buscarProducto() {
        String idText = codigoProducto.getValue();
        if (idText.isEmpty()) return;

        try {
            Long id = Long.parseLong(idText);  // Convertir el ID ingresado a número
            Optional<Producto> productoOpt = productoRepo.findById(id);  // Buscar el producto en la base de datos
            if (productoOpt.isPresent()) {
                Producto producto = productoOpt.get();
                nombreProducto.setValue(producto.getNombre());
                precioUnitario.setValue(producto.getPrecio());
                calcularSubtotal();  // Calcular el subtotal con los valores actuales
            } else {
                limpiarCamposProducto();
                Notification.show("Producto no encontrado");  // Notificar si el producto no existe
            }
        } catch (NumberFormatException e) {
            Notification.show("ID inválido");  // Notificar si el ID no es un número válido
        }
    }

    // Método para calcular el subtotal basado en la cantidad y precio unitario
    private void calcularSubtotal() {
        if (precioUnitario.getValue() != null && cantidadField.getValue() != null) {
            double subtotal = precioUnitario.getValue() * cantidadField.getValue();
            subtotalField.setValue(subtotal);  // Mostrar el subtotal calculado
        }
    }

    // Método para agregar un producto a la venta
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
        

        Producto producto = productoRepo.findById(Long.parseLong(codigoProducto.getValue())).orElse(null);
        if (producto != null) {
            detalle.setProducto(producto);
        }

        detalles.add(detalle);  // Añadir el producto al detalle de la venta
        grid.setItems(detalles);  // Actualizar el grid para mostrar los productos añadidos
        totalVenta += subtotal;  // Actualizar el total de la venta
        totalVentaLabel.setText("Total: $" + totalVenta);

        limpiarCamposProducto();  // Limpiar los campos para agregar otro producto

        pagarBtn.setEnabled(true);  // Habilitar el botón de pago
        pagarFiadoBtn.setEnabled(true);  // Habilitar el botón de pago fiado
    }

    // Método para mostrar las opciones de pago (efectivo, tarjeta, digital o fiado)
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

    // Método para registrar el pago de la venta con un determinado método
    // 🔥 Método para registrar el pago de la venta con un mensaje de confirmación
    private void pagarVenta(String metodo) {
        Venta venta = new Venta();
        venta.setFechaVenta(LocalDate.now());
        venta.setTotal(totalVenta);
        venta.setMetodoPago(metodo);

        ventaRepo.save(venta);
        detalles.forEach(d -> {
            d.setVenta(venta);
            detalleRepo.save(d);
        });

        Notification.show("Venta registrada con éxito. Pago realizado con método: " + metodo);
        cancelarVenta();
    }
    // 📌 Método para registrar la venta fiada asociada a un cliente
    private Venta venta; // 👈 Declaramos la variable como un atributo de la clase

    private void registrarVentaFiada(Cliente cliente) {
        if (cliente == null) {
            Notification.show("Error: Cliente no válido.");
            return;
        }
    
        Notification.show("Registrando venta fiada para el cliente: " + cliente.getNombre());
    
        double totalVenta = detalles.stream().mapToDouble(DetalleVenta::getSubtotal).sum();
    
        venta = new Venta();  // 👈 Ahora usamos la variable de instancia
        venta.setFechaVenta(LocalDate.now());
        venta.setTotal(totalVenta);
        venta.setMetodoPago("FIADO");
        venta.setCliente(cliente);
    
        venta = ventaRepo.save(venta); // 💾 Guardamos la venta y aseguramos que reciba su ID
    
        System.out.println("✅ Venta guardada con ID: " + venta.getId());
    
        if (venta.getId() == null) {
            Notification.show("⚠ Error: La venta NO se registró en la base de datos.");
            return;
        }
    
        detalles.forEach(d -> {
            d.setVenta(venta);
            detalleRepo.save(d);
        });
    
        Notification.show("Venta fiada registrada con éxito. Total: $" + totalVenta);
    
        cancelarVenta(); // 🔄 Limpiar la vista después del registro
    }    
 // Método corregido para pedir ID y registrar automáticamente al cliente en ventas fiadas
 private void pedirIdClienteParaFiado() {
    Dialog dialog = new Dialog();
    dialog.setHeaderTitle("Registrar Cliente para Venta Fiada");

    TextField idClienteField = new TextField("ID Cliente");
    TextField nombreClienteField = new TextField("Nombre Cliente");

    Button confirmarBtn = new Button("Confirmar", event -> {
        Notification.show("Botón Confirmar presionado");
        String idCliente = idClienteField.getValue();
        String nombreCliente = nombreClienteField.getValue();

        if (idCliente.isEmpty() || nombreCliente.isEmpty()) {
            Notification.show("Debe ingresar un ID y nombre de cliente válido.");
            return;
        }

        Cliente cliente = clienteRepo.findByIdentificacion(idCliente);

        if (cliente == null) {
            cliente = new Cliente();
            cliente.setIdentificacion(idCliente);
            cliente.setNombre(nombreCliente);
            clienteRepo.save(cliente);
            Notification.show("Cliente nuevo registrado: " + cliente.getNombre());
        } else {
            Notification.show("Cliente encontrado: " + cliente.getNombre());
        }

         // 💾 Registrar la venta fiada
            registrarVentaFiada(cliente);

         // 🔄 Cerrar la ventana emergente
            dialog.close();

        // 🔥 Cierra el diálogo inmediatamente después de mostrar los mensajes
        getUI().ifPresent(ui -> ui.access(dialog::close));
    });

    dialog.add(new VerticalLayout(idClienteField, nombreClienteField, confirmarBtn));
    dialog.open();
}
// Método corregido para navegar correctamente a la pantalla de pago-fiados
private void pagarVentaFiada() {
    // 🔄 Redirigir a la vista completa para pagar ventas fiadas
    getUI().ifPresent(ui -> ui.navigate("pagos-fiados")); 
}
    // Método para cancelar la venta y limpiar la vista
    private void cancelarVenta() {
        detalles.clear();  // Limpiar los detalles de la venta
        grid.setItems(detalles);  // Actualizar el grid
        totalVenta = 0;  // Reiniciar el total de la venta
        totalVentaLabel.setText("Total: $0");  // Reiniciar el total en la interfaz
        pagarBtn.setEnabled(false);  // Deshabilitar los botones de pago
        limpiarCamposProducto();  // Limpiar los campos de producto
    }
       

    // Método para limpiar los campos de producto
    private void limpiarCamposProducto() {
        codigoProducto.clear();  // Limpiar el campo de ID del producto
        nombreProducto.clear();  // Limpiar el campo de nombre del producto
        precioUnitario.clear();  // Limpiar el campo de precio
        cantidadField.clear();   // Limpiar el campo de cantidad
        subtotalField.clear();   // Limpiar el campo de subtotal
    }
}
