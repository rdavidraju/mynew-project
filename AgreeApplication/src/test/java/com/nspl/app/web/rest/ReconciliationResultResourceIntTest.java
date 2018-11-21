package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.ReconciliationResult;
import com.nspl.app.repository.ReconciliationResultRepository;
import com.nspl.app.service.ReconciliationResultService;
import com.nspl.app.repository.search.ReconciliationResultSearchRepository;
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
 * Test class for the ReconciliationResultResource REST controller.
 *
 * @see ReconciliationResultResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class ReconciliationResultResourceIntTest {

    private static final Long DEFAULT_ORIGINAL_ROW_ID = 1L;
    private static final Long UPDATED_ORIGINAL_ROW_ID = 2L;

    private static final Long DEFAULT_ORIGINAL_VIEW_ID = 1L;
    private static final Long UPDATED_ORIGINAL_VIEW_ID = 2L;

    private static final String DEFAULT_ORIGINAL_VIEW = "AAAAAAAAAA";
    private static final String UPDATED_ORIGINAL_VIEW = "BBBBBBBBBB";

    private static final Long DEFAULT_TARGET_ROW_ID = 1L;
    private static final Long UPDATED_TARGET_ROW_ID = 2L;

    private static final Long DEFAULT_TARGET_VIEW_ID = 1L;
    private static final Long UPDATED_TARGET_VIEW_ID = 2L;

    private static final String DEFAULT_TARGET_VIEW = "AAAAAAAAAA";
    private static final String UPDATED_TARGET_VIEW = "BBBBBBBBBB";

    private static final String DEFAULT_RECON_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_RECON_REFERENCE = "BBBBBBBBBB";

    private static final String DEFAULT_RECONCILIATION_RULE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_RECONCILIATION_RULE_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_RECONCILIATION_RULE_ID = 1L;
    private static final Long UPDATED_RECONCILIATION_RULE_ID = 2L;

    private static final Long DEFAULT_RECONCILIATION_USER_ID = 1L;
    private static final Long UPDATED_RECONCILIATION_USER_ID = 2L;

    private static final String DEFAULT_RECON_JOB_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_RECON_JOB_REFERENCE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_RECONCILED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_RECONCILED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_TENANT_ID = 1L;
    private static final Long UPDATED_TENANT_ID = 2L;

    @Autowired
    private ReconciliationResultRepository reconciliationResultRepository;

    @Autowired
    private ReconciliationResultService reconciliationResultService;

    @Autowired
    private ReconciliationResultSearchRepository reconciliationResultSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restReconciliationResultMockMvc;

    private ReconciliationResult reconciliationResult;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReconciliationResultResource reconciliationResultResource = new ReconciliationResultResource(reconciliationResultService);
        this.restReconciliationResultMockMvc = MockMvcBuilders.standaloneSetup(reconciliationResultResource)
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
    public static ReconciliationResult createEntity(EntityManager em) {
        ReconciliationResult reconciliationResult = new ReconciliationResult()
            .originalRowId(DEFAULT_ORIGINAL_ROW_ID)
            .originalViewId(DEFAULT_ORIGINAL_VIEW_ID)
            .originalView(DEFAULT_ORIGINAL_VIEW)
            .targetRowId(DEFAULT_TARGET_ROW_ID)
            .targetViewId(DEFAULT_TARGET_VIEW_ID)
            .targetView(DEFAULT_TARGET_VIEW)
            .reconReference(DEFAULT_RECON_REFERENCE)
            .reconciliationRuleName(DEFAULT_RECONCILIATION_RULE_NAME)
            .reconciliationRuleId(DEFAULT_RECONCILIATION_RULE_ID)
            .reconciliationUserId(DEFAULT_RECONCILIATION_USER_ID)
            .reconJobReference(DEFAULT_RECON_JOB_REFERENCE)
            .reconciledDate(DEFAULT_RECONCILED_DATE)
            .tenantId(DEFAULT_TENANT_ID);
        return reconciliationResult;
    }

    @Before
    public void initTest() {
        reconciliationResultSearchRepository.deleteAll();
        reconciliationResult = createEntity(em);
    }

    @Test
    @Transactional
    public void createReconciliationResult() throws Exception {
        int databaseSizeBeforeCreate = reconciliationResultRepository.findAll().size();

        // Create the ReconciliationResult
        restReconciliationResultMockMvc.perform(post("/api/reconciliation-results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reconciliationResult)))
            .andExpect(status().isCreated());

        // Validate the ReconciliationResult in the database
        List<ReconciliationResult> reconciliationResultList = reconciliationResultRepository.findAll();
        assertThat(reconciliationResultList).hasSize(databaseSizeBeforeCreate + 1);
        ReconciliationResult testReconciliationResult = reconciliationResultList.get(reconciliationResultList.size() - 1);
        assertThat(testReconciliationResult.getOriginalRowId()).isEqualTo(DEFAULT_ORIGINAL_ROW_ID);
        assertThat(testReconciliationResult.getOriginalViewId()).isEqualTo(DEFAULT_ORIGINAL_VIEW_ID);
        assertThat(testReconciliationResult.getOriginalView()).isEqualTo(DEFAULT_ORIGINAL_VIEW);
        assertThat(testReconciliationResult.getTargetRowId()).isEqualTo(DEFAULT_TARGET_ROW_ID);
        assertThat(testReconciliationResult.getTargetViewId()).isEqualTo(DEFAULT_TARGET_VIEW_ID);
        assertThat(testReconciliationResult.getTargetView()).isEqualTo(DEFAULT_TARGET_VIEW);
        assertThat(testReconciliationResult.getReconReference()).isEqualTo(DEFAULT_RECON_REFERENCE);
        assertThat(testReconciliationResult.getReconciliationRuleName()).isEqualTo(DEFAULT_RECONCILIATION_RULE_NAME);
        assertThat(testReconciliationResult.getReconciliationRuleId()).isEqualTo(DEFAULT_RECONCILIATION_RULE_ID);
        assertThat(testReconciliationResult.getReconciliationUserId()).isEqualTo(DEFAULT_RECONCILIATION_USER_ID);
        assertThat(testReconciliationResult.getReconJobReference()).isEqualTo(DEFAULT_RECON_JOB_REFERENCE);
        assertThat(testReconciliationResult.getReconciledDate()).isEqualTo(DEFAULT_RECONCILED_DATE);
        assertThat(testReconciliationResult.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);

        // Validate the ReconciliationResult in Elasticsearch
        ReconciliationResult reconciliationResultEs = reconciliationResultSearchRepository.findOne(testReconciliationResult.getId());
        assertThat(reconciliationResultEs).isEqualToComparingFieldByField(testReconciliationResult);
    }

    @Test
    @Transactional
    public void createReconciliationResultWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reconciliationResultRepository.findAll().size();

        // Create the ReconciliationResult with an existing ID
        reconciliationResult.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReconciliationResultMockMvc.perform(post("/api/reconciliation-results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reconciliationResult)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ReconciliationResult> reconciliationResultList = reconciliationResultRepository.findAll();
        assertThat(reconciliationResultList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllReconciliationResults() throws Exception {
        // Initialize the database
        reconciliationResultRepository.saveAndFlush(reconciliationResult);

        // Get all the reconciliationResultList
        restReconciliationResultMockMvc.perform(get("/api/reconciliation-results?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reconciliationResult.getId().intValue())))
            .andExpect(jsonPath("$.[*].originalRowId").value(hasItem(DEFAULT_ORIGINAL_ROW_ID.intValue())))
            .andExpect(jsonPath("$.[*].originalViewId").value(hasItem(DEFAULT_ORIGINAL_VIEW_ID.intValue())))
            .andExpect(jsonPath("$.[*].originalView").value(hasItem(DEFAULT_ORIGINAL_VIEW.toString())))
            .andExpect(jsonPath("$.[*].targetRowId").value(hasItem(DEFAULT_TARGET_ROW_ID.intValue())))
            .andExpect(jsonPath("$.[*].targetViewId").value(hasItem(DEFAULT_TARGET_VIEW_ID.intValue())))
            .andExpect(jsonPath("$.[*].targetView").value(hasItem(DEFAULT_TARGET_VIEW.toString())))
            .andExpect(jsonPath("$.[*].reconReference").value(hasItem(DEFAULT_RECON_REFERENCE.toString())))
            .andExpect(jsonPath("$.[*].reconciliationRuleName").value(hasItem(DEFAULT_RECONCILIATION_RULE_NAME.toString())))
            .andExpect(jsonPath("$.[*].reconciliationRuleId").value(hasItem(DEFAULT_RECONCILIATION_RULE_ID.intValue())))
            .andExpect(jsonPath("$.[*].reconciliationUserId").value(hasItem(DEFAULT_RECONCILIATION_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].reconJobReference").value(hasItem(DEFAULT_RECON_JOB_REFERENCE.toString())))
            .andExpect(jsonPath("$.[*].reconciledDate").value(hasItem(sameInstant(DEFAULT_RECONCILED_DATE))))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())));
    }

    @Test
    @Transactional
    public void getReconciliationResult() throws Exception {
        // Initialize the database
        reconciliationResultRepository.saveAndFlush(reconciliationResult);

        // Get the reconciliationResult
        restReconciliationResultMockMvc.perform(get("/api/reconciliation-results/{id}", reconciliationResult.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(reconciliationResult.getId().intValue()))
            .andExpect(jsonPath("$.originalRowId").value(DEFAULT_ORIGINAL_ROW_ID.intValue()))
            .andExpect(jsonPath("$.originalViewId").value(DEFAULT_ORIGINAL_VIEW_ID.intValue()))
            .andExpect(jsonPath("$.originalView").value(DEFAULT_ORIGINAL_VIEW.toString()))
            .andExpect(jsonPath("$.targetRowId").value(DEFAULT_TARGET_ROW_ID.intValue()))
            .andExpect(jsonPath("$.targetViewId").value(DEFAULT_TARGET_VIEW_ID.intValue()))
            .andExpect(jsonPath("$.targetView").value(DEFAULT_TARGET_VIEW.toString()))
            .andExpect(jsonPath("$.reconReference").value(DEFAULT_RECON_REFERENCE.toString()))
            .andExpect(jsonPath("$.reconciliationRuleName").value(DEFAULT_RECONCILIATION_RULE_NAME.toString()))
            .andExpect(jsonPath("$.reconciliationRuleId").value(DEFAULT_RECONCILIATION_RULE_ID.intValue()))
            .andExpect(jsonPath("$.reconciliationUserId").value(DEFAULT_RECONCILIATION_USER_ID.intValue()))
            .andExpect(jsonPath("$.reconJobReference").value(DEFAULT_RECON_JOB_REFERENCE.toString()))
            .andExpect(jsonPath("$.reconciledDate").value(sameInstant(DEFAULT_RECONCILED_DATE)))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingReconciliationResult() throws Exception {
        // Get the reconciliationResult
        restReconciliationResultMockMvc.perform(get("/api/reconciliation-results/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReconciliationResult() throws Exception {
        // Initialize the database
        reconciliationResultService.save(reconciliationResult);

        int databaseSizeBeforeUpdate = reconciliationResultRepository.findAll().size();

        // Update the reconciliationResult
        ReconciliationResult updatedReconciliationResult = reconciliationResultRepository.findOne(reconciliationResult.getId());
        updatedReconciliationResult
            .originalRowId(UPDATED_ORIGINAL_ROW_ID)
            .originalViewId(UPDATED_ORIGINAL_VIEW_ID)
            .originalView(UPDATED_ORIGINAL_VIEW)
            .targetRowId(UPDATED_TARGET_ROW_ID)
            .targetViewId(UPDATED_TARGET_VIEW_ID)
            .targetView(UPDATED_TARGET_VIEW)
            .reconReference(UPDATED_RECON_REFERENCE)
            .reconciliationRuleName(UPDATED_RECONCILIATION_RULE_NAME)
            .reconciliationRuleId(UPDATED_RECONCILIATION_RULE_ID)
            .reconciliationUserId(UPDATED_RECONCILIATION_USER_ID)
            .reconJobReference(UPDATED_RECON_JOB_REFERENCE)
            .reconciledDate(UPDATED_RECONCILED_DATE)
            .tenantId(UPDATED_TENANT_ID);

        restReconciliationResultMockMvc.perform(put("/api/reconciliation-results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedReconciliationResult)))
            .andExpect(status().isOk());

        // Validate the ReconciliationResult in the database
        List<ReconciliationResult> reconciliationResultList = reconciliationResultRepository.findAll();
        assertThat(reconciliationResultList).hasSize(databaseSizeBeforeUpdate);
        ReconciliationResult testReconciliationResult = reconciliationResultList.get(reconciliationResultList.size() - 1);
        assertThat(testReconciliationResult.getOriginalRowId()).isEqualTo(UPDATED_ORIGINAL_ROW_ID);
        assertThat(testReconciliationResult.getOriginalViewId()).isEqualTo(UPDATED_ORIGINAL_VIEW_ID);
        assertThat(testReconciliationResult.getOriginalView()).isEqualTo(UPDATED_ORIGINAL_VIEW);
        assertThat(testReconciliationResult.getTargetRowId()).isEqualTo(UPDATED_TARGET_ROW_ID);
        assertThat(testReconciliationResult.getTargetViewId()).isEqualTo(UPDATED_TARGET_VIEW_ID);
        assertThat(testReconciliationResult.getTargetView()).isEqualTo(UPDATED_TARGET_VIEW);
        assertThat(testReconciliationResult.getReconReference()).isEqualTo(UPDATED_RECON_REFERENCE);
        assertThat(testReconciliationResult.getReconciliationRuleName()).isEqualTo(UPDATED_RECONCILIATION_RULE_NAME);
        assertThat(testReconciliationResult.getReconciliationRuleId()).isEqualTo(UPDATED_RECONCILIATION_RULE_ID);
        assertThat(testReconciliationResult.getReconciliationUserId()).isEqualTo(UPDATED_RECONCILIATION_USER_ID);
        assertThat(testReconciliationResult.getReconJobReference()).isEqualTo(UPDATED_RECON_JOB_REFERENCE);
        assertThat(testReconciliationResult.getReconciledDate()).isEqualTo(UPDATED_RECONCILED_DATE);
        assertThat(testReconciliationResult.getTenantId()).isEqualTo(UPDATED_TENANT_ID);

        // Validate the ReconciliationResult in Elasticsearch
        ReconciliationResult reconciliationResultEs = reconciliationResultSearchRepository.findOne(testReconciliationResult.getId());
        assertThat(reconciliationResultEs).isEqualToComparingFieldByField(testReconciliationResult);
    }

    @Test
    @Transactional
    public void updateNonExistingReconciliationResult() throws Exception {
        int databaseSizeBeforeUpdate = reconciliationResultRepository.findAll().size();

        // Create the ReconciliationResult

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restReconciliationResultMockMvc.perform(put("/api/reconciliation-results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reconciliationResult)))
            .andExpect(status().isCreated());

        // Validate the ReconciliationResult in the database
        List<ReconciliationResult> reconciliationResultList = reconciliationResultRepository.findAll();
        assertThat(reconciliationResultList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteReconciliationResult() throws Exception {
        // Initialize the database
        reconciliationResultService.save(reconciliationResult);

        int databaseSizeBeforeDelete = reconciliationResultRepository.findAll().size();

        // Get the reconciliationResult
        restReconciliationResultMockMvc.perform(delete("/api/reconciliation-results/{id}", reconciliationResult.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean reconciliationResultExistsInEs = reconciliationResultSearchRepository.exists(reconciliationResult.getId());
        assertThat(reconciliationResultExistsInEs).isFalse();

        // Validate the database is empty
        List<ReconciliationResult> reconciliationResultList = reconciliationResultRepository.findAll();
        assertThat(reconciliationResultList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchReconciliationResult() throws Exception {
        // Initialize the database
        reconciliationResultService.save(reconciliationResult);

        // Search the reconciliationResult
        restReconciliationResultMockMvc.perform(get("/api/_search/reconciliation-results?query=id:" + reconciliationResult.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reconciliationResult.getId().intValue())))
            .andExpect(jsonPath("$.[*].originalRowId").value(hasItem(DEFAULT_ORIGINAL_ROW_ID.intValue())))
            .andExpect(jsonPath("$.[*].originalViewId").value(hasItem(DEFAULT_ORIGINAL_VIEW_ID.intValue())))
            .andExpect(jsonPath("$.[*].originalView").value(hasItem(DEFAULT_ORIGINAL_VIEW.toString())))
            .andExpect(jsonPath("$.[*].targetRowId").value(hasItem(DEFAULT_TARGET_ROW_ID.intValue())))
            .andExpect(jsonPath("$.[*].targetViewId").value(hasItem(DEFAULT_TARGET_VIEW_ID.intValue())))
            .andExpect(jsonPath("$.[*].targetView").value(hasItem(DEFAULT_TARGET_VIEW.toString())))
            .andExpect(jsonPath("$.[*].reconReference").value(hasItem(DEFAULT_RECON_REFERENCE.toString())))
            .andExpect(jsonPath("$.[*].reconciliationRuleName").value(hasItem(DEFAULT_RECONCILIATION_RULE_NAME.toString())))
            .andExpect(jsonPath("$.[*].reconciliationRuleId").value(hasItem(DEFAULT_RECONCILIATION_RULE_ID.intValue())))
            .andExpect(jsonPath("$.[*].reconciliationUserId").value(hasItem(DEFAULT_RECONCILIATION_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].reconJobReference").value(hasItem(DEFAULT_RECON_JOB_REFERENCE.toString())))
            .andExpect(jsonPath("$.[*].reconciledDate").value(hasItem(sameInstant(DEFAULT_RECONCILED_DATE))))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReconciliationResult.class);
    }
}
