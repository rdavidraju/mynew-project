package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.MappingSet;
import com.nspl.app.repository.MappingSetRepository;
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
 * Test class for the MappingSetResource REST controller.
 *
 * @see MappingSetResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class MappingSetResourceIntTest {

    private static final Long DEFAULT_TENANT_ID = 1L;
    private static final Long UPDATED_TENANT_ID = 2L;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private MappingSetRepository mappingSetRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMappingSetMockMvc;

    private MappingSet mappingSet;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MappingSetResource mappingSetResource = new MappingSetResource(mappingSetRepository);
        this.restMappingSetMockMvc = MockMvcBuilders.standaloneSetup(mappingSetResource)
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
    public static MappingSet createEntity(EntityManager em) {
        MappingSet mappingSet = new MappingSet()
            .tenantId(DEFAULT_TENANT_ID)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .createdBy(DEFAULT_CREATED_BY)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE);
        return mappingSet;
    }

    @Before
    public void initTest() {
        mappingSet = createEntity(em);
    }

    @Test
    @Transactional
    public void createMappingSet() throws Exception {
        int databaseSizeBeforeCreate = mappingSetRepository.findAll().size();

        // Create the MappingSet
        restMappingSetMockMvc.perform(post("/api/mapping-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mappingSet)))
            .andExpect(status().isCreated());

        // Validate the MappingSet in the database
        List<MappingSet> mappingSetList = mappingSetRepository.findAll();
        assertThat(mappingSetList).hasSize(databaseSizeBeforeCreate + 1);
        MappingSet testMappingSet = mappingSetList.get(mappingSetList.size() - 1);
        assertThat(testMappingSet.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testMappingSet.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMappingSet.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMappingSet.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testMappingSet.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testMappingSet.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testMappingSet.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void createMappingSetWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = mappingSetRepository.findAll().size();

        // Create the MappingSet with an existing ID
        mappingSet.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMappingSetMockMvc.perform(post("/api/mapping-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mappingSet)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<MappingSet> mappingSetList = mappingSetRepository.findAll();
        assertThat(mappingSetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMappingSets() throws Exception {
        // Initialize the database
        mappingSetRepository.saveAndFlush(mappingSet);

        // Get all the mappingSetList
        restMappingSetMockMvc.perform(get("/api/mapping-sets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mappingSet.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void getMappingSet() throws Exception {
        // Initialize the database
        mappingSetRepository.saveAndFlush(mappingSet);

        // Get the mappingSet
        restMappingSetMockMvc.perform(get("/api/mapping-sets/{id}", mappingSet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(mappingSet.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingMappingSet() throws Exception {
        // Get the mappingSet
        restMappingSetMockMvc.perform(get("/api/mapping-sets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMappingSet() throws Exception {
        // Initialize the database
        mappingSetRepository.saveAndFlush(mappingSet);
        int databaseSizeBeforeUpdate = mappingSetRepository.findAll().size();

        // Update the mappingSet
        MappingSet updatedMappingSet = mappingSetRepository.findOne(mappingSet.getId());
        updatedMappingSet
            .tenantId(UPDATED_TENANT_ID)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdBy(UPDATED_CREATED_BY)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE);

        restMappingSetMockMvc.perform(put("/api/mapping-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMappingSet)))
            .andExpect(status().isOk());

        // Validate the MappingSet in the database
        List<MappingSet> mappingSetList = mappingSetRepository.findAll();
        assertThat(mappingSetList).hasSize(databaseSizeBeforeUpdate);
        MappingSet testMappingSet = mappingSetList.get(mappingSetList.size() - 1);
        assertThat(testMappingSet.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testMappingSet.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMappingSet.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMappingSet.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testMappingSet.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testMappingSet.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testMappingSet.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingMappingSet() throws Exception {
        int databaseSizeBeforeUpdate = mappingSetRepository.findAll().size();

        // Create the MappingSet

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMappingSetMockMvc.perform(put("/api/mapping-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mappingSet)))
            .andExpect(status().isCreated());

        // Validate the MappingSet in the database
        List<MappingSet> mappingSetList = mappingSetRepository.findAll();
        assertThat(mappingSetList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMappingSet() throws Exception {
        // Initialize the database
        mappingSetRepository.saveAndFlush(mappingSet);
        int databaseSizeBeforeDelete = mappingSetRepository.findAll().size();

        // Get the mappingSet
        restMappingSetMockMvc.perform(delete("/api/mapping-sets/{id}", mappingSet.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<MappingSet> mappingSetList = mappingSetRepository.findAll();
        assertThat(mappingSetList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MappingSet.class);
    }
}
