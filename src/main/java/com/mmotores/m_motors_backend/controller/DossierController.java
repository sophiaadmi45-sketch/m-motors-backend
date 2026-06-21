package com.mmotores.m_motors_backend.controller;

import com.mmotores.m_motors_backend.entity.Dossier;
import com.mmotores.m_motors_backend.repository.DossierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/dossiers")
@CrossOrigin(origins = "*") 
public class DossierController {

    @Autowired
    private DossierRepository repository;

    @PostMapping("/depot")
    public Dossier depot(
            @RequestParam Long vehicleId, @RequestParam String clientName,
            @RequestParam String clientEmail, @RequestParam String typeContrat,
            @RequestParam MultipartFile pieceIdentite, @RequestParam MultipartFile justificatifDomicile) {
        
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
}