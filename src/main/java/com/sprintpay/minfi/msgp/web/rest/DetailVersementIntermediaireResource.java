package com.sprintpay.minfi.msgp.web.rest;

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

import com.sprintpay.minfi.msgp.service.DetailVersementIntermediaireService;
import com.sprintpay.minfi.msgp.service.dto.DetailVersementIntermediaireDTO;
import com.sprintpay.minfi.msgp.web.rest.errors.BadRequestAlertException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.sprintpay.minfi.msgp.domain.DetailVersementIntermediaire}.
 */
@RestController
@RequestMapping("/api")
public class DetailVersementIntermediaireResource {

    private final Logger log = LoggerFactory.getLogger(DetailVersementIntermediaireResource.class);

    private static final String ENTITY_NAME = "spminfimsgpDetailVersementIntermediaire";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DetailVersementIntermediaireService detailVersementIntermediaireService;

    public DetailVersementIntermediaireResource(DetailVersementIntermediaireService detailVersementIntermediaireService) {
        this.detailVersementIntermediaireService = detailVersementIntermediaireService;
    }

    /**
     * {@code POST  /detail-versement-intermediaires} : Create a new detailVersementIntermediaire.
     *
     * @param detailVersementIntermediaireDTO the detailVersementIntermediaireDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new detailVersementIntermediaireDTO, or with status {@code 400 (Bad Request)} if the detailVersementIntermediaire has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/detail-versement-intermediaires")
    public ResponseEntity<DetailVersementIntermediaireDTO> createDetailVersementIntermediaire(@RequestBody DetailVersementIntermediaireDTO detailVersementIntermediaireDTO) throws URISyntaxException {
        log.debug("REST request to save DetailVersementIntermediaire : {}", detailVersementIntermediaireDTO);
        if (detailVersementIntermediaireDTO.getId() != null) {
            throw new BadRequestAlertException("A new detailVersementIntermediaire cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DetailVersementIntermediaireDTO result = detailVersementIntermediaireService.save(detailVersementIntermediaireDTO);
        return ResponseEntity.created(new URI("/api/detail-versement-intermediaires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /detail-versement-intermediaires} : Updates an existing detailVersementIntermediaire.
     *
     * @param detailVersementIntermediaireDTO the detailVersementIntermediaireDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated detailVersementIntermediaireDTO,
     * or with status {@code 400 (Bad Request)} if the detailVersementIntermediaireDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the detailVersementIntermediaireDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/detail-versement-intermediaires")
    public ResponseEntity<DetailVersementIntermediaireDTO> updateDetailVersementIntermediaire(@RequestBody DetailVersementIntermediaireDTO detailVersementIntermediaireDTO) throws URISyntaxException {
        log.debug("REST request to update DetailVersementIntermediaire : {}", detailVersementIntermediaireDTO);
        if (detailVersementIntermediaireDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DetailVersementIntermediaireDTO result = detailVersementIntermediaireService.save(detailVersementIntermediaireDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, detailVersementIntermediaireDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /detail-versement-intermediaires} : get all the detailVersementIntermediaires.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of detailVersementIntermediaires in body.
     */
    @GetMapping("/detail-versement-intermediaires")
    public ResponseEntity<List<DetailVersementIntermediaireDTO>> getAllDetailVersementIntermediaires(Pageable pageable) {
        log.debug("REST request to get a page of DetailVersementIntermediaires");
        Page<DetailVersementIntermediaireDTO> page = detailVersementIntermediaireService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /detail-versement-intermediaires/:id} : get the "id" detailVersementIntermediaire.
     *
     * @param id the id of the detailVersementIntermediaireDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the detailVersementIntermediaireDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/detail-versement-intermediaires/{id}")
    public ResponseEntity<DetailVersementIntermediaireDTO> getDetailVersementIntermediaire(@PathVariable Long id) {
        log.debug("REST request to get DetailVersementIntermediaire : {}", id);
        Optional<DetailVersementIntermediaireDTO> detailVersementIntermediaireDTO = detailVersementIntermediaireService.findOne(id);
        return ResponseUtil.wrapOrNotFound(detailVersementIntermediaireDTO);
    }

    /**
     * {@code DELETE  /detail-versement-intermediaires/:id} : delete the "id" detailVersementIntermediaire.
     *
     * @param id the id of the detailVersementIntermediaireDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/detail-versement-intermediaires/{id}")
    public ResponseEntity<Void> deleteDetailVersementIntermediaire(@PathVariable Long id) {
        log.debug("REST request to delete DetailVersementIntermediaire : {}", id);
        detailVersementIntermediaireService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
