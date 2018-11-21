package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.DataViewsSrcMappings;
import com.nspl.app.repository.DataViewsSrcMappingsRepository;
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
 * Test class for the DataViewsSrcMappingsResource REST controller.
 *
 * @see DataViewsSrcMappingsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class DataViewsSrcMappingsResourceIntTest {

    private static final Long DEFAULT_DATA_VIEW_ID = 1L;
    private static final Long UPDATED_DATA_VIEW_ID = 2L;

    private static final Long DEFAULT_TEMPLATE_ID = 1L;
    private static final Long UPDATED_TEMPLATE_ID = 2L;

    private static final String DEFAULT_BASE = "AAAAAAAAAA";
    private static final String UPDATED_BASE = "BBBBBBBBBB";

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private DataViewsSrcMappingsRepository dataViewsSrcMappingsRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDataViewsSrcMappingsMockMvc;

    private DataViewsSrcMappings dataViewsSrcMappings;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DataViewsSrcMappingsResource dataViewsSrcMappingsResource = new DataViewsSrcMappingsResource(dataViewsSrcMappingsRepository);
        this.restDataViewsSrcMappingsMockMvc = MockMvcBuilders.standaloneSetup(dataViewsSrcMappingsResource)
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
    public static DataViewsSrcMappings createEntity(EntityManager em) {
        DataViewsSrcMappings dataViewsSrcMappings = new DataViewsSrcMappings()
            .dataViewId(DEFAULT_DATA_VIEW_ID)
            .templateId(DEFAULT_TEMPLATE_ID)
            .base(DEFAULT_BASE)
            .createdBy(DEFAULT_CREATED_BY)
            .creationDate(DEFAULT_CREATION_DATE)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE);
        return dataViewsSrcMappings;
    }

    @Before
    public void initTest() {
        dataViewsSrcMappings = createEntity(em);
    }

    @Test
    @Transactional
    public void createDataViewsSrcMappings() throws Exception {
        int databaseSizeBeforeCreate = dataViewsSrcMappingsRepository.findAll().size();

        // Create the DataViewsSrcMappings
        restDataViewsSrcMappingsMockMvc.perform(post("/api/data-views-src-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataViewsSrcMappings)))
            .andExpect(status().isCreated());

        // Validate the DataViewsSrcMappings in the database
        List<DataViewsSrcMappings> dataViewsSrcMappingsList = dataViewsSrcMappingsRepository.findAll();
        assertThat(dataViewsSrcMappingsList).hasSize(databaseSizeBeforeCreate + 1);
        DataViewsSrcMappings testDataViewsSrcMappings = dataViewsSrcMappingsList.get(dataViewsSrcMappingsList.size() - 1);
        assertThat(testDataViewsSrcMappings.getDataViewId()).isEqualTo(DEFAULT_DATA_VIEW_ID);
        assertThat(testDataViewsSrcMappings.getTemplateId()).isEqualTo(DEFAULT_TEMPLATE_ID);
        assertThat(testDataViewsSrcMappings.getBase()).isEqualTo(DEFAULT_BASE);
        assertThat(testDataViewsSrcMappings.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testDataViewsSrcMappings.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testDataViewsSrcMappings.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testDataViewsSrcMappings.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void createDataViewsSrcMappingsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dataViewsSrcMappingsRepository.findAll().size();

        // Create the DataViewsSrcMappings with an existing ID
        dataViewsSrcMappings.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDataViewsSrcMappingsMockMvc.perform(post("/api/data-views-src-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataViewsSrcMappings)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<DataViewsSrcMappings> dataViewsSrcMappingsList = dataViewsSrcMappingsRepository.findAll();
        assertThat(dataViewsSrcMappingsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDataViewsSrcMappings() throws Exception {
        // Initialize the database
        dataViewsSrcMappingsRepository.saveAndFlush(dataViewsSrcMappings);

        // Get all the dataViewsSrcMappingsList
        restDataViewsSrcMappingsMockMvc.perform(get("/api/data-views-src-mappings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataViewsSrcMappings.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataViewId").value(hasItem(DEFAULT_DATA_VIEW_ID.intValue())))
            .andExpect(jsonPath("$.[*].templateId").value(hasItem(DEFAULT_TEMPLATE_ID.intValue())))
            .andExpect(jsonPath("$.[*].base").value(hasItem(DEFAULT_BASE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void getDataViewsSrcMappings() throws Exception {
        // Initialize the database
        dataViewsSrcMappingsRepository.saveAndFlush(dataViewsSrcMappings);

        // Get the dataViewsSrcMappings
        restDataViewsSrcMappingsMockMvc.perform(get("/api/data-views-src-mappings/{id}", dataViewsSrcMappings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dataViewsSrcMappings.getId().intValue()))
            .andExpect(jsonPath("$.dataViewId").value(DEFAULT_DATA_VIEW_ID.intValue()))
            .andExpect(jsonPath("$.templateId").value(DEFAULT_TEMPLATE_ID.intValue()))
            .andExpect(jsonPath("$.base").value(DEFAULT_BASE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.creationDate").value(sameInstant(DEFAULT_CREATION_DATE)))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingDataViewsSrcMappings() throws Exception {
        // Get the dataViewsSrcMappings
        restDataViewsSrcMappingsMockMvc.perform(get("/api/data-views-src-mappings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDataViewsSrcMappings() throws Exception {
        // Initialize the database
        dataViewsSrcMappingsRepository.saveAndFlush(dataViewsSrcMappings);
        int databaseSizeBeforeUpdate = dataViewsSrcMappingsRepository.findAll().size();

        // Update the dataViewsSrcMappings
        DataViewsSrcMappings updatedDataViewsSrcMappings = dataViewsSrcMappingsRepository.findOne(dataViewsSrcMappings.getId());
        updatedDataViewsSrcMappings
            .dataViewId(UPDATED_DATA_VIEW_ID)
            .templateId(UPDATED_TEMPLATE_ID)
            .base(UPDATED_BASE)
            .createdBy(UPDATED_CREATED_BY)
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE);

        restDataViewsSrcMappingsMockMvc.perform(put("/api/data-views-src-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDataViewsSrcMappings)))
            .andExpect(status().isOk());

        // Validate the DataViewsSrcMappings in the database
        List<DataViewsSrcMappings> dataViewsSrcMappingsList = dataViewsSrcMappingsRepository.findAll();
        assertThat(dataViewsSrcMappingsList).hasSize(databaseSizeBeforeUpdate);
        DataViewsSrcMappings testDataViewsSrcMappings = dataViewsSrcMappingsList.get(dataViewsSrcMappingsList.size() - 1);
        assertThat(testDataViewsSrcMappings.getDataViewId()).isEqualTo(UPDATED_DATA_VIEW_ID);
        assertThat(testDataViewsSrcMappings.getTemplateId()).isEqualTo(UPDATED_TEMPLATE_ID);
        assertThat(testDataViewsSrcMappings.getBase()).isEqualTo(UPDATED_BASE);
        assertThat(testDataViewsSrcMappings.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testDataViewsSrcMappings.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testDataViewsSrcMappings.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testDataViewsSrcMappings.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingDataViewsSrcMappings() throws Exception {
        int databaseSizeBeforeUpdate = dataViewsSrcMappingsRepository.findAll().size();

        // Create the DataViewsSrcMappings

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDataViewsSrcMappingsMockMvc.perform(put("/api/data-views-src-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataViewsSrcMappings)))
            .andExpect(status().isCreated());

        // Validate the DataViewsSrcMappings in the database
        List<DataViewsSrcMappings> dataViewsSrcMappingsList = dataViewsSrcMappingsRepository.findAll();
        assertThat(dataViewsSrcMappingsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDataViewsSrcMappings() throws Exception {
        // Initialize the database
        dataViewsSrcMappingsRepository.saveAndFlush(dataViewsSrcMappings);
        int databaseSizeBeforeDelete = dataViewsSrcMappingsRepository.findAll().size();

        // Get the dataViewsSrcMappings
        restDataViewsSrcMappingsMockMvc.perform(delete("/api/data-views-src-mappings/{id}", dataViewsSrcMappings.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DataViewsSrcMappings> dataViewsSrcMappingsList = dataViewsSrcMappingsRepository.findAll();
        assertThat(dataViewsSrcMappingsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DataViewsSrcMappings.class);
    }
}
