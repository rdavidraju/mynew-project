package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.DataViews;
import com.nspl.app.repository.DataViewsRepository;
import com.nspl.app.repository.search.DataViewsSearchRepository;
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
 * Test class for the DataViewsResource REST controller.
 *
 * @see DataViewsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class DataViewsResourceIntTest {

    private static final Long DEFAULT_TENANT_ID = 1L;
    private static final Long UPDATED_TENANT_ID = 2L;

    private static final String DEFAULT_DATA_VIEW_DISP_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DATA_VIEW_DISP_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DATA_VIEW_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DATA_VIEW_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DATA_VIEW_OBJECT = "AAAAAAAAAA";
    private static final String UPDATED_DATA_VIEW_OBJECT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED_FLAG = false;
    private static final Boolean UPDATED_ENABLED_FLAG = true;

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private DataViewsRepository dataViewsRepository;

    @Autowired
    private DataViewsSearchRepository dataViewsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDataViewsMockMvc;

    private DataViews dataViews;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DataViewsResource dataViewsResource = new DataViewsResource(dataViewsRepository, dataViewsSearchRepository);
        this.restDataViewsMockMvc = MockMvcBuilders.standaloneSetup(dataViewsResource)
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
    public static DataViews createEntity(EntityManager em) {
        DataViews dataViews = new DataViews()
            .tenantId(DEFAULT_TENANT_ID)
            .dataViewDispName(DEFAULT_DATA_VIEW_DISP_NAME)
            .dataViewName(DEFAULT_DATA_VIEW_NAME)
            .dataViewObject(DEFAULT_DATA_VIEW_OBJECT)
            .enabledFlag(DEFAULT_ENABLED_FLAG)
            .createdBy(DEFAULT_CREATED_BY)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .creationDate(DEFAULT_CREATION_DATE)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE);
        return dataViews;
    }

    @Before
    public void initTest() {
        dataViewsSearchRepository.deleteAll();
        dataViews = createEntity(em);
    }

    @Test
    @Transactional
    public void createDataViews() throws Exception {
        int databaseSizeBeforeCreate = dataViewsRepository.findAll().size();

        // Create the DataViews
        restDataViewsMockMvc.perform(post("/api/data-views")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataViews)))
            .andExpect(status().isCreated());

        // Validate the DataViews in the database
        List<DataViews> dataViewsList = dataViewsRepository.findAll();
        assertThat(dataViewsList).hasSize(databaseSizeBeforeCreate + 1);
        DataViews testDataViews = dataViewsList.get(dataViewsList.size() - 1);
        assertThat(testDataViews.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testDataViews.getDataViewDispName()).isEqualTo(DEFAULT_DATA_VIEW_DISP_NAME);
        assertThat(testDataViews.getDataViewName()).isEqualTo(DEFAULT_DATA_VIEW_NAME);
        assertThat(testDataViews.getDataViewObject()).isEqualTo(DEFAULT_DATA_VIEW_OBJECT);
        assertThat(testDataViews.isEnabledFlag()).isEqualTo(DEFAULT_ENABLED_FLAG);
        assertThat(testDataViews.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testDataViews.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testDataViews.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testDataViews.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);

        // Validate the DataViews in Elasticsearch
        DataViews dataViewsEs = dataViewsSearchRepository.findOne(testDataViews.getId());
        assertThat(dataViewsEs).isEqualToComparingFieldByField(testDataViews);
    }

    @Test
    @Transactional
    public void createDataViewsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dataViewsRepository.findAll().size();

        // Create the DataViews with an existing ID
        dataViews.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDataViewsMockMvc.perform(post("/api/data-views")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataViews)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<DataViews> dataViewsList = dataViewsRepository.findAll();
        assertThat(dataViewsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDataViews() throws Exception {
        // Initialize the database
        dataViewsRepository.saveAndFlush(dataViews);

        // Get all the dataViewsList
        restDataViewsMockMvc.perform(get("/api/data-views?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataViews.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].dataViewDispName").value(hasItem(DEFAULT_DATA_VIEW_DISP_NAME.toString())))
            .andExpect(jsonPath("$.[*].dataViewName").value(hasItem(DEFAULT_DATA_VIEW_NAME.toString())))
            .andExpect(jsonPath("$.[*].dataViewObject").value(hasItem(DEFAULT_DATA_VIEW_OBJECT.toString())))
            .andExpect(jsonPath("$.[*].enabledFlag").value(hasItem(DEFAULT_ENABLED_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void getDataViews() throws Exception {
        // Initialize the database
        dataViewsRepository.saveAndFlush(dataViews);

        // Get the dataViews
        restDataViewsMockMvc.perform(get("/api/data-views/{id}", dataViews.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dataViews.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID.intValue()))
            .andExpect(jsonPath("$.dataViewDispName").value(DEFAULT_DATA_VIEW_DISP_NAME.toString()))
            .andExpect(jsonPath("$.dataViewName").value(DEFAULT_DATA_VIEW_NAME.toString()))
            .andExpect(jsonPath("$.dataViewObject").value(DEFAULT_DATA_VIEW_OBJECT.toString()))
            .andExpect(jsonPath("$.enabledFlag").value(DEFAULT_ENABLED_FLAG.booleanValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.creationDate").value(sameInstant(DEFAULT_CREATION_DATE)))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingDataViews() throws Exception {
        // Get the dataViews
        restDataViewsMockMvc.perform(get("/api/data-views/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDataViews() throws Exception {
        // Initialize the database
        dataViewsRepository.saveAndFlush(dataViews);
        dataViewsSearchRepository.save(dataViews);
        int databaseSizeBeforeUpdate = dataViewsRepository.findAll().size();

        // Update the dataViews
        DataViews updatedDataViews = dataViewsRepository.findOne(dataViews.getId());
        updatedDataViews
            .tenantId(UPDATED_TENANT_ID)
            .dataViewDispName(UPDATED_DATA_VIEW_DISP_NAME)
            .dataViewName(UPDATED_DATA_VIEW_NAME)
            .dataViewObject(UPDATED_DATA_VIEW_OBJECT)
            .enabledFlag(UPDATED_ENABLED_FLAG)
            .createdBy(UPDATED_CREATED_BY)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE);

        restDataViewsMockMvc.perform(put("/api/data-views")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDataViews)))
            .andExpect(status().isOk());

        // Validate the DataViews in the database
        List<DataViews> dataViewsList = dataViewsRepository.findAll();
        assertThat(dataViewsList).hasSize(databaseSizeBeforeUpdate);
        DataViews testDataViews = dataViewsList.get(dataViewsList.size() - 1);
        assertThat(testDataViews.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testDataViews.getDataViewDispName()).isEqualTo(UPDATED_DATA_VIEW_DISP_NAME);
        assertThat(testDataViews.getDataViewName()).isEqualTo(UPDATED_DATA_VIEW_NAME);
        assertThat(testDataViews.getDataViewObject()).isEqualTo(UPDATED_DATA_VIEW_OBJECT);
        assertThat(testDataViews.isEnabledFlag()).isEqualTo(UPDATED_ENABLED_FLAG);
        assertThat(testDataViews.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testDataViews.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testDataViews.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testDataViews.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);

        // Validate the DataViews in Elasticsearch
        DataViews dataViewsEs = dataViewsSearchRepository.findOne(testDataViews.getId());
        assertThat(dataViewsEs).isEqualToComparingFieldByField(testDataViews);
    }

    @Test
    @Transactional
    public void updateNonExistingDataViews() throws Exception {
        int databaseSizeBeforeUpdate = dataViewsRepository.findAll().size();

        // Create the DataViews

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDataViewsMockMvc.perform(put("/api/data-views")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataViews)))
            .andExpect(status().isCreated());

        // Validate the DataViews in the database
        List<DataViews> dataViewsList = dataViewsRepository.findAll();
        assertThat(dataViewsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDataViews() throws Exception {
        // Initialize the database
        dataViewsRepository.saveAndFlush(dataViews);
        dataViewsSearchRepository.save(dataViews);
        int databaseSizeBeforeDelete = dataViewsRepository.findAll().size();

        // Get the dataViews
        restDataViewsMockMvc.perform(delete("/api/data-views/{id}", dataViews.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean dataViewsExistsInEs = dataViewsSearchRepository.exists(dataViews.getId());
        assertThat(dataViewsExistsInEs).isFalse();

        // Validate the database is empty
        List<DataViews> dataViewsList = dataViewsRepository.findAll();
        assertThat(dataViewsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDataViews() throws Exception {
        // Initialize the database
        dataViewsRepository.saveAndFlush(dataViews);
        dataViewsSearchRepository.save(dataViews);

        // Search the dataViews
        restDataViewsMockMvc.perform(get("/api/_search/data-views?query=id:" + dataViews.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataViews.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].dataViewDispName").value(hasItem(DEFAULT_DATA_VIEW_DISP_NAME.toString())))
            .andExpect(jsonPath("$.[*].dataViewName").value(hasItem(DEFAULT_DATA_VIEW_NAME.toString())))
            .andExpect(jsonPath("$.[*].dataViewObject").value(hasItem(DEFAULT_DATA_VIEW_OBJECT.toString())))
            .andExpect(jsonPath("$.[*].enabledFlag").value(hasItem(DEFAULT_ENABLED_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DataViews.class);
    }
}
