package com.mmotores.m_motors_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "vehicles")
@Data
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String marque;
    private String modele;
    private String type;           // LLD ou Achat
    private Double prix;
    private Integer kilometrage;
    private String description;
    private String imageUrl;
    private Boolean disponible = true;
}