package com.sprint.minfi.msgp.repository;

import com.sprint.minfi.msgp.domain.HistoriquePayment;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the HistoriquePayment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HistoriquePaymentRepository extends JpaRepository<HistoriquePayment, Long> {

}
