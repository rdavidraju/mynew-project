package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.JeLdrDetails;
import com.nspl.app.repository.JeLdrDetailsRepository;
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
 * Test class for the JeLdrDetailsResource REST controller.
 *
 * @see JeLdrDetailsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class JeLdrDetailsResourceIntTest {

    private static final Long DEFAULT_JE_TEMP_ID = 1L;
    private static final Long UPDATED_JE_TEMP_ID = 2L;

    private static final String DEFAULT_COL_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_COL_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_COL_VALUE = 1L;
    private static final Long UPDATED_COL_VALUE = 2L;

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private JeLdrDetailsRepository jeLdrDetailsRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restJeLdrDetailsMockMvc;

    private JeLdrDetails jeLdrDetails;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        JeLdrDetailsResource jeLdrDetailsResource = new JeLdrDetailsResource(jeLdrDetailsRepository);
        this.restJeLdrDetailsMockMvc = MockMvcBuilders.standaloneSetup(jeLdrDetailsResource)
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
    public static JeLdrDetails createEntity(EntityManager em) {
        JeLdrDetails jeLdrDetails = new JeLdrDetails()
            .jeTempId(DEFAULT_JE_TEMP_ID)
            .colType(DEFAULT_COL_TYPE)
            .colValue(DEFAULT_COL_VALUE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE);
        return jeLdrDetails;
    }

    @Before
    public void initTest() {
        jeLdrDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createJeLdrDetails() throws Exception {
        int databaseSizeBeforeCreate = jeLdrDetailsRepository.findAll().size();

        // Create the JeLdrDetails
        restJeLdrDetailsMockMvc.perform(post("/api/je-ldr-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jeLdrDetails)))
            .andExpect(status().isCreated());

        // Validate the JeLdrDetails in the database
        List<JeLdrDetails> jeLdrDetailsList = jeLdrDetailsRepository.findAll();
        assertThat(jeLdrDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        JeLdrDetails testJeLdrDetails = jeLdrDetailsList.get(jeLdrDetailsList.size() - 1);
        assertThat(testJeLdrDetails.getJeTempId()).isEqualTo(DEFAULT_JE_TEMP_ID);
        assertThat(testJeLdrDetails.getColType()).isEqualTo(DEFAULT_COL_TYPE);
        assertThat(testJeLdrDetails.getColValue()).isEqualTo(DEFAULT_COL_VALUE);
        assertThat(testJeLdrDetails.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testJeLdrDetails.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testJeLdrDetails.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testJeLdrDetails.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void createJeLdrDetailsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jeLdrDetailsRepository.findAll().size();

        // Create the JeLdrDetails with an existing ID
        jeLdrDetails.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJeLdrDetailsMockMvc.perform(post("/api/je-ldr-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jeLdrDetails)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<JeLdrDetails> jeLdrDetailsList = jeLdrDetailsRepository.findAll();
        assertThat(jeLdrDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllJeLdrDetails() throws Exception {
        // Initialize the database
        jeLdrDetailsRepository.saveAndFlush(jeLdrDetails);

        // Get all the jeLdrDetailsList
        restJeLdrDetailsMockMvc.perform(get("/api/je-ldr-details?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jeLdrDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].jeTempId").value(hasItem(DEFAULT_JE_TEMP_ID.intValue())))
            .andExpect(jsonPath("$.[*].colType").value(hasItem(DEFAULT_COL_TYPE.toString())))
            .andExpect(jsonPath("$.[*].colValue").value(hasItem(DEFAULT_COL_VALUE.intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void getJeLdrDetails() throws Exception {
        // Initialize the database
        jeLdrDetailsRepository.saveAndFlush(jeLdrDetails);

        // Get the jeLdrDetails
        restJeLdrDetailsMockMvc.perform(get("/api/je-ldr-details/{id}", jeLdrDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(jeLdrDetails.getId().intValue()))
            .andExpect(jsonPath("$.jeTempId").value(DEFAULT_JE_TEMP_ID.intValue()))
            .andExpect(jsonPath("$.colType").value(DEFAULT_COL_TYPE.toString()))
            .andExpect(jsonPath("$.colValue").value(DEFAULT_COL_VALUE.intValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingJeLdrDetails() throws Exception {
        // Get the jeLdrDetails
        restJeLdrDetailsMockMvc.perform(get("/api/je-ldr-details/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJeLdrDetails() throws Exception {
        // Initialize the database
        jeLdrDetailsRepository.saveAndFlush(jeLdrDetails);
        int databaseSizeBeforeUpdate = jeLdrDetailsRepository.findAll().size();

        // Update the jeLdrDetails
        JeLdrDetails updatedJeLdrDetails = jeLdrDetailsRepository.findOne(jeLdrDetails.getId());
        updatedJeLdrDetails
            .jeTempId(UPDATED_JE_TEMP_ID)
            .colType(UPDATED_COL_TYPE)
            .colValue(UPDATED_COL_VALUE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE);

        restJeLdrDetailsMockMvc.perform(put("/api/je-ldr-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedJeLdrDetails)))
            .andExpect(status().isOk());

        // Validate the JeLdrDetails in the database
        List<JeLdrDetails> jeLdrDetailsList = jeLdrDetailsRepository.findAll();
        assertThat(jeLdrDetailsList).hasSize(databaseSizeBeforeUpdate);
        JeLdrDetails testJeLdrDetails = jeLdrDetailsList.get(jeLdrDetailsList.size() - 1);
        assertThat(testJeLdrDetails.getJeTempId()).isEqualTo(UPDATED_JE_TEMP_ID);
        assertThat(testJeLdrDetails.getColType()).isEqualTo(UPDATED_COL_TYPE);
        assertThat(testJeLdrDetails.getColValue()).isEqualTo(UPDATED_COL_VALUE);
        assertThat(testJeLdrDetails.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testJeLdrDetails.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testJeLdrDetails.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testJeLdrDetails.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingJeLdrDetails() throws Exception {
        int databaseSizeBeforeUpdate = jeLdrDetailsRepository.findAll().size();

        // Create the JeLdrDetails

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restJeLdrDetailsMockMvc.perform(put("/api/je-ldr-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jeLdrDetails)))
            .andExpect(status().isCreated());

        // Validate the JeLdrDetails in the database
        List<JeLdrDetails> jeLdrDetailsList = jeLdrDetailsRepository.findAll();
        assertThat(jeLdrDetailsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteJeLdrDetails() throws Exception {
        // Initialize the database
        jeLdrDetailsRepository.saveAndFlush(jeLdrDetails);
        int databaseSizeBeforeDelete = jeLdrDetailsRepository.findAll().size();

        // Get the jeLdrDetails
        restJeLdrDetailsMockMvc.perform(delete("/api/je-ldr-details/{id}", jeLdrDetails.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<JeLdrDetails> jeLdrDetailsList = jeLdrDetailsRepository.findAll();
        assertThat(jeLdrDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JeLdrDetails.class);
        JeLdrDetails jeLdrDetails1 = new JeLdrDetails();
        jeLdrDetails1.setId(1L);
        JeLdrDetails jeLdrDetails2 = new JeLdrDetails();
        jeLdrDetails2.setId(jeLdrDetails1.getId());
        assertThat(jeLdrDetails1).isEqualTo(jeLdrDetails2);
        jeLdrDetails2.setId(2L);
        assertThat(jeLdrDetails1).isNotEqualTo(jeLdrDetails2);
        jeLdrDetails1.setId(null);
        assertThat(jeLdrDetails1).isNotEqualTo(jeLdrDetails2);
    }
}
