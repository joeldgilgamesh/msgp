package com.sprint.minfi.msgp.web.rest;

import com.sprint.minfi.msgp.service.EmissionService;
import com.sprint.minfi.msgp.web.rest.errors.BadRequestAlertException;
import com.sprint.minfi.msgp.service.dto.EmissionDTO;

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
 * REST controller for managing {@link com.sprint.minfi.msgp.domain.Emission}.
 */
@RestController
@RequestMapping("/api")
public class EmissionResource {

    private final Logger log = LoggerFactory.getLogger(EmissionResource.class);

    private static final String ENTITY_NAME = "spminfimsgpEmission";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmissionService emissionService;

    public EmissionResource(EmissionService emissionService) {
        this.emissionService = emissionService;
    }

    /**
     * {@code POST  /emissions} : Create a new emission.
     *
     * @param emissionDTO the emissionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new emissionDTO, or with status {@code 400 (Bad Request)} if the emission has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/emissions")
    public ResponseEntity<EmissionDTO> createEmission(@RequestBody EmissionDTO emissionDTO) throws URISyntaxException {
        log.debug("REST request to save Emission : {}", emissionDTO);
        if (emissionDTO.getId() != null) {
            throw new BadRequestAlertException("A new emission cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EmissionDTO result = emissionService.save(emissionDTO);
        return ResponseEntity.created(new URI("/api/emissions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /emissions} : Updates an existing emission.
     *
     * @param emissionDTO the emissionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emissionDTO,
     * or with status {@code 400 (Bad Request)} if the emissionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the emissionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/emissions")
    public ResponseEntity<EmissionDTO> updateEmission(@RequestBody EmissionDTO emissionDTO) throws URISyntaxException {
        log.debug("REST request to update Emission : {}", emissionDTO);
        if (emissionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EmissionDTO result = emissionService.save(emissionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, emissionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /emissions} : get all the emissions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of emissions in body.
     */
    @GetMapping("/emissions")
    public List<EmissionDTO> getAllEmissions() {
        log.debug("REST request to get all Emissions");
        return emissionService.findAll();
    }

    /**
     * {@code GET  /emissions/:id} : get the "id" emission.
     *
     * @param id the id of the emissionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the emissionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/emissions/{id}")
    public ResponseEntity<EmissionDTO> getEmission(@PathVariable Long id) {
        log.debug("REST request to get Emission : {}", id);
        Optional<EmissionDTO> emissionDTO = emissionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(emissionDTO);
    }

    /**
     * {@code DELETE  /emissions/:id} : delete the "id" emission.
     *
     * @param id the id of the emissionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/emissions/{id}")
    public ResponseEntity<Void> deleteEmission(@PathVariable Long id) {
        log.debug("REST request to delete Emission : {}", id);
        emissionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
