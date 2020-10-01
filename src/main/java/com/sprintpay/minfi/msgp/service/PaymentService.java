package com.sprintpay.minfi.msgp.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.sprintpay.minfi.msgp.domain.DetailVersementIntermediaire;
import com.sprintpay.minfi.msgp.domain.Payment;
import com.sprintpay.minfi.msgp.domain.enumeration.MeansOfPayment;
import com.sprintpay.minfi.msgp.domain.enumeration.Statut;
import com.sprintpay.minfi.msgp.service.dto.PaymentDTO;
import com.sprintpay.minfi.msgp.service.dto.TransactionDTO;

/**
 * Service Interface for managing {@link com.sprintpay.minfi.msgp.domain.Payment}.
 */
/**
 * @author yan
 *
 */
public interface PaymentService {

    /**
     * Save a payment.
     *
     * @param paymentDTO the entity to save.
     * @return the persisted entity.
     */
    PaymentDTO save(PaymentDTO paymentDTO);

    /**
     * Get all the payments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PaymentDTO> findAll(Pageable pageable);

    /**
     * Get the "id" payment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PaymentDTO> findOne(Long id);

    /**
     * Delete the "id" payment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     *
     * @param idPaymeLong
     * @param state
     * @return
     */
    void update(Long idPaymeLong, Statut state);

    /**
     *
     * @param idPaymeLong
     * @param state
     * @param idTransaction
     * @param refTransaction
     * @return
     */
    void update(Long idPaymeLong, Statut state, Long idTransaction, String refTransaction);
    /**
     *
     * @param Id
     * @return PaymentDTO
     */
	Payment findByIdTransation(Long Id);

	/**
	 *
	 * @param code
	 * @return
	 */
	Payment findByCode(String code);

	/**
	 *
	 * @param statut
	 * @param pageable
	 * @return
	 */
	Page<Object> findByStatut(Statut status, Pageable pageable);

	/**
	 *
	 * @param idTransaction
	 * @return
	 */
	Payment findByIdTransaction(Long idTransaction);

	/**
	 *
	 * @param refTransaction
	 * @return
	 */
	Payment findByRefTransaction(String refTransaction);

	/**
	 *
	 * @param idEmis
	 * @return
	 */
	Payment findByIdEmission(Long idEmis);

    List<Payment> findByRefTransactionInAndStatut(Set<String> refs, Statut statut);

    void updateAllPayments(Set<String> refs, Statut statut, DetailVersementIntermediaire dt);

	void update(Long id, Statut status, TransactionDTO transactionDTO);

    Page<Payment> findAllByCreatedBy(String username, Pageable pageable);

    Page<Payment> findEmissionByCreatedBy(@Param("user") String username, Pageable pageable);

    Page<Payment> findRNFByCreatedBy(@Param("user") String username, Pageable pageable);
    
    List<Payment> findByStatutAndMeansOfPayment(Statut status, MeansOfPayment MeansOfPayment);
    
    Double summReversementByMeansOfPayment(MeansOfPayment meansOfPayment);

	Double summReversementByMeansOfPaymentByOrganisation(MeansOfPayment meansOfPaymemnt, Long idOrg);
}
