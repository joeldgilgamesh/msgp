package com.sprintpay.minfi.msgp.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.validation.Valid;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.internal.json.Json;
import com.sprintpay.minfi.msgp.config.ApplicationProperties;
import com.sprintpay.minfi.msgp.domain.Payment;
import com.sprintpay.minfi.msgp.domain.enumeration.MeansOfPayment;
import com.sprintpay.minfi.msgp.domain.enumeration.Nature;
import com.sprintpay.minfi.msgp.domain.enumeration.Statut;
import com.sprintpay.minfi.msgp.security.SecurityUtils;
import com.sprintpay.minfi.msgp.service.DetailVersementIntermediaireService;
import com.sprintpay.minfi.msgp.service.HistoriquePaymentService;
import com.sprintpay.minfi.msgp.service.PaymentService;
import com.sprintpay.minfi.msgp.service.PaymentSpecialServices;
import com.sprintpay.minfi.msgp.service.RESTClientEmissionService;
import com.sprintpay.minfi.msgp.service.RESTClientNotificationService;
import com.sprintpay.minfi.msgp.service.RESTClientOrganisationService;
import com.sprintpay.minfi.msgp.service.RESTClientQuittanceService;
import com.sprintpay.minfi.msgp.service.RESTClientRNFService;
import com.sprintpay.minfi.msgp.service.RESTClientReportService;
import com.sprintpay.minfi.msgp.service.RESTClientTransactionService;
import com.sprintpay.minfi.msgp.service.RESTClientUAAService;
import com.sprintpay.minfi.msgp.service.dto.AddedParamsPaymentDTO;
import com.sprintpay.minfi.msgp.service.dto.EmissionDTO;
import com.sprintpay.minfi.msgp.service.dto.EmissionHistoriqueDTO;
import com.sprintpay.minfi.msgp.service.dto.ImputationDTO;
import com.sprintpay.minfi.msgp.service.dto.JustificatifPaiementDTO;
import com.sprintpay.minfi.msgp.service.dto.NotificationDTO;
import com.sprintpay.minfi.msgp.service.dto.PaymentDTO;
import com.sprintpay.minfi.msgp.service.dto.TransactionDTO;
import com.sprintpay.minfi.msgp.service.dto.TypeNotificationDTO;
import com.sprintpay.minfi.msgp.service.dto.UserDTO;
import com.sprintpay.minfi.msgp.service.mapper.PaymentMapper;
import com.sprintpay.minfi.msgp.utils.ResponseSumm;
import com.sprintpay.minfi.msgp.utils.RetPaiFiscalis;
import com.sprintpay.minfi.msgp.web.rest.errors.BadRequestAlertException;

import feign.FeignException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.sprintpay.minfi.msgp.domain.Payment}.
 */
@RestController
@RequestMapping("/api")
public class PaymentResource {

	private static final Long DEFAULT_ORGANISATION_DGI_ID = 3L;
	private static final Long DEFAULT_ORGANISATION_DGD_ID = 4L;
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
	private final RESTClientUAAService restClientUAAService;
	private final RESTClientRNFService restClientRNFService;
	private final RESTClientOrganisationService restClientOrganisationService;
	private final RESTClientNotificationService restClientNotificationService;
	private final RESTClientReportService restClientReportService;
	private final ApplicationProperties app;

    private final KafkaTemplate<String, NotificationDTO> kafkaTemplate;

    @Value("${kafka.servers.topic.notification}")
    private String topic ;

	public PaymentResource(PaymentService paymentService, HistoriquePaymentService historiquePaymentService,
                           DetailVersementIntermediaireService detailVersementIntermediaireService,
                           RESTClientTransactionService restClientTransactionService,
                           RESTClientEmissionService restClientEmissionService, PaymentSpecialServices paymentSpecialServices,
                           RESTClientQuittanceService restClientQuittanceService, PaymentMapper paymentMapper,
                           RESTClientUAAService restClientUAAService, RESTClientRNFService restClientRNFService,
                           RESTClientOrganisationService restClientOrganisationService,
                           RESTClientNotificationService restClientNotificationService,
                           RESTClientReportService restClientReportService,
                           ApplicationProperties app, KafkaTemplate<String, NotificationDTO> kafkaTemplate) {
		this.paymentService = paymentService;
		this.historiquePaymentService = historiquePaymentService;
		this.detailVersementIntermediaireService = detailVersementIntermediaireService;
		this.restClientTransactionService = restClientTransactionService;
		this.restClientEmissionService = restClientEmissionService;
		this.paymentSpecialServices = paymentSpecialServices;
		this.restClientQuittanceService = restClientQuittanceService;
		this.paymentMapper = paymentMapper;
		this.restClientUAAService = restClientUAAService;
		this.restClientRNFService = restClientRNFService;
		this.restClientOrganisationService = restClientOrganisationService;
		this.restClientNotificationService = restClientNotificationService;
		this.restClientReportService = restClientReportService;
		this.app = app;
        this.kafkaTemplate = kafkaTemplate;
    }
	
	
	
	@PostMapping("/repartitionByOrganizationByPeriod")
	public ResponseEntity<Map<String, Object>> repartitionByOrganisation(@RequestBody Map<String, String> object) {
		
		Map<String, Object> list = restClientReportService.repartitionByOrganisation(object);
		HttpHeaders headers = new HttpHeaders();
		headers.set("Status", HttpStatus.OK.name());
		return ResponseEntity.ok().headers(headers).body(list);
		
	}
	
	

