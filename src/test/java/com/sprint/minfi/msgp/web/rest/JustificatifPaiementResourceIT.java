package com.sprint.minfi.msgp.web.rest;

import com.sprint.minfi.msgp.SpminfimsgpApp;
import com.sprint.minfi.msgp.config.SecurityBeanOverrideConfiguration;
import com.sprint.minfi.msgp.domain.JustificatifPaiement;
import com.sprint.minfi.msgp.repository.JustificatifPaiementRepository;
import com.sprint.minfi.msgp.service.JustificatifPaiementService;
import com.sprint.minfi.msgp.service.dto.JustificatifPaiementDTO;
import com.sprint.minfi.msgp.service.mapper.JustificatifPaiementMapper;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

import static com.sprint.minfi.msgp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link JustificatifPaiementResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, SpminfimsgpApp.class})
public class JustificatifPaiementResourceIT {

    private static final Long DEFAULT_ID_PAIEMENT = 1L;
    private static final Long UPDATED_ID_PAIEMENT = 2L;

    private static final LocalDateTime DEFAULT_DATE_CREATION = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
    private static final LocalDateTime UPDATED_DATE_CREATION = LocalDateTime.now(ZoneId.systemDefault());

    private static final String DEFAULT_REFERENCE_PAIEMENT = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE_PAIEMENT = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE_PAIEMENT = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_PAIEMENT = "BBBBBBBBBB";

    private static final String DEFAULT_NATURE_RECETTE = "AAAAAAAAAA";
    private static final String UPDATED_NATURE_RECETTE = "BBBBBBBBBB";

    private static final Double DEFAULT_MONTANT = 1D;
    private static final Double UPDATED_MONTANT = 2D;

    private static final String DEFAULT_NUI = "AAAAAAAAAA";
    private static final String UPDATED_NUI = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_PRENOM_CLIENT = "AAAAAAAAAA";
    private static final String UPDATED_NOM_PRENOM_CLIENT = "BBBBBBBBBB";

    private static final Long DEFAULT_ID_ORGANISATION = 1L;
    private static final Long UPDATED_ID_ORGANISATION = 2L;

    private static final String DEFAULT_CODE_ORGANISATION = "AAAAAAAAAA";
    private static final String UPDATED_CODE_ORGANISATION = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_ORGANISATION = "AAAAAAAAAA";
    private static final String UPDATED_NOM_ORGANISATION = "BBBBBBBBBB";

    private static final String DEFAULT_EXERCISE = "AAAAAAAAAA";
    private static final String UPDATED_EXERCISE = "BBBBBBBBBB";

    private static final String DEFAULT_MOIS = "AAAAAAAAAA";
    private static final String UPDATED_MOIS = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE_CENTRE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_CENTRE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE_COURT_CENTRE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_COURT_CENTRE = "BBBBBBBBBB";

    private static final String DEFAULT_IFU = "AAAAAAAAAA";
    private static final String UPDATED_IFU = "BBBBBBBBBB";

    private static final String DEFAULT_RAISON_SOCIALE = "AAAAAAAAAA";
    private static final String UPDATED_RAISON_SOCIALE = "BBBBBBBBBB";

    private static final String DEFAULT_SIGLE = "AAAAAAAAAA";
    private static final String UPDATED_SIGLE = "BBBBBBBBBB";

    private static final Long DEFAULT_CODE_POSTE = 1L;
    private static final Long UPDATED_CODE_POSTE = 2L;

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Long DEFAULT_NUMERO = 1L;
    private static final Long UPDATED_NUMERO = 2L;

    @Autowired
    private JustificatifPaiementRepository justificatifPaiementRepository;

    @Autowired
    private JustificatifPaiementMapper justificatifPaiementMapper;

    @Autowired
    private JustificatifPaiementService justificatifPaiementService;

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

    private MockMvc restJustificatifPaiementMockMvc;

