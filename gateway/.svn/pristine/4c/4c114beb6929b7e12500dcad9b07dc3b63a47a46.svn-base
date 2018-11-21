package com.nspl.app.web.rest;

import com.nspl.app.AgreeGatewayV1App;
import com.nspl.app.domain.UserRoleAssignment;
import com.nspl.app.repository.UserRoleAssignmentRepository;
import com.nspl.app.service.UserService;
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
 * Test class for the UserRoleAssignmentResource REST controller.
 *
 * @see UserRoleAssignmentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeGatewayV1App.class)
public class UserRoleAssignmentResourceIntTest {

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final Long DEFAULT_ROLE_ID = 1L;
    private static final Long UPDATED_ROLE_ID = 2L;

    private static final Long DEFAULT_ASSIGNED_BY = 1L;
    private static final Long UPDATED_ASSIGNED_BY = 2L;

    private static final Boolean DEFAULT_DELETE_FLAG = false;
    private static final Boolean UPDATED_DELETE_FLAG = true;

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_ACTIVE_FLAG = false;
    private static final Boolean UPDATED_ACTIVE_FLAG = true;

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private UserRoleAssignmentRepository userRoleAssignmentRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUserRoleAssignmentMockMvc;

    private UserRoleAssignment userRoleAssignment;
    
