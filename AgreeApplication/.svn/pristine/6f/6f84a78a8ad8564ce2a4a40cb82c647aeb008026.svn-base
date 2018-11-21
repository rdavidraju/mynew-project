package com.nspl.app.web.rest;

import com.nspl.app.AgreeApplicationApp;
import com.nspl.app.domain.FavouriteReports;
import com.nspl.app.repository.FavouriteReportsRepository;
import com.nspl.app.web.rest.errors.ExceptionTranslator;

import com.nspl.app.service.UserJdbcService;

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
 * Test class for the FavouriteReportsResource REST controller.
 *
 * @see FavouriteReportsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeApplicationApp.class)
public class FavouriteReportsResourceIntTest {

    private static final Long DEFAULT_REPORT_ID = 1L;
    private static final Long UPDATED_REPORT_ID = 2L;

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final Long DEFAULT_TENANT_ID = 1L;
    private static final Long UPDATED_TENANT_ID = 2L;

    @Autowired
    private FavouriteReportsRepository favouriteReportsRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFavouriteReportsMockMvc;

    private FavouriteReports favouriteReports;
    
    //private final UserJdbcService userJdbcService;

    /*@Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FavouriteReportsResource favouriteReportsResource = new FavouriteReportsResource(favouriteReportsRepository,userJdbcService);
        this.restFavouriteReportsMockMvc = MockMvcBuilders.standaloneSetup(favouriteReportsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }
*/
    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FavouriteReports createEntity(EntityManager em) {
        FavouriteReports favouriteReports = new FavouriteReports()
            .reportId(DEFAULT_REPORT_ID)
            .userId(DEFAULT_USER_ID)
            .tenantId(DEFAULT_TENANT_ID);
        return favouriteReports;
    }

    @Before
    public void initTest() {
        favouriteReports = createEntity(em);
    }

    @Test
    @Transactional
    public void createFavouriteReports() throws Exception {
        int databaseSizeBeforeCreate = favouriteReportsRepository.findAll().size();

        // Create the FavouriteReports
        restFavouriteReportsMockMvc.perform(post("/api/favourite-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(favouriteReports)))
            .andExpect(status().isCreated());

        // Validate the FavouriteReports in the database
        List<FavouriteReports> favouriteReportsList = favouriteReportsRepository.findAll();
        assertThat(favouriteReportsList).hasSize(databaseSizeBeforeCreate + 1);
        FavouriteReports testFavouriteReports = favouriteReportsList.get(favouriteReportsList.size() - 1);
        assertThat(testFavouriteReports.getReportId()).isEqualTo(DEFAULT_REPORT_ID);
        assertThat(testFavouriteReports.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testFavouriteReports.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
    }

    @Test
    @Transactional
    public void createFavouriteReportsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = favouriteReportsRepository.findAll().size();

        // Create the FavouriteReports with an existing ID
        favouriteReports.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFavouriteReportsMockMvc.perform(post("/api/favourite-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(favouriteReports)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<FavouriteReports> favouriteReportsList = favouriteReportsRepository.findAll();
        assertThat(favouriteReportsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllFavouriteReports() throws Exception {
        // Initialize the database
        favouriteReportsRepository.saveAndFlush(favouriteReports);

        // Get all the favouriteReportsList
        restFavouriteReportsMockMvc.perform(get("/api/favourite-reports?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(favouriteReports.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportId").value(hasItem(DEFAULT_REPORT_ID.intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())));
    }

    @Test
    @Transactional
    public void getFavouriteReports() throws Exception {
        // Initialize the database
        favouriteReportsRepository.saveAndFlush(favouriteReports);

        // Get the favouriteReports
        restFavouriteReportsMockMvc.perform(get("/api/favourite-reports/{id}", favouriteReports.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(favouriteReports.getId().intValue()))
            .andExpect(jsonPath("$.reportId").value(DEFAULT_REPORT_ID.intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFavouriteReports() throws Exception {
        // Get the favouriteReports
        restFavouriteReportsMockMvc.perform(get("/api/favourite-reports/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFavouriteReports() throws Exception {
        // Initialize the database
        favouriteReportsRepository.saveAndFlush(favouriteReports);
        int databaseSizeBeforeUpdate = favouriteReportsRepository.findAll().size();

        // Update the favouriteReports
        FavouriteReports updatedFavouriteReports = favouriteReportsRepository.findOne(favouriteReports.getId());
        updatedFavouriteReports
            .reportId(UPDATED_REPORT_ID)
            .userId(UPDATED_USER_ID)
            .tenantId(UPDATED_TENANT_ID);

        restFavouriteReportsMockMvc.perform(put("/api/favourite-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFavouriteReports)))
            .andExpect(status().isOk());

        // Validate the FavouriteReports in the database
        List<FavouriteReports> favouriteReportsList = favouriteReportsRepository.findAll();
        assertThat(favouriteReportsList).hasSize(databaseSizeBeforeUpdate);
        FavouriteReports testFavouriteReports = favouriteReportsList.get(favouriteReportsList.size() - 1);
        assertThat(testFavouriteReports.getReportId()).isEqualTo(UPDATED_REPORT_ID);
        assertThat(testFavouriteReports.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testFavouriteReports.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingFavouriteReports() throws Exception {
        int databaseSizeBeforeUpdate = favouriteReportsRepository.findAll().size();

        // Create the FavouriteReports

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFavouriteReportsMockMvc.perform(put("/api/favourite-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(favouriteReports)))
            .andExpect(status().isCreated());

        // Validate the FavouriteReports in the database
        List<FavouriteReports> favouriteReportsList = favouriteReportsRepository.findAll();
        assertThat(favouriteReportsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFavouriteReports() throws Exception {
        // Initialize the database
        favouriteReportsRepository.saveAndFlush(favouriteReports);
        int databaseSizeBeforeDelete = favouriteReportsRepository.findAll().size();

        // Get the favouriteReports
        restFavouriteReportsMockMvc.perform(delete("/api/favourite-reports/{id}", favouriteReports.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<FavouriteReports> favouriteReportsList = favouriteReportsRepository.findAll();
        assertThat(favouriteReportsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FavouriteReports.class);
        FavouriteReports favouriteReports1 = new FavouriteReports();
        favouriteReports1.setId(1L);
        FavouriteReports favouriteReports2 = new FavouriteReports();
        favouriteReports2.setId(favouriteReports1.getId());
        assertThat(favouriteReports1).isEqualTo(favouriteReports2);
        favouriteReports2.setId(2L);
        assertThat(favouriteReports1).isNotEqualTo(favouriteReports2);
        favouriteReports1.setId(null);
        assertThat(favouriteReports1).isNotEqualTo(favouriteReports2);
    }
}
