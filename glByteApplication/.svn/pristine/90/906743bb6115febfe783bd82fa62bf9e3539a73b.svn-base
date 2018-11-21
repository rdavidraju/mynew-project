package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.AcctRuleConditions;
import com.nspl.app.repository.AcctRuleConditionsRepository;
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
 * Test class for the AcctRuleConditionsResource REST controller.
 *
 * @see AcctRuleConditionsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class AcctRuleConditionsResourceIntTest {

    private static final Long DEFAULT_RULE_ID = 1L;
    private static final Long UPDATED_RULE_ID = 2L;

    private static final Long DEFAULT_RULE_ACTION_ID = 1L;
    private static final Long UPDATED_RULE_ACTION_ID = 2L;

    private static final String DEFAULT_OPEN_BRACKET = "AAAAAAAAAA";
    private static final String UPDATED_OPEN_BRACKET = "BBBBBBBBBB";

    private static final Long DEFAULT_S_VIEW_COLUMN_Id= 1L;
    private static final Long UPDATED_S_VIEW_COLUMN_Id = 2L;

    private static final String DEFAULT_OPERATOR = "AAAAAAAAAA";
    private static final String UPDATED_OPERATOR = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_CLOSE_BRACKET = "AAAAAAAAAA";
    private static final String UPDATED_CLOSE_BRACKET = "BBBBBBBBBB";

    private static final String DEFAULT_LOGICAL_OPERATOR = "AAAAAAAAAA";
    private static final String UPDATED_LOGICAL_OPERATOR = "BBBBBBBBBB";

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private AcctRuleConditionsRepository acctRuleConditionsRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAcctRuleConditionsMockMvc;

    private AcctRuleConditions acctRuleConditions;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AcctRuleConditionsResource acctRuleConditionsResource = new AcctRuleConditionsResource(acctRuleConditionsRepository);
        this.restAcctRuleConditionsMockMvc = MockMvcBuilders.standaloneSetup(acctRuleConditionsResource)
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
    public static AcctRuleConditions createEntity(EntityManager em) {
        AcctRuleConditions acctRuleConditions = new AcctRuleConditions()
            .ruleId(DEFAULT_RULE_ID)
            .ruleActionId(DEFAULT_RULE_ACTION_ID)
            .openBracket(DEFAULT_OPEN_BRACKET)
            .sViewColumnId(DEFAULT_S_VIEW_COLUMN_Id)
            .operator(DEFAULT_OPERATOR)
            .value(DEFAULT_VALUE)
            .closeBracket(DEFAULT_CLOSE_BRACKET)
            .logicalOperator(DEFAULT_LOGICAL_OPERATOR)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE);
        return acctRuleConditions;
    }

    @Before
    public void initTest() {
        acctRuleConditions = createEntity(em);
    }

    @Test
    @Transactional
    public void createAcctRuleConditions() throws Exception {
        int databaseSizeBeforeCreate = acctRuleConditionsRepository.findAll().size();

        // Create the AcctRuleConditions
        restAcctRuleConditionsMockMvc.perform(post("/api/acct-rule-conditions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(acctRuleConditions)))
            .andExpect(status().isCreated());

        // Validate the AcctRuleConditions in the database
        List<AcctRuleConditions> acctRuleConditionsList = acctRuleConditionsRepository.findAll();
        assertThat(acctRuleConditionsList).hasSize(databaseSizeBeforeCreate + 1);
        AcctRuleConditions testAcctRuleConditions = acctRuleConditionsList.get(acctRuleConditionsList.size() - 1);
        assertThat(testAcctRuleConditions.getRuleId()).isEqualTo(DEFAULT_RULE_ID);
        assertThat(testAcctRuleConditions.getRuleActionId()).isEqualTo(DEFAULT_RULE_ACTION_ID);
        assertThat(testAcctRuleConditions.getOpenBracket()).isEqualTo(DEFAULT_OPEN_BRACKET);
        assertThat(testAcctRuleConditions.getsViewColumnId()).isEqualTo(DEFAULT_S_VIEW_COLUMN_Id);
        assertThat(testAcctRuleConditions.getOperator()).isEqualTo(DEFAULT_OPERATOR);
        assertThat(testAcctRuleConditions.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testAcctRuleConditions.getCloseBracket()).isEqualTo(DEFAULT_CLOSE_BRACKET);
        assertThat(testAcctRuleConditions.getLogicalOperator()).isEqualTo(DEFAULT_LOGICAL_OPERATOR);
        assertThat(testAcctRuleConditions.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testAcctRuleConditions.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testAcctRuleConditions.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testAcctRuleConditions.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void createAcctRuleConditionsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = acctRuleConditionsRepository.findAll().size();

        // Create the AcctRuleConditions with an existing ID
        acctRuleConditions.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAcctRuleConditionsMockMvc.perform(post("/api/acct-rule-conditions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(acctRuleConditions)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<AcctRuleConditions> acctRuleConditionsList = acctRuleConditionsRepository.findAll();
        assertThat(acctRuleConditionsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAcctRuleConditions() throws Exception {
        // Initialize the database
        acctRuleConditionsRepository.saveAndFlush(acctRuleConditions);

        // Get all the acctRuleConditionsList
        restAcctRuleConditionsMockMvc.perform(get("/api/acct-rule-conditions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(acctRuleConditions.getId().intValue())))
            .andExpect(jsonPath("$.[*].ruleId").value(hasItem(DEFAULT_RULE_ID.intValue())))
            .andExpect(jsonPath("$.[*].ruleActionId").value(hasItem(DEFAULT_RULE_ACTION_ID.intValue())))
            .andExpect(jsonPath("$.[*].openBracket").value(hasItem(DEFAULT_OPEN_BRACKET.toString())))
            .andExpect(jsonPath("$.[*].sViewColumn").value(hasItem(DEFAULT_S_VIEW_COLUMN_Id.toString())))
            .andExpect(jsonPath("$.[*].operator").value(hasItem(DEFAULT_OPERATOR.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].closeBracket").value(hasItem(DEFAULT_CLOSE_BRACKET.toString())))
            .andExpect(jsonPath("$.[*].logicalOperator").value(hasItem(DEFAULT_LOGICAL_OPERATOR.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void getAcctRuleConditions() throws Exception {
        // Initialize the database
        acctRuleConditionsRepository.saveAndFlush(acctRuleConditions);

        // Get the acctRuleConditions
        restAcctRuleConditionsMockMvc.perform(get("/api/acct-rule-conditions/{id}", acctRuleConditions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(acctRuleConditions.getId().intValue()))
            .andExpect(jsonPath("$.ruleId").value(DEFAULT_RULE_ID.intValue()))
            .andExpect(jsonPath("$.ruleActionId").value(DEFAULT_RULE_ACTION_ID.intValue()))
            .andExpect(jsonPath("$.openBracket").value(DEFAULT_OPEN_BRACKET.toString()))
            .andExpect(jsonPath("$.sViewColumn").value(DEFAULT_S_VIEW_COLUMN_Id.toString()))
            .andExpect(jsonPath("$.operator").value(DEFAULT_OPERATOR.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()))
            .andExpect(jsonPath("$.closeBracket").value(DEFAULT_CLOSE_BRACKET.toString()))
            .andExpect(jsonPath("$.logicalOperator").value(DEFAULT_LOGICAL_OPERATOR.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingAcctRuleConditions() throws Exception {
        // Get the acctRuleConditions
        restAcctRuleConditionsMockMvc.perform(get("/api/acct-rule-conditions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAcctRuleConditions() throws Exception {
        // Initialize the database
        acctRuleConditionsRepository.saveAndFlush(acctRuleConditions);
        int databaseSizeBeforeUpdate = acctRuleConditionsRepository.findAll().size();

        // Update the acctRuleConditions
        AcctRuleConditions updatedAcctRuleConditions = acctRuleConditionsRepository.findOne(acctRuleConditions.getId());
        updatedAcctRuleConditions
            .ruleId(UPDATED_RULE_ID)
            .ruleActionId(UPDATED_RULE_ACTION_ID)
            .openBracket(UPDATED_OPEN_BRACKET)
            .sViewColumnId(UPDATED_S_VIEW_COLUMN_Id)
            .operator(UPDATED_OPERATOR)
            .value(UPDATED_VALUE)
            .closeBracket(UPDATED_CLOSE_BRACKET)
            .logicalOperator(UPDATED_LOGICAL_OPERATOR)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE);

        restAcctRuleConditionsMockMvc.perform(put("/api/acct-rule-conditions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAcctRuleConditions)))
            .andExpect(status().isOk());

        // Validate the AcctRuleConditions in the database
        List<AcctRuleConditions> acctRuleConditionsList = acctRuleConditionsRepository.findAll();
        assertThat(acctRuleConditionsList).hasSize(databaseSizeBeforeUpdate);
        AcctRuleConditions testAcctRuleConditions = acctRuleConditionsList.get(acctRuleConditionsList.size() - 1);
        assertThat(testAcctRuleConditions.getRuleId()).isEqualTo(UPDATED_RULE_ID);
        assertThat(testAcctRuleConditions.getRuleActionId()).isEqualTo(UPDATED_RULE_ACTION_ID);
        assertThat(testAcctRuleConditions.getOpenBracket()).isEqualTo(UPDATED_OPEN_BRACKET);
        assertThat(testAcctRuleConditions.getsViewColumnId()).isEqualTo(UPDATED_S_VIEW_COLUMN_Id);
        assertThat(testAcctRuleConditions.getOperator()).isEqualTo(UPDATED_OPERATOR);
        assertThat(testAcctRuleConditions.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testAcctRuleConditions.getCloseBracket()).isEqualTo(UPDATED_CLOSE_BRACKET);
        assertThat(testAcctRuleConditions.getLogicalOperator()).isEqualTo(UPDATED_LOGICAL_OPERATOR);
        assertThat(testAcctRuleConditions.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAcctRuleConditions.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testAcctRuleConditions.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testAcctRuleConditions.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingAcctRuleConditions() throws Exception {
        int databaseSizeBeforeUpdate = acctRuleConditionsRepository.findAll().size();

        // Create the AcctRuleConditions

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAcctRuleConditionsMockMvc.perform(put("/api/acct-rule-conditions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(acctRuleConditions)))
            .andExpect(status().isCreated());

        // Validate the AcctRuleConditions in the database
        List<AcctRuleConditions> acctRuleConditionsList = acctRuleConditionsRepository.findAll();
        assertThat(acctRuleConditionsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAcctRuleConditions() throws Exception {
        // Initialize the database
        acctRuleConditionsRepository.saveAndFlush(acctRuleConditions);
        int databaseSizeBeforeDelete = acctRuleConditionsRepository.findAll().size();

        // Get the acctRuleConditions
        restAcctRuleConditionsMockMvc.perform(delete("/api/acct-rule-conditions/{id}", acctRuleConditions.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AcctRuleConditions> acctRuleConditionsList = acctRuleConditionsRepository.findAll();
        assertThat(acctRuleConditionsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AcctRuleConditions.class);
    }
}
