package com.nspl.app.web.rest;

import static com.nspl.app.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import javax.persistence.EntityManager;

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

import com.nspl.app.AgreeApplicationApp;
import com.nspl.app.domain.SourceProfiles;
import com.nspl.app.repository.FileTemplatesRepository;
import com.nspl.app.repository.SourceConnectionDetailsRepository;
import com.nspl.app.repository.SourceProfileFileAssignmentsRepository;
import com.nspl.app.repository.SourceProfilesRepository;
import com.nspl.app.repository.search.SourceProfileFileAssignmentsSearchRepository;
import com.nspl.app.repository.search.SourceProfilesSearchRepository;
import com.nspl.app.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the SourceProfilesResource REST controller.
 *
 * @see SourceProfilesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class SourceProfilesResourceIntTest {

    private static final String DEFAULT_SOURCE_PROFILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_PROFILE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END_DATE =  ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_ENABLED_FLAG = false;
    private static final Boolean UPDATED_ENABLED_FLAG = true;

    private static final Long DEFAULT_CONNECTION_ID = 1L;
    private static final Long UPDATED_CONNECTION_ID = 2L;

    private static final Long DEFAULT_TENANT_ID = 1L;
    private static final Long UPDATED_TENANT_ID = 2L;

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private SourceProfilesRepository sourceProfilesRepository;

    @Autowired
    private SourceProfilesSearchRepository sourceProfilesSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSourceProfilesMockMvc;

    private SourceProfiles sourceProfiles;
    
    private SourceConnectionDetailsRepository sourceConnectionDetailsRepository;
    
    private SourceProfileFileAssignmentsRepository sourceProfileFileAssignmentsRepository;

    private SourceProfileFileAssignmentsSearchRepository sourceProfileFileAssignmentsSearchRepository;
    
    private FileTemplatesRepository fileTemplatesRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SourceProfilesResource sourceProfilesResource = new SourceProfilesResource(sourceProfilesRepository,
        		sourceProfilesSearchRepository,sourceConnectionDetailsRepository,sourceProfileFileAssignmentsRepository,sourceProfileFileAssignmentsSearchRepository,fileTemplatesRepository);
        this.restSourceProfilesMockMvc = MockMvcBuilders.standaloneSetup(sourceProfilesResource)
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
    public static SourceProfiles createEntity(EntityManager em) {
        SourceProfiles sourceProfiles = new SourceProfiles()
            .sourceProfileName(DEFAULT_SOURCE_PROFILE_NAME)
            .description(DEFAULT_DESCRIPTION)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .enabledFlag(DEFAULT_ENABLED_FLAG)
            .connectionId(DEFAULT_CONNECTION_ID)
            .tenantId(DEFAULT_TENANT_ID)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE);
        return sourceProfiles;
    }

    @Before
    public void initTest() {
        sourceProfilesSearchRepository.deleteAll();
        sourceProfiles = createEntity(em);
    }

    @Test
    @Transactional
    public void createSourceProfiles() throws Exception {
        int databaseSizeBeforeCreate = sourceProfilesRepository.findAll().size();

        // Create the SourceProfiles
        restSourceProfilesMockMvc.perform(post("/api/source-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sourceProfiles)))
            .andExpect(status().isCreated());

        // Validate the SourceProfiles in the database
        List<SourceProfiles> sourceProfilesList = sourceProfilesRepository.findAll();
        assertThat(sourceProfilesList).hasSize(databaseSizeBeforeCreate + 1);
        SourceProfiles testSourceProfiles = sourceProfilesList.get(sourceProfilesList.size() - 1);
        assertThat(testSourceProfiles.getSourceProfileName()).isEqualTo(DEFAULT_SOURCE_PROFILE_NAME);
        assertThat(testSourceProfiles.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSourceProfiles.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testSourceProfiles.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testSourceProfiles.isEnabledFlag()).isEqualTo(DEFAULT_ENABLED_FLAG);
        assertThat(testSourceProfiles.getConnectionId()).isEqualTo(DEFAULT_CONNECTION_ID);
        assertThat(testSourceProfiles.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testSourceProfiles.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSourceProfiles.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSourceProfiles.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testSourceProfiles.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);

        // Validate the SourceProfiles in Elasticsearch
        SourceProfiles sourceProfilesEs = sourceProfilesSearchRepository.findOne(testSourceProfiles.getId());
        assertThat(sourceProfilesEs).isEqualToComparingFieldByField(testSourceProfiles);
    }

    @Test
    @Transactional
    public void createSourceProfilesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sourceProfilesRepository.findAll().size();

        // Create the SourceProfiles with an existing ID
        sourceProfiles.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSourceProfilesMockMvc.perform(post("/api/source-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sourceProfiles)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<SourceProfiles> sourceProfilesList = sourceProfilesRepository.findAll();
        assertThat(sourceProfilesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSourceProfiles() throws Exception {
        // Initialize the database
        sourceProfilesRepository.saveAndFlush(sourceProfiles);

        // Get all the sourceProfilesList
        restSourceProfilesMockMvc.perform(get("/api/source-profiles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sourceProfiles.getId().intValue())))
            .andExpect(jsonPath("$.[*].sourceProfileName").value(hasItem(DEFAULT_SOURCE_PROFILE_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].enabledFlag").value(hasItem(DEFAULT_ENABLED_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].connectionId").value(hasItem(DEFAULT_CONNECTION_ID.intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void getSourceProfiles() throws Exception {
        // Initialize the database
        sourceProfilesRepository.saveAndFlush(sourceProfiles);

        // Get the sourceProfiles
        restSourceProfilesMockMvc.perform(get("/api/source-profiles/{id}", sourceProfiles.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sourceProfiles.getId().intValue()))
            .andExpect(jsonPath("$.sourceProfileName").value(DEFAULT_SOURCE_PROFILE_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.enabledFlag").value(DEFAULT_ENABLED_FLAG.booleanValue()))
            .andExpect(jsonPath("$.connectionId").value(DEFAULT_CONNECTION_ID.intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID.intValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingSourceProfiles() throws Exception {
        // Get the sourceProfiles
        restSourceProfilesMockMvc.perform(get("/api/source-profiles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSourceProfiles() throws Exception {
        // Initialize the database
        sourceProfilesRepository.saveAndFlush(sourceProfiles);
        sourceProfilesSearchRepository.save(sourceProfiles);
        int databaseSizeBeforeUpdate = sourceProfilesRepository.findAll().size();

        // Update the sourceProfiles
        SourceProfiles updatedSourceProfiles = sourceProfilesRepository.findOne(sourceProfiles.getId());
        updatedSourceProfiles
            .sourceProfileName(UPDATED_SOURCE_PROFILE_NAME)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .enabledFlag(UPDATED_ENABLED_FLAG)
            .connectionId(UPDATED_CONNECTION_ID)
            .tenantId(UPDATED_TENANT_ID)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE);

        restSourceProfilesMockMvc.perform(put("/api/source-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSourceProfiles)))
            .andExpect(status().isOk());

        // Validate the SourceProfiles in the database
        List<SourceProfiles> sourceProfilesList = sourceProfilesRepository.findAll();
        assertThat(sourceProfilesList).hasSize(databaseSizeBeforeUpdate);
        SourceProfiles testSourceProfiles = sourceProfilesList.get(sourceProfilesList.size() - 1);
        assertThat(testSourceProfiles.getSourceProfileName()).isEqualTo(UPDATED_SOURCE_PROFILE_NAME);
        assertThat(testSourceProfiles.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSourceProfiles.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testSourceProfiles.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testSourceProfiles.isEnabledFlag()).isEqualTo(UPDATED_ENABLED_FLAG);
        assertThat(testSourceProfiles.getConnectionId()).isEqualTo(UPDATED_CONNECTION_ID);
        assertThat(testSourceProfiles.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testSourceProfiles.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSourceProfiles.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSourceProfiles.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testSourceProfiles.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);

        // Validate the SourceProfiles in Elasticsearch
        SourceProfiles sourceProfilesEs = sourceProfilesSearchRepository.findOne(testSourceProfiles.getId());
        assertThat(sourceProfilesEs).isEqualToComparingFieldByField(testSourceProfiles);
    }

    @Test
    @Transactional
    public void updateNonExistingSourceProfiles() throws Exception {
        int databaseSizeBeforeUpdate = sourceProfilesRepository.findAll().size();

        // Create the SourceProfiles

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSourceProfilesMockMvc.perform(put("/api/source-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sourceProfiles)))
            .andExpect(status().isCreated());

        // Validate the SourceProfiles in the database
        List<SourceProfiles> sourceProfilesList = sourceProfilesRepository.findAll();
        assertThat(sourceProfilesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSourceProfiles() throws Exception {
        // Initialize the database
        sourceProfilesRepository.saveAndFlush(sourceProfiles);
        sourceProfilesSearchRepository.save(sourceProfiles);
        int databaseSizeBeforeDelete = sourceProfilesRepository.findAll().size();

        // Get the sourceProfiles
        restSourceProfilesMockMvc.perform(delete("/api/source-profiles/{id}", sourceProfiles.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean sourceProfilesExistsInEs = sourceProfilesSearchRepository.exists(sourceProfiles.getId());
        assertThat(sourceProfilesExistsInEs).isFalse();

        // Validate the database is empty
        List<SourceProfiles> sourceProfilesList = sourceProfilesRepository.findAll();
        assertThat(sourceProfilesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSourceProfiles() throws Exception {
        // Initialize the database
        sourceProfilesRepository.saveAndFlush(sourceProfiles);
        sourceProfilesSearchRepository.save(sourceProfiles);

        // Search the sourceProfiles
        restSourceProfilesMockMvc.perform(get("/api/_search/source-profiles?query=id:" + sourceProfiles.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sourceProfiles.getId().intValue())))
            .andExpect(jsonPath("$.[*].sourceProfileName").value(hasItem(DEFAULT_SOURCE_PROFILE_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].enabledFlag").value(hasItem(DEFAULT_ENABLED_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].connectionId").value(hasItem(DEFAULT_CONNECTION_ID.intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SourceProfiles.class);
        SourceProfiles sourceProfiles1 = new SourceProfiles();
        sourceProfiles1.setId(1L);
        SourceProfiles sourceProfiles2 = new SourceProfiles();
        sourceProfiles2.setId(sourceProfiles1.getId());
        assertThat(sourceProfiles1).isEqualTo(sourceProfiles2);
        sourceProfiles2.setId(2L);
        assertThat(sourceProfiles1).isNotEqualTo(sourceProfiles2);
        sourceProfiles1.setId(null);
        assertThat(sourceProfiles1).isNotEqualTo(sourceProfiles2);
    }
}
