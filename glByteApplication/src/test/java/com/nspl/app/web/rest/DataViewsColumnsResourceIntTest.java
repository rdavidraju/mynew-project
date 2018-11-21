package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.DataViewsColumns;
import com.nspl.app.repository.DataViewsColumnsRepository;
import com.nspl.app.repository.search.DataViewsColumnsSearchRepository;
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
 * Test class for the DataViewsColumnsResource REST controller.
 *
 * @see DataViewsColumnsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class DataViewsColumnsResourceIntTest {

    private static final Long DEFAULT_DATA_VIEW_ID = 1L;
    private static final Long UPDATED_DATA_VIEW_ID = 2L;

    private static final String DEFAULT_REF_DV_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_REF_DV_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_REF_DV_NAME = "AAAAAAAAAA";
    private static final String UPDATED_REF_DV_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_REF_DV_COLUMN = "AAAAAAAAAA";
    private static final String UPDATED_REF_DV_COLUMN = "BBBBBBBBBB";

    private static final String DEFAULT_COLUMN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COLUMN_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COL_DATA_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_COL_DATA_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private DataViewsColumnsRepository dataViewsColumnsRepository;

    @Autowired
    private DataViewsColumnsSearchRepository dataViewsColumnsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDataViewsColumnsMockMvc;

    private DataViewsColumns dataViewsColumns;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DataViewsColumnsResource dataViewsColumnsResource = new DataViewsColumnsResource(dataViewsColumnsRepository, dataViewsColumnsSearchRepository);
        this.restDataViewsColumnsMockMvc = MockMvcBuilders.standaloneSetup(dataViewsColumnsResource)
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
    public static DataViewsColumns createEntity(EntityManager em) {
        DataViewsColumns dataViewsColumns = new DataViewsColumns()
            .dataViewId(DEFAULT_DATA_VIEW_ID)
            .refDvType(DEFAULT_REF_DV_TYPE)
            .refDvName(DEFAULT_REF_DV_NAME)
            .refDvColumn(DEFAULT_REF_DV_COLUMN)
            .columnName(DEFAULT_COLUMN_NAME)
            .colDataType(DEFAULT_COL_DATA_TYPE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .creationDate(DEFAULT_CREATION_DATE)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE);
        return dataViewsColumns;
    }

    @Before
    public void initTest() {
        dataViewsColumnsSearchRepository.deleteAll();
        dataViewsColumns = createEntity(em);
    }

    @Test
    @Transactional
    public void createDataViewsColumns() throws Exception {
        int databaseSizeBeforeCreate = dataViewsColumnsRepository.findAll().size();

        // Create the DataViewsColumns
        restDataViewsColumnsMockMvc.perform(post("/api/data-views-columns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataViewsColumns)))
            .andExpect(status().isCreated());

        // Validate the DataViewsColumns in the database
        List<DataViewsColumns> dataViewsColumnsList = dataViewsColumnsRepository.findAll();
        assertThat(dataViewsColumnsList).hasSize(databaseSizeBeforeCreate + 1);
        DataViewsColumns testDataViewsColumns = dataViewsColumnsList.get(dataViewsColumnsList.size() - 1);
        assertThat(testDataViewsColumns.getDataViewId()).isEqualTo(DEFAULT_DATA_VIEW_ID);
        assertThat(testDataViewsColumns.getRefDvType()).isEqualTo(DEFAULT_REF_DV_TYPE);
        assertThat(testDataViewsColumns.getRefDvName()).isEqualTo(DEFAULT_REF_DV_NAME);
        assertThat(testDataViewsColumns.getRefDvColumn()).isEqualTo(DEFAULT_REF_DV_COLUMN);
        assertThat(testDataViewsColumns.getColumnName()).isEqualTo(DEFAULT_COLUMN_NAME);
        assertThat(testDataViewsColumns.getColDataType()).isEqualTo(DEFAULT_COL_DATA_TYPE);
        assertThat(testDataViewsColumns.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testDataViewsColumns.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testDataViewsColumns.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testDataViewsColumns.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);

        // Validate the DataViewsColumns in Elasticsearch
        DataViewsColumns dataViewsColumnsEs = dataViewsColumnsSearchRepository.findOne(testDataViewsColumns.getId());
        assertThat(dataViewsColumnsEs).isEqualToComparingFieldByField(testDataViewsColumns);
    }

    @Test
    @Transactional
    public void createDataViewsColumnsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dataViewsColumnsRepository.findAll().size();

        // Create the DataViewsColumns with an existing ID
        dataViewsColumns.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDataViewsColumnsMockMvc.perform(post("/api/data-views-columns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataViewsColumns)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<DataViewsColumns> dataViewsColumnsList = dataViewsColumnsRepository.findAll();
        assertThat(dataViewsColumnsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDataViewsColumns() throws Exception {
        // Initialize the database
        dataViewsColumnsRepository.saveAndFlush(dataViewsColumns);

        // Get all the dataViewsColumnsList
        restDataViewsColumnsMockMvc.perform(get("/api/data-views-columns?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataViewsColumns.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataViewId").value(hasItem(DEFAULT_DATA_VIEW_ID.intValue())))
            .andExpect(jsonPath("$.[*].refDvType").value(hasItem(DEFAULT_REF_DV_TYPE.toString())))
            .andExpect(jsonPath("$.[*].refDvName").value(hasItem(DEFAULT_REF_DV_NAME.toString())))
            .andExpect(jsonPath("$.[*].refDvColumn").value(hasItem(DEFAULT_REF_DV_COLUMN.toString())))
            .andExpect(jsonPath("$.[*].columnName").value(hasItem(DEFAULT_COLUMN_NAME.toString())))
            .andExpect(jsonPath("$.[*].colDataType").value(hasItem(DEFAULT_COL_DATA_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void getDataViewsColumns() throws Exception {
        // Initialize the database
        dataViewsColumnsRepository.saveAndFlush(dataViewsColumns);

        // Get the dataViewsColumns
        restDataViewsColumnsMockMvc.perform(get("/api/data-views-columns/{id}", dataViewsColumns.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dataViewsColumns.getId().intValue()))
            .andExpect(jsonPath("$.dataViewId").value(DEFAULT_DATA_VIEW_ID.intValue()))
            .andExpect(jsonPath("$.refDvType").value(DEFAULT_REF_DV_TYPE.toString()))
            .andExpect(jsonPath("$.refDvName").value(DEFAULT_REF_DV_NAME.toString()))
            .andExpect(jsonPath("$.refDvColumn").value(DEFAULT_REF_DV_COLUMN.toString()))
            .andExpect(jsonPath("$.columnName").value(DEFAULT_COLUMN_NAME.toString()))
            .andExpect(jsonPath("$.colDataType").value(DEFAULT_COL_DATA_TYPE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.creationDate").value(sameInstant(DEFAULT_CREATION_DATE)))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingDataViewsColumns() throws Exception {
        // Get the dataViewsColumns
        restDataViewsColumnsMockMvc.perform(get("/api/data-views-columns/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDataViewsColumns() throws Exception {
        // Initialize the database
        dataViewsColumnsRepository.saveAndFlush(dataViewsColumns);
        dataViewsColumnsSearchRepository.save(dataViewsColumns);
        int databaseSizeBeforeUpdate = dataViewsColumnsRepository.findAll().size();

        // Update the dataViewsColumns
        DataViewsColumns updatedDataViewsColumns = dataViewsColumnsRepository.findOne(dataViewsColumns.getId());
        updatedDataViewsColumns
            .dataViewId(UPDATED_DATA_VIEW_ID)
            .refDvType(UPDATED_REF_DV_TYPE)
            .refDvName(UPDATED_REF_DV_NAME)
            .refDvColumn(UPDATED_REF_DV_COLUMN)
            .columnName(UPDATED_COLUMN_NAME)
            .colDataType(UPDATED_COL_DATA_TYPE)
            .createdBy(UPDATED_CREATED_BY)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE);

        restDataViewsColumnsMockMvc.perform(put("/api/data-views-columns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDataViewsColumns)))
            .andExpect(status().isOk());

        // Validate the DataViewsColumns in the database
        List<DataViewsColumns> dataViewsColumnsList = dataViewsColumnsRepository.findAll();
        assertThat(dataViewsColumnsList).hasSize(databaseSizeBeforeUpdate);
        DataViewsColumns testDataViewsColumns = dataViewsColumnsList.get(dataViewsColumnsList.size() - 1);
        assertThat(testDataViewsColumns.getDataViewId()).isEqualTo(UPDATED_DATA_VIEW_ID);
        assertThat(testDataViewsColumns.getRefDvType()).isEqualTo(UPDATED_REF_DV_TYPE);
        assertThat(testDataViewsColumns.getRefDvName()).isEqualTo(UPDATED_REF_DV_NAME);
        assertThat(testDataViewsColumns.getRefDvColumn()).isEqualTo(UPDATED_REF_DV_COLUMN);
        assertThat(testDataViewsColumns.getColumnName()).isEqualTo(UPDATED_COLUMN_NAME);
        assertThat(testDataViewsColumns.getColDataType()).isEqualTo(UPDATED_COL_DATA_TYPE);
        assertThat(testDataViewsColumns.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testDataViewsColumns.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testDataViewsColumns.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testDataViewsColumns.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);

        // Validate the DataViewsColumns in Elasticsearch
        DataViewsColumns dataViewsColumnsEs = dataViewsColumnsSearchRepository.findOne(testDataViewsColumns.getId());
        assertThat(dataViewsColumnsEs).isEqualToComparingFieldByField(testDataViewsColumns);
    }

    @Test
    @Transactional
    public void updateNonExistingDataViewsColumns() throws Exception {
        int databaseSizeBeforeUpdate = dataViewsColumnsRepository.findAll().size();

        // Create the DataViewsColumns

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDataViewsColumnsMockMvc.perform(put("/api/data-views-columns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataViewsColumns)))
            .andExpect(status().isCreated());

        // Validate the DataViewsColumns in the database
        List<DataViewsColumns> dataViewsColumnsList = dataViewsColumnsRepository.findAll();
        assertThat(dataViewsColumnsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDataViewsColumns() throws Exception {
        // Initialize the database
        dataViewsColumnsRepository.saveAndFlush(dataViewsColumns);
        dataViewsColumnsSearchRepository.save(dataViewsColumns);
        int databaseSizeBeforeDelete = dataViewsColumnsRepository.findAll().size();

        // Get the dataViewsColumns
        restDataViewsColumnsMockMvc.perform(delete("/api/data-views-columns/{id}", dataViewsColumns.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean dataViewsColumnsExistsInEs = dataViewsColumnsSearchRepository.exists(dataViewsColumns.getId());
        assertThat(dataViewsColumnsExistsInEs).isFalse();

        // Validate the database is empty
        List<DataViewsColumns> dataViewsColumnsList = dataViewsColumnsRepository.findAll();
        assertThat(dataViewsColumnsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDataViewsColumns() throws Exception {
        // Initialize the database
        dataViewsColumnsRepository.saveAndFlush(dataViewsColumns);
        dataViewsColumnsSearchRepository.save(dataViewsColumns);

        // Search the dataViewsColumns
        restDataViewsColumnsMockMvc.perform(get("/api/_search/data-views-columns?query=id:" + dataViewsColumns.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataViewsColumns.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataViewId").value(hasItem(DEFAULT_DATA_VIEW_ID.intValue())))
            .andExpect(jsonPath("$.[*].refDvType").value(hasItem(DEFAULT_REF_DV_TYPE.toString())))
            .andExpect(jsonPath("$.[*].refDvName").value(hasItem(DEFAULT_REF_DV_NAME.toString())))
            .andExpect(jsonPath("$.[*].refDvColumn").value(hasItem(DEFAULT_REF_DV_COLUMN.toString())))
            .andExpect(jsonPath("$.[*].columnName").value(hasItem(DEFAULT_COLUMN_NAME.toString())))
            .andExpect(jsonPath("$.[*].colDataType").value(hasItem(DEFAULT_COL_DATA_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DataViewsColumns.class);
    }
}
