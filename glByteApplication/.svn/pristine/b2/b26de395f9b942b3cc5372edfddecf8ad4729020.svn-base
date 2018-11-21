package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.JobDetails;
import com.nspl.app.repository.JobDetailsRepository;
import com.nspl.app.repository.search.JobDetailsSearchRepository;
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
 * Test class for the JobDetailsResource REST controller.
 *
 * @see JobDetailsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class JobDetailsResourceIntTest {

    private static final Long DEFAULT_PROGRAMM_ID = 1L;
    private static final Long UPDATED_PROGRAMM_ID = 2L;

    private static final String DEFAULT_JOB_NAME = "AAAAAAAAAA";
    private static final String UPDATED_JOB_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_JOB_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_JOB_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLE = false;
    private static final Boolean UPDATED_ENABLE = true;

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_PARAMETER_ARGUMENTS_1 = "AAAAAAAAAA";
    private static final String UPDATED_PARAMETER_ARGUMENTS_1 = "BBBBBBBBBB";

    private static final String DEFAULT_PARAMETER_ARGUMENTS_2 = "AAAAAAAAAA";
    private static final String UPDATED_PARAMETER_ARGUMENTS_2 = "BBBBBBBBBB";

    private static final String DEFAULT_PARAMETER_ARGUMENTS_3 = "AAAAAAAAAA";
    private static final String UPDATED_PARAMETER_ARGUMENTS_3 = "BBBBBBBBBB";

    private static final String DEFAULT_PARAMETER_ARGUMENTS_4 = "AAAAAAAAAA";
    private static final String UPDATED_PARAMETER_ARGUMENTS_4 = "BBBBBBBBBB";

    private static final String DEFAULT_PARAMETER_ARGUMENTS_5 = "AAAAAAAAAA";
    private static final String UPDATED_PARAMETER_ARGUMENTS_5 = "BBBBBBBBBB";

    private static final String DEFAULT_PARAMETER_ARGUMENTS_6 = "AAAAAAAAAA";
    private static final String UPDATED_PARAMETER_ARGUMENTS_6 = "BBBBBBBBBB";

    private static final String DEFAULT_PARAMETER_ARGUMENTS_7 = "AAAAAAAAAA";
    private static final String UPDATED_PARAMETER_ARGUMENTS_7 = "BBBBBBBBBB";

    private static final String DEFAULT_PARAMETER_ARGUMENTS_8 = "AAAAAAAAAA";
    private static final String UPDATED_PARAMETER_ARGUMENTS_8 = "BBBBBBBBBB";

    private static final String DEFAULT_PARAMETER_ARGUMENTS_9 = "AAAAAAAAAA";
    private static final String UPDATED_PARAMETER_ARGUMENTS_9 = "BBBBBBBBBB";

    private static final String DEFAULT_PARAMETER_ARGUMENTS_10 = "AAAAAAAAAA";
    private static final String UPDATED_PARAMETER_ARGUMENTS_10 = "BBBBBBBBBB";

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_TENANT_ID = 1L;
    private static final Long UPDATED_TENANT_ID = 2L;

    @Autowired
    private JobDetailsRepository jobDetailsRepository;

    @Autowired
    private JobDetailsSearchRepository jobDetailsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restJobDetailsMockMvc;

    private JobDetails jobDetails;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        JobDetailsResource jobDetailsResource = new JobDetailsResource(jobDetailsRepository, jobDetailsSearchRepository);
        this.restJobDetailsMockMvc = MockMvcBuilders.standaloneSetup(jobDetailsResource)
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
    public static JobDetails createEntity(EntityManager em) {
        JobDetails jobDetails = new JobDetails()
            .programmId(DEFAULT_PROGRAMM_ID)
            .jobName(DEFAULT_JOB_NAME)
            .jobDescription(DEFAULT_JOB_DESCRIPTION)
            .enable(DEFAULT_ENABLE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .parameterArguments1(DEFAULT_PARAMETER_ARGUMENTS_1)
            .parameterArguments2(DEFAULT_PARAMETER_ARGUMENTS_2)
            .parameterArguments3(DEFAULT_PARAMETER_ARGUMENTS_3)
            .parameterArguments4(DEFAULT_PARAMETER_ARGUMENTS_4)
            .parameterArguments5(DEFAULT_PARAMETER_ARGUMENTS_5)
            .parameterArguments6(DEFAULT_PARAMETER_ARGUMENTS_6)
            .parameterArguments7(DEFAULT_PARAMETER_ARGUMENTS_7)
            .parameterArguments8(DEFAULT_PARAMETER_ARGUMENTS_8)
            .parameterArguments9(DEFAULT_PARAMETER_ARGUMENTS_9)
            .parameterArguments10(DEFAULT_PARAMETER_ARGUMENTS_10)
            .createdBy(DEFAULT_CREATED_BY)
            .creationDate(DEFAULT_CREATION_DATE)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE)
            .tenantId(DEFAULT_TENANT_ID);
        return jobDetails;
    }

    @Before
    public void initTest() {
        jobDetailsSearchRepository.deleteAll();
        jobDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createJobDetails() throws Exception {
        int databaseSizeBeforeCreate = jobDetailsRepository.findAll().size();

        // Create the JobDetails
        restJobDetailsMockMvc.perform(post("/api/job-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobDetails)))
            .andExpect(status().isCreated());

        // Validate the JobDetails in the database
        List<JobDetails> jobDetailsList = jobDetailsRepository.findAll();
        assertThat(jobDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        JobDetails testJobDetails = jobDetailsList.get(jobDetailsList.size() - 1);
        assertThat(testJobDetails.getProgrammId()).isEqualTo(DEFAULT_PROGRAMM_ID);
        assertThat(testJobDetails.getJobName()).isEqualTo(DEFAULT_JOB_NAME);
        assertThat(testJobDetails.getJobDescription()).isEqualTo(DEFAULT_JOB_DESCRIPTION);
        assertThat(testJobDetails.isEnable()).isEqualTo(DEFAULT_ENABLE);
        assertThat(testJobDetails.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testJobDetails.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testJobDetails.getParameterArguments1()).isEqualTo(DEFAULT_PARAMETER_ARGUMENTS_1);
        assertThat(testJobDetails.getParameterArguments2()).isEqualTo(DEFAULT_PARAMETER_ARGUMENTS_2);
        assertThat(testJobDetails.getParameterArguments3()).isEqualTo(DEFAULT_PARAMETER_ARGUMENTS_3);
        assertThat(testJobDetails.getParameterArguments4()).isEqualTo(DEFAULT_PARAMETER_ARGUMENTS_4);
        assertThat(testJobDetails.getParameterArguments5()).isEqualTo(DEFAULT_PARAMETER_ARGUMENTS_5);
        assertThat(testJobDetails.getParameterArguments6()).isEqualTo(DEFAULT_PARAMETER_ARGUMENTS_6);
        assertThat(testJobDetails.getParameterArguments7()).isEqualTo(DEFAULT_PARAMETER_ARGUMENTS_7);
        assertThat(testJobDetails.getParameterArguments8()).isEqualTo(DEFAULT_PARAMETER_ARGUMENTS_8);
        assertThat(testJobDetails.getParameterArguments9()).isEqualTo(DEFAULT_PARAMETER_ARGUMENTS_9);
        assertThat(testJobDetails.getParameterArguments10()).isEqualTo(DEFAULT_PARAMETER_ARGUMENTS_10);
        assertThat(testJobDetails.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testJobDetails.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testJobDetails.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testJobDetails.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);
        assertThat(testJobDetails.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);

        // Validate the JobDetails in Elasticsearch
        JobDetails jobDetailsEs = jobDetailsSearchRepository.findOne(testJobDetails.getId());
        assertThat(jobDetailsEs).isEqualToComparingFieldByField(testJobDetails);
    }

    @Test
    @Transactional
    public void createJobDetailsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jobDetailsRepository.findAll().size();

        // Create the JobDetails with an existing ID
        jobDetails.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobDetailsMockMvc.perform(post("/api/job-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobDetails)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<JobDetails> jobDetailsList = jobDetailsRepository.findAll();
        assertThat(jobDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllJobDetails() throws Exception {
        // Initialize the database
        jobDetailsRepository.saveAndFlush(jobDetails);

        // Get all the jobDetailsList
        restJobDetailsMockMvc.perform(get("/api/job-details?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].programmId").value(hasItem(DEFAULT_PROGRAMM_ID.intValue())))
            .andExpect(jsonPath("$.[*].jobName").value(hasItem(DEFAULT_JOB_NAME.toString())))
            .andExpect(jsonPath("$.[*].jobDescription").value(hasItem(DEFAULT_JOB_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].enable").value(hasItem(DEFAULT_ENABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].parameterArguments1").value(hasItem(DEFAULT_PARAMETER_ARGUMENTS_1.toString())))
            .andExpect(jsonPath("$.[*].parameterArguments2").value(hasItem(DEFAULT_PARAMETER_ARGUMENTS_2.toString())))
            .andExpect(jsonPath("$.[*].parameterArguments3").value(hasItem(DEFAULT_PARAMETER_ARGUMENTS_3.toString())))
            .andExpect(jsonPath("$.[*].parameterArguments4").value(hasItem(DEFAULT_PARAMETER_ARGUMENTS_4.toString())))
            .andExpect(jsonPath("$.[*].parameterArguments5").value(hasItem(DEFAULT_PARAMETER_ARGUMENTS_5.toString())))
            .andExpect(jsonPath("$.[*].parameterArguments6").value(hasItem(DEFAULT_PARAMETER_ARGUMENTS_6.toString())))
            .andExpect(jsonPath("$.[*].parameterArguments7").value(hasItem(DEFAULT_PARAMETER_ARGUMENTS_7.toString())))
            .andExpect(jsonPath("$.[*].parameterArguments8").value(hasItem(DEFAULT_PARAMETER_ARGUMENTS_8.toString())))
            .andExpect(jsonPath("$.[*].parameterArguments9").value(hasItem(DEFAULT_PARAMETER_ARGUMENTS_9.toString())))
            .andExpect(jsonPath("$.[*].parameterArguments10").value(hasItem(DEFAULT_PARAMETER_ARGUMENTS_10.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())));
    }

    @Test
    @Transactional
    public void getJobDetails() throws Exception {
        // Initialize the database
        jobDetailsRepository.saveAndFlush(jobDetails);

        // Get the jobDetails
        restJobDetailsMockMvc.perform(get("/api/job-details/{id}", jobDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(jobDetails.getId().intValue()))
            .andExpect(jsonPath("$.programmId").value(DEFAULT_PROGRAMM_ID.intValue()))
            .andExpect(jsonPath("$.jobName").value(DEFAULT_JOB_NAME.toString()))
            .andExpect(jsonPath("$.jobDescription").value(DEFAULT_JOB_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.enable").value(DEFAULT_ENABLE.booleanValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.parameterArguments1").value(DEFAULT_PARAMETER_ARGUMENTS_1.toString()))
            .andExpect(jsonPath("$.parameterArguments2").value(DEFAULT_PARAMETER_ARGUMENTS_2.toString()))
            .andExpect(jsonPath("$.parameterArguments3").value(DEFAULT_PARAMETER_ARGUMENTS_3.toString()))
            .andExpect(jsonPath("$.parameterArguments4").value(DEFAULT_PARAMETER_ARGUMENTS_4.toString()))
            .andExpect(jsonPath("$.parameterArguments5").value(DEFAULT_PARAMETER_ARGUMENTS_5.toString()))
            .andExpect(jsonPath("$.parameterArguments6").value(DEFAULT_PARAMETER_ARGUMENTS_6.toString()))
            .andExpect(jsonPath("$.parameterArguments7").value(DEFAULT_PARAMETER_ARGUMENTS_7.toString()))
            .andExpect(jsonPath("$.parameterArguments8").value(DEFAULT_PARAMETER_ARGUMENTS_8.toString()))
            .andExpect(jsonPath("$.parameterArguments9").value(DEFAULT_PARAMETER_ARGUMENTS_9.toString()))
            .andExpect(jsonPath("$.parameterArguments10").value(DEFAULT_PARAMETER_ARGUMENTS_10.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.creationDate").value(sameInstant(DEFAULT_CREATION_DATE)))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingJobDetails() throws Exception {
        // Get the jobDetails
        restJobDetailsMockMvc.perform(get("/api/job-details/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJobDetails() throws Exception {
        // Initialize the database
        jobDetailsRepository.saveAndFlush(jobDetails);
        jobDetailsSearchRepository.save(jobDetails);
        int databaseSizeBeforeUpdate = jobDetailsRepository.findAll().size();

        // Update the jobDetails
        JobDetails updatedJobDetails = jobDetailsRepository.findOne(jobDetails.getId());
        updatedJobDetails
            .programmId(UPDATED_PROGRAMM_ID)
            .jobName(UPDATED_JOB_NAME)
            .jobDescription(UPDATED_JOB_DESCRIPTION)
            .enable(UPDATED_ENABLE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .parameterArguments1(UPDATED_PARAMETER_ARGUMENTS_1)
            .parameterArguments2(UPDATED_PARAMETER_ARGUMENTS_2)
            .parameterArguments3(UPDATED_PARAMETER_ARGUMENTS_3)
            .parameterArguments4(UPDATED_PARAMETER_ARGUMENTS_4)
            .parameterArguments5(UPDATED_PARAMETER_ARGUMENTS_5)
            .parameterArguments6(UPDATED_PARAMETER_ARGUMENTS_6)
            .parameterArguments7(UPDATED_PARAMETER_ARGUMENTS_7)
            .parameterArguments8(UPDATED_PARAMETER_ARGUMENTS_8)
            .parameterArguments9(UPDATED_PARAMETER_ARGUMENTS_9)
            .parameterArguments10(UPDATED_PARAMETER_ARGUMENTS_10)
            .createdBy(UPDATED_CREATED_BY)
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE)
            .tenantId(UPDATED_TENANT_ID);

        restJobDetailsMockMvc.perform(put("/api/job-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedJobDetails)))
            .andExpect(status().isOk());

        // Validate the JobDetails in the database
        List<JobDetails> jobDetailsList = jobDetailsRepository.findAll();
        assertThat(jobDetailsList).hasSize(databaseSizeBeforeUpdate);
        JobDetails testJobDetails = jobDetailsList.get(jobDetailsList.size() - 1);
        assertThat(testJobDetails.getProgrammId()).isEqualTo(UPDATED_PROGRAMM_ID);
        assertThat(testJobDetails.getJobName()).isEqualTo(UPDATED_JOB_NAME);
        assertThat(testJobDetails.getJobDescription()).isEqualTo(UPDATED_JOB_DESCRIPTION);
        assertThat(testJobDetails.isEnable()).isEqualTo(UPDATED_ENABLE);
        assertThat(testJobDetails.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testJobDetails.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testJobDetails.getParameterArguments1()).isEqualTo(UPDATED_PARAMETER_ARGUMENTS_1);
        assertThat(testJobDetails.getParameterArguments2()).isEqualTo(UPDATED_PARAMETER_ARGUMENTS_2);
        assertThat(testJobDetails.getParameterArguments3()).isEqualTo(UPDATED_PARAMETER_ARGUMENTS_3);
        assertThat(testJobDetails.getParameterArguments4()).isEqualTo(UPDATED_PARAMETER_ARGUMENTS_4);
        assertThat(testJobDetails.getParameterArguments5()).isEqualTo(UPDATED_PARAMETER_ARGUMENTS_5);
        assertThat(testJobDetails.getParameterArguments6()).isEqualTo(UPDATED_PARAMETER_ARGUMENTS_6);
        assertThat(testJobDetails.getParameterArguments7()).isEqualTo(UPDATED_PARAMETER_ARGUMENTS_7);
        assertThat(testJobDetails.getParameterArguments8()).isEqualTo(UPDATED_PARAMETER_ARGUMENTS_8);
        assertThat(testJobDetails.getParameterArguments9()).isEqualTo(UPDATED_PARAMETER_ARGUMENTS_9);
        assertThat(testJobDetails.getParameterArguments10()).isEqualTo(UPDATED_PARAMETER_ARGUMENTS_10);
        assertThat(testJobDetails.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testJobDetails.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testJobDetails.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testJobDetails.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);
        assertThat(testJobDetails.getTenantId()).isEqualTo(UPDATED_TENANT_ID);

        // Validate the JobDetails in Elasticsearch
        JobDetails jobDetailsEs = jobDetailsSearchRepository.findOne(testJobDetails.getId());
        assertThat(jobDetailsEs).isEqualToComparingFieldByField(testJobDetails);
    }

    @Test
    @Transactional
    public void updateNonExistingJobDetails() throws Exception {
        int databaseSizeBeforeUpdate = jobDetailsRepository.findAll().size();

        // Create the JobDetails

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restJobDetailsMockMvc.perform(put("/api/job-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobDetails)))
            .andExpect(status().isCreated());

        // Validate the JobDetails in the database
        List<JobDetails> jobDetailsList = jobDetailsRepository.findAll();
        assertThat(jobDetailsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteJobDetails() throws Exception {
        // Initialize the database
        jobDetailsRepository.saveAndFlush(jobDetails);
        jobDetailsSearchRepository.save(jobDetails);
        int databaseSizeBeforeDelete = jobDetailsRepository.findAll().size();

        // Get the jobDetails
        restJobDetailsMockMvc.perform(delete("/api/job-details/{id}", jobDetails.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean jobDetailsExistsInEs = jobDetailsSearchRepository.exists(jobDetails.getId());
        assertThat(jobDetailsExistsInEs).isFalse();

        // Validate the database is empty
        List<JobDetails> jobDetailsList = jobDetailsRepository.findAll();
        assertThat(jobDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchJobDetails() throws Exception {
        // Initialize the database
        jobDetailsRepository.saveAndFlush(jobDetails);
        jobDetailsSearchRepository.save(jobDetails);

        // Search the jobDetails
        restJobDetailsMockMvc.perform(get("/api/_search/job-details?query=id:" + jobDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].programmId").value(hasItem(DEFAULT_PROGRAMM_ID.intValue())))
            .andExpect(jsonPath("$.[*].jobName").value(hasItem(DEFAULT_JOB_NAME.toString())))
            .andExpect(jsonPath("$.[*].jobDescription").value(hasItem(DEFAULT_JOB_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].enable").value(hasItem(DEFAULT_ENABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].parameterArguments1").value(hasItem(DEFAULT_PARAMETER_ARGUMENTS_1.toString())))
            .andExpect(jsonPath("$.[*].parameterArguments2").value(hasItem(DEFAULT_PARAMETER_ARGUMENTS_2.toString())))
            .andExpect(jsonPath("$.[*].parameterArguments3").value(hasItem(DEFAULT_PARAMETER_ARGUMENTS_3.toString())))
            .andExpect(jsonPath("$.[*].parameterArguments4").value(hasItem(DEFAULT_PARAMETER_ARGUMENTS_4.toString())))
            .andExpect(jsonPath("$.[*].parameterArguments5").value(hasItem(DEFAULT_PARAMETER_ARGUMENTS_5.toString())))
            .andExpect(jsonPath("$.[*].parameterArguments6").value(hasItem(DEFAULT_PARAMETER_ARGUMENTS_6.toString())))
            .andExpect(jsonPath("$.[*].parameterArguments7").value(hasItem(DEFAULT_PARAMETER_ARGUMENTS_7.toString())))
            .andExpect(jsonPath("$.[*].parameterArguments8").value(hasItem(DEFAULT_PARAMETER_ARGUMENTS_8.toString())))
            .andExpect(jsonPath("$.[*].parameterArguments9").value(hasItem(DEFAULT_PARAMETER_ARGUMENTS_9.toString())))
            .andExpect(jsonPath("$.[*].parameterArguments10").value(hasItem(DEFAULT_PARAMETER_ARGUMENTS_10.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobDetails.class);
    }
}
