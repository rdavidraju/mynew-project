package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.AttachmentList;
import com.nspl.app.repository.AttachmentListRepository;
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
 * Test class for the AttachmentListResource REST controller.
 *
 * @see AttachmentListResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class AttachmentListResourceIntTest {

    private static final String DEFAULT_ATTACHMENT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_ATTACHMENT_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_ATTACHMENT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_ATTACHMENT_CATEGORY = "BBBBBBBBBB";

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private AttachmentListRepository attachmentListRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAttachmentListMockMvc;

    private AttachmentList attachmentList;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AttachmentListResource attachmentListResource = new AttachmentListResource(attachmentListRepository);
        this.restAttachmentListMockMvc = MockMvcBuilders.standaloneSetup(attachmentListResource)
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
    public static AttachmentList createEntity(EntityManager em) {
        AttachmentList attachmentList = new AttachmentList()
            .attachmentKey(DEFAULT_ATTACHMENT_KEY)
            .attachmentCategory(DEFAULT_ATTACHMENT_CATEGORY)
            .createdBy(DEFAULT_CREATED_BY)
            .creationDate(DEFAULT_CREATION_DATE)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE);
        return attachmentList;
    }

    @Before
    public void initTest() {
        attachmentList = createEntity(em);
    }

    @Test
    @Transactional
    public void createAttachmentList() throws Exception {
        int databaseSizeBeforeCreate = attachmentListRepository.findAll().size();

        // Create the AttachmentList
        restAttachmentListMockMvc.perform(post("/api/attachment-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attachmentList)))
            .andExpect(status().isCreated());

        // Validate the AttachmentList in the database
        List<AttachmentList> attachmentListList = attachmentListRepository.findAll();
        assertThat(attachmentListList).hasSize(databaseSizeBeforeCreate + 1);
        AttachmentList testAttachmentList = attachmentListList.get(attachmentListList.size() - 1);
        assertThat(testAttachmentList.getAttachmentKey()).isEqualTo(DEFAULT_ATTACHMENT_KEY);
        assertThat(testAttachmentList.getAttachmentCategory()).isEqualTo(DEFAULT_ATTACHMENT_CATEGORY);
        assertThat(testAttachmentList.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testAttachmentList.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testAttachmentList.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testAttachmentList.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void createAttachmentListWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = attachmentListRepository.findAll().size();

        // Create the AttachmentList with an existing ID
        attachmentList.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttachmentListMockMvc.perform(post("/api/attachment-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attachmentList)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<AttachmentList> attachmentListList = attachmentListRepository.findAll();
        assertThat(attachmentListList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAttachmentLists() throws Exception {
        // Initialize the database
        attachmentListRepository.saveAndFlush(attachmentList);

        // Get all the attachmentListList
        restAttachmentListMockMvc.perform(get("/api/attachment-lists?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attachmentList.getId().intValue())))
            .andExpect(jsonPath("$.[*].attachmentKey").value(hasItem(DEFAULT_ATTACHMENT_KEY.toString())))
            .andExpect(jsonPath("$.[*].attachmentCategory").value(hasItem(DEFAULT_ATTACHMENT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void getAttachmentList() throws Exception {
        // Initialize the database
        attachmentListRepository.saveAndFlush(attachmentList);

        // Get the attachmentList
        restAttachmentListMockMvc.perform(get("/api/attachment-lists/{id}", attachmentList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(attachmentList.getId().intValue()))
            .andExpect(jsonPath("$.attachmentKey").value(DEFAULT_ATTACHMENT_KEY.toString()))
            .andExpect(jsonPath("$.attachmentCategory").value(DEFAULT_ATTACHMENT_CATEGORY.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.creationDate").value(sameInstant(DEFAULT_CREATION_DATE)))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingAttachmentList() throws Exception {
        // Get the attachmentList
        restAttachmentListMockMvc.perform(get("/api/attachment-lists/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAttachmentList() throws Exception {
        // Initialize the database
        attachmentListRepository.saveAndFlush(attachmentList);
        int databaseSizeBeforeUpdate = attachmentListRepository.findAll().size();

        // Update the attachmentList
        AttachmentList updatedAttachmentList = attachmentListRepository.findOne(attachmentList.getId());
        updatedAttachmentList
            .attachmentKey(UPDATED_ATTACHMENT_KEY)
            .attachmentCategory(UPDATED_ATTACHMENT_CATEGORY)
            .createdBy(UPDATED_CREATED_BY)
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE);

        restAttachmentListMockMvc.perform(put("/api/attachment-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAttachmentList)))
            .andExpect(status().isOk());

        // Validate the AttachmentList in the database
        List<AttachmentList> attachmentListList = attachmentListRepository.findAll();
        assertThat(attachmentListList).hasSize(databaseSizeBeforeUpdate);
        AttachmentList testAttachmentList = attachmentListList.get(attachmentListList.size() - 1);
        assertThat(testAttachmentList.getAttachmentKey()).isEqualTo(UPDATED_ATTACHMENT_KEY);
        assertThat(testAttachmentList.getAttachmentCategory()).isEqualTo(UPDATED_ATTACHMENT_CATEGORY);
        assertThat(testAttachmentList.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAttachmentList.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testAttachmentList.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testAttachmentList.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingAttachmentList() throws Exception {
        int databaseSizeBeforeUpdate = attachmentListRepository.findAll().size();

        // Create the AttachmentList

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAttachmentListMockMvc.perform(put("/api/attachment-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attachmentList)))
            .andExpect(status().isCreated());

        // Validate the AttachmentList in the database
        List<AttachmentList> attachmentListList = attachmentListRepository.findAll();
        assertThat(attachmentListList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAttachmentList() throws Exception {
        // Initialize the database
        attachmentListRepository.saveAndFlush(attachmentList);
        int databaseSizeBeforeDelete = attachmentListRepository.findAll().size();

        // Get the attachmentList
        restAttachmentListMockMvc.perform(delete("/api/attachment-lists/{id}", attachmentList.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AttachmentList> attachmentListList = attachmentListRepository.findAll();
        assertThat(attachmentListList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AttachmentList.class);
        AttachmentList attachmentList1 = new AttachmentList();
        attachmentList1.setId(1L);
        AttachmentList attachmentList2 = new AttachmentList();
        attachmentList2.setId(attachmentList1.getId());
        assertThat(attachmentList1).isEqualTo(attachmentList2);
        attachmentList2.setId(2L);
        assertThat(attachmentList1).isNotEqualTo(attachmentList2);
        attachmentList1.setId(null);
        assertThat(attachmentList1).isNotEqualTo(attachmentList2);
    }
}
