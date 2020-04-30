package com.sprintpay.minfi.msgp.web.rest;

import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.sprintpay.minfi.msgp.config.ApplicationProperties;
import com.sprintpay.minfi.msgp.domain.Payment;
import com.sprintpay.minfi.msgp.domain.enumeration.Statut;
import com.sprintpay.minfi.msgp.service.PaymentService;
import com.sprintpay.minfi.msgp.service.RESTClientQuittanceService;
import com.sprintpay.minfi.msgp.service.RESTClientSystacSygmaService;
import com.sprintpay.minfi.msgp.service.dto.JustificatifPaiementDTO;
import com.sprintpay.minfi.msgp.service.dto.TransactionSSDTO;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.sprintpay.minfi.msgp.service.DetailVersementIntermediaireService;
import com.sprintpay.minfi.msgp.service.dto.DetailVersementIntermediaireDTO;
import com.sprintpay.minfi.msgp.web.rest.errors.BadRequestAlertException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * REST controller for managing {@link com.sprintpay.minfi.msgp.domain.DetailVersementIntermediaire}.
 */
@RestController
@RequestMapping("/api")
public class DetailVersementIntermediaireResource {

    private final Logger log = LoggerFactory.getLogger(DetailVersementIntermediaireResource.class);

    private static final String ENTITY_NAME = "spminfimsgpDetailVersementIntermediaire";

    private final static int MAX_RETRY_COUNT = 5;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DetailVersementIntermediaireService detailVersementIntermediaireService;

    private final PaymentService paymentService;

    private final RESTClientSystacSygmaService restClientSystacSygmaService;

    private final ApplicationProperties applicationProperties;

    private final RESTClientQuittanceService restClientQuittanceService;

    public DetailVersementIntermediaireResource(DetailVersementIntermediaireService detailVersementIntermediaireService,
                                                PaymentService paymentService,
                                                RESTClientSystacSygmaService restClientSystacSygmaService,
                                                ApplicationProperties applicationProperties,
                                                RESTClientQuittanceService restClientQuittanceService) {
        this.detailVersementIntermediaireService = detailVersementIntermediaireService;
        this.paymentService = paymentService;
        this.restClientSystacSygmaService = restClientSystacSygmaService;
        this.applicationProperties = applicationProperties;
        this.restClientQuittanceService = restClientQuittanceService;
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
            throw new BadRequestAlertException("A new detailVersementIntermediaire cannot already have an ID", ENTITY_NAME, "idExists");
        }
        if (detailVersementIntermediaireDTO.getPaymentRefs() == null || detailVersementIntermediaireDTO.getPaymentRefs().isEmpty()) {
            throw new BadRequestAlertException("A new detailVersementIntermediaire cannot be save without payments references", ENTITY_NAME, "paymentRefsRequired");
        }

        // Check if there is no previous data with same NumeroVersment
        Optional<DetailVersementIntermediaireDTO> oldNumeroVersment = detailVersementIntermediaireService.findByNumeroVersment(detailVersementIntermediaireDTO.getNumeroVersment());
        if (oldNumeroVersment.isPresent()){
            throw new BadRequestAlertException("A detailVersementIntermediaire already exist", ENTITY_NAME, "NumeroVersmentExists");
        }

        // Check if payments are already reconciled
        // TODO

        // Check if all provided payments exists and if they are in VALIDATE statut
        List<Payment> paymentsToReconciled = paymentService.findByRefTransactionInAndStatut(detailVersementIntermediaireDTO.getPaymentRefs(), Statut.VALIDATED);
        if (paymentsToReconciled == null) {
            throw new BadRequestAlertException("None of the payments references provided are found in the system", ENTITY_NAME, "paymentRefsNotFound");
        }
        if (paymentsToReconciled.size() != detailVersementIntermediaireDTO.getPaymentRefs().size()) {
            List<String> different = new ArrayList<String>( detailVersementIntermediaireDTO.getPaymentRefs() );
            different.removeAll( paymentsToReconciled.stream().map(payment -> payment.getRefTransaction()).collect(Collectors.toList()) );
            throw new BadRequestAlertException("Theses payments references are not found or validated in the system", different.toString(), "paymentRefsNotFound");
        }

