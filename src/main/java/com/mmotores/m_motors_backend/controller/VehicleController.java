package com.mmotores.m_motors_backend.controller;

import com.mmotores.m_motors_backend.entity.Vehicle;
import com.mmotores.m_motors_backend.repository.VehicleRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
@CrossOrigin(origins = "http://localhost:5173")   
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

    @GetMapping("/{id}")
    public org.springframework.http.ResponseEntity<Vehicle> getVehicleById(@PathVariable Long id) {
        return vehicleRepository.findById(id)
                .map(vehicle -> org.springframework.http.ResponseEntity.ok().body(vehicle))
                .orElse(org.springframework.http.ResponseEntity.notFound().build());
    }


// Ajouter un véhicule (US-007)
    @PostMapping
    public Vehicle addVehicle(@RequestBody Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    // Modifier et basculer un véhicule (US-007)
    @PutMapping("/{id}")
    public org.springframework.http.ResponseEntity<Vehicle> updateVehicle(@PathVariable Long id, @RequestBody Vehicle vehicleDetails) {
        return vehicleRepository.findById(id)
                .map(vehicle -> {
                    vehicle.setMarque(vehicleDetails.getMarque());
                    vehicle.setModele(vehicleDetails.getModele());
                    vehicle.setType(vehicleDetails.getType()); // Permet la bascule "LLD" <=> "Achat"
                    vehicle.setPrix(vehicleDetails.getPrix());
                    vehicle.setKilometrage(vehicleDetails.getKilometrage());
                    vehicle.setDescription(vehicleDetails.getDescription());
                    vehicle.setImageUrl(vehicleDetails.getImageUrl());
                    vehicle.setDisponible(vehicleDetails.getDisponible());
                    
                    Vehicle updatedVehicle = vehicleRepository.save(vehicle);
                    return org.springframework.http.ResponseEntity.ok().body(updatedVehicle);
                })
                .orElse(org.springframework.http.ResponseEntity.notFound().build());
    }
    }
