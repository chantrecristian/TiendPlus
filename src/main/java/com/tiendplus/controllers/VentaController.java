package com.tiendplus.controllers;

import com.tiendplus.models.Venta;
import com.tiendplus.models.VentaFiada;
import com.tiendplus.services.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ventas")
public class VentaController {

    private final VentaService ventaService;

    @Autowired
    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @PostMapping("/registrar")
    public Venta registrarVenta(@RequestBody Venta venta) {
        return ventaService.registrarVenta(venta);
    }

    @GetMapping("/listar")
    public List<Venta> obtenerVentas() {
        return ventaService.obtenerTodasLasVentas();
    }

    @GetMapping("/ventas-fiadas/{DocumentoCliente}")
    public List<VentaFiada> buscarVentasFiadas(@PathVariable String DocumentoCliente) {
        return ventaService.obtenerVentasFiadasPorDocumento(DocumentoCliente);
    }

    @PutMapping("/actualizar/{id}")
    public Venta actualizarVenta(@PathVariable Long id, @RequestBody Venta ventaActualizada) {
        return ventaService.actualizarVenta(id, ventaActualizada);
    }

    @DeleteMapping("/eliminar/{id}")
    public void eliminarVenta(@PathVariable Long id) {
        ventaService.eliminarVenta(id);
    }
}
