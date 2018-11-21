package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;
import com.nspl.app.domain.SourceProfileFileAssignments;
import com.nspl.app.repository.FileTemplatesRepository;
import com.nspl.app.repository.LookUpCodeRepository;
import com.nspl.app.repository.SourceProfileFileAssignmentsRepository;
import com.nspl.app.repository.SourceProfilesRepository;
import com.nspl.app.repository.search.SourceProfileFileAssignmentsSearchRepository;
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
 * Test class for the SourceProfileFileAssignmentsResource REST controller.
 *
 * @see SourceProfileFileAssignmentsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class SourceProfileFileAssignmentsResourceIntTest {

    private static final Long DEFAULT_SOURCE_PROFILE_ID = 1L;
    private static final Long UPDATED_SOURCE_PROFILE_ID = 2L;

    private static final String DEFAULT_FILE_NAME_FORMAT = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME_FORMAT = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_FILE_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_EXTENSION = "AAAAAAAAAA";
    private static final String UPDATED_FILE_EXTENSION = "BBBBBBBBBB";

    private static final String DEFAULT_FREQUENCY_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_FREQUENCY_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DUE_TIME = "AAAAA";
    private static final String UPDATED_DUE_TIME = "BBBBB";

    private static final Integer DEFAULT_DAY = 1;
    private static final Integer UPDATED_DAY = 2;

    private static final String DEFAULT_SOURCE_DIRECTORY_PATH = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_DIRECTORY_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_LOCAL_DIRECTORY_PATH = "AAAAAAAAAA";
    private static final String UPDATED_LOCAL_DIRECTORY_PATH = "BBBBBBBBBB";

    private static final Long DEFAULT_TEMPLATE_ID = 1L;
    private static final Long UPDATED_TEMPLATE_ID = 2L;

    private static final Boolean DEFAULT_ENABLED_FLAG = false;
    private static final Boolean UPDATED_ENABLED_FLAG = true;

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private SourceProfileFileAssignmentsRepository sourceProfileFileAssignmentsRepository;

    @Autowired
    private SourceProfileFileAssignmentsSearchRepository sourceProfileFileAssignmentsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSourceProfileFileAssignmentsMockMvc;

    private SourceProfileFileAssignments sourceProfileFileAssignments;
    
    
    private SourceProfilesRepository sourceProfilesRepository;
    
    private FileTemplatesRepository fileTemplatesRepository;
    private  LookUpCodeRepository lookUpCodeRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SourceProfileFileAssignmentsResource sourceProfileFileAssignmentsResource = 
        		new SourceProfileFileAssignmentsResource(sourceProfileFileAssignmentsRepository,
        				sourceProfileFileAssignmentsSearchRepository,sourceProfilesRepository,
        				fileTemplatesRepository,lookUpCodeRepository);
        this.restSourceProfileFileAssignmentsMockMvc = MockMvcBuilders.standaloneSetup(sourceProfileFileAssignmentsResource)
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
    public static SourceProfileFileAssignments createEntity(EntityManager em) {
        SourceProfileFileAssignments sourceProfileFileAssignments = new SourceProfileFileAssignments()
            .sourceProfileId(DEFAULT_SOURCE_PROFILE_ID)
            .fileNameFormat(DEFAULT_FILE_NAME_FORMAT)
            .fileDescription(DEFAULT_FILE_DESCRIPTION)
            .fileExtension(DEFAULT_FILE_EXTENSION)
            .frequencyType(DEFAULT_FREQUENCY_TYPE)
            .dueTime(DEFAULT_DUE_TIME)
            .day(DEFAULT_DAY)
            .sourceDirectoryPath(DEFAULT_SOURCE_DIRECTORY_PATH)
            .localDirectoryPath(DEFAULT_LOCAL_DIRECTORY_PATH)
            .templateId(DEFAULT_TEMPLATE_ID)
            .enabledFlag(DEFAULT_ENABLED_FLAG)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE);
        return sourceProfileFileAssignments;
    }

    @Before
    public void initTest() {
        sourceProfileFileAssignmentsSearchRepository.deleteAll();
        sourceProfileFileAssignments = createEntity(em);
    }

    @Test
    @Transactional
    public void createSourceProfileFileAssignments() throws Exception {
        int databaseSizeBeforeCreate = sourceProfileFileAssignmentsRepository.findAll().size();

        // Create the SourceProfileFileAssignments
        restSourceProfileFileAssignmentsMockMvc.perform(post("/api/source-profile-file-assignments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sourceProfileFileAssignments)))
            .andExpect(status().isCreated());

        // Validate the SourceProfileFileAssignments in the database
        List<SourceProfileFileAssignments> sourceProfileFileAssignmentsList = sourceProfileFileAssignmentsRepository.findAll();
        assertThat(sourceProfileFileAssignmentsList).hasSize(databaseSizeBeforeCreate + 1);
        SourceProfileFileAssignments testSourceProfileFileAssignments = sourceProfileFileAssignmentsList.get(sourceProfileFileAssignmentsList.size() - 1);
        assertThat(testSourceProfileFileAssignments.getSourceProfileId()).isEqualTo(DEFAULT_SOURCE_PROFILE_ID);
        assertThat(testSourceProfileFileAssignments.getFileNameFormat()).isEqualTo(DEFAULT_FILE_NAME_FORMAT);
        assertThat(testSourceProfileFileAssignments.getFileDescription()).isEqualTo(DEFAULT_FILE_DESCRIPTION);
        assertThat(testSourceProfileFileAssignments.getFileExtension()).isEqualTo(DEFAULT_FILE_EXTENSION);
        assertThat(testSourceProfileFileAssignments.getFrequencyType()).isEqualTo(DEFAULT_FREQUENCY_TYPE);
        assertThat(testSourceProfileFileAssignments.getDueTime()).isEqualTo(DEFAULT_DUE_TIME);
        assertThat(testSourceProfileFileAssignments.getDay()).isEqualTo(DEFAULT_DAY);
        assertThat(testSourceProfileFileAssignments.getSourceDirectoryPath()).isEqualTo(DEFAULT_SOURCE_DIRECTORY_PATH);
        assertThat(testSourceProfileFileAssignments.getLocalDirectoryPath()).isEqualTo(DEFAULT_LOCAL_DIRECTORY_PATH);
        assertThat(testSourceProfileFileAssignments.getTemplateId()).isEqualTo(DEFAULT_TEMPLATE_ID);
        assertThat(testSourceProfileFileAssignments.isEnabledFlag()).isEqualTo(DEFAULT_ENABLED_FLAG);
        assertThat(testSourceProfileFileAssignments.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSourceProfileFileAssignments.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSourceProfileFileAssignments.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testSourceProfileFileAssignments.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);

        // Validate the SourceProfileFileAssignments in Elasticsearch
        SourceProfileFileAssignments sourceProfileFileAssignmentsEs = sourceProfileFileAssignmentsSearchRepository.findOne(testSourceProfileFileAssignments.getId());
        assertThat(sourceProfileFileAssignmentsEs).isEqualToComparingFieldByField(testSourceProfileFileAssignments);
    }

    @Test
    @Transactional
    public void createSourceProfileFileAssignmentsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sourceProfileFileAssignmentsRepository.findAll().size();

        // Create the SourceProfileFileAssignments with an existing ID
        sourceProfileFileAssignments.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSourceProfileFileAssignmentsMockMvc.perform(post("/api/source-profile-file-assignments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sourceProfileFileAssignments)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<SourceProfileFileAssignments> sourceProfileFileAssignmentsList = sourceProfileFileAssignmentsRepository.findAll();
        assertThat(sourceProfileFileAssignmentsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSourceProfileFileAssignments() throws Exception {
        // Initialize the database
        sourceProfileFileAssignmentsRepository.saveAndFlush(sourceProfileFileAssignments);

        // Get all the sourceProfileFileAssignmentsList
        restSourceProfileFileAssignmentsMockMvc.perform(get("/api/source-profile-file-assignments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sourceProfileFileAssignments.getId().intValue())))
            .andExpect(jsonPath("$.[*].sourceProfileId").value(hasItem(DEFAULT_SOURCE_PROFILE_ID.intValue())))
            .andExpect(jsonPath("$.[*].fileNameFormat").value(hasItem(DEFAULT_FILE_NAME_FORMAT.toString())))
            .andExpect(jsonPath("$.[*].fileDescription").value(hasItem(DEFAULT_FILE_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].fileExtension").value(hasItem(DEFAULT_FILE_EXTENSION.toString())))
            .andExpect(jsonPath("$.[*].frequencyType").value(hasItem(DEFAULT_FREQUENCY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].dueTime").value(hasItem(DEFAULT_DUE_TIME.toString())))
            .andExpect(jsonPath("$.[*].day").value(hasItem(DEFAULT_DAY)))
            .andExpect(jsonPath("$.[*].sourceDirectoryPath").value(hasItem(DEFAULT_SOURCE_DIRECTORY_PATH.toString())))
            .andExpect(jsonPath("$.[*].localDirectoryPath").value(hasItem(DEFAULT_LOCAL_DIRECTORY_PATH.toString())))
            .andExpect(jsonPath("$.[*].templateId").value(hasItem(DEFAULT_TEMPLATE_ID.intValue())))
            .andExpect(jsonPath("$.[*].enabledFlag").value(hasItem(DEFAULT_ENABLED_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void getSourceProfileFileAssignments() throws Exception {
        // Initialize the database
        sourceProfileFileAssignmentsRepository.saveAndFlush(sourceProfileFileAssignments);

        // Get the sourceProfileFileAssignments
        restSourceProfileFileAssignmentsMockMvc.perform(get("/api/source-profile-file-assignments/{id}", sourceProfileFileAssignments.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sourceProfileFileAssignments.getId().intValue()))
            .andExpect(jsonPath("$.sourceProfileId").value(DEFAULT_SOURCE_PROFILE_ID.intValue()))
            .andExpect(jsonPath("$.fileNameFormat").value(DEFAULT_FILE_NAME_FORMAT.toString()))
            .andExpect(jsonPath("$.fileDescription").value(DEFAULT_FILE_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.fileExtension").value(DEFAULT_FILE_EXTENSION.toString()))
            .andExpect(jsonPath("$.frequencyType").value(DEFAULT_FREQUENCY_TYPE.toString()))
            .andExpect(jsonPath("$.dueTime").value(DEFAULT_DUE_TIME.toString()))
            .andExpect(jsonPath("$.day").value(DEFAULT_DAY))
            .andExpect(jsonPath("$.sourceDirectoryPath").value(DEFAULT_SOURCE_DIRECTORY_PATH.toString()))
            .andExpect(jsonPath("$.localDirectoryPath").value(DEFAULT_LOCAL_DIRECTORY_PATH.toString()))
            .andExpect(jsonPath("$.templateId").value(DEFAULT_TEMPLATE_ID.intValue()))
            .andExpect(jsonPath("$.enabledFlag").value(DEFAULT_ENABLED_FLAG.booleanValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingSourceProfileFileAssignments() throws Exception {
        // Get the sourceProfileFileAssignments
        restSourceProfileFileAssignmentsMockMvc.perform(get("/api/source-profile-file-assignments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSourceProfileFileAssignments() throws Exception {
        // Initialize the database
        sourceProfileFileAssignmentsRepository.saveAndFlush(sourceProfileFileAssignments);
        sourceProfileFileAssignmentsSearchRepository.save(sourceProfileFileAssignments);
        int databaseSizeBeforeUpdate = sourceProfileFileAssignmentsRepository.findAll().size();

        // Update the sourceProfileFileAssignments
        SourceProfileFileAssignments updatedSourceProfileFileAssignments = sourceProfileFileAssignmentsRepository.findOne(sourceProfileFileAssignments.getId());
        updatedSourceProfileFileAssignments
            .sourceProfileId(UPDATED_SOURCE_PROFILE_ID)
            .fileNameFormat(UPDATED_FILE_NAME_FORMAT)
            .fileDescription(UPDATED_FILE_DESCRIPTION)
            .fileExtension(UPDATED_FILE_EXTENSION)
            .frequencyType(UPDATED_FREQUENCY_TYPE)
            .dueTime(UPDATED_DUE_TIME)
            .day(UPDATED_DAY)
            .sourceDirectoryPath(UPDATED_SOURCE_DIRECTORY_PATH)
            .localDirectoryPath(UPDATED_LOCAL_DIRECTORY_PATH)
            .templateId(UPDATED_TEMPLATE_ID)
            .enabledFlag(UPDATED_ENABLED_FLAG)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE);

        restSourceProfileFileAssignmentsMockMvc.perform(put("/api/source-profile-file-assignments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSourceProfileFileAssignments)))
            .andExpect(status().isOk());

        // Validate the SourceProfileFileAssignments in the database
        List<SourceProfileFileAssignments> sourceProfileFileAssignmentsList = sourceProfileFileAssignmentsRepository.findAll();
        assertThat(sourceProfileFileAssignmentsList).hasSize(databaseSizeBeforeUpdate);
        SourceProfileFileAssignments testSourceProfileFileAssignments = sourceProfileFileAssignmentsList.get(sourceProfileFileAssignmentsList.size() - 1);
        assertThat(testSourceProfileFileAssignments.getSourceProfileId()).isEqualTo(UPDATED_SOURCE_PROFILE_ID);
        assertThat(testSourceProfileFileAssignments.getFileNameFormat()).isEqualTo(UPDATED_FILE_NAME_FORMAT);
        assertThat(testSourceProfileFileAssignments.getFileDescription()).isEqualTo(UPDATED_FILE_DESCRIPTION);
        assertThat(testSourceProfileFileAssignments.getFileExtension()).isEqualTo(UPDATED_FILE_EXTENSION);
        assertThat(testSourceProfileFileAssignments.getFrequencyType()).isEqualTo(UPDATED_FREQUENCY_TYPE);
        assertThat(testSourceProfileFileAssignments.getDueTime()).isEqualTo(UPDATED_DUE_TIME);
        assertThat(testSourceProfileFileAssignments.getDay()).isEqualTo(UPDATED_DAY);
        assertThat(testSourceProfileFileAssignments.getSourceDirectoryPath()).isEqualTo(UPDATED_SOURCE_DIRECTORY_PATH);
        assertThat(testSourceProfileFileAssignments.getLocalDirectoryPath()).isEqualTo(UPDATED_LOCAL_DIRECTORY_PATH);
        assertThat(testSourceProfileFileAssignments.getTemplateId()).isEqualTo(UPDATED_TEMPLATE_ID);
        assertThat(testSourceProfileFileAssignments.isEnabledFlag()).isEqualTo(UPDATED_ENABLED_FLAG);
        assertThat(testSourceProfileFileAssignments.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSourceProfileFileAssignments.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSourceProfileFileAssignments.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testSourceProfileFileAssignments.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);

        // Validate the SourceProfileFileAssignments in Elasticsearch
        SourceProfileFileAssignments sourceProfileFileAssignmentsEs = sourceProfileFileAssignmentsSearchRepository.findOne(testSourceProfileFileAssignments.getId());
        assertThat(sourceProfileFileAssignmentsEs).isEqualToComparingFieldByField(testSourceProfileFileAssignments);
    }

    @Test
    @Transactional
    public void updateNonExistingSourceProfileFileAssignments() throws Exception {
        int databaseSizeBeforeUpdate = sourceProfileFileAssignmentsRepository.findAll().size();

        // Create the SourceProfileFileAssignments

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSourceProfileFileAssignmentsMockMvc.perform(put("/api/source-profile-file-assignments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sourceProfileFileAssignments)))
            .andExpect(status().isCreated());

        // Validate the SourceProfileFileAssignments in the database
        List<SourceProfileFileAssignments> sourceProfileFileAssignmentsList = sourceProfileFileAssignmentsRepository.findAll();
        assertThat(sourceProfileFileAssignmentsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSourceProfileFileAssignments() throws Exception {
        // Initialize the database
        sourceProfileFileAssignmentsRepository.saveAndFlush(sourceProfileFileAssignments);
        sourceProfileFileAssignmentsSearchRepository.save(sourceProfileFileAssignments);
        int databaseSizeBeforeDelete = sourceProfileFileAssignmentsRepository.findAll().size();

        // Get the sourceProfileFileAssignments
        restSourceProfileFileAssignmentsMockMvc.perform(delete("/api/source-profile-file-assignments/{id}", sourceProfileFileAssignments.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean sourceProfileFileAssignmentsExistsInEs = sourceProfileFileAssignmentsSearchRepository.exists(sourceProfileFileAssignments.getId());
        assertThat(sourceProfileFileAssignmentsExistsInEs).isFalse();

        // Validate the database is empty
        List<SourceProfileFileAssignments> sourceProfileFileAssignmentsList = sourceProfileFileAssignmentsRepository.findAll();
        assertThat(sourceProfileFileAssignmentsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSourceProfileFileAssignments() throws Exception {
        // Initialize the database
        sourceProfileFileAssignmentsRepository.saveAndFlush(sourceProfileFileAssignments);
        sourceProfileFileAssignmentsSearchRepository.save(sourceProfileFileAssignments);

        // Search the sourceProfileFileAssignments
        restSourceProfileFileAssignmentsMockMvc.perform(get("/api/_search/source-profile-file-assignments?query=id:" + sourceProfileFileAssignments.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sourceProfileFileAssignments.getId().intValue())))
            .andExpect(jsonPath("$.[*].sourceProfileId").value(hasItem(DEFAULT_SOURCE_PROFILE_ID.intValue())))
            .andExpect(jsonPath("$.[*].fileNameFormat").value(hasItem(DEFAULT_FILE_NAME_FORMAT.toString())))
            .andExpect(jsonPath("$.[*].fileDescription").value(hasItem(DEFAULT_FILE_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].fileExtension").value(hasItem(DEFAULT_FILE_EXTENSION.toString())))
            .andExpect(jsonPath("$.[*].frequencyType").value(hasItem(DEFAULT_FREQUENCY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].dueTime").value(hasItem(DEFAULT_DUE_TIME.toString())))
            .andExpect(jsonPath("$.[*].day").value(hasItem(DEFAULT_DAY)))
            .andExpect(jsonPath("$.[*].sourceDirectoryPath").value(hasItem(DEFAULT_SOURCE_DIRECTORY_PATH.toString())))
            .andExpect(jsonPath("$.[*].localDirectoryPath").value(hasItem(DEFAULT_LOCAL_DIRECTORY_PATH.toString())))
            .andExpect(jsonPath("$.[*].templateId").value(hasItem(DEFAULT_TEMPLATE_ID.intValue())))
            .andExpect(jsonPath("$.[*].enabledFlag").value(hasItem(DEFAULT_ENABLED_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SourceProfileFileAssignments.class);
        SourceProfileFileAssignments sourceProfileFileAssignments1 = new SourceProfileFileAssignments();
        sourceProfileFileAssignments1.setId(1L);
        SourceProfileFileAssignments sourceProfileFileAssignments2 = new SourceProfileFileAssignments();
        sourceProfileFileAssignments2.setId(sourceProfileFileAssignments1.getId());
        assertThat(sourceProfileFileAssignments1).isEqualTo(sourceProfileFileAssignments2);
        sourceProfileFileAssignments2.setId(2L);
        assertThat(sourceProfileFileAssignments1).isNotEqualTo(sourceProfileFileAssignments2);
        sourceProfileFileAssignments1.setId(null);
        assertThat(sourceProfileFileAssignments1).isNotEqualTo(sourceProfileFileAssignments2);
    }
}
