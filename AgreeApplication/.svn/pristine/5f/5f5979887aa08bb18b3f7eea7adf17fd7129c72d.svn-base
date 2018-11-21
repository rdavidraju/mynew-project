package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.TenantConfigModules;
import com.nspl.app.repository.TenantConfigModulesRepository;
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
 * Test class for the TenantConfigModulesResource REST controller.
 *
 * @see TenantConfigModulesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class TenantConfigModulesResourceIntTest {

    private static final Long DEFAULT_TENANT_ID = 1L;
    private static final Long UPDATED_TENANT_ID = 2L;

    private static final String DEFAULT_MODULES = "AAAAAAAAAA";
    private static final String UPDATED_MODULES = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED_FLAG = false;
    private static final Boolean UPDATED_ENABLED_FLAG = true;

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_CONTRACT_NUM = "AAAAAAAAAA";
    private static final String UPDATED_CONTRACT_NUM = "BBBBBBBBBB";

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private TenantConfigModulesRepository tenantConfigModulesRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTenantConfigModulesMockMvc;

    private TenantConfigModules tenantConfigModules;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TenantConfigModulesResource tenantConfigModulesResource = new TenantConfigModulesResource(tenantConfigModulesRepository);
        this.restTenantConfigModulesMockMvc = MockMvcBuilders.standaloneSetup(tenantConfigModulesResource)
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
    public static TenantConfigModules createEntity(EntityManager em) {
        TenantConfigModules tenantConfigModules = new TenantConfigModules()
            .tenantId(DEFAULT_TENANT_ID)
            .modules(DEFAULT_MODULES)
            .enabledFlag(DEFAULT_ENABLED_FLAG)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .contractNum(DEFAULT_CONTRACT_NUM)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE);
        return tenantConfigModules;
    }

    @Before
    public void initTest() {
        tenantConfigModules = createEntity(em);
    }

    @Test
    @Transactional
    public void createTenantConfigModules() throws Exception {
        int databaseSizeBeforeCreate = tenantConfigModulesRepository.findAll().size();

        // Create the TenantConfigModules
        restTenantConfigModulesMockMvc.perform(post("/api/tenant-config-modules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tenantConfigModules)))
            .andExpect(status().isCreated());

        // Validate the TenantConfigModules in the database
        List<TenantConfigModules> tenantConfigModulesList = tenantConfigModulesRepository.findAll();
        assertThat(tenantConfigModulesList).hasSize(databaseSizeBeforeCreate + 1);
        TenantConfigModules testTenantConfigModules = tenantConfigModulesList.get(tenantConfigModulesList.size() - 1);
        assertThat(testTenantConfigModules.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testTenantConfigModules.getModules()).isEqualTo(DEFAULT_MODULES);
        assertThat(testTenantConfigModules.isEnabledFlag()).isEqualTo(DEFAULT_ENABLED_FLAG);
        assertThat(testTenantConfigModules.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testTenantConfigModules.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testTenantConfigModules.getContractNum()).isEqualTo(DEFAULT_CONTRACT_NUM);
        assertThat(testTenantConfigModules.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testTenantConfigModules.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testTenantConfigModules.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testTenantConfigModules.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void createTenantConfigModulesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tenantConfigModulesRepository.findAll().size();

        // Create the TenantConfigModules with an existing ID
        tenantConfigModules.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTenantConfigModulesMockMvc.perform(post("/api/tenant-config-modules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tenantConfigModules)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<TenantConfigModules> tenantConfigModulesList = tenantConfigModulesRepository.findAll();
        assertThat(tenantConfigModulesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTenantConfigModules() throws Exception {
        // Initialize the database
        tenantConfigModulesRepository.saveAndFlush(tenantConfigModules);

        // Get all the tenantConfigModulesList
        restTenantConfigModulesMockMvc.perform(get("/api/tenant-config-modules?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tenantConfigModules.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].modules").value(hasItem(DEFAULT_MODULES.toString())))
            .andExpect(jsonPath("$.[*].enabledFlag").value(hasItem(DEFAULT_ENABLED_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].contractNum").value(hasItem(DEFAULT_CONTRACT_NUM.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void getTenantConfigModules() throws Exception {
        // Initialize the database
        tenantConfigModulesRepository.saveAndFlush(tenantConfigModules);

        // Get the tenantConfigModules
        restTenantConfigModulesMockMvc.perform(get("/api/tenant-config-modules/{id}", tenantConfigModules.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tenantConfigModules.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID.intValue()))
            .andExpect(jsonPath("$.modules").value(DEFAULT_MODULES.toString()))
            .andExpect(jsonPath("$.enabledFlag").value(DEFAULT_ENABLED_FLAG.booleanValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.contractNum").value(DEFAULT_CONTRACT_NUM.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingTenantConfigModules() throws Exception {
        // Get the tenantConfigModules
        restTenantConfigModulesMockMvc.perform(get("/api/tenant-config-modules/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTenantConfigModules() throws Exception {
        // Initialize the database
        tenantConfigModulesRepository.saveAndFlush(tenantConfigModules);
        int databaseSizeBeforeUpdate = tenantConfigModulesRepository.findAll().size();

        // Update the tenantConfigModules
        TenantConfigModules updatedTenantConfigModules = tenantConfigModulesRepository.findOne(tenantConfigModules.getId());
        updatedTenantConfigModules
            .tenantId(UPDATED_TENANT_ID)
            .modules(UPDATED_MODULES)
            .enabledFlag(UPDATED_ENABLED_FLAG)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .contractNum(UPDATED_CONTRACT_NUM)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE);

        restTenantConfigModulesMockMvc.perform(put("/api/tenant-config-modules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTenantConfigModules)))
            .andExpect(status().isOk());

        // Validate the TenantConfigModules in the database
        List<TenantConfigModules> tenantConfigModulesList = tenantConfigModulesRepository.findAll();
        assertThat(tenantConfigModulesList).hasSize(databaseSizeBeforeUpdate);
        TenantConfigModules testTenantConfigModules = tenantConfigModulesList.get(tenantConfigModulesList.size() - 1);
        assertThat(testTenantConfigModules.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testTenantConfigModules.getModules()).isEqualTo(UPDATED_MODULES);
        assertThat(testTenantConfigModules.isEnabledFlag()).isEqualTo(UPDATED_ENABLED_FLAG);
        assertThat(testTenantConfigModules.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testTenantConfigModules.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testTenantConfigModules.getContractNum()).isEqualTo(UPDATED_CONTRACT_NUM);
        assertThat(testTenantConfigModules.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTenantConfigModules.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testTenantConfigModules.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testTenantConfigModules.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingTenantConfigModules() throws Exception {
        int databaseSizeBeforeUpdate = tenantConfigModulesRepository.findAll().size();

        // Create the TenantConfigModules

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTenantConfigModulesMockMvc.perform(put("/api/tenant-config-modules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tenantConfigModules)))
            .andExpect(status().isCreated());

        // Validate the TenantConfigModules in the database
        List<TenantConfigModules> tenantConfigModulesList = tenantConfigModulesRepository.findAll();
        assertThat(tenantConfigModulesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTenantConfigModules() throws Exception {
        // Initialize the database
        tenantConfigModulesRepository.saveAndFlush(tenantConfigModules);
        int databaseSizeBeforeDelete = tenantConfigModulesRepository.findAll().size();

        // Get the tenantConfigModules
        restTenantConfigModulesMockMvc.perform(delete("/api/tenant-config-modules/{id}", tenantConfigModules.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TenantConfigModules> tenantConfigModulesList = tenantConfigModulesRepository.findAll();
        assertThat(tenantConfigModulesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TenantConfigModules.class);
    }
}