        System.out.println("******************************** "+detailVersementIntermediaireDTO+" *******************************************");
        // Check if the numeroVersment exist on SYSTAC SYGMA transactions
        int retryCount = 0;
        while(true){
            try {
                ResponseEntity<TransactionSSDTO> transactionSSDTO = restClientSystacSygmaService.searchTransaction(detailVersementIntermediaireDTO.getNumeroVersment(), applicationProperties.getSpMinfiMsssToken());
                if (transactionSSDTO == null || transactionSSDTO.getBody() == null) {
                    throw new BadRequestAlertException("No detailVersementIntermediaire found in system now, try again later", ENTITY_NAME, "NumeroVersmentNotAvailable");
                }
                // Check if the global amount of payments provided matches with the amount of SYSTAC SYGMA transaction
                Double globalPaymentsAmount = paymentsToReconciled.stream().mapToDouble(Payment::getAmount).sum();
                if (transactionSSDTO.getBody().getMontant().compareTo(globalPaymentsAmount) != 0 ){
                    throw new BadRequestAlertException("The global payments amount is different to the SYSTAC/SYGMA transaction", "Global Amount is: "+globalPaymentsAmount+" SYSTAC/SYGMA Amount is: "+transactionSSDTO.getBody().getMontant(), "AmountsNotMatch");
                }
                if (detailVersementIntermediaireDTO.getMontant().compareTo(globalPaymentsAmount) != 0 ){
                    throw new BadRequestAlertException("The global payments amount is different to detailVersementIntermediaire Amount", "Global Amount is: "+globalPaymentsAmount+" detailVersementIntermediaireDTO Amount is: "+detailVersementIntermediaireDTO.getMontant(), "AmountsNotMatch");
                }
            break;
            }catch (HystrixRuntimeException ex){
                if(retryCount > MAX_RETRY_COUNT){
                    throw(ex);
                }
                retryCount++;
                continue;
            }
        }

        // Save detailVersementIntermediaire
        DetailVersementIntermediaireDTO result = detailVersementIntermediaireService.save(detailVersementIntermediaireDTO);

        // Update detailVersementIntermediaire.payments
        paymentService.updateAllPayments(paymentsToReconciled.stream().map(payment -> payment.getRefTransaction()).collect(Collectors.toSet()), Statut.RECONCILED);

        // Try to Generate Quittances
        List<JustificatifPaiementDTO> justificatifPaiementDTOs = prepareJustificatifsPayment(paymentsToReconciled);
        retryCount = 0;
        while(true){
            try {
                restClientQuittanceService.createManyJustificatifPaiement(justificatifPaiementDTOs);
                break;
            }catch (HystrixRuntimeException ex){
                if(retryCount > MAX_RETRY_COUNT){
                    throw(ex);
                }
                retryCount++;
                continue;
            }
        }
        return ResponseEntity.created(new URI("/api/detail-versement-intermediaires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    private List<JustificatifPaiementDTO> prepareJustificatifsPayment(List<Payment> paymentsToReconciled) {
        List<JustificatifPaiementDTO> justificatifPaiementDTOs = new ArrayList<JustificatifPaiementDTO>();
        for (Payment payment: paymentsToReconciled ) {
            JustificatifPaiementDTO justificatifPaiementDTO = new JustificatifPaiementDTO();
            justificatifPaiementDTO.setReferencePaiement(payment.getCode());
            justificatifPaiementDTO.setIdPaiement(payment.getId());
            justificatifPaiementDTO.setDateCreation(LocalDateTime.now());
            justificatifPaiementDTO.setMontant(payment.getAmount());
            justificatifPaiementDTO.setReferencePaiement(payment.getRefTransaction());
            justificatifPaiementDTO.setNui("");
            justificatifPaiementDTO.setNumero(0L);
            justificatifPaiementDTOs.add(justificatifPaiementDTO);
        }
        return justificatifPaiementDTOs;
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
    //@PutMapping("/detail-versement-intermediaires")
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
    //@DeleteMapping("/detail-versement-intermediaires/{id}")
    public ResponseEntity<Void> deleteDetailVersementIntermediaire(@PathVariable Long id) {
        log.debug("REST request to delete DetailVersementIntermediaire : {}", id);
        detailVersementIntermediaireService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
