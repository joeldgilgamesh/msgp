package com.sprintpay.minfi.msgp.web.rest;

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
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import com.sprintpay.minfi.msgp.config.ApplicationProperties;
import com.sprintpay.minfi.msgp.config.SecurityBeanOverrideConfiguration;
import com.sprintpay.minfi.msgp.domain.Payment;
import com.sprintpay.minfi.msgp.service.PaymentService;
import com.sprintpay.minfi.msgp.service.RESTClientNotificationService;
import com.sprintpay.minfi.msgp.service.RESTClientQuittanceService;
import com.sprintpay.minfi.msgp.service.RESTClientSystacSygmaService;
import com.sprintpay.minfi.msgp.service.RESTClientUAAService;

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

import com.sprintpay.minfi.msgp.SpminfimsgpApp;
import com.sprintpay.minfi.msgp.domain.DetailVersementIntermediaire;
import com.sprintpay.minfi.msgp.repository.DetailVersementIntermediaireRepository;
import com.sprintpay.minfi.msgp.service.DetailVersementIntermediaireService;
import com.sprintpay.minfi.msgp.service.dto.DetailVersementIntermediaireDTO;
import com.sprintpay.minfi.msgp.service.mapper.DetailVersementIntermediaireMapper;
import com.sprintpay.minfi.msgp.web.rest.errors.ExceptionTranslator;

