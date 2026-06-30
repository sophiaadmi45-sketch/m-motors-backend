package com.mmotores.m_motors_backend.controller;

import com.mmotores.m_motors_backend.entity.Utilisateur;
import com.mmotores.m_motors_backend.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Map;
import java.util.Optional;
import com.mmotores.m_motors_backend.request.ConnexionRequest;
import java.util.HashMap;

@RestController
@RequestMapping("/api/auth")

public class AuthController {

    @Autowired
    private UtilisateurRepository repository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/connexion")
    public ResponseEntity<?> connexion(@RequestBody ConnexionRequest request) {
        String email = request.getEmail();
        String password = request.getMotDePasse();

        Optional<Utilisateur> oUtilisateur = repository.findByEmail(email);
        
        if (oUtilisateur.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("message", "Identifiants incorrects."));
        }

        Utilisateur utilisateur = oUtilisateur.get();

         if (!passwordEncoder.matches(password, utilisateur.getMotDePasse())) {
            return ResponseEntity.status(401).body(Map.of("message", "Identifiants incorrects."));
        }

        

       // Génération du token JWT (Valable 24 heures) avec signature cryptographique
        String token = io.jsonwebtoken.Jwts.builder()
                .setSubject(utilisateur.getEmail())
                .claim("role", utilisateur.getRole())
                .setIssuedAt(new java.util.Date())
                .setExpiration(new java.util.Date(System.currentTimeMillis() + 86400000))
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS256, "MaCleSecreteSuperSecuriseePourMonExamenMMotors2026")
                .compact();

        return ResponseEntity.ok(Map.of(
            "email", utilisateur.getEmail(),
            "role", utilisateur.getRole(),
            "token", token,
            "message", "Connexion réussie !"
        ));
    }

    @PostMapping("/inscription")
    public ResponseEntity<?> inscription(@RequestBody Utilisateur nouvelUtilisateur) {
        
        Optional<Utilisateur> userExistant = repository.findByEmail(nouvelUtilisateur.getEmail());
        if (userExistant.isPresent()) {
            return ResponseEntity.status(400).body(Map.of("message", "Cet email est déjà utilisé."));
        }
       
        String motDePasseHache = passwordEncoder.encode(nouvelUtilisateur.getMotDePasse());
        nouvelUtilisateur.setMotDePasse(motDePasseHache);

        nouvelUtilisateur.setRole("CLIENT");
        nouvelUtilisateur.setActif(false);
        Utilisateur utilisateurSauvegarde = repository.save(nouvelUtilisateur);

        return ResponseEntity.ok(Map.of(
            "email", utilisateurSauvegarde.getEmail(),
            "message", "Inscription réussie ! Un e-mail de validation vous a été envoyé."
        ));
    }
}