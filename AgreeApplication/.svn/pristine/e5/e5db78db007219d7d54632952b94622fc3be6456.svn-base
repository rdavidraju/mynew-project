package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.SourceFileInbHistory;
import com.nspl.app.repository.SourceFileInbHistoryRepository;
import com.nspl.app.repository.search.SourceFileInbHistorySearchRepository;
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
 * Test class for the SourceFileInbHistoryResource REST controller.
 *
 * @see SourceFileInbHistoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class SourceFileInbHistoryResourceIntTest {

    private static final String DEFAULT_JOB_ID = "AAAAAAAAAA";
    private static final String UPDATED_JOB_ID = "BBBBBBBBBB";

    private static final Long DEFAULT_PROFILE_ID = 1L;
    private static final Long UPDATED_PROFILE_ID = 2L;

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_FILE_RECEIVED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FILE_RECEIVED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_LOCAL_DIR_PATH = "AAAAAAAAAA";
    private static final String UPDATED_LOCAL_DIR_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_EXT = "AAAAAAAAAA";
    private static final String UPDATED_FILE_EXT = "BBBBBBBBBB";

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private SourceFileInbHistoryRepository sourceFileInbHistoryRepository;

    @Autowired
    private SourceFileInbHistorySearchRepository sourceFileInbHistorySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSourceFileInbHistoryMockMvc;

    private SourceFileInbHistory sourceFileInbHistory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SourceFileInbHistoryResource sourceFileInbHistoryResource = new SourceFileInbHistoryResource(sourceFileInbHistoryRepository, sourceFileInbHistorySearchRepository);
        this.restSourceFileInbHistoryMockMvc = MockMvcBuilders.standaloneSetup(sourceFileInbHistoryResource)
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
    public static SourceFileInbHistory createEntity(EntityManager em) {
        SourceFileInbHistory sourceFileInbHistory = new SourceFileInbHistory()
            .jobId(DEFAULT_JOB_ID)
            .profileId(DEFAULT_PROFILE_ID)
            .fileName(DEFAULT_FILE_NAME)
            .fileReceivedDate(DEFAULT_FILE_RECEIVED_DATE)
            .localDirPath(DEFAULT_LOCAL_DIR_PATH)
            .fileExt(DEFAULT_FILE_EXT)
            .createdBy(DEFAULT_CREATED_BY)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .creationDate(DEFAULT_CREATION_DATE)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE);
        return sourceFileInbHistory;
    }

    @Before
    public void initTest() {
        sourceFileInbHistorySearchRepository.deleteAll();
        sourceFileInbHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createSourceFileInbHistory() throws Exception {
        int databaseSizeBeforeCreate = sourceFileInbHistoryRepository.findAll().size();

        // Create the SourceFileInbHistory
        restSourceFileInbHistoryMockMvc.perform(post("/api/source-file-inb-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sourceFileInbHistory)))
            .andExpect(status().isCreated());

        // Validate the SourceFileInbHistory in the database
        List<SourceFileInbHistory> sourceFileInbHistoryList = sourceFileInbHistoryRepository.findAll();
        assertThat(sourceFileInbHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        SourceFileInbHistory testSourceFileInbHistory = sourceFileInbHistoryList.get(sourceFileInbHistoryList.size() - 1);
        assertThat(testSourceFileInbHistory.getJobId()).isEqualTo(DEFAULT_JOB_ID);
        assertThat(testSourceFileInbHistory.getProfileId()).isEqualTo(DEFAULT_PROFILE_ID);
        assertThat(testSourceFileInbHistory.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testSourceFileInbHistory.getFileReceivedDate()).isEqualTo(DEFAULT_FILE_RECEIVED_DATE);
        assertThat(testSourceFileInbHistory.getLocalDirPath()).isEqualTo(DEFAULT_LOCAL_DIR_PATH);
        assertThat(testSourceFileInbHistory.getFileExt()).isEqualTo(DEFAULT_FILE_EXT);
        assertThat(testSourceFileInbHistory.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSourceFileInbHistory.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testSourceFileInbHistory.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testSourceFileInbHistory.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);

        // Validate the SourceFileInbHistory in Elasticsearch
        SourceFileInbHistory sourceFileInbHistoryEs = sourceFileInbHistorySearchRepository.findOne(testSourceFileInbHistory.getId());
        assertThat(sourceFileInbHistoryEs).isEqualToComparingFieldByField(testSourceFileInbHistory);
    }

    @Test
    @Transactional
    public void createSourceFileInbHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sourceFileInbHistoryRepository.findAll().size();

        // Create the SourceFileInbHistory with an existing ID
        sourceFileInbHistory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSourceFileInbHistoryMockMvc.perform(post("/api/source-file-inb-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sourceFileInbHistory)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<SourceFileInbHistory> sourceFileInbHistoryList = sourceFileInbHistoryRepository.findAll();
        assertThat(sourceFileInbHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSourceFileInbHistories() throws Exception {
        // Initialize the database
        sourceFileInbHistoryRepository.saveAndFlush(sourceFileInbHistory);

        // Get all the sourceFileInbHistoryList
        restSourceFileInbHistoryMockMvc.perform(get("/api/source-file-inb-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sourceFileInbHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobId").value(hasItem(DEFAULT_JOB_ID)))
            .andExpect(jsonPath("$.[*].profileId").value(hasItem(DEFAULT_PROFILE_ID.intValue())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME.toString())))
            .andExpect(jsonPath("$.[*].fileReceivedDate").value(hasItem(sameInstant(DEFAULT_FILE_RECEIVED_DATE))))
            .andExpect(jsonPath("$.[*].localDirPath").value(hasItem(DEFAULT_LOCAL_DIR_PATH.toString())))
            .andExpect(jsonPath("$.[*].fileExt").value(hasItem(DEFAULT_FILE_EXT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void getSourceFileInbHistory() throws Exception {
        // Initialize the database
        sourceFileInbHistoryRepository.saveAndFlush(sourceFileInbHistory);

        // Get the sourceFileInbHistory
        restSourceFileInbHistoryMockMvc.perform(get("/api/source-file-inb-histories/{id}", sourceFileInbHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sourceFileInbHistory.getId().intValue()))
            .andExpect(jsonPath("$.jobId").value(DEFAULT_JOB_ID))
            .andExpect(jsonPath("$.profileId").value(DEFAULT_PROFILE_ID.intValue()))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME.toString()))
            .andExpect(jsonPath("$.fileReceivedDate").value(sameInstant(DEFAULT_FILE_RECEIVED_DATE)))
            .andExpect(jsonPath("$.localDirPath").value(DEFAULT_LOCAL_DIR_PATH.toString()))
            .andExpect(jsonPath("$.fileExt").value(DEFAULT_FILE_EXT.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.creationDate").value(sameInstant(DEFAULT_CREATION_DATE)))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingSourceFileInbHistory() throws Exception {
        // Get the sourceFileInbHistory
        restSourceFileInbHistoryMockMvc.perform(get("/api/source-file-inb-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSourceFileInbHistory() throws Exception {
        // Initialize the database
        sourceFileInbHistoryRepository.saveAndFlush(sourceFileInbHistory);
        sourceFileInbHistorySearchRepository.save(sourceFileInbHistory);
        int databaseSizeBeforeUpdate = sourceFileInbHistoryRepository.findAll().size();

        // Update the sourceFileInbHistory
        SourceFileInbHistory updatedSourceFileInbHistory = sourceFileInbHistoryRepository.findOne(sourceFileInbHistory.getId());
        updatedSourceFileInbHistory
            .jobId(UPDATED_JOB_ID)
            .profileId(UPDATED_PROFILE_ID)
            .fileName(UPDATED_FILE_NAME)
            .fileReceivedDate(UPDATED_FILE_RECEIVED_DATE)
            .localDirPath(UPDATED_LOCAL_DIR_PATH)
            .fileExt(UPDATED_FILE_EXT)
            .createdBy(UPDATED_CREATED_BY)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE);

        restSourceFileInbHistoryMockMvc.perform(put("/api/source-file-inb-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSourceFileInbHistory)))
            .andExpect(status().isOk());

        // Validate the SourceFileInbHistory in the database
        List<SourceFileInbHistory> sourceFileInbHistoryList = sourceFileInbHistoryRepository.findAll();
        assertThat(sourceFileInbHistoryList).hasSize(databaseSizeBeforeUpdate);
        SourceFileInbHistory testSourceFileInbHistory = sourceFileInbHistoryList.get(sourceFileInbHistoryList.size() - 1);
        assertThat(testSourceFileInbHistory.getJobId()).isEqualTo(UPDATED_JOB_ID);
        assertThat(testSourceFileInbHistory.getProfileId()).isEqualTo(UPDATED_PROFILE_ID);
        assertThat(testSourceFileInbHistory.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testSourceFileInbHistory.getFileReceivedDate()).isEqualTo(UPDATED_FILE_RECEIVED_DATE);
        assertThat(testSourceFileInbHistory.getLocalDirPath()).isEqualTo(UPDATED_LOCAL_DIR_PATH);
        assertThat(testSourceFileInbHistory.getFileExt()).isEqualTo(UPDATED_FILE_EXT);
        assertThat(testSourceFileInbHistory.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSourceFileInbHistory.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testSourceFileInbHistory.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testSourceFileInbHistory.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);

        // Validate the SourceFileInbHistory in Elasticsearch
        SourceFileInbHistory sourceFileInbHistoryEs = sourceFileInbHistorySearchRepository.findOne(testSourceFileInbHistory.getId());
        assertThat(sourceFileInbHistoryEs).isEqualToComparingFieldByField(testSourceFileInbHistory);
    }

    @Test
    @Transactional
    public void updateNonExistingSourceFileInbHistory() throws Exception {
        int databaseSizeBeforeUpdate = sourceFileInbHistoryRepository.findAll().size();

        // Create the SourceFileInbHistory

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSourceFileInbHistoryMockMvc.perform(put("/api/source-file-inb-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sourceFileInbHistory)))
            .andExpect(status().isCreated());

        // Validate the SourceFileInbHistory in the database
        List<SourceFileInbHistory> sourceFileInbHistoryList = sourceFileInbHistoryRepository.findAll();
        assertThat(sourceFileInbHistoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSourceFileInbHistory() throws Exception {
        // Initialize the database
        sourceFileInbHistoryRepository.saveAndFlush(sourceFileInbHistory);
        sourceFileInbHistorySearchRepository.save(sourceFileInbHistory);
        int databaseSizeBeforeDelete = sourceFileInbHistoryRepository.findAll().size();

        // Get the sourceFileInbHistory
        restSourceFileInbHistoryMockMvc.perform(delete("/api/source-file-inb-histories/{id}", sourceFileInbHistory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean sourceFileInbHistoryExistsInEs = sourceFileInbHistorySearchRepository.exists(sourceFileInbHistory.getId());
        assertThat(sourceFileInbHistoryExistsInEs).isFalse();

        // Validate the database is empty
        List<SourceFileInbHistory> sourceFileInbHistoryList = sourceFileInbHistoryRepository.findAll();
        assertThat(sourceFileInbHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSourceFileInbHistory() throws Exception {
        // Initialize the database
        sourceFileInbHistoryRepository.saveAndFlush(sourceFileInbHistory);
        sourceFileInbHistorySearchRepository.save(sourceFileInbHistory);

        // Search the sourceFileInbHistory
        restSourceFileInbHistoryMockMvc.perform(get("/api/_search/source-file-inb-histories?query=id:" + sourceFileInbHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sourceFileInbHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobId").value(hasItem(DEFAULT_JOB_ID)))
            .andExpect(jsonPath("$.[*].profileId").value(hasItem(DEFAULT_PROFILE_ID.intValue())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME.toString())))
            .andExpect(jsonPath("$.[*].fileReceivedDate").value(hasItem(sameInstant(DEFAULT_FILE_RECEIVED_DATE))))
            .andExpect(jsonPath("$.[*].localDirPath").value(hasItem(DEFAULT_LOCAL_DIR_PATH.toString())))
            .andExpect(jsonPath("$.[*].fileExt").value(hasItem(DEFAULT_FILE_EXT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SourceFileInbHistory.class);
    }
}
