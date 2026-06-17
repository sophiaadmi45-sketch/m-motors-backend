package com.mmotores.m_motors_backend.config;

import com.mmotores.m_motors_backend.entity.Vehicle;
import com.mmotores.m_motors_backend.repository.VehicleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final VehicleRepository vehicleRepository;

    public DataInitializer(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public void run(String... args) {
        vehicleRepository.deleteAll();

            // 1. Peugeot 308
            Vehicle v1 = new Vehicle();
            v1.setMarque("Peugeot");
            v1.setModele("308 Active Pack");
            v1.setType("Achat");
            v1.setPrix(24990.0);
            v1.setKilometrage(12500);
            v1.setDescription("Peugeot 308 Active Pack grise - i-Cockpit, écran 10'', aide au stationnement");
            v1.setImageUrl("/images/peugeot-308.webp");
            v1.setDisponible(true);

            // 2. Renault Clio
            Vehicle v2 = new Vehicle();
            v2.setMarque("Renault");
            v2.setModele("Clio 2023");
            v2.setType("LLD");
            v2.setPrix(18900.0);
            v2.setKilometrage(10);
            v2.setDescription("Renault Clio blanche 2023 - Excellent état, première main");
            v2.setImageUrl("/images/renault-clio.webp");
            v2.setDisponible(true);

            // 3. BMW Série 3
            Vehicle v3 = new Vehicle();
            v3.setMarque("BMW");
            v3.setModele("Série 3 Touring 318d");
            v3.setType("Achat");
            v3.setPrix(32900.0);
            v3.setKilometrage(59030);
            v3.setDescription("BMW Série 3 Touring noire 2023 - 136 ch");
            v3.setImageUrl("/images/bmw-serie3.webp");
            v3.setDisponible(true);

            // 4. Audi A3
            Vehicle v4 = new Vehicle();
            v4.setMarque("Audi");
            v4.setModele("A3 Sportback 40 TFSI e");
            v4.setType("LLD");
            v4.setPrix(38100.0);
            v4.setKilometrage(15000);
            v4.setDescription("Audi A3 Sportback hybride rechargeable bleue - 204 ch");
            v4.setImageUrl("/images/audi-a3.webp");
            v4.setDisponible(true);

            // 5. Toyota Corolla
            Vehicle v5 = new Vehicle();
            v5.setMarque("Toyota");
            v5.setModele("Corolla GR Sport");
            v5.setType("Achat");
            v5.setPrix(27880.0);
            v5.setKilometrage(23195);
            v5.setDescription("Toyota Corolla hybride blanche 2023 - Fiable et économique");
            v5.setImageUrl("/images/toyota-corolla.webp");
            v5.setDisponible(true);

            // 6. Volkswagen Golf
            Vehicle v6 = new Vehicle();
            v6.setMarque("Volkswagen");
            v6.setModele("Golf 2022");
            v6.setType("LLD");
            v6.setPrix(29500.0);
            v6.setKilometrage(0);
            v6.setDescription("Volkswagen Golf grise 2022 - Diesel automatique");
            v6.setImageUrl("/images/volkswagen-golf.webp");
            v6.setDisponible(true);

            vehicleRepository.saveAll(List.of(v1, v2, v3, v4, v5, v6));

            System.out.println("✅ 6 véhicules de test ajoutés avec succès dans PostgreSQL !");
        }
    }
