package com.sprintpay.minfi.msgp.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.el.ELManager;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sprintpay.minfi.msgp.domain.DetailVersementIntermediaire;
import com.sprintpay.minfi.msgp.domain.Payment;
import com.sprintpay.minfi.msgp.domain.enumeration.MeansOfPayment;
import com.sprintpay.minfi.msgp.domain.enumeration.Statut;
import com.sprintpay.minfi.msgp.repository.PaymentRepository;
import com.sprintpay.minfi.msgp.service.PaymentService;
import com.sprintpay.minfi.msgp.service.dto.PaymentDTO;
import com.sprintpay.minfi.msgp.service.dto.TransactionDTO;
import com.sprintpay.minfi.msgp.service.mapper.PaymentMapper;
import com.sprintpay.minfi.msgp.utils.ResponseSumm;

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
	public void update(Long idPaymeLong, Statut state) {
		// TODO Auto-generated method stub
		Optional<Payment> op = paymentRepository.findById(idPaymeLong);
		Payment p = new Payment();
		if (op.isPresent()) { 
			p = op.get();
			p.setStatut(state);
			paymentRepository.saveAndFlush(p);
		}
		//paymentRepository.updatePayment(idPaymeLong, state);
	}

	/**
     * Update the payment by id and state.
     * @param1 id the id of the entity.
     * @param2 the state to set to update Payment.
     */
	@Override
	public void update(Long idPaymeLong, Statut state, Long idTransaction, String refTransaction) {
		// TODO Auto-generated method stub
		Optional<Payment> op = paymentRepository.findById(idPaymeLong);
		Payment p = op.get();
		p.setStatut(state);
		p.setIdTransaction(idTransaction);
		p.setRefTransaction(refTransaction);
		paymentRepository.saveAndFlush(p);
		// paymentRepository.updatePayment(idPaymeLong, state, idTransaction, refTransaction);
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

	@Override
	public Payment findByIdEmission(Long idEmis) {
		// TODO Auto-generated method stub
		return paymentRepository.findByIdEmission(idEmis);
	}

	@Override
    public List<Payment> findByRefTransactionInAndStatut(Set<String> refs, Statut statut){
	    return paymentRepository.findByRefTransactionInAndStatut(refs, statut);
    }

    @Override
    public void updateAllPayments(Set<String> refs, Statut statut, DetailVersementIntermediaire dt){
    	List<Payment> ls = paymentRepository.findByRefTransactionIn(refs);
    	for (Payment payment : ls) {
			payment.setStatut(statut);
			payment.setIdDetVers(dt);
		}
    	paymentRepository.saveAll(ls);
        // paymentRepository.updateAllPayments(refs, statut);
    }

	@Override
	public void update(Long id, Statut status, TransactionDTO transactionDTO) {
		// TODO Auto-generated method stub
		Payment p = paymentRepository.findById(id).get();
		p.setStatut(status);
		p.setIdTransaction(transactionDTO.getId());
		p.setRefTransaction(transactionDTO.getCodeTransaction());
		
		//paymentRepository.updatePaymentWithTransaction(id, status, transactionDTO.getId(), transactionDTO.getCodeTransaction());
	}

    @Override
    public Page<Payment> findAllByCreatedBy(String username, Pageable pageable) {
        return paymentRepository.findAllByCreatedBy(username, pageable);
    }

    @Override
    public Page<Payment> findEmissionByCreatedBy(String username, Pageable pageable) {
        return paymentRepository.findEmissionByCreatedBy(username, pageable);
    }

    @Override
    public Page<Payment> findRNFByCreatedBy(String username, Pageable pageable) {
        return paymentRepository.findRNFByCreatedBy(username, pageable);
    }

	@Override
	public List<Payment> findByStatutAndMeansOfPayment(Statut status, MeansOfPayment MeansOfPayment) {
		// TODO Auto-generated method stub
		return paymentRepository.findByStatutANDMeansOfPayment(status, MeansOfPayment);
	}

	@Override
	public Double summReversementByMeansOfPayment(MeansOfPayment meansOfPayment) {
		// TODO Auto-generated method stub
		Double value = paymentRepository.summReversementByMeansOfPayment(meansOfPayment);
		Double resp = (value != null ? value : 0d);
		return resp;
	}

	@Override
	public Double summReversementByMeansOfPaymentByOrganisation(MeansOfPayment meansOfPaymemnt, Long idOrg) {
		// TODO Auto-generated method stub
		Double value = paymentRepository.summReversementByMeansOfPaymentByOrganisation(meansOfPaymemnt, idOrg);
		Double resp = (value != null ? value : 0d);
		return resp;
	}


}
