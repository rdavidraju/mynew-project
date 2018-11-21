package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.FormConfig;
import com.nspl.app.repository.FormConfigRepository;
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
 * Test class for the FormConfigResource REST controller.
 *
 * @see FormConfigResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class FormConfigResourceIntTest {

    private static final Long DEFAULT_TENANT_ID = 1L;
    private static final Long UPDATED_TENANT_ID = 2L;

    private static final String DEFAULT_FORM_CONFIG = "AAAAAAAAAA";
    private static final String UPDATED_FORM_CONFIG = "BBBBBBBBBB";

    private static final String DEFAULT_FORM_LEVEL = "AAAAAAAAAA";
    private static final String UPDATED_FORM_LEVEL = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    @Autowired
    private FormConfigRepository formConfigRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFormConfigMockMvc;

    private FormConfig formConfig;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FormConfigResource formConfigResource = new FormConfigResource(formConfigRepository);
        this.restFormConfigMockMvc = MockMvcBuilders.standaloneSetup(formConfigResource)
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
    public static FormConfig createEntity(EntityManager em) {
        FormConfig formConfig = new FormConfig()
            .tenantId(DEFAULT_TENANT_ID)
            .formConfig(DEFAULT_FORM_CONFIG)
            .formLevel(DEFAULT_FORM_LEVEL)
            .value(DEFAULT_VALUE)
            .createdDate(DEFAULT_CREATED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY);
        return formConfig;
    }

    @Before
    public void initTest() {
        formConfig = createEntity(em);
    }

    @Test
    @Transactional
    public void createFormConfig() throws Exception {
        int databaseSizeBeforeCreate = formConfigRepository.findAll().size();

        // Create the FormConfig
        restFormConfigMockMvc.perform(post("/api/form-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formConfig)))
            .andExpect(status().isCreated());

        // Validate the FormConfig in the database
        List<FormConfig> formConfigList = formConfigRepository.findAll();
        assertThat(formConfigList).hasSize(databaseSizeBeforeCreate + 1);
        FormConfig testFormConfig = formConfigList.get(formConfigList.size() - 1);
        assertThat(testFormConfig.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testFormConfig.getFormConfig()).isEqualTo(DEFAULT_FORM_CONFIG);
        assertThat(testFormConfig.getFormLevel()).isEqualTo(DEFAULT_FORM_LEVEL);
        assertThat(testFormConfig.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testFormConfig.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testFormConfig.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testFormConfig.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);
        assertThat(testFormConfig.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
    }

    @Test
    @Transactional
    public void createFormConfigWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = formConfigRepository.findAll().size();

        // Create the FormConfig with an existing ID
        formConfig.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormConfigMockMvc.perform(post("/api/form-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formConfig)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<FormConfig> formConfigList = formConfigRepository.findAll();
        assertThat(formConfigList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllFormConfigs() throws Exception {
        // Initialize the database
        formConfigRepository.saveAndFlush(formConfig);

        // Get all the formConfigList
        restFormConfigMockMvc.perform(get("/api/form-configs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].formConfig").value(hasItem(DEFAULT_FORM_CONFIG.toString())))
            .andExpect(jsonPath("$.[*].formLevel").value(hasItem(DEFAULT_FORM_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())));
    }

    @Test
    @Transactional
    public void getFormConfig() throws Exception {
        // Initialize the database
        formConfigRepository.saveAndFlush(formConfig);

        // Get the formConfig
        restFormConfigMockMvc.perform(get("/api/form-configs/{id}", formConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(formConfig.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID.intValue()))
            .andExpect(jsonPath("$.formConfig").value(DEFAULT_FORM_CONFIG.toString()))
            .andExpect(jsonPath("$.formLevel").value(DEFAULT_FORM_LEVEL.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFormConfig() throws Exception {
        // Get the formConfig
        restFormConfigMockMvc.perform(get("/api/form-configs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFormConfig() throws Exception {
        // Initialize the database
        formConfigRepository.saveAndFlush(formConfig);
        int databaseSizeBeforeUpdate = formConfigRepository.findAll().size();

        // Update the formConfig
        FormConfig updatedFormConfig = formConfigRepository.findOne(formConfig.getId());
        updatedFormConfig
            .tenantId(UPDATED_TENANT_ID)
            .formConfig(UPDATED_FORM_CONFIG)
            .formLevel(UPDATED_FORM_LEVEL)
            .value(UPDATED_VALUE)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY);

        restFormConfigMockMvc.perform(put("/api/form-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFormConfig)))
            .andExpect(status().isOk());

        // Validate the FormConfig in the database
        List<FormConfig> formConfigList = formConfigRepository.findAll();
        assertThat(formConfigList).hasSize(databaseSizeBeforeUpdate);
        FormConfig testFormConfig = formConfigList.get(formConfigList.size() - 1);
        assertThat(testFormConfig.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testFormConfig.getFormConfig()).isEqualTo(UPDATED_FORM_CONFIG);
        assertThat(testFormConfig.getFormLevel()).isEqualTo(UPDATED_FORM_LEVEL);
        assertThat(testFormConfig.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testFormConfig.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testFormConfig.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testFormConfig.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);
        assertThat(testFormConfig.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
    }

    @Test
    @Transactional
    public void updateNonExistingFormConfig() throws Exception {
        int databaseSizeBeforeUpdate = formConfigRepository.findAll().size();

        // Create the FormConfig

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFormConfigMockMvc.perform(put("/api/form-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formConfig)))
            .andExpect(status().isCreated());

        // Validate the FormConfig in the database
        List<FormConfig> formConfigList = formConfigRepository.findAll();
        assertThat(formConfigList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFormConfig() throws Exception {
        // Initialize the database
        formConfigRepository.saveAndFlush(formConfig);
        int databaseSizeBeforeDelete = formConfigRepository.findAll().size();

        // Get the formConfig
        restFormConfigMockMvc.perform(delete("/api/form-configs/{id}", formConfig.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<FormConfig> formConfigList = formConfigRepository.findAll();
        assertThat(formConfigList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormConfig.class);
        FormConfig formConfig1 = new FormConfig();
        formConfig1.setId(1L);
        FormConfig formConfig2 = new FormConfig();
        formConfig2.setId(formConfig1.getId());
        assertThat(formConfig1).isEqualTo(formConfig2);
        formConfig2.setId(2L);
        assertThat(formConfig1).isNotEqualTo(formConfig2);
        formConfig1.setId(null);
        assertThat(formConfig1).isNotEqualTo(formConfig2);
    }
}
