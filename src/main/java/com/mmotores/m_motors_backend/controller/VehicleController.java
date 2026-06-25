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
@CrossOrigin(origins = "https://m-motors-frontend-ldfn.onrender.com")
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
        
        File fichierCible = new File(dossierDestination + nouveauNomFichier);
        
        // Conversion et écriture
        System.out.println("[LOG BACK] Écriture physique du fichier WebP vers: " + fichierCible.getAbsolutePath());
        ImmutableImage.loader().fromBytes(fichierOrigine.getBytes())
                .output(new WebpWriter(80, 0, 0, false), fichierCible);
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

 @PostMapping(consumes = {"multipart/form-data"})
    public Vehicle addVehicle(
            
            @RequestParam String marque,
            @RequestParam String modele,
            @RequestParam String type,
            @RequestParam Double prix,
            @RequestParam Integer kilometrage,
            @RequestParam String description,
            @RequestParam Boolean disponible,
            @RequestParam MultipartFile imageFile) {
            

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
        
        try {
            Vehicle vehicle = new Vehicle();
            vehicle.setMarque(marque);
            vehicle.setModele(modele);
            vehicle.setType(type);
            vehicle.setPrix(prix);
            vehicle.setKilometrage(kilometrage);
            vehicle.setDescription(description);
            vehicle.setDisponible(disponible);
            
            // Appel de la conversion automatique et écriture sur le disque
            String cheminWebp = convertirEtSauvegarderEnWebp(imageFile);
            vehicle.setImageUrl(cheminWebp); 

            System.out.println("[LOG BACK] Tentative de sauvegarde en base de données PostgreSQL...");
            Vehicle savedVehicle = vehicleRepository.save(vehicle);
            System.out.println("[LOG BACK] ✅ Sauvegarde réussie avec l'ID #" + savedVehicle.getId());
            
            
            return vehicleRepository.save(vehicle);
        } catch (Exception e) {
            System.err.println("[LOG ERROR] 🔥 Crash survenu dans addVehicle ! Voici le détail :");
            e.printStackTrace(); // <- Force l'affichage complet de l'erreur dans ton terminal
            throw new RuntimeException("Erreur lors de la création du véhicule", e);
        }
    }


    // Modifier et basculer un véhicule 
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

    
    