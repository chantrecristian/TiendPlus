package com.tiendplus.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ventas")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venta")
    private Long idVenta;

    @Column(name = "fecha_venta")
    private LocalDate fechaVenta;

    @Column(name = "total")
    private double total;

    @Column(name = "metodo_pago")
    private String metodoPago;

    // ✅ Relación con Cliente usando documentoCliente como clave primaria
    @ManyToOne
    @JoinColumn(name = "n_documento", referencedColumnName = "n_documento", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "cajero_id")
    private Usuario cajero;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleVenta> detalles = new ArrayList<>();

    public Venta() {
        this.fechaVenta = LocalDate.now();
    }

    // Getters y Setters

    public Long getIdVenta() {
        return idVenta;
    }

    public LocalDate getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDate fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public Usuario getCajero() {
        return cajero;
    }

    public void setCajero(Usuario cajero) {
        this.cajero = cajero;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<DetalleVenta> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
        if (detalles != null) {
            for (DetalleVenta d : detalles) {
                d.setVenta(this);
            }
        }
    }

    // ✅ Devuelve el documento del cliente directamente
    public String getDocumentoCliente() {
        return cliente != null ? cliente.getDocumentoCliente() : null;
    }
}
