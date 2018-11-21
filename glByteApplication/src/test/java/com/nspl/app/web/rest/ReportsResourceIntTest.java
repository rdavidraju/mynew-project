package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;
import com.nspl.app.domain.Reports;
import com.nspl.app.repository.FavouriteReportsRepository;
import com.nspl.app.repository.ReportsRepository;
import com.nspl.app.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.spring4.SpringTemplateEngine;

import io.github.jhipster.config.JHipsterProperties;

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
 * Test class for the ReportsResource REST controller.
 *
 * @see ReportsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class ReportsResourceIntTest {

    private static final Long DEFAULT_REPORT_TYPE_ID = 1L;
    private static final Long UPDATED_REPORT_TYPE_ID = 2L;

    private static final String DEFAULT_REPORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_REPORT_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_SOURCE_VIEW_ID = 1L;
    private static final Long UPDATED_SOURCE_VIEW_ID = 2L;

    private static final Boolean DEFAULT_ENABLE_FLAG = false;
    private static final Boolean UPDATED_ENABLE_FLAG = true;

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    @Autowired
    private ReportsRepository reportsRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;
    
    @Autowired
    private FavouriteReportsRepository favouriteReportsRepository;

    private MockMvc restReportsMockMvc;

    private Reports reports;
    
    private JHipsterProperties jHipsterProperties;
	   
	private SpringTemplateEngine templateEngine;
	  
	private MessageSource messageSource;
	   
	private JavaMailSender javaMailSender;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReportsResource reportsResource = new ReportsResource(reportsRepository, favouriteReportsRepository,jHipsterProperties,javaMailSender,messageSource,templateEngine);
        this.restReportsMockMvc = MockMvcBuilders.standaloneSetup(reportsResource)
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
    public static Reports createEntity(EntityManager em) {
        Reports reports = new Reports()
            .reportTypeId(DEFAULT_REPORT_TYPE_ID)
            .reportName(DEFAULT_REPORT_NAME)
            .sourceViewId(DEFAULT_SOURCE_VIEW_ID)
            .enableFlag(DEFAULT_ENABLE_FLAG)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .creationDate(DEFAULT_CREATION_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY);
        return reports;
    }

    @Before
    public void initTest() {
        reports = createEntity(em);
    }

    @Test
    @Transactional
    public void createReports() throws Exception {
        int databaseSizeBeforeCreate = reportsRepository.findAll().size();

        // Create the Reports
        restReportsMockMvc.perform(post("/api/reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reports)))
            .andExpect(status().isCreated());

        // Validate the Reports in the database
        List<Reports> reportsList = reportsRepository.findAll();
        assertThat(reportsList).hasSize(databaseSizeBeforeCreate + 1);
        Reports testReports = reportsList.get(reportsList.size() - 1);
        assertThat(testReports.getReportTypeId()).isEqualTo(DEFAULT_REPORT_TYPE_ID);
        assertThat(testReports.getReportName()).isEqualTo(DEFAULT_REPORT_NAME);
        assertThat(testReports.getSourceViewId()).isEqualTo(DEFAULT_SOURCE_VIEW_ID);
        assertThat(testReports.isEnableFlag()).isEqualTo(DEFAULT_ENABLE_FLAG);
        assertThat(testReports.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testReports.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testReports.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testReports.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testReports.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);
        assertThat(testReports.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
    }

    @Test
    @Transactional
    public void createReportsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reportsRepository.findAll().size();

        // Create the Reports with an existing ID
        reports.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportsMockMvc.perform(post("/api/reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reports)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Reports> reportsList = reportsRepository.findAll();
        assertThat(reportsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllReports() throws Exception {
        // Initialize the database
        reportsRepository.saveAndFlush(reports);

        // Get all the reportsList
        restReportsMockMvc.perform(get("/api/reports?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reports.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportTypeId").value(hasItem(DEFAULT_REPORT_TYPE_ID.intValue())))
            .andExpect(jsonPath("$.[*].reportName").value(hasItem(DEFAULT_REPORT_NAME.toString())))
            .andExpect(jsonPath("$.[*].sourceViewId").value(hasItem(DEFAULT_SOURCE_VIEW_ID.intValue())))
            .andExpect(jsonPath("$.[*].enableFlag").value(hasItem(DEFAULT_ENABLE_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())));
    }

    @Test
    @Transactional
    public void getReports() throws Exception {
        // Initialize the database
        reportsRepository.saveAndFlush(reports);

        // Get the reports
        restReportsMockMvc.perform(get("/api/reports/{id}", reports.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(reports.getId().intValue()))
            .andExpect(jsonPath("$.reportTypeId").value(DEFAULT_REPORT_TYPE_ID.intValue()))
            .andExpect(jsonPath("$.reportName").value(DEFAULT_REPORT_NAME.toString()))
            .andExpect(jsonPath("$.sourceViewId").value(DEFAULT_SOURCE_VIEW_ID.intValue()))
            .andExpect(jsonPath("$.enableFlag").value(DEFAULT_ENABLE_FLAG.booleanValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.creationDate").value(sameInstant(DEFAULT_CREATION_DATE)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingReports() throws Exception {
        // Get the reports
        restReportsMockMvc.perform(get("/api/reports/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReports() throws Exception {
        // Initialize the database
        reportsRepository.saveAndFlush(reports);
        int databaseSizeBeforeUpdate = reportsRepository.findAll().size();

        // Update the reports
        Reports updatedReports = reportsRepository.findOne(reports.getId());
        updatedReports
            .reportTypeId(UPDATED_REPORT_TYPE_ID)
            .reportName(UPDATED_REPORT_NAME)
            .sourceViewId(UPDATED_SOURCE_VIEW_ID)
            .enableFlag(UPDATED_ENABLE_FLAG)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .creationDate(UPDATED_CREATION_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY);

        restReportsMockMvc.perform(put("/api/reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedReports)))
            .andExpect(status().isOk());

        // Validate the Reports in the database
        List<Reports> reportsList = reportsRepository.findAll();
        assertThat(reportsList).hasSize(databaseSizeBeforeUpdate);
        Reports testReports = reportsList.get(reportsList.size() - 1);
        assertThat(testReports.getReportTypeId()).isEqualTo(UPDATED_REPORT_TYPE_ID);
        assertThat(testReports.getReportName()).isEqualTo(UPDATED_REPORT_NAME);
        assertThat(testReports.getSourceViewId()).isEqualTo(UPDATED_SOURCE_VIEW_ID);
        assertThat(testReports.isEnableFlag()).isEqualTo(UPDATED_ENABLE_FLAG);
        assertThat(testReports.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testReports.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testReports.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testReports.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testReports.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);
        assertThat(testReports.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
    }

    @Test
    @Transactional
    public void updateNonExistingReports() throws Exception {
        int databaseSizeBeforeUpdate = reportsRepository.findAll().size();

        // Create the Reports

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restReportsMockMvc.perform(put("/api/reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reports)))
            .andExpect(status().isCreated());

        // Validate the Reports in the database
        List<Reports> reportsList = reportsRepository.findAll();
        assertThat(reportsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteReports() throws Exception {
        // Initialize the database
        reportsRepository.saveAndFlush(reports);
        int databaseSizeBeforeDelete = reportsRepository.findAll().size();

        // Get the reports
        restReportsMockMvc.perform(delete("/api/reports/{id}", reports.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Reports> reportsList = reportsRepository.findAll();
        assertThat(reportsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reports.class);
        Reports reports1 = new Reports();
        reports1.setId(1L);
        Reports reports2 = new Reports();
        reports2.setId(reports1.getId());
        assertThat(reports1).isEqualTo(reports2);
        reports2.setId(2L);
        assertThat(reports1).isNotEqualTo(reports2);
        reports1.setId(null);
        assertThat(reports1).isNotEqualTo(reports2);
    }
}
