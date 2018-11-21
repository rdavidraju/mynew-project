package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.SummaryFileTemplateLines;
import com.nspl.app.repository.SummaryFileTemplateLinesRepository;
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
 * Test class for the SummaryFileTemplateLinesResource REST controller.
 *
 * @see SummaryFileTemplateLinesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class SummaryFileTemplateLinesResourceIntTest {

    private static final Integer DEFAULT_TEMPLATE_LINE_ID = 1;
    private static final Integer UPDATED_TEMPLATE_LINE_ID = 2;

    private static final Integer DEFAULT_FILE_TEMPLATE_ID = 1;
    private static final Integer UPDATED_FILE_TEMPLATE_ID = 2;

    private static final Boolean DEFAULT_GROUPING = false;
    private static final Boolean UPDATED_GROUPING = true;

    private static final Boolean DEFAULT_AGGREGATE = false;
    private static final Boolean UPDATED_AGGREGATE = true;

    private static final String DEFAULT_AGGREGATE_METHOD = "AAAAAAAAAA";
    private static final String UPDATED_AGGREGATE_METHOD = "BBBBBBBBBB";

    @Autowired
    private SummaryFileTemplateLinesRepository summaryFileTemplateLinesRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSummaryFileTemplateLinesMockMvc;

    private SummaryFileTemplateLines summaryFileTemplateLines;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SummaryFileTemplateLinesResource summaryFileTemplateLinesResource = new SummaryFileTemplateLinesResource(summaryFileTemplateLinesRepository);
        this.restSummaryFileTemplateLinesMockMvc = MockMvcBuilders.standaloneSetup(summaryFileTemplateLinesResource)
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
    public static SummaryFileTemplateLines createEntity(EntityManager em) {
        SummaryFileTemplateLines summaryFileTemplateLines = new SummaryFileTemplateLines()
            .templateLineId(DEFAULT_TEMPLATE_LINE_ID)
//            .fileTemplateId(DEFAULT_FILE_TEMPLATE_ID)
            .grouping(DEFAULT_GROUPING)
            .aggregate(DEFAULT_AGGREGATE)
            .aggregateMethod(DEFAULT_AGGREGATE_METHOD);
        return summaryFileTemplateLines;
    }

    @Before
    public void initTest() {
        summaryFileTemplateLines = createEntity(em);
    }

    @Test
    @Transactional
    public void createSummaryFileTemplateLines() throws Exception {
        int databaseSizeBeforeCreate = summaryFileTemplateLinesRepository.findAll().size();

        // Create the SummaryFileTemplateLines
        restSummaryFileTemplateLinesMockMvc.perform(post("/api/summary-file-template-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(summaryFileTemplateLines)))
            .andExpect(status().isCreated());

        // Validate the SummaryFileTemplateLines in the database
        List<SummaryFileTemplateLines> summaryFileTemplateLinesList = summaryFileTemplateLinesRepository.findAll();
        assertThat(summaryFileTemplateLinesList).hasSize(databaseSizeBeforeCreate + 1);
        SummaryFileTemplateLines testSummaryFileTemplateLines = summaryFileTemplateLinesList.get(summaryFileTemplateLinesList.size() - 1);
        assertThat(testSummaryFileTemplateLines.getTemplateLineId()).isEqualTo(DEFAULT_TEMPLATE_LINE_ID);
        assertThat(testSummaryFileTemplateLines.getFileTemplateId()).isEqualTo(DEFAULT_FILE_TEMPLATE_ID);
        assertThat(testSummaryFileTemplateLines.isGrouping()).isEqualTo(DEFAULT_GROUPING);
        assertThat(testSummaryFileTemplateLines.isAggregate()).isEqualTo(DEFAULT_AGGREGATE);
        assertThat(testSummaryFileTemplateLines.getAggregateMethod()).isEqualTo(DEFAULT_AGGREGATE_METHOD);
    }

    @Test
    @Transactional
    public void createSummaryFileTemplateLinesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = summaryFileTemplateLinesRepository.findAll().size();

        // Create the SummaryFileTemplateLines with an existing ID
        summaryFileTemplateLines.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSummaryFileTemplateLinesMockMvc.perform(post("/api/summary-file-template-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(summaryFileTemplateLines)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<SummaryFileTemplateLines> summaryFileTemplateLinesList = summaryFileTemplateLinesRepository.findAll();
        assertThat(summaryFileTemplateLinesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSummaryFileTemplateLines() throws Exception {
        // Initialize the database
        summaryFileTemplateLinesRepository.saveAndFlush(summaryFileTemplateLines);

        // Get all the summaryFileTemplateLinesList
        restSummaryFileTemplateLinesMockMvc.perform(get("/api/summary-file-template-lines?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(summaryFileTemplateLines.getId().intValue())))
            .andExpect(jsonPath("$.[*].templateLineId").value(hasItem(DEFAULT_TEMPLATE_LINE_ID)))
            .andExpect(jsonPath("$.[*].fileTemplateId").value(hasItem(DEFAULT_FILE_TEMPLATE_ID)))
            .andExpect(jsonPath("$.[*].grouping").value(hasItem(DEFAULT_GROUPING.booleanValue())))
            .andExpect(jsonPath("$.[*].aggregate").value(hasItem(DEFAULT_AGGREGATE.booleanValue())))
            .andExpect(jsonPath("$.[*].aggregateMethod").value(hasItem(DEFAULT_AGGREGATE_METHOD.toString())));
    }

    @Test
    @Transactional
    public void getSummaryFileTemplateLines() throws Exception {
        // Initialize the database
        summaryFileTemplateLinesRepository.saveAndFlush(summaryFileTemplateLines);

        // Get the summaryFileTemplateLines
        restSummaryFileTemplateLinesMockMvc.perform(get("/api/summary-file-template-lines/{id}", summaryFileTemplateLines.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(summaryFileTemplateLines.getId().intValue()))
            .andExpect(jsonPath("$.templateLineId").value(DEFAULT_TEMPLATE_LINE_ID))
            .andExpect(jsonPath("$.fileTemplateId").value(DEFAULT_FILE_TEMPLATE_ID))
            .andExpect(jsonPath("$.grouping").value(DEFAULT_GROUPING.booleanValue()))
            .andExpect(jsonPath("$.aggregate").value(DEFAULT_AGGREGATE.booleanValue()))
            .andExpect(jsonPath("$.aggregateMethod").value(DEFAULT_AGGREGATE_METHOD.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSummaryFileTemplateLines() throws Exception {
        // Get the summaryFileTemplateLines
        restSummaryFileTemplateLinesMockMvc.perform(get("/api/summary-file-template-lines/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSummaryFileTemplateLines() throws Exception {
        // Initialize the database
        summaryFileTemplateLinesRepository.saveAndFlush(summaryFileTemplateLines);
        int databaseSizeBeforeUpdate = summaryFileTemplateLinesRepository.findAll().size();

        // Update the summaryFileTemplateLines
        SummaryFileTemplateLines updatedSummaryFileTemplateLines = summaryFileTemplateLinesRepository.findOne(summaryFileTemplateLines.getId());
        updatedSummaryFileTemplateLines
            .templateLineId(UPDATED_TEMPLATE_LINE_ID)
//            .fileTemplateId(UPDATED_FILE_TEMPLATE_ID)
            .grouping(UPDATED_GROUPING)
            .aggregate(UPDATED_AGGREGATE)
            .aggregateMethod(UPDATED_AGGREGATE_METHOD);

        restSummaryFileTemplateLinesMockMvc.perform(put("/api/summary-file-template-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSummaryFileTemplateLines)))
            .andExpect(status().isOk());

        // Validate the SummaryFileTemplateLines in the database
        List<SummaryFileTemplateLines> summaryFileTemplateLinesList = summaryFileTemplateLinesRepository.findAll();
        assertThat(summaryFileTemplateLinesList).hasSize(databaseSizeBeforeUpdate);
        SummaryFileTemplateLines testSummaryFileTemplateLines = summaryFileTemplateLinesList.get(summaryFileTemplateLinesList.size() - 1);
        assertThat(testSummaryFileTemplateLines.getTemplateLineId()).isEqualTo(UPDATED_TEMPLATE_LINE_ID);
        assertThat(testSummaryFileTemplateLines.getFileTemplateId()).isEqualTo(UPDATED_FILE_TEMPLATE_ID);
        assertThat(testSummaryFileTemplateLines.isGrouping()).isEqualTo(UPDATED_GROUPING);
        assertThat(testSummaryFileTemplateLines.isAggregate()).isEqualTo(UPDATED_AGGREGATE);
        assertThat(testSummaryFileTemplateLines.getAggregateMethod()).isEqualTo(UPDATED_AGGREGATE_METHOD);
    }

    @Test
    @Transactional
    public void updateNonExistingSummaryFileTemplateLines() throws Exception {
        int databaseSizeBeforeUpdate = summaryFileTemplateLinesRepository.findAll().size();

        // Create the SummaryFileTemplateLines

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSummaryFileTemplateLinesMockMvc.perform(put("/api/summary-file-template-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(summaryFileTemplateLines)))
            .andExpect(status().isCreated());

        // Validate the SummaryFileTemplateLines in the database
        List<SummaryFileTemplateLines> summaryFileTemplateLinesList = summaryFileTemplateLinesRepository.findAll();
        assertThat(summaryFileTemplateLinesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSummaryFileTemplateLines() throws Exception {
        // Initialize the database
        summaryFileTemplateLinesRepository.saveAndFlush(summaryFileTemplateLines);
        int databaseSizeBeforeDelete = summaryFileTemplateLinesRepository.findAll().size();

        // Get the summaryFileTemplateLines
        restSummaryFileTemplateLinesMockMvc.perform(delete("/api/summary-file-template-lines/{id}", summaryFileTemplateLines.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SummaryFileTemplateLines> summaryFileTemplateLinesList = summaryFileTemplateLinesRepository.findAll();
        assertThat(summaryFileTemplateLinesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SummaryFileTemplateLines.class);
        SummaryFileTemplateLines summaryFileTemplateLines1 = new SummaryFileTemplateLines();
        summaryFileTemplateLines1.setId(1L);
        SummaryFileTemplateLines summaryFileTemplateLines2 = new SummaryFileTemplateLines();
        summaryFileTemplateLines2.setId(summaryFileTemplateLines1.getId());
        assertThat(summaryFileTemplateLines1).isEqualTo(summaryFileTemplateLines2);
        summaryFileTemplateLines2.setId(2L);
        assertThat(summaryFileTemplateLines1).isNotEqualTo(summaryFileTemplateLines2);
        summaryFileTemplateLines1.setId(null);
        assertThat(summaryFileTemplateLines1).isNotEqualTo(summaryFileTemplateLines2);
    }
}
