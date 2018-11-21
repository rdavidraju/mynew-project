package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.DataStaging;
import com.nspl.app.repository.BatchHeaderRepository;
import com.nspl.app.repository.DataStagingRepository;
import com.nspl.app.repository.SourceFileInbHistoryRepository;
import com.nspl.app.repository.search.DataStagingSearchRepository;
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
import org.springframework.util.Base64Utils;

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
 * Test class for the DataStagingResource REST controller.
 *
 * @see DataStagingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class DataStagingResourceIntTest {

    private static final Long DEFAULT_TENANT_ID = 1L;
    private static final Long UPDATED_TENANT_ID = 2L;

    private static final Long DEFAULT_PROFILE_ID = 1L;
    private static final Long UPDATED_PROFILE_ID = 2L;

    private static final Long DEFAULT_TEMPLATE_ID = 1L;
    private static final Long UPDATED_TEMPLATE_ID = 2L;

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_FILE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FILE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_LINE_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_LINE_CONTENT = "BBBBBBBBBB";

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

    private static final String DEFAULT_FIELD_16 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_16 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_17 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_17 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_18 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_18 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_19 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_19 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_20 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_20 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_21 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_21 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_22 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_22 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_23 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_23 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_24 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_24 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_25 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_25 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_26 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_26 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_27 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_27 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_28 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_28 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_29 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_29 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_30 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_30 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_31 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_31 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_32 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_32 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_33 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_33 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_34 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_34 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_35 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_35 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_36 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_36 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_37 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_37 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_38 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_38 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_39 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_39 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_40 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_40 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_41 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_41 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_42 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_42 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_43 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_43 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_44 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_44 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_45 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_45 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_46 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_46 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_47 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_47 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_48 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_48 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_49 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_49 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_50 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_50 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_51 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_51 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_52 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_52 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_53 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_53 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_54 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_54 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_55 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_55 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_56 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_56 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_57 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_57 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_58 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_58 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_59 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_59 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_60 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_60 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_61 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_61 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_62 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_62 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_63 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_63 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_64 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_64 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_65 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_65 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_66 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_66 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_67 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_67 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_68 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_68 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_69 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_69 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_70 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_70 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_71 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_71 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_72 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_72 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_73 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_73 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_74 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_74 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_75 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_75 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_76 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_76 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_77 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_77 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_78 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_78 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_79 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_79 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_80 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_80 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_81 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_81 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_82 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_82 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_83 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_83 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_84 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_84 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_85 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_85 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_86 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_86 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_87 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_87 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_88 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_88 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_89 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_89 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_90 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_90 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_91 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_91 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_92 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_92 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_93 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_93 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_94 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_94 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_95 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_95 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_96 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_96 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_97 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_97 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_98 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_98 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_99 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_99 = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_100 = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_100 = "BBBBBBBBBB";

    private static final String DEFAULT_RECORD_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_RECORD_STATUS = "BBBBBBBBBB";

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private DataStagingRepository dataStagingRepository;

    @Autowired
    private DataStagingSearchRepository dataStagingSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDataStagingMockMvc;

    private DataStaging dataStaging;
    
    private BatchHeaderRepository batchHeaderRepository;
    
    
    private SourceFileInbHistoryRepository sourceFileInbHistoryRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DataStagingResource dataStagingResource = new DataStagingResource(dataStagingRepository, dataStagingSearchRepository, sourceFileInbHistoryRepository,batchHeaderRepository);
        this.restDataStagingMockMvc = MockMvcBuilders.standaloneSetup(dataStagingResource)
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
    public static DataStaging createEntity(EntityManager em) {
        DataStaging dataStaging = new DataStaging()
            .tenantId(DEFAULT_TENANT_ID)
            .profileId(DEFAULT_PROFILE_ID)
            .templateId(DEFAULT_TEMPLATE_ID)
            .fileName(DEFAULT_FILE_NAME)
            .fileDate(DEFAULT_FILE_DATE)
            .lineContent(DEFAULT_LINE_CONTENT)
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
            .field16(DEFAULT_FIELD_16)
            .field17(DEFAULT_FIELD_17)
            .field18(DEFAULT_FIELD_18)
            .field19(DEFAULT_FIELD_19)
            .field20(DEFAULT_FIELD_20)
            .field21(DEFAULT_FIELD_21)
            .field22(DEFAULT_FIELD_22)
            .field23(DEFAULT_FIELD_23)
            .field24(DEFAULT_FIELD_24)
            .field25(DEFAULT_FIELD_25)
            .field26(DEFAULT_FIELD_26)
            .field27(DEFAULT_FIELD_27)
            .field28(DEFAULT_FIELD_28)
            .field29(DEFAULT_FIELD_29)
            .field30(DEFAULT_FIELD_30)
            .field31(DEFAULT_FIELD_31)
            .field32(DEFAULT_FIELD_32)
            .field33(DEFAULT_FIELD_33)
            .field34(DEFAULT_FIELD_34)
            .field35(DEFAULT_FIELD_35)
            .field36(DEFAULT_FIELD_36)
            .field37(DEFAULT_FIELD_37)
            .field38(DEFAULT_FIELD_38)
            .field39(DEFAULT_FIELD_39)
            .field40(DEFAULT_FIELD_40)
            .field41(DEFAULT_FIELD_41)
            .field42(DEFAULT_FIELD_42)
            .field43(DEFAULT_FIELD_43)
            .field44(DEFAULT_FIELD_44)
            .field45(DEFAULT_FIELD_45)
            .field46(DEFAULT_FIELD_46)
            .field47(DEFAULT_FIELD_47)
            .field48(DEFAULT_FIELD_48)
            .field49(DEFAULT_FIELD_49)
            .field50(DEFAULT_FIELD_50)
            .field51(DEFAULT_FIELD_51)
            .field52(DEFAULT_FIELD_52)
            .field53(DEFAULT_FIELD_53)
            .field54(DEFAULT_FIELD_54)
            .field55(DEFAULT_FIELD_55)
            .field56(DEFAULT_FIELD_56)
            .field57(DEFAULT_FIELD_57)
            .field58(DEFAULT_FIELD_58)
            .field59(DEFAULT_FIELD_59)
            .field60(DEFAULT_FIELD_60)
            .field61(DEFAULT_FIELD_61)
            .field62(DEFAULT_FIELD_62)
            .field63(DEFAULT_FIELD_63)
            .field64(DEFAULT_FIELD_64)
            .field65(DEFAULT_FIELD_65)
            .field66(DEFAULT_FIELD_66)
            .field67(DEFAULT_FIELD_67)
            .field68(DEFAULT_FIELD_68)
            .field69(DEFAULT_FIELD_69)
            .field70(DEFAULT_FIELD_70)
            .field71(DEFAULT_FIELD_71)
            .field72(DEFAULT_FIELD_72)
            .field73(DEFAULT_FIELD_73)
            .field74(DEFAULT_FIELD_74)
            .field75(DEFAULT_FIELD_75)
            .field76(DEFAULT_FIELD_76)
            .field77(DEFAULT_FIELD_77)
            .field78(DEFAULT_FIELD_78)
            .field79(DEFAULT_FIELD_79)
            .field80(DEFAULT_FIELD_80)
            .field81(DEFAULT_FIELD_81)
            .field82(DEFAULT_FIELD_82)
            .field83(DEFAULT_FIELD_83)
            .field84(DEFAULT_FIELD_84)
            .field85(DEFAULT_FIELD_85)
            .field86(DEFAULT_FIELD_86)
            .field87(DEFAULT_FIELD_87)
            .field88(DEFAULT_FIELD_88)
            .field89(DEFAULT_FIELD_89)
            .field90(DEFAULT_FIELD_90)
            .field91(DEFAULT_FIELD_91)
            .field92(DEFAULT_FIELD_92)
            .field93(DEFAULT_FIELD_93)
            .field94(DEFAULT_FIELD_94)
            .field95(DEFAULT_FIELD_95)
            .field96(DEFAULT_FIELD_96)
            .field97(DEFAULT_FIELD_97)
            .field98(DEFAULT_FIELD_98)
            .field99(DEFAULT_FIELD_99)
            .field100(DEFAULT_FIELD_100)
            .recordStatus(DEFAULT_RECORD_STATUS)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE);
        return dataStaging;
    }

    @Before
    public void initTest() {
        dataStagingSearchRepository.deleteAll();
        dataStaging = createEntity(em);
    }

    @Test
    @Transactional
    public void createDataStaging() throws Exception {
        int databaseSizeBeforeCreate = dataStagingRepository.findAll().size();

        // Create the DataStaging
        restDataStagingMockMvc.perform(post("/api/data-stagings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataStaging)))
            .andExpect(status().isCreated());

        // Validate the DataStaging in the database
        List<DataStaging> dataStagingList = dataStagingRepository.findAll();
        assertThat(dataStagingList).hasSize(databaseSizeBeforeCreate + 1);
        DataStaging testDataStaging = dataStagingList.get(dataStagingList.size() - 1);
        assertThat(testDataStaging.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testDataStaging.getProfileId()).isEqualTo(DEFAULT_PROFILE_ID);
        assertThat(testDataStaging.getTemplateId()).isEqualTo(DEFAULT_TEMPLATE_ID);
        assertThat(testDataStaging.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testDataStaging.getFileDate()).isEqualTo(DEFAULT_FILE_DATE);
        assertThat(testDataStaging.getLineContent()).isEqualTo(DEFAULT_LINE_CONTENT);
        assertThat(testDataStaging.getField01()).isEqualTo(DEFAULT_FIELD_01);
        assertThat(testDataStaging.getField02()).isEqualTo(DEFAULT_FIELD_02);
        assertThat(testDataStaging.getField03()).isEqualTo(DEFAULT_FIELD_03);
        assertThat(testDataStaging.getField04()).isEqualTo(DEFAULT_FIELD_04);
        assertThat(testDataStaging.getField05()).isEqualTo(DEFAULT_FIELD_05);
        assertThat(testDataStaging.getField06()).isEqualTo(DEFAULT_FIELD_06);
        assertThat(testDataStaging.getField07()).isEqualTo(DEFAULT_FIELD_07);
        assertThat(testDataStaging.getField08()).isEqualTo(DEFAULT_FIELD_08);
        assertThat(testDataStaging.getField09()).isEqualTo(DEFAULT_FIELD_09);
        assertThat(testDataStaging.getField10()).isEqualTo(DEFAULT_FIELD_10);
        assertThat(testDataStaging.getField11()).isEqualTo(DEFAULT_FIELD_11);
        assertThat(testDataStaging.getField12()).isEqualTo(DEFAULT_FIELD_12);
        assertThat(testDataStaging.getField13()).isEqualTo(DEFAULT_FIELD_13);
        assertThat(testDataStaging.getField14()).isEqualTo(DEFAULT_FIELD_14);
        assertThat(testDataStaging.getField15()).isEqualTo(DEFAULT_FIELD_15);
        assertThat(testDataStaging.getField16()).isEqualTo(DEFAULT_FIELD_16);
        assertThat(testDataStaging.getField17()).isEqualTo(DEFAULT_FIELD_17);
        assertThat(testDataStaging.getField18()).isEqualTo(DEFAULT_FIELD_18);
        assertThat(testDataStaging.getField19()).isEqualTo(DEFAULT_FIELD_19);
        assertThat(testDataStaging.getField20()).isEqualTo(DEFAULT_FIELD_20);
        assertThat(testDataStaging.getField21()).isEqualTo(DEFAULT_FIELD_21);
        assertThat(testDataStaging.getField22()).isEqualTo(DEFAULT_FIELD_22);
        assertThat(testDataStaging.getField23()).isEqualTo(DEFAULT_FIELD_23);
        assertThat(testDataStaging.getField24()).isEqualTo(DEFAULT_FIELD_24);
        assertThat(testDataStaging.getField25()).isEqualTo(DEFAULT_FIELD_25);
        assertThat(testDataStaging.getField26()).isEqualTo(DEFAULT_FIELD_26);
        assertThat(testDataStaging.getField27()).isEqualTo(DEFAULT_FIELD_27);
        assertThat(testDataStaging.getField28()).isEqualTo(DEFAULT_FIELD_28);
        assertThat(testDataStaging.getField29()).isEqualTo(DEFAULT_FIELD_29);
        assertThat(testDataStaging.getField30()).isEqualTo(DEFAULT_FIELD_30);
        assertThat(testDataStaging.getField31()).isEqualTo(DEFAULT_FIELD_31);
        assertThat(testDataStaging.getField32()).isEqualTo(DEFAULT_FIELD_32);
        assertThat(testDataStaging.getField33()).isEqualTo(DEFAULT_FIELD_33);
        assertThat(testDataStaging.getField34()).isEqualTo(DEFAULT_FIELD_34);
        assertThat(testDataStaging.getField35()).isEqualTo(DEFAULT_FIELD_35);
        assertThat(testDataStaging.getField36()).isEqualTo(DEFAULT_FIELD_36);
        assertThat(testDataStaging.getField37()).isEqualTo(DEFAULT_FIELD_37);
        assertThat(testDataStaging.getField38()).isEqualTo(DEFAULT_FIELD_38);
        assertThat(testDataStaging.getField39()).isEqualTo(DEFAULT_FIELD_39);
        assertThat(testDataStaging.getField40()).isEqualTo(DEFAULT_FIELD_40);
        assertThat(testDataStaging.getField41()).isEqualTo(DEFAULT_FIELD_41);
        assertThat(testDataStaging.getField42()).isEqualTo(DEFAULT_FIELD_42);
        assertThat(testDataStaging.getField43()).isEqualTo(DEFAULT_FIELD_43);
        assertThat(testDataStaging.getField44()).isEqualTo(DEFAULT_FIELD_44);
        assertThat(testDataStaging.getField45()).isEqualTo(DEFAULT_FIELD_45);
        assertThat(testDataStaging.getField46()).isEqualTo(DEFAULT_FIELD_46);
        assertThat(testDataStaging.getField47()).isEqualTo(DEFAULT_FIELD_47);
        assertThat(testDataStaging.getField48()).isEqualTo(DEFAULT_FIELD_48);
        assertThat(testDataStaging.getField49()).isEqualTo(DEFAULT_FIELD_49);
        assertThat(testDataStaging.getField50()).isEqualTo(DEFAULT_FIELD_50);
        assertThat(testDataStaging.getField51()).isEqualTo(DEFAULT_FIELD_51);
        assertThat(testDataStaging.getField52()).isEqualTo(DEFAULT_FIELD_52);
        assertThat(testDataStaging.getField53()).isEqualTo(DEFAULT_FIELD_53);
        assertThat(testDataStaging.getField54()).isEqualTo(DEFAULT_FIELD_54);
        assertThat(testDataStaging.getField55()).isEqualTo(DEFAULT_FIELD_55);
        assertThat(testDataStaging.getField56()).isEqualTo(DEFAULT_FIELD_56);
        assertThat(testDataStaging.getField57()).isEqualTo(DEFAULT_FIELD_57);
        assertThat(testDataStaging.getField58()).isEqualTo(DEFAULT_FIELD_58);
        assertThat(testDataStaging.getField59()).isEqualTo(DEFAULT_FIELD_59);
        assertThat(testDataStaging.getField60()).isEqualTo(DEFAULT_FIELD_60);
        assertThat(testDataStaging.getField61()).isEqualTo(DEFAULT_FIELD_61);
        assertThat(testDataStaging.getField62()).isEqualTo(DEFAULT_FIELD_62);
        assertThat(testDataStaging.getField63()).isEqualTo(DEFAULT_FIELD_63);
        assertThat(testDataStaging.getField64()).isEqualTo(DEFAULT_FIELD_64);
        assertThat(testDataStaging.getField65()).isEqualTo(DEFAULT_FIELD_65);
        assertThat(testDataStaging.getField66()).isEqualTo(DEFAULT_FIELD_66);
        assertThat(testDataStaging.getField67()).isEqualTo(DEFAULT_FIELD_67);
        assertThat(testDataStaging.getField68()).isEqualTo(DEFAULT_FIELD_68);
        assertThat(testDataStaging.getField69()).isEqualTo(DEFAULT_FIELD_69);
        assertThat(testDataStaging.getField70()).isEqualTo(DEFAULT_FIELD_70);
        assertThat(testDataStaging.getField71()).isEqualTo(DEFAULT_FIELD_71);
        assertThat(testDataStaging.getField72()).isEqualTo(DEFAULT_FIELD_72);
        assertThat(testDataStaging.getField73()).isEqualTo(DEFAULT_FIELD_73);
        assertThat(testDataStaging.getField74()).isEqualTo(DEFAULT_FIELD_74);
        assertThat(testDataStaging.getField75()).isEqualTo(DEFAULT_FIELD_75);
        assertThat(testDataStaging.getField76()).isEqualTo(DEFAULT_FIELD_76);
        assertThat(testDataStaging.getField77()).isEqualTo(DEFAULT_FIELD_77);
        assertThat(testDataStaging.getField78()).isEqualTo(DEFAULT_FIELD_78);
        assertThat(testDataStaging.getField79()).isEqualTo(DEFAULT_FIELD_79);
        assertThat(testDataStaging.getField80()).isEqualTo(DEFAULT_FIELD_80);
        assertThat(testDataStaging.getField81()).isEqualTo(DEFAULT_FIELD_81);
        assertThat(testDataStaging.getField82()).isEqualTo(DEFAULT_FIELD_82);
        assertThat(testDataStaging.getField83()).isEqualTo(DEFAULT_FIELD_83);
        assertThat(testDataStaging.getField84()).isEqualTo(DEFAULT_FIELD_84);
        assertThat(testDataStaging.getField85()).isEqualTo(DEFAULT_FIELD_85);
        assertThat(testDataStaging.getField86()).isEqualTo(DEFAULT_FIELD_86);
        assertThat(testDataStaging.getField87()).isEqualTo(DEFAULT_FIELD_87);
        assertThat(testDataStaging.getField88()).isEqualTo(DEFAULT_FIELD_88);
        assertThat(testDataStaging.getField89()).isEqualTo(DEFAULT_FIELD_89);
        assertThat(testDataStaging.getField90()).isEqualTo(DEFAULT_FIELD_90);
        assertThat(testDataStaging.getField91()).isEqualTo(DEFAULT_FIELD_91);
        assertThat(testDataStaging.getField92()).isEqualTo(DEFAULT_FIELD_92);
        assertThat(testDataStaging.getField93()).isEqualTo(DEFAULT_FIELD_93);
        assertThat(testDataStaging.getField94()).isEqualTo(DEFAULT_FIELD_94);
        assertThat(testDataStaging.getField95()).isEqualTo(DEFAULT_FIELD_95);
        assertThat(testDataStaging.getField96()).isEqualTo(DEFAULT_FIELD_96);
        assertThat(testDataStaging.getField97()).isEqualTo(DEFAULT_FIELD_97);
        assertThat(testDataStaging.getField98()).isEqualTo(DEFAULT_FIELD_98);
        assertThat(testDataStaging.getField99()).isEqualTo(DEFAULT_FIELD_99);
        assertThat(testDataStaging.getField100()).isEqualTo(DEFAULT_FIELD_100);
        assertThat(testDataStaging.getRecordStatus()).isEqualTo(DEFAULT_RECORD_STATUS);
        assertThat(testDataStaging.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testDataStaging.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testDataStaging.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testDataStaging.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);

        // Validate the DataStaging in Elasticsearch
        DataStaging dataStagingEs = dataStagingSearchRepository.findOne(testDataStaging.getId());
        assertThat(dataStagingEs).isEqualToComparingFieldByField(testDataStaging);
    }

    @Test
    @Transactional
    public void createDataStagingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dataStagingRepository.findAll().size();

        // Create the DataStaging with an existing ID
        dataStaging.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDataStagingMockMvc.perform(post("/api/data-stagings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataStaging)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<DataStaging> dataStagingList = dataStagingRepository.findAll();
        assertThat(dataStagingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDataStagings() throws Exception {
        // Initialize the database
        dataStagingRepository.saveAndFlush(dataStaging);

        // Get all the dataStagingList
        restDataStagingMockMvc.perform(get("/api/data-stagings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataStaging.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].profileId").value(hasItem(DEFAULT_PROFILE_ID.intValue())))
            .andExpect(jsonPath("$.[*].templateId").value(hasItem(DEFAULT_TEMPLATE_ID.intValue())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME.toString())))
            .andExpect(jsonPath("$.[*].fileDate").value(hasItem(sameInstant(DEFAULT_FILE_DATE))))
            .andExpect(jsonPath("$.[*].lineContent").value(hasItem(DEFAULT_LINE_CONTENT.toString())))
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
            .andExpect(jsonPath("$.[*].field16").value(hasItem(DEFAULT_FIELD_16.toString())))
            .andExpect(jsonPath("$.[*].field17").value(hasItem(DEFAULT_FIELD_17.toString())))
            .andExpect(jsonPath("$.[*].field18").value(hasItem(DEFAULT_FIELD_18.toString())))
            .andExpect(jsonPath("$.[*].field19").value(hasItem(DEFAULT_FIELD_19.toString())))
            .andExpect(jsonPath("$.[*].field20").value(hasItem(DEFAULT_FIELD_20.toString())))
            .andExpect(jsonPath("$.[*].field21").value(hasItem(DEFAULT_FIELD_21.toString())))
            .andExpect(jsonPath("$.[*].field22").value(hasItem(DEFAULT_FIELD_22.toString())))
            .andExpect(jsonPath("$.[*].field23").value(hasItem(DEFAULT_FIELD_23.toString())))
            .andExpect(jsonPath("$.[*].field24").value(hasItem(DEFAULT_FIELD_24.toString())))
            .andExpect(jsonPath("$.[*].field25").value(hasItem(DEFAULT_FIELD_25.toString())))
            .andExpect(jsonPath("$.[*].field26").value(hasItem(DEFAULT_FIELD_26.toString())))
            .andExpect(jsonPath("$.[*].field27").value(hasItem(DEFAULT_FIELD_27.toString())))
            .andExpect(jsonPath("$.[*].field28").value(hasItem(DEFAULT_FIELD_28.toString())))
            .andExpect(jsonPath("$.[*].field29").value(hasItem(DEFAULT_FIELD_29.toString())))
            .andExpect(jsonPath("$.[*].field30").value(hasItem(DEFAULT_FIELD_30.toString())))
            .andExpect(jsonPath("$.[*].field31").value(hasItem(DEFAULT_FIELD_31.toString())))
            .andExpect(jsonPath("$.[*].field32").value(hasItem(DEFAULT_FIELD_32.toString())))
            .andExpect(jsonPath("$.[*].field33").value(hasItem(DEFAULT_FIELD_33.toString())))
            .andExpect(jsonPath("$.[*].field34").value(hasItem(DEFAULT_FIELD_34.toString())))
            .andExpect(jsonPath("$.[*].field35").value(hasItem(DEFAULT_FIELD_35.toString())))
            .andExpect(jsonPath("$.[*].field36").value(hasItem(DEFAULT_FIELD_36.toString())))
            .andExpect(jsonPath("$.[*].field37").value(hasItem(DEFAULT_FIELD_37.toString())))
            .andExpect(jsonPath("$.[*].field38").value(hasItem(DEFAULT_FIELD_38.toString())))
            .andExpect(jsonPath("$.[*].field39").value(hasItem(DEFAULT_FIELD_39.toString())))
            .andExpect(jsonPath("$.[*].field40").value(hasItem(DEFAULT_FIELD_40.toString())))
            .andExpect(jsonPath("$.[*].field41").value(hasItem(DEFAULT_FIELD_41.toString())))
            .andExpect(jsonPath("$.[*].field42").value(hasItem(DEFAULT_FIELD_42.toString())))
            .andExpect(jsonPath("$.[*].field43").value(hasItem(DEFAULT_FIELD_43.toString())))
            .andExpect(jsonPath("$.[*].field44").value(hasItem(DEFAULT_FIELD_44.toString())))
            .andExpect(jsonPath("$.[*].field45").value(hasItem(DEFAULT_FIELD_45.toString())))
            .andExpect(jsonPath("$.[*].field46").value(hasItem(DEFAULT_FIELD_46.toString())))
            .andExpect(jsonPath("$.[*].field47").value(hasItem(DEFAULT_FIELD_47.toString())))
            .andExpect(jsonPath("$.[*].field48").value(hasItem(DEFAULT_FIELD_48.toString())))
            .andExpect(jsonPath("$.[*].field49").value(hasItem(DEFAULT_FIELD_49.toString())))
            .andExpect(jsonPath("$.[*].field50").value(hasItem(DEFAULT_FIELD_50.toString())))
            .andExpect(jsonPath("$.[*].field51").value(hasItem(DEFAULT_FIELD_51.toString())))
            .andExpect(jsonPath("$.[*].field52").value(hasItem(DEFAULT_FIELD_52.toString())))
            .andExpect(jsonPath("$.[*].field53").value(hasItem(DEFAULT_FIELD_53.toString())))
            .andExpect(jsonPath("$.[*].field54").value(hasItem(DEFAULT_FIELD_54.toString())))
            .andExpect(jsonPath("$.[*].field55").value(hasItem(DEFAULT_FIELD_55.toString())))
            .andExpect(jsonPath("$.[*].field56").value(hasItem(DEFAULT_FIELD_56.toString())))
            .andExpect(jsonPath("$.[*].field57").value(hasItem(DEFAULT_FIELD_57.toString())))
            .andExpect(jsonPath("$.[*].field58").value(hasItem(DEFAULT_FIELD_58.toString())))
            .andExpect(jsonPath("$.[*].field59").value(hasItem(DEFAULT_FIELD_59.toString())))
            .andExpect(jsonPath("$.[*].field60").value(hasItem(DEFAULT_FIELD_60.toString())))
            .andExpect(jsonPath("$.[*].field61").value(hasItem(DEFAULT_FIELD_61.toString())))
            .andExpect(jsonPath("$.[*].field62").value(hasItem(DEFAULT_FIELD_62.toString())))
            .andExpect(jsonPath("$.[*].field63").value(hasItem(DEFAULT_FIELD_63.toString())))
            .andExpect(jsonPath("$.[*].field64").value(hasItem(DEFAULT_FIELD_64.toString())))
            .andExpect(jsonPath("$.[*].field65").value(hasItem(DEFAULT_FIELD_65.toString())))
            .andExpect(jsonPath("$.[*].field66").value(hasItem(DEFAULT_FIELD_66.toString())))
            .andExpect(jsonPath("$.[*].field67").value(hasItem(DEFAULT_FIELD_67.toString())))
            .andExpect(jsonPath("$.[*].field68").value(hasItem(DEFAULT_FIELD_68.toString())))
            .andExpect(jsonPath("$.[*].field69").value(hasItem(DEFAULT_FIELD_69.toString())))
            .andExpect(jsonPath("$.[*].field70").value(hasItem(DEFAULT_FIELD_70.toString())))
            .andExpect(jsonPath("$.[*].field71").value(hasItem(DEFAULT_FIELD_71.toString())))
            .andExpect(jsonPath("$.[*].field72").value(hasItem(DEFAULT_FIELD_72.toString())))
            .andExpect(jsonPath("$.[*].field73").value(hasItem(DEFAULT_FIELD_73.toString())))
            .andExpect(jsonPath("$.[*].field74").value(hasItem(DEFAULT_FIELD_74.toString())))
            .andExpect(jsonPath("$.[*].field75").value(hasItem(DEFAULT_FIELD_75.toString())))
            .andExpect(jsonPath("$.[*].field76").value(hasItem(DEFAULT_FIELD_76.toString())))
            .andExpect(jsonPath("$.[*].field77").value(hasItem(DEFAULT_FIELD_77.toString())))
            .andExpect(jsonPath("$.[*].field78").value(hasItem(DEFAULT_FIELD_78.toString())))
            .andExpect(jsonPath("$.[*].field79").value(hasItem(DEFAULT_FIELD_79.toString())))
            .andExpect(jsonPath("$.[*].field80").value(hasItem(DEFAULT_FIELD_80.toString())))
            .andExpect(jsonPath("$.[*].field81").value(hasItem(DEFAULT_FIELD_81.toString())))
            .andExpect(jsonPath("$.[*].field82").value(hasItem(DEFAULT_FIELD_82.toString())))
            .andExpect(jsonPath("$.[*].field83").value(hasItem(DEFAULT_FIELD_83.toString())))
            .andExpect(jsonPath("$.[*].field84").value(hasItem(DEFAULT_FIELD_84.toString())))
            .andExpect(jsonPath("$.[*].field85").value(hasItem(DEFAULT_FIELD_85.toString())))
            .andExpect(jsonPath("$.[*].field86").value(hasItem(DEFAULT_FIELD_86.toString())))
            .andExpect(jsonPath("$.[*].field87").value(hasItem(DEFAULT_FIELD_87.toString())))
            .andExpect(jsonPath("$.[*].field88").value(hasItem(DEFAULT_FIELD_88.toString())))
            .andExpect(jsonPath("$.[*].field89").value(hasItem(DEFAULT_FIELD_89.toString())))
            .andExpect(jsonPath("$.[*].field90").value(hasItem(DEFAULT_FIELD_90.toString())))
            .andExpect(jsonPath("$.[*].field91").value(hasItem(DEFAULT_FIELD_91.toString())))
            .andExpect(jsonPath("$.[*].field92").value(hasItem(DEFAULT_FIELD_92.toString())))
            .andExpect(jsonPath("$.[*].field93").value(hasItem(DEFAULT_FIELD_93.toString())))
            .andExpect(jsonPath("$.[*].field94").value(hasItem(DEFAULT_FIELD_94.toString())))
            .andExpect(jsonPath("$.[*].field95").value(hasItem(DEFAULT_FIELD_95.toString())))
            .andExpect(jsonPath("$.[*].field96").value(hasItem(DEFAULT_FIELD_96.toString())))
            .andExpect(jsonPath("$.[*].field97").value(hasItem(DEFAULT_FIELD_97.toString())))
            .andExpect(jsonPath("$.[*].field98").value(hasItem(DEFAULT_FIELD_98.toString())))
            .andExpect(jsonPath("$.[*].field99").value(hasItem(DEFAULT_FIELD_99.toString())))
            .andExpect(jsonPath("$.[*].field100").value(hasItem(DEFAULT_FIELD_100.toString())))
            .andExpect(jsonPath("$.[*].recordStatus").value(hasItem(DEFAULT_RECORD_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void getDataStaging() throws Exception {
        // Initialize the database
        dataStagingRepository.saveAndFlush(dataStaging);

        // Get the dataStaging
        restDataStagingMockMvc.perform(get("/api/data-stagings/{id}", dataStaging.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dataStaging.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID.intValue()))
            .andExpect(jsonPath("$.profileId").value(DEFAULT_PROFILE_ID.intValue()))
            .andExpect(jsonPath("$.templateId").value(DEFAULT_TEMPLATE_ID.intValue()))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME.toString()))
            .andExpect(jsonPath("$.fileDate").value(sameInstant(DEFAULT_FILE_DATE)))
            .andExpect(jsonPath("$.lineContent").value(DEFAULT_LINE_CONTENT.toString()))
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
            .andExpect(jsonPath("$.field16").value(DEFAULT_FIELD_16.toString()))
            .andExpect(jsonPath("$.field17").value(DEFAULT_FIELD_17.toString()))
            .andExpect(jsonPath("$.field18").value(DEFAULT_FIELD_18.toString()))
            .andExpect(jsonPath("$.field19").value(DEFAULT_FIELD_19.toString()))
            .andExpect(jsonPath("$.field20").value(DEFAULT_FIELD_20.toString()))
            .andExpect(jsonPath("$.field21").value(DEFAULT_FIELD_21.toString()))
            .andExpect(jsonPath("$.field22").value(DEFAULT_FIELD_22.toString()))
            .andExpect(jsonPath("$.field23").value(DEFAULT_FIELD_23.toString()))
            .andExpect(jsonPath("$.field24").value(DEFAULT_FIELD_24.toString()))
            .andExpect(jsonPath("$.field25").value(DEFAULT_FIELD_25.toString()))
            .andExpect(jsonPath("$.field26").value(DEFAULT_FIELD_26.toString()))
            .andExpect(jsonPath("$.field27").value(DEFAULT_FIELD_27.toString()))
            .andExpect(jsonPath("$.field28").value(DEFAULT_FIELD_28.toString()))
            .andExpect(jsonPath("$.field29").value(DEFAULT_FIELD_29.toString()))
            .andExpect(jsonPath("$.field30").value(DEFAULT_FIELD_30.toString()))
            .andExpect(jsonPath("$.field31").value(DEFAULT_FIELD_31.toString()))
            .andExpect(jsonPath("$.field32").value(DEFAULT_FIELD_32.toString()))
            .andExpect(jsonPath("$.field33").value(DEFAULT_FIELD_33.toString()))
            .andExpect(jsonPath("$.field34").value(DEFAULT_FIELD_34.toString()))
            .andExpect(jsonPath("$.field35").value(DEFAULT_FIELD_35.toString()))
            .andExpect(jsonPath("$.field36").value(DEFAULT_FIELD_36.toString()))
            .andExpect(jsonPath("$.field37").value(DEFAULT_FIELD_37.toString()))
            .andExpect(jsonPath("$.field38").value(DEFAULT_FIELD_38.toString()))
            .andExpect(jsonPath("$.field39").value(DEFAULT_FIELD_39.toString()))
            .andExpect(jsonPath("$.field40").value(DEFAULT_FIELD_40.toString()))
            .andExpect(jsonPath("$.field41").value(DEFAULT_FIELD_41.toString()))
            .andExpect(jsonPath("$.field42").value(DEFAULT_FIELD_42.toString()))
            .andExpect(jsonPath("$.field43").value(DEFAULT_FIELD_43.toString()))
            .andExpect(jsonPath("$.field44").value(DEFAULT_FIELD_44.toString()))
            .andExpect(jsonPath("$.field45").value(DEFAULT_FIELD_45.toString()))
            .andExpect(jsonPath("$.field46").value(DEFAULT_FIELD_46.toString()))
            .andExpect(jsonPath("$.field47").value(DEFAULT_FIELD_47.toString()))
            .andExpect(jsonPath("$.field48").value(DEFAULT_FIELD_48.toString()))
            .andExpect(jsonPath("$.field49").value(DEFAULT_FIELD_49.toString()))
            .andExpect(jsonPath("$.field50").value(DEFAULT_FIELD_50.toString()))
            .andExpect(jsonPath("$.field51").value(DEFAULT_FIELD_51.toString()))
            .andExpect(jsonPath("$.field52").value(DEFAULT_FIELD_52.toString()))
            .andExpect(jsonPath("$.field53").value(DEFAULT_FIELD_53.toString()))
            .andExpect(jsonPath("$.field54").value(DEFAULT_FIELD_54.toString()))
            .andExpect(jsonPath("$.field55").value(DEFAULT_FIELD_55.toString()))
            .andExpect(jsonPath("$.field56").value(DEFAULT_FIELD_56.toString()))
            .andExpect(jsonPath("$.field57").value(DEFAULT_FIELD_57.toString()))
            .andExpect(jsonPath("$.field58").value(DEFAULT_FIELD_58.toString()))
            .andExpect(jsonPath("$.field59").value(DEFAULT_FIELD_59.toString()))
            .andExpect(jsonPath("$.field60").value(DEFAULT_FIELD_60.toString()))
            .andExpect(jsonPath("$.field61").value(DEFAULT_FIELD_61.toString()))
            .andExpect(jsonPath("$.field62").value(DEFAULT_FIELD_62.toString()))
            .andExpect(jsonPath("$.field63").value(DEFAULT_FIELD_63.toString()))
            .andExpect(jsonPath("$.field64").value(DEFAULT_FIELD_64.toString()))
            .andExpect(jsonPath("$.field65").value(DEFAULT_FIELD_65.toString()))
            .andExpect(jsonPath("$.field66").value(DEFAULT_FIELD_66.toString()))
            .andExpect(jsonPath("$.field67").value(DEFAULT_FIELD_67.toString()))
            .andExpect(jsonPath("$.field68").value(DEFAULT_FIELD_68.toString()))
            .andExpect(jsonPath("$.field69").value(DEFAULT_FIELD_69.toString()))
            .andExpect(jsonPath("$.field70").value(DEFAULT_FIELD_70.toString()))
            .andExpect(jsonPath("$.field71").value(DEFAULT_FIELD_71.toString()))
            .andExpect(jsonPath("$.field72").value(DEFAULT_FIELD_72.toString()))
            .andExpect(jsonPath("$.field73").value(DEFAULT_FIELD_73.toString()))
            .andExpect(jsonPath("$.field74").value(DEFAULT_FIELD_74.toString()))
            .andExpect(jsonPath("$.field75").value(DEFAULT_FIELD_75.toString()))
            .andExpect(jsonPath("$.field76").value(DEFAULT_FIELD_76.toString()))
            .andExpect(jsonPath("$.field77").value(DEFAULT_FIELD_77.toString()))
            .andExpect(jsonPath("$.field78").value(DEFAULT_FIELD_78.toString()))
            .andExpect(jsonPath("$.field79").value(DEFAULT_FIELD_79.toString()))
            .andExpect(jsonPath("$.field80").value(DEFAULT_FIELD_80.toString()))
            .andExpect(jsonPath("$.field81").value(DEFAULT_FIELD_81.toString()))
            .andExpect(jsonPath("$.field82").value(DEFAULT_FIELD_82.toString()))
            .andExpect(jsonPath("$.field83").value(DEFAULT_FIELD_83.toString()))
            .andExpect(jsonPath("$.field84").value(DEFAULT_FIELD_84.toString()))
            .andExpect(jsonPath("$.field85").value(DEFAULT_FIELD_85.toString()))
            .andExpect(jsonPath("$.field86").value(DEFAULT_FIELD_86.toString()))
            .andExpect(jsonPath("$.field87").value(DEFAULT_FIELD_87.toString()))
            .andExpect(jsonPath("$.field88").value(DEFAULT_FIELD_88.toString()))
            .andExpect(jsonPath("$.field89").value(DEFAULT_FIELD_89.toString()))
            .andExpect(jsonPath("$.field90").value(DEFAULT_FIELD_90.toString()))
            .andExpect(jsonPath("$.field91").value(DEFAULT_FIELD_91.toString()))
            .andExpect(jsonPath("$.field92").value(DEFAULT_FIELD_92.toString()))
            .andExpect(jsonPath("$.field93").value(DEFAULT_FIELD_93.toString()))
            .andExpect(jsonPath("$.field94").value(DEFAULT_FIELD_94.toString()))
            .andExpect(jsonPath("$.field95").value(DEFAULT_FIELD_95.toString()))
            .andExpect(jsonPath("$.field96").value(DEFAULT_FIELD_96.toString()))
            .andExpect(jsonPath("$.field97").value(DEFAULT_FIELD_97.toString()))
            .andExpect(jsonPath("$.field98").value(DEFAULT_FIELD_98.toString()))
            .andExpect(jsonPath("$.field99").value(DEFAULT_FIELD_99.toString()))
            .andExpect(jsonPath("$.field100").value(DEFAULT_FIELD_100.toString()))
            .andExpect(jsonPath("$.recordStatus").value(DEFAULT_RECORD_STATUS.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingDataStaging() throws Exception {
        // Get the dataStaging
        restDataStagingMockMvc.perform(get("/api/data-stagings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDataStaging() throws Exception {
        // Initialize the database
        dataStagingRepository.saveAndFlush(dataStaging);
        dataStagingSearchRepository.save(dataStaging);
        int databaseSizeBeforeUpdate = dataStagingRepository.findAll().size();

        // Update the dataStaging
        DataStaging updatedDataStaging = dataStagingRepository.findOne(dataStaging.getId());
        updatedDataStaging
            .tenantId(UPDATED_TENANT_ID)
            .profileId(UPDATED_PROFILE_ID)
            .templateId(UPDATED_TEMPLATE_ID)
            .fileName(UPDATED_FILE_NAME)
            .fileDate(UPDATED_FILE_DATE)
            .lineContent(UPDATED_LINE_CONTENT)
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
            .field16(UPDATED_FIELD_16)
            .field17(UPDATED_FIELD_17)
            .field18(UPDATED_FIELD_18)
            .field19(UPDATED_FIELD_19)
            .field20(UPDATED_FIELD_20)
            .field21(UPDATED_FIELD_21)
            .field22(UPDATED_FIELD_22)
            .field23(UPDATED_FIELD_23)
            .field24(UPDATED_FIELD_24)
            .field25(UPDATED_FIELD_25)
            .field26(UPDATED_FIELD_26)
            .field27(UPDATED_FIELD_27)
            .field28(UPDATED_FIELD_28)
            .field29(UPDATED_FIELD_29)
            .field30(UPDATED_FIELD_30)
            .field31(UPDATED_FIELD_31)
            .field32(UPDATED_FIELD_32)
            .field33(UPDATED_FIELD_33)
            .field34(UPDATED_FIELD_34)
            .field35(UPDATED_FIELD_35)
            .field36(UPDATED_FIELD_36)
            .field37(UPDATED_FIELD_37)
            .field38(UPDATED_FIELD_38)
            .field39(UPDATED_FIELD_39)
            .field40(UPDATED_FIELD_40)
            .field41(UPDATED_FIELD_41)
            .field42(UPDATED_FIELD_42)
            .field43(UPDATED_FIELD_43)
            .field44(UPDATED_FIELD_44)
            .field45(UPDATED_FIELD_45)
            .field46(UPDATED_FIELD_46)
            .field47(UPDATED_FIELD_47)
            .field48(UPDATED_FIELD_48)
            .field49(UPDATED_FIELD_49)
            .field50(UPDATED_FIELD_50)
            .field51(UPDATED_FIELD_51)
            .field52(UPDATED_FIELD_52)
            .field53(UPDATED_FIELD_53)
            .field54(UPDATED_FIELD_54)
            .field55(UPDATED_FIELD_55)
            .field56(UPDATED_FIELD_56)
            .field57(UPDATED_FIELD_57)
            .field58(UPDATED_FIELD_58)
            .field59(UPDATED_FIELD_59)
            .field60(UPDATED_FIELD_60)
            .field61(UPDATED_FIELD_61)
            .field62(UPDATED_FIELD_62)
            .field63(UPDATED_FIELD_63)
            .field64(UPDATED_FIELD_64)
            .field65(UPDATED_FIELD_65)
            .field66(UPDATED_FIELD_66)
            .field67(UPDATED_FIELD_67)
            .field68(UPDATED_FIELD_68)
            .field69(UPDATED_FIELD_69)
            .field70(UPDATED_FIELD_70)
            .field71(UPDATED_FIELD_71)
            .field72(UPDATED_FIELD_72)
            .field73(UPDATED_FIELD_73)
            .field74(UPDATED_FIELD_74)
            .field75(UPDATED_FIELD_75)
            .field76(UPDATED_FIELD_76)
            .field77(UPDATED_FIELD_77)
            .field78(UPDATED_FIELD_78)
            .field79(UPDATED_FIELD_79)
            .field80(UPDATED_FIELD_80)
            .field81(UPDATED_FIELD_81)
            .field82(UPDATED_FIELD_82)
            .field83(UPDATED_FIELD_83)
            .field84(UPDATED_FIELD_84)
            .field85(UPDATED_FIELD_85)
            .field86(UPDATED_FIELD_86)
            .field87(UPDATED_FIELD_87)
            .field88(UPDATED_FIELD_88)
            .field89(UPDATED_FIELD_89)
            .field90(UPDATED_FIELD_90)
            .field91(UPDATED_FIELD_91)
            .field92(UPDATED_FIELD_92)
            .field93(UPDATED_FIELD_93)
            .field94(UPDATED_FIELD_94)
            .field95(UPDATED_FIELD_95)
            .field96(UPDATED_FIELD_96)
            .field97(UPDATED_FIELD_97)
            .field98(UPDATED_FIELD_98)
            .field99(UPDATED_FIELD_99)
            .field100(UPDATED_FIELD_100)
            .recordStatus(UPDATED_RECORD_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE);

        restDataStagingMockMvc.perform(put("/api/data-stagings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDataStaging)))
            .andExpect(status().isOk());

        // Validate the DataStaging in the database
        List<DataStaging> dataStagingList = dataStagingRepository.findAll();
        assertThat(dataStagingList).hasSize(databaseSizeBeforeUpdate);
        DataStaging testDataStaging = dataStagingList.get(dataStagingList.size() - 1);
        assertThat(testDataStaging.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testDataStaging.getProfileId()).isEqualTo(UPDATED_PROFILE_ID);
        assertThat(testDataStaging.getTemplateId()).isEqualTo(UPDATED_TEMPLATE_ID);
        assertThat(testDataStaging.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testDataStaging.getFileDate()).isEqualTo(UPDATED_FILE_DATE);
        assertThat(testDataStaging.getLineContent()).isEqualTo(UPDATED_LINE_CONTENT);
        assertThat(testDataStaging.getField01()).isEqualTo(UPDATED_FIELD_01);
        assertThat(testDataStaging.getField02()).isEqualTo(UPDATED_FIELD_02);
        assertThat(testDataStaging.getField03()).isEqualTo(UPDATED_FIELD_03);
        assertThat(testDataStaging.getField04()).isEqualTo(UPDATED_FIELD_04);
        assertThat(testDataStaging.getField05()).isEqualTo(UPDATED_FIELD_05);
        assertThat(testDataStaging.getField06()).isEqualTo(UPDATED_FIELD_06);
        assertThat(testDataStaging.getField07()).isEqualTo(UPDATED_FIELD_07);
        assertThat(testDataStaging.getField08()).isEqualTo(UPDATED_FIELD_08);
        assertThat(testDataStaging.getField09()).isEqualTo(UPDATED_FIELD_09);
        assertThat(testDataStaging.getField10()).isEqualTo(UPDATED_FIELD_10);
        assertThat(testDataStaging.getField11()).isEqualTo(UPDATED_FIELD_11);
        assertThat(testDataStaging.getField12()).isEqualTo(UPDATED_FIELD_12);
        assertThat(testDataStaging.getField13()).isEqualTo(UPDATED_FIELD_13);
        assertThat(testDataStaging.getField14()).isEqualTo(UPDATED_FIELD_14);
        assertThat(testDataStaging.getField15()).isEqualTo(UPDATED_FIELD_15);
        assertThat(testDataStaging.getField16()).isEqualTo(UPDATED_FIELD_16);
        assertThat(testDataStaging.getField17()).isEqualTo(UPDATED_FIELD_17);
        assertThat(testDataStaging.getField18()).isEqualTo(UPDATED_FIELD_18);
        assertThat(testDataStaging.getField19()).isEqualTo(UPDATED_FIELD_19);
        assertThat(testDataStaging.getField20()).isEqualTo(UPDATED_FIELD_20);
        assertThat(testDataStaging.getField21()).isEqualTo(UPDATED_FIELD_21);
        assertThat(testDataStaging.getField22()).isEqualTo(UPDATED_FIELD_22);
        assertThat(testDataStaging.getField23()).isEqualTo(UPDATED_FIELD_23);
        assertThat(testDataStaging.getField24()).isEqualTo(UPDATED_FIELD_24);
        assertThat(testDataStaging.getField25()).isEqualTo(UPDATED_FIELD_25);
        assertThat(testDataStaging.getField26()).isEqualTo(UPDATED_FIELD_26);
        assertThat(testDataStaging.getField27()).isEqualTo(UPDATED_FIELD_27);
        assertThat(testDataStaging.getField28()).isEqualTo(UPDATED_FIELD_28);
        assertThat(testDataStaging.getField29()).isEqualTo(UPDATED_FIELD_29);
        assertThat(testDataStaging.getField30()).isEqualTo(UPDATED_FIELD_30);
        assertThat(testDataStaging.getField31()).isEqualTo(UPDATED_FIELD_31);
        assertThat(testDataStaging.getField32()).isEqualTo(UPDATED_FIELD_32);
        assertThat(testDataStaging.getField33()).isEqualTo(UPDATED_FIELD_33);
        assertThat(testDataStaging.getField34()).isEqualTo(UPDATED_FIELD_34);
        assertThat(testDataStaging.getField35()).isEqualTo(UPDATED_FIELD_35);
        assertThat(testDataStaging.getField36()).isEqualTo(UPDATED_FIELD_36);
        assertThat(testDataStaging.getField37()).isEqualTo(UPDATED_FIELD_37);
        assertThat(testDataStaging.getField38()).isEqualTo(UPDATED_FIELD_38);
        assertThat(testDataStaging.getField39()).isEqualTo(UPDATED_FIELD_39);
        assertThat(testDataStaging.getField40()).isEqualTo(UPDATED_FIELD_40);
        assertThat(testDataStaging.getField41()).isEqualTo(UPDATED_FIELD_41);
        assertThat(testDataStaging.getField42()).isEqualTo(UPDATED_FIELD_42);
        assertThat(testDataStaging.getField43()).isEqualTo(UPDATED_FIELD_43);
        assertThat(testDataStaging.getField44()).isEqualTo(UPDATED_FIELD_44);
        assertThat(testDataStaging.getField45()).isEqualTo(UPDATED_FIELD_45);
        assertThat(testDataStaging.getField46()).isEqualTo(UPDATED_FIELD_46);
        assertThat(testDataStaging.getField47()).isEqualTo(UPDATED_FIELD_47);
        assertThat(testDataStaging.getField48()).isEqualTo(UPDATED_FIELD_48);
        assertThat(testDataStaging.getField49()).isEqualTo(UPDATED_FIELD_49);
        assertThat(testDataStaging.getField50()).isEqualTo(UPDATED_FIELD_50);
        assertThat(testDataStaging.getField51()).isEqualTo(UPDATED_FIELD_51);
        assertThat(testDataStaging.getField52()).isEqualTo(UPDATED_FIELD_52);
        assertThat(testDataStaging.getField53()).isEqualTo(UPDATED_FIELD_53);
        assertThat(testDataStaging.getField54()).isEqualTo(UPDATED_FIELD_54);
        assertThat(testDataStaging.getField55()).isEqualTo(UPDATED_FIELD_55);
        assertThat(testDataStaging.getField56()).isEqualTo(UPDATED_FIELD_56);
        assertThat(testDataStaging.getField57()).isEqualTo(UPDATED_FIELD_57);
        assertThat(testDataStaging.getField58()).isEqualTo(UPDATED_FIELD_58);
        assertThat(testDataStaging.getField59()).isEqualTo(UPDATED_FIELD_59);
        assertThat(testDataStaging.getField60()).isEqualTo(UPDATED_FIELD_60);
        assertThat(testDataStaging.getField61()).isEqualTo(UPDATED_FIELD_61);
        assertThat(testDataStaging.getField62()).isEqualTo(UPDATED_FIELD_62);
        assertThat(testDataStaging.getField63()).isEqualTo(UPDATED_FIELD_63);
        assertThat(testDataStaging.getField64()).isEqualTo(UPDATED_FIELD_64);
        assertThat(testDataStaging.getField65()).isEqualTo(UPDATED_FIELD_65);
        assertThat(testDataStaging.getField66()).isEqualTo(UPDATED_FIELD_66);
        assertThat(testDataStaging.getField67()).isEqualTo(UPDATED_FIELD_67);
        assertThat(testDataStaging.getField68()).isEqualTo(UPDATED_FIELD_68);
        assertThat(testDataStaging.getField69()).isEqualTo(UPDATED_FIELD_69);
        assertThat(testDataStaging.getField70()).isEqualTo(UPDATED_FIELD_70);
        assertThat(testDataStaging.getField71()).isEqualTo(UPDATED_FIELD_71);
        assertThat(testDataStaging.getField72()).isEqualTo(UPDATED_FIELD_72);
        assertThat(testDataStaging.getField73()).isEqualTo(UPDATED_FIELD_73);
        assertThat(testDataStaging.getField74()).isEqualTo(UPDATED_FIELD_74);
        assertThat(testDataStaging.getField75()).isEqualTo(UPDATED_FIELD_75);
        assertThat(testDataStaging.getField76()).isEqualTo(UPDATED_FIELD_76);
        assertThat(testDataStaging.getField77()).isEqualTo(UPDATED_FIELD_77);
        assertThat(testDataStaging.getField78()).isEqualTo(UPDATED_FIELD_78);
        assertThat(testDataStaging.getField79()).isEqualTo(UPDATED_FIELD_79);
        assertThat(testDataStaging.getField80()).isEqualTo(UPDATED_FIELD_80);
        assertThat(testDataStaging.getField81()).isEqualTo(UPDATED_FIELD_81);
        assertThat(testDataStaging.getField82()).isEqualTo(UPDATED_FIELD_82);
        assertThat(testDataStaging.getField83()).isEqualTo(UPDATED_FIELD_83);
        assertThat(testDataStaging.getField84()).isEqualTo(UPDATED_FIELD_84);
        assertThat(testDataStaging.getField85()).isEqualTo(UPDATED_FIELD_85);
        assertThat(testDataStaging.getField86()).isEqualTo(UPDATED_FIELD_86);
        assertThat(testDataStaging.getField87()).isEqualTo(UPDATED_FIELD_87);
        assertThat(testDataStaging.getField88()).isEqualTo(UPDATED_FIELD_88);
        assertThat(testDataStaging.getField89()).isEqualTo(UPDATED_FIELD_89);
        assertThat(testDataStaging.getField90()).isEqualTo(UPDATED_FIELD_90);
        assertThat(testDataStaging.getField91()).isEqualTo(UPDATED_FIELD_91);
        assertThat(testDataStaging.getField92()).isEqualTo(UPDATED_FIELD_92);
        assertThat(testDataStaging.getField93()).isEqualTo(UPDATED_FIELD_93);
        assertThat(testDataStaging.getField94()).isEqualTo(UPDATED_FIELD_94);
        assertThat(testDataStaging.getField95()).isEqualTo(UPDATED_FIELD_95);
        assertThat(testDataStaging.getField96()).isEqualTo(UPDATED_FIELD_96);
        assertThat(testDataStaging.getField97()).isEqualTo(UPDATED_FIELD_97);
        assertThat(testDataStaging.getField98()).isEqualTo(UPDATED_FIELD_98);
        assertThat(testDataStaging.getField99()).isEqualTo(UPDATED_FIELD_99);
        assertThat(testDataStaging.getField100()).isEqualTo(UPDATED_FIELD_100);
        assertThat(testDataStaging.getRecordStatus()).isEqualTo(UPDATED_RECORD_STATUS);
        assertThat(testDataStaging.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testDataStaging.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testDataStaging.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testDataStaging.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);

        // Validate the DataStaging in Elasticsearch
        DataStaging dataStagingEs = dataStagingSearchRepository.findOne(testDataStaging.getId());
        assertThat(dataStagingEs).isEqualToComparingFieldByField(testDataStaging);
    }

    @Test
    @Transactional
    public void updateNonExistingDataStaging() throws Exception {
        int databaseSizeBeforeUpdate = dataStagingRepository.findAll().size();

        // Create the DataStaging

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDataStagingMockMvc.perform(put("/api/data-stagings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataStaging)))
            .andExpect(status().isCreated());

        // Validate the DataStaging in the database
        List<DataStaging> dataStagingList = dataStagingRepository.findAll();
        assertThat(dataStagingList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDataStaging() throws Exception {
        // Initialize the database
        dataStagingRepository.saveAndFlush(dataStaging);
        dataStagingSearchRepository.save(dataStaging);
        int databaseSizeBeforeDelete = dataStagingRepository.findAll().size();

        // Get the dataStaging
        restDataStagingMockMvc.perform(delete("/api/data-stagings/{id}", dataStaging.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean dataStagingExistsInEs = dataStagingSearchRepository.exists(dataStaging.getId());
        assertThat(dataStagingExistsInEs).isFalse();

        // Validate the database is empty
        List<DataStaging> dataStagingList = dataStagingRepository.findAll();
        assertThat(dataStagingList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDataStaging() throws Exception {
        // Initialize the database
        dataStagingRepository.saveAndFlush(dataStaging);
        dataStagingSearchRepository.save(dataStaging);

        // Search the dataStaging
        restDataStagingMockMvc.perform(get("/api/_search/data-stagings?query=id:" + dataStaging.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataStaging.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].profileId").value(hasItem(DEFAULT_PROFILE_ID.intValue())))
            .andExpect(jsonPath("$.[*].templateId").value(hasItem(DEFAULT_TEMPLATE_ID.intValue())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME.toString())))
            .andExpect(jsonPath("$.[*].fileDate").value(hasItem(sameInstant(DEFAULT_FILE_DATE))))
            .andExpect(jsonPath("$.[*].lineContent").value(hasItem(DEFAULT_LINE_CONTENT.toString())))
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
            .andExpect(jsonPath("$.[*].field16").value(hasItem(DEFAULT_FIELD_16.toString())))
            .andExpect(jsonPath("$.[*].field17").value(hasItem(DEFAULT_FIELD_17.toString())))
            .andExpect(jsonPath("$.[*].field18").value(hasItem(DEFAULT_FIELD_18.toString())))
            .andExpect(jsonPath("$.[*].field19").value(hasItem(DEFAULT_FIELD_19.toString())))
            .andExpect(jsonPath("$.[*].field20").value(hasItem(DEFAULT_FIELD_20.toString())))
            .andExpect(jsonPath("$.[*].field21").value(hasItem(DEFAULT_FIELD_21.toString())))
            .andExpect(jsonPath("$.[*].field22").value(hasItem(DEFAULT_FIELD_22.toString())))
            .andExpect(jsonPath("$.[*].field23").value(hasItem(DEFAULT_FIELD_23.toString())))
            .andExpect(jsonPath("$.[*].field24").value(hasItem(DEFAULT_FIELD_24.toString())))
            .andExpect(jsonPath("$.[*].field25").value(hasItem(DEFAULT_FIELD_25.toString())))
            .andExpect(jsonPath("$.[*].field26").value(hasItem(DEFAULT_FIELD_26.toString())))
            .andExpect(jsonPath("$.[*].field27").value(hasItem(DEFAULT_FIELD_27.toString())))
            .andExpect(jsonPath("$.[*].field28").value(hasItem(DEFAULT_FIELD_28.toString())))
            .andExpect(jsonPath("$.[*].field29").value(hasItem(DEFAULT_FIELD_29.toString())))
            .andExpect(jsonPath("$.[*].field30").value(hasItem(DEFAULT_FIELD_30.toString())))
            .andExpect(jsonPath("$.[*].field31").value(hasItem(DEFAULT_FIELD_31.toString())))
            .andExpect(jsonPath("$.[*].field32").value(hasItem(DEFAULT_FIELD_32.toString())))
            .andExpect(jsonPath("$.[*].field33").value(hasItem(DEFAULT_FIELD_33.toString())))
            .andExpect(jsonPath("$.[*].field34").value(hasItem(DEFAULT_FIELD_34.toString())))
            .andExpect(jsonPath("$.[*].field35").value(hasItem(DEFAULT_FIELD_35.toString())))
            .andExpect(jsonPath("$.[*].field36").value(hasItem(DEFAULT_FIELD_36.toString())))
            .andExpect(jsonPath("$.[*].field37").value(hasItem(DEFAULT_FIELD_37.toString())))
            .andExpect(jsonPath("$.[*].field38").value(hasItem(DEFAULT_FIELD_38.toString())))
            .andExpect(jsonPath("$.[*].field39").value(hasItem(DEFAULT_FIELD_39.toString())))
            .andExpect(jsonPath("$.[*].field40").value(hasItem(DEFAULT_FIELD_40.toString())))
            .andExpect(jsonPath("$.[*].field41").value(hasItem(DEFAULT_FIELD_41.toString())))
            .andExpect(jsonPath("$.[*].field42").value(hasItem(DEFAULT_FIELD_42.toString())))
            .andExpect(jsonPath("$.[*].field43").value(hasItem(DEFAULT_FIELD_43.toString())))
            .andExpect(jsonPath("$.[*].field44").value(hasItem(DEFAULT_FIELD_44.toString())))
            .andExpect(jsonPath("$.[*].field45").value(hasItem(DEFAULT_FIELD_45.toString())))
            .andExpect(jsonPath("$.[*].field46").value(hasItem(DEFAULT_FIELD_46.toString())))
            .andExpect(jsonPath("$.[*].field47").value(hasItem(DEFAULT_FIELD_47.toString())))
            .andExpect(jsonPath("$.[*].field48").value(hasItem(DEFAULT_FIELD_48.toString())))
            .andExpect(jsonPath("$.[*].field49").value(hasItem(DEFAULT_FIELD_49.toString())))
            .andExpect(jsonPath("$.[*].field50").value(hasItem(DEFAULT_FIELD_50.toString())))
            .andExpect(jsonPath("$.[*].field51").value(hasItem(DEFAULT_FIELD_51.toString())))
            .andExpect(jsonPath("$.[*].field52").value(hasItem(DEFAULT_FIELD_52.toString())))
            .andExpect(jsonPath("$.[*].field53").value(hasItem(DEFAULT_FIELD_53.toString())))
            .andExpect(jsonPath("$.[*].field54").value(hasItem(DEFAULT_FIELD_54.toString())))
            .andExpect(jsonPath("$.[*].field55").value(hasItem(DEFAULT_FIELD_55.toString())))
            .andExpect(jsonPath("$.[*].field56").value(hasItem(DEFAULT_FIELD_56.toString())))
            .andExpect(jsonPath("$.[*].field57").value(hasItem(DEFAULT_FIELD_57.toString())))
            .andExpect(jsonPath("$.[*].field58").value(hasItem(DEFAULT_FIELD_58.toString())))
            .andExpect(jsonPath("$.[*].field59").value(hasItem(DEFAULT_FIELD_59.toString())))
            .andExpect(jsonPath("$.[*].field60").value(hasItem(DEFAULT_FIELD_60.toString())))
            .andExpect(jsonPath("$.[*].field61").value(hasItem(DEFAULT_FIELD_61.toString())))
            .andExpect(jsonPath("$.[*].field62").value(hasItem(DEFAULT_FIELD_62.toString())))
            .andExpect(jsonPath("$.[*].field63").value(hasItem(DEFAULT_FIELD_63.toString())))
            .andExpect(jsonPath("$.[*].field64").value(hasItem(DEFAULT_FIELD_64.toString())))
            .andExpect(jsonPath("$.[*].field65").value(hasItem(DEFAULT_FIELD_65.toString())))
            .andExpect(jsonPath("$.[*].field66").value(hasItem(DEFAULT_FIELD_66.toString())))
            .andExpect(jsonPath("$.[*].field67").value(hasItem(DEFAULT_FIELD_67.toString())))
            .andExpect(jsonPath("$.[*].field68").value(hasItem(DEFAULT_FIELD_68.toString())))
            .andExpect(jsonPath("$.[*].field69").value(hasItem(DEFAULT_FIELD_69.toString())))
            .andExpect(jsonPath("$.[*].field70").value(hasItem(DEFAULT_FIELD_70.toString())))
            .andExpect(jsonPath("$.[*].field71").value(hasItem(DEFAULT_FIELD_71.toString())))
            .andExpect(jsonPath("$.[*].field72").value(hasItem(DEFAULT_FIELD_72.toString())))
            .andExpect(jsonPath("$.[*].field73").value(hasItem(DEFAULT_FIELD_73.toString())))
            .andExpect(jsonPath("$.[*].field74").value(hasItem(DEFAULT_FIELD_74.toString())))
            .andExpect(jsonPath("$.[*].field75").value(hasItem(DEFAULT_FIELD_75.toString())))
            .andExpect(jsonPath("$.[*].field76").value(hasItem(DEFAULT_FIELD_76.toString())))
            .andExpect(jsonPath("$.[*].field77").value(hasItem(DEFAULT_FIELD_77.toString())))
            .andExpect(jsonPath("$.[*].field78").value(hasItem(DEFAULT_FIELD_78.toString())))
            .andExpect(jsonPath("$.[*].field79").value(hasItem(DEFAULT_FIELD_79.toString())))
            .andExpect(jsonPath("$.[*].field80").value(hasItem(DEFAULT_FIELD_80.toString())))
            .andExpect(jsonPath("$.[*].field81").value(hasItem(DEFAULT_FIELD_81.toString())))
            .andExpect(jsonPath("$.[*].field82").value(hasItem(DEFAULT_FIELD_82.toString())))
            .andExpect(jsonPath("$.[*].field83").value(hasItem(DEFAULT_FIELD_83.toString())))
            .andExpect(jsonPath("$.[*].field84").value(hasItem(DEFAULT_FIELD_84.toString())))
            .andExpect(jsonPath("$.[*].field85").value(hasItem(DEFAULT_FIELD_85.toString())))
            .andExpect(jsonPath("$.[*].field86").value(hasItem(DEFAULT_FIELD_86.toString())))
            .andExpect(jsonPath("$.[*].field87").value(hasItem(DEFAULT_FIELD_87.toString())))
            .andExpect(jsonPath("$.[*].field88").value(hasItem(DEFAULT_FIELD_88.toString())))
            .andExpect(jsonPath("$.[*].field89").value(hasItem(DEFAULT_FIELD_89.toString())))
            .andExpect(jsonPath("$.[*].field90").value(hasItem(DEFAULT_FIELD_90.toString())))
            .andExpect(jsonPath("$.[*].field91").value(hasItem(DEFAULT_FIELD_91.toString())))
            .andExpect(jsonPath("$.[*].field92").value(hasItem(DEFAULT_FIELD_92.toString())))
            .andExpect(jsonPath("$.[*].field93").value(hasItem(DEFAULT_FIELD_93.toString())))
            .andExpect(jsonPath("$.[*].field94").value(hasItem(DEFAULT_FIELD_94.toString())))
            .andExpect(jsonPath("$.[*].field95").value(hasItem(DEFAULT_FIELD_95.toString())))
            .andExpect(jsonPath("$.[*].field96").value(hasItem(DEFAULT_FIELD_96.toString())))
            .andExpect(jsonPath("$.[*].field97").value(hasItem(DEFAULT_FIELD_97.toString())))
            .andExpect(jsonPath("$.[*].field98").value(hasItem(DEFAULT_FIELD_98.toString())))
            .andExpect(jsonPath("$.[*].field99").value(hasItem(DEFAULT_FIELD_99.toString())))
            .andExpect(jsonPath("$.[*].field100").value(hasItem(DEFAULT_FIELD_100.toString())))
            .andExpect(jsonPath("$.[*].recordStatus").value(hasItem(DEFAULT_RECORD_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DataStaging.class);
        DataStaging dataStaging1 = new DataStaging();
        dataStaging1.setId(1L);
        DataStaging dataStaging2 = new DataStaging();
        dataStaging2.setId(dataStaging1.getId());
        assertThat(dataStaging1).isEqualTo(dataStaging2);
        dataStaging2.setId(2L);
        assertThat(dataStaging1).isNotEqualTo(dataStaging2);
        dataStaging1.setId(null);
        assertThat(dataStaging1).isNotEqualTo(dataStaging2);
    }
}
