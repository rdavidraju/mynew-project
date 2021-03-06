package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;
import com.nspl.app.domain.FxRatesDetails;
import com.nspl.app.repository.FxRatesDetailsRepository;
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

import java.math.BigDecimal;
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
 * Test class for the FxRatesDetailsResource REST controller.
 *
 * @see FxRatesDetailsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class FxRatesDetailsResourceIntTest {

    private static final Long DEFAULT_FX_RATE_ID = 1L;
    private static final Long UPDATED_FX_RATE_ID = 2L;

    private static final String DEFAULT_FROM_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_FROM_CURRENCY = "BBBBBBBBBB";

    private static final String DEFAULT_TO_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_TO_CURRENCY = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_FROM_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FROM_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_TO_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TO_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final BigDecimal DEFAULT_CONVERSION_RATE = new BigDecimal(1);
    private static final BigDecimal UPDATED_CONVERSION_RATE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_INVERSE_RATE = new BigDecimal(1);
    private static final BigDecimal UPDATED_INVERSE_RATE = new BigDecimal(2);

    private static final String DEFAULT_STATUS_CODE = "AAAAAAAAAA";
    private static final String UPDATED_STATUS_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE = "BBBBBBBBBB";

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private FxRatesDetailsRepository fxRatesDetailsRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFxRatesDetailsMockMvc;

    private FxRatesDetails fxRatesDetails;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FxRatesDetailsResource fxRatesDetailsResource = new FxRatesDetailsResource(fxRatesDetailsRepository);
        this.restFxRatesDetailsMockMvc = MockMvcBuilders.standaloneSetup(fxRatesDetailsResource)
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
    public static FxRatesDetails createEntity(EntityManager em) {
        FxRatesDetails fxRatesDetails = new FxRatesDetails()
            .fxRateId(DEFAULT_FX_RATE_ID)
            .fromCurrency(DEFAULT_FROM_CURRENCY)
            .toCurrency(DEFAULT_TO_CURRENCY)
            .fromDate(DEFAULT_FROM_DATE)
            .toDate(DEFAULT_TO_DATE)
            .conversionRate(DEFAULT_CONVERSION_RATE)
            .inverseRate(DEFAULT_INVERSE_RATE)
          //  .statusCode(DEFAULT_STATUS_CODE)
            .source(DEFAULT_SOURCE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE);
        return fxRatesDetails;
    }

    @Before
    public void initTest() {
        fxRatesDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createFxRatesDetails() throws Exception {
        int databaseSizeBeforeCreate = fxRatesDetailsRepository.findAll().size();

        // Create the FxRatesDetails
        restFxRatesDetailsMockMvc.perform(post("/api/fx-rates-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fxRatesDetails)))
            .andExpect(status().isCreated());

        // Validate the FxRatesDetails in the database
        List<FxRatesDetails> fxRatesDetailsList = fxRatesDetailsRepository.findAll();
        assertThat(fxRatesDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        FxRatesDetails testFxRatesDetails = fxRatesDetailsList.get(fxRatesDetailsList.size() - 1);
        assertThat(testFxRatesDetails.getFxRateId()).isEqualTo(DEFAULT_FX_RATE_ID);
        assertThat(testFxRatesDetails.getFromCurrency()).isEqualTo(DEFAULT_FROM_CURRENCY);
        assertThat(testFxRatesDetails.getToCurrency()).isEqualTo(DEFAULT_TO_CURRENCY);
        assertThat(testFxRatesDetails.getFromDate()).isEqualTo(DEFAULT_FROM_DATE);
        assertThat(testFxRatesDetails.getToDate()).isEqualTo(DEFAULT_TO_DATE);
        assertThat(testFxRatesDetails.getConversionRate()).isEqualTo(DEFAULT_CONVERSION_RATE);
        assertThat(testFxRatesDetails.getInverseRate()).isEqualTo(DEFAULT_INVERSE_RATE);
       // assertThat(testFxRatesDetails.getStatusCode()).isEqualTo(DEFAULT_STATUS_CODE);
        assertThat(testFxRatesDetails.getSource()).isEqualTo(DEFAULT_SOURCE);
        assertThat(testFxRatesDetails.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testFxRatesDetails.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testFxRatesDetails.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testFxRatesDetails.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void createFxRatesDetailsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fxRatesDetailsRepository.findAll().size();

        // Create the FxRatesDetails with an existing ID
        fxRatesDetails.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFxRatesDetailsMockMvc.perform(post("/api/fx-rates-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fxRatesDetails)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<FxRatesDetails> fxRatesDetailsList = fxRatesDetailsRepository.findAll();
        assertThat(fxRatesDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllFxRatesDetails() throws Exception {
        // Initialize the database
        fxRatesDetailsRepository.saveAndFlush(fxRatesDetails);

        // Get all the fxRatesDetailsList
        restFxRatesDetailsMockMvc.perform(get("/api/fx-rates-details?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fxRatesDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].fxRateId").value(hasItem(DEFAULT_FX_RATE_ID.intValue())))
            .andExpect(jsonPath("$.[*].fromCurrency").value(hasItem(DEFAULT_FROM_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].toCurrency").value(hasItem(DEFAULT_TO_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].toDate").value(hasItem(DEFAULT_TO_DATE.toString())))
            .andExpect(jsonPath("$.[*].conversionRate").value(hasItem(DEFAULT_CONVERSION_RATE)))
            .andExpect(jsonPath("$.[*].inverseRate").value(hasItem(DEFAULT_INVERSE_RATE)))
            .andExpect(jsonPath("$.[*].statusCode").value(hasItem(DEFAULT_STATUS_CODE.toString())))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void getFxRatesDetails() throws Exception {
        // Initialize the database
        fxRatesDetailsRepository.saveAndFlush(fxRatesDetails);

        // Get the fxRatesDetails
        restFxRatesDetailsMockMvc.perform(get("/api/fx-rates-details/{id}", fxRatesDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(fxRatesDetails.getId().intValue()))
            .andExpect(jsonPath("$.fxRateId").value(DEFAULT_FX_RATE_ID.intValue()))
            .andExpect(jsonPath("$.fromCurrency").value(DEFAULT_FROM_CURRENCY.toString()))
            .andExpect(jsonPath("$.toCurrency").value(DEFAULT_TO_CURRENCY.toString()))
            .andExpect(jsonPath("$.fromDate").value(DEFAULT_FROM_DATE.toString()))
            .andExpect(jsonPath("$.toDate").value(DEFAULT_TO_DATE.toString()))
            .andExpect(jsonPath("$.conversionRate").value(DEFAULT_CONVERSION_RATE))
            .andExpect(jsonPath("$.inverseRate").value(DEFAULT_INVERSE_RATE))
            .andExpect(jsonPath("$.statusCode").value(DEFAULT_STATUS_CODE.toString()))
            .andExpect(jsonPath("$.source").value(DEFAULT_SOURCE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingFxRatesDetails() throws Exception {
        // Get the fxRatesDetails
        restFxRatesDetailsMockMvc.perform(get("/api/fx-rates-details/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFxRatesDetails() throws Exception {
        // Initialize the database
        fxRatesDetailsRepository.saveAndFlush(fxRatesDetails);
        int databaseSizeBeforeUpdate = fxRatesDetailsRepository.findAll().size();

        // Update the fxRatesDetails
        FxRatesDetails updatedFxRatesDetails = fxRatesDetailsRepository.findOne(fxRatesDetails.getId());
        updatedFxRatesDetails
            .fxRateId(UPDATED_FX_RATE_ID)
            .fromCurrency(UPDATED_FROM_CURRENCY)
            .toCurrency(UPDATED_TO_CURRENCY)
            .fromDate(UPDATED_FROM_DATE)
            .toDate(UPDATED_TO_DATE)
            .conversionRate(UPDATED_CONVERSION_RATE)
            .inverseRate(UPDATED_INVERSE_RATE)
          //  .statusCode(UPDATED_STATUS_CODE)
            .source(UPDATED_SOURCE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE);

        restFxRatesDetailsMockMvc.perform(put("/api/fx-rates-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFxRatesDetails)))
            .andExpect(status().isOk());

        // Validate the FxRatesDetails in the database
        List<FxRatesDetails> fxRatesDetailsList = fxRatesDetailsRepository.findAll();
        assertThat(fxRatesDetailsList).hasSize(databaseSizeBeforeUpdate);
        FxRatesDetails testFxRatesDetails = fxRatesDetailsList.get(fxRatesDetailsList.size() - 1);
        assertThat(testFxRatesDetails.getFxRateId()).isEqualTo(UPDATED_FX_RATE_ID);
        assertThat(testFxRatesDetails.getFromCurrency()).isEqualTo(UPDATED_FROM_CURRENCY);
        assertThat(testFxRatesDetails.getToCurrency()).isEqualTo(UPDATED_TO_CURRENCY);
        assertThat(testFxRatesDetails.getFromDate()).isEqualTo(UPDATED_FROM_DATE);
        assertThat(testFxRatesDetails.getToDate()).isEqualTo(UPDATED_TO_DATE);
        assertThat(testFxRatesDetails.getConversionRate()).isEqualTo(UPDATED_CONVERSION_RATE);
        assertThat(testFxRatesDetails.getInverseRate()).isEqualTo(UPDATED_INVERSE_RATE);
     //   assertThat(testFxRatesDetails.getStatusCode()).isEqualTo(UPDATED_STATUS_CODE);
        assertThat(testFxRatesDetails.getSource()).isEqualTo(UPDATED_SOURCE);
        assertThat(testFxRatesDetails.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testFxRatesDetails.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testFxRatesDetails.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testFxRatesDetails.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingFxRatesDetails() throws Exception {
        int databaseSizeBeforeUpdate = fxRatesDetailsRepository.findAll().size();

        // Create the FxRatesDetails

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFxRatesDetailsMockMvc.perform(put("/api/fx-rates-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fxRatesDetails)))
            .andExpect(status().isCreated());

        // Validate the FxRatesDetails in the database
        List<FxRatesDetails> fxRatesDetailsList = fxRatesDetailsRepository.findAll();
        assertThat(fxRatesDetailsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFxRatesDetails() throws Exception {
        // Initialize the database
        fxRatesDetailsRepository.saveAndFlush(fxRatesDetails);
        int databaseSizeBeforeDelete = fxRatesDetailsRepository.findAll().size();

        // Get the fxRatesDetails
        restFxRatesDetailsMockMvc.perform(delete("/api/fx-rates-details/{id}", fxRatesDetails.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<FxRatesDetails> fxRatesDetailsList = fxRatesDetailsRepository.findAll();
        assertThat(fxRatesDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FxRatesDetails.class);
    }
}
