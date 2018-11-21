package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.AppModuleSummary;
import com.nspl.app.repository.AppModuleSummaryRepository;
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
 * Test class for the AppModuleSummaryResource REST controller.
 *
 * @see AppModuleSummaryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class AppModuleSummaryResourceIntTest {

    private static final LocalDate DEFAULT_INSERT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INSERT_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_FILE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FILE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_MODULE = "AAAAAAAAAA";
    private static final String UPDATED_MODULE = "BBBBBBBBBB";

    private static final Long DEFAULT_RULE_GROUP_ID = 1L;
    private static final Long UPDATED_RULE_GROUP_ID = 2L;

    private static final Long DEFAULT_RULE_ID = 1L;
    private static final Long UPDATED_RULE_ID = 2L;

    private static final Long DEFAULT_VIEW_ID = 1L;
    private static final Long UPDATED_VIEW_ID = 2L;

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_DV_COUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_DV_COUNT = new BigDecimal(2);

    private static final Long DEFAULT_TYPE_COUNT = 1L;
    private static final Long UPDATED_TYPE_COUNT = 2L;

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private AppModuleSummaryRepository appModuleSummaryRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAppModuleSummaryMockMvc;

    private AppModuleSummary appModuleSummary;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AppModuleSummaryResource appModuleSummaryResource = new AppModuleSummaryResource(appModuleSummaryRepository);
        this.restAppModuleSummaryMockMvc = MockMvcBuilders.standaloneSetup(appModuleSummaryResource)
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
    public static AppModuleSummary createEntity(EntityManager em) {
        AppModuleSummary appModuleSummary = new AppModuleSummary()
            .insertDate(DEFAULT_INSERT_DATE)
            .fileDate(DEFAULT_FILE_DATE)
            .module(DEFAULT_MODULE)
            .ruleGroupId(DEFAULT_RULE_GROUP_ID)
            .ruleId(DEFAULT_RULE_ID)
            .viewId(DEFAULT_VIEW_ID)
            .type(DEFAULT_TYPE)
            .dvCount(DEFAULT_DV_COUNT)
            .typeCount(DEFAULT_TYPE_COUNT)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE);
        return appModuleSummary;
    }

    @Before
    public void initTest() {
        appModuleSummary = createEntity(em);
    }

    @Test
    @Transactional
    public void createAppModuleSummary() throws Exception {
        int databaseSizeBeforeCreate = appModuleSummaryRepository.findAll().size();

        // Create the AppModuleSummary
        restAppModuleSummaryMockMvc.perform(post("/api/app-module-summaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appModuleSummary)))
            .andExpect(status().isCreated());

        // Validate the AppModuleSummary in the database
        List<AppModuleSummary> appModuleSummaryList = appModuleSummaryRepository.findAll();
        assertThat(appModuleSummaryList).hasSize(databaseSizeBeforeCreate + 1);
        AppModuleSummary testAppModuleSummary = appModuleSummaryList.get(appModuleSummaryList.size() - 1);
        assertThat(testAppModuleSummary.getInsertDate()).isEqualTo(DEFAULT_INSERT_DATE);
        assertThat(testAppModuleSummary.getFileDate()).isEqualTo(DEFAULT_FILE_DATE);
        assertThat(testAppModuleSummary.getModule()).isEqualTo(DEFAULT_MODULE);
        assertThat(testAppModuleSummary.getRuleGroupId()).isEqualTo(DEFAULT_RULE_GROUP_ID);
        assertThat(testAppModuleSummary.getRuleId()).isEqualTo(DEFAULT_RULE_ID);
        assertThat(testAppModuleSummary.getViewId()).isEqualTo(DEFAULT_VIEW_ID);
        assertThat(testAppModuleSummary.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testAppModuleSummary.getDvCount()).isEqualTo(DEFAULT_DV_COUNT);
        assertThat(testAppModuleSummary.getTypeCount()).isEqualTo(DEFAULT_TYPE_COUNT);
        assertThat(testAppModuleSummary.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testAppModuleSummary.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testAppModuleSummary.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testAppModuleSummary.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void createAppModuleSummaryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = appModuleSummaryRepository.findAll().size();

        // Create the AppModuleSummary with an existing ID
        appModuleSummary.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppModuleSummaryMockMvc.perform(post("/api/app-module-summaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appModuleSummary)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<AppModuleSummary> appModuleSummaryList = appModuleSummaryRepository.findAll();
        assertThat(appModuleSummaryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAppModuleSummaries() throws Exception {
        // Initialize the database
        appModuleSummaryRepository.saveAndFlush(appModuleSummary);

        // Get all the appModuleSummaryList
        restAppModuleSummaryMockMvc.perform(get("/api/app-module-summaries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appModuleSummary.getId().intValue())))
            .andExpect(jsonPath("$.[*].insertDate").value(hasItem(DEFAULT_INSERT_DATE.toString())))
            .andExpect(jsonPath("$.[*].fileDate").value(hasItem(DEFAULT_FILE_DATE.toString())))
            .andExpect(jsonPath("$.[*].module").value(hasItem(DEFAULT_MODULE.toString())))
            .andExpect(jsonPath("$.[*].ruleGroupId").value(hasItem(DEFAULT_RULE_GROUP_ID.intValue())))
            .andExpect(jsonPath("$.[*].ruleId").value(hasItem(DEFAULT_RULE_ID.intValue())))
            .andExpect(jsonPath("$.[*].viewId").value(hasItem(DEFAULT_VIEW_ID.intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].dvCount").value(hasItem(DEFAULT_DV_COUNT.intValue())))
            .andExpect(jsonPath("$.[*].typeCount").value(hasItem(DEFAULT_TYPE_COUNT.intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void getAppModuleSummary() throws Exception {
        // Initialize the database
        appModuleSummaryRepository.saveAndFlush(appModuleSummary);

        // Get the appModuleSummary
        restAppModuleSummaryMockMvc.perform(get("/api/app-module-summaries/{id}", appModuleSummary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(appModuleSummary.getId().intValue()))
            .andExpect(jsonPath("$.insertDate").value(DEFAULT_INSERT_DATE.toString()))
            .andExpect(jsonPath("$.fileDate").value(DEFAULT_FILE_DATE.toString()))
            .andExpect(jsonPath("$.module").value(DEFAULT_MODULE.toString()))
            .andExpect(jsonPath("$.ruleGroupId").value(DEFAULT_RULE_GROUP_ID.intValue()))
            .andExpect(jsonPath("$.ruleId").value(DEFAULT_RULE_ID.intValue()))
            .andExpect(jsonPath("$.viewId").value(DEFAULT_VIEW_ID.intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.dvCount").value(DEFAULT_DV_COUNT.intValue()))
            .andExpect(jsonPath("$.typeCount").value(DEFAULT_TYPE_COUNT.intValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingAppModuleSummary() throws Exception {
        // Get the appModuleSummary
        restAppModuleSummaryMockMvc.perform(get("/api/app-module-summaries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAppModuleSummary() throws Exception {
        // Initialize the database
        appModuleSummaryRepository.saveAndFlush(appModuleSummary);
        int databaseSizeBeforeUpdate = appModuleSummaryRepository.findAll().size();

        // Update the appModuleSummary
        AppModuleSummary updatedAppModuleSummary = appModuleSummaryRepository.findOne(appModuleSummary.getId());
        updatedAppModuleSummary
            .insertDate(UPDATED_INSERT_DATE)
            .fileDate(UPDATED_FILE_DATE)
            .module(UPDATED_MODULE)
            .ruleGroupId(UPDATED_RULE_GROUP_ID)
            .ruleId(UPDATED_RULE_ID)
            .viewId(UPDATED_VIEW_ID)
            .type(UPDATED_TYPE)
            .dvCount(UPDATED_DV_COUNT)
            .typeCount(UPDATED_TYPE_COUNT)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE);

        restAppModuleSummaryMockMvc.perform(put("/api/app-module-summaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAppModuleSummary)))
            .andExpect(status().isOk());

        // Validate the AppModuleSummary in the database
        List<AppModuleSummary> appModuleSummaryList = appModuleSummaryRepository.findAll();
        assertThat(appModuleSummaryList).hasSize(databaseSizeBeforeUpdate);
        AppModuleSummary testAppModuleSummary = appModuleSummaryList.get(appModuleSummaryList.size() - 1);
        assertThat(testAppModuleSummary.getInsertDate()).isEqualTo(UPDATED_INSERT_DATE);
        assertThat(testAppModuleSummary.getFileDate()).isEqualTo(UPDATED_FILE_DATE);
        assertThat(testAppModuleSummary.getModule()).isEqualTo(UPDATED_MODULE);
        assertThat(testAppModuleSummary.getRuleGroupId()).isEqualTo(UPDATED_RULE_GROUP_ID);
        assertThat(testAppModuleSummary.getRuleId()).isEqualTo(UPDATED_RULE_ID);
        assertThat(testAppModuleSummary.getViewId()).isEqualTo(UPDATED_VIEW_ID);
        assertThat(testAppModuleSummary.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAppModuleSummary.getDvCount()).isEqualTo(UPDATED_DV_COUNT);
        assertThat(testAppModuleSummary.getTypeCount()).isEqualTo(UPDATED_TYPE_COUNT);
        assertThat(testAppModuleSummary.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAppModuleSummary.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testAppModuleSummary.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testAppModuleSummary.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingAppModuleSummary() throws Exception {
        int databaseSizeBeforeUpdate = appModuleSummaryRepository.findAll().size();

        // Create the AppModuleSummary

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAppModuleSummaryMockMvc.perform(put("/api/app-module-summaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appModuleSummary)))
            .andExpect(status().isCreated());

        // Validate the AppModuleSummary in the database
        List<AppModuleSummary> appModuleSummaryList = appModuleSummaryRepository.findAll();
        assertThat(appModuleSummaryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAppModuleSummary() throws Exception {
        // Initialize the database
        appModuleSummaryRepository.saveAndFlush(appModuleSummary);
        int databaseSizeBeforeDelete = appModuleSummaryRepository.findAll().size();

        // Get the appModuleSummary
        restAppModuleSummaryMockMvc.perform(delete("/api/app-module-summaries/{id}", appModuleSummary.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AppModuleSummary> appModuleSummaryList = appModuleSummaryRepository.findAll();
        assertThat(appModuleSummaryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppModuleSummary.class);
        AppModuleSummary appModuleSummary1 = new AppModuleSummary();
        appModuleSummary1.setId(1L);
        AppModuleSummary appModuleSummary2 = new AppModuleSummary();
        appModuleSummary2.setId(appModuleSummary1.getId());
        assertThat(appModuleSummary1).isEqualTo(appModuleSummary2);
        appModuleSummary2.setId(2L);
        assertThat(appModuleSummary1).isNotEqualTo(appModuleSummary2);
        appModuleSummary1.setId(null);
        assertThat(appModuleSummary1).isNotEqualTo(appModuleSummary2);
    }
}
