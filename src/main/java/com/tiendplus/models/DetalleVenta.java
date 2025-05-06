package com.tiendplus.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * Clase que representa el detalle de una venta en Tiend Plus.
 * Contiene la cantidad vendida de un producto, su precio unitario y el subtotal.
 */
@Entity
@Table(name = "detalle_venta")
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Relación con la venta a la que pertenece este detalle */
    @ManyToOne
    @JoinColumn(name = "venta_id", nullable = false)
    private Venta venta;

    /** Relación con el producto que se está vendiendo */
    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    /** Cantidad de productos vendidos en esta transacción */
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private int cantidad;

    /** Precio unitario del producto en el momento de la venta */
    @NotNull(message = "El precio unitario no puede ser nulo")
    @PositiveOrZero(message = "El precio unitario no puede ser negativo")
    @Column(name = "precio_unitario", precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    /** Subtotal calculado automáticamente basado en cantidad y precio unitario */
    @Transient
    public BigDecimal getSubtotal() {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }

    // Constructor vacío (requerido por JPA)
    public DetalleVenta() {}

    // Constructor útil para construir desde servicios
    public DetalleVenta(Venta venta, Producto producto, int cantidad, BigDecimal precioUnitario) {
        this.venta = venta;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
}
