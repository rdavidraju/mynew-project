package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;

import com.nspl.app.domain.JeLines;
import com.nspl.app.repository.JeLinesRepository;
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
import java.math.BigDecimal;
import java.util.List;

import static com.nspl.app.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the JeLinesResource REST controller.
 *
 * @see JeLinesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class JeLinesResourceIntTest {

    private static final Long DEFAULT_JE_BATCH_ID = 1L;
    private static final Long UPDATED_JE_BATCH_ID = 2L;

    private static final Long DEFAULT_ROW_ID = 1L;
    private static final Long UPDATED_ROW_ID = 2L;

    private static final Integer DEFAULT_LINE_NUM = 1;
    private static final Integer UPDATED_LINE_NUM = 2;

    private static final String DEFAULT_DESCRIPTION_ATTRIBUTE = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION_ATTRIBUTE = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_DEBIT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_DEBIT_AMOUNT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_CREDIT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_CREDIT_AMOUNT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_ACCOUNTED_DEBIT = new BigDecimal(1);
    private static final BigDecimal UPDATED_ACCOUNTED_DEBIT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_ACCOUNTED_CREDIT = new BigDecimal(1);
    private static final BigDecimal UPDATED_ACCOUNTED_CREDIT = new BigDecimal(2);

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final Long DEFAULT_LAST_UPDATED_BY = 1L;
    private static final Long UPDATED_LAST_UPDATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private JeLinesRepository jeLinesRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restJeLinesMockMvc;

    private JeLines jeLines;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        JeLinesResource jeLinesResource = new JeLinesResource(jeLinesRepository);
        this.restJeLinesMockMvc = MockMvcBuilders.standaloneSetup(jeLinesResource)
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
    public static JeLines createEntity(EntityManager em) {
        JeLines jeLines = new JeLines()
            .jeBatchId(DEFAULT_JE_BATCH_ID)
            .rowId(DEFAULT_ROW_ID)
            .lineNum(DEFAULT_LINE_NUM)
            .descriptionAttribute(DEFAULT_DESCRIPTION_ATTRIBUTE)
            .currency(DEFAULT_CURRENCY)
            .debitAmount(DEFAULT_DEBIT_AMOUNT)
            .creditAmount(DEFAULT_CREDIT_AMOUNT)
            .accountedDebit(DEFAULT_ACCOUNTED_DEBIT)
            .accountedCredit(DEFAULT_ACCOUNTED_CREDIT)
            .comments(DEFAULT_COMMENTS)
            .createdBy(DEFAULT_CREATED_BY)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE);
        return jeLines;
    }

    @Before
    public void initTest() {
        jeLines = createEntity(em);
    }

    @Test
    @Transactional
    public void createJeLines() throws Exception {
        int databaseSizeBeforeCreate = jeLinesRepository.findAll().size();

        // Create the JeLines
        restJeLinesMockMvc.perform(post("/api/je-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jeLines)))
            .andExpect(status().isCreated());

        // Validate the JeLines in the database
        List<JeLines> jeLinesList = jeLinesRepository.findAll();
        assertThat(jeLinesList).hasSize(databaseSizeBeforeCreate + 1);
        JeLines testJeLines = jeLinesList.get(jeLinesList.size() - 1);
        assertThat(testJeLines.getJeBatchId()).isEqualTo(DEFAULT_JE_BATCH_ID);
        assertThat(testJeLines.getRowId()).isEqualTo(DEFAULT_ROW_ID);
        assertThat(testJeLines.getLineNum()).isEqualTo(DEFAULT_LINE_NUM);
        assertThat(testJeLines.getDescriptionAttribute()).isEqualTo(DEFAULT_DESCRIPTION_ATTRIBUTE);
        assertThat(testJeLines.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testJeLines.getDebitAmount()).isEqualTo(DEFAULT_DEBIT_AMOUNT);
        assertThat(testJeLines.getCreditAmount()).isEqualTo(DEFAULT_CREDIT_AMOUNT);
        assertThat(testJeLines.getAccountedDebit()).isEqualTo(DEFAULT_ACCOUNTED_DEBIT);
        assertThat(testJeLines.getAccountedCredit()).isEqualTo(DEFAULT_ACCOUNTED_CREDIT);
        assertThat(testJeLines.getComments()).isEqualTo(DEFAULT_COMMENTS);
        assertThat(testJeLines.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testJeLines.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testJeLines.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testJeLines.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void createJeLinesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jeLinesRepository.findAll().size();

        // Create the JeLines with an existing ID
        jeLines.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJeLinesMockMvc.perform(post("/api/je-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jeLines)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<JeLines> jeLinesList = jeLinesRepository.findAll();
        assertThat(jeLinesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllJeLines() throws Exception {
        // Initialize the database
        jeLinesRepository.saveAndFlush(jeLines);

        // Get all the jeLinesList
        restJeLinesMockMvc.perform(get("/api/je-lines?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jeLines.getId().intValue())))
            .andExpect(jsonPath("$.[*].jeBatchId").value(hasItem(DEFAULT_JE_BATCH_ID.intValue())))
            .andExpect(jsonPath("$.[*].rowId").value(hasItem(DEFAULT_ROW_ID.intValue())))
            .andExpect(jsonPath("$.[*].lineNum").value(hasItem(DEFAULT_LINE_NUM)))
            .andExpect(jsonPath("$.[*].descriptionAttribute").value(hasItem(DEFAULT_DESCRIPTION_ATTRIBUTE.toString())))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].debitAmount").value(hasItem(DEFAULT_DEBIT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].creditAmount").value(hasItem(DEFAULT_CREDIT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].accountedDebit").value(hasItem(DEFAULT_ACCOUNTED_DEBIT.intValue())))
            .andExpect(jsonPath("$.[*].accountedCredit").value(hasItem(DEFAULT_ACCOUNTED_CREDIT.intValue())))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void getJeLines() throws Exception {
        // Initialize the database
        jeLinesRepository.saveAndFlush(jeLines);

        // Get the jeLines
        restJeLinesMockMvc.perform(get("/api/je-lines/{id}", jeLines.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(jeLines.getId().intValue()))
            .andExpect(jsonPath("$.jeBatchId").value(DEFAULT_JE_BATCH_ID.intValue()))
            .andExpect(jsonPath("$.rowId").value(DEFAULT_ROW_ID.intValue()))
            .andExpect(jsonPath("$.lineNum").value(DEFAULT_LINE_NUM))
            .andExpect(jsonPath("$.descriptionAttribute").value(DEFAULT_DESCRIPTION_ATTRIBUTE.toString()))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY.toString()))
            .andExpect(jsonPath("$.debitAmount").value(DEFAULT_DEBIT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.creditAmount").value(DEFAULT_CREDIT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.accountedDebit").value(DEFAULT_ACCOUNTED_DEBIT.intValue()))
            .andExpect(jsonPath("$.accountedCredit").value(DEFAULT_ACCOUNTED_CREDIT.intValue()))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingJeLines() throws Exception {
        // Get the jeLines
        restJeLinesMockMvc.perform(get("/api/je-lines/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJeLines() throws Exception {
        // Initialize the database
        jeLinesRepository.saveAndFlush(jeLines);
        int databaseSizeBeforeUpdate = jeLinesRepository.findAll().size();

        // Update the jeLines
        JeLines updatedJeLines = jeLinesRepository.findOne(jeLines.getId());
        updatedJeLines
            .jeBatchId(UPDATED_JE_BATCH_ID)
            .rowId(UPDATED_ROW_ID)
            .lineNum(UPDATED_LINE_NUM)
            .descriptionAttribute(UPDATED_DESCRIPTION_ATTRIBUTE)
            .currency(UPDATED_CURRENCY)
            .debitAmount(UPDATED_DEBIT_AMOUNT)
            .creditAmount(UPDATED_CREDIT_AMOUNT)
            .accountedDebit(UPDATED_ACCOUNTED_DEBIT)
            .accountedCredit(UPDATED_ACCOUNTED_CREDIT)
            .comments(UPDATED_COMMENTS)
            .createdBy(UPDATED_CREATED_BY)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE);

        restJeLinesMockMvc.perform(put("/api/je-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedJeLines)))
            .andExpect(status().isOk());

        // Validate the JeLines in the database
        List<JeLines> jeLinesList = jeLinesRepository.findAll();
        assertThat(jeLinesList).hasSize(databaseSizeBeforeUpdate);
        JeLines testJeLines = jeLinesList.get(jeLinesList.size() - 1);
        assertThat(testJeLines.getJeBatchId()).isEqualTo(UPDATED_JE_BATCH_ID);
        assertThat(testJeLines.getRowId()).isEqualTo(UPDATED_ROW_ID);
        assertThat(testJeLines.getLineNum()).isEqualTo(UPDATED_LINE_NUM);
        assertThat(testJeLines.getDescriptionAttribute()).isEqualTo(UPDATED_DESCRIPTION_ATTRIBUTE);
        assertThat(testJeLines.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testJeLines.getDebitAmount()).isEqualTo(UPDATED_DEBIT_AMOUNT);
        assertThat(testJeLines.getCreditAmount()).isEqualTo(UPDATED_CREDIT_AMOUNT);
        assertThat(testJeLines.getAccountedDebit()).isEqualTo(UPDATED_ACCOUNTED_DEBIT);
        assertThat(testJeLines.getAccountedCredit()).isEqualTo(UPDATED_ACCOUNTED_CREDIT);
        assertThat(testJeLines.getComments()).isEqualTo(UPDATED_COMMENTS);
        assertThat(testJeLines.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testJeLines.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testJeLines.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testJeLines.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingJeLines() throws Exception {
        int databaseSizeBeforeUpdate = jeLinesRepository.findAll().size();

        // Create the JeLines

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restJeLinesMockMvc.perform(put("/api/je-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jeLines)))
            .andExpect(status().isCreated());

        // Validate the JeLines in the database
        List<JeLines> jeLinesList = jeLinesRepository.findAll();
        assertThat(jeLinesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteJeLines() throws Exception {
        // Initialize the database
        jeLinesRepository.saveAndFlush(jeLines);
        int databaseSizeBeforeDelete = jeLinesRepository.findAll().size();

        // Get the jeLines
        restJeLinesMockMvc.perform(delete("/api/je-lines/{id}", jeLines.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<JeLines> jeLinesList = jeLinesRepository.findAll();
        assertThat(jeLinesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JeLines.class);
    }
}
