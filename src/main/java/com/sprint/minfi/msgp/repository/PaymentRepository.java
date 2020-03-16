package com.sprint.minfi.msgp.repository;

import com.sprint.minfi.msgp.domain.Payment;
import com.sprint.minfi.msgp.service.dto.PaymentDTO;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Payment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

	/**
	 * 
	 * @param idT
	 * @return idPayment 
	 */
	@Query("SELECT code FROM Payment p WHERE p.idTransaction = :idT ")
	PaymentDTO findByIdTransaction(@Param("idT") Long idT);

	@Query("update Payment p set p.statut = :state where p.id = :idPaymeLong")
	PaymentDTO updatePayment(@Param("idPaymeLong") Long idP, @Param("state") String state);

	PaymentDTO findByCode(String code);

	@Query(value = "SELECT code FROM Payment p ORDER BY p.code DESC LIMIT 1", nativeQuery = true)
	String findLastCode();

}
