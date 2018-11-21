package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.RuleGroupDetails;
import com.nspl.app.repository.RuleGroupDetailsRepository;
import com.nspl.app.repository.search.RuleGroupDetailsSearchRepository;
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
 * Test class for the RuleGroupDetailsResource REST controller.
 *
 * @see RuleGroupDetailsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class RuleGroupDetailsResourceIntTest {

    private static final Long DEFAULT_RULE_GROUP_ID = 1L;
    private static final Long UPDATED_RULE_GROUP_ID = 2L;

    private static final Long DEFAULT_RULE_ID = 1L;
    private static final Long UPDATED_RULE_ID = 2L;

    private static final Integer DEFAULT_PRIORITY = 1;
    private static final Integer UPDATED_PRIORITY = 2;

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private RuleGroupDetailsRepository ruleGroupDetailsRepository;

    @Autowired
    private RuleGroupDetailsSearchRepository ruleGroupDetailsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRuleGroupDetailsMockMvc;

    private RuleGroupDetails ruleGroupDetails;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RuleGroupDetailsResource ruleGroupDetailsResource = new RuleGroupDetailsResource(ruleGroupDetailsRepository, ruleGroupDetailsSearchRepository);
        this.restRuleGroupDetailsMockMvc = MockMvcBuilders.standaloneSetup(ruleGroupDetailsResource)
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
    public static RuleGroupDetails createEntity(EntityManager em) {
        RuleGroupDetails ruleGroupDetails = new RuleGroupDetails()
            .ruleGroupId(DEFAULT_RULE_GROUP_ID)
            .ruleId(DEFAULT_RULE_ID)
            .priority(DEFAULT_PRIORITY)
            .createdBy(DEFAULT_CREATED_BY)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .creationDate(DEFAULT_CREATION_DATE)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE);
        return ruleGroupDetails;
    }

    @Before
    public void initTest() {
        ruleGroupDetailsSearchRepository.deleteAll();
        ruleGroupDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createRuleGroupDetails() throws Exception {
        int databaseSizeBeforeCreate = ruleGroupDetailsRepository.findAll().size();

        // Create the RuleGroupDetails
        restRuleGroupDetailsMockMvc.perform(post("/api/rule-group-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ruleGroupDetails)))
            .andExpect(status().isCreated());

        // Validate the RuleGroupDetails in the database
        List<RuleGroupDetails> ruleGroupDetailsList = ruleGroupDetailsRepository.findAll();
        assertThat(ruleGroupDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        RuleGroupDetails testRuleGroupDetails = ruleGroupDetailsList.get(ruleGroupDetailsList.size() - 1);
        assertThat(testRuleGroupDetails.getRuleGroupId()).isEqualTo(DEFAULT_RULE_GROUP_ID);
        assertThat(testRuleGroupDetails.getRuleId()).isEqualTo(DEFAULT_RULE_ID);
        assertThat(testRuleGroupDetails.getPriority()).isEqualTo(DEFAULT_PRIORITY);
        assertThat(testRuleGroupDetails.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testRuleGroupDetails.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testRuleGroupDetails.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testRuleGroupDetails.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);

        // Validate the RuleGroupDetails in Elasticsearch
        RuleGroupDetails ruleGroupDetailsEs = ruleGroupDetailsSearchRepository.findOne(testRuleGroupDetails.getId());
        assertThat(ruleGroupDetailsEs).isEqualToComparingFieldByField(testRuleGroupDetails);
    }

    @Test
    @Transactional
    public void createRuleGroupDetailsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ruleGroupDetailsRepository.findAll().size();

        // Create the RuleGroupDetails with an existing ID
        ruleGroupDetails.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRuleGroupDetailsMockMvc.perform(post("/api/rule-group-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ruleGroupDetails)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<RuleGroupDetails> ruleGroupDetailsList = ruleGroupDetailsRepository.findAll();
        assertThat(ruleGroupDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRuleGroupDetails() throws Exception {
        // Initialize the database
        ruleGroupDetailsRepository.saveAndFlush(ruleGroupDetails);

        // Get all the ruleGroupDetailsList
        restRuleGroupDetailsMockMvc.perform(get("/api/rule-group-details?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ruleGroupDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].ruleGroupId").value(hasItem(DEFAULT_RULE_GROUP_ID.intValue())))
            .andExpect(jsonPath("$.[*].ruleId").value(hasItem(DEFAULT_RULE_ID.intValue())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void getRuleGroupDetails() throws Exception {
        // Initialize the database
        ruleGroupDetailsRepository.saveAndFlush(ruleGroupDetails);

        // Get the ruleGroupDetails
        restRuleGroupDetailsMockMvc.perform(get("/api/rule-group-details/{id}", ruleGroupDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ruleGroupDetails.getId().intValue()))
            .andExpect(jsonPath("$.ruleGroupId").value(DEFAULT_RULE_GROUP_ID.intValue()))
            .andExpect(jsonPath("$.ruleId").value(DEFAULT_RULE_ID.intValue()))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.creationDate").value(sameInstant(DEFAULT_CREATION_DATE)))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingRuleGroupDetails() throws Exception {
        // Get the ruleGroupDetails
        restRuleGroupDetailsMockMvc.perform(get("/api/rule-group-details/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRuleGroupDetails() throws Exception {
        // Initialize the database
        ruleGroupDetailsRepository.saveAndFlush(ruleGroupDetails);
        ruleGroupDetailsSearchRepository.save(ruleGroupDetails);
        int databaseSizeBeforeUpdate = ruleGroupDetailsRepository.findAll().size();

        // Update the ruleGroupDetails
        RuleGroupDetails updatedRuleGroupDetails = ruleGroupDetailsRepository.findOne(ruleGroupDetails.getId());
        updatedRuleGroupDetails
            .ruleGroupId(UPDATED_RULE_GROUP_ID)
            .ruleId(UPDATED_RULE_ID)
            .priority(UPDATED_PRIORITY)
            .createdBy(UPDATED_CREATED_BY)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE);

        restRuleGroupDetailsMockMvc.perform(put("/api/rule-group-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRuleGroupDetails)))
            .andExpect(status().isOk());

        // Validate the RuleGroupDetails in the database
        List<RuleGroupDetails> ruleGroupDetailsList = ruleGroupDetailsRepository.findAll();
        assertThat(ruleGroupDetailsList).hasSize(databaseSizeBeforeUpdate);
        RuleGroupDetails testRuleGroupDetails = ruleGroupDetailsList.get(ruleGroupDetailsList.size() - 1);
        assertThat(testRuleGroupDetails.getRuleGroupId()).isEqualTo(UPDATED_RULE_GROUP_ID);
        assertThat(testRuleGroupDetails.getRuleId()).isEqualTo(UPDATED_RULE_ID);
        assertThat(testRuleGroupDetails.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testRuleGroupDetails.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testRuleGroupDetails.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testRuleGroupDetails.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testRuleGroupDetails.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);

        // Validate the RuleGroupDetails in Elasticsearch
        RuleGroupDetails ruleGroupDetailsEs = ruleGroupDetailsSearchRepository.findOne(testRuleGroupDetails.getId());
        assertThat(ruleGroupDetailsEs).isEqualToComparingFieldByField(testRuleGroupDetails);
    }

    @Test
    @Transactional
    public void updateNonExistingRuleGroupDetails() throws Exception {
        int databaseSizeBeforeUpdate = ruleGroupDetailsRepository.findAll().size();

        // Create the RuleGroupDetails

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRuleGroupDetailsMockMvc.perform(put("/api/rule-group-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ruleGroupDetails)))
            .andExpect(status().isCreated());

        // Validate the RuleGroupDetails in the database
        List<RuleGroupDetails> ruleGroupDetailsList = ruleGroupDetailsRepository.findAll();
        assertThat(ruleGroupDetailsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRuleGroupDetails() throws Exception {
        // Initialize the database
        ruleGroupDetailsRepository.saveAndFlush(ruleGroupDetails);
        ruleGroupDetailsSearchRepository.save(ruleGroupDetails);
        int databaseSizeBeforeDelete = ruleGroupDetailsRepository.findAll().size();

        // Get the ruleGroupDetails
        restRuleGroupDetailsMockMvc.perform(delete("/api/rule-group-details/{id}", ruleGroupDetails.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean ruleGroupDetailsExistsInEs = ruleGroupDetailsSearchRepository.exists(ruleGroupDetails.getId());
        assertThat(ruleGroupDetailsExistsInEs).isFalse();

        // Validate the database is empty
        List<RuleGroupDetails> ruleGroupDetailsList = ruleGroupDetailsRepository.findAll();
        assertThat(ruleGroupDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchRuleGroupDetails() throws Exception {
        // Initialize the database
        ruleGroupDetailsRepository.saveAndFlush(ruleGroupDetails);
        ruleGroupDetailsSearchRepository.save(ruleGroupDetails);

        // Search the ruleGroupDetails
        restRuleGroupDetailsMockMvc.perform(get("/api/_search/rule-group-details?query=id:" + ruleGroupDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ruleGroupDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].ruleGroupId").value(hasItem(DEFAULT_RULE_GROUP_ID.intValue())))
            .andExpect(jsonPath("$.[*].ruleId").value(hasItem(DEFAULT_RULE_ID.intValue())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RuleGroupDetails.class);
    }
}