    private JustificatifPaiement justificatifPaiement;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final JustificatifPaiementResource justificatifPaiementResource = new JustificatifPaiementResource(justificatifPaiementService);
        this.restJustificatifPaiementMockMvc = MockMvcBuilders.standaloneSetup(justificatifPaiementResource)
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
    public static JustificatifPaiement createEntity(EntityManager em) {
        JustificatifPaiement justificatifPaiement = new JustificatifPaiement()
            .idPaiement(DEFAULT_ID_PAIEMENT)
            .dateCreation(DEFAULT_DATE_CREATION)
            .referencePaiement(DEFAULT_REFERENCE_PAIEMENT)
            .typePaiement(DEFAULT_TYPE_PAIEMENT)
            .natureRecette(DEFAULT_NATURE_RECETTE)
            .montant(DEFAULT_MONTANT)
            .nui(DEFAULT_NUI)
            .nomPrenomClient(DEFAULT_NOM_PRENOM_CLIENT)
            .idOrganisation(DEFAULT_ID_ORGANISATION)
            .codeOrganisation(DEFAULT_CODE_ORGANISATION)
            .nomOrganisation(DEFAULT_NOM_ORGANISATION)
            .exercise(DEFAULT_EXERCISE)
            .mois(DEFAULT_MOIS)
            .libelleCentre(DEFAULT_LIBELLE_CENTRE)
            .libelleCourtCentre(DEFAULT_LIBELLE_COURT_CENTRE)
            .ifu(DEFAULT_IFU)
            .raisonSociale(DEFAULT_RAISON_SOCIALE)
            .sigle(DEFAULT_SIGLE)
            .codePoste(DEFAULT_CODE_POSTE)
            .code(DEFAULT_CODE)
            .numero(DEFAULT_NUMERO);
        return justificatifPaiement;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JustificatifPaiement createUpdatedEntity(EntityManager em) {
        JustificatifPaiement justificatifPaiement = new JustificatifPaiement()
            .idPaiement(UPDATED_ID_PAIEMENT)
            .dateCreation(UPDATED_DATE_CREATION)
            .referencePaiement(UPDATED_REFERENCE_PAIEMENT)
            .typePaiement(UPDATED_TYPE_PAIEMENT)
            .natureRecette(UPDATED_NATURE_RECETTE)
            .montant(UPDATED_MONTANT)
            .nui(UPDATED_NUI)
            .nomPrenomClient(UPDATED_NOM_PRENOM_CLIENT)
            .idOrganisation(UPDATED_ID_ORGANISATION)
            .codeOrganisation(UPDATED_CODE_ORGANISATION)
            .nomOrganisation(UPDATED_NOM_ORGANISATION)
            .exercise(UPDATED_EXERCISE)
            .mois(UPDATED_MOIS)
            .libelleCentre(UPDATED_LIBELLE_CENTRE)
            .libelleCourtCentre(UPDATED_LIBELLE_COURT_CENTRE)
            .ifu(UPDATED_IFU)
            .raisonSociale(UPDATED_RAISON_SOCIALE)
            .sigle(UPDATED_SIGLE)
            .codePoste(UPDATED_CODE_POSTE)
            .code(UPDATED_CODE)
            .numero(UPDATED_NUMERO);
        return justificatifPaiement;
    }

    @BeforeEach
    public void initTest() {
        justificatifPaiement = createEntity(em);
    }

    @Test
    @Transactional
    public void createJustificatifPaiement() throws Exception {
        int databaseSizeBeforeCreate = justificatifPaiementRepository.findAll().size();

        // Create the JustificatifPaiement
        JustificatifPaiementDTO justificatifPaiementDTO = justificatifPaiementMapper.toDto(justificatifPaiement);
        restJustificatifPaiementMockMvc.perform(post("/api/justificatif-paiements")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(justificatifPaiementDTO)))
            .andExpect(status().isCreated());

        // Validate the JustificatifPaiement in the database
        List<JustificatifPaiement> justificatifPaiementList = justificatifPaiementRepository.findAll();
        assertThat(justificatifPaiementList).hasSize(databaseSizeBeforeCreate + 1);
        JustificatifPaiement testJustificatifPaiement = justificatifPaiementList.get(justificatifPaiementList.size() - 1);
        assertThat(testJustificatifPaiement.getIdPaiement()).isEqualTo(DEFAULT_ID_PAIEMENT);
        assertThat(testJustificatifPaiement.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testJustificatifPaiement.getReferencePaiement()).isEqualTo(DEFAULT_REFERENCE_PAIEMENT);
        assertThat(testJustificatifPaiement.getTypePaiement()).isEqualTo(DEFAULT_TYPE_PAIEMENT);
        assertThat(testJustificatifPaiement.getNatureRecette()).isEqualTo(DEFAULT_NATURE_RECETTE);
        assertThat(testJustificatifPaiement.getMontant()).isEqualTo(DEFAULT_MONTANT);
        assertThat(testJustificatifPaiement.getNui()).isEqualTo(DEFAULT_NUI);
        assertThat(testJustificatifPaiement.getNomPrenomClient()).isEqualTo(DEFAULT_NOM_PRENOM_CLIENT);
        assertThat(testJustificatifPaiement.getIdOrganisation()).isEqualTo(DEFAULT_ID_ORGANISATION);
        assertThat(testJustificatifPaiement.getCodeOrganisation()).isEqualTo(DEFAULT_CODE_ORGANISATION);
        assertThat(testJustificatifPaiement.getNomOrganisation()).isEqualTo(DEFAULT_NOM_ORGANISATION);
        assertThat(testJustificatifPaiement.getExercise()).isEqualTo(DEFAULT_EXERCISE);
        assertThat(testJustificatifPaiement.getMois()).isEqualTo(DEFAULT_MOIS);
        assertThat(testJustificatifPaiement.getLibelleCentre()).isEqualTo(DEFAULT_LIBELLE_CENTRE);
        assertThat(testJustificatifPaiement.getLibelleCourtCentre()).isEqualTo(DEFAULT_LIBELLE_COURT_CENTRE);
        assertThat(testJustificatifPaiement.getIfu()).isEqualTo(DEFAULT_IFU);
        assertThat(testJustificatifPaiement.getRaisonSociale()).isEqualTo(DEFAULT_RAISON_SOCIALE);
        assertThat(testJustificatifPaiement.getSigle()).isEqualTo(DEFAULT_SIGLE);
        assertThat(testJustificatifPaiement.getCodePoste()).isEqualTo(DEFAULT_CODE_POSTE);
        assertThat(testJustificatifPaiement.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testJustificatifPaiement.getNumero()).isEqualTo(DEFAULT_NUMERO);
    }

