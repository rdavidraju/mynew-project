package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.AttachmentFiles;
import com.nspl.app.repository.AttachmentFilesRepository;
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
 * Test class for the AttachmentFilesResource REST controller.
 *
 * @see AttachmentFilesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class AttachmentFilesResourceIntTest {

    private static final Long DEFAULT_ATTACHMENT_ID = 1L;
    private static final Long UPDATED_ATTACHMENT_ID = 2L;

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_FILE_TYPE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FILE_CONTENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILE_CONTENT = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_FILE_CONTENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILE_CONTENT_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private AttachmentFilesRepository attachmentFilesRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAttachmentFilesMockMvc;

    private AttachmentFiles attachmentFiles;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AttachmentFilesResource attachmentFilesResource = new AttachmentFilesResource(attachmentFilesRepository);
        this.restAttachmentFilesMockMvc = MockMvcBuilders.standaloneSetup(attachmentFilesResource)
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
    public static AttachmentFiles createEntity(EntityManager em) {
        AttachmentFiles attachmentFiles = new AttachmentFiles()
            .attachmentId(DEFAULT_ATTACHMENT_ID)
            .fileName(DEFAULT_FILE_NAME)
            .fileType(DEFAULT_FILE_TYPE)
            .fileContent(DEFAULT_FILE_CONTENT)
            .url(DEFAULT_URL)
            .createdBy(DEFAULT_CREATED_BY)
            .creationDate(DEFAULT_CREATION_DATE)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE);
        return attachmentFiles;
    }

    @Before
    public void initTest() {
        attachmentFiles = createEntity(em);
    }

    @Test
    @Transactional
    public void createAttachmentFiles() throws Exception {
        int databaseSizeBeforeCreate = attachmentFilesRepository.findAll().size();

        // Create the AttachmentFiles
        restAttachmentFilesMockMvc.perform(post("/api/attachment-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attachmentFiles)))
            .andExpect(status().isCreated());

        // Validate the AttachmentFiles in the database
        List<AttachmentFiles> attachmentFilesList = attachmentFilesRepository.findAll();
        assertThat(attachmentFilesList).hasSize(databaseSizeBeforeCreate + 1);
        AttachmentFiles testAttachmentFiles = attachmentFilesList.get(attachmentFilesList.size() - 1);
        assertThat(testAttachmentFiles.getAttachmentId()).isEqualTo(DEFAULT_ATTACHMENT_ID);
        assertThat(testAttachmentFiles.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testAttachmentFiles.getFileType()).isEqualTo(DEFAULT_FILE_TYPE);
        assertThat(testAttachmentFiles.getFileContent()).isEqualTo(DEFAULT_FILE_CONTENT);
        assertThat(testAttachmentFiles.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testAttachmentFiles.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testAttachmentFiles.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testAttachmentFiles.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testAttachmentFiles.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void createAttachmentFilesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = attachmentFilesRepository.findAll().size();

        // Create the AttachmentFiles with an existing ID
        attachmentFiles.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttachmentFilesMockMvc.perform(post("/api/attachment-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attachmentFiles)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<AttachmentFiles> attachmentFilesList = attachmentFilesRepository.findAll();
        assertThat(attachmentFilesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAttachmentFiles() throws Exception {
        // Initialize the database
        attachmentFilesRepository.saveAndFlush(attachmentFiles);

        // Get all the attachmentFilesList
        restAttachmentFilesMockMvc.perform(get("/api/attachment-files?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attachmentFiles.getId().intValue())))
            .andExpect(jsonPath("$.[*].attachmentId").value(hasItem(DEFAULT_ATTACHMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME.toString())))
            .andExpect(jsonPath("$.[*].fileType").value(hasItem(DEFAULT_FILE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].fileContentContentType").value(hasItem(DEFAULT_FILE_CONTENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fileContent").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE_CONTENT))))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void getAttachmentFiles() throws Exception {
        // Initialize the database
        attachmentFilesRepository.saveAndFlush(attachmentFiles);

        // Get the attachmentFiles
        restAttachmentFilesMockMvc.perform(get("/api/attachment-files/{id}", attachmentFiles.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(attachmentFiles.getId().intValue()))
            .andExpect(jsonPath("$.attachmentId").value(DEFAULT_ATTACHMENT_ID.intValue()))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME.toString()))
            .andExpect(jsonPath("$.fileType").value(DEFAULT_FILE_TYPE.toString()))
            .andExpect(jsonPath("$.fileContentContentType").value(DEFAULT_FILE_CONTENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.fileContent").value(Base64Utils.encodeToString(DEFAULT_FILE_CONTENT)))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.creationDate").value(sameInstant(DEFAULT_CREATION_DATE)))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingAttachmentFiles() throws Exception {
        // Get the attachmentFiles
        restAttachmentFilesMockMvc.perform(get("/api/attachment-files/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAttachmentFiles() throws Exception {
        // Initialize the database
        attachmentFilesRepository.saveAndFlush(attachmentFiles);
        int databaseSizeBeforeUpdate = attachmentFilesRepository.findAll().size();

        // Update the attachmentFiles
        AttachmentFiles updatedAttachmentFiles = attachmentFilesRepository.findOne(attachmentFiles.getId());
        updatedAttachmentFiles
            .attachmentId(UPDATED_ATTACHMENT_ID)
            .fileName(UPDATED_FILE_NAME)
            .fileType(UPDATED_FILE_TYPE)
            .fileContent(UPDATED_FILE_CONTENT)
            .url(UPDATED_URL)
            .createdBy(UPDATED_CREATED_BY)
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE);

        restAttachmentFilesMockMvc.perform(put("/api/attachment-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAttachmentFiles)))
            .andExpect(status().isOk());

        // Validate the AttachmentFiles in the database
        List<AttachmentFiles> attachmentFilesList = attachmentFilesRepository.findAll();
        assertThat(attachmentFilesList).hasSize(databaseSizeBeforeUpdate);
        AttachmentFiles testAttachmentFiles = attachmentFilesList.get(attachmentFilesList.size() - 1);
        assertThat(testAttachmentFiles.getAttachmentId()).isEqualTo(UPDATED_ATTACHMENT_ID);
        assertThat(testAttachmentFiles.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testAttachmentFiles.getFileType()).isEqualTo(UPDATED_FILE_TYPE);
        assertThat(testAttachmentFiles.getFileContent()).isEqualTo(UPDATED_FILE_CONTENT);
        assertThat(testAttachmentFiles.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testAttachmentFiles.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAttachmentFiles.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testAttachmentFiles.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testAttachmentFiles.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingAttachmentFiles() throws Exception {
        int databaseSizeBeforeUpdate = attachmentFilesRepository.findAll().size();

        // Create the AttachmentFiles

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAttachmentFilesMockMvc.perform(put("/api/attachment-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attachmentFiles)))
            .andExpect(status().isCreated());

        // Validate the AttachmentFiles in the database
        List<AttachmentFiles> attachmentFilesList = attachmentFilesRepository.findAll();
        assertThat(attachmentFilesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAttachmentFiles() throws Exception {
        // Initialize the database
        attachmentFilesRepository.saveAndFlush(attachmentFiles);
        int databaseSizeBeforeDelete = attachmentFilesRepository.findAll().size();

        // Get the attachmentFiles
        restAttachmentFilesMockMvc.perform(delete("/api/attachment-files/{id}", attachmentFiles.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AttachmentFiles> attachmentFilesList = attachmentFilesRepository.findAll();
        assertThat(attachmentFilesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AttachmentFiles.class);
        AttachmentFiles attachmentFiles1 = new AttachmentFiles();
        attachmentFiles1.setId(1L);
        AttachmentFiles attachmentFiles2 = new AttachmentFiles();
        attachmentFiles2.setId(attachmentFiles1.getId());
        assertThat(attachmentFiles1).isEqualTo(attachmentFiles2);
        attachmentFiles2.setId(2L);
        assertThat(attachmentFiles1).isNotEqualTo(attachmentFiles2);
        attachmentFiles1.setId(null);
        assertThat(attachmentFiles1).isNotEqualTo(attachmentFiles2);
    }
}
