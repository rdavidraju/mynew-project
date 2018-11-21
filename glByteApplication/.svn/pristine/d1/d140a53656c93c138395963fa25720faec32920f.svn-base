package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.AppRuleConditions;
import com.nspl.app.repository.AppRuleConditionsRepository;
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
 * Test class for the AppRuleConditionsResource REST controller.
 *
 * @see AppRuleConditionsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class AppRuleConditionsResourceIntTest {

    private static final Long DEFAULT_RULE_ID = 1L;
    private static final Long UPDATED_RULE_ID = 2L;

    private static final String DEFAULT_OPEN_BRACKET = "AAAAAAAAAA";
    private static final String UPDATED_OPEN_BRACKET = "BBBBBBBBBB";

    private static final String DEFAULT_OPERATOR = "AAAAAAAAAA";
    private static final String UPDATED_OPERATOR = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_CLOSE_BRACKET = "AAAAAAAAAA";
    private static final String UPDATED_CLOSE_BRACKET = "BBBBBBBBBB";

    private static final String DEFAULT_LOGICAL_OPERATOR = "AAAAAAAAAA";
    private static final String UPDATED_LOGICAL_OPERATOR = "BBBBBBBBBB";

    private static final Long DEFAULT_COLUMN_ID = 1L;
    private static final Long UPDATED_COLUMN_ID = 2L;

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private AppRuleConditionsRepository appRuleConditionsRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAppRuleConditionsMockMvc;

    private AppRuleConditions appRuleConditions;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AppRuleConditionsResource appRuleConditionsResource = new AppRuleConditionsResource(appRuleConditionsRepository);
        this.restAppRuleConditionsMockMvc = MockMvcBuilders.standaloneSetup(appRuleConditionsResource)
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
    public static AppRuleConditions createEntity(EntityManager em) {
        AppRuleConditions appRuleConditions = new AppRuleConditions()
            .ruleId(DEFAULT_RULE_ID)
            .openBracket(DEFAULT_OPEN_BRACKET)
            .operator(DEFAULT_OPERATOR)
            .value(DEFAULT_VALUE)
            .closeBracket(DEFAULT_CLOSE_BRACKET)
            .logicalOperator(DEFAULT_LOGICAL_OPERATOR)
            .columnId(DEFAULT_COLUMN_ID)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE);
        return appRuleConditions;
    }

    @Before
    public void initTest() {
        appRuleConditions = createEntity(em);
    }

    @Test
    @Transactional
    public void createAppRuleConditions() throws Exception {
        int databaseSizeBeforeCreate = appRuleConditionsRepository.findAll().size();

        // Create the AppRuleConditions
        restAppRuleConditionsMockMvc.perform(post("/api/app-rule-conditions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appRuleConditions)))
            .andExpect(status().isCreated());

        // Validate the AppRuleConditions in the database
        List<AppRuleConditions> appRuleConditionsList = appRuleConditionsRepository.findAll();
        assertThat(appRuleConditionsList).hasSize(databaseSizeBeforeCreate + 1);
        AppRuleConditions testAppRuleConditions = appRuleConditionsList.get(appRuleConditionsList.size() - 1);
        assertThat(testAppRuleConditions.getRuleId()).isEqualTo(DEFAULT_RULE_ID);
        assertThat(testAppRuleConditions.getOpenBracket()).isEqualTo(DEFAULT_OPEN_BRACKET);
        assertThat(testAppRuleConditions.getOperator()).isEqualTo(DEFAULT_OPERATOR);
        assertThat(testAppRuleConditions.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testAppRuleConditions.getCloseBracket()).isEqualTo(DEFAULT_CLOSE_BRACKET);
        assertThat(testAppRuleConditions.getLogicalOperator()).isEqualTo(DEFAULT_LOGICAL_OPERATOR);
        assertThat(testAppRuleConditions.getColumnId()).isEqualTo(DEFAULT_COLUMN_ID);
        assertThat(testAppRuleConditions.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testAppRuleConditions.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testAppRuleConditions.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testAppRuleConditions.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void createAppRuleConditionsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = appRuleConditionsRepository.findAll().size();

        // Create the AppRuleConditions with an existing ID
        appRuleConditions.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppRuleConditionsMockMvc.perform(post("/api/app-rule-conditions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appRuleConditions)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<AppRuleConditions> appRuleConditionsList = appRuleConditionsRepository.findAll();
        assertThat(appRuleConditionsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAppRuleConditions() throws Exception {
        // Initialize the database
        appRuleConditionsRepository.saveAndFlush(appRuleConditions);

        // Get all the appRuleConditionsList
        restAppRuleConditionsMockMvc.perform(get("/api/app-rule-conditions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appRuleConditions.getId().intValue())))
            .andExpect(jsonPath("$.[*].ruleId").value(hasItem(DEFAULT_RULE_ID.intValue())))
            .andExpect(jsonPath("$.[*].openBracket").value(hasItem(DEFAULT_OPEN_BRACKET.toString())))
            .andExpect(jsonPath("$.[*].operator").value(hasItem(DEFAULT_OPERATOR.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].closeBracket").value(hasItem(DEFAULT_CLOSE_BRACKET.toString())))
            .andExpect(jsonPath("$.[*].logicalOperator").value(hasItem(DEFAULT_LOGICAL_OPERATOR.toString())))
            .andExpect(jsonPath("$.[*].columnId").value(hasItem(DEFAULT_COLUMN_ID.intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void getAppRuleConditions() throws Exception {
        // Initialize the database
        appRuleConditionsRepository.saveAndFlush(appRuleConditions);

        // Get the appRuleConditions
        restAppRuleConditionsMockMvc.perform(get("/api/app-rule-conditions/{id}", appRuleConditions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(appRuleConditions.getId().intValue()))
            .andExpect(jsonPath("$.ruleId").value(DEFAULT_RULE_ID.intValue()))
            .andExpect(jsonPath("$.openBracket").value(DEFAULT_OPEN_BRACKET.toString()))
            .andExpect(jsonPath("$.operator").value(DEFAULT_OPERATOR.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()))
            .andExpect(jsonPath("$.closeBracket").value(DEFAULT_CLOSE_BRACKET.toString()))
            .andExpect(jsonPath("$.logicalOperator").value(DEFAULT_LOGICAL_OPERATOR.toString()))
            .andExpect(jsonPath("$.columnId").value(DEFAULT_COLUMN_ID.intValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingAppRuleConditions() throws Exception {
        // Get the appRuleConditions
        restAppRuleConditionsMockMvc.perform(get("/api/app-rule-conditions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAppRuleConditions() throws Exception {
        // Initialize the database
        appRuleConditionsRepository.saveAndFlush(appRuleConditions);
        int databaseSizeBeforeUpdate = appRuleConditionsRepository.findAll().size();

        // Update the appRuleConditions
        AppRuleConditions updatedAppRuleConditions = appRuleConditionsRepository.findOne(appRuleConditions.getId());
        updatedAppRuleConditions
            .ruleId(UPDATED_RULE_ID)
            .openBracket(UPDATED_OPEN_BRACKET)
            .operator(UPDATED_OPERATOR)
            .value(UPDATED_VALUE)
            .closeBracket(UPDATED_CLOSE_BRACKET)
            .logicalOperator(UPDATED_LOGICAL_OPERATOR)
            .columnId(UPDATED_COLUMN_ID)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE);

        restAppRuleConditionsMockMvc.perform(put("/api/app-rule-conditions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAppRuleConditions)))
            .andExpect(status().isOk());

        // Validate the AppRuleConditions in the database
        List<AppRuleConditions> appRuleConditionsList = appRuleConditionsRepository.findAll();
        assertThat(appRuleConditionsList).hasSize(databaseSizeBeforeUpdate);
        AppRuleConditions testAppRuleConditions = appRuleConditionsList.get(appRuleConditionsList.size() - 1);
        assertThat(testAppRuleConditions.getRuleId()).isEqualTo(UPDATED_RULE_ID);
        assertThat(testAppRuleConditions.getOpenBracket()).isEqualTo(UPDATED_OPEN_BRACKET);
        assertThat(testAppRuleConditions.getOperator()).isEqualTo(UPDATED_OPERATOR);
        assertThat(testAppRuleConditions.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testAppRuleConditions.getCloseBracket()).isEqualTo(UPDATED_CLOSE_BRACKET);
        assertThat(testAppRuleConditions.getLogicalOperator()).isEqualTo(UPDATED_LOGICAL_OPERATOR);
        assertThat(testAppRuleConditions.getColumnId()).isEqualTo(UPDATED_COLUMN_ID);
        assertThat(testAppRuleConditions.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAppRuleConditions.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testAppRuleConditions.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testAppRuleConditions.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingAppRuleConditions() throws Exception {
        int databaseSizeBeforeUpdate = appRuleConditionsRepository.findAll().size();

        // Create the AppRuleConditions

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAppRuleConditionsMockMvc.perform(put("/api/app-rule-conditions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appRuleConditions)))
            .andExpect(status().isCreated());

        // Validate the AppRuleConditions in the database
        List<AppRuleConditions> appRuleConditionsList = appRuleConditionsRepository.findAll();
        assertThat(appRuleConditionsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAppRuleConditions() throws Exception {
        // Initialize the database
        appRuleConditionsRepository.saveAndFlush(appRuleConditions);
        int databaseSizeBeforeDelete = appRuleConditionsRepository.findAll().size();

        // Get the appRuleConditions
        restAppRuleConditionsMockMvc.perform(delete("/api/app-rule-conditions/{id}", appRuleConditions.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AppRuleConditions> appRuleConditionsList = appRuleConditionsRepository.findAll();
        assertThat(appRuleConditionsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppRuleConditions.class);
    }
}
