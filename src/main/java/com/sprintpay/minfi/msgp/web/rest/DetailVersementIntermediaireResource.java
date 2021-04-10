package com.sprintpay.minfi.msgp.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.sprintpay.minfi.msgp.config.ApplicationProperties;
import com.sprintpay.minfi.msgp.domain.DetailVersementIntermediaire;
import com.sprintpay.minfi.msgp.domain.Payment;
import com.sprintpay.minfi.msgp.domain.enumeration.MeansOfPayment;
import com.sprintpay.minfi.msgp.domain.enumeration.Statut;
import com.sprintpay.minfi.msgp.service.DetailVersementIntermediaireService;
import com.sprintpay.minfi.msgp.service.PaymentService;
import com.sprintpay.minfi.msgp.service.RESTClientEmissionService;
import com.sprintpay.minfi.msgp.service.RESTClientNotificationService;
import com.sprintpay.minfi.msgp.service.RESTClientOrganisationService;
import com.sprintpay.minfi.msgp.service.RESTClientQuittanceService;
import com.sprintpay.minfi.msgp.service.RESTClientSystacSygmaService;
import com.sprintpay.minfi.msgp.service.RESTClientTransactionService;
import com.sprintpay.minfi.msgp.service.RESTClientUAAService;
import com.sprintpay.minfi.msgp.service.dto.DetailVersementIntermediaireDTO;
import com.sprintpay.minfi.msgp.service.dto.JustificatifPaiementDTO;
import com.sprintpay.minfi.msgp.service.dto.NotificationDTO;
import com.sprintpay.minfi.msgp.service.dto.TransactionSSDTO;
import com.sprintpay.minfi.msgp.service.dto.TypeNotificationDTO;
import com.sprintpay.minfi.msgp.service.dto.UserDTO;
import com.sprintpay.minfi.msgp.service.mapper.DetailVersementIntermediaireMapper;
import com.sprintpay.minfi.msgp.service.mapper.PaymentMapper;
import com.sprintpay.minfi.msgp.utils.ResponseSumm;
import com.sprintpay.minfi.msgp.web.rest.errors.BadRequestAlertException;

