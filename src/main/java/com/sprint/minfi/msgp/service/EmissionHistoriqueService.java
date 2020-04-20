package com.sprint.minfi.msgp.service;

import com.sprint.minfi.msgp.service.dto.EmissionHistoriqueDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.sprint.minfi.msgp.domain.EmissionHistorique}.
 */
public interface EmissionHistoriqueService {

    /**
     * Save a emissionHistorique.
     *
     * @param emissionHistoriqueDTO the entity to save.
     * @return the persisted entity.
     */
    EmissionHistoriqueDTO save(EmissionHistoriqueDTO emissionHistoriqueDTO);

    /**
     * Get all the emissionHistoriques.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EmissionHistoriqueDTO> findAll(Pageable pageable);

    /**
     * Get the "id" emissionHistorique.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EmissionHistoriqueDTO> findOne(Long id);

    /**
     * Delete the "id" emissionHistorique.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
