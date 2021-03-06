package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.FxRates;
import com.nspl.app.repository.FxRatesRepository;
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
import java.time.LocalDate;
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
 * Test class for the FxRatesResource REST controller.
 *
 * @see FxRatesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class FxRatesResourceIntTest {

    private static final Long DEFAULT_TENANT_ID = 1L;
    private static final Long UPDATED_TENANT_ID = 2L;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CONVERSION_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CONVERSION_TYPE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_ENABLED_FLAG = false;
    private static final Boolean UPDATED_ENABLED_FLAG = true;

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private FxRatesRepository fxRatesRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFxRatesMockMvc;

    private FxRates fxRates;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FxRatesResource fxRatesResource = new FxRatesResource(fxRatesRepository);
        this.restFxRatesMockMvc = MockMvcBuilders.standaloneSetup(fxRatesResource)
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
    public static FxRates createEntity(EntityManager em) {
        FxRates fxRates = new FxRates()
            .tenantId(DEFAULT_TENANT_ID)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .conversionType(DEFAULT_CONVERSION_TYPE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .enabledFlag(DEFAULT_ENABLED_FLAG)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE);
        return fxRates;
    }

    @Before
    public void initTest() {
        fxRates = createEntity(em);
    }

    @Test
    @Transactional
    public void createFxRates() throws Exception {
        int databaseSizeBeforeCreate = fxRatesRepository.findAll().size();

        // Create the FxRates
        restFxRatesMockMvc.perform(post("/api/fx-rates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fxRates)))
            .andExpect(status().isCreated());

        // Validate the FxRates in the database
        List<FxRates> fxRatesList = fxRatesRepository.findAll();
        assertThat(fxRatesList).hasSize(databaseSizeBeforeCreate + 1);
        FxRates testFxRates = fxRatesList.get(fxRatesList.size() - 1);
        assertThat(testFxRates.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testFxRates.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFxRates.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFxRates.getConversionType()).isEqualTo(DEFAULT_CONVERSION_TYPE);
        assertThat(testFxRates.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testFxRates.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testFxRates.isEnabledFlag()).isEqualTo(DEFAULT_ENABLED_FLAG);
        assertThat(testFxRates.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testFxRates.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testFxRates.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testFxRates.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void createFxRatesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fxRatesRepository.findAll().size();

        // Create the FxRates with an existing ID
        fxRates.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFxRatesMockMvc.perform(post("/api/fx-rates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fxRates)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<FxRates> fxRatesList = fxRatesRepository.findAll();
        assertThat(fxRatesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllFxRates() throws Exception {
        // Initialize the database
        fxRatesRepository.saveAndFlush(fxRates);

        // Get all the fxRatesList
        restFxRatesMockMvc.perform(get("/api/fx-rates?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fxRates.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].conversionType").value(hasItem(DEFAULT_CONVERSION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].enabledFlag").value(hasItem(DEFAULT_ENABLED_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void getFxRates() throws Exception {
        // Initialize the database
        fxRatesRepository.saveAndFlush(fxRates);

        // Get the fxRates
        restFxRatesMockMvc.perform(get("/api/fx-rates/{id}", fxRates.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(fxRates.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.conversionType").value(DEFAULT_CONVERSION_TYPE.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.enabledFlag").value(DEFAULT_ENABLED_FLAG.booleanValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingFxRates() throws Exception {
        // Get the fxRates
        restFxRatesMockMvc.perform(get("/api/fx-rates/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFxRates() throws Exception {
        // Initialize the database
        fxRatesRepository.saveAndFlush(fxRates);
        int databaseSizeBeforeUpdate = fxRatesRepository.findAll().size();

        // Update the fxRates
        FxRates updatedFxRates = fxRatesRepository.findOne(fxRates.getId());
        updatedFxRates
            .tenantId(UPDATED_TENANT_ID)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .conversionType(UPDATED_CONVERSION_TYPE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .enabledFlag(UPDATED_ENABLED_FLAG)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE);

        restFxRatesMockMvc.perform(put("/api/fx-rates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFxRates)))
            .andExpect(status().isOk());

        // Validate the FxRates in the database
        List<FxRates> fxRatesList = fxRatesRepository.findAll();
        assertThat(fxRatesList).hasSize(databaseSizeBeforeUpdate);
        FxRates testFxRates = fxRatesList.get(fxRatesList.size() - 1);
        assertThat(testFxRates.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testFxRates.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFxRates.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFxRates.getConversionType()).isEqualTo(UPDATED_CONVERSION_TYPE);
        assertThat(testFxRates.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testFxRates.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testFxRates.isEnabledFlag()).isEqualTo(UPDATED_ENABLED_FLAG);
        assertThat(testFxRates.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testFxRates.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testFxRates.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testFxRates.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingFxRates() throws Exception {
        int databaseSizeBeforeUpdate = fxRatesRepository.findAll().size();

        // Create the FxRates

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFxRatesMockMvc.perform(put("/api/fx-rates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fxRates)))
            .andExpect(status().isCreated());

        // Validate the FxRates in the database
        List<FxRates> fxRatesList = fxRatesRepository.findAll();
        assertThat(fxRatesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFxRates() throws Exception {
        // Initialize the database
        fxRatesRepository.saveAndFlush(fxRates);
        int databaseSizeBeforeDelete = fxRatesRepository.findAll().size();

        // Get the fxRates
        restFxRatesMockMvc.perform(delete("/api/fx-rates/{id}", fxRates.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<FxRates> fxRatesList = fxRatesRepository.findAll();
        assertThat(fxRatesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FxRates.class);
    }
}
