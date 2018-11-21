package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.JournalBatches;
import com.nspl.app.repository.JournalBatchesRepository;
import com.nspl.app.service.JournalBatchesService;
import com.nspl.app.repository.search.JournalBatchesSearchRepository;
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
 * Test class for the JournalBatchesResource REST controller.
 *
 * @see JournalBatchesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class JournalBatchesResourceIntTest {

    private static final String DEFAULT_JOURNAL_BATCH_NAME = "AAAAAAAAAA";
    private static final String UPDATED_JOURNAL_BATCH_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_JE_TEMP_ID = 1L;
    private static final Long UPDATED_JE_TEMP_ID = 2L;

    private static final LocalDate DEFAULT_JE_BATCH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_JE_BATCH_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_TENANT_ID = 1L;
    private static final Long UPDATED_TENANT_ID = 2L;

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private JournalBatchesRepository journalBatchesRepository;

    @Autowired
    private JournalBatchesService journalBatchesService;

    @Autowired
    private JournalBatchesSearchRepository journalBatchesSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restJournalBatchesMockMvc;

    private JournalBatches journalBatches;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        JournalBatchesResource journalBatchesResource = new JournalBatchesResource(journalBatchesService);
        this.restJournalBatchesMockMvc = MockMvcBuilders.standaloneSetup(journalBatchesResource)
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
    public static JournalBatches createEntity(EntityManager em) {
        JournalBatches journalBatches = new JournalBatches()
            .journalBatchName(DEFAULT_JOURNAL_BATCH_NAME)
            .jeTempId(DEFAULT_JE_TEMP_ID)
            .jeBatchDate(DEFAULT_JE_BATCH_DATE)
            .tenantId(DEFAULT_TENANT_ID)
            .createdBy(DEFAULT_CREATED_BY)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE);
        return journalBatches;
    }

    @Before
    public void initTest() {
        journalBatchesSearchRepository.deleteAll();
        journalBatches = createEntity(em);
    }

    @Test
    @Transactional
    public void createJournalBatches() throws Exception {
        int databaseSizeBeforeCreate = journalBatchesRepository.findAll().size();

        // Create the JournalBatches
        restJournalBatchesMockMvc.perform(post("/api/journal-batches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(journalBatches)))
            .andExpect(status().isCreated());

        // Validate the JournalBatches in the database
        List<JournalBatches> journalBatchesList = journalBatchesRepository.findAll();
        assertThat(journalBatchesList).hasSize(databaseSizeBeforeCreate + 1);
        JournalBatches testJournalBatches = journalBatchesList.get(journalBatchesList.size() - 1);
        assertThat(testJournalBatches.getJournalBatchName()).isEqualTo(DEFAULT_JOURNAL_BATCH_NAME);
        assertThat(testJournalBatches.getJeTempId()).isEqualTo(DEFAULT_JE_TEMP_ID);
        assertThat(testJournalBatches.getJeBatchDate()).isEqualTo(DEFAULT_JE_BATCH_DATE);
        assertThat(testJournalBatches.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testJournalBatches.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testJournalBatches.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testJournalBatches.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testJournalBatches.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);

        // Validate the JournalBatches in Elasticsearch
        JournalBatches journalBatchesEs = journalBatchesSearchRepository.findOne(testJournalBatches.getId());
        assertThat(journalBatchesEs).isEqualToComparingFieldByField(testJournalBatches);
    }

    @Test
    @Transactional
    public void createJournalBatchesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = journalBatchesRepository.findAll().size();

        // Create the JournalBatches with an existing ID
        journalBatches.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJournalBatchesMockMvc.perform(post("/api/journal-batches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(journalBatches)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<JournalBatches> journalBatchesList = journalBatchesRepository.findAll();
        assertThat(journalBatchesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllJournalBatches() throws Exception {
        // Initialize the database
        journalBatchesRepository.saveAndFlush(journalBatches);

        // Get all the journalBatchesList
        restJournalBatchesMockMvc.perform(get("/api/journal-batches?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(journalBatches.getId().intValue())))
            .andExpect(jsonPath("$.[*].journalBatchName").value(hasItem(DEFAULT_JOURNAL_BATCH_NAME.toString())))
            .andExpect(jsonPath("$.[*].jeTempId").value(hasItem(DEFAULT_JE_TEMP_ID.intValue())))
            .andExpect(jsonPath("$.[*].jeBatchDate").value(hasItem(DEFAULT_JE_BATCH_DATE.toString())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void getJournalBatches() throws Exception {
        // Initialize the database
        journalBatchesRepository.saveAndFlush(journalBatches);

        // Get the journalBatches
        restJournalBatchesMockMvc.perform(get("/api/journal-batches/{id}", journalBatches.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(journalBatches.getId().intValue()))
            .andExpect(jsonPath("$.journalBatchName").value(DEFAULT_JOURNAL_BATCH_NAME.toString()))
            .andExpect(jsonPath("$.jeTempId").value(DEFAULT_JE_TEMP_ID.intValue()))
            .andExpect(jsonPath("$.jeBatchDate").value(DEFAULT_JE_BATCH_DATE.toString()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID.intValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingJournalBatches() throws Exception {
        // Get the journalBatches
        restJournalBatchesMockMvc.perform(get("/api/journal-batches/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJournalBatches() throws Exception {
        // Initialize the database
        journalBatchesService.save(journalBatches);

        int databaseSizeBeforeUpdate = journalBatchesRepository.findAll().size();

        // Update the journalBatches
        JournalBatches updatedJournalBatches = journalBatchesRepository.findOne(journalBatches.getId());
        updatedJournalBatches
            .journalBatchName(UPDATED_JOURNAL_BATCH_NAME)
            .jeTempId(UPDATED_JE_TEMP_ID)
            .jeBatchDate(UPDATED_JE_BATCH_DATE)
            .tenantId(UPDATED_TENANT_ID)
            .createdBy(UPDATED_CREATED_BY)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE);

        restJournalBatchesMockMvc.perform(put("/api/journal-batches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedJournalBatches)))
            .andExpect(status().isOk());

        // Validate the JournalBatches in the database
        List<JournalBatches> journalBatchesList = journalBatchesRepository.findAll();
        assertThat(journalBatchesList).hasSize(databaseSizeBeforeUpdate);
        JournalBatches testJournalBatches = journalBatchesList.get(journalBatchesList.size() - 1);
        assertThat(testJournalBatches.getJournalBatchName()).isEqualTo(UPDATED_JOURNAL_BATCH_NAME);
        assertThat(testJournalBatches.getJeTempId()).isEqualTo(UPDATED_JE_TEMP_ID);
        assertThat(testJournalBatches.getJeBatchDate()).isEqualTo(UPDATED_JE_BATCH_DATE);
        assertThat(testJournalBatches.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testJournalBatches.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testJournalBatches.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testJournalBatches.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testJournalBatches.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);

        // Validate the JournalBatches in Elasticsearch
        JournalBatches journalBatchesEs = journalBatchesSearchRepository.findOne(testJournalBatches.getId());
        assertThat(journalBatchesEs).isEqualToComparingFieldByField(testJournalBatches);
    }

    @Test
    @Transactional
    public void updateNonExistingJournalBatches() throws Exception {
        int databaseSizeBeforeUpdate = journalBatchesRepository.findAll().size();

        // Create the JournalBatches

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restJournalBatchesMockMvc.perform(put("/api/journal-batches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(journalBatches)))
            .andExpect(status().isCreated());

        // Validate the JournalBatches in the database
        List<JournalBatches> journalBatchesList = journalBatchesRepository.findAll();
        assertThat(journalBatchesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteJournalBatches() throws Exception {
        // Initialize the database
        journalBatchesService.save(journalBatches);

        int databaseSizeBeforeDelete = journalBatchesRepository.findAll().size();

        // Get the journalBatches
        restJournalBatchesMockMvc.perform(delete("/api/journal-batches/{id}", journalBatches.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean journalBatchesExistsInEs = journalBatchesSearchRepository.exists(journalBatches.getId());
        assertThat(journalBatchesExistsInEs).isFalse();

        // Validate the database is empty
        List<JournalBatches> journalBatchesList = journalBatchesRepository.findAll();
        assertThat(journalBatchesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchJournalBatches() throws Exception {
        // Initialize the database
        journalBatchesService.save(journalBatches);

        // Search the journalBatches
        restJournalBatchesMockMvc.perform(get("/api/_search/journal-batches?query=id:" + journalBatches.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(journalBatches.getId().intValue())))
            .andExpect(jsonPath("$.[*].journalBatchName").value(hasItem(DEFAULT_JOURNAL_BATCH_NAME.toString())))
            .andExpect(jsonPath("$.[*].jeTempId").value(hasItem(DEFAULT_JE_TEMP_ID.intValue())))
            .andExpect(jsonPath("$.[*].jeBatchDate").value(hasItem(DEFAULT_JE_BATCH_DATE.toString())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JournalBatches.class);
    }
}