	/**
	 * {@code POST  /payments} : save payment.
	 *
	 * @param paymentDTO the paymentDTO to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (saved)} and with
	 *         body the new paymentDTO, or with status {@code 400 (Bad Request)} if
	 *         the payment has already an ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/payments")
	public ResponseEntity<PaymentDTO> savePayment(@Valid @RequestBody PaymentDTO paymentDTO) throws URISyntaxException {
		log.debug("REST request to save Payment : {}", paymentDTO);
		if (paymentDTO.getId() != null) {
			throw new BadRequestAlertException("A new payment cannot already have an ID", ENTITY_NAME, "idexists");
		}
		PaymentDTO result = paymentService.save(paymentDTO);
		return ResponseEntity
				.created(new URI("/api/payments/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
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

    @PreAuthorize("hasRole('AUTH_PAIEMENT_EMISSION') or hasRole('AUTH_PAIEMENT_RECETTE')")
	@PostMapping("/effectuerPaiement/{debitInfo}/{niu}/{refEmi}")
	public ResponseEntity<Map<String, Object>> effectuerPaiement(@RequestBody Map<String, Object> body
	// , PaymentDTO paymentDTO
			, @PathVariable String debitInfo, @PathVariable String niu, @PathVariable String refEmi
	// , AddedParamsPaymentDTO addedParamsPaymentDTO
	) {

		Map<String, Object> result = new LinkedHashMap<String, Object>();
		Map<String, String> resultTransaction = new LinkedHashMap<String, String>();
		Map<String, String> resultEmission = new LinkedHashMap<String, String>();
		Object resultRecette = null;
		Map<String, String> requestBuild = new LinkedHashMap<String, String>();
		Long refEmissionOuRecette = 0L;

		// controle body enter
		if (body == null) {
			result.put("Reject", "Enter Datas is Null");
			return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
		}

		JSONObject bodyJson = new JSONObject(body);
		JSONObject paymentDTOJson = new JSONObject(bodyJson.get("paymentDTO").toString());
		JSONObject addedParamsPaymentDTOJson = new JSONObject(bodyJson.get("addedParamsPaymentDTO").toString());

		PaymentDTO paymentDTO = null;
		AddedParamsPaymentDTO addedParamsPaymentDTO = null;

		try {
			paymentDTO = new ObjectMapper().readValue(paymentDTOJson.toString(), PaymentDTO.class);
			addedParamsPaymentDTO = new ObjectMapper().readValue(addedParamsPaymentDTOJson.toString(),
					AddedParamsPaymentDTO.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			result.put("Reject", "Bad Datas Entry Of Payment");
			return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
		}

		if (paymentDTO == null) {
			result.put("Reject", "Bad Datas Entry Of Payment");
			return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
		}

		String provider = paymentSpecialServices.convertProvider(paymentDTO.getMeansOfPayment().toString());

		// controle du niu en cas des emissions
		if (!refEmi.equals("null")) {

			Object niuVerif = restClientUAAService.getNiuContribuablesEnregistres(niu);

			if (niuVerif == null) {
				result.put("Reject", "Usurpateur Voulant effectuer le paiement");
				return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
			}

			// controle du depassement du montant a payer
			if ((provider.matches("MOBILE_MONEY|MOBILE_MONEY2|ORANGE_MONEY|ORANGE_MONEY2|EXPRESS_UNION|EXPRESS_UNION2|ECOBANK|ECOBANK2"))
					&& (paymentDTO.getAmount() > 500000 || paymentDTO.getAmount() <= 0)) {
				result.put("Reject", "Depassement de montant, le montant doit etre compris entre 0 et 500mill");
				return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
			}
		}

		// controle du numero de telephone, selon le moyen de paiement
		if ((provider.matches("MOBILE_MONEY|MOBILE_MONEY2|ORANGE_MONEY|ORANGE_MONEY2|EXPRESS_UNION|EXPRESS_UNION2|ECOBANK|ECOBANK2"))
				&& (debitInfo.isEmpty() || debitInfo == null)) {
			result.put("Reject", "Phone Number is Required");
			return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
		}

		// controle des données du paiement
		if (paymentDTO.getId() != null || (paymentDTO.getIdTransaction() != null) || paymentDTO.getIdDetVersId() != null
				|| ((paymentDTO.getIdEmission() == null || paymentDTO.getIdEmission() <= 0)
						&& (paymentDTO.getIdRecette() == null || paymentDTO.getIdRecette() <= 0))) {
			result.put("Reject", "Bad Entry");
			return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
		}

		// complete datas paymentDTO
		paymentDTO.setStatut(Statut.DRAFT);
		paymentDTO.setCode(UUID.randomUUID().toString());
		
		PaymentDTO paymentDTO2;

		// case emission
		if (!refEmi.equals("null")) {

			resultEmission = restClientEmissionService.findRefEmission(paymentDTO.getIdEmission());
			refEmissionOuRecette = paymentDTO.getIdEmission();

			if (resultEmission == null) {// si l emission a payer n existe pas dans la liste des emission
				result.put("Reject", "Emission Not Exist");
				return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
			}

			if (resultEmission.get("refEmi") == null || resultEmission.get("refEmi").equals("")) {
				result.put("Reject", "Emission Not Have Reference");
				return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
			}

			if ((Double.parseDouble(resultEmission.get("amount")) - paymentDTO.getAmount()) > 0) {// si les montant ne
																									// matche pas
				result.put("Reject", "Paiement Reject");
				return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
			}
			
			// Before sending and making the payment on the transaction ms we shall first get the Emission from CAMCIS
			// case of CAMCIS only
			if (!resultEmission.get("type").equalsIgnoreCase(Nature.AVIS.name())
					&& !resultEmission.get("type").equalsIgnoreCase(Nature.AMR.name())
					&& !resultEmission.get("type").equalsIgnoreCase(Nature.IMPOTS.name())) {
				 if(!restClientEmissionService.checkEmission(niu, refEmi)) {
					 result.put("paymentCode", null);
					 result.put("paymentStatus", "CANCELED");
					 result.put("paymentMessageStatus", "Payment can't be done - refEmission Not Found");
					 return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
				 }
				 
	    	}

			// initialize datas of emission ot create before save payment
			EmissionDTO emissionDTO = new EmissionDTO();
			emissionDTO.setStatus(Statut.DRAFT);
			emissionDTO.setAmount(paymentDTO.getAmount());
			emissionDTO.setRefEmi(refEmi.toString());
			emissionDTO.setCodeContribuable(niu);
			emissionDTO.setNature(Nature.valueOf(resultEmission.get("type")));

			Map<String, Object> organisationDetails = restClientOrganisationService
					.findOrganisationByLibelleCourt(resultEmission.get("codeOrg"));
			if (!organisationDetails.isEmpty()) {
				log.info(".................. " + resultEmission.toString());
				log.info(".................. " + organisationDetails.get("idOrganisation"));
				emissionDTO.setIdOrganisation(Long.parseLong(organisationDetails.get("idOrganisation").toString()));
			} else {
				if (resultEmission.get("type").equalsIgnoreCase(Nature.AVIS.name())
						|| resultEmission.get("type").equalsIgnoreCase(Nature.AMR.name())
						|| resultEmission.get("type").equalsIgnoreCase(Nature.IMPOTS.name())) {
					emissionDTO.setIdOrganisation(DEFAULT_ORGANISATION_DGI_ID);
				} else {
					emissionDTO.setIdOrganisation(DEFAULT_ORGANISATION_DGD_ID);
				}
			}

			//create emission with datas to complete
			EmissionDTO emissionDTO2 = restClientEmissionService.createEmission(emissionDTO);

			// complete datas payment with idEmission create, and save payment
			paymentDTO.setIdEmission(emissionDTO2.getId());
			paymentDTO.setIdOrganisation(emissionDTO.getIdOrganisation());
			paymentDTO2 = paymentService.save(paymentDTO);
		} else {// case recette non fiscale, create payment directly with idRecette in
				// PaymentDTO entry

			if (paymentDTO.getAmount() <= 0) {
				result.put("paymentCode", null);
				result.put("paymentStatus", "CANCELED");
				result.put("paymentMessageStatus", "payment failed -->> le montant de la recette doit etre au moins positif");
				return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
			}

			resultRecette = this.restClientRNFService.getRecettesService(paymentDTO.getIdRecette());
			refEmissionOuRecette = paymentDTO.getIdRecette();
			if (resultRecette != null) {
				paymentDTO2 = paymentService.save(paymentDTO);
			} else {
				result.put("Reject", "Recette Not Found");
				return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
			}
		}

		// create historique payment
		historiquePaymentService.saveHistPay(Statut.DRAFT.toString(), LocalDateTime.now(),
				paymentMapper.toEntity(paymentDTO2));

		switch (provider) {

			case "uba":
			case "ecobankcmr2": {
	
				if (addedParamsPaymentDTO != null) {
					// construct request build
					requestBuild = paymentSpecialServices.buildRequestUBA(debitInfo, paymentDTO.getCode(),
							String.valueOf((int) Math.round(paymentDTO.getAmount())), addedParamsPaymentDTO.getEmail(), addedParamsPaymentDTO.getFirstname(),
							addedParamsPaymentDTO.getLastname(), provider, niu);
				} else {
					result.put("Reject", "Bad Datas Entry Of AddedParamsPayment");
					return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
				}
			}
				break;
	
			case "orangecmr":
			case "orangecmr2":
			case "mtncmr":
			case "mtncmr2":
			case "yup":
			case "eucmr":
			case "eucmr2":
			case "visionfinancecmr":
				requestBuild = paymentSpecialServices.buildRequest(debitInfo, String.valueOf(paymentDTO.getAmount()),
						paymentDTO.getMeansOfPayment().toString(), paymentDTO.getCode());
				break;
	
			case "ecobankcmr":
	
				requestBuild = paymentSpecialServices.buildRequest(debitInfo, String.valueOf((int) Math.round(paymentDTO.getAmount())),
						paymentDTO.getMeansOfPayment().toString(), paymentDTO.getCode());
				break;
	
			case "afrilandcmr":
				requestBuild = paymentSpecialServices.buildRequestAfriland(debitInfo, paymentDTO.getCode(), addedParamsPaymentDTO.getContribuableId(), "",
						String.valueOf((int) Math.round(paymentDTO.getAmount())), refEmissionOuRecette);
				break;
	
			default:
				break;
		}

		if (!MeansOfPayment.AFRILAND.getAll().contains(paymentDTO.getMeansOfPayment().toString())) {
			result.put("Reject", "MeansOfPayment Not Founds");
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
		}
		
		resultTransaction = restClientTransactionService.getTransaction(
				paymentSpecialServices.convertProvider(paymentDTO.getMeansOfPayment().toString()), requestBuild);

		result.put("paymentDTO", paymentDTO2);
		result.put("resultTransaction", resultTransaction);

		return new ResponseEntity<>(result, HttpStatus.OK);
    	
	}
    
    

	@PostMapping("/callbackTransaction/{codePaiement}/{status_code}")
	public ResponseEntity<String> callbackTransaction(@Valid @RequestBody TransactionDTO transactionDTO,
			@PathVariable String codePaiement, @PathVariable String status_code) {// cette methode sera démarrer par un
																					// client feign configuré dans
																					// mstransaction

		String resultat = "Success";
		Statut status = null;
		Payment payment = new Payment();
		EmissionDTO emissionDTO = null;
		RetPaiFiscalis[] retourPaiFiscalis = null;
//		TransactionDTO transaction = new TransactionDTO();

		// we accept status code equal <100> or <400>
		if (!status_code.matches("400|100"))
			return new ResponseEntity<>(resultat = "status_code reject", HttpStatus.NOT_ACCEPTABLE);

		if (status_code.equals("100")) /* Payment Sucessfull */ status = Statut.VALIDATED;
		else /* Payment Failed */ status = Statut.CANCEL;

		// create transaction
//    	transactionService.save(transactionDTO);
//    	transaction = transactionService.save(transactionDTO);

		// find payment by codePaiement and update status
		payment = paymentService.findByCode(codePaiement);
		if (payment == null) return new ResponseEntity<>(resultat = "Payment Not Exist", HttpStatus.NOT_ACCEPTABLE);

