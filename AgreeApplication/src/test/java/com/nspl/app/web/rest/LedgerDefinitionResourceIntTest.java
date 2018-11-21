package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.LedgerDefinition;
import com.nspl.app.repository.LedgerDefinitionRepository;
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
 * Test class for the LedgerDefinitionResource REST controller.
 *
 * @see LedgerDefinitionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class LedgerDefinitionResourceIntTest {

    private static final Long DEFAULT_TENANT_ID = 1L;
    private static final Long UPDATED_TENANT_ID = 2L;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_LEDGER_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_LEDGER_TYPE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE =ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY = "BBBBBBBBBB";

    private static final Long DEFAULT_CALENDAR_ID = 1L;
    private static final Long UPDATED_CALENDAR_ID = 2L;

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

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
    private LedgerDefinitionRepository ledgerDefinitionRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLedgerDefinitionMockMvc;

    private LedgerDefinition ledgerDefinition;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LedgerDefinitionResource ledgerDefinitionResource = new LedgerDefinitionResource(ledgerDefinitionRepository);
        this.restLedgerDefinitionMockMvc = MockMvcBuilders.standaloneSetup(ledgerDefinitionResource)
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
    public static LedgerDefinition createEntity(EntityManager em) {
        LedgerDefinition ledgerDefinition = new LedgerDefinition()
            .tenantId(DEFAULT_TENANT_ID)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .ledgerType(DEFAULT_LEDGER_TYPE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .currency(DEFAULT_CURRENCY)
            .calendarId(DEFAULT_CALENDAR_ID)
            .status(DEFAULT_STATUS)
            .enabledFlag(DEFAULT_ENABLED_FLAG)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE);
        return ledgerDefinition;
    }

    @Before
    public void initTest() {
        ledgerDefinition = createEntity(em);
    }

    @Test
    @Transactional
    public void createLedgerDefinition() throws Exception {
        int databaseSizeBeforeCreate = ledgerDefinitionRepository.findAll().size();

        // Create the LedgerDefinition
        restLedgerDefinitionMockMvc.perform(post("/api/ledger-definitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ledgerDefinition)))
            .andExpect(status().isCreated());

        // Validate the LedgerDefinition in the database
        List<LedgerDefinition> ledgerDefinitionList = ledgerDefinitionRepository.findAll();
        assertThat(ledgerDefinitionList).hasSize(databaseSizeBeforeCreate + 1);
        LedgerDefinition testLedgerDefinition = ledgerDefinitionList.get(ledgerDefinitionList.size() - 1);
        assertThat(testLedgerDefinition.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testLedgerDefinition.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLedgerDefinition.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testLedgerDefinition.getLedgerType()).isEqualTo(DEFAULT_LEDGER_TYPE);
        assertThat(testLedgerDefinition.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testLedgerDefinition.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testLedgerDefinition.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testLedgerDefinition.getCalendarId()).isEqualTo(DEFAULT_CALENDAR_ID);
        assertThat(testLedgerDefinition.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testLedgerDefinition.isEnabledFlag()).isEqualTo(DEFAULT_ENABLED_FLAG);
        assertThat(testLedgerDefinition.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testLedgerDefinition.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testLedgerDefinition.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testLedgerDefinition.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void createLedgerDefinitionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ledgerDefinitionRepository.findAll().size();

        // Create the LedgerDefinition with an existing ID
        ledgerDefinition.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLedgerDefinitionMockMvc.perform(post("/api/ledger-definitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ledgerDefinition)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<LedgerDefinition> ledgerDefinitionList = ledgerDefinitionRepository.findAll();
        assertThat(ledgerDefinitionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllLedgerDefinitions() throws Exception {
        // Initialize the database
        ledgerDefinitionRepository.saveAndFlush(ledgerDefinition);

        // Get all the ledgerDefinitionList
        restLedgerDefinitionMockMvc.perform(get("/api/ledger-definitions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ledgerDefinition.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].ledgerType").value(hasItem(DEFAULT_LEDGER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].calendarId").value(hasItem(DEFAULT_CALENDAR_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].enabledFlag").value(hasItem(DEFAULT_ENABLED_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void getLedgerDefinition() throws Exception {
        // Initialize the database
        ledgerDefinitionRepository.saveAndFlush(ledgerDefinition);

        // Get the ledgerDefinition
        restLedgerDefinitionMockMvc.perform(get("/api/ledger-definitions/{id}", ledgerDefinition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ledgerDefinition.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.ledgerType").value(DEFAULT_LEDGER_TYPE.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY.toString()))
            .andExpect(jsonPath("$.calendarId").value(DEFAULT_CALENDAR_ID.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.enabledFlag").value(DEFAULT_ENABLED_FLAG.booleanValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingLedgerDefinition() throws Exception {
        // Get the ledgerDefinition
        restLedgerDefinitionMockMvc.perform(get("/api/ledger-definitions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLedgerDefinition() throws Exception {
        // Initialize the database
        ledgerDefinitionRepository.saveAndFlush(ledgerDefinition);
        int databaseSizeBeforeUpdate = ledgerDefinitionRepository.findAll().size();

        // Update the ledgerDefinition
        LedgerDefinition updatedLedgerDefinition = ledgerDefinitionRepository.findOne(ledgerDefinition.getId());
        updatedLedgerDefinition
            .tenantId(UPDATED_TENANT_ID)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .ledgerType(UPDATED_LEDGER_TYPE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .currency(UPDATED_CURRENCY)
            .calendarId(UPDATED_CALENDAR_ID)
            .status(UPDATED_STATUS)
            .enabledFlag(UPDATED_ENABLED_FLAG)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE);

        restLedgerDefinitionMockMvc.perform(put("/api/ledger-definitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLedgerDefinition)))
            .andExpect(status().isOk());

        // Validate the LedgerDefinition in the database
        List<LedgerDefinition> ledgerDefinitionList = ledgerDefinitionRepository.findAll();
        assertThat(ledgerDefinitionList).hasSize(databaseSizeBeforeUpdate);
        LedgerDefinition testLedgerDefinition = ledgerDefinitionList.get(ledgerDefinitionList.size() - 1);
        assertThat(testLedgerDefinition.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testLedgerDefinition.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLedgerDefinition.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLedgerDefinition.getLedgerType()).isEqualTo(UPDATED_LEDGER_TYPE);
        assertThat(testLedgerDefinition.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testLedgerDefinition.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testLedgerDefinition.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testLedgerDefinition.getCalendarId()).isEqualTo(UPDATED_CALENDAR_ID);
        assertThat(testLedgerDefinition.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testLedgerDefinition.isEnabledFlag()).isEqualTo(UPDATED_ENABLED_FLAG);
        assertThat(testLedgerDefinition.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testLedgerDefinition.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testLedgerDefinition.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testLedgerDefinition.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingLedgerDefinition() throws Exception {
        int databaseSizeBeforeUpdate = ledgerDefinitionRepository.findAll().size();

        // Create the LedgerDefinition

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLedgerDefinitionMockMvc.perform(put("/api/ledger-definitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ledgerDefinition)))
            .andExpect(status().isCreated());

        // Validate the LedgerDefinition in the database
        List<LedgerDefinition> ledgerDefinitionList = ledgerDefinitionRepository.findAll();
        assertThat(ledgerDefinitionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLedgerDefinition() throws Exception {
        // Initialize the database
        ledgerDefinitionRepository.saveAndFlush(ledgerDefinition);
        int databaseSizeBeforeDelete = ledgerDefinitionRepository.findAll().size();

        // Get the ledgerDefinition
        restLedgerDefinitionMockMvc.perform(delete("/api/ledger-definitions/{id}", ledgerDefinition.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<LedgerDefinition> ledgerDefinitionList = ledgerDefinitionRepository.findAll();
        assertThat(ledgerDefinitionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LedgerDefinition.class);
    }
}
