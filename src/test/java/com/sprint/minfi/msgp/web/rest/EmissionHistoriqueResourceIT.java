package com.sprint.minfi.msgp.web.rest;

import com.sprint.minfi.msgp.SpminfimsgpApp;
import com.sprint.minfi.msgp.config.SecurityBeanOverrideConfiguration;
import com.sprint.minfi.msgp.domain.EmissionHistorique;
import com.sprint.minfi.msgp.repository.EmissionHistoriqueRepository;
import com.sprint.minfi.msgp.service.EmissionHistoriqueService;
import com.sprint.minfi.msgp.service.dto.EmissionHistoriqueDTO;
import com.sprint.minfi.msgp.service.mapper.EmissionHistoriqueMapper;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.sprint.minfi.msgp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link EmissionHistoriqueResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, SpminfimsgpApp.class})
public class EmissionHistoriqueResourceIT {

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final Long DEFAULT_ID_EMI = 1L;
    private static final Long UPDATED_ID_EMI = 2L;

    private static final LocalDate DEFAULT_DATE_STATUS = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_STATUS = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private EmissionHistoriqueRepository emissionHistoriqueRepository;

    @Autowired
    private EmissionHistoriqueMapper emissionHistoriqueMapper;

    @Autowired
    private EmissionHistoriqueService emissionHistoriqueService;

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

    private MockMvc restEmissionHistoriqueMockMvc;

    private EmissionHistorique emissionHistorique;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EmissionHistoriqueResource emissionHistoriqueResource = new EmissionHistoriqueResource(emissionHistoriqueService);
        this.restEmissionHistoriqueMockMvc = MockMvcBuilders.standaloneSetup(emissionHistoriqueResource)
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
    public static EmissionHistorique createEntity(EntityManager em) {
        EmissionHistorique emissionHistorique = new EmissionHistorique()
            .status(DEFAULT_STATUS)
            .idEmi(DEFAULT_ID_EMI)
            .dateStatus(DEFAULT_DATE_STATUS);
        return emissionHistorique;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmissionHistorique createUpdatedEntity(EntityManager em) {
        EmissionHistorique emissionHistorique = new EmissionHistorique()
            .status(UPDATED_STATUS)
            .idEmi(UPDATED_ID_EMI)
            .dateStatus(UPDATED_DATE_STATUS);
        return emissionHistorique;
    }

    @BeforeEach
    public void initTest() {
        emissionHistorique = createEntity(em);
    }

    @Test
    @Transactional
    public void createEmissionHistorique() throws Exception {
        int databaseSizeBeforeCreate = emissionHistoriqueRepository.findAll().size();

        // Create the EmissionHistorique
        EmissionHistoriqueDTO emissionHistoriqueDTO = emissionHistoriqueMapper.toDto(emissionHistorique);
        restEmissionHistoriqueMockMvc.perform(post("/api/emission-historiques")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(emissionHistoriqueDTO)))
            .andExpect(status().isCreated());

        // Validate the EmissionHistorique in the database
        List<EmissionHistorique> emissionHistoriqueList = emissionHistoriqueRepository.findAll();
        assertThat(emissionHistoriqueList).hasSize(databaseSizeBeforeCreate + 1);
        EmissionHistorique testEmissionHistorique = emissionHistoriqueList.get(emissionHistoriqueList.size() - 1);
        assertThat(testEmissionHistorique.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testEmissionHistorique.getIdEmi()).isEqualTo(DEFAULT_ID_EMI);
        assertThat(testEmissionHistorique.getDateStatus()).isEqualTo(DEFAULT_DATE_STATUS);
    }

    @Test
    @Transactional
    public void createEmissionHistoriqueWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = emissionHistoriqueRepository.findAll().size();

