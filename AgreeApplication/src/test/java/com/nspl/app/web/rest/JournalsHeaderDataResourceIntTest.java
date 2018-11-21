package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.JournalsHeaderData;
import com.nspl.app.repository.JournalsHeaderDataRepository;
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
 * Test class for the JournalsHeaderDataResource REST controller.
 *
 * @see JournalsHeaderDataResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class JournalsHeaderDataResourceIntTest {

    private static final Long DEFAULT_TENANT_ID = 1L;
    private static final Long UPDATED_TENANT_ID = 2L;

    private static final Long DEFAULT_JE_TEMP_ID = 1L;
    private static final Long UPDATED_JE_TEMP_ID = 2L;

    private static final String DEFAULT_JE_BATCH_NAME = "AAAAAAAAAA";
    private static final String UPDATED_JE_BATCH_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_JE_BATCH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_JE_BATCH_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_GL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_GL_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY = "BBBBBBBBBB";

    private static final String DEFAULT_CONVERSION_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CONVERSION_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE = "BBBBBBBBBB";

    private static final String DEFAULT_LEDGER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LEDGER_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_BATCH_TOTAL = 1;
    private static final Integer UPDATED_BATCH_TOTAL = 2;

    private static final LocalDate DEFAULT_RUN_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RUN_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_SUBMITTED_BY = "AAAAAAAAAA";
    private static final String UPDATED_SUBMITTED_BY = "BBBBBBBBBB";

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private JournalsHeaderDataRepository journalsHeaderDataRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restJournalsHeaderDataMockMvc;

    private JournalsHeaderData journalsHeaderData;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        JournalsHeaderDataResource journalsHeaderDataResource = new JournalsHeaderDataResource(journalsHeaderDataRepository);
        this.restJournalsHeaderDataMockMvc = MockMvcBuilders.standaloneSetup(journalsHeaderDataResource)
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
    public static JournalsHeaderData createEntity(EntityManager em) {
        JournalsHeaderData journalsHeaderData = new JournalsHeaderData()
            .tenantId(DEFAULT_TENANT_ID)
            .jeTempId(DEFAULT_JE_TEMP_ID)
            .jeBatchName(DEFAULT_JE_BATCH_NAME)
            .jeBatchDate(DEFAULT_JE_BATCH_DATE)
            .glDate(DEFAULT_GL_DATE)
            .currency(DEFAULT_CURRENCY)
            .conversionType(DEFAULT_CONVERSION_TYPE)
            .category(DEFAULT_CATEGORY)
            .source(DEFAULT_SOURCE)
            .ledgerName(DEFAULT_LEDGER_NAME)
            .batchTotal(DEFAULT_BATCH_TOTAL)
            .runDate(DEFAULT_RUN_DATE)
            .submittedBy(DEFAULT_SUBMITTED_BY)
            .createdBy(DEFAULT_CREATED_BY)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE);
        return journalsHeaderData;
    }

    @Before
    public void initTest() {
        journalsHeaderData = createEntity(em);
    }

    @Test
    @Transactional
    public void createJournalsHeaderData() throws Exception {
        int databaseSizeBeforeCreate = journalsHeaderDataRepository.findAll().size();

        // Create the JournalsHeaderData
        restJournalsHeaderDataMockMvc.perform(post("/api/journals-header-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(journalsHeaderData)))
            .andExpect(status().isCreated());

        // Validate the JournalsHeaderData in the database
        List<JournalsHeaderData> journalsHeaderDataList = journalsHeaderDataRepository.findAll();
        assertThat(journalsHeaderDataList).hasSize(databaseSizeBeforeCreate + 1);
        JournalsHeaderData testJournalsHeaderData = journalsHeaderDataList.get(journalsHeaderDataList.size() - 1);
        assertThat(testJournalsHeaderData.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testJournalsHeaderData.getJeTempId()).isEqualTo(DEFAULT_JE_TEMP_ID);
        assertThat(testJournalsHeaderData.getJeBatchName()).isEqualTo(DEFAULT_JE_BATCH_NAME);
        assertThat(testJournalsHeaderData.getJeBatchDate()).isEqualTo(DEFAULT_JE_BATCH_DATE);
        assertThat(testJournalsHeaderData.getGlDate()).isEqualTo(DEFAULT_GL_DATE);
        assertThat(testJournalsHeaderData.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testJournalsHeaderData.getConversionType()).isEqualTo(DEFAULT_CONVERSION_TYPE);
        assertThat(testJournalsHeaderData.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testJournalsHeaderData.getSource()).isEqualTo(DEFAULT_SOURCE);
        assertThat(testJournalsHeaderData.getLedgerName()).isEqualTo(DEFAULT_LEDGER_NAME);
        assertThat(testJournalsHeaderData.getBatchTotal()).isEqualTo(DEFAULT_BATCH_TOTAL);
        assertThat(testJournalsHeaderData.getRunDate()).isEqualTo(DEFAULT_RUN_DATE);
        assertThat(testJournalsHeaderData.getSubmittedBy()).isEqualTo(DEFAULT_SUBMITTED_BY);
        assertThat(testJournalsHeaderData.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testJournalsHeaderData.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testJournalsHeaderData.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testJournalsHeaderData.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void createJournalsHeaderDataWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = journalsHeaderDataRepository.findAll().size();

        // Create the JournalsHeaderData with an existing ID
        journalsHeaderData.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJournalsHeaderDataMockMvc.perform(post("/api/journals-header-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(journalsHeaderData)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<JournalsHeaderData> journalsHeaderDataList = journalsHeaderDataRepository.findAll();
        assertThat(journalsHeaderDataList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllJournalsHeaderData() throws Exception {
        // Initialize the database
        journalsHeaderDataRepository.saveAndFlush(journalsHeaderData);

        // Get all the journalsHeaderDataList
        restJournalsHeaderDataMockMvc.perform(get("/api/journals-header-data?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(journalsHeaderData.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].jeTempId").value(hasItem(DEFAULT_JE_TEMP_ID.intValue())))
            .andExpect(jsonPath("$.[*].jeBatchName").value(hasItem(DEFAULT_JE_BATCH_NAME.toString())))
            .andExpect(jsonPath("$.[*].jeBatchDate").value(hasItem(DEFAULT_JE_BATCH_DATE.toString())))
            .andExpect(jsonPath("$.[*].glDate").value(hasItem(DEFAULT_GL_DATE.toString())))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].conversionType").value(hasItem(DEFAULT_CONVERSION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE.toString())))
            .andExpect(jsonPath("$.[*].ledgerName").value(hasItem(DEFAULT_LEDGER_NAME.toString())))
            .andExpect(jsonPath("$.[*].batchTotal").value(hasItem(DEFAULT_BATCH_TOTAL)))
            .andExpect(jsonPath("$.[*].runDate").value(hasItem(DEFAULT_RUN_DATE.toString())))
            .andExpect(jsonPath("$.[*].submittedBy").value(hasItem(DEFAULT_SUBMITTED_BY.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void getJournalsHeaderData() throws Exception {
        // Initialize the database
        journalsHeaderDataRepository.saveAndFlush(journalsHeaderData);

        // Get the journalsHeaderData
        restJournalsHeaderDataMockMvc.perform(get("/api/journals-header-data/{id}", journalsHeaderData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(journalsHeaderData.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID.intValue()))
            .andExpect(jsonPath("$.jeTempId").value(DEFAULT_JE_TEMP_ID.intValue()))
            .andExpect(jsonPath("$.jeBatchName").value(DEFAULT_JE_BATCH_NAME.toString()))
            .andExpect(jsonPath("$.jeBatchDate").value(DEFAULT_JE_BATCH_DATE.toString()))
            .andExpect(jsonPath("$.glDate").value(DEFAULT_GL_DATE.toString()))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY.toString()))
            .andExpect(jsonPath("$.conversionType").value(DEFAULT_CONVERSION_TYPE.toString()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.source").value(DEFAULT_SOURCE.toString()))
            .andExpect(jsonPath("$.ledgerName").value(DEFAULT_LEDGER_NAME.toString()))
            .andExpect(jsonPath("$.batchTotal").value(DEFAULT_BATCH_TOTAL))
            .andExpect(jsonPath("$.runDate").value(DEFAULT_RUN_DATE.toString()))
            .andExpect(jsonPath("$.submittedBy").value(DEFAULT_SUBMITTED_BY.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingJournalsHeaderData() throws Exception {
        // Get the journalsHeaderData
        restJournalsHeaderDataMockMvc.perform(get("/api/journals-header-data/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJournalsHeaderData() throws Exception {
        // Initialize the database
        journalsHeaderDataRepository.saveAndFlush(journalsHeaderData);
        int databaseSizeBeforeUpdate = journalsHeaderDataRepository.findAll().size();

        // Update the journalsHeaderData
        JournalsHeaderData updatedJournalsHeaderData = journalsHeaderDataRepository.findOne(journalsHeaderData.getId());
        updatedJournalsHeaderData
            .tenantId(UPDATED_TENANT_ID)
            .jeTempId(UPDATED_JE_TEMP_ID)
            .jeBatchName(UPDATED_JE_BATCH_NAME)
            .jeBatchDate(UPDATED_JE_BATCH_DATE)
            .glDate(UPDATED_GL_DATE)
            .currency(UPDATED_CURRENCY)
            .conversionType(UPDATED_CONVERSION_TYPE)
            .category(UPDATED_CATEGORY)
            .source(UPDATED_SOURCE)
            .ledgerName(UPDATED_LEDGER_NAME)
            .batchTotal(UPDATED_BATCH_TOTAL)
            .runDate(UPDATED_RUN_DATE)
            .submittedBy(UPDATED_SUBMITTED_BY)
            .createdBy(UPDATED_CREATED_BY)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE);

        restJournalsHeaderDataMockMvc.perform(put("/api/journals-header-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedJournalsHeaderData)))
            .andExpect(status().isOk());

        // Validate the JournalsHeaderData in the database
        List<JournalsHeaderData> journalsHeaderDataList = journalsHeaderDataRepository.findAll();
        assertThat(journalsHeaderDataList).hasSize(databaseSizeBeforeUpdate);
        JournalsHeaderData testJournalsHeaderData = journalsHeaderDataList.get(journalsHeaderDataList.size() - 1);
        assertThat(testJournalsHeaderData.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testJournalsHeaderData.getJeTempId()).isEqualTo(UPDATED_JE_TEMP_ID);
        assertThat(testJournalsHeaderData.getJeBatchName()).isEqualTo(UPDATED_JE_BATCH_NAME);
        assertThat(testJournalsHeaderData.getJeBatchDate()).isEqualTo(UPDATED_JE_BATCH_DATE);
        assertThat(testJournalsHeaderData.getGlDate()).isEqualTo(UPDATED_GL_DATE);
        assertThat(testJournalsHeaderData.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testJournalsHeaderData.getConversionType()).isEqualTo(UPDATED_CONVERSION_TYPE);
        assertThat(testJournalsHeaderData.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testJournalsHeaderData.getSource()).isEqualTo(UPDATED_SOURCE);
        assertThat(testJournalsHeaderData.getLedgerName()).isEqualTo(UPDATED_LEDGER_NAME);
        assertThat(testJournalsHeaderData.getBatchTotal()).isEqualTo(UPDATED_BATCH_TOTAL);
        assertThat(testJournalsHeaderData.getRunDate()).isEqualTo(UPDATED_RUN_DATE);
        assertThat(testJournalsHeaderData.getSubmittedBy()).isEqualTo(UPDATED_SUBMITTED_BY);
        assertThat(testJournalsHeaderData.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testJournalsHeaderData.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testJournalsHeaderData.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testJournalsHeaderData.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingJournalsHeaderData() throws Exception {
        int databaseSizeBeforeUpdate = journalsHeaderDataRepository.findAll().size();

        // Create the JournalsHeaderData

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restJournalsHeaderDataMockMvc.perform(put("/api/journals-header-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(journalsHeaderData)))
            .andExpect(status().isCreated());

        // Validate the JournalsHeaderData in the database
        List<JournalsHeaderData> journalsHeaderDataList = journalsHeaderDataRepository.findAll();
        assertThat(journalsHeaderDataList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteJournalsHeaderData() throws Exception {
        // Initialize the database
        journalsHeaderDataRepository.saveAndFlush(journalsHeaderData);
        int databaseSizeBeforeDelete = journalsHeaderDataRepository.findAll().size();

        // Get the journalsHeaderData
        restJournalsHeaderDataMockMvc.perform(delete("/api/journals-header-data/{id}", journalsHeaderData.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<JournalsHeaderData> journalsHeaderDataList = journalsHeaderDataRepository.findAll();
        assertThat(journalsHeaderDataList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JournalsHeaderData.class);
    }
}
