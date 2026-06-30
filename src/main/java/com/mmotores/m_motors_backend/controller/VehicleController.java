package com.mmotores.m_motors_backend.controller;

import com.mmotores.m_motors_backend.entity.Vehicle;
import com.mmotores.m_motors_backend.repository.VehicleRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.webp.WebpWriter;
import java.io.File;
import java.nio.file.Paths;

import java.util.List;

@RestController
@RequestMapping("/vehicles")

public class VehicleController {

    private final VehicleRepository vehicleRepository;

    public VehicleController(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

   private String convertirEtSauvegarderEnWebp(MultipartFile fichierOrigine) throws Exception {
        System.out.println("[LOG BACK] Début conversion de la photo...");
        String nomOriginal = fichierOrigine.getOriginalFilename();
        if (nomOriginal == null || !nomOriginal.contains(".")) {
            nomOriginal = "image.jpg";
        }
        String nomSansExtension = nomOriginal.substring(0, nomOriginal.lastIndexOf("."));
        String nouveauNomFichier = nomSansExtension + "_" + System.currentTimeMillis() + ".webp";

        
        String dossierDestination = "/tmp/images/"; 
        File dossier = new File(dossierDestination);
        if (!dossier.exists()) {
            dossier.mkdirs(); 
        }
        
        
        java.nio.file.Path cheminCible = java.nio.file.Paths.get(dossierDestination, nouveauNomFichier);
        
        System.out.println("[LOG BACK] Écriture absolue via StandardCopyOption vers: " + cheminCible.toAbsolutePath());
        
       
        java.nio.file.Files.copy(
            fichierOrigine.getInputStream(), 
            cheminCible, 
            java.nio.file.StandardCopyOption.REPLACE_EXISTING
        );
        
        System.out.println("[LOG BACK] Écriture physique réussie.");
        return "/images/" + nouveauNomFichier;
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

 @PostMapping
    public org.springframework.http.ResponseEntity<?> addVehicle(
            @RequestParam(required = false) String marque,
            @RequestParam(required = false) String modele,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Double prix,
            @RequestParam(required = false) Integer kilometrage,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Boolean disponible,
            @RequestParam(required = false) MultipartFile imageFile) {
            
        System.out.println("=================== [LOG POST /vehicles] ===================");
        System.out.println("Marque reçue : " + marque);
        System.out.println("Modèle reçu : " + modele);
        System.out.println("Type reçu : " + type);
        System.out.println("Prix reçu : " + prix);
        System.out.println("Kilométrage reçu : " + kilometrage);
        System.out.println("Description reçue : " + description);
        System.out.println("Disponible reçu : " + disponible);
        System.out.println("Fichier image reçu : " + (imageFile != null ? imageFile.getOriginalFilename() : "NULL"));
        System.out.println("=========================================================");

        
        if (marque == null || imageFile == null) {
            System.out.println("[LOG BACK] ⚠️ Requête de test détectée (Body vide ou incomplet). Aucun véhicule créé.");
            return org.springframework.http.ResponseEntity.badRequest().body("Données de formulaire ou image manquantes.");
        }

        try {
            Vehicle vehicle = new Vehicle();
            vehicle.setMarque(marque);
            vehicle.setModele(modele);
            vehicle.setType(type);
            vehicle.setPrix(prix);
            vehicle.setKilometrage(kilometrage);
            vehicle.setDescription(description);
            vehicle.setDisponible(disponible);
            
            
            String cheminWebp = convertirEtSauvegarderEnWebp(imageFile);
            vehicle.setImageUrl(cheminWebp); 

            System.out.println("[LOG BACK] Tentative de sauvegarde en base de données PostgreSQL...");
            Vehicle savedVehicle = vehicleRepository.save(vehicle);
            System.out.println("[LOG BACK] ✅ Sauvegarde réussie avec l'ID #" + savedVehicle.getId());
            
            return org.springframework.http.ResponseEntity.ok().body(savedVehicle);

        } catch (Exception e) {
            System.err.println("[LOG ERROR] 🔥 Crash survenu dans addVehicle ! Voici le détail :");
            e.printStackTrace(); 
            return org.springframework.http.ResponseEntity.status(500).body("Erreur lors de la création du véhicule : " + e.getMessage());
        }
    }


    // Modifier et basculer un véhicule 
    @PutMapping("/{id}")
    public org.springframework.http.ResponseEntity<Vehicle> updateVehicle(@PathVariable Long id, @RequestBody Vehicle vehicleDetails) {
        return vehicleRepository.findById(id)
                .map(vehicle -> {
                    vehicle.setMarque(vehicleDetails.getMarque());
                    vehicle.setModele(vehicleDetails.getModele());
                    vehicle.setType(vehicleDetails.getType()); 
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

    
    