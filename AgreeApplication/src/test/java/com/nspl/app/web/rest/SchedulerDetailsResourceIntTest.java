package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.SchedulerDetails;
import com.nspl.app.repository.SchedulerDetailsRepository;
import com.nspl.app.repository.search.SchedulerDetailsSearchRepository;
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
 * Test class for the SchedulerDetailsResource REST controller.
 *
 * @see SchedulerDetailsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class SchedulerDetailsResourceIntTest {

    private static final Long DEFAULT_JOB_ID = 1L;
    private static final Long UPDATED_JOB_ID = 2L;

    private static final String DEFAULT_FREQUENCY = "AAAAAAAAAA";
    private static final String UPDATED_FREQUENCY = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_START_DATE =  ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE =  ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_FREQUENCY_VALUE = 1;
    private static final Integer UPDATED_FREQUENCY_VALUE = 2;

    private static final String DEFAULT_DAY_OF = "AAAAAAAAAA";
    private static final String UPDATED_DAY_OF = "BBBBBBBBBB";

    private static final String DEFAULT_TIME = "AAAAAAAAAA";
    private static final String UPDATED_TIME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_DATE =  ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

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
    private SchedulerDetailsRepository schedulerDetailsRepository;

    @Autowired
    private SchedulerDetailsSearchRepository schedulerDetailsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSchedulerDetailsMockMvc;

    private SchedulerDetails schedulerDetails;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SchedulerDetailsResource schedulerDetailsResource = new SchedulerDetailsResource(schedulerDetailsRepository, schedulerDetailsSearchRepository);
        this.restSchedulerDetailsMockMvc = MockMvcBuilders.standaloneSetup(schedulerDetailsResource)
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
    public static SchedulerDetails createEntity(EntityManager em) {
        SchedulerDetails schedulerDetails = new SchedulerDetails()
            .jobId(DEFAULT_JOB_ID)
            .frequency(DEFAULT_FREQUENCY)
            .startDate(DEFAULT_START_DATE)
            .frequencyValue(DEFAULT_FREQUENCY_VALUE)
            .dayOf(DEFAULT_DAY_OF)
            .time(DEFAULT_TIME)
            .endDate(DEFAULT_END_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .creationDate(DEFAULT_CREATION_DATE)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE)
            .tenantId(DEFAULT_TENANT_ID);
        return schedulerDetails;
    }

    @Before
    public void initTest() {
        schedulerDetailsSearchRepository.deleteAll();
        schedulerDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createSchedulerDetails() throws Exception {
        int databaseSizeBeforeCreate = schedulerDetailsRepository.findAll().size();

        // Create the SchedulerDetails
        restSchedulerDetailsMockMvc.perform(post("/api/scheduler-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schedulerDetails)))
            .andExpect(status().isCreated());

        // Validate the SchedulerDetails in the database
        List<SchedulerDetails> schedulerDetailsList = schedulerDetailsRepository.findAll();
        assertThat(schedulerDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        SchedulerDetails testSchedulerDetails = schedulerDetailsList.get(schedulerDetailsList.size() - 1);
        assertThat(testSchedulerDetails.getJobId()).isEqualTo(DEFAULT_JOB_ID);
        assertThat(testSchedulerDetails.getFrequency()).isEqualTo(DEFAULT_FREQUENCY);
        assertThat(testSchedulerDetails.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testSchedulerDetails.getFrequencyValue()).isEqualTo(DEFAULT_FREQUENCY_VALUE);
        assertThat(testSchedulerDetails.getDayOf()).isEqualTo(DEFAULT_DAY_OF);
        assertThat(testSchedulerDetails.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testSchedulerDetails.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testSchedulerDetails.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSchedulerDetails.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testSchedulerDetails.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testSchedulerDetails.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);
        assertThat(testSchedulerDetails.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);

        // Validate the SchedulerDetails in Elasticsearch
        SchedulerDetails schedulerDetailsEs = schedulerDetailsSearchRepository.findOne(testSchedulerDetails.getId());
        assertThat(schedulerDetailsEs).isEqualToComparingFieldByField(testSchedulerDetails);
    }

    @Test
    @Transactional
    public void createSchedulerDetailsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = schedulerDetailsRepository.findAll().size();

        // Create the SchedulerDetails with an existing ID
        schedulerDetails.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSchedulerDetailsMockMvc.perform(post("/api/scheduler-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schedulerDetails)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<SchedulerDetails> schedulerDetailsList = schedulerDetailsRepository.findAll();
        assertThat(schedulerDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSchedulerDetails() throws Exception {
        // Initialize the database
        schedulerDetailsRepository.saveAndFlush(schedulerDetails);

        // Get all the schedulerDetailsList
        restSchedulerDetailsMockMvc.perform(get("/api/scheduler-details?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schedulerDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobId").value(hasItem(DEFAULT_JOB_ID.intValue())))
            .andExpect(jsonPath("$.[*].frequency").value(hasItem(DEFAULT_FREQUENCY.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].frequencyValue").value(hasItem(DEFAULT_FREQUENCY_VALUE)))
            .andExpect(jsonPath("$.[*].dayOf").value(hasItem(DEFAULT_DAY_OF.toString())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())));
    }

    @Test
    @Transactional
    public void getSchedulerDetails() throws Exception {
        // Initialize the database
        schedulerDetailsRepository.saveAndFlush(schedulerDetails);

        // Get the schedulerDetails
        restSchedulerDetailsMockMvc.perform(get("/api/scheduler-details/{id}", schedulerDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(schedulerDetails.getId().intValue()))
            .andExpect(jsonPath("$.jobId").value(DEFAULT_JOB_ID.intValue()))
            .andExpect(jsonPath("$.frequency").value(DEFAULT_FREQUENCY.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.frequencyValue").value(DEFAULT_FREQUENCY_VALUE))
            .andExpect(jsonPath("$.dayOf").value(DEFAULT_DAY_OF.toString()))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.creationDate").value(sameInstant(DEFAULT_CREATION_DATE)))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSchedulerDetails() throws Exception {
        // Get the schedulerDetails
        restSchedulerDetailsMockMvc.perform(get("/api/scheduler-details/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSchedulerDetails() throws Exception {
        // Initialize the database
        schedulerDetailsRepository.saveAndFlush(schedulerDetails);
        schedulerDetailsSearchRepository.save(schedulerDetails);
        int databaseSizeBeforeUpdate = schedulerDetailsRepository.findAll().size();

        // Update the schedulerDetails
        SchedulerDetails updatedSchedulerDetails = schedulerDetailsRepository.findOne(schedulerDetails.getId());
        updatedSchedulerDetails
            .jobId(UPDATED_JOB_ID)
            .frequency(UPDATED_FREQUENCY)
            .startDate(UPDATED_START_DATE)
            .frequencyValue(UPDATED_FREQUENCY_VALUE)
            .dayOf(UPDATED_DAY_OF)
            .time(UPDATED_TIME)
            .endDate(UPDATED_END_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE)
            .tenantId(UPDATED_TENANT_ID);

        restSchedulerDetailsMockMvc.perform(put("/api/scheduler-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSchedulerDetails)))
            .andExpect(status().isOk());

        // Validate the SchedulerDetails in the database
        List<SchedulerDetails> schedulerDetailsList = schedulerDetailsRepository.findAll();
        assertThat(schedulerDetailsList).hasSize(databaseSizeBeforeUpdate);
        SchedulerDetails testSchedulerDetails = schedulerDetailsList.get(schedulerDetailsList.size() - 1);
        assertThat(testSchedulerDetails.getJobId()).isEqualTo(UPDATED_JOB_ID);
        assertThat(testSchedulerDetails.getFrequency()).isEqualTo(UPDATED_FREQUENCY);
        assertThat(testSchedulerDetails.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testSchedulerDetails.getFrequencyValue()).isEqualTo(UPDATED_FREQUENCY_VALUE);
        assertThat(testSchedulerDetails.getDayOf()).isEqualTo(UPDATED_DAY_OF);
        assertThat(testSchedulerDetails.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testSchedulerDetails.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testSchedulerDetails.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSchedulerDetails.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testSchedulerDetails.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testSchedulerDetails.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);
        assertThat(testSchedulerDetails.getTenantId()).isEqualTo(UPDATED_TENANT_ID);

        // Validate the SchedulerDetails in Elasticsearch
        SchedulerDetails schedulerDetailsEs = schedulerDetailsSearchRepository.findOne(testSchedulerDetails.getId());
        assertThat(schedulerDetailsEs).isEqualToComparingFieldByField(testSchedulerDetails);
    }

    @Test
    @Transactional
    public void updateNonExistingSchedulerDetails() throws Exception {
        int databaseSizeBeforeUpdate = schedulerDetailsRepository.findAll().size();

        // Create the SchedulerDetails

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSchedulerDetailsMockMvc.perform(put("/api/scheduler-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schedulerDetails)))
            .andExpect(status().isCreated());

        // Validate the SchedulerDetails in the database
        List<SchedulerDetails> schedulerDetailsList = schedulerDetailsRepository.findAll();
        assertThat(schedulerDetailsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSchedulerDetails() throws Exception {
        // Initialize the database
        schedulerDetailsRepository.saveAndFlush(schedulerDetails);
        schedulerDetailsSearchRepository.save(schedulerDetails);
        int databaseSizeBeforeDelete = schedulerDetailsRepository.findAll().size();

        // Get the schedulerDetails
        restSchedulerDetailsMockMvc.perform(delete("/api/scheduler-details/{id}", schedulerDetails.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean schedulerDetailsExistsInEs = schedulerDetailsSearchRepository.exists(schedulerDetails.getId());
        assertThat(schedulerDetailsExistsInEs).isFalse();

        // Validate the database is empty
        List<SchedulerDetails> schedulerDetailsList = schedulerDetailsRepository.findAll();
        assertThat(schedulerDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSchedulerDetails() throws Exception {
        // Initialize the database
        schedulerDetailsRepository.saveAndFlush(schedulerDetails);
        schedulerDetailsSearchRepository.save(schedulerDetails);

        // Search the schedulerDetails
        restSchedulerDetailsMockMvc.perform(get("/api/_search/scheduler-details?query=id:" + schedulerDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schedulerDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobId").value(hasItem(DEFAULT_JOB_ID.intValue())))
            .andExpect(jsonPath("$.[*].frequency").value(hasItem(DEFAULT_FREQUENCY.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].frequencyValue").value(hasItem(DEFAULT_FREQUENCY_VALUE)))
            .andExpect(jsonPath("$.[*].dayOf").value(hasItem(DEFAULT_DAY_OF.toString())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SchedulerDetails.class);
    }
}
