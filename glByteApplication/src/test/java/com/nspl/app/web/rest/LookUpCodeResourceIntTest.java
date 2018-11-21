package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.LookUpCode;
import com.nspl.app.repository.LookUpCodeRepository;
import com.nspl.app.repository.search.LookUpCodeSearchRepository;
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
 * Test class for the LookUpCodeResource REST controller.
 *
 * @see LookUpCodeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class LookUpCodeResourceIntTest {

    private static final Long DEFAULT_TENANT_ID = 1L;
    private static final Long UPDATED_TENANT_ID = 2L;

    private static final String DEFAULT_LOOK_UP_CODE = "AAAAAAAAAA";
    private static final String UPDATED_LOOK_UP_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LOOK_UP_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_LOOK_UP_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_MEANING = "AAAAAAAAAA";
    private static final String UPDATED_MEANING = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLE_FLAG = false;
    private static final Boolean UPDATED_ENABLE_FLAG = true;

    private static final ZonedDateTime DEFAULT_ACTIVE_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ACTIVE_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_ACTIVE_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ACTIVE_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_SECURE_ATTRIBUTE_1 = "AAAAAAAAAA";
    private static final String UPDATED_SECURE_ATTRIBUTE_1 = "BBBBBBBBBB";

    private static final String DEFAULT_SECURE_ATTRIBUTE_2 = "AAAAAAAAAA";
    private static final String UPDATED_SECURE_ATTRIBUTE_2 = "BBBBBBBBBB";

    private static final String DEFAULT_SECURE_ATTRIBUTE_3 = "AAAAAAAAAA";
    private static final String UPDATED_SECURE_ATTRIBUTE_3 = "BBBBBBBBBB";

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private LookUpCodeRepository lookUpCodeRepository;

    @Autowired
    private LookUpCodeSearchRepository lookUpCodeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLookUpCodeMockMvc;

    private LookUpCode lookUpCode;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LookUpCodeResource lookUpCodeResource = new LookUpCodeResource(lookUpCodeRepository, lookUpCodeSearchRepository);
        this.restLookUpCodeMockMvc = MockMvcBuilders.standaloneSetup(lookUpCodeResource)
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
    public static LookUpCode createEntity(EntityManager em) {
        LookUpCode lookUpCode = new LookUpCode()
            .tenantId(DEFAULT_TENANT_ID)
            .lookUpCode(DEFAULT_LOOK_UP_CODE)
            .lookUpType(DEFAULT_LOOK_UP_TYPE)
            .meaning(DEFAULT_MEANING)
            .description(DEFAULT_DESCRIPTION)
            .enableFlag(DEFAULT_ENABLE_FLAG)
          //  .activeStartDate(DEFAULT_ACTIVE_START_DATE)
            .activeEndDate(DEFAULT_ACTIVE_END_DATE)
            .secureAttribute1(DEFAULT_SECURE_ATTRIBUTE_1)
            .secureAttribute2(DEFAULT_SECURE_ATTRIBUTE_2)
            .secureAttribute3(DEFAULT_SECURE_ATTRIBUTE_3)
            .createdBy(DEFAULT_CREATED_BY)
            .creationDate(DEFAULT_CREATION_DATE)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE);
        return lookUpCode;
    }

    @Before
    public void initTest() {
        lookUpCodeSearchRepository.deleteAll();
        lookUpCode = createEntity(em);
    }

    @Test
    @Transactional
    public void createLookUpCode() throws Exception {
        int databaseSizeBeforeCreate = lookUpCodeRepository.findAll().size();

        // Create the LookUpCode
        restLookUpCodeMockMvc.perform(post("/api/look-up-codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lookUpCode)))
            .andExpect(status().isCreated());

        // Validate the LookUpCode in the database
        List<LookUpCode> lookUpCodeList = lookUpCodeRepository.findAll();
        assertThat(lookUpCodeList).hasSize(databaseSizeBeforeCreate + 1);
        LookUpCode testLookUpCode = lookUpCodeList.get(lookUpCodeList.size() - 1);
        assertThat(testLookUpCode.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testLookUpCode.getLookUpCode()).isEqualTo(DEFAULT_LOOK_UP_CODE);
        assertThat(testLookUpCode.getLookUpType()).isEqualTo(DEFAULT_LOOK_UP_TYPE);
        assertThat(testLookUpCode.getMeaning()).isEqualTo(DEFAULT_MEANING);
        assertThat(testLookUpCode.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testLookUpCode.isEnableFlag()).isEqualTo(DEFAULT_ENABLE_FLAG);
        assertThat(testLookUpCode.getActiveStartDate()).isEqualTo(DEFAULT_ACTIVE_START_DATE);
        assertThat(testLookUpCode.getActiveEndDate()).isEqualTo(DEFAULT_ACTIVE_END_DATE);
        assertThat(testLookUpCode.getSecureAttribute1()).isEqualTo(DEFAULT_SECURE_ATTRIBUTE_1);
        assertThat(testLookUpCode.getSecureAttribute2()).isEqualTo(DEFAULT_SECURE_ATTRIBUTE_2);
        assertThat(testLookUpCode.getSecureAttribute3()).isEqualTo(DEFAULT_SECURE_ATTRIBUTE_3);
        assertThat(testLookUpCode.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testLookUpCode.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testLookUpCode.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testLookUpCode.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);

        // Validate the LookUpCode in Elasticsearch
        LookUpCode lookUpCodeEs = lookUpCodeSearchRepository.findOne(testLookUpCode.getId());
        assertThat(lookUpCodeEs).isEqualToComparingFieldByField(testLookUpCode);
    }

    @Test
    @Transactional
    public void createLookUpCodeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = lookUpCodeRepository.findAll().size();

        // Create the LookUpCode with an existing ID
        lookUpCode.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLookUpCodeMockMvc.perform(post("/api/look-up-codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lookUpCode)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<LookUpCode> lookUpCodeList = lookUpCodeRepository.findAll();
        assertThat(lookUpCodeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllLookUpCodes() throws Exception {
        // Initialize the database
        lookUpCodeRepository.saveAndFlush(lookUpCode);

        // Get all the lookUpCodeList
        restLookUpCodeMockMvc.perform(get("/api/look-up-codes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lookUpCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].lookUpCode").value(hasItem(DEFAULT_LOOK_UP_CODE.toString())))
            .andExpect(jsonPath("$.[*].lookUpType").value(hasItem(DEFAULT_LOOK_UP_TYPE.toString())))
            .andExpect(jsonPath("$.[*].meaning").value(hasItem(DEFAULT_MEANING.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].enableFlag").value(hasItem(DEFAULT_ENABLE_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].activeStartDate").value(hasItem(DEFAULT_ACTIVE_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].activeEndDate").value(hasItem(DEFAULT_ACTIVE_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].secureAttribute1").value(hasItem(DEFAULT_SECURE_ATTRIBUTE_1.toString())))
            .andExpect(jsonPath("$.[*].secureAttribute2").value(hasItem(DEFAULT_SECURE_ATTRIBUTE_2.toString())))
            .andExpect(jsonPath("$.[*].secureAttribute3").value(hasItem(DEFAULT_SECURE_ATTRIBUTE_3.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void getLookUpCode() throws Exception {
        // Initialize the database
        lookUpCodeRepository.saveAndFlush(lookUpCode);

        // Get the lookUpCode
        restLookUpCodeMockMvc.perform(get("/api/look-up-codes/{id}", lookUpCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(lookUpCode.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID.intValue()))
            .andExpect(jsonPath("$.lookUpCode").value(DEFAULT_LOOK_UP_CODE.toString()))
            .andExpect(jsonPath("$.lookUpType").value(DEFAULT_LOOK_UP_TYPE.toString()))
            .andExpect(jsonPath("$.meaning").value(DEFAULT_MEANING.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.enableFlag").value(DEFAULT_ENABLE_FLAG.booleanValue()))
            .andExpect(jsonPath("$.activeStartDate").value(DEFAULT_ACTIVE_START_DATE.toString()))
            .andExpect(jsonPath("$.activeEndDate").value(DEFAULT_ACTIVE_END_DATE.toString()))
            .andExpect(jsonPath("$.secureAttribute1").value(DEFAULT_SECURE_ATTRIBUTE_1.toString()))
            .andExpect(jsonPath("$.secureAttribute2").value(DEFAULT_SECURE_ATTRIBUTE_2.toString()))
            .andExpect(jsonPath("$.secureAttribute3").value(DEFAULT_SECURE_ATTRIBUTE_3.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.creationDate").value(sameInstant(DEFAULT_CREATION_DATE)))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingLookUpCode() throws Exception {
        // Get the lookUpCode
        restLookUpCodeMockMvc.perform(get("/api/look-up-codes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLookUpCode() throws Exception {
        // Initialize the database
        lookUpCodeRepository.saveAndFlush(lookUpCode);
        lookUpCodeSearchRepository.save(lookUpCode);
        int databaseSizeBeforeUpdate = lookUpCodeRepository.findAll().size();

        // Update the lookUpCode
        LookUpCode updatedLookUpCode = lookUpCodeRepository.findOne(lookUpCode.getId());
        updatedLookUpCode
            .tenantId(UPDATED_TENANT_ID)
            .lookUpCode(UPDATED_LOOK_UP_CODE)
            .lookUpType(UPDATED_LOOK_UP_TYPE)
            .meaning(UPDATED_MEANING)
            .description(UPDATED_DESCRIPTION)
            .enableFlag(UPDATED_ENABLE_FLAG)
            .activeStartDate(UPDATED_ACTIVE_START_DATE)
            .activeEndDate(UPDATED_ACTIVE_END_DATE)
            .secureAttribute1(UPDATED_SECURE_ATTRIBUTE_1)
            .secureAttribute2(UPDATED_SECURE_ATTRIBUTE_2)
            .secureAttribute3(UPDATED_SECURE_ATTRIBUTE_3)
            .createdBy(UPDATED_CREATED_BY)
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE);

        restLookUpCodeMockMvc.perform(put("/api/look-up-codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLookUpCode)))
            .andExpect(status().isOk());

        // Validate the LookUpCode in the database
        List<LookUpCode> lookUpCodeList = lookUpCodeRepository.findAll();
        assertThat(lookUpCodeList).hasSize(databaseSizeBeforeUpdate);
        LookUpCode testLookUpCode = lookUpCodeList.get(lookUpCodeList.size() - 1);
        assertThat(testLookUpCode.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testLookUpCode.getLookUpCode()).isEqualTo(UPDATED_LOOK_UP_CODE);
        assertThat(testLookUpCode.getLookUpType()).isEqualTo(UPDATED_LOOK_UP_TYPE);
        assertThat(testLookUpCode.getMeaning()).isEqualTo(UPDATED_MEANING);
        assertThat(testLookUpCode.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLookUpCode.isEnableFlag()).isEqualTo(UPDATED_ENABLE_FLAG);
        assertThat(testLookUpCode.getActiveStartDate()).isEqualTo(UPDATED_ACTIVE_START_DATE);
        assertThat(testLookUpCode.getActiveEndDate()).isEqualTo(UPDATED_ACTIVE_END_DATE);
        assertThat(testLookUpCode.getSecureAttribute1()).isEqualTo(UPDATED_SECURE_ATTRIBUTE_1);
        assertThat(testLookUpCode.getSecureAttribute2()).isEqualTo(UPDATED_SECURE_ATTRIBUTE_2);
        assertThat(testLookUpCode.getSecureAttribute3()).isEqualTo(UPDATED_SECURE_ATTRIBUTE_3);
        assertThat(testLookUpCode.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testLookUpCode.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testLookUpCode.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testLookUpCode.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);

        // Validate the LookUpCode in Elasticsearch
        LookUpCode lookUpCodeEs = lookUpCodeSearchRepository.findOne(testLookUpCode.getId());
        assertThat(lookUpCodeEs).isEqualToComparingFieldByField(testLookUpCode);
    }

    @Test
    @Transactional
    public void updateNonExistingLookUpCode() throws Exception {
        int databaseSizeBeforeUpdate = lookUpCodeRepository.findAll().size();

        // Create the LookUpCode

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLookUpCodeMockMvc.perform(put("/api/look-up-codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lookUpCode)))
            .andExpect(status().isCreated());

        // Validate the LookUpCode in the database
        List<LookUpCode> lookUpCodeList = lookUpCodeRepository.findAll();
        assertThat(lookUpCodeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLookUpCode() throws Exception {
        // Initialize the database
        lookUpCodeRepository.saveAndFlush(lookUpCode);
        lookUpCodeSearchRepository.save(lookUpCode);
        int databaseSizeBeforeDelete = lookUpCodeRepository.findAll().size();

        // Get the lookUpCode
        restLookUpCodeMockMvc.perform(delete("/api/look-up-codes/{id}", lookUpCode.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean lookUpCodeExistsInEs = lookUpCodeSearchRepository.exists(lookUpCode.getId());
        assertThat(lookUpCodeExistsInEs).isFalse();

        // Validate the database is empty
        List<LookUpCode> lookUpCodeList = lookUpCodeRepository.findAll();
        assertThat(lookUpCodeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLookUpCode() throws Exception {
        // Initialize the database
        lookUpCodeRepository.saveAndFlush(lookUpCode);
        lookUpCodeSearchRepository.save(lookUpCode);

        // Search the lookUpCode
        restLookUpCodeMockMvc.perform(get("/api/_search/look-up-codes?query=id:" + lookUpCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lookUpCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].lookUpCode").value(hasItem(DEFAULT_LOOK_UP_CODE.toString())))
            .andExpect(jsonPath("$.[*].lookUpType").value(hasItem(DEFAULT_LOOK_UP_TYPE.toString())))
            .andExpect(jsonPath("$.[*].meaning").value(hasItem(DEFAULT_MEANING.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].enableFlag").value(hasItem(DEFAULT_ENABLE_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].activeStartDate").value(hasItem(DEFAULT_ACTIVE_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].activeEndDate").value(hasItem(DEFAULT_ACTIVE_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].secureAttribute1").value(hasItem(DEFAULT_SECURE_ATTRIBUTE_1.toString())))
            .andExpect(jsonPath("$.[*].secureAttribute2").value(hasItem(DEFAULT_SECURE_ATTRIBUTE_2.toString())))
            .andExpect(jsonPath("$.[*].secureAttribute3").value(hasItem(DEFAULT_SECURE_ATTRIBUTE_3.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LookUpCode.class);
    }
}
