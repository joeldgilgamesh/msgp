package com.sprintpay.minfi.msgp.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.*;

import javax.validation.Valid;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprintpay.minfi.msgp.domain.enumeration.Nature;
import com.sprintpay.minfi.msgp.security.SecurityUtils;
import com.sprintpay.minfi.msgp.service.*;
import com.sprintpay.minfi.msgp.service.dto.*;
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

import com.sprintpay.minfi.msgp.domain.Payment;
import com.sprintpay.minfi.msgp.domain.enumeration.Statut;
import com.sprintpay.minfi.msgp.service.mapper.PaymentMapper;
import com.sprintpay.minfi.msgp.utils.RetPaiFiscalis;
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

    public PaymentResource(PaymentService paymentService, HistoriquePaymentService historiquePaymentService
        , DetailVersementIntermediaireService detailVersementIntermediaireService
        , RESTClientTransactionService restClientTransactionService
        , RESTClientEmissionService restClientEmissionService
        , PaymentSpecialServices paymentSpecialServices
        , RESTClientQuittanceService restClientQuittanceService
        , PaymentMapper paymentMapper
        , RESTClientUAAService restClientUAAService
        , RESTClientRNFService restClientRNFService,
        RESTClientOrganisationService restClientOrganisationService) {
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


//    @PreAuthorize("hasRole('AUTH_PAIEMENT_EMISSION') or hasRole('AUTH_PAIEMENT_RECETTE')")
	@PostMapping("/effectuerPaiement/{debitInfo}/{niu}/{refEmi}")
    public ResponseEntity<Map<String, Object>> effectuerPaiement(@RequestBody Map<String, Object> body
    												//, PaymentDTO paymentDTO
    												, @PathVariable String debitInfo
    												, @PathVariable String niu
    												, @PathVariable String refEmi
    												//, AddedParamsPaymentDTO addedParamsPaymentDTO
    ) {

		Map<String, Object> result = new LinkedHashMap<String, Object>();
    	Map<String, String> resultTransaction = new LinkedHashMap<String, String>();
    	Map<String, String> resultEmission = new LinkedHashMap<String, String>();
    	Object resultRecette = null;
    	Map<String, String> requestBuild = new LinkedHashMap<String, String>();

		//controle body enter
		if (body == null) {
			result.put("Reject", "Enter Datas is Null");
			return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
		}

		JSONObject bodyJson = new JSONObject(body);
		JSONObject paymentDTOJson = new JSONObject(bodyJson.get("paymentDTO").toString());
        JSONObject addedParamsPaymentDTOJson = new JSONObject(bodyJson.get("addedParamsPaymentDTO").toString());

        PaymentDTO paymentDTO = null;// (PaymentDTO) body.get("paymentDTO");
        AddedParamsPaymentDTO addedParamsPaymentDTO=null;
        try {
            paymentDTO = new ObjectMapper().readValue(paymentDTOJson.toString(), PaymentDTO.class);
            addedParamsPaymentDTO = new ObjectMapper().readValue(addedParamsPaymentDTOJson.toString(), AddedParamsPaymentDTO.class); //(AddedParamsPaymentDTO) body.get("addedParamsPaymentDTO");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            result.put("Reject", "Bad Datas Entry Of Payment");
            return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
        }

		//construct paymentDTO and addedParamsPaymentDTO
		//paymentDTO = paymentSpecialServices.constructPaymentDTO(paymentDTO, paymentDTOJson.getDouble("amount"), paymentDTOJson.getLong("idEmission"),
		//		paymentDTOJson.getLong("idOrganisation"), paymentDTOJson.getLong("idRecette"), paymentDTOJson.getString("meansOfPayment"));

		if (paymentDTO == null) {
			result.put("Reject", "Bad Datas Entry Of Payment");
			return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
		}

		String provider = paymentSpecialServices.convertProvider(paymentDTO.getMeansOfPayment().toString());

    	//controle du niu en cas des emissions
    	if (!refEmi.equals("null")) {

    		Object niuVerif = restClientUAAService.getNiuContribuablesEnregistres(niu);

        	if (niuVerif == null) {
        		result.put("Reject", "Usurpateur Voulant effectuer le paiement");
    			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    		}

        	//controle du depassement du montant a payer
        	if ((provider.equals("MOBILE_MONEY") || provider.equals("ORANGE_MONEY")) && (paymentDTO.getAmount() > 500000 || paymentDTO.getAmount() <= 0)) {
        		result.put("Reject", "Depassement de montant, le montant doit etre compris entre 0 et 500mill");
    			return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
    		}
		}

    	//controle du numero de telephone, selon le moyen de paiement
    	if ((provider.equals("MOBILE_MONEY") || provider.equals("ORANGE_MONEY") || provider.equals("EXPRESS_UNION"))
    		&& (debitInfo.isEmpty() || debitInfo == null)) {
    		result.put("Reject", "Phone Number is Required");
			return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
		}

    	//controle des données du paiement
		if(paymentDTO.getId() != null || (paymentDTO.getIdTransaction() != null) || paymentDTO.getIdDetVersId() != null
			|| ((paymentDTO.getIdEmission() == null || paymentDTO.getIdEmission() <= 0) && (paymentDTO.getIdRecette() == null || paymentDTO.getIdRecette() <= 0)))  {
			result.put("Reject", "Bad Entry");
			return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
		}

    	//complete datas paymentDTO
    	paymentDTO.setStatut(Statut.DRAFT);
        paymentDTO.setCode(UUID.randomUUID().toString());

        PaymentDTO paymentDTO2;

        //case emission
        if (!refEmi.equals("null")) {

        	resultEmission = restClientEmissionService.findRefEmission(paymentDTO.getIdEmission());

        	if(resultEmission == null) {//si l emission a payer n existe pas dans la liste des emission
        		result.put("Reject", "Emission Not Exist");
    			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        	}

        	if (resultEmission.get("refEmi") == null || resultEmission.get("refEmi").equals("")) {
        		result.put("Reject", "Emission Not Have Reference");
    			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        	}

        	if ((Double.parseDouble(resultEmission.get("amount")) - paymentDTO.getAmount()) > 0) {//si les montant ne matche pas
    			result.put("Reject", "Paiement Reject");
    			return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
    		}

        	//create emission before save payment
            EmissionDTO emissionDTO = new EmissionDTO();
        	emissionDTO.setStatus(Statut.DRAFT);
        	emissionDTO.setAmount(paymentDTO.getAmount());
        	emissionDTO.setRefEmi(refEmi.toString());
        	emissionDTO.setCodeContribuable(niu);
            emissionDTO.setNature(Nature.valueOf(resultEmission.get("type")));

            Map<String, Object> organisationDetails = restClientOrganisationService.findOrganisationByLibelleCourt(resultEmission.get("codeOrg"));
            if(!organisationDetails.isEmpty()){
                emissionDTO.setIdOrganisation((Long) organisationDetails.get("idOrganisation"));
            } else {
                if (resultEmission.get("type").equalsIgnoreCase(Nature.AVIS.name()) ||
                    resultEmission.get("type").equalsIgnoreCase(Nature.AMR.name()) ||
                    resultEmission.get("type").equalsIgnoreCase(Nature.IMPOTS.name())
                ){
                    emissionDTO.setIdOrganisation(DEFAULT_ORGANISATION_DGI_ID);
                } else {
                    emissionDTO.setIdOrganisation(DEFAULT_ORGANISATION_DGD_ID);
                }
            }

        	EmissionDTO emissionDTO2 = restClientEmissionService.createEmission(emissionDTO);

        	//complete datas payment with idEmission create, and save payment
        	paymentDTO.setIdEmission(emissionDTO2.getId());
        	paymentDTO.setIdOrganisation(emissionDTO.getIdOrganisation());
        	paymentDTO2 =  paymentService.save(paymentDTO);
        } else {//case recette non fiscale, create payment directly with idRecette in PaymentDTO entry

        	resultRecette = this.restClientRNFService.getRecettesService(paymentDTO.getIdRecette());
        	if (resultRecette != null){
        	    paymentDTO2 =  paymentService.save(paymentDTO);
        	} else {
        		result.put("Reject", "Recette Not Found");
    			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        	}
        }

    	//create historique payment
    	historiquePaymentService.saveHistPay(Statut.DRAFT.toString(), LocalDateTime.now(), paymentMapper.toEntity(paymentDTO2));

    	switch (provider) {

    	case "uba":
    		{
    			/*JSONObject addedParamsPaymentDTOJson = new JSONObject(bodyJson.get("addedParamsPaymentDTO").toString());
    			addedParamsPaymentDTO = paymentSpecialServices.constructAddedParamsPaymentDTO(addedParamsPaymentDTO, addedParamsPaymentDTOJson.getString("email"),
    					addedParamsPaymentDTOJson.getString("firstname"), addedParamsPaymentDTOJson.getString("lastname"));
                */
    			if (addedParamsPaymentDTO != null) {
    				//construct request build
    				requestBuild = paymentSpecialServices.buildRequestBankUBA(debitInfo, paymentDTO.getCode(), paymentDTO.getAmount(),
    			    		addedParamsPaymentDTO.getEmail(), addedParamsPaymentDTO.getFirstname(), addedParamsPaymentDTO.getLastname());
    			}
    			else {
    				result.put("Reject", "Bad Datas Entry Of AddedParamsPayment");
    				return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
    			}
    		}
    		break;

    	case "orangecmr": case "mtncmr": case "yup": requestBuild = paymentSpecialServices.buildRequest(debitInfo, paymentDTO.getAmount(),
    			paymentDTO.getMeansOfPayment().toString(), paymentDTO.getCode());
    	break;

    	case "afrilandcmr": requestBuild = paymentSpecialServices.buildRequestBank(debitInfo, paymentDTO.getCode(),
    			niu, "", paymentDTO.getAmount(), refEmi.toString());
    	break;

    	default:
    		break;
    			}

    	resultTransaction = restClientTransactionService.getTransaction(paymentSpecialServices.convertProvider(paymentDTO.getMeansOfPayment().toString()),
    			requestBuild);

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
		EmissionDTO emissionDTO = null;
		RetPaiFiscalis[] retourPaiFiscalis = null;
//		TransactionDTO transaction = new TransactionDTO();

		//we accept status code equal <100> or <400>
		if (!status_code.equals("400") && !status_code.equals("100")) return new ResponseEntity<>(resultat = "status code Reject", HttpStatus.NOT_ACCEPTABLE);

		if (status_code.equals("100")) {//Payment Sucessfull
			status = Statut.VALIDATED;
		}
		else if (status_code.equals("400")) {//Payment Failed
			status = Statut.CANCEL;
		}

		//create transaction
//    	transactionService.save(transactionDTO);
//    	transaction = transactionService.save(transactionDTO);


    	//find payment by codePaiement and update status
    	payment = paymentService.findByCode(codePaiement);
        Optional<UserDTO> userDTO = restClientUAAService.searchUser(payment.getCreatedBy());
    	if (payment == null) return new ResponseEntity<>(resultat = "Payment Not Exist", HttpStatus.NOT_ACCEPTABLE);

    	paymentService.update(payment.getId(), status, transactionDTO);
    	historiquePaymentService.saveHistPay(status.toString(), transactionDTO.getDate(), payment);
        log.info("========// "+payment+" //============");
    	//TODO UPDATE THIS SECTION
        //detail organisation
        Map<String, Object> organisationDetails = new HashMap<String, Object>();
        Map<String, Object> recetteServiceDetails = new HashMap<String, Object>();
    	if (status_code.equals("100")) {
    	    //ici on génère le reçu en cas de paiement réussi
    		//appel du service de retour paiement pour construire l objet imputation
    		//url a lancer -> http://teledeclaration-dgi.cm:1020/api/paiement dans le service retour paiement
    		//et le retour paiement doit me retourner les imputations a envoyer a quittance
    		//si msquittance n est pas actif, penser a service de gestion des file d attente des recu et quittance
    		//coesa quand msquittance demarre il vient lire les recu et quittance en attente et il les genere

            //en cas de paiement d une emission on met a jour le statut de l emission
            if (payment.getIdEmission() != null && payment.getIdEmission() > 0) {
                //update emission status
                retourPaiFiscalis = restClientEmissionService.updateEmission(payment.getIdEmission(), status).getBody();

                //create historique emission
                restClientEmissionService.createEmissionHistorique(new EmissionHistoriqueDTO(), status.toString(), payment.getIdEmission());

                emissionDTO = restClientEmissionService.getEmission(payment.getIdEmission());
            }
            log.info("======== JUSTIF 1============");
            //update recette service
            if (payment.getIdRecette() != null && payment.getIdRecette() > 0){

                restClientRNFService.payerRecettesService(payment.getIdRecette(), payment.getId());
            }

            if (emissionDTO == null && payment.getIdRecette() == null && payment.getIdRecette() < 0) return new ResponseEntity<>(resultat = "Emission Not Exist", HttpStatus.NOT_FOUND);

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
                organisationDetails = restClientOrganisationService.findOrganisationById(emissionDTO.getIdOrganisation());
                log.info("======== JUSTIF 4============");
                if(retourPaiFiscalis != null) {
                    for (int i = 0; i < retourPaiFiscalis.length; i++) {
//	    	    	imputationDTO.setMontant(Double.parseDouble(retourPaiFiscalis[i].getMontant()));

                        imputationDTO.setMontant(Double.valueOf(retourPaiFiscalis[i].getMontant_imputation()));
                        imputationDTO.setNumDeclarationImputation(Long.valueOf(retourPaiFiscalis[i].getNumerodeclaration()));
                        imputationDTO.setOperation(retourPaiFiscalis[i].getNumeropaiement());
                        imputationDTO.setNatrureDesDroits(retourPaiFiscalis[i].getLibelle_imputation());
                        listImput.add(imputationDTO);
                    }
                }else{
                    imputationDTO.setMontant(payment.getAmount());
                    imputationDTO.setNumDeclarationImputation(emissionDTO.getId());
                    imputationDTO.setOperation(String.valueOf(payment.getId()));
                    imputationDTO.setNatrureDesDroits(emissionDTO.getNature().name()+" N° "+emissionDTO.getRefEmi());
                    listImput.add(imputationDTO);
                }
                log.info("======== JUSTIF 5============");
	    		justificatifPaiementDTO.setNui(emissionDTO.getCodeContribuable());
	    		justificatifPaiementDTO.setIdOrganisation(Long.valueOf((Integer) organisationDetails.get("idOrganisation")));
	    		justificatifPaiementDTO.setNatureRecette(emissionDTO.getRefEmi());
                log.info("======== JUSTIF 6============");
	    	}

	    	if (payment.getIdRecette() != null && payment.getIdRecette() > 0) {//normalement ceci correspond à emissionDTO == null
                organisationDetails = restClientOrganisationService.findOrganisationById(payment.getIdOrganisation());
                recetteServiceDetails = restClientRNFService.getResumeRecettesService(payment.getIdRecette());
	    		justificatifPaiementDTO.setIdOrganisation(payment.getIdOrganisation());

	    		justificatifPaiementDTO.setNui(userDTO.get().getNumeroContrubuable()); //a enlever
                justificatifPaiementDTO.setNatureRecette((String) recetteServiceDetails.get("nature")); //comment recuperer ceci
	    		imputationDTO.setMontant(payment.getAmount());
    	    	imputationDTO.setNumDeclarationImputation(1L);
    	    	imputationDTO.setOperation(String.valueOf(payment.getId()));
    	    	imputationDTO.setNatrureDesDroits((String) recetteServiceDetails.get("nature"));
    	    	listImput.add(imputationDTO);
	    	}

	    	justificatifPaiementDTO.setTypePaiement(payment.getMeansOfPayment().name());
	    	justificatifPaiementDTO.setTypeJustificatifPaiement("RECU");
	    	justificatifPaiementDTO.setCode(payment.getCode());
            log.info("======== JUSTIF 7============");
            if(userDTO.get().getFirstName() == null){
                userDTO.get().setFirstName("");
            }
            if(userDTO.get().getLastName() == null){
                userDTO.get().setLastName("");
            }
            log.info("======== JUSTIF 8============");
	    	justificatifPaiementDTO.setNomPrenomClient(userDTO.get().getFirstName() + " " + userDTO.get().getLastName()); //comment recuperer ceci
	    	justificatifPaiementDTO.setNomOrganisation((String) organisationDetails.get("nomOrganisation")); //comment recuperer ceci
	    	justificatifPaiementDTO.setCodeOrganisation((String) organisationDetails.get("codeOrg")); //comment recuperer ceci
	    	justificatifPaiementDTO.setRaisonSociale(userDTO.get().getRaisonSocialeEntreprise()); //comment recuperer ceci
	    	justificatifPaiementDTO.setSigle(""); //comment recuperer ceci
	    	justificatifPaiementDTO.setCodePoste(1L); //comment recuperer ceci
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
		}
        log.info("======== JUSTIF 13============");
    	return new ResponseEntity<>(resultat, HttpStatus.OK);

    }


