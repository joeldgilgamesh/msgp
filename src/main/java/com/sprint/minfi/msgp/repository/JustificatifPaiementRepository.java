package com.sprint.minfi.msgp.repository;

import com.sprint.minfi.msgp.domain.JustificatifPaiement;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the JustificatifPaiement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JustificatifPaiementRepository extends JpaRepository<JustificatifPaiement, Long> {

}
