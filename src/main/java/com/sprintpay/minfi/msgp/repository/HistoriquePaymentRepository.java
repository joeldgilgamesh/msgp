package com.sprintpay.minfi.msgp.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import com.sprintpay.minfi.msgp.domain.HistoriquePayment;

/**
 * Spring Data  repository for the HistoriquePayment entity.
 */

@Repository
public interface HistoriquePaymentRepository extends JpaRepository<HistoriquePayment, Long> {

}
