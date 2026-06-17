package com.mmotores.m_motors_backend.controller;

import com.mmotores.m_motors_backend.entity.Vehicle;
import com.mmotores.m_motors_backend.repository.VehicleRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
@CrossOrigin(origins = "http://localhost:5173")   // Autorise le frontend
public class VehicleController {

    private final VehicleRepository vehicleRepository;

    public VehicleController(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    // Recherche de véhicules
    @GetMapping
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    // Recherche par marque
    @GetMapping("/search")
    public List<Vehicle> searchVehicles(@RequestParam String marque) {
        return vehicleRepository.findByMarqueContainingIgnoreCase(marque);
    }

    // Recherche par type
    @GetMapping("/search/type")
    public List<Vehicle> searchVehiclesByType(@RequestParam String type) {
        return vehicleRepository.findByType(type);
    }
}