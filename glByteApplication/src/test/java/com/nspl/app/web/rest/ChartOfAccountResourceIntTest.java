package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.ChartOfAccount;
import com.nspl.app.repository.ChartOfAccountRepository;
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
 * Test class for the ChartOfAccountResource REST controller.
 *
 * @see ChartOfAccountResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class ChartOfAccountResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_SEGMENT_SEPERATOR = "AAAAAAAAAA";
    private static final String UPDATED_SEGMENT_SEPERATOR = "BBBBBBBBBB";

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
    private ChartOfAccountRepository chartOfAccountRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restChartOfAccountMockMvc;

    private ChartOfAccount chartOfAccount;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ChartOfAccountResource chartOfAccountResource = new ChartOfAccountResource(chartOfAccountRepository);
        this.restChartOfAccountMockMvc = MockMvcBuilders.standaloneSetup(chartOfAccountResource)
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
    public static ChartOfAccount createEntity(EntityManager em) {
        ChartOfAccount chartOfAccount = new ChartOfAccount()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .segmentSeperator(DEFAULT_SEGMENT_SEPERATOR)
            .enabledFlag(DEFAULT_ENABLED_FLAG)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE);
        return chartOfAccount;
    }

    @Before
    public void initTest() {
        chartOfAccount = createEntity(em);
    }

    @Test
    @Transactional
    public void createChartOfAccount() throws Exception {
        int databaseSizeBeforeCreate = chartOfAccountRepository.findAll().size();

        // Create the ChartOfAccount
        restChartOfAccountMockMvc.perform(post("/api/chart-of-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chartOfAccount)))
            .andExpect(status().isCreated());

        // Validate the ChartOfAccount in the database
        List<ChartOfAccount> chartOfAccountList = chartOfAccountRepository.findAll();
        assertThat(chartOfAccountList).hasSize(databaseSizeBeforeCreate + 1);
        ChartOfAccount testChartOfAccount = chartOfAccountList.get(chartOfAccountList.size() - 1);
        assertThat(testChartOfAccount.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testChartOfAccount.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testChartOfAccount.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testChartOfAccount.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testChartOfAccount.getSegmentSeperator()).isEqualTo(DEFAULT_SEGMENT_SEPERATOR);
        assertThat(testChartOfAccount.isEnabledFlag()).isEqualTo(DEFAULT_ENABLED_FLAG);
        assertThat(testChartOfAccount.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testChartOfAccount.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testChartOfAccount.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testChartOfAccount.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void createChartOfAccountWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = chartOfAccountRepository.findAll().size();

        // Create the ChartOfAccount with an existing ID
        chartOfAccount.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restChartOfAccountMockMvc.perform(post("/api/chart-of-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chartOfAccount)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ChartOfAccount> chartOfAccountList = chartOfAccountRepository.findAll();
        assertThat(chartOfAccountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllChartOfAccounts() throws Exception {
        // Initialize the database
        chartOfAccountRepository.saveAndFlush(chartOfAccount);

        // Get all the chartOfAccountList
        restChartOfAccountMockMvc.perform(get("/api/chart-of-accounts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chartOfAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].segmentSeperator").value(hasItem(DEFAULT_SEGMENT_SEPERATOR.toString())))
            .andExpect(jsonPath("$.[*].enabledFlag").value(hasItem(DEFAULT_ENABLED_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void getChartOfAccount() throws Exception {
        // Initialize the database
        chartOfAccountRepository.saveAndFlush(chartOfAccount);

        // Get the chartOfAccount
        restChartOfAccountMockMvc.perform(get("/api/chart-of-accounts/{id}", chartOfAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(chartOfAccount.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.segmentSeperator").value(DEFAULT_SEGMENT_SEPERATOR.toString()))
            .andExpect(jsonPath("$.enabledFlag").value(DEFAULT_ENABLED_FLAG.booleanValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingChartOfAccount() throws Exception {
        // Get the chartOfAccount
        restChartOfAccountMockMvc.perform(get("/api/chart-of-accounts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChartOfAccount() throws Exception {
        // Initialize the database
        chartOfAccountRepository.saveAndFlush(chartOfAccount);
        int databaseSizeBeforeUpdate = chartOfAccountRepository.findAll().size();

        // Update the chartOfAccount
        ChartOfAccount updatedChartOfAccount = chartOfAccountRepository.findOne(chartOfAccount.getId());
        updatedChartOfAccount
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .segmentSeperator(UPDATED_SEGMENT_SEPERATOR)
            .enabledFlag(UPDATED_ENABLED_FLAG)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE);

        restChartOfAccountMockMvc.perform(put("/api/chart-of-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedChartOfAccount)))
            .andExpect(status().isOk());

        // Validate the ChartOfAccount in the database
        List<ChartOfAccount> chartOfAccountList = chartOfAccountRepository.findAll();
        assertThat(chartOfAccountList).hasSize(databaseSizeBeforeUpdate);
        ChartOfAccount testChartOfAccount = chartOfAccountList.get(chartOfAccountList.size() - 1);
        assertThat(testChartOfAccount.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testChartOfAccount.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testChartOfAccount.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testChartOfAccount.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testChartOfAccount.getSegmentSeperator()).isEqualTo(UPDATED_SEGMENT_SEPERATOR);
        assertThat(testChartOfAccount.isEnabledFlag()).isEqualTo(UPDATED_ENABLED_FLAG);
        assertThat(testChartOfAccount.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testChartOfAccount.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testChartOfAccount.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testChartOfAccount.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingChartOfAccount() throws Exception {
        int databaseSizeBeforeUpdate = chartOfAccountRepository.findAll().size();

        // Create the ChartOfAccount

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restChartOfAccountMockMvc.perform(put("/api/chart-of-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chartOfAccount)))
            .andExpect(status().isCreated());

        // Validate the ChartOfAccount in the database
        List<ChartOfAccount> chartOfAccountList = chartOfAccountRepository.findAll();
        assertThat(chartOfAccountList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteChartOfAccount() throws Exception {
        // Initialize the database
        chartOfAccountRepository.saveAndFlush(chartOfAccount);
        int databaseSizeBeforeDelete = chartOfAccountRepository.findAll().size();

        // Get the chartOfAccount
        restChartOfAccountMockMvc.perform(delete("/api/chart-of-accounts/{id}", chartOfAccount.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ChartOfAccount> chartOfAccountList = chartOfAccountRepository.findAll();
        assertThat(chartOfAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChartOfAccount.class);
        ChartOfAccount chartOfAccount1 = new ChartOfAccount();
        chartOfAccount1.setId(1L);
        ChartOfAccount chartOfAccount2 = new ChartOfAccount();
        chartOfAccount2.setId(chartOfAccount1.getId());
        assertThat(chartOfAccount1).isEqualTo(chartOfAccount2);
        chartOfAccount2.setId(2L);
        assertThat(chartOfAccount1).isNotEqualTo(chartOfAccount2);
        chartOfAccount1.setId(null);
        assertThat(chartOfAccount1).isNotEqualTo(chartOfAccount2);
    }
}
