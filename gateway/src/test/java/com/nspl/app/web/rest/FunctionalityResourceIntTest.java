package com.nspl.app.web.rest;

import com.nspl.app.AgreeGatewayV1App;
import com.nspl.app.domain.Functionality;
import com.nspl.app.repository.FunctionalityRepository;
import com.nspl.app.repository.RoleFunctionAssignmentRepository;
import com.nspl.app.repository.RolesRepository;
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
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the FunctionalityResource REST controller.
 *
 * @see FunctionalityResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgreeGatewayV1App.class)
public class FunctionalityResourceIntTest {

	private static final String DEFAULT_FUNC_NAME = "AAAAAAAAAA";
	private static final String UPDATED_FUNC_NAME = "BBBBBBBBBB";

	private static final String DEFAULT_FUNC_DESC = "AAAAAAAAAA";
	private static final String UPDATED_FUNC_DESC = "BBBBBBBBBB";

	private static final String DEFAULT_FUNC_TYPE = "AAAAAAAAAA";
	private static final String UPDATED_FUNC_TYPE = "BBBBBBBBBB";

	private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
	private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

	private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
	private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());


	private static final Boolean DEFAULT_ACTIVE_IND = false;
	private static final Boolean UPDATED_ACTIVE_IND = true;

	private static final Long DEFAULT_TENANT_ID = 1L;
	private static final Long UPDATED_TENANT_ID = 2L;

	@Autowired
	private FunctionalityRepository functionalityRepository;

	@Autowired
	 private UserService userService;

	@Autowired
	private  RoleFunctionAssignmentRepository roleFunctionAssignmentRepository;
	@Autowired
	private  RolesRepository rolesRepository;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private EntityManager em;

	private MockMvc restFunctionalityMockMvc;

	private Functionality functionality;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		FunctionalityResource functionalityResource = new FunctionalityResource(functionalityRepository,roleFunctionAssignmentRepository,rolesRepository,userService);
		this.restFunctionalityMockMvc = MockMvcBuilders.standaloneSetup(functionalityResource)
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
	public static Functionality createEntity(EntityManager em) {
		Functionality functionality = new Functionality()
		.funcName(DEFAULT_FUNC_NAME)
		.funcDesc(DEFAULT_FUNC_DESC)
		.funcType(DEFAULT_FUNC_TYPE)
		.startDate(DEFAULT_START_DATE)
		.endDate(DEFAULT_END_DATE)
		.activeInd(DEFAULT_ACTIVE_IND)
		.tenantId(DEFAULT_TENANT_ID);
		return functionality;
	}

	@Before
	public void initTest() {
		functionality = createEntity(em);
	}

	@Test
	@Transactional
	public void createFunctionality() throws Exception {
		int databaseSizeBeforeCreate = functionalityRepository.findAll().size();

		// Create the Functionality
		restFunctionalityMockMvc.perform(post("/api/functionalities")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(functionality)))
				.andExpect(status().isCreated());

		// Validate the Functionality in the database
		List<Functionality> functionalityList = functionalityRepository.findAll();
		assertThat(functionalityList).hasSize(databaseSizeBeforeCreate + 1);
		Functionality testFunctionality = functionalityList.get(functionalityList.size() - 1);
		assertThat(testFunctionality.getFuncName()).isEqualTo(DEFAULT_FUNC_NAME);
		assertThat(testFunctionality.getFuncDesc()).isEqualTo(DEFAULT_FUNC_DESC);
		assertThat(testFunctionality.getFuncType()).isEqualTo(DEFAULT_FUNC_TYPE);
		assertThat(testFunctionality.getStartDate()).isEqualTo(DEFAULT_START_DATE);
		assertThat(testFunctionality.getEndDate()).isEqualTo(DEFAULT_END_DATE);
		assertThat(testFunctionality.getActiveInd()).isEqualTo(DEFAULT_ACTIVE_IND);
		assertThat(testFunctionality.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
	}

	@Test
	@Transactional
	public void createFunctionalityWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = functionalityRepository.findAll().size();

		// Create the Functionality with an existing ID
		functionality.setId(1L);

		// An entity with an existing ID cannot be created, so this API call must fail
		restFunctionalityMockMvc.perform(post("/api/functionalities")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(functionality)))
				.andExpect(status().isBadRequest());

		// Validate the Alice in the database
		List<Functionality> functionalityList = functionalityRepository.findAll();
		assertThat(functionalityList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void getAllFunctionalities() throws Exception {
		// Initialize the database
		functionalityRepository.saveAndFlush(functionality);

		// Get all the functionalityList
		restFunctionalityMockMvc.perform(get("/api/functionalities?sort=id,desc"))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(jsonPath("$.[*].id").value(hasItem(functionality.getId().intValue())))
		.andExpect(jsonPath("$.[*].funcName").value(hasItem(DEFAULT_FUNC_NAME.toString())))
		.andExpect(jsonPath("$.[*].funcDesc").value(hasItem(DEFAULT_FUNC_DESC.toString())))
		.andExpect(jsonPath("$.[*].funcType").value(hasItem(DEFAULT_FUNC_TYPE.toString())))
		.andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
		.andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
		.andExpect(jsonPath("$.[*].activeInd").value(hasItem(DEFAULT_ACTIVE_IND.toString())))
		.andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())));
	}

	@Test
	@Transactional
	public void getFunctionality() throws Exception {
		// Initialize the database
		functionalityRepository.saveAndFlush(functionality);

		// Get the functionality
		restFunctionalityMockMvc.perform(get("/api/functionalities/{id}", functionality.getId()))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(jsonPath("$.id").value(functionality.getId().intValue()))
		.andExpect(jsonPath("$.funcName").value(DEFAULT_FUNC_NAME.toString()))
		.andExpect(jsonPath("$.funcDesc").value(DEFAULT_FUNC_DESC.toString()))
		.andExpect(jsonPath("$.funcType").value(DEFAULT_FUNC_TYPE.toString()))
		.andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
		.andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
		.andExpect(jsonPath("$.activeInd").value(DEFAULT_ACTIVE_IND.toString()))
		.andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID.intValue()));
	}

	@Test
	@Transactional
	public void getNonExistingFunctionality() throws Exception {
		// Get the functionality
		restFunctionalityMockMvc.perform(get("/api/functionalities/{id}", Long.MAX_VALUE))
		.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateFunctionality() throws Exception {
		// Initialize the database
		functionalityRepository.saveAndFlush(functionality);
		int databaseSizeBeforeUpdate = functionalityRepository.findAll().size();

		// Update the functionality
		Functionality updatedFunctionality = functionalityRepository.findOne(functionality.getId());
		updatedFunctionality
		.funcName(UPDATED_FUNC_NAME)
		.funcDesc(UPDATED_FUNC_DESC)
		.funcType(UPDATED_FUNC_TYPE)
		.startDate(UPDATED_START_DATE)
		.endDate(UPDATED_END_DATE)
		.activeInd(UPDATED_ACTIVE_IND)
		.tenantId(UPDATED_TENANT_ID);

		restFunctionalityMockMvc.perform(put("/api/functionalities")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(updatedFunctionality)))
				.andExpect(status().isOk());

		// Validate the Functionality in the database
		List<Functionality> functionalityList = functionalityRepository.findAll();
		assertThat(functionalityList).hasSize(databaseSizeBeforeUpdate);
		Functionality testFunctionality = functionalityList.get(functionalityList.size() - 1);
		assertThat(testFunctionality.getFuncName()).isEqualTo(UPDATED_FUNC_NAME);
		assertThat(testFunctionality.getFuncDesc()).isEqualTo(UPDATED_FUNC_DESC);
		assertThat(testFunctionality.getFuncType()).isEqualTo(UPDATED_FUNC_TYPE);
		assertThat(testFunctionality.getStartDate()).isEqualTo(UPDATED_START_DATE);
		assertThat(testFunctionality.getEndDate()).isEqualTo(UPDATED_END_DATE);
		assertThat(testFunctionality.getActiveInd()).isEqualTo(UPDATED_ACTIVE_IND);
		assertThat(testFunctionality.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
	}

	@Test
	@Transactional
	public void updateNonExistingFunctionality() throws Exception {
		int databaseSizeBeforeUpdate = functionalityRepository.findAll().size();

		// Create the Functionality

		// If the entity doesn't have an ID, it will be created instead of just being updated
		restFunctionalityMockMvc.perform(put("/api/functionalities")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(functionality)))
				.andExpect(status().isCreated());

		// Validate the Functionality in the database
		List<Functionality> functionalityList = functionalityRepository.findAll();
		assertThat(functionalityList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	@Test
	@Transactional
	public void deleteFunctionality() throws Exception {
		// Initialize the database
		functionalityRepository.saveAndFlush(functionality);
		int databaseSizeBeforeDelete = functionalityRepository.findAll().size();

		// Get the functionality
		restFunctionalityMockMvc.perform(delete("/api/functionalities/{id}", functionality.getId())
				.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate the database is empty
		List<Functionality> functionalityList = functionalityRepository.findAll();
		assertThat(functionalityList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(Functionality.class);
		Functionality functionality1 = new Functionality();
		functionality1.setId(1L);
		Functionality functionality2 = new Functionality();
		functionality2.setId(functionality1.getId());
		assertThat(functionality1).isEqualTo(functionality2);
		functionality2.setId(2L);
		assertThat(functionality1).isNotEqualTo(functionality2);
		functionality1.setId(null);
		assertThat(functionality1).isNotEqualTo(functionality2);
	}
}
