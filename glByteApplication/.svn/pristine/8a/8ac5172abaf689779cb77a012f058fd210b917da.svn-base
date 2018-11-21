package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.ReportDefination;
import com.nspl.app.repository.ReportDefinationRepository;
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
 * Test class for the ReportDefinationResource REST controller.
 *
 * @see ReportDefinationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class ReportDefinationResourceIntTest {

    private static final Long DEFAULT_REPORT_ID = 1L;
    private static final Long UPDATED_REPORT_ID = 2L;

    private static final String DEFAULT_DISPLAY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DISPLAY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_REF_TYPE_ID = "AAAAAAAAAA";
    private static final String UPDATED_REF_TYPE_ID = "BBBBBBBBBB";

    private static final Long DEFAULT_REF_SRC_ID = 1L;
    private static final Long UPDATED_REF_SRC_ID = 2L;

    private static final Long DEFAULT_REF_COL_ID = 1L;
    private static final Long UPDATED_REF_COL_ID = 2L;

    private static final String DEFAULT_DATA_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_DATA_TYPE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_GOUP_BY = false;
    private static final Boolean UPDATED_GOUP_BY = true;

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    @Autowired
    private ReportDefinationRepository reportDefinationRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restReportDefinationMockMvc;

    private ReportDefination reportDefination;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReportDefinationResource reportDefinationResource = new ReportDefinationResource(reportDefinationRepository);
        this.restReportDefinationMockMvc = MockMvcBuilders.standaloneSetup(reportDefinationResource)
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
    public static ReportDefination createEntity(EntityManager em) {
        ReportDefination reportDefination = new ReportDefination()
            .reportId(DEFAULT_REPORT_ID)
            .displayName(DEFAULT_DISPLAY_NAME)
            .refTypeId(DEFAULT_REF_TYPE_ID)
            .refSrcId(DEFAULT_REF_SRC_ID)
            .refColId(DEFAULT_REF_COL_ID)
            .dataType(DEFAULT_DATA_TYPE)
            .goupBy(DEFAULT_GOUP_BY)
            .creationDate(DEFAULT_CREATION_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY);
        return reportDefination;
    }

    @Before
    public void initTest() {
        reportDefination = createEntity(em);
    }

    @Test
    @Transactional
    public void createReportDefination() throws Exception {
        int databaseSizeBeforeCreate = reportDefinationRepository.findAll().size();

        // Create the ReportDefination
        restReportDefinationMockMvc.perform(post("/api/report-definations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportDefination)))
            .andExpect(status().isCreated());

        // Validate the ReportDefination in the database
        List<ReportDefination> reportDefinationList = reportDefinationRepository.findAll();
        assertThat(reportDefinationList).hasSize(databaseSizeBeforeCreate + 1);
        ReportDefination testReportDefination = reportDefinationList.get(reportDefinationList.size() - 1);
        assertThat(testReportDefination.getReportId()).isEqualTo(DEFAULT_REPORT_ID);
        assertThat(testReportDefination.getDisplayName()).isEqualTo(DEFAULT_DISPLAY_NAME);
        assertThat(testReportDefination.getRefTypeId()).isEqualTo(DEFAULT_REF_TYPE_ID);
        assertThat(testReportDefination.getRefSrcId()).isEqualTo(DEFAULT_REF_SRC_ID);
        assertThat(testReportDefination.getRefColId()).isEqualTo(DEFAULT_REF_COL_ID);
        assertThat(testReportDefination.getDataType()).isEqualTo(DEFAULT_DATA_TYPE);
        assertThat(testReportDefination.isGoupBy()).isEqualTo(DEFAULT_GOUP_BY);
        assertThat(testReportDefination.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testReportDefination.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testReportDefination.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);
        assertThat(testReportDefination.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
    }

    @Test
    @Transactional
    public void createReportDefinationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reportDefinationRepository.findAll().size();

        // Create the ReportDefination with an existing ID
        reportDefination.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportDefinationMockMvc.perform(post("/api/report-definations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportDefination)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ReportDefination> reportDefinationList = reportDefinationRepository.findAll();
        assertThat(reportDefinationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllReportDefinations() throws Exception {
        // Initialize the database
        reportDefinationRepository.saveAndFlush(reportDefination);

        // Get all the reportDefinationList
        restReportDefinationMockMvc.perform(get("/api/report-definations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportDefination.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportId").value(hasItem(DEFAULT_REPORT_ID.intValue())))
            .andExpect(jsonPath("$.[*].displayName").value(hasItem(DEFAULT_DISPLAY_NAME.toString())))
            .andExpect(jsonPath("$.[*].refTypeId").value(hasItem(DEFAULT_REF_TYPE_ID.toString())))
            .andExpect(jsonPath("$.[*].refSrcId").value(hasItem(DEFAULT_REF_SRC_ID.intValue())))
            .andExpect(jsonPath("$.[*].refColId").value(hasItem(DEFAULT_REF_COL_ID.intValue())))
            .andExpect(jsonPath("$.[*].dataType").value(hasItem(DEFAULT_DATA_TYPE.toString())))
            .andExpect(jsonPath("$.[*].goupBy").value(hasItem(DEFAULT_GOUP_BY.booleanValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())));
    }

    @Test
    @Transactional
    public void getReportDefination() throws Exception {
        // Initialize the database
        reportDefinationRepository.saveAndFlush(reportDefination);

        // Get the reportDefination
        restReportDefinationMockMvc.perform(get("/api/report-definations/{id}", reportDefination.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(reportDefination.getId().intValue()))
            .andExpect(jsonPath("$.reportId").value(DEFAULT_REPORT_ID.intValue()))
            .andExpect(jsonPath("$.displayName").value(DEFAULT_DISPLAY_NAME.toString()))
            .andExpect(jsonPath("$.refTypeId").value(DEFAULT_REF_TYPE_ID.toString()))
            .andExpect(jsonPath("$.refSrcId").value(DEFAULT_REF_SRC_ID.intValue()))
            .andExpect(jsonPath("$.refColId").value(DEFAULT_REF_COL_ID.intValue()))
            .andExpect(jsonPath("$.dataType").value(DEFAULT_DATA_TYPE.toString()))
            .andExpect(jsonPath("$.goupBy").value(DEFAULT_GOUP_BY.booleanValue()))
            .andExpect(jsonPath("$.creationDate").value(sameInstant(DEFAULT_CREATION_DATE)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingReportDefination() throws Exception {
        // Get the reportDefination
        restReportDefinationMockMvc.perform(get("/api/report-definations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReportDefination() throws Exception {
        // Initialize the database
        reportDefinationRepository.saveAndFlush(reportDefination);
        int databaseSizeBeforeUpdate = reportDefinationRepository.findAll().size();

        // Update the reportDefination
        ReportDefination updatedReportDefination = reportDefinationRepository.findOne(reportDefination.getId());
        updatedReportDefination
            .reportId(UPDATED_REPORT_ID)
            .displayName(UPDATED_DISPLAY_NAME)
            .refTypeId(UPDATED_REF_TYPE_ID)
            .refSrcId(UPDATED_REF_SRC_ID)
            .refColId(UPDATED_REF_COL_ID)
            .dataType(UPDATED_DATA_TYPE)
            .goupBy(UPDATED_GOUP_BY)
            .creationDate(UPDATED_CREATION_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY);

        restReportDefinationMockMvc.perform(put("/api/report-definations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedReportDefination)))
            .andExpect(status().isOk());

        // Validate the ReportDefination in the database
        List<ReportDefination> reportDefinationList = reportDefinationRepository.findAll();
        assertThat(reportDefinationList).hasSize(databaseSizeBeforeUpdate);
        ReportDefination testReportDefination = reportDefinationList.get(reportDefinationList.size() - 1);
        assertThat(testReportDefination.getReportId()).isEqualTo(UPDATED_REPORT_ID);
        assertThat(testReportDefination.getDisplayName()).isEqualTo(UPDATED_DISPLAY_NAME);
        assertThat(testReportDefination.getRefTypeId()).isEqualTo(UPDATED_REF_TYPE_ID);
        assertThat(testReportDefination.getRefSrcId()).isEqualTo(UPDATED_REF_SRC_ID);
        assertThat(testReportDefination.getRefColId()).isEqualTo(UPDATED_REF_COL_ID);
        assertThat(testReportDefination.getDataType()).isEqualTo(UPDATED_DATA_TYPE);
        assertThat(testReportDefination.isGoupBy()).isEqualTo(UPDATED_GOUP_BY);
        assertThat(testReportDefination.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testReportDefination.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testReportDefination.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);
        assertThat(testReportDefination.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
    }

    @Test
    @Transactional
    public void updateNonExistingReportDefination() throws Exception {
        int databaseSizeBeforeUpdate = reportDefinationRepository.findAll().size();

        // Create the ReportDefination

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restReportDefinationMockMvc.perform(put("/api/report-definations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportDefination)))
            .andExpect(status().isCreated());

        // Validate the ReportDefination in the database
        List<ReportDefination> reportDefinationList = reportDefinationRepository.findAll();
        assertThat(reportDefinationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteReportDefination() throws Exception {
        // Initialize the database
        reportDefinationRepository.saveAndFlush(reportDefination);
        int databaseSizeBeforeDelete = reportDefinationRepository.findAll().size();

        // Get the reportDefination
        restReportDefinationMockMvc.perform(delete("/api/report-definations/{id}", reportDefination.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ReportDefination> reportDefinationList = reportDefinationRepository.findAll();
        assertThat(reportDefinationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportDefination.class);
        ReportDefination reportDefination1 = new ReportDefination();
        reportDefination1.setId(1L);
        ReportDefination reportDefination2 = new ReportDefination();
        reportDefination2.setId(reportDefination1.getId());
        assertThat(reportDefination1).isEqualTo(reportDefination2);
        reportDefination2.setId(2L);
        assertThat(reportDefination1).isNotEqualTo(reportDefination2);
        reportDefination1.setId(null);
        assertThat(reportDefination1).isNotEqualTo(reportDefination2);
    }
}