    private UserService userService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserRoleAssignmentResource userRoleAssignmentResource = new UserRoleAssignmentResource(userRoleAssignmentRepository, userService);
        this.restUserRoleAssignmentMockMvc = MockMvcBuilders.standaloneSetup(userRoleAssignmentResource)
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
    public static UserRoleAssignment createEntity(EntityManager em) {
        UserRoleAssignment userRoleAssignment = new UserRoleAssignment()
            .userId(DEFAULT_USER_ID)
            .roleId(DEFAULT_ROLE_ID)
            .assignedBy(DEFAULT_ASSIGNED_BY)
            .deleteFlag(DEFAULT_DELETE_FLAG)
            .createdBy(DEFAULT_CREATED_BY)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .creationDate(DEFAULT_CREATION_DATE)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE)
            .activeFlag(DEFAULT_ACTIVE_FLAG)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE);
        return userRoleAssignment;
    }

    @Before
    public void initTest() {
        userRoleAssignment = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserRoleAssignment() throws Exception {
        int databaseSizeBeforeCreate = userRoleAssignmentRepository.findAll().size();

        // Create the UserRoleAssignment
        restUserRoleAssignmentMockMvc.perform(post("/api/user-role-assignments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userRoleAssignment)))
            .andExpect(status().isCreated());

        // Validate the UserRoleAssignment in the database
        List<UserRoleAssignment> userRoleAssignmentList = userRoleAssignmentRepository.findAll();
        assertThat(userRoleAssignmentList).hasSize(databaseSizeBeforeCreate + 1);
        UserRoleAssignment testUserRoleAssignment = userRoleAssignmentList.get(userRoleAssignmentList.size() - 1);
        assertThat(testUserRoleAssignment.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testUserRoleAssignment.getRoleId()).isEqualTo(DEFAULT_ROLE_ID);
        assertThat(testUserRoleAssignment.getAssignedBy()).isEqualTo(DEFAULT_ASSIGNED_BY);
        assertThat(testUserRoleAssignment.isDeleteFlag()).isEqualTo(DEFAULT_DELETE_FLAG);
        assertThat(testUserRoleAssignment.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testUserRoleAssignment.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testUserRoleAssignment.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testUserRoleAssignment.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);
        assertThat(testUserRoleAssignment.isActiveFlag()).isEqualTo(DEFAULT_ACTIVE_FLAG);
        assertThat(testUserRoleAssignment.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testUserRoleAssignment.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    public void createUserRoleAssignmentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userRoleAssignmentRepository.findAll().size();

        // Create the UserRoleAssignment with an existing ID
        userRoleAssignment.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserRoleAssignmentMockMvc.perform(post("/api/user-role-assignments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userRoleAssignment)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<UserRoleAssignment> userRoleAssignmentList = userRoleAssignmentRepository.findAll();
        assertThat(userRoleAssignmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllUserRoleAssignments() throws Exception {
        // Initialize the database
        userRoleAssignmentRepository.saveAndFlush(userRoleAssignment);

        // Get all the userRoleAssignmentList
        restUserRoleAssignmentMockMvc.perform(get("/api/user-role-assignments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userRoleAssignment.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].roleId").value(hasItem(DEFAULT_ROLE_ID.intValue())))
            .andExpect(jsonPath("$.[*].assignedBy").value(hasItem(DEFAULT_ASSIGNED_BY.intValue())))
            .andExpect(jsonPath("$.[*].deleteFlag").value(hasItem(DEFAULT_DELETE_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))))
            .andExpect(jsonPath("$.[*].activeFlag").value(hasItem(DEFAULT_ACTIVE_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }

    @Test
    @Transactional
    public void getUserRoleAssignment() throws Exception {
        // Initialize the database
        userRoleAssignmentRepository.saveAndFlush(userRoleAssignment);

        // Get the userRoleAssignment
        restUserRoleAssignmentMockMvc.perform(get("/api/user-role-assignments/{id}", userRoleAssignment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userRoleAssignment.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.roleId").value(DEFAULT_ROLE_ID.intValue()))
            .andExpect(jsonPath("$.assignedBy").value(DEFAULT_ASSIGNED_BY.intValue()))
            .andExpect(jsonPath("$.deleteFlag").value(DEFAULT_DELETE_FLAG.booleanValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.creationDate").value(sameInstant(DEFAULT_CREATION_DATE)))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)))
            .andExpect(jsonPath("$.activeFlag").value(DEFAULT_ACTIVE_FLAG.booleanValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserRoleAssignment() throws Exception {
        // Get the userRoleAssignment
        restUserRoleAssignmentMockMvc.perform(get("/api/user-role-assignments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserRoleAssignment() throws Exception {
        // Initialize the database
        userRoleAssignmentRepository.saveAndFlush(userRoleAssignment);
        int databaseSizeBeforeUpdate = userRoleAssignmentRepository.findAll().size();

        // Update the userRoleAssignment
        UserRoleAssignment updatedUserRoleAssignment = userRoleAssignmentRepository.findOne(userRoleAssignment.getId());
        updatedUserRoleAssignment
            .userId(UPDATED_USER_ID)
            .roleId(UPDATED_ROLE_ID)
            .assignedBy(UPDATED_ASSIGNED_BY)
            .deleteFlag(UPDATED_DELETE_FLAG)
            .createdBy(UPDATED_CREATED_BY)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE)
            .activeFlag(UPDATED_ACTIVE_FLAG)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);

        restUserRoleAssignmentMockMvc.perform(put("/api/user-role-assignments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserRoleAssignment)))
            .andExpect(status().isOk());

        // Validate the UserRoleAssignment in the database
        List<UserRoleAssignment> userRoleAssignmentList = userRoleAssignmentRepository.findAll();
        assertThat(userRoleAssignmentList).hasSize(databaseSizeBeforeUpdate);
        UserRoleAssignment testUserRoleAssignment = userRoleAssignmentList.get(userRoleAssignmentList.size() - 1);
        assertThat(testUserRoleAssignment.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testUserRoleAssignment.getRoleId()).isEqualTo(UPDATED_ROLE_ID);
        assertThat(testUserRoleAssignment.getAssignedBy()).isEqualTo(UPDATED_ASSIGNED_BY);
        assertThat(testUserRoleAssignment.isDeleteFlag()).isEqualTo(UPDATED_DELETE_FLAG);
        assertThat(testUserRoleAssignment.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUserRoleAssignment.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testUserRoleAssignment.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testUserRoleAssignment.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);
        assertThat(testUserRoleAssignment.isActiveFlag()).isEqualTo(UPDATED_ACTIVE_FLAG);
        assertThat(testUserRoleAssignment.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testUserRoleAssignment.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingUserRoleAssignment() throws Exception {
        int databaseSizeBeforeUpdate = userRoleAssignmentRepository.findAll().size();

        // Create the UserRoleAssignment

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUserRoleAssignmentMockMvc.perform(put("/api/user-role-assignments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userRoleAssignment)))
            .andExpect(status().isCreated());

        // Validate the UserRoleAssignment in the database
        List<UserRoleAssignment> userRoleAssignmentList = userRoleAssignmentRepository.findAll();
        assertThat(userRoleAssignmentList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteUserRoleAssignment() throws Exception {
        // Initialize the database
        userRoleAssignmentRepository.saveAndFlush(userRoleAssignment);
        int databaseSizeBeforeDelete = userRoleAssignmentRepository.findAll().size();

        // Get the userRoleAssignment
        restUserRoleAssignmentMockMvc.perform(delete("/api/user-role-assignments/{id}", userRoleAssignment.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserRoleAssignment> userRoleAssignmentList = userRoleAssignmentRepository.findAll();
        assertThat(userRoleAssignmentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserRoleAssignment.class);
        UserRoleAssignment userRoleAssignment1 = new UserRoleAssignment();
        userRoleAssignment1.setId(1L);
        UserRoleAssignment userRoleAssignment2 = new UserRoleAssignment();
        userRoleAssignment2.setId(userRoleAssignment1.getId());
        assertThat(userRoleAssignment1).isEqualTo(userRoleAssignment2);
        userRoleAssignment2.setId(2L);
        assertThat(userRoleAssignment1).isNotEqualTo(userRoleAssignment2);
        userRoleAssignment1.setId(null);
        assertThat(userRoleAssignment1).isNotEqualTo(userRoleAssignment2);
    }
}
