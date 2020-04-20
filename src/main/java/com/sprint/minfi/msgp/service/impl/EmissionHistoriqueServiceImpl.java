package com.sprint.minfi.msgp.service.impl;

import com.sprint.minfi.msgp.service.EmissionHistoriqueService;
import com.sprint.minfi.msgp.domain.EmissionHistorique;
import com.sprint.minfi.msgp.repository.EmissionHistoriqueRepository;
import com.sprint.minfi.msgp.service.dto.EmissionHistoriqueDTO;
import com.sprint.minfi.msgp.service.mapper.EmissionHistoriqueMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link EmissionHistorique}.
 */
@Service
@Transactional
public class EmissionHistoriqueServiceImpl implements EmissionHistoriqueService {

    private final Logger log = LoggerFactory.getLogger(EmissionHistoriqueServiceImpl.class);

    private final EmissionHistoriqueRepository emissionHistoriqueRepository;

    private final EmissionHistoriqueMapper emissionHistoriqueMapper;

    public EmissionHistoriqueServiceImpl(EmissionHistoriqueRepository emissionHistoriqueRepository, EmissionHistoriqueMapper emissionHistoriqueMapper) {
        this.emissionHistoriqueRepository = emissionHistoriqueRepository;
        this.emissionHistoriqueMapper = emissionHistoriqueMapper;
    }

    /**
     * Save a emissionHistorique.
     *
     * @param emissionHistoriqueDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public EmissionHistoriqueDTO save(EmissionHistoriqueDTO emissionHistoriqueDTO) {
        log.debug("Request to save EmissionHistorique : {}", emissionHistoriqueDTO);
        EmissionHistorique emissionHistorique = emissionHistoriqueMapper.toEntity(emissionHistoriqueDTO);
        emissionHistorique = emissionHistoriqueRepository.save(emissionHistorique);
        return emissionHistoriqueMapper.toDto(emissionHistorique);
    }

    /**
     * Get all the emissionHistoriques.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<EmissionHistoriqueDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EmissionHistoriques");
        return emissionHistoriqueRepository.findAll(pageable)
            .map(emissionHistoriqueMapper::toDto);
    }

    /**
     * Get one emissionHistorique by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<EmissionHistoriqueDTO> findOne(Long id) {
        log.debug("Request to get EmissionHistorique : {}", id);
        return emissionHistoriqueRepository.findById(id)
            .map(emissionHistoriqueMapper::toDto);
    }

    /**
     * Delete the emissionHistorique by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete EmissionHistorique : {}", id);
        emissionHistoriqueRepository.deleteById(id);
    }
}