		Optional<UserDTO> userDTO = restClientUAAService.searchUser(payment.getCreatedBy());
		userDTO.orElse(new UserDTO());
		paymentService.update(payment.getId(), status, transactionDTO);
		historiquePaymentService.saveHistPay(status.toString(), transactionDTO.getDate(), payment);
		//log.info("========// " + payment + " //============");
		// TODO UPDATE THIS SECTION
		// detail organisation
		Map<String, Object> organisationDetails = new HashMap<String, Object>();
		Map<String, Object> recetteServiceDetails = new HashMap<String, Object>();
		if (status_code.equals("100")) {//on génère le reçu en cas de paiement réussi

			// en cas de paiement d une emission on met a jour le statut de l emission
			if (payment.getIdEmission() != null && payment.getIdEmission() > 0) {


				// update emission status
				retourPaiFiscalis = restClientEmissionService.updateEmission(payment.getIdEmission(), status, transactionDTO.getCamcisRef(), transactionDTO.getTelephone(), paymentMapper.toDto(payment)).getBody();

				// create historique emission
				restClientEmissionService.createEmissionHistorique(new EmissionHistoriqueDTO(), status.toString(),
						payment.getIdEmission());

				emissionDTO = restClientEmissionService.getEmission(payment.getIdEmission());
			}
			log.info("======== JUSTIF 1============");
			// update recette service
			if (payment.getIdRecette() != null && payment.getIdRecette() > 0) {

				restClientRNFService.payerRecettesService(payment.getIdRecette(), payment.getId());
			}

			if (emissionDTO == null && payment.getIdRecette() == null && payment.getIdRecette() < 0)
				return new ResponseEntity<>(resultat = "Emission Not Exist", HttpStatus.NOT_FOUND);

			log.info("======== JUSTIF 2============");
			JustificatifPaiementDTO justificatifPaiementDTO = new JustificatifPaiementDTO();
			Set<ImputationDTO> listImput = new HashSet<ImputationDTO>();
			ImputationDTO imputationDTO = new ImputationDTO();

			justificatifPaiementDTO.setIdPaiement(payment.getId());
			justificatifPaiementDTO.setDateCreation(transactionDTO.getDate());
			justificatifPaiementDTO.setMontant(payment.getAmount());
			justificatifPaiementDTO.setReferencePaiement(payment.getCode());
			log.info("======== JUSTIF 3============");
			if (emissionDTO != null) {
				organisationDetails = restClientOrganisationService
						.findOrganisationById(emissionDTO.getIdOrganisation());
				log.info("======== JUSTIF 4============");

				if (retourPaiFiscalis != null) {
					for (int i = 0; i < retourPaiFiscalis.length; i++) {
						imputationDTO.setMontant(Double.valueOf(retourPaiFiscalis[i].getMontant_imputation()));
						imputationDTO.setNumDeclarationImputation(payment.getId());
						imputationDTO.setOperation(emissionDTO.getRefEmi());
						imputationDTO.setNatrureDesDroits(retourPaiFiscalis[i].getLibelle_imputation());
						listImput.add(imputationDTO);
						imputationDTO = new ImputationDTO();
					}
				} else {
					imputationDTO.setMontant(payment.getAmount());
					imputationDTO.setNumDeclarationImputation(payment.getId());
					imputationDTO.setOperation(emissionDTO.getRefEmi());
					imputationDTO
							.setNatrureDesDroits(emissionDTO.getNature().name() + " N° " + emissionDTO.getRefEmi());
					listImput.add(imputationDTO);
				}

				log.info("======== JUSTIF 5============");
				justificatifPaiementDTO.setNui(emissionDTO.getCodeContribuable());
				justificatifPaiementDTO
						.setIdOrganisation(Long.valueOf((Integer) organisationDetails.get("idOrganisation")));
				justificatifPaiementDTO.setNatureRecette(emissionDTO.getRefEmi());
				log.info("======== JUSTIF 6============");
			}

			if (payment.getIdRecette() != null && payment.getIdRecette() > 0) {// emissionDTO == null

				organisationDetails = restClientOrganisationService.findOrganisationById(payment.getIdOrganisation());
				recetteServiceDetails = restClientRNFService.getResumeRecettesService(payment.getIdRecette());
				justificatifPaiementDTO.setIdOrganisation(payment.getIdOrganisation());

				justificatifPaiementDTO.setNui(userDTO.get().getNumeroContrubuable());
				justificatifPaiementDTO.setNatureRecette((String) recetteServiceDetails.get("nature"));
				imputationDTO.setMontant(payment.getAmount());
				imputationDTO.setNumDeclarationImputation(payment.getId());
				imputationDTO.setOperation(String.valueOf(payment.getIdRecette()));
				imputationDTO.setNatrureDesDroits((String) recetteServiceDetails.get("nature"));
				listImput.add(imputationDTO);
			}

			justificatifPaiementDTO.setTypePaiement(payment.getMeansOfPayment().name());
			justificatifPaiementDTO.setTypeJustificatifPaiement("RECU");
			justificatifPaiementDTO.setCode(payment.getCode());
			log.info("======== JUSTIF 7============");
			if (userDTO.get().getFirstName() == null) {
				userDTO.get().setFirstName("");
			}
			if (userDTO.get().getLastName() == null) {
				userDTO.get().setLastName("");
			}
			log.info("======== JUSTIF 8============");
			justificatifPaiementDTO
					.setNomPrenomClient(userDTO.get().getFirstName() + " " + userDTO.get().getLastName());
			justificatifPaiementDTO.setNomOrganisation((String) organisationDetails.get("nomOrganisation"));
			justificatifPaiementDTO.setCodeOrganisation((String) organisationDetails.get("codeOrg"));
			justificatifPaiementDTO.setRaisonSociale(userDTO.get().getRaisonSocialeEntreprise());
			justificatifPaiementDTO.setSigle("");
			justificatifPaiementDTO.setCodePoste(1L);
			log.info("======== JUSTIF 9============");
			justificatifPaiementDTO.setExercise(String.valueOf(LocalDateTime.now().getYear()));
			justificatifPaiementDTO.setMois(LocalDateTime.now().getMonth().name());
			justificatifPaiementDTO.setLibelleCentre((String) organisationDetails.get("nomOrganisation"));
			justificatifPaiementDTO.setLibelleCourtCentre((String) organisationDetails.get("codeOrg"));
			justificatifPaiementDTO.setIfu(" ");
			log.info("======== JUSTIF 10============");
			justificatifPaiementDTO.setImputations(listImput);
			log.info("======== JUSTIF 11============");
			restClientQuittanceService.genererRecuOuQuittance(justificatifPaiementDTO);
			log.info("======== JUSTIF 12============");

			// generate notification
			TypeNotificationDTO typeNotificationPayment = null;
			try {
				typeNotificationPayment = restClientNotificationService.getTypeNotification("payment");
				log.info("======== CHECK 1============");
			} catch (FeignException e) {
				log.error(e.getMessage());
				e.printStackTrace();
				log.info("======== CHECK 2============");
			} finally {

				if (typeNotificationPayment == null) {
					typeNotificationPayment = new TypeNotificationDTO(null, "payment", "Notification de paiement",
							"Notification des paiements effectués", null, "PUSH", null);
					typeNotificationPayment = restClientNotificationService.createTypeNotification(typeNotificationPayment);
					log.info("======== CHECK 3============");
				}
				NotificationDTO notificationPayment = new NotificationDTO(null,
						"Votre payment N° [" + payment.getId() + "] d'un montant de " + payment.getAmount()
								+ " effectué via " + payment.getMeansOfPayment().name()
								+ " a réussi.",
						userDTO.get().getId(), applicationName, "NONTRANSMIS", typeNotificationPayment.getId(), null);
				//restClientNotificationService.createNotification(notificationPayment);
                kafkaTemplate.send(topic,applicationName+ LocalDateTime.now(),notificationPayment);
                log.info("Notification créé et transmit au broker {}", notificationPayment);
				log.info("======== CHECK 4============");
			}
		}
		log.info("======== JUSTIF 13============");
		return new ResponseEntity<>(resultat, HttpStatus.OK);

	}

	@PostMapping(value = "/confirmPaymentAfriland/{otp}/{trxid}")
	Map<String, String> confirmPaymentAfriland(@PathVariable("otp") String otp, @PathVariable("trxid") String trxid) {

		Map<String, String> result = new HashMap<>();

		try {
			 result = restClientTransactionService.confirmPayment(otp, trxid);
		} catch (Exception e) {
			log.error(e.getMessage());
			result.put("Exception when confirmPaymentAfriland", e.getMessage());
			return result;
		}

		return result;
	}

	@GetMapping("/literPaymentByStatut/{statut}")
	public ResponseEntity<List<Object>> literPaymentByStatut(@PathVariable Statut statut, Pageable pageable) {

		Page<Object> pageresult = paymentService.findByStatut(statut, pageable);
		HttpHeaders headers = null;
		List<Object> body = null;

		try {
			body= pageresult.getContent();
		} catch (NullPointerException e) {
			// TODO: handle exception
			log.error(e.getMessage());
			headers = PaginationUtil
					.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), null);
			return new ResponseEntity<>(null, headers, HttpStatus.NOT_FOUND);
		}

		headers = PaginationUtil
				.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), pageresult);
		return new ResponseEntity<>(body, headers, HttpStatus.OK);
	}

	@GetMapping("/literPaymentEmissionContrib/{niu}")
	public ResponseEntity<List<Payment>> literPaymentEmissionContrib(@PathVariable String niu) {

		// Get All Id of Emission already payed
		List<String> emissionIdList = restClientEmissionService.getEmissionsContri(niu);
		List<Payment> paymentList = new ArrayList<>();

		// Get All Payment where Id_Emission equals Id in emissionIdList
		if (emissionIdList != null) {
			for (String idEmis : emissionIdList) {
				paymentList.add(paymentService.findByIdEmission(Long.parseLong(idEmis)));
			}
		}

		return new ResponseEntity<>(paymentList, HttpStatus.OK);
	}

	/**
	 *
	 * @param option   valeurs possibles [all, emissions, rnf]
	 * @param pageable
	 * @return
	 */
	@GetMapping("/listPaymentUser/{option}")
	public ResponseEntity<List<Payment>> literPaymentByStatut(@PathVariable String option, Pageable pageable) {
		String username = SecurityUtils.getCurrentUserLogin().get();
		Page<Payment> pageresult = null;
		if (option.equalsIgnoreCase("all")) {
			pageresult = paymentService.findAllByCreatedBy(username, pageable);
		} else if (option.equalsIgnoreCase("emissions")) {
			pageresult = paymentService.findEmissionByCreatedBy(username, pageable);
		} else if (option.equalsIgnoreCase("rnf")) {
			pageresult = paymentService.findRNFByCreatedBy(username, pageable);
		}

		List<Payment> body = null;
		HttpHeaders headers = null;
		try {
			body = pageresult.getContent();
		} catch (NullPointerException e) {
			// TODO: handle exception
			log.error(e.getMessage());
			headers = PaginationUtil
					.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), null);
			return new ResponseEntity<>(null, headers, HttpStatus.NOT_FOUND);
		}

		headers = PaginationUtil
				.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), pageresult);
		return new ResponseEntity<>(body, headers, HttpStatus.OK);
	}

