package com.nspl.app.web.rest;

import com.nspl.app.AgreeGatewayV1App;

import com.nspl.app.domain.UserComments;
import com.nspl.app.repository.UserCommentsRepository;
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
 * Test class for the UserCommentsResource REST controller.
 *
 * @see UserCommentsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeGatewayV1App.class)
public class UserCommentsResourceIntTest {

    private static final String DEFAULT_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT = "BBBBBBBBBB";

    private static final Long DEFAULT_TENANT_ID = 1L;
    private static final Long UPDATED_TENANT_ID = 2L;

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final Long DEFAULT_RECIPIENT_USER_ID = 1L;
    private static final Long UPDATED_RECIPIENT_USER_ID = 2L;

    private static final String DEFAULT_MESSAGE_BODY = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE_BODY = "BBBBBBBBBB";

    private static final Long DEFAULT_REPLIED_TO_COMMENT_ID = 1L;
    private static final Long UPDATED_REPLIED_TO_COMMENT_ID = 2L;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Boolean DEFAULT_IS_READ = false;
    private static final Boolean UPDATED_IS_READ = true;

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    @Autowired
    private UserCommentsRepository userCommentsRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUserCommentsMockMvc;

    private UserComments userComments;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserCommentsResource userCommentsResource = new UserCommentsResource(userCommentsRepository);
        this.restUserCommentsMockMvc = MockMvcBuilders.standaloneSetup(userCommentsResource)
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
    public static UserComments createEntity(EntityManager em) {
        UserComments userComments = new UserComments()
            .subject(DEFAULT_SUBJECT)
            .tenantId(DEFAULT_TENANT_ID)
            .userId(DEFAULT_USER_ID)
            .recipientUserId(DEFAULT_RECIPIENT_USER_ID)
            .messageBody(DEFAULT_MESSAGE_BODY)
            .repliedToCommentId(DEFAULT_REPLIED_TO_COMMENT_ID)
            .isActive(DEFAULT_IS_ACTIVE)
            .isRead(DEFAULT_IS_READ)
            .creationDate(DEFAULT_CREATION_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY);
        return userComments;
    }

