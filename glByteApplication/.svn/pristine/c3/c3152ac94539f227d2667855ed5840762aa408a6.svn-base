package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.BalanceType;
import com.nspl.app.repository.BalanceTypeRepository;
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
 * Test class for the BalanceTypeResource REST controller.
 *
 * @see BalanceTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class BalanceTypeResourceIntTest {

    private static final Long DEFAULT_REPORT_ID = 1L;
    private static final Long UPDATED_REPORT_ID = 2L;

    private static final Long DEFAULT_TENANT_ID = 1L;
    private static final Long UPDATED_TENANT_ID = 2L;

    private static final Long DEFAULT_SRC_ID = 1L;
    private static final Long UPDATED_SRC_ID = 2L;

    private static final String DEFAULT_FIELD_01 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_01 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_02 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_02 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_03 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_03 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_04 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_04 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_05 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_05 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_06 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_06 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_07 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_07 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_08 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_08 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_09 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_09 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_10 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_10 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_11 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_11 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_12 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_12 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_13 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_13 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_14 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_14 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_15 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_15 = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_OPENING_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_OPENING_BALANCE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_ADDITIONS_AMT = new BigDecimal(1);
    private static final BigDecimal UPDATED_ADDITIONS_AMT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_RECONCILIATION_AMT = new BigDecimal(1);
    private static final BigDecimal UPDATED_RECONCILIATION_AMT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_UN_RECON_AMT = new BigDecimal(1);
    private static final BigDecimal UPDATED_UN_RECON_AMT = new BigDecimal(2);

    private static final String DEFAULT_ACCOUNTING_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNTING_STATUS = "BBBBBBBBBB";

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private BalanceTypeRepository balanceTypeRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBalanceTypeMockMvc;

    private BalanceType balanceType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BalanceTypeResource balanceTypeResource = new BalanceTypeResource(balanceTypeRepository);
        this.restBalanceTypeMockMvc = MockMvcBuilders.standaloneSetup(balanceTypeResource)
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
    public static BalanceType createEntity(EntityManager em) {
        BalanceType balanceType = new BalanceType()
            .tenantId(DEFAULT_TENANT_ID)
            .srcId(DEFAULT_SRC_ID)
            .field01(DEFAULT_FIELD_01)
            .field02(DEFAULT_FIELD_02)
            .field03(DEFAULT_FIELD_03)
            .field04(DEFAULT_FIELD_04)
            .field05(DEFAULT_FIELD_05)
            .field06(DEFAULT_FIELD_06)
            .field07(DEFAULT_FIELD_07)
            .field08(DEFAULT_FIELD_08)
            .field09(DEFAULT_FIELD_09)
            .field10(DEFAULT_FIELD_10)
            .field11(DEFAULT_FIELD_11)
            .field12(DEFAULT_FIELD_12)
            .field13(DEFAULT_FIELD_13)
            .field14(DEFAULT_FIELD_14)
            .field15(DEFAULT_FIELD_15)
            .openingBalance(DEFAULT_OPENING_BALANCE)
            .additionsAmt(DEFAULT_ADDITIONS_AMT)
            .createdBy(DEFAULT_CREATED_BY)
            .creationDate(DEFAULT_CREATION_DATE)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE);
        return balanceType;
    }

    @Before
    public void initTest() {
        balanceType = createEntity(em);
    }

    @Test
    @Transactional
    public void createBalanceType() throws Exception {
        int databaseSizeBeforeCreate = balanceTypeRepository.findAll().size();

        // Create the BalanceType
        restBalanceTypeMockMvc.perform(post("/api/balance-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(balanceType)))
            .andExpect(status().isCreated());

        // Validate the BalanceType in the database
        List<BalanceType> balanceTypeList = balanceTypeRepository.findAll();
        assertThat(balanceTypeList).hasSize(databaseSizeBeforeCreate + 1);
        BalanceType testBalanceType = balanceTypeList.get(balanceTypeList.size() - 1);
        assertThat(testBalanceType.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testBalanceType.getSrcId()).isEqualTo(DEFAULT_SRC_ID);
        assertThat(testBalanceType.getField01()).isEqualTo(DEFAULT_FIELD_01);
        assertThat(testBalanceType.getField02()).isEqualTo(DEFAULT_FIELD_02);
        assertThat(testBalanceType.getField03()).isEqualTo(DEFAULT_FIELD_03);
        assertThat(testBalanceType.getField04()).isEqualTo(DEFAULT_FIELD_04);
        assertThat(testBalanceType.getField05()).isEqualTo(DEFAULT_FIELD_05);
        assertThat(testBalanceType.getField06()).isEqualTo(DEFAULT_FIELD_06);
        assertThat(testBalanceType.getField07()).isEqualTo(DEFAULT_FIELD_07);
        assertThat(testBalanceType.getField08()).isEqualTo(DEFAULT_FIELD_08);
        assertThat(testBalanceType.getField09()).isEqualTo(DEFAULT_FIELD_09);
        assertThat(testBalanceType.getField10()).isEqualTo(DEFAULT_FIELD_10);
        assertThat(testBalanceType.getField11()).isEqualTo(DEFAULT_FIELD_11);
        assertThat(testBalanceType.getField12()).isEqualTo(DEFAULT_FIELD_12);
        assertThat(testBalanceType.getField13()).isEqualTo(DEFAULT_FIELD_13);
        assertThat(testBalanceType.getField14()).isEqualTo(DEFAULT_FIELD_14);
        assertThat(testBalanceType.getField15()).isEqualTo(DEFAULT_FIELD_15);
        assertThat(testBalanceType.getOpeningBalance()).isEqualTo(DEFAULT_OPENING_BALANCE);
        assertThat(testBalanceType.getAdditionsAmt()).isEqualTo(DEFAULT_ADDITIONS_AMT);
        assertThat(testBalanceType.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testBalanceType.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testBalanceType.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testBalanceType.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void createBalanceTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = balanceTypeRepository.findAll().size();

        // Create the BalanceType with an existing ID
        balanceType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBalanceTypeMockMvc.perform(post("/api/balance-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(balanceType)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<BalanceType> balanceTypeList = balanceTypeRepository.findAll();
        assertThat(balanceTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllBalanceTypes() throws Exception {
        // Initialize the database
        balanceTypeRepository.saveAndFlush(balanceType);

        // Get all the balanceTypeList
        restBalanceTypeMockMvc.perform(get("/api/balance-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(balanceType.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportId").value(hasItem(DEFAULT_REPORT_ID.intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].srcId").value(hasItem(DEFAULT_SRC_ID.intValue())))
            .andExpect(jsonPath("$.[*].field01").value(hasItem(DEFAULT_FIELD_01.toString())))
            .andExpect(jsonPath("$.[*].field02").value(hasItem(DEFAULT_FIELD_02.toString())))
            .andExpect(jsonPath("$.[*].field03").value(hasItem(DEFAULT_FIELD_03.toString())))
            .andExpect(jsonPath("$.[*].field04").value(hasItem(DEFAULT_FIELD_04.toString())))
            .andExpect(jsonPath("$.[*].field05").value(hasItem(DEFAULT_FIELD_05.toString())))
            .andExpect(jsonPath("$.[*].field06").value(hasItem(DEFAULT_FIELD_06.toString())))
            .andExpect(jsonPath("$.[*].field07").value(hasItem(DEFAULT_FIELD_07.toString())))
            .andExpect(jsonPath("$.[*].field08").value(hasItem(DEFAULT_FIELD_08.toString())))
            .andExpect(jsonPath("$.[*].field09").value(hasItem(DEFAULT_FIELD_09.toString())))
            .andExpect(jsonPath("$.[*].field10").value(hasItem(DEFAULT_FIELD_10.toString())))
            .andExpect(jsonPath("$.[*].field11").value(hasItem(DEFAULT_FIELD_11.toString())))
            .andExpect(jsonPath("$.[*].field12").value(hasItem(DEFAULT_FIELD_12.toString())))
            .andExpect(jsonPath("$.[*].field13").value(hasItem(DEFAULT_FIELD_13.toString())))
            .andExpect(jsonPath("$.[*].field14").value(hasItem(DEFAULT_FIELD_14.toString())))
            .andExpect(jsonPath("$.[*].field15").value(hasItem(DEFAULT_FIELD_15.toString())))
            .andExpect(jsonPath("$.[*].openingBalance").value(hasItem(DEFAULT_OPENING_BALANCE.toString())))
            .andExpect(jsonPath("$.[*].additionsAmt").value(hasItem(DEFAULT_ADDITIONS_AMT.toString())))
            .andExpect(jsonPath("$.[*].reconciliationAmt").value(hasItem(DEFAULT_RECONCILIATION_AMT.intValue())))
            .andExpect(jsonPath("$.[*].unReconAmt").value(hasItem(DEFAULT_UN_RECON_AMT.intValue())))
            .andExpect(jsonPath("$.[*].accountingStatus").value(hasItem(DEFAULT_ACCOUNTING_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void getBalanceType() throws Exception {
        // Initialize the database
        balanceTypeRepository.saveAndFlush(balanceType);

        // Get the balanceType
        restBalanceTypeMockMvc.perform(get("/api/balance-types/{id}", balanceType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(balanceType.getId().intValue()))
            .andExpect(jsonPath("$.reportId").value(DEFAULT_REPORT_ID.intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID.intValue()))
            .andExpect(jsonPath("$.srcId").value(DEFAULT_SRC_ID.intValue()))
            .andExpect(jsonPath("$.field01").value(DEFAULT_FIELD_01.toString()))
            .andExpect(jsonPath("$.field02").value(DEFAULT_FIELD_02.toString()))
            .andExpect(jsonPath("$.field03").value(DEFAULT_FIELD_03.toString()))
            .andExpect(jsonPath("$.field04").value(DEFAULT_FIELD_04.toString()))
            .andExpect(jsonPath("$.field05").value(DEFAULT_FIELD_05.toString()))
            .andExpect(jsonPath("$.field06").value(DEFAULT_FIELD_06.toString()))
            .andExpect(jsonPath("$.field07").value(DEFAULT_FIELD_07.toString()))
            .andExpect(jsonPath("$.field08").value(DEFAULT_FIELD_08.toString()))
            .andExpect(jsonPath("$.field09").value(DEFAULT_FIELD_09.toString()))
            .andExpect(jsonPath("$.field10").value(DEFAULT_FIELD_10.toString()))
            .andExpect(jsonPath("$.field11").value(DEFAULT_FIELD_11.toString()))
            .andExpect(jsonPath("$.field12").value(DEFAULT_FIELD_12.toString()))
            .andExpect(jsonPath("$.field13").value(DEFAULT_FIELD_13.toString()))
            .andExpect(jsonPath("$.field14").value(DEFAULT_FIELD_14.toString()))
            .andExpect(jsonPath("$.field15").value(DEFAULT_FIELD_15.toString()))
            .andExpect(jsonPath("$.openingBalance").value(DEFAULT_OPENING_BALANCE.toString()))
            .andExpect(jsonPath("$.additionsAmt").value(DEFAULT_ADDITIONS_AMT.toString()))
            .andExpect(jsonPath("$.reconciliationAmt").value(DEFAULT_RECONCILIATION_AMT.intValue()))
            .andExpect(jsonPath("$.unReconAmt").value(DEFAULT_UN_RECON_AMT.intValue()))
            .andExpect(jsonPath("$.accountingStatus").value(DEFAULT_ACCOUNTING_STATUS.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.creationDate").value(sameInstant(DEFAULT_CREATION_DATE)))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingBalanceType() throws Exception {
        // Get the balanceType
        restBalanceTypeMockMvc.perform(get("/api/balance-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBalanceType() throws Exception {
        // Initialize the database
        balanceTypeRepository.saveAndFlush(balanceType);
        int databaseSizeBeforeUpdate = balanceTypeRepository.findAll().size();

        // Update the balanceType
        BalanceType updatedBalanceType = balanceTypeRepository.findOne(balanceType.getId());
        updatedBalanceType
            .tenantId(UPDATED_TENANT_ID)
            .srcId(UPDATED_SRC_ID)
            .field01(UPDATED_FIELD_01)
            .field02(UPDATED_FIELD_02)
            .field03(UPDATED_FIELD_03)
            .field04(UPDATED_FIELD_04)
            .field05(UPDATED_FIELD_05)
            .field06(UPDATED_FIELD_06)
            .field07(UPDATED_FIELD_07)
            .field08(UPDATED_FIELD_08)
            .field09(UPDATED_FIELD_09)
            .field10(UPDATED_FIELD_10)
            .field11(UPDATED_FIELD_11)
            .field12(UPDATED_FIELD_12)
            .field13(UPDATED_FIELD_13)
            .field14(UPDATED_FIELD_14)
            .field15(UPDATED_FIELD_15)
            .openingBalance(UPDATED_OPENING_BALANCE)
            .additionsAmt(UPDATED_ADDITIONS_AMT)
            .createdBy(UPDATED_CREATED_BY)
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE);

        restBalanceTypeMockMvc.perform(put("/api/balance-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBalanceType)))
            .andExpect(status().isOk());

        // Validate the BalanceType in the database
        List<BalanceType> balanceTypeList = balanceTypeRepository.findAll();
        assertThat(balanceTypeList).hasSize(databaseSizeBeforeUpdate);
        BalanceType testBalanceType = balanceTypeList.get(balanceTypeList.size() - 1);
        assertThat(testBalanceType.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testBalanceType.getSrcId()).isEqualTo(UPDATED_SRC_ID);
        assertThat(testBalanceType.getField01()).isEqualTo(UPDATED_FIELD_01);
        assertThat(testBalanceType.getField02()).isEqualTo(UPDATED_FIELD_02);
        assertThat(testBalanceType.getField03()).isEqualTo(UPDATED_FIELD_03);
        assertThat(testBalanceType.getField04()).isEqualTo(UPDATED_FIELD_04);
        assertThat(testBalanceType.getField05()).isEqualTo(UPDATED_FIELD_05);
        assertThat(testBalanceType.getField06()).isEqualTo(UPDATED_FIELD_06);
        assertThat(testBalanceType.getField07()).isEqualTo(UPDATED_FIELD_07);
        assertThat(testBalanceType.getField08()).isEqualTo(UPDATED_FIELD_08);
        assertThat(testBalanceType.getField09()).isEqualTo(UPDATED_FIELD_09);
        assertThat(testBalanceType.getField10()).isEqualTo(UPDATED_FIELD_10);
        assertThat(testBalanceType.getField11()).isEqualTo(UPDATED_FIELD_11);
        assertThat(testBalanceType.getField12()).isEqualTo(UPDATED_FIELD_12);
        assertThat(testBalanceType.getField13()).isEqualTo(UPDATED_FIELD_13);
        assertThat(testBalanceType.getField14()).isEqualTo(UPDATED_FIELD_14);
        assertThat(testBalanceType.getField15()).isEqualTo(UPDATED_FIELD_15);
        assertThat(testBalanceType.getOpeningBalance()).isEqualTo(UPDATED_OPENING_BALANCE);
        assertThat(testBalanceType.getAdditionsAmt()).isEqualTo(UPDATED_ADDITIONS_AMT);
        assertThat(testBalanceType.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testBalanceType.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testBalanceType.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testBalanceType.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingBalanceType() throws Exception {
        int databaseSizeBeforeUpdate = balanceTypeRepository.findAll().size();

        // Create the BalanceType

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBalanceTypeMockMvc.perform(put("/api/balance-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(balanceType)))
            .andExpect(status().isCreated());

        // Validate the BalanceType in the database
        List<BalanceType> balanceTypeList = balanceTypeRepository.findAll();
        assertThat(balanceTypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBalanceType() throws Exception {
        // Initialize the database
        balanceTypeRepository.saveAndFlush(balanceType);
        int databaseSizeBeforeDelete = balanceTypeRepository.findAll().size();

        // Get the balanceType
        restBalanceTypeMockMvc.perform(delete("/api/balance-types/{id}", balanceType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<BalanceType> balanceTypeList = balanceTypeRepository.findAll();
        assertThat(balanceTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BalanceType.class);
        BalanceType balanceType1 = new BalanceType();
        balanceType1.setId(1L);
        BalanceType balanceType2 = new BalanceType();
        balanceType2.setId(balanceType1.getId());
        assertThat(balanceType1).isEqualTo(balanceType2);
        balanceType2.setId(2L);
        assertThat(balanceType1).isNotEqualTo(balanceType2);
        balanceType1.setId(null);
        assertThat(balanceType1).isNotEqualTo(balanceType2);
    }
}