/**
 * Integration tests for the {@link DetailVersementIntermediaireResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, SpminfimsgpApp.class})
public class DetailVersementIntermediaireResourceIT {

    private static final String DEFAULT_NUMERO_VERSMENT = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_VERSMENT = "BBBBBBBBBB";

    private static final LocalDateTime DEFAULT_DATE = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
    private static final LocalDateTime UPDATED_DATE = LocalDateTime.now(ZoneId.systemDefault());

    private static final Double DEFAULT_MONTANT = 1D;
    private static final Double UPDATED_MONTANT = 2D;

    @Autowired
    private DetailVersementIntermediaireRepository detailVersementIntermediaireRepository;

    @Autowired
    private DetailVersementIntermediaireMapper detailVersementIntermediaireMapper;

    @Autowired
    private DetailVersementIntermediaireService detailVersementIntermediaireService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private RESTClientSystacSygmaService restClientSystacSygmaService;
    @Autowired
    private ApplicationProperties applicationProperties;
    @Autowired
    private RESTClientQuittanceService restClientQuittanceService;

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

    private MockMvc restDetailVersementIntermediaireMockMvc;

    private DetailVersementIntermediaire detailVersementIntermediaire;
    
    @Autowired
    private RESTClientNotificationService restClientNotificationService;
    
    private RESTClientUAAService restClientUAAService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DetailVersementIntermediaireResource detailVersementIntermediaireResource = new DetailVersementIntermediaireResource(detailVersementIntermediaireService,
            paymentService, restClientSystacSygmaService, applicationProperties, restClientQuittanceService, restClientNotificationService, restClientUAAService);
        this.restDetailVersementIntermediaireMockMvc = MockMvcBuilders.standaloneSetup(detailVersementIntermediaireResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(TestUtil.createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DetailVersementIntermediaire createEntity(EntityManager em) {
        DetailVersementIntermediaire detailVersementIntermediaire = new DetailVersementIntermediaire()
            .numeroVersment(DEFAULT_NUMERO_VERSMENT)
            .date(DEFAULT_DATE)
            .montant(DEFAULT_MONTANT);
        Set<Payment> payments = new HashSet<>();
        payments.add(PaymentResourceIT.createEntity(em));
        detailVersementIntermediaire.setPayments(payments);
        return detailVersementIntermediaire;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DetailVersementIntermediaire createUpdatedEntity(EntityManager em) {
        DetailVersementIntermediaire detailVersementIntermediaire = new DetailVersementIntermediaire()
            .numeroVersment(UPDATED_NUMERO_VERSMENT)
            .date(UPDATED_DATE)
            .montant(UPDATED_MONTANT);
        return detailVersementIntermediaire;
    }

    @BeforeEach
    public void initTest() {
        detailVersementIntermediaire = createEntity(em);
    }

    @Test
    @Transactional
    public void createDetailVersementIntermediaire() throws Exception {
        int databaseSizeBeforeCreate = detailVersementIntermediaireRepository.findAll().size();

        // Create the DetailVersementIntermediaire
        DetailVersementIntermediaireDTO detailVersementIntermediaireDTO = detailVersementIntermediaireMapper.toDto(detailVersementIntermediaire);
        // System.out.println("********************** "+detailVersementIntermediaireDTO+" ***********************");
        restDetailVersementIntermediaireMockMvc.perform(post("/api/detail-versement-intermediaires")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(detailVersementIntermediaireDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DetailVersementIntermediaire in the database
        /*
        List<DetailVersementIntermediaire> detailVersementIntermediaireList = detailVersementIntermediaireRepository.findAll();
        assertThat(detailVersementIntermediaireList).hasSize(databaseSizeBeforeCreate + 1);
        DetailVersementIntermediaire testDetailVersementIntermediaire = detailVersementIntermediaireList.get(detailVersementIntermediaireList.size() - 1);
        assertThat(testDetailVersementIntermediaire.getNumeroVersment()).isEqualTo(DEFAULT_NUMERO_VERSMENT);
        assertThat(testDetailVersementIntermediaire.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testDetailVersementIntermediaire.getMontant()).isEqualTo(DEFAULT_MONTANT);
        */
    }

    @Test
    @Transactional
    public void createDetailVersementIntermediaireWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = detailVersementIntermediaireRepository.findAll().size();

        // Create the DetailVersementIntermediaire with an existing ID
        detailVersementIntermediaire.setId(1L);
        DetailVersementIntermediaireDTO detailVersementIntermediaireDTO = detailVersementIntermediaireMapper.toDto(detailVersementIntermediaire);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDetailVersementIntermediaireMockMvc.perform(post("/api/detail-versement-intermediaires")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(detailVersementIntermediaireDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DetailVersementIntermediaire in the database
        List<DetailVersementIntermediaire> detailVersementIntermediaireList = detailVersementIntermediaireRepository.findAll();
        assertThat(detailVersementIntermediaireList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllDetailVersementIntermediaires() throws Exception {
        // Initialize the database
        detailVersementIntermediaireRepository.saveAndFlush(detailVersementIntermediaire);

        // Get all the detailVersementIntermediaireList
        restDetailVersementIntermediaireMockMvc.perform(get("/api/detail-versement-intermediaires?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(detailVersementIntermediaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroVersment").value(hasItem(DEFAULT_NUMERO_VERSMENT)))
//            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.doubleValue())));
    }

    @Test
    @Transactional
    public void getDetailVersementIntermediaire() throws Exception {
        // Initialize the database
        detailVersementIntermediaireRepository.saveAndFlush(detailVersementIntermediaire);

        // Get the detailVersementIntermediaire
        restDetailVersementIntermediaireMockMvc.perform(get("/api/detail-versement-intermediaires/{id}", detailVersementIntermediaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(detailVersementIntermediaire.getId().intValue()))
            .andExpect(jsonPath("$.numeroVersment").value(DEFAULT_NUMERO_VERSMENT))
//            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDetailVersementIntermediaire() throws Exception {
        // Get the detailVersementIntermediaire
        restDetailVersementIntermediaireMockMvc.perform(get("/api/detail-versement-intermediaires/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    /*@Test
    @Transactional
    public void updateDetailVersementIntermediaire() throws Exception {
        // Initialize the database
        detailVersementIntermediaireRepository.saveAndFlush(detailVersementIntermediaire);

        int databaseSizeBeforeUpdate = detailVersementIntermediaireRepository.findAll().size();

        // Update the detailVersementIntermediaire
        DetailVersementIntermediaire updatedDetailVersementIntermediaire = detailVersementIntermediaireRepository.findById(detailVersementIntermediaire.getId()).get();
        // Disconnect from session so that the updates on updatedDetailVersementIntermediaire are not directly saved in db
        em.detach(updatedDetailVersementIntermediaire);
        updatedDetailVersementIntermediaire
            .numeroVersment(UPDATED_NUMERO_VERSMENT)
            .date(UPDATED_DATE)
            .montant(UPDATED_MONTANT);
        DetailVersementIntermediaireDTO detailVersementIntermediaireDTO = detailVersementIntermediaireMapper.toDto(updatedDetailVersementIntermediaire);

        restDetailVersementIntermediaireMockMvc.perform(put("/api/detail-versement-intermediaires")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(detailVersementIntermediaireDTO)))
            .andExpect(status().isOk());

        // Validate the DetailVersementIntermediaire in the database
        List<DetailVersementIntermediaire> detailVersementIntermediaireList = detailVersementIntermediaireRepository.findAll();
        assertThat(detailVersementIntermediaireList).hasSize(databaseSizeBeforeUpdate);
        DetailVersementIntermediaire testDetailVersementIntermediaire = detailVersementIntermediaireList.get(detailVersementIntermediaireList.size() - 1);
        assertThat(testDetailVersementIntermediaire.getNumeroVersment()).isEqualTo(UPDATED_NUMERO_VERSMENT);
        assertThat(testDetailVersementIntermediaire.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDetailVersementIntermediaire.getMontant()).isEqualTo(UPDATED_MONTANT);
    }

    @Test
    @Transactional
    public void updateNonExistingDetailVersementIntermediaire() throws Exception {
        int databaseSizeBeforeUpdate = detailVersementIntermediaireRepository.findAll().size();

        // Create the DetailVersementIntermediaire
        DetailVersementIntermediaireDTO detailVersementIntermediaireDTO = detailVersementIntermediaireMapper.toDto(detailVersementIntermediaire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDetailVersementIntermediaireMockMvc.perform(put("/api/detail-versement-intermediaires")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(detailVersementIntermediaireDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DetailVersementIntermediaire in the database
        List<DetailVersementIntermediaire> detailVersementIntermediaireList = detailVersementIntermediaireRepository.findAll();
        assertThat(detailVersementIntermediaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDetailVersementIntermediaire() throws Exception {
        // Initialize the database
        detailVersementIntermediaireRepository.saveAndFlush(detailVersementIntermediaire);

        int databaseSizeBeforeDelete = detailVersementIntermediaireRepository.findAll().size();

        // Delete the detailVersementIntermediaire
        restDetailVersementIntermediaireMockMvc.perform(delete("/api/detail-versement-intermediaires/{id}", detailVersementIntermediaire.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DetailVersementIntermediaire> detailVersementIntermediaireList = detailVersementIntermediaireRepository.findAll();
        assertThat(detailVersementIntermediaireList).hasSize(databaseSizeBeforeDelete - 1);
    }*/
}
