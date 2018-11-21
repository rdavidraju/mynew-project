package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;
import com.nspl.app.domain.SourceConnectionDetails;
import com.nspl.app.repository.SourceConnectionDetailsRepository;
import com.nspl.app.repository.search.SourceConnectionDetailsSearchRepository;
import com.nspl.app.repository.search.SourceProfilesSearchRepository;
import com.nspl.app.service.SourceConnectionDetailsService;
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
 * Test class for the SourceConnectionDetailsResource REST controller.
 *
 * @see SourceConnectionDetailsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class SourceConnectionDetailsResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_PROTOCOL = "AAAAAAAAAA";
    private static final String UPDATED_PROTOCOL = "BBBBBBBBBB";

    private static final String DEFAULT_CLIENT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_CLIENT_SECRET = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT_SECRET = "BBBBBBBBBB";

    private static final String DEFAULT_AUTH_ENDPOINT_URL = "AAAAAAAAAA";
    private static final String UPDATED_AUTH_ENDPOINT_URL = "BBBBBBBBBB";

    private static final String DEFAULT_TOKEN_ENDPOINT_URL = "AAAAAAAAAA";
    private static final String UPDATED_TOKEN_ENDPOINT_URL = "BBBBBBBBBB";

    private static final String DEFAULT_CALL_BACK_URL = "AAAAAAAAAA";
    private static final String UPDATED_CALL_BACK_URL = "BBBBBBBBBB";

    private static final String DEFAULT_HOST = "AAAAAAAAAA";
    private static final String UPDATED_HOST = "BBBBBBBBBB";

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final Long DEFAULT_TENANT_ID = 1L;
    private static final Long UPDATED_TENANT_ID = 2L;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private SourceConnectionDetailsRepository sourceConnectionDetailsRepository;

    @Autowired
    private SourceConnectionDetailsSearchRepository sourceConnectionDetailsSearchRepository;
    
    @Autowired
    private SourceProfilesSearchRepository sourceProfilesSearchRepository;
    
    @Autowired
    private SourceConnectionDetailsService sourceConnectionDetailsService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSourceConnectionDetailsMockMvc;

    private SourceConnectionDetails sourceConnectionDetails;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SourceConnectionDetailsResource sourceConnectionDetailsResource = new SourceConnectionDetailsResource(sourceConnectionDetailsRepository, sourceConnectionDetailsSearchRepository, sourceProfilesSearchRepository, sourceConnectionDetailsService);
        this.restSourceConnectionDetailsMockMvc = MockMvcBuilders.standaloneSetup(sourceConnectionDetailsResource)
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
    public static SourceConnectionDetails createEntity(EntityManager em) {
        SourceConnectionDetails sourceConnectionDetails = new SourceConnectionDetails()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .protocol(DEFAULT_PROTOCOL)
            .clientKey(DEFAULT_CLIENT_KEY)
            .clientSecret(DEFAULT_CLIENT_SECRET)
            .authEndpointUrl(DEFAULT_AUTH_ENDPOINT_URL)
            .tokenEndpointUrl(DEFAULT_TOKEN_ENDPOINT_URL)
            .callBackUrl(DEFAULT_CALL_BACK_URL)
            .host(DEFAULT_HOST)
            .userName(DEFAULT_USER_NAME)
            .password(DEFAULT_PASSWORD)
            .url(DEFAULT_URL)
            .tenantId(DEFAULT_TENANT_ID)
            .createdDate(DEFAULT_CREATED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE);
        return sourceConnectionDetails;
    }

    @Before
    public void initTest() {
        sourceConnectionDetailsSearchRepository.deleteAll();
        sourceConnectionDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createSourceConnectionDetails() throws Exception {
        int databaseSizeBeforeCreate = sourceConnectionDetailsRepository.findAll().size();

        // Create the SourceConnectionDetails
        restSourceConnectionDetailsMockMvc.perform(post("/api/source-connection-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sourceConnectionDetails)))
            .andExpect(status().isCreated());

        // Validate the SourceConnectionDetails in the database
        List<SourceConnectionDetails> sourceConnectionDetailsList = sourceConnectionDetailsRepository.findAll();
        assertThat(sourceConnectionDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        SourceConnectionDetails testSourceConnectionDetails = sourceConnectionDetailsList.get(sourceConnectionDetailsList.size() - 1);
        assertThat(testSourceConnectionDetails.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSourceConnectionDetails.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSourceConnectionDetails.getProtocol()).isEqualTo(DEFAULT_PROTOCOL);
        assertThat(testSourceConnectionDetails.getClientKey()).isEqualTo(DEFAULT_CLIENT_KEY);
        assertThat(testSourceConnectionDetails.getClientSecret()).isEqualTo(DEFAULT_CLIENT_SECRET);
        assertThat(testSourceConnectionDetails.getAuthEndpointUrl()).isEqualTo(DEFAULT_AUTH_ENDPOINT_URL);
        assertThat(testSourceConnectionDetails.getTokenEndpointUrl()).isEqualTo(DEFAULT_TOKEN_ENDPOINT_URL);
        assertThat(testSourceConnectionDetails.getCallBackUrl()).isEqualTo(DEFAULT_CALL_BACK_URL);
        assertThat(testSourceConnectionDetails.getHost()).isEqualTo(DEFAULT_HOST);
        assertThat(testSourceConnectionDetails.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testSourceConnectionDetails.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testSourceConnectionDetails.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testSourceConnectionDetails.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testSourceConnectionDetails.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSourceConnectionDetails.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSourceConnectionDetails.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testSourceConnectionDetails.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);

        // Validate the SourceConnectionDetails in Elasticsearch
        SourceConnectionDetails sourceConnectionDetailsEs = sourceConnectionDetailsSearchRepository.findOne(testSourceConnectionDetails.getId());
        assertThat(sourceConnectionDetailsEs).isEqualToComparingFieldByField(testSourceConnectionDetails);
    }

    @Test
    @Transactional
    public void createSourceConnectionDetailsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sourceConnectionDetailsRepository.findAll().size();

        // Create the SourceConnectionDetails with an existing ID
        sourceConnectionDetails.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSourceConnectionDetailsMockMvc.perform(post("/api/source-connection-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sourceConnectionDetails)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<SourceConnectionDetails> sourceConnectionDetailsList = sourceConnectionDetailsRepository.findAll();
        assertThat(sourceConnectionDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSourceConnectionDetails() throws Exception {
        // Initialize the database
        sourceConnectionDetailsRepository.saveAndFlush(sourceConnectionDetails);

        // Get all the sourceConnectionDetailsList
        restSourceConnectionDetailsMockMvc.perform(get("/api/source-connection-details?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sourceConnectionDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].protocol").value(hasItem(DEFAULT_PROTOCOL.toString())))
            .andExpect(jsonPath("$.[*].clientKey").value(hasItem(DEFAULT_CLIENT_KEY.toString())))
            .andExpect(jsonPath("$.[*].clientSecret").value(hasItem(DEFAULT_CLIENT_SECRET.toString())))
            .andExpect(jsonPath("$.[*].authEndpointUrl").value(hasItem(DEFAULT_AUTH_ENDPOINT_URL.toString())))
            .andExpect(jsonPath("$.[*].tokenEndpointUrl").value(hasItem(DEFAULT_TOKEN_ENDPOINT_URL.toString())))
            .andExpect(jsonPath("$.[*].callBackUrl").value(hasItem(DEFAULT_CALL_BACK_URL.toString())))
            .andExpect(jsonPath("$.[*].host").value(hasItem(DEFAULT_HOST.toString())))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME.toString())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void getSourceConnectionDetails() throws Exception {
        // Initialize the database
        sourceConnectionDetailsRepository.saveAndFlush(sourceConnectionDetails);

        // Get the sourceConnectionDetails
        restSourceConnectionDetailsMockMvc.perform(get("/api/source-connection-details/{id}", sourceConnectionDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sourceConnectionDetails.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.protocol").value(DEFAULT_PROTOCOL.toString()))
            .andExpect(jsonPath("$.clientKey").value(DEFAULT_CLIENT_KEY.toString()))
            .andExpect(jsonPath("$.clientSecret").value(DEFAULT_CLIENT_SECRET.toString()))
            .andExpect(jsonPath("$.authEndpointUrl").value(DEFAULT_AUTH_ENDPOINT_URL.toString()))
            .andExpect(jsonPath("$.tokenEndpointUrl").value(DEFAULT_TOKEN_ENDPOINT_URL.toString()))
            .andExpect(jsonPath("$.callBackUrl").value(DEFAULT_CALL_BACK_URL.toString()))
            .andExpect(jsonPath("$.host").value(DEFAULT_HOST.toString()))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID.intValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingSourceConnectionDetails() throws Exception {
        // Get the sourceConnectionDetails
        restSourceConnectionDetailsMockMvc.perform(get("/api/source-connection-details/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSourceConnectionDetails() throws Exception {
        // Initialize the database
        sourceConnectionDetailsRepository.saveAndFlush(sourceConnectionDetails);
        sourceConnectionDetailsSearchRepository.save(sourceConnectionDetails);
        int databaseSizeBeforeUpdate = sourceConnectionDetailsRepository.findAll().size();

        // Update the sourceConnectionDetails
        SourceConnectionDetails updatedSourceConnectionDetails = sourceConnectionDetailsRepository.findOne(sourceConnectionDetails.getId());
        updatedSourceConnectionDetails
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .protocol(UPDATED_PROTOCOL)
            .clientKey(UPDATED_CLIENT_KEY)
            .clientSecret(UPDATED_CLIENT_SECRET)
            .authEndpointUrl(UPDATED_AUTH_ENDPOINT_URL)
            .tokenEndpointUrl(UPDATED_TOKEN_ENDPOINT_URL)
            .callBackUrl(UPDATED_CALL_BACK_URL)
            .host(UPDATED_HOST)
            .userName(UPDATED_USER_NAME)
            .password(UPDATED_PASSWORD)
            .url(UPDATED_URL)
            .tenantId(UPDATED_TENANT_ID)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE);

        restSourceConnectionDetailsMockMvc.perform(put("/api/source-connection-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSourceConnectionDetails)))
            .andExpect(status().isOk());

        // Validate the SourceConnectionDetails in the database
        List<SourceConnectionDetails> sourceConnectionDetailsList = sourceConnectionDetailsRepository.findAll();
        assertThat(sourceConnectionDetailsList).hasSize(databaseSizeBeforeUpdate);
        SourceConnectionDetails testSourceConnectionDetails = sourceConnectionDetailsList.get(sourceConnectionDetailsList.size() - 1);
        assertThat(testSourceConnectionDetails.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSourceConnectionDetails.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSourceConnectionDetails.getProtocol()).isEqualTo(UPDATED_PROTOCOL);
        assertThat(testSourceConnectionDetails.getClientKey()).isEqualTo(UPDATED_CLIENT_KEY);
        assertThat(testSourceConnectionDetails.getClientSecret()).isEqualTo(UPDATED_CLIENT_SECRET);
        assertThat(testSourceConnectionDetails.getAuthEndpointUrl()).isEqualTo(UPDATED_AUTH_ENDPOINT_URL);
        assertThat(testSourceConnectionDetails.getTokenEndpointUrl()).isEqualTo(UPDATED_TOKEN_ENDPOINT_URL);
        assertThat(testSourceConnectionDetails.getCallBackUrl()).isEqualTo(UPDATED_CALL_BACK_URL);
        assertThat(testSourceConnectionDetails.getHost()).isEqualTo(UPDATED_HOST);
        assertThat(testSourceConnectionDetails.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testSourceConnectionDetails.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testSourceConnectionDetails.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testSourceConnectionDetails.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testSourceConnectionDetails.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSourceConnectionDetails.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSourceConnectionDetails.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testSourceConnectionDetails.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);

        // Validate the SourceConnectionDetails in Elasticsearch
        SourceConnectionDetails sourceConnectionDetailsEs = sourceConnectionDetailsSearchRepository.findOne(testSourceConnectionDetails.getId());
        assertThat(sourceConnectionDetailsEs).isEqualToComparingFieldByField(testSourceConnectionDetails);
    }

    @Test
    @Transactional
    public void updateNonExistingSourceConnectionDetails() throws Exception {
        int databaseSizeBeforeUpdate = sourceConnectionDetailsRepository.findAll().size();

        // Create the SourceConnectionDetails

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSourceConnectionDetailsMockMvc.perform(put("/api/source-connection-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sourceConnectionDetails)))
            .andExpect(status().isCreated());

        // Validate the SourceConnectionDetails in the database
        List<SourceConnectionDetails> sourceConnectionDetailsList = sourceConnectionDetailsRepository.findAll();
        assertThat(sourceConnectionDetailsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSourceConnectionDetails() throws Exception {
        // Initialize the database
        sourceConnectionDetailsRepository.saveAndFlush(sourceConnectionDetails);
        sourceConnectionDetailsSearchRepository.save(sourceConnectionDetails);
        int databaseSizeBeforeDelete = sourceConnectionDetailsRepository.findAll().size();

        // Get the sourceConnectionDetails
        restSourceConnectionDetailsMockMvc.perform(delete("/api/source-connection-details/{id}", sourceConnectionDetails.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean sourceConnectionDetailsExistsInEs = sourceConnectionDetailsSearchRepository.exists(sourceConnectionDetails.getId());
        assertThat(sourceConnectionDetailsExistsInEs).isFalse();

        // Validate the database is empty
        List<SourceConnectionDetails> sourceConnectionDetailsList = sourceConnectionDetailsRepository.findAll();
        assertThat(sourceConnectionDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSourceConnectionDetails() throws Exception {
        // Initialize the database
        sourceConnectionDetailsRepository.saveAndFlush(sourceConnectionDetails);
        sourceConnectionDetailsSearchRepository.save(sourceConnectionDetails);

        // Search the sourceConnectionDetails
        restSourceConnectionDetailsMockMvc.perform(get("/api/_search/source-connection-details?query=id:" + sourceConnectionDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sourceConnectionDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].protocol").value(hasItem(DEFAULT_PROTOCOL.toString())))
            .andExpect(jsonPath("$.[*].clientKey").value(hasItem(DEFAULT_CLIENT_KEY.toString())))
            .andExpect(jsonPath("$.[*].clientSecret").value(hasItem(DEFAULT_CLIENT_SECRET.toString())))
            .andExpect(jsonPath("$.[*].authEndpointUrl").value(hasItem(DEFAULT_AUTH_ENDPOINT_URL.toString())))
            .andExpect(jsonPath("$.[*].tokenEndpointUrl").value(hasItem(DEFAULT_TOKEN_ENDPOINT_URL.toString())))
            .andExpect(jsonPath("$.[*].callBackUrl").value(hasItem(DEFAULT_CALL_BACK_URL.toString())))
            .andExpect(jsonPath("$.[*].host").value(hasItem(DEFAULT_HOST.toString())))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME.toString())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SourceConnectionDetails.class);
        SourceConnectionDetails sourceConnectionDetails1 = new SourceConnectionDetails();
        sourceConnectionDetails1.setId(1L);
        SourceConnectionDetails sourceConnectionDetails2 = new SourceConnectionDetails();
        sourceConnectionDetails2.setId(sourceConnectionDetails1.getId());
        assertThat(sourceConnectionDetails1).isEqualTo(sourceConnectionDetails2);
        sourceConnectionDetails2.setId(2L);
        assertThat(sourceConnectionDetails1).isNotEqualTo(sourceConnectionDetails2);
        sourceConnectionDetails1.setId(null);
        assertThat(sourceConnectionDetails1).isNotEqualTo(sourceConnectionDetails2);
    }
}
