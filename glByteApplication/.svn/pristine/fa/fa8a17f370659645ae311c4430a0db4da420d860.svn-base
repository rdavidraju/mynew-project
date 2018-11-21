package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.DataMaster;
import com.nspl.app.repository.DataMasterRepository;
import com.nspl.app.repository.search.DataMasterSearchRepository;
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
 * Test class for the DataMasterResource REST controller.
 *
 * @see DataMasterResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class DataMasterResourceIntTest {

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

    private static final String DEFAULT_RECON_FLAG = "AAAAAAAAAA";
    private static final String UPDATED_RECON_FLAG = "BBBBBBBBBB";

    private static final String DEFAULT_RECONCILED_DATE = "AAAAAAAAAA";
    private static final String UPDATED_RECONCILED_DATE = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNTED_FLAG = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNTED_FLAG = "BBBBBBBBBB";

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private DataMasterRepository dataMasterRepository;

    @Autowired
    private DataMasterSearchRepository dataMasterSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDataMasterMockMvc;

    private DataMaster dataMaster;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DataMasterResource dataMasterResource = new DataMasterResource(dataMasterRepository, dataMasterSearchRepository);
        this.restDataMasterMockMvc = MockMvcBuilders.standaloneSetup(dataMasterResource)
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
    public static DataMaster createEntity(EntityManager em) {
        DataMaster dataMaster = new DataMaster()
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
            .reconFlag(DEFAULT_RECON_FLAG)
            .reconciledDate(DEFAULT_RECONCILED_DATE)
            .accountedFlag(DEFAULT_ACCOUNTED_FLAG)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE);
        return dataMaster;
    }

    @Before
    public void initTest() {
        dataMasterSearchRepository.deleteAll();
        dataMaster = createEntity(em);
    }

    @Test
    @Transactional
    public void createDataMaster() throws Exception {
        int databaseSizeBeforeCreate = dataMasterRepository.findAll().size();

        // Create the DataMaster
        restDataMasterMockMvc.perform(post("/api/data-masters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataMaster)))
            .andExpect(status().isCreated());

        // Validate the DataMaster in the database
        List<DataMaster> dataMasterList = dataMasterRepository.findAll();
        assertThat(dataMasterList).hasSize(databaseSizeBeforeCreate + 1);
        DataMaster testDataMaster = dataMasterList.get(dataMasterList.size() - 1);
        assertThat(testDataMaster.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testDataMaster.getProfileId()).isEqualTo(DEFAULT_PROFILE_ID);
        assertThat(testDataMaster.getTemplateId()).isEqualTo(DEFAULT_TEMPLATE_ID);
        assertThat(testDataMaster.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testDataMaster.getFileDate()).isEqualTo(DEFAULT_FILE_DATE);
        assertThat(testDataMaster.getLineContent()).isEqualTo(DEFAULT_LINE_CONTENT);
        assertThat(testDataMaster.getField01()).isEqualTo(DEFAULT_FIELD_01);
        assertThat(testDataMaster.getField02()).isEqualTo(DEFAULT_FIELD_02);
        assertThat(testDataMaster.getField03()).isEqualTo(DEFAULT_FIELD_03);
        assertThat(testDataMaster.getField04()).isEqualTo(DEFAULT_FIELD_04);
        assertThat(testDataMaster.getField05()).isEqualTo(DEFAULT_FIELD_05);
        assertThat(testDataMaster.getField06()).isEqualTo(DEFAULT_FIELD_06);
        assertThat(testDataMaster.getField07()).isEqualTo(DEFAULT_FIELD_07);
        assertThat(testDataMaster.getField08()).isEqualTo(DEFAULT_FIELD_08);
        assertThat(testDataMaster.getField09()).isEqualTo(DEFAULT_FIELD_09);
        assertThat(testDataMaster.getField10()).isEqualTo(DEFAULT_FIELD_10);
        assertThat(testDataMaster.getField11()).isEqualTo(DEFAULT_FIELD_11);
        assertThat(testDataMaster.getField12()).isEqualTo(DEFAULT_FIELD_12);
        assertThat(testDataMaster.getField13()).isEqualTo(DEFAULT_FIELD_13);
        assertThat(testDataMaster.getField14()).isEqualTo(DEFAULT_FIELD_14);
        assertThat(testDataMaster.getField15()).isEqualTo(DEFAULT_FIELD_15);
        assertThat(testDataMaster.getField16()).isEqualTo(DEFAULT_FIELD_16);
        assertThat(testDataMaster.getField17()).isEqualTo(DEFAULT_FIELD_17);
        assertThat(testDataMaster.getField18()).isEqualTo(DEFAULT_FIELD_18);
        assertThat(testDataMaster.getField19()).isEqualTo(DEFAULT_FIELD_19);
        assertThat(testDataMaster.getField20()).isEqualTo(DEFAULT_FIELD_20);
        assertThat(testDataMaster.getField21()).isEqualTo(DEFAULT_FIELD_21);
        assertThat(testDataMaster.getField22()).isEqualTo(DEFAULT_FIELD_22);
        assertThat(testDataMaster.getField23()).isEqualTo(DEFAULT_FIELD_23);
        assertThat(testDataMaster.getField24()).isEqualTo(DEFAULT_FIELD_24);
        assertThat(testDataMaster.getField25()).isEqualTo(DEFAULT_FIELD_25);
        assertThat(testDataMaster.getField26()).isEqualTo(DEFAULT_FIELD_26);
        assertThat(testDataMaster.getField27()).isEqualTo(DEFAULT_FIELD_27);
        assertThat(testDataMaster.getField28()).isEqualTo(DEFAULT_FIELD_28);
        assertThat(testDataMaster.getField29()).isEqualTo(DEFAULT_FIELD_29);
        assertThat(testDataMaster.getField30()).isEqualTo(DEFAULT_FIELD_30);
        assertThat(testDataMaster.getField31()).isEqualTo(DEFAULT_FIELD_31);
        assertThat(testDataMaster.getField32()).isEqualTo(DEFAULT_FIELD_32);
        assertThat(testDataMaster.getField33()).isEqualTo(DEFAULT_FIELD_33);
        assertThat(testDataMaster.getField34()).isEqualTo(DEFAULT_FIELD_34);
        assertThat(testDataMaster.getField35()).isEqualTo(DEFAULT_FIELD_35);
        assertThat(testDataMaster.getField36()).isEqualTo(DEFAULT_FIELD_36);
        assertThat(testDataMaster.getField37()).isEqualTo(DEFAULT_FIELD_37);
        assertThat(testDataMaster.getField38()).isEqualTo(DEFAULT_FIELD_38);
        assertThat(testDataMaster.getField39()).isEqualTo(DEFAULT_FIELD_39);
        assertThat(testDataMaster.getField40()).isEqualTo(DEFAULT_FIELD_40);
        assertThat(testDataMaster.getField41()).isEqualTo(DEFAULT_FIELD_41);
        assertThat(testDataMaster.getField42()).isEqualTo(DEFAULT_FIELD_42);
        assertThat(testDataMaster.getField43()).isEqualTo(DEFAULT_FIELD_43);
        assertThat(testDataMaster.getField44()).isEqualTo(DEFAULT_FIELD_44);
        assertThat(testDataMaster.getField45()).isEqualTo(DEFAULT_FIELD_45);
        assertThat(testDataMaster.getField46()).isEqualTo(DEFAULT_FIELD_46);
        assertThat(testDataMaster.getField47()).isEqualTo(DEFAULT_FIELD_47);
        assertThat(testDataMaster.getField48()).isEqualTo(DEFAULT_FIELD_48);
        assertThat(testDataMaster.getField49()).isEqualTo(DEFAULT_FIELD_49);
        assertThat(testDataMaster.getField50()).isEqualTo(DEFAULT_FIELD_50);
        assertThat(testDataMaster.getField51()).isEqualTo(DEFAULT_FIELD_51);
        assertThat(testDataMaster.getField52()).isEqualTo(DEFAULT_FIELD_52);
        assertThat(testDataMaster.getField53()).isEqualTo(DEFAULT_FIELD_53);
        assertThat(testDataMaster.getField54()).isEqualTo(DEFAULT_FIELD_54);
        assertThat(testDataMaster.getField55()).isEqualTo(DEFAULT_FIELD_55);
        assertThat(testDataMaster.getField56()).isEqualTo(DEFAULT_FIELD_56);
        assertThat(testDataMaster.getField57()).isEqualTo(DEFAULT_FIELD_57);
        assertThat(testDataMaster.getField58()).isEqualTo(DEFAULT_FIELD_58);
        assertThat(testDataMaster.getField59()).isEqualTo(DEFAULT_FIELD_59);
        assertThat(testDataMaster.getField60()).isEqualTo(DEFAULT_FIELD_60);
        assertThat(testDataMaster.getField61()).isEqualTo(DEFAULT_FIELD_61);
        assertThat(testDataMaster.getField62()).isEqualTo(DEFAULT_FIELD_62);
        assertThat(testDataMaster.getField63()).isEqualTo(DEFAULT_FIELD_63);
        assertThat(testDataMaster.getField64()).isEqualTo(DEFAULT_FIELD_64);
        assertThat(testDataMaster.getField65()).isEqualTo(DEFAULT_FIELD_65);
        assertThat(testDataMaster.getField66()).isEqualTo(DEFAULT_FIELD_66);
        assertThat(testDataMaster.getField67()).isEqualTo(DEFAULT_FIELD_67);
        assertThat(testDataMaster.getField68()).isEqualTo(DEFAULT_FIELD_68);
        assertThat(testDataMaster.getField69()).isEqualTo(DEFAULT_FIELD_69);
        assertThat(testDataMaster.getField70()).isEqualTo(DEFAULT_FIELD_70);
        assertThat(testDataMaster.getField71()).isEqualTo(DEFAULT_FIELD_71);
        assertThat(testDataMaster.getField72()).isEqualTo(DEFAULT_FIELD_72);
        assertThat(testDataMaster.getField73()).isEqualTo(DEFAULT_FIELD_73);
        assertThat(testDataMaster.getField74()).isEqualTo(DEFAULT_FIELD_74);
        assertThat(testDataMaster.getField75()).isEqualTo(DEFAULT_FIELD_75);
        assertThat(testDataMaster.getField76()).isEqualTo(DEFAULT_FIELD_76);
        assertThat(testDataMaster.getField77()).isEqualTo(DEFAULT_FIELD_77);
        assertThat(testDataMaster.getField78()).isEqualTo(DEFAULT_FIELD_78);
        assertThat(testDataMaster.getField79()).isEqualTo(DEFAULT_FIELD_79);
        assertThat(testDataMaster.getField80()).isEqualTo(DEFAULT_FIELD_80);
        assertThat(testDataMaster.getField81()).isEqualTo(DEFAULT_FIELD_81);
        assertThat(testDataMaster.getField82()).isEqualTo(DEFAULT_FIELD_82);
        assertThat(testDataMaster.getField83()).isEqualTo(DEFAULT_FIELD_83);
        assertThat(testDataMaster.getField84()).isEqualTo(DEFAULT_FIELD_84);
        assertThat(testDataMaster.getField85()).isEqualTo(DEFAULT_FIELD_85);
        assertThat(testDataMaster.getField86()).isEqualTo(DEFAULT_FIELD_86);
        assertThat(testDataMaster.getField87()).isEqualTo(DEFAULT_FIELD_87);
        assertThat(testDataMaster.getField88()).isEqualTo(DEFAULT_FIELD_88);
        assertThat(testDataMaster.getField89()).isEqualTo(DEFAULT_FIELD_89);
        assertThat(testDataMaster.getField90()).isEqualTo(DEFAULT_FIELD_90);
        assertThat(testDataMaster.getField91()).isEqualTo(DEFAULT_FIELD_91);
        assertThat(testDataMaster.getField92()).isEqualTo(DEFAULT_FIELD_92);
        assertThat(testDataMaster.getField93()).isEqualTo(DEFAULT_FIELD_93);
        assertThat(testDataMaster.getField94()).isEqualTo(DEFAULT_FIELD_94);
        assertThat(testDataMaster.getField95()).isEqualTo(DEFAULT_FIELD_95);
        assertThat(testDataMaster.getField96()).isEqualTo(DEFAULT_FIELD_96);
        assertThat(testDataMaster.getField97()).isEqualTo(DEFAULT_FIELD_97);
        assertThat(testDataMaster.getField98()).isEqualTo(DEFAULT_FIELD_98);
        assertThat(testDataMaster.getField99()).isEqualTo(DEFAULT_FIELD_99);
        assertThat(testDataMaster.getField100()).isEqualTo(DEFAULT_FIELD_100);
        assertThat(testDataMaster.getReconFlag()).isEqualTo(DEFAULT_RECON_FLAG);
        assertThat(testDataMaster.getReconciledDate()).isEqualTo(DEFAULT_RECONCILED_DATE);
        assertThat(testDataMaster.getAccountedFlag()).isEqualTo(DEFAULT_ACCOUNTED_FLAG);
        assertThat(testDataMaster.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testDataMaster.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testDataMaster.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testDataMaster.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);

        // Validate the DataMaster in Elasticsearch
        DataMaster dataMasterEs = dataMasterSearchRepository.findOne(testDataMaster.getId());
        assertThat(dataMasterEs).isEqualToComparingFieldByField(testDataMaster);
    }

    @Test
    @Transactional
    public void createDataMasterWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dataMasterRepository.findAll().size();

        // Create the DataMaster with an existing ID
        dataMaster.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDataMasterMockMvc.perform(post("/api/data-masters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataMaster)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<DataMaster> dataMasterList = dataMasterRepository.findAll();
        assertThat(dataMasterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDataMasters() throws Exception {
        // Initialize the database
        dataMasterRepository.saveAndFlush(dataMaster);

        // Get all the dataMasterList
        restDataMasterMockMvc.perform(get("/api/data-masters?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataMaster.getId().intValue())))
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
            .andExpect(jsonPath("$.[*].reconFlag").value(hasItem(DEFAULT_RECON_FLAG.toString())))
            .andExpect(jsonPath("$.[*].reconciledDate").value(hasItem(DEFAULT_RECONCILED_DATE.toString())))
            .andExpect(jsonPath("$.[*].accountedFlag").value(hasItem(DEFAULT_ACCOUNTED_FLAG.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void getDataMaster() throws Exception {
        // Initialize the database
        dataMasterRepository.saveAndFlush(dataMaster);

        // Get the dataMaster
        restDataMasterMockMvc.perform(get("/api/data-masters/{id}", dataMaster.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dataMaster.getId().intValue()))
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
            .andExpect(jsonPath("$.reconFlag").value(DEFAULT_RECON_FLAG.toString()))
            .andExpect(jsonPath("$.reconciledDate").value(DEFAULT_RECONCILED_DATE.toString()))
            .andExpect(jsonPath("$.accountedFlag").value(DEFAULT_ACCOUNTED_FLAG.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingDataMaster() throws Exception {
        // Get the dataMaster
        restDataMasterMockMvc.perform(get("/api/data-masters/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDataMaster() throws Exception {
        // Initialize the database
        dataMasterRepository.saveAndFlush(dataMaster);
        dataMasterSearchRepository.save(dataMaster);
        int databaseSizeBeforeUpdate = dataMasterRepository.findAll().size();

        // Update the dataMaster
        DataMaster updatedDataMaster = dataMasterRepository.findOne(dataMaster.getId());
        updatedDataMaster
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
            .reconFlag(UPDATED_RECON_FLAG)
            .reconciledDate(UPDATED_RECONCILED_DATE)
            .accountedFlag(UPDATED_ACCOUNTED_FLAG)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE);

        restDataMasterMockMvc.perform(put("/api/data-masters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDataMaster)))
            .andExpect(status().isOk());

        // Validate the DataMaster in the database
        List<DataMaster> dataMasterList = dataMasterRepository.findAll();
        assertThat(dataMasterList).hasSize(databaseSizeBeforeUpdate);
        DataMaster testDataMaster = dataMasterList.get(dataMasterList.size() - 1);
        assertThat(testDataMaster.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testDataMaster.getProfileId()).isEqualTo(UPDATED_PROFILE_ID);
        assertThat(testDataMaster.getTemplateId()).isEqualTo(UPDATED_TEMPLATE_ID);
        assertThat(testDataMaster.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testDataMaster.getFileDate()).isEqualTo(UPDATED_FILE_DATE);
        assertThat(testDataMaster.getLineContent()).isEqualTo(UPDATED_LINE_CONTENT);
        assertThat(testDataMaster.getField01()).isEqualTo(UPDATED_FIELD_01);
        assertThat(testDataMaster.getField02()).isEqualTo(UPDATED_FIELD_02);
        assertThat(testDataMaster.getField03()).isEqualTo(UPDATED_FIELD_03);
        assertThat(testDataMaster.getField04()).isEqualTo(UPDATED_FIELD_04);
        assertThat(testDataMaster.getField05()).isEqualTo(UPDATED_FIELD_05);
        assertThat(testDataMaster.getField06()).isEqualTo(UPDATED_FIELD_06);
        assertThat(testDataMaster.getField07()).isEqualTo(UPDATED_FIELD_07);
        assertThat(testDataMaster.getField08()).isEqualTo(UPDATED_FIELD_08);
        assertThat(testDataMaster.getField09()).isEqualTo(UPDATED_FIELD_09);
        assertThat(testDataMaster.getField10()).isEqualTo(UPDATED_FIELD_10);
        assertThat(testDataMaster.getField11()).isEqualTo(UPDATED_FIELD_11);
        assertThat(testDataMaster.getField12()).isEqualTo(UPDATED_FIELD_12);
        assertThat(testDataMaster.getField13()).isEqualTo(UPDATED_FIELD_13);
        assertThat(testDataMaster.getField14()).isEqualTo(UPDATED_FIELD_14);
        assertThat(testDataMaster.getField15()).isEqualTo(UPDATED_FIELD_15);
        assertThat(testDataMaster.getField16()).isEqualTo(UPDATED_FIELD_16);
        assertThat(testDataMaster.getField17()).isEqualTo(UPDATED_FIELD_17);
        assertThat(testDataMaster.getField18()).isEqualTo(UPDATED_FIELD_18);
        assertThat(testDataMaster.getField19()).isEqualTo(UPDATED_FIELD_19);
        assertThat(testDataMaster.getField20()).isEqualTo(UPDATED_FIELD_20);
        assertThat(testDataMaster.getField21()).isEqualTo(UPDATED_FIELD_21);
        assertThat(testDataMaster.getField22()).isEqualTo(UPDATED_FIELD_22);
        assertThat(testDataMaster.getField23()).isEqualTo(UPDATED_FIELD_23);
        assertThat(testDataMaster.getField24()).isEqualTo(UPDATED_FIELD_24);
        assertThat(testDataMaster.getField25()).isEqualTo(UPDATED_FIELD_25);
        assertThat(testDataMaster.getField26()).isEqualTo(UPDATED_FIELD_26);
        assertThat(testDataMaster.getField27()).isEqualTo(UPDATED_FIELD_27);
        assertThat(testDataMaster.getField28()).isEqualTo(UPDATED_FIELD_28);
        assertThat(testDataMaster.getField29()).isEqualTo(UPDATED_FIELD_29);
        assertThat(testDataMaster.getField30()).isEqualTo(UPDATED_FIELD_30);
        assertThat(testDataMaster.getField31()).isEqualTo(UPDATED_FIELD_31);
        assertThat(testDataMaster.getField32()).isEqualTo(UPDATED_FIELD_32);
        assertThat(testDataMaster.getField33()).isEqualTo(UPDATED_FIELD_33);
        assertThat(testDataMaster.getField34()).isEqualTo(UPDATED_FIELD_34);
        assertThat(testDataMaster.getField35()).isEqualTo(UPDATED_FIELD_35);
        assertThat(testDataMaster.getField36()).isEqualTo(UPDATED_FIELD_36);
        assertThat(testDataMaster.getField37()).isEqualTo(UPDATED_FIELD_37);
        assertThat(testDataMaster.getField38()).isEqualTo(UPDATED_FIELD_38);
        assertThat(testDataMaster.getField39()).isEqualTo(UPDATED_FIELD_39);
        assertThat(testDataMaster.getField40()).isEqualTo(UPDATED_FIELD_40);
        assertThat(testDataMaster.getField41()).isEqualTo(UPDATED_FIELD_41);
        assertThat(testDataMaster.getField42()).isEqualTo(UPDATED_FIELD_42);
        assertThat(testDataMaster.getField43()).isEqualTo(UPDATED_FIELD_43);
        assertThat(testDataMaster.getField44()).isEqualTo(UPDATED_FIELD_44);
        assertThat(testDataMaster.getField45()).isEqualTo(UPDATED_FIELD_45);
        assertThat(testDataMaster.getField46()).isEqualTo(UPDATED_FIELD_46);
        assertThat(testDataMaster.getField47()).isEqualTo(UPDATED_FIELD_47);
        assertThat(testDataMaster.getField48()).isEqualTo(UPDATED_FIELD_48);
        assertThat(testDataMaster.getField49()).isEqualTo(UPDATED_FIELD_49);
        assertThat(testDataMaster.getField50()).isEqualTo(UPDATED_FIELD_50);
        assertThat(testDataMaster.getField51()).isEqualTo(UPDATED_FIELD_51);
        assertThat(testDataMaster.getField52()).isEqualTo(UPDATED_FIELD_52);
        assertThat(testDataMaster.getField53()).isEqualTo(UPDATED_FIELD_53);
        assertThat(testDataMaster.getField54()).isEqualTo(UPDATED_FIELD_54);
        assertThat(testDataMaster.getField55()).isEqualTo(UPDATED_FIELD_55);
        assertThat(testDataMaster.getField56()).isEqualTo(UPDATED_FIELD_56);
        assertThat(testDataMaster.getField57()).isEqualTo(UPDATED_FIELD_57);
        assertThat(testDataMaster.getField58()).isEqualTo(UPDATED_FIELD_58);
        assertThat(testDataMaster.getField59()).isEqualTo(UPDATED_FIELD_59);
        assertThat(testDataMaster.getField60()).isEqualTo(UPDATED_FIELD_60);
        assertThat(testDataMaster.getField61()).isEqualTo(UPDATED_FIELD_61);
        assertThat(testDataMaster.getField62()).isEqualTo(UPDATED_FIELD_62);
        assertThat(testDataMaster.getField63()).isEqualTo(UPDATED_FIELD_63);
        assertThat(testDataMaster.getField64()).isEqualTo(UPDATED_FIELD_64);
        assertThat(testDataMaster.getField65()).isEqualTo(UPDATED_FIELD_65);
        assertThat(testDataMaster.getField66()).isEqualTo(UPDATED_FIELD_66);
        assertThat(testDataMaster.getField67()).isEqualTo(UPDATED_FIELD_67);
        assertThat(testDataMaster.getField68()).isEqualTo(UPDATED_FIELD_68);
        assertThat(testDataMaster.getField69()).isEqualTo(UPDATED_FIELD_69);
        assertThat(testDataMaster.getField70()).isEqualTo(UPDATED_FIELD_70);
        assertThat(testDataMaster.getField71()).isEqualTo(UPDATED_FIELD_71);
        assertThat(testDataMaster.getField72()).isEqualTo(UPDATED_FIELD_72);
        assertThat(testDataMaster.getField73()).isEqualTo(UPDATED_FIELD_73);
        assertThat(testDataMaster.getField74()).isEqualTo(UPDATED_FIELD_74);
        assertThat(testDataMaster.getField75()).isEqualTo(UPDATED_FIELD_75);
        assertThat(testDataMaster.getField76()).isEqualTo(UPDATED_FIELD_76);
        assertThat(testDataMaster.getField77()).isEqualTo(UPDATED_FIELD_77);
        assertThat(testDataMaster.getField78()).isEqualTo(UPDATED_FIELD_78);
        assertThat(testDataMaster.getField79()).isEqualTo(UPDATED_FIELD_79);
        assertThat(testDataMaster.getField80()).isEqualTo(UPDATED_FIELD_80);
        assertThat(testDataMaster.getField81()).isEqualTo(UPDATED_FIELD_81);
        assertThat(testDataMaster.getField82()).isEqualTo(UPDATED_FIELD_82);
        assertThat(testDataMaster.getField83()).isEqualTo(UPDATED_FIELD_83);
        assertThat(testDataMaster.getField84()).isEqualTo(UPDATED_FIELD_84);
        assertThat(testDataMaster.getField85()).isEqualTo(UPDATED_FIELD_85);
        assertThat(testDataMaster.getField86()).isEqualTo(UPDATED_FIELD_86);
        assertThat(testDataMaster.getField87()).isEqualTo(UPDATED_FIELD_87);
        assertThat(testDataMaster.getField88()).isEqualTo(UPDATED_FIELD_88);
        assertThat(testDataMaster.getField89()).isEqualTo(UPDATED_FIELD_89);
        assertThat(testDataMaster.getField90()).isEqualTo(UPDATED_FIELD_90);
        assertThat(testDataMaster.getField91()).isEqualTo(UPDATED_FIELD_91);
        assertThat(testDataMaster.getField92()).isEqualTo(UPDATED_FIELD_92);
        assertThat(testDataMaster.getField93()).isEqualTo(UPDATED_FIELD_93);
        assertThat(testDataMaster.getField94()).isEqualTo(UPDATED_FIELD_94);
        assertThat(testDataMaster.getField95()).isEqualTo(UPDATED_FIELD_95);
        assertThat(testDataMaster.getField96()).isEqualTo(UPDATED_FIELD_96);
        assertThat(testDataMaster.getField97()).isEqualTo(UPDATED_FIELD_97);
        assertThat(testDataMaster.getField98()).isEqualTo(UPDATED_FIELD_98);
        assertThat(testDataMaster.getField99()).isEqualTo(UPDATED_FIELD_99);
        assertThat(testDataMaster.getField100()).isEqualTo(UPDATED_FIELD_100);
        assertThat(testDataMaster.getReconFlag()).isEqualTo(UPDATED_RECON_FLAG);
        assertThat(testDataMaster.getReconciledDate()).isEqualTo(UPDATED_RECONCILED_DATE);
        assertThat(testDataMaster.getAccountedFlag()).isEqualTo(UPDATED_ACCOUNTED_FLAG);
        assertThat(testDataMaster.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testDataMaster.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testDataMaster.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testDataMaster.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);

        // Validate the DataMaster in Elasticsearch
        DataMaster dataMasterEs = dataMasterSearchRepository.findOne(testDataMaster.getId());
        assertThat(dataMasterEs).isEqualToComparingFieldByField(testDataMaster);
    }

    @Test
    @Transactional
    public void updateNonExistingDataMaster() throws Exception {
        int databaseSizeBeforeUpdate = dataMasterRepository.findAll().size();

        // Create the DataMaster

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDataMasterMockMvc.perform(put("/api/data-masters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataMaster)))
            .andExpect(status().isCreated());

        // Validate the DataMaster in the database
        List<DataMaster> dataMasterList = dataMasterRepository.findAll();
        assertThat(dataMasterList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDataMaster() throws Exception {
        // Initialize the database
        dataMasterRepository.saveAndFlush(dataMaster);
        dataMasterSearchRepository.save(dataMaster);
        int databaseSizeBeforeDelete = dataMasterRepository.findAll().size();

        // Get the dataMaster
        restDataMasterMockMvc.perform(delete("/api/data-masters/{id}", dataMaster.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean dataMasterExistsInEs = dataMasterSearchRepository.exists(dataMaster.getId());
        assertThat(dataMasterExistsInEs).isFalse();

        // Validate the database is empty
        List<DataMaster> dataMasterList = dataMasterRepository.findAll();
        assertThat(dataMasterList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDataMaster() throws Exception {
        // Initialize the database
        dataMasterRepository.saveAndFlush(dataMaster);
        dataMasterSearchRepository.save(dataMaster);

        // Search the dataMaster
        restDataMasterMockMvc.perform(get("/api/_search/data-masters?query=id:" + dataMaster.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataMaster.getId().intValue())))
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
            .andExpect(jsonPath("$.[*].reconFlag").value(hasItem(DEFAULT_RECON_FLAG.toString())))
            .andExpect(jsonPath("$.[*].reconciledDate").value(hasItem(DEFAULT_RECONCILED_DATE.toString())))
            .andExpect(jsonPath("$.[*].accountedFlag").value(hasItem(DEFAULT_ACCOUNTED_FLAG.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DataMaster.class);
        DataMaster dataMaster1 = new DataMaster();
        dataMaster1.setId(1L);
        DataMaster dataMaster2 = new DataMaster();
        dataMaster2.setId(dataMaster1.getId());
        assertThat(dataMaster1).isEqualTo(dataMaster2);
        dataMaster2.setId(2L);
        assertThat(dataMaster1).isNotEqualTo(dataMaster2);
        dataMaster1.setId(null);
        assertThat(dataMaster1).isNotEqualTo(dataMaster2);
    }
}
