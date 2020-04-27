package com.sprintpay.minfi.msgp.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.sprintpay.minfi.msgp.domain.Payment;
import com.sprintpay.minfi.msgp.domain.enumeration.Statut;
import com.sprintpay.minfi.msgp.service.DetailVersementIntermediaireService;
import com.sprintpay.minfi.msgp.service.HistoriquePaymentService;
import com.sprintpay.minfi.msgp.service.PaymentService;
import com.sprintpay.minfi.msgp.service.PaymentSpecialServices;
import com.sprintpay.minfi.msgp.service.RESTClientEmissionService;
import com.sprintpay.minfi.msgp.service.RESTClientQuittanceService;
import com.sprintpay.minfi.msgp.service.RESTClientTransactionService;
import com.sprintpay.minfi.msgp.service.dto.DetailVersementIntermediaireDTO;
import com.sprintpay.minfi.msgp.service.dto.EmissionDTO;
import com.sprintpay.minfi.msgp.service.dto.EmissionHistoriqueDTO;
import com.sprintpay.minfi.msgp.service.dto.JustificatifPaiementDTO;
import com.sprintpay.minfi.msgp.service.dto.PaymentDTO;
import com.sprintpay.minfi.msgp.service.dto.TransactionDTO;
import com.sprintpay.minfi.msgp.service.mapper.PaymentMapper;
import com.sprintpay.minfi.msgp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.sprintpay.minfi.msgp.domain.Payment}.
 */
@RestController
@RequestMapping("/api")
public class PaymentResource {

    private final Logger log = LoggerFactory.getLogger(PaymentResource.class);

    private static final String ENTITY_NAME = "spminfimsgpPayment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentService paymentService;
    private final HistoriquePaymentService historiquePaymentService;
    private final DetailVersementIntermediaireService detailVersementIntermediaireService;
    private final RESTClientTransactionService restClientTransactionService;
    private final RESTClientEmissionService restClientEmissionService;
    private final RESTClientQuittanceService restClientQuittanceService;
    private final PaymentSpecialServices paymentSpecialServices;
    private final PaymentMapper paymentMapper;

    public PaymentResource(PaymentService paymentService, HistoriquePaymentService historiquePaymentService
    					   , DetailVersementIntermediaireService detailVersementIntermediaireService
    					   , RESTClientTransactionService restClientTransactionService
    					   , RESTClientEmissionService restClientEmissionService
    					   , PaymentSpecialServices paymentSpecialServices
    					   , RESTClientQuittanceService restClientQuittanceService
    					   , PaymentMapper paymentMapper) {
        this.paymentService = paymentService;
        this.historiquePaymentService = historiquePaymentService;
        this.detailVersementIntermediaireService = detailVersementIntermediaireService;
        this.restClientTransactionService = restClientTransactionService;
        this.restClientEmissionService = restClientEmissionService;
        this.paymentSpecialServices = paymentSpecialServices;
        this.restClientQuittanceService = restClientQuittanceService; 
        this.paymentMapper = paymentMapper;
    }

