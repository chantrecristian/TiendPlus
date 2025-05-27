package com.tiendplus.services;

import com.tiendplus.models.Venta;
import com.tiendplus.models.DetalleVenta;
import com.tiendplus.models.VentaFiada;
import com.tiendplus.models.Cliente;
import com.tiendplus.repositories.VentaRepository;
import com.tiendplus.repositories.DetalleVentaRepository;
import com.tiendplus.repositories.VentaFiadaRepository;
import com.tiendplus.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Autowired
    private VentaFiadaRepository ventaFiadaRepository;

    @Autowired
    private ClienteRepository clienteRepository; // ✅ Agregado para manejar Cliente

    @Transactional
    public Venta registrarVenta(Venta venta) {
        System.out.println("⏳ Registrando venta...");

        if (venta.getDetalles() == null) {
            venta.setDetalles(new ArrayList<>());  
        }

        if (venta.getCliente() == null) {
            throw new IllegalArgumentException("❌ Error: La venta debe estar asociada a un Cliente.");
        }

        System.out.println("🔍 Documento Cliente recibido: " + venta.getCliente().getDocumentoCliente());

        Venta nuevaVenta = ventaRepository.save(venta);
        System.out.println("✅ Venta guardada con ID: " + nuevaVenta.getIdVenta());

        for (DetalleVenta detalle : venta.getDetalles()) {
            detalle.setVenta(nuevaVenta);
            detalleVentaRepository.save(detalle);
        }

        if ("Fiado".equalsIgnoreCase(nuevaVenta.getMetodoPago())) {
            VentaFiada ventaFiada = new VentaFiada();
            ventaFiada.setDocumentoCliente(nuevaVenta.getCliente().getDocumentoCliente());
            ventaFiada.setNombreCliente(nuevaVenta.getCliente().getNombre());
            ventaFiada.setMetodoPago(nuevaVenta.getMetodoPago());
            ventaFiada.setMontoFiado(nuevaVenta.getTotal());
            ventaFiada.setFechaVenta(nuevaVenta.getFechaVenta());

            System.out.println("✅ Guardando venta fiada con documento: " + ventaFiada.getDocumentoCliente());
            ventaFiadaRepository.save(ventaFiada);
        }

        return nuevaVenta;
    }

    public List<Venta> obtenerTodasLasVentas() {
        return ventaRepository.findAll();
    }

    public List<VentaFiada> obtenerVentasFiadasPorDocumento(String documentoCliente) {
        if (documentoCliente == null || documentoCliente.isBlank()) {
            return List.of();
        }
        return ventaFiadaRepository.findByDocumentoCliente(documentoCliente);
    }

    public Venta actualizarVenta(Long id, Venta ventaActualizada) {
        return ventaRepository.findById(id).map(venta -> {
            venta.setTotal(ventaActualizada.getTotal());
            venta.setMetodoPago(ventaActualizada.getMetodoPago());

            if (ventaActualizada.getCliente() != null) {
                venta.setCliente(ventaActualizada.getCliente()); 
            }

            return ventaRepository.save(venta);
        }).orElse(null);
    }

    public void eliminarVenta(Long id) {
        ventaRepository.deleteById(id);
    }
}
