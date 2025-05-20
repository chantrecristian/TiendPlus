package com.tiendplus.models;

import jakarta.persistence.*;

/**
 * Clase que representa el detalle de una venta en Tiend Plus.
 * Contiene la cantidad vendida de un producto, su precio unitario y el subtotal.
 */
@Entity
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Relación con la venta a la que pertenece este detalle */
    @ManyToOne
    @JoinColumn(name = "venta_id")
    private Venta venta;

    /** Relación con el producto que se está vendiendo */
    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    /** Cantidad de productos vendidos en esta transacción */
    private int cantidad;

    /** Precio unitario del producto en el momento de la venta */
    private double precioUnitario;

    /** Subtotal calculado automáticamente basado en cantidad y precio unitario */

    public DetalleVenta() {}

    // Getters y setters

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

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    /**
     * Calcula el subtotal dinámicamente sin necesidad de actualizarlo manualmente.
     * Evita errores al no depender de una actualización manual en el código.
     * @return subtotal de la venta basado en cantidad y precio unitario.
     */
    public double getSubtotal() {
        return cantidad * precioUnitario;
    }
}
