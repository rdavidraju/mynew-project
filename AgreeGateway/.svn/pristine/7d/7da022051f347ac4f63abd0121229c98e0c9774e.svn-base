package com.nspl.app.web.rest;

import com.nspl.app.AgreeGatewayV1App;
import com.nspl.app.domain.TenantDetails;
import com.nspl.app.repository.TenantDetailsRepository;
import com.nspl.app.repository.UserRepository;
import com.nspl.app.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.crypto.password.PasswordEncoder;
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
 * Test class for the TenantDetailsResource REST controller.
 *
 * @see TenantDetailsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeGatewayV1App.class)
public class TenantDetailsResourceIntTest {

    private static final String DEFAULT_TENANT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TENANT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PRIMARY_CONTACT = "AAAAAAAAAA";
    private static final String UPDATED_PRIMARY_CONTACT = "BBBBBBBBBB";

    private static final String DEFAULT_PRIMARY_CONTACT_EXT = "AAAAAAAAAA";
    private static final String UPDATED_PRIMARY_CONTACT_EXT = "BBBBBBBBBB";

    private static final String DEFAULT_SECONDARY_CONTACT = "AAAAAAAAAA";
    private static final String UPDATED_SECONDARY_CONTACT = "BBBBBBBBBB";

    private static final String DEFAULT_SECONDARY_CONTACT_EXT = "AAAAAAAAAA";
    private static final String UPDATED_SECONDARY_CONTACT_EXT = "BBBBBBBBBB";

    private static final String DEFAULT_WEBSITE = "AAAAAAAAAA";
    private static final String UPDATED_WEBSITE = "BBBBBBBBBB";

    private static final String DEFAULT_CORPORATE_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_CORPORATE_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CORPORATE_ADDRESS_2 = "AAAAAAAAAA";
    private static final String UPDATED_CORPORATE_ADDRESS_2 = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_PINCODE = "AAAAAAAAAA";
    private static final String UPDATED_PINCODE = "BBBBBBBBBB";

    private static final String DEFAULT_DOMAIN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DOMAIN_NAME = "BBBBBBBBBB";

    @Autowired
    private TenantDetailsRepository tenantDetailsRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTenantDetailsMockMvc;

    private TenantDetails tenantDetails;
    
    private UserRepository userRepository;
    
    private PasswordEncoder passwordEncoder;
    
    private Environment env;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TenantDetailsResource tenantDetailsResource = new TenantDetailsResource(tenantDetailsRepository,userRepository,passwordEncoder,env);
        this.restTenantDetailsMockMvc = MockMvcBuilders.standaloneSetup(tenantDetailsResource)
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
    public static TenantDetails createEntity(EntityManager em) {
        TenantDetails tenantDetails = new TenantDetails()
            .tenantName(DEFAULT_TENANT_NAME)
            .primaryContact(DEFAULT_PRIMARY_CONTACT)
            .primaryContactExt(DEFAULT_PRIMARY_CONTACT_EXT)
            .secondaryContact(DEFAULT_SECONDARY_CONTACT)
            .secondaryContactExt(DEFAULT_SECONDARY_CONTACT_EXT)
            .website(DEFAULT_WEBSITE)
            .corporateAddress(DEFAULT_CORPORATE_ADDRESS)
            .corporateAddress2(DEFAULT_CORPORATE_ADDRESS_2)
            .city(DEFAULT_CITY)
            .state(DEFAULT_STATE)
            .country(DEFAULT_COUNTRY)
            .pincode(DEFAULT_PINCODE)
            .domainName(DEFAULT_DOMAIN_NAME);
        return tenantDetails;
    }

