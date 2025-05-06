package com.tiendplus.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "ventas")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venta")
    private Long idVenta;

    @Column(name = "fecha_venta", nullable = false)
    private LocalDate fechaVenta;

    @NotNull(message = "El total no puede ser nulo")
    @PositiveOrZero(message = "El total no puede ser negativo")
    @Column(name = "total", precision = 12, scale = 2)
    private BigDecimal total;

    @Column(name = "metodo_pago", nullable = false)
    private String metodoPago; // efectivo, tarjeta, digital, fiado

    @ManyToOne(optional = false)
    @JoinColumn(name = "cajero_id", nullable = false)
    private Usuario cajero;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente; // puede ser null si no es fiado

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleVenta> detalles;

    // Constructor sin argumentos (por JPA)
    public Venta() {
        this.fechaVenta = LocalDate.now();
        this.total = BigDecimal.ZERO;
    }

    // Getters y Setters

    public Long getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Long idVenta) {
        this.idVenta = idVenta;
    }

    public LocalDate getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDate fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
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
            // Calcular total automáticamente
            this.total = detalles.stream()
                .map(DetalleVenta::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
    }
}
