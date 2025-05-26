package com.tiendplus.controllers;

import com.tiendplus.models.VentaFiada;
import com.tiendplus.repositories.VentaFiadaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas-fiadas")
@CrossOrigin(origins = "*")  // Puedes ajustar el origen para el frontend
public class VentaFiadaController {

    @Autowired
    private VentaFiadaRepository ventaFiadaRepository;

    // Obtener todas las ventas fiadas
    @GetMapping
    public List<VentaFiada> getAllVentasFiadas() {
        return ventaFiadaRepository.findAll();
    }

    // Obtener ventas fiadas por ID de cliente
    @GetMapping("/cliente/{idCliente}")
    public List<VentaFiada> getVentasByCliente(@PathVariable Long idCliente) {
        return ventaFiadaRepository.findByIdCliente(idCliente);
    }

    // Obtener ventas fiadas por ID de cliente ordenadas por fecha (más recientes primero)
    @GetMapping("/cliente/{idCliente}/ordenadas")
    public List<VentaFiada> getVentasByClienteOrdenadas(@PathVariable Integer idCliente) {
        return ventaFiadaRepository.findByIdClienteOrderByFechaVentaDesc(idCliente);
    }

    // Crear una nueva venta fiada
    @PostMapping
    public VentaFiada crearVentaFiada(@RequestBody VentaFiada venta) {
        return ventaFiadaRepository.save(venta);
    }

    // Obtener una venta fiada por su ID
    @GetMapping("/{idFiado}")
    public ResponseEntity<VentaFiada> getVentaById(@PathVariable Integer idFiado) {
        return ventaFiadaRepository.findById(idFiado)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Actualizar una venta fiada
    @PutMapping("/{idFiado}")
    public ResponseEntity<VentaFiada> actualizarVentaFiada(@PathVariable Integer idFiado, @RequestBody VentaFiada ventaActualizada) {
        return ventaFiadaRepository.findById(idFiado).map(venta -> {
            venta.setIdCliente(ventaActualizada.getIdCliente());
            venta.setMontoFiado(ventaActualizada.getMontoFiado());
            venta.setFechaVenta(ventaActualizada.getFechaVenta());
            return ResponseEntity.ok(ventaFiadaRepository.save(venta));
        }).orElse(ResponseEntity.notFound().build());
    }

    // Eliminar una venta fiada
    @DeleteMapping("/{idFiado}")
    public ResponseEntity<Void> eliminarVentaFiada(@PathVariable Integer idFiado) {
        return ventaFiadaRepository.findById(idFiado).map(venta -> {
            ventaFiadaRepository.delete(venta);
            return ResponseEntity.noContent().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
