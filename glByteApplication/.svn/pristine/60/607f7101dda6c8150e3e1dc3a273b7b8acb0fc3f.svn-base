package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.AcctRuleDerivations;
import com.nspl.app.repository.AcctRuleDerivationsRepository;
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
 * Test class for the AcctRuleDerivationsResource REST controller.
 *
 * @see AcctRuleDerivationsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class AcctRuleDerivationsResourceIntTest {

    private static final Long DEFAULT_ACCT_RULE_ACTION_ID = 1L;
    private static final Long UPDATED_ACCT_RULE_ACTION_ID = 2L;

    private static final String DEFAULT_DATA_VIEW_COLUMN = "AAAAAAAAAA";
    private static final String UPDATED_DATA_VIEW_COLUMN = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNTING_REFERENCES = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNTING_REFERENCES = "BBBBBBBBBB";

    private static final String DEFAULT_CONSTANT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_CONSTANT_VALUE = "BBBBBBBBBB";

    private static final Long DEFAULT_MAPPING_SET_ID = 1L;
    private static final Long UPDATED_MAPPING_SET_ID = 2L;

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private AcctRuleDerivationsRepository acctRuleDerivationsRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAcctRuleDerivationsMockMvc;

    private AcctRuleDerivations acctRuleDerivations;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AcctRuleDerivationsResource acctRuleDerivationsResource = new AcctRuleDerivationsResource(acctRuleDerivationsRepository);
        this.restAcctRuleDerivationsMockMvc = MockMvcBuilders.standaloneSetup(acctRuleDerivationsResource)
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
    public static AcctRuleDerivations createEntity(EntityManager em) {
        AcctRuleDerivations acctRuleDerivations = new AcctRuleDerivations()
            .acctRuleActionId(DEFAULT_ACCT_RULE_ACTION_ID)
            .dataViewColumn(DEFAULT_DATA_VIEW_COLUMN)
            .accountingReferences(DEFAULT_ACCOUNTING_REFERENCES)
            .constantValue(DEFAULT_CONSTANT_VALUE)
            .mappingSetId(DEFAULT_MAPPING_SET_ID)
            .createdBy(DEFAULT_CREATED_BY)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE);
        return acctRuleDerivations;
    }

    @Before
    public void initTest() {
        acctRuleDerivations = createEntity(em);
    }

    @Test
    @Transactional
    public void createAcctRuleDerivations() throws Exception {
        int databaseSizeBeforeCreate = acctRuleDerivationsRepository.findAll().size();

        // Create the AcctRuleDerivations
        restAcctRuleDerivationsMockMvc.perform(post("/api/acct-rule-derivations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(acctRuleDerivations)))
            .andExpect(status().isCreated());

        // Validate the AcctRuleDerivations in the database
        List<AcctRuleDerivations> acctRuleDerivationsList = acctRuleDerivationsRepository.findAll();
        assertThat(acctRuleDerivationsList).hasSize(databaseSizeBeforeCreate + 1);
        AcctRuleDerivations testAcctRuleDerivations = acctRuleDerivationsList.get(acctRuleDerivationsList.size() - 1);
        assertThat(testAcctRuleDerivations.getAcctRuleActionId()).isEqualTo(DEFAULT_ACCT_RULE_ACTION_ID);
        assertThat(testAcctRuleDerivations.getDataViewColumn()).isEqualTo(DEFAULT_DATA_VIEW_COLUMN);
        assertThat(testAcctRuleDerivations.getAccountingReferences()).isEqualTo(DEFAULT_ACCOUNTING_REFERENCES);
        assertThat(testAcctRuleDerivations.getConstantValue()).isEqualTo(DEFAULT_CONSTANT_VALUE);
        assertThat(testAcctRuleDerivations.getMappingSetId()).isEqualTo(DEFAULT_MAPPING_SET_ID);
        assertThat(testAcctRuleDerivations.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testAcctRuleDerivations.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testAcctRuleDerivations.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testAcctRuleDerivations.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void createAcctRuleDerivationsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = acctRuleDerivationsRepository.findAll().size();

        // Create the AcctRuleDerivations with an existing ID
        acctRuleDerivations.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAcctRuleDerivationsMockMvc.perform(post("/api/acct-rule-derivations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(acctRuleDerivations)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<AcctRuleDerivations> acctRuleDerivationsList = acctRuleDerivationsRepository.findAll();
        assertThat(acctRuleDerivationsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAcctRuleDerivations() throws Exception {
        // Initialize the database
        acctRuleDerivationsRepository.saveAndFlush(acctRuleDerivations);

        // Get all the acctRuleDerivationsList
        restAcctRuleDerivationsMockMvc.perform(get("/api/acct-rule-derivations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(acctRuleDerivations.getId().intValue())))
            .andExpect(jsonPath("$.[*].acctRuleActionId").value(hasItem(DEFAULT_ACCT_RULE_ACTION_ID.intValue())))
            .andExpect(jsonPath("$.[*].dataViewColumn").value(hasItem(DEFAULT_DATA_VIEW_COLUMN.toString())))
            .andExpect(jsonPath("$.[*].accountingReferences").value(hasItem(DEFAULT_ACCOUNTING_REFERENCES.toString())))
            .andExpect(jsonPath("$.[*].constantValue").value(hasItem(DEFAULT_CONSTANT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].mappingSetId").value(hasItem(DEFAULT_MAPPING_SET_ID.intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void getAcctRuleDerivations() throws Exception {
        // Initialize the database
        acctRuleDerivationsRepository.saveAndFlush(acctRuleDerivations);

        // Get the acctRuleDerivations
        restAcctRuleDerivationsMockMvc.perform(get("/api/acct-rule-derivations/{id}", acctRuleDerivations.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(acctRuleDerivations.getId().intValue()))
            .andExpect(jsonPath("$.acctRuleActionId").value(DEFAULT_ACCT_RULE_ACTION_ID.intValue()))
            .andExpect(jsonPath("$.dataViewColumn").value(DEFAULT_DATA_VIEW_COLUMN.toString()))
            .andExpect(jsonPath("$.accountingReferences").value(DEFAULT_ACCOUNTING_REFERENCES.toString()))
            .andExpect(jsonPath("$.constantValue").value(DEFAULT_CONSTANT_VALUE.toString()))
            .andExpect(jsonPath("$.mappingSetId").value(DEFAULT_MAPPING_SET_ID.intValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingAcctRuleDerivations() throws Exception {
        // Get the acctRuleDerivations
        restAcctRuleDerivationsMockMvc.perform(get("/api/acct-rule-derivations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAcctRuleDerivations() throws Exception {
        // Initialize the database
        acctRuleDerivationsRepository.saveAndFlush(acctRuleDerivations);
        int databaseSizeBeforeUpdate = acctRuleDerivationsRepository.findAll().size();

        // Update the acctRuleDerivations
        AcctRuleDerivations updatedAcctRuleDerivations = acctRuleDerivationsRepository.findOne(acctRuleDerivations.getId());
        updatedAcctRuleDerivations
            .acctRuleActionId(UPDATED_ACCT_RULE_ACTION_ID)
            .dataViewColumn(UPDATED_DATA_VIEW_COLUMN)
            .accountingReferences(UPDATED_ACCOUNTING_REFERENCES)
            .constantValue(UPDATED_CONSTANT_VALUE)
            .mappingSetId(UPDATED_MAPPING_SET_ID)
            .createdBy(UPDATED_CREATED_BY)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE);

        restAcctRuleDerivationsMockMvc.perform(put("/api/acct-rule-derivations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAcctRuleDerivations)))
            .andExpect(status().isOk());

        // Validate the AcctRuleDerivations in the database
        List<AcctRuleDerivations> acctRuleDerivationsList = acctRuleDerivationsRepository.findAll();
        assertThat(acctRuleDerivationsList).hasSize(databaseSizeBeforeUpdate);
        AcctRuleDerivations testAcctRuleDerivations = acctRuleDerivationsList.get(acctRuleDerivationsList.size() - 1);
        assertThat(testAcctRuleDerivations.getAcctRuleActionId()).isEqualTo(UPDATED_ACCT_RULE_ACTION_ID);
        assertThat(testAcctRuleDerivations.getDataViewColumn()).isEqualTo(UPDATED_DATA_VIEW_COLUMN);
        assertThat(testAcctRuleDerivations.getAccountingReferences()).isEqualTo(UPDATED_ACCOUNTING_REFERENCES);
        assertThat(testAcctRuleDerivations.getConstantValue()).isEqualTo(UPDATED_CONSTANT_VALUE);
        assertThat(testAcctRuleDerivations.getMappingSetId()).isEqualTo(UPDATED_MAPPING_SET_ID);
        assertThat(testAcctRuleDerivations.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAcctRuleDerivations.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testAcctRuleDerivations.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testAcctRuleDerivations.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingAcctRuleDerivations() throws Exception {
        int databaseSizeBeforeUpdate = acctRuleDerivationsRepository.findAll().size();

        // Create the AcctRuleDerivations

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAcctRuleDerivationsMockMvc.perform(put("/api/acct-rule-derivations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(acctRuleDerivations)))
            .andExpect(status().isCreated());

        // Validate the AcctRuleDerivations in the database
        List<AcctRuleDerivations> acctRuleDerivationsList = acctRuleDerivationsRepository.findAll();
        assertThat(acctRuleDerivationsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAcctRuleDerivations() throws Exception {
        // Initialize the database
        acctRuleDerivationsRepository.saveAndFlush(acctRuleDerivations);
        int databaseSizeBeforeDelete = acctRuleDerivationsRepository.findAll().size();

        // Get the acctRuleDerivations
        restAcctRuleDerivationsMockMvc.perform(delete("/api/acct-rule-derivations/{id}", acctRuleDerivations.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AcctRuleDerivations> acctRuleDerivationsList = acctRuleDerivationsRepository.findAll();
        assertThat(acctRuleDerivationsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AcctRuleDerivations.class);
    }
}
