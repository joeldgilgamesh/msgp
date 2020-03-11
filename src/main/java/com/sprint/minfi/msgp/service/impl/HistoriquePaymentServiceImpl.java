package com.sprint.minfi.msgp.service.impl;

import com.sprint.minfi.msgp.service.HistoriquePaymentService;
import com.sprint.minfi.msgp.domain.HistoriquePayment;
import com.sprint.minfi.msgp.repository.HistoriquePaymentRepository;
import com.sprint.minfi.msgp.service.dto.HistoriquePaymentDTO;
import com.sprint.minfi.msgp.service.mapper.HistoriquePaymentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Service Implementation for managing {@link HistoriquePayment}.
 */
@Service
@Transactional
public class HistoriquePaymentServiceImpl implements HistoriquePaymentService {

    private final Logger log = LoggerFactory.getLogger(HistoriquePaymentServiceImpl.class);

    private final HistoriquePaymentRepository historiquePaymentRepository;

    private final HistoriquePaymentMapper historiquePaymentMapper;

    public HistoriquePaymentServiceImpl(HistoriquePaymentRepository historiquePaymentRepository, HistoriquePaymentMapper historiquePaymentMapper) {
        this.historiquePaymentRepository = historiquePaymentRepository;
        this.historiquePaymentMapper = historiquePaymentMapper;
    }

    /**
     * Save a historiquePayment.
     *
     * @param historiquePaymentDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public HistoriquePaymentDTO save(HistoriquePaymentDTO historiquePaymentDTO) {
        log.debug("Request to save HistoriquePayment : {}", historiquePaymentDTO);
        HistoriquePayment historiquePayment = historiquePaymentMapper.toEntity(historiquePaymentDTO);
        historiquePayment = historiquePaymentRepository.save(historiquePayment);
        return historiquePaymentMapper.toDto(historiquePayment);
    }

    /**
     * Get all the historiquePayments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<HistoriquePaymentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all HistoriquePayments");
        return historiquePaymentRepository.findAll(pageable)
            .map(historiquePaymentMapper::toDto);
    }

    /**
     * Get one historiquePayment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<HistoriquePaymentDTO> findOne(Long id) {
        log.debug("Request to get HistoriquePayment : {}", id);
        return historiquePaymentRepository.findById(id)
            .map(historiquePaymentMapper::toDto);
    }

    /**
     * Delete the historiquePayment by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete HistoriquePayment : {}", id);
        historiquePaymentRepository.deleteById(id);
    }

	@Override
	public HistoriquePaymentDTO saveHistPay(String status, LocalDateTime datenow) {
		
        HistoriquePayment historiquePayment = historiquePaymentMapper.toEntity(new HistoriquePaymentDTO());
        historiquePayment.setStatus(status);
        historiquePayment.setDateStatus(LocalDateTime.now());
        historiquePayment = historiquePaymentRepository.save(historiquePayment);
        return historiquePaymentMapper.toDto(historiquePayment);
	}
}
