package com.sprintpay.minfi.msgp.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.sprintpay.minfi.msgp.domain.DetailVersementIntermediaire;
import com.sprintpay.minfi.msgp.domain.Payment;
import com.sprintpay.minfi.msgp.domain.enumeration.MeansOfPayment;
import com.sprintpay.minfi.msgp.domain.enumeration.Statut;
import com.sprintpay.minfi.msgp.repository.PaymentRepository;
import com.sprintpay.minfi.msgp.service.PaymentService;
import com.sprintpay.minfi.msgp.service.RESTClientEmissionService;
import com.sprintpay.minfi.msgp.service.RESTClientNotificationService;
import com.sprintpay.minfi.msgp.service.RESTClientOrganisationService;
import com.sprintpay.minfi.msgp.service.RESTClientUAAService;
import com.sprintpay.minfi.msgp.service.dto.NotificationDTO;
import com.sprintpay.minfi.msgp.service.dto.PaymentDTO;
import com.sprintpay.minfi.msgp.service.dto.TransactionDTO;
import com.sprintpay.minfi.msgp.service.dto.TypeNotificationDTO;
import com.sprintpay.minfi.msgp.service.dto.UserDTO;
import com.sprintpay.minfi.msgp.service.mapper.PaymentMapper;

import feign.FeignException;

/**
 * Service Implementation for managing {@link Payment}.
 */
