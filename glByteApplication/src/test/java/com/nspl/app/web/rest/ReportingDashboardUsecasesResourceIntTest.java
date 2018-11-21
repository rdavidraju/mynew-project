package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.ReportingDashboardUsecases;
import com.nspl.app.repository.ReportingDashboardUsecasesRepository;
import com.nspl.app.repository.search.ReportingDashboardUsecasesSearchRepository;
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
 * Test class for the ReportingDashboardUsecasesResource REST controller.
 *
 * @see ReportingDashboardUsecasesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class ReportingDashboardUsecasesResourceIntTest {

    private static final Long DEFAULT_DASHBOARD_ID = 1L;
    private static final Long UPDATED_DASHBOARD_ID = 2L;

    private static final Integer DEFAULT_SEQ_NUM = 1;
    private static final Integer UPDATED_SEQ_NUM = 2;

    private static final String DEFAULT_USECASE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USECASE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_REPORT_TEMP_ID = "AAAAAAAAAA";
    private static final String UPDATED_REPORT_TEMP_ID = "BBBBBBBBBB";

    private static final String DEFAULT_OUTPUT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_OUTPUT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_GROUPBY_COL = "AAAAAAAAAA";
    private static final String UPDATED_GROUPBY_COL = "BBBBBBBBBB";

    @Autowired
    private ReportingDashboardUsecasesRepository reportingDashboardUsecasesRepository;

    @Autowired
    private ReportingDashboardUsecasesSearchRepository reportingDashboardUsecasesSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restReportingDashboardUsecasesMockMvc;

    private ReportingDashboardUsecases reportingDashboardUsecases;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReportingDashboardUsecasesResource reportingDashboardUsecasesResource = new ReportingDashboardUsecasesResource(reportingDashboardUsecasesRepository, reportingDashboardUsecasesSearchRepository);
        this.restReportingDashboardUsecasesMockMvc = MockMvcBuilders.standaloneSetup(reportingDashboardUsecasesResource)
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
    public static ReportingDashboardUsecases createEntity(EntityManager em) {
        ReportingDashboardUsecases reportingDashboardUsecases = new ReportingDashboardUsecases()
            .dashboardId(DEFAULT_DASHBOARD_ID)
            .seqNum(DEFAULT_SEQ_NUM)
            .usecaseName(DEFAULT_USECASE_NAME)
            .reportTempId(DEFAULT_REPORT_TEMP_ID)
            .outputType(DEFAULT_OUTPUT_TYPE)
            .groupbyCol(DEFAULT_GROUPBY_COL);
        return reportingDashboardUsecases;
    }

    @Before
    public void initTest() {
        reportingDashboardUsecasesSearchRepository.deleteAll();
        reportingDashboardUsecases = createEntity(em);
    }

    @Test
    @Transactional
    public void createReportingDashboardUsecases() throws Exception {
        int databaseSizeBeforeCreate = reportingDashboardUsecasesRepository.findAll().size();

        // Create the ReportingDashboardUsecases
        restReportingDashboardUsecasesMockMvc.perform(post("/api/reporting-dashboard-usecases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportingDashboardUsecases)))
            .andExpect(status().isCreated());

        // Validate the ReportingDashboardUsecases in the database
        List<ReportingDashboardUsecases> reportingDashboardUsecasesList = reportingDashboardUsecasesRepository.findAll();
        assertThat(reportingDashboardUsecasesList).hasSize(databaseSizeBeforeCreate + 1);
        ReportingDashboardUsecases testReportingDashboardUsecases = reportingDashboardUsecasesList.get(reportingDashboardUsecasesList.size() - 1);
        assertThat(testReportingDashboardUsecases.getDashboardId()).isEqualTo(DEFAULT_DASHBOARD_ID);
        assertThat(testReportingDashboardUsecases.getSeqNum()).isEqualTo(DEFAULT_SEQ_NUM);
        assertThat(testReportingDashboardUsecases.getUsecaseName()).isEqualTo(DEFAULT_USECASE_NAME);
        assertThat(testReportingDashboardUsecases.getReportTempId()).isEqualTo(DEFAULT_REPORT_TEMP_ID);
        assertThat(testReportingDashboardUsecases.getOutputType()).isEqualTo(DEFAULT_OUTPUT_TYPE);
        assertThat(testReportingDashboardUsecases.getGroupbyCol()).isEqualTo(DEFAULT_GROUPBY_COL);

        // Validate the ReportingDashboardUsecases in Elasticsearch
        ReportingDashboardUsecases reportingDashboardUsecasesEs = reportingDashboardUsecasesSearchRepository.findOne(testReportingDashboardUsecases.getId());
        assertThat(reportingDashboardUsecasesEs).isEqualToComparingFieldByField(testReportingDashboardUsecases);
    }

    @Test
    @Transactional
    public void createReportingDashboardUsecasesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reportingDashboardUsecasesRepository.findAll().size();

        // Create the ReportingDashboardUsecases with an existing ID
        reportingDashboardUsecases.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportingDashboardUsecasesMockMvc.perform(post("/api/reporting-dashboard-usecases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportingDashboardUsecases)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ReportingDashboardUsecases> reportingDashboardUsecasesList = reportingDashboardUsecasesRepository.findAll();
        assertThat(reportingDashboardUsecasesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllReportingDashboardUsecases() throws Exception {
        // Initialize the database
        reportingDashboardUsecasesRepository.saveAndFlush(reportingDashboardUsecases);

        // Get all the reportingDashboardUsecasesList
        restReportingDashboardUsecasesMockMvc.perform(get("/api/reporting-dashboard-usecases?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportingDashboardUsecases.getId().intValue())))
            .andExpect(jsonPath("$.[*].dashboardId").value(hasItem(DEFAULT_DASHBOARD_ID.intValue())))
            .andExpect(jsonPath("$.[*].seqNum").value(hasItem(DEFAULT_SEQ_NUM)))
            .andExpect(jsonPath("$.[*].usecaseName").value(hasItem(DEFAULT_USECASE_NAME.toString())))
            .andExpect(jsonPath("$.[*].reportTempId").value(hasItem(DEFAULT_REPORT_TEMP_ID.toString())))
            .andExpect(jsonPath("$.[*].outputType").value(hasItem(DEFAULT_OUTPUT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].groupbyCol").value(hasItem(DEFAULT_GROUPBY_COL.toString())));
    }

    @Test
    @Transactional
    public void getReportingDashboardUsecases() throws Exception {
        // Initialize the database
        reportingDashboardUsecasesRepository.saveAndFlush(reportingDashboardUsecases);

        // Get the reportingDashboardUsecases
        restReportingDashboardUsecasesMockMvc.perform(get("/api/reporting-dashboard-usecases/{id}", reportingDashboardUsecases.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(reportingDashboardUsecases.getId().intValue()))
            .andExpect(jsonPath("$.dashboardId").value(DEFAULT_DASHBOARD_ID.intValue()))
            .andExpect(jsonPath("$.seqNum").value(DEFAULT_SEQ_NUM))
            .andExpect(jsonPath("$.usecaseName").value(DEFAULT_USECASE_NAME.toString()))
            .andExpect(jsonPath("$.reportTempId").value(DEFAULT_REPORT_TEMP_ID.toString()))
            .andExpect(jsonPath("$.outputType").value(DEFAULT_OUTPUT_TYPE.toString()))
            .andExpect(jsonPath("$.groupbyCol").value(DEFAULT_GROUPBY_COL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingReportingDashboardUsecases() throws Exception {
        // Get the reportingDashboardUsecases
        restReportingDashboardUsecasesMockMvc.perform(get("/api/reporting-dashboard-usecases/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReportingDashboardUsecases() throws Exception {
        // Initialize the database
        reportingDashboardUsecasesRepository.saveAndFlush(reportingDashboardUsecases);
        reportingDashboardUsecasesSearchRepository.save(reportingDashboardUsecases);
        int databaseSizeBeforeUpdate = reportingDashboardUsecasesRepository.findAll().size();

        // Update the reportingDashboardUsecases
        ReportingDashboardUsecases updatedReportingDashboardUsecases = reportingDashboardUsecasesRepository.findOne(reportingDashboardUsecases.getId());
        updatedReportingDashboardUsecases
            .dashboardId(UPDATED_DASHBOARD_ID)
            .seqNum(UPDATED_SEQ_NUM)
            .usecaseName(UPDATED_USECASE_NAME)
            .reportTempId(UPDATED_REPORT_TEMP_ID)
            .outputType(UPDATED_OUTPUT_TYPE)
            .groupbyCol(UPDATED_GROUPBY_COL);

        restReportingDashboardUsecasesMockMvc.perform(put("/api/reporting-dashboard-usecases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedReportingDashboardUsecases)))
            .andExpect(status().isOk());

        // Validate the ReportingDashboardUsecases in the database
        List<ReportingDashboardUsecases> reportingDashboardUsecasesList = reportingDashboardUsecasesRepository.findAll();
        assertThat(reportingDashboardUsecasesList).hasSize(databaseSizeBeforeUpdate);
        ReportingDashboardUsecases testReportingDashboardUsecases = reportingDashboardUsecasesList.get(reportingDashboardUsecasesList.size() - 1);
        assertThat(testReportingDashboardUsecases.getDashboardId()).isEqualTo(UPDATED_DASHBOARD_ID);
        assertThat(testReportingDashboardUsecases.getSeqNum()).isEqualTo(UPDATED_SEQ_NUM);
        assertThat(testReportingDashboardUsecases.getUsecaseName()).isEqualTo(UPDATED_USECASE_NAME);
        assertThat(testReportingDashboardUsecases.getReportTempId()).isEqualTo(UPDATED_REPORT_TEMP_ID);
        assertThat(testReportingDashboardUsecases.getOutputType()).isEqualTo(UPDATED_OUTPUT_TYPE);
        assertThat(testReportingDashboardUsecases.getGroupbyCol()).isEqualTo(UPDATED_GROUPBY_COL);

        // Validate the ReportingDashboardUsecases in Elasticsearch
        ReportingDashboardUsecases reportingDashboardUsecasesEs = reportingDashboardUsecasesSearchRepository.findOne(testReportingDashboardUsecases.getId());
        assertThat(reportingDashboardUsecasesEs).isEqualToComparingFieldByField(testReportingDashboardUsecases);
    }

    @Test
    @Transactional
    public void updateNonExistingReportingDashboardUsecases() throws Exception {
        int databaseSizeBeforeUpdate = reportingDashboardUsecasesRepository.findAll().size();

        // Create the ReportingDashboardUsecases

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restReportingDashboardUsecasesMockMvc.perform(put("/api/reporting-dashboard-usecases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportingDashboardUsecases)))
            .andExpect(status().isCreated());

        // Validate the ReportingDashboardUsecases in the database
        List<ReportingDashboardUsecases> reportingDashboardUsecasesList = reportingDashboardUsecasesRepository.findAll();
        assertThat(reportingDashboardUsecasesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteReportingDashboardUsecases() throws Exception {
        // Initialize the database
        reportingDashboardUsecasesRepository.saveAndFlush(reportingDashboardUsecases);
        reportingDashboardUsecasesSearchRepository.save(reportingDashboardUsecases);
        int databaseSizeBeforeDelete = reportingDashboardUsecasesRepository.findAll().size();

        // Get the reportingDashboardUsecases
        restReportingDashboardUsecasesMockMvc.perform(delete("/api/reporting-dashboard-usecases/{id}", reportingDashboardUsecases.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean reportingDashboardUsecasesExistsInEs = reportingDashboardUsecasesSearchRepository.exists(reportingDashboardUsecases.getId());
        assertThat(reportingDashboardUsecasesExistsInEs).isFalse();

        // Validate the database is empty
        List<ReportingDashboardUsecases> reportingDashboardUsecasesList = reportingDashboardUsecasesRepository.findAll();
        assertThat(reportingDashboardUsecasesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchReportingDashboardUsecases() throws Exception {
        // Initialize the database
        reportingDashboardUsecasesRepository.saveAndFlush(reportingDashboardUsecases);
        reportingDashboardUsecasesSearchRepository.save(reportingDashboardUsecases);

        // Search the reportingDashboardUsecases
        restReportingDashboardUsecasesMockMvc.perform(get("/api/_search/reporting-dashboard-usecases?query=id:" + reportingDashboardUsecases.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportingDashboardUsecases.getId().intValue())))
            .andExpect(jsonPath("$.[*].dashboardId").value(hasItem(DEFAULT_DASHBOARD_ID.intValue())))
            .andExpect(jsonPath("$.[*].seqNum").value(hasItem(DEFAULT_SEQ_NUM)))
            .andExpect(jsonPath("$.[*].usecaseName").value(hasItem(DEFAULT_USECASE_NAME.toString())))
            .andExpect(jsonPath("$.[*].reportTempId").value(hasItem(DEFAULT_REPORT_TEMP_ID.toString())))
            .andExpect(jsonPath("$.[*].outputType").value(hasItem(DEFAULT_OUTPUT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].groupbyCol").value(hasItem(DEFAULT_GROUPBY_COL.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportingDashboardUsecases.class);
        ReportingDashboardUsecases reportingDashboardUsecases1 = new ReportingDashboardUsecases();
        reportingDashboardUsecases1.setId(1L);
        ReportingDashboardUsecases reportingDashboardUsecases2 = new ReportingDashboardUsecases();
        reportingDashboardUsecases2.setId(reportingDashboardUsecases1.getId());
        assertThat(reportingDashboardUsecases1).isEqualTo(reportingDashboardUsecases2);
        reportingDashboardUsecases2.setId(2L);
        assertThat(reportingDashboardUsecases1).isNotEqualTo(reportingDashboardUsecases2);
        reportingDashboardUsecases1.setId(null);
        assertThat(reportingDashboardUsecases1).isNotEqualTo(reportingDashboardUsecases2);
    }
}
