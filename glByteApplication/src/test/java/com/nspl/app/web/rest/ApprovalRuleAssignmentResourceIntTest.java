package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.ApprovalRuleAssignment;
import com.nspl.app.repository.ApprovalRuleAssignmentRepository;
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
 * Test class for the ApprovalRuleAssignmentResource REST controller.
 *
 * @see ApprovalRuleAssignmentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class ApprovalRuleAssignmentResourceIntTest {

    private static final Long DEFAULT_RULE_ID = 1L;
    private static final Long UPDATED_RULE_ID = 2L;

    private static final String DEFAULT_ASSIGN_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ASSIGN_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_ASSIGNEE_ID = 1L;
    private static final Long UPDATED_ASSIGNEE_ID = 2L;

    private static final Boolean DEFAULT_EMAIL = false;
    private static final Boolean UPDATED_EMAIL = true;

    private static final Boolean DEFAULT_AUTO_APPROVAL = false;
    private static final Boolean UPDATED_AUTO_APPROVAL = true;

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private ApprovalRuleAssignmentRepository approvalRuleAssignmentRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restApprovalRuleAssignmentMockMvc;

    private ApprovalRuleAssignment approvalRuleAssignment;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ApprovalRuleAssignmentResource approvalRuleAssignmentResource = new ApprovalRuleAssignmentResource(approvalRuleAssignmentRepository);
        this.restApprovalRuleAssignmentMockMvc = MockMvcBuilders.standaloneSetup(approvalRuleAssignmentResource)
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
    public static ApprovalRuleAssignment createEntity(EntityManager em) {
        ApprovalRuleAssignment approvalRuleAssignment = new ApprovalRuleAssignment()
            .ruleId(DEFAULT_RULE_ID)
            .assignType(DEFAULT_ASSIGN_TYPE)
            .assigneeId(DEFAULT_ASSIGNEE_ID)
            .email(DEFAULT_EMAIL)
            .autoApproval(DEFAULT_AUTO_APPROVAL)
            .createdBy(DEFAULT_CREATED_BY)
            .creationDate(DEFAULT_CREATION_DATE)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE);
        return approvalRuleAssignment;
    }

    @Before
    public void initTest() {
        approvalRuleAssignment = createEntity(em);
    }

    @Test
    @Transactional
    public void createApprovalRuleAssignment() throws Exception {
        int databaseSizeBeforeCreate = approvalRuleAssignmentRepository.findAll().size();

        // Create the ApprovalRuleAssignment
        restApprovalRuleAssignmentMockMvc.perform(post("/api/approval-rule-assignments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(approvalRuleAssignment)))
            .andExpect(status().isCreated());

        // Validate the ApprovalRuleAssignment in the database
        List<ApprovalRuleAssignment> approvalRuleAssignmentList = approvalRuleAssignmentRepository.findAll();
        assertThat(approvalRuleAssignmentList).hasSize(databaseSizeBeforeCreate + 1);
        ApprovalRuleAssignment testApprovalRuleAssignment = approvalRuleAssignmentList.get(approvalRuleAssignmentList.size() - 1);
        assertThat(testApprovalRuleAssignment.getRuleId()).isEqualTo(DEFAULT_RULE_ID);
        assertThat(testApprovalRuleAssignment.getAssignType()).isEqualTo(DEFAULT_ASSIGN_TYPE);
        assertThat(testApprovalRuleAssignment.getAssigneeId()).isEqualTo(DEFAULT_ASSIGNEE_ID);
        assertThat(testApprovalRuleAssignment.isEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testApprovalRuleAssignment.isAutoApproval()).isEqualTo(DEFAULT_AUTO_APPROVAL);
        assertThat(testApprovalRuleAssignment.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testApprovalRuleAssignment.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testApprovalRuleAssignment.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testApprovalRuleAssignment.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void createApprovalRuleAssignmentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = approvalRuleAssignmentRepository.findAll().size();

        // Create the ApprovalRuleAssignment with an existing ID
        approvalRuleAssignment.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restApprovalRuleAssignmentMockMvc.perform(post("/api/approval-rule-assignments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(approvalRuleAssignment)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ApprovalRuleAssignment> approvalRuleAssignmentList = approvalRuleAssignmentRepository.findAll();
        assertThat(approvalRuleAssignmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllApprovalRuleAssignments() throws Exception {
        // Initialize the database
        approvalRuleAssignmentRepository.saveAndFlush(approvalRuleAssignment);

        // Get all the approvalRuleAssignmentList
        restApprovalRuleAssignmentMockMvc.perform(get("/api/approval-rule-assignments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(approvalRuleAssignment.getId().intValue())))
            .andExpect(jsonPath("$.[*].ruleId").value(hasItem(DEFAULT_RULE_ID.intValue())))
            .andExpect(jsonPath("$.[*].assignType").value(hasItem(DEFAULT_ASSIGN_TYPE.toString())))
            .andExpect(jsonPath("$.[*].assigneeId").value(hasItem(DEFAULT_ASSIGNEE_ID.intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.booleanValue())))
            .andExpect(jsonPath("$.[*].autoApproval").value(hasItem(DEFAULT_AUTO_APPROVAL.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void getApprovalRuleAssignment() throws Exception {
        // Initialize the database
        approvalRuleAssignmentRepository.saveAndFlush(approvalRuleAssignment);

        // Get the approvalRuleAssignment
        restApprovalRuleAssignmentMockMvc.perform(get("/api/approval-rule-assignments/{id}", approvalRuleAssignment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(approvalRuleAssignment.getId().intValue()))
            .andExpect(jsonPath("$.ruleId").value(DEFAULT_RULE_ID.intValue()))
            .andExpect(jsonPath("$.assignType").value(DEFAULT_ASSIGN_TYPE.toString()))
            .andExpect(jsonPath("$.assigneeId").value(DEFAULT_ASSIGNEE_ID.intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.booleanValue()))
            .andExpect(jsonPath("$.autoApproval").value(DEFAULT_AUTO_APPROVAL.booleanValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.creationDate").value(sameInstant(DEFAULT_CREATION_DATE)))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingApprovalRuleAssignment() throws Exception {
        // Get the approvalRuleAssignment
        restApprovalRuleAssignmentMockMvc.perform(get("/api/approval-rule-assignments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApprovalRuleAssignment() throws Exception {
        // Initialize the database
        approvalRuleAssignmentRepository.saveAndFlush(approvalRuleAssignment);
        int databaseSizeBeforeUpdate = approvalRuleAssignmentRepository.findAll().size();

        // Update the approvalRuleAssignment
        ApprovalRuleAssignment updatedApprovalRuleAssignment = approvalRuleAssignmentRepository.findOne(approvalRuleAssignment.getId());
        updatedApprovalRuleAssignment
            .ruleId(UPDATED_RULE_ID)
            .assignType(UPDATED_ASSIGN_TYPE)
            .assigneeId(UPDATED_ASSIGNEE_ID)
            .email(UPDATED_EMAIL)
            .autoApproval(UPDATED_AUTO_APPROVAL)
            .createdBy(UPDATED_CREATED_BY)
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE);

        restApprovalRuleAssignmentMockMvc.perform(put("/api/approval-rule-assignments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedApprovalRuleAssignment)))
            .andExpect(status().isOk());

        // Validate the ApprovalRuleAssignment in the database
        List<ApprovalRuleAssignment> approvalRuleAssignmentList = approvalRuleAssignmentRepository.findAll();
        assertThat(approvalRuleAssignmentList).hasSize(databaseSizeBeforeUpdate);
        ApprovalRuleAssignment testApprovalRuleAssignment = approvalRuleAssignmentList.get(approvalRuleAssignmentList.size() - 1);
        assertThat(testApprovalRuleAssignment.getRuleId()).isEqualTo(UPDATED_RULE_ID);
        assertThat(testApprovalRuleAssignment.getAssignType()).isEqualTo(UPDATED_ASSIGN_TYPE);
        assertThat(testApprovalRuleAssignment.getAssigneeId()).isEqualTo(UPDATED_ASSIGNEE_ID);
        assertThat(testApprovalRuleAssignment.isEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testApprovalRuleAssignment.isAutoApproval()).isEqualTo(UPDATED_AUTO_APPROVAL);
        assertThat(testApprovalRuleAssignment.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testApprovalRuleAssignment.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testApprovalRuleAssignment.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testApprovalRuleAssignment.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingApprovalRuleAssignment() throws Exception {
        int databaseSizeBeforeUpdate = approvalRuleAssignmentRepository.findAll().size();

        // Create the ApprovalRuleAssignment

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restApprovalRuleAssignmentMockMvc.perform(put("/api/approval-rule-assignments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(approvalRuleAssignment)))
            .andExpect(status().isCreated());

        // Validate the ApprovalRuleAssignment in the database
        List<ApprovalRuleAssignment> approvalRuleAssignmentList = approvalRuleAssignmentRepository.findAll();
        assertThat(approvalRuleAssignmentList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteApprovalRuleAssignment() throws Exception {
        // Initialize the database
        approvalRuleAssignmentRepository.saveAndFlush(approvalRuleAssignment);
        int databaseSizeBeforeDelete = approvalRuleAssignmentRepository.findAll().size();

        // Get the approvalRuleAssignment
        restApprovalRuleAssignmentMockMvc.perform(delete("/api/approval-rule-assignments/{id}", approvalRuleAssignment.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ApprovalRuleAssignment> approvalRuleAssignmentList = approvalRuleAssignmentRepository.findAll();
        assertThat(approvalRuleAssignmentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApprovalRuleAssignment.class);
    }
}
