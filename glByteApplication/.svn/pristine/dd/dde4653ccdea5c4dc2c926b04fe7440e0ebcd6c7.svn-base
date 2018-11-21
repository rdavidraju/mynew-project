package com.nspl.app.web.rest;

import static com.nspl.app.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import javax.persistence.EntityManager;

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

import com.nspl.app.AgreeApplicationApp;
import com.nspl.app.domain.BatchHeader;
import com.nspl.app.repository.BatchHeaderRepository;
import com.nspl.app.repository.FileTemplatesRepository;
import com.nspl.app.repository.SchedulerDetailsRepository;
import com.nspl.app.repository.SourceFileInbHistoryRepository;
import com.nspl.app.repository.SourceProfileFileAssignmentsRepository;
import com.nspl.app.repository.SourceProfilesRepository;
import com.nspl.app.repository.search.BatchHeaderSearchRepository;
import com.nspl.app.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the BatchHeaderResource REST controller.
 *
 * @see BatchHeaderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class BatchHeaderResourceIntTest {

    private static final Long DEFAULT_TENANT_ID = 1L;
    private static final Long UPDATED_TENANT_ID = 2L;

    private static final String DEFAULT_BATCH_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BATCH_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_EXT_REF = "AAAAAAAAAA";
    private static final String UPDATED_EXT_REF = "BBBBBBBBBB";

    private static final String DEFAULT_JOB_REF = "AAAAAAAAAA";
    private static final String UPDATED_JOB_REF = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_EXTRACTED_DATETIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_EXTRACTED_DATETIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_EXTRACTION_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_EXTRACTION_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_TRANSFORMATION_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_TRANSFORMATION_STATUS = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_TRANSFORMED_DATETIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TRANSFORMED_DATETIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_NEXT_SCHEDULE = "AAAAAAAAAA";
    private static final String UPDATED_NEXT_SCHEDULE = "BBBBBBBBBB";

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_LAST_UPDATEDATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATEDATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private BatchHeaderRepository batchHeaderRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBatchHeaderMockMvc;

    private BatchHeader batchHeader;
    
    private SourceFileInbHistoryRepository SrcFileInbHistoryRepository;
    
    private SourceProfilesRepository sourceProfilesRepository;
    
    private SourceProfileFileAssignmentsRepository sourceProfileFileAssignmentsRepository;
    
    private FileTemplatesRepository fileTemplatesRepository;
    
    private BatchHeaderSearchRepository batchHeaderSearchRepository;
    
    private SchedulerDetailsRepository schedulerDetailsRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BatchHeaderResource batchHeaderResource = new BatchHeaderResource(batchHeaderRepository,SrcFileInbHistoryRepository,sourceProfilesRepository,sourceProfileFileAssignmentsRepository,fileTemplatesRepository,batchHeaderSearchRepository, schedulerDetailsRepository);
        this.restBatchHeaderMockMvc = MockMvcBuilders.standaloneSetup(batchHeaderResource)
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
    public static BatchHeader createEntity(EntityManager em) {
        BatchHeader batchHeader = new BatchHeader()
            .tenantId(DEFAULT_TENANT_ID)
            .batchName(DEFAULT_BATCH_NAME)
            .type(DEFAULT_TYPE)
            .extRef(DEFAULT_EXT_REF)
            .jobRef(DEFAULT_JOB_REF)
            .extractedDatetime(DEFAULT_EXTRACTED_DATETIME)
            .extractionStatus(DEFAULT_EXTRACTION_STATUS)
            .transformationStatus(DEFAULT_TRANSFORMATION_STATUS)
            .transformedDatetime(DEFAULT_TRANSFORMED_DATETIME)
            .nextSchedule(DEFAULT_NEXT_SCHEDULE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .lastUpdatedate(DEFAULT_LAST_UPDATEDATE);
        return batchHeader;
    }

    @Before
    public void initTest() {
        batchHeader = createEntity(em);
    }

    @Test
    @Transactional
    public void createBatchHeader() throws Exception {
        int databaseSizeBeforeCreate = batchHeaderRepository.findAll().size();

        // Create the BatchHeader
        restBatchHeaderMockMvc.perform(post("/api/batch-headers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(batchHeader)))
            .andExpect(status().isCreated());

        // Validate the BatchHeader in the database
        List<BatchHeader> batchHeaderList = batchHeaderRepository.findAll();
        assertThat(batchHeaderList).hasSize(databaseSizeBeforeCreate + 1);
        BatchHeader testBatchHeader = batchHeaderList.get(batchHeaderList.size() - 1);
        assertThat(testBatchHeader.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testBatchHeader.getBatchName()).isEqualTo(DEFAULT_BATCH_NAME);
        assertThat(testBatchHeader.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testBatchHeader.getExtRef()).isEqualTo(DEFAULT_EXT_REF);
        assertThat(testBatchHeader.getJobRef()).isEqualTo(DEFAULT_JOB_REF);
        assertThat(testBatchHeader.getExtractedDatetime()).isEqualTo(DEFAULT_EXTRACTED_DATETIME);
        assertThat(testBatchHeader.getExtractionStatus()).isEqualTo(DEFAULT_EXTRACTION_STATUS);
        assertThat(testBatchHeader.getTransformationStatus()).isEqualTo(DEFAULT_TRANSFORMATION_STATUS);
        assertThat(testBatchHeader.getTransformedDatetime()).isEqualTo(DEFAULT_TRANSFORMED_DATETIME);
        assertThat(testBatchHeader.getNextSchedule()).isEqualTo(DEFAULT_NEXT_SCHEDULE);
        assertThat(testBatchHeader.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testBatchHeader.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testBatchHeader.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testBatchHeader.getLastUpdatedate()).isEqualTo(DEFAULT_LAST_UPDATEDATE);
    }

    @Test
    @Transactional
    public void createBatchHeaderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = batchHeaderRepository.findAll().size();

        // Create the BatchHeader with an existing ID
        batchHeader.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBatchHeaderMockMvc.perform(post("/api/batch-headers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(batchHeader)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<BatchHeader> batchHeaderList = batchHeaderRepository.findAll();
        assertThat(batchHeaderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllBatchHeaders() throws Exception {
        // Initialize the database
        batchHeaderRepository.saveAndFlush(batchHeader);

        // Get all the batchHeaderList
        restBatchHeaderMockMvc.perform(get("/api/batch-headers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(batchHeader.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].batchName").value(hasItem(DEFAULT_BATCH_NAME.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].extRef").value(hasItem(DEFAULT_EXT_REF.toString())))
            .andExpect(jsonPath("$.[*].jobRef").value(hasItem(DEFAULT_JOB_REF.toString())))
            .andExpect(jsonPath("$.[*].extractedDatetime").value(hasItem(sameInstant(DEFAULT_EXTRACTED_DATETIME))))
            .andExpect(jsonPath("$.[*].extractionStatus").value(hasItem(DEFAULT_EXTRACTION_STATUS.toString())))
            .andExpect(jsonPath("$.[*].transformationStatus").value(hasItem(DEFAULT_TRANSFORMATION_STATUS.toString())))
            .andExpect(jsonPath("$.[*].transformedDatetime").value(hasItem(sameInstant(DEFAULT_TRANSFORMED_DATETIME))))
            .andExpect(jsonPath("$.[*].nextSchedule").value(hasItem(DEFAULT_NEXT_SCHEDULE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATEDATE))));
    }

    @Test
    @Transactional
    public void getBatchHeader() throws Exception {
        // Initialize the database
        batchHeaderRepository.saveAndFlush(batchHeader);

        // Get the batchHeader
        restBatchHeaderMockMvc.perform(get("/api/batch-headers/{id}", batchHeader.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(batchHeader.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID.intValue()))
            .andExpect(jsonPath("$.batchName").value(DEFAULT_BATCH_NAME.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.extRef").value(DEFAULT_EXT_REF.toString()))
            .andExpect(jsonPath("$.jobRef").value(DEFAULT_JOB_REF.toString()))
            .andExpect(jsonPath("$.extractedDatetime").value(sameInstant(DEFAULT_EXTRACTED_DATETIME)))
            .andExpect(jsonPath("$.extractionStatus").value(DEFAULT_EXTRACTION_STATUS.toString()))
            .andExpect(jsonPath("$.transformationStatus").value(DEFAULT_TRANSFORMATION_STATUS.toString()))
            .andExpect(jsonPath("$.transformedDatetime").value(sameInstant(DEFAULT_TRANSFORMED_DATETIME)))
            .andExpect(jsonPath("$.nextSchedule").value(DEFAULT_NEXT_SCHEDULE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedate").value(sameInstant(DEFAULT_LAST_UPDATEDATE)));
    }

    @Test
    @Transactional
    public void getNonExistingBatchHeader() throws Exception {
        // Get the batchHeader
        restBatchHeaderMockMvc.perform(get("/api/batch-headers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBatchHeader() throws Exception {
        // Initialize the database
        batchHeaderRepository.saveAndFlush(batchHeader);
        int databaseSizeBeforeUpdate = batchHeaderRepository.findAll().size();

        // Update the batchHeader
        BatchHeader updatedBatchHeader = batchHeaderRepository.findOne(batchHeader.getId());
        updatedBatchHeader
            .tenantId(UPDATED_TENANT_ID)
            .batchName(UPDATED_BATCH_NAME)
            .type(UPDATED_TYPE)
            .extRef(UPDATED_EXT_REF)
            .jobRef(UPDATED_JOB_REF)
            .extractedDatetime(UPDATED_EXTRACTED_DATETIME)
            .extractionStatus(UPDATED_EXTRACTION_STATUS)
            .transformationStatus(UPDATED_TRANSFORMATION_STATUS)
            .transformedDatetime(UPDATED_TRANSFORMED_DATETIME)
            .nextSchedule(UPDATED_NEXT_SCHEDULE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedate(UPDATED_LAST_UPDATEDATE);

        restBatchHeaderMockMvc.perform(put("/api/batch-headers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBatchHeader)))
            .andExpect(status().isOk());

        // Validate the BatchHeader in the database
        List<BatchHeader> batchHeaderList = batchHeaderRepository.findAll();
        assertThat(batchHeaderList).hasSize(databaseSizeBeforeUpdate);
        BatchHeader testBatchHeader = batchHeaderList.get(batchHeaderList.size() - 1);
        assertThat(testBatchHeader.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testBatchHeader.getBatchName()).isEqualTo(UPDATED_BATCH_NAME);
        assertThat(testBatchHeader.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testBatchHeader.getExtRef()).isEqualTo(UPDATED_EXT_REF);
        assertThat(testBatchHeader.getJobRef()).isEqualTo(UPDATED_JOB_REF);
        assertThat(testBatchHeader.getExtractedDatetime()).isEqualTo(UPDATED_EXTRACTED_DATETIME);
        assertThat(testBatchHeader.getExtractionStatus()).isEqualTo(UPDATED_EXTRACTION_STATUS);
        assertThat(testBatchHeader.getTransformationStatus()).isEqualTo(UPDATED_TRANSFORMATION_STATUS);
        assertThat(testBatchHeader.getTransformedDatetime()).isEqualTo(UPDATED_TRANSFORMED_DATETIME);
        assertThat(testBatchHeader.getNextSchedule()).isEqualTo(UPDATED_NEXT_SCHEDULE);
        assertThat(testBatchHeader.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testBatchHeader.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testBatchHeader.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testBatchHeader.getLastUpdatedate()).isEqualTo(UPDATED_LAST_UPDATEDATE);
    }

    @Test
    @Transactional
    public void updateNonExistingBatchHeader() throws Exception {
        int databaseSizeBeforeUpdate = batchHeaderRepository.findAll().size();

        // Create the BatchHeader

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBatchHeaderMockMvc.perform(put("/api/batch-headers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(batchHeader)))
            .andExpect(status().isCreated());

        // Validate the BatchHeader in the database
        List<BatchHeader> batchHeaderList = batchHeaderRepository.findAll();
        assertThat(batchHeaderList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBatchHeader() throws Exception {
        // Initialize the database
        batchHeaderRepository.saveAndFlush(batchHeader);
        int databaseSizeBeforeDelete = batchHeaderRepository.findAll().size();

        // Get the batchHeader
        restBatchHeaderMockMvc.perform(delete("/api/batch-headers/{id}", batchHeader.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<BatchHeader> batchHeaderList = batchHeaderRepository.findAll();
        assertThat(batchHeaderList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BatchHeader.class);
        BatchHeader batchHeader1 = new BatchHeader();
        batchHeader1.setId(1L);
        BatchHeader batchHeader2 = new BatchHeader();
        batchHeader2.setId(batchHeader1.getId());
        assertThat(batchHeader1).isEqualTo(batchHeader2);
        batchHeader2.setId(2L);
        assertThat(batchHeader1).isNotEqualTo(batchHeader2);
        batchHeader1.setId(null);
        assertThat(batchHeader1).isNotEqualTo(batchHeader2);
    }
}
