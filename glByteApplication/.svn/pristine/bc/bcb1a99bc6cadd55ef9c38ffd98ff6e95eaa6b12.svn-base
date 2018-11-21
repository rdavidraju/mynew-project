package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.DataViewUnion;
import com.nspl.app.repository.DataViewUnionRepository;
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
 * Test class for the DataViewUnionResource REST controller.
 *
 * @see DataViewUnionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class DataViewUnionResourceIntTest {

    private static final Long DEFAULT_DATA_VIEW_LINE_ID = 1L;
    private static final Long UPDATED_DATA_VIEW_LINE_ID = 2L;

    private static final String DEFAULT_REF_DV_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_REF_DV_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_REF_DV_NAME = 1L;
    private static final Long UPDATED_REF_DV_NAME = 2L;

    private static final Long DEFAULT_REF_DV_COLUMN = 1L;
    private static final Long UPDATED_REF_DV_COLUMN = 2L;

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private DataViewUnionRepository dataViewUnionRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDataViewUnionMockMvc;

    private DataViewUnion dataViewUnion;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DataViewUnionResource dataViewUnionResource = new DataViewUnionResource(dataViewUnionRepository);
        this.restDataViewUnionMockMvc = MockMvcBuilders.standaloneSetup(dataViewUnionResource)
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
    public static DataViewUnion createEntity(EntityManager em) {
        DataViewUnion dataViewUnion = new DataViewUnion()
            .dataViewLineId(DEFAULT_DATA_VIEW_LINE_ID)
            .refDvType(DEFAULT_REF_DV_TYPE)
            .refDvName(DEFAULT_REF_DV_NAME)
            .refDvColumn(DEFAULT_REF_DV_COLUMN)
            .createdBy(DEFAULT_CREATED_BY)
            .creationDate(DEFAULT_CREATION_DATE)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE);
        return dataViewUnion;
    }

    @Before
    public void initTest() {
        dataViewUnion = createEntity(em);
    }

    @Test
    @Transactional
    public void createDataViewUnion() throws Exception {
        int databaseSizeBeforeCreate = dataViewUnionRepository.findAll().size();

        // Create the DataViewUnion
        restDataViewUnionMockMvc.perform(post("/api/data-view-unions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataViewUnion)))
            .andExpect(status().isCreated());

        // Validate the DataViewUnion in the database
        List<DataViewUnion> dataViewUnionList = dataViewUnionRepository.findAll();
        assertThat(dataViewUnionList).hasSize(databaseSizeBeforeCreate + 1);
        DataViewUnion testDataViewUnion = dataViewUnionList.get(dataViewUnionList.size() - 1);
        assertThat(testDataViewUnion.getDataViewLineId()).isEqualTo(DEFAULT_DATA_VIEW_LINE_ID);
        assertThat(testDataViewUnion.getRefDvType()).isEqualTo(DEFAULT_REF_DV_TYPE);
        assertThat(testDataViewUnion.getRefDvName()).isEqualTo(DEFAULT_REF_DV_NAME);
        assertThat(testDataViewUnion.getRefDvColumn()).isEqualTo(DEFAULT_REF_DV_COLUMN);
        assertThat(testDataViewUnion.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testDataViewUnion.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testDataViewUnion.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testDataViewUnion.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void createDataViewUnionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dataViewUnionRepository.findAll().size();

        // Create the DataViewUnion with an existing ID
        dataViewUnion.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDataViewUnionMockMvc.perform(post("/api/data-view-unions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataViewUnion)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<DataViewUnion> dataViewUnionList = dataViewUnionRepository.findAll();
        assertThat(dataViewUnionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDataViewUnions() throws Exception {
        // Initialize the database
        dataViewUnionRepository.saveAndFlush(dataViewUnion);

        // Get all the dataViewUnionList
        restDataViewUnionMockMvc.perform(get("/api/data-view-unions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataViewUnion.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataViewLineId").value(hasItem(DEFAULT_DATA_VIEW_LINE_ID.intValue())))
            .andExpect(jsonPath("$.[*].refDvType").value(hasItem(DEFAULT_REF_DV_TYPE.toString())))
            .andExpect(jsonPath("$.[*].refDvName").value(hasItem(DEFAULT_REF_DV_NAME.intValue())))
            .andExpect(jsonPath("$.[*].refDvColumn").value(hasItem(DEFAULT_REF_DV_COLUMN.intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void getDataViewUnion() throws Exception {
        // Initialize the database
        dataViewUnionRepository.saveAndFlush(dataViewUnion);

        // Get the dataViewUnion
        restDataViewUnionMockMvc.perform(get("/api/data-view-unions/{id}", dataViewUnion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dataViewUnion.getId().intValue()))
            .andExpect(jsonPath("$.dataViewLineId").value(DEFAULT_DATA_VIEW_LINE_ID.intValue()))
            .andExpect(jsonPath("$.refDvType").value(DEFAULT_REF_DV_TYPE.toString()))
            .andExpect(jsonPath("$.refDvName").value(DEFAULT_REF_DV_NAME.intValue()))
            .andExpect(jsonPath("$.refDvColumn").value(DEFAULT_REF_DV_COLUMN.intValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.creationDate").value(sameInstant(DEFAULT_CREATION_DATE)))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingDataViewUnion() throws Exception {
        // Get the dataViewUnion
        restDataViewUnionMockMvc.perform(get("/api/data-view-unions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDataViewUnion() throws Exception {
        // Initialize the database
        dataViewUnionRepository.saveAndFlush(dataViewUnion);
        int databaseSizeBeforeUpdate = dataViewUnionRepository.findAll().size();

        // Update the dataViewUnion
        DataViewUnion updatedDataViewUnion = dataViewUnionRepository.findOne(dataViewUnion.getId());
        updatedDataViewUnion
            .dataViewLineId(UPDATED_DATA_VIEW_LINE_ID)
            .refDvType(UPDATED_REF_DV_TYPE)
            .refDvName(UPDATED_REF_DV_NAME)
            .refDvColumn(UPDATED_REF_DV_COLUMN)
            .createdBy(UPDATED_CREATED_BY)
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE);

        restDataViewUnionMockMvc.perform(put("/api/data-view-unions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDataViewUnion)))
            .andExpect(status().isOk());

        // Validate the DataViewUnion in the database
        List<DataViewUnion> dataViewUnionList = dataViewUnionRepository.findAll();
        assertThat(dataViewUnionList).hasSize(databaseSizeBeforeUpdate);
        DataViewUnion testDataViewUnion = dataViewUnionList.get(dataViewUnionList.size() - 1);
        assertThat(testDataViewUnion.getDataViewLineId()).isEqualTo(UPDATED_DATA_VIEW_LINE_ID);
        assertThat(testDataViewUnion.getRefDvType()).isEqualTo(UPDATED_REF_DV_TYPE);
        assertThat(testDataViewUnion.getRefDvName()).isEqualTo(UPDATED_REF_DV_NAME);
        assertThat(testDataViewUnion.getRefDvColumn()).isEqualTo(UPDATED_REF_DV_COLUMN);
        assertThat(testDataViewUnion.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testDataViewUnion.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testDataViewUnion.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testDataViewUnion.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingDataViewUnion() throws Exception {
        int databaseSizeBeforeUpdate = dataViewUnionRepository.findAll().size();

        // Create the DataViewUnion

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDataViewUnionMockMvc.perform(put("/api/data-view-unions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataViewUnion)))
            .andExpect(status().isCreated());

        // Validate the DataViewUnion in the database
        List<DataViewUnion> dataViewUnionList = dataViewUnionRepository.findAll();
        assertThat(dataViewUnionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDataViewUnion() throws Exception {
        // Initialize the database
        dataViewUnionRepository.saveAndFlush(dataViewUnion);
        int databaseSizeBeforeDelete = dataViewUnionRepository.findAll().size();

        // Get the dataViewUnion
        restDataViewUnionMockMvc.perform(delete("/api/data-view-unions/{id}", dataViewUnion.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DataViewUnion> dataViewUnionList = dataViewUnionRepository.findAll();
        assertThat(dataViewUnionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DataViewUnion.class);
    }
}