    @Before
    public void initTest() {
        userComments = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserComments() throws Exception {
        int databaseSizeBeforeCreate = userCommentsRepository.findAll().size();

        // Create the UserComments
        restUserCommentsMockMvc.perform(post("/api/user-comments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userComments)))
            .andExpect(status().isCreated());

        // Validate the UserComments in the database
        List<UserComments> userCommentsList = userCommentsRepository.findAll();
        assertThat(userCommentsList).hasSize(databaseSizeBeforeCreate + 1);
        UserComments testUserComments = userCommentsList.get(userCommentsList.size() - 1);
        assertThat(testUserComments.getSubject()).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testUserComments.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testUserComments.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testUserComments.getRecipientUserId()).isEqualTo(DEFAULT_RECIPIENT_USER_ID);
        assertThat(testUserComments.getMessageBody()).isEqualTo(DEFAULT_MESSAGE_BODY);
        assertThat(testUserComments.getRepliedToCommentId()).isEqualTo(DEFAULT_REPLIED_TO_COMMENT_ID);
        assertThat(testUserComments.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testUserComments.isIsRead()).isEqualTo(DEFAULT_IS_READ);
        assertThat(testUserComments.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testUserComments.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testUserComments.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);
        assertThat(testUserComments.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
    }

    @Test
    @Transactional
    public void createUserCommentsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userCommentsRepository.findAll().size();

        // Create the UserComments with an existing ID
        userComments.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserCommentsMockMvc.perform(post("/api/user-comments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userComments)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<UserComments> userCommentsList = userCommentsRepository.findAll();
        assertThat(userCommentsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllUserComments() throws Exception {
        // Initialize the database
        userCommentsRepository.saveAndFlush(userComments);

        // Get all the userCommentsList
        restUserCommentsMockMvc.perform(get("/api/user-comments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userComments.getId().intValue())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT.toString())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].recipientUserId").value(hasItem(DEFAULT_RECIPIENT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].messageBody").value(hasItem(DEFAULT_MESSAGE_BODY.toString())))
            .andExpect(jsonPath("$.[*].repliedToCommentId").value(hasItem(DEFAULT_REPLIED_TO_COMMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].isRead").value(hasItem(DEFAULT_IS_READ.booleanValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())));
    }

    @Test
    @Transactional
    public void getUserComments() throws Exception {
        // Initialize the database
        userCommentsRepository.saveAndFlush(userComments);

        // Get the userComments
        restUserCommentsMockMvc.perform(get("/api/user-comments/{id}", userComments.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userComments.getId().intValue()))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT.toString()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID.intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.recipientUserId").value(DEFAULT_RECIPIENT_USER_ID.intValue()))
            .andExpect(jsonPath("$.messageBody").value(DEFAULT_MESSAGE_BODY.toString()))
            .andExpect(jsonPath("$.repliedToCommentId").value(DEFAULT_REPLIED_TO_COMMENT_ID.intValue()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.isRead").value(DEFAULT_IS_READ.booleanValue()))
            .andExpect(jsonPath("$.creationDate").value(sameInstant(DEFAULT_CREATION_DATE)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingUserComments() throws Exception {
        // Get the userComments
        restUserCommentsMockMvc.perform(get("/api/user-comments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserComments() throws Exception {
        // Initialize the database
        userCommentsRepository.saveAndFlush(userComments);
        int databaseSizeBeforeUpdate = userCommentsRepository.findAll().size();

        // Update the userComments
        UserComments updatedUserComments = userCommentsRepository.findOne(userComments.getId());
        updatedUserComments
            .subject(UPDATED_SUBJECT)
            .tenantId(UPDATED_TENANT_ID)
            .userId(UPDATED_USER_ID)
            .recipientUserId(UPDATED_RECIPIENT_USER_ID)
            .messageBody(UPDATED_MESSAGE_BODY)
            .repliedToCommentId(UPDATED_REPLIED_TO_COMMENT_ID)
            .isActive(UPDATED_IS_ACTIVE)
            .isRead(UPDATED_IS_READ)
            .creationDate(UPDATED_CREATION_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY);

        restUserCommentsMockMvc.perform(put("/api/user-comments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserComments)))
            .andExpect(status().isOk());

        // Validate the UserComments in the database
        List<UserComments> userCommentsList = userCommentsRepository.findAll();
        assertThat(userCommentsList).hasSize(databaseSizeBeforeUpdate);
        UserComments testUserComments = userCommentsList.get(userCommentsList.size() - 1);
        assertThat(testUserComments.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testUserComments.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testUserComments.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testUserComments.getRecipientUserId()).isEqualTo(UPDATED_RECIPIENT_USER_ID);
        assertThat(testUserComments.getMessageBody()).isEqualTo(UPDATED_MESSAGE_BODY);
        assertThat(testUserComments.getRepliedToCommentId()).isEqualTo(UPDATED_REPLIED_TO_COMMENT_ID);
        assertThat(testUserComments.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testUserComments.isIsRead()).isEqualTo(UPDATED_IS_READ);
        assertThat(testUserComments.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testUserComments.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUserComments.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);
        assertThat(testUserComments.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
    }

    @Test
    @Transactional
    public void updateNonExistingUserComments() throws Exception {
        int databaseSizeBeforeUpdate = userCommentsRepository.findAll().size();

        // Create the UserComments

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUserCommentsMockMvc.perform(put("/api/user-comments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userComments)))
            .andExpect(status().isCreated());

        // Validate the UserComments in the database
        List<UserComments> userCommentsList = userCommentsRepository.findAll();
        assertThat(userCommentsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteUserComments() throws Exception {
        // Initialize the database
        userCommentsRepository.saveAndFlush(userComments);
        int databaseSizeBeforeDelete = userCommentsRepository.findAll().size();

        // Get the userComments
        restUserCommentsMockMvc.perform(delete("/api/user-comments/{id}", userComments.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserComments> userCommentsList = userCommentsRepository.findAll();
        assertThat(userCommentsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserComments.class);
        UserComments userComments1 = new UserComments();
        userComments1.setId(1L);
        UserComments userComments2 = new UserComments();
        userComments2.setId(userComments1.getId());
        assertThat(userComments1).isEqualTo(userComments2);
        userComments2.setId(2L);
        assertThat(userComments1).isNotEqualTo(userComments2);
        userComments1.setId(null);
        assertThat(userComments1).isNotEqualTo(userComments2);
    }
}
