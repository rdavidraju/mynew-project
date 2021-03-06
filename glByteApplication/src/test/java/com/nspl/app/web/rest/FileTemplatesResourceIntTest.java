package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;
import com.nspl.app.domain.FileTemplates;
import com.nspl.app.repository.FileTemplateLinesRepository;
import com.nspl.app.repository.FileTemplatesRepository;
import com.nspl.app.repository.SourceProfileFileAssignmentsRepository;
import com.nspl.app.repository.SourceProfilesRepository;
import com.nspl.app.repository.search.FileTemplatesSearchRepository;
import com.nspl.app.service.FindDelimiterAndFileExtensionService;
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
 * Test class for the FileTemplatesResource REST controller.
 *
 * @see FileTemplatesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class FileTemplatesResourceIntTest {

	private static final String DEFAULT_TEMPLATE_NAME = "AAAAAAAAAA";
	private static final String UPDATED_TEMPLATE_NAME = "BBBBBBBBBB";

	private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
	private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

	private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
	private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

	private static final ZonedDateTime DEFAULT_END_DATE =ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
	private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

	private static final String DEFAULT_STATUS = "AAAAAAAAAA";
	private static final String UPDATED_STATUS = "BBBBBBBBBB";

	private static final String DEFAULT_FILE_TYPE = "AAAAAAAAAA";
	private static final String UPDATED_FILE_TYPE = "BBBBBBBBBB";

	private static final String DEFAULT_DELIMITER = "AAAAAAAAAA";
	private static final String UPDATED_DELIMITER = "BBBBBBBBBB";

	private static final String DEFAULT_SOURCE = "AAAAAAAAAA";
	private static final String UPDATED_SOURCE = "BBBBBBBBBB";

	private static final Integer DEFAULT_SKIP_ROWS_START = 1;
	private static final Integer UPDATED_SKIP_ROWS_START = 2;

	private static final Integer DEFAULT_SKIP_ROWS_END = 1;
	private static final Integer UPDATED_SKIP_ROWS_END = 2;

	private static final Integer DEFAULT_NUMBER_OF_COLUMNS = 1;
	private static final Integer UPDATED_NUMBER_OF_COLUMNS = 2;

	private static final Integer DEFAULT_HEADER_ROW_NUMBER = 1;
	private static final Integer UPDATED_HEADER_ROW_NUMBER = 2;

	private static final Long DEFAULT_TENANT_ID = 1L;
	private static final Long UPDATED_TENANT_ID = 2L;

	private static final Long DEFAULT_CREATED_BY = 1L;
	private static final Long UPDATED_CREATED_BY = 2L;

	private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
	private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

	private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
	private static final Long UPDATED_LAST_UPDATED_BY = 2L;

	private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
	private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

	@Autowired
	private FileTemplatesRepository fileTemplatesRepository;

	@Autowired
	private FileTemplatesSearchRepository fileTemplatesSearchRepository;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private EntityManager em;

	private MockMvc restFileTemplatesMockMvc;

	private FileTemplates fileTemplates;

	@Autowired
	private  SourceProfileFileAssignmentsRepository sourceProfileFileAssignmentsRepository;

	@Autowired
	private  SourceProfilesRepository sourceProfilesRepository;
	@Autowired
	private FileTemplateLinesRepository fileTemplateLinesRepository;
	
	private FindDelimiterAndFileExtensionService findDelimiterAndFileExtensionService;
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		FileTemplatesResource fileTemplatesResource = new FileTemplatesResource(fileTemplatesRepository, fileTemplatesSearchRepository,sourceProfileFileAssignmentsRepository,sourceProfilesRepository,fileTemplateLinesRepository,findDelimiterAndFileExtensionService);
		this.restFileTemplatesMockMvc = MockMvcBuilders.standaloneSetup(fileTemplatesResource)
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
	public static FileTemplates createEntity(EntityManager em) {
		FileTemplates fileTemplates = new FileTemplates()
		.templateName(DEFAULT_TEMPLATE_NAME)
		.description(DEFAULT_DESCRIPTION)
		.startDate(DEFAULT_START_DATE)
		.endDate(DEFAULT_END_DATE)
		.status(DEFAULT_STATUS)
		.fileType(DEFAULT_FILE_TYPE)
		.delimiter(DEFAULT_DELIMITER)
		.source(DEFAULT_SOURCE)
		.skipRowsStart(DEFAULT_SKIP_ROWS_START)
		.skipRowsEnd(DEFAULT_SKIP_ROWS_END)
		.number_of_columns(DEFAULT_NUMBER_OF_COLUMNS)
		.headerRowNumber(DEFAULT_HEADER_ROW_NUMBER)
		.tenantId(DEFAULT_TENANT_ID)
		.createdBy(DEFAULT_CREATED_BY)
		.createdDate(DEFAULT_CREATED_DATE)
		.lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
		.lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE);
		return fileTemplates;
	}

	@Before
	public void initTest() {
		fileTemplatesSearchRepository.deleteAll();
		fileTemplates = createEntity(em);
	}

	@Test
	@Transactional
	public void createFileTemplates() throws Exception {
		int databaseSizeBeforeCreate = fileTemplatesRepository.findAll().size();

		// Create the FileTemplates
		restFileTemplatesMockMvc.perform(post("/api/file-templates")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(fileTemplates)))
				.andExpect(status().isCreated());

		// Validate the FileTemplates in the database
		List<FileTemplates> fileTemplatesList = fileTemplatesRepository.findAll();
		assertThat(fileTemplatesList).hasSize(databaseSizeBeforeCreate + 1);
		FileTemplates testFileTemplates = fileTemplatesList.get(fileTemplatesList.size() - 1);
		assertThat(testFileTemplates.getTemplateName()).isEqualTo(DEFAULT_TEMPLATE_NAME);
		assertThat(testFileTemplates.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
		assertThat(testFileTemplates.getStartDate()).isEqualTo(DEFAULT_START_DATE);
		assertThat(testFileTemplates.getEndDate()).isEqualTo(DEFAULT_END_DATE);
		assertThat(testFileTemplates.getStatus()).isEqualTo(DEFAULT_STATUS);
		assertThat(testFileTemplates.getFileType()).isEqualTo(DEFAULT_FILE_TYPE);
		assertThat(testFileTemplates.getDelimiter()).isEqualTo(DEFAULT_DELIMITER);
		assertThat(testFileTemplates.getSource()).isEqualTo(DEFAULT_SOURCE);
		assertThat(testFileTemplates.getSkipRowsStart()).isEqualTo(DEFAULT_SKIP_ROWS_START);
		assertThat(testFileTemplates.getSkipRowsEnd()).isEqualTo(DEFAULT_SKIP_ROWS_END);
		assertThat(testFileTemplates.getNumber_of_columns()).isEqualTo(DEFAULT_NUMBER_OF_COLUMNS);
		assertThat(testFileTemplates.getHeaderRowNumber()).isEqualTo(DEFAULT_HEADER_ROW_NUMBER);
		assertThat(testFileTemplates.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
		assertThat(testFileTemplates.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
		assertThat(testFileTemplates.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
		assertThat(testFileTemplates.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
		assertThat(testFileTemplates.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);

		// Validate the FileTemplates in Elasticsearch
		FileTemplates fileTemplatesEs = fileTemplatesSearchRepository.findOne(testFileTemplates.getId());
		assertThat(fileTemplatesEs).isEqualToComparingFieldByField(testFileTemplates);
	}

	@Test
	@Transactional
	public void createFileTemplatesWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = fileTemplatesRepository.findAll().size();

		// Create the FileTemplates with an existing ID
		fileTemplates.setId(1L);

		// An entity with an existing ID cannot be created, so this API call must fail
		restFileTemplatesMockMvc.perform(post("/api/file-templates")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(fileTemplates)))
				.andExpect(status().isBadRequest());

		// Validate the Alice in the database
		List<FileTemplates> fileTemplatesList = fileTemplatesRepository.findAll();
		assertThat(fileTemplatesList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void getAllFileTemplates() throws Exception {
		// Initialize the database
		fileTemplatesRepository.saveAndFlush(fileTemplates);

		// Get all the fileTemplatesList
		restFileTemplatesMockMvc.perform(get("/api/file-templates?sort=id,desc"))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(jsonPath("$.[*].id").value(hasItem(fileTemplates.getId().intValue())))
		.andExpect(jsonPath("$.[*].templateName").value(hasItem(DEFAULT_TEMPLATE_NAME.toString())))
		.andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
		.andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
		.andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
		.andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
		.andExpect(jsonPath("$.[*].fileType").value(hasItem(DEFAULT_FILE_TYPE.toString())))
		.andExpect(jsonPath("$.[*].delimiter").value(hasItem(DEFAULT_DELIMITER.toString())))
		.andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE.toString())))
		.andExpect(jsonPath("$.[*].skipRowsStart").value(hasItem(DEFAULT_SKIP_ROWS_START)))
		.andExpect(jsonPath("$.[*].skipRowsEnd").value(hasItem(DEFAULT_SKIP_ROWS_END)))
		.andExpect(jsonPath("$.[*].number_of_columns").value(hasItem(DEFAULT_NUMBER_OF_COLUMNS)))
		.andExpect(jsonPath("$.[*].headerRowNumber").value(hasItem(DEFAULT_HEADER_ROW_NUMBER)))
		.andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
		.andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
		.andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
		.andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
		.andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
	}

	@Test
	@Transactional
	public void getFileTemplates() throws Exception {
		// Initialize the database
		fileTemplatesRepository.saveAndFlush(fileTemplates);

		// Get the fileTemplates
		restFileTemplatesMockMvc.perform(get("/api/file-templates/{id}", fileTemplates.getId()))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(jsonPath("$.id").value(fileTemplates.getId().intValue()))
		.andExpect(jsonPath("$.templateName").value(DEFAULT_TEMPLATE_NAME.toString()))
		.andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
		.andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
		.andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
		.andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
		.andExpect(jsonPath("$.fileType").value(DEFAULT_FILE_TYPE.toString()))
		.andExpect(jsonPath("$.delimiter").value(DEFAULT_DELIMITER.toString()))
		.andExpect(jsonPath("$.source").value(DEFAULT_SOURCE.toString()))
		.andExpect(jsonPath("$.skipRowsStart").value(DEFAULT_SKIP_ROWS_START))
		.andExpect(jsonPath("$.skipRowsEnd").value(DEFAULT_SKIP_ROWS_END))
		.andExpect(jsonPath("$.number_of_columns").value(DEFAULT_NUMBER_OF_COLUMNS))
		.andExpect(jsonPath("$.headerRowNumber").value(DEFAULT_HEADER_ROW_NUMBER))
		.andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID.intValue()))
		.andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
		.andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
		.andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()))
		.andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)));
	}

	@Test
	@Transactional
	public void getNonExistingFileTemplates() throws Exception {
		// Get the fileTemplates
		restFileTemplatesMockMvc.perform(get("/api/file-templates/{id}", Long.MAX_VALUE))
		.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateFileTemplates() throws Exception {
		// Initialize the database
		fileTemplatesRepository.saveAndFlush(fileTemplates);
		fileTemplatesSearchRepository.save(fileTemplates);
		int databaseSizeBeforeUpdate = fileTemplatesRepository.findAll().size();

		// Update the fileTemplates
		FileTemplates updatedFileTemplates = fileTemplatesRepository.findOne(fileTemplates.getId());
		updatedFileTemplates
		.templateName(UPDATED_TEMPLATE_NAME)
		.description(UPDATED_DESCRIPTION)
		.startDate(UPDATED_START_DATE)
		.endDate(UPDATED_END_DATE)
		.status(UPDATED_STATUS)
		.fileType(UPDATED_FILE_TYPE)
		.delimiter(UPDATED_DELIMITER)
		.source(UPDATED_SOURCE)
		.skipRowsStart(UPDATED_SKIP_ROWS_START)
		.skipRowsEnd(UPDATED_SKIP_ROWS_END)
		.number_of_columns(UPDATED_NUMBER_OF_COLUMNS)
		.headerRowNumber(UPDATED_HEADER_ROW_NUMBER)
		.tenantId(UPDATED_TENANT_ID)
		.createdBy(UPDATED_CREATED_BY)
		.createdDate(UPDATED_CREATED_DATE)
		.lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
		.lastUpdatedDate(UPDATED_LAST_UPDATED_DATE);

		restFileTemplatesMockMvc.perform(put("/api/file-templates")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(updatedFileTemplates)))
				.andExpect(status().isOk());

		// Validate the FileTemplates in the database
		List<FileTemplates> fileTemplatesList = fileTemplatesRepository.findAll();
		assertThat(fileTemplatesList).hasSize(databaseSizeBeforeUpdate);
		FileTemplates testFileTemplates = fileTemplatesList.get(fileTemplatesList.size() - 1);
		assertThat(testFileTemplates.getTemplateName()).isEqualTo(UPDATED_TEMPLATE_NAME);
		assertThat(testFileTemplates.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
		assertThat(testFileTemplates.getStartDate()).isEqualTo(UPDATED_START_DATE);
		assertThat(testFileTemplates.getEndDate()).isEqualTo(UPDATED_END_DATE);
		assertThat(testFileTemplates.getStatus()).isEqualTo(UPDATED_STATUS);
		assertThat(testFileTemplates.getFileType()).isEqualTo(UPDATED_FILE_TYPE);
		assertThat(testFileTemplates.getDelimiter()).isEqualTo(UPDATED_DELIMITER);
		assertThat(testFileTemplates.getSource()).isEqualTo(UPDATED_SOURCE);
		assertThat(testFileTemplates.getSkipRowsStart()).isEqualTo(UPDATED_SKIP_ROWS_START);
		assertThat(testFileTemplates.getSkipRowsEnd()).isEqualTo(UPDATED_SKIP_ROWS_END);
		assertThat(testFileTemplates.getNumber_of_columns()).isEqualTo(UPDATED_NUMBER_OF_COLUMNS);
		assertThat(testFileTemplates.getHeaderRowNumber()).isEqualTo(UPDATED_HEADER_ROW_NUMBER);
		assertThat(testFileTemplates.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
		assertThat(testFileTemplates.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
		assertThat(testFileTemplates.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
		assertThat(testFileTemplates.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
		assertThat(testFileTemplates.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);

		// Validate the FileTemplates in Elasticsearch
		FileTemplates fileTemplatesEs = fileTemplatesSearchRepository.findOne(testFileTemplates.getId());
		assertThat(fileTemplatesEs).isEqualToComparingFieldByField(testFileTemplates);
	}

	@Test
	@Transactional
	public void updateNonExistingFileTemplates() throws Exception {
		int databaseSizeBeforeUpdate = fileTemplatesRepository.findAll().size();

		// Create the FileTemplates

		// If the entity doesn't have an ID, it will be created instead of just being updated
		restFileTemplatesMockMvc.perform(put("/api/file-templates")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(fileTemplates)))
				.andExpect(status().isCreated());

		// Validate the FileTemplates in the database
		List<FileTemplates> fileTemplatesList = fileTemplatesRepository.findAll();
		assertThat(fileTemplatesList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	@Test
	@Transactional
	public void deleteFileTemplates() throws Exception {
		// Initialize the database
		fileTemplatesRepository.saveAndFlush(fileTemplates);
		fileTemplatesSearchRepository.save(fileTemplates);
		int databaseSizeBeforeDelete = fileTemplatesRepository.findAll().size();

		// Get the fileTemplates
		restFileTemplatesMockMvc.perform(delete("/api/file-templates/{id}", fileTemplates.getId())
				.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate Elasticsearch is empty
		boolean fileTemplatesExistsInEs = fileTemplatesSearchRepository.exists(fileTemplates.getId());
		assertThat(fileTemplatesExistsInEs).isFalse();

		// Validate the database is empty
		List<FileTemplates> fileTemplatesList = fileTemplatesRepository.findAll();
		assertThat(fileTemplatesList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	public void searchFileTemplates() throws Exception {
		// Initialize the database
		fileTemplatesRepository.saveAndFlush(fileTemplates);
		fileTemplatesSearchRepository.save(fileTemplates);

		// Search the fileTemplates
		restFileTemplatesMockMvc.perform(get("/api/_search/file-templates?query=id:" + fileTemplates.getId()))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(jsonPath("$.[*].id").value(hasItem(fileTemplates.getId().intValue())))
		.andExpect(jsonPath("$.[*].templateName").value(hasItem(DEFAULT_TEMPLATE_NAME.toString())))
		.andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
		.andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
		.andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
		.andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
		.andExpect(jsonPath("$.[*].fileType").value(hasItem(DEFAULT_FILE_TYPE.toString())))
		.andExpect(jsonPath("$.[*].delimiter").value(hasItem(DEFAULT_DELIMITER.toString())))
		.andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE.toString())))
		.andExpect(jsonPath("$.[*].skipRowsStart").value(hasItem(DEFAULT_SKIP_ROWS_START)))
		.andExpect(jsonPath("$.[*].skipRowsEnd").value(hasItem(DEFAULT_SKIP_ROWS_END)))
		.andExpect(jsonPath("$.[*].number_of_columns").value(hasItem(DEFAULT_NUMBER_OF_COLUMNS)))
		.andExpect(jsonPath("$.[*].headerRowNumber").value(hasItem(DEFAULT_HEADER_ROW_NUMBER)))
		.andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
		.andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
		.andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
		.andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
		.andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(FileTemplates.class);
		FileTemplates fileTemplates1 = new FileTemplates();
		fileTemplates1.setId(1L);
		FileTemplates fileTemplates2 = new FileTemplates();
		fileTemplates2.setId(fileTemplates1.getId());
		assertThat(fileTemplates1).isEqualTo(fileTemplates2);
		fileTemplates2.setId(2L);
		assertThat(fileTemplates1).isNotEqualTo(fileTemplates2);
		fileTemplates1.setId(null);
		assertThat(fileTemplates1).isNotEqualTo(fileTemplates2);
	}
}
