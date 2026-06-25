package com.mmotores.m_motors_backend.controller;

import com.mmotores.m_motors_backend.entity.Dossier;
import com.mmotores.m_motors_backend.repository.DossierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/api/dossiers")
 
public class DossierController {

    @Autowired
    private DossierRepository repository;


    @PostMapping("/depot")
    public Dossier depot(
            @RequestParam(required = false) Long vehicleId, 
            @RequestParam(required = false) String clientName,
            @RequestParam(required = false) String clientEmail, 
            @RequestParam(required = false) String typeContrat,
            @RequestParam(required = false) MultipartFile pieceIdentite, 
            @RequestParam(required = false) MultipartFile justificatifDomicile) {
        
        Dossier d = new Dossier();
        d.setVehicleId(vehicleId);
        d.setClientName(clientName);
        d.setClientEmail(clientEmail);
        d.setTypeContrat(typeContrat);
        d.setPieceIdentiteName(pieceIdentite.getOriginalFilename());
        d.setJustificatifDomicileName(justificatifDomicile.getOriginalFilename());
        return repository.save(d);
    }

    @GetMapping("/suivi")
    public List<Dossier> suivi(@RequestParam String email) {
        return repository.findByClientEmail(email);
    }

    // ==========================================================================
    // AJOUT DE L'US-008 : Routes pour l'Espace Pro (Back-office)
    // ==========================================================================

    @GetMapping
    public List<Dossier> getAllDossiers() {
        return repository.findAll();
    }

    @PutMapping("/{id}/statut")
    public ResponseEntity<?> updateStatutDossier(
            @PathVariable Long id, 
            @RequestBody Dossier dossierDetails) {
        
        Optional<Dossier> oDossier = repository.findById(id);
        if (oDossier.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Dossier dossier = oDossier.get();
        
        if (dossierDetails.getCommentaireHistorique() == null || dossierDetails.getCommentaireHistorique().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Le commentaire est obligatoire.");
        }

        dossier.setStatut(dossierDetails.getStatut());
        dossier.setCommentaireHistorique(dossierDetails.getCommentaireHistorique());

        Dossier updatedDossier = repository.save(dossier);
        return ResponseEntity.ok(updatedDossier);
    }
}