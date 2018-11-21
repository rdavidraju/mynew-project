package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.TenantConfig;
import com.nspl.app.repository.TenantConfigRepository;
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
 * Test class for the TenantConfigResource REST controller.
 *
 * @see TenantConfigResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class TenantConfigResourceIntTest {

    private static final Long DEFAULT_TENANT_ID = 1L;
    private static final Long UPDATED_TENANT_ID = 2L;

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private TenantConfigRepository tenantConfigRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTenantConfigMockMvc;

    private TenantConfig tenantConfig;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TenantConfigResource tenantConfigResource = new TenantConfigResource(tenantConfigRepository);
        this.restTenantConfigMockMvc = MockMvcBuilders.standaloneSetup(tenantConfigResource)
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
    public static TenantConfig createEntity(EntityManager em) {
        TenantConfig tenantConfig = new TenantConfig()
            .tenantId(DEFAULT_TENANT_ID)
            .key(DEFAULT_KEY)
            .value(DEFAULT_VALUE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .creationDate(DEFAULT_CREATION_DATE)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE);
        return tenantConfig;
    }

    @Before
    public void initTest() {
        tenantConfig = createEntity(em);
    }

    @Test
    @Transactional
    public void createTenantConfig() throws Exception {
        int databaseSizeBeforeCreate = tenantConfigRepository.findAll().size();

        // Create the TenantConfig
        restTenantConfigMockMvc.perform(post("/api/tenant-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tenantConfig)))
            .andExpect(status().isCreated());

        // Validate the TenantConfig in the database
        List<TenantConfig> tenantConfigList = tenantConfigRepository.findAll();
        assertThat(tenantConfigList).hasSize(databaseSizeBeforeCreate + 1);
        TenantConfig testTenantConfig = tenantConfigList.get(tenantConfigList.size() - 1);
        assertThat(testTenantConfig.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testTenantConfig.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testTenantConfig.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testTenantConfig.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testTenantConfig.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testTenantConfig.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testTenantConfig.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void createTenantConfigWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tenantConfigRepository.findAll().size();

        // Create the TenantConfig with an existing ID
        tenantConfig.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTenantConfigMockMvc.perform(post("/api/tenant-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tenantConfig)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<TenantConfig> tenantConfigList = tenantConfigRepository.findAll();
        assertThat(tenantConfigList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTenantConfigs() throws Exception {
        // Initialize the database
        tenantConfigRepository.saveAndFlush(tenantConfig);

        // Get all the tenantConfigList
        restTenantConfigMockMvc.perform(get("/api/tenant-configs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tenantConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void getTenantConfig() throws Exception {
        // Initialize the database
        tenantConfigRepository.saveAndFlush(tenantConfig);

        // Get the tenantConfig
        restTenantConfigMockMvc.perform(get("/api/tenant-configs/{id}", tenantConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tenantConfig.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID.intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.creationDate").value(sameInstant(DEFAULT_CREATION_DATE)))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingTenantConfig() throws Exception {
        // Get the tenantConfig
        restTenantConfigMockMvc.perform(get("/api/tenant-configs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTenantConfig() throws Exception {
        // Initialize the database
        tenantConfigRepository.saveAndFlush(tenantConfig);
        int databaseSizeBeforeUpdate = tenantConfigRepository.findAll().size();

        // Update the tenantConfig
        TenantConfig updatedTenantConfig = tenantConfigRepository.findOne(tenantConfig.getId());
        updatedTenantConfig
            .tenantId(UPDATED_TENANT_ID)
            .key(UPDATED_KEY)
            .value(UPDATED_VALUE)
            .createdBy(UPDATED_CREATED_BY)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE);

        restTenantConfigMockMvc.perform(put("/api/tenant-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTenantConfig)))
            .andExpect(status().isOk());

        // Validate the TenantConfig in the database
        List<TenantConfig> tenantConfigList = tenantConfigRepository.findAll();
        assertThat(tenantConfigList).hasSize(databaseSizeBeforeUpdate);
        TenantConfig testTenantConfig = tenantConfigList.get(tenantConfigList.size() - 1);
        assertThat(testTenantConfig.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testTenantConfig.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testTenantConfig.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testTenantConfig.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTenantConfig.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testTenantConfig.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testTenantConfig.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingTenantConfig() throws Exception {
        int databaseSizeBeforeUpdate = tenantConfigRepository.findAll().size();

        // Create the TenantConfig

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTenantConfigMockMvc.perform(put("/api/tenant-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tenantConfig)))
            .andExpect(status().isCreated());

        // Validate the TenantConfig in the database
        List<TenantConfig> tenantConfigList = tenantConfigRepository.findAll();
        assertThat(tenantConfigList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTenantConfig() throws Exception {
        // Initialize the database
        tenantConfigRepository.saveAndFlush(tenantConfig);
        int databaseSizeBeforeDelete = tenantConfigRepository.findAll().size();

        // Get the tenantConfig
        restTenantConfigMockMvc.perform(delete("/api/tenant-configs/{id}", tenantConfig.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TenantConfig> tenantConfigList = tenantConfigRepository.findAll();
        assertThat(tenantConfigList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TenantConfig.class);
        TenantConfig tenantConfig1 = new TenantConfig();
        tenantConfig1.setId(1L);
        TenantConfig tenantConfig2 = new TenantConfig();
        tenantConfig2.setId(tenantConfig1.getId());
        assertThat(tenantConfig1).isEqualTo(tenantConfig2);
        tenantConfig2.setId(2L);
        assertThat(tenantConfig1).isNotEqualTo(tenantConfig2);
        tenantConfig1.setId(null);
        assertThat(tenantConfig1).isNotEqualTo(tenantConfig2);
    }
}
