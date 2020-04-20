package com.sprint.minfi.msgp.repository;

import com.sprint.minfi.msgp.domain.EmissionHistorique;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the EmissionHistorique entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmissionHistoriqueRepository extends JpaRepository<EmissionHistorique, Long> {

}
