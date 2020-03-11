package com.sprint.minfi.msgp.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.json.JSONException;
import org.json.JSONObject;
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

import com.sprint.minfi.msgp.service.DetailVersementIntermediaireService;
import com.sprint.minfi.msgp.service.HistoriquePaymentService;
import com.sprint.minfi.msgp.service.PaymentService;
import com.sprint.minfi.msgp.service.RESTClientEmissionService;
import com.sprint.minfi.msgp.service.RESTClientTransactionService;
import com.sprint.minfi.msgp.service.TransactionService;
import com.sprint.minfi.msgp.service.dto.DetailVersementIntermediaireDTO;
import com.sprint.minfi.msgp.service.dto.PaymentDTO;
import com.sprint.minfi.msgp.service.dto.TransactionDTO;
import com.sprint.minfi.msgp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.sprint.minfi.msgp.domain.Payment}.
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
    private final TransactionService transactionService;
    private final DetailVersementIntermediaireService detailVersementIntermediaireService;
    private final RESTClientTransactionService restClientTransactionService;
    private final RESTClientEmissionService restClientEmissionService; 

    public PaymentResource(PaymentService paymentService, HistoriquePaymentService historiquePaymentService
    					   , TransactionService transactionService
    					   , DetailVersementIntermediaireService detailVersementIntermediaireService
    					   , RESTClientTransactionService restClientTransactionService
    					   , RESTClientEmissionService restClientEmissionService) {
        this.paymentService = paymentService;
        this.historiquePaymentService = historiquePaymentService;
        this.transactionService = transactionService;
        this.detailVersementIntermediaireService = detailVersementIntermediaireService;
        this.restClientTransactionService = restClientTransactionService;
        this.restClientEmissionService = restClientEmissionService;
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
    
    @PostMapping("/effectuerPaiement/{debitInfo}")
    public ResponseEntity<String> effectuerPaiement(@Valid @RequestBody PaymentDTO paymentDTO
    												, @PathVariable String debitInfo) throws URISyntaxException, JSONException {
    	//strategie de generation du code et mise a jour du code dans PayementDTO
    	
    	String resultat = "";
    	if (paymentDTO.getId() != null) {
			return new ResponseEntity<>(resultat = "Payment Exist", HttpStatus.CONFLICT);
		}
    	
    	//gestion historiquePaymentDTO, valider, historiser le paiement
    	historiquePaymentService.saveHistPay("DRAFT", LocalDateTime.now());
    	
    	//appel du service -> demande transaction
    	Map<String, String> requestTransaction = new HashMap<String, String>();
    	requestTransaction.put("clientId", "");
    	requestTransaction.put("clientToken", "");
    	requestTransaction.put("phone", debitInfo);
    	requestTransaction.put("orderId", "");
    	requestTransaction.put("uniqueId", "");
    	requestTransaction.put("amount", paymentDTO.getAmount().toString());
    	requestTransaction.put("email", "");
    	requestTransaction.put("firstname","");
    	requestTransaction.put("lastname","");
    	requestTransaction.put("currency", "");
    	requestTransaction.put("description", "");
    	requestTransaction.put("companyName", "");
    	requestTransaction.put("successUrl", "");
    	requestTransaction.put("failureUrl", "");
    	requestTransaction.put("returnUrl", "");
    	requestTransaction.put("transactionid", "");
    	requestTransaction.put("ref", "");
    	requestTransaction.put("notificationUrl", "");
    	requestTransaction.put("ipAddress", "");
    	System.out.println("***************" + debitInfo + paymentDTO.getAmount());
    	restClientTransactionService.getTransaction("mtncmr", requestTransaction);
//    	System.out.print("--------------------");
//    	System.out.println(restClientTransactionService.getAlltransaction("mtncmr"));

    	return new ResponseEntity<>(resultat = "Payment in Progress...", HttpStatus.OK);
    }
    
    @GetMapping("/callbackTransaction")
    public ResponseEntity<String> callbackTransaction(@Valid @RequestBody TransactionDTO transactionDTO) {//cette methode sera executé automatiquement lorsque le flux qui contient sa donnée d entré est chargé
    	
    	//controle des données du flux en entrée, quelles sont elles ?
    	//ici je vais d abord tester que la transaction a réussi, voici un exemple
    	String resultat = "";
    	if (transactionDTO.getMsg().isEmpty()) {
    		return new ResponseEntity<>(resultat = "Failed", HttpStatus.EXPECTATION_FAILED);
		}
    	
    	//gestion transactionDTO, appel du service -> save transaction (il faut bien spécifier l objet Transaction)
    	transactionService.save(transactionDTO);
    	
    	//appel du service mise a jour du statut du payment, historiser le paiement et l emission
    	//doit elles etre exécuté simultanément ou sequentiellement ????
    	PaymentDTO paymentDTO = paymentService.findByIdTransation(transactionDTO.getId());
    	paymentService.update(paymentDTO.getId(), "PAYE");
    	historiquePaymentService.saveHistPay("PAYE", transactionDTO.getDate());
    	restClientEmissionService.historiserEmission(paymentDTO.getIdEmission());
    	
    	//appel du service generer recu de payment (micro service quittance pas encore pret)
    	//en attente...
    	
    	return new ResponseEntity<>(resultat = "Success", HttpStatus.OK);
    }
    
    
    @PostMapping("/reconcilierPaiement/{codeVersement}/{montant}")
    public ResponseEntity<String> reconcilierPaiement(@Valid @RequestBody PaymentDTO paymentDTO
    												  , @PathVariable String codeVersement
    												  , @PathVariable double montant) throws URISyntaxException {
    	//validation des champs des objets
    	
    	//au cas où le paiement est dejà reconcilié
    	
    	String resultat = "";
    	if (paymentDTO.getStatut().toString() == "RECONCILED") {
			return new ResponseEntity<>(resultat = "Paiement Already RECONCILED", HttpStatus.CONFLICT);
		}
    	
    	//appel du service verifier detail versement 
    	DetailVersementIntermediaireDTO det = detailVersementIntermediaireService.findByCode(codeVersement);
    	
    	if (det.getNumeroVersment().isEmpty()) {
			return new ResponseEntity<>(resultat = "codeVersement Not Exist", HttpStatus.NOT_FOUND);
		}
    	
    	//mettre a jour le statut du paiement en cours de reconciliation
    	paymentService.update(paymentDTO.getId(), "RECONCILED");
    	
    	//historiser le paiement
    	historiquePaymentService.saveHistPay("RECONCILED", LocalDateTime.now());
    	
    	//appel du service de comparaisons des données des paiements des deux cotés
    	if (!detailVersementIntermediaireService.comparerDonnReconcil(det.getMontant(), montant)) {//si different
    		return new ResponseEntity<>(resultat = "Failed RECONCILED, Amount not mapping", HttpStatus.EXPECTATION_FAILED);
		}
    	
    	//appel du service generer quittance (existe deja, mais à distance) en envoyant l objet payment pour construire la quittance
    	
    	return new ResponseEntity<>(resultat = "RECONCILED Succes", HttpStatus.OK);
    }
    
    @GetMapping("/listertransaction")
    public ResponseEntity<Page<TransactionDTO>> listerTransaction(Pageable pageable) {
    	/**
    	 * ici on fera appel à RESTClientTransactionService.getAllTransaction pour recuperer les transaction probablement selon un critere
    	 * qu'on definira
    	 */
 
    	return new ResponseEntity<>(transactionService.findAll(pageable), HttpStatus.OK);
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
