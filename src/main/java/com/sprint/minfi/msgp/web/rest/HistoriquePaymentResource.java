package com.sprint.minfi.msgp.web.rest;

import com.sprint.minfi.msgp.service.HistoriquePaymentService;
import com.sprint.minfi.msgp.web.rest.errors.BadRequestAlertException;
import com.sprint.minfi.msgp.service.dto.HistoriquePaymentDTO;

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
 * REST controller for managing {@link com.sprint.minfi.msgp.domain.HistoriquePayment}.
 */
@RestController
@RequestMapping("/api")
public class HistoriquePaymentResource {

    private final Logger log = LoggerFactory.getLogger(HistoriquePaymentResource.class);

    private static final String ENTITY_NAME = "spminfimsgpHistoriquePayment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HistoriquePaymentService historiquePaymentService;

    public HistoriquePaymentResource(HistoriquePaymentService historiquePaymentService) {
        this.historiquePaymentService = historiquePaymentService;
    }

    /**
     * {@code POST  /historique-payments} : Create a new historiquePayment.
     *
     * @param historiquePaymentDTO the historiquePaymentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new historiquePaymentDTO, or with status {@code 400 (Bad Request)} if the historiquePayment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/historique-payments")
    public ResponseEntity<HistoriquePaymentDTO> createHistoriquePayment(@RequestBody HistoriquePaymentDTO historiquePaymentDTO) throws URISyntaxException {
        log.debug("REST request to save HistoriquePayment : {}", historiquePaymentDTO);
        if (historiquePaymentDTO.getId() != null) {
            throw new BadRequestAlertException("A new historiquePayment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HistoriquePaymentDTO result = historiquePaymentService.save(historiquePaymentDTO);
        return ResponseEntity.created(new URI("/api/historique-payments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /historique-payments} : Updates an existing historiquePayment.
     *
     * @param historiquePaymentDTO the historiquePaymentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historiquePaymentDTO,
     * or with status {@code 400 (Bad Request)} if the historiquePaymentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the historiquePaymentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/historique-payments")
    public ResponseEntity<HistoriquePaymentDTO> updateHistoriquePayment(@RequestBody HistoriquePaymentDTO historiquePaymentDTO) throws URISyntaxException {
        log.debug("REST request to update HistoriquePayment : {}", historiquePaymentDTO);
        if (historiquePaymentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        HistoriquePaymentDTO result = historiquePaymentService.save(historiquePaymentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, historiquePaymentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /historique-payments} : get all the historiquePayments.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of historiquePayments in body.
     */
    @GetMapping("/historique-payments")
    public ResponseEntity<List<HistoriquePaymentDTO>> getAllHistoriquePayments(Pageable pageable) {
        log.debug("REST request to get a page of HistoriquePayments");
        Page<HistoriquePaymentDTO> page = historiquePaymentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /historique-payments/:id} : get the "id" historiquePayment.
     *
     * @param id the id of the historiquePaymentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the historiquePaymentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/historique-payments/{id}")
    public ResponseEntity<HistoriquePaymentDTO> getHistoriquePayment(@PathVariable Long id) {
        log.debug("REST request to get HistoriquePayment : {}", id);
        Optional<HistoriquePaymentDTO> historiquePaymentDTO = historiquePaymentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(historiquePaymentDTO);
    }

    /**
     * {@code DELETE  /historique-payments/:id} : delete the "id" historiquePayment.
     *
     * @param id the id of the historiquePaymentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
//    @DeleteMapping("/historique-payments/{id}")
//    public ResponseEntity<Void> deleteHistoriquePayment(@PathVariable Long id) {
//        log.debug("REST request to delete HistoriquePayment : {}", id);
//        historiquePaymentService.delete(id);
//        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
//    }
}
