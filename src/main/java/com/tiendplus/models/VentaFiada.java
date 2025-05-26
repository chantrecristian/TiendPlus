package com.tiendplus.models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "ventas_fiadas")
public class VentaFiada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idFiado;

    @Column(name = "id_cliente", nullable = false)
    private Long idCliente;  // Tipo Long para coincidir con Cliente.id

    @Column(name = "monto_fiado", nullable = false)
    private Double montoFiado;

    @Column(name = "fecha_venta", nullable = false)
    private LocalDate fechaVenta;

    // Constructor vacío
    public VentaFiada() {}

    // Constructor con argumentos
    public VentaFiada(Long idCliente, Double montoFiado, LocalDate fechaVenta) {
        this.idCliente = idCliente;
        this.montoFiado = montoFiado;
        this.fechaVenta = fechaVenta;
    }

    // Getters y Setters
    public Integer getIdFiado() {
        return idFiado;
    }

    public void setIdFiado(Integer idFiado) {
        this.idFiado = idFiado;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public Double getMontoFiado() {
        return montoFiado;
    }

    public void setMontoFiado(Double montoFiado) {
        this.montoFiado = montoFiado;
    }

    public LocalDate getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDate fechaVenta) {
        this.fechaVenta = fechaVenta;
    }
}
