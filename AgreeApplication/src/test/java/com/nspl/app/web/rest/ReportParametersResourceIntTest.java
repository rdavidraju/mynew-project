package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.ReportParameters;
import com.nspl.app.repository.ReportParametersRepository;
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
 * Test class for the ReportParametersResource REST controller.
 *
 * @see ReportParametersResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class ReportParametersResourceIntTest {

    private static final Long DEFAULT_REPORT_ID = 1L;
    private static final Long UPDATED_REPORT_ID = 2L;

    private static final String DEFAULT_DISPLAY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DISPLAY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DATA_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_DATA_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_SELECTION_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_SELECTION_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_REF_TYPEID = "AAAAAAAAAA";
    private static final String UPDATED_REF_TYPEID = "BBBBBBBBBB";

    private static final Long DEFAULT_REF_SRC_ID = 1L;
    private static final Long UPDATED_REF_SRC_ID = 2L;

    private static final Long DEFAULT_REF_COL_ID = 1L;
    private static final Long UPDATED_REF_COL_ID = 2L;

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    @Autowired
    private ReportParametersRepository reportParametersRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restReportParametersMockMvc;

    private ReportParameters reportParameters;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReportParametersResource reportParametersResource = new ReportParametersResource(reportParametersRepository);
        this.restReportParametersMockMvc = MockMvcBuilders.standaloneSetup(reportParametersResource)
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
    public static ReportParameters createEntity(EntityManager em) {
        ReportParameters reportParameters = new ReportParameters()
            .reportId(DEFAULT_REPORT_ID)
            .displayName(DEFAULT_DISPLAY_NAME)
            .dataType(DEFAULT_DATA_TYPE)
            .selectionType(DEFAULT_SELECTION_TYPE)
            .refTypeid(DEFAULT_REF_TYPEID)
            .refSrcId(DEFAULT_REF_SRC_ID)
            .refColId(DEFAULT_REF_COL_ID)
            .creationDate(DEFAULT_CREATION_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY);
        return reportParameters;
    }

    @Before
    public void initTest() {
        reportParameters = createEntity(em);
    }

    @Test
    @Transactional
    public void createReportParameters() throws Exception {
        int databaseSizeBeforeCreate = reportParametersRepository.findAll().size();

        // Create the ReportParameters
        restReportParametersMockMvc.perform(post("/api/report-parameters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportParameters)))
            .andExpect(status().isCreated());

        // Validate the ReportParameters in the database
        List<ReportParameters> reportParametersList = reportParametersRepository.findAll();
        assertThat(reportParametersList).hasSize(databaseSizeBeforeCreate + 1);
        ReportParameters testReportParameters = reportParametersList.get(reportParametersList.size() - 1);
        assertThat(testReportParameters.getReportId()).isEqualTo(DEFAULT_REPORT_ID);
        assertThat(testReportParameters.getDisplayName()).isEqualTo(DEFAULT_DISPLAY_NAME);
        assertThat(testReportParameters.getDataType()).isEqualTo(DEFAULT_DATA_TYPE);
        assertThat(testReportParameters.getSelectionType()).isEqualTo(DEFAULT_SELECTION_TYPE);
        assertThat(testReportParameters.getRefTypeid()).isEqualTo(DEFAULT_REF_TYPEID);
        assertThat(testReportParameters.getRefSrcId()).isEqualTo(DEFAULT_REF_SRC_ID);
        assertThat(testReportParameters.getRefColId()).isEqualTo(DEFAULT_REF_COL_ID);
        assertThat(testReportParameters.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testReportParameters.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testReportParameters.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);
        assertThat(testReportParameters.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
    }

    @Test
    @Transactional
    public void createReportParametersWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reportParametersRepository.findAll().size();

        // Create the ReportParameters with an existing ID
        reportParameters.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportParametersMockMvc.perform(post("/api/report-parameters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportParameters)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ReportParameters> reportParametersList = reportParametersRepository.findAll();
        assertThat(reportParametersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllReportParameters() throws Exception {
        // Initialize the database
        reportParametersRepository.saveAndFlush(reportParameters);

        // Get all the reportParametersList
        restReportParametersMockMvc.perform(get("/api/report-parameters?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportParameters.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportId").value(hasItem(DEFAULT_REPORT_ID.intValue())))
            .andExpect(jsonPath("$.[*].displayName").value(hasItem(DEFAULT_DISPLAY_NAME.toString())))
            .andExpect(jsonPath("$.[*].dataType").value(hasItem(DEFAULT_DATA_TYPE.toString())))
            .andExpect(jsonPath("$.[*].selectionType").value(hasItem(DEFAULT_SELECTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].refTypeid").value(hasItem(DEFAULT_REF_TYPEID.toString())))
            .andExpect(jsonPath("$.[*].refSrcId").value(hasItem(DEFAULT_REF_SRC_ID.intValue())))
            .andExpect(jsonPath("$.[*].refColId").value(hasItem(DEFAULT_REF_COL_ID.intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())));
    }

    @Test
    @Transactional
    public void getReportParameters() throws Exception {
        // Initialize the database
        reportParametersRepository.saveAndFlush(reportParameters);

        // Get the reportParameters
        restReportParametersMockMvc.perform(get("/api/report-parameters/{id}", reportParameters.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(reportParameters.getId().intValue()))
            .andExpect(jsonPath("$.reportId").value(DEFAULT_REPORT_ID.intValue()))
            .andExpect(jsonPath("$.displayName").value(DEFAULT_DISPLAY_NAME.toString()))
            .andExpect(jsonPath("$.dataType").value(DEFAULT_DATA_TYPE.toString()))
            .andExpect(jsonPath("$.selectionType").value(DEFAULT_SELECTION_TYPE.toString()))
            .andExpect(jsonPath("$.refTypeid").value(DEFAULT_REF_TYPEID.toString()))
            .andExpect(jsonPath("$.refSrcId").value(DEFAULT_REF_SRC_ID.intValue()))
            .andExpect(jsonPath("$.refColId").value(DEFAULT_REF_COL_ID.intValue()))
            .andExpect(jsonPath("$.creationDate").value(sameInstant(DEFAULT_CREATION_DATE)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingReportParameters() throws Exception {
        // Get the reportParameters
        restReportParametersMockMvc.perform(get("/api/report-parameters/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReportParameters() throws Exception {
        // Initialize the database
        reportParametersRepository.saveAndFlush(reportParameters);
        int databaseSizeBeforeUpdate = reportParametersRepository.findAll().size();

        // Update the reportParameters
        ReportParameters updatedReportParameters = reportParametersRepository.findOne(reportParameters.getId());
        updatedReportParameters
            .reportId(UPDATED_REPORT_ID)
            .displayName(UPDATED_DISPLAY_NAME)
            .dataType(UPDATED_DATA_TYPE)
            .selectionType(UPDATED_SELECTION_TYPE)
            .refTypeid(UPDATED_REF_TYPEID)
            .refSrcId(UPDATED_REF_SRC_ID)
            .refColId(UPDATED_REF_COL_ID)
            .creationDate(UPDATED_CREATION_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY);

        restReportParametersMockMvc.perform(put("/api/report-parameters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedReportParameters)))
            .andExpect(status().isOk());

        // Validate the ReportParameters in the database
        List<ReportParameters> reportParametersList = reportParametersRepository.findAll();
        assertThat(reportParametersList).hasSize(databaseSizeBeforeUpdate);
        ReportParameters testReportParameters = reportParametersList.get(reportParametersList.size() - 1);
        assertThat(testReportParameters.getReportId()).isEqualTo(UPDATED_REPORT_ID);
        assertThat(testReportParameters.getDisplayName()).isEqualTo(UPDATED_DISPLAY_NAME);
        assertThat(testReportParameters.getDataType()).isEqualTo(UPDATED_DATA_TYPE);
        assertThat(testReportParameters.getSelectionType()).isEqualTo(UPDATED_SELECTION_TYPE);
        assertThat(testReportParameters.getRefTypeid()).isEqualTo(UPDATED_REF_TYPEID);
        assertThat(testReportParameters.getRefSrcId()).isEqualTo(UPDATED_REF_SRC_ID);
        assertThat(testReportParameters.getRefColId()).isEqualTo(UPDATED_REF_COL_ID);
        assertThat(testReportParameters.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testReportParameters.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testReportParameters.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);
        assertThat(testReportParameters.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
    }

    @Test
    @Transactional
    public void updateNonExistingReportParameters() throws Exception {
        int databaseSizeBeforeUpdate = reportParametersRepository.findAll().size();

        // Create the ReportParameters

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restReportParametersMockMvc.perform(put("/api/report-parameters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportParameters)))
            .andExpect(status().isCreated());

        // Validate the ReportParameters in the database
        List<ReportParameters> reportParametersList = reportParametersRepository.findAll();
        assertThat(reportParametersList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteReportParameters() throws Exception {
        // Initialize the database
        reportParametersRepository.saveAndFlush(reportParameters);
        int databaseSizeBeforeDelete = reportParametersRepository.findAll().size();

        // Get the reportParameters
        restReportParametersMockMvc.perform(delete("/api/report-parameters/{id}", reportParameters.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ReportParameters> reportParametersList = reportParametersRepository.findAll();
        assertThat(reportParametersList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportParameters.class);
        ReportParameters reportParameters1 = new ReportParameters();
        reportParameters1.setId(1L);
        ReportParameters reportParameters2 = new ReportParameters();
        reportParameters2.setId(reportParameters1.getId());
        assertThat(reportParameters1).isEqualTo(reportParameters2);
        reportParameters2.setId(2L);
        assertThat(reportParameters1).isNotEqualTo(reportParameters2);
        reportParameters1.setId(null);
        assertThat(reportParameters1).isNotEqualTo(reportParameters2);
    }
}
