package com.sprint.minfi.msgp.web.rest;

import static com.sprint.minfi.msgp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import com.sprint.minfi.msgp.SpminfimsgpApp;
import com.sprint.minfi.msgp.config.SecurityBeanOverrideConfiguration;
import com.sprint.minfi.msgp.domain.HistoriquePayment;
import com.sprint.minfi.msgp.domain.Payment;
import com.sprint.minfi.msgp.domain.Transaction;
import com.sprint.minfi.msgp.domain.enumeration.MeansOfPayment;
import com.sprint.minfi.msgp.domain.enumeration.Statut;
import com.sprint.minfi.msgp.repository.HistoriquePaymentRepository;
import com.sprint.minfi.msgp.repository.PaymentRepository;
import com.sprint.minfi.msgp.repository.TransactionRepository;
import com.sprint.minfi.msgp.service.DetailVersementIntermediaireService;
import com.sprint.minfi.msgp.service.HistoriquePaymentService;
import com.sprint.minfi.msgp.service.PaymentService;
import com.sprint.minfi.msgp.service.PaymentSpecialServices;
import com.sprint.minfi.msgp.service.RESTClientEmissionService;
import com.sprint.minfi.msgp.service.RESTClientQuittanceService;
import com.sprint.minfi.msgp.service.RESTClientTransactionService;
import com.sprint.minfi.msgp.service.TransactionService;
import com.sprint.minfi.msgp.service.dto.PaymentDTO;
import com.sprint.minfi.msgp.service.dto.TransactionDTO;
import com.sprint.minfi.msgp.service.mapper.PaymentMapper;
import com.sprint.minfi.msgp.service.mapper.TransactionMapper;
import com.sprint.minfi.msgp.web.rest.errors.ExceptionTranslator;
/**
 * Integration tests for the {@link PaymentResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, SpminfimsgpApp.class})
public class PaymentResourceIT {

    private static final String DEFAULT_CODE = "code_01";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final MeansOfPayment DEFAULT_MEANS_OF_PAYMENT = MeansOfPayment.MOBILE_MONEY;
    private static final MeansOfPayment UPDATED_MEANS_OF_PAYMENT = MeansOfPayment.ORANGE_MONEY;

    private static final Statut DEFAULT_STATUT = Statut.DRAFT;
    private static final Statut UPDATED_STATUT = Statut.PENDING;

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final Long DEFAULT_ID_EMISSION = 1L;
    private static final Long UPDATED_ID_EMISSION = 2L;

    private static final Long DEFAULT_ID_RECETTE = 1L;
    private static final Long UPDATED_ID_RECETTE = 2L;

    private static final Long DEFAULT_ID_ORGANISATION = 1L;
    private static final Long UPDATED_ID_ORGANISATION = 2L;
    
    private static final String DEFAULT_DEBIT_INFO = "657826658";

    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private HistoriquePaymentRepository historiquePaymentRepo;
    
    @Autowired
    private TransactionRepository transactionRepo;

    @Autowired
    private PaymentMapper paymentMapper;
    
    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private HistoriquePaymentService historiquePaymentService;
    
    @Autowired
    private TransactionService transactionService;
    
    @Autowired
    private DetailVersementIntermediaireService detailVersementIntermediaireService;
    
    @MockBean
    private RESTClientTransactionService restClientTransactionService;
    
    @MockBean
    private RESTClientEmissionService restClientEmissionService; 
    
    @MockBean
    private RESTClientQuittanceService restClientQuittanceService;
    
    @MockBean
    private PaymentSpecialServices paymentSpecialServices;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restPaymentMockMvc;

    private Payment payment;
    
    private HistoriquePayment historique;
    
    private Transaction transaction;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PaymentResource paymentResource = new PaymentResource(paymentService, historiquePaymentService, transactionService, 
        															detailVersementIntermediaireService, restClientTransactionService, 
        															restClientEmissionService, paymentSpecialServices, restClientQuittanceService);
        this.restPaymentMockMvc = MockMvcBuilders.standaloneSetup(paymentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payment createEntity(EntityManager em) {
        Payment payment = new Payment()
            .code(DEFAULT_CODE)
            .meansOfPayment(DEFAULT_MEANS_OF_PAYMENT)
            .statut(DEFAULT_STATUT)
            .amount(DEFAULT_AMOUNT)
            .idEmission(DEFAULT_ID_EMISSION)
            .idRecette(DEFAULT_ID_RECETTE)
            .idOrganisation(DEFAULT_ID_ORGANISATION);
        return payment;
    }
    
    /**
     * create entity historique for test.
     * @param em
     * @return
     */
    public static HistoriquePayment createEntityHistorique(EntityManager em) {
    	HistoriquePayment historique = new HistoriquePayment()
    			.dateStatus(LocalDateTime.now())
    			.status(DEFAULT_STATUT.toString());
    	return historique;
    }
    
    /**
     * create entity transaction for test
     * @param em
     * @return
     */
    public static Transaction createEntityTransaction(EntityManager em) {
    	Transaction transaction = new Transaction()
    			.codeTransaction(DEFAULT_CODE)
    			.telephone("651826658")
    			.date(LocalDateTime.now())
    			.msg("transaction failed");
    	return transaction;
    }
    
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payment createUpdatedEntity(EntityManager em) {
        Payment payment = new Payment()
            .code(UPDATED_CODE)
            .meansOfPayment(UPDATED_MEANS_OF_PAYMENT)
            .statut(UPDATED_STATUT)
            .amount(UPDATED_AMOUNT)
            .idEmission(UPDATED_ID_EMISSION)
            .idRecette(UPDATED_ID_RECETTE)
            .idOrganisation(UPDATED_ID_ORGANISATION);
        return payment;
    }

    @BeforeEach
    public void initTest() {
        payment = createEntity(em);
        historique = createEntityHistorique(em);
        transaction = createEntityTransaction(em);
    }

    @Test
    @Transactional
    public void createPayment() throws Exception {
        int databaseSizeBeforeCreate = paymentRepository.findAll().size();

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);
        restPaymentMockMvc.perform(post("/api/payments")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isCreated());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeCreate + 1);
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testPayment.getMeansOfPayment()).isEqualTo(DEFAULT_MEANS_OF_PAYMENT);
        assertThat(testPayment.getStatut()).isEqualTo(DEFAULT_STATUT);
        assertThat(testPayment.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testPayment.getIdEmission()).isEqualTo(DEFAULT_ID_EMISSION);
        assertThat(testPayment.getIdRecette()).isEqualTo(DEFAULT_ID_RECETTE);
        assertThat(testPayment.getIdOrganisation()).isEqualTo(DEFAULT_ID_ORGANISATION);
    }

    @Test
    @Transactional
    public void createPaymentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = paymentRepository.findAll().size();

        // Create the Payment with an existing ID
        payment.setId(1L);
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentMockMvc.perform(post("/api/payments")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentRepository.findAll().size();
        // set the field null
        payment.setCode(null);

        // Create the Payment, which fails.
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        restPaymentMockMvc.perform(post("/api/payments")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPayments() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList
        restPaymentMockMvc.perform(get("/api/payments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payment.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].meansOfPayment").value(hasItem(DEFAULT_MEANS_OF_PAYMENT.toString())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].idEmission").value(hasItem(DEFAULT_ID_EMISSION.intValue())))
            .andExpect(jsonPath("$.[*].idRecette").value(hasItem(DEFAULT_ID_RECETTE.intValue())))
            .andExpect(jsonPath("$.[*].idOrganisation").value(hasItem(DEFAULT_ID_ORGANISATION.intValue())));
    }
    
    @Test
    @Transactional
    public void getPayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get the payment
        restPaymentMockMvc.perform(get("/api/payments/{id}", payment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(payment.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.meansOfPayment").value(DEFAULT_MEANS_OF_PAYMENT.toString()))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.idEmission").value(DEFAULT_ID_EMISSION.intValue()))
            .andExpect(jsonPath("$.idRecette").value(DEFAULT_ID_RECETTE.intValue()))
            .andExpect(jsonPath("$.idOrganisation").value(DEFAULT_ID_ORGANISATION.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPayment() throws Exception {
        // Get the payment
        restPaymentMockMvc.perform(get("/api/payments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();

        // Update the payment
        Payment updatedPayment = paymentRepository.findById(payment.getId()).get();
        // Disconnect from session so that the updates on updatedPayment are not directly saved in db
        em.detach(updatedPayment);
        updatedPayment
            .code(UPDATED_CODE)
            .meansOfPayment(UPDATED_MEANS_OF_PAYMENT)
            .statut(UPDATED_STATUT)
            .amount(UPDATED_AMOUNT)
            .idEmission(UPDATED_ID_EMISSION)
            .idRecette(UPDATED_ID_RECETTE)
            .idOrganisation(UPDATED_ID_ORGANISATION);
        PaymentDTO paymentDTO = paymentMapper.toDto(updatedPayment);

        restPaymentMockMvc.perform(put("/api/payments")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isOk());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPayment.getMeansOfPayment()).isEqualTo(UPDATED_MEANS_OF_PAYMENT);
        assertThat(testPayment.getStatut()).isEqualTo(UPDATED_STATUT);
        assertThat(testPayment.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPayment.getIdEmission()).isEqualTo(UPDATED_ID_EMISSION);
        assertThat(testPayment.getIdRecette()).isEqualTo(UPDATED_ID_RECETTE);
        assertThat(testPayment.getIdOrganisation()).isEqualTo(UPDATED_ID_ORGANISATION);
    }

    @Test
    @Transactional
    public void updateNonExistingPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentMockMvc.perform(put("/api/payments")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        int databaseSizeBeforeDelete = paymentRepository.findAll().size();

        // Delete the payment
        restPaymentMockMvc.perform(delete("/api/payments/{id}", payment.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeDelete - 1);
    }
    
    @Test
    @Transactional
    public void effectuerPayment() throws Exception {
    	//initialize database
    	paymentRepository.saveAndFlush(payment);
    	
        // effectuer Payment en mode test
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);
        paymentDTO.setCode(DEFAULT_CODE);
        restPaymentMockMvc.perform(post("/api/effectuerPaiement/{debitInfo}", transaction.getTelephone())
        .contentType(TestUtil.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
        .andExpect(status().isOk());


    }
    
    @Test
    @Transactional
    public void callbackTransaction() throws Exception {
    	//initialize database
    	transactionRepo.saveAndFlush(transaction);
    	
        // callbackTransaction en mode test
    	TransactionDTO transactionDTO = transactionMapper.toDto(transaction);
        restPaymentMockMvc.perform(post("/api/callbackTransaction/{codePaiement}", transactionDTO.getCodeTransaction())
        		.contentType(TestUtil.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(transactionDTO)))
         	    .andExpect(status().isBadRequest());

    }
    
    @Test
    @Transactional
    public void reconcilierPaiement() throws Exception {
    	//initialize database
    	paymentRepository.saveAndFlush(payment);
    	
    	//reconcilier Paiement en mode test
    	PaymentDTO paymentDTO = paymentMapper.toDto(payment);
    	restPaymentMockMvc.perform(post("/api/reconcilierPaiement/{codeVersement}/{montant}", DEFAULT_CODE, DEFAULT_AMOUNT)
    			.contentType(TestUtil.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
    			.andExpect(status().isBadRequest());
    	
    }
    
}
