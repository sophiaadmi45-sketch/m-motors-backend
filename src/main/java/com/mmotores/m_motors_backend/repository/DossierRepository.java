package com.mmotores.m_motors_backend.repository;

import com.mmotores.m_motors_backend.entity.Dossier;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DossierRepository extends JpaRepository<Dossier, Long> {
    List<Dossier> findByClientEmail(String email);
}