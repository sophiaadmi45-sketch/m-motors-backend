package com.mmotores.m_motors_backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmotores.m_motors_backend.controller.AuthController;
import com.mmotores.m_motors_backend.entity.Utilisateur;
import com.mmotores.m_motors_backend.repository.UtilisateurRepository;
import com.mmotores.m_motors_backend.request.ConnexionRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UtilisateurRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    // --- TESTS DE LA CONNEXION ---

    @Test
    public void testConnexionReussie() throws Exception {
        ConnexionRequest request = new ConnexionRequest();
        request.setEmail("faux-email-de-test@mmotors.com");
        request.setMotDePasse("monFauxMotDePasseSimule"); 

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail("faux-email-de-test@mmotors.com");
        utilisateur.setMotDePasse("monFauxMotDePasseSimule"); 
        utilisateur.setRole("CLIENT");

        Mockito.when(repository.findByEmail("faux-email-de-test@mmotors.com")).thenReturn(Optional.of(utilisateur));

        mockMvc.perform(post("/api/auth/connexion")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Connexion réussie !"));
    }

    @Test
    public void testConnexionEchecEmailInexistant() throws Exception {
        ConnexionRequest request = new ConnexionRequest();
        request.setEmail("inconnu@mmotors.com");
        request.setMotDePasse("mot-de-passe-au-hasard");

        Mockito.when(repository.findByEmail("inconnu@mmotors.com")).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/auth/connexion")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Identifiants incorrects."));
    }

    @Test
    public void testConnexionEchecMauvaisMotDePasse() throws Exception {
        ConnexionRequest request = new ConnexionRequest();
        request.setEmail("faux-email-de-test@mmotors.com");
        request.setMotDePasse("un-mauvais-mot-de-passe");

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail("faux-email-de-test@mmotors.com");
        utilisateur.setMotDePasse("monFauxMotDePasseSimule");

        Mockito.when(repository.findByEmail("faux-email-de-test@mmotors.com")).thenReturn(Optional.of(utilisateur));

        mockMvc.perform(post("/api/auth/connexion")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Identifiants incorrects."));
    }

    // --- TESTS DE L'INSCRIPTION ---

    @Test
    public void testInscriptionReussie() throws Exception {
        Utilisateur nouvelUser = new Utilisateur();
        nouvelUser.setEmail("nouveau-compte-de-test@mmotors.com");
        nouvelUser.setMotDePasse("autreFauxMotDePasse");

        Mockito.when(repository.findByEmail("nouveau-compte-de-test@mmotors.com")).thenReturn(Optional.empty());
        Mockito.when(repository.save(Mockito.any(Utilisateur.class))).thenReturn(nouvelUser);

        mockMvc.perform(post("/api/auth/inscription")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nouvelUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Inscription réussie ! Un e-mail de validation vous a été envoyé."));
    }

    @Test
    public void testInscriptionEchecEmailDejaUtilise() throws Exception {
        Utilisateur nouvelUser = new Utilisateur();
        nouvelUser.setEmail("deja-pris@mmotors.com");

        Utilisateur userExistant = new Utilisateur();
        userExistant.setEmail("deja-pris@mmotors.com");

        Mockito.when(repository.findByEmail("deja-pris@mmotors.com")).thenReturn(Optional.of(userExistant));

        mockMvc.perform(post("/api/auth/inscription")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nouvelUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Cet email est déjà utilisé."));
    }
}