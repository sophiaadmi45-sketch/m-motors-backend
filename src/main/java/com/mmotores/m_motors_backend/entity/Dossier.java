package com.mmotores.m_motors_backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "dossiers")
public class Dossier {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long vehicleId;
    private String clientName;
    private String clientEmail;
    private String typeContrat;
    private String statut = "EN_COURS";
    private String pieceIdentiteName;
    private String justificatifDomicileName;

    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }
    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }
    public String getClientEmail() { return clientEmail; }
    public void setClientEmail(String clientEmail) { this.clientEmail = clientEmail; }
    public String getTypeContrat() { return typeContrat; }
    public void setTypeContrat(String typeContrat) { this.typeContrat = typeContrat; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public String getPieceIdentiteName() { return pieceIdentiteName; }
    public void setPieceIdentiteName(String p) { this.pieceIdentiteName = p; }
    public String getJustificatifDomicileName() { return justificatifDomicileName; }
    public void setJustificatifDomicileName(String j) { this.justificatifDomicileName = j; }
}