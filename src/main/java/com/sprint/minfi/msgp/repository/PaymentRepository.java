package com.sprint.minfi.msgp.repository;

import com.sprint.minfi.msgp.domain.Payment;
import com.sprint.minfi.msgp.domain.enumeration.Statut;
import com.sprint.minfi.msgp.service.dto.PaymentDTO;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
	@Query("SELECT code FROM Payment p WHERE p.idTransaction = :idT")
	PaymentDTO findByIdTransaction(@Param("idT") Long idT);

	@Query("update Payment p set p.statut = :state where p.id = :idPaymeLong")
	PaymentDTO updatePayment(@Param("idPaymeLong") Long idP, @Param("state") String state);

	PaymentDTO findByCode(String code);

	@Query(value = "SELECT COUNT(id) FROM Payment", nativeQuery = true)
	Long countLine();
	
	@Query("SELECT id FROM Payment p WHERE p.id = :idLast")
	Long getLastId(@Param("idLast") Long idLast);

//	@Query("SELECT p FROM Payment p WHERE p.statut = 'VALIDATED'")
//	Page<Object> findByPaymentValidated(Pageable pageable);
	
	@Query("SELECT p FROM Payment p WHERE p.statut = :stat")
	Page<Object> findByPaymentValidated(@Param("stat") Statut status, Pageable pageable);


}
