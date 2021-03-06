package com.sprintpay.minfi.msgp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.sprintpay.minfi.msgp.domain.DetailVersementIntermediaire;
import com.sprintpay.minfi.msgp.domain.enumeration.MeansOfPayment;
import com.sprintpay.minfi.msgp.service.dto.DetailVersementIntermediaireDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.sprintpay.minfi.msgp.domain.DetailVersementIntermediaire}.
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
	Optional<DetailVersementIntermediaireDTO> findByNumeroVersment(String codeVersement);
	
	/**
	 * 
	 * @param meansOfPayment
	 * @return
	 */
	List<DetailVersementIntermediaire> findDetailVersementIntermediaire(MeansOfPayment meansOfPayment);

	List<DetailVersementIntermediaire> findDetailVersementIntermediaireByOrganisation(MeansOfPayment meanOfPayment,
			Long idOrg);
}
