package com.nspl.app.web.rest;

import au.com.bytecode.opencsv.CSVReader;

import com.codahale.metrics.annotation.Timed;
import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.InputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.mysql.jdbc.Blob;
import com.nspl.app.config.ApplicationContextProvider;
import com.nspl.app.domain.BatchHeader;
import com.nspl.app.domain.DataViews;
import com.nspl.app.domain.FileTemplateLines;
import com.nspl.app.domain.FileTemplates;
//import com.nspl.app.domain.FormConfig;
import com.nspl.app.domain.IntermediateTable;
import com.nspl.app.domain.LookUpCode;
import com.nspl.app.domain.RowConditions;
import com.nspl.app.domain.RuleGroup;
import com.nspl.app.domain.RuleGroupDetails;
import com.nspl.app.domain.Rules;
import com.nspl.app.domain.SourceFileInbHistory;
import com.nspl.app.domain.SourceProfileFileAssignments;
import com.nspl.app.domain.SourceProfiles;
import com.nspl.app.domain.TenantConfig;
import com.nspl.app.repository.BatchHeaderRepository;
import com.nspl.app.repository.FileTemplateLinesRepository;
import com.nspl.app.repository.FileTemplatesRepository;
//import com.nspl.app.repository.FormConfigRepository;
import com.nspl.app.repository.IntermediateTableRepository;
import com.nspl.app.repository.LookUpCodeRepository;
import com.nspl.app.repository.ReportParametersRepository;
import com.nspl.app.repository.ReportsRepository;
import com.nspl.app.repository.RowConditionsRepository;
import com.nspl.app.repository.RuleGroupDetailsRepository;
import com.nspl.app.repository.RuleGroupRepository;
import com.nspl.app.repository.RulesRepository;
import com.nspl.app.repository.SourceFileInbHistoryRepository;
import com.nspl.app.repository.SourceProfileFileAssignmentsRepository;
import com.nspl.app.repository.SourceProfilesRepository;
import com.nspl.app.repository.TenantConfigRepository;
import com.nspl.app.repository.search.FileTemplateLinesSearchRepository;
import com.nspl.app.repository.search.FileTemplatesSearchRepository;
import com.nspl.app.security.IDORUtils;
import com.nspl.app.service.ActiveStatusService;
import com.nspl.app.service.FileTemplatesService;
import com.nspl.app.service.FindDelimiterAndFileExtensionService;
import com.nspl.app.service.UserJdbcService;
import com.nspl.app.web.rest.dto.ErrorReport;
import com.nspl.app.web.rest.dto.ExcelConditionsDTO;
import com.nspl.app.web.rest.dto.ExcelFileReadingDTO;
import com.nspl.app.web.rest.dto.FileTempDTO;
import com.nspl.app.web.rest.dto.FileTemplateDTO;
import com.nspl.app.web.rest.dto.FileTemplateDataDTO;
import com.nspl.app.web.rest.dto.FileTemplateLinesDTO;
//import com.nspl.app.web.rest.dto.FileTemplatesInputDTO;
import com.nspl.app.web.rest.dto.FileTemplatesPostingDTO;
import com.nspl.app.web.rest.dto.FileTmpDTO;
import com.nspl.app.web.rest.dto.MultipleIdentifiersDTO;
import com.nspl.app.web.rest.dto.RowConditionsDTO;
import com.nspl.app.web.rest.dto.SampleDataDTO;
import com.nspl.app.web.rest.dto.SourceProfileFileAssignmentDTO;
import com.nspl.app.web.rest.dto.StatusStringDTO;
import com.nspl.app.web.rest.util.HeaderUtil;
import com.nspl.app.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;

import org.apache.hadoop.net.unix.DomainSocket.DomainChannel;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.json.JSONException;
//import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.core.JsonParseException;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.core.JsonParser;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.transaction.SystemException;

import static org.elasticsearch.index.query.QueryBuilders.*;


/**
 * REST controller for managing FileTemplates.
 */
@RestController
@RequestMapping("/api")

public class FileTemplatesResource {

	private final Logger log = LoggerFactory.getLogger(FileTemplatesResource.class);

	private static final String ENTITY_NAME = "fileTemplates";

	private final FileTemplatesRepository fileTemplatesRepository;

	private final FileTemplatesSearchRepository fileTemplatesSearchRepository;

	private final SourceProfileFileAssignmentsRepository sourceProfileFileAssignmentsRepository;

	private final SourceProfilesRepository sourceProfilesRepository;

	private final FileTemplateLinesRepository fileTemplateLinesRepository;

	private final FindDelimiterAndFileExtensionService findDelimiterAndFileExtensionService;

	@Inject
	JobDetailsResource jobDetailsResource;

	@Inject
	FileTemplatesService fileTemplatesService;

	@Inject
	BatchHeaderRepository batchHeaderRepository;

	@Inject
	SourceFileInbHistoryRepository sourceFileInbHistoryRepository;

	//	@Inject
	//	FormConfigRepository formConfigRepository;

	public List<HashMap<String, String>> multipleIdentifiersList;

	@Inject
	RuleGroupRepository ruleGroupRepository;

	@Inject
	RuleGroupDetailsRepository ruleGroupDetailsRepository;

	@Inject
	RulesRepository rulesRepository;

	@Inject
	UserJdbcService userJdbcService;

	@Inject
	LookUpCodeRepository lookUpCodeRepository;

	@Inject
	IntermediateTableRepository intermediateTableRepository;

	@PersistenceContext(unitName="default")
	private EntityManager em;

	@Inject
	FileTemplateLinesSearchRepository fileTemplateLinesSearchRepository;

	@Inject
	private Environment env;

	@Inject
	TenantConfigRepository tenantConfigRepository;

	@Inject
	RowConditionsRepository rowConditionsRepository;

	@Inject
	ReportParametersRepository reportsParametersRepository;
	
	@Inject
	ActiveStatusService activeStatusService;
	

	//declaration for img paths
	private String chaseImg="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAMAAABg3Am1AAAAk1BMVEX///8AXLnw8PEAWLcYZrQAYMIAXr1ahbFOfaxKi818q9vt7u5bibhLfrEXaLoib73Y29/29vcsdsDR194accne4eS4w89ChMcNacXn6es5drjBzdoXbMJmndUAZcuktMVljLSctMw2fMOSrMd6nL9xn81UldYfbr0JZ8RJf7aYveOFnrxnlME/fbwKYLamwNuFs+KsN3Y0AAABXElEQVRIie3W23KCMBAG4ATctB4QAyIirQdapUht+/5P14gKG7JBOtObzvS/5f8gM4ENjLUjjyOco1HAGUvGSnC1TKS9H50SxibAtYBnFUnw4ZiAQ2ER4wwGFOAQO1TfyUDQgEOWmH0ZA7cBSshC9ayAQxAR/Q7AwZ9p4NLqABzy0Oh3Ai7mjXi/djqBEmmrfwPClrdF1d/Xt6zAdjqwJj8/o2yWUAHp2DNW1z9droN7Gf0+KAuvzmTWA3hraHLoAR7QxrjDf/BXwZeLvpA+IFpNm6x6ADUiUYi+Ce5FA88/A7BSa4geO7KVGEBQjdgN/kBaecIA/OuAXSwFpyN8BERej9fUJjDAo5Klc1ogoPUZC2nRALFMmZYwp0QNbmMVZeYTU7sGZl/tR2CKG1hvqB0lxBXAjn4HkqwtLmD9QvfVexsDAbxXW18d7if9x8Q9g9DeV6fJfohzSI3GNxONHJoVvN79AAAAAElFTkSuQmCC";

	private String bOAImg="data:image/png;base64,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";

	private String wellsforgoImg="http://www.montrosechamber.org/wp-content/sabai/File/files/664e01a80cd59928f6152513282a9f9c.png";

	private String citiImg="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAMAAABEpIrGAAAAP1BMVEX///8AAJkzM8ymyvBmZsz/AACZmcwAAMzMzP//MzP/mZn/Zmb/zMwAM8z/fID/UFDx8fH/mczM7P/j4+Pq6urRYjM3AAAAuklEQVQ4jeWRSQKDIAxFMxFQtHa6/1mbQNUWtd23WTD9l08IAD8Zw9SFELp8OR/IrnaFGXb03s77ujKm3+inELLPwnVzaoGcSxKhFpM8HdRJyO1REiJxb0nAEpFEfbPogh6RIZo52QqRICG+6KRKKMWcdURRNiDO/rEWpfPtdVwBxfG9vBYQlG8AfQZ4LXcfsMeVO+QQUHu5eHsWgJTgugJgPbCgBOMzt2kUwI2l/CDrvWZ4Y3TzI/8cDzOPBHXjaCmjAAAAAElFTkSuQmCC";

	private String googleDriveImg="https://upload.wikimedia.org/wikipedia/commons/9/9b/Logo_of_Google_Drive.png";

	private String dropBoxImg="https://cfl.dropboxstatic.com/static/images/brand/twitter-card-glyph@2x-vflVqhKLO.png";

	private String sFTPImg="https://static1.squarespace.com/static/5410eb95e4b015076b7107ef/t/547e7ce3e4b08db18187a6cb/1417575651995/odrive-attachestoSFTP.png";




	public FileTemplatesResource(FileTemplatesRepository fileTemplatesRepository, FileTemplatesSearchRepository fileTemplatesSearchRepository,SourceProfileFileAssignmentsRepository sourceProfileFileAssignmentsRepository,SourceProfilesRepository sourceProfilesRepository,FileTemplateLinesRepository fileTemplateLinesRepository,FindDelimiterAndFileExtensionService findDelimiterAndFileExtensionService) {
		this.fileTemplatesRepository = fileTemplatesRepository;
		this.fileTemplatesSearchRepository = fileTemplatesSearchRepository;
		this.sourceProfileFileAssignmentsRepository=sourceProfileFileAssignmentsRepository;
		this.sourceProfilesRepository=sourceProfilesRepository;
		this.fileTemplateLinesRepository=fileTemplateLinesRepository;
		this.findDelimiterAndFileExtensionService=findDelimiterAndFileExtensionService;
	}
	/**
	 * POST  /file-templates : Create a new fileTemplates.
	 *
	 * @param fileTemplates the fileTemplates to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new fileTemplates, or with status 400 (Bad Request) if the fileTemplates has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/file-templates/{userId}")
	@Timed
	public ResponseEntity<FileTemplates> createFileTemplates(HttpServletRequest request,@RequestBody FileTemplates fileTemplates)throws URISyntaxException {
		//			,@RequestParam Long userId) throws URISyntaxException {
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long userId=Long.parseLong(map.get("userId").toString());
		log.debug("REST request to save FileTemplates for user:"+userId+" -> ", fileTemplates);

		if (fileTemplates.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new fileTemplates cannot already have an ID")).body(null);
		}
		fileTemplates.setLastUpdatedDate(ZonedDateTime.now());
		fileTemplates.setCreatedDate(ZonedDateTime.now());
		fileTemplates.setCreatedBy(userId);
		fileTemplates.setLastUpdatedBy(userId);
		FileTemplates result = fileTemplatesRepository.save(fileTemplates);

		String idForDisplay = IDORUtils.computeFrontEndIdentifier(result.getId().toString());
		result.setIdForDisplay(idForDisplay);
		result = fileTemplatesRepository.save(result);

		fileTemplatesSearchRepository.save(result);
		return ResponseEntity.created(new URI("/api/file-templates/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /file-templates : Updates an existing fileTemplates.
	 *
	 * @param fileTemplates the fileTemplates to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated fileTemplates,
	 * or with status 400 (Bad Request) if the fileTemplates is not valid,
	 * or with status 500 (Internal Server Error) if the fileTemplates couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/file-templates")
	@Timed
	public ResponseEntity<FileTemplates> updateFileTemplates(HttpServletRequest request,@RequestBody FileTemplates fileTemplates)throws URISyntaxException {
		//			,@RequestParam Long userId) throws URISyntaxException {
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long userId=Long.parseLong(map.get("userId").toString());
		log.debug("REST request to update FileTemplates for user:"+userId+" -> ", fileTemplates);


		if (fileTemplates.getId() == null) {
			return createFileTemplates(request,fileTemplates);
		}
		fileTemplates.setLastUpdatedDate(ZonedDateTime.now());
		fileTemplates.setCreatedBy(userId);
		fileTemplates.setLastUpdatedBy(userId);
		FileTemplates result = fileTemplatesRepository.save(fileTemplates);
		fileTemplatesSearchRepository.save(result);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, fileTemplates.getId().toString()))
				.body(result);
	}

	/**
	 * Author : shobha
	 */
	@GetMapping("/_search/file-templates")
	@Timed
	public List<FileTmpDTO> searchFileTemplates(HttpServletRequest request,@RequestParam(value="filterValue",required=false) String filterValue,
			@RequestParam(value = "page" , required = false) Integer offset,
			@RequestParam(value = "per_page", required = false) Integer limit) {
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());


