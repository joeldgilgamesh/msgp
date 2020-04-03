package com.sprint.minfi.msgp.service.impl;

import com.sprint.minfi.msgp.service.PaymentService;
import com.sprint.minfi.msgp.domain.Payment;
import com.sprint.minfi.msgp.domain.enumeration.Statut;
import com.sprint.minfi.msgp.repository.PaymentRepository;
import com.sprint.minfi.msgp.service.dto.PaymentDTO;
import com.sprint.minfi.msgp.service.mapper.PaymentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Payment}.
 */
@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentRepository paymentRepository;

    private final PaymentMapper paymentMapper;

    public PaymentServiceImpl(PaymentRepository paymentRepository, PaymentMapper paymentMapper) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
    }

    /**
     * Save a payment.
     *
     * @param paymentDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public PaymentDTO save(PaymentDTO paymentDTO) {
        log.debug("Request to save Payment : {}", paymentDTO);
        Payment payment = paymentMapper.toEntity(paymentDTO);
        payment = paymentRepository.save(payment);
        return paymentMapper.toDto(payment);
    }

    /**
     * Get all the payments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PaymentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Payments");
        return paymentRepository.findAll(pageable)
            .map(paymentMapper::toDto);
    }

    /**
     * Get one payment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentDTO> findOne(Long id) {
        log.debug("Request to get Payment : {}", id);
        return paymentRepository.findById(id)
            .map(paymentMapper::toDto);
    }

    /**
     * Delete the payment by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Payment : {}", id);
        paymentRepository.deleteById(id);
    }

    /**
     * Update the payment by id and state.
     * @param1 id the id of the entity.
     * @param2 the state to set to update Payment.
     */
	@Override
	public PaymentDTO update(Long idPaymeLong, String state) {
		// TODO Auto-generated method stub
		return paymentRepository.updatePayment(idPaymeLong, state);
	}

	@Override
	public PaymentDTO findByIdTransation(Long Id) {
		// TODO Auto-generated method stub
		return paymentRepository.findByIdTransaction(Id);
	}

	@Override
	public PaymentDTO findByCode(String code) {
		// TODO Auto-generated method stub
		return paymentRepository.findByCode(code);
	}

	@Override
	public Page<PaymentDTO> findByStatut(Statut statut, Pageable pageable) {
		// TODO Auto-generated method stub
		return paymentRepository.findByStatut(statut, pageable);
	}
}
