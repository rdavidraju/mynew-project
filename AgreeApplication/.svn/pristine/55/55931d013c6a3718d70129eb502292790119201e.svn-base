package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.ApplicationPrograms;
import com.nspl.app.repository.ApplicationProgramsRepository;
import com.nspl.app.repository.search.ApplicationProgramsSearchRepository;
import com.nspl.app.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.nspl.app.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ApplicationProgramsResource REST controller.
 *
 * @see ApplicationProgramsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class ApplicationProgramsResourceIntTest {

    private static final String DEFAULT_PRGM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PRGM_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_TENANT_ID = 1L;
    private static final Long UPDATED_TENANT_ID = 2L;

    private static final String DEFAULT_PRGM_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_PRGM_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_GENERATED_PATH = "AAAAAAAAAA";
    private static final String UPDATED_GENERATED_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_TARGET_PATH = "AAAAAAAAAA";
    private static final String UPDATED_TARGET_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_PRGM_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PRGM_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_PRGM_OR_CLASS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PRGM_OR_CLASS_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PRGM_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_PRGM_TYPE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLE = false;
    private static final Boolean UPDATED_ENABLE = true;

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_LAST_UPDATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    @Autowired
    private ApplicationProgramsRepository applicationProgramsRepository;

    @Autowired
    private ApplicationProgramsSearchRepository applicationProgramsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restApplicationProgramsMockMvc;

    private ApplicationPrograms applicationPrograms;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ApplicationProgramsResource applicationProgramsResource = new ApplicationProgramsResource(applicationProgramsRepository, applicationProgramsSearchRepository);
        this.restApplicationProgramsMockMvc = MockMvcBuilders.standaloneSetup(applicationProgramsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApplicationPrograms createEntity(EntityManager em) {
        ApplicationPrograms applicationPrograms = new ApplicationPrograms()
            .prgmName(DEFAULT_PRGM_NAME)
            .tenantId(DEFAULT_TENANT_ID)
            .prgmDescription(DEFAULT_PRGM_DESCRIPTION)
            .generatedPath(DEFAULT_GENERATED_PATH)
            .targetPath(DEFAULT_TARGET_PATH)
            .prgmPath(DEFAULT_PRGM_PATH)
            .prgmOrClassName(DEFAULT_PRGM_OR_CLASS_NAME)
            .prgmType(DEFAULT_PRGM_TYPE)
            .enable(DEFAULT_ENABLE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .creationDate(DEFAULT_CREATION_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastUpdationDate(DEFAULT_LAST_UPDATION_DATE)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY);
        return applicationPrograms;
    }

    @Before
    public void initTest() {
        applicationProgramsSearchRepository.deleteAll();
        applicationPrograms = createEntity(em);
    }

    @Test
    @Transactional
    public void createApplicationPrograms() throws Exception {
        int databaseSizeBeforeCreate = applicationProgramsRepository.findAll().size();

        // Create the ApplicationPrograms
        restApplicationProgramsMockMvc.perform(post("/api/application-programs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationPrograms)))
            .andExpect(status().isCreated());

        // Validate the ApplicationPrograms in the database
        List<ApplicationPrograms> applicationProgramsList = applicationProgramsRepository.findAll();
        assertThat(applicationProgramsList).hasSize(databaseSizeBeforeCreate + 1);
        ApplicationPrograms testApplicationPrograms = applicationProgramsList.get(applicationProgramsList.size() - 1);
        assertThat(testApplicationPrograms.getPrgmName()).isEqualTo(DEFAULT_PRGM_NAME);
        assertThat(testApplicationPrograms.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testApplicationPrograms.getPrgmDescription()).isEqualTo(DEFAULT_PRGM_DESCRIPTION);
        assertThat(testApplicationPrograms.getGeneratedPath()).isEqualTo(DEFAULT_GENERATED_PATH);
        assertThat(testApplicationPrograms.getTargetPath()).isEqualTo(DEFAULT_TARGET_PATH);
        assertThat(testApplicationPrograms.getPrgmPath()).isEqualTo(DEFAULT_PRGM_PATH);
        assertThat(testApplicationPrograms.getPrgmOrClassName()).isEqualTo(DEFAULT_PRGM_OR_CLASS_NAME);
        assertThat(testApplicationPrograms.getPrgmType()).isEqualTo(DEFAULT_PRGM_TYPE);
        assertThat(testApplicationPrograms.isEnable()).isEqualTo(DEFAULT_ENABLE);
        assertThat(testApplicationPrograms.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testApplicationPrograms.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testApplicationPrograms.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testApplicationPrograms.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testApplicationPrograms.getLastUpdationDate()).isEqualTo(DEFAULT_LAST_UPDATION_DATE);
        assertThat(testApplicationPrograms.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);

        // Validate the ApplicationPrograms in Elasticsearch
        ApplicationPrograms applicationProgramsEs = applicationProgramsSearchRepository.findOne(testApplicationPrograms.getId());
        assertThat(applicationProgramsEs).isEqualToComparingFieldByField(testApplicationPrograms);
    }

    @Test
    @Transactional
    public void createApplicationProgramsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = applicationProgramsRepository.findAll().size();

        // Create the ApplicationPrograms with an existing ID
        applicationPrograms.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplicationProgramsMockMvc.perform(post("/api/application-programs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationPrograms)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ApplicationPrograms> applicationProgramsList = applicationProgramsRepository.findAll();
        assertThat(applicationProgramsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllApplicationPrograms() throws Exception {
        // Initialize the database
        applicationProgramsRepository.saveAndFlush(applicationPrograms);

        // Get all the applicationProgramsList
        restApplicationProgramsMockMvc.perform(get("/api/application-programs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicationPrograms.getId().intValue())))
            .andExpect(jsonPath("$.[*].prgmName").value(hasItem(DEFAULT_PRGM_NAME.toString())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].prgmDescription").value(hasItem(DEFAULT_PRGM_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].generatedPath").value(hasItem(DEFAULT_GENERATED_PATH.toString())))
            .andExpect(jsonPath("$.[*].targetPath").value(hasItem(DEFAULT_TARGET_PATH.toString())))
            .andExpect(jsonPath("$.[*].prgmPath").value(hasItem(DEFAULT_PRGM_PATH.toString())))
            .andExpect(jsonPath("$.[*].prgmOrClassName").value(hasItem(DEFAULT_PRGM_OR_CLASS_NAME.toString())))
            .andExpect(jsonPath("$.[*].prgmType").value(hasItem(DEFAULT_PRGM_TYPE.toString())))
            .andExpect(jsonPath("$.[*].enable").value(hasItem(DEFAULT_ENABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdationDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATION_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())));
    }

    @Test
    @Transactional
    public void getApplicationPrograms() throws Exception {
        // Initialize the database
        applicationProgramsRepository.saveAndFlush(applicationPrograms);

        // Get the applicationPrograms
        restApplicationProgramsMockMvc.perform(get("/api/application-programs/{id}", applicationPrograms.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(applicationPrograms.getId().intValue()))
            .andExpect(jsonPath("$.prgmName").value(DEFAULT_PRGM_NAME.toString()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID.intValue()))
            .andExpect(jsonPath("$.prgmDescription").value(DEFAULT_PRGM_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.generatedPath").value(DEFAULT_GENERATED_PATH.toString()))
            .andExpect(jsonPath("$.targetPath").value(DEFAULT_TARGET_PATH.toString()))
            .andExpect(jsonPath("$.prgmPath").value(DEFAULT_PRGM_PATH.toString()))
            .andExpect(jsonPath("$.prgmOrClassName").value(DEFAULT_PRGM_OR_CLASS_NAME.toString()))
            .andExpect(jsonPath("$.prgmType").value(DEFAULT_PRGM_TYPE.toString()))
            .andExpect(jsonPath("$.enable").value(DEFAULT_ENABLE.booleanValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.creationDate").value(sameInstant(DEFAULT_CREATION_DATE)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdationDate").value(sameInstant(DEFAULT_LAST_UPDATION_DATE)))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingApplicationPrograms() throws Exception {
        // Get the applicationPrograms
        restApplicationProgramsMockMvc.perform(get("/api/application-programs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApplicationPrograms() throws Exception {
        // Initialize the database
        applicationProgramsRepository.saveAndFlush(applicationPrograms);
        applicationProgramsSearchRepository.save(applicationPrograms);
        int databaseSizeBeforeUpdate = applicationProgramsRepository.findAll().size();

        // Update the applicationPrograms
        ApplicationPrograms updatedApplicationPrograms = applicationProgramsRepository.findOne(applicationPrograms.getId());
        updatedApplicationPrograms
            .prgmName(UPDATED_PRGM_NAME)
            .tenantId(UPDATED_TENANT_ID)
            .prgmDescription(UPDATED_PRGM_DESCRIPTION)
            .generatedPath(UPDATED_GENERATED_PATH)
            .targetPath(UPDATED_TARGET_PATH)
            .prgmPath(UPDATED_PRGM_PATH)
            .prgmOrClassName(UPDATED_PRGM_OR_CLASS_NAME)
            .prgmType(UPDATED_PRGM_TYPE)
            .enable(UPDATED_ENABLE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .creationDate(UPDATED_CREATION_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastUpdationDate(UPDATED_LAST_UPDATION_DATE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY);

        restApplicationProgramsMockMvc.perform(put("/api/application-programs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedApplicationPrograms)))
            .andExpect(status().isOk());

        // Validate the ApplicationPrograms in the database
        List<ApplicationPrograms> applicationProgramsList = applicationProgramsRepository.findAll();
        assertThat(applicationProgramsList).hasSize(databaseSizeBeforeUpdate);
        ApplicationPrograms testApplicationPrograms = applicationProgramsList.get(applicationProgramsList.size() - 1);
        assertThat(testApplicationPrograms.getPrgmName()).isEqualTo(UPDATED_PRGM_NAME);
        assertThat(testApplicationPrograms.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testApplicationPrograms.getPrgmDescription()).isEqualTo(UPDATED_PRGM_DESCRIPTION);
        assertThat(testApplicationPrograms.getGeneratedPath()).isEqualTo(UPDATED_GENERATED_PATH);
        assertThat(testApplicationPrograms.getTargetPath()).isEqualTo(UPDATED_TARGET_PATH);
        assertThat(testApplicationPrograms.getPrgmPath()).isEqualTo(UPDATED_PRGM_PATH);
        assertThat(testApplicationPrograms.getPrgmOrClassName()).isEqualTo(UPDATED_PRGM_OR_CLASS_NAME);
        assertThat(testApplicationPrograms.getPrgmType()).isEqualTo(UPDATED_PRGM_TYPE);
        assertThat(testApplicationPrograms.isEnable()).isEqualTo(UPDATED_ENABLE);
        assertThat(testApplicationPrograms.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testApplicationPrograms.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testApplicationPrograms.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testApplicationPrograms.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testApplicationPrograms.getLastUpdationDate()).isEqualTo(UPDATED_LAST_UPDATION_DATE);
        assertThat(testApplicationPrograms.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);

        // Validate the ApplicationPrograms in Elasticsearch
        ApplicationPrograms applicationProgramsEs = applicationProgramsSearchRepository.findOne(testApplicationPrograms.getId());
        assertThat(applicationProgramsEs).isEqualToComparingFieldByField(testApplicationPrograms);
    }

    @Test
    @Transactional
    public void updateNonExistingApplicationPrograms() throws Exception {
        int databaseSizeBeforeUpdate = applicationProgramsRepository.findAll().size();

        // Create the ApplicationPrograms

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restApplicationProgramsMockMvc.perform(put("/api/application-programs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationPrograms)))
            .andExpect(status().isCreated());

        // Validate the ApplicationPrograms in the database
        List<ApplicationPrograms> applicationProgramsList = applicationProgramsRepository.findAll();
        assertThat(applicationProgramsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteApplicationPrograms() throws Exception {
        // Initialize the database
        applicationProgramsRepository.saveAndFlush(applicationPrograms);
        applicationProgramsSearchRepository.save(applicationPrograms);
        int databaseSizeBeforeDelete = applicationProgramsRepository.findAll().size();

        // Get the applicationPrograms
        restApplicationProgramsMockMvc.perform(delete("/api/application-programs/{id}", applicationPrograms.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean applicationProgramsExistsInEs = applicationProgramsSearchRepository.exists(applicationPrograms.getId());
        assertThat(applicationProgramsExistsInEs).isFalse();

        // Validate the database is empty
        List<ApplicationPrograms> applicationProgramsList = applicationProgramsRepository.findAll();
        assertThat(applicationProgramsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchApplicationPrograms() throws Exception {
        // Initialize the database
        applicationProgramsRepository.saveAndFlush(applicationPrograms);
        applicationProgramsSearchRepository.save(applicationPrograms);

        // Search the applicationPrograms
        restApplicationProgramsMockMvc.perform(get("/api/_search/application-programs?query=id:" + applicationPrograms.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicationPrograms.getId().intValue())))
            .andExpect(jsonPath("$.[*].prgmName").value(hasItem(DEFAULT_PRGM_NAME.toString())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].prgmDescription").value(hasItem(DEFAULT_PRGM_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].generatedPath").value(hasItem(DEFAULT_GENERATED_PATH.toString())))
            .andExpect(jsonPath("$.[*].targetPath").value(hasItem(DEFAULT_TARGET_PATH.toString())))
            .andExpect(jsonPath("$.[*].prgmPath").value(hasItem(DEFAULT_PRGM_PATH.toString())))
            .andExpect(jsonPath("$.[*].prgmOrClassName").value(hasItem(DEFAULT_PRGM_OR_CLASS_NAME.toString())))
            .andExpect(jsonPath("$.[*].prgmType").value(hasItem(DEFAULT_PRGM_TYPE.toString())))
            .andExpect(jsonPath("$.[*].enable").value(hasItem(DEFAULT_ENABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdationDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATION_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicationPrograms.class);
    }
}
