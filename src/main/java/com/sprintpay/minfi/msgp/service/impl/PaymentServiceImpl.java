package com.sprintpay.minfi.msgp.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sprintpay.minfi.msgp.domain.Payment;
import com.sprintpay.minfi.msgp.domain.enumeration.Statut;
import com.sprintpay.minfi.msgp.repository.PaymentRepository;
import com.sprintpay.minfi.msgp.service.PaymentService;
import com.sprintpay.minfi.msgp.service.RESTClientEmissionService;
import com.sprintpay.minfi.msgp.service.dto.EmissionDTO;
import com.sprintpay.minfi.msgp.service.dto.PaymentDTO;
import com.sprintpay.minfi.msgp.service.mapper.PaymentMapper;

/**
 * Service Implementation for managing {@link Payment}.
 */
@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentRepository paymentRepository;

    private final PaymentMapper paymentMapper;
    
    private final RESTClientEmissionService restClientEmissionService;

    public PaymentServiceImpl(PaymentRepository paymentRepository, PaymentMapper paymentMapper, RESTClientEmissionService restClientEmissionService) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.restClientEmissionService = restClientEmissionService;
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
	public void update(Long idPaymeLong, Statut state) {
		// TODO Auto-generated method stub
		paymentRepository.updatePayment(idPaymeLong, state);
	}
	
	/**
     * Update the payment by id and state.
     * @param1 id the id of the entity.
     * @param2 the state to set to update Payment.
     */
	@Override
	public void update(Long idPaymeLong, Statut state, Long idTransaction, String refTransaction) {
		// TODO Auto-generated method stub
		paymentRepository.updatePayment(idPaymeLong, state, idTransaction, refTransaction);
	}

	@Override
	public Payment findByIdTransation(Long Id) {
		// TODO Auto-generated method stub
		return paymentRepository.findByIdTransaction(Id);
	}

	@Override
	public Payment findByCode(String code) {
		// TODO Auto-generated method stub
		return paymentRepository.findByCode(code);
	}

	@Override
	public Page<Object> findByStatut(Statut status, Pageable pageable) {
		// TODO Auto-generated method stub
		return paymentRepository.findByPaymentValidated(status, pageable);
	}

	@Override
	public Payment findByIdTransaction(Long id) {
		// TODO Auto-generated method stub
		return paymentRepository.findByIdTransaction(id);
	}
	
	@Override
	public Payment findByRefTransaction(String refTransaction) {
		// TODO Auto-generated method stub
		return paymentRepository.findByRefTransaction(refTransaction);
	}
	
//	@Scheduled(fixedDelay = 60000)
//	public void testFind(){
//		System.out.println("------------------ cecic est le service de test ouvert a tous les tests " + paymentRepository.findAll().get(0));
//	}
	
}