package com.tiendplus.models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "ventas_fiadas")  // Se enlaza con la tabla en la BD
public class VentaFiada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idFiado;  // ID de la venta fiada

    @Column(name = "id_cliente")
    private Integer idCliente;

    @Column(name = "nombre_cliente")
    private String nombreCliente;

    @Column(name = "n_documento")
    private String nDocumento;

    @Column(name = "monto_fiado")
    private Double montoFiado;

    @Column(name = "fecha_venta")
    private LocalDate fechaVenta;

    // Constructor vacío
    public VentaFiada() {}

    // Constructor con todos los atributos
    public VentaFiada(Integer idCliente, String nombreCliente, String nDocumento, Double montoFiado, LocalDate fechaVenta) {
        this.idCliente = idCliente;
        this.nombreCliente = nombreCliente;
        this.nDocumento = nDocumento;
        this.montoFiado = montoFiado;
        this.fechaVenta = fechaVenta;
    }

    // Getters y Setters
    public Integer getIdFiado() { return idFiado; }
    public void setIdFiado(Integer idFiado) { this.idFiado = idFiado; }

    public Integer getIdCliente() { return idCliente; }
    public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getNDocumento() { return nDocumento; }
    public void setNDocumento(String nDocumento) { this.nDocumento = nDocumento; }

    public Double getMontoFiado() { return montoFiado; }
    public void setMontoFiado(Double montoFiado) { this.montoFiado = montoFiado; }

    public LocalDate getFechaVenta() { return fechaVenta; }
    public void setFechaVenta(LocalDate fechaVenta) { this.fechaVenta = fechaVenta; }
}