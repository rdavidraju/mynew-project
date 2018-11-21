package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.TemplateDetails;
import com.nspl.app.repository.TemplateDetailsRepository;
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
 * Test class for the TemplateDetailsResource REST controller.
 *
 * @see TemplateDetailsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class TemplateDetailsResourceIntTest {

    private static final Long DEFAULT_TENANT_ID = 1L;
    private static final Long UPDATED_TENANT_ID = 2L;

    private static final String DEFAULT_TARGET_APP_SOURCE = "AAAAAAAAAA";
    private static final String UPDATED_TARGET_APP_SOURCE = "BBBBBBBBBB";

    private static final String DEFAULT_TEMPLATE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TEMPLATE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Long DEFAULT_VIEW_ID = 1L;
    private static final Long UPDATED_VIEW_ID = 2L;

    private static final String DEFAULT_VIEW_NAME = "AAAAAAAAAA";
    private static final String UPDATED_VIEW_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_ENABLE = false;
    private static final Boolean UPDATED_ENABLE = true;

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private TemplateDetailsRepository templateDetailsRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTemplateDetailsMockMvc;

    private TemplateDetails templateDetails;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TemplateDetailsResource templateDetailsResource = new TemplateDetailsResource(templateDetailsRepository);
        this.restTemplateDetailsMockMvc = MockMvcBuilders.standaloneSetup(templateDetailsResource)
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
    public static TemplateDetails createEntity(EntityManager em) {
        TemplateDetails templateDetails = new TemplateDetails()
            .tenantId(DEFAULT_TENANT_ID)
            .targetAppSource(DEFAULT_TARGET_APP_SOURCE)
            .templateName(DEFAULT_TEMPLATE_NAME)
            .description(DEFAULT_DESCRIPTION)
            .viewId(DEFAULT_VIEW_ID)
            .viewName(DEFAULT_VIEW_NAME)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .enabledFlag(DEFAULT_ENABLE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE);
        return templateDetails;
    }

    @Before
    public void initTest() {
        templateDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createTemplateDetails() throws Exception {
        int databaseSizeBeforeCreate = templateDetailsRepository.findAll().size();

        // Create the TemplateDetails
        restTemplateDetailsMockMvc.perform(post("/api/template-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(templateDetails)))
            .andExpect(status().isCreated());

        // Validate the TemplateDetails in the database
        List<TemplateDetails> templateDetailsList = templateDetailsRepository.findAll();
        assertThat(templateDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        TemplateDetails testTemplateDetails = templateDetailsList.get(templateDetailsList.size() - 1);
        assertThat(testTemplateDetails.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testTemplateDetails.getTargetAppSource()).isEqualTo(DEFAULT_TARGET_APP_SOURCE);
        assertThat(testTemplateDetails.getTemplateName()).isEqualTo(DEFAULT_TEMPLATE_NAME);
        assertThat(testTemplateDetails.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTemplateDetails.getViewId()).isEqualTo(DEFAULT_VIEW_ID);
        assertThat(testTemplateDetails.getViewName()).isEqualTo(DEFAULT_VIEW_NAME);
        assertThat(testTemplateDetails.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testTemplateDetails.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testTemplateDetails.isEnabledFlag()).isEqualTo(DEFAULT_ENABLE);
        assertThat(testTemplateDetails.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testTemplateDetails.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testTemplateDetails.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testTemplateDetails.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void createTemplateDetailsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = templateDetailsRepository.findAll().size();

        // Create the TemplateDetails with an existing ID
        templateDetails.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTemplateDetailsMockMvc.perform(post("/api/template-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(templateDetails)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<TemplateDetails> templateDetailsList = templateDetailsRepository.findAll();
        assertThat(templateDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTemplateDetails() throws Exception {
        // Initialize the database
        templateDetailsRepository.saveAndFlush(templateDetails);

        // Get all the templateDetailsList
        restTemplateDetailsMockMvc.perform(get("/api/template-details?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(templateDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].targetAppSource").value(hasItem(DEFAULT_TARGET_APP_SOURCE.toString())))
            .andExpect(jsonPath("$.[*].templateName").value(hasItem(DEFAULT_TEMPLATE_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].viewId").value(hasItem(DEFAULT_VIEW_ID.toString())))
            .andExpect(jsonPath("$.[*].viewName").value(hasItem(DEFAULT_VIEW_NAME.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].enable").value(hasItem(DEFAULT_ENABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void getTemplateDetails() throws Exception {
        // Initialize the database
        templateDetailsRepository.saveAndFlush(templateDetails);

        // Get the templateDetails
        restTemplateDetailsMockMvc.perform(get("/api/template-details/{id}", templateDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(templateDetails.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID.intValue()))
            .andExpect(jsonPath("$.targetAppSource").value(DEFAULT_TARGET_APP_SOURCE.toString()))
            .andExpect(jsonPath("$.templateName").value(DEFAULT_TEMPLATE_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.viewId").value(DEFAULT_VIEW_ID.toString()))
            .andExpect(jsonPath("$.viewName").value(DEFAULT_VIEW_NAME.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.enable").value(DEFAULT_ENABLE.booleanValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingTemplateDetails() throws Exception {
        // Get the templateDetails
        restTemplateDetailsMockMvc.perform(get("/api/template-details/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTemplateDetails() throws Exception {
        // Initialize the database
        templateDetailsRepository.saveAndFlush(templateDetails);
        int databaseSizeBeforeUpdate = templateDetailsRepository.findAll().size();

        // Update the templateDetails
        TemplateDetails updatedTemplateDetails = templateDetailsRepository.findOne(templateDetails.getId());
        updatedTemplateDetails
            .tenantId(UPDATED_TENANT_ID)
            .targetAppSource(UPDATED_TARGET_APP_SOURCE)
            .templateName(UPDATED_TEMPLATE_NAME)
            .description(UPDATED_DESCRIPTION)
            .viewId(UPDATED_VIEW_ID)
            .viewName(UPDATED_VIEW_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .enabledFlag(UPDATED_ENABLE)
            .createdBy(UPDATED_CREATED_BY)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE);

        restTemplateDetailsMockMvc.perform(put("/api/template-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTemplateDetails)))
            .andExpect(status().isOk());

        // Validate the TemplateDetails in the database
        List<TemplateDetails> templateDetailsList = templateDetailsRepository.findAll();
        assertThat(templateDetailsList).hasSize(databaseSizeBeforeUpdate);
        TemplateDetails testTemplateDetails = templateDetailsList.get(templateDetailsList.size() - 1);
        assertThat(testTemplateDetails.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testTemplateDetails.getTargetAppSource()).isEqualTo(UPDATED_TARGET_APP_SOURCE);
        assertThat(testTemplateDetails.getTemplateName()).isEqualTo(UPDATED_TEMPLATE_NAME);
        assertThat(testTemplateDetails.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTemplateDetails.getViewId()).isEqualTo(UPDATED_VIEW_ID);
        assertThat(testTemplateDetails.getViewName()).isEqualTo(UPDATED_VIEW_NAME);
        assertThat(testTemplateDetails.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testTemplateDetails.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testTemplateDetails.isEnabledFlag()).isEqualTo(UPDATED_ENABLE);
        assertThat(testTemplateDetails.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTemplateDetails.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testTemplateDetails.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testTemplateDetails.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingTemplateDetails() throws Exception {
        int databaseSizeBeforeUpdate = templateDetailsRepository.findAll().size();

        // Create the TemplateDetails

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTemplateDetailsMockMvc.perform(put("/api/template-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(templateDetails)))
            .andExpect(status().isCreated());

        // Validate the TemplateDetails in the database
        List<TemplateDetails> templateDetailsList = templateDetailsRepository.findAll();
        assertThat(templateDetailsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTemplateDetails() throws Exception {
        // Initialize the database
        templateDetailsRepository.saveAndFlush(templateDetails);
        int databaseSizeBeforeDelete = templateDetailsRepository.findAll().size();

        // Get the templateDetails
        restTemplateDetailsMockMvc.perform(delete("/api/template-details/{id}", templateDetails.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TemplateDetails> templateDetailsList = templateDetailsRepository.findAll();
        assertThat(templateDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TemplateDetails.class);
    }
}
