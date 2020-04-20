package com.sprint.minfi.msgp.web.rest;

import com.sprint.minfi.msgp.service.EmissionHistoriqueService;
import com.sprint.minfi.msgp.web.rest.errors.BadRequestAlertException;
import com.sprint.minfi.msgp.service.dto.EmissionHistoriqueDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.sprint.minfi.msgp.domain.EmissionHistorique}.
 */
@RestController
@RequestMapping("/api")
public class EmissionHistoriqueResource {

    private final Logger log = LoggerFactory.getLogger(EmissionHistoriqueResource.class);

    private static final String ENTITY_NAME = "spminfimsgpEmissionHistorique";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmissionHistoriqueService emissionHistoriqueService;

    public EmissionHistoriqueResource(EmissionHistoriqueService emissionHistoriqueService) {
        this.emissionHistoriqueService = emissionHistoriqueService;
    }

    /**
     * {@code POST  /emission-historiques} : Create a new emissionHistorique.
     *
     * @param emissionHistoriqueDTO the emissionHistoriqueDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new emissionHistoriqueDTO, or with status {@code 400 (Bad Request)} if the emissionHistorique has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/emission-historiques")
    public ResponseEntity<EmissionHistoriqueDTO> createEmissionHistorique(@RequestBody EmissionHistoriqueDTO emissionHistoriqueDTO) throws URISyntaxException {
        log.debug("REST request to save EmissionHistorique : {}", emissionHistoriqueDTO);
        if (emissionHistoriqueDTO.getId() != null) {
            throw new BadRequestAlertException("A new emissionHistorique cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EmissionHistoriqueDTO result = emissionHistoriqueService.save(emissionHistoriqueDTO);
        return ResponseEntity.created(new URI("/api/emission-historiques/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /emission-historiques} : Updates an existing emissionHistorique.
     *
     * @param emissionHistoriqueDTO the emissionHistoriqueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emissionHistoriqueDTO,
     * or with status {@code 400 (Bad Request)} if the emissionHistoriqueDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the emissionHistoriqueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/emission-historiques")
    public ResponseEntity<EmissionHistoriqueDTO> updateEmissionHistorique(@RequestBody EmissionHistoriqueDTO emissionHistoriqueDTO) throws URISyntaxException {
        log.debug("REST request to update EmissionHistorique : {}", emissionHistoriqueDTO);
        if (emissionHistoriqueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EmissionHistoriqueDTO result = emissionHistoriqueService.save(emissionHistoriqueDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, emissionHistoriqueDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /emission-historiques} : get all the emissionHistoriques.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of emissionHistoriques in body.
     */
    @GetMapping("/emission-historiques")
    public ResponseEntity<List<EmissionHistoriqueDTO>> getAllEmissionHistoriques(Pageable pageable) {
        log.debug("REST request to get a page of EmissionHistoriques");
        Page<EmissionHistoriqueDTO> page = emissionHistoriqueService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /emission-historiques/:id} : get the "id" emissionHistorique.
     *
     * @param id the id of the emissionHistoriqueDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the emissionHistoriqueDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/emission-historiques/{id}")
    public ResponseEntity<EmissionHistoriqueDTO> getEmissionHistorique(@PathVariable Long id) {
        log.debug("REST request to get EmissionHistorique : {}", id);
        Optional<EmissionHistoriqueDTO> emissionHistoriqueDTO = emissionHistoriqueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(emissionHistoriqueDTO);
    }

    /**
     * {@code DELETE  /emission-historiques/:id} : delete the "id" emissionHistorique.
     *
     * @param id the id of the emissionHistoriqueDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/emission-historiques/{id}")
    public ResponseEntity<Void> deleteEmissionHistorique(@PathVariable Long id) {
        log.debug("REST request to delete EmissionHistorique : {}", id);
        emissionHistoriqueService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