//    @GetMapping("/listerPaymentByCodeTransaction/{codeTransaction}")
//    public ResponseEntity<Map<String, String>> listerPaymentByCodeTransaction(@PathVariable String codeTransaction){
//    	Map<String, String> resultData = new LinkedHashMap<String, String>();
//    	TransactionDTO transaction = transactionService.findByCodeTransaction(codeTransaction);
//    	Payment payment = paymentService.findByIdTransactionId(transaction.getId());
//
//    	resultData.put("statusPaie", payment.getStatut().toString());
//    	resultData.put("codePaie", payment.getCode());
//    	resultData.put("datePaie", transaction.getDate().toString());
//    	return new ResponseEntity<>(resultData, HttpStatus.OK);
//    }

	/**
	 * {@code PUT  /payments} : Updates an existing payment.
	 *
	 * @param paymentDTO the paymentDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated paymentDTO, or with status {@code 400 (Bad Request)} if
	 *         the paymentDTO is not valid, or with status
	 *         {@code 500 (Internal Server Error)} if the paymentDTO couldn't be
	 *         updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PutMapping("/payments")
	public ResponseEntity<PaymentDTO> updatePayment(@Valid @RequestBody PaymentDTO paymentDTO)
			throws URISyntaxException {
		log.debug("REST request to update Payment : {}", paymentDTO);
		if (paymentDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		PaymentDTO result = paymentService.save(paymentDTO);
		return ResponseEntity.ok().headers(
				HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentDTO.getId().toString()))
				.body(result);
	}

	/**
	 * {@code GET  /payments} : get all the payments.
	 *
	 * @param pageable the pagination information.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of payments in body.
	 */
	@GetMapping("/payments")
	public ResponseEntity<List<PaymentDTO>> getAllPayments(Pageable pageable) {
		log.debug("REST request to get a page of Payments");
		Page<PaymentDTO> page = paymentService.findAll(pageable);
		HttpHeaders headers = PaginationUtil
				.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
		return ResponseEntity.ok().headers(headers).body(page.getContent());
	}

	/**
	 * {@code GET  /payments/:id} : get the "id" payment.
	 *
	 * @param id the id of the paymentDTO to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the paymentDTO, or with status {@code 404 (Not Found)}.
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
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
				.build();
	}

	@PreAuthorize("hasRole('AUTH_PAIEMENT_EMISSION') or hasRole('AUTH_PAIEMENT_RECETTE')")
	@PostMapping("/effectuerPaiementWithoutApi/{debitInfo}/{niu}/{refEmi}")
	public ResponseEntity<Map<String, Object>> effectuerPaiementManuel(@RequestBody Map<String, Object> body
			, @PathVariable String debitInfo, @PathVariable String niu, @PathVariable String refEmi
	) {

		Map<String, Object> result = new LinkedHashMap<String, Object>();
		Map<String, String> resultEmission = new LinkedHashMap<String, String>();
		Map<String, Object> organisationDetails = new HashMap<String, Object>();
		Object resultRecette = null;
		EmissionDTO emissionDTO2 = null;

		// controle body enter
		if (body == null) {
			result.put("paymentCode", null);
			result.put("paymentStatus", "CANCELED");
			result.put("paymentMessageStatus", "payment failed -->> Enter Datas is Null");
			return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
		}

		JSONObject bodyJson = new JSONObject(body);
		JSONObject paymentDTOJson = new JSONObject(bodyJson.get("paymentDTO").toString());
		JSONObject addedParamsPaymentDTOJson = new JSONObject(bodyJson.get("addedParamsPaymentDTO").toString());

		PaymentDTO paymentDTO = null;
		AddedParamsPaymentDTO addedParamsPaymentDTO = null;

		try {
			paymentDTO = new ObjectMapper().readValue(paymentDTOJson.toString(), PaymentDTO.class);
			addedParamsPaymentDTO = new ObjectMapper().readValue(addedParamsPaymentDTOJson.toString(),
					AddedParamsPaymentDTO.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			result.put("paymentCode", null);
			result.put("paymentStatus", "CANCELED");
			result.put("paymentMessageStatus", "payment failed -->> Bad Datas Entry Of Payment");
			return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
		}

		if (paymentDTO == null) {
			result.put("paymentCode", null);
			result.put("paymentStatus", "CANCELED");
			result.put("paymentMessageStatus", "payment failed -->> Bad Datas Entry Of Payment");
			return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
		}

		String provider = paymentSpecialServices.convertProvider(paymentDTO.getMeansOfPayment().toString());

		// controle du niu en cas des emissions
		if (!refEmi.equals("null")) {

			Object niuVerif = restClientUAAService.getNiuContribuablesEnregistres(niu);

			if (niuVerif == null) {
				result.put("paymentCode", null);
				result.put("paymentStatus", "CANCELED");
				result.put("paymentMessageStatus", "payment failed -->> Usurpateur Voulant effectuer le paiement");
				return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
			}

			// controle du depassement du montant a payer
			if ((provider.matches("MOBILE_MONEY|MOBILE_MONEY2|ORANGE_MONEY|ORANGE_MONEY2|EXPRESS_UNION|EXPRESS_UNION2|ECOBANK|ECOBANK2"))
					&& (paymentDTO.getAmount() > 500000 || paymentDTO.getAmount() <= 0)) {
				result.put("paymentCode", null);
				result.put("paymentStatus", "CANCELED");
				result.put("paymentMessageStatus", "payment failed -->> Depassement de montant, le montant doit etre compris entre 0 et 500mill");
				return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
			}
		}

		// controle du numero de telephone, selon le moyen de paiement
		if ((provider.matches("MOBILE_MONEY|MOBILE_MONEY2|ORANGE_MONEY|ORANGE_MONEY2|EXPRESS_UNION|EXPRESS_UNION2|ECOBANK|ECOBANK2"))
				&& (debitInfo.isEmpty() || debitInfo == null)) {
			result.put("paymentCode", null);
			result.put("paymentStatus", "CANCELED");
			result.put("paymentMessageStatus", "payment failed -->> Phone Number or account number is Required");
			return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
		}

		// controle des données du paiement
		if (paymentDTO.getId() != null || (paymentDTO.getIdTransaction() != null) || paymentDTO.getIdDetVersId() != null
				|| ((paymentDTO.getIdEmission() == null || paymentDTO.getIdEmission() <= 0)
						&& (paymentDTO.getIdRecette() == null || paymentDTO.getIdRecette() <= 0))) {
			result.put("paymentCode", null);
			result.put("paymentStatus", "CANCELED");
			result.put("paymentMessageStatus", "payment failed -->> Bad Entry");
			return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
		}

		// complete datas paymentDTO
		paymentDTO.setStatut(Statut.VALIDATED);
		paymentDTO.setCode(UUID.randomUUID().toString());

		PaymentDTO paymentDTO2;
		
		// case emission
		if (!refEmi.equals("null")) {

			resultEmission = restClientEmissionService.findRefEmission(paymentDTO.getIdEmission());

			if (resultEmission == null) {// si l emission a payer n existe pas dans la liste des emission
				result.put("paymentCode", null);
				result.put("paymentStatus", "CANCELED");
				result.put("paymentMessageStatus", "payment failed -->> Emission Does Not Exist");
				return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
			}

			if (resultEmission.get("refEmi") == null || resultEmission.get("refEmi").equals("")) {
				result.put("paymentCode", null);
				result.put("paymentStatus", "CANCELED");
				result.put("paymentMessageStatus", "payment failed -->> Emission Does Not Have Reference");
				return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
			}

			if ((Double.parseDouble(resultEmission.get("amount")) - paymentDTO.getAmount()) != 0) {// si les montant ne matche pas

				result.put("paymentCode", null);
				result.put("paymentStatus", "CANCELED");
				result.put("paymentMessageStatus", "payment failed -->> Amount not correct - Paiement Rejected");
				return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
			}

			if(!resultEmission.get("refEmi").equals(refEmi)) {
				result.put("paymentCode", null);
				result.put("paymentStatus", "CANCELED");
				result.put("paymentMessageStatus", "payment failed -->> Emission Reference not matching with Payment.IdEmission");
				return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
			}
			
			// Before sending and making the payment on the transaction ms we shall first get the Emission from CAMCIS
			// case of CAMCIS only
			if (!resultEmission.get("type").equalsIgnoreCase(Nature.AVIS.name())
					&& !resultEmission.get("type").equalsIgnoreCase(Nature.AMR.name())
					&& !resultEmission.get("type").equalsIgnoreCase(Nature.IMPOTS.name())) {
				 if(!restClientEmissionService.checkEmission(niu, refEmi)) {
					 result.put("paymentCode", null);
					 result.put("paymentStatus", "CANCELED");
					 result.put("paymentMessageStatus", "Payment can't be done - refEmission Not Found");
					 return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
				 }
				 
	    	}

			// initialize datas of emission ot create before save payment
			EmissionDTO emissionDTO = new EmissionDTO();
			emissionDTO.setStatus(Statut.DRAFT);
			emissionDTO.setAmount(paymentDTO.getAmount());
			emissionDTO.setRefEmi(refEmi.toString());
			emissionDTO.setCodeContribuable(niu);
			emissionDTO.setNature(Nature.valueOf(resultEmission.get("type")));

//			emissionDTO.setIdOrganisation(1L);
			organisationDetails = restClientOrganisationService
					.findOrganisationByLibelleCourt(resultEmission.get("codeOrg"));
			if (!organisationDetails.isEmpty()) {
				log.info(".................. " + resultEmission.toString());
				log.info(".................. " + organisationDetails.get("idOrganisation"));
				emissionDTO.setIdOrganisation(Long.parseLong(organisationDetails.get("idOrganisation").toString()));
			} else {
				if (resultEmission.get("type").equalsIgnoreCase(Nature.AVIS.name())
						|| resultEmission.get("type").equalsIgnoreCase(Nature.AMR.name())
						|| resultEmission.get("type").equalsIgnoreCase(Nature.IMPOTS.name())) {
					emissionDTO.setIdOrganisation(DEFAULT_ORGANISATION_DGI_ID);
				} else {
					emissionDTO.setIdOrganisation(DEFAULT_ORGANISATION_DGD_ID);
				}
			}


			//create emission with datas to complete
			emissionDTO2 = restClientEmissionService.createEmission(emissionDTO);

			// complete datas payment with idEmission create, and save payment
			paymentDTO.setIdEmission(emissionDTO2.getId());
			paymentDTO.setIdOrganisation(emissionDTO.getIdOrganisation());
			
		
			
		} else {// case recette non fiscale, create payment directly with idRecette in
				// PaymentDTO entry

			if (paymentDTO.getAmount() <= 0) {
				result.put("paymentCode", null);
				result.put("paymentStatus", "CANCELED");
				result.put("paymentMessageStatus", "payment failed -->> le montant de la recette doit etre au moins positif");
				return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
			}

//			paymentDTO2 = paymentService.save(paymentDTO);
			resultRecette = this.restClientRNFService.getRecettesService(paymentDTO.getIdRecette());
			if (resultRecette == null) {
			
				result.put("paymentCode", null);
				result.put("paymentStatus", "CANCELED");
				result.put("paymentMessageStatus", "payment failed -->> Recette Not Found");
				return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
			}
			
			
		}
		
		Map<String, String> res = restClientTransactionService.processPaymentInCash(provider,
				paymentSpecialServices.buildRequestWithoutApi(paymentDTO.getCode(), niu, debitInfo,
						String.valueOf((int) Math.round(paymentDTO.getAmount())),
						addedParamsPaymentDTO.getFirstname(), addedParamsPaymentDTO.getLastname(), ""), app.getSecret());

		paymentDTO.setRefTransaction(res.get("transactionid"));
		paymentDTO2 = paymentService.save(paymentDTO);
		
		
		// create historique payment
				historiquePaymentService.saveHistPay(Statut.DRAFT.toString(), LocalDateTime.now(),
						paymentMapper.toEntity(paymentDTO2));
				
		//generated recu
		Map<String, Object> recetteServiceDetails = new HashMap<String, Object>();
		Payment payment = paymentService.findByCode(paymentDTO2.getCode());
		Optional<UserDTO> userDTO = Optional.of(new UserDTO());
		userDTO.orElse(new UserDTO());
		RetPaiFiscalis[] retourPaiFiscalis = null;

		if (restClientUAAService.searchUser(payment.getCreatedBy()) != null)
			userDTO = restClientUAAService.searchUser(payment.getCreatedBy());
		else {

			userDTO.get().setFirstName(addedParamsPaymentDTO.getFirstname());
			userDTO.get().setLastName(addedParamsPaymentDTO.getLastname());
			userDTO.get().setRaisonSocialeEntreprise(organisationDetails.get("nomOrganisation").toString());
		}

		// case emission
		if (!refEmi.equals("null")) {
			// update emission status
			retourPaiFiscalis = restClientEmissionService.updateEmission(payment.getIdEmission(), Statut.VALIDATED, res.get("camcisref"), debitInfo, paymentMapper.toDto(payment)).getBody();
			
			// create historique emission
			restClientEmissionService.createEmissionHistorique(new EmissionHistoriqueDTO(), Statut.VALIDATED.toString(),
					payment.getIdEmission());
		}

		historiquePaymentService.saveHistPay(Statut.VALIDATED.toString(), LocalDateTime.now(),
				paymentMapper.toEntity(paymentDTO2));
		
		/*
		 * if (retourPaiFiscalis == null) { result.put("paymentMessageStatus",
		 * "payment Failed"); result.put("suggestion",
		 * "you have to generate manualy reçu or quittance"); return new
		 * ResponseEntity<>(result, HttpStatus.OK); }
		 */

		JustificatifPaiementDTO justificatifPaiementDTO = new JustificatifPaiementDTO();
		Set<ImputationDTO> listImput = new HashSet<ImputationDTO>();
		ImputationDTO imputationDTO = new ImputationDTO();

		justificatifPaiementDTO.setIdPaiement(payment.getId());
		justificatifPaiementDTO.setDateCreation(LocalDateTime.now());
		justificatifPaiementDTO.setMontant(payment.getAmount());
		justificatifPaiementDTO.setReferencePaiement(payment.getCode());
		log.info("======== JUSTIF 3============");

		if (emissionDTO2 != null) {
			organisationDetails = restClientOrganisationService
					.findOrganisationById(emissionDTO2.getIdOrganisation());
//			System.out.println("---------------------***********************" + emissionDTO2.getIdOrganisation());
//			System.out.println("---------------------***********************" + organisationDetails);
			log.info("======== JUSTIF 4============");

			if (retourPaiFiscalis != null) {
				for (int i = 0; i < retourPaiFiscalis.length; i++) {
					imputationDTO.setMontant(Double.valueOf(retourPaiFiscalis[i].getMontant_imputation()));
					imputationDTO.setNumDeclarationImputation(payment.getId());
					imputationDTO.setOperation(emissionDTO2.getRefEmi());
					imputationDTO.setNatrureDesDroits(retourPaiFiscalis[i].getLibelle_imputation());
					listImput.add(imputationDTO);
//					imputationDTO = new ImputationDTO();
				}
			} else {
				imputationDTO.setMontant(payment.getAmount());
				imputationDTO.setNumDeclarationImputation(payment.getId());
				imputationDTO.setOperation(emissionDTO2.getRefEmi());
				imputationDTO
						.setNatrureDesDroits(emissionDTO2.getNature().name() + " N° " + emissionDTO2.getRefEmi());
				listImput.add(imputationDTO);
			}

//			imputationDTO.setMontant(100d);
//			imputationDTO.setNumDeclarationImputation(payment.getId());
//			imputationDTO.setOperation(emissionDTO2.getRefEmi());
//			imputationDTO.setNatrureDesDroits("nature");
//			listImput.add(imputationDTO);

			log.info("======== JUSTIF 5============");
			justificatifPaiementDTO.setNui(niu);
			justificatifPaiementDTO
					.setIdOrganisation(Long.valueOf((Integer) organisationDetails.get("idOrganisation")));
//			justificatifPaiementDTO.setIdOrganisation(1L);
			justificatifPaiementDTO.setNatureRecette(emissionDTO2.getRefEmi());
			log.info("======== JUSTIF 6============");
		}


		if (paymentDTO2.getIdRecette() != null && paymentDTO2.getIdRecette() > 0) {// emissionDTO == null

			organisationDetails = restClientOrganisationService.findOrganisationById(payment.getIdOrganisation());
			recetteServiceDetails = restClientRNFService.getResumeRecettesService(payment.getIdRecette());
			justificatifPaiementDTO.setIdOrganisation(payment.getIdOrganisation());

			justificatifPaiementDTO.setNui(niu);
			justificatifPaiementDTO.setNatureRecette((String) recetteServiceDetails.get("nature"));
//			justificatifPaiementDTO.setNatureRecette("nature");
			imputationDTO.setMontant(payment.getAmount());
			imputationDTO.setNumDeclarationImputation(payment.getId());
			imputationDTO.setOperation(String.valueOf(payment.getIdRecette()));
			imputationDTO.setNatrureDesDroits((String) recetteServiceDetails.get("nature"));
//			imputationDTO.setNatrureDesDroits("nature");
			listImput.add(imputationDTO);
		}

		justificatifPaiementDTO.setTypePaiement(payment.getMeansOfPayment().name());
		justificatifPaiementDTO.setTypeJustificatifPaiement("RECU");
		justificatifPaiementDTO.setCode(payment.getCode());
		log.info("======== JUSTIF 7============");
		if (userDTO.get().getFirstName() == null) {
			userDTO.get().setFirstName("");
		}
		if (userDTO.get().getLastName() == null) {
			userDTO.get().setLastName("");
		}
		log.info("======== JUSTIF 8============");
		justificatifPaiementDTO
				.setNomPrenomClient(userDTO.get().getFirstName() + " " + userDTO.get().getLastName());
		justificatifPaiementDTO.setNomOrganisation((String) organisationDetails.get("nomOrganisation"));
		justificatifPaiementDTO.setCodeOrganisation((String) organisationDetails.get("codeOrg"));
