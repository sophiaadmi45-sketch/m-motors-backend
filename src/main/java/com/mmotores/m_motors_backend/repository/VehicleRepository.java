package com.mmotores.m_motors_backend.repository;

import com.mmotores.m_motors_backend.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    // Recherche par marque (insensible à la casse)
    List<Vehicle> findByMarqueContainingIgnoreCase(String marque);
    
    // Recherche par type (LLD ou Achat)
    List<Vehicle> findByType(String type);
}