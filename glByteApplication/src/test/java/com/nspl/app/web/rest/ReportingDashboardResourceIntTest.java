package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.ReportingDashboard;
import com.nspl.app.repository.ReportingDashboardRepository;
import com.nspl.app.repository.search.ReportingDashboardSearchRepository;
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
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ReportingDashboardResource REST controller.
 *
 * @see ReportingDashboardResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class ReportingDashboardResourceIntTest {

    private static final Long DEFAULT_TENANT_ID = 1L;
    private static final Long UPDATED_TENANT_ID = 2L;

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DEFAULT_FLAG = false;
    private static final Boolean UPDATED_DEFAULT_FLAG = true;

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_UPDATED_BY = 1L;
    private static final Long UPDATED_UPDATED_BY = 2L;

    private static final LocalDate DEFAULT_UDATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private ReportingDashboardRepository reportingDashboardRepository;

    @Autowired
    private ReportingDashboardSearchRepository reportingDashboardSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restReportingDashboardMockMvc;

    private ReportingDashboard reportingDashboard;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReportingDashboardResource reportingDashboardResource = new ReportingDashboardResource(reportingDashboardRepository, reportingDashboardSearchRepository);
        this.restReportingDashboardMockMvc = MockMvcBuilders.standaloneSetup(reportingDashboardResource)
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
    public static ReportingDashboard createEntity(EntityManager em) {
        ReportingDashboard reportingDashboard = new ReportingDashboard()
            .tenantId(DEFAULT_TENANT_ID)
            .userId(DEFAULT_USER_ID)
            .name(DEFAULT_NAME)
            .defaultFlag(DEFAULT_DEFAULT_FLAG)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .updatedBy(DEFAULT_UPDATED_BY)
            .udatedDate(DEFAULT_UDATED_DATE);
        return reportingDashboard;
    }

    @Before
    public void initTest() {
        reportingDashboardSearchRepository.deleteAll();
        reportingDashboard = createEntity(em);
    }

    @Test
    @Transactional
    public void createReportingDashboard() throws Exception {
        int databaseSizeBeforeCreate = reportingDashboardRepository.findAll().size();

        // Create the ReportingDashboard
        restReportingDashboardMockMvc.perform(post("/api/reporting-dashboards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportingDashboard)))
            .andExpect(status().isCreated());

        // Validate the ReportingDashboard in the database
        List<ReportingDashboard> reportingDashboardList = reportingDashboardRepository.findAll();
        assertThat(reportingDashboardList).hasSize(databaseSizeBeforeCreate + 1);
        ReportingDashboard testReportingDashboard = reportingDashboardList.get(reportingDashboardList.size() - 1);
        assertThat(testReportingDashboard.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testReportingDashboard.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testReportingDashboard.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testReportingDashboard.isDefaultFlag()).isEqualTo(DEFAULT_DEFAULT_FLAG);
        assertThat(testReportingDashboard.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testReportingDashboard.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testReportingDashboard.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testReportingDashboard.getUdatedDate()).isEqualTo(DEFAULT_UDATED_DATE);

        // Validate the ReportingDashboard in Elasticsearch
        ReportingDashboard reportingDashboardEs = reportingDashboardSearchRepository.findOne(testReportingDashboard.getId());
        assertThat(reportingDashboardEs).isEqualToComparingFieldByField(testReportingDashboard);
    }

    @Test
    @Transactional
    public void createReportingDashboardWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reportingDashboardRepository.findAll().size();

        // Create the ReportingDashboard with an existing ID
        reportingDashboard.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportingDashboardMockMvc.perform(post("/api/reporting-dashboards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportingDashboard)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ReportingDashboard> reportingDashboardList = reportingDashboardRepository.findAll();
        assertThat(reportingDashboardList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllReportingDashboards() throws Exception {
        // Initialize the database
        reportingDashboardRepository.saveAndFlush(reportingDashboard);

        // Get all the reportingDashboardList
        restReportingDashboardMockMvc.perform(get("/api/reporting-dashboards?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportingDashboard.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].defaultFlag").value(hasItem(DEFAULT_DEFAULT_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].udatedDate").value(hasItem(DEFAULT_UDATED_DATE.toString())));
    }

    @Test
    @Transactional
    public void getReportingDashboard() throws Exception {
        // Initialize the database
        reportingDashboardRepository.saveAndFlush(reportingDashboard);

        // Get the reportingDashboard
        restReportingDashboardMockMvc.perform(get("/api/reporting-dashboards/{id}", reportingDashboard.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(reportingDashboard.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID.intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.defaultFlag").value(DEFAULT_DEFAULT_FLAG.booleanValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.udatedDate").value(DEFAULT_UDATED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingReportingDashboard() throws Exception {
        // Get the reportingDashboard
        restReportingDashboardMockMvc.perform(get("/api/reporting-dashboards/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReportingDashboard() throws Exception {
        // Initialize the database
        reportingDashboardRepository.saveAndFlush(reportingDashboard);
        reportingDashboardSearchRepository.save(reportingDashboard);
        int databaseSizeBeforeUpdate = reportingDashboardRepository.findAll().size();

        // Update the reportingDashboard
        ReportingDashboard updatedReportingDashboard = reportingDashboardRepository.findOne(reportingDashboard.getId());
        updatedReportingDashboard
            .tenantId(UPDATED_TENANT_ID)
            .userId(UPDATED_USER_ID)
            .name(UPDATED_NAME)
            .defaultFlag(UPDATED_DEFAULT_FLAG)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .updatedBy(UPDATED_UPDATED_BY)
            .udatedDate(UPDATED_UDATED_DATE);

        restReportingDashboardMockMvc.perform(put("/api/reporting-dashboards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedReportingDashboard)))
            .andExpect(status().isOk());

        // Validate the ReportingDashboard in the database
        List<ReportingDashboard> reportingDashboardList = reportingDashboardRepository.findAll();
        assertThat(reportingDashboardList).hasSize(databaseSizeBeforeUpdate);
        ReportingDashboard testReportingDashboard = reportingDashboardList.get(reportingDashboardList.size() - 1);
        assertThat(testReportingDashboard.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testReportingDashboard.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testReportingDashboard.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testReportingDashboard.isDefaultFlag()).isEqualTo(UPDATED_DEFAULT_FLAG);
        assertThat(testReportingDashboard.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testReportingDashboard.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testReportingDashboard.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testReportingDashboard.getUdatedDate()).isEqualTo(UPDATED_UDATED_DATE);

        // Validate the ReportingDashboard in Elasticsearch
        ReportingDashboard reportingDashboardEs = reportingDashboardSearchRepository.findOne(testReportingDashboard.getId());
        assertThat(reportingDashboardEs).isEqualToComparingFieldByField(testReportingDashboard);
    }

    @Test
    @Transactional
    public void updateNonExistingReportingDashboard() throws Exception {
        int databaseSizeBeforeUpdate = reportingDashboardRepository.findAll().size();

        // Create the ReportingDashboard

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restReportingDashboardMockMvc.perform(put("/api/reporting-dashboards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportingDashboard)))
            .andExpect(status().isCreated());

        // Validate the ReportingDashboard in the database
        List<ReportingDashboard> reportingDashboardList = reportingDashboardRepository.findAll();
        assertThat(reportingDashboardList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteReportingDashboard() throws Exception {
        // Initialize the database
        reportingDashboardRepository.saveAndFlush(reportingDashboard);
        reportingDashboardSearchRepository.save(reportingDashboard);
        int databaseSizeBeforeDelete = reportingDashboardRepository.findAll().size();

        // Get the reportingDashboard
        restReportingDashboardMockMvc.perform(delete("/api/reporting-dashboards/{id}", reportingDashboard.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean reportingDashboardExistsInEs = reportingDashboardSearchRepository.exists(reportingDashboard.getId());
        assertThat(reportingDashboardExistsInEs).isFalse();

        // Validate the database is empty
        List<ReportingDashboard> reportingDashboardList = reportingDashboardRepository.findAll();
        assertThat(reportingDashboardList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchReportingDashboard() throws Exception {
        // Initialize the database
        reportingDashboardRepository.saveAndFlush(reportingDashboard);
        reportingDashboardSearchRepository.save(reportingDashboard);

        // Search the reportingDashboard
        restReportingDashboardMockMvc.perform(get("/api/_search/reporting-dashboards?query=id:" + reportingDashboard.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportingDashboard.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].defaultFlag").value(hasItem(DEFAULT_DEFAULT_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].udatedDate").value(hasItem(DEFAULT_UDATED_DATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportingDashboard.class);
        ReportingDashboard reportingDashboard1 = new ReportingDashboard();
        reportingDashboard1.setId(1L);
        ReportingDashboard reportingDashboard2 = new ReportingDashboard();
        reportingDashboard2.setId(reportingDashboard1.getId());
        assertThat(reportingDashboard1).isEqualTo(reportingDashboard2);
        reportingDashboard2.setId(2L);
        assertThat(reportingDashboard1).isNotEqualTo(reportingDashboard2);
        reportingDashboard1.setId(null);
        assertThat(reportingDashboard1).isNotEqualTo(reportingDashboard2);
    }
}
