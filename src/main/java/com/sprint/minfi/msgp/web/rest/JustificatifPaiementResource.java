package com.sprint.minfi.msgp.web.rest;

import com.sprint.minfi.msgp.service.JustificatifPaiementService;
import com.sprint.minfi.msgp.web.rest.errors.BadRequestAlertException;
import com.sprint.minfi.msgp.service.dto.JustificatifPaiementDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.sprint.minfi.msgp.domain.JustificatifPaiement}.
 */
@RestController
@RequestMapping("/api")
public class JustificatifPaiementResource {

    private final Logger log = LoggerFactory.getLogger(JustificatifPaiementResource.class);

    private static final String ENTITY_NAME = "spminfimsgpJustificatifPaiement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JustificatifPaiementService justificatifPaiementService;

    public JustificatifPaiementResource(JustificatifPaiementService justificatifPaiementService) {
        this.justificatifPaiementService = justificatifPaiementService;
    }

    /**
     * {@code POST  /justificatif-paiements} : Create a new justificatifPaiement.
     *
     * @param justificatifPaiementDTO the justificatifPaiementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new justificatifPaiementDTO, or with status {@code 400 (Bad Request)} if the justificatifPaiement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/justificatif-paiements")
    public ResponseEntity<JustificatifPaiementDTO> createJustificatifPaiement(@RequestBody JustificatifPaiementDTO justificatifPaiementDTO) throws URISyntaxException {
        log.debug("REST request to save JustificatifPaiement : {}", justificatifPaiementDTO);
        if (justificatifPaiementDTO.getId() != null) {
            throw new BadRequestAlertException("A new justificatifPaiement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        JustificatifPaiementDTO result = justificatifPaiementService.save(justificatifPaiementDTO);
        return ResponseEntity.created(new URI("/api/justificatif-paiements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /justificatif-paiements} : Updates an existing justificatifPaiement.
     *
     * @param justificatifPaiementDTO the justificatifPaiementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated justificatifPaiementDTO,
     * or with status {@code 400 (Bad Request)} if the justificatifPaiementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the justificatifPaiementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/justificatif-paiements")
    public ResponseEntity<JustificatifPaiementDTO> updateJustificatifPaiement(@RequestBody JustificatifPaiementDTO justificatifPaiementDTO) throws URISyntaxException {
        log.debug("REST request to update JustificatifPaiement : {}", justificatifPaiementDTO);
        if (justificatifPaiementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        JustificatifPaiementDTO result = justificatifPaiementService.save(justificatifPaiementDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, justificatifPaiementDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /justificatif-paiements} : get all the justificatifPaiements.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of justificatifPaiements in body.
     */
    @GetMapping("/justificatif-paiements")
    public List<JustificatifPaiementDTO> getAllJustificatifPaiements() {
        log.debug("REST request to get all JustificatifPaiements");
        return justificatifPaiementService.findAll();
    }

    /**
     * {@code GET  /justificatif-paiements/:id} : get the "id" justificatifPaiement.
     *
     * @param id the id of the justificatifPaiementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the justificatifPaiementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/justificatif-paiements/{id}")
    public ResponseEntity<JustificatifPaiementDTO> getJustificatifPaiement(@PathVariable Long id) {
        log.debug("REST request to get JustificatifPaiement : {}", id);
        Optional<JustificatifPaiementDTO> justificatifPaiementDTO = justificatifPaiementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(justificatifPaiementDTO);
    }

    /**
     * {@code DELETE  /justificatif-paiements/:id} : delete the "id" justificatifPaiement.
     *
     * @param id the id of the justificatifPaiementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/justificatif-paiements/{id}")
    public ResponseEntity<Void> deleteJustificatifPaiement(@PathVariable Long id) {
        log.debug("REST request to delete JustificatifPaiement : {}", id);
        justificatifPaiementService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
