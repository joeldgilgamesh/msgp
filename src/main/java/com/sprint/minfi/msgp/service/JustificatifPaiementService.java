package com.sprint.minfi.msgp.service;

import com.sprint.minfi.msgp.domain.JustificatifPaiement;
import com.sprint.minfi.msgp.repository.JustificatifPaiementRepository;
import com.sprint.minfi.msgp.service.dto.JustificatifPaiementDTO;
import com.sprint.minfi.msgp.service.mapper.JustificatifPaiementMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link JustificatifPaiement}.
 */
@Service
@Transactional
public class JustificatifPaiementService {

    private final Logger log = LoggerFactory.getLogger(JustificatifPaiementService.class);

    private final JustificatifPaiementRepository justificatifPaiementRepository;

    private final JustificatifPaiementMapper justificatifPaiementMapper;

    public JustificatifPaiementService(JustificatifPaiementRepository justificatifPaiementRepository, JustificatifPaiementMapper justificatifPaiementMapper) {
        this.justificatifPaiementRepository = justificatifPaiementRepository;
        this.justificatifPaiementMapper = justificatifPaiementMapper;
    }

    /**
     * Save a justificatifPaiement.
     *
     * @param justificatifPaiementDTO the entity to save.
     * @return the persisted entity.
     */
    public JustificatifPaiementDTO save(JustificatifPaiementDTO justificatifPaiementDTO) {
        log.debug("Request to save JustificatifPaiement : {}", justificatifPaiementDTO);
        JustificatifPaiement justificatifPaiement = justificatifPaiementMapper.toEntity(justificatifPaiementDTO);
        justificatifPaiement = justificatifPaiementRepository.save(justificatifPaiement);
        return justificatifPaiementMapper.toDto(justificatifPaiement);
    }

    /**
     * Get all the justificatifPaiements.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<JustificatifPaiementDTO> findAll() {
        log.debug("Request to get all JustificatifPaiements");
        return justificatifPaiementRepository.findAll().stream()
            .map(justificatifPaiementMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one justificatifPaiement by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<JustificatifPaiementDTO> findOne(Long id) {
        log.debug("Request to get JustificatifPaiement : {}", id);
        return justificatifPaiementRepository.findById(id)
            .map(justificatifPaiementMapper::toDto);
    }

    /**
     * Delete the justificatifPaiement by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete JustificatifPaiement : {}", id);
        justificatifPaiementRepository.deleteById(id);
    }
}
