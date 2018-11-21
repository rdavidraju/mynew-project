package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.DataViewFilters;
import com.nspl.app.repository.DataViewFiltersRepository;
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
 * Test class for the DataViewFiltersResource REST controller.
 *
 * @see DataViewFiltersResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class DataViewFiltersResourceIntTest {

    private static final Long DEFAULT_DATA_VIEW_ID = 1L;
    private static final Long UPDATED_DATA_VIEW_ID = 2L;

    private static final String DEFAULT_REF_SRC_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_REF_SRC_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_REF_SRC_ID = 1L;
    private static final Long UPDATED_REF_SRC_ID = 2L;

    private static final Long DEFAULT_REF_SRC_COL_ID = 1L;
    private static final Long UPDATED_REF_SRC_COL_ID = 2L;

    private static final String DEFAULT_FILTER_OPERATOR = "AAAAAAAAAA";
    private static final String UPDATED_FILTER_OPERATOR = "BBBBBBBBBB";

    private static final String DEFAULT_FILTER_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_FILTER_VALUE = "BBBBBBBBBB";

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private DataViewFiltersRepository dataViewFiltersRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDataViewFiltersMockMvc;

    private DataViewFilters dataViewFilters;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DataViewFiltersResource dataViewFiltersResource = new DataViewFiltersResource(dataViewFiltersRepository);
        this.restDataViewFiltersMockMvc = MockMvcBuilders.standaloneSetup(dataViewFiltersResource)
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
    public static DataViewFilters createEntity(EntityManager em) {
        DataViewFilters dataViewFilters = new DataViewFilters()
            .dataViewId(DEFAULT_DATA_VIEW_ID)
            .refSrcType(DEFAULT_REF_SRC_TYPE)
            .refSrcId(DEFAULT_REF_SRC_ID)
            .refSrcColId(DEFAULT_REF_SRC_COL_ID)
            .filterOperator(DEFAULT_FILTER_OPERATOR)
            .filterValue(DEFAULT_FILTER_VALUE)
            .createdBy(DEFAULT_CREATED_BY)
            .creationDate(DEFAULT_CREATION_DATE)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE);
        return dataViewFilters;
    }

    @Before
    public void initTest() {
        dataViewFilters = createEntity(em);
    }

    @Test
    @Transactional
    public void createDataViewFilters() throws Exception {
        int databaseSizeBeforeCreate = dataViewFiltersRepository.findAll().size();

        // Create the DataViewFilters
        restDataViewFiltersMockMvc.perform(post("/api/data-view-filters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataViewFilters)))
            .andExpect(status().isCreated());

        // Validate the DataViewFilters in the database
        List<DataViewFilters> dataViewFiltersList = dataViewFiltersRepository.findAll();
        assertThat(dataViewFiltersList).hasSize(databaseSizeBeforeCreate + 1);
        DataViewFilters testDataViewFilters = dataViewFiltersList.get(dataViewFiltersList.size() - 1);
        assertThat(testDataViewFilters.getDataViewId()).isEqualTo(DEFAULT_DATA_VIEW_ID);
        assertThat(testDataViewFilters.getRefSrcType()).isEqualTo(DEFAULT_REF_SRC_TYPE);
        assertThat(testDataViewFilters.getRefSrcId()).isEqualTo(DEFAULT_REF_SRC_ID);
        assertThat(testDataViewFilters.getRefSrcColId()).isEqualTo(DEFAULT_REF_SRC_COL_ID);
        assertThat(testDataViewFilters.getFilterOperator()).isEqualTo(DEFAULT_FILTER_OPERATOR);
        assertThat(testDataViewFilters.getFilterValue()).isEqualTo(DEFAULT_FILTER_VALUE);
        assertThat(testDataViewFilters.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testDataViewFilters.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testDataViewFilters.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testDataViewFilters.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void createDataViewFiltersWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dataViewFiltersRepository.findAll().size();

        // Create the DataViewFilters with an existing ID
        dataViewFilters.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDataViewFiltersMockMvc.perform(post("/api/data-view-filters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataViewFilters)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<DataViewFilters> dataViewFiltersList = dataViewFiltersRepository.findAll();
        assertThat(dataViewFiltersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDataViewFilters() throws Exception {
        // Initialize the database
        dataViewFiltersRepository.saveAndFlush(dataViewFilters);

        // Get all the dataViewFiltersList
        restDataViewFiltersMockMvc.perform(get("/api/data-view-filters?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataViewFilters.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataViewId").value(hasItem(DEFAULT_DATA_VIEW_ID.intValue())))
            .andExpect(jsonPath("$.[*].refSrcType").value(hasItem(DEFAULT_REF_SRC_TYPE.toString())))
            .andExpect(jsonPath("$.[*].refSrcId").value(hasItem(DEFAULT_REF_SRC_ID.intValue())))
            .andExpect(jsonPath("$.[*].refSrcColId").value(hasItem(DEFAULT_REF_SRC_COL_ID.intValue())))
            .andExpect(jsonPath("$.[*].filterOperator").value(hasItem(DEFAULT_FILTER_OPERATOR.toString())))
            .andExpect(jsonPath("$.[*].filterValue").value(hasItem(DEFAULT_FILTER_VALUE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void getDataViewFilters() throws Exception {
        // Initialize the database
        dataViewFiltersRepository.saveAndFlush(dataViewFilters);

        // Get the dataViewFilters
        restDataViewFiltersMockMvc.perform(get("/api/data-view-filters/{id}", dataViewFilters.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dataViewFilters.getId().intValue()))
            .andExpect(jsonPath("$.dataViewId").value(DEFAULT_DATA_VIEW_ID.intValue()))
            .andExpect(jsonPath("$.refSrcType").value(DEFAULT_REF_SRC_TYPE.toString()))
            .andExpect(jsonPath("$.refSrcId").value(DEFAULT_REF_SRC_ID.intValue()))
            .andExpect(jsonPath("$.refSrcColId").value(DEFAULT_REF_SRC_COL_ID.intValue()))
            .andExpect(jsonPath("$.filterOperator").value(DEFAULT_FILTER_OPERATOR.toString()))
            .andExpect(jsonPath("$.filterValue").value(DEFAULT_FILTER_VALUE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.creationDate").value(sameInstant(DEFAULT_CREATION_DATE)))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingDataViewFilters() throws Exception {
        // Get the dataViewFilters
        restDataViewFiltersMockMvc.perform(get("/api/data-view-filters/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDataViewFilters() throws Exception {
        // Initialize the database
        dataViewFiltersRepository.saveAndFlush(dataViewFilters);
        int databaseSizeBeforeUpdate = dataViewFiltersRepository.findAll().size();

        // Update the dataViewFilters
        DataViewFilters updatedDataViewFilters = dataViewFiltersRepository.findOne(dataViewFilters.getId());
        updatedDataViewFilters
            .dataViewId(UPDATED_DATA_VIEW_ID)
            .refSrcType(UPDATED_REF_SRC_TYPE)
            .refSrcId(UPDATED_REF_SRC_ID)
            .refSrcColId(UPDATED_REF_SRC_COL_ID)
            .filterOperator(UPDATED_FILTER_OPERATOR)
            .filterValue(UPDATED_FILTER_VALUE)
            .createdBy(UPDATED_CREATED_BY)
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE);

        restDataViewFiltersMockMvc.perform(put("/api/data-view-filters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDataViewFilters)))
            .andExpect(status().isOk());

        // Validate the DataViewFilters in the database
        List<DataViewFilters> dataViewFiltersList = dataViewFiltersRepository.findAll();
        assertThat(dataViewFiltersList).hasSize(databaseSizeBeforeUpdate);
        DataViewFilters testDataViewFilters = dataViewFiltersList.get(dataViewFiltersList.size() - 1);
        assertThat(testDataViewFilters.getDataViewId()).isEqualTo(UPDATED_DATA_VIEW_ID);
        assertThat(testDataViewFilters.getRefSrcType()).isEqualTo(UPDATED_REF_SRC_TYPE);
        assertThat(testDataViewFilters.getRefSrcId()).isEqualTo(UPDATED_REF_SRC_ID);
        assertThat(testDataViewFilters.getRefSrcColId()).isEqualTo(UPDATED_REF_SRC_COL_ID);
        assertThat(testDataViewFilters.getFilterOperator()).isEqualTo(UPDATED_FILTER_OPERATOR);
        assertThat(testDataViewFilters.getFilterValue()).isEqualTo(UPDATED_FILTER_VALUE);
        assertThat(testDataViewFilters.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testDataViewFilters.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testDataViewFilters.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testDataViewFilters.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingDataViewFilters() throws Exception {
        int databaseSizeBeforeUpdate = dataViewFiltersRepository.findAll().size();

        // Create the DataViewFilters

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDataViewFiltersMockMvc.perform(put("/api/data-view-filters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataViewFilters)))
            .andExpect(status().isCreated());

        // Validate the DataViewFilters in the database
        List<DataViewFilters> dataViewFiltersList = dataViewFiltersRepository.findAll();
        assertThat(dataViewFiltersList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDataViewFilters() throws Exception {
        // Initialize the database
        dataViewFiltersRepository.saveAndFlush(dataViewFilters);
        int databaseSizeBeforeDelete = dataViewFiltersRepository.findAll().size();

        // Get the dataViewFilters
        restDataViewFiltersMockMvc.perform(delete("/api/data-view-filters/{id}", dataViewFilters.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DataViewFilters> dataViewFiltersList = dataViewFiltersRepository.findAll();
        assertThat(dataViewFiltersList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DataViewFilters.class);
        DataViewFilters dataViewFilters1 = new DataViewFilters();
        dataViewFilters1.setId(1L);
        DataViewFilters dataViewFilters2 = new DataViewFilters();
        dataViewFilters2.setId(dataViewFilters1.getId());
        assertThat(dataViewFilters1).isEqualTo(dataViewFilters2);
        dataViewFilters2.setId(2L);
        assertThat(dataViewFilters1).isNotEqualTo(dataViewFilters2);
        dataViewFilters1.setId(null);
        assertThat(dataViewFilters1).isNotEqualTo(dataViewFilters2);
    }
}
