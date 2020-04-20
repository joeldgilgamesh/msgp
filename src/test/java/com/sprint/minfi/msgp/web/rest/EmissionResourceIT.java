package com.sprint.minfi.msgp.web.rest;

import com.sprint.minfi.msgp.SpminfimsgpApp;
import com.sprint.minfi.msgp.config.SecurityBeanOverrideConfiguration;
import com.sprint.minfi.msgp.domain.Emission;
import com.sprint.minfi.msgp.repository.EmissionRepository;
import com.sprint.minfi.msgp.service.EmissionService;
import com.sprint.minfi.msgp.service.dto.EmissionDTO;
import com.sprint.minfi.msgp.service.mapper.EmissionMapper;
import com.sprint.minfi.msgp.web.rest.errors.ExceptionTranslator;

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

import javax.persistence.EntityManager;
import java.util.List;

import static com.sprint.minfi.msgp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sprint.minfi.msgp.domain.enumeration.Statut;
/**
 * Integration tests for the {@link EmissionResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, SpminfimsgpApp.class})
public class EmissionResourceIT {

    private static final String DEFAULT_REF_EMI = "AAAAAAAAAA";
    private static final String UPDATED_REF_EMI = "BBBBBBBBBB";

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final String DEFAULT_CODE_CONTRIBUABLE = "AAAAAAAAAA";
    private static final String UPDATED_CODE_CONTRIBUABLE = "BBBBBBBBBB";

    private static final Statut DEFAULT_STATUS = Statut.DRAFT;
    private static final Statut UPDATED_STATUS = Statut.PENDING;

    private static final Long DEFAULT_ID_ORGANISATION = 1L;
    private static final Long UPDATED_ID_ORGANISATION = 2L;

    private static final Long DEFAULT_ID_TYPE_EMIS_ID = 1L;
    private static final Long UPDATED_ID_TYPE_EMIS_ID = 2L;

    @Autowired
    private EmissionRepository emissionRepository;

    @Autowired
    private EmissionMapper emissionMapper;

    @Autowired
    private EmissionService emissionService;

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

    private MockMvc restEmissionMockMvc;

    private Emission emission;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EmissionResource emissionResource = new EmissionResource(emissionService);
        this.restEmissionMockMvc = MockMvcBuilders.standaloneSetup(emissionResource)
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
    public static Emission createEntity(EntityManager em) {
        Emission emission = new Emission()
            .refEmi(DEFAULT_REF_EMI)
            .amount(DEFAULT_AMOUNT)
            .codeContribuable(DEFAULT_CODE_CONTRIBUABLE)
            .status(DEFAULT_STATUS)
            .idOrganisation(DEFAULT_ID_ORGANISATION)
            .idTypeEmisId(DEFAULT_ID_TYPE_EMIS_ID);
        return emission;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Emission createUpdatedEntity(EntityManager em) {
        Emission emission = new Emission()
            .refEmi(UPDATED_REF_EMI)
            .amount(UPDATED_AMOUNT)
            .codeContribuable(UPDATED_CODE_CONTRIBUABLE)
            .status(UPDATED_STATUS)
            .idOrganisation(UPDATED_ID_ORGANISATION)
            .idTypeEmisId(UPDATED_ID_TYPE_EMIS_ID);
        return emission;
    }

    @BeforeEach
    public void initTest() {
        emission = createEntity(em);
    }

    @Test
    @Transactional
    public void createEmission() throws Exception {
        int databaseSizeBeforeCreate = emissionRepository.findAll().size();

        // Create the Emission
        EmissionDTO emissionDTO = emissionMapper.toDto(emission);
        restEmissionMockMvc.perform(post("/api/emissions")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(emissionDTO)))
            .andExpect(status().isCreated());

        // Validate the Emission in the database
        List<Emission> emissionList = emissionRepository.findAll();
        assertThat(emissionList).hasSize(databaseSizeBeforeCreate + 1);
        Emission testEmission = emissionList.get(emissionList.size() - 1);
        assertThat(testEmission.getRefEmi()).isEqualTo(DEFAULT_REF_EMI);
        assertThat(testEmission.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testEmission.getCodeContribuable()).isEqualTo(DEFAULT_CODE_CONTRIBUABLE);
        assertThat(testEmission.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testEmission.getIdOrganisation()).isEqualTo(DEFAULT_ID_ORGANISATION);
        assertThat(testEmission.getIdTypeEmisId()).isEqualTo(DEFAULT_ID_TYPE_EMIS_ID);
    }

    @Test
    @Transactional
    public void createEmissionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = emissionRepository.findAll().size();

        // Create the Emission with an existing ID
        emission.setId(1L);
        EmissionDTO emissionDTO = emissionMapper.toDto(emission);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmissionMockMvc.perform(post("/api/emissions")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(emissionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Emission in the database
        List<Emission> emissionList = emissionRepository.findAll();
        assertThat(emissionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllEmissions() throws Exception {
        // Initialize the database
        emissionRepository.saveAndFlush(emission);

        // Get all the emissionList
        restEmissionMockMvc.perform(get("/api/emissions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emission.getId().intValue())))
            .andExpect(jsonPath("$.[*].refEmi").value(hasItem(DEFAULT_REF_EMI)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].codeContribuable").value(hasItem(DEFAULT_CODE_CONTRIBUABLE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].idOrganisation").value(hasItem(DEFAULT_ID_ORGANISATION.intValue())))
            .andExpect(jsonPath("$.[*].idTypeEmisId").value(hasItem(DEFAULT_ID_TYPE_EMIS_ID.intValue())));
    }
    
//    @Test
//    @Transactional
//    public void getEmission() throws Exception {
//        // Initialize the database
//        emissionRepository.saveAndFlush(emission);
//
//        // Get the emission
//        restEmissionMockMvc.perform(get("/api/emissions/{id}", emission.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(jsonPath("$.id").value(emission.getId().intValue()))
//            .andExpect(jsonPath("$.refEmi").value(DEFAULT_REF_EMI))
//            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
//            .andExpect(jsonPath("$.codeContribuable").value(DEFAULT_CODE_CONTRIBUABLE))
//            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
//            .andExpect(jsonPath("$.idOrganisation").value(DEFAULT_ID_ORGANISATION.intValue()))
//            .andExpect(jsonPath("$.idTypeEmisId").value(DEFAULT_ID_TYPE_EMIS_ID.intValue()));
//    }

//    @Test
//    @Transactional
//    public void getNonExistingEmission() throws Exception {
//        // Get the emission
//        restEmissionMockMvc.perform(get("/api/emissions/{id}", Long.MAX_VALUE))
//            .andExpect(status().isNotFound());
//    }

    @Test
    @Transactional
    public void updateEmission() throws Exception {
        // Initialize the database
        emissionRepository.saveAndFlush(emission);

        int databaseSizeBeforeUpdate = emissionRepository.findAll().size();

        // Update the emission
        Emission updatedEmission = emissionRepository.findById(emission.getId()).get();
        // Disconnect from session so that the updates on updatedEmission are not directly saved in db
        em.detach(updatedEmission);
        updatedEmission
            .refEmi(UPDATED_REF_EMI)
            .amount(UPDATED_AMOUNT)
            .codeContribuable(UPDATED_CODE_CONTRIBUABLE)
            .status(UPDATED_STATUS)
            .idOrganisation(UPDATED_ID_ORGANISATION)
            .idTypeEmisId(UPDATED_ID_TYPE_EMIS_ID);
        EmissionDTO emissionDTO = emissionMapper.toDto(updatedEmission);

        restEmissionMockMvc.perform(put("/api/emissions")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(emissionDTO)))
            .andExpect(status().isOk());

        // Validate the Emission in the database
        List<Emission> emissionList = emissionRepository.findAll();
        assertThat(emissionList).hasSize(databaseSizeBeforeUpdate);
        Emission testEmission = emissionList.get(emissionList.size() - 1);
        assertThat(testEmission.getRefEmi()).isEqualTo(UPDATED_REF_EMI);
        assertThat(testEmission.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testEmission.getCodeContribuable()).isEqualTo(UPDATED_CODE_CONTRIBUABLE);
        assertThat(testEmission.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testEmission.getIdOrganisation()).isEqualTo(UPDATED_ID_ORGANISATION);
        assertThat(testEmission.getIdTypeEmisId()).isEqualTo(UPDATED_ID_TYPE_EMIS_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingEmission() throws Exception {
        int databaseSizeBeforeUpdate = emissionRepository.findAll().size();

        // Create the Emission
        EmissionDTO emissionDTO = emissionMapper.toDto(emission);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmissionMockMvc.perform(put("/api/emissions")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(emissionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Emission in the database
        List<Emission> emissionList = emissionRepository.findAll();
        assertThat(emissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEmission() throws Exception {
        // Initialize the database
        emissionRepository.saveAndFlush(emission);

        int databaseSizeBeforeDelete = emissionRepository.findAll().size();

        // Delete the emission
        restEmissionMockMvc.perform(delete("/api/emissions/{id}", emission.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Emission> emissionList = emissionRepository.findAll();
        assertThat(emissionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