//		justificatifPaiementDTO.setNomOrganisation("nomOrganisation");
//		justificatifPaiementDTO.setCodeOrganisation("codeOrg");
		justificatifPaiementDTO.setRaisonSociale(userDTO.get().getRaisonSocialeEntreprise());
//		justificatifPaiementDTO.setRaisonSociale("raison");
		justificatifPaiementDTO.setSigle("");
		justificatifPaiementDTO.setCodePoste(1L);
		log.info("======== JUSTIF 9============");
		justificatifPaiementDTO.setExercise(String.valueOf(LocalDateTime.now().getYear()));
		justificatifPaiementDTO.setMois(LocalDateTime.now().getMonth().name());
		justificatifPaiementDTO.setLibelleCentre((String) organisationDetails.get("nomOrganisation"));
		justificatifPaiementDTO.setLibelleCourtCentre((String) organisationDetails.get("codeOrg"));
//		justificatifPaiementDTO.setLibelleCentre("nomOrganisation");
//		justificatifPaiementDTO.setLibelleCourtCentre("codeOrg");
		justificatifPaiementDTO.setIfu(" ");
		log.info("======== JUSTIF 10============");
		justificatifPaiementDTO.setImputations(listImput);
		log.info("======== JUSTIF 11============");
		restClientQuittanceService.genererRecuOuQuittance(justificatifPaiementDTO);
		log.info("======== JUSTIF 12============");

		// generate notification
		TypeNotificationDTO typeNotificationPayment = null;
		try {
			typeNotificationPayment = restClientNotificationService.getTypeNotification("payment");
			log.info("======== CHECK 1============");
		} catch (FeignException e) {
			log.error(e.getMessage());
			e.printStackTrace();
			log.info("======== CHECK 2============");
		} finally {

			if (typeNotificationPayment == null) {
				typeNotificationPayment = new TypeNotificationDTO(null, "payment", "Notification de paiement",
						"Notification des paiements effectués", null, "PUSH", null);
				typeNotificationPayment = restClientNotificationService.createTypeNotification(typeNotificationPayment);
				log.info("======== CHECK 3============");
			}
			NotificationDTO notificationPayment = new NotificationDTO(null,
					"Votre payment N° [" + payment.getId() + "] d'un montant de " + payment.getAmount()
							+ " effectué via " + payment.getMeansOfPayment().name()
							+ " a réussi.",
					userDTO.get().getId(), applicationName, "NONTRANSMIS", typeNotificationPayment.getId(), null);
			//restClientNotificationService.createNotification(notificationPayment);
            kafkaTemplate.send(topic,applicationName+ LocalDateTime.now(),notificationPayment);
            log.info("Notification créé et transmit au broker {}", notificationPayment);
			log.info("======== CHECK 4============");
		}
		
		result.put("paymentId", paymentDTO2.getId());
		result.put("transactionId", res.get("transactionid"));
		result.put("paymentStatus", paymentDTO2.getStatut());
		result.put("paymentMessageStatus", "payment successful");
		return new ResponseEntity<>(result, HttpStatus.OK);

	  }
	
	@PreAuthorize("hasRole('AUTH_PAIEMENT_EMISSION') or hasRole('AUTH_PAIEMENT_RECETTE')")
	@PostMapping("/confirmationPaymentUBA/{phone}/{refEmi}/{niu}/{partnerTrxId}")
	public ResponseEntity<Map<String, Object>> confirmationPaymentUBA(@RequestBody Map<String, Object> body,
			@PathVariable String phone, @PathVariable String refEmi, @PathVariable String niu, @PathVariable String partnerTrxId) {

		Map<String, Object> result = new LinkedHashMap<String, Object>();
		Map<String, String> resultEmission = new LinkedHashMap<String, String>();
		Map<String, Object> organisationDetails = new HashMap<String, Object>();
		Object resultRecette = null;
		EmissionDTO emissionDTO2 = null;

		// controle body enter
		if (body == null) {
			result.put("paymentCode", null);
			result.put("paymentStatus", "CANCELED");
			result.put("paymentMessageStatus", "payment failed -->> Enter Datas is Null");
			return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
		}

		JSONObject bodyJson = new JSONObject(body);
		JSONObject paymentDTOJson = new JSONObject(bodyJson.get("paymentDTO").toString());
		JSONObject addedParamsPaymentDTOJson = new JSONObject(bodyJson.get("addedParamsPaymentDTO").toString());

		PaymentDTO paymentDTO = null;
		AddedParamsPaymentDTO addedParamsPaymentDTO = null;

		try {
			paymentDTO = new ObjectMapper().readValue(paymentDTOJson.toString(), PaymentDTO.class);
			addedParamsPaymentDTO = new ObjectMapper().readValue(addedParamsPaymentDTOJson.toString(),
					AddedParamsPaymentDTO.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			result.put("paymentCode", null);
			result.put("paymentStatus", "CANCELED");
			result.put("paymentMessageStatus", "payment failed -->> Bad Datas Entry Of Payment");
			return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
		}

		if (paymentDTO == null) {
			result.put("paymentCode", null);
			result.put("paymentStatus", "CANCELED");
			result.put("paymentMessageStatus", "payment failed -->> Datas Entry is null");
			return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
		}

		String provider = paymentSpecialServices.convertProvider(paymentDTO.getMeansOfPayment().toString());

		// complete datas paymentDTO
		paymentDTO.setStatut(Statut.VALIDATED);
		paymentDTO.setCode(UUID.randomUUID().toString());

		PaymentDTO paymentDTO2;

		// case emission
		if (!refEmi.equals("null")) {
			
			resultEmission = restClientEmissionService.findRefEmission(paymentDTO.getIdEmission());

			// initialize datas of emission ot create before save payment
			EmissionDTO emissionDTO = new EmissionDTO();
			emissionDTO.setStatus(Statut.VALIDATED);
			emissionDTO.setAmount(paymentDTO.getAmount());
			emissionDTO.setRefEmi(refEmi.toString());
			emissionDTO.setCodeContribuable(niu);
			emissionDTO.setNature(Nature.valueOf(resultEmission.get("type")));

//			emissionDTO.setIdOrganisation(1L);
			
			organisationDetails = restClientOrganisationService
					.findOrganisationByLibelleCourt(resultEmission.get("codeOrg"));
			if (!organisationDetails.isEmpty()) {
				log.info(".................. " + resultEmission.toString());
				log.info(".................. " + organisationDetails.get("idOrganisation"));
				emissionDTO.setIdOrganisation(Long.parseLong(organisationDetails.get("idOrganisation").toString()));
			} else {
				if (resultEmission.get("type").equalsIgnoreCase(Nature.AVIS.name())
						|| resultEmission.get("type").equalsIgnoreCase(Nature.AMR.name())
						|| resultEmission.get("type").equalsIgnoreCase(Nature.IMPOTS.name())) {
					emissionDTO.setIdOrganisation(DEFAULT_ORGANISATION_DGI_ID);
				} else {
					emissionDTO.setIdOrganisation(DEFAULT_ORGANISATION_DGD_ID);
				}
			}


			//create emission with datas to complete
			emissionDTO2 = restClientEmissionService.createEmission(emissionDTO);

			// complete datas payment with idEmission create, and save payment
			paymentDTO.setIdEmission(emissionDTO2.getId());
			paymentDTO.setIdOrganisation(emissionDTO.getIdOrganisation());
			paymentDTO2 = paymentService.save(paymentDTO);
		} else {// case recette non fiscale, create payment directly with idRecette in
				// PaymentDTO entry

			resultRecette = this.restClientRNFService.getRecettesService(paymentDTO.getIdRecette());
			if (resultRecette != null) {
				paymentDTO2 = paymentService.save(paymentDTO);
			} else {
				result.put("paymentCode", null);
				result.put("paymentStatus", "CANCELED");
				result.put("paymentMessageStatus", "payment failed -->> Recette Not Found");
				return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
			}

		}

		// create historique payment
		historiquePaymentService.saveHistPay(Statut.VALIDATED.toString(), LocalDateTime.now(),
				paymentMapper.toEntity(paymentDTO2));

		Map<String, String> res = restClientTransactionService.confirmationPaymentUBA(provider,
				paymentSpecialServices.buildRequestUBA(paymentDTO.getCode(), niu, phone,
						String.valueOf((int) Math.round(paymentDTO.getAmount())),
						addedParamsPaymentDTO.getFirstname(), addedParamsPaymentDTO.getLastname(), partnerTrxId), app.getSecret());

		//generated recu
		Map<String, Object> recetteServiceDetails = new HashMap<String, Object>();
		Payment payment = paymentService.findByCode(paymentDTO2.getCode());
		Optional<UserDTO> userDTO = Optional.of(new UserDTO());
		userDTO.orElse(new UserDTO());
		RetPaiFiscalis[] retourPaiFiscalis = null;

		if (restClientUAAService.searchUser(payment.getCreatedBy()) != null)
			userDTO = restClientUAAService.searchUser(payment.getCreatedBy());
		else {

			userDTO.get().setFirstName(addedParamsPaymentDTO.getFirstname());
			userDTO.get().setLastName(addedParamsPaymentDTO.getLastname());
			userDTO.get().setRaisonSocialeEntreprise(organisationDetails.get("nomOrganisation").toString());
		}

		// case emission
		if (!refEmi.equals("null")) {
			// update emission status
			//retourPaiFiscalis = restClientEmissionService.updateEmission(payment.getIdEmission(), Statut.VALIDATED, paymentMapper.toDto(payment)).getBody();

		}

		JustificatifPaiementDTO justificatifPaiementDTO = new JustificatifPaiementDTO();
		Set<ImputationDTO> listImput = new HashSet<ImputationDTO>();
		ImputationDTO imputationDTO = new ImputationDTO();

		justificatifPaiementDTO.setIdPaiement(payment.getId());
		justificatifPaiementDTO.setDateCreation(LocalDateTime.now());
		justificatifPaiementDTO.setMontant(payment.getAmount());
		justificatifPaiementDTO.setReferencePaiement(payment.getCode());
		log.info("======== JUSTIF 3============");

		if (emissionDTO2 != null) {
			organisationDetails = restClientOrganisationService
					.findOrganisationById(emissionDTO2.getIdOrganisation());
//			System.out.println("---------------------***********************" + emissionDTO2.getIdOrganisation());
//			System.out.println("---------------------***********************" + organisationDetails);
			log.info("======== JUSTIF 4============");

//			if (retourPaiFiscalis != null) {
//				for (int i = 0; i < retourPaiFiscalis.length; i++) {
//					imputationDTO.setMontant(Double.valueOf(retourPaiFiscalis[i].getMontant_imputation()));
//					imputationDTO.setNumDeclarationImputation(payment.getId());
//					imputationDTO.setOperation(emissionDTO2.getRefEmi());
//					imputationDTO.setNatrureDesDroits(retourPaiFiscalis[i].getLibelle_imputation());
//					listImput.add(imputationDTO);
////					imputationDTO = new ImputationDTO();
//				}
//			} else {
//				imputationDTO.setMontant(payment.getAmount());
//				imputationDTO.setNumDeclarationImputation(payment.getId());
//				imputationDTO.setOperation(emissionDTO2.getRefEmi());
//				imputationDTO
//						.setNatrureDesDroits(emissionDTO2.getNature().name() + " N° " + emissionDTO2.getRefEmi());
//				listImput.add(imputationDTO);
//			}

			imputationDTO.setMontant(100d);
			imputationDTO.setNumDeclarationImputation(payment.getId());
			imputationDTO.setOperation(emissionDTO2.getRefEmi());
			imputationDTO.setNatrureDesDroits("nature");
			listImput.add(imputationDTO);

			log.info("======== JUSTIF 5============");
			justificatifPaiementDTO.setNui(niu);
			justificatifPaiementDTO
					.setIdOrganisation(Long.valueOf((Integer) organisationDetails.get("idOrganisation")));
//			justificatifPaiementDTO.setIdOrganisation(1L);
			justificatifPaiementDTO.setNatureRecette(emissionDTO2.getRefEmi());
			log.info("======== JUSTIF 6============");
		}


		if (paymentDTO2.getIdRecette() != null && paymentDTO2.getIdRecette() > 0) {// emissionDTO == null

			organisationDetails = restClientOrganisationService.findOrganisationById(payment.getIdOrganisation());
			recetteServiceDetails = restClientRNFService.getResumeRecettesService(payment.getIdRecette());
			justificatifPaiementDTO.setIdOrganisation(payment.getIdOrganisation());

			justificatifPaiementDTO.setNui(niu);
			justificatifPaiementDTO.setNatureRecette((String) recetteServiceDetails.get("nature"));
//			justificatifPaiementDTO.setNatureRecette("nature");
			imputationDTO.setMontant(payment.getAmount());
			imputationDTO.setNumDeclarationImputation(payment.getId());
			imputationDTO.setOperation(String.valueOf(payment.getIdRecette()));
			imputationDTO.setNatrureDesDroits((String) recetteServiceDetails.get("nature"));
//			imputationDTO.setNatrureDesDroits("nature");
			listImput.add(imputationDTO);
		}

		justificatifPaiementDTO.setTypePaiement(payment.getMeansOfPayment().name());
		justificatifPaiementDTO.setTypeJustificatifPaiement("RECU");
		justificatifPaiementDTO.setCode(payment.getCode());
		log.info("======== JUSTIF 7============");
		if (userDTO.get().getFirstName() == null) {
			userDTO.get().setFirstName("");
		}
		if (userDTO.get().getLastName() == null) {
			userDTO.get().setLastName("");
		}
		log.info("======== JUSTIF 8============");
		justificatifPaiementDTO
				.setNomPrenomClient(userDTO.get().getFirstName() + " " + userDTO.get().getLastName());
		justificatifPaiementDTO.setNomOrganisation((String) organisationDetails.get("nomOrganisation"));
		justificatifPaiementDTO.setCodeOrganisation((String) organisationDetails.get("codeOrg"));
//		justificatifPaiementDTO.setNomOrganisation("nomOrganisation");
//		justificatifPaiementDTO.setCodeOrganisation("codeOrg");
		justificatifPaiementDTO.setRaisonSociale(userDTO.get().getRaisonSocialeEntreprise());
//		justificatifPaiementDTO.setRaisonSociale("raison");
		justificatifPaiementDTO.setSigle("");
		justificatifPaiementDTO.setCodePoste(1L);
		log.info("======== JUSTIF 9============");
		justificatifPaiementDTO.setExercise(String.valueOf(LocalDateTime.now().getYear()));
		justificatifPaiementDTO.setMois(LocalDateTime.now().getMonth().name());
		justificatifPaiementDTO.setLibelleCentre((String) organisationDetails.get("nomOrganisation"));
		justificatifPaiementDTO.setLibelleCourtCentre((String) organisationDetails.get("codeOrg"));
//		justificatifPaiementDTO.setLibelleCentre("nomOrganisation");
//		justificatifPaiementDTO.setLibelleCourtCentre("codeOrg");
		justificatifPaiementDTO.setIfu(" ");
		log.info("======== JUSTIF 10============");
		justificatifPaiementDTO.setImputations(listImput);
		log.info("======== JUSTIF 11============");
		restClientQuittanceService.genererRecuOuQuittance(justificatifPaiementDTO);
		log.info("======== JUSTIF 12============");

		// generate notification
		TypeNotificationDTO typeNotificationPayment = null;
		try {
			typeNotificationPayment = restClientNotificationService.getTypeNotification("payment");
			log.info("======== CHECK 1============");
		} catch (FeignException e) {
			log.error(e.getMessage());
			e.printStackTrace();
			log.info("======== CHECK 2============");
		} finally {

			if (typeNotificationPayment == null) {
				typeNotificationPayment = new TypeNotificationDTO(null, "payment", "Notification de paiement",
						"Notification des paiements effectués", null, "PUSH", null);
				typeNotificationPayment = restClientNotificationService.createTypeNotification(typeNotificationPayment);
				log.info("======== CHECK 3============");
			}
			NotificationDTO notificationPayment = new NotificationDTO(null,
					"Votre payment N° [" + payment.getId() + "] d'un montant de " + payment.getAmount()
							+ " effectué via " + payment.getMeansOfPayment().name()
							+ " a réussi.",
					userDTO.get().getId(), applicationName, "NONTRANSMIS", typeNotificationPayment.getId(), null);
			restClientNotificationService.createNotification(notificationPayment);
			log.info("======== CHECK 4============");
		}


		result.put("paymentId", paymentDTO2.getId());
		result.put("transactionId", res.get("transactionid"));
		result.put("paymentStatus", paymentDTO2.getStatut());
		result.put("backPayment", res);
		result.put("paymentMessageStatus", "payment successful");
		return new ResponseEntity<>(result, HttpStatus.OK);
	  }
	
	@GetMapping("/findPaymentReconciledByMeanOfPayment/{meansOfPayment}")
	public ResponseEntity<List<Payment>> findPaymentReconciledByMeanOfPayment(@PathVariable MeansOfPayment meansOfPayment){
		//implement controls here
		
		List<Payment> payments = paymentService.findByStatutAndMeansOfPayment(Statut.RECONCILED, meansOfPayment);
//		List<MeansOfPayment> AllMeans = new ArrayList<>();
//		Map<String, List<Payment>> listePaymentByMeansOfPayment = new HashMap<String, List<Payment>>();
//		
//		for (MeansOfPayment meansOfPayment : MeansOfPayment.values()) {
//			AllMeans.add(meansOfPayment);
//		}
//		
//		AllMeans.stream().forEach(meansOfPaymemnt -> listePaymentByMeansOfPayment.put(meansOfPaymemnt.name(), 
//				paymentService.findByStatutAndMeansOfPayment(Statut.RECONCILED, meansOfPaymemnt)));
		
//		return new ResponseEntity<>(payments, HttpStatus.FOUND);
//		List<Payment> payments = paymentService.findByStatutAndMeansOfPayment(Statut.RECONCILED, meanOfPayment);
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Status", HttpStatus.OK.name());
		return ResponseEntity.ok().headers(headers).body(payments);
	}
	
	@GetMapping("/summReversementByMeansOfPayment")
	public ResponseEntity<List<ResponseSumm>> summReversementByMeansOfPayment(){
		//implement controls here
		
		List<MeansOfPayment> AllMeans = new ArrayList<>();
		List<ResponseSumm> listePaymentSummByMeansOfPayment = new ArrayList<>();
		
		for (MeansOfPayment meansOfPayment : MeansOfPayment.values()) {
			AllMeans.add(meansOfPayment);
		}
		
		AllMeans.stream().forEach(meansOfPaymemnt -> 
		{
			Double amount = paymentService.summReversementByMeansOfPayment(meansOfPaymemnt);
			Double amountSend = amount != null ? amount : 0d;
			listePaymentSummByMeansOfPayment.add(new ResponseSumm(meansOfPaymemnt, amountSend));
		});
		
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Status", HttpStatus.OK.name());
		return ResponseEntity.ok().headers(headers).body(listePaymentSummByMeansOfPayment);
	}
	
	
	/**
	 * This method return the sum of payments by meansofpayment reconciled
	 * by organization with its children organizations.
	 *
	 * @param none.
	 */
	@GetMapping("/summReversementByMeansOfPaymentByOrganisation/{idOrg}")
	public ResponseEntity<List<ResponseSumm>> summReversementByMeansOfPaymentByOrganisation(@PathVariable Long idOrg){
		//implement controls here
		
		List<MeansOfPayment> AllMeans = new ArrayList<>();
		List<ResponseSumm> listePaymentSummByMeansOfPayment = new ArrayList<>();
		
		//first select all the child organization of the current org
		List<Map<String, Object>> listids = restClientOrganisationService.getOrganisationByParent(idOrg);
		
		List<Long> childids = new ArrayList<Long>(); 
		
		if (listids != null) {
			listids.stream().forEach(org -> {
				childids.add(Long.parseLong(org.get("id").toString()));
			});
		}
		// add current id parent
		childids.add(idOrg);
		
		// add all meansofpayment into a table
		for (MeansOfPayment meansOfPayment : MeansOfPayment.values()) {
			AllMeans.add(meansOfPayment);
		}
				
		//iterate by meansofpayment
		AllMeans.stream().forEach(meansOfPaymemnt -> 
		{
			Double amountSend = 0d;
			Double amount = 0d;
			
			// iteration by organization
			for (Long idorg : childids) {
				amount = paymentService.summReversementByMeansOfPaymentByOrganisation(meansOfPaymemnt, idorg);
				amountSend = amount != null ? (amountSend+amount) : 0d;
			}
			
			listePaymentSummByMeansOfPayment.add(new ResponseSumm(meansOfPaymemnt, amountSend));
		});
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Status", HttpStatus.OK.name());
		return ResponseEntity.ok().headers(headers).body(listePaymentSummByMeansOfPayment);
	}
	
	
	
	/*
	 * @GetMapping(
	 * "/summReversementByMeansOfPaymentByOrganisationByParent/{parent}") public
	 * ResponseEntity<List<ResponseSumm>>
	 * summReversementByMeansOfPaymentByOrganisationByParent(@PathVariable String
	 * parent){
	 * 
	 * //some control here HttpHeaders headers = new HttpHeaders(); if
	 * (!parent.matches("DGD|DGI")) { headers.set("Status",
	 * HttpStatus.NOT_FOUND.name()); return
	 * ResponseEntity.ok().headers(headers).body(new ArrayList<>()); }
	 * 
	 * Long idParent = null; switch (parent) { case "DGD": idParent = 4L; break;
	 * 
	 * case "DGI": idParent = 3L; break;
	 * 
	 * default: break; }
	 * 
	 * 
	 * List<MeansOfPayment> AllMeans = new ArrayList<>(); List<Long> idOrgList = new
	 * ArrayList<Long>(); List<ResponseSumm> listePaymentSummByMeansOfPayment = new
	 * ArrayList<>(); List<ResponseSumm> listePaymentSummByMeansOfPaymentFinal = new
	 * ArrayList<>();
	 * 
	 * //call ms organisation to get all organisation and retrieve all
	 * idOrganisation and construct Array of idOrganisation List<Object> listIdOrg =
	 * restClientOrganisationService.getOrganisationByParent(idParent);
	 * 
	 * if (listIdOrg != null) { listIdOrg.stream().forEach(org -> { JSONObject json
	 * = new JSONObject(org); idOrgList.add(json.getLong("id")); }); }
	 * 
	 * //parcourir le tableau des idorganisation et lancer l'action a traiter
	 * 
	 * 
	 * for (MeansOfPayment meansOfPayment : MeansOfPayment.values()) {
	 * AllMeans.add(meansOfPayment); }
	 * 
	 * 
	 * for (Long idOrg : idOrgList) {
	 * 
	 * AllMeans.stream().forEach(meansOfPaymemnt -> { Double amount =
	 * paymentService.summReversementByMeansOfPaymentByOrganisation(meansOfPaymemnt,
	 * idOrg); Double amountSend = amount != null ? amount : 0d;
	 * listePaymentSummByMeansOfPayment.add(new ResponseSumm(meansOfPaymemnt,
	 * amountSend)); }); }
	 * 
	 * for (MeansOfPayment meansOfPayment : MeansOfPayment.values()) {
	 * listePaymentSummByMeansOfPayment.stream().forEach(payment -> { Double amount
	 * = 0d; if (payment.getMeansOfPayment().equals(meansOfPayment)) { amount +=
	 * payment.getAmount(); } listePaymentSummByMeansOfPaymentFinal.add(new
	 * ResponseSumm(meansOfPayment, amount)); }); }
	 * 
	 * headers.set("Status", HttpStatus.OK.name()); return
	 * ResponseEntity.ok().headers(headers).body(
	 * listePaymentSummByMeansOfPaymentFinal); }
	 */

 }


