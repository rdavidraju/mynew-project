package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.RowConditions;
import com.nspl.app.repository.RowConditionsRepository;
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
 * Test class for the RowConditionsResource REST controller.
 *
 * @see RowConditionsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class RowConditionsResourceIntTest {

    private static final Long DEFAULT_TEMPLATE_LINE_ID = 1L;
    private static final Long UPDATED_TEMPLATE_LINE_ID = 2L;

    private static final String DEFAULT_OPERATOR = "AAAAAAAAAA";
    private static final String UPDATED_OPERATOR = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    @Autowired
    private RowConditionsRepository rowConditionsRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRowConditionsMockMvc;

    private RowConditions rowConditions;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RowConditionsResource rowConditionsResource = new RowConditionsResource(rowConditionsRepository);
        this.restRowConditionsMockMvc = MockMvcBuilders.standaloneSetup(rowConditionsResource)
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
    public static RowConditions createEntity(EntityManager em) {
        RowConditions rowConditions = new RowConditions()
            .templateLineId(DEFAULT_TEMPLATE_LINE_ID)
            .operator(DEFAULT_OPERATOR)
            .value(DEFAULT_VALUE)
            .type(DEFAULT_TYPE);
        return rowConditions;
    }

    @Before
    public void initTest() {
        rowConditions = createEntity(em);
    }

    @Test
    @Transactional
    public void createRowConditions() throws Exception {
        int databaseSizeBeforeCreate = rowConditionsRepository.findAll().size();

        // Create the RowConditions
        restRowConditionsMockMvc.perform(post("/api/row-conditions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rowConditions)))
            .andExpect(status().isCreated());

        // Validate the RowConditions in the database
        List<RowConditions> rowConditionsList = rowConditionsRepository.findAll();
        assertThat(rowConditionsList).hasSize(databaseSizeBeforeCreate + 1);
        RowConditions testRowConditions = rowConditionsList.get(rowConditionsList.size() - 1);
        assertThat(testRowConditions.getTemplateLineId()).isEqualTo(DEFAULT_TEMPLATE_LINE_ID);
        assertThat(testRowConditions.getOperator()).isEqualTo(DEFAULT_OPERATOR);
        assertThat(testRowConditions.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testRowConditions.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createRowConditionsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rowConditionsRepository.findAll().size();

        // Create the RowConditions with an existing ID
        rowConditions.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRowConditionsMockMvc.perform(post("/api/row-conditions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rowConditions)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<RowConditions> rowConditionsList = rowConditionsRepository.findAll();
        assertThat(rowConditionsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRowConditions() throws Exception {
        // Initialize the database
        rowConditionsRepository.saveAndFlush(rowConditions);

        // Get all the rowConditionsList
        restRowConditionsMockMvc.perform(get("/api/row-conditions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rowConditions.getId().intValue())))
            .andExpect(jsonPath("$.[*].templateLineId").value(hasItem(DEFAULT_TEMPLATE_LINE_ID.intValue())))
            .andExpect(jsonPath("$.[*].operator").value(hasItem(DEFAULT_OPERATOR.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getRowConditions() throws Exception {
        // Initialize the database
        rowConditionsRepository.saveAndFlush(rowConditions);

        // Get the rowConditions
        restRowConditionsMockMvc.perform(get("/api/row-conditions/{id}", rowConditions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rowConditions.getId().intValue()))
            .andExpect(jsonPath("$.templateLineId").value(DEFAULT_TEMPLATE_LINE_ID.intValue()))
            .andExpect(jsonPath("$.operator").value(DEFAULT_OPERATOR.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRowConditions() throws Exception {
        // Get the rowConditions
        restRowConditionsMockMvc.perform(get("/api/row-conditions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRowConditions() throws Exception {
        // Initialize the database
        rowConditionsRepository.saveAndFlush(rowConditions);
        int databaseSizeBeforeUpdate = rowConditionsRepository.findAll().size();

        // Update the rowConditions
        RowConditions updatedRowConditions = rowConditionsRepository.findOne(rowConditions.getId());
        updatedRowConditions
            .templateLineId(UPDATED_TEMPLATE_LINE_ID)
            .operator(UPDATED_OPERATOR)
            .value(UPDATED_VALUE)
            .type(UPDATED_TYPE);

        restRowConditionsMockMvc.perform(put("/api/row-conditions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRowConditions)))
            .andExpect(status().isOk());

        // Validate the RowConditions in the database
        List<RowConditions> rowConditionsList = rowConditionsRepository.findAll();
        assertThat(rowConditionsList).hasSize(databaseSizeBeforeUpdate);
        RowConditions testRowConditions = rowConditionsList.get(rowConditionsList.size() - 1);
        assertThat(testRowConditions.getTemplateLineId()).isEqualTo(UPDATED_TEMPLATE_LINE_ID);
        assertThat(testRowConditions.getOperator()).isEqualTo(UPDATED_OPERATOR);
        assertThat(testRowConditions.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testRowConditions.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingRowConditions() throws Exception {
        int databaseSizeBeforeUpdate = rowConditionsRepository.findAll().size();

        // Create the RowConditions

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRowConditionsMockMvc.perform(put("/api/row-conditions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rowConditions)))
            .andExpect(status().isCreated());

        // Validate the RowConditions in the database
        List<RowConditions> rowConditionsList = rowConditionsRepository.findAll();
        assertThat(rowConditionsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRowConditions() throws Exception {
        // Initialize the database
        rowConditionsRepository.saveAndFlush(rowConditions);
        int databaseSizeBeforeDelete = rowConditionsRepository.findAll().size();

        // Get the rowConditions
        restRowConditionsMockMvc.perform(delete("/api/row-conditions/{id}", rowConditions.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RowConditions> rowConditionsList = rowConditionsRepository.findAll();
        assertThat(rowConditionsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RowConditions.class);
        RowConditions rowConditions1 = new RowConditions();
        rowConditions1.setId(1L);
        RowConditions rowConditions2 = new RowConditions();
        rowConditions2.setId(rowConditions1.getId());
        assertThat(rowConditions1).isEqualTo(rowConditions2);
        rowConditions2.setId(2L);
        assertThat(rowConditions1).isNotEqualTo(rowConditions2);
        rowConditions1.setId(null);
        assertThat(rowConditions1).isNotEqualTo(rowConditions2);
    }
}