    @Before
    public void initTest() {
        tenantDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createTenantDetails() throws Exception {
        int databaseSizeBeforeCreate = tenantDetailsRepository.findAll().size();

        // Create the TenantDetails
        restTenantDetailsMockMvc.perform(post("/api/tenant-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tenantDetails)))
            .andExpect(status().isCreated());

        // Validate the TenantDetails in the database
        List<TenantDetails> tenantDetailsList = tenantDetailsRepository.findAll();
        assertThat(tenantDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        TenantDetails testTenantDetails = tenantDetailsList.get(tenantDetailsList.size() - 1);
        assertThat(testTenantDetails.getTenantName()).isEqualTo(DEFAULT_TENANT_NAME);
        assertThat(testTenantDetails.getPrimaryContact()).isEqualTo(DEFAULT_PRIMARY_CONTACT);
        assertThat(testTenantDetails.getPrimaryContactExt()).isEqualTo(DEFAULT_PRIMARY_CONTACT_EXT);
        assertThat(testTenantDetails.getSecondaryContact()).isEqualTo(DEFAULT_SECONDARY_CONTACT);
        assertThat(testTenantDetails.getSecondaryContactExt()).isEqualTo(DEFAULT_SECONDARY_CONTACT_EXT);
        assertThat(testTenantDetails.getWebsite()).isEqualTo(DEFAULT_WEBSITE);
        assertThat(testTenantDetails.getCorporateAddress()).isEqualTo(DEFAULT_CORPORATE_ADDRESS);
        assertThat(testTenantDetails.getCorporateAddress2()).isEqualTo(DEFAULT_CORPORATE_ADDRESS_2);
        assertThat(testTenantDetails.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testTenantDetails.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testTenantDetails.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testTenantDetails.getPincode()).isEqualTo(DEFAULT_PINCODE);
        assertThat(testTenantDetails.getDomainName()).isEqualTo(DEFAULT_DOMAIN_NAME);
    }

    @Test
    @Transactional
    public void createTenantDetailsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tenantDetailsRepository.findAll().size();

        // Create the TenantDetails with an existing ID
        tenantDetails.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTenantDetailsMockMvc.perform(post("/api/tenant-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tenantDetails)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<TenantDetails> tenantDetailsList = tenantDetailsRepository.findAll();
        assertThat(tenantDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTenantDetails() throws Exception {
        // Initialize the database
        tenantDetailsRepository.saveAndFlush(tenantDetails);

        // Get all the tenantDetailsList
        restTenantDetailsMockMvc.perform(get("/api/tenant-details?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tenantDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantName").value(hasItem(DEFAULT_TENANT_NAME.toString())))
            .andExpect(jsonPath("$.[*].primaryContact").value(hasItem(DEFAULT_PRIMARY_CONTACT.toString())))
            .andExpect(jsonPath("$.[*].primaryContactExt").value(hasItem(DEFAULT_PRIMARY_CONTACT_EXT.toString())))
            .andExpect(jsonPath("$.[*].secondaryContact").value(hasItem(DEFAULT_SECONDARY_CONTACT.toString())))
            .andExpect(jsonPath("$.[*].secondaryContactExt").value(hasItem(DEFAULT_SECONDARY_CONTACT_EXT.toString())))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE.toString())))
            .andExpect(jsonPath("$.[*].corporateAddress").value(hasItem(DEFAULT_CORPORATE_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].corporateAddress2").value(hasItem(DEFAULT_CORPORATE_ADDRESS_2.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
            .andExpect(jsonPath("$.[*].pincode").value(hasItem(DEFAULT_PINCODE.toString())))
            .andExpect(jsonPath("$.[*].domainName").value(hasItem(DEFAULT_DOMAIN_NAME.toString())));
    }

    @Test
    @Transactional
    public void getTenantDetails() throws Exception {
        // Initialize the database
        tenantDetailsRepository.saveAndFlush(tenantDetails);

        // Get the tenantDetails
        restTenantDetailsMockMvc.perform(get("/api/tenant-details/{id}", tenantDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tenantDetails.getId().intValue()))
            .andExpect(jsonPath("$.tenantName").value(DEFAULT_TENANT_NAME.toString()))
            .andExpect(jsonPath("$.primaryContact").value(DEFAULT_PRIMARY_CONTACT.toString()))
            .andExpect(jsonPath("$.primaryContactExt").value(DEFAULT_PRIMARY_CONTACT_EXT.toString()))
            .andExpect(jsonPath("$.secondaryContact").value(DEFAULT_SECONDARY_CONTACT.toString()))
            .andExpect(jsonPath("$.secondaryContactExt").value(DEFAULT_SECONDARY_CONTACT_EXT.toString()))
            .andExpect(jsonPath("$.website").value(DEFAULT_WEBSITE.toString()))
            .andExpect(jsonPath("$.corporateAddress").value(DEFAULT_CORPORATE_ADDRESS.toString()))
            .andExpect(jsonPath("$.corporateAddress2").value(DEFAULT_CORPORATE_ADDRESS_2.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()))
            .andExpect(jsonPath("$.pincode").value(DEFAULT_PINCODE.toString()))
            .andExpect(jsonPath("$.domainName").value(DEFAULT_DOMAIN_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTenantDetails() throws Exception {
        // Get the tenantDetails
        restTenantDetailsMockMvc.perform(get("/api/tenant-details/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTenantDetails() throws Exception {
        // Initialize the database
        tenantDetailsRepository.saveAndFlush(tenantDetails);
        int databaseSizeBeforeUpdate = tenantDetailsRepository.findAll().size();

        // Update the tenantDetails
        TenantDetails updatedTenantDetails = tenantDetailsRepository.findOne(tenantDetails.getId());
        updatedTenantDetails
            .tenantName(UPDATED_TENANT_NAME)
            .primaryContact(UPDATED_PRIMARY_CONTACT)
            .primaryContactExt(UPDATED_PRIMARY_CONTACT_EXT)
            .secondaryContact(UPDATED_SECONDARY_CONTACT)
            .secondaryContactExt(UPDATED_SECONDARY_CONTACT_EXT)
            .website(UPDATED_WEBSITE)
            .corporateAddress(UPDATED_CORPORATE_ADDRESS)
            .corporateAddress2(UPDATED_CORPORATE_ADDRESS_2)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .country(UPDATED_COUNTRY)
            .pincode(UPDATED_PINCODE)
            .domainName(UPDATED_DOMAIN_NAME);

        restTenantDetailsMockMvc.perform(put("/api/tenant-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTenantDetails)))
            .andExpect(status().isOk());

        // Validate the TenantDetails in the database
        List<TenantDetails> tenantDetailsList = tenantDetailsRepository.findAll();
        assertThat(tenantDetailsList).hasSize(databaseSizeBeforeUpdate);
        TenantDetails testTenantDetails = tenantDetailsList.get(tenantDetailsList.size() - 1);
        assertThat(testTenantDetails.getTenantName()).isEqualTo(UPDATED_TENANT_NAME);
        assertThat(testTenantDetails.getPrimaryContact()).isEqualTo(UPDATED_PRIMARY_CONTACT);
        assertThat(testTenantDetails.getPrimaryContactExt()).isEqualTo(UPDATED_PRIMARY_CONTACT_EXT);
        assertThat(testTenantDetails.getSecondaryContact()).isEqualTo(UPDATED_SECONDARY_CONTACT);
        assertThat(testTenantDetails.getSecondaryContactExt()).isEqualTo(UPDATED_SECONDARY_CONTACT_EXT);
        assertThat(testTenantDetails.getWebsite()).isEqualTo(UPDATED_WEBSITE);
        assertThat(testTenantDetails.getCorporateAddress()).isEqualTo(UPDATED_CORPORATE_ADDRESS);
        assertThat(testTenantDetails.getCorporateAddress2()).isEqualTo(UPDATED_CORPORATE_ADDRESS_2);
        assertThat(testTenantDetails.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testTenantDetails.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testTenantDetails.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testTenantDetails.getPincode()).isEqualTo(UPDATED_PINCODE);
        assertThat(testTenantDetails.getDomainName()).isEqualTo(UPDATED_DOMAIN_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingTenantDetails() throws Exception {
        int databaseSizeBeforeUpdate = tenantDetailsRepository.findAll().size();

        // Create the TenantDetails

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTenantDetailsMockMvc.perform(put("/api/tenant-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tenantDetails)))
            .andExpect(status().isCreated());

        // Validate the TenantDetails in the database
        List<TenantDetails> tenantDetailsList = tenantDetailsRepository.findAll();
        assertThat(tenantDetailsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTenantDetails() throws Exception {
        // Initialize the database
        tenantDetailsRepository.saveAndFlush(tenantDetails);
        int databaseSizeBeforeDelete = tenantDetailsRepository.findAll().size();

        // Get the tenantDetails
        restTenantDetailsMockMvc.perform(delete("/api/tenant-details/{id}", tenantDetails.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TenantDetails> tenantDetailsList = tenantDetailsRepository.findAll();
        assertThat(tenantDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TenantDetails.class);
        TenantDetails tenantDetails1 = new TenantDetails();
        tenantDetails1.setId(1L);
        TenantDetails tenantDetails2 = new TenantDetails();
        tenantDetails2.setId(tenantDetails1.getId());
        assertThat(tenantDetails1).isEqualTo(tenantDetails2);
        tenantDetails2.setId(2L);
        assertThat(tenantDetails1).isNotEqualTo(tenantDetails2);
        tenantDetails1.setId(null);
        assertThat(tenantDetails1).isNotEqualTo(tenantDetails2);
    }
}
