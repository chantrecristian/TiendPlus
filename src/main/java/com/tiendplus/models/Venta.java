package com.tiendplus.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "ventas")  // Asegura que se mapea correctamente con la tabla en la BD
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_venta") // Coincide con la BD
    private LocalDate fechaVenta;


    private double total;

    @Column(name = "metodo_pago")  // Asegura que coincide con la columna en la BD
    private String metodoPago;  // Valores posibles: efectivo, tarjeta, digital, fiado

    @ManyToOne
    @JoinColumn(name = "cajero_id")
    private Usuario cajero;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente; // Null si no es venta fiada

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleVenta> detalles;

    public Venta() {
        this.fechaVenta = LocalDate.now(); // Usa fechaVenta, que coincide con la BD
    }
    

    // Getters y setters

    public Long getId() { return id; }

    public LocalDate getFechaVenta() { return fechaVenta; }

    public void setFechaVenta(LocalDate fechaVenta) { this.fechaVenta = fechaVenta; }

    public double getTotal() { return total; }

    public void setTotal(double total) { this.total = total; }

    public String getMetodoPago() { return metodoPago; }

    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public Usuario getCajero() { return cajero; }

    public void setCajero(Usuario cajero) { this.cajero = cajero; }

    public Cliente getCliente() { return cliente; }

    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public List<DetalleVenta> getDetalles() { return detalles; }

    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
        if (detalles != null) {
            for (DetalleVenta d : detalles) {
                d.setVenta(this);
            }
        }
    }
}