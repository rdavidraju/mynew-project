package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.TemplAttributeMapping;
import com.nspl.app.repository.TemplAttributeMappingRepository;
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
 * Test class for the TemplAttributeMappingResource REST controller.
 *
 * @see TemplAttributeMappingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class TemplAttributeMappingResourceIntTest {

    private static final Long DEFAULT_JE_TEMP_ID = 1L;
    private static final Long UPDATED_JE_TEMP_ID = 2L;

    private static final String DEFAULT_ATTRIBUTE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ATTRIBUTE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MAPPING_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_MAPPING_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final Long DEFAULT_SOURCE_VIEW_COLUMN_ID = 1L;
    private static final Long UPDATED_SOURCE_VIEW_COLUMN_ID = 2L;

    private static final Long DEFAULT_CRETAED_BY = 1L;
    private static final Long UPDATED_CRETAED_BY = 2L;

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private TemplAttributeMappingRepository templAttributeMappingRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTemplAttributeMappingMockMvc;

    private TemplAttributeMapping templAttributeMapping;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TemplAttributeMappingResource templAttributeMappingResource = new TemplAttributeMappingResource(templAttributeMappingRepository);
        this.restTemplAttributeMappingMockMvc = MockMvcBuilders.standaloneSetup(templAttributeMappingResource)
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
    public static TemplAttributeMapping createEntity(EntityManager em) {
        TemplAttributeMapping templAttributeMapping = new TemplAttributeMapping()
            .jeTempId(DEFAULT_JE_TEMP_ID)
            .attributeName(DEFAULT_ATTRIBUTE_NAME)
            .mappingType(DEFAULT_MAPPING_TYPE)
            .value(DEFAULT_VALUE)
            .sourceViewColumnId(DEFAULT_SOURCE_VIEW_COLUMN_ID)
            .cretaedBy(DEFAULT_CRETAED_BY)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE);
        return templAttributeMapping;
    }

    @Before
    public void initTest() {
        templAttributeMapping = createEntity(em);
    }

    @Test
    @Transactional
    public void createTemplAttributeMapping() throws Exception {
        int databaseSizeBeforeCreate = templAttributeMappingRepository.findAll().size();

        // Create the TemplAttributeMapping
        restTemplAttributeMappingMockMvc.perform(post("/api/templ-attribute-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(templAttributeMapping)))
            .andExpect(status().isCreated());

        // Validate the TemplAttributeMapping in the database
        List<TemplAttributeMapping> templAttributeMappingList = templAttributeMappingRepository.findAll();
        assertThat(templAttributeMappingList).hasSize(databaseSizeBeforeCreate + 1);
        TemplAttributeMapping testTemplAttributeMapping = templAttributeMappingList.get(templAttributeMappingList.size() - 1);
        assertThat(testTemplAttributeMapping.getJeTempId()).isEqualTo(DEFAULT_JE_TEMP_ID);
        assertThat(testTemplAttributeMapping.getAttributeName()).isEqualTo(DEFAULT_ATTRIBUTE_NAME);
        assertThat(testTemplAttributeMapping.getMappingType()).isEqualTo(DEFAULT_MAPPING_TYPE);
        assertThat(testTemplAttributeMapping.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testTemplAttributeMapping.getSourceViewColumnId()).isEqualTo(DEFAULT_SOURCE_VIEW_COLUMN_ID);
        assertThat(testTemplAttributeMapping.getCretaedBy()).isEqualTo(DEFAULT_CRETAED_BY);
        assertThat(testTemplAttributeMapping.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testTemplAttributeMapping.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testTemplAttributeMapping.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void createTemplAttributeMappingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = templAttributeMappingRepository.findAll().size();

        // Create the TemplAttributeMapping with an existing ID
        templAttributeMapping.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTemplAttributeMappingMockMvc.perform(post("/api/templ-attribute-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(templAttributeMapping)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<TemplAttributeMapping> templAttributeMappingList = templAttributeMappingRepository.findAll();
        assertThat(templAttributeMappingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTemplAttributeMappings() throws Exception {
        // Initialize the database
        templAttributeMappingRepository.saveAndFlush(templAttributeMapping);

        // Get all the templAttributeMappingList
        restTemplAttributeMappingMockMvc.perform(get("/api/templ-attribute-mappings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(templAttributeMapping.getId().intValue())))
            .andExpect(jsonPath("$.[*].jeTempId").value(hasItem(DEFAULT_JE_TEMP_ID.intValue())))
            .andExpect(jsonPath("$.[*].attributeName").value(hasItem(DEFAULT_ATTRIBUTE_NAME.toString())))
            .andExpect(jsonPath("$.[*].mappingType").value(hasItem(DEFAULT_MAPPING_TYPE.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].sourceViewColumnId").value(hasItem(DEFAULT_SOURCE_VIEW_COLUMN_ID.intValue())))
            .andExpect(jsonPath("$.[*].cretaedBy").value(hasItem(DEFAULT_CRETAED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void getTemplAttributeMapping() throws Exception {
        // Initialize the database
        templAttributeMappingRepository.saveAndFlush(templAttributeMapping);

        // Get the templAttributeMapping
        restTemplAttributeMappingMockMvc.perform(get("/api/templ-attribute-mappings/{id}", templAttributeMapping.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(templAttributeMapping.getId().intValue()))
            .andExpect(jsonPath("$.jeTempId").value(DEFAULT_JE_TEMP_ID.intValue()))
            .andExpect(jsonPath("$.attributeName").value(DEFAULT_ATTRIBUTE_NAME.toString()))
            .andExpect(jsonPath("$.mappingType").value(DEFAULT_MAPPING_TYPE.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()))
            .andExpect(jsonPath("$.sourceViewColumnId").value(DEFAULT_SOURCE_VIEW_COLUMN_ID.intValue()))
            .andExpect(jsonPath("$.cretaedBy").value(DEFAULT_CRETAED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingTemplAttributeMapping() throws Exception {
        // Get the templAttributeMapping
        restTemplAttributeMappingMockMvc.perform(get("/api/templ-attribute-mappings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTemplAttributeMapping() throws Exception {
        // Initialize the database
        templAttributeMappingRepository.saveAndFlush(templAttributeMapping);
        int databaseSizeBeforeUpdate = templAttributeMappingRepository.findAll().size();

        // Update the templAttributeMapping
        TemplAttributeMapping updatedTemplAttributeMapping = templAttributeMappingRepository.findOne(templAttributeMapping.getId());
        updatedTemplAttributeMapping
            .jeTempId(UPDATED_JE_TEMP_ID)
            .attributeName(UPDATED_ATTRIBUTE_NAME)
            .mappingType(UPDATED_MAPPING_TYPE)
            .value(UPDATED_VALUE)
            .sourceViewColumnId(UPDATED_SOURCE_VIEW_COLUMN_ID)
            .cretaedBy(UPDATED_CRETAED_BY)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE);

        restTemplAttributeMappingMockMvc.perform(put("/api/templ-attribute-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTemplAttributeMapping)))
            .andExpect(status().isOk());

        // Validate the TemplAttributeMapping in the database
        List<TemplAttributeMapping> templAttributeMappingList = templAttributeMappingRepository.findAll();
        assertThat(templAttributeMappingList).hasSize(databaseSizeBeforeUpdate);
        TemplAttributeMapping testTemplAttributeMapping = templAttributeMappingList.get(templAttributeMappingList.size() - 1);
        assertThat(testTemplAttributeMapping.getJeTempId()).isEqualTo(UPDATED_JE_TEMP_ID);
        assertThat(testTemplAttributeMapping.getAttributeName()).isEqualTo(UPDATED_ATTRIBUTE_NAME);
        assertThat(testTemplAttributeMapping.getMappingType()).isEqualTo(UPDATED_MAPPING_TYPE);
        assertThat(testTemplAttributeMapping.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testTemplAttributeMapping.getSourceViewColumnId()).isEqualTo(UPDATED_SOURCE_VIEW_COLUMN_ID);
        assertThat(testTemplAttributeMapping.getCretaedBy()).isEqualTo(UPDATED_CRETAED_BY);
        assertThat(testTemplAttributeMapping.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testTemplAttributeMapping.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testTemplAttributeMapping.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingTemplAttributeMapping() throws Exception {
        int databaseSizeBeforeUpdate = templAttributeMappingRepository.findAll().size();

        // Create the TemplAttributeMapping

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTemplAttributeMappingMockMvc.perform(put("/api/templ-attribute-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(templAttributeMapping)))
            .andExpect(status().isCreated());

        // Validate the TemplAttributeMapping in the database
        List<TemplAttributeMapping> templAttributeMappingList = templAttributeMappingRepository.findAll();
        assertThat(templAttributeMappingList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTemplAttributeMapping() throws Exception {
        // Initialize the database
        templAttributeMappingRepository.saveAndFlush(templAttributeMapping);
        int databaseSizeBeforeDelete = templAttributeMappingRepository.findAll().size();

        // Get the templAttributeMapping
        restTemplAttributeMappingMockMvc.perform(delete("/api/templ-attribute-mappings/{id}", templAttributeMapping.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TemplAttributeMapping> templAttributeMappingList = templAttributeMappingRepository.findAll();
        assertThat(templAttributeMappingList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TemplAttributeMapping.class);
    }
}
