package com.sprintpay.minfi.msgp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sprintpay.minfi.msgp.domain.DetailVersementIntermediaire;

import java.util.Optional;

/**
 * Spring Data  repository for the DetailVersementIntermediaire entity.
 */

@Repository
public interface DetailVersementIntermediaireRepository extends JpaRepository<DetailVersementIntermediaire, Long> {

    Optional<DetailVersementIntermediaire> findByNumeroVersment(String numeroVersment);
}