        // Create the EmissionHistorique with an existing ID
        emissionHistorique.setId(1L);
        EmissionHistoriqueDTO emissionHistoriqueDTO = emissionHistoriqueMapper.toDto(emissionHistorique);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmissionHistoriqueMockMvc.perform(post("/api/emission-historiques")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(emissionHistoriqueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EmissionHistorique in the database
        List<EmissionHistorique> emissionHistoriqueList = emissionHistoriqueRepository.findAll();
        assertThat(emissionHistoriqueList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllEmissionHistoriques() throws Exception {
        // Initialize the database
        emissionHistoriqueRepository.saveAndFlush(emissionHistorique);

        // Get all the emissionHistoriqueList
        restEmissionHistoriqueMockMvc.perform(get("/api/emission-historiques?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emissionHistorique.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].idEmi").value(hasItem(DEFAULT_ID_EMI.intValue())))
            .andExpect(jsonPath("$.[*].dateStatus").value(hasItem(DEFAULT_DATE_STATUS.toString())));
    }
    
    @Test
    @Transactional
    public void getEmissionHistorique() throws Exception {
        // Initialize the database
        emissionHistoriqueRepository.saveAndFlush(emissionHistorique);

        // Get the emissionHistorique
        restEmissionHistoriqueMockMvc.perform(get("/api/emission-historiques/{id}", emissionHistorique.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(emissionHistorique.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.idEmi").value(DEFAULT_ID_EMI.intValue()))
            .andExpect(jsonPath("$.dateStatus").value(DEFAULT_DATE_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEmissionHistorique() throws Exception {
        // Get the emissionHistorique
        restEmissionHistoriqueMockMvc.perform(get("/api/emission-historiques/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmissionHistorique() throws Exception {
        // Initialize the database
        emissionHistoriqueRepository.saveAndFlush(emissionHistorique);

        int databaseSizeBeforeUpdate = emissionHistoriqueRepository.findAll().size();

        // Update the emissionHistorique
        EmissionHistorique updatedEmissionHistorique = emissionHistoriqueRepository.findById(emissionHistorique.getId()).get();
        // Disconnect from session so that the updates on updatedEmissionHistorique are not directly saved in db
        em.detach(updatedEmissionHistorique);
        updatedEmissionHistorique
            .status(UPDATED_STATUS)
            .idEmi(UPDATED_ID_EMI)
            .dateStatus(UPDATED_DATE_STATUS);
        EmissionHistoriqueDTO emissionHistoriqueDTO = emissionHistoriqueMapper.toDto(updatedEmissionHistorique);

        restEmissionHistoriqueMockMvc.perform(put("/api/emission-historiques")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(emissionHistoriqueDTO)))
            .andExpect(status().isOk());

        // Validate the EmissionHistorique in the database
        List<EmissionHistorique> emissionHistoriqueList = emissionHistoriqueRepository.findAll();
        assertThat(emissionHistoriqueList).hasSize(databaseSizeBeforeUpdate);
        EmissionHistorique testEmissionHistorique = emissionHistoriqueList.get(emissionHistoriqueList.size() - 1);
        assertThat(testEmissionHistorique.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testEmissionHistorique.getIdEmi()).isEqualTo(UPDATED_ID_EMI);
        assertThat(testEmissionHistorique.getDateStatus()).isEqualTo(UPDATED_DATE_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingEmissionHistorique() throws Exception {
        int databaseSizeBeforeUpdate = emissionHistoriqueRepository.findAll().size();

        // Create the EmissionHistorique
        EmissionHistoriqueDTO emissionHistoriqueDTO = emissionHistoriqueMapper.toDto(emissionHistorique);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmissionHistoriqueMockMvc.perform(put("/api/emission-historiques")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(emissionHistoriqueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EmissionHistorique in the database
        List<EmissionHistorique> emissionHistoriqueList = emissionHistoriqueRepository.findAll();
        assertThat(emissionHistoriqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEmissionHistorique() throws Exception {
        // Initialize the database
        emissionHistoriqueRepository.saveAndFlush(emissionHistorique);

        int databaseSizeBeforeDelete = emissionHistoriqueRepository.findAll().size();

        // Delete the emissionHistorique
        restEmissionHistoriqueMockMvc.perform(delete("/api/emission-historiques/{id}", emissionHistorique.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EmissionHistorique> emissionHistoriqueList = emissionHistoriqueRepository.findAll();
        assertThat(emissionHistoriqueList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