@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {
	
	@Value("${jhipster.clientApp.name}")
	private String applicationName;

    private final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    @Autowired
    private RESTClientOrganisationService restClientOrganisationService;
    private final RESTClientEmissionService restClientEmissionService;
    private final RESTClientUAAService restClientUAAService;
    private final RESTClientNotificationService restClientNotificationService;
    
    private final KafkaTemplate<String, NotificationDTO> kafkaTemplate;
    
    @Value("${kafka.servers.topic.notification}")
    private String topic ;

    public PaymentServiceImpl(PaymentRepository paymentRepository, PaymentMapper paymentMapper, 
    		RESTClientEmissionService restClientEmissionService, KafkaTemplate<String, NotificationDTO> kafkaTemplate,
    		RESTClientUAAService restClientUAAService, RESTClientNotificationService restClientNotificationService) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.restClientEmissionService = restClientEmissionService;
        this.restClientUAAService = restClientUAAService;
        this.restClientNotificationService = restClientNotificationService;
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Save a payment.
     *
     * @param paymentDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public PaymentDTO save(PaymentDTO paymentDTO) {
        log.debug("Request to save Payment : {}", paymentDTO);
        Payment payment = paymentMapper.toEntity(paymentDTO);
        payment = paymentRepository.save(payment);
        return paymentMapper.toDto(payment);
    }

    /**
     * Get all the payments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PaymentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Payments");
        return paymentRepository.findAll(pageable)
            .map(paymentMapper::toDto);
    }

    /**
     * Get one payment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentDTO> findOne(Long id) {
        log.debug("Request to get Payment : {}", id);
        return paymentRepository.findById(id)
            .map(paymentMapper::toDto);
    }

    /**
     * Delete the payment by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Payment : {}", id);
        paymentRepository.deleteById(id);
    }

    /**
     * Update the payment by id and state.
     * @param1 id the id of the entity.
     * @param2 the state to set to update Payment.
     */
	@Override
	public void update(Long idPaymeLong, Statut state) {
		// TODO Auto-generated method stub
		Optional<Payment> op = paymentRepository.findById(idPaymeLong);
		Payment p = new Payment();
		if (op.isPresent()) { 
			p = op.get();
			p.setStatut(state);
			paymentRepository.saveAndFlush(p);
		}
		//paymentRepository.updatePayment(idPaymeLong, state);
	}

	/**
     * Update the payment by id and state.
     * @param1 id the id of the entity.
     * @param2 the state to set to update Payment.
     */
	@Override
	public void update(Long idPaymeLong, Statut state, Long idTransaction, String refTransaction) {
		// TODO Auto-generated method stub
		Optional<Payment> op = paymentRepository.findById(idPaymeLong);
		Payment p = op.get();
		p.setStatut(state);
		p.setIdTransaction(idTransaction);
		p.setRefTransaction(refTransaction);
		paymentRepository.saveAndFlush(p);
		// paymentRepository.updatePayment(idPaymeLong, state, idTransaction, refTransaction);
	}

	@Override
	public Payment findByIdTransation(Long Id) {
		// TODO Auto-generated method stub
		return paymentRepository.findByIdTransaction(Id);
	}

	@Override
	public Payment findByCode(String code) {
		// TODO Auto-generated method stub
		return paymentRepository.findByCode(code);
	}

	@Override
	public Page<Object> findByStatut(Statut status, Pageable pageable) {
		// TODO Auto-generated method stub
		return paymentRepository.findByPaymentValidated(status, pageable);
	}

	@Override
	public Payment findByIdTransaction(Long id) {
		// TODO Auto-generated method stub
		return paymentRepository.findByIdTransaction(id);
	}

	@Override
	public Payment findByRefTransaction(String refTransaction) {
		// TODO Auto-generated method stub
		return paymentRepository.findByRefTransaction(refTransaction);
	}

	@Override
	public Payment findByIdEmission(Long idEmis) {
		// TODO Auto-generated method stub
		return paymentRepository.findByIdEmission(idEmis);
	}

	@Override
    public List<Payment> findByRefTransactionInAndStatut(Set<String> refs, Statut statut){
	    return paymentRepository.findByRefTransactionInAndStatut(refs, statut);
    }

    @Override
    public void updateAllPayments(Set<String> refs, Statut statut, DetailVersementIntermediaire dt){
    	List<Payment> ls = paymentRepository.findByRefTransactionIn(refs);
    	for (Payment payment : ls) {
			payment.setStatut(statut);
			payment.setIdDetVers(dt);
		}
    	paymentRepository.saveAll(ls);
        // paymentRepository.updateAllPayments(refs, statut);
    }

	@Override
	public void update(Long id, Statut status, TransactionDTO transactionDTO) {
		// TODO Auto-generated method stub
		Payment p = paymentRepository.findById(id).get();
		p.setStatut(status);
		p.setIdTransaction(transactionDTO.getId());
		p.setRefTransaction(transactionDTO.getCodeTransaction());
		
		//paymentRepository.updatePaymentWithTransaction(id, status, transactionDTO.getId(), transactionDTO.getCodeTransaction());
	}

    @Override
    public Page<Payment> findAllByCreatedBy(String username, Pageable pageable) {
        return paymentRepository.findAllByCreatedBy(username, pageable);
    }

    @Override
    public Page<Payment> findEmissionByCreatedBy(String username, Pageable pageable) {
        return paymentRepository.findEmissionByCreatedBy(username, pageable);
    }

    @Override
    public Page<Payment> findRNFByCreatedBy(String username, Pageable pageable) {
        return paymentRepository.findRNFByCreatedBy(username, pageable);
    }

	@Override
	public List<Payment> findByStatutAndMeansOfPayment(Statut status, MeansOfPayment MeansOfPayment) {
		// TODO Auto-generated method stub
		return paymentRepository.findByStatutANDMeansOfPayment(status, MeansOfPayment);
	}

	@Override
	public Double summReversementByMeansOfPayment(MeansOfPayment meansOfPayment) {
		// TODO Auto-generated method stub
		Double value = paymentRepository.summReversementByMeansOfPayment(meansOfPayment);
		Double resp = (value != null ? value : 0d);
		return resp;
	}

	@Override
	public Double summReversementByMeansOfPaymentByOrganisation(MeansOfPayment meansOfPaymemnt, Long idOrg) {
		// TODO Auto-generated method stub
		Double value = paymentRepository.summReversementByMeansOfPaymentByOrganisation(meansOfPaymemnt, idOrg);
		Double resp = (value != null ? value : 0d);
		return resp;
	}

//	@Scheduled(fixedDelay = 6000)
//	public void testEndpoint() {
//		
//		try {
//			List<Object> organisations = restClientOrganisationService.getOrganisationByParent(4L);
//			System.out.println("************************* liste des organisations ******************************");
//			System.out.println(restClientOrganisationService.getOrganisationByParent(4L));
//		} catch (HystrixRuntimeException e) {
//			// TODO: handle exception
//			System.out.println("************************* liste des organisations ******************************");
//			System.out.println(restClientOrganisationService.getOrganisationByParent(4L));
//		}
//		
//		
//	}
	
	/**
	 * This method notify the contribuable when its less or equal to 2 days before the Duedate.
	 *
	 * @param none.
	 */
	@Scheduled(cron = "* 30 12 * * ?") // everyday at noon 12pm
	public void paymentDuedateNotif() {
		
		// first we get the list of all emissons temp created less than one month from db
		 List<Map<String, Object>> emissionsTemps = restClientEmissionService.getAllEmissionTemps();
		 
		 System.out.println("\n==============> Reading list..");
		 
		 if(emissionsTemps!= null) {
			 
		 
	        for (Map<String, Object> emission : emissionsTemps) {
	        	
	        	//get the details for notification
	        	String num = null;
	        	String amount = null;
	        	String niu = null;
	        	if(emission.get("refDeclaration")!=null)
	        		num = emission.get("refDeclaration").toString();
	        	else if(emission.get("numeroImposition")!=null)
	        		num = emission.get("numeroImposition").toString();
	        	
	        	if(emission.get("reste")!=null)
	        		amount = emission.get("reste").toString();
	        	
	        	niu = emission.get("niu").toString();
	        	
	        	//check for current user details with his niu from emission
	        	Optional<UserDTO> user = restClientUAAService.getNiuContribuablesEnregistres(niu);
	        	log.info("\n==============> NIU.."+niu);
	        	log.info("\n==============> USER.."+user.get());
	        	
	        	if(user != null && !user.isEmpty()) {
	        		Long userid = user.get().getId();
	        		log.info("\n==============> USER.."+user.get());
		        	//Si date liquidation non vide
	        		
		            if(emission.get("dateLiquidation") != null) {
		            	
		            	Date today = new Date();
		            	String format = "MM/dd/yyyy HH:mm:ss";
		            	SimpleDateFormat sdf = new SimpleDateFormat(format);
		            	
		            	String  date = converDate(emission.get("dateLiquidation").toString());
			        	Date duedate = null;
		                
						try {
							duedate = sdf.parse(date);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						log.info("\n==============> DUEDATE.."+duedate);
		            	long diff = today.getTime() - duedate.getTime();
		            	int diffDays = (int) (diff / (24 * 60 * 60 * 1000));
		            	
		            	// if the difference is less than or equal to 2 days
		                if(diffDays<=2){
		                	
		                	// generate notification
		        			TypeNotificationDTO typeNotificationPayment = null;
		        			try {
		        				typeNotificationPayment = restClientNotificationService.getTypeNotification("alerte");
		        				
		        			} catch (FeignException e) {
		        				log.error(e.getMessage());
		        				e.printStackTrace();
		        				
		        			} finally {
	
		        				if (typeNotificationPayment == null) {
		        					typeNotificationPayment = new TypeNotificationDTO(null, "alerte", "Notification de paiement",
		        							"Alertes des paiements à effectuer", null, "EMAIL", null);
		        					typeNotificationPayment = restClientNotificationService.createTypeNotification(typeNotificationPayment);
		        					
		        				}
		        				NotificationDTO notificationPayment = new NotificationDTO(null,
		        						"Votre facture fiscale N° [" +num + "] d'un montant de " + amount
		        								+ " est déjà disponible."
		        								+ " Bien vouloir penser à  la solder.",
		        						userid, applicationName, "NONTRANSMIS", typeNotificationPayment.getId(), null);
		        				//restClientNotificationService.createNotification(notificationPayment);
		                        kafkaTemplate.send(topic,applicationName+ LocalDateTime.now(),notificationPayment);
		                        log.info("Notification créé et transmit au broker {}", notificationPayment);
		        				log.info("======== CHECK 4============");
		        			}
		                	
		                }
		            }
	        	}    
	        }
		 } 
	}

	private String converDate(String object) {
		
	    String year = ""+object.charAt(0)+ object.charAt(1)+object.charAt(2)+object.charAt(3);
	    String month = ""+object.charAt(4)+ object.charAt(5);
	    String day = ""+object.charAt(6)+ object.charAt(7);
	    
	    String date = month+"/"+day+"/"+year+" 00:00:00";
	   
		return date;
	}

}
