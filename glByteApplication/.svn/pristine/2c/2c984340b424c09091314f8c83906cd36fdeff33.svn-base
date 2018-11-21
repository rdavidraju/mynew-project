package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.FileTemplateSummaryToDetailMapping;
import com.nspl.app.repository.FileTemplateSummaryToDetailMappingRepository;
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
 * Test class for the FileTemplateSummaryToDetailMappingResource REST controller.
 *
 * @see FileTemplateSummaryToDetailMappingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class FileTemplateSummaryToDetailMappingResourceIntTest {

    private static final Long DEFAULT_DETAIL_TEMPLATE_ID = 1L;
    private static final Long UPDATED_DETAIL_TEMPLATE_ID = 2L;

    private static final Long DEFAULT_SUMMARY_TEMPLATE_ID = 1L;
    private static final Long UPDATED_SUMMARY_TEMPLATE_ID = 2L;

    private static final Long DEFAULT_DETAIL_ROW_ID = 1L;
    private static final Long UPDATED_DETAIL_ROW_ID = 2L;

    private static final Long DEFAULT_SUMMARY_ROW_ID = 1L;
    private static final Long UPDATED_SUMMARY_ROW_ID = 2L;

    @Autowired
    private FileTemplateSummaryToDetailMappingRepository fileTemplateSummaryToDetailMappingRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFileTemplateSummaryToDetailMappingMockMvc;

    private FileTemplateSummaryToDetailMapping fileTemplateSummaryToDetailMapping;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FileTemplateSummaryToDetailMappingResource fileTemplateSummaryToDetailMappingResource = new FileTemplateSummaryToDetailMappingResource(fileTemplateSummaryToDetailMappingRepository);
        this.restFileTemplateSummaryToDetailMappingMockMvc = MockMvcBuilders.standaloneSetup(fileTemplateSummaryToDetailMappingResource)
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
    public static FileTemplateSummaryToDetailMapping createEntity(EntityManager em) {
        FileTemplateSummaryToDetailMapping fileTemplateSummaryToDetailMapping = new FileTemplateSummaryToDetailMapping()
            .detailTemplateId(DEFAULT_DETAIL_TEMPLATE_ID)
            .summaryTemplateId(DEFAULT_SUMMARY_TEMPLATE_ID)
            .detailRowId(DEFAULT_DETAIL_ROW_ID)
            .summaryRowId(DEFAULT_SUMMARY_ROW_ID);
        return fileTemplateSummaryToDetailMapping;
    }

    @Before
    public void initTest() {
        fileTemplateSummaryToDetailMapping = createEntity(em);
    }

    @Test
    @Transactional
    public void createFileTemplateSummaryToDetailMapping() throws Exception {
        int databaseSizeBeforeCreate = fileTemplateSummaryToDetailMappingRepository.findAll().size();

        // Create the FileTemplateSummaryToDetailMapping
        restFileTemplateSummaryToDetailMappingMockMvc.perform(post("/api/file-template-summary-to-detail-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fileTemplateSummaryToDetailMapping)))
            .andExpect(status().isCreated());

        // Validate the FileTemplateSummaryToDetailMapping in the database
        List<FileTemplateSummaryToDetailMapping> fileTemplateSummaryToDetailMappingList = fileTemplateSummaryToDetailMappingRepository.findAll();
        assertThat(fileTemplateSummaryToDetailMappingList).hasSize(databaseSizeBeforeCreate + 1);
        FileTemplateSummaryToDetailMapping testFileTemplateSummaryToDetailMapping = fileTemplateSummaryToDetailMappingList.get(fileTemplateSummaryToDetailMappingList.size() - 1);
        assertThat(testFileTemplateSummaryToDetailMapping.getDetailTemplateId()).isEqualTo(DEFAULT_DETAIL_TEMPLATE_ID);
        assertThat(testFileTemplateSummaryToDetailMapping.getSummaryTemplateId()).isEqualTo(DEFAULT_SUMMARY_TEMPLATE_ID);
        assertThat(testFileTemplateSummaryToDetailMapping.getDetailRowId()).isEqualTo(DEFAULT_DETAIL_ROW_ID);
        assertThat(testFileTemplateSummaryToDetailMapping.getSummaryRowId()).isEqualTo(DEFAULT_SUMMARY_ROW_ID);
    }

    @Test
    @Transactional
    public void createFileTemplateSummaryToDetailMappingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fileTemplateSummaryToDetailMappingRepository.findAll().size();

        // Create the FileTemplateSummaryToDetailMapping with an existing ID
        fileTemplateSummaryToDetailMapping.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFileTemplateSummaryToDetailMappingMockMvc.perform(post("/api/file-template-summary-to-detail-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fileTemplateSummaryToDetailMapping)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<FileTemplateSummaryToDetailMapping> fileTemplateSummaryToDetailMappingList = fileTemplateSummaryToDetailMappingRepository.findAll();
        assertThat(fileTemplateSummaryToDetailMappingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllFileTemplateSummaryToDetailMappings() throws Exception {
        // Initialize the database
        fileTemplateSummaryToDetailMappingRepository.saveAndFlush(fileTemplateSummaryToDetailMapping);

        // Get all the fileTemplateSummaryToDetailMappingList
        restFileTemplateSummaryToDetailMappingMockMvc.perform(get("/api/file-template-summary-to-detail-mappings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fileTemplateSummaryToDetailMapping.getId().intValue())))
            .andExpect(jsonPath("$.[*].detailTemplateId").value(hasItem(DEFAULT_DETAIL_TEMPLATE_ID.intValue())))
            .andExpect(jsonPath("$.[*].summaryTemplateId").value(hasItem(DEFAULT_SUMMARY_TEMPLATE_ID.intValue())))
            .andExpect(jsonPath("$.[*].detailRowId").value(hasItem(DEFAULT_DETAIL_ROW_ID.intValue())))
            .andExpect(jsonPath("$.[*].summaryRowId").value(hasItem(DEFAULT_SUMMARY_ROW_ID.intValue())));
    }

    @Test
    @Transactional
    public void getFileTemplateSummaryToDetailMapping() throws Exception {
        // Initialize the database
        fileTemplateSummaryToDetailMappingRepository.saveAndFlush(fileTemplateSummaryToDetailMapping);

        // Get the fileTemplateSummaryToDetailMapping
        restFileTemplateSummaryToDetailMappingMockMvc.perform(get("/api/file-template-summary-to-detail-mappings/{id}", fileTemplateSummaryToDetailMapping.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(fileTemplateSummaryToDetailMapping.getId().intValue()))
            .andExpect(jsonPath("$.detailTemplateId").value(DEFAULT_DETAIL_TEMPLATE_ID.intValue()))
            .andExpect(jsonPath("$.summaryTemplateId").value(DEFAULT_SUMMARY_TEMPLATE_ID.intValue()))
            .andExpect(jsonPath("$.detailRowId").value(DEFAULT_DETAIL_ROW_ID.intValue()))
            .andExpect(jsonPath("$.summaryRowId").value(DEFAULT_SUMMARY_ROW_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFileTemplateSummaryToDetailMapping() throws Exception {
        // Get the fileTemplateSummaryToDetailMapping
        restFileTemplateSummaryToDetailMappingMockMvc.perform(get("/api/file-template-summary-to-detail-mappings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFileTemplateSummaryToDetailMapping() throws Exception {
        // Initialize the database
        fileTemplateSummaryToDetailMappingRepository.saveAndFlush(fileTemplateSummaryToDetailMapping);
        int databaseSizeBeforeUpdate = fileTemplateSummaryToDetailMappingRepository.findAll().size();

        // Update the fileTemplateSummaryToDetailMapping
        FileTemplateSummaryToDetailMapping updatedFileTemplateSummaryToDetailMapping = fileTemplateSummaryToDetailMappingRepository.findOne(fileTemplateSummaryToDetailMapping.getId());
        updatedFileTemplateSummaryToDetailMapping
            .detailTemplateId(UPDATED_DETAIL_TEMPLATE_ID)
            .summaryTemplateId(UPDATED_SUMMARY_TEMPLATE_ID)
            .detailRowId(UPDATED_DETAIL_ROW_ID)
            .summaryRowId(UPDATED_SUMMARY_ROW_ID);

        restFileTemplateSummaryToDetailMappingMockMvc.perform(put("/api/file-template-summary-to-detail-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFileTemplateSummaryToDetailMapping)))
            .andExpect(status().isOk());

        // Validate the FileTemplateSummaryToDetailMapping in the database
        List<FileTemplateSummaryToDetailMapping> fileTemplateSummaryToDetailMappingList = fileTemplateSummaryToDetailMappingRepository.findAll();
        assertThat(fileTemplateSummaryToDetailMappingList).hasSize(databaseSizeBeforeUpdate);
        FileTemplateSummaryToDetailMapping testFileTemplateSummaryToDetailMapping = fileTemplateSummaryToDetailMappingList.get(fileTemplateSummaryToDetailMappingList.size() - 1);
        assertThat(testFileTemplateSummaryToDetailMapping.getDetailTemplateId()).isEqualTo(UPDATED_DETAIL_TEMPLATE_ID);
        assertThat(testFileTemplateSummaryToDetailMapping.getSummaryTemplateId()).isEqualTo(UPDATED_SUMMARY_TEMPLATE_ID);
        assertThat(testFileTemplateSummaryToDetailMapping.getDetailRowId()).isEqualTo(UPDATED_DETAIL_ROW_ID);
        assertThat(testFileTemplateSummaryToDetailMapping.getSummaryRowId()).isEqualTo(UPDATED_SUMMARY_ROW_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingFileTemplateSummaryToDetailMapping() throws Exception {
        int databaseSizeBeforeUpdate = fileTemplateSummaryToDetailMappingRepository.findAll().size();

        // Create the FileTemplateSummaryToDetailMapping

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFileTemplateSummaryToDetailMappingMockMvc.perform(put("/api/file-template-summary-to-detail-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fileTemplateSummaryToDetailMapping)))
            .andExpect(status().isCreated());

        // Validate the FileTemplateSummaryToDetailMapping in the database
        List<FileTemplateSummaryToDetailMapping> fileTemplateSummaryToDetailMappingList = fileTemplateSummaryToDetailMappingRepository.findAll();
        assertThat(fileTemplateSummaryToDetailMappingList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFileTemplateSummaryToDetailMapping() throws Exception {
        // Initialize the database
        fileTemplateSummaryToDetailMappingRepository.saveAndFlush(fileTemplateSummaryToDetailMapping);
        int databaseSizeBeforeDelete = fileTemplateSummaryToDetailMappingRepository.findAll().size();

        // Get the fileTemplateSummaryToDetailMapping
        restFileTemplateSummaryToDetailMappingMockMvc.perform(delete("/api/file-template-summary-to-detail-mappings/{id}", fileTemplateSummaryToDetailMapping.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<FileTemplateSummaryToDetailMapping> fileTemplateSummaryToDetailMappingList = fileTemplateSummaryToDetailMappingRepository.findAll();
        assertThat(fileTemplateSummaryToDetailMappingList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FileTemplateSummaryToDetailMapping.class);
        FileTemplateSummaryToDetailMapping fileTemplateSummaryToDetailMapping1 = new FileTemplateSummaryToDetailMapping();
        fileTemplateSummaryToDetailMapping1.setId(1L);
        FileTemplateSummaryToDetailMapping fileTemplateSummaryToDetailMapping2 = new FileTemplateSummaryToDetailMapping();
        fileTemplateSummaryToDetailMapping2.setId(fileTemplateSummaryToDetailMapping1.getId());
        assertThat(fileTemplateSummaryToDetailMapping1).isEqualTo(fileTemplateSummaryToDetailMapping2);
        fileTemplateSummaryToDetailMapping2.setId(2L);
        assertThat(fileTemplateSummaryToDetailMapping1).isNotEqualTo(fileTemplateSummaryToDetailMapping2);
        fileTemplateSummaryToDetailMapping1.setId(null);
        assertThat(fileTemplateSummaryToDetailMapping1).isNotEqualTo(fileTemplateSummaryToDetailMapping2);
    }
}
