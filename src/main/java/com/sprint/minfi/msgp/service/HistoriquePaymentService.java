package com.sprint.minfi.msgp.service;

import com.sprint.minfi.msgp.domain.Payment;
import com.sprint.minfi.msgp.service.dto.HistoriquePaymentDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.sprint.minfi.msgp.domain.HistoriquePayment}.
 */
public interface HistoriquePaymentService {

    /**
     * Save a historiquePayment.
     *
     * @param historiquePaymentDTO the entity to save.
     * @return the persisted entity.
     */
    HistoriquePaymentDTO save(HistoriquePaymentDTO historiquePaymentDTO);

    /**
     * Get all the historiquePayments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HistoriquePaymentDTO> findAll(Pageable pageable);

    /**
     * Get the "id" historiquePayment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HistoriquePaymentDTO> findOne(Long id);

    /**
     * Delete the "id" historiquePayment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

	HistoriquePaymentDTO saveHistPay(String string, LocalDateTime datenow, Payment payment);
}
