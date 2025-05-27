package com.tiendplus.services;

import com.tiendplus.models.Venta;
import com.tiendplus.repositories.VentaRepository;

import java.util.List;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ClienteService {

    private final VentaRepository ventaRepository;

    public ClienteService(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    public void save(Venta venta) {
        ventaRepository.save(venta);
    }

    public void delete(Venta venta) {
        ventaRepository.delete(venta);
    }

    public List<Venta> findAll() {
        return ventaRepository.findAll();
    }
}
