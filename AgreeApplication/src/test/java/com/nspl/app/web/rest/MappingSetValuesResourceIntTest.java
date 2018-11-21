package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.MappingSetValues;
import com.nspl.app.repository.MappingSetValuesRepository;
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
 * Test class for the MappingSetValuesResource REST controller.
 *
 * @see MappingSetValuesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class MappingSetValuesResourceIntTest {

    private static final Long DEFAULT_MAPPING_SET_ID = 1L;
    private static final Long UPDATED_MAPPING_SET_ID = 2L;

    private static final String DEFAULT_SOURCE_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_TARGET_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_TARGET_VALUE = "BBBBBBBBBB";

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private MappingSetValuesRepository mappingSetValuesRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMappingSetValuesMockMvc;

    private MappingSetValues mappingSetValues;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MappingSetValuesResource mappingSetValuesResource = new MappingSetValuesResource(mappingSetValuesRepository);
        this.restMappingSetValuesMockMvc = MockMvcBuilders.standaloneSetup(mappingSetValuesResource)
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
    public static MappingSetValues createEntity(EntityManager em) {
        MappingSetValues mappingSetValues = new MappingSetValues()
            .mappingSetId(DEFAULT_MAPPING_SET_ID)
            .sourceValue(DEFAULT_SOURCE_VALUE)
            .targetValue(DEFAULT_TARGET_VALUE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE);
        return mappingSetValues;
    }

    @Before
    public void initTest() {
        mappingSetValues = createEntity(em);
    }

    @Test
    @Transactional
    public void createMappingSetValues() throws Exception {
        int databaseSizeBeforeCreate = mappingSetValuesRepository.findAll().size();

        // Create the MappingSetValues
        restMappingSetValuesMockMvc.perform(post("/api/mapping-set-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mappingSetValues)))
            .andExpect(status().isCreated());

        // Validate the MappingSetValues in the database
        List<MappingSetValues> mappingSetValuesList = mappingSetValuesRepository.findAll();
        assertThat(mappingSetValuesList).hasSize(databaseSizeBeforeCreate + 1);
        MappingSetValues testMappingSetValues = mappingSetValuesList.get(mappingSetValuesList.size() - 1);
        assertThat(testMappingSetValues.getMappingSetId()).isEqualTo(DEFAULT_MAPPING_SET_ID);
        assertThat(testMappingSetValues.getSourceValue()).isEqualTo(DEFAULT_SOURCE_VALUE);
        assertThat(testMappingSetValues.getTargetValue()).isEqualTo(DEFAULT_TARGET_VALUE);
        assertThat(testMappingSetValues.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testMappingSetValues.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testMappingSetValues.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testMappingSetValues.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void createMappingSetValuesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = mappingSetValuesRepository.findAll().size();

        // Create the MappingSetValues with an existing ID
        mappingSetValues.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMappingSetValuesMockMvc.perform(post("/api/mapping-set-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mappingSetValues)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<MappingSetValues> mappingSetValuesList = mappingSetValuesRepository.findAll();
        assertThat(mappingSetValuesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMappingSetValues() throws Exception {
        // Initialize the database
        mappingSetValuesRepository.saveAndFlush(mappingSetValues);

        // Get all the mappingSetValuesList
        restMappingSetValuesMockMvc.perform(get("/api/mapping-set-values?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mappingSetValues.getId().intValue())))
            .andExpect(jsonPath("$.[*].mappingSetId").value(hasItem(DEFAULT_MAPPING_SET_ID.intValue())))
            .andExpect(jsonPath("$.[*].sourceValue").value(hasItem(DEFAULT_SOURCE_VALUE.toString())))
            .andExpect(jsonPath("$.[*].targetValue").value(hasItem(DEFAULT_TARGET_VALUE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void getMappingSetValues() throws Exception {
        // Initialize the database
        mappingSetValuesRepository.saveAndFlush(mappingSetValues);

        // Get the mappingSetValues
        restMappingSetValuesMockMvc.perform(get("/api/mapping-set-values/{id}", mappingSetValues.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(mappingSetValues.getId().intValue()))
            .andExpect(jsonPath("$.mappingSetId").value(DEFAULT_MAPPING_SET_ID.intValue()))
            .andExpect(jsonPath("$.sourceValue").value(DEFAULT_SOURCE_VALUE.toString()))
            .andExpect(jsonPath("$.targetValue").value(DEFAULT_TARGET_VALUE))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingMappingSetValues() throws Exception {
        // Get the mappingSetValues
        restMappingSetValuesMockMvc.perform(get("/api/mapping-set-values/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMappingSetValues() throws Exception {
        // Initialize the database
        mappingSetValuesRepository.saveAndFlush(mappingSetValues);
        int databaseSizeBeforeUpdate = mappingSetValuesRepository.findAll().size();

        // Update the mappingSetValues
        MappingSetValues updatedMappingSetValues = mappingSetValuesRepository.findOne(mappingSetValues.getId());
        updatedMappingSetValues
            .mappingSetId(UPDATED_MAPPING_SET_ID)
            .sourceValue(UPDATED_SOURCE_VALUE)
            .targetValue(UPDATED_TARGET_VALUE)
            .createdBy(UPDATED_CREATED_BY)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE);

        restMappingSetValuesMockMvc.perform(put("/api/mapping-set-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMappingSetValues)))
            .andExpect(status().isOk());

        // Validate the MappingSetValues in the database
        List<MappingSetValues> mappingSetValuesList = mappingSetValuesRepository.findAll();
        assertThat(mappingSetValuesList).hasSize(databaseSizeBeforeUpdate);
        MappingSetValues testMappingSetValues = mappingSetValuesList.get(mappingSetValuesList.size() - 1);
        assertThat(testMappingSetValues.getMappingSetId()).isEqualTo(UPDATED_MAPPING_SET_ID);
        assertThat(testMappingSetValues.getSourceValue()).isEqualTo(UPDATED_SOURCE_VALUE);
        assertThat(testMappingSetValues.getTargetValue()).isEqualTo(UPDATED_TARGET_VALUE);
        assertThat(testMappingSetValues.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testMappingSetValues.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testMappingSetValues.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testMappingSetValues.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingMappingSetValues() throws Exception {
        int databaseSizeBeforeUpdate = mappingSetValuesRepository.findAll().size();

        // Create the MappingSetValues

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMappingSetValuesMockMvc.perform(put("/api/mapping-set-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mappingSetValues)))
            .andExpect(status().isCreated());

        // Validate the MappingSetValues in the database
        List<MappingSetValues> mappingSetValuesList = mappingSetValuesRepository.findAll();
        assertThat(mappingSetValuesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMappingSetValues() throws Exception {
        // Initialize the database
        mappingSetValuesRepository.saveAndFlush(mappingSetValues);
        int databaseSizeBeforeDelete = mappingSetValuesRepository.findAll().size();

        // Get the mappingSetValues
        restMappingSetValuesMockMvc.perform(delete("/api/mapping-set-values/{id}", mappingSetValues.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<MappingSetValues> mappingSetValuesList = mappingSetValuesRepository.findAll();
        assertThat(mappingSetValuesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MappingSetValues.class);
    }
}
