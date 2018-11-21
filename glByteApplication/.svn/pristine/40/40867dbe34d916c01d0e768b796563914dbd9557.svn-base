package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.Segments;
import com.nspl.app.repository.SegmentsRepository;
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
 * Test class for the SegmentsResource REST controller.
 *
 * @see SegmentsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class SegmentsResourceIntTest {

    private static final Long DEFAULT_COA_ID = 1L;
    private static final Long UPDATED_COA_ID = 2L;

    private static final String DEFAULT_SEGMENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SEGMENT_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_VALUES = 1L;
    private static final Long UPDATED_VALUES = 2L;

    private static final Integer DEFAULT_SEGMENT_LENGTH = 1;
    private static final Integer UPDATED_SEGMENT_LENGTH = 2;

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private SegmentsRepository segmentsRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSegmentsMockMvc;

    private Segments segments;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SegmentsResource segmentsResource = new SegmentsResource(segmentsRepository);
        this.restSegmentsMockMvc = MockMvcBuilders.standaloneSetup(segmentsResource)
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
    public static Segments createEntity(EntityManager em) {
        Segments segments = new Segments()
            .coaId(DEFAULT_COA_ID)
            .segmentName(DEFAULT_SEGMENT_NAME)
            .valueId(DEFAULT_VALUES)
            .segmentLength(DEFAULT_SEGMENT_LENGTH)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE);
        return segments;
    }

    @Before
    public void initTest() {
        segments = createEntity(em);
    }

    @Test
    @Transactional
    public void createSegments() throws Exception {
        int databaseSizeBeforeCreate = segmentsRepository.findAll().size();

        // Create the Segments
        restSegmentsMockMvc.perform(post("/api/segments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(segments)))
            .andExpect(status().isCreated());

        // Validate the Segments in the database
        List<Segments> segmentsList = segmentsRepository.findAll();
        assertThat(segmentsList).hasSize(databaseSizeBeforeCreate + 1);
        Segments testSegments = segmentsList.get(segmentsList.size() - 1);
        assertThat(testSegments.getCoaId()).isEqualTo(DEFAULT_COA_ID);
        assertThat(testSegments.getSegmentName()).isEqualTo(DEFAULT_SEGMENT_NAME);
        assertThat(testSegments.getValueId()).isEqualTo(DEFAULT_VALUES);
        assertThat(testSegments.getSegmentLength()).isEqualTo(DEFAULT_SEGMENT_LENGTH);
        assertThat(testSegments.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSegments.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSegments.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testSegments.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void createSegmentsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = segmentsRepository.findAll().size();

        // Create the Segments with an existing ID
        segments.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSegmentsMockMvc.perform(post("/api/segments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(segments)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Segments> segmentsList = segmentsRepository.findAll();
        assertThat(segmentsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSegments() throws Exception {
        // Initialize the database
        segmentsRepository.saveAndFlush(segments);

        // Get all the segmentsList
        restSegmentsMockMvc.perform(get("/api/segments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(segments.getId().intValue())))
            .andExpect(jsonPath("$.[*].coaId").value(hasItem(DEFAULT_COA_ID.intValue())))
            .andExpect(jsonPath("$.[*].segmentName").value(hasItem(DEFAULT_SEGMENT_NAME.toString())))
            .andExpect(jsonPath("$.[*].values").value(hasItem(DEFAULT_VALUES.intValue())))
            .andExpect(jsonPath("$.[*].segmentLength").value(hasItem(DEFAULT_SEGMENT_LENGTH)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void getSegments() throws Exception {
        // Initialize the database
        segmentsRepository.saveAndFlush(segments);

        // Get the segments
        restSegmentsMockMvc.perform(get("/api/segments/{id}", segments.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(segments.getId().intValue()))
            .andExpect(jsonPath("$.coaId").value(DEFAULT_COA_ID.intValue()))
            .andExpect(jsonPath("$.segmentName").value(DEFAULT_SEGMENT_NAME.toString()))
            .andExpect(jsonPath("$.values").value(DEFAULT_VALUES.intValue()))
            .andExpect(jsonPath("$.segmentLength").value(DEFAULT_SEGMENT_LENGTH))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingSegments() throws Exception {
        // Get the segments
        restSegmentsMockMvc.perform(get("/api/segments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSegments() throws Exception {
        // Initialize the database
        segmentsRepository.saveAndFlush(segments);
        int databaseSizeBeforeUpdate = segmentsRepository.findAll().size();

        // Update the segments
        Segments updatedSegments = segmentsRepository.findOne(segments.getId());
        updatedSegments
            .coaId(UPDATED_COA_ID)
            .segmentName(UPDATED_SEGMENT_NAME)
            .valueId(UPDATED_VALUES)
            .segmentLength(UPDATED_SEGMENT_LENGTH)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE);

        restSegmentsMockMvc.perform(put("/api/segments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSegments)))
            .andExpect(status().isOk());

        // Validate the Segments in the database
        List<Segments> segmentsList = segmentsRepository.findAll();
        assertThat(segmentsList).hasSize(databaseSizeBeforeUpdate);
        Segments testSegments = segmentsList.get(segmentsList.size() - 1);
        assertThat(testSegments.getCoaId()).isEqualTo(UPDATED_COA_ID);
        assertThat(testSegments.getSegmentName()).isEqualTo(UPDATED_SEGMENT_NAME);
        assertThat(testSegments.getValueId()).isEqualTo(UPDATED_VALUES);
        assertThat(testSegments.getSegmentLength()).isEqualTo(UPDATED_SEGMENT_LENGTH);
        assertThat(testSegments.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSegments.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSegments.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testSegments.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingSegments() throws Exception {
        int databaseSizeBeforeUpdate = segmentsRepository.findAll().size();

        // Create the Segments

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSegmentsMockMvc.perform(put("/api/segments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(segments)))
            .andExpect(status().isCreated());

        // Validate the Segments in the database
        List<Segments> segmentsList = segmentsRepository.findAll();
        assertThat(segmentsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSegments() throws Exception {
        // Initialize the database
        segmentsRepository.saveAndFlush(segments);
        int databaseSizeBeforeDelete = segmentsRepository.findAll().size();

        // Get the segments
        restSegmentsMockMvc.perform(delete("/api/segments/{id}", segments.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Segments> segmentsList = segmentsRepository.findAll();
        assertThat(segmentsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Segments.class);
        Segments segments1 = new Segments();
        segments1.setId(1L);
        Segments segments2 = new Segments();
        segments2.setId(segments1.getId());
        assertThat(segments1).isEqualTo(segments2);
        segments2.setId(2L);
        assertThat(segments1).isNotEqualTo(segments2);
        segments1.setId(null);
        assertThat(segments1).isNotEqualTo(segments2);
    }
}
