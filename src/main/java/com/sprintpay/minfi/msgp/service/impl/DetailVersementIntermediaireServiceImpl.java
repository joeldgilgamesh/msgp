package com.sprintpay.minfi.msgp.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sprintpay.minfi.msgp.domain.DetailVersementIntermediaire;
import com.sprintpay.minfi.msgp.repository.DetailVersementIntermediaireRepository;
import com.sprintpay.minfi.msgp.service.DetailVersementIntermediaireService;
import com.sprintpay.minfi.msgp.service.dto.DetailVersementIntermediaireDTO;
import com.sprintpay.minfi.msgp.service.mapper.DetailVersementIntermediaireMapper;

import java.util.Optional;

/**
 * Service Implementation for managing {@link DetailVersementIntermediaire}.
 */
@Service
@Transactional
public class DetailVersementIntermediaireServiceImpl implements DetailVersementIntermediaireService {

    private final Logger log = LoggerFactory.getLogger(DetailVersementIntermediaireServiceImpl.class);

    private final DetailVersementIntermediaireRepository detailVersementIntermediaireRepository;

    private final DetailVersementIntermediaireMapper detailVersementIntermediaireMapper;

    public DetailVersementIntermediaireServiceImpl(DetailVersementIntermediaireRepository detailVersementIntermediaireRepository, DetailVersementIntermediaireMapper detailVersementIntermediaireMapper) {
        this.detailVersementIntermediaireRepository = detailVersementIntermediaireRepository;
        this.detailVersementIntermediaireMapper = detailVersementIntermediaireMapper;
    }

    /**
     * Save a detailVersementIntermediaire.
     *
     * @param detailVersementIntermediaireDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public DetailVersementIntermediaireDTO save(DetailVersementIntermediaireDTO detailVersementIntermediaireDTO) {
        log.debug("Request to save DetailVersementIntermediaire : {}", detailVersementIntermediaireDTO);
        DetailVersementIntermediaire detailVersementIntermediaire = detailVersementIntermediaireMapper.toEntity(detailVersementIntermediaireDTO);
        detailVersementIntermediaire = detailVersementIntermediaireRepository.save(detailVersementIntermediaire);
        return detailVersementIntermediaireMapper.toDto(detailVersementIntermediaire);
    }

    /**
     * Get all the detailVersementIntermediaires.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DetailVersementIntermediaireDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DetailVersementIntermediaires");
        return detailVersementIntermediaireRepository.findAll(pageable)
            .map(detailVersementIntermediaireMapper::toDto);
    }

    /**
     * Get one detailVersementIntermediaire by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DetailVersementIntermediaireDTO> findOne(Long id) {
        log.debug("Request to get DetailVersementIntermediaire : {}", id);
        return detailVersementIntermediaireRepository.findById(id)
            .map(detailVersementIntermediaireMapper::toDto);
    }

    /**
     * Delete the detailVersementIntermediaire by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete DetailVersementIntermediaire : {}", id);
        detailVersementIntermediaireRepository.deleteById(id);
    }

	@Override
	public boolean comparerDonnReconcil(double montant1, double montant2) {
		// TODO Auto-generated method stub
		return (montant1 - montant2 == 0 ? true : false);
	}

	@Override
	public DetailVersementIntermediaireDTO findByCode(String codeVersement) {
		// TODO Auto-generated method stub
		return detailVersementIntermediaireRepository.findByNumeroVersment(codeVersement);
	}
	
	

}
