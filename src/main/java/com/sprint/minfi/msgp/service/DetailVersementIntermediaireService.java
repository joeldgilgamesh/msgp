package com.sprint.minfi.msgp.service;

import com.sprint.minfi.msgp.service.dto.DetailVersementIntermediaireDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.sprint.minfi.msgp.domain.DetailVersementIntermediaire}.
 */
public interface DetailVersementIntermediaireService {

    /**
     * Save a detailVersementIntermediaire.
     *
     * @param detailVersementIntermediaireDTO the entity to save.
     * @return the persisted entity.
     */
    DetailVersementIntermediaireDTO save(DetailVersementIntermediaireDTO detailVersementIntermediaireDTO);

    /**
     * Get all the detailVersementIntermediaires.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DetailVersementIntermediaireDTO> findAll(Pageable pageable);

    /**
     * Get the "id" detailVersementIntermediaire.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DetailVersementIntermediaireDTO> findOne(Long id);

    /**
     * Delete the "id" detailVersementIntermediaire.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
    
    /**
     * 
     * @param montant1
     * @param montant2
     * @return booléen qui represente l'etat de la comparaison, s'il ya egalité ou pas
     */
    boolean comparerDonnReconcil(double montant1, double montant2);
    
    /**
     * 
     * @param codeVersement
     * @return
     */
	public DetailVersementIntermediaireDTO findByCode(String codeVersement);
}
