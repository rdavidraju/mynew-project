package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.JobActions;
import com.nspl.app.repository.JobActionsRepository;
import com.nspl.app.repository.search.JobActionsSearchRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the JobActionsResource REST controller.
 *
 * @see JobActionsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class JobActionsResourceIntTest {

    private static final Long DEFAULT_STEP = 1L;
    private static final Long UPDATED_STEP = 2L;

    private static final String DEFAULT_ACTION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ACTION_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_SCHEDULER_ID = 1L;
    private static final Long UPDATED_SCHEDULER_ID = 2L;

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    @Autowired
    private JobActionsRepository jobActionsRepository;

    @Autowired
    private JobActionsSearchRepository jobActionsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restJobActionsMockMvc;

    private JobActions jobActions;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        JobActionsResource jobActionsResource = new JobActionsResource(jobActionsRepository, jobActionsSearchRepository);
        this.restJobActionsMockMvc = MockMvcBuilders.standaloneSetup(jobActionsResource)
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
    public static JobActions createEntity(EntityManager em) {
        JobActions jobActions = new JobActions()
            .step(DEFAULT_STEP)
            .actionName(DEFAULT_ACTION_NAME)
            .schedulerId(DEFAULT_SCHEDULER_ID)
            .status(DEFAULT_STATUS);
        return jobActions;
    }

    @Before
    public void initTest() {
        jobActionsSearchRepository.deleteAll();
        jobActions = createEntity(em);
    }

    @Test
    @Transactional
    public void createJobActions() throws Exception {
        int databaseSizeBeforeCreate = jobActionsRepository.findAll().size();

        // Create the JobActions
        restJobActionsMockMvc.perform(post("/api/job-actions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobActions)))
            .andExpect(status().isCreated());

        // Validate the JobActions in the database
        List<JobActions> jobActionsList = jobActionsRepository.findAll();
        assertThat(jobActionsList).hasSize(databaseSizeBeforeCreate + 1);
        JobActions testJobActions = jobActionsList.get(jobActionsList.size() - 1);
        assertThat(testJobActions.getStep()).isEqualTo(DEFAULT_STEP);
        assertThat(testJobActions.getActionName()).isEqualTo(DEFAULT_ACTION_NAME);
        assertThat(testJobActions.getSchedulerId()).isEqualTo(DEFAULT_SCHEDULER_ID);
        assertThat(testJobActions.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the JobActions in Elasticsearch
        JobActions jobActionsEs = jobActionsSearchRepository.findOne(testJobActions.getId());
        assertThat(jobActionsEs).isEqualToComparingFieldByField(testJobActions);
    }

    @Test
    @Transactional
    public void createJobActionsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jobActionsRepository.findAll().size();

        // Create the JobActions with an existing ID
        jobActions.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobActionsMockMvc.perform(post("/api/job-actions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobActions)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<JobActions> jobActionsList = jobActionsRepository.findAll();
        assertThat(jobActionsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllJobActions() throws Exception {
        // Initialize the database
        jobActionsRepository.saveAndFlush(jobActions);

        // Get all the jobActionsList
        restJobActionsMockMvc.perform(get("/api/job-actions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobActions.getId().intValue())))
            .andExpect(jsonPath("$.[*].step").value(hasItem(DEFAULT_STEP.intValue())))
            .andExpect(jsonPath("$.[*].actionName").value(hasItem(DEFAULT_ACTION_NAME.toString())))
            .andExpect(jsonPath("$.[*].schedulerId").value(hasItem(DEFAULT_SCHEDULER_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getJobActions() throws Exception {
        // Initialize the database
        jobActionsRepository.saveAndFlush(jobActions);

        // Get the jobActions
        restJobActionsMockMvc.perform(get("/api/job-actions/{id}", jobActions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(jobActions.getId().intValue()))
            .andExpect(jsonPath("$.step").value(DEFAULT_STEP.intValue()))
            .andExpect(jsonPath("$.actionName").value(DEFAULT_ACTION_NAME.toString()))
            .andExpect(jsonPath("$.schedulerId").value(DEFAULT_SCHEDULER_ID.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingJobActions() throws Exception {
        // Get the jobActions
        restJobActionsMockMvc.perform(get("/api/job-actions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJobActions() throws Exception {
        // Initialize the database
        jobActionsRepository.saveAndFlush(jobActions);
        jobActionsSearchRepository.save(jobActions);
        int databaseSizeBeforeUpdate = jobActionsRepository.findAll().size();

        // Update the jobActions
        JobActions updatedJobActions = jobActionsRepository.findOne(jobActions.getId());
        updatedJobActions
            .step(UPDATED_STEP)
            .actionName(UPDATED_ACTION_NAME)
            .schedulerId(UPDATED_SCHEDULER_ID)
            .status(UPDATED_STATUS);

        restJobActionsMockMvc.perform(put("/api/job-actions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedJobActions)))
            .andExpect(status().isOk());

        // Validate the JobActions in the database
        List<JobActions> jobActionsList = jobActionsRepository.findAll();
        assertThat(jobActionsList).hasSize(databaseSizeBeforeUpdate);
        JobActions testJobActions = jobActionsList.get(jobActionsList.size() - 1);
        assertThat(testJobActions.getStep()).isEqualTo(UPDATED_STEP);
        assertThat(testJobActions.getActionName()).isEqualTo(UPDATED_ACTION_NAME);
        assertThat(testJobActions.getSchedulerId()).isEqualTo(UPDATED_SCHEDULER_ID);
        assertThat(testJobActions.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the JobActions in Elasticsearch
        JobActions jobActionsEs = jobActionsSearchRepository.findOne(testJobActions.getId());
        assertThat(jobActionsEs).isEqualToComparingFieldByField(testJobActions);
    }

    @Test
    @Transactional
    public void updateNonExistingJobActions() throws Exception {
        int databaseSizeBeforeUpdate = jobActionsRepository.findAll().size();

        // Create the JobActions

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restJobActionsMockMvc.perform(put("/api/job-actions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobActions)))
            .andExpect(status().isCreated());

        // Validate the JobActions in the database
        List<JobActions> jobActionsList = jobActionsRepository.findAll();
        assertThat(jobActionsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteJobActions() throws Exception {
        // Initialize the database
        jobActionsRepository.saveAndFlush(jobActions);
        jobActionsSearchRepository.save(jobActions);
        int databaseSizeBeforeDelete = jobActionsRepository.findAll().size();

        // Get the jobActions
        restJobActionsMockMvc.perform(delete("/api/job-actions/{id}", jobActions.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean jobActionsExistsInEs = jobActionsSearchRepository.exists(jobActions.getId());
        assertThat(jobActionsExistsInEs).isFalse();

        // Validate the database is empty
        List<JobActions> jobActionsList = jobActionsRepository.findAll();
        assertThat(jobActionsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchJobActions() throws Exception {
        // Initialize the database
        jobActionsRepository.saveAndFlush(jobActions);
        jobActionsSearchRepository.save(jobActions);

        // Search the jobActions
        restJobActionsMockMvc.perform(get("/api/_search/job-actions?query=id:" + jobActions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobActions.getId().intValue())))
            .andExpect(jsonPath("$.[*].step").value(hasItem(DEFAULT_STEP.intValue())))
            .andExpect(jsonPath("$.[*].actionName").value(hasItem(DEFAULT_ACTION_NAME.toString())))
            .andExpect(jsonPath("$.[*].schedulerId").value(hasItem(DEFAULT_SCHEDULER_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobActions.class);
    }
}
