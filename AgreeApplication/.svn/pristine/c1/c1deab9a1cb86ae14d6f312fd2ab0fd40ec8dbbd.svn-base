package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.DataChild;
import com.nspl.app.repository.DataChildRepository;
import com.nspl.app.repository.search.DataChildSearchRepository;
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
 * Test class for the DataChildResource REST controller.
 *
 * @see DataChildResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class DataChildResourceIntTest {

    private static final Long DEFAULT_TENANT_ID = 1L;
    private static final Long UPDATED_TENANT_ID = 2L;

    private static final Long DEFAULT_DATA_VIEW_ID = 1L;
    private static final Long UPDATED_DATA_VIEW_ID = 2L;

    private static final Long DEFAULT_MASTER_ROW_ID = 1L;
    private static final Long UPDATED_MASTER_ROW_ID = 2L;

    private static final String DEFAULT_ROW_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_ROW_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ADJUSTMENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ADJUSTMENT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_ADJUSTMENT_METHOD = "AAAAAAAAAA";
    private static final String UPDATED_ADJUSTMENT_METHOD = "BBBBBBBBBB";

    private static final Double DEFAULT_PERCENT_VALUE = 1D;
    private static final Double UPDATED_PERCENT_VALUE = 2D;

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private DataChildRepository dataChildRepository;

    @Autowired
    private DataChildSearchRepository dataChildSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDataChildMockMvc;

    private DataChild dataChild;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DataChildResource dataChildResource = new DataChildResource(dataChildRepository, dataChildSearchRepository);
        this.restDataChildMockMvc = MockMvcBuilders.standaloneSetup(dataChildResource)
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
    public static DataChild createEntity(EntityManager em) {
        DataChild dataChild = new DataChild()
            .tenantId(DEFAULT_TENANT_ID)
            .dataViewId(DEFAULT_DATA_VIEW_ID)
            .masterRowId(DEFAULT_MASTER_ROW_ID)
            .rowDescription(DEFAULT_ROW_DESCRIPTION)
            .adjustmentType(DEFAULT_ADJUSTMENT_TYPE)
            .adjustmentMethod(DEFAULT_ADJUSTMENT_METHOD)
            .percentValue(DEFAULT_PERCENT_VALUE)
            .amount(DEFAULT_AMOUNT)
            .createdBy(DEFAULT_CREATED_BY)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .creationDate(DEFAULT_CREATION_DATE)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE);
        return dataChild;
    }

    @Before
    public void initTest() {
        dataChildSearchRepository.deleteAll();
        dataChild = createEntity(em);
    }

    @Test
    @Transactional
    public void createDataChild() throws Exception {
        int databaseSizeBeforeCreate = dataChildRepository.findAll().size();

        // Create the DataChild
        restDataChildMockMvc.perform(post("/api/data-children")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataChild)))
            .andExpect(status().isCreated());

        // Validate the DataChild in the database
        List<DataChild> dataChildList = dataChildRepository.findAll();
        assertThat(dataChildList).hasSize(databaseSizeBeforeCreate + 1);
        DataChild testDataChild = dataChildList.get(dataChildList.size() - 1);
        assertThat(testDataChild.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testDataChild.getDataViewId()).isEqualTo(DEFAULT_DATA_VIEW_ID);
        assertThat(testDataChild.getMasterRowId()).isEqualTo(DEFAULT_MASTER_ROW_ID);
        assertThat(testDataChild.getRowDescription()).isEqualTo(DEFAULT_ROW_DESCRIPTION);
        assertThat(testDataChild.getAdjustmentType()).isEqualTo(DEFAULT_ADJUSTMENT_TYPE);
        assertThat(testDataChild.getAdjustmentMethod()).isEqualTo(DEFAULT_ADJUSTMENT_METHOD);
        assertThat(testDataChild.getPercentValue()).isEqualTo(DEFAULT_PERCENT_VALUE);
        assertThat(testDataChild.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testDataChild.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testDataChild.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testDataChild.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testDataChild.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);

        // Validate the DataChild in Elasticsearch
        DataChild dataChildEs = dataChildSearchRepository.findOne(testDataChild.getId());
        assertThat(dataChildEs).isEqualToComparingFieldByField(testDataChild);
    }

    @Test
    @Transactional
    public void createDataChildWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dataChildRepository.findAll().size();

        // Create the DataChild with an existing ID
        dataChild.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDataChildMockMvc.perform(post("/api/data-children")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataChild)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<DataChild> dataChildList = dataChildRepository.findAll();
        assertThat(dataChildList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDataChildren() throws Exception {
        // Initialize the database
        dataChildRepository.saveAndFlush(dataChild);

        // Get all the dataChildList
        restDataChildMockMvc.perform(get("/api/data-children?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataChild.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].dataViewId").value(hasItem(DEFAULT_DATA_VIEW_ID.intValue())))
            .andExpect(jsonPath("$.[*].masterRowId").value(hasItem(DEFAULT_MASTER_ROW_ID.intValue())))
            .andExpect(jsonPath("$.[*].rowDescription").value(hasItem(DEFAULT_ROW_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].adjustmentType").value(hasItem(DEFAULT_ADJUSTMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].adjustmentMethod").value(hasItem(DEFAULT_ADJUSTMENT_METHOD.toString())))
            .andExpect(jsonPath("$.[*].percentValue").value(hasItem(DEFAULT_PERCENT_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void getDataChild() throws Exception {
        // Initialize the database
        dataChildRepository.saveAndFlush(dataChild);

        // Get the dataChild
        restDataChildMockMvc.perform(get("/api/data-children/{id}", dataChild.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dataChild.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID.intValue()))
            .andExpect(jsonPath("$.dataViewId").value(DEFAULT_DATA_VIEW_ID.intValue()))
            .andExpect(jsonPath("$.masterRowId").value(DEFAULT_MASTER_ROW_ID.intValue()))
            .andExpect(jsonPath("$.rowDescription").value(DEFAULT_ROW_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.adjustmentType").value(DEFAULT_ADJUSTMENT_TYPE.toString()))
            .andExpect(jsonPath("$.adjustmentMethod").value(DEFAULT_ADJUSTMENT_METHOD.toString()))
            .andExpect(jsonPath("$.percentValue").value(DEFAULT_PERCENT_VALUE.doubleValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.creationDate").value(sameInstant(DEFAULT_CREATION_DATE)))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingDataChild() throws Exception {
        // Get the dataChild
        restDataChildMockMvc.perform(get("/api/data-children/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDataChild() throws Exception {
        // Initialize the database
        dataChildRepository.saveAndFlush(dataChild);
        dataChildSearchRepository.save(dataChild);
        int databaseSizeBeforeUpdate = dataChildRepository.findAll().size();

        // Update the dataChild
        DataChild updatedDataChild = dataChildRepository.findOne(dataChild.getId());
        updatedDataChild
            .tenantId(UPDATED_TENANT_ID)
            .dataViewId(UPDATED_DATA_VIEW_ID)
            .masterRowId(UPDATED_MASTER_ROW_ID)
            .rowDescription(UPDATED_ROW_DESCRIPTION)
            .adjustmentType(UPDATED_ADJUSTMENT_TYPE)
            .adjustmentMethod(UPDATED_ADJUSTMENT_METHOD)
            .percentValue(UPDATED_PERCENT_VALUE)
            .amount(UPDATED_AMOUNT)
            .createdBy(UPDATED_CREATED_BY)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE);

        restDataChildMockMvc.perform(put("/api/data-children")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDataChild)))
            .andExpect(status().isOk());

        // Validate the DataChild in the database
        List<DataChild> dataChildList = dataChildRepository.findAll();
        assertThat(dataChildList).hasSize(databaseSizeBeforeUpdate);
        DataChild testDataChild = dataChildList.get(dataChildList.size() - 1);
        assertThat(testDataChild.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testDataChild.getDataViewId()).isEqualTo(UPDATED_DATA_VIEW_ID);
        assertThat(testDataChild.getMasterRowId()).isEqualTo(UPDATED_MASTER_ROW_ID);
        assertThat(testDataChild.getRowDescription()).isEqualTo(UPDATED_ROW_DESCRIPTION);
        assertThat(testDataChild.getAdjustmentType()).isEqualTo(UPDATED_ADJUSTMENT_TYPE);
        assertThat(testDataChild.getAdjustmentMethod()).isEqualTo(UPDATED_ADJUSTMENT_METHOD);
        assertThat(testDataChild.getPercentValue()).isEqualTo(UPDATED_PERCENT_VALUE);
        assertThat(testDataChild.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testDataChild.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testDataChild.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testDataChild.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testDataChild.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);

        // Validate the DataChild in Elasticsearch
        DataChild dataChildEs = dataChildSearchRepository.findOne(testDataChild.getId());
        assertThat(dataChildEs).isEqualToComparingFieldByField(testDataChild);
    }

    @Test
    @Transactional
    public void updateNonExistingDataChild() throws Exception {
        int databaseSizeBeforeUpdate = dataChildRepository.findAll().size();

        // Create the DataChild

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDataChildMockMvc.perform(put("/api/data-children")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataChild)))
            .andExpect(status().isCreated());

        // Validate the DataChild in the database
        List<DataChild> dataChildList = dataChildRepository.findAll();
        assertThat(dataChildList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDataChild() throws Exception {
        // Initialize the database
        dataChildRepository.saveAndFlush(dataChild);
        dataChildSearchRepository.save(dataChild);
        int databaseSizeBeforeDelete = dataChildRepository.findAll().size();

        // Get the dataChild
        restDataChildMockMvc.perform(delete("/api/data-children/{id}", dataChild.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean dataChildExistsInEs = dataChildSearchRepository.exists(dataChild.getId());
        assertThat(dataChildExistsInEs).isFalse();

        // Validate the database is empty
        List<DataChild> dataChildList = dataChildRepository.findAll();
        assertThat(dataChildList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDataChild() throws Exception {
        // Initialize the database
        dataChildRepository.saveAndFlush(dataChild);
        dataChildSearchRepository.save(dataChild);

        // Search the dataChild
        restDataChildMockMvc.perform(get("/api/_search/data-children?query=id:" + dataChild.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataChild.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].dataViewId").value(hasItem(DEFAULT_DATA_VIEW_ID.intValue())))
            .andExpect(jsonPath("$.[*].masterRowId").value(hasItem(DEFAULT_MASTER_ROW_ID.intValue())))
            .andExpect(jsonPath("$.[*].rowDescription").value(hasItem(DEFAULT_ROW_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].adjustmentType").value(hasItem(DEFAULT_ADJUSTMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].adjustmentMethod").value(hasItem(DEFAULT_ADJUSTMENT_METHOD.toString())))
            .andExpect(jsonPath("$.[*].percentValue").value(hasItem(DEFAULT_PERCENT_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DataChild.class);
    }
}
