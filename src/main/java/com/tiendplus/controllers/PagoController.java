package com.tiendplus.controllers;

import com.tiendplus.models.Venta;
import com.tiendplus.repositories.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pagos")
public class PagoController {

    @Autowired
    private VentaRepository ventaRepository;

    // 🏦 Procesar pago de una venta
    @PostMapping("/realizar/{idVenta}")
    public String procesarPago(@PathVariable Long idVenta, @RequestParam String metodoPago) {
        return ventaRepository.findById(idVenta).map(venta -> {
            venta.setMetodoPago(metodoPago);
            ventaRepository.save(venta);
            return "Pago realizado con éxito mediante " + metodoPago;
        }).orElse("Error: La venta no existe.");
    }
}
