package com.sprint.minfi.msgp.service.impl;

import com.sprint.minfi.msgp.service.EmissionService;
import com.sprint.minfi.msgp.domain.Emission;
import com.sprint.minfi.msgp.repository.EmissionRepository;
import com.sprint.minfi.msgp.service.dto.EmissionDTO;
import com.sprint.minfi.msgp.service.mapper.EmissionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Emission}.
 */
@Service
@Transactional
public class EmissionServiceImpl implements EmissionService {

    private final Logger log = LoggerFactory.getLogger(EmissionServiceImpl.class);

    private final EmissionRepository emissionRepository;

    private final EmissionMapper emissionMapper;

    public EmissionServiceImpl(EmissionRepository emissionRepository, EmissionMapper emissionMapper) {
        this.emissionRepository = emissionRepository;
        this.emissionMapper = emissionMapper;
    }

    /**
     * Save a emission.
     *
     * @param emissionDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public EmissionDTO save(EmissionDTO emissionDTO) {
        log.debug("Request to save Emission : {}", emissionDTO);
        Emission emission = emissionMapper.toEntity(emissionDTO);
        emission = emissionRepository.save(emission);
        return emissionMapper.toDto(emission);
    }

    /**
     * Get all the emissions.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<EmissionDTO> findAll() {
        log.debug("Request to get all Emissions");
        return emissionRepository.findAll().stream()
            .map(emissionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one emission by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<EmissionDTO> findOne(Long id) {
        log.debug("Request to get Emission : {}", id);
        return emissionRepository.findById(id)
            .map(emissionMapper::toDto);
    }

    /**
     * Delete the emission by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Emission : {}", id);
        emissionRepository.deleteById(id);
    }
}
