package com.mmotores.m_motors_backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmotores.m_motors_backend.controller.DossierController;
import com.mmotores.m_motors_backend.entity.Dossier;
import com.mmotores.m_motors_backend.repository.DossierRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DossierController.class)
public class DossierControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DossierRepository dossierRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testDepotDossier() throws Exception {
        MockMultipartFile file1 = new MockMultipartFile("pieceIdentite", "id.pdf", "application/pdf", "data".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("justificatifDomicile", "domicile.pdf", "application/pdf", "data".getBytes());

        Dossier dossierSauvegarde = new Dossier();
        dossierSauvegarde.setClientName("Sophie");

        Mockito.when(dossierRepository.save(Mockito.any(Dossier.class))).thenReturn(dossierSauvegarde);

        mockMvc.perform(multipart("/api/dossiers/depot")
                .file(file1)
                .file(file2)
                .param("vehicleId", "1")
                .param("clientName", "Sophie")
                .param("clientEmail", "sophie@test.com")
                .param("typeContrat", "LLD"))
                .andExpect(status().isOk());
    }

    @Test
    public void testSuiviDossier() throws Exception {
        Mockito.when(dossierRepository.findByClientEmail("sophie@test.com")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/dossiers/suivi").param("email", "sophie@test.com"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllDossiers() throws Exception {
        Mockito.when(dossierRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/dossiers"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateStatutNotFound() throws Exception {
        Dossier details = new Dossier();
        details.setCommentaireHistorique("OK");

        Mockito.when(dossierRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/dossiers/1/statut")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(details)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateStatutBadRequestCommentaireVide() throws Exception {
        Dossier existing = new Dossier();
        Dossier details = new Dossier(); 

        Mockito.when(dossierRepository.findById(1L)).thenReturn(Optional.of(existing));

        mockMvc.perform(put("/api/dossiers/1/statut")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(details)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Le commentaire est obligatoire."));
    }

    @Test
    public void testUpdateStatutSuccess() throws Exception {
        Dossier existing = new Dossier();
        Dossier details = new Dossier();
        details.setStatut("VALIDE");
        details.setCommentaireHistorique("Dossier complet");

        Mockito.when(dossierRepository.findById(1L)).thenReturn(Optional.of(existing));
        Mockito.when(dossierRepository.save(Mockito.any(Dossier.class))).thenReturn(existing);

        mockMvc.perform(put("/api/dossiers/1/statut")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(details)))
                .andExpect(status().isOk());
    }
}