		log.info("Rest api for fetching file templates data from elasticsearch repository for the tenant_id: "+tenantId);
		List<FileTmpDTO> templatesDto = new ArrayList<FileTmpDTO>();
		String query = "";
		query = query + " tenantId: \""+tenantId+"\" ";		// Filtering with tenant id	
		if(filterValue != null)
		{
			query = query + " AND \""+filterValue+"\"";
		} 
		log.info("query"+query);
		log.info("offset:"+offset+"limit"+limit);
		Page<FileTemplates> page = fileTemplatesSearchRepository.search(queryStringQuery(query),PaginationUtil.generatePageRequest(offset, limit));
		HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/file-templates");
		int count = 0 ;
		count = fileTemplatesRepository.findByTenantId(tenantId).size();
		if(page != null && page.getContent().size()>0)
		{
			List <FileTemplates> temps = new ArrayList<FileTemplates>();
			temps = page.getContent();
			for(int i=0;i<page.getContent().size();i++)
			{
				FileTmpDTO fileTmpDTO=new FileTmpDTO();
				fileTmpDTO.setTotalCount(count);
				fileTmpDTO.setIndex(i);
				Long ftId=page.getContent().get(i).getId();
				fileTmpDTO.setId(ftId.toString());
				if(page.getContent().get(i).getIdForDisplay()!=null && !page.getContent().get(i).getIdForDisplay().isEmpty())
					fileTmpDTO.setId(page.getContent().get(i).getIdForDisplay());
				if(page.getContent().get(i).getTemplateName()!=null && !page.getContent().get(i).getTemplateName().isEmpty())
					fileTmpDTO.setTemplateName(page.getContent().get(i).getTemplateName());
				if(page.getContent().get(i).getDescription()!=null && !page.getContent().get(i).getDescription().isEmpty())
					fileTmpDTO.setDescription(page.getContent().get(i).getDescription());
				if(page.getContent().get(i).getStartDate()!=null)
					fileTmpDTO.setStartDate(page.getContent().get(i).getStartDate());
				if(page.getContent().get(i).getEndDate()!=null)
					fileTmpDTO.setEndDate(page.getContent().get(i).getEndDate());
				if(page.getContent().get(i).getEndDate()!=null && page.getContent().get(i).getEndDate().isBefore(ZonedDateTime.now()))
				{

					fileTmpDTO.setEndDated(true);
				}
				else
				{
					fileTmpDTO.setEndDated(false);
				}
				if(page.getContent().get(i).getStatus()!=null && !page.getContent().get(i).getStatus().isEmpty())
					fileTmpDTO.setStatus(page.getContent().get(i).getStatus());
				if(page.getContent().get(i).getFileType()!=null && !page.getContent().get(i).getFileType().isEmpty())
					fileTmpDTO.setFileType(page.getContent().get(i).getFileType());
				if(page.getContent().get(i).getDelimiter()!=null && !page.getContent().get(i).getDelimiter().isEmpty())
				{
					LookUpCode lookup = new LookUpCode();
					lookup = lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("DELIMITER",page.getContent().get(i).getDelimiter(),  tenantId);
					if(lookup != null && lookup.getDescription() != null )
						fileTmpDTO.setDelimiter(lookup.getDescription());
				}

				if(page.getContent().get(i).getSource()!=null && !page.getContent().get(i).getSource().isEmpty())
					fileTmpDTO.setSource(page.getContent().get(i).getSource());
				if(page.getContent().get(i).getSkipRowsStart()!=null)
					fileTmpDTO.setSkipRowsStart(page.getContent().get(i).getSkipRowsStart());
				if(page.getContent().get(i).getSkipRowsEnd()!=null)
					fileTmpDTO.setSkipRowsEnd(page.getContent().get(i).getSkipRowsEnd());
				if(page.getContent().get(i).getNumber_of_columns()!=null)
					fileTmpDTO.setNumberOfColumns(page.getContent().get(i).getNumber_of_columns());
				if(page.getContent().get(i).getHeaderRowNumber()!=null)
					fileTmpDTO.setHeaderRowNumber(page.getContent().get(i).getHeaderRowNumber());
				fileTmpDTO.setTenantId(tenantId);
				if(page.getContent().get(i).getCreatedBy()!=null)
					fileTmpDTO.setCreatedBy(page.getContent().get(i).getCreatedBy());
				if(page.getContent().get(i).getCreatedDate()!=null)
					fileTmpDTO.setCreatedDate(page.getContent().get(i).getCreatedDate());
				if(page.getContent().get(i).getLastUpdatedBy()!=null)
					fileTmpDTO.setLastUpdatedBy(page.getContent().get(i).getLastUpdatedBy());
				if(page.getContent().get(i).getLastUpdatedDate()!=null)
					fileTmpDTO.setLastUpdatedDate(page.getContent().get(i).getLastUpdatedDate());
				if(page.getContent().get(i).getData()!=null && !page.getContent().get(i).getData().isEmpty())
					fileTmpDTO.setData(page.getContent().get(i).getData());
				if(page.getContent().get(i).getSdFilename()!=null && !page.getContent().get(i).getSdFilename().isEmpty())
					fileTmpDTO.setSdFilename(page.getContent().get(i).getSdFilename());
				if(page.getContent().get(i).getRowIdentifier()!=null && !page.getContent().get(i).getRowIdentifier().isEmpty())
					fileTmpDTO.setRowIdentifier(page.getContent().get(i).getRowIdentifier());
				if(page.getContent().get(i).getMultipleIdentifier()!=null){
					fileTmpDTO.setMultipleIdentifier(page.getContent().get(i).getMultipleIdentifier());
				}
				int taggedProfCnt = 0;
				taggedProfCnt = sourceProfileFileAssignmentsRepository.findByTemplateId(page.getContent().get(i).getId()).size();
				fileTmpDTO.setTaggedToProfCnt(taggedProfCnt);
				templatesDto.add(fileTmpDTO);
			}
		}
		//return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
		return templatesDto;
	}

	/**
	 * GET  /file-templates/:id : get the "id" fileTemplates.
	 *
	 * @param id the id of the fileTemplates to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the fileTemplates, or with status 404 (Not Found)
	 */
	@GetMapping("/file-templates/{idForDisplay}")
	@Timed
	public ResponseEntity<FileTempDTO> getFileTemplates(HttpServletRequest request, @PathVariable String idForDisplay) {
		log.debug("REST request to get FileTemplates : {}", idForDisplay);

		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());

		FileTemplates fileTemplates = fileTemplatesRepository.findByIdForDisplayAndTenantId(idForDisplay,tenantId);
		FileTempDTO fileTempDTO = new FileTempDTO();
		if(fileTemplates.getCreatedBy() != null)
			fileTempDTO.setCreatedBy(fileTemplates.getCreatedBy());
		fileTempDTO.setDelimiter(fileTemplates.getDelimiter());
		fileTempDTO.setDescription(fileTemplates.getDescription());
		fileTempDTO.setEndDate(fileTemplates.getEndDate());
		fileTempDTO.setFileType(fileTemplates.getFileType());
		fileTempDTO.setHeaderRowNumber(fileTemplates.getHeaderRowNumber());
		fileTempDTO.setId(idForDisplay);
		fileTempDTO.setLastUpdatedBy(fileTemplates.getLastUpdatedBy());
		fileTempDTO.setNumberOfColumns(fileTemplates.getNumber_of_columns());
		/*if(fileTemplates.getData() != null && !fileTemplates.getData().isEmpty() && !fileTemplates.getData().equals(""))
		{
			SampleDataDTO sampleDataDTO  =  new SampleDataDTO();
			sampleDataDTO = findDelimiterAndFileExtensionService.displayingBlobData(fileTemplates.getData());
			fileTempDTO.setData(fileTemplates.getData());
			fileTempDTO.setColHeaders(sampleDataDTO.getColHeaders());
		}*/
		JSONParser parser = new JSONParser();
		JSONArray newJObject = null;
		try {
			//sample data may not exist for templates uploaded through bulk upload
			if(fileTemplates.getData() != null)
			{
				newJObject = (JSONArray) parser.parse(fileTemplates.getData());
				fileTempDTO.setSampleData(newJObject);
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		fileTempDTO.setSdFilename(fileTemplates.getSdFilename());
		fileTempDTO.setSkipRowsEnd(fileTemplates.getSkipRowsEnd());
		fileTempDTO.setSkipRowsStart(fileTemplates.getSkipRowsStart());
		fileTempDTO.setSource(fileTemplates.getSource());
		fileTempDTO.setStartDate(fileTemplates.getStartDate());
		fileTempDTO.setStatus(fileTemplates.getStatus());
		fileTempDTO.setTemplateName(fileTemplates.getTemplateName());
		fileTempDTO.setTenantId(fileTemplates.getTenantId());
		fileTempDTO.setRowIdentifier(fileTemplates.getRowIdentifier());
		fileTempDTO.setMultipleRowIdentifiers(fileTemplates.getMultipleIdentifier());
		if(fileTemplates.getStartRowColumns() != null)
		{
			String [] cols = fileTemplates.getStartRowColumns().split(",");
			List<String> startRowColumns = new ArrayList<String>();
			for(int c=0;c<cols.length;c++)
			{
				startRowColumns.add(cols[c]);
			}
			ExcelConditionsDTO excelConditions = new ExcelConditionsDTO();
			excelConditions.setColumnsList(startRowColumns);

			List<RowConditions> skipRowConditions  = new ArrayList<RowConditions>();
			skipRowConditions  = rowConditionsRepository.findConditionsByTemplateIdAndType(fileTempDTO.getId(),"skip");


			List<RowConditionsDTO> skipRowConds = new ArrayList<RowConditionsDTO>();
			for(int i=0;i<skipRowConditions.size();i++)
			{
				RowConditionsDTO rowConditionsDTO = new RowConditionsDTO();

				FileTemplateLines tempLine = new FileTemplateLines();
				tempLine = fileTemplateLinesRepository.findOne(skipRowConditions.get(i).getTemplateLineId());
				if(tempLine != null && tempLine.getColumnHeader() != null)
					rowConditionsDTO.setColumnName(tempLine.getColumnHeader());

				if(skipRowConditions.get(i).getOperator() != null)
				{
					LookUpCode lookUpCode = lookUpCodeRepository.findByTenantIdAndLookUpCodeAndLookUpType(tenantId,skipRowConditions.get(i).getOperator(),"VARCHAR");
					if(lookUpCode != null && lookUpCode.getMeaning() != null)
						rowConditionsDTO.setOperatorMeaning(lookUpCode.getMeaning());
				}

				if(skipRowConditions.get(i).getValue() != null)
					rowConditionsDTO.setValue(skipRowConditions.get(i).getValue());

				if(skipRowConditions.get(i).getLogicalOperator() != null )
				{
					LookUpCode lookUpCode = lookUpCodeRepository.findByTenantIdAndLookUpCodeAndLookUpType(tenantId,skipRowConditions.get(i).getLogicalOperator(),"LOGICAL_OPERATOR");
					if(lookUpCode != null && lookUpCode.getMeaning() != null)
					{
						rowConditionsDTO.setLogicalOperator(lookUpCode.getMeaning());
					}
				}
				skipRowConds.add(rowConditionsDTO);
			}
			excelConditions.setSkipConditionsList(skipRowConds);


			List<RowConditions> endRowConditions  = new ArrayList<RowConditions>();
			endRowConditions  = rowConditionsRepository.findConditionsByTemplateIdAndType(fileTempDTO.getId(),"end");
			List<RowConditionsDTO> endRowConds = new ArrayList<RowConditionsDTO>();
			for(int i=0;i<endRowConditions.size();i++)
			{
				RowConditionsDTO rowConditionsDTO = new RowConditionsDTO();

				FileTemplateLines tempLine = new FileTemplateLines();
				log.info("endRowConditions.get(i).getId()"+endRowConditions.get(i).getId());
				log.info(" tempLine.getColumnHeader()"+endRowConditions.get(i).getId());
				tempLine = fileTemplateLinesRepository.findOne(endRowConditions.get(i).getTemplateLineId());
				if(tempLine != null && tempLine.getColumnHeader() != null)
					rowConditionsDTO.setColumnName(tempLine.getColumnHeader());

				if(endRowConditions.get(i).getOperator() != null)
				{
					LookUpCode lookUpCode = lookUpCodeRepository.findByTenantIdAndLookUpCodeAndLookUpType(tenantId,endRowConditions.get(i).getOperator(),"VARCHAR");
					if(lookUpCode != null && lookUpCode.getMeaning() != null)
						rowConditionsDTO.setOperatorMeaning(lookUpCode.getMeaning());
				}

				if(endRowConditions.get(i).getValue() != null)
					rowConditionsDTO.setValue(endRowConditions.get(i).getValue());

				if(endRowConditions.get(i).getLogicalOperator() != null )
				{
					LookUpCode lookUpCode = lookUpCodeRepository.findByTenantIdAndLookUpCodeAndLookUpType(tenantId,endRowConditions.get(i).getLogicalOperator(),"LOGICAL_OPERATOR");
					if(lookUpCode != null && lookUpCode.getMeaning() != null)
					{
						rowConditionsDTO.setLogicalOperator(lookUpCode.getMeaning());
					}
				}
				endRowConds.add(rowConditionsDTO);
			}
			excelConditions.setEndConditionsList(endRowConds);


			fileTempDTO.setExcelConditions(excelConditions);

			fileTempDTO.setStartRowColumns(startRowColumns);
		}
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(fileTempDTO));
	}

	/**
	 * DELETE  /file-templates/:id : delete the "id" fileTemplates.
	 *
	 * @param id the id of the fileTemplates to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/file-templates/{id}")
	@Timed
	public ResponseEntity<Void> deleteFileTemplates(@PathVariable Long id) {
		log.debug("REST request to delete FileTemplates : {}", id);
		fileTemplatesRepository.delete(id);
		fileTemplatesSearchRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH  /_search/file-templates?query=:query : search for the fileTemplates corresponding
	 * to the query.
	 *
	 * @param query the query of the fileTemplates search 
	 * @param pageable the pagination information
	 * @return the result of the search
	 */
	/*	@GetMapping("/_search/file-templates")
	@Timed
	public ResponseEntity<List<FileTemplates>> searchFileTemplates(@RequestParam String query, @ApiParam Pageable pageable) {
		log.debug("REST request to search for a page of FileTemplates for query {}", query);
		Page<FileTemplates> page = fileTemplatesSearchRepository.search(queryStringQuery(query), pageable);
		HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/file-templates");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}*/
	/**
	 * Author : Shobha
	 * @param file
	 * @return
	 * @throws IOException 
	 */
	@PostMapping("/fileUpload")
	@Timed
	public StatusStringDTO fileUpload(@RequestParam MultipartFile file) throws IOException
	{
		log.debug("Rest request for file upload");
		StatusStringDTO statusDTo = new StatusStringDTO();

		try {
			InputStream is = file.getInputStream();
			OutputStream os = new FileOutputStream("/home/nspl/Files/sample.csv");
			byte[] buffer = new byte[1024];
			int bytesRead;
			//read from is to buffer
			while((bytesRead = is.read(buffer)) !=-1){
				os.write(buffer, 0, bytesRead);
			}
			is.close();
			//flush OutputStream to write any buffered data to file
			os.flush();
			os.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*if(file.getOriginalFilename() != null)
			statusDTo.setStatus("success");
		else
			statusDTo.setStatus("failure");*/
		return statusDTo;
	}
	/**
	 * Author : Shobha
	 * @param file
	 * @return
	 */
	@PostMapping("/multipleFileUpload")
	@Timed
	public StatusStringDTO multipleFileUpload(@RequestParam List<MultipartFile> file)
	{
		log.debug("Rest request to file upload"+file.size());
		log.info("file name is:"+file.get(0).getOriginalFilename());
		StatusStringDTO statusDTo = new StatusStringDTO();
		if(file.get(0).getOriginalFilename() != null)
			statusDTo.setStatus("success");
		else
			statusDTo.setStatus("failure");
		return statusDTo;
	}



	/**
	 * Auhtor:Ravali
	 *@param templateId
	 * @return sourceAssignmnetProfile and sourceProfile(DTO)
	 */
	@GetMapping("/getSourceProfileAssigAndSourceProfile")
	@Timed
	public List<FileTemplateDTO> fetchfileTemplateDTO(@RequestParam Long templateId)
	{
		List<FileTemplateDTO> fileTemplateDtoList=new ArrayList<FileTemplateDTO>();

		List<SourceProfileFileAssignments> sourceProfileAssignments = new ArrayList<SourceProfileFileAssignments>();
		sourceProfileAssignments = sourceProfileFileAssignmentsRepository.findByTemplateId(templateId);
		for(int i=0;i<sourceProfileAssignments.size();i++)
		{

			FileTemplateDTO fileTemplateDto=new FileTemplateDTO();
			fileTemplateDto.setSPAId(sourceProfileAssignments.get(i).getId());
			if(sourceProfileAssignments.get(i).getSourceProfileId()!=null)
				fileTemplateDto.setSourceProfileId(sourceProfileAssignments.get(i).getSourceProfileId());
			if(sourceProfileAssignments.get(i).getFileNameFormat()!=null&&!sourceProfileAssignments.get(i).getFileNameFormat().isEmpty())
				fileTemplateDto.setFileNameFormat(sourceProfileAssignments.get(i).getFileNameFormat());
			if(sourceProfileAssignments.get(i).getFileDescription()!=null&&!sourceProfileAssignments.get(i).getFileDescription().isEmpty())
				fileTemplateDto.setFileDescription(sourceProfileAssignments.get(i).getFileDescription());
			if(sourceProfileAssignments.get(i).getFileExtension()!=null&&!sourceProfileAssignments.get(i).getFileExtension().isEmpty())
				fileTemplateDto.setFileExtension(sourceProfileAssignments.get(i).getFileExtension());
			if(sourceProfileAssignments.get(i).getFrequencyType()!=null&&!sourceProfileAssignments.get(i).getFrequencyType().isEmpty())
				fileTemplateDto.setFrequencyType(sourceProfileAssignments.get(i).getFrequencyType());
			if(sourceProfileAssignments.get(i).getDueTime()!=null&&!sourceProfileAssignments.get(i).getDueTime().isEmpty())
				fileTemplateDto.setDueTime(sourceProfileAssignments.get(i).getDueTime());
			if(sourceProfileAssignments.get(i).getDay()!=null)
				fileTemplateDto.setDay(sourceProfileAssignments.get(i).getDay());
			if(sourceProfileAssignments.get(i).getSourceDirectoryPath()!=null&&!sourceProfileAssignments.get(i).getSourceDirectoryPath().isEmpty())
				fileTemplateDto.setSourceDirectoryPath(sourceProfileAssignments.get(i).getSourceDirectoryPath());
			if(sourceProfileAssignments.get(i).getLocalDirectoryPath()!=null&&!sourceProfileAssignments.get(i).getLocalDirectoryPath().isEmpty())
				fileTemplateDto.setLocalDirectoryPath(sourceProfileAssignments.get(i).getLocalDirectoryPath());
			if(sourceProfileAssignments.get(i).getTemplateId()!=null)
				fileTemplateDto.setTemplateId(sourceProfileAssignments.get(i).getTemplateId());
			if(sourceProfileAssignments.get(i).isEnabledFlag()!=null)
				fileTemplateDto.setSPAEnabledFlag(sourceProfileAssignments.get(i).isEnabledFlag());
			if(sourceProfileAssignments.get(i).getCreatedBy()!=null)
				fileTemplateDto.setSPACreatedBy(sourceProfileAssignments.get(i).getCreatedBy());
			if(sourceProfileAssignments.get(i).getCreatedDate()!=null)
				fileTemplateDto.setSPACreationDate(sourceProfileAssignments.get(i).getCreatedDate());
			if(sourceProfileAssignments.get(i).getLastUpdatedBy()!=null)
				fileTemplateDto.setSPALastUpdatedBy(sourceProfileAssignments.get(i).getLastUpdatedBy());
			if(sourceProfileAssignments.get(i).getLastUpdatedDate()!=null)
				fileTemplateDto.setSPALastUpdatedDate(sourceProfileAssignments.get(i).getLastUpdatedDate());


			if(sourceProfileAssignments.get(i).getSourceProfileId()!=null)
			{
				SourceProfiles sourceProfiles=sourceProfilesRepository.findOne(sourceProfileAssignments.get(i).getSourceProfileId());

				if(sourceProfiles.getSourceProfileName()!=null&&!sourceProfiles.getSourceProfileName().isEmpty())
					fileTemplateDto.setSourceProfileName(sourceProfiles.getSourceProfileName());
				if(sourceProfiles.getDescription()!=null&&!sourceProfiles.getDescription().isEmpty())
					fileTemplateDto.setDescription(sourceProfiles.getDescription());
				if(sourceProfiles.getStartDate()!=null)
					fileTemplateDto.setStartDate(sourceProfiles.getStartDate());
				if(sourceProfiles.getEndDate()!=null)
					fileTemplateDto.setEndDate(sourceProfiles.getEndDate());
				if(sourceProfiles.isEnabledFlag()!=null)
					fileTemplateDto.setSourceProfileEnableFlag(sourceProfiles.isEnabledFlag());
				if(sourceProfiles.getConnectionId()!=null)
					fileTemplateDto.setConnectionId(sourceProfiles.getConnectionId());
				if(sourceProfiles.getTenantId()!=null)
					fileTemplateDto.setTenantId(sourceProfiles.getTenantId());
				if(sourceProfiles.getCreatedBy()!=null)
					fileTemplateDto.setSourceProfileCreatedBy(sourceProfiles.getCreatedBy());
				if(sourceProfiles.getCreatedDate()!=null)
					fileTemplateDto.setSourceProfileCreationDate(sourceProfiles.getCreatedDate());
				if(sourceProfiles.getLastUpdatedBy()!=null)
					fileTemplateDto.setSourceProfileLastUpdatedBy(sourceProfiles.getLastUpdatedBy());
				if(sourceProfiles.getLastUpdatedDate()!=null)
					fileTemplateDto.setSourceProfileLastUpdateDate(sourceProfiles.getLastUpdatedDate());
			}
			fileTemplateDtoList.add(fileTemplateDto);

		}
		return fileTemplateDtoList;

	}

	@PostMapping("/templatesBulkUpload")
	@Timed
	public List<ErrorReport> templatesBulkUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request,@RequestParam String templatesList,@RequestParam String bulkUpload) throws Exception
	{
		log.info("Rest request to save file templates");
		List<ErrorReport> finalErrorReport= new ArrayList<ErrorReport>();
		ErrorReport errorReports = new ErrorReport();
		ObjectMapper objMapper = new ObjectMapper();
		TypeFactory typeFact = objMapper.getTypeFactory();
		List<FileTemplatesPostingDTO> fileTemplatesPostingDTOs= objMapper.readValue(templatesList, typeFact.constructCollectionType(List.class,FileTemplatesPostingDTO.class));
		String templateName = "";
		for(FileTemplatesPostingDTO fileTemplatesPostingDTO : fileTemplatesPostingDTOs )
		{
			templateName = fileTemplatesPostingDTO.getFileTempDTO().getTemplateName();
			try{
				errorReports = fileTemplatesService.saveTemplate(fileTemplatesPostingDTO,request,bulkUpload);	
				finalErrorReport.add(errorReports);
			}
			catch(Exception e)
			{

			}

		}


		return finalErrorReport;

	}


	/**
	 * Auhtor:Ravali
	 *@param FileTemplatesPostingDTO
	 * @return ErrorReport
	 * @throws Exception 
	 */
	@PostMapping("/FileTemplatesPostingDTO")
	@Timed
	public ErrorReport postingFileTempAndFileTempLinesAndSPA(@RequestBody FileTemplatesPostingDTO fileTemplatesPostingDTO, HttpServletRequest request) throws Exception
	{
		log.info("Rest request to save file template and template lines and assignments");
		ErrorReport finalErrorReport=new ErrorReport();
		//	User currentUser=userService.getCurrentUser();
		//	Long tenantId=currentUser.getTenantId();
		finalErrorReport = fileTemplatesService.saveTemplate(fileTemplatesPostingDTO,request,"false");
		return finalErrorReport;


	}

	/*
	 *//**Auhtor:Ravali
	 * @return details of source column values in a hashMap
	 *//*
	@GetMapping("/getFileTempMap")
	@Timed
	public List<HashMap> getFileTemp()
	{
		log.info("Rest Request to get distinct source details");
		//log.info("columnName :"+columnName);
		List<String> distinctList=fileTemplatesRepository.fetchDistinct();
		log.info("distinctList :"+distinctList);
		List<HashMap> FinalMap=new ArrayList<HashMap>();
		int k=0;
		for(int i=0;i<distinctList.size();i++)
		{
			HashMap map1=new HashMap();
			k=k+1;
			map1.put("id",k);
			map1.put("Source", distinctList.get(i));
			String displayName=distinctList.get(i).replaceAll("\\W", " ");
			map1.put("dispname", displayName);
			String img="";
			if(distinctList.get(i).equalsIgnoreCase("Chase"))
			{
				img="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAMAAABg3Am1AAAAk1BMVEX///8AXLnw8PEAWLcYZrQAYMIAXr1ahbFOfaxKi818q9vt7u5bibhLfrEXaLoib73Y29/29vcsdsDR194accne4eS4w89ChMcNacXn6es5drjBzdoXbMJmndUAZcuktMVljLSctMw2fMOSrMd6nL9xn81UldYfbr0JZ8RJf7aYveOFnrxnlME/fbwKYLamwNuFs+KsN3Y0AAABXElEQVRIie3W23KCMBAG4ATctB4QAyIirQdapUht+/5P14gKG7JBOtObzvS/5f8gM4ENjLUjjyOco1HAGUvGSnC1TKS9H50SxibAtYBnFUnw4ZiAQ2ER4wwGFOAQO1TfyUDQgEOWmH0ZA7cBSshC9ayAQxAR/Q7AwZ9p4NLqABzy0Oh3Ai7mjXi/djqBEmmrfwPClrdF1d/Xt6zAdjqwJj8/o2yWUAHp2DNW1z9droN7Gf0+KAuvzmTWA3hraHLoAR7QxrjDf/BXwZeLvpA+IFpNm6x6ADUiUYi+Ce5FA88/A7BSa4geO7KVGEBQjdgN/kBaecIA/OuAXSwFpyN8BERej9fUJjDAo5Klc1ogoPUZC2nRALFMmZYwp0QNbmMVZeYTU7sGZl/tR2CKG1hvqB0lxBXAjn4HkqwtLmD9QvfVexsDAbxXW18d7if9x8Q9g9DeV6fJfohzSI3GNxONHJoVvN79AAAAAElFTkSuQmCC";
			}
			else if(distinctList.get(i).equalsIgnoreCase("BOA"))
			{
				img="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAOEAAADhCAMAAAAJbSJIAAAA0lBMVEX////jGTIrWajjFjDjFC/jEC3iACYoV6fiACTiACEkVabiCyriACf+9fb++PniDSsbUKT86evhAB397/H50tb63eD86uzypKv2wcb84+boR1nmMUbhABjtgoz2+Pz0qbHn7PXkHzjrXGzmPU/wkJrhABHscH3xm6M8ZK29yuL3usL4zdHteoX0s7kxXqvlJz/Y4O6dsNR2kMNQc7SzwNyTps7rZXRkhL9+msrk6fPM1efmOUzpT2Hxlp/scn1EarBefbmmuNrxiJXoVmR3lMYLSqJ33dEgAAANSklEQVR4nO2ce3+iOBSGixAFQfACVUFFLkVFKa1tsdZLvUy//1faqJ0pQVAyddTuL89fu7Po5DXJec85CXtzQyAQCAQCgUAgEAgEAoFAIBAIBAKBQCAQCAQCgUAgEAgEAoFAIBAIBAKBQCAQsBHFmr0WstNqRbz0UE5PoaTUmipXZiWaBlxjOuuXKpce0+moKH2vqYJyFlC/AWx2MqjJ/wuRolwdTKmyAGgKhREa1kCv3V56gN9GVPS5Vs4yVAyMIEycmfvzZ7Ki1FaThhAvkgea2XR/XuSRZ06nHvp3sdIfGHAmo0t1K1JifHXQv9hY8Sn0B36Wl3henaHrT25qFNjbjxtoIGU1u57whVdF4dadM58LkgbsnTVTEO+rNacakOJEUgzbUPX6de9JuN/gUhTC4wdc1qzK4ckpuM21z4I4jRQoG46rFC4m4DAF6HkTNiacSJwReP2wKSgbd4wPPDTPWnbtCper2K82VT7eEnbDnuty6PmC7A18VoqfSNYIOrWLSUmi5tANIXZ7fW4ynprO9fBE3ipV845P+EWANe9c2Y4UCxXXYRI8bzdswPvWPOwJYkV2uHJsbIW/iKEO5MS/7lKINcfgpfgh70QC2kLnpqBbIP4T8GFK9a4wE+gPLJ+Ot4PduDk+6CMi5blF8fFhB5T9VeX6Qqu4sYP9RPtr3PydqithkfWZqcVrhCbJDvpXmJyXvNWaToqs1MYl/RViCRWvaWXjPZLO0oEuX361PvR6D8gfQM+bCHziRDKC71QR/+hX1Vj7ACznB0hmLspnn9Tx8nXR7S5en9vhP60oXkCxiRNJC5RqI+m24k0F1D5oiStbK1eufwmsu/ZcDWbnzAlaz8On+3w+l8vnu0+/nlvh/1ZROlojMezQgJk4iLVX+ib7x1VpviysdaUUijRyx1E1RgA8pQ6U88i76Y26mXw+80k+l7n/9YY8IPbXIJsYdhhgqB7yPLRInqEZwHMg8JDV2F9ZBixNPpN6YAT/3i5bb6+ZDzh5mRBwKj/yrw/oTM5UH8SHka0jcB0xvM0Kjm/4atMN/ZFYcE2hgf5QMCRbtX/pJO2H5dNHPhNLsfj+iMQd0Z1bdGLYoTnKRgoKRQ/vz1u4n/ly3KdBY+KV/k2UbT08DzPFBH1wsRbvFy8t9CP9WWAk5nQMO7H7sfNR7+sBiJW308iqnX+wIdu9l6d8ojw4g08vEevYDXZTTiRq5PymG9VYdzuOzyVbzlajoHZOvCEfHkfd5OnLFTPDXngbtkP/XOnPtHKCr/MNY4UorHi26QsJqU5EY/OETZ7nX0+biJm0PD/eX8bt0ONvL4tR2EBERbe4fY2gTDku0sDo22tfOjx7YY3WieqQ1uPTfS5JXy5XLC7ewruv9bzoQpe8f38Niy64KrKvtt0cD2nmKLbqU8nZewwM8J3v78eH12Ixcfpy+Uz3JSzkZvya3z0ORX4sEJPsm8ane0DTNyZIR06sVNXGgbw9XqAEDN8sfUte+22YLyavzlx3tAxNX6vdG30UkeX79DwOPdB3LArQQPCnyBYSSzWHilnFh+XxYLIefLOnPO4tDk1fHgbP0NMtaJV7sShXfH8Jr2HZnlJqUw//7qV+xxIOtULi5AlgEti1b5ri23KR5O1bfaNl2Bugl7zHxlqo8bXXDmlEfF2UPYfBnD6a56xBFTnbEWUXO+ZA80s2h8zGHMZhfcthN3E154r3w+dx3F8C60OVSRs6PwGc4XhoISXrzamhzrA2ZG/4nmzu0Pxe39qhp8cvi/vk3bohf7943MsGFBvW+HjTx3DstIrKK7gD1RckmJSv0/fpYOWQbH7QHV7D0eNm/Ov9gFd+fij/cf+IJnS1YELhTR8jNIyZUkA2nwzl/W5pAWPt3qShB3Oz5PCSu0dTz4dNMDqsDxYemaclWndULZ6PPZFKABZWtOZEcpjKDGY/IYeBHjs9uh1bbwfCJ1xsT4i+Vm904OnfH8q8jxBfLNQHICGLS5o93rAcHcnuChXXvNvPd6WGc7DsaL+NDuSe+fvRsh16etw78PTXj7J4QXZgqTYvZ3FWJw14LYik2aX+SivHd86zdCex19HuDQ/py4+Qhsx4uThQaXx+aC+KyroJBJzpo7PU2kYPxAsyLK2SO0FMdqrH96x6wwMBMV8cIUMdL4+vT6gPjbk3NVtNrojjgNYwcNHMswQd5khpxcOEPkbfr/figbEuwqZ9035cZI7PH6z4kfBSG1h45iA1rI4SOU+emf7xL6F5Ldqyah/yh0zxHumlbWqN4/PXXY5RfaYm4ehjWM6s3aJRwzXTOgwDJrPQZ8evB0YMDQL1h8fkUuo3eagPdT/dYsDxqvZrEgBvrCIBo9KxaCn9dwBe/dOybL1075M2VT73juhrL49kL9uYuwhn5DdiQfcTTtMSBgf8aRXtbRSUOaw+0n8HLdFaEGrKjp+HT7mYjZXLv7+GQ/34+em4vvfhM6KvXtXKGNNHSYw6QJup0GHMgwexe/oEY7qKev/bIwylqEi4l17DVt1+PpSMf34EFhLI+lSqanJgjxkbz5sdNHURocNw8d4XDyP4QTWu9G9vy4mvKcrnh+HKrvU8POp/G3tA9MmdqYARXmiBDjx0bCJ0GAlLH+s3a0llRuvhefS7pM8Vn8IG3zrolp+fiOpTZlMce6BZZhC5jyF6mA7DlH378NXH9sPrRgkc7WPY4B+G3WPzl8//QvXdQnvH0sevIndqRM/UDlwRiNOnderHW/7tZbdYHIYDTGuYOVI+ZPIfI7T+E20NQx/NZLno7S+xalEY7rCpLLRq2gqxFw4wrZdicrazW58wJ4/qY7n0g6OBMVlFKoLCisFyGEaKHmelpQUD6LH12R0h/ndz6/ocxuAkY92JuF+9w7BYjVNYfqSrfvf09YbHEtB8Zoiej5Y8E2Pz0IIUVNHQV+jbGp4+QRv8nb6bm5fjAXQUOeT2AiZ9cKcFaq5H9l9tNcFxUIrmNLv21zep3l6SW2iZnaWgTbTa3MdIPnjgRC9895sTrPBJs8ZK/s6JaesheRphzvOI6lMGE4zlBVgz+tvLAwtLH5Wlmt/StxP5EntwkcsXf6EBtGJr6esHGpStaM9acTC+YPMdAusoJzkMbr3s9QthAbVA9RV0I/32gVY5qUb+lnrApTg2/IIBhnO62yet5SJcQsI6ZLFE9clqI/3wNv6A/vaiMmhg9W+gPzinPQRuL0d/Ct9c7glt78LxZePvxcYB+HUVjS+iMjOyOPpoXpuf/rbtGKbl+czuOAldoPXOJH0AZbJ7twyUjooXX3h/sHf6fyKNiyIMMEM0wy7oZuK1mf0fn4W5P/qtdbwKa3ODfC8Gn1DjsvuOdN2ggQV+eofn+b3gXl1T6Rc4hGlME8u/k9BCO0ziSkuf/wPO7EeCu7s28E7uOb96ziuKoouRgACgRZNH2ZTw9EmGfda7w8o84RWROH20akfmrzRoYDXAGUmbn/WVjLrns2kHR0tWMzK4OswQMORBg/Cds76OIfbN9DUub0TfUbv11lgFBAWowDvrAq3bWuoUhGmYbiQ61OYGTv+MorNq9awLVPRMOvUMsH50cLAEwTvf5gW7ftZL7aWBnzoEAm7v4nJnglVBUMydeeZL+66feoRM6Hjkk5rK4J1wC9q5X6Zd3aVdYrSkRR1CmeNdD6KBdl4H3A6ymbLTy/vRW+clDIPZfYUxv8iLwrKtHj/sZPi1jk5gAcdgtl8hmPql3pqRO8dOlHjNjtZIto9V41Kslnyr4t8jKlWfOzQ6M/LKQEFf40VQIDjRNP3MiHWbS5oT6S56gCDj1Fjbn8j/NzUuHkrAxoVVpqxGLdD28a7/gjvnCvRtUAIjqpFmND3ylIx3zA3rEOuKXnl2AzTDBEa0BVaZpW9ybH8iMFldyQR+Ug2YrzYEq0Z24ObVJbwFSp23SEpDvTP9bOUCIfoChIJRg2wRtOo/7cL8Jcpsa3WspUdCqLfGamNTDGde37vqW0TFKUtcdAfeNjU8i8j63hW+3vyJWJtEU+TaFLNKKgfnekP0JFRszBAq7bnMVSPK0zJeCGXO8PrrCbntALwdyE+ileR1IztYNw1gpXV9HniIiq5iTqCP977LxakHeIWusL5wmYRPoUqlT2SYxuB6PTCZyjrlnW5aov/urtbl8SwqxVIFhvkTJ3BHaXU8ZQOTznXVSXiIbnA47aZ580d5RAz16qHzF4aPduN+IIdyNx5cQ6vp+5RmfPw9BG6vW/VjkeN6x4zQ3NbKlX7tfyC0YPuRaaTBZGuChQ6lqcbkyv4ve3+Di/YyaCnYHbd0rJnudgL1ZyWlsdTD7QwA7J0k0a+5zdq0Y9oXHt4pEL3p73pK0H5fu5QnN16gm57uXHRsp0Jp7g4dhfUfl98obJqBWA0uObDTUdBhwKGFwdeBmWh4rierlen/YZVuUayygLw4UtWa8o2naT83+d5jFbnY1vEnqqH+jwTuU/f0n558EwgEAoFAIBAIBAKBQCAQCAQCgUAgEAgEAoFAIBAIBAKBQCAQCAQCgUAg/DT+A7l2UdeirT0QAAAAAElFTkSuQmCC";
			}
			else if(distinctList.get(i).equalsIgnoreCase("Wellsforgo"))
			{
				img="http://www.montrosechamber.org/wp-content/sabai/File/files/664e01a80cd59928f6152513282a9f9c.png";
			}
			else if(distinctList.get(i).equalsIgnoreCase("Citi"))
			{
				img="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAMAAABEpIrGAAAAP1BMVEX///8AAJkzM8ymyvBmZsz/AACZmcwAAMzMzP//MzP/mZn/Zmb/zMwAM8z/fID/UFDx8fH/mczM7P/j4+Pq6urRYjM3AAAAuklEQVQ4jeWRSQKDIAxFMxFQtHa6/1mbQNUWtd23WTD9l08IAD8Zw9SFELp8OR/IrnaFGXb03s77ujKm3+inELLPwnVzaoGcSxKhFpM8HdRJyO1REiJxb0nAEpFEfbPogh6RIZo52QqRICG+6KRKKMWcdURRNiDO/rEWpfPtdVwBxfG9vBYQlG8AfQZ4LXcfsMeVO+QQUHu5eHsWgJTgugJgPbCgBOMzt2kUwI2l/CDrvWZ4Y3TzI/8cDzOPBHXjaCmjAAAAAElFTkSuQmCC";
			}
			map1.put("imagePath",img);
			List<HashMap> templateListMap=new ArrayList<HashMap>();
			List<FileTemplates> fileTempList=fileTemplatesRepository.findBySource(distinctList.get(i));
			log.info("fileTempList :"+fileTempList);
			for(FileTemplates fileTemp:fileTempList)
			{
				HashMap map2=new HashMap();
				map2.put("tempId", fileTemp.getId());
				map2.put("templateName", fileTemp.getTemplateName());
				templateListMap.add(map2);
			}
			log.info("templateListMap :"+templateListMap);
			map1.put("templateList", templateListMap);
			FinalMap.add(map1);
		}
		return FinalMap;
	}
	  */


	/**Auhtor:Ravali
	 * @param tableName
	 * @param columnName
	 * @return details of column values in a hashMap
	 */
	@GetMapping("/fetchFileTemplateByColumnNameAndTableName/{tableName}/{columnName}")
	@Timed
	public List<HashMap> getFileTempList(HttpServletRequest request,@PathVariable String tableName,@PathVariable String columnName)
	{
		HashMap map0=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map0.get("tenantId").toString());
		log.info("Rest Request to get distinct source details for tenant: "+tenantId);
		log.info("columnName :"+columnName);


		Query distinctList=em.createQuery("select distinct"+"("+columnName+")"+ " FROM "+tableName+" where tenant_id="+tenantId);
		log.info("distinctList : "+distinctList);
		List distinct = new ArrayList<String>();
		distinct =distinctList.getResultList();
		log.info("distinct :"+distinct);

		HashMap allMap=new HashMap();
		List<HashMap> FinalMap=new ArrayList<HashMap>();
		List<HashMap> allRuleGrpListMap=new ArrayList<HashMap>();
		if(tableName.equalsIgnoreCase("RuleGroup"))
		{
			allMap.put("rulePurpose", "All");
			List<RuleGroup> ruleGroups = new ArrayList<RuleGroup>();
			ruleGroups = ruleGroupRepository.findByTenantIdOrderByIdDesc(tenantId);

			//for(int i=0;i<ruleGroups.size();i++)
			//{
			//	Rules rule = new Rules();
			//	HashMap newRuleMap=new HashMap();
			//	newRuleMap.put("id", ruleGroups.get(i).getId());
			//	if(ruleGroups.get(i).getName() != null && !ruleGroups.get(i).getName().isEmpty())
			//	newRuleMap.put("name", ruleGroups.get(i).getName());
			//allRuleGrpListMap.add(newRuleMap);
			//}
			allMap.put("ruleGroups", allRuleGrpListMap);
		}
		else
		{
			allMap.put("dispname", "All");
		}


		FinalMap.add(allMap);


		int k=0;
		int allCount = 0;
		for(int i=0;i<distinct.size();i++)
		{
			if(tableName.equalsIgnoreCase("RuleGroup"))
			{

				if(distinct.get(i) != null)
				{
					HashMap ruleGroupMap = new HashMap();
					List ruleGrpList=em.createQuery("select id FROM RuleGroup where tenantId="+tenantId +"and "+columnName+" ='"+distinct.get(i).toString()+"'").getResultList();
					List<HashMap> ruleGroupsListMap=new ArrayList<HashMap>();
					List<Long> ids = new ArrayList<Long>();

					for(int j=0;j<ruleGrpList.size();j++)
					{
						HashMap ruleGroupWithRulesMap = new HashMap();
						RuleGroup ruleGroup=ruleGroupRepository.findOne(Long.valueOf(ruleGrpList.get(j).toString()));
						if(ruleGroup!=null && ruleGroup.getName()!=null)
							ruleGroupWithRulesMap.put("name", ruleGroup.getName());
						ruleGroupsListMap.add(ruleGroupWithRulesMap);
						allRuleGrpListMap.add(ruleGroupWithRulesMap);
						//						HashMap ruleGroupWithRulesMap = new HashMap();
						//						HashMap ruleMap=new HashMap();
						//						List<RuleGroupDetails> ruleGrpDetailsList=ruleGroupDetailsRepository.findByRuleGroupId(Long.valueOf(ruleGrpList.get(j).toString()));
						//						RuleGroup ruleGroup=ruleGroupRepository.findOne(Long.valueOf(ruleGrpList.get(j).toString()));
						//						ruleGroupWithRulesMap.put("id", ruleGroup.getIdForDisplay());
						//						if(ruleGroup!=null && ruleGroup.getName()!=null)
						//							ruleGroupWithRulesMap.put("name", ruleGroup.getName());
						//						if(ruleGrpDetailsList.size()>0)
						//						{
						//							List<HashMap> map=new ArrayList<HashMap>();
						//							for(RuleGroupDetails ruleGrpDetail:ruleGrpDetailsList)
						//							{
						//								List<Rules> rulesList=new ArrayList<Rules>();
						//
						//								if(ruleGrpDetail.getRuleId()!=null){
						//									Rules rules=rulesRepository.findOne(ruleGrpDetail.getRuleId());
						//									if(rules != null )
						//									{
						//										HashMap newRuleMap=new HashMap();
						//										newRuleMap.put("id", rules.getId());
						//										if(rules.getRuleCode()!=null && !rules.getRuleCode().isEmpty())
						//											newRuleMap.put("ruleCode", rules.getRuleCode());
						//										map.add(newRuleMap);
						//									}
						//								}
						//							}
						//							ruleGroupWithRulesMap.put("rules", map);
						//							ruleGroupsListMap.add(ruleGroupWithRulesMap);
						//						}

					}

					LookUpCode lKCode=lookUpCodeRepository.findByTenantIdAndLookUpCodeAndLookUpType(tenantId,distinct.get(i).toString(),"RULE_GROUP_TYPE");
					if(lKCode != null && lKCode.getMeaning() != null)
						ruleGroupMap.put("rulePurpose", lKCode.getMeaning());
					ruleGroupMap.put("ruleGroups", ruleGroupsListMap);
					ruleGroupMap.put("count", ruleGrpList.size());
					allCount = allCount+ ruleGrpList.size();
					FinalMap.add(ruleGroupMap);
					HashMap allmap =new HashMap();
					allmap = FinalMap.get(0);
					List<HashMap> allm = (List<HashMap>)allmap.get("ruleGroups");
					allm=allRuleGrpListMap;
					allmap.put("ruleGroups", allm) ;

				}

			}


			else
			{
				if(distinct.get(i) != null && !distinct.get(i).toString().isEmpty() && !distinct.get(i).toString().equals(""))
				{
					HashMap map1=new HashMap();
					k=k+1;
					String img="";
					if(distinct.get(i).toString().equalsIgnoreCase("Chase"))
					{
						img=chaseImg;
					}
					else if(distinct.get(i).toString().equalsIgnoreCase("BOA"))
					{
						img=bOAImg;
					}
					else if(distinct.get(i).toString().equalsIgnoreCase("Wellsforgo"))
					{
						img=wellsforgoImg;
					}
					else if(distinct.get(i).toString().equalsIgnoreCase("Citi"))
					{
						img=citiImg;
					}
					else if(distinct.get(i).toString().equalsIgnoreCase("GOOGLE_DRIVE"))
					{
						img=googleDriveImg;
					}
					else if(distinct.get(i).toString().equalsIgnoreCase("DROP_BOX"))
					{
						img=dropBoxImg;
					}
					else if(distinct.get(i).toString().equalsIgnoreCase("SFTP"))
					{
						img=sFTPImg;
					}


					String str=columnName+"='"+distinct.get(i)+"' and tenantId="+tenantId;
					//					List entity=em.createQuery("select u FROM "+tableName+" u"+ " where "+str).getResultList();
					List entityRaw=em.createQuery("FROM "+tableName+ " where "+str).getResultList();

					List entity = new ArrayList();

					if(tableName.equalsIgnoreCase("FileTemplates"))
					{
						for(Object entObj:entityRaw)
						{
							FileTemplates ft = (FileTemplates)entObj;
							FileTempDTO ftDTO = new FileTempDTO();

							ftDTO.setId(ft.getIdForDisplay());
							ftDTO.setTemplateName(ft.getTemplateName());
							ftDTO.setDescription(ft.getDescription());
							ftDTO.setStartDate(ft.getStartDate());
							ftDTO.setEndDate(ft.getEndDate());
							ftDTO.setFileType(ft.getFileType());
							ftDTO.setStatus(ft.getStatus());
							ftDTO.setDelimiter(ft.getDelimiter());
							ftDTO.setSource(ft.getSource());
							ftDTO.setSkipRowsStart(ft.getSkipRowsStart());
							ftDTO.setSkipRowsEnd(ft.getSkipRowsEnd());
							ftDTO.setNumberOfColumns(ft.getNumber_of_columns());
							ftDTO.setHeaderRowNumber(ft.getHeaderRowNumber());

							entity.add(ftDTO);
						}
					}
					else
					{
						entity = entityRaw;
					}

					//log.info("entity.size() :"+entity.size());
					map1.put("S.no",k);
					map1.put(columnName, distinct.get(i));
					String displayName=distinct.get(i).toString().replaceAll("\\W", " ");
					if(columnName.equalsIgnoreCase("connectionType"))
					{
						String lookUpType="CONNECTION_TYPE";
						LookUpCode meaning=lookUpCodeRepository.findByTenantIdAndLookUpCodeAndLookUpType(tenantId,distinct.get(i).toString(),lookUpType);
						map1.put("dispname", meaning.getMeaning());
					}

					else
						map1.put("dispname", displayName);
					map1.put("imagePath",img);
					map1.put("count",entity.size());
					map1.put("Lists", entity);
					allCount = allCount + entity.size();
					FinalMap.add(map1);
				}
			}

		}
		for(HashMap map : FinalMap)
		{
			if(map.containsValue("All"))
			{
				HashMap allmapObj = new HashMap<>();
				allmapObj = map;
				allmapObj.put("count", allCount);
				List entity =null;
				//tableName.class.getAnnotation(Table.);
				//entity=em.createQuery("SELECT * FROM "+tableName+ " WHERE tenant_id = "+tenantId).getResultList();
				allmapObj.put("Lists",entity);
			}
		}


		return FinalMap;
	}

	@GetMapping("/testView/{tableName}/{tenantId}")
	@Timed
	public Query testView(@PathVariable String tableName, @PathVariable Long tenantId)
	{
		log.info("Rest Request to get distinct source details");

		Query distinctList=em.createQuery(" FROM "+tableName);
		log.info("distinctList: "+distinctList);
		return distinctList;
	}
	/*@GetMapping("/fileTemplateswithStatus")
	@Timed
	public List<FileTemplateDTO> fetchFileTemplatesWithStatus(@RequestParam(value="sourceProfileId") Long sourceProfileId)
	{
		log.info("Rest Request to get file templates with source profile id");
		List<FileTemplateDTO> fileTemplatesDTOList = new ArrayList<FileTemplateDTO>();
		List<SourceProfileFileAssignments> spas = new ArrayList<SourceProfileFileAssignments>();
		spas = sourceProfileFileAssignmentsRepository.findBySourceProfileId(sourceProfileId);
		for(SourceProfileFileAssignments spa : spas)
		{
			FileTemplateDTO fileTemplateDTO = new FileTemplateDTO();
			if(spa.getId() != null)
			{
				fileTemplateDTO.setSPAId(spa.getId());
			}
			if(spa.getTemplateId() != null)
			{
				FileTemplates ft = new FileTemplates();
				fileTemplateDTO.setSourceProfileId(sourceProfileId);
				fileTemplateDTO.setFileNameFormat(spa.getFileNameFormat());
				fileTemplateDTO.setFileExtension(spa.getFileExtension());
				fileTemplateDTO.setFileDescription(spa.getFileDescription());
				fileTemplateDTO.setTemplateId(spa.getTemplateId());
				ft = fileTemplatesRepository.findOne(spa.getTemplateId());
				if(ft != null && ft.getTemplateName() != null)
					fileTemplateDTO.setTemplateName(ft.getTemplateName());
				if(ft.getStatus() != null)
					fileTemplateDTO.setTemplateStatus(ft.getStatus());
			}
			if(spa.getHold() != null)
				fileTemplateDTO.setHold(spa.getHold());
			if(spa.getHoldReason() != null)
				fileTemplateDTO.setHoldReason(spa.getHoldReason() );
			if(spa.getSourceDirectoryPath() != null)
				fileTemplateDTO.setSourceDirectoryPath(spa.getSourceDirectoryPath() );
			if(spa.getLocalDirectoryPath() != null)
				fileTemplateDTO.setLocalDirectoryPath(spa.getLocalDirectoryPath());
			fileTemplatesDTOList.add(fileTemplateDTO);
		}
		return fileTemplatesDTOList;
	}*/


	/**
	 * Author : Shobha
	 * @param fileName
	 * @param sourceProfileId
	 * @param fileTemplatesDTOList
	 * @return
	 */
	@GetMapping("/fetchMatchedTemplatesByFileAndProfile")
	@Timed
	public List<String[]> fetchMatchedTemplatesByFileAndProfile(@RequestParam("fileName") String fileName,@RequestParam("fileTemplatesDTOList") String fileTemplatesDTOList)
	{
		log.debug("Rest request to fetch matched templates by file and profile"+fileName);
		List<String[]> matchedTemplatesWithNamesAndId= new ArrayList<String[]> ();

		try{
			ObjectMapper objMapper = new ObjectMapper();
			TypeFactory typeFact = objMapper.getTypeFactory();
			List<FileTemplateDTO> spas = objMapper.readValue(fileTemplatesDTOList, typeFact.constructCollectionType(List.class,FileTemplateDTO.class));
			for(FileTemplateDTO spa : spas)
			{

				String fileNameFormat = null;
				try{
					fileNameFormat = spa.getFileNameFormat();
					if(fileNameFormat.contains("*"))
					{
						fileNameFormat = fileNameFormat.replaceAll("\\*", "(.*)");
					}
				}
				catch(Exception exp)
				{
					log.info("fileNameFormat cannot be null");
				}


				if(fileName.matches(fileNameFormat))
				{
					String[] strArr = new String[3];
					//Process or function
					strArr[0]=spa.getTemplateId().toString();
					strArr[1]=spa.getTemplateName();
					strArr[2]=spa.getSPAid().toString();

					matchedTemplatesWithNamesAndId.add(strArr);
				}
			}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return matchedTemplatesWithNamesAndId;

	}

	/** 
	 * Author Kiran
	 * @param sourceProfileId
	 * @param offset
	 * @param sortColName
	 * @param sortOrder
	 * @param limit
	 * @return
	 * @throws SQLException
	 */
	@GetMapping("/fileTemplateswithStatus")
	@Timed
	public List<HashMap> fetchFileTemplatesWithStatusKiran(@RequestParam(value="sourceProfileId") Long sourceProfileId, @RequestParam(value = "page" , required = false) Integer offset,
			@RequestParam(required = false) String sortColName, @RequestParam(required = false) String sortOrder,@RequestParam(value = "per_page", required = false) Integer limit) throws SQLException
			{
		Connection conn = null;
		Statement stmt = null;
		ResultSet result = null; 
		String schemaName=null;
		List<HashMap> dataMap = new ArrayList<HashMap>();
		try{
			DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
			conn=ds.getConnection();
			stmt = conn.createStatement();
			log.info("Successfully connected to JDBC with schema "+schemaName);

			//HashMap map=userJdbcService.getuserInfoFromToken(request);
			//Long tenantId=Long.parseLong(map.get("tenantId").toString());
			if(limit == null || limit == 0 )
			{
				limit = sourceProfileFileAssignmentsRepository.findBySourceProfileId(sourceProfileId).size();
			}
			if(offset == null )
			{
				offset=0;
			}
			if(sortOrder==null)
				sortOrder="desc";
			if(sortColName==null)
				sortColName="SPAid";
			int offSt = 0;
			offSt = (offset * limit + 1)-1;
			String query = "";

			//create view 
			//			String view="CREATE OR REPLACE VIEW `profileAsmts_and_templates` AS SELECT `spfa`.`id` AS `AsmtId`, `spfa`.`source_directory_path` AS `sourceDirectoryPath`, `spfa`.`local_directory_path` As`LocalDirectoryPath`, `ft`.`id` AS `ftId`, `ft`.`template_name` AS `templateName` , ( select count(*) from `t_source_profile_file_assignments` where source_profile_id= "+sourceProfileId+") AS `count` FROM  ((`t_source_profile_file_assignments` `spfa`  JOIN `t_file_templates` `ft`)  )  WHERE  (`spfa`.`template_id` =  `ft`.`id` ) and `spfa`.source_profile_id = "+sourceProfileId;

			String view="SELECT "
					+ "`spfa`.`id` AS `SPAid`, "
					+ "`spfa`.`source_profile_id` AS `sourceProfileId`, "
					+ "`spfa`.`file_name_format` AS `fileNameFormat`, "
					+ "`spfa`.`file_extension` AS `fileExtension`, "
					+ "`spfa`.`file_description` AS `fileDescription`, "
					+ "`spfa`.`template_id` AS `templateId`, "
					+ "`spfa`.`hold` AS `hold`, "
					+ "`spfa`.`hold_reason` AS `holdReason`, "
					+ "`spfa`.`source_directory_path` AS `sourceDirectoryPath`, "
					+ "`spfa`.`local_directory_path` As`localDirectoryPath`, "
					+ "`ft`.`template_name` AS `templateName` , "
					+ "`ft`.`status` AS `templateStatus`, "
					+ "`ft`.`id` AS `ftId`, "
					+ "( select count(*) from `t_source_profile_file_assignments` "
					+ "where source_profile_id= "+sourceProfileId+") AS `count` FROM  ((`t_source_profile_file_assignments` `spfa`  JOIN `t_file_templates` `ft`)  )  WHERE  (`spfa`.`template_id` =  `ft`.`id` ) and `spfa`.source_profile_id = "+sourceProfileId; 

			query = "SELECT * from ("+view+") AS profileAsmts_and_templates"+" order by "+sortColName +" "+sortOrder +" limit "+offSt+", "+limit;
			stmt.executeQuery(query);
			log.info("sortColName: "+sortColName+" sortOrder: "+sortOrder+" offSt: "+offSt+" limit: "+limit);
			//query ="SELECT * from "+schemaName+".`"+"profileAsmts_and_templates"+"` "+" order by "+sortColName +" "+sortOrder +" limit "+offSt+", "+limit;
			log.info("query: "+query);
			result=stmt.executeQuery(query);
			log.info("query"+query);
			ResultSetMetaData rsmd = result.getMetaData();
			int colCount = rsmd.getColumnCount();
			while(result.next()){
				HashMap hm = new HashMap();
				for(int i=1; i<=colCount; i++)
				{
					hm.put(rsmd.getColumnName(i), result.getString(i));
				}
				dataMap.add(hm);
			}
			log.info("Data Size: "+ dataMap.size());
		}
		catch(Exception e)
		{
			log.info("Exceptin while fetching data: "+ e);
		}
		finally
		{
			//drop view
			//stmt.executeUpdate( "DROP view `"+schemaName+"`.`profileAsmts_and_templates`");

			if(result != null)
				result.close();	
			if(stmt != null)
				stmt.close();
			if(conn != null)
				conn.close();
		}
		return dataMap;}

	@GetMapping("/templatesForTenant")
	@Timed
	public List<FileTemplates> fetchTemplatesForTenant(HttpServletRequest request) throws SQLException
	{
		List<FileTemplates> fileTemps = new ArrayList<FileTemplates>();
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		log.info("Request fetch templates For Tenant: "+tenantId);
		fileTemps = fileTemplatesRepository.findByTenantId(tenantId);
		return fileTemps;
	}

	/**
	 * Author1: Ravali
	 * Author2: Swetha [Added DFR specific template retrieval]
	 * GET  /file-templates : get all the fileTemplates.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of fileTemplates in body
	 * @throws URISyntaxException 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/fileTemplatesByTenantId")
	@Timed
	public List<FileTmpDTO> getAllFileTemplatesDfr(@RequestParam(value = "page" , required = false) Integer offset,
			@RequestParam(value = "per_page", required = false) Integer limit, @RequestParam(required = false) String formType, HttpServletRequest request,
			@RequestParam(required = false) String sortColName, @RequestParam(required = false) String sortOrder) throws URISyntaxException {
		HashMap map0=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map0.get("tenantId").toString());
		log.debug("REST request to get a page of FileTemplates with formType: "+formType+" and tenant" +tenantId);

		HashMap map=new HashMap();
		List<FileTmpDTO> fileTmpList=new ArrayList<FileTmpDTO>();
		List<Long> ftIds=new ArrayList<Long>();
		List<Long> multIdentifierIds=new ArrayList<Long>();
		List<FileTmpDTO> fileTmpListWithDfrDivided=new ArrayList<FileTmpDTO>();
		List<FileTmpDTO> finList=new ArrayList<FileTmpDTO>();
		HashMap indexMap=new HashMap();

		if(sortOrder==null)
			sortOrder="Descending";
		if(sortColName==null)
			sortColName="id";

		map=fileTemplatesService.getAllFileTemplates(offset, limit, formType, tenantId, sortColName, sortOrder);
		fileTmpList=(List<FileTmpDTO>)map.get("fileTmpList");
		int sz=fileTmpList.size(); 
		log.info("fileTmpList sz: "+sz);
		ftIds=(List<Long>)map.get("ftIds"); 
		multIdentifierIds=(List<Long>)map.get("multIdentifierIds"); 
		indexMap=(HashMap) map.get("indexMap"); 
		HashMap MultIdMap=new HashMap();
		MultIdMap=(HashMap)map.get("MultIdMap");
		if(formType==null || (formType!=null && !(formType.equalsIgnoreCase("DATA_VIEW")))){
			finList=fileTmpList;
		}
		else if(formType.equalsIgnoreCase("DATA_VIEW")){
			finList.addAll(fileTmpList); 
			fileTmpListWithDfrDivided.addAll(fileTmpList); 

			if(multIdentifierIds!=null && multIdentifierIds.size()>0){

				for(int i=0;i<multIdentifierIds.size();i++)
				{ 
					Long ftId=multIdentifierIds.get(i); 
					FileTemplates ft=fileTemplatesRepository.findOne(ftId);
					int index2=Integer.parseInt(indexMap.get(ftId).toString()); 
					FileTmpDTO ftDto=fileTmpListWithDfrDivided.get(index2); 
					finList.remove(ftDto);
					List<IntermediateTable> itList=intermediateTableRepository.findByTemplateId(ftId); 
					String rowIdentifier="";

					FileTmpDTO dto=new FileTmpDTO();
					dto=(FileTmpDTO) MultIdMap.get(ftId);
					String templateName=ft.getTemplateName();
					List<FileTmpDTO> finListNew=new ArrayList<FileTmpDTO>();

					for(int j=0;j<itList.size();j++)
					{
						FileTmpDTO fnw = new FileTmpDTO();

						IntermediateTable it=itList.get(j); 
						Long intermediateId=it.getId();
						rowIdentifier=it.getRowIdentifier(); 

						String dfrTempName=templateName+"-"+rowIdentifier;
						dto.setTemplateName(dfrTempName);

						fnw.setId(dto.getId());
						fnw.setTemplateName(dfrTempName);
						fnw.setDescription(dto.getDescription());
						fnw.setStartDate(dto.getStartDate());
						fnw.setEndDate(dto.getEndDate());
						/** active check**/
		    			Boolean activeStatus=activeStatusService.activeStatus(dto.getStartDate(), dto.getEndDate(),Boolean.valueOf(dto.getStatus()));
		    			fnw.setStatus(activeStatus.toString());
		    			/** active check end**/
						
						
						fnw.setFileType(dto.getFileType());
						fnw.setDelimiter(dto.getDelimiter());
						fnw.setSource(dto.getSource());
						fnw.setSkipRowsStart(dto.getSkipRowsStart());
						fnw.setSkipRowsEnd(dto.getSkipRowsEnd());
						fnw.setNumberOfColumns(dto.getNumberOfColumns());
						fnw.setHeaderRowNumber(dto.getHeaderRowNumber());
						fnw.setTenantId(dto.getTenantId());
						fnw.setCreatedBy(dto.getCreatedBy());
						fnw.setCreatedDate(dto.getCreatedDate());
						fnw.setLastUpdatedBy(dto.getLastUpdatedBy());
						fnw.setLastUpdatedDate(dto.getLastUpdatedDate());
						fnw.setData(dto.getData());
						fnw.setSdFilename(dto.getSdFilename());
						fnw.setRowIdentifier(rowIdentifier);
						fnw.setEndDated(dto.getEndDated());
						fnw.setMultipleIdentifier(dto.getMultipleIdentifier());
						fnw.setIndex(dto.getIndex());
						fnw.setIntermediateId(intermediateId);
						finListNew.add(fnw); 

						rowIdentifier="";
					}
					for(int k=0;k<itList.size() && k<finListNew.size();k++)
					{
						finList.add(finListNew.get(k));
					}
				}
			}
			log.info("finList sz: "+finList.size());
		}
		return finList;
	}
	/**
	 * Author : Shobha
	 * @param tenantId
	 * @param userId
	 * @param tableName
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 * @throws IOException 
	 */
	/*	@PostMapping("/loadDefaultTemplatesForCurrentTenant")
	@Timed
	public void loadDefaultTemplatesForCurrentTenant(@RequestParam Long tenantId, @RequestParam Long userId) throws IOException
	{
		log.info("Load template definition for tenant"+tenantId);
		//log.info("Table.class"+Table.class);
		Table table = FileTemplates.class.getAnnotation(Table.class);
		String tableName = table.name();
		log.info("tableName"+tableName);
		log.info("FileTemplates.class"+FileTemplates.class);
		InputStreamReader inputStream = new  InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("jsonFile/FileTemplates.csv"));
		if(inputStream!=null)
		{
			CSVReader csvReader = new CSVReader(inputStream, ',' , '"');
			List<String[]> templateRows;
			templateRows = csvReader.readAll();
			try {
			csvReader.close();
			log.info("Template rows size in file"+ templateRows.size());
			if(templateRows.size()>0)
			{
				String[] header = templateRows.get(0);
				for(int i=0; i<header.length; i++)
				{
					log.info("header-"+i+" "+header[i]);
				}
			}
			}
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
	 */

	/*@PostMapping("/loadDefaultTemplatesForCurrentTenant")
	@Timed
	public List<HashMap<String,String>> loadDefaultTemplatesForCurrentTenant( @RequestParam MultipartFile multipart, HttpServletRequest request,@RequestParam String dispNameToDomainName,
		@RequestParam String domainNames) throws JsonParseException, JsonMappingException, IOException
	{
		log.info("Rest request to load templates for current tenant");

		HashMap map=userJdbcService.getuserInfoFromToken(request);
      	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	Long userId=Long.parseLong(map.get("userId").toString());
    	List<HashMap<String,String>> rows = new ArrayList<HashMap<String,String>>();
    	 Map<String, String> dispNameToDomainColNameMap =new HashMap<String,String>();
    	 try
    	    {
    	        ObjectMapper mapper = new ObjectMapper();
    	        dispNameToDomainColNameMap = mapper.readValue(dispNameToDomainName,Map.class);
    	    }
    	    catch (Exception e)
    	    {
    	        throw new RuntimeException("Couldnt parse json:" + dispNameToDomainName, e);
    	    }
    	try{
    		CSVReader csvReader = new CSVReader(new InputStreamReader(multipart.getInputStream()), ',' , '"');
    		HashMap<String,String> domainColNameToTableColNameMap = new HashMap<String,String>();

    		//Class classVar = Class.forName(domainName); 
    		String className = "com.nspl.app.domain.FileTemplates";

    		Class<?> entityClass = Class.forName(className);

    		Annotation[] annotation = entityClass.getAnnotations();

	 *//*************************************** Get Fields from domain and column names of table *********************************************************//*
    			Table table = entityClass.getAnnotation(Table.class);
    			String tableName = table.name();
    			Field[] fields = entityClass.getDeclaredFields();
    			for(int f=0;f<fields.length;f++)
    			{
    				//log.info("fields[f]"+fields[f]);
    				Class type = fields[f].getType();
    				  String name = fields[f].getName();
    				  Column column = fields[f].getAnnotation(Column.class);
    				   if (column != null)
    				   {
    					   domainColNameToTableColNameMap.put(name, column.name());
    					   //log.info(column.name());
    				   }
    			}

	  *//**********************************************************************************************************************//*

	   *//**********************************************Read header and then read rows starting index from 1************************************************************************//*

    			List<String[]> rowsList = csvReader.readAll();
    			if(rowsList.size()>0)
    			{
    				log.info("Getting Header Row Indexes...");
    				String[] header = rowsList.get(0);

	    *//***********Split the columns of different tables and store in a map*********************//*
    				HashMap<String,String[]> headerMaps = new HashMap<String,String[]>();
    				headerMaps = splitHeaders(header);

	     *//***************************************************************************************//*
//    				for(int i=0; i<header.length; i++)
//    				{
//    					log.info("header[i]"+header[i]+" and domain value is :"+dispNameToDomainColNameMap.get(header[i]));
//    				}

    				for(int rowIndex=1;rowIndex<rowsList.size();rowIndex++)
    				{
    					HashMap<String,String> rowMap = new HashMap<String,String>();
    					for(String key : headerMaps.keySet())
    					{
    						String[] headerValues = headerMaps.get(key);
    						try{
    							//String[] row = rowsList.get(rowIndex);
    							for(int colIndex=0;colIndex<headerValues.length;colIndex++)
    							{
    								log.info("headerValues[colIndex]"+headerValues[colIndex]);
    								if(headerValues[colIndex] == null || headerValues[colIndex].isEmpty())
    								{

    								}
    								else if(headerValues[colIndex]!=null)
    								{
    									if(headerValues[colIndex] != null && !headerValues[colIndex].isEmpty() && dispNameToDomainColNameMap.get(headerValues[colIndex])!=null && 
    											dispNameToDomainColNameMap.get(headerValues[colIndex]).toString() != "")
    									rowMap.put(dispNameToDomainColNameMap.get(headerValues[colIndex]).toString(),headerValues[colIndex]);
    								}
    							}
    							rows.add(rowMap);
        					}
        					catch(DateTimeParseException e)
    						{
    							break;
    						}
    						catch(Exception e)
    						{
    							break;
    						}
    					}

    				}
    				//return rows;
    			}
    			log.info("rows.size()"+rows.size());
    			List<String> defaultColumnsListToSet = new ArrayList<String>();
    			defaultColumnsListToSet.add("Status");
    			defaultColumnsListToSet.add("enableFlag");
    			defaultColumnsListToSet.add("enabledFlag");
    			defaultColumnsListToSet.add("tenantId");
    			defaultColumnsListToSet.add("createdBy");
    			defaultColumnsListToSet.add("createdDate");
    			defaultColumnsListToSet.add("lastUpdatedBy");
    			defaultColumnsListToSet.add("lastUpdatedDate");

    			List<String> relationalColumns = new ArrayList<String>();

	      *//**********************************************************************************************************************//*
    			List<String> insertQueriesList = new ArrayList<String>();
    			for(int i=0;i<rows.size();i++)
    			{
    				HashMap<String,String> row = new HashMap<String,String>();
    				row=rows.get(i);
    				String query =  "insert into "+tableName;
    				String keys = "(";
    				String values = "(";
    				int keyIndex=0;
    				for (String key : row.keySet()) {
    					log.info("key set size"+row.keySet().size());
    					if(key !=null && !key.isEmpty() && row.get(key)!=null && !row.get(key).isEmpty())
    					{
    						String tableColumn =  domainColNameToTableColNameMap.get(key);
    						log.info("key :"+key+"Value :"+row.get(key) +"tableColumn :"+tableColumn);

        					if(keyIndex==(row.keySet().size()-1))
        						keys = keys + tableColumn ;
        					else
        						keys = keys +  tableColumn +",";

        					log.info("keyIndex"+keyIndex);
        					log.info("row.getkeyset size"+(row.keySet().size()-1));

        					if(keyIndex==(row.keySet().size()-1))
        					{
        						if(row.get(key).equalsIgnoreCase("y") || row.get(key).equalsIgnoreCase("n") || row.get(key).equalsIgnoreCase("true") || row.get(key).equalsIgnoreCase("false"))
        						{
        							values = values  + row.get(key)  ;
        						}
        						else
        						values = values + "'" + row.get(key) + "'" ;
        					}
        					else
        					{
        						if(row.get(key).equalsIgnoreCase("y") || row.get(key).equalsIgnoreCase("n") || row.get(key).equalsIgnoreCase("true") || row.get(key).equalsIgnoreCase("false"))
        						{
        							values = values  + row.get(key)   + ",";
        						}
        						else
        						values = values + "'" + row.get(key) +  "'"  + ",";
        					}

        					keyIndex=keyIndex+1;
    					}


    				}
    				keyIndex = 0; *//**********Nullify the key index to set default columns**************//*
    				for (String key : domainColNameToTableColNameMap.keySet()) 
        				{

    					if(keyIndex == 0)
						{
							keys = keys + ",";
							values = values + ",";
							log.info("keys at start"+keys);
							log.info("values at start"+values);
						}
        					if(defaultColumnsListToSet.contains(key))
        					{

        						String tableColumn =  domainColNameToTableColNameMap.get(key);
        						if(keyIndex==(domainColNameToTableColNameMap.keySet().size()-1))
            						keys = keys + tableColumn ;
            					else
            						keys = keys +  tableColumn +",";

        						if(keyIndex==(domainColNameToTableColNameMap.keySet().size()-1))
        						{
        							if(key.endsWith("By"))
        								values = values + "'" + userId + "'" ;
        							if(key.endsWith("Id"))
            							values = values + "'" + tenantId + "'" ;
        							if(key.endsWith("Date"))
        							{
        								//SimpleDateFormat dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        								log.info("Local Date"+new LocalDateTime());
        								log.info("zoned Date"+ZonedDateTime.now());
        								log.info("Date time"+new DateTime());
        								SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        								Date date = new Date();
        								String currentDateTime = dateFormat.format(date);
        								values = values + "'" + currentDateTime;
        							}
        							if(key.endsWith("Flag"))
            							values = values + "TRUE" ;

        							if(key.equalsIgnoreCase("status"))
        							{
            							values = values + "'" + "Active" + "'" + ",";
        							}

        						}
            					else
            					{
            						if(key.equalsIgnoreCase("status"))
        							{
            							values = values + "'" + "Active" + "'" + ",";
        							}
            						if(key.endsWith("By"))
        								values = values + "'" + userId + "'" + ",";
        							if(key.endsWith("Id"))
            							values = values + "'" + tenantId + "'"+ "," ;
        							if(key.endsWith("Date"))
        							{
        								SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        								Date date = new Date();
        								String currentDateTime = dateFormat.format(date);
        								log.info("Local Date"+new LocalDateTime());
        								log.info("zoned Date"+ZonedDateTime.now());
        								log.info("Date time"+new DateTime());
        								values = values  + "'" + currentDateTime + "'" + "," ;
        							}
        							if(key.endsWith("Flag"))
            							values = values + "TRUE" + ",";
            					}
        					}
        					keyIndex=keyIndex+1;
        				}
    				log.info("keys bef sub string"+keys);
    				if(keys.endsWith(","))
    				{
    					keys = keys.substring(0, keys.length() - 1);
    				}
    				log.info("keys after sub string"+keys);
    				log.info("values bef sub string"+values);
    				if(values.endsWith(","))
    				{
    					values = values.substring(0, values.length() - 1);
    				}
    				log.info("values after sub string"+values);
    				keys = keys +")";
    				values = values +")";
    				query = query + keys+" values "+ values;
    				insertQueriesList.add(query);
    			}
    			for(int i=0;i<insertQueriesList.size();i++)
    			{
    				log.info("query=>"+i+"------"+insertQueriesList.get(i));
    			}


    				 *//*******************************************Create connection**********************************************************//*
    			String dbUrl=env.getProperty("spring.datasource.url");
				String[] parts=dbUrl.split("[\\s@&?$+-]+");
				String host = parts[0].split("/")[2].split(":")[0];
				log.info("host :"+host);
				String schemaName=parts[0].split("/")[3];
				log.info("schemaName :"+schemaName);
				String userName = env.getProperty("spring.datasource.username");
				String password = env.getProperty("spring.datasource.password");
				String jdbcDriver = env.getProperty("spring.datasource.jdbcdriver");
				Connection conn = null;
				PreparedStatement preparedStatement = null;
				conn = DriverManager.getConnection(dbUrl, userName, password);
				log.info("Connected database successfully...");

				ResultSet result2=null;
				try{
					for(int i=0;i<insertQueriesList.size();i++)
	    			{
						preparedStatement = conn.prepareStatement(insertQueriesList.get(i),PreparedStatement.RETURN_GENERATED_KEYS);
						preparedStatement.executeUpdate();
						 ResultSet rs = preparedStatement.getGeneratedKeys();
						 if(rs.next()) {
							 //In this exp, the autoKey val is in 1st col
							 Long id=rs.getLong(1);
							 //now this's a real value of col Id
							 log.info("saved id is:"+id);

    				  *//******************************************************************************************//*
							 String idForDisplay = IDORUtils.computeFrontEndIdentifier(id.toString());
							 FileTemplates fileTemp = new FileTemplates();
							 fileTemp = fileTemplatesRepository.findOne(id);
							 fileTemp.setIdForDisplay(idForDisplay);
							 log.info("idForDisplay"+idForDisplay);
							 fileTemp = fileTemplatesRepository.save(fileTemp);
    				   *//******************************************************************************************//*
						 }
					}
				}
				catch (SQLException e) {

					System.out.println(e.getMessage());

				} finally {

					if (preparedStatement != null) {
						preparedStatement.close();
					}

					if (conn != null) {
						conn.close();
					}

				}



    				    *//**********************************************************************************************************************//*

    	}
    	catch(Exception e)
    	{
    		log.info("Exception: "+e);
    	}
		return rows;
	}*/

	public HashMap<String,String[]> splitHeaders(String[] header)
	{
		HashMap<String,String[]> headerMaps = new HashMap<String,String[]>();
		String[] headerList = new String[header.length];
		int headerIndex =0;
		log.info("header.length"+header.length);
		for(int i=0; i<header.length; i++)
		{
			log.info("header[i]"+header[i]);
			if(header[i] == null || header[i].equalsIgnoreCase("") || header[i].isEmpty())
			{
				headerMaps.put("header-"+headerIndex,headerList);
				headerIndex = headerIndex+1;
				headerList = new String[header.length];
			}
			else
			{
				headerList[i]=(header[i]);
			}
		}
		return headerMaps;
	}

	@PostMapping("/loadDefaultTemplatesForCurrentTenant")
	@Timed
	public List<HashMap<String,String>> loadDefaultTemplatesForCurrentTenant( @RequestParam MultipartFile multipart, HttpServletRequest request,@RequestParam String dispNameToDomainName,
			@RequestParam String domainNames) throws JsonParseException, JsonMappingException, IOException
			{
		log.info("Rest request to load templates for current tenant");

		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		Long userId=Long.parseLong(map.get("userId").toString());
		List<HashMap<String,String>> rows = new ArrayList<HashMap<String,String>>();
		Map<String, String> dispNameToDomainColNameMap =new HashMap<String,String>();
		List<HashMap<String,List<HashMap<String,String>>>> domainToFieldsMap = new ArrayList<HashMap<String,List<HashMap<String,String>>>>();
		try
		{
			ObjectMapper mapper = new ObjectMapper();
			dispNameToDomainColNameMap = mapper.readValue(dispNameToDomainName,Map.class);
		}
		catch (Exception e)
		{
			throw new RuntimeException("Couldnt parse json:" + dispNameToDomainName, e);
		}
		try{
			CSVReader csvReader = new CSVReader(new InputStreamReader(multipart.getInputStream()), ',' , '"');
			HashMap<String,String> domainColNameToTableColNameMap = new HashMap<String,String>();

			//Class classVar = Class.forName(domainName); 
			String className = "com.nspl.app.domain.FileTemplates";

			Class<?> entityClass = Class.forName(className);

			Annotation[] annotation = entityClass.getAnnotations();

			/*************************************** Get Fields from domain and column names of table *********************************************************/
			Table table = entityClass.getAnnotation(Table.class);
			String tableName = table.name();
			Field[] fields = entityClass.getDeclaredFields();
			for(int f=0;f<fields.length;f++)
			{
				//log.info("fields[f]"+fields[f]);
				Class type = fields[f].getType();
				String name = fields[f].getName();
				Column column = fields[f].getAnnotation(Column.class);
				if (column != null)
				{
					domainColNameToTableColNameMap.put(name, column.name());
					//log.info(column.name());
				}
			}

			/**********************************************************************************************************************/

			/**********************************************Read header and then read rows starting index from 1************************************************************************/

			List<String[]> rowsList = csvReader.readAll();
			if(rowsList.size()>0)
			{
				log.info("Getting Header Row Indexes...");
				String[] header = rowsList.get(0);

				/***********Split the columns of different tables and store in a map*********************/
				HashMap<String,String[]> headerMaps = new HashMap<String,String[]>();
				headerMaps = splitHeaders(header);

				/***************************************************************************************/
				//    				for(int i=0; i<header.length; i++)
				//    				{
				//    					log.info("header[i]"+header[i]+" and domain value is :"+dispNameToDomainColNameMap.get(header[i]));
				//    				}

				for(int rowIndex=1;rowIndex<rowsList.size();rowIndex++)
				{
					HashMap<String,String> rowMap = new HashMap<String,String>();
					for(String key : headerMaps.keySet())
					{
						String[] headerValues = headerMaps.get(key);
						try{
							//String[] row = rowsList.get(rowIndex);
							for(int colIndex=0;colIndex<headerValues.length;colIndex++)
							{
								log.info("headerValues[colIndex]"+headerValues[colIndex]);
								if(headerValues[colIndex] == null || headerValues[colIndex].isEmpty())
								{

								}
								else if(headerValues[colIndex]!=null)
								{
									if(headerValues[colIndex] != null && !headerValues[colIndex].isEmpty() && dispNameToDomainColNameMap.get(headerValues[colIndex])!=null && 
											dispNameToDomainColNameMap.get(headerValues[colIndex]).toString() != "")
										rowMap.put(dispNameToDomainColNameMap.get(headerValues[colIndex]).toString(),headerValues[colIndex]);
								}
							}
							rows.add(rowMap);
						}
						catch(DateTimeParseException e)
						{
							break;
						}
						catch(Exception e)
						{
							break;
						}
					}

				}
				//return rows;
			}
			log.info("rows.size()"+rows.size());
			List<String> defaultColumnsListToSet = new ArrayList<String>();
			defaultColumnsListToSet.add("Status");
			defaultColumnsListToSet.add("enableFlag");
			defaultColumnsListToSet.add("enabledFlag");
			defaultColumnsListToSet.add("tenantId");
			defaultColumnsListToSet.add("createdBy");
			defaultColumnsListToSet.add("createdDate");
			defaultColumnsListToSet.add("lastUpdatedBy");
			defaultColumnsListToSet.add("lastUpdatedDate");

			List<String> relationalColumns = new ArrayList<String>();

			/**********************************************************************************************************************/
			List<String> insertQueriesList = new ArrayList<String>();
			for(int i=0;i<rows.size();i++)
			{
				HashMap<String,String> row = new HashMap<String,String>();
				row=rows.get(i);
				String query =  "insert into "+tableName;
				String keys = "(";
				String values = "(";
				int keyIndex=0;
				for (String key : row.keySet()) {
					log.info("key set size"+row.keySet().size());
					if(key !=null && !key.isEmpty() && row.get(key)!=null && !row.get(key).isEmpty())
					{
						String tableColumn =  domainColNameToTableColNameMap.get(key);
						log.info("key :"+key+"Value :"+row.get(key) +"tableColumn :"+tableColumn);

						if(keyIndex==(row.keySet().size()-1))
							keys = keys + tableColumn ;
						else
							keys = keys +  tableColumn +",";

						log.info("keyIndex"+keyIndex);
						log.info("row.getkeyset size"+(row.keySet().size()-1));

						if(keyIndex==(row.keySet().size()-1))
						{
							if(row.get(key).equalsIgnoreCase("y") || row.get(key).equalsIgnoreCase("n") || row.get(key).equalsIgnoreCase("true") || row.get(key).equalsIgnoreCase("false"))
							{
								values = values  + row.get(key)  ;
							}
							else
								values = values + "'" + row.get(key) + "'" ;
						}
						else
						{
							if(row.get(key).equalsIgnoreCase("y") || row.get(key).equalsIgnoreCase("n") || row.get(key).equalsIgnoreCase("true") || row.get(key).equalsIgnoreCase("false"))
							{
								values = values  + row.get(key)   + ",";
							}
							else
								values = values + "'" + row.get(key) +  "'"  + ",";
						}

						keyIndex=keyIndex+1;
					}


				}
				keyIndex = 0; /**********Nullify the key index to set default columns**************/
				for (String key : domainColNameToTableColNameMap.keySet()) 
				{

					if(keyIndex == 0)
					{
						keys = keys + ",";
						values = values + ",";
						log.info("keys at start"+keys);
						log.info("values at start"+values);
					}
					if(defaultColumnsListToSet.contains(key))
					{

						String tableColumn =  domainColNameToTableColNameMap.get(key);
						if(keyIndex==(domainColNameToTableColNameMap.keySet().size()-1))
							keys = keys + tableColumn ;
						else
							keys = keys +  tableColumn +",";

						if(keyIndex==(domainColNameToTableColNameMap.keySet().size()-1))
						{
							if(key.endsWith("By"))
								values = values + "'" + userId + "'" ;
							if(key.endsWith("Id"))
								values = values + "'" + tenantId + "'" ;
							if(key.endsWith("Date"))
							{
								//SimpleDateFormat dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								log.info("Local Date"+new LocalDateTime());
								log.info("zoned Date"+ZonedDateTime.now());
								log.info("Date time"+new DateTime());
								SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								Date date = new Date();
								String currentDateTime = dateFormat.format(date);
								values = values + "'" + currentDateTime;
							}
							if(key.endsWith("Flag"))
								values = values + "TRUE" ;

							if(key.equalsIgnoreCase("status"))
							{
								values = values + "'" + "Active" + "'" + ",";
							}

						}
						else
						{
							if(key.equalsIgnoreCase("status"))
							{
								values = values + "'" + "Active" + "'" + ",";
							}
							if(key.endsWith("By"))
								values = values + "'" + userId + "'" + ",";
							if(key.endsWith("Id"))
								values = values + "'" + tenantId + "'"+ "," ;
							if(key.endsWith("Date"))
							{
								SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								Date date = new Date();
								String currentDateTime = dateFormat.format(date);
								log.info("Local Date"+new LocalDateTime());
								log.info("zoned Date"+ZonedDateTime.now());
								log.info("Date time"+new DateTime());
								values = values  + "'" + currentDateTime + "'" + "," ;
							}
							if(key.endsWith("Flag"))
								values = values + "TRUE" + ",";
						}
					}
					keyIndex=keyIndex+1;
				}
				log.info("keys bef sub string"+keys);
				if(keys.endsWith(","))
				{
					keys = keys.substring(0, keys.length() - 1);
				}
				log.info("keys after sub string"+keys);
				log.info("values bef sub string"+values);
				if(values.endsWith(","))
				{
					values = values.substring(0, values.length() - 1);
				}
				log.info("values after sub string"+values);
				keys = keys +")";
				values = values +")";
				query = query + keys+" values "+ values;
				insertQueriesList.add(query);
			}
			for(int i=0;i<insertQueriesList.size();i++)
			{
				log.info("query=>"+i+"------"+insertQueriesList.get(i));
			}


			/*******************************************Create connection**********************************************************/
			/*String dbUrl=env.getProperty("spring.datasource.url");
				String[] parts=dbUrl.split("[\\s@&?$+-]+");
				String host = parts[0].split("/")[2].split(":")[0];
				log.info("host :"+host);
				String schemaName=parts[0].split("/")[3];
				log.info("schemaName :"+schemaName);
				String userName = env.getProperty("spring.datasource.username");
				String password = env.getProperty("spring.datasource.password");
				String jdbcDriver = env.getProperty("spring.datasource.jdbcdriver");
				Connection conn = null;
				PreparedStatement preparedStatement = null;
				conn = DriverManager.getConnection(dbUrl, userName, password);
				log.info("Connected database successfully...");

				ResultSet result2=null;
				try{
					for(int i=0;i<insertQueriesList.size();i++)
	    			{
						preparedStatement = conn.prepareStatement(insertQueriesList.get(i),PreparedStatement.RETURN_GENERATED_KEYS);
						preparedStatement.executeUpdate();
						 ResultSet rs = preparedStatement.getGeneratedKeys();
						 if(rs.next()) {
							 //In this exp, the autoKey val is in 1st col
							 Long id=rs.getLong(1);
							 //now this's a real value of col Id
							 log.info("saved id is:"+id);

			 *//******************************************************************************************//*
							 String idForDisplay = IDORUtils.computeFrontEndIdentifier(id.toString());
							 FileTemplates fileTemp = new FileTemplates();
							 fileTemp = fileTemplatesRepository.findOne(id);
							 fileTemp.setIdForDisplay(idForDisplay);
							 log.info("idForDisplay"+idForDisplay);
							 fileTemp = fileTemplatesRepository.save(fileTemp);
			  *//******************************************************************************************//*
						 }
					}
				}
				catch (SQLException e) {

					System.out.println(e.getMessage());

				} finally {

					if (preparedStatement != null) {
						preparedStatement.close();
					}

					if (conn != null) {
						conn.close();
					}

				}
			   */


			/**********************************************************************************************************************/

		}
		catch(Exception e)
		{
			log.info("Exception: "+e);
		}
		return rows;
			}	


	@GetMapping("/uploadCSVToTable")
	@Timed
	public void uploadCSVToTable(@RequestParam String str, HttpServletRequest request)
	{


	}

	/**
	 * Author : Shobha
	 * @param request
	 * @param source
	 * @return
	 */
	@GetMapping("/fetchTemplatesBySource")
	@Timed
	public List<FileTemplateDTO> fetchTemplatesBySource(HttpServletRequest request,@RequestParam String source)
	{
		log.info("REst request to get templates by source "+source);
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long userId=Long.parseLong(map.get("userId").toString());
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		List<FileTemplateDTO> fileTemps = new ArrayList<FileTemplateDTO>();
		List<FileTemplates> temps = new ArrayList<FileTemplates>();
		temps = fileTemplatesRepository.findBySourceAndTenantId(source,tenantId);
		for(FileTemplates temp : temps){
			FileTemplateDTO tempDTO = new FileTemplateDTO();
			tempDTO.setIdForDisplay(temp.getIdForDisplay());
			tempDTO.setSource(temp.getSource());
			tempDTO.setTemplateName(temp.getTemplateName());
			if(temp.getDefaultTemplate() == null || temp.getDefaultTemplate() == false){
				tempDTO.setDefaultTemplate(false);	
			}else{
				tempDTO.setDefaultTemplate(true);	
			}
			
			fileTemps.add(tempDTO);
		}
		return fileTemps;

	}


	/*************
	 * Author Kiran
	 * Find
	 *************/
	@GetMapping("/fileTemplateLinesForBAi2Format")
	@Timed
	public List<FileTemplateDataDTO> presentingTemplateLinesFromJson(HttpServletRequest request,
			@RequestParam(value="delimiter", required=false) String delimiter ,
			@RequestParam(value="fileType", required=false) String fileType)
			{

		List<FileTemplateDataDTO> ListOfFileTemplatesData = new ArrayList<FileTemplateDataDTO>();

		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		log.info("Rest request to get template lines of Bai2 based File Format for tenant: "+tenantId);

		if(fileType!=null && fileType.equalsIgnoreCase("BAI2"))
		{
			log.info("Setting Columns for BAI2 File Format");
			FileTemplateDataDTO fileTemplatesData = new FileTemplateDataDTO();

			fileTemplatesData.setFileType(fileType);
			int val=0;
			try{
				val=Integer.valueOf(delimiter);
				log.info("Delimiter value: "+String.valueOf(val)+" and tenantId "+tenantId);
			}
			catch (NumberFormatException e) 
			{
				log.info("NumberFormatException for UserId: " + e.getMessage());
			}

			LookUpCode lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("DELIMITER",String.valueOf(val),tenantId);
			if(lookUpCode!=null && lookUpCode.getDescription()!=null)
			{
				fileTemplatesData.setDelimiter(lookUpCode.getLookUpCode());
				fileTemplatesData.setDelimeterDescription(lookUpCode.getDescription());
			}

			JSONParser parser = new JSONParser();
			List<Object> objList = null;
			try 
			{
				objList = (List<Object>) parser.parse(new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("jsonFile/BAI2Columns.json"))));
				log.info("Size of Json Object "+objList.size());
				List<String> headersList = new ArrayList<String>(); 
				List<FileTemplateLinesDTO> listOfFileTemplateLines = new ArrayList<FileTemplateLinesDTO>();
				for(Object obj : objList)
				{
					JSONObject jsonObject = (JSONObject) obj;
					JSONArray columnNames = (JSONArray) jsonObject.get("columns");
					int indx_num=0;
					for(int i = 0; i < columnNames.size();i++) 
					{
						JSONObject innerObj = (JSONObject) columnNames.get(i);
						//String colNum= (String) innerObj.get("columnNumber");
						String colHeader= (String) innerObj.get("columnHeader");
						String recordStrtRow= (String) innerObj.get("recordStartRow");
						String recordType= (String) innerObj.get("recordType");
						String dateFormat=null;
						if(innerObj.get("dateFormat")!=null)
						{
							dateFormat=(String) innerObj.get("dateFormat");
						}

						FileTemplateLinesDTO fileTemplatelines= new FileTemplateLinesDTO();

						indx_num=i+1;
						fileTemplatelines.setColumnNumber(indx_num);
						if(i<9)
						{
							fileTemplatelines.setMasterTableReferenceColumn("FIELD_0"+(indx_num));
						}
						else{
							fileTemplatelines.setMasterTableReferenceColumn("FIELD_"+(indx_num));
						}
						//log.info("recordStrtRow: "+recordStrtRow);//+" and recordIdentifier: "+recordIdentifier);
						fileTemplatelines.setRecordStartRow(recordStrtRow);
						fileTemplatelines.setColumnHeader(colHeader);
						fileTemplatelines.setRecordTYpe(recordType);
						fileTemplatelines.setDateFormat(dateFormat);
						headersList.add(colHeader);

						//	log.info("columnNames.size(): "+columnNames.size()+" and i: "+i);
						if(i == (columnNames.size()-1))
						{
							fileTemplatelines.setLastMasterTableRefCol(true);
							fileTemplatelines.setLastColNumber(true);
						}
						listOfFileTemplateLines.add(fileTemplatelines);
						//log.info("===> key: "+colNum+" Value: "+colHeader);
					}
				}

				fileTemplatesData.setHeaders(headersList);
				fileTemplatesData.setStatus("Success");
				fileTemplatesData.setTemplateLines(listOfFileTemplateLines);
				ListOfFileTemplatesData.add(fileTemplatesData);
				return ListOfFileTemplatesData;
			} 
			catch (Exception e) 
			{
				log.error("Exception while Parsing BAI2 Json File");
				e.printStackTrace();
			}
		}
		else if(fileType!=null && (fileType.equalsIgnoreCase("BAI2_ACH") || fileType.equalsIgnoreCase("BAI2_LOCKBOX") || fileType.equalsIgnoreCase("MT940_Standard")))
		{
			log.info("Setting Columns for BAI2_ACH/BAI2_LOCKBOX File Format:- "+fileType);
			FileTemplateDataDTO fileTemplatesData = new FileTemplateDataDTO();

			fileTemplatesData.setFileType(fileType);
			fileTemplatesData.setDelimiter(null);
			fileTemplatesData.setDelimeterDescription(null);

			JSONParser parser = new JSONParser();
			List<Object> objList = null;
			try 
			{
				if(fileType.equalsIgnoreCase("BAI2_ACH"))
					objList = (List<Object>) parser.parse(new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("jsonFile/BAI2_ACH_Columns.json"))));
				else if(fileType.equalsIgnoreCase("BAI2_LOCKBOX"))
					objList = (List<Object>) parser.parse(new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("jsonFile/BAI2_LOCKBOX_Columns.json"))));
				else if(fileType.equalsIgnoreCase("MT940_Standard"))
					objList = (List<Object>) parser.parse(new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("jsonFile/MT940_STANDARD.json"))));
				log.info("Size of Json Object (Number of Template Lines)  for "+fileType+" is "+objList.size());

				List<String> headersList = new ArrayList<String>(); 
				List<FileTemplateLinesDTO> listOfFileTemplateLines = new ArrayList<FileTemplateLinesDTO>();
				for(Object obj : objList)
				{
					JSONObject jsonObject = (JSONObject) obj;
					JSONArray columnNames = (JSONArray) jsonObject.get("columns");
					int indx_num=0;
					for(int i = 0; i < columnNames.size();i++) 
					{
						JSONObject innerObj = (JSONObject) columnNames.get(i);
						String colHeader= (String) innerObj.get("columnHeader");
						String recordStrtRow= (String) innerObj.get("recordStartRow");
						String recordType= (String) innerObj.get("recordType");
						String posBegin= (String) innerObj.get("positionBegin");
						String posEnd= (String) innerObj.get("positionEnd");
						String dateFormat=null;
						if(innerObj.get("dateFormat")!=null)
						{
							dateFormat=(String) innerObj.get("dateFormat");
						}

						FileTemplateLinesDTO fileTemplatelines= new FileTemplateLinesDTO();

						indx_num=i+1;
						fileTemplatelines.setColumnNumber(indx_num);
						if(i<9)
						{
							fileTemplatelines.setMasterTableReferenceColumn("FIELD_0"+(indx_num));
						}
						else{
							fileTemplatelines.setMasterTableReferenceColumn("FIELD_"+(indx_num));
						}
						//log.info("recordStrtRow: "+recordStrtRow);//+" and recordIdentifier: "+recordIdentifier);
						fileTemplatelines.setRecordStartRow(recordStrtRow);
						fileTemplatelines.setColumnHeader(colHeader);
						fileTemplatelines.setRecordTYpe(recordType);
						fileTemplatelines.setPositionBegin(Integer.parseInt(posBegin));
						fileTemplatelines.setPositionEnd(Integer.parseInt(posEnd));
						fileTemplatelines.setDateFormat(dateFormat);
						headersList.add(colHeader);

						//	log.info("columnNames.size(): "+columnNames.size()+" and i: "+i);
						if(i == (columnNames.size()-1))
						{
							fileTemplatelines.setLastMasterTableRefCol(true);
							fileTemplatelines.setLastColNumber(true);
						}
						listOfFileTemplateLines.add(fileTemplatelines);

						//log.info("===> key: "+colNum+" Value: "+colHeader);
					}
				}
				fileTemplatesData.setHeaders(headersList);
				fileTemplatesData.setStatus("Success");
				fileTemplatesData.setTemplateLines(listOfFileTemplateLines);
				ListOfFileTemplatesData.add(fileTemplatesData);
				return ListOfFileTemplatesData;
			}
			catch (Exception e) 
			{
				log.error("Exception while Parsing BAI2/ACH/LOCKBOX Json File");
				e.printStackTrace();
			}
		}
		return ListOfFileTemplatesData;
			}

	/*************
	 * Author Kiran
	 * Example to refresh sample data
	 *************/
	@PostMapping("/refreshSampleData1")
	@Timed
	public List<FileTemplateDataDTO> refreshSampleDataMethod(@RequestBody List<FileTemplateDataDTO> fileTemplateDataDTOList)
	{
		log.info("Sample data");
		List<FileTemplateDataDTO> fileTemplateDataDTOListNew=findDelimiterAndFileExtensionService.refreshSampleData(fileTemplateDataDTOList,",",1l);
		return fileTemplateDataDTOListNew;
	}

	/*************
	 * Author Kiran
	 * Fetch template lines for csv and dfr files
	 *************/
	@PostMapping("/findDelimiterAndFileExtension")
	@Timed
	public List<FileTemplateDataDTO> findDelimiterAndFileExtensionWhenFileUploaded(
			HttpServletRequest request,
			@RequestParam("file") MultipartFile file, 
			@RequestParam(required=false) String multipleIdentifiersList,
			@RequestParam(value = "skipStartRow" , defaultValue = "0") int skipStartRow,
			@RequestParam(value = "skipEndRow" , defaultValue = "0") int skipEndRow,
			@RequestParam(value="templateColumns", required=false) String templateColumns, 
			@RequestParam(value="rowIdentifier", required=false) String rowIdentifier,
			@RequestParam(value="delimiter", required=false) String delimiter ,
			@RequestParam(value="multipleIdentifier", required=false) boolean multipleIdentifier,
			@RequestParam(value="fileType", required=false) String fileType,
			@RequestParam(value="excelDTO", required=false) String excelDTO,
			@RequestParam(value="refreshFileTemplateData", required=false) String refreshFileTemplateData) throws JsonParseException, JsonMappingException, IOException
			{
		List<FileTemplateDataDTO> fileTemplateDataDTOList = new ArrayList<FileTemplateDataDTO>();
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());

		log.info("REST API To Find Delimiter, Header Details and Sample Data from file"+file.getOriginalFilename()+", RowIdentifier "+rowIdentifier+", Delimiter '"+delimiter+"'");
		ObjectMapper objMapper = new ObjectMapper();
		if(refreshFileTemplateData !=  null && !refreshFileTemplateData.isEmpty() && !fileType.equalsIgnoreCase("dfr"))
		{
			TypeFactory typeFact = objMapper.getTypeFactory();
			log.info("In Refreshing Sample Data of 10 lines");
			try 
			{
				fileTemplateDataDTOList = objMapper.readValue(refreshFileTemplateData, typeFact.constructCollectionType(List.class,FileTemplateDataDTO.class));
				if(fileTemplateDataDTOList!=null &&  fileTemplateDataDTOList.size()>0 && fileTemplateDataDTOList.get(0).getTemplateLines().size()>0 && fileTemplateDataDTOList.get(0).getListOfSampledataLines().size()>0)
				{
					fileTemplateDataDTOList=findDelimiterAndFileExtensionService.refreshSampleData(fileTemplateDataDTOList,delimiter,tenantId);
					log.info("fileTemplateDataDTOList: "+fileTemplateDataDTOList.size());
					return fileTemplateDataDTOList;
				}
			} 
			catch (IOException e) 
			{
				log.error("Exception while refreshing sample data..");
				e.printStackTrace();
			}
			return fileTemplateDataDTOList;
		}
		FileTemplateDataDTO fileTemplateData=new FileTemplateDataDTO();
		fileTemplateData.setStatus("Failed");

		if(fileType!=null && fileType.equalsIgnoreCase("Excel"))
		{
			ExcelFileReadingDTO excelConditionsAndHeadersDTO = new ExcelFileReadingDTO();
			log.info("excelDTO "+excelDTO);
			excelConditionsAndHeadersDTO = objMapper.readValue(excelDTO, ExcelFileReadingDTO.class);
			excelConditionsAndHeadersDTO.setFilePath(file);
			try 
			{
				fileTemplateData=findDelimiterAndFileExtensionService.readExcelFile(excelConditionsAndHeadersDTO, fileType);
				if(fileTemplateData!=null)
				{
					fileTemplateDataDTOList.add(fileTemplateData);
					return fileTemplateDataDTOList;
				}
			} catch (Exception  e) 
			{
				log.error("Exception While Reading a Excel file");
				e.printStackTrace();
			}
		}
		else if(fileType!=null)
		{
			int lineIndx=0;
			String lineData="";
			InputStream inputStream = null;
			Set<Integer> keys = new HashSet<Integer>(); 
			List<HashMap<Integer, Integer>> occeranceListOfDelimiter = new ArrayList<HashMap<Integer,Integer>>(); // to find the delimiter maximum occurrences from 10 lines
			try
			{
				inputStream = file.getInputStream();
				BufferedReader bfrReader = null;
				try {
					/***********************************
					 * Reading the File that is Uploaded 
					 ***********************************/
					bfrReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				/***********************************
				 * When Delimiter is GIVEN by the User
				 *********************************/
				if(delimiter!=null && !delimiter.isEmpty())
				{
					int asciiOfDelimiter=0;
					try{
						asciiOfDelimiter=Integer.valueOf(delimiter);}
					catch (NumberFormatException e) 
					{
						log.info("NumberFormatException for asciiOfDelimiter: " + e.getMessage());
						if(bfrReader!=null)
							bfrReader.close();
						if(inputStream!=null)
							inputStream.close();
						return fileTemplateDataDTOList;

					}
					char delimiterChar = (char) asciiOfDelimiter;

					fileTemplateDataDTOList=findDelimiterAndFileExtensionService.fetchTemplateDefinitionData(file, multipleIdentifiersList, fileTemplateDataDTOList, fileTemplateData, delimiterChar, skipStartRow, skipEndRow, tenantId, rowIdentifier, multipleIdentifier, fileType);

					if(bfrReader!=null)
						bfrReader.close();
					if(inputStream!=null)
						inputStream.close();

					if(fileTemplateDataDTOList.size()>0)
						return fileTemplateDataDTOList;
				}
				/***********************************
				 * When Delimiter is NOT GIVEN by the User
				 *********************************/
				else{
					if(templateColumns!=null && templateColumns.equalsIgnoreCase("Variable Columns") && rowIdentifier!=null)
					{
						while((lineData = bfrReader.readLine()) != null && lineIndx <11)
						{
							int rowIdentifierLength=rowIdentifier.length();
							String rowIdInLine=lineData.substring(0, rowIdentifierLength);
							if(rowIdInLine.equalsIgnoreCase(rowIdentifier))
							{
								//log.info("lineData with RowIdentifier ("+rowIdentifier+"): "+lineData);
								HashMap<Integer, Integer> occuranceOfDelimiters=findDelimiterAndFileExtensionService.getOccuranceOfDelimiters(lineData);
								if(occuranceOfDelimiters.size()!=0)
								{
									keys.addAll(occuranceOfDelimiters.keySet());
									occeranceListOfDelimiter.add(occuranceOfDelimiters);
								}
								lineIndx+=1;
							}
						}
					}
					else{
						while((lineData = bfrReader.readLine()) != null && lineIndx <11)
						{
							//log.info("lineData without RowIdentifier: "+lineData);
							HashMap<Integer, Integer> occuranceOfDelimiters=findDelimiterAndFileExtensionService.getOccuranceOfDelimiters(lineData);
							if(occuranceOfDelimiters.size()!=0)
							{
								keys.addAll(occuranceOfDelimiters.keySet());
								occeranceListOfDelimiter.add(occuranceOfDelimiters);
							}
							lineIndx+=1;
						}
					}
					log.info("occerance ListOf Delimiter: "+occeranceListOfDelimiter);
					Integer delimiterObatined=findDelimiterAndFileExtensionService.findTheDelimiter(occeranceListOfDelimiter, keys);
					log.info("Delimiter Obtained (when delimiter is not given by user): "+delimiterObatined);
					log.info("================= Successfully Found Delimiter ==================");

					int asciiOfDelimiter=0;
					try{
						asciiOfDelimiter=Integer.valueOf(delimiterObatined);}
					catch (NumberFormatException e) 
					{
						log.info("NumberFormatException for asciiOfDelimiter: " + e.getMessage());
						if(bfrReader!=null)
							bfrReader.close();
						if(inputStream!=null)
							inputStream.close();
						return fileTemplateDataDTOList;
					}
					char delimiterChar = (char) asciiOfDelimiter;

					fileTemplateDataDTOList=findDelimiterAndFileExtensionService.fetchTemplateDefinitionData(file, multipleIdentifiersList, fileTemplateDataDTOList, fileTemplateData, delimiterChar, skipStartRow, skipEndRow, tenantId, rowIdentifier, multipleIdentifier, fileType);

					if(bfrReader!=null)
						bfrReader.close();
					if(inputStream!=null)
						inputStream.close();

					if(fileTemplateDataDTOList.size()>0)
						return fileTemplateDataDTOList;
				}
			}
			catch(NullPointerException e){
				log.info("Null Pointer Exception ");
			}
			finally
			{
				if(inputStream!=null){try{inputStream.close();}catch(Exception e){log.info("Exception while closing Inputstream");}}
			}
		}
		return null;
			}

	/*************
	 * Author Kiran
	 * Not Using (Bkp)
	 *************/
	@PostMapping("/findDelimiterAndFileExtensionOld")
	@Timed
	public List<FileTemplateDataDTO> findDelimiterAndFileExtensionWhenFileUploadedOld(
			HttpServletRequest request,
			@RequestParam("file") MultipartFile file, 
			@RequestParam(required=false) String multipleIdentifiersList,
			@RequestParam(value = "skipStartRow" , defaultValue = "0") int skipStartRow,
			@RequestParam(value = "skipEndRow" , defaultValue = "0") int skipEndRow,
			//@RequestParam Long tenantId, 
			@RequestParam(value="templateColumns", required=false) String templateColumns, 
			@RequestParam(value="rowIdentifier", required=false) String rowIdentifier,
			@RequestParam(value="delimiter", required=false) String delimiter ,
			@RequestParam(value="multipleIdentifier", required=false) boolean multipleIdentifier,
			@RequestParam(value="fileType", required=false) String fileType,
			@RequestParam(value="excelDTO", required=false) String excelDTO,
			@RequestParam(value="refreshData", required=false) boolean refreshData,
			@RequestParam(value="templatesLinesDto", required=false) List<FileTemplateLinesDTO> templatesLinesDto) throws JsonParseException, JsonMappingException, IOException
			{

		List<FileTemplateDataDTO> fileTempDtoList = new ArrayList<FileTemplateDataDTO>();
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());

		log.info("====== * Finding Delimiter and file excetion Api, filename:- "+file.getOriginalFilename()+" Delimiter: "+delimiter+" and rowIdentifier: "+rowIdentifier+" tenant: "+tenantId);
		FileTemplateDataDTO result=new FileTemplateDataDTO();
		result.setStatus("Failed");
		//fileTempDtoList.add(result);
		//		FileTemplateDataDTO fileData = new FileTemplateDataDTO();
		ObjectMapper objMapper = new ObjectMapper();
		TypeFactory typeFact = objMapper.getTypeFactory();
		if(fileType!=null && fileType.equalsIgnoreCase("EXCEL"))
		{
			ExcelFileReadingDTO excelConditionsAndHeadersDTO = new ExcelFileReadingDTO();
			log.info("excelDTO "+excelDTO);
			excelConditionsAndHeadersDTO = objMapper.readValue(excelDTO, ExcelFileReadingDTO.class);
			excelConditionsAndHeadersDTO.setFilePath(file);

			try 
			{
				result=findDelimiterAndFileExtensionService.readExcelFile(excelConditionsAndHeadersDTO, fileType);
				if(result!=null)// && !result.getStatus().equalsIgnoreCase("Failed"))
				{
					fileTempDtoList.add(result);
					return fileTempDtoList;
				}
			} catch (Exception  e) {
				e.printStackTrace();
			}


		}
		else{
			InputStream inputStream;
			try 
			{
				inputStream = file.getInputStream();
				BufferedReader bfrReader = null;
				try {
					/** Here we are reading the file that is uploaded */
					bfrReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				String givenLine="";
				List<HashMap<Integer, Integer>> occeranceList = new ArrayList<HashMap<Integer,Integer>>(); // to find the delimiter maximum occurrences from 10 lines 
				Set<Integer> keys = new HashSet<Integer>(); // Unique Keys/ delimiters

				int tempValue = 0; // get the count of 10 lines

				String criteria = null;
				int posBeging=0;
				int posEnd=0;


				//When Delimiter is given
				if(delimiter!=null && !delimiter.isEmpty())
				{
					log.info("When Delimiter is given and Delimiter: "+delimiter);
					int val=0;
					try{
						val=Integer.valueOf(delimiter);
					}
					catch (NumberFormatException e) 
					{
						log.info("NumberFormatException for UserId: " + e.getMessage());
					}
					char charDigit = (char) val;
					//				char charDigit = delimiter.charAt(0);
					log.info("Reading the data using Delimiter: "+charDigit);
					log.info("***************************************multipleIdentifiersList: "+multipleIdentifiersList);


					try {

						if(multipleIdentifiersList!=null)
						{

							/**Code to parse List of String to Json or Hashmap */

							List<MultipleIdentifiersDTO> multiIdList= objMapper.readValue(multipleIdentifiersList, typeFact.constructCollectionType(List.class,MultipleIdentifiersDTO.class));

							for(MultipleIdentifiersDTO multiId:multiIdList)
							{
								log.info("Criteria: "+multiId.getCriteria());
								log.info("RowIdentifier: "+multiId.getRowIdentifier());
								log.info("positionStart: "+multiId.getPositionStart());
								log.info("positionEnd: "+multiId.getPositionEnd());


								rowIdentifier=multiId.getRowIdentifier();
								criteria = multiId.getCriteria();
								if(multiId.getPositionStart()!=0)
									posBeging=multiId.getPositionStart();
								if(multiId.getPositionEnd()!=0)
									posEnd=multiId.getPositionEnd();


								result=findDelimiterAndFileExtensionService.readingFileAndFetchingDataForMultipleRowIdentifiers(charDigit,file,skipStartRow,skipEndRow,tenantId,rowIdentifier,multipleIdentifier,criteria,posBeging,posEnd, fileType);
								//					}
								//					FileTemplateDataDTO result=findDelimiterAndFileExtensionService.readingTemplateFileData(charDigit,file,skipStartRow,skipEndRow,tenantId,rowIdentifierList.get(0));
								log.info("Status Result-1:-> "+result.getStatus());
								if(result!=null )//&& !result.getStatus().equalsIgnoreCase("Failed"))
								{

									fileTempDtoList.add(result);
									//								return fileTempDtoList;
								}
							}
							return fileTempDtoList;
						}
						else{
							result=findDelimiterAndFileExtensionService.readingFileAndFetchingTemplateData(charDigit,file,skipStartRow,skipEndRow,tenantId,rowIdentifier, fileType);
							log.info("Status Result-2:-> "+result.getStatus());
							if(result!=null )
							{
								fileTempDtoList.add(result);
								return fileTempDtoList;
							}
						}

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					finally{
						if(bfrReader!=null){try{bfrReader.close();} catch(Exception e){}}
					}
				}
				// When delimiter is not given 
				else
				{
					log.info("When delimiter is not given");
					if(templateColumns!=null && templateColumns.equalsIgnoreCase("Variable Columns"))
					{
						while((givenLine = bfrReader.readLine()) != null && tempValue <11)
						{
							/**
							 * Below code is for Dfr file to get the delimiter based on the record identifier
							 * */

							if(rowIdentifier!=null)
							{
								int rowIL=rowIdentifier.length();
								String dataRow=givenLine.substring(0,rowIL);
								//log.info("Row Identifier in file: "+dataRow+" and given row Identifier: "+rowIdentifier);
								if(dataRow.equalsIgnoreCase(rowIdentifier))
								{
									log.info("Given Line if row identifier is given:->"+givenLine);
									List<Integer> listInt = findDelimiterAndFileExtensionService.toGetIndexNumbers(givenLine); 
									//log.info("listInt:->"+listInt);
									for(int k=0;k<(listInt.size()/2);k++)
									{
										List<Integer> listIntInside=findDelimiterAndFileExtensionService.toGetIndexNumbers(givenLine);
										//log.info("listIntInside:-> "+listIntInside);
										int start =listIntInside.get(0);
										int end = (listIntInside.get(1)+1);

										StringBuffer buf = new StringBuffer(givenLine);
										String afterRemoving = buf.replace(start, end, "").toString(); 
										givenLine = afterRemoving;
									}
									//log.info("After Removing "+givenLine);
									HashMap<Integer, Integer> occerance = findDelimiterAndFileExtensionService.toFindDelimiter(givenLine);
									//log.info("occerance.keySet():"+occerance.keySet());
									//log.info("occerance.size():"+occerance.size());
									if(occerance.size()!=0)
									{
										keys.addAll(occerance.keySet());
										occeranceList.add(occerance);
									}
									tempValue++;
								}
							}
							else{
								log.info("rowIdentifier Not Given");
							}
						}
					}
					else{
						while((givenLine = bfrReader.readLine()) != null && tempValue <11)
						{
							log.info("Given Line is:->"+givenLine);
							List<Integer> listInt = findDelimiterAndFileExtensionService.toGetIndexNumbers(givenLine); 
							//log.info("listInt:->"+listInt);
							for(int k=0;k<(listInt.size()/2);k++)
							{
								List<Integer> listIntInside=findDelimiterAndFileExtensionService.toGetIndexNumbers(givenLine);
								//log.info("listIntInside:-> "+listIntInside);
								int start =listIntInside.get(0);
								int end = (listIntInside.get(1)+1);

								StringBuffer buf = new StringBuffer(givenLine);
								String afterRemoving = buf.replace(start, end, "").toString(); 
								givenLine = afterRemoving;
							}
							//log.info("After Removing "+givenLine);
							HashMap<Integer, Integer> occerance = findDelimiterAndFileExtensionService.toFindDelimiter(givenLine);
							//log.info("occerance.keySet():"+occerance.keySet()+" and occerance.size(): "+occerance.size());
							if(occerance.size()!=0)
							{
								keys.addAll(occerance.keySet());
								occeranceList.add(occerance);
							}
							tempValue++;
						}

					}
					log.info("occeranceList:>>"+occeranceList);
					Integer delimiterObatined=findDelimiterAndFileExtensionService.findTheDelimiter(occeranceList, keys);
					log.info("Result(Delimiter Obtained):- "+delimiterObatined);
					log.info("================= Success Found Delimiter ==================");


					int val=0;
					try{
						val=Integer.valueOf(delimiterObatined);}
					catch (NumberFormatException e) 
					{
						log.info("NumberFormatException for UserId: " + e.getMessage());
					}
					char charDigit = (char) val;
					//				char charDigit = delimiter.charAt(0);
					log.info("Reading the data using Delimiter: "+charDigit);

					try{
						if(multipleIdentifiersList!=null && !multipleIdentifiersList.isEmpty())
						{
							/**Code to parse List of String to Json or Hashmap */
							List<MultipleIdentifiersDTO> multiIdList= objMapper.readValue(multipleIdentifiersList, typeFact.constructCollectionType(List.class,MultipleIdentifiersDTO.class));

							for(MultipleIdentifiersDTO multiId:multiIdList)
							{
								log.info("Criteria: "+multiId.getCriteria());
								log.info("RowIdentifier: "+multiId.getRowIdentifier());
								log.info("positionStart: "+multiId.getPositionStart());
								log.info("positionEnd: "+multiId.getPositionEnd());


								rowIdentifier=multiId.getRowIdentifier();
								criteria = multiId.getCriteria();
								if(multiId.getPositionStart()!=0)
									posBeging=multiId.getPositionStart();
								if(multiId.getPositionEnd()!=0)
									posEnd=multiId.getPositionEnd();

								result=findDelimiterAndFileExtensionService.readingFileAndFetchingDataForMultipleRowIdentifiers(charDigit,file,skipStartRow,skipEndRow,tenantId,rowIdentifier,multipleIdentifier,criteria,posBeging,posEnd, fileType);
								//					FileTemplateDataDTO result=findDelimiterAndFileExtensionService.readingTemplateFileData(charDigit,file,skipStartRow,skipEndRow,tenantId,rowIdentifierList.get(0));
								log.info("Status Result-3:-> "+result.getStatus());
								if(result!=null )
								{
									fileTempDtoList.add(result);
									//								return fileTempDtoList;
								}
							}
							return fileTempDtoList;
						}
						else{
							result=findDelimiterAndFileExtensionService.readingFileAndFetchingTemplateData(charDigit,file,skipStartRow,skipEndRow,tenantId,rowIdentifier, fileType);
							log.info("Status Result-4:-> "+result.getStatus());
							if(result!=null )
							{
								fileTempDtoList.add(result);
								return fileTempDtoList;
							}
						}
					}
					catch (IOException e) 
					{
						e.printStackTrace();
					}
					finally{
						if(bfrReader!=null){try{bfrReader.close();} catch(Exception e){}}
					}

				}
			} 
			catch (IOException e) {
				log.info("Exception");
				e.printStackTrace();
			}
		}
		//}
		return fileTempDtoList;
			}

	/*************
	 * Author Kiran
	 *************/
	@PostMapping("/DropZoneTransformationForDropArea")
	@Timed
	public void transformationUsingDropZoneForDropArea(HttpServletRequest request,
			@RequestParam Long srcPrfAsmtlId,
			@RequestParam("file") MultipartFile file) 
	{
		log.info("Rest Post Api to Transform the file after file Drop");
		HashMap map0=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map0.get("tenantId").toString());
		Long userId=Long.parseLong(map0.get("userId").toString());
		SourceProfileFileAssignments srcFileAsmtDetails=sourceProfileFileAssignmentsRepository.findOne(srcPrfAsmtlId);

		if(srcFileAsmtDetails!=null)
		{
			InputStream inputStream;
			try {
				inputStream = new BufferedInputStream(file.getInputStream());
				String fileName=file.getOriginalFilename();

				if(inputStream!=null)
					dataTransformationProcessMethod(request,inputStream, fileName, userId, tenantId, srcPrfAsmtlId,srcFileAsmtDetails);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			log.info("Invalid Source Profile Asmt Id");
		}
		//return null;
	}

	/*************
	 * Author Kiran
	 *************/
	@PostMapping("/DropZoneTransformationForDropBoxAndDrive")
	@Timed
	public void transformationUsingDropZoneForDropBoxAndDrive(HttpServletRequest request,

			@RequestParam Long srcPrfAsmtlId,
			@RequestParam String accessToken,
			@RequestParam String fileId)
	{

		log.info("Rest Post Api to Transform the file after file Drop");
		HashMap map0=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map0.get("tenantId").toString());
		Long userId=Long.parseLong(map0.get("userId").toString());
		SourceProfileFileAssignments srcFileAsmtDetails=sourceProfileFileAssignmentsRepository.findOne(srcPrfAsmtlId);
		InputStream inputStream=null;
		String fileName=null;
		if(srcFileAsmtDetails!=null)
		{
			DbxRequestConfig config = new DbxRequestConfig("com.dropbox/V2");
			DbxClientV2 clientV2 = new DbxClientV2(config, accessToken);
			FullAccount account;
			try {
				account = clientV2.users().getCurrentAccount();
				log.info("Connected with drop box using account: "+account.getName().getDisplayName());
			} 
			catch (DbxException e) {
				e.printStackTrace();
			}
			try 
			{
				Metadata md=clientV2.files().getMetadata("id:Q6fLPAnfb4AAAAAAAAAACQ");
				fileName=md.getName();
				log.info("fileName- "+md.getName());

				DbxDownloader<FileMetadata> downloder =clientV2.files().downloadBuilder(fileId).start();
				//log.info("--> "+downloder.getResult());
				inputStream = downloder.getInputStream();

			} 
			catch (DbxException e) 
			{
				e.printStackTrace();
			}

			if(inputStream!=null )
				dataTransformationProcessMethod(request,inputStream, fileName, userId,tenantId, srcPrfAsmtlId,srcFileAsmtDetails);

		}
		else{
			log.info("Invalid Source Profile Asmt Id");
		}
		//return null;
	}

	/*************
	 * Author Kiran
	 *************/
	public void dataTransformationProcessMethod(HttpServletRequest request,
			InputStream inputStream, 
			String fileName,
			Long userId,
			Long tenantId, 
			Long srcPrfAsmtlId,
			SourceProfileFileAssignments srcFileAsmtDetails)
	{
		String targetFolderPath=srcFileAsmtDetails.getLocalDirectoryPath();
		log.info("fileName: "+fileName);
		DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
		String filename1 = "";
		String filename2 = "";
		if ((fileName.lastIndexOf(".") != -1) && (fileName.lastIndexOf(".") != 0))
		{
			int val = fileName.lastIndexOf(".");
			filename1 = fileName.substring(0, val);
			filename2 = fileName.substring(val, fileName.length());
		}
		String fileWithTimeStamp = filename1 + "_" + df.format(new Date()) + filename2;
		log.info("TemplateName with timestamp:- " + fileWithTimeStamp);


		//		String formConfig="Local_Connection";
		//		FormConfig formDetails=formConfigRepository.findByFormConfig(formConfig);

		TenantConfig tenantConfig = tenantConfigRepository.findByTenantIdAndKey(tenantId, "dataExtractionCentralRepositoryConn");


		if(tenantConfig!=null)
		{
			String str=tenantConfig.getValue();
			String[] strVals=str.split(",");
			String host=strVals[0];
			String port=strVals[1];
			String userName=strVals[2];
			String password=strVals[3];

			Channel channel =sftpConnection(host, port, userName,password);
			ChannelSftp targetchannelSftp = (ChannelSftp)channel;


			putFile(targetchannelSftp, targetFolderPath, fileWithTimeStamp, inputStream);

			Vector<ChannelSftp.LsEntry> filesMoved;
			try 
			{
				filesMoved = targetchannelSftp.ls(targetFolderPath);

				BatchHeader batchHdrDetails=null;
				if (filesMoved.size() != 0)
				{
					log.info("File Moved to local Dir Path: "+targetFolderPath);

					Long profileId=srcFileAsmtDetails.getSourceProfileId();
					SourceProfiles srcPrfLDetails=sourceProfilesRepository.findOne(profileId);

					batchHdrDetails=new BatchHeader();
					batchHdrDetails.setBatchName("Batch_"+srcPrfLDetails.getSourceProfileName());
					batchHdrDetails.setCreatedBy(userId);
					batchHdrDetails.setCreatedDate(ZonedDateTime.now());
					batchHdrDetails.setExtractedDatetime(ZonedDateTime.now());
					batchHdrDetails.setExtractionStatus("1 file(s) - EXTRACTED");
					batchHdrDetails.setTenantId(tenantId);
					batchHdrDetails.setType("Manual");
					BatchHeader batchHdr=batchHeaderRepository.save(batchHdrDetails);  

					batchHdrDetails=batchHeaderRepository.findOne(batchHdr.getId());
					batchHdrDetails.setBatchName("Batch_"+srcPrfLDetails.getSourceProfileName()+"_"+batchHdr.getId());
					batchHeaderRepository.save(batchHdrDetails);


					SourceFileInbHistory srcFileHstry = new SourceFileInbHistory();
					srcFileHstry.setProfileId(profileId);
					srcFileHstry.setFileName(fileWithTimeStamp);
					srcFileHstry.setFileReceivedDate(ZonedDateTime.now());
					srcFileHstry.setLocalDirPath(targetFolderPath);
					srcFileHstry.setFileExt(filename2);
					srcFileHstry.setCreatedBy(userId);
					srcFileHstry.setCreationDate(ZonedDateTime.now());
					srcFileHstry.setSrcPrfFileAsmtId(srcPrfAsmtlId);
					srcFileHstry.setTenantId(tenantId);
					srcFileHstry.setStatus("Extracted");
					srcFileHstry.setBatchId(batchHdr.getId());
					srcFileHstry.setHold(false);
					sourceFileInbHistoryRepository.save(srcFileHstry);

					/**Code to start Transformation Process**/

					HashMap parameterSet = new HashMap();
					parameterSet.put("param1", profileId);
					parameterSet.put("param2", srcFileAsmtDetails.getTemplateId());
					//parameterSet.put("param3", "null");
					//parameterSet.put("param4", "null");

					log.info("Api call to Intiate Job for Data Transformation process: "+parameterSet);
					ResponseEntity response=jobDetailsResource.jobIntiateForAcctAndRec(request, "DataTransformation", parameterSet);
				}
				else
				{
					log.info("File Not moved to the central repository path");
				}
			} catch (SftpException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			log.info("Tenant Configuration details were not present");
		}
	}

	/*************
	 * Author Kiran
	 *************/
	public Channel sftpConnection(String host, String prt, String userName, String password)
	{
		Channel channel = null ;
		//log.info("port:"+prt);
		int port=0;
		try{
			port = Integer.parseInt(prt);}
		catch(NumberFormatException ex)
		{
			log.info("NumberFormat Exception for port: "+prt);
			System.exit(0);
		}
		log.info("Host: "+host+" Port: "+port+" UserName "+userName+" Password: "+password);
		Session session = null;
		try 
		{
			JSch jsch = new JSch();
			session = jsch.getSession(userName, host, port);
			session.setPassword(password);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			//log.info("==========config========="+config);
			session.connect();
			channel = session.openChannel("sftp");
			//log.info("==========channel=========="+channel);
			channel.connect();
			log.info("channel success connection :"+channel.isConnected());
		} 
		catch (Exception ex) {
			ex.printStackTrace();
			log.info("Failed connection");
		}

		return channel ;
	}

	/*************
	 * Author Kiran
	 *************/
	public void putFile(Channel channel, String targetFolder, String fileToTransfer, InputStream sourceStream)
	{
		log.info("targetFolder: "+targetFolder+" --> file name: "+fileToTransfer);
		ChannelSftp channelSftp = (ChannelSftp) channel;

		//		String tTargetFolder = targetFolder;
		//log.info("Target Folder: "+targetFolder);
		/***/
		try {
			channelSftp.cd("/");
		} catch (SftpException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		String[] folders = targetFolder.split( "/" );
		for ( String folder : folders ) {
			//log.info("folder: "+folder);
			if ( folder.length() > 0 ) {
				try {
					channelSftp.cd( folder );
					//	log.info("Change Folder: "+folder);
				}
				catch ( SftpException e ) {
					try {
						channelSftp.mkdir( folder );
						//log.info("Creating Folder: "+folder);
						channelSftp.chmod(0777, folder);
						channelSftp.cd( folder );
						//log.info("Change Folder: "+folder);
					} catch (SftpException e1) {
						e1.printStackTrace();
					}

				}
			}
		}
		/***/
		//	channelSftp.mkdir(targetFolder);

		try {
			channelSftp.cd(targetFolder);
		}
		catch(Exception e)
		{
			log.info("Target folder not found");
		}
		try {	
			FileInputStream fis = null;
			if(sourceStream == null)
			{
				File f = new File(fileToTransfer);
				fis = new FileInputStream(f);
				channelSftp.put(fis, f.getName());
			}
			else
			{
				log.info("Copying file to server ");
				//	log.info("sourceStream: "+sourceStream+" fileToTransfer: "+fileToTransfer);
				channelSftp.put(sourceStream, fileToTransfer);
			}
			if(fis != null)
			{
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		catch (SftpException e) {
			log.info("SFTP Exception "+e.getMessage());
			e.printStackTrace();
			disconnect(channel);
		} catch (FileNotFoundException e) {
			log.info("File Not Found Exception "+e.getMessage());
			e.printStackTrace();
		}
	}

	/*************
	 * Author Kiran
	 *************/
	public  void disconnect(Channel channel)
	{
		try {
			Session session = channel.getSession();
			channel.disconnect();
			session.disconnect();
		} catch (JSchException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Author: Shobha
	 * @param fileTemplates
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/loadDefaultTemplatesForTenant")
	@Timed
	public List<ErrorReport> loadDefaultTemplatesForTenant(@RequestBody String fileTemplates, HttpServletRequest request) throws Exception
	{
		List<ErrorReport> finalErrorReport=new ArrayList<ErrorReport>();
		ObjectMapper objMapper = new ObjectMapper();
		TypeFactory typeFact = objMapper.getTypeFactory();
		List<FileTemplatesPostingDTO> fileTemplatesPostingDTOs= objMapper.readValue(fileTemplates, typeFact.constructCollectionType(List.class,FileTemplatesPostingDTO.class));
		for(FileTemplatesPostingDTO fileTemplatesPostingDTO : fileTemplatesPostingDTOs )
		{
			ErrorReport errorReport=new ErrorReport();
			fileTemplatesPostingDTO.getFileTempDTO().setStartDate(ZonedDateTime.now());
			errorReport = fileTemplatesService.saveTemplate(fileTemplatesPostingDTO,request,"false");
			finalErrorReport.add(errorReport);
		}
		return finalErrorReport;
	}


	/**
	 * Author : Shobha
	 * @param request
	 * @param file
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/findMatchedTemplatesBySourceFile")
	@Timed
	public List<HashMap<String,String>> findMatchedTemplatesBySourceFile(HttpServletRequest request,@RequestParam("file") MultipartFile file) throws IOException 
	{
		log.info("Rest request to fetch matched templates by file");
		List<HashMap<String,String>> templates = new ArrayList<HashMap<String,String>>();
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()), ',' , '"');
		List<String[]> allRows = csvReader.readAll();
		/*************************Check if file matched with BAI2***********************************************/

		String[] firstLine = allRows.get(0);
		String[] lastLine = allRows.get(allRows.size()-1);
		if(firstLine[0] != null && firstLine[0].equalsIgnoreCase("01")){
			log.info("first line contains 01");
			if(lastLine[0] != null && lastLine[0].equalsIgnoreCase("99")){
				log.info("last line contains 99");
				//get default template for BIA2
				//FileTemplates defaultTemplateForBAI2 = new FileTemplates();
				//defaultTemplateForBAI2 = fileTemplatesRepository.findByTenantIdAndTemplateName(tenantId, "BAI2 default template");
				List<FileTemplates> templatesForBAI2 = new ArrayList<FileTemplates>();
				templatesForBAI2=fileTemplatesRepository.findByTenantIdAndFileType(tenantId,"BAI2");
				if(templatesForBAI2 != null && templatesForBAI2.size()>0)
				{
					for(int i =0;i<templatesForBAI2.size();i++)
					{
						HashMap<String,String> templateMap = new HashMap<String,String>();
						FileTemplates ft = new FileTemplates();
						ft = templatesForBAI2.get(i);
						templateMap.put("id", ft.getIdForDisplay());
						templateMap.put("templateName",ft.getTemplateName());
						templates.add(templateMap);
					}

				}
			}

		}
		return templates;
	}


	/**
	 * Author: Kiran
	 * @param fileTemplates
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/DefaultFileTemplatesPostingDTO")
	@Timed
	public List<ErrorReport> savingDefaultFileTemplatesPostingDTO(@RequestParam Long tenantId, @RequestParam Long userId)
	{
		log.info("Rest Request to save Default template for tenantId: "+tenantId+", userId: "+userId);
		List<ErrorReport> finalErrorReport=new ArrayList<ErrorReport>();
		JSONParser jsonParser = new JSONParser();

		List<String> listOfFileTypes=new ArrayList<String>();
		listOfFileTypes.add("Bai2.json");
		listOfFileTypes.add("Bai2_Lockbox.json");
		listOfFileTypes.add("Bai2_ACH.json");
		listOfFileTypes.add("MT940_Standard.json");

		listOfFileTypes.add("Adyan.json");
		listOfFileTypes.add("Braintree.json");
		listOfFileTypes.add("Paytm.json");
		listOfFileTypes.add("Worldpay_Chbk.json");
		listOfFileTypes.add("Worldpay.json");
		listOfFileTypes.add("Zaakpay.json");
		listOfFileTypes.add("Paypal.json");
		listOfFileTypes.add("Chase.json");
		
		log.info("listOfFileTypes available for creating default template {}",listOfFileTypes);

		//		List<FileTemplatesPostingDTO> fileTemplatesPostingDTOs= objMapper.readValue(fileTemplates, typeFact.constructCollectionType(List.class,FileTemplatesPostingDTO.class));
		for(int indx_i=0;indx_i<listOfFileTypes.size();indx_i++)
		{
			Object jsonObjectList = null;
			String filePath=listOfFileTypes.get(indx_i);
			log.info("File Path: "+filePath);
			try 
			{
				jsonObjectList = (Object) jsonParser.parse(new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("jsonFile/default_"+filePath))));
			}
			catch(Exception e)
			{
				log.info("Exception While fetching Json Object of "+filePath);
				continue;
			}

			String fileTemplateDtoString=jsonObjectList.toString();
			ErrorReport errorReport=new ErrorReport();

			ObjectMapper objMapper = new ObjectMapper();
			TypeFactory typeFact = objMapper.getTypeFactory();
			try 
			{
				FileTemplatesPostingDTO fileTemplatesPostingDTO=objMapper.readValue(fileTemplateDtoString, FileTemplatesPostingDTO.class);

				fileTemplatesPostingDTO.getFileTempDTO().setStartDate(ZonedDateTime.now());
				errorReport = fileTemplatesService.saveDefaultTemplate(fileTemplatesPostingDTO, tenantId, userId, "false");
			}
			catch (Exception e) 
			{
				log.info("Exception while Saving DTO");
				e.printStackTrace();
			}
			finalErrorReport.add(errorReport);
		}
		return finalErrorReport;
	}


}
