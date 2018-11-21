package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.DataViewConditions;
import com.nspl.app.repository.DataViewConditionsRepository;
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
 * Test class for the DataViewConditionsResource REST controller.
 *
 * @see DataViewConditionsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class DataViewConditionsResourceIntTest {

    private static final String DEFAULT_REF_SRC_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_REF_SRC_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_REF_SRC_ID = 1L;
    private static final Long UPDATED_REF_SRC_ID = 2L;

    private static final Long DEFAULT_REF_SRC_COL_ID = 1L;
    private static final Long UPDATED_REF_SRC_COL_ID = 2L;

    private static final String DEFAULT_FILTER_OPERATOR = "AAAAAAAAAA";
    private static final String UPDATED_FILTER_OPERATOR = "BBBBBBBBBB";

    private static final String DEFAULT_REF_SRC_TYPE_2 = "AAAAAAAAAA";
    private static final String UPDATED_REF_SRC_TYPE_2 = "BBBBBBBBBB";

    private static final Long DEFAULT_REF_SRC_ID_2 = 1L;
    private static final Long UPDATED_REF_SRC_ID_2 = 2L;

    private static final Long DEFAULT_REF_SRC_COL_ID_2 = 1L;
    private static final Long UPDATED_REF_SRC_COL_ID_2 = 2L;

    private static final String DEFAULT_LOGICAL_OPERATOR = "AAAAAAAAAA";
    private static final String UPDATED_LOGICAL_OPERATOR = "BBBBBBBBBB";

    @Autowired
    private DataViewConditionsRepository dataViewConditionsRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDataViewConditionsMockMvc;

    private DataViewConditions dataViewConditions;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DataViewConditionsResource dataViewConditionsResource = new DataViewConditionsResource(dataViewConditionsRepository);
        this.restDataViewConditionsMockMvc = MockMvcBuilders.standaloneSetup(dataViewConditionsResource)
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
    public static DataViewConditions createEntity(EntityManager em) {
        DataViewConditions dataViewConditions = new DataViewConditions()
            .refSrcType(DEFAULT_REF_SRC_TYPE)
            .refSrcId(DEFAULT_REF_SRC_ID)
            .refSrcColId(DEFAULT_REF_SRC_COL_ID)
            .filterOperator(DEFAULT_FILTER_OPERATOR)
            .refSrcType2(DEFAULT_REF_SRC_TYPE_2)
            .refSrcId2(DEFAULT_REF_SRC_ID_2)
            .refSrcColId2(DEFAULT_REF_SRC_COL_ID_2)
            .logicalOperator(DEFAULT_LOGICAL_OPERATOR);
        return dataViewConditions;
    }

    @Before
    public void initTest() {
        dataViewConditions = createEntity(em);
    }

    @Test
    @Transactional
    public void createDataViewConditions() throws Exception {
        int databaseSizeBeforeCreate = dataViewConditionsRepository.findAll().size();

        // Create the DataViewConditions
        restDataViewConditionsMockMvc.perform(post("/api/data-view-conditions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataViewConditions)))
            .andExpect(status().isCreated());

        // Validate the DataViewConditions in the database
        List<DataViewConditions> dataViewConditionsList = dataViewConditionsRepository.findAll();
        assertThat(dataViewConditionsList).hasSize(databaseSizeBeforeCreate + 1);
        DataViewConditions testDataViewConditions = dataViewConditionsList.get(dataViewConditionsList.size() - 1);
        assertThat(testDataViewConditions.getRefSrcType()).isEqualTo(DEFAULT_REF_SRC_TYPE);
        assertThat(testDataViewConditions.getRefSrcId()).isEqualTo(DEFAULT_REF_SRC_ID);
        assertThat(testDataViewConditions.getRefSrcColId()).isEqualTo(DEFAULT_REF_SRC_COL_ID);
        assertThat(testDataViewConditions.getFilterOperator()).isEqualTo(DEFAULT_FILTER_OPERATOR);
        assertThat(testDataViewConditions.getRefSrcType2()).isEqualTo(DEFAULT_REF_SRC_TYPE_2);
        assertThat(testDataViewConditions.getRefSrcId2()).isEqualTo(DEFAULT_REF_SRC_ID_2);
        assertThat(testDataViewConditions.getRefSrcColId2()).isEqualTo(DEFAULT_REF_SRC_COL_ID_2);
        assertThat(testDataViewConditions.getLogicalOperator()).isEqualTo(DEFAULT_LOGICAL_OPERATOR);
    }

    @Test
    @Transactional
    public void createDataViewConditionsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dataViewConditionsRepository.findAll().size();

        // Create the DataViewConditions with an existing ID
        dataViewConditions.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDataViewConditionsMockMvc.perform(post("/api/data-view-conditions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataViewConditions)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<DataViewConditions> dataViewConditionsList = dataViewConditionsRepository.findAll();
        assertThat(dataViewConditionsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDataViewConditions() throws Exception {
        // Initialize the database
        dataViewConditionsRepository.saveAndFlush(dataViewConditions);

        // Get all the dataViewConditionsList
        restDataViewConditionsMockMvc.perform(get("/api/data-view-conditions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataViewConditions.getId().intValue())))
            .andExpect(jsonPath("$.[*].refSrcType").value(hasItem(DEFAULT_REF_SRC_TYPE.toString())))
            .andExpect(jsonPath("$.[*].refSrcId").value(hasItem(DEFAULT_REF_SRC_ID.intValue())))
            .andExpect(jsonPath("$.[*].refSrcColId").value(hasItem(DEFAULT_REF_SRC_COL_ID.intValue())))
            .andExpect(jsonPath("$.[*].filterOperator").value(hasItem(DEFAULT_FILTER_OPERATOR.toString())))
            .andExpect(jsonPath("$.[*].refSrcType2").value(hasItem(DEFAULT_REF_SRC_TYPE_2.toString())))
            .andExpect(jsonPath("$.[*].refSrcId2").value(hasItem(DEFAULT_REF_SRC_ID_2.intValue())))
            .andExpect(jsonPath("$.[*].refSrcColId2").value(hasItem(DEFAULT_REF_SRC_COL_ID_2.intValue())))
            .andExpect(jsonPath("$.[*].logicalOperator").value(hasItem(DEFAULT_LOGICAL_OPERATOR.toString())));
    }

    @Test
    @Transactional
    public void getDataViewConditions() throws Exception {
        // Initialize the database
        dataViewConditionsRepository.saveAndFlush(dataViewConditions);

        // Get the dataViewConditions
        restDataViewConditionsMockMvc.perform(get("/api/data-view-conditions/{id}", dataViewConditions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dataViewConditions.getId().intValue()))
            .andExpect(jsonPath("$.refSrcType").value(DEFAULT_REF_SRC_TYPE.toString()))
            .andExpect(jsonPath("$.refSrcId").value(DEFAULT_REF_SRC_ID.intValue()))
            .andExpect(jsonPath("$.refSrcColId").value(DEFAULT_REF_SRC_COL_ID.intValue()))
            .andExpect(jsonPath("$.filterOperator").value(DEFAULT_FILTER_OPERATOR.toString()))
            .andExpect(jsonPath("$.refSrcType2").value(DEFAULT_REF_SRC_TYPE_2.toString()))
            .andExpect(jsonPath("$.refSrcId2").value(DEFAULT_REF_SRC_ID_2.intValue()))
            .andExpect(jsonPath("$.refSrcColId2").value(DEFAULT_REF_SRC_COL_ID_2.intValue()))
            .andExpect(jsonPath("$.logicalOperator").value(DEFAULT_LOGICAL_OPERATOR.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDataViewConditions() throws Exception {
        // Get the dataViewConditions
        restDataViewConditionsMockMvc.perform(get("/api/data-view-conditions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDataViewConditions() throws Exception {
        // Initialize the database
        dataViewConditionsRepository.saveAndFlush(dataViewConditions);
        int databaseSizeBeforeUpdate = dataViewConditionsRepository.findAll().size();

        // Update the dataViewConditions
        DataViewConditions updatedDataViewConditions = dataViewConditionsRepository.findOne(dataViewConditions.getId());
        updatedDataViewConditions
            .refSrcType(UPDATED_REF_SRC_TYPE)
            .refSrcId(UPDATED_REF_SRC_ID)
            .refSrcColId(UPDATED_REF_SRC_COL_ID)
            .filterOperator(UPDATED_FILTER_OPERATOR)
            .refSrcType2(UPDATED_REF_SRC_TYPE_2)
            .refSrcId2(UPDATED_REF_SRC_ID_2)
            .refSrcColId2(UPDATED_REF_SRC_COL_ID_2)
            .logicalOperator(UPDATED_LOGICAL_OPERATOR);

        restDataViewConditionsMockMvc.perform(put("/api/data-view-conditions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDataViewConditions)))
            .andExpect(status().isOk());

        // Validate the DataViewConditions in the database
        List<DataViewConditions> dataViewConditionsList = dataViewConditionsRepository.findAll();
        assertThat(dataViewConditionsList).hasSize(databaseSizeBeforeUpdate);
        DataViewConditions testDataViewConditions = dataViewConditionsList.get(dataViewConditionsList.size() - 1);
        assertThat(testDataViewConditions.getRefSrcType()).isEqualTo(UPDATED_REF_SRC_TYPE);
        assertThat(testDataViewConditions.getRefSrcId()).isEqualTo(UPDATED_REF_SRC_ID);
        assertThat(testDataViewConditions.getRefSrcColId()).isEqualTo(UPDATED_REF_SRC_COL_ID);
        assertThat(testDataViewConditions.getFilterOperator()).isEqualTo(UPDATED_FILTER_OPERATOR);
        assertThat(testDataViewConditions.getRefSrcType2()).isEqualTo(UPDATED_REF_SRC_TYPE_2);
        assertThat(testDataViewConditions.getRefSrcId2()).isEqualTo(UPDATED_REF_SRC_ID_2);
        assertThat(testDataViewConditions.getRefSrcColId2()).isEqualTo(UPDATED_REF_SRC_COL_ID_2);
        assertThat(testDataViewConditions.getLogicalOperator()).isEqualTo(UPDATED_LOGICAL_OPERATOR);
    }

    @Test
    @Transactional
    public void updateNonExistingDataViewConditions() throws Exception {
        int databaseSizeBeforeUpdate = dataViewConditionsRepository.findAll().size();

        // Create the DataViewConditions

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDataViewConditionsMockMvc.perform(put("/api/data-view-conditions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataViewConditions)))
            .andExpect(status().isCreated());

        // Validate the DataViewConditions in the database
        List<DataViewConditions> dataViewConditionsList = dataViewConditionsRepository.findAll();
        assertThat(dataViewConditionsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDataViewConditions() throws Exception {
        // Initialize the database
        dataViewConditionsRepository.saveAndFlush(dataViewConditions);
        int databaseSizeBeforeDelete = dataViewConditionsRepository.findAll().size();

        // Get the dataViewConditions
        restDataViewConditionsMockMvc.perform(delete("/api/data-view-conditions/{id}", dataViewConditions.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DataViewConditions> dataViewConditionsList = dataViewConditionsRepository.findAll();
        assertThat(dataViewConditionsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DataViewConditions.class);
        DataViewConditions dataViewConditions1 = new DataViewConditions();
        dataViewConditions1.setId(1L);
        DataViewConditions dataViewConditions2 = new DataViewConditions();
        dataViewConditions2.setId(dataViewConditions1.getId());
        assertThat(dataViewConditions1).isEqualTo(dataViewConditions2);
        dataViewConditions2.setId(2L);
        assertThat(dataViewConditions1).isNotEqualTo(dataViewConditions2);
        dataViewConditions1.setId(null);
        assertThat(dataViewConditions1).isNotEqualTo(dataViewConditions2);
    }
}