    /**
     * {@code POST  /payments} : save payment.
     *
     * @param paymentDTO the paymentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (saved)} and with body the new paymentDTO, or with status {@code 400 (Bad Request)} if the payment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/payments")
    public ResponseEntity<PaymentDTO> savePayment(@Valid @RequestBody PaymentDTO paymentDTO) throws URISyntaxException {
        log.debug("REST request to save Payment : {}", paymentDTO);
        if (paymentDTO.getId() != null) {
            throw new BadRequestAlertException("A new payment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PaymentDTO result = paymentService.save(paymentDTO);
        return ResponseEntity.created(new URI("/api/payments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }


//    @PostMapping("/payments")
//    public ResponseEntity<PaymentDTO> savePayment(@Valid @RequestBody PaymentDTO paymentDTO) {
//        log.debug("REST request to save Payment : {}", paymentDTO);
//
//        PaymentDTO result = paymentService.save(paymentDTO);
//
//        return new ResponseEntity<PaymentDTO>(result, HttpStatus.CREATED);
//    }


	@PostMapping("/effectuerPaiement/{debitInfo}/{niu}/{refEmi}")
    public ResponseEntity<Map<String, Object>> effectuerPaiement(@Valid @RequestBody PaymentDTO paymentDTO
    												, @PathVariable String debitInfo
    												, @PathVariable String niu
    												, @PathVariable Long refEmi) {

		Map<String, Object> result = new LinkedHashMap<String, Object>();
    	Map<String, String> resultTransaction = new LinkedHashMap<String, String>();
    	Map<String, String> resultEmission = new LinkedHashMap<String, String>();
    	resultEmission = restClientEmissionService.findRefEmission(paymentDTO.getIdEmission());

    	//controle des données du paiement
		if((paymentDTO.getIdTransactionId() != null) || paymentDTO.getIdDetVersId() != null 
				|| debitInfo.isEmpty() 
				|| (paymentDTO.getIdEmission() == null || paymentDTO.getIdEmission() == 0) && (paymentDTO.getIdRecette() == null || paymentDTO.getIdRecette() == 0))  {
			result.put("Reject", "Bad Entry");
			return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
		}

		//si id du paiement est non null, il est probable que le paiement existe
		if (paymentDTO.getId() != null || (Double.parseDouble(resultEmission.get("amount")) - paymentDTO.getAmount()) > 0 || paymentDTO.getAmount() == 0) {
			result.put("Reject", "Paiement Reject");
			return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
		}

		PaymentDTO paymentDTO2 = new PaymentDTO();
		
    	//complete datas payment
    	paymentDTO.setStatut(Statut.DRAFT);
    	// paymentDTO.setCode(paymentSpecialServices.codeNext());
        paymentDTO.setCode(UUID.randomUUID().toString());
        
        //case emission 
        if (refEmi != 0) {
        	
            if (resultEmission.get("refEmi") != null) {//controle existance emission in msged
            	//create emission before save payment
                EmissionDTO emissionDTO = new EmissionDTO();
            	emissionDTO.setStatus(Statut.DRAFT);
            	emissionDTO.setAmount(paymentDTO.getAmount());
            	emissionDTO.setRefEmi(refEmi.toString());
            	emissionDTO.setCodeContribuable(niu);
            	EmissionDTO emissionDTO2 = restClientEmissionService.createEmission(emissionDTO);
            	
            	//complete datas payment with idEmission create, and save payment
            	paymentDTO.setIdEmission(emissionDTO2.getId());
            	paymentDTO2 =  paymentService.save(paymentDTO);
    		}
            else {
            	System.out.println("ici je vais sortir du programme");
            	result.put("Reject", "Emission Not Found");
    			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
            }
        }
        else {//case recette non fiscale, create payment directly with idRecette in PaymentDTO entry
        	paymentDTO2 =  paymentService.save(paymentDTO);
        }

    	//create historique payment
    	historiquePaymentService.saveHistPay(Statut.DRAFT.toString(), LocalDateTime.now(), paymentMapper.toEntity(paymentDTO2));

		//call transaction service to debit account
    	if (paymentSpecialServices.convertProvider(paymentDTO.getMeansOfPayment().toString()).equals("AFRILAND")) {
    		resultTransaction = restClientTransactionService.getTransaction(paymentSpecialServices.convertProvider(paymentDTO.getMeansOfPayment().toString()),
        			paymentSpecialServices.buildRequest(debitInfo, paymentDTO.getAmount(), paymentDTO.getMeansOfPayment().toString(), paymentDTO.getCode()));
		}
    	else {
    		resultTransaction = restClientTransactionService.getTransaction(paymentSpecialServices.convertProvider(paymentDTO.getMeansOfPayment().toString()),
        			paymentSpecialServices.buildRequest(debitInfo, paymentDTO.getAmount(), paymentDTO.getMeansOfPayment().toString(), paymentDTO.getCode()));
    	}
			
	    //build response body to send at front
		result.put("paymentDTO", paymentDTO2);
		result.put("resultTransaction", resultTransaction);
		
		return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @PostMapping("/callbackTransaction/{codePaiement}/{status_code}")
    public ResponseEntity<String> callbackTransaction(@Valid @RequestBody TransactionDTO transactionDTO,
    													@PathVariable String codePaiement,
    													@PathVariable String status_code) {//cette methode sera démarrer par un client feign configuré dans mstransaction

    	
    	String resultat = "Success";
    	Statut status = null;
		Payment payment = new Payment();
//		TransactionDTO transaction = new TransactionDTO();
		
		//we accept status code equal <100> or <400>
		if (!status_code.equals("400") && !status_code.equals("100")) return new ResponseEntity<>(resultat = "status code Reject", HttpStatus.NOT_ACCEPTABLE);

		if (status_code.equals("100")) {//Payment Sucessfull
			status = Statut.VALIDATED;
		}
		else if (status_code.equals("400")) {//Payment Failed
			status = Statut.CANCEL;
		}
		
//    	transaction = transactionService.save(transactionDTO);
    	

    	//find payment by codePaiement and update status
    	payment = paymentService.findByCode(codePaiement);

    	if (payment == null) return new ResponseEntity<>(resultat = "Payment Not Exist", HttpStatus.NOT_ACCEPTABLE);

    	paymentService.update(payment.getId(), status, transactionDTO.getId(), transactionDTO.getCodeTransaction());
    	historiquePaymentService.saveHistPay(status.toString(), transactionDTO.getDate(), payment);
    	
    	//en cas de paiement d une emission on met a jour le statut de l emission
    	if (payment.getIdEmission() != null) {
    		//update emission status
    		restClientEmissionService.updateEmission(payment.getIdEmission(), status);
    		
    		//create historique emission
    		restClientEmissionService.createEmissionHistorique(new EmissionHistoriqueDTO(), status.toString(), payment.getIdEmission());
    	}
    	
    	EmissionDTO emissionDTO = restClientEmissionService.getEmission(payment.getIdEmission());
    	
    	if (status_code.equals("100") && emissionDTO != null) {//ici on génère le reçu en cas de paiement réussi
	    	JustificatifPaiementDTO justificatifPaiementDTO = new JustificatifPaiementDTO();
	    	justificatifPaiementDTO.setReferencePaiement(payment.getCode());
	    	justificatifPaiementDTO.setIdPaiement(payment.getId());
	    	justificatifPaiementDTO.setDateCreation(transactionDTO.getDate()); 
	    	justificatifPaiementDTO.setMontant(payment.getAmount());
	    	justificatifPaiementDTO.setReferencePaiement(payment.getCode());
	    	justificatifPaiementDTO.setNui(emissionDTO.getCodeContribuable());
	    	justificatifPaiementDTO.setNumero(Long.parseLong(transactionDTO.getTelephone()));
	    	
	    	restClientQuittanceService.createJustificatifPaiement(justificatifPaiementDTO);
		}
    	
    	return new ResponseEntity<>(resultat, HttpStatus.OK);

    }


    @PostMapping("/reconcilierPaiement/{codeVersement}/{montant}")
    public ResponseEntity<String> reconcilierPaiement(@RequestBody List<PaymentDTO> paymentDTOList
    												  , @PathVariable String codeVersement
    												  , @PathVariable double montant) {

    	String resultat = "RECONCILED Succes";
    	double reelmontant = 0;
    	
    	//calcul du montant des paiements a reconcilier
    	for (PaymentDTO paymentDTO2 : paymentDTOList) {
			reelmontant += paymentDTO2.getAmount();
		}

    	if (codeVersement == null || montant == 0 || paymentDTOList == null || (reelmontant - montant) > 0) return new ResponseEntity<>(resultat = "Bad Entry", HttpStatus.BAD_REQUEST);
    	
    	//appel du service verifier detail versement
    	DetailVersementIntermediaireDTO det = detailVersementIntermediaireService.findByCode(codeVersement);

    	if (det == null) return new ResponseEntity<>(resultat = "Failed", HttpStatus.NOT_FOUND);

    	if (det.getNumeroVersment().isEmpty()) return new ResponseEntity<>(resultat = "codeVersement Not Exist", HttpStatus.NOT_FOUND);

    	Statut status = null;

    	//appel du service de comparaisons des données des paiements des deux cotés
    	if (!detailVersementIntermediaireService.comparerDonnReconcil(det.getMontant(), montant)) {//si montant different, echec reconciliation
    		status = Statut.CANCEL;
    		resultat = "Failed RECONCILED, Amount not mapping";
        	//creer historique a etat reconcilied
        	
		}
    	
    	else status = Statut.RECONCILED;  //si montant egaux, alors succès reconciliation
    	
    	for (PaymentDTO paymentDTO : paymentDTOList) {
    		paymentService.update(paymentDTO.getId(), status);
        	historiquePaymentService.saveHistPay(status.toString(), LocalDateTime.now(), paymentMapper.toEntity(paymentDTO));
		}
    	
    	if (detailVersementIntermediaireService.comparerDonnReconcil(det.getMontant(), montant)) {
    		//appel du endpoint generer quittance (existe deja, mais à distance) en envoyant l objet payment pour construire la quittance
        	//appel du endpoint notification pour renseigner sur l etat de la reconciliation
        	//appel du endpoint update emission, en testant dabord quil sagit du paiement dune emission
    	}
    	
    	return new ResponseEntity<>(resultat, HttpStatus.OK);
    	
    }


    @GetMapping("/literPaymentByStatut/{statut}")
    public ResponseEntity<List<Object>> literPaymentByStatut(@PathVariable Statut statut, Pageable pageable) {

    	Page<Object> pageresult = paymentService.findByStatut(statut, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), pageresult);
        return new ResponseEntity<>(pageresult.getContent(), headers, HttpStatus.OK);
    }


    @GetMapping("/listerPaymentByCodeTransaction/{codeTransaction}")
    public ResponseEntity<Map<String, String>> listerPaymentByCodeTransaction(@PathVariable String codeTransaction){
    	Map<String, String> resultData = new LinkedHashMap<String, String>();
    	
    	Payment payment = paymentService.findByRefTransaction(codeTransaction);

    	resultData.put("statusPaie", payment.getStatut().toString());
    	resultData.put("codePaie", payment.getCode());
    	resultData.put("datePaie", payment.getLastModifiedBy().toString());
    	return new ResponseEntity<>(resultData, HttpStatus.OK);
    }


    /**
     * {@code PUT  /payments} : Updates an existing payment.
     *
     * @param paymentDTO the paymentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentDTO,
     * or with status {@code 400 (Bad Request)} if the paymentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/payments")
    public ResponseEntity<PaymentDTO> updatePayment(@Valid @RequestBody PaymentDTO paymentDTO) throws URISyntaxException {
        log.debug("REST request to update Payment : {}", paymentDTO);
        if (paymentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PaymentDTO result = paymentService.save(paymentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /payments} : get all the payments.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of payments in body.
     */
    @GetMapping("/payments")
    public ResponseEntity<List<PaymentDTO>> getAllPayments(Pageable pageable) {
        log.debug("REST request to get a page of Payments");
        Page<PaymentDTO> page = paymentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /payments/:id} : get the "id" payment.
     *
     * @param id the id of the paymentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/payments/{id}")
    public ResponseEntity<PaymentDTO> getPayment(@PathVariable Long id) {
        log.debug("REST request to get Payment : {}", id);
        Optional<PaymentDTO> paymentDTO = paymentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentDTO);
    }

    /**
     * {@code DELETE  /payments/:id} : delete the "id" payment.
     *
     * @param id the id of the paymentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/payments/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        log.debug("REST request to delete Payment : {}", id);
        paymentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

}