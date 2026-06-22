package com.mmotores.m_motors_backend.controller;

import com.mmotores.m_motors_backend.entity.Utilisateur;
import com.mmotores.m_motors_backend.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UtilisateurRepository repository;

    @PostMapping("/connexion")
    public ResponseEntity<?> connexion(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");

       
        Optional<Utilisateur> oUtilisateur = repository.findByEmail(email);
        
        if (oUtilisateur.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("message", "Identifiants incorrects."));
        }

        Utilisateur utilisateur = oUtilisateur.get();

        // 2. Vérification stricte du mot de passe
        if (!utilisateur.getMotDePasse().equals(password)) {
            return ResponseEntity.status(401).body(Map.of("message", "Identifiants incorrects."));
        }

        // 3. Succès : On renvoie uniquement les infos nécessaires, le mot de passe est exclu !
        return ResponseEntity.ok(Map.of(
            "email", utilisateur.getEmail(),
            "role", utilisateur.getRole(),
            "message", "Connexion réussie !"
        ));
    }
}