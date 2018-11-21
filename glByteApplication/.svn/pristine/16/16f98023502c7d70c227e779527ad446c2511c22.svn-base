package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.AccountingData;
import com.nspl.app.repository.AccountingDataRepository;
import com.nspl.app.repository.search.AccountingDataSearchRepository;
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
 * Test class for the AccountingDataResource REST controller.
 *
 * @see AccountingDataResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class AccountingDataResourceIntTest {

    private static final Long DEFAULT_TENANT_ID = 1L;
    private static final Long UPDATED_TENANT_ID = 2L;

    private static final Long DEFAULT_ORIGINAL_ROW_ID = 1L;
    private static final Long UPDATED_ORIGINAL_ROW_ID = 2L;

    private static final String DEFAULT_ACCOUNTING_REF_1 = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNTING_REF_1 = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNTING_REF_2 = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNTING_REF_2 = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNTING_REF_3 = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNTING_REF_3 = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNTING_REF_4 = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNTING_REF_4 = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNTING_REF_5 = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNTING_REF_5 = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNTING_REF_6 = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNTING_REF_6 = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNTING_REF_7 = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNTING_REF_7 = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNTING_REF_8 = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNTING_REF_8 = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNTING_REF_9 = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNTING_REF_9 = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNTING_REF_10 = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNTING_REF_10 = "BBBBBBBBBB";

    private static final String DEFAULT_LEDGER_REF = "AAAAAAAAAA";
    private static final String UPDATED_LEDGER_REF = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENCY_REF = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY_REF = "BBBBBBBBBB";

    private static final String DEFAULT_LINE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_LINE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_COA_REF = "AAAAAAAAAA";
    private static final String UPDATED_COA_REF = "BBBBBBBBBB";

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private AccountingDataRepository accountingDataRepository;

    @Autowired
    private AccountingDataSearchRepository accountingDataSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAccountingDataMockMvc;

    private AccountingData accountingData;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AccountingDataResource accountingDataResource = new AccountingDataResource(accountingDataRepository, accountingDataSearchRepository);
        this.restAccountingDataMockMvc = MockMvcBuilders.standaloneSetup(accountingDataResource)
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
    public static AccountingData createEntity(EntityManager em) {
        AccountingData accountingData = new AccountingData()
            .tenantId(DEFAULT_TENANT_ID)
            .originalRowId(DEFAULT_ORIGINAL_ROW_ID)
            .accountingRef1(DEFAULT_ACCOUNTING_REF_1)
            .accountingRef2(DEFAULT_ACCOUNTING_REF_2)
            .accountingRef3(DEFAULT_ACCOUNTING_REF_3)
            .accountingRef4(DEFAULT_ACCOUNTING_REF_4)
            .accountingRef5(DEFAULT_ACCOUNTING_REF_5)
            .accountingRef6(DEFAULT_ACCOUNTING_REF_6)
            .accountingRef7(DEFAULT_ACCOUNTING_REF_7)
            .accountingRef8(DEFAULT_ACCOUNTING_REF_8)
            .accountingRef9(DEFAULT_ACCOUNTING_REF_9)
            .accountingRef10(DEFAULT_ACCOUNTING_REF_10)
            .ledgerRef(DEFAULT_LEDGER_REF)
            .currencyRef(DEFAULT_CURRENCY_REF)
            .lineType(DEFAULT_LINE_TYPE)
            .coaRef(DEFAULT_COA_REF)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE);
        return accountingData;
    }

    @Before
    public void initTest() {
        accountingDataSearchRepository.deleteAll();
        accountingData = createEntity(em);
    }

    @Test
    @Transactional
    public void createAccountingData() throws Exception {
        int databaseSizeBeforeCreate = accountingDataRepository.findAll().size();

        // Create the AccountingData
        restAccountingDataMockMvc.perform(post("/api/accounting-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountingData)))
            .andExpect(status().isCreated());

        // Validate the AccountingData in the database
        List<AccountingData> accountingDataList = accountingDataRepository.findAll();
        assertThat(accountingDataList).hasSize(databaseSizeBeforeCreate + 1);
        AccountingData testAccountingData = accountingDataList.get(accountingDataList.size() - 1);
        assertThat(testAccountingData.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testAccountingData.getOriginalRowId()).isEqualTo(DEFAULT_ORIGINAL_ROW_ID);
        assertThat(testAccountingData.getAccountingRef1()).isEqualTo(DEFAULT_ACCOUNTING_REF_1);
        assertThat(testAccountingData.getAccountingRef2()).isEqualTo(DEFAULT_ACCOUNTING_REF_2);
        assertThat(testAccountingData.getAccountingRef3()).isEqualTo(DEFAULT_ACCOUNTING_REF_3);
        assertThat(testAccountingData.getAccountingRef4()).isEqualTo(DEFAULT_ACCOUNTING_REF_4);
        assertThat(testAccountingData.getAccountingRef5()).isEqualTo(DEFAULT_ACCOUNTING_REF_5);
        assertThat(testAccountingData.getAccountingRef6()).isEqualTo(DEFAULT_ACCOUNTING_REF_6);
        assertThat(testAccountingData.getAccountingRef7()).isEqualTo(DEFAULT_ACCOUNTING_REF_7);
        assertThat(testAccountingData.getAccountingRef8()).isEqualTo(DEFAULT_ACCOUNTING_REF_8);
        assertThat(testAccountingData.getAccountingRef9()).isEqualTo(DEFAULT_ACCOUNTING_REF_9);
        assertThat(testAccountingData.getAccountingRef10()).isEqualTo(DEFAULT_ACCOUNTING_REF_10);
        assertThat(testAccountingData.getLedgerRef()).isEqualTo(DEFAULT_LEDGER_REF);
        assertThat(testAccountingData.getCurrencyRef()).isEqualTo(DEFAULT_CURRENCY_REF);
        assertThat(testAccountingData.getLineType()).isEqualTo(DEFAULT_LINE_TYPE);
        assertThat(testAccountingData.getCoaRef()).isEqualTo(DEFAULT_COA_REF);
        assertThat(testAccountingData.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testAccountingData.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testAccountingData.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testAccountingData.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);

        // Validate the AccountingData in Elasticsearch
        AccountingData accountingDataEs = accountingDataSearchRepository.findOne(testAccountingData.getId());
        assertThat(accountingDataEs).isEqualToComparingFieldByField(testAccountingData);
    }

    @Test
    @Transactional
    public void createAccountingDataWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = accountingDataRepository.findAll().size();

        // Create the AccountingData with an existing ID
        accountingData.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountingDataMockMvc.perform(post("/api/accounting-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountingData)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<AccountingData> accountingDataList = accountingDataRepository.findAll();
        assertThat(accountingDataList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAccountingData() throws Exception {
        // Initialize the database
        accountingDataRepository.saveAndFlush(accountingData);

        // Get all the accountingDataList
        restAccountingDataMockMvc.perform(get("/api/accounting-data?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountingData.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].originalRowId").value(hasItem(DEFAULT_ORIGINAL_ROW_ID.intValue())))
            .andExpect(jsonPath("$.[*].accountingRef1").value(hasItem(DEFAULT_ACCOUNTING_REF_1.toString())))
            .andExpect(jsonPath("$.[*].accountingRef2").value(hasItem(DEFAULT_ACCOUNTING_REF_2.toString())))
            .andExpect(jsonPath("$.[*].accountingRef3").value(hasItem(DEFAULT_ACCOUNTING_REF_3.toString())))
            .andExpect(jsonPath("$.[*].accountingRef4").value(hasItem(DEFAULT_ACCOUNTING_REF_4.toString())))
            .andExpect(jsonPath("$.[*].accountingRef5").value(hasItem(DEFAULT_ACCOUNTING_REF_5.toString())))
            .andExpect(jsonPath("$.[*].accountingRef6").value(hasItem(DEFAULT_ACCOUNTING_REF_6.toString())))
            .andExpect(jsonPath("$.[*].accountingRef7").value(hasItem(DEFAULT_ACCOUNTING_REF_7.toString())))
            .andExpect(jsonPath("$.[*].accountingRef8").value(hasItem(DEFAULT_ACCOUNTING_REF_8.toString())))
            .andExpect(jsonPath("$.[*].accountingRef9").value(hasItem(DEFAULT_ACCOUNTING_REF_9.toString())))
            .andExpect(jsonPath("$.[*].accountingRef10").value(hasItem(DEFAULT_ACCOUNTING_REF_10.toString())))
            .andExpect(jsonPath("$.[*].ledgerRef").value(hasItem(DEFAULT_LEDGER_REF.toString())))
            .andExpect(jsonPath("$.[*].currencyRef").value(hasItem(DEFAULT_CURRENCY_REF.toString())))
            .andExpect(jsonPath("$.[*].lineType").value(hasItem(DEFAULT_LINE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].coaRef").value(hasItem(DEFAULT_COA_REF.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void getAccountingData() throws Exception {
        // Initialize the database
        accountingDataRepository.saveAndFlush(accountingData);

        // Get the accountingData
        restAccountingDataMockMvc.perform(get("/api/accounting-data/{id}", accountingData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(accountingData.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID.intValue()))
            .andExpect(jsonPath("$.originalRowId").value(DEFAULT_ORIGINAL_ROW_ID.intValue()))
            .andExpect(jsonPath("$.accountingRef1").value(DEFAULT_ACCOUNTING_REF_1.toString()))
            .andExpect(jsonPath("$.accountingRef2").value(DEFAULT_ACCOUNTING_REF_2.toString()))
            .andExpect(jsonPath("$.accountingRef3").value(DEFAULT_ACCOUNTING_REF_3.toString()))
            .andExpect(jsonPath("$.accountingRef4").value(DEFAULT_ACCOUNTING_REF_4.toString()))
            .andExpect(jsonPath("$.accountingRef5").value(DEFAULT_ACCOUNTING_REF_5.toString()))
            .andExpect(jsonPath("$.accountingRef6").value(DEFAULT_ACCOUNTING_REF_6.toString()))
            .andExpect(jsonPath("$.accountingRef7").value(DEFAULT_ACCOUNTING_REF_7.toString()))
            .andExpect(jsonPath("$.accountingRef8").value(DEFAULT_ACCOUNTING_REF_8.toString()))
            .andExpect(jsonPath("$.accountingRef9").value(DEFAULT_ACCOUNTING_REF_9.toString()))
            .andExpect(jsonPath("$.accountingRef10").value(DEFAULT_ACCOUNTING_REF_10.toString()))
            .andExpect(jsonPath("$.ledgerRef").value(DEFAULT_LEDGER_REF.toString()))
            .andExpect(jsonPath("$.currencyRef").value(DEFAULT_CURRENCY_REF.toString()))
            .andExpect(jsonPath("$.lineType").value(DEFAULT_LINE_TYPE.toString()))
            .andExpect(jsonPath("$.coaRef").value(DEFAULT_COA_REF.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingAccountingData() throws Exception {
        // Get the accountingData
        restAccountingDataMockMvc.perform(get("/api/accounting-data/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAccountingData() throws Exception {
        // Initialize the database
        accountingDataRepository.saveAndFlush(accountingData);
        accountingDataSearchRepository.save(accountingData);
        int databaseSizeBeforeUpdate = accountingDataRepository.findAll().size();

        // Update the accountingData
        AccountingData updatedAccountingData = accountingDataRepository.findOne(accountingData.getId());
        updatedAccountingData
            .tenantId(UPDATED_TENANT_ID)
            .originalRowId(UPDATED_ORIGINAL_ROW_ID)
            .accountingRef1(UPDATED_ACCOUNTING_REF_1)
            .accountingRef2(UPDATED_ACCOUNTING_REF_2)
            .accountingRef3(UPDATED_ACCOUNTING_REF_3)
            .accountingRef4(UPDATED_ACCOUNTING_REF_4)
            .accountingRef5(UPDATED_ACCOUNTING_REF_5)
            .accountingRef6(UPDATED_ACCOUNTING_REF_6)
            .accountingRef7(UPDATED_ACCOUNTING_REF_7)
            .accountingRef8(UPDATED_ACCOUNTING_REF_8)
            .accountingRef9(UPDATED_ACCOUNTING_REF_9)
            .accountingRef10(UPDATED_ACCOUNTING_REF_10)
            .ledgerRef(UPDATED_LEDGER_REF)
            .currencyRef(UPDATED_CURRENCY_REF)
            .lineType(UPDATED_LINE_TYPE)
            .coaRef(UPDATED_COA_REF)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE);

        restAccountingDataMockMvc.perform(put("/api/accounting-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAccountingData)))
            .andExpect(status().isOk());

        // Validate the AccountingData in the database
        List<AccountingData> accountingDataList = accountingDataRepository.findAll();
        assertThat(accountingDataList).hasSize(databaseSizeBeforeUpdate);
        AccountingData testAccountingData = accountingDataList.get(accountingDataList.size() - 1);
        assertThat(testAccountingData.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testAccountingData.getOriginalRowId()).isEqualTo(UPDATED_ORIGINAL_ROW_ID);
        assertThat(testAccountingData.getAccountingRef1()).isEqualTo(UPDATED_ACCOUNTING_REF_1);
        assertThat(testAccountingData.getAccountingRef2()).isEqualTo(UPDATED_ACCOUNTING_REF_2);
        assertThat(testAccountingData.getAccountingRef3()).isEqualTo(UPDATED_ACCOUNTING_REF_3);
        assertThat(testAccountingData.getAccountingRef4()).isEqualTo(UPDATED_ACCOUNTING_REF_4);
        assertThat(testAccountingData.getAccountingRef5()).isEqualTo(UPDATED_ACCOUNTING_REF_5);
        assertThat(testAccountingData.getAccountingRef6()).isEqualTo(UPDATED_ACCOUNTING_REF_6);
        assertThat(testAccountingData.getAccountingRef7()).isEqualTo(UPDATED_ACCOUNTING_REF_7);
        assertThat(testAccountingData.getAccountingRef8()).isEqualTo(UPDATED_ACCOUNTING_REF_8);
        assertThat(testAccountingData.getAccountingRef9()).isEqualTo(UPDATED_ACCOUNTING_REF_9);
        assertThat(testAccountingData.getAccountingRef10()).isEqualTo(UPDATED_ACCOUNTING_REF_10);
        assertThat(testAccountingData.getLedgerRef()).isEqualTo(UPDATED_LEDGER_REF);
        assertThat(testAccountingData.getCurrencyRef()).isEqualTo(UPDATED_CURRENCY_REF);
        assertThat(testAccountingData.getLineType()).isEqualTo(UPDATED_LINE_TYPE);
        assertThat(testAccountingData.getCoaRef()).isEqualTo(UPDATED_COA_REF);
        assertThat(testAccountingData.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAccountingData.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testAccountingData.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testAccountingData.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);

        // Validate the AccountingData in Elasticsearch
        AccountingData accountingDataEs = accountingDataSearchRepository.findOne(testAccountingData.getId());
        assertThat(accountingDataEs).isEqualToComparingFieldByField(testAccountingData);
    }

    @Test
    @Transactional
    public void updateNonExistingAccountingData() throws Exception {
        int databaseSizeBeforeUpdate = accountingDataRepository.findAll().size();

        // Create the AccountingData

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAccountingDataMockMvc.perform(put("/api/accounting-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountingData)))
            .andExpect(status().isCreated());

        // Validate the AccountingData in the database
        List<AccountingData> accountingDataList = accountingDataRepository.findAll();
        assertThat(accountingDataList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAccountingData() throws Exception {
        // Initialize the database
        accountingDataRepository.saveAndFlush(accountingData);
        accountingDataSearchRepository.save(accountingData);
        int databaseSizeBeforeDelete = accountingDataRepository.findAll().size();

        // Get the accountingData
        restAccountingDataMockMvc.perform(delete("/api/accounting-data/{id}", accountingData.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean accountingDataExistsInEs = accountingDataSearchRepository.exists(accountingData.getId());
        assertThat(accountingDataExistsInEs).isFalse();

        // Validate the database is empty
        List<AccountingData> accountingDataList = accountingDataRepository.findAll();
        assertThat(accountingDataList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAccountingData() throws Exception {
        // Initialize the database
        accountingDataRepository.saveAndFlush(accountingData);
        accountingDataSearchRepository.save(accountingData);

        // Search the accountingData
        restAccountingDataMockMvc.perform(get("/api/_search/accounting-data?query=id:" + accountingData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountingData.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].originalRowId").value(hasItem(DEFAULT_ORIGINAL_ROW_ID.intValue())))
            .andExpect(jsonPath("$.[*].accountingRef1").value(hasItem(DEFAULT_ACCOUNTING_REF_1.toString())))
            .andExpect(jsonPath("$.[*].accountingRef2").value(hasItem(DEFAULT_ACCOUNTING_REF_2.toString())))
            .andExpect(jsonPath("$.[*].accountingRef3").value(hasItem(DEFAULT_ACCOUNTING_REF_3.toString())))
            .andExpect(jsonPath("$.[*].accountingRef4").value(hasItem(DEFAULT_ACCOUNTING_REF_4.toString())))
            .andExpect(jsonPath("$.[*].accountingRef5").value(hasItem(DEFAULT_ACCOUNTING_REF_5.toString())))
            .andExpect(jsonPath("$.[*].accountingRef6").value(hasItem(DEFAULT_ACCOUNTING_REF_6.toString())))
            .andExpect(jsonPath("$.[*].accountingRef7").value(hasItem(DEFAULT_ACCOUNTING_REF_7.toString())))
            .andExpect(jsonPath("$.[*].accountingRef8").value(hasItem(DEFAULT_ACCOUNTING_REF_8.toString())))
            .andExpect(jsonPath("$.[*].accountingRef9").value(hasItem(DEFAULT_ACCOUNTING_REF_9.toString())))
            .andExpect(jsonPath("$.[*].accountingRef10").value(hasItem(DEFAULT_ACCOUNTING_REF_10.toString())))
            .andExpect(jsonPath("$.[*].ledgerRef").value(hasItem(DEFAULT_LEDGER_REF.toString())))
            .andExpect(jsonPath("$.[*].currencyRef").value(hasItem(DEFAULT_CURRENCY_REF.toString())))
            .andExpect(jsonPath("$.[*].lineType").value(hasItem(DEFAULT_LINE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].coaRef").value(hasItem(DEFAULT_COA_REF.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountingData.class);
        AccountingData accountingData1 = new AccountingData();
        accountingData1.setId(1L);
        AccountingData accountingData2 = new AccountingData();
        accountingData2.setId(accountingData1.getId());
        assertThat(accountingData1).isEqualTo(accountingData2);
        accountingData2.setId(2L);
        assertThat(accountingData1).isNotEqualTo(accountingData2);
        accountingData1.setId(null);
        assertThat(accountingData1).isNotEqualTo(accountingData2);
    }
}
