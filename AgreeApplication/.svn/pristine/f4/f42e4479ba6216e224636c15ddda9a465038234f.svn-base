package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.AccountingLineTypes;
import com.nspl.app.repository.AccountingLineTypesRepository;
import com.nspl.app.repository.search.AccountingLineTypesSearchRepository;
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
 * Test class for the AccountingLineTypesResource REST controller.
 *
 * @see AccountingLineTypesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class AccountingLineTypesResourceIntTest {

    private static final Long DEFAULT_TENANT_ID = 1L;
    private static final Long UPDATED_TENANT_ID = 2L;

    private static final Long DEFAULT_RULE_ID = 1L;
    private static final Long UPDATED_RULE_ID = 2L;

    private static final String DEFAULT_LINE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_LINE_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private AccountingLineTypesRepository accountingLineTypesRepository;

    @Autowired
    private AccountingLineTypesSearchRepository accountingLineTypesSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAccountingLineTypesMockMvc;

    private AccountingLineTypes accountingLineTypes;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AccountingLineTypesResource accountingLineTypesResource = new AccountingLineTypesResource(accountingLineTypesRepository, accountingLineTypesSearchRepository);
        this.restAccountingLineTypesMockMvc = MockMvcBuilders.standaloneSetup(accountingLineTypesResource)
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
    public static AccountingLineTypes createEntity(EntityManager em) {
        AccountingLineTypes accountingLineTypes = new AccountingLineTypes()
            .tenantId(DEFAULT_TENANT_ID)
            .ruleId(DEFAULT_RULE_ID)
            .lineType(DEFAULT_LINE_TYPE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .creationDate(DEFAULT_CREATION_DATE)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE);
        return accountingLineTypes;
    }

    @Before
    public void initTest() {
        accountingLineTypesSearchRepository.deleteAll();
        accountingLineTypes = createEntity(em);
    }

    @Test
    @Transactional
    public void createAccountingLineTypes() throws Exception {
        int databaseSizeBeforeCreate = accountingLineTypesRepository.findAll().size();

        // Create the AccountingLineTypes
        restAccountingLineTypesMockMvc.perform(post("/api/accounting-line-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountingLineTypes)))
            .andExpect(status().isCreated());

        // Validate the AccountingLineTypes in the database
        List<AccountingLineTypes> accountingLineTypesList = accountingLineTypesRepository.findAll();
        assertThat(accountingLineTypesList).hasSize(databaseSizeBeforeCreate + 1);
        AccountingLineTypes testAccountingLineTypes = accountingLineTypesList.get(accountingLineTypesList.size() - 1);
        assertThat(testAccountingLineTypes.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testAccountingLineTypes.getRuleId()).isEqualTo(DEFAULT_RULE_ID);
        assertThat(testAccountingLineTypes.getLineType()).isEqualTo(DEFAULT_LINE_TYPE);
        assertThat(testAccountingLineTypes.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testAccountingLineTypes.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testAccountingLineTypes.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testAccountingLineTypes.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);

        // Validate the AccountingLineTypes in Elasticsearch
        AccountingLineTypes accountingLineTypesEs = accountingLineTypesSearchRepository.findOne(testAccountingLineTypes.getId());
        assertThat(accountingLineTypesEs).isEqualToComparingFieldByField(testAccountingLineTypes);
    }

    @Test
    @Transactional
    public void createAccountingLineTypesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = accountingLineTypesRepository.findAll().size();

        // Create the AccountingLineTypes with an existing ID
        accountingLineTypes.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountingLineTypesMockMvc.perform(post("/api/accounting-line-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountingLineTypes)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<AccountingLineTypes> accountingLineTypesList = accountingLineTypesRepository.findAll();
        assertThat(accountingLineTypesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAccountingLineTypes() throws Exception {
        // Initialize the database
        accountingLineTypesRepository.saveAndFlush(accountingLineTypes);

        // Get all the accountingLineTypesList
        restAccountingLineTypesMockMvc.perform(get("/api/accounting-line-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountingLineTypes.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].ruleId").value(hasItem(DEFAULT_RULE_ID.intValue())))
            .andExpect(jsonPath("$.[*].lineType").value(hasItem(DEFAULT_LINE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void getAccountingLineTypes() throws Exception {
        // Initialize the database
        accountingLineTypesRepository.saveAndFlush(accountingLineTypes);

        // Get the accountingLineTypes
        restAccountingLineTypesMockMvc.perform(get("/api/accounting-line-types/{id}", accountingLineTypes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(accountingLineTypes.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID.intValue()))
            .andExpect(jsonPath("$.ruleId").value(DEFAULT_RULE_ID.intValue()))
            .andExpect(jsonPath("$.lineType").value(DEFAULT_LINE_TYPE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.creationDate").value(sameInstant(DEFAULT_CREATION_DATE)))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingAccountingLineTypes() throws Exception {
        // Get the accountingLineTypes
        restAccountingLineTypesMockMvc.perform(get("/api/accounting-line-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAccountingLineTypes() throws Exception {
        // Initialize the database
        accountingLineTypesRepository.saveAndFlush(accountingLineTypes);
        accountingLineTypesSearchRepository.save(accountingLineTypes);
        int databaseSizeBeforeUpdate = accountingLineTypesRepository.findAll().size();

        // Update the accountingLineTypes
        AccountingLineTypes updatedAccountingLineTypes = accountingLineTypesRepository.findOne(accountingLineTypes.getId());
        updatedAccountingLineTypes
            .tenantId(UPDATED_TENANT_ID)
            .ruleId(UPDATED_RULE_ID)
            .lineType(UPDATED_LINE_TYPE)
            .createdBy(UPDATED_CREATED_BY)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE);

        restAccountingLineTypesMockMvc.perform(put("/api/accounting-line-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAccountingLineTypes)))
            .andExpect(status().isOk());

        // Validate the AccountingLineTypes in the database
        List<AccountingLineTypes> accountingLineTypesList = accountingLineTypesRepository.findAll();
        assertThat(accountingLineTypesList).hasSize(databaseSizeBeforeUpdate);
        AccountingLineTypes testAccountingLineTypes = accountingLineTypesList.get(accountingLineTypesList.size() - 1);
        assertThat(testAccountingLineTypes.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testAccountingLineTypes.getRuleId()).isEqualTo(UPDATED_RULE_ID);
        assertThat(testAccountingLineTypes.getLineType()).isEqualTo(UPDATED_LINE_TYPE);
        assertThat(testAccountingLineTypes.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAccountingLineTypes.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testAccountingLineTypes.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testAccountingLineTypes.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);

        // Validate the AccountingLineTypes in Elasticsearch
        AccountingLineTypes accountingLineTypesEs = accountingLineTypesSearchRepository.findOne(testAccountingLineTypes.getId());
        assertThat(accountingLineTypesEs).isEqualToComparingFieldByField(testAccountingLineTypes);
    }

    @Test
    @Transactional
    public void updateNonExistingAccountingLineTypes() throws Exception {
        int databaseSizeBeforeUpdate = accountingLineTypesRepository.findAll().size();

        // Create the AccountingLineTypes

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAccountingLineTypesMockMvc.perform(put("/api/accounting-line-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountingLineTypes)))
            .andExpect(status().isCreated());

        // Validate the AccountingLineTypes in the database
        List<AccountingLineTypes> accountingLineTypesList = accountingLineTypesRepository.findAll();
        assertThat(accountingLineTypesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAccountingLineTypes() throws Exception {
        // Initialize the database
        accountingLineTypesRepository.saveAndFlush(accountingLineTypes);
        accountingLineTypesSearchRepository.save(accountingLineTypes);
        int databaseSizeBeforeDelete = accountingLineTypesRepository.findAll().size();

        // Get the accountingLineTypes
        restAccountingLineTypesMockMvc.perform(delete("/api/accounting-line-types/{id}", accountingLineTypes.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean accountingLineTypesExistsInEs = accountingLineTypesSearchRepository.exists(accountingLineTypes.getId());
        assertThat(accountingLineTypesExistsInEs).isFalse();

        // Validate the database is empty
        List<AccountingLineTypes> accountingLineTypesList = accountingLineTypesRepository.findAll();
        assertThat(accountingLineTypesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAccountingLineTypes() throws Exception {
        // Initialize the database
        accountingLineTypesRepository.saveAndFlush(accountingLineTypes);
        accountingLineTypesSearchRepository.save(accountingLineTypes);

        // Search the accountingLineTypes
        restAccountingLineTypesMockMvc.perform(get("/api/_search/accounting-line-types?query=id:" + accountingLineTypes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountingLineTypes.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].ruleId").value(hasItem(DEFAULT_RULE_ID.intValue())))
            .andExpect(jsonPath("$.[*].lineType").value(hasItem(DEFAULT_LINE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountingLineTypes.class);
    }
}
