package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.ReconciliationDuplicateResult;
import com.nspl.app.repository.ReconciliationDuplicateResultRepository;
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
 * Test class for the ReconciliationDuplicateResultResource REST controller.
 *
 * @see ReconciliationDuplicateResultResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class ReconciliationDuplicateResultResourceIntTest {

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

    private static final Long DEFAULT_RECONCILIATION_RULE_GROUP_ID = 1L;
    private static final Long UPDATED_RECONCILIATION_RULE_GROUP_ID = 2L;

    private static final Long DEFAULT_RECONCILIATION_RULE_ID = 1L;
    private static final Long UPDATED_RECONCILIATION_RULE_ID = 2L;

    private static final Long DEFAULT_RECONCILIATION_USER_ID = 1L;
    private static final Long UPDATED_RECONCILIATION_USER_ID = 2L;

    private static final String DEFAULT_RECON_JOB_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_RECON_JOB_REFERENCE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_RECONCILED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_RECONCILED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_RECON_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_RECON_STATUS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CURRENT_RECORD_FLAG = false;
    private static final Boolean UPDATED_CURRENT_RECORD_FLAG = true;

    private static final Long DEFAULT_TENANT_ID = 1L;
    private static final Long UPDATED_TENANT_ID = 2L;

/*    private static final String DEFAULT_FINAL_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_FINAL_STATUS = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_FINAL_ACTION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FINAL_ACTION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);*/

    @Autowired
    private ReconciliationDuplicateResultRepository reconciliationDuplicateResultRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restReconciliationDuplicateResultMockMvc;

    private ReconciliationDuplicateResult reconciliationDuplicateResult;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReconciliationDuplicateResultResource reconciliationDuplicateResultResource = new ReconciliationDuplicateResultResource(reconciliationDuplicateResultRepository);
        this.restReconciliationDuplicateResultMockMvc = MockMvcBuilders.standaloneSetup(reconciliationDuplicateResultResource)
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
    public static ReconciliationDuplicateResult createEntity(EntityManager em) {
        ReconciliationDuplicateResult reconciliationDuplicateResult = new ReconciliationDuplicateResult()
            .originalRowId(DEFAULT_ORIGINAL_ROW_ID)
            .originalViewId(DEFAULT_ORIGINAL_VIEW_ID)
            .originalView(DEFAULT_ORIGINAL_VIEW)
            .targetRowId(DEFAULT_TARGET_ROW_ID)
            .targetViewId(DEFAULT_TARGET_VIEW_ID)
            .targetView(DEFAULT_TARGET_VIEW)
            .reconReference(DEFAULT_RECON_REFERENCE)
            .reconciliationRuleName(DEFAULT_RECONCILIATION_RULE_NAME)
            .reconciliationRuleGroupId(DEFAULT_RECONCILIATION_RULE_GROUP_ID)
            .reconciliationRuleId(DEFAULT_RECONCILIATION_RULE_ID)
            .reconciliationUserId(DEFAULT_RECONCILIATION_USER_ID)
            .reconJobReference(DEFAULT_RECON_JOB_REFERENCE)
            .reconciledDate(DEFAULT_RECONCILED_DATE)
            .reconStatus(DEFAULT_RECON_STATUS)
            .currentRecordFlag(DEFAULT_CURRENT_RECORD_FLAG)
            .tenantId(DEFAULT_TENANT_ID)
/*            .finalStatus(DEFAULT_FINAL_STATUS)
            .finalActionDate(DEFAULT_FINAL_ACTION_DATE)*/;
        return reconciliationDuplicateResult;
    }

    @Before
    public void initTest() {
        reconciliationDuplicateResult = createEntity(em);
    }

    @Test
    @Transactional
    public void createReconciliationDuplicateResult() throws Exception {
        int databaseSizeBeforeCreate = reconciliationDuplicateResultRepository.findAll().size();

        // Create the ReconciliationDuplicateResult
        restReconciliationDuplicateResultMockMvc.perform(post("/api/reconciliation-duplicate-results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reconciliationDuplicateResult)))
            .andExpect(status().isCreated());

        // Validate the ReconciliationDuplicateResult in the database
        List<ReconciliationDuplicateResult> reconciliationDuplicateResultList = reconciliationDuplicateResultRepository.findAll();
        assertThat(reconciliationDuplicateResultList).hasSize(databaseSizeBeforeCreate + 1);
        ReconciliationDuplicateResult testReconciliationDuplicateResult = reconciliationDuplicateResultList.get(reconciliationDuplicateResultList.size() - 1);
        assertThat(testReconciliationDuplicateResult.getOriginalRowId()).isEqualTo(DEFAULT_ORIGINAL_ROW_ID);
        assertThat(testReconciliationDuplicateResult.getOriginalViewId()).isEqualTo(DEFAULT_ORIGINAL_VIEW_ID);
        assertThat(testReconciliationDuplicateResult.getOriginalView()).isEqualTo(DEFAULT_ORIGINAL_VIEW);
        assertThat(testReconciliationDuplicateResult.getTargetRowId()).isEqualTo(DEFAULT_TARGET_ROW_ID);
        assertThat(testReconciliationDuplicateResult.getTargetViewId()).isEqualTo(DEFAULT_TARGET_VIEW_ID);
        assertThat(testReconciliationDuplicateResult.getTargetView()).isEqualTo(DEFAULT_TARGET_VIEW);
        assertThat(testReconciliationDuplicateResult.getReconReference()).isEqualTo(DEFAULT_RECON_REFERENCE);
        assertThat(testReconciliationDuplicateResult.getReconciliationRuleName()).isEqualTo(DEFAULT_RECONCILIATION_RULE_NAME);
        assertThat(testReconciliationDuplicateResult.getReconciliationRuleGroupId()).isEqualTo(DEFAULT_RECONCILIATION_RULE_GROUP_ID);
        assertThat(testReconciliationDuplicateResult.getReconciliationRuleId()).isEqualTo(DEFAULT_RECONCILIATION_RULE_ID);
        assertThat(testReconciliationDuplicateResult.getReconciliationUserId()).isEqualTo(DEFAULT_RECONCILIATION_USER_ID);
        assertThat(testReconciliationDuplicateResult.getReconJobReference()).isEqualTo(DEFAULT_RECON_JOB_REFERENCE);
        assertThat(testReconciliationDuplicateResult.getReconciledDate()).isEqualTo(DEFAULT_RECONCILED_DATE);
        assertThat(testReconciliationDuplicateResult.getReconStatus()).isEqualTo(DEFAULT_RECON_STATUS);
        assertThat(testReconciliationDuplicateResult.isCurrentRecordFlag()).isEqualTo(DEFAULT_CURRENT_RECORD_FLAG);
        assertThat(testReconciliationDuplicateResult.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
/*        assertThat(testReconciliationDuplicateResult.getFinalStatus()).isEqualTo(DEFAULT_FINAL_STATUS);
        assertThat(testReconciliationDuplicateResult.getFinalActionDate()).isEqualTo(DEFAULT_FINAL_ACTION_DATE);*/
    }

    @Test
    @Transactional
    public void createReconciliationDuplicateResultWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reconciliationDuplicateResultRepository.findAll().size();

        // Create the ReconciliationDuplicateResult with an existing ID
        reconciliationDuplicateResult.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReconciliationDuplicateResultMockMvc.perform(post("/api/reconciliation-duplicate-results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reconciliationDuplicateResult)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ReconciliationDuplicateResult> reconciliationDuplicateResultList = reconciliationDuplicateResultRepository.findAll();
        assertThat(reconciliationDuplicateResultList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllReconciliationDuplicateResults() throws Exception {
        // Initialize the database
        reconciliationDuplicateResultRepository.saveAndFlush(reconciliationDuplicateResult);

        // Get all the reconciliationDuplicateResultList
        restReconciliationDuplicateResultMockMvc.perform(get("/api/reconciliation-duplicate-results?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reconciliationDuplicateResult.getId().intValue())))
            .andExpect(jsonPath("$.[*].originalRowId").value(hasItem(DEFAULT_ORIGINAL_ROW_ID.intValue())))
            .andExpect(jsonPath("$.[*].originalViewId").value(hasItem(DEFAULT_ORIGINAL_VIEW_ID.intValue())))
            .andExpect(jsonPath("$.[*].originalView").value(hasItem(DEFAULT_ORIGINAL_VIEW.toString())))
            .andExpect(jsonPath("$.[*].targetRowId").value(hasItem(DEFAULT_TARGET_ROW_ID.intValue())))
            .andExpect(jsonPath("$.[*].targetViewId").value(hasItem(DEFAULT_TARGET_VIEW_ID.intValue())))
            .andExpect(jsonPath("$.[*].targetView").value(hasItem(DEFAULT_TARGET_VIEW.toString())))
            .andExpect(jsonPath("$.[*].reconReference").value(hasItem(DEFAULT_RECON_REFERENCE.toString())))
            .andExpect(jsonPath("$.[*].reconciliationRuleName").value(hasItem(DEFAULT_RECONCILIATION_RULE_NAME.toString())))
            .andExpect(jsonPath("$.[*].reconciliationRuleGroupId").value(hasItem(DEFAULT_RECONCILIATION_RULE_GROUP_ID.intValue())))
            .andExpect(jsonPath("$.[*].reconciliationRuleId").value(hasItem(DEFAULT_RECONCILIATION_RULE_ID.intValue())))
            .andExpect(jsonPath("$.[*].reconciliationUserId").value(hasItem(DEFAULT_RECONCILIATION_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].reconJobReference").value(hasItem(DEFAULT_RECON_JOB_REFERENCE.toString())))
            .andExpect(jsonPath("$.[*].reconciledDate").value(hasItem(sameInstant(DEFAULT_RECONCILED_DATE))))
            .andExpect(jsonPath("$.[*].reconStatus").value(hasItem(DEFAULT_RECON_STATUS.toString())))
            .andExpect(jsonPath("$.[*].currentRecordFlag").value(hasItem(DEFAULT_CURRENT_RECORD_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
/*            .andExpect(jsonPath("$.[*].finalStatus").value(hasItem(DEFAULT_FINAL_STATUS.toString())))
            .andExpect(jsonPath("$.[*].finalActionDate").value(hasItem(sameInstant(DEFAULT_FINAL_ACTION_DATE))))*/;
    }

    @Test
    @Transactional
    public void getReconciliationDuplicateResult() throws Exception {
        // Initialize the database
        reconciliationDuplicateResultRepository.saveAndFlush(reconciliationDuplicateResult);

        // Get the reconciliationDuplicateResult
        restReconciliationDuplicateResultMockMvc.perform(get("/api/reconciliation-duplicate-results/{id}", reconciliationDuplicateResult.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(reconciliationDuplicateResult.getId().intValue()))
            .andExpect(jsonPath("$.originalRowId").value(DEFAULT_ORIGINAL_ROW_ID.intValue()))
            .andExpect(jsonPath("$.originalViewId").value(DEFAULT_ORIGINAL_VIEW_ID.intValue()))
            .andExpect(jsonPath("$.originalView").value(DEFAULT_ORIGINAL_VIEW.toString()))
            .andExpect(jsonPath("$.targetRowId").value(DEFAULT_TARGET_ROW_ID.intValue()))
            .andExpect(jsonPath("$.targetViewId").value(DEFAULT_TARGET_VIEW_ID.intValue()))
            .andExpect(jsonPath("$.targetView").value(DEFAULT_TARGET_VIEW.toString()))
            .andExpect(jsonPath("$.reconReference").value(DEFAULT_RECON_REFERENCE.toString()))
            .andExpect(jsonPath("$.reconciliationRuleName").value(DEFAULT_RECONCILIATION_RULE_NAME.toString()))
            .andExpect(jsonPath("$.reconciliationRuleGroupId").value(DEFAULT_RECONCILIATION_RULE_GROUP_ID.intValue()))
            .andExpect(jsonPath("$.reconciliationRuleId").value(DEFAULT_RECONCILIATION_RULE_ID.intValue()))
            .andExpect(jsonPath("$.reconciliationUserId").value(DEFAULT_RECONCILIATION_USER_ID.intValue()))
            .andExpect(jsonPath("$.reconJobReference").value(DEFAULT_RECON_JOB_REFERENCE.toString()))
            .andExpect(jsonPath("$.reconciledDate").value(sameInstant(DEFAULT_RECONCILED_DATE)))
            .andExpect(jsonPath("$.reconStatus").value(DEFAULT_RECON_STATUS.toString()))
            .andExpect(jsonPath("$.currentRecordFlag").value(DEFAULT_CURRENT_RECORD_FLAG.booleanValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID.intValue()))
/*            .andExpect(jsonPath("$.finalStatus").value(DEFAULT_FINAL_STATUS.toString()))
            .andExpect(jsonPath("$.finalActionDate").value(sameInstant(DEFAULT_FINAL_ACTION_DATE)))*/;
    }

    @Test
    @Transactional
    public void getNonExistingReconciliationDuplicateResult() throws Exception {
        // Get the reconciliationDuplicateResult
        restReconciliationDuplicateResultMockMvc.perform(get("/api/reconciliation-duplicate-results/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReconciliationDuplicateResult() throws Exception {
        // Initialize the database
        reconciliationDuplicateResultRepository.saveAndFlush(reconciliationDuplicateResult);
        int databaseSizeBeforeUpdate = reconciliationDuplicateResultRepository.findAll().size();

        // Update the reconciliationDuplicateResult
        ReconciliationDuplicateResult updatedReconciliationDuplicateResult = reconciliationDuplicateResultRepository.findOne(reconciliationDuplicateResult.getId());
        updatedReconciliationDuplicateResult
            .originalRowId(UPDATED_ORIGINAL_ROW_ID)
            .originalViewId(UPDATED_ORIGINAL_VIEW_ID)
            .originalView(UPDATED_ORIGINAL_VIEW)
            .targetRowId(UPDATED_TARGET_ROW_ID)
            .targetViewId(UPDATED_TARGET_VIEW_ID)
            .targetView(UPDATED_TARGET_VIEW)
            .reconReference(UPDATED_RECON_REFERENCE)
            .reconciliationRuleName(UPDATED_RECONCILIATION_RULE_NAME)
            .reconciliationRuleGroupId(UPDATED_RECONCILIATION_RULE_GROUP_ID)
            .reconciliationRuleId(UPDATED_RECONCILIATION_RULE_ID)
            .reconciliationUserId(UPDATED_RECONCILIATION_USER_ID)
            .reconJobReference(UPDATED_RECON_JOB_REFERENCE)
            .reconciledDate(UPDATED_RECONCILED_DATE)
            .reconStatus(UPDATED_RECON_STATUS)
            .currentRecordFlag(UPDATED_CURRENT_RECORD_FLAG)
            .tenantId(UPDATED_TENANT_ID)
/*            .finalStatus(UPDATED_FINAL_STATUS)
            .finalActionDate(UPDATED_FINAL_ACTION_DATE)*/;

        restReconciliationDuplicateResultMockMvc.perform(put("/api/reconciliation-duplicate-results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedReconciliationDuplicateResult)))
            .andExpect(status().isOk());

        // Validate the ReconciliationDuplicateResult in the database
        List<ReconciliationDuplicateResult> reconciliationDuplicateResultList = reconciliationDuplicateResultRepository.findAll();
        assertThat(reconciliationDuplicateResultList).hasSize(databaseSizeBeforeUpdate);
        ReconciliationDuplicateResult testReconciliationDuplicateResult = reconciliationDuplicateResultList.get(reconciliationDuplicateResultList.size() - 1);
        assertThat(testReconciliationDuplicateResult.getOriginalRowId()).isEqualTo(UPDATED_ORIGINAL_ROW_ID);
        assertThat(testReconciliationDuplicateResult.getOriginalViewId()).isEqualTo(UPDATED_ORIGINAL_VIEW_ID);
        assertThat(testReconciliationDuplicateResult.getOriginalView()).isEqualTo(UPDATED_ORIGINAL_VIEW);
        assertThat(testReconciliationDuplicateResult.getTargetRowId()).isEqualTo(UPDATED_TARGET_ROW_ID);
        assertThat(testReconciliationDuplicateResult.getTargetViewId()).isEqualTo(UPDATED_TARGET_VIEW_ID);
        assertThat(testReconciliationDuplicateResult.getTargetView()).isEqualTo(UPDATED_TARGET_VIEW);
        assertThat(testReconciliationDuplicateResult.getReconReference()).isEqualTo(UPDATED_RECON_REFERENCE);
        assertThat(testReconciliationDuplicateResult.getReconciliationRuleName()).isEqualTo(UPDATED_RECONCILIATION_RULE_NAME);
        assertThat(testReconciliationDuplicateResult.getReconciliationRuleGroupId()).isEqualTo(UPDATED_RECONCILIATION_RULE_GROUP_ID);
        assertThat(testReconciliationDuplicateResult.getReconciliationRuleId()).isEqualTo(UPDATED_RECONCILIATION_RULE_ID);
        assertThat(testReconciliationDuplicateResult.getReconciliationUserId()).isEqualTo(UPDATED_RECONCILIATION_USER_ID);
        assertThat(testReconciliationDuplicateResult.getReconJobReference()).isEqualTo(UPDATED_RECON_JOB_REFERENCE);
        assertThat(testReconciliationDuplicateResult.getReconciledDate()).isEqualTo(UPDATED_RECONCILED_DATE);
        assertThat(testReconciliationDuplicateResult.getReconStatus()).isEqualTo(UPDATED_RECON_STATUS);
        assertThat(testReconciliationDuplicateResult.isCurrentRecordFlag()).isEqualTo(UPDATED_CURRENT_RECORD_FLAG);
        assertThat(testReconciliationDuplicateResult.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
/*        assertThat(testReconciliationDuplicateResult.getFinalStatus()).isEqualTo(UPDATED_FINAL_STATUS);
        assertThat(testReconciliationDuplicateResult.getFinalActionDate()).isEqualTo(UPDATED_FINAL_ACTION_DATE)*/;
    }

    @Test
    @Transactional
    public void updateNonExistingReconciliationDuplicateResult() throws Exception {
        int databaseSizeBeforeUpdate = reconciliationDuplicateResultRepository.findAll().size();

        // Create the ReconciliationDuplicateResult

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restReconciliationDuplicateResultMockMvc.perform(put("/api/reconciliation-duplicate-results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reconciliationDuplicateResult)))
            .andExpect(status().isCreated());

        // Validate the ReconciliationDuplicateResult in the database
        List<ReconciliationDuplicateResult> reconciliationDuplicateResultList = reconciliationDuplicateResultRepository.findAll();
        assertThat(reconciliationDuplicateResultList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteReconciliationDuplicateResult() throws Exception {
        // Initialize the database
        reconciliationDuplicateResultRepository.saveAndFlush(reconciliationDuplicateResult);
        int databaseSizeBeforeDelete = reconciliationDuplicateResultRepository.findAll().size();

        // Get the reconciliationDuplicateResult
        restReconciliationDuplicateResultMockMvc.perform(delete("/api/reconciliation-duplicate-results/{id}", reconciliationDuplicateResult.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ReconciliationDuplicateResult> reconciliationDuplicateResultList = reconciliationDuplicateResultRepository.findAll();
        assertThat(reconciliationDuplicateResultList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReconciliationDuplicateResult.class);
        ReconciliationDuplicateResult reconciliationDuplicateResult1 = new ReconciliationDuplicateResult();
        reconciliationDuplicateResult1.setId(1L);
        ReconciliationDuplicateResult reconciliationDuplicateResult2 = new ReconciliationDuplicateResult();
        reconciliationDuplicateResult2.setId(reconciliationDuplicateResult1.getId());
        assertThat(reconciliationDuplicateResult1).isEqualTo(reconciliationDuplicateResult2);
        reconciliationDuplicateResult2.setId(2L);
        assertThat(reconciliationDuplicateResult1).isNotEqualTo(reconciliationDuplicateResult2);
        reconciliationDuplicateResult1.setId(null);
        assertThat(reconciliationDuplicateResult1).isNotEqualTo(reconciliationDuplicateResult2);
    }
}