import feign.FeignException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing
 * {@link com.sprintpay.minfi.msgp.domain.DetailVersementIntermediaire}.
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

	private final RESTClientNotificationService restClientNotificationService;

	private final RESTClientUAAService restClientUAAService;
	
	private final DetailVersementIntermediaireMapper detailversementMapper;
	
	private final RESTClientOrganisationService restClientOrganisationService;
	
	private final RESTClientEmissionService restClientEmissionService;
	
	private final RESTClientTransactionService restClientTransactionService;

    private final KafkaTemplate<String, NotificationDTO> kafkaTemplate;

    @Value("${kafka.servers.topic.notification}")
    private String topic ;

	public DetailVersementIntermediaireResource(DetailVersementIntermediaireService detailVersementIntermediaireService,
                                                PaymentService paymentService, RESTClientSystacSygmaService restClientSystacSygmaService,
                                                ApplicationProperties applicationProperties, RESTClientQuittanceService restClientQuittanceService,
                                                RESTClientNotificationService restClientNotificationService, RESTClientUAAService restClientUAAService,
                                                RESTClientEmissionService restClientEmissionService,
                                                RESTClientTransactionService restClientTransactionService,
                                                RESTClientOrganisationService restClientOrganisationService, KafkaTemplate<String, NotificationDTO> kafkaTemplate, DetailVersementIntermediaireMapper detailversementMapper) {
		this.detailVersementIntermediaireService = detailVersementIntermediaireService;
		this.paymentService = paymentService;
		this.restClientSystacSygmaService = restClientSystacSygmaService;
		this.applicationProperties = applicationProperties;
		this.restClientQuittanceService = restClientQuittanceService;
		this.restClientNotificationService = restClientNotificationService;
		this.restClientUAAService = restClientUAAService;
		this.restClientOrganisationService = restClientOrganisationService;
		this.restClientEmissionService = restClientEmissionService;
		this.restClientTransactionService = restClientTransactionService;
		this.detailversementMapper = detailversementMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

	/**
	 * {@code POST  /detail-versement-intermediaires} : Create a new
	 * detailVersementIntermediaire.
	 *
	 * @param detailVersementIntermediaireDTO the detailVersementIntermediaireDTO to
	 *                                        create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new detailVersementIntermediaireDTO, or with status
	 *         {@code 400 (Bad Request)} if the detailVersementIntermediaire has
	 *         already an ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/detail-versement-intermediaires")
	public ResponseEntity<DetailVersementIntermediaireDTO> createDetailVersementIntermediaire(
			@RequestBody DetailVersementIntermediaireDTO detailVersementIntermediaireDTO) throws URISyntaxException {
		log.debug("REST request to save DetailVersementIntermediaire : {}", detailVersementIntermediaireDTO);
		if (detailVersementIntermediaireDTO.getId() != null) {
			throw new BadRequestAlertException("A new detailVersementIntermediaire cannot already have an ID",
					ENTITY_NAME, "idExists");
		}
		if (detailVersementIntermediaireDTO.getPaymentRefs() == null
				|| detailVersementIntermediaireDTO.getPaymentRefs().isEmpty()) {
			throw new BadRequestAlertException(
					"A new detailVersementIntermediaire cannot be save without payments references", ENTITY_NAME,
					"paymentRefsRequired");
		}

		// Check if there is no previous data with same NumeroVersment
		Optional<DetailVersementIntermediaireDTO> oldNumeroVersment = detailVersementIntermediaireService
				.findByNumeroVersment(detailVersementIntermediaireDTO.getNumeroVersment());
		if (oldNumeroVersment.isPresent()) {
			throw new BadRequestAlertException("A detailVersementIntermediaire already exist", ENTITY_NAME,
					"NumeroVersmentExists");
		}

		// Check if payments are already reconciled
		// TODO

		// Check if all provided payments exists and if they are in VALIDATE statut
		List<Payment> paymentsToReconciled = paymentService
				.findByRefTransactionInAndStatut(detailVersementIntermediaireDTO.getPaymentRefs(), Statut.VALIDATED);
		if (paymentsToReconciled == null) {
			throw new BadRequestAlertException("None of the payments references provided are found in the system",
					ENTITY_NAME, "paymentRefsNotFound");
		}
		if (paymentsToReconciled.size() != detailVersementIntermediaireDTO.getPaymentRefs().size()) {
			List<String> different = new ArrayList<String>(detailVersementIntermediaireDTO.getPaymentRefs());
			different.removeAll(paymentsToReconciled.stream().map(payment -> payment.getRefTransaction())
					.collect(Collectors.toList()));
			throw new BadRequestAlertException("Theses payments references are not found or validated in the system",
					different.toString(), "paymentRefsNotFound");
		}

		// Check if the numeroVersment exist on SYSTAC SYGMA transactions
		int retryCount = 0;
		while (retryCount < MAX_RETRY_COUNT) {
			try {
				ResponseEntity<TransactionSSDTO> transactionSSDTO = restClientSystacSygmaService.searchTransaction(
						detailVersementIntermediaireDTO.getNumeroVersment(),
						applicationProperties.getSpMinfiMsssToken());
				if (transactionSSDTO == null || transactionSSDTO.getBody() == null) {
					throw new BadRequestAlertException(
							"No detailVersementIntermediaire found in system now, try again later", ENTITY_NAME,
							"NumeroVersmentNotAvailable");
				}
				
				// Check if the global amount of payments provided matches with the amount of
				// SYSTAC SYGMA transaction
				Double globalPaymentsAmount = paymentsToReconciled.stream().mapToDouble(Payment::getAmount).sum();
				if (transactionSSDTO.getBody().getMontant().compareTo(globalPaymentsAmount) != 0 ) {
					throw new BadRequestAlertException(
							"The global payments amount is different to the SYSTAC/SYGMA transaction",
							"Global Amount is: " + globalPaymentsAmount + " SYSTAC/SYGMA Amount is: "
									+ transactionSSDTO.getBody().getMontant(),
							"AmountsNotMatch");
				}
				
				// Check if the global amount of payments from frontend provided matches with the amount of
				// SYSTAC SYGMA transaction
				if(transactionSSDTO.getBody().getMontant().compareTo(detailVersementIntermediaireDTO.getMontant()) != 0 ) {
					throw new BadRequestAlertException(
							"The global payments amount is different to the SYSTAC/SYGMA transaction",
							"Amount Received is: " + detailVersementIntermediaireDTO.getMontant() + " SYSTAC/SYGMA Amount is: "
									+ transactionSSDTO.getBody().getMontant(),
							"AmountsNotMatch");
				}
				/*
				 * if
				 * (detailVersementIntermediaireDTO.getMontant().compareTo(globalPaymentsAmount)
				 * != 0 ){ throw new
				 * BadRequestAlertException("The global payments amount is different to detailVersementIntermediaire Amount"
				 * , "Global Amount is: "
				 * +globalPaymentsAmount+" detailVersementIntermediaireDTO Amount is: "
				 * +detailVersementIntermediaireDTO.getMontant(), "AmountsNotMatch"); }
				 */
				retryCount = MAX_RETRY_COUNT;
				break;
			} catch (HystrixRuntimeException ex) {
				if (retryCount > MAX_RETRY_COUNT) {
					throw (ex);
				}
				retryCount++;
				continue;
			}
		}
		
		//check refpayment from detailversementDTO in transaction and insert them into a list
		List<String> paymentRefs = new ArrayList<String>();
		paymentRefs.addAll(detailVersementIntermediaireDTO.getPaymentRefs());
		List<String> references = new ArrayList<String>();
		for (String ref : paymentRefs) {
			Map<String, String> transactionId =restClientTransactionService.getTransactionRefOrOrderId(ref);
			if(transactionId.get("transactionId") != null || !transactionId.get("transactionId").equals(""))
				references.add(transactionId.get("transactionId"));
		}
		
		//After all the check on the Mss transaction table, we shall notify CAMCIS on the state of the transactions reconciled
		List<String> refError = restClientEmissionService.notifyReconciledEmission(references);
		if(!refError.isEmpty()) {
			throw new BadRequestAlertException(
					"Camcis ventilation Error, try again later", ENTITY_NAME,
					"Error Camcis Ventilation on payments: "+ refError.toString());
		}
		
		// Save detailVersementIntermediaire
		DetailVersementIntermediaireDTO result = detailVersementIntermediaireDTO;
		
		// Update detailVersementIntermediaire.payments
		paymentService.updateAllPayments(
				paymentsToReconciled.stream().map(payment -> payment.getRefTransaction()).collect(Collectors.toSet()),
				Statut.RECONCILED, detailversementMapper.toEntity(detailVersementIntermediaireDTO));
			

		// TODO: update Emissions and RNF

		// Try to Generate Quittances
		retryCount = 0;
		while (retryCount < MAX_RETRY_COUNT) {
			try {
				restClientQuittanceService.genererListeQuittances(
						paymentsToReconciled.stream().map(payment -> payment.getId()).collect(Collectors.toSet()));
				break;
			} catch (HystrixRuntimeException ex) {
				if (retryCount > MAX_RETRY_COUNT) {
					throw (ex);
				}
				retryCount++;
				continue;
			}
		}
		// generate notification
		TypeNotificationDTO typeNotificationPayment = null;
		try {
			typeNotificationPayment = restClientNotificationService.getTypeNotification("quittance");
			log.info("======== CHECK 1============");
		} catch (FeignException e) {
			log.error(e.getMessage());
			e.printStackTrace();
			log.info("======== CHECK 2============");
		} finally {
			if (typeNotificationPayment == null) {
				typeNotificationPayment = new TypeNotificationDTO(null, "quittance", "Nouvelle quittance disponible",
						"Notification des quittances disponible", null, "PUSH", null);
				typeNotificationPayment = restClientNotificationService.createTypeNotification(typeNotificationPayment);
				log.info("======== CHECK 3============");
			}

			for (Payment payment2 : paymentsToReconciled) {
				Optional<UserDTO> userDTO = restClientUAAService.searchUser(payment2.getCreatedBy());
				userDTO.orElse(new UserDTO());
				NotificationDTO notificationPayment = new NotificationDTO(null,
						"La quittance est disponible pour le payment N° [" + payment2.getId() + "] d'un montant de "
								+ payment2.getAmount() + " effectué via " + payment2.getMeansOfPayment().name(),
						userDTO.get().getId(), applicationName, "NONTRANSMIS", typeNotificationPayment.getId(), null);
				//restClientNotificationService.createNotification(notificationPayment);
                kafkaTemplate.send(topic,applicationName+ LocalDateTime.now(),notificationPayment);
                log.info("Notification créé et transmit au broker {}", notificationPayment);
				log.info("======== CHECK 4============");
			}
		}

		return ResponseEntity.ok().body(result);
	}

	private List<JustificatifPaiementDTO> prepareJustificatifsPayment(List<Payment> paymentsToReconciled) {
		List<JustificatifPaiementDTO> justificatifPaiementDTOs = new ArrayList<JustificatifPaiementDTO>();
		for (Payment payment : paymentsToReconciled) {
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
	 * {@code PUT  /detail-versement-intermediaires} : Updates an existing
	 * detailVersementIntermediaire.
	 *
	 * @param detailVersementIntermediaireDTO the detailVersementIntermediaireDTO to
	 *                                        update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated detailVersementIntermediaireDTO, or with status
	 *         {@code 400 (Bad Request)} if the detailVersementIntermediaireDTO is
	 *         not valid, or with status {@code 500 (Internal Server Error)} if the
	 *         detailVersementIntermediaireDTO couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	// @PutMapping("/detail-versement-intermediaires")
	public ResponseEntity<DetailVersementIntermediaireDTO> updateDetailVersementIntermediaire(
			@RequestBody DetailVersementIntermediaireDTO detailVersementIntermediaireDTO) throws URISyntaxException {
		log.debug("REST request to update DetailVersementIntermediaire : {}", detailVersementIntermediaireDTO);
		if (detailVersementIntermediaireDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		DetailVersementIntermediaireDTO result = detailVersementIntermediaireService
				.save(detailVersementIntermediaireDTO);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
				detailVersementIntermediaireDTO.getId().toString())).body(result);
	}

	/**
	 * {@code GET  /detail-versement-intermediaires} : get all the
	 * detailVersementIntermediaires.
	 *
	 * @param pageable the pagination information.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of detailVersementIntermediaires in body.
	 */
	@GetMapping("/detail-versement-intermediaires")
	public ResponseEntity<List<DetailVersementIntermediaireDTO>> getAllDetailVersementIntermediaires(
			Pageable pageable) {
		log.debug("REST request to get a page of DetailVersementIntermediaires");
		Page<DetailVersementIntermediaireDTO> page = detailVersementIntermediaireService.findAll(pageable);
		HttpHeaders headers = PaginationUtil
				.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
		return ResponseEntity.ok().headers(headers).body(page.getContent());
	}

	/**
	 * {@code GET  /detail-versement-intermediaires/:id} : get the "id"
	 * detailVersementIntermediaire.
	 *
	 * @param id the id of the detailVersementIntermediaireDTO to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the detailVersementIntermediaireDTO, or with status
	 *         {@code 404 (Not Found)}.
	 */
	@GetMapping("/detail-versement-intermediaires/{id}")
	public ResponseEntity<DetailVersementIntermediaireDTO> getDetailVersementIntermediaire(@PathVariable Long id) {
		log.debug("REST request to get DetailVersementIntermediaire : {}", id);
		Optional<DetailVersementIntermediaireDTO> detailVersementIntermediaireDTO = detailVersementIntermediaireService
				.findOne(id);
		return ResponseUtil.wrapOrNotFound(detailVersementIntermediaireDTO);
	}

	/**
	 * {@code DELETE  /detail-versement-intermediaires/:id} : delete the "id"
	 * detailVersementIntermediaire.
	 *
	 * @param id the id of the detailVersementIntermediaireDTO to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	// @DeleteMapping("/detail-versement-intermediaires/{id}")
	public ResponseEntity<Void> deleteDetailVersementIntermediaire(@PathVariable Long id) {
		log.debug("REST request to delete DetailVersementIntermediaire : {}", id);
		detailVersementIntermediaireService.delete(id);
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
				.build();
	}
	
	@GetMapping("/findDetailVersementIntermediaire/{meanOfPayment}")
	public ResponseEntity<List<DetailVersementIntermediaire>> findDetailVersementIntermediaire(@PathVariable MeansOfPayment meanOfPayment){
		//implement controls here
		
		List<DetailVersementIntermediaire> versements = detailVersementIntermediaireService.findDetailVersementIntermediaire(meanOfPayment);
		HttpHeaders headers = new HttpHeaders();
		headers.set("Status", HttpStatus.OK.name());
		return ResponseEntity.ok().headers(headers).body(versements);
	}
	
	
	/**
	 * This method return the list of payments reconciled by meanofpayments
	 * by organization with its children organizations.
	 *
	 * @param none.
	 */
	@GetMapping("/findDetailVersementIntermediaireByOrganisation/{meanOfPayment}/{idOrg}")
	public ResponseEntity<List<DetailVersementIntermediaire>> findDetailVersementIntermediaireByOrganisation(@PathVariable MeansOfPayment meanOfPayment, @PathVariable Long idOrg){
		//implement controls here
		
		//first select all the child organization of the current org
		List<Map<String, Object>> listids = restClientOrganisationService.getOrganisationByParent(idOrg);
		
		List<Long> childids = new ArrayList<Long>(); 
		
		List<DetailVersementIntermediaire> listversements = new ArrayList<>();
		
		if (listids != null) {
			listids.stream().forEach(org -> {
				childids.add(Long.parseLong(org.get("id").toString()));
			});
		}
		// add current id parent
		childids.add(idOrg);
		
		// iteration by organization
		for (Long idorg : childids) {
			
			List<DetailVersementIntermediaire> versements = detailVersementIntermediaireService.findDetailVersementIntermediaireByOrganisation(meanOfPayment, idorg);
			//add list to list
			versements.stream().forEach(det -> {listversements.add(det);});
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Status", HttpStatus.OK.name());
		return ResponseEntity.ok().headers(headers).body(listversements);
	}
}