    @Test
    @Transactional
    public void createJustificatifPaiementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = justificatifPaiementRepository.findAll().size();

        // Create the JustificatifPaiement with an existing ID
        justificatifPaiement.setId(1L);
        JustificatifPaiementDTO justificatifPaiementDTO = justificatifPaiementMapper.toDto(justificatifPaiement);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJustificatifPaiementMockMvc.perform(post("/api/justificatif-paiements")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(justificatifPaiementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the JustificatifPaiement in the database
        List<JustificatifPaiement> justificatifPaiementList = justificatifPaiementRepository.findAll();
        assertThat(justificatifPaiementList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllJustificatifPaiements() throws Exception {
        // Initialize the database
        justificatifPaiementRepository.saveAndFlush(justificatifPaiement);

        // Get all the justificatifPaiementList
        restJustificatifPaiementMockMvc.perform(get("/api/justificatif-paiements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(justificatifPaiement.getId().intValue())))
            .andExpect(jsonPath("$.[*].idPaiement").value(hasItem(DEFAULT_ID_PAIEMENT.intValue())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].referencePaiement").value(hasItem(DEFAULT_REFERENCE_PAIEMENT)))
            .andExpect(jsonPath("$.[*].typePaiement").value(hasItem(DEFAULT_TYPE_PAIEMENT)))
            .andExpect(jsonPath("$.[*].natureRecette").value(hasItem(DEFAULT_NATURE_RECETTE)))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.doubleValue())))
            .andExpect(jsonPath("$.[*].nui").value(hasItem(DEFAULT_NUI)))
            .andExpect(jsonPath("$.[*].nomPrenomClient").value(hasItem(DEFAULT_NOM_PRENOM_CLIENT)))
            .andExpect(jsonPath("$.[*].idOrganisation").value(hasItem(DEFAULT_ID_ORGANISATION.intValue())))
            .andExpect(jsonPath("$.[*].codeOrganisation").value(hasItem(DEFAULT_CODE_ORGANISATION)))
            .andExpect(jsonPath("$.[*].nomOrganisation").value(hasItem(DEFAULT_NOM_ORGANISATION)))
            .andExpect(jsonPath("$.[*].exercise").value(hasItem(DEFAULT_EXERCISE)))
            .andExpect(jsonPath("$.[*].mois").value(hasItem(DEFAULT_MOIS)))
            .andExpect(jsonPath("$.[*].libelleCentre").value(hasItem(DEFAULT_LIBELLE_CENTRE)))
            .andExpect(jsonPath("$.[*].libelleCourtCentre").value(hasItem(DEFAULT_LIBELLE_COURT_CENTRE)))
            .andExpect(jsonPath("$.[*].ifu").value(hasItem(DEFAULT_IFU)))
            .andExpect(jsonPath("$.[*].raisonSociale").value(hasItem(DEFAULT_RAISON_SOCIALE)))
            .andExpect(jsonPath("$.[*].sigle").value(hasItem(DEFAULT_SIGLE)))
            .andExpect(jsonPath("$.[*].codePoste").value(hasItem(DEFAULT_CODE_POSTE.intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO.intValue())));
    }
    
    @Test
    @Transactional
    public void getJustificatifPaiement() throws Exception {
        // Initialize the database
        justificatifPaiementRepository.saveAndFlush(justificatifPaiement);

        // Get the justificatifPaiement
        restJustificatifPaiementMockMvc.perform(get("/api/justificatif-paiements/{id}", justificatifPaiement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(justificatifPaiement.getId().intValue()))
            .andExpect(jsonPath("$.idPaiement").value(DEFAULT_ID_PAIEMENT.intValue()))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()))
            .andExpect(jsonPath("$.referencePaiement").value(DEFAULT_REFERENCE_PAIEMENT))
            .andExpect(jsonPath("$.typePaiement").value(DEFAULT_TYPE_PAIEMENT))
            .andExpect(jsonPath("$.natureRecette").value(DEFAULT_NATURE_RECETTE))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT.doubleValue()))
            .andExpect(jsonPath("$.nui").value(DEFAULT_NUI))
            .andExpect(jsonPath("$.nomPrenomClient").value(DEFAULT_NOM_PRENOM_CLIENT))
            .andExpect(jsonPath("$.idOrganisation").value(DEFAULT_ID_ORGANISATION.intValue()))
            .andExpect(jsonPath("$.codeOrganisation").value(DEFAULT_CODE_ORGANISATION))
            .andExpect(jsonPath("$.nomOrganisation").value(DEFAULT_NOM_ORGANISATION))
            .andExpect(jsonPath("$.exercise").value(DEFAULT_EXERCISE))
            .andExpect(jsonPath("$.mois").value(DEFAULT_MOIS))
            .andExpect(jsonPath("$.libelleCentre").value(DEFAULT_LIBELLE_CENTRE))
            .andExpect(jsonPath("$.libelleCourtCentre").value(DEFAULT_LIBELLE_COURT_CENTRE))
            .andExpect(jsonPath("$.ifu").value(DEFAULT_IFU))
            .andExpect(jsonPath("$.raisonSociale").value(DEFAULT_RAISON_SOCIALE))
            .andExpect(jsonPath("$.sigle").value(DEFAULT_SIGLE))
            .andExpect(jsonPath("$.codePoste").value(DEFAULT_CODE_POSTE.intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingJustificatifPaiement() throws Exception {
        // Get the justificatifPaiement
        restJustificatifPaiementMockMvc.perform(get("/api/justificatif-paiements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJustificatifPaiement() throws Exception {
        // Initialize the database
        justificatifPaiementRepository.saveAndFlush(justificatifPaiement);

        int databaseSizeBeforeUpdate = justificatifPaiementRepository.findAll().size();

        // Update the justificatifPaiement
        JustificatifPaiement updatedJustificatifPaiement = justificatifPaiementRepository.findById(justificatifPaiement.getId()).get();
        // Disconnect from session so that the updates on updatedJustificatifPaiement are not directly saved in db
        em.detach(updatedJustificatifPaiement);
        updatedJustificatifPaiement
            .idPaiement(UPDATED_ID_PAIEMENT)
            .dateCreation(UPDATED_DATE_CREATION)
            .referencePaiement(UPDATED_REFERENCE_PAIEMENT)
            .typePaiement(UPDATED_TYPE_PAIEMENT)
            .natureRecette(UPDATED_NATURE_RECETTE)
            .montant(UPDATED_MONTANT)
            .nui(UPDATED_NUI)
            .nomPrenomClient(UPDATED_NOM_PRENOM_CLIENT)
            .idOrganisation(UPDATED_ID_ORGANISATION)
            .codeOrganisation(UPDATED_CODE_ORGANISATION)
            .nomOrganisation(UPDATED_NOM_ORGANISATION)
            .exercise(UPDATED_EXERCISE)
            .mois(UPDATED_MOIS)
            .libelleCentre(UPDATED_LIBELLE_CENTRE)
            .libelleCourtCentre(UPDATED_LIBELLE_COURT_CENTRE)
            .ifu(UPDATED_IFU)
            .raisonSociale(UPDATED_RAISON_SOCIALE)
            .sigle(UPDATED_SIGLE)
            .codePoste(UPDATED_CODE_POSTE)
            .code(UPDATED_CODE)
            .numero(UPDATED_NUMERO);
        JustificatifPaiementDTO justificatifPaiementDTO = justificatifPaiementMapper.toDto(updatedJustificatifPaiement);

        restJustificatifPaiementMockMvc.perform(put("/api/justificatif-paiements")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(justificatifPaiementDTO)))
            .andExpect(status().isOk());

        // Validate the JustificatifPaiement in the database
        List<JustificatifPaiement> justificatifPaiementList = justificatifPaiementRepository.findAll();
        assertThat(justificatifPaiementList).hasSize(databaseSizeBeforeUpdate);
        JustificatifPaiement testJustificatifPaiement = justificatifPaiementList.get(justificatifPaiementList.size() - 1);
        assertThat(testJustificatifPaiement.getIdPaiement()).isEqualTo(UPDATED_ID_PAIEMENT);
        assertThat(testJustificatifPaiement.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testJustificatifPaiement.getReferencePaiement()).isEqualTo(UPDATED_REFERENCE_PAIEMENT);
        assertThat(testJustificatifPaiement.getTypePaiement()).isEqualTo(UPDATED_TYPE_PAIEMENT);
        assertThat(testJustificatifPaiement.getNatureRecette()).isEqualTo(UPDATED_NATURE_RECETTE);
        assertThat(testJustificatifPaiement.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testJustificatifPaiement.getNui()).isEqualTo(UPDATED_NUI);
        assertThat(testJustificatifPaiement.getNomPrenomClient()).isEqualTo(UPDATED_NOM_PRENOM_CLIENT);
        assertThat(testJustificatifPaiement.getIdOrganisation()).isEqualTo(UPDATED_ID_ORGANISATION);
        assertThat(testJustificatifPaiement.getCodeOrganisation()).isEqualTo(UPDATED_CODE_ORGANISATION);
        assertThat(testJustificatifPaiement.getNomOrganisation()).isEqualTo(UPDATED_NOM_ORGANISATION);
        assertThat(testJustificatifPaiement.getExercise()).isEqualTo(UPDATED_EXERCISE);
        assertThat(testJustificatifPaiement.getMois()).isEqualTo(UPDATED_MOIS);
        assertThat(testJustificatifPaiement.getLibelleCentre()).isEqualTo(UPDATED_LIBELLE_CENTRE);
        assertThat(testJustificatifPaiement.getLibelleCourtCentre()).isEqualTo(UPDATED_LIBELLE_COURT_CENTRE);
        assertThat(testJustificatifPaiement.getIfu()).isEqualTo(UPDATED_IFU);
        assertThat(testJustificatifPaiement.getRaisonSociale()).isEqualTo(UPDATED_RAISON_SOCIALE);
        assertThat(testJustificatifPaiement.getSigle()).isEqualTo(UPDATED_SIGLE);
        assertThat(testJustificatifPaiement.getCodePoste()).isEqualTo(UPDATED_CODE_POSTE);
        assertThat(testJustificatifPaiement.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testJustificatifPaiement.getNumero()).isEqualTo(UPDATED_NUMERO);
    }

    @Test
    @Transactional
    public void updateNonExistingJustificatifPaiement() throws Exception {
        int databaseSizeBeforeUpdate = justificatifPaiementRepository.findAll().size();

        // Create the JustificatifPaiement
        JustificatifPaiementDTO justificatifPaiementDTO = justificatifPaiementMapper.toDto(justificatifPaiement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJustificatifPaiementMockMvc.perform(put("/api/justificatif-paiements")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(justificatifPaiementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the JustificatifPaiement in the database
        List<JustificatifPaiement> justificatifPaiementList = justificatifPaiementRepository.findAll();
        assertThat(justificatifPaiementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteJustificatifPaiement() throws Exception {
        // Initialize the database
        justificatifPaiementRepository.saveAndFlush(justificatifPaiement);

        int databaseSizeBeforeDelete = justificatifPaiementRepository.findAll().size();

        // Delete the justificatifPaiement
        restJustificatifPaiementMockMvc.perform(delete("/api/justificatif-paiements/{id}", justificatifPaiement.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<JustificatifPaiement> justificatifPaiementList = justificatifPaiementRepository.findAll();
        assertThat(justificatifPaiementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
