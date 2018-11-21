package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.AccountingEvents;
import com.nspl.app.repository.AccountingEventsRepository;
import com.nspl.app.service.AccountingEventsService;
import com.nspl.app.repository.search.AccountingEventsSearchRepository;
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
 * Test class for the AccountingEventsResource REST controller.
 *
 * @see AccountingEventsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class AccountingEventsResourceIntTest {

    private static final Long DEFAULT_DATA_VIEW_ID = 1L;
    private static final Long UPDATED_DATA_VIEW_ID = 2L;

    private static final Long DEFAULT_ROW_ID = 1L;
    private static final Long UPDATED_ROW_ID = 2L;

    private static final String DEFAULT_EVENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_ACCT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_ACCT_STATUS = "BBBBBBBBBB";

    private static final Long DEFAULT_ACCT_RULE_GROUP_ID = 1L;
    private static final Long UPDATED_ACCT_RULE_GROUP_ID = 2L;

    private static final Long DEFAULT_RULE_ID = 1L;
    private static final Long UPDATED_RULE_ID = 2L;

    private static final ZonedDateTime DEFAULT_EVENT_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_EVENT_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private AccountingEventsRepository accountingEventsRepository;

    @Autowired
    private AccountingEventsService accountingEventsService;

    @Autowired
    private AccountingEventsSearchRepository accountingEventsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAccountingEventsMockMvc;

    private AccountingEvents accountingEvents;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AccountingEventsResource accountingEventsResource = new AccountingEventsResource(accountingEventsService);
        this.restAccountingEventsMockMvc = MockMvcBuilders.standaloneSetup(accountingEventsResource)
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
    public static AccountingEvents createEntity(EntityManager em) {
        AccountingEvents accountingEvents = new AccountingEvents()
            .dataViewId(DEFAULT_DATA_VIEW_ID)
            .rowId(DEFAULT_ROW_ID)
            .eventType(DEFAULT_EVENT_TYPE)
            .acctStatus(DEFAULT_ACCT_STATUS)
            .acctRuleGroupId(DEFAULT_ACCT_RULE_GROUP_ID)
            .ruleId(DEFAULT_RULE_ID)
            .eventTime(DEFAULT_EVENT_TIME)
            .createdBy(DEFAULT_CREATED_BY)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .creationDate(DEFAULT_CREATION_DATE)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE);
        return accountingEvents;
    }

    @Before
    public void initTest() {
        accountingEventsSearchRepository.deleteAll();
        accountingEvents = createEntity(em);
    }

    @Test
    @Transactional
    public void createAccountingEvents() throws Exception {
        int databaseSizeBeforeCreate = accountingEventsRepository.findAll().size();

        // Create the AccountingEvents
        restAccountingEventsMockMvc.perform(post("/api/accounting-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountingEvents)))
            .andExpect(status().isCreated());

        // Validate the AccountingEvents in the database
        List<AccountingEvents> accountingEventsList = accountingEventsRepository.findAll();
        assertThat(accountingEventsList).hasSize(databaseSizeBeforeCreate + 1);
        AccountingEvents testAccountingEvents = accountingEventsList.get(accountingEventsList.size() - 1);
        assertThat(testAccountingEvents.getDataViewId()).isEqualTo(DEFAULT_DATA_VIEW_ID);
        assertThat(testAccountingEvents.getRowId()).isEqualTo(DEFAULT_ROW_ID);
        assertThat(testAccountingEvents.getEventType()).isEqualTo(DEFAULT_EVENT_TYPE);
        assertThat(testAccountingEvents.getAcctStatus()).isEqualTo(DEFAULT_ACCT_STATUS);
        assertThat(testAccountingEvents.getAcctRuleGroupId()).isEqualTo(DEFAULT_ACCT_RULE_GROUP_ID);
        assertThat(testAccountingEvents.getRuleId()).isEqualTo(DEFAULT_RULE_ID);
        assertThat(testAccountingEvents.getEventTime()).isEqualTo(DEFAULT_EVENT_TIME);
        assertThat(testAccountingEvents.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testAccountingEvents.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testAccountingEvents.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testAccountingEvents.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);

        // Validate the AccountingEvents in Elasticsearch
        AccountingEvents accountingEventsEs = accountingEventsSearchRepository.findOne(testAccountingEvents.getId());
        assertThat(accountingEventsEs).isEqualToComparingFieldByField(testAccountingEvents);
    }

    @Test
    @Transactional
    public void createAccountingEventsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = accountingEventsRepository.findAll().size();

        // Create the AccountingEvents with an existing ID
        accountingEvents.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountingEventsMockMvc.perform(post("/api/accounting-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountingEvents)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<AccountingEvents> accountingEventsList = accountingEventsRepository.findAll();
        assertThat(accountingEventsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAccountingEvents() throws Exception {
        // Initialize the database
        accountingEventsRepository.saveAndFlush(accountingEvents);

        // Get all the accountingEventsList
        restAccountingEventsMockMvc.perform(get("/api/accounting-events?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountingEvents.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataViewId").value(hasItem(DEFAULT_DATA_VIEW_ID.intValue())))
            .andExpect(jsonPath("$.[*].rowId").value(hasItem(DEFAULT_ROW_ID.intValue())))
            .andExpect(jsonPath("$.[*].eventType").value(hasItem(DEFAULT_EVENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].acctStatus").value(hasItem(DEFAULT_ACCT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].acctRuleGroupId").value(hasItem(DEFAULT_ACCT_RULE_GROUP_ID.intValue())))
            .andExpect(jsonPath("$.[*].ruleId").value(hasItem(DEFAULT_RULE_ID.intValue())))
            .andExpect(jsonPath("$.[*].eventTime").value(hasItem(sameInstant(DEFAULT_EVENT_TIME))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void getAccountingEvents() throws Exception {
        // Initialize the database
        accountingEventsRepository.saveAndFlush(accountingEvents);

        // Get the accountingEvents
        restAccountingEventsMockMvc.perform(get("/api/accounting-events/{id}", accountingEvents.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(accountingEvents.getId().intValue()))
            .andExpect(jsonPath("$.dataViewId").value(DEFAULT_DATA_VIEW_ID.intValue()))
            .andExpect(jsonPath("$.rowId").value(DEFAULT_ROW_ID.intValue()))
            .andExpect(jsonPath("$.eventType").value(DEFAULT_EVENT_TYPE.toString()))
            .andExpect(jsonPath("$.acctStatus").value(DEFAULT_ACCT_STATUS.toString()))
            .andExpect(jsonPath("$.acctRuleGroupId").value(DEFAULT_ACCT_RULE_GROUP_ID.intValue()))
            .andExpect(jsonPath("$.ruleId").value(DEFAULT_RULE_ID.intValue()))
            .andExpect(jsonPath("$.eventTime").value(sameInstant(DEFAULT_EVENT_TIME)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.creationDate").value(sameInstant(DEFAULT_CREATION_DATE)))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingAccountingEvents() throws Exception {
        // Get the accountingEvents
        restAccountingEventsMockMvc.perform(get("/api/accounting-events/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAccountingEvents() throws Exception {
        // Initialize the database
        accountingEventsService.save(accountingEvents);

        int databaseSizeBeforeUpdate = accountingEventsRepository.findAll().size();

        // Update the accountingEvents
        AccountingEvents updatedAccountingEvents = accountingEventsRepository.findOne(accountingEvents.getId());
        updatedAccountingEvents
            .dataViewId(UPDATED_DATA_VIEW_ID)
            .rowId(UPDATED_ROW_ID)
            .eventType(UPDATED_EVENT_TYPE)
            .acctStatus(UPDATED_ACCT_STATUS)
            .acctRuleGroupId(UPDATED_ACCT_RULE_GROUP_ID)
            .ruleId(UPDATED_RULE_ID)
            .eventTime(UPDATED_EVENT_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE);

        restAccountingEventsMockMvc.perform(put("/api/accounting-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAccountingEvents)))
            .andExpect(status().isOk());

        // Validate the AccountingEvents in the database
        List<AccountingEvents> accountingEventsList = accountingEventsRepository.findAll();
        assertThat(accountingEventsList).hasSize(databaseSizeBeforeUpdate);
        AccountingEvents testAccountingEvents = accountingEventsList.get(accountingEventsList.size() - 1);
        assertThat(testAccountingEvents.getDataViewId()).isEqualTo(UPDATED_DATA_VIEW_ID);
        assertThat(testAccountingEvents.getRowId()).isEqualTo(UPDATED_ROW_ID);
        assertThat(testAccountingEvents.getEventType()).isEqualTo(UPDATED_EVENT_TYPE);
        assertThat(testAccountingEvents.getAcctStatus()).isEqualTo(UPDATED_ACCT_STATUS);
        assertThat(testAccountingEvents.getAcctRuleGroupId()).isEqualTo(UPDATED_ACCT_RULE_GROUP_ID);
        assertThat(testAccountingEvents.getRuleId()).isEqualTo(UPDATED_RULE_ID);
        assertThat(testAccountingEvents.getEventTime()).isEqualTo(UPDATED_EVENT_TIME);
        assertThat(testAccountingEvents.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAccountingEvents.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testAccountingEvents.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testAccountingEvents.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);

        // Validate the AccountingEvents in Elasticsearch
        AccountingEvents accountingEventsEs = accountingEventsSearchRepository.findOne(testAccountingEvents.getId());
        assertThat(accountingEventsEs).isEqualToComparingFieldByField(testAccountingEvents);
    }

    @Test
    @Transactional
    public void updateNonExistingAccountingEvents() throws Exception {
        int databaseSizeBeforeUpdate = accountingEventsRepository.findAll().size();

        // Create the AccountingEvents

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAccountingEventsMockMvc.perform(put("/api/accounting-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountingEvents)))
            .andExpect(status().isCreated());

        // Validate the AccountingEvents in the database
        List<AccountingEvents> accountingEventsList = accountingEventsRepository.findAll();
        assertThat(accountingEventsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAccountingEvents() throws Exception {
        // Initialize the database
        accountingEventsService.save(accountingEvents);

        int databaseSizeBeforeDelete = accountingEventsRepository.findAll().size();

        // Get the accountingEvents
        restAccountingEventsMockMvc.perform(delete("/api/accounting-events/{id}", accountingEvents.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean accountingEventsExistsInEs = accountingEventsSearchRepository.exists(accountingEvents.getId());
        assertThat(accountingEventsExistsInEs).isFalse();

        // Validate the database is empty
        List<AccountingEvents> accountingEventsList = accountingEventsRepository.findAll();
        assertThat(accountingEventsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAccountingEvents() throws Exception {
        // Initialize the database
        accountingEventsService.save(accountingEvents);

        // Search the accountingEvents
        restAccountingEventsMockMvc.perform(get("/api/_search/accounting-events?query=id:" + accountingEvents.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountingEvents.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataViewId").value(hasItem(DEFAULT_DATA_VIEW_ID.intValue())))
            .andExpect(jsonPath("$.[*].rowId").value(hasItem(DEFAULT_ROW_ID.intValue())))
            .andExpect(jsonPath("$.[*].eventType").value(hasItem(DEFAULT_EVENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].acctStatus").value(hasItem(DEFAULT_ACCT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].acctRuleGroupId").value(hasItem(DEFAULT_ACCT_RULE_GROUP_ID.intValue())))
            .andExpect(jsonPath("$.[*].ruleId").value(hasItem(DEFAULT_RULE_ID.intValue())))
            .andExpect(jsonPath("$.[*].eventTime").value(hasItem(sameInstant(DEFAULT_EVENT_TIME))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountingEvents.class);
    }
}
