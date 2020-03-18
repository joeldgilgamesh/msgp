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
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
import com.sprint.minfi.msgp.repository.HistoriquePaymentRepository;
import com.sprint.minfi.msgp.service.HistoriquePaymentService;
import com.sprint.minfi.msgp.service.dto.HistoriquePaymentDTO;
import com.sprint.minfi.msgp.service.mapper.HistoriquePaymentMapper;
import com.sprint.minfi.msgp.web.rest.errors.ExceptionTranslator;

/**
 * Integration tests for the {@link HistoriquePaymentResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, SpminfimsgpApp.class})
public class HistoriquePaymentResourceIT {

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final LocalDateTime DEFAULT_DATE_STATUS = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
    private static final LocalDateTime UPDATED_DATE_STATUS = LocalDateTime.now(ZoneId.systemDefault());

    @Autowired
    private HistoriquePaymentRepository historiquePaymentRepository;

    @Autowired
    private HistoriquePaymentMapper historiquePaymentMapper;

    @Autowired
    private HistoriquePaymentService historiquePaymentService;

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

    private MockMvc restHistoriquePaymentMockMvc;

    private HistoriquePayment historiquePayment;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HistoriquePaymentResource historiquePaymentResource = new HistoriquePaymentResource(historiquePaymentService);
        this.restHistoriquePaymentMockMvc = MockMvcBuilders.standaloneSetup(historiquePaymentResource)
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
    public static HistoriquePayment createEntity(EntityManager em) {
        HistoriquePayment historiquePayment = new HistoriquePayment()
            .status(DEFAULT_STATUS)
            .dateStatus(DEFAULT_DATE_STATUS);
        return historiquePayment;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoriquePayment createUpdatedEntity(EntityManager em) {
        HistoriquePayment historiquePayment = new HistoriquePayment()
            .status(UPDATED_STATUS)
            .dateStatus(UPDATED_DATE_STATUS);
        return historiquePayment;
    }

    @BeforeEach
    public void initTest() {
        historiquePayment = createEntity(em);
    }

    @Test
    @Transactional
    public void createHistoriquePayment() throws Exception {
        int databaseSizeBeforeCreate = historiquePaymentRepository.findAll().size();

        // Create the HistoriquePayment
        HistoriquePaymentDTO historiquePaymentDTO = historiquePaymentMapper.toDto(historiquePayment);
        restHistoriquePaymentMockMvc.perform(post("/api/historique-payments")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(historiquePaymentDTO)))
            .andExpect(status().isCreated());

        // Validate the HistoriquePayment in the database
        List<HistoriquePayment> historiquePaymentList = historiquePaymentRepository.findAll();
        assertThat(historiquePaymentList).hasSize(databaseSizeBeforeCreate + 1);
        HistoriquePayment testHistoriquePayment = historiquePaymentList.get(historiquePaymentList.size() - 1);
        assertThat(testHistoriquePayment.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testHistoriquePayment.getDateStatus()).isEqualTo(DEFAULT_DATE_STATUS);
    }

    @Test
    @Transactional
    public void createHistoriquePaymentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = historiquePaymentRepository.findAll().size();

        // Create the HistoriquePayment with an existing ID
        historiquePayment.setId(1L);
        HistoriquePaymentDTO historiquePaymentDTO = historiquePaymentMapper.toDto(historiquePayment);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHistoriquePaymentMockMvc.perform(post("/api/historique-payments")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(historiquePaymentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HistoriquePayment in the database
        List<HistoriquePayment> historiquePaymentList = historiquePaymentRepository.findAll();
        assertThat(historiquePaymentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllHistoriquePayments() throws Exception {
        // Initialize the database
        historiquePaymentRepository.saveAndFlush(historiquePayment);

        // Get all the historiquePaymentList
        restHistoriquePaymentMockMvc.perform(get("/api/historique-payments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historiquePayment.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
//            .andExpect(jsonPath("$.[*].dateStatus").value(hasItem(DEFAULT_DATE_STATUS.toString())));
    }
    
    @Test
    @Transactional
    public void getHistoriquePayment() throws Exception {
        // Initialize the database
        historiquePaymentRepository.saveAndFlush(historiquePayment);

        // Get the historiquePayment
        restHistoriquePaymentMockMvc.perform(get("/api/historique-payments/{id}", historiquePayment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(historiquePayment.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
//            .andExpect(jsonPath("$.dateStatus").value(DEFAULT_DATE_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHistoriquePayment() throws Exception {
        // Get the historiquePayment
        restHistoriquePaymentMockMvc.perform(get("/api/historique-payments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHistoriquePayment() throws Exception {
        // Initialize the database
        historiquePaymentRepository.saveAndFlush(historiquePayment);

        int databaseSizeBeforeUpdate = historiquePaymentRepository.findAll().size();

        // Update the historiquePayment
        HistoriquePayment updatedHistoriquePayment = historiquePaymentRepository.findById(historiquePayment.getId()).get();
        // Disconnect from session so that the updates on updatedHistoriquePayment are not directly saved in db
        em.detach(updatedHistoriquePayment);
        updatedHistoriquePayment
            .status(UPDATED_STATUS)
            .setDateStatus(UPDATED_DATE_STATUS);
        HistoriquePaymentDTO historiquePaymentDTO = historiquePaymentMapper.toDto(updatedHistoriquePayment);

        restHistoriquePaymentMockMvc.perform(put("/api/historique-payments")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(historiquePaymentDTO)))
            .andExpect(status().isOk());

        // Validate the HistoriquePayment in the database
        List<HistoriquePayment> historiquePaymentList = historiquePaymentRepository.findAll();
        assertThat(historiquePaymentList).hasSize(databaseSizeBeforeUpdate);
        HistoriquePayment testHistoriquePayment = historiquePaymentList.get(historiquePaymentList.size() - 1);
        assertThat(testHistoriquePayment.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testHistoriquePayment.getDateStatus()).isEqualTo(UPDATED_DATE_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingHistoriquePayment() throws Exception {
        int databaseSizeBeforeUpdate = historiquePaymentRepository.findAll().size();

        // Create the HistoriquePayment
        HistoriquePaymentDTO historiquePaymentDTO = historiquePaymentMapper.toDto(historiquePayment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoriquePaymentMockMvc.perform(put("/api/historique-payments")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(historiquePaymentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HistoriquePayment in the database
        List<HistoriquePayment> historiquePaymentList = historiquePaymentRepository.findAll();
        assertThat(historiquePaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteHistoriquePayment() throws Exception {
        // Initialize the database
        historiquePaymentRepository.saveAndFlush(historiquePayment);

        int databaseSizeBeforeDelete = historiquePaymentRepository.findAll().size();

        // Delete the historiquePayment
        restHistoriquePaymentMockMvc.perform(delete("/api/historique-payments/{id}", historiquePayment.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HistoriquePayment> historiquePaymentList = historiquePaymentRepository.findAll();
        assertThat(historiquePaymentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
