package com.tiendplus.controllers;

import com.tiendplus.models.Venta;
import com.tiendplus.repositories.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ventas")
public class VentaController {

    @Autowired
    private VentaRepository ventaRepository;

    // 🛒 Registrar una nueva venta
    @PostMapping("/registrar")
    public Venta registrarVenta(@RequestBody Venta venta) {
        return ventaRepository.save(venta);
    }

    // 🔍 Obtener todas las ventas
    @GetMapping("/listar")
    public List<Venta> obtenerVentas() {
        return ventaRepository.findAll();
    }

    // 🏷 Obtener una venta por su ID
    @GetMapping("/{id}")
    public Venta obtenerVentaPorId(@PathVariable Long id) {
        return ventaRepository.findById(id).orElse(null);
    }

    // 📝 Actualizar una venta
    @PutMapping("/actualizar/{id}")
    public Venta actualizarVenta(@PathVariable Long id, @RequestBody Venta ventaActualizada) {
        return ventaRepository.findById(id).map(venta -> {
            venta.setTotal(ventaActualizada.getTotal());
            return ventaRepository.save(venta);
        }).orElse(null);
    }

    // 🚮 Eliminar una venta
    @DeleteMapping("/eliminar/{id}")
    public void eliminarVenta(@PathVariable Long id) {
        ventaRepository.deleteById(id);
    }
}
