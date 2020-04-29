package com.sprintpay.minfi.msgp.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sprintpay.minfi.msgp.domain.Payment;
import com.sprintpay.minfi.msgp.domain.enumeration.Statut;
import com.sprintpay.minfi.msgp.service.dto.PaymentDTO;

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
	
	@Modifying
	@Query("update Payment p set p.statut = :state where p.id = :idPaymeLong")
	void updatePayment(@Param("idPaymeLong") Long idPaymeLong, @Param("state") Statut state);

	@Modifying
	@Query("update Payment p set p.statut = :state, p.idTransaction = :idTransaction, p.refTransaction = :refTransaction where p.id = :idPaymeLong")
	void updatePayment(@Param("idPaymeLong") Long idPaymeLong, @Param("state") Statut state,  @Param("idTransaction") Long idTransaction, @Param("refTransaction") String refTransaction);

	Payment findByCode(String code);

	@Query(value = "SELECT COUNT(id) FROM Payment")
	Long countLine();
	
	@Query("SELECT id FROM Payment p WHERE p.id = :idLast")
	Long getLastId(@Param("idLast") Long idLast);

//	@Query("SELECT p FROM Payment p WHERE p.statut = 'VALIDATED'")
//	Page<Object> findByPaymentValidated(Pageable pageable);
	
	@Query("SELECT p FROM Payment p WHERE p.statut = :stat")
	Page<Object> findByPaymentValidated(@Param("stat") Statut status, Pageable pageable);

	Payment findByIdTransaction(Long idTransaction);

	Payment findByRefTransaction(String refTransaction);
	
	Payment findByIdEmission(String idEmis);
	
//	@Query("SELECT p FROM Payment p INNER JOIN Emission e ON e.id = p.emission WHERE e.codeContribuable = :niu")
//	Page<Object> findPaymentEmissionContrib(@Param("niu") String niu, Pageable pageable);


}
