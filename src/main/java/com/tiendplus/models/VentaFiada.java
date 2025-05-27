package com.tiendplus.models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "ventas_fiadas") // Se enlaza con la tabla en la BD
public class VentaFiada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idFiado; // ID de la venta fiada

    @Column(name = "n_documento", nullable = false)
    private String documentoCliente; // ✅ Nuevo nombre, pero sigue mapeado a la BD

    @Column(name = "nombre_cliente", nullable = false)
    private String nombreCliente;

    @Column(name = "monto_fiado", nullable = false)
    private Double montoFiado;

    @Column(name = "metodo_pago", nullable = false)
    private String metodoPago;

    @Column(name = "fecha_venta")
    private LocalDate fechaVenta;

    // ✅ Constructor vacío requerido por JPA
    public VentaFiada() {}

    // ✅ Constructor con todos los campos excepto ID
    public VentaFiada(String nombreCliente, String documentoCliente, String metodoPago, Double montoFiado, LocalDate fechaVenta) {
        this.nombreCliente = nombreCliente;
        this.documentoCliente = documentoCliente;
        this.metodoPago = metodoPago;
        this.montoFiado = montoFiado;
        this.fechaVenta = fechaVenta;
    }

    // ✅ Getters y Setters optimizados
    public Integer getIdFiado() { return idFiado; }
    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
    public String getDocumentoCliente() { return documentoCliente; }
    public void setDocumentoCliente(String documentoCliente) { this.documentoCliente = documentoCliente; }
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    public Double getMontoFiado() { return montoFiado; }
    public void setMontoFiado(Double montoFiado) { this.montoFiado = montoFiado; }
    public LocalDate getFechaVenta() { return fechaVenta; }
    public void setFechaVenta(LocalDate fechaVenta) { this.fechaVenta = fechaVenta; }
}
