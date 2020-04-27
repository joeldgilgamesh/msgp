package com.sprintpay.minfi.msgp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sprintpay.minfi.msgp.domain.Payment;
import com.sprintpay.minfi.msgp.domain.enumeration.Statut;
import com.sprintpay.minfi.msgp.service.dto.PaymentDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.sprint.minfi.msgp.domain.Payment}.
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
}