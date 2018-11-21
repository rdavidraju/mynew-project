package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.JournalApprovalInfo;
import com.nspl.app.repository.JournalApprovalInfoRepository;
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
 * Test class for the JournalApprovalInfoResource REST controller.
 *
 * @see JournalApprovalInfoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class JournalApprovalInfoResourceIntTest {

    private static final Long DEFAULT_JE_HEADER_ID = 1L;
    private static final Long UPDATED_JE_HEADER_ID = 2L;

    private static final Long DEFAULT_APPROVAL_GROUP_ID = 1L;
    private static final Long UPDATED_APPROVAL_GROUP_ID = 2L;

    private static final Long DEFAULT_APPROVAL_RULE_ID = 1L;
    private static final Long UPDATED_APPROVAL_RULE_ID = 2L;

    private static final ZonedDateTime DEFAULT_APPROVAL_INITIATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_APPROVAL_INITIATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_APPROVAL_BATCH_ID = 1L;
    private static final Long UPDATED_APPROVAL_BATCH_ID = 2L;

    private static final String DEFAULT_APPR_REF_01 = "AAAAAAAAAA";
    private static final String UPDATED_APPR_REF_01 = "BBBBBBBBBB";

    private static final String DEFAULT_APPR_REF_02 = "AAAAAAAAAA";
    private static final String UPDATED_APPR_REF_02 = "BBBBBBBBBB";

    private static final String DEFAULT_APPR_REF_03 = "AAAAAAAAAA";
    private static final String UPDATED_APPR_REF_03 = "BBBBBBBBBB";

    private static final String DEFAULT_APPR_REF_04 = "AAAAAAAAAA";
    private static final String UPDATED_APPR_REF_04 = "BBBBBBBBBB";

    private static final String DEFAULT_APPR_REF_05 = "AAAAAAAAAA";
    private static final String UPDATED_APPR_REF_05 = "BBBBBBBBBB";

    private static final String DEFAULT_APPR_REF_06 = "AAAAAAAAAA";
    private static final String UPDATED_APPR_REF_06 = "BBBBBBBBBB";

    private static final String DEFAULT_APPR_REF_07 = "AAAAAAAAAA";
    private static final String UPDATED_APPR_REF_07 = "BBBBBBBBBB";

    private static final String DEFAULT_APPR_REF_08 = "AAAAAAAAAA";
    private static final String UPDATED_APPR_REF_08 = "BBBBBBBBBB";

    private static final String DEFAULT_APPR_REF_09 = "AAAAAAAAAA";
    private static final String UPDATED_APPR_REF_09 = "BBBBBBBBBB";

    private static final String DEFAULT_APPR_REF_10 = "AAAAAAAAAA";
    private static final String UPDATED_APPR_REF_10 = "BBBBBBBBBB";

    private static final String DEFAULT_APPR_REF_11 = "AAAAAAAAAA";
    private static final String UPDATED_APPR_REF_11 = "BBBBBBBBBB";

    private static final String DEFAULT_APPR_REF_12 = "AAAAAAAAAA";
    private static final String UPDATED_APPR_REF_12 = "BBBBBBBBBB";

    private static final String DEFAULT_APPR_REF_13 = "AAAAAAAAAA";
    private static final String UPDATED_APPR_REF_13 = "BBBBBBBBBB";

    private static final String DEFAULT_APPR_REF_14 = "AAAAAAAAAA";
    private static final String UPDATED_APPR_REF_14 = "BBBBBBBBBB";

    private static final String DEFAULT_APPR_REF_15 = "AAAAAAAAAA";
    private static final String UPDATED_APPR_REF_15 = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_FINAL_ACTION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FINAL_ACTION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_FINAL_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_FINAL_STATUS = "BBBBBBBBBB";

    @Autowired
    private JournalApprovalInfoRepository journalApprovalInfoRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restJournalApprovalInfoMockMvc;

    private JournalApprovalInfo journalApprovalInfo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        JournalApprovalInfoResource journalApprovalInfoResource = new JournalApprovalInfoResource(journalApprovalInfoRepository);
        this.restJournalApprovalInfoMockMvc = MockMvcBuilders.standaloneSetup(journalApprovalInfoResource)
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
    public static JournalApprovalInfo createEntity(EntityManager em) {
        JournalApprovalInfo journalApprovalInfo = new JournalApprovalInfo()
            .jeHeaderId(DEFAULT_JE_HEADER_ID)
            .approvalGroupId(DEFAULT_APPROVAL_GROUP_ID)
            .approvalRuleId(DEFAULT_APPROVAL_RULE_ID)
            .approvalInitiationDate(DEFAULT_APPROVAL_INITIATION_DATE)
            .approvalBatchId(DEFAULT_APPROVAL_BATCH_ID)
            .apprRef01(DEFAULT_APPR_REF_01)
            .apprRef02(DEFAULT_APPR_REF_02)
            .apprRef03(DEFAULT_APPR_REF_03)
            .apprRef04(DEFAULT_APPR_REF_04)
            .apprRef05(DEFAULT_APPR_REF_05)
            .apprRef06(DEFAULT_APPR_REF_06)
            .apprRef07(DEFAULT_APPR_REF_07)
            .apprRef08(DEFAULT_APPR_REF_08)
            .apprRef09(DEFAULT_APPR_REF_09)
            .apprRef10(DEFAULT_APPR_REF_10)
            .apprRef11(DEFAULT_APPR_REF_11)
            .apprRef12(DEFAULT_APPR_REF_12)
            .apprRef13(DEFAULT_APPR_REF_13)
            .apprRef14(DEFAULT_APPR_REF_14)
            .apprRef15(DEFAULT_APPR_REF_15)
            .finalActionDate(DEFAULT_FINAL_ACTION_DATE)
            .finalStatus(DEFAULT_FINAL_STATUS);
        return journalApprovalInfo;
    }

    @Before
    public void initTest() {
        journalApprovalInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createJournalApprovalInfo() throws Exception {
        int databaseSizeBeforeCreate = journalApprovalInfoRepository.findAll().size();

        // Create the JournalApprovalInfo
        restJournalApprovalInfoMockMvc.perform(post("/api/journal-approval-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(journalApprovalInfo)))
            .andExpect(status().isCreated());

        // Validate the JournalApprovalInfo in the database
        List<JournalApprovalInfo> journalApprovalInfoList = journalApprovalInfoRepository.findAll();
        assertThat(journalApprovalInfoList).hasSize(databaseSizeBeforeCreate + 1);
        JournalApprovalInfo testJournalApprovalInfo = journalApprovalInfoList.get(journalApprovalInfoList.size() - 1);
        assertThat(testJournalApprovalInfo.getJeHeaderId()).isEqualTo(DEFAULT_JE_HEADER_ID);
        assertThat(testJournalApprovalInfo.getApprovalGroupId()).isEqualTo(DEFAULT_APPROVAL_GROUP_ID);
        assertThat(testJournalApprovalInfo.getApprovalRuleId()).isEqualTo(DEFAULT_APPROVAL_RULE_ID);
        assertThat(testJournalApprovalInfo.getApprovalInitiationDate()).isEqualTo(DEFAULT_APPROVAL_INITIATION_DATE);
        assertThat(testJournalApprovalInfo.getApprovalBatchId()).isEqualTo(DEFAULT_APPROVAL_BATCH_ID);
        assertThat(testJournalApprovalInfo.getApprRef01()).isEqualTo(DEFAULT_APPR_REF_01);
        assertThat(testJournalApprovalInfo.getApprRef02()).isEqualTo(DEFAULT_APPR_REF_02);
        assertThat(testJournalApprovalInfo.getApprRef03()).isEqualTo(DEFAULT_APPR_REF_03);
        assertThat(testJournalApprovalInfo.getApprRef04()).isEqualTo(DEFAULT_APPR_REF_04);
        assertThat(testJournalApprovalInfo.getApprRef05()).isEqualTo(DEFAULT_APPR_REF_05);
        assertThat(testJournalApprovalInfo.getApprRef06()).isEqualTo(DEFAULT_APPR_REF_06);
        assertThat(testJournalApprovalInfo.getApprRef07()).isEqualTo(DEFAULT_APPR_REF_07);
        assertThat(testJournalApprovalInfo.getApprRef08()).isEqualTo(DEFAULT_APPR_REF_08);
        assertThat(testJournalApprovalInfo.getApprRef09()).isEqualTo(DEFAULT_APPR_REF_09);
        assertThat(testJournalApprovalInfo.getApprRef10()).isEqualTo(DEFAULT_APPR_REF_10);
        assertThat(testJournalApprovalInfo.getApprRef11()).isEqualTo(DEFAULT_APPR_REF_11);
        assertThat(testJournalApprovalInfo.getApprRef12()).isEqualTo(DEFAULT_APPR_REF_12);
        assertThat(testJournalApprovalInfo.getApprRef13()).isEqualTo(DEFAULT_APPR_REF_13);
        assertThat(testJournalApprovalInfo.getApprRef14()).isEqualTo(DEFAULT_APPR_REF_14);
        assertThat(testJournalApprovalInfo.getApprRef15()).isEqualTo(DEFAULT_APPR_REF_15);
        assertThat(testJournalApprovalInfo.getFinalActionDate()).isEqualTo(DEFAULT_FINAL_ACTION_DATE);
        assertThat(testJournalApprovalInfo.getFinalStatus()).isEqualTo(DEFAULT_FINAL_STATUS);
    }

    @Test
    @Transactional
    public void createJournalApprovalInfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = journalApprovalInfoRepository.findAll().size();

        // Create the JournalApprovalInfo with an existing ID
        journalApprovalInfo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJournalApprovalInfoMockMvc.perform(post("/api/journal-approval-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(journalApprovalInfo)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<JournalApprovalInfo> journalApprovalInfoList = journalApprovalInfoRepository.findAll();
        assertThat(journalApprovalInfoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllJournalApprovalInfos() throws Exception {
        // Initialize the database
        journalApprovalInfoRepository.saveAndFlush(journalApprovalInfo);

        // Get all the journalApprovalInfoList
        restJournalApprovalInfoMockMvc.perform(get("/api/journal-approval-infos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(journalApprovalInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].jeHeaderId").value(hasItem(DEFAULT_JE_HEADER_ID.intValue())))
            .andExpect(jsonPath("$.[*].approvalGroupId").value(hasItem(DEFAULT_APPROVAL_GROUP_ID.intValue())))
            .andExpect(jsonPath("$.[*].approvalRuleId").value(hasItem(DEFAULT_APPROVAL_RULE_ID.intValue())))
            .andExpect(jsonPath("$.[*].approvalInitiationDate").value(hasItem(sameInstant(DEFAULT_APPROVAL_INITIATION_DATE))))
            .andExpect(jsonPath("$.[*].approvalBatchId").value(hasItem(DEFAULT_APPROVAL_BATCH_ID.intValue())))
            .andExpect(jsonPath("$.[*].apprRef01").value(hasItem(DEFAULT_APPR_REF_01.toString())))
            .andExpect(jsonPath("$.[*].apprRef02").value(hasItem(DEFAULT_APPR_REF_02.toString())))
            .andExpect(jsonPath("$.[*].apprRef03").value(hasItem(DEFAULT_APPR_REF_03.toString())))
            .andExpect(jsonPath("$.[*].apprRef04").value(hasItem(DEFAULT_APPR_REF_04.toString())))
            .andExpect(jsonPath("$.[*].apprRef05").value(hasItem(DEFAULT_APPR_REF_05.toString())))
            .andExpect(jsonPath("$.[*].apprRef06").value(hasItem(DEFAULT_APPR_REF_06.toString())))
            .andExpect(jsonPath("$.[*].apprRef07").value(hasItem(DEFAULT_APPR_REF_07.toString())))
            .andExpect(jsonPath("$.[*].apprRef08").value(hasItem(DEFAULT_APPR_REF_08.toString())))
            .andExpect(jsonPath("$.[*].apprRef09").value(hasItem(DEFAULT_APPR_REF_09.toString())))
            .andExpect(jsonPath("$.[*].apprRef10").value(hasItem(DEFAULT_APPR_REF_10.toString())))
            .andExpect(jsonPath("$.[*].apprRef11").value(hasItem(DEFAULT_APPR_REF_11.toString())))
            .andExpect(jsonPath("$.[*].apprRef12").value(hasItem(DEFAULT_APPR_REF_12.toString())))
            .andExpect(jsonPath("$.[*].apprRef13").value(hasItem(DEFAULT_APPR_REF_13.toString())))
            .andExpect(jsonPath("$.[*].apprRef14").value(hasItem(DEFAULT_APPR_REF_14.toString())))
            .andExpect(jsonPath("$.[*].apprRef15").value(hasItem(DEFAULT_APPR_REF_15.toString())))
            .andExpect(jsonPath("$.[*].finalActionDate").value(hasItem(sameInstant(DEFAULT_FINAL_ACTION_DATE))))
            .andExpect(jsonPath("$.[*].finalStatus").value(hasItem(DEFAULT_FINAL_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getJournalApprovalInfo() throws Exception {
        // Initialize the database
        journalApprovalInfoRepository.saveAndFlush(journalApprovalInfo);

        // Get the journalApprovalInfo
        restJournalApprovalInfoMockMvc.perform(get("/api/journal-approval-infos/{id}", journalApprovalInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(journalApprovalInfo.getId().intValue()))
            .andExpect(jsonPath("$.jeHeaderId").value(DEFAULT_JE_HEADER_ID.intValue()))
            .andExpect(jsonPath("$.approvalGroupId").value(DEFAULT_APPROVAL_GROUP_ID.intValue()))
            .andExpect(jsonPath("$.approvalRuleId").value(DEFAULT_APPROVAL_RULE_ID.intValue()))
            .andExpect(jsonPath("$.approvalInitiationDate").value(sameInstant(DEFAULT_APPROVAL_INITIATION_DATE)))
            .andExpect(jsonPath("$.approvalBatchId").value(DEFAULT_APPROVAL_BATCH_ID.intValue()))
            .andExpect(jsonPath("$.apprRef01").value(DEFAULT_APPR_REF_01.toString()))
            .andExpect(jsonPath("$.apprRef02").value(DEFAULT_APPR_REF_02.toString()))
            .andExpect(jsonPath("$.apprRef03").value(DEFAULT_APPR_REF_03.toString()))
            .andExpect(jsonPath("$.apprRef04").value(DEFAULT_APPR_REF_04.toString()))
            .andExpect(jsonPath("$.apprRef05").value(DEFAULT_APPR_REF_05.toString()))
            .andExpect(jsonPath("$.apprRef06").value(DEFAULT_APPR_REF_06.toString()))
            .andExpect(jsonPath("$.apprRef07").value(DEFAULT_APPR_REF_07.toString()))
            .andExpect(jsonPath("$.apprRef08").value(DEFAULT_APPR_REF_08.toString()))
            .andExpect(jsonPath("$.apprRef09").value(DEFAULT_APPR_REF_09.toString()))
            .andExpect(jsonPath("$.apprRef10").value(DEFAULT_APPR_REF_10.toString()))
            .andExpect(jsonPath("$.apprRef11").value(DEFAULT_APPR_REF_11.toString()))
            .andExpect(jsonPath("$.apprRef12").value(DEFAULT_APPR_REF_12.toString()))
            .andExpect(jsonPath("$.apprRef13").value(DEFAULT_APPR_REF_13.toString()))
            .andExpect(jsonPath("$.apprRef14").value(DEFAULT_APPR_REF_14.toString()))
            .andExpect(jsonPath("$.apprRef15").value(DEFAULT_APPR_REF_15.toString()))
            .andExpect(jsonPath("$.finalActionDate").value(sameInstant(DEFAULT_FINAL_ACTION_DATE)))
            .andExpect(jsonPath("$.finalStatus").value(DEFAULT_FINAL_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingJournalApprovalInfo() throws Exception {
        // Get the journalApprovalInfo
        restJournalApprovalInfoMockMvc.perform(get("/api/journal-approval-infos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJournalApprovalInfo() throws Exception {
        // Initialize the database
        journalApprovalInfoRepository.saveAndFlush(journalApprovalInfo);
        int databaseSizeBeforeUpdate = journalApprovalInfoRepository.findAll().size();

        // Update the journalApprovalInfo
        JournalApprovalInfo updatedJournalApprovalInfo = journalApprovalInfoRepository.findOne(journalApprovalInfo.getId());
        updatedJournalApprovalInfo
            .jeHeaderId(UPDATED_JE_HEADER_ID)
            .approvalGroupId(UPDATED_APPROVAL_GROUP_ID)
            .approvalRuleId(UPDATED_APPROVAL_RULE_ID)
            .approvalInitiationDate(UPDATED_APPROVAL_INITIATION_DATE)
            .approvalBatchId(UPDATED_APPROVAL_BATCH_ID)
            .apprRef01(UPDATED_APPR_REF_01)
            .apprRef02(UPDATED_APPR_REF_02)
            .apprRef03(UPDATED_APPR_REF_03)
            .apprRef04(UPDATED_APPR_REF_04)
            .apprRef05(UPDATED_APPR_REF_05)
            .apprRef06(UPDATED_APPR_REF_06)
            .apprRef07(UPDATED_APPR_REF_07)
            .apprRef08(UPDATED_APPR_REF_08)
            .apprRef09(UPDATED_APPR_REF_09)
            .apprRef10(UPDATED_APPR_REF_10)
            .apprRef11(UPDATED_APPR_REF_11)
            .apprRef12(UPDATED_APPR_REF_12)
            .apprRef13(UPDATED_APPR_REF_13)
            .apprRef14(UPDATED_APPR_REF_14)
            .apprRef15(UPDATED_APPR_REF_15)
            .finalActionDate(UPDATED_FINAL_ACTION_DATE)
            .finalStatus(UPDATED_FINAL_STATUS);

        restJournalApprovalInfoMockMvc.perform(put("/api/journal-approval-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedJournalApprovalInfo)))
            .andExpect(status().isOk());

        // Validate the JournalApprovalInfo in the database
        List<JournalApprovalInfo> journalApprovalInfoList = journalApprovalInfoRepository.findAll();
        assertThat(journalApprovalInfoList).hasSize(databaseSizeBeforeUpdate);
        JournalApprovalInfo testJournalApprovalInfo = journalApprovalInfoList.get(journalApprovalInfoList.size() - 1);
        assertThat(testJournalApprovalInfo.getJeHeaderId()).isEqualTo(UPDATED_JE_HEADER_ID);
        assertThat(testJournalApprovalInfo.getApprovalGroupId()).isEqualTo(UPDATED_APPROVAL_GROUP_ID);
        assertThat(testJournalApprovalInfo.getApprovalRuleId()).isEqualTo(UPDATED_APPROVAL_RULE_ID);
        assertThat(testJournalApprovalInfo.getApprovalInitiationDate()).isEqualTo(UPDATED_APPROVAL_INITIATION_DATE);
        assertThat(testJournalApprovalInfo.getApprovalBatchId()).isEqualTo(UPDATED_APPROVAL_BATCH_ID);
        assertThat(testJournalApprovalInfo.getApprRef01()).isEqualTo(UPDATED_APPR_REF_01);
        assertThat(testJournalApprovalInfo.getApprRef02()).isEqualTo(UPDATED_APPR_REF_02);
        assertThat(testJournalApprovalInfo.getApprRef03()).isEqualTo(UPDATED_APPR_REF_03);
        assertThat(testJournalApprovalInfo.getApprRef04()).isEqualTo(UPDATED_APPR_REF_04);
        assertThat(testJournalApprovalInfo.getApprRef05()).isEqualTo(UPDATED_APPR_REF_05);
        assertThat(testJournalApprovalInfo.getApprRef06()).isEqualTo(UPDATED_APPR_REF_06);
        assertThat(testJournalApprovalInfo.getApprRef07()).isEqualTo(UPDATED_APPR_REF_07);
        assertThat(testJournalApprovalInfo.getApprRef08()).isEqualTo(UPDATED_APPR_REF_08);
        assertThat(testJournalApprovalInfo.getApprRef09()).isEqualTo(UPDATED_APPR_REF_09);
        assertThat(testJournalApprovalInfo.getApprRef10()).isEqualTo(UPDATED_APPR_REF_10);
        assertThat(testJournalApprovalInfo.getApprRef11()).isEqualTo(UPDATED_APPR_REF_11);
        assertThat(testJournalApprovalInfo.getApprRef12()).isEqualTo(UPDATED_APPR_REF_12);
        assertThat(testJournalApprovalInfo.getApprRef13()).isEqualTo(UPDATED_APPR_REF_13);
        assertThat(testJournalApprovalInfo.getApprRef14()).isEqualTo(UPDATED_APPR_REF_14);
        assertThat(testJournalApprovalInfo.getApprRef15()).isEqualTo(UPDATED_APPR_REF_15);
        assertThat(testJournalApprovalInfo.getFinalActionDate()).isEqualTo(UPDATED_FINAL_ACTION_DATE);
        assertThat(testJournalApprovalInfo.getFinalStatus()).isEqualTo(UPDATED_FINAL_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingJournalApprovalInfo() throws Exception {
        int databaseSizeBeforeUpdate = journalApprovalInfoRepository.findAll().size();

        // Create the JournalApprovalInfo

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restJournalApprovalInfoMockMvc.perform(put("/api/journal-approval-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(journalApprovalInfo)))
            .andExpect(status().isCreated());

        // Validate the JournalApprovalInfo in the database
        List<JournalApprovalInfo> journalApprovalInfoList = journalApprovalInfoRepository.findAll();
        assertThat(journalApprovalInfoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteJournalApprovalInfo() throws Exception {
        // Initialize the database
        journalApprovalInfoRepository.saveAndFlush(journalApprovalInfo);
        int databaseSizeBeforeDelete = journalApprovalInfoRepository.findAll().size();

        // Get the journalApprovalInfo
        restJournalApprovalInfoMockMvc.perform(delete("/api/journal-approval-infos/{id}", journalApprovalInfo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<JournalApprovalInfo> journalApprovalInfoList = journalApprovalInfoRepository.findAll();
        assertThat(journalApprovalInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JournalApprovalInfo.class);
        JournalApprovalInfo journalApprovalInfo1 = new JournalApprovalInfo();
        journalApprovalInfo1.setId(1L);
        JournalApprovalInfo journalApprovalInfo2 = new JournalApprovalInfo();
        journalApprovalInfo2.setId(journalApprovalInfo1.getId());
        assertThat(journalApprovalInfo1).isEqualTo(journalApprovalInfo2);
        journalApprovalInfo2.setId(2L);
        assertThat(journalApprovalInfo1).isNotEqualTo(journalApprovalInfo2);
        journalApprovalInfo1.setId(null);
        assertThat(journalApprovalInfo1).isNotEqualTo(journalApprovalInfo2);
    }
}
