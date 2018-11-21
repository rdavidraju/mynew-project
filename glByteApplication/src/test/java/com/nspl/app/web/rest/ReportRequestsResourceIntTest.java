package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.ReportRequests;
import com.nspl.app.repository.ReportRequestsRepository;
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
 * Test class for the ReportRequestsResource REST controller.
 *
 * @see ReportRequestsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class ReportRequestsResourceIntTest {

    private static final String DEFAULT_REQ_NAME = "AAAAAAAAAA";
    private static final String UPDATED_REQ_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_REPORT_ID = 1L;
    private static final Long UPDATED_REPORT_ID = 2L;

    private static final String DEFAULT_OUTPUT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_OUTPUT_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_TENANT_ID = 1L;
    private static final Long UPDATED_TENANT_ID = 2L;

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private ReportRequestsRepository reportRequestsRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restReportRequestsMockMvc;

    private ReportRequests reportRequests;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReportRequestsResource reportRequestsResource = new ReportRequestsResource(reportRequestsRepository);
        this.restReportRequestsMockMvc = MockMvcBuilders.standaloneSetup(reportRequestsResource)
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
    public static ReportRequests createEntity(EntityManager em) {
        ReportRequests reportRequests = new ReportRequests()
            .reqName(DEFAULT_REQ_NAME)
            .reportId(DEFAULT_REPORT_ID)
            .outputPath(DEFAULT_OUTPUT_PATH)
            .fileName(DEFAULT_FILE_NAME)
            .tenantId(DEFAULT_TENANT_ID)
            .status(DEFAULT_STATUS)
            .createdDate(DEFAULT_CREATED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE);
        return reportRequests;
    }

    @Before
    public void initTest() {
        reportRequests = createEntity(em);
    }

    @Test
    @Transactional
    public void createReportRequests() throws Exception {
        int databaseSizeBeforeCreate = reportRequestsRepository.findAll().size();

        // Create the ReportRequests
        restReportRequestsMockMvc.perform(post("/api/report-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportRequests)))
            .andExpect(status().isCreated());

        // Validate the ReportRequests in the database
        List<ReportRequests> reportRequestsList = reportRequestsRepository.findAll();
        assertThat(reportRequestsList).hasSize(databaseSizeBeforeCreate + 1);
        ReportRequests testReportRequests = reportRequestsList.get(reportRequestsList.size() - 1);
        assertThat(testReportRequests.getReqName()).isEqualTo(DEFAULT_REQ_NAME);
        assertThat(testReportRequests.getReportId()).isEqualTo(DEFAULT_REPORT_ID);
        assertThat(testReportRequests.getOutputPath()).isEqualTo(DEFAULT_OUTPUT_PATH);
        assertThat(testReportRequests.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testReportRequests.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testReportRequests.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testReportRequests.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testReportRequests.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testReportRequests.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testReportRequests.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void createReportRequestsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reportRequestsRepository.findAll().size();

        // Create the ReportRequests with an existing ID
        reportRequests.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportRequestsMockMvc.perform(post("/api/report-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportRequests)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ReportRequests> reportRequestsList = reportRequestsRepository.findAll();
        assertThat(reportRequestsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllReportRequests() throws Exception {
        // Initialize the database
        reportRequestsRepository.saveAndFlush(reportRequests);

        // Get all the reportRequestsList
        restReportRequestsMockMvc.perform(get("/api/report-requests?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportRequests.getId().intValue())))
            .andExpect(jsonPath("$.[*].reqName").value(hasItem(DEFAULT_REQ_NAME.toString())))
            .andExpect(jsonPath("$.[*].reportId").value(hasItem(DEFAULT_REPORT_ID.intValue())))
            .andExpect(jsonPath("$.[*].outputPath").value(hasItem(DEFAULT_OUTPUT_PATH.toString())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME.toString())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void getReportRequests() throws Exception {
        // Initialize the database
        reportRequestsRepository.saveAndFlush(reportRequests);

        // Get the reportRequests
        restReportRequestsMockMvc.perform(get("/api/report-requests/{id}", reportRequests.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(reportRequests.getId().intValue()))
            .andExpect(jsonPath("$.reqName").value(DEFAULT_REQ_NAME.toString()))
            .andExpect(jsonPath("$.reportId").value(DEFAULT_REPORT_ID.intValue()))
            .andExpect(jsonPath("$.outputPath").value(DEFAULT_OUTPUT_PATH.toString()))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME.toString()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingReportRequests() throws Exception {
        // Get the reportRequests
        restReportRequestsMockMvc.perform(get("/api/report-requests/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReportRequests() throws Exception {
        // Initialize the database
        reportRequestsRepository.saveAndFlush(reportRequests);
        int databaseSizeBeforeUpdate = reportRequestsRepository.findAll().size();

        // Update the reportRequests
        ReportRequests updatedReportRequests = reportRequestsRepository.findOne(reportRequests.getId());
        updatedReportRequests
            .reqName(UPDATED_REQ_NAME)
            .reportId(UPDATED_REPORT_ID)
            .outputPath(UPDATED_OUTPUT_PATH)
            .fileName(UPDATED_FILE_NAME)
            .tenantId(UPDATED_TENANT_ID)
            .status(UPDATED_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE);

        restReportRequestsMockMvc.perform(put("/api/report-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedReportRequests)))
            .andExpect(status().isOk());

        // Validate the ReportRequests in the database
        List<ReportRequests> reportRequestsList = reportRequestsRepository.findAll();
        assertThat(reportRequestsList).hasSize(databaseSizeBeforeUpdate);
        ReportRequests testReportRequests = reportRequestsList.get(reportRequestsList.size() - 1);
        assertThat(testReportRequests.getReqName()).isEqualTo(UPDATED_REQ_NAME);
        assertThat(testReportRequests.getReportId()).isEqualTo(UPDATED_REPORT_ID);
        assertThat(testReportRequests.getOutputPath()).isEqualTo(UPDATED_OUTPUT_PATH);
        assertThat(testReportRequests.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testReportRequests.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testReportRequests.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testReportRequests.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testReportRequests.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testReportRequests.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testReportRequests.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingReportRequests() throws Exception {
        int databaseSizeBeforeUpdate = reportRequestsRepository.findAll().size();

        // Create the ReportRequests

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restReportRequestsMockMvc.perform(put("/api/report-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportRequests)))
            .andExpect(status().isCreated());

        // Validate the ReportRequests in the database
        List<ReportRequests> reportRequestsList = reportRequestsRepository.findAll();
        assertThat(reportRequestsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteReportRequests() throws Exception {
        // Initialize the database
        reportRequestsRepository.saveAndFlush(reportRequests);
        int databaseSizeBeforeDelete = reportRequestsRepository.findAll().size();

        // Get the reportRequests
        restReportRequestsMockMvc.perform(delete("/api/report-requests/{id}", reportRequests.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ReportRequests> reportRequestsList = reportRequestsRepository.findAll();
        assertThat(reportRequestsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportRequests.class);
        ReportRequests reportRequests1 = new ReportRequests();
        reportRequests1.setId(1L);
        ReportRequests reportRequests2 = new ReportRequests();
        reportRequests2.setId(reportRequests1.getId());
        assertThat(reportRequests1).isEqualTo(reportRequests2);
        reportRequests2.setId(2L);
        assertThat(reportRequests1).isNotEqualTo(reportRequests2);
        reportRequests1.setId(null);
        assertThat(reportRequests1).isNotEqualTo(reportRequests2);
    }
}
