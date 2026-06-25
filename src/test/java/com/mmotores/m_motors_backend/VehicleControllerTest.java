package com.mmotores.m_motors_backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmotores.m_motors_backend.controller.VehicleController;
import com.mmotores.m_motors_backend.entity.Vehicle;
import com.mmotores.m_motors_backend.repository.VehicleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import jakarta.servlet.ServletException;

import java.util.Collections;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VehicleController.class)
public class VehicleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VehicleRepository vehicleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllVehicles() throws Exception {
        Mockito.when(vehicleRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/vehicles"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    public void testSearchVehiclesByMarque() throws Exception {
        Mockito.when(vehicleRepository.findByMarqueContainingIgnoreCase("Peugeot")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/vehicles/search").param("marque", "Peugeot"))
                .andExpect(status().isOk());
    }

    @Test
    public void testSearchVehiclesByType() throws Exception {
        Mockito.when(vehicleRepository.findByType("Achat")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/vehicles/search/type").param("type", "Achat"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetVehicleByIdSuccess() throws Exception {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(1L);
        vehicle.setMarque("Renault");

        Mockito.when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));

        mockMvc.perform(get("/vehicles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.marque").value("Renault"));
    }

    @Test
    public void testGetVehicleByIdNotFound() throws Exception {
        Mockito.when(vehicleRepository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/vehicles/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateVehicleSuccess() throws Exception {
        Vehicle existingVehicle = new Vehicle();
        existingVehicle.setId(1L);

        Vehicle updatedDetails = new Vehicle();
        updatedDetails.setMarque("Audi");
        updatedDetails.setModele("A3");

        Mockito.when(vehicleRepository.findById(1L)).thenReturn(Optional.of(existingVehicle));
        Mockito.when(vehicleRepository.save(Mockito.any(Vehicle.class))).thenReturn(updatedDetails);

        mockMvc.perform(put("/vehicles/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.marque").value("Audi"));
    }

    @Test
    public void testAddVehicleExceptionHandling() {

        MockMultipartFile emptyFile = new MockMultipartFile("imageFile", "photo.jpg", "image/jpeg", new byte[0]);

        Assertions.assertThrows(ServletException.class, () -> {
            mockMvc.perform(multipart("/vehicles")
                    .file(emptyFile)
                    .param("marque", "Clio")
                    .param("modele", "V")
                    .param("type", "Achat")
                    .param("prix", "15000.0")
                    .param("kilometrage", "10000")
                    .param("description", "Super citadine")
                    .param("disponible", "true"));
        });
    }
}