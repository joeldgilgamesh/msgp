package com.sprintpay.minfi.msgp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sprintpay.minfi.msgp.domain.DetailVersementIntermediaire;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the DetailVersementIntermediaire entity.
 */

@Repository
public interface DetailVersementIntermediaireRepository extends JpaRepository<DetailVersementIntermediaire, Long> {

    Optional<DetailVersementIntermediaire> findByNumeroVersment(String numeroVersment);
    
    @Query("SELECT dt FROM DetailVersementIntermediaire dt WHERE dt.id IN (SELECT p.idDetVers FROM Payment p WHERE p.statut = 'RECONCILED' AND p.meansOfPayment = :meansOfPayment) ")
    List<DetailVersementIntermediaire> findDetailVersementIntermediaire(@Param("meansOfPayment") String meansOfPayment);
}
