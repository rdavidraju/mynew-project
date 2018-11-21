package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.NotificationBatch;
import com.nspl.app.repository.NotificationBatchRepository;
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
 * Test class for the NotificationBatchResource REST controller.
 *
 * @see NotificationBatchResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class NotificationBatchResourceIntTest {

    private static final String DEFAULT_NOTIFICATION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NOTIFICATION_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_CURRENT_APPROVER = 1L;
    private static final Long UPDATED_CURRENT_APPROVER = 2L;

    private static final Long DEFAULT_PARENT_BATCH = 1L;
    private static final Long UPDATED_PARENT_BATCH = 2L;

    private static final Integer DEFAULT_REF_LEVEL = 1;
    private static final Integer UPDATED_REF_LEVEL = 2;

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private NotificationBatchRepository notificationBatchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restNotificationBatchMockMvc;

    private NotificationBatch notificationBatch;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        NotificationBatchResource notificationBatchResource = new NotificationBatchResource(notificationBatchRepository);
        this.restNotificationBatchMockMvc = MockMvcBuilders.standaloneSetup(notificationBatchResource)
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
    public static NotificationBatch createEntity(EntityManager em) {
        NotificationBatch notificationBatch = new NotificationBatch()
            .notificationName(DEFAULT_NOTIFICATION_NAME)
            .currentApprover(DEFAULT_CURRENT_APPROVER)
            .parentBatch(DEFAULT_PARENT_BATCH)
            .refLevel(DEFAULT_REF_LEVEL)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE);
        return notificationBatch;
    }

    @Before
    public void initTest() {
        notificationBatch = createEntity(em);
    }

    @Test
    @Transactional
    public void createNotificationBatch() throws Exception {
        int databaseSizeBeforeCreate = notificationBatchRepository.findAll().size();

        // Create the NotificationBatch
        restNotificationBatchMockMvc.perform(post("/api/notification-batches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notificationBatch)))
            .andExpect(status().isCreated());

        // Validate the NotificationBatch in the database
        List<NotificationBatch> notificationBatchList = notificationBatchRepository.findAll();
        assertThat(notificationBatchList).hasSize(databaseSizeBeforeCreate + 1);
        NotificationBatch testNotificationBatch = notificationBatchList.get(notificationBatchList.size() - 1);
        assertThat(testNotificationBatch.getNotificationName()).isEqualTo(DEFAULT_NOTIFICATION_NAME);
        assertThat(testNotificationBatch.getCurrentApprover()).isEqualTo(DEFAULT_CURRENT_APPROVER);
        assertThat(testNotificationBatch.getParentBatch()).isEqualTo(DEFAULT_PARENT_BATCH);
        assertThat(testNotificationBatch.getRefLevel()).isEqualTo(DEFAULT_REF_LEVEL);
        assertThat(testNotificationBatch.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testNotificationBatch.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testNotificationBatch.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testNotificationBatch.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void createNotificationBatchWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = notificationBatchRepository.findAll().size();

        // Create the NotificationBatch with an existing ID
        notificationBatch.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotificationBatchMockMvc.perform(post("/api/notification-batches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notificationBatch)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<NotificationBatch> notificationBatchList = notificationBatchRepository.findAll();
        assertThat(notificationBatchList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllNotificationBatches() throws Exception {
        // Initialize the database
        notificationBatchRepository.saveAndFlush(notificationBatch);

        // Get all the notificationBatchList
        restNotificationBatchMockMvc.perform(get("/api/notification-batches?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificationBatch.getId().intValue())))
            .andExpect(jsonPath("$.[*].notificationName").value(hasItem(DEFAULT_NOTIFICATION_NAME.toString())))
            .andExpect(jsonPath("$.[*].currentApprover").value(hasItem(DEFAULT_CURRENT_APPROVER.intValue())))
            .andExpect(jsonPath("$.[*].parentBatch").value(hasItem(DEFAULT_PARENT_BATCH.intValue())))
            .andExpect(jsonPath("$.[*].refLevel").value(hasItem(DEFAULT_REF_LEVEL)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void getNotificationBatch() throws Exception {
        // Initialize the database
        notificationBatchRepository.saveAndFlush(notificationBatch);

        // Get the notificationBatch
        restNotificationBatchMockMvc.perform(get("/api/notification-batches/{id}", notificationBatch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(notificationBatch.getId().intValue()))
            .andExpect(jsonPath("$.notificationName").value(DEFAULT_NOTIFICATION_NAME.toString()))
            .andExpect(jsonPath("$.currentApprover").value(DEFAULT_CURRENT_APPROVER.intValue()))
            .andExpect(jsonPath("$.parentBatch").value(DEFAULT_PARENT_BATCH.intValue()))
            .andExpect(jsonPath("$.refLevel").value(DEFAULT_REF_LEVEL))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingNotificationBatch() throws Exception {
        // Get the notificationBatch
        restNotificationBatchMockMvc.perform(get("/api/notification-batches/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNotificationBatch() throws Exception {
        // Initialize the database
        notificationBatchRepository.saveAndFlush(notificationBatch);
        int databaseSizeBeforeUpdate = notificationBatchRepository.findAll().size();

        // Update the notificationBatch
        NotificationBatch updatedNotificationBatch = notificationBatchRepository.findOne(notificationBatch.getId());
        updatedNotificationBatch
            .notificationName(UPDATED_NOTIFICATION_NAME)
            .currentApprover(UPDATED_CURRENT_APPROVER)
            .parentBatch(UPDATED_PARENT_BATCH)
            .refLevel(UPDATED_REF_LEVEL)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE);

        restNotificationBatchMockMvc.perform(put("/api/notification-batches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedNotificationBatch)))
            .andExpect(status().isOk());

        // Validate the NotificationBatch in the database
        List<NotificationBatch> notificationBatchList = notificationBatchRepository.findAll();
        assertThat(notificationBatchList).hasSize(databaseSizeBeforeUpdate);
        NotificationBatch testNotificationBatch = notificationBatchList.get(notificationBatchList.size() - 1);
        assertThat(testNotificationBatch.getNotificationName()).isEqualTo(UPDATED_NOTIFICATION_NAME);
        assertThat(testNotificationBatch.getCurrentApprover()).isEqualTo(UPDATED_CURRENT_APPROVER);
        assertThat(testNotificationBatch.getParentBatch()).isEqualTo(UPDATED_PARENT_BATCH);
        assertThat(testNotificationBatch.getRefLevel()).isEqualTo(UPDATED_REF_LEVEL);
        assertThat(testNotificationBatch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testNotificationBatch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testNotificationBatch.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testNotificationBatch.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingNotificationBatch() throws Exception {
        int databaseSizeBeforeUpdate = notificationBatchRepository.findAll().size();

        // Create the NotificationBatch

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restNotificationBatchMockMvc.perform(put("/api/notification-batches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notificationBatch)))
            .andExpect(status().isCreated());

        // Validate the NotificationBatch in the database
        List<NotificationBatch> notificationBatchList = notificationBatchRepository.findAll();
        assertThat(notificationBatchList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteNotificationBatch() throws Exception {
        // Initialize the database
        notificationBatchRepository.saveAndFlush(notificationBatch);
        int databaseSizeBeforeDelete = notificationBatchRepository.findAll().size();

        // Get the notificationBatch
        restNotificationBatchMockMvc.perform(delete("/api/notification-batches/{id}", notificationBatch.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<NotificationBatch> notificationBatchList = notificationBatchRepository.findAll();
        assertThat(notificationBatchList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NotificationBatch.class);
    }
}
