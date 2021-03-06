package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.Processes;
import com.nspl.app.repository.ProcessesRepository;
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
 * Test class for the ProcessesResource REST controller.
 *
 * @see ProcessesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class ProcessesResourceIntTest {

    private static final String DEFAULT_PROCESS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PROCESS_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESC = "AAAAAAAAAA";
    private static final String UPDATED_DESC = "BBBBBBBBBB";

    private static final Long DEFAULT_TENANT_ID = 1L;
    private static final Long UPDATED_TENANT_ID = 2L;

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_ENABLE = false;
    private static final Boolean UPDATED_ENABLE = true;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    @Autowired
    private ProcessesRepository processesRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProcessesMockMvc;

    private Processes processes;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProcessesResource processesResource = new ProcessesResource(processesRepository);
        this.restProcessesMockMvc = MockMvcBuilders.standaloneSetup(processesResource)
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
    public static Processes createEntity(EntityManager em) {
        Processes processes = new Processes()
            .processName(DEFAULT_PROCESS_NAME)
            .desc(DEFAULT_DESC)
            .tenantId(DEFAULT_TENANT_ID)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .enable(DEFAULT_ENABLE)
            .createdDate(DEFAULT_CREATED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY);
        return processes;
    }

    @Before
    public void initTest() {
        processes = createEntity(em);
    }

    @Test
    @Transactional
    public void createProcesses() throws Exception {
        int databaseSizeBeforeCreate = processesRepository.findAll().size();

        // Create the Processes
        restProcessesMockMvc.perform(post("/api/processes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(processes)))
            .andExpect(status().isCreated());

        // Validate the Processes in the database
        List<Processes> processesList = processesRepository.findAll();
        assertThat(processesList).hasSize(databaseSizeBeforeCreate + 1);
        Processes testProcesses = processesList.get(processesList.size() - 1);
        assertThat(testProcesses.getProcessName()).isEqualTo(DEFAULT_PROCESS_NAME);
        assertThat(testProcesses.getDesc()).isEqualTo(DEFAULT_DESC);
        assertThat(testProcesses.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testProcesses.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testProcesses.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testProcesses.isEnable()).isEqualTo(DEFAULT_ENABLE);
        assertThat(testProcesses.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testProcesses.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testProcesses.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);
        assertThat(testProcesses.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
    }

    @Test
    @Transactional
    public void createProcessesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = processesRepository.findAll().size();

        // Create the Processes with an existing ID
        processes.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProcessesMockMvc.perform(post("/api/processes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(processes)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Processes> processesList = processesRepository.findAll();
        assertThat(processesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllProcesses() throws Exception {
        // Initialize the database
        processesRepository.saveAndFlush(processes);

        // Get all the processesList
        restProcessesMockMvc.perform(get("/api/processes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(processes.getId().intValue())))
            .andExpect(jsonPath("$.[*].processName").value(hasItem(DEFAULT_PROCESS_NAME.toString())))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC.toString())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].enable").value(hasItem(DEFAULT_ENABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())));
    }

    @Test
    @Transactional
    public void getProcesses() throws Exception {
        // Initialize the database
        processesRepository.saveAndFlush(processes);

        // Get the processes
        restProcessesMockMvc.perform(get("/api/processes/{id}", processes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(processes.getId().intValue()))
            .andExpect(jsonPath("$.processName").value(DEFAULT_PROCESS_NAME.toString()))
            .andExpect(jsonPath("$.desc").value(DEFAULT_DESC.toString()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID.intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.enable").value(DEFAULT_ENABLE.booleanValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingProcesses() throws Exception {
        // Get the processes
        restProcessesMockMvc.perform(get("/api/processes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProcesses() throws Exception {
        // Initialize the database
        processesRepository.saveAndFlush(processes);
        int databaseSizeBeforeUpdate = processesRepository.findAll().size();

        // Update the processes
        Processes updatedProcesses = processesRepository.findOne(processes.getId());
        updatedProcesses
            .processName(UPDATED_PROCESS_NAME)
            .desc(UPDATED_DESC)
            .tenantId(UPDATED_TENANT_ID)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .enable(UPDATED_ENABLE)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY);

        restProcessesMockMvc.perform(put("/api/processes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProcesses)))
            .andExpect(status().isOk());

        // Validate the Processes in the database
        List<Processes> processesList = processesRepository.findAll();
        assertThat(processesList).hasSize(databaseSizeBeforeUpdate);
        Processes testProcesses = processesList.get(processesList.size() - 1);
        assertThat(testProcesses.getProcessName()).isEqualTo(UPDATED_PROCESS_NAME);
        assertThat(testProcesses.getDesc()).isEqualTo(UPDATED_DESC);
        assertThat(testProcesses.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testProcesses.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testProcesses.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testProcesses.isEnable()).isEqualTo(UPDATED_ENABLE);
        assertThat(testProcesses.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testProcesses.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testProcesses.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);
        assertThat(testProcesses.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
    }

    @Test
    @Transactional
    public void updateNonExistingProcesses() throws Exception {
        int databaseSizeBeforeUpdate = processesRepository.findAll().size();

        // Create the Processes

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProcessesMockMvc.perform(put("/api/processes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(processes)))
            .andExpect(status().isCreated());

        // Validate the Processes in the database
        List<Processes> processesList = processesRepository.findAll();
        assertThat(processesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProcesses() throws Exception {
        // Initialize the database
        processesRepository.saveAndFlush(processes);
        int databaseSizeBeforeDelete = processesRepository.findAll().size();

        // Get the processes
        restProcessesMockMvc.perform(delete("/api/processes/{id}", processes.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Processes> processesList = processesRepository.findAll();
        assertThat(processesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Processes.class);
        Processes processes1 = new Processes();
        processes1.setId(1L);
        Processes processes2 = new Processes();
        processes2.setId(processes1.getId());
        assertThat(processes1).isEqualTo(processes2);
        processes2.setId(2L);
        assertThat(processes1).isNotEqualTo(processes2);
        processes1.setId(null);
        assertThat(processes1).isNotEqualTo(processes2);
    }
}
