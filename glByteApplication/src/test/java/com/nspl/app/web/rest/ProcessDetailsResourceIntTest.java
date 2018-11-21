package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.ProcessDetails;
import com.nspl.app.repository.ProcessDetailsRepository;
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
 * Test class for the ProcessDetailsResource REST controller.
 *
 * @see ProcessDetailsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class ProcessDetailsResourceIntTest {

    private static final Long DEFAULT_PROCESS_ID = 1L;
    private static final Long UPDATED_PROCESS_ID = 2L;

    private static final String DEFAULT_TAG_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TAG_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_TYPE_ID = 1L;
    private static final Long UPDATED_TYPE_ID = 2L;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    @Autowired
    private ProcessDetailsRepository processDetailsRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProcessDetailsMockMvc;

    private ProcessDetails processDetails;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProcessDetailsResource processDetailsResource = new ProcessDetailsResource(processDetailsRepository);
        this.restProcessDetailsMockMvc = MockMvcBuilders.standaloneSetup(processDetailsResource)
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
    public static ProcessDetails createEntity(EntityManager em) {
        ProcessDetails processDetails = new ProcessDetails()
            .processId(DEFAULT_PROCESS_ID)
            .tagType(DEFAULT_TAG_TYPE)
            .typeId(DEFAULT_TYPE_ID)
            .createdDate(DEFAULT_CREATED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY);
        return processDetails;
    }

    @Before
    public void initTest() {
        processDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createProcessDetails() throws Exception {
        int databaseSizeBeforeCreate = processDetailsRepository.findAll().size();

        // Create the ProcessDetails
        restProcessDetailsMockMvc.perform(post("/api/process-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(processDetails)))
            .andExpect(status().isCreated());

        // Validate the ProcessDetails in the database
        List<ProcessDetails> processDetailsList = processDetailsRepository.findAll();
        assertThat(processDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        ProcessDetails testProcessDetails = processDetailsList.get(processDetailsList.size() - 1);
        assertThat(testProcessDetails.getProcessId()).isEqualTo(DEFAULT_PROCESS_ID);
        assertThat(testProcessDetails.getTagType()).isEqualTo(DEFAULT_TAG_TYPE);
        assertThat(testProcessDetails.getTypeId()).isEqualTo(DEFAULT_TYPE_ID);
        assertThat(testProcessDetails.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testProcessDetails.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testProcessDetails.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);
        assertThat(testProcessDetails.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
    }

    @Test
    @Transactional
    public void createProcessDetailsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = processDetailsRepository.findAll().size();

        // Create the ProcessDetails with an existing ID
        processDetails.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProcessDetailsMockMvc.perform(post("/api/process-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(processDetails)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ProcessDetails> processDetailsList = processDetailsRepository.findAll();
        assertThat(processDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllProcessDetails() throws Exception {
        // Initialize the database
        processDetailsRepository.saveAndFlush(processDetails);

        // Get all the processDetailsList
        restProcessDetailsMockMvc.perform(get("/api/process-details?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(processDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].processId").value(hasItem(DEFAULT_PROCESS_ID.intValue())))
            .andExpect(jsonPath("$.[*].tagType").value(hasItem(DEFAULT_TAG_TYPE.toString())))
            .andExpect(jsonPath("$.[*].typeId").value(hasItem(DEFAULT_TYPE_ID.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())));
    }

    @Test
    @Transactional
    public void getProcessDetails() throws Exception {
        // Initialize the database
        processDetailsRepository.saveAndFlush(processDetails);

        // Get the processDetails
        restProcessDetailsMockMvc.perform(get("/api/process-details/{id}", processDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(processDetails.getId().intValue()))
            .andExpect(jsonPath("$.processId").value(DEFAULT_PROCESS_ID.intValue()))
            .andExpect(jsonPath("$.tagType").value(DEFAULT_TAG_TYPE.toString()))
            .andExpect(jsonPath("$.typeId").value(DEFAULT_TYPE_ID.intValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingProcessDetails() throws Exception {
        // Get the processDetails
        restProcessDetailsMockMvc.perform(get("/api/process-details/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProcessDetails() throws Exception {
        // Initialize the database
        processDetailsRepository.saveAndFlush(processDetails);
        int databaseSizeBeforeUpdate = processDetailsRepository.findAll().size();

        // Update the processDetails
        ProcessDetails updatedProcessDetails = processDetailsRepository.findOne(processDetails.getId());
        updatedProcessDetails
            .processId(UPDATED_PROCESS_ID)
            .tagType(UPDATED_TAG_TYPE)
            .typeId(UPDATED_TYPE_ID)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY);

        restProcessDetailsMockMvc.perform(put("/api/process-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProcessDetails)))
            .andExpect(status().isOk());

        // Validate the ProcessDetails in the database
        List<ProcessDetails> processDetailsList = processDetailsRepository.findAll();
        assertThat(processDetailsList).hasSize(databaseSizeBeforeUpdate);
        ProcessDetails testProcessDetails = processDetailsList.get(processDetailsList.size() - 1);
        assertThat(testProcessDetails.getProcessId()).isEqualTo(UPDATED_PROCESS_ID);
        assertThat(testProcessDetails.getTagType()).isEqualTo(UPDATED_TAG_TYPE);
        assertThat(testProcessDetails.getTypeId()).isEqualTo(UPDATED_TYPE_ID);
        assertThat(testProcessDetails.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testProcessDetails.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testProcessDetails.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);
        assertThat(testProcessDetails.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
    }

    @Test
    @Transactional
    public void updateNonExistingProcessDetails() throws Exception {
        int databaseSizeBeforeUpdate = processDetailsRepository.findAll().size();

        // Create the ProcessDetails

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProcessDetailsMockMvc.perform(put("/api/process-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(processDetails)))
            .andExpect(status().isCreated());

        // Validate the ProcessDetails in the database
        List<ProcessDetails> processDetailsList = processDetailsRepository.findAll();
        assertThat(processDetailsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProcessDetails() throws Exception {
        // Initialize the database
        processDetailsRepository.saveAndFlush(processDetails);
        int databaseSizeBeforeDelete = processDetailsRepository.findAll().size();

        // Get the processDetails
        restProcessDetailsMockMvc.perform(delete("/api/process-details/{id}", processDetails.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ProcessDetails> processDetailsList = processDetailsRepository.findAll();
        assertThat(processDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProcessDetails.class);
        ProcessDetails processDetails1 = new ProcessDetails();
        processDetails1.setId(1L);
        ProcessDetails processDetails2 = new ProcessDetails();
        processDetails2.setId(processDetails1.getId());
        assertThat(processDetails1).isEqualTo(processDetails2);
        processDetails2.setId(2L);
        assertThat(processDetails1).isNotEqualTo(processDetails2);
        processDetails1.setId(null);
        assertThat(processDetails1).isNotEqualTo(processDetails2);
    }
}