//    @PostMapping("/reconcilierPaiement/{codeVersement}/{montant}")
//    public ResponseEntity<String> reconcilierPaiement(@RequestBody List<PaymentDTO> paymentDTOList
//    												  , @PathVariable String codeVersement
//    												  , @PathVariable double montant) {
//
//    	String resultat = "RECONCILED Succes";
//    	double reelmontant = 0;
//
//    	//calcul du montant des paiements a reconcilier
//    	for (PaymentDTO paymentDTO2 : paymentDTOList) {
//			reelmontant += paymentDTO2.getAmount();
//		}
//
//    	if (codeVersement == null || montant == 0 || paymentDTOList == null || (reelmontant - montant) > 0) return new ResponseEntity<>(resultat = "Bad Entry", HttpStatus.BAD_REQUEST);
//
//    	//appel du service verifier detail versement
//        Optional<DetailVersementIntermediaireDTO> det = detailVersementIntermediaireService.findByNumeroVersment(codeVersement);
//
//    	if (det == null) return new ResponseEntity<>(resultat = "Failed", HttpStatus.NOT_FOUND);
//
//    	if (det.get().getNumeroVersment().isEmpty()) return new ResponseEntity<>(resultat = "codeVersement Not Exist", HttpStatus.NOT_FOUND);
//
//    	Statut status = null;
//
//    	//appel du service de comparaisons des données des paiements des deux cotés
//    	if (!detailVersementIntermediaireService.comparerDonnReconcil(det.get().getMontant(), montant)) {//si montant different, echec reconciliation
//    		status = Statut.CANCEL;
//    		resultat = "Failed RECONCILED, Amount not mapping";
//        	//creer historique a etat reconcilied
//
//		}
//
//    	else status = Statut.RECONCILED;  //si montant egaux, alors succès reconciliation
//
//    	for (PaymentDTO paymentDTO : paymentDTOList) {
//    		paymentService.update(paymentDTO.getId(), status);
//        	historiquePaymentService.saveHistPay(status.toString(), LocalDateTime.now(), paymentMapper.toEntity(paymentDTO));
//		}
//
//    	if (detailVersementIntermediaireService.comparerDonnReconcil(det.get().getMontant(), montant)) {
//    		//appel du endpoint generer quittance (existe deja, mais à distance) en envoyant l objet payment pour construire la quittance
//        	//appel du endpoint notification pour renseigner sur l etat de la reconciliation
//        	//appel du endpoint update emission, en testant dabord quil sagit du paiement dune emission
//    	}
//
//    	return new ResponseEntity<>(resultat, HttpStatus.OK);
//
//    }


    @GetMapping("/literPaymentByStatut/{statut}")
    public ResponseEntity<List<Object>> literPaymentByStatut(@PathVariable Statut statut, Pageable pageable) {

    	Page<Object> pageresult = paymentService.findByStatut(statut, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), pageresult);
        return new ResponseEntity<>(pageresult.getContent(), headers, HttpStatus.OK);
    }

	@GetMapping("/literPaymentEmissionContrib/{niu}")
    public ResponseEntity<List<Payment>> literPaymentEmissionContrib(@PathVariable String niu) {

    	//Get All Id of Emission already payed
    	List<String> emissionIdList = restClientEmissionService.getEmissionsContri(niu);
    	List<Payment> paymentList = new ArrayList<>();

    	//Get All Payment where Id_Emission equals Id in emissionIdList
    	if (emissionIdList != null) {
    		for (String idEmis : emissionIdList) {
    			paymentList.add(paymentService.findByIdEmission(Long.parseLong(idEmis)));
    		}
		}

        return new ResponseEntity<>(paymentList, HttpStatus.OK);
    }


    /**
     *
     * @param option valeurs possibles [all, emissions, rnf]
     * @param pageable
     * @return
     */
    @GetMapping("/listPaymentUser/{option}")
    public ResponseEntity<List<Payment>> literPaymentByStatut(@PathVariable String option, Pageable pageable) {
        String username = SecurityUtils.getCurrentUserLogin().get();
        Page<Payment> pageresult = null;
        if(option.equalsIgnoreCase("all")){
            pageresult = paymentService.findAllByCreatedBy(username, pageable);
        } else if (option.equalsIgnoreCase("emissions")){
            pageresult = paymentService.findEmissionByCreatedBy(username, pageable);
        }else if (option.equalsIgnoreCase("rnf")){
            pageresult = paymentService.findRNFByCreatedBy(username, pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), pageresult);
        return new ResponseEntity<>(pageresult.getContent(), headers, HttpStatus.OK);
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
