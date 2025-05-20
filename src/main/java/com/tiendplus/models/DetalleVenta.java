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

    @ManyToOne
    @JoinColumn(name = "venta_id")
    private Venta venta;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    private int cantidad;

    private double precioUnitario;

    @Column(nullable = false)
    private double subtotal;

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
        calcularSubtotal(); // recalcula al cambiar cantidad
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
        calcularSubtotal(); // recalcula al cambiar precio
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    /** Calcula el subtotal automáticamente */
    private void calcularSubtotal() {
        this.subtotal = this.cantidad * this.precioUnitario;
    }
}