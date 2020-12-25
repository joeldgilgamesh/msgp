package com.sprintpay.minfi.msgp.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sprintpay.minfi.msgp.domain.Payment;
import com.sprintpay.minfi.msgp.domain.enumeration.MeansOfPayment;
import com.sprintpay.minfi.msgp.domain.enumeration.Statut;

/**
 * Spring Data  repository for the Payment entity.
 */

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

	Payment findByCode(String code);

	@Query(value = "SELECT COUNT(id) FROM Payment")
	Long countLine();

	@Query("SELECT p FROM Payment p WHERE p.statut = :stat")
	Page<Object> findByPaymentValidated(@Param("stat") Statut status, Pageable pageable);

	Payment findByIdTransaction(Long idTransaction);

	Payment findByRefTransaction(String refTransaction);

	Payment findByIdEmission(Long idEmis);

	List<Payment> findByRefTransactionInAndStatut(Set<String> refs, Statut statut);
	
	List<Payment> findByRefTransactionIn(Set<String> refs);

    @Query("SELECT p FROM Payment p WHERE p.createdBy = :user ORDER BY p.createdDate DESC")
    Page<Payment> findAllByCreatedBy(@Param("user") String username, Pageable pageable);

    @Query("SELECT p FROM Payment p WHERE p.createdBy = :user AND (p.idEmission is not null AND p.idEmission > 0) ORDER BY p.createdDate DESC")
    Page<Payment> findEmissionByCreatedBy(@Param("user") String username, Pageable pageable);

    @Query("SELECT p FROM Payment p WHERE p.createdBy = :user AND (p.idRecette is not null AND p.idRecette > 0) ORDER BY p.createdDate DESC")
    Page<Payment> findRNFByCreatedBy(@Param("user") String username, Pageable pageable);
    
    
    @Query("SELECT p FROM Payment p WHERE p.statut = :status AND (p.idDetVers is not null AND p.meansOfPayment = :meansOfPayment)")
    List<Payment> findByStatutANDMeansOfPayment(@Param("status") Statut status, @Param("meansOfPayment") MeansOfPayment meansOfPayment); //les paiement reconcilié d'un moyen de paiement
    
    
//    List<Payment> findByStatutAndMeansOfPayment(@Param("status") Statut status, @Param("MeansOfPayment") MeansOfPayment MeansOfPayment);
    
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.statut = 'RECONCILED' AND ((p.idDetVers is not null AND p.meansOfPayment = :meansOfPayment))")
    Double summReversementByMeansOfPayment(@Param("meansOfPayment") MeansOfPayment meansOfPayment); //la somme des reversements des paiement reconcilié d'un moyen de paiement
    
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.statut = 'RECONCILED' AND ((p.idDetVers is not null AND ((p.meansOfPayment = :meansOfPayment AND p.idOrganisation = :idOrg))))")
    Double summReversementByMeansOfPaymentByOrganisation(@Param("meansOfPayment") MeansOfPayment meansOfPayment, @Param("idOrg") Long idOrg); //la somme des reversements des paiement reconcilié d'un moyen de paiement et dune organisation
    		
}
