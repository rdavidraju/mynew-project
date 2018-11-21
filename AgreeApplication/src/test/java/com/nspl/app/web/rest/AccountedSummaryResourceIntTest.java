package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.AccountedSummary;
import com.nspl.app.repository.AccountedSummaryRepository;
import com.nspl.app.service.AccountedSummaryService;
import com.nspl.app.repository.search.AccountedSummarySearchRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AccountedSummaryResource REST controller.
 *
 * @see AccountedSummaryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class AccountedSummaryResourceIntTest {

    private static final Long DEFAULT_RULE_ID = 1L;
    private static final Long UPDATED_RULE_ID = 2L;

    private static final Integer DEFAULT_DEBIT_COUNT = 1;
    private static final Integer UPDATED_DEBIT_COUNT = 2;

    private static final Integer DEFAULT_CREDIT_COUNT = 1;
    private static final Integer UPDATED_CREDIT_COUNT = 2;

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    @Autowired
    private AccountedSummaryRepository accountedSummaryRepository;

    @Autowired
    private AccountedSummaryService accountedSummaryService;

    @Autowired
    private AccountedSummarySearchRepository accountedSummarySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAccountedSummaryMockMvc;

    private AccountedSummary accountedSummary;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AccountedSummaryResource accountedSummaryResource = new AccountedSummaryResource(accountedSummaryService);
        this.restAccountedSummaryMockMvc = MockMvcBuilders.standaloneSetup(accountedSummaryResource)
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
    public static AccountedSummary createEntity(EntityManager em) {
        AccountedSummary accountedSummary = new AccountedSummary()
            .ruleId(DEFAULT_RULE_ID)
            .debitCount(DEFAULT_DEBIT_COUNT)
            .creditCount(DEFAULT_CREDIT_COUNT)
            .status(DEFAULT_STATUS);
        return accountedSummary;
    }

    @Before
    public void initTest() {
        accountedSummarySearchRepository.deleteAll();
        accountedSummary = createEntity(em);
    }

    @Test
    @Transactional
    public void createAccountedSummary() throws Exception {
        int databaseSizeBeforeCreate = accountedSummaryRepository.findAll().size();

        // Create the AccountedSummary
        restAccountedSummaryMockMvc.perform(post("/api/accounted-summaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountedSummary)))
            .andExpect(status().isCreated());

        // Validate the AccountedSummary in the database
        List<AccountedSummary> accountedSummaryList = accountedSummaryRepository.findAll();
        assertThat(accountedSummaryList).hasSize(databaseSizeBeforeCreate + 1);
        AccountedSummary testAccountedSummary = accountedSummaryList.get(accountedSummaryList.size() - 1);
        assertThat(testAccountedSummary.getRuleId()).isEqualTo(DEFAULT_RULE_ID);
        assertThat(testAccountedSummary.getDebitCount()).isEqualTo(DEFAULT_DEBIT_COUNT);
        assertThat(testAccountedSummary.getCreditCount()).isEqualTo(DEFAULT_CREDIT_COUNT);
        assertThat(testAccountedSummary.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the AccountedSummary in Elasticsearch
        AccountedSummary accountedSummaryEs = accountedSummarySearchRepository.findOne(testAccountedSummary.getId());
        assertThat(accountedSummaryEs).isEqualToComparingFieldByField(testAccountedSummary);
    }

    @Test
    @Transactional
    public void createAccountedSummaryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = accountedSummaryRepository.findAll().size();

        // Create the AccountedSummary with an existing ID
        accountedSummary.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountedSummaryMockMvc.perform(post("/api/accounted-summaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountedSummary)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<AccountedSummary> accountedSummaryList = accountedSummaryRepository.findAll();
        assertThat(accountedSummaryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAccountedSummaries() throws Exception {
        // Initialize the database
        accountedSummaryRepository.saveAndFlush(accountedSummary);

        // Get all the accountedSummaryList
        restAccountedSummaryMockMvc.perform(get("/api/accounted-summaries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountedSummary.getId().intValue())))
            .andExpect(jsonPath("$.[*].ruleId").value(hasItem(DEFAULT_RULE_ID.intValue())))
            .andExpect(jsonPath("$.[*].debitCount").value(hasItem(DEFAULT_DEBIT_COUNT)))
            .andExpect(jsonPath("$.[*].creditCount").value(hasItem(DEFAULT_CREDIT_COUNT)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getAccountedSummary() throws Exception {
        // Initialize the database
        accountedSummaryRepository.saveAndFlush(accountedSummary);

        // Get the accountedSummary
        restAccountedSummaryMockMvc.perform(get("/api/accounted-summaries/{id}", accountedSummary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(accountedSummary.getId().intValue()))
            .andExpect(jsonPath("$.ruleId").value(DEFAULT_RULE_ID.intValue()))
            .andExpect(jsonPath("$.debitCount").value(DEFAULT_DEBIT_COUNT))
            .andExpect(jsonPath("$.creditCount").value(DEFAULT_CREDIT_COUNT))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAccountedSummary() throws Exception {
        // Get the accountedSummary
        restAccountedSummaryMockMvc.perform(get("/api/accounted-summaries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAccountedSummary() throws Exception {
        // Initialize the database
        accountedSummaryService.save(accountedSummary);

        int databaseSizeBeforeUpdate = accountedSummaryRepository.findAll().size();

        // Update the accountedSummary
        AccountedSummary updatedAccountedSummary = accountedSummaryRepository.findOne(accountedSummary.getId());
        updatedAccountedSummary
            .ruleId(UPDATED_RULE_ID)
            .debitCount(UPDATED_DEBIT_COUNT)
            .creditCount(UPDATED_CREDIT_COUNT)
            .status(UPDATED_STATUS);

        restAccountedSummaryMockMvc.perform(put("/api/accounted-summaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAccountedSummary)))
            .andExpect(status().isOk());

        // Validate the AccountedSummary in the database
        List<AccountedSummary> accountedSummaryList = accountedSummaryRepository.findAll();
        assertThat(accountedSummaryList).hasSize(databaseSizeBeforeUpdate);
        AccountedSummary testAccountedSummary = accountedSummaryList.get(accountedSummaryList.size() - 1);
        assertThat(testAccountedSummary.getRuleId()).isEqualTo(UPDATED_RULE_ID);
        assertThat(testAccountedSummary.getDebitCount()).isEqualTo(UPDATED_DEBIT_COUNT);
        assertThat(testAccountedSummary.getCreditCount()).isEqualTo(UPDATED_CREDIT_COUNT);
        assertThat(testAccountedSummary.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the AccountedSummary in Elasticsearch
        AccountedSummary accountedSummaryEs = accountedSummarySearchRepository.findOne(testAccountedSummary.getId());
        assertThat(accountedSummaryEs).isEqualToComparingFieldByField(testAccountedSummary);
    }

    @Test
    @Transactional
    public void updateNonExistingAccountedSummary() throws Exception {
        int databaseSizeBeforeUpdate = accountedSummaryRepository.findAll().size();

        // Create the AccountedSummary

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAccountedSummaryMockMvc.perform(put("/api/accounted-summaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountedSummary)))
            .andExpect(status().isCreated());

        // Validate the AccountedSummary in the database
        List<AccountedSummary> accountedSummaryList = accountedSummaryRepository.findAll();
        assertThat(accountedSummaryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAccountedSummary() throws Exception {
        // Initialize the database
        accountedSummaryService.save(accountedSummary);

        int databaseSizeBeforeDelete = accountedSummaryRepository.findAll().size();

        // Get the accountedSummary
        restAccountedSummaryMockMvc.perform(delete("/api/accounted-summaries/{id}", accountedSummary.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean accountedSummaryExistsInEs = accountedSummarySearchRepository.exists(accountedSummary.getId());
        assertThat(accountedSummaryExistsInEs).isFalse();

        // Validate the database is empty
        List<AccountedSummary> accountedSummaryList = accountedSummaryRepository.findAll();
        assertThat(accountedSummaryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAccountedSummary() throws Exception {
        // Initialize the database
        accountedSummaryService.save(accountedSummary);

        // Search the accountedSummary
        restAccountedSummaryMockMvc.perform(get("/api/_search/accounted-summaries?query=id:" + accountedSummary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountedSummary.getId().intValue())))
            .andExpect(jsonPath("$.[*].ruleId").value(hasItem(DEFAULT_RULE_ID.intValue())))
            .andExpect(jsonPath("$.[*].debitCount").value(hasItem(DEFAULT_DEBIT_COUNT)))
            .andExpect(jsonPath("$.[*].creditCount").value(hasItem(DEFAULT_CREDIT_COUNT)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountedSummary.class);
    }
}
