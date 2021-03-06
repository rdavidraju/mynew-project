package com.nspl.app.web.rest;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.BucketDetails;
import com.nspl.app.domain.BucketList;
import com.nspl.app.domain.DataViews;
import com.nspl.app.domain.DataViewsColumns;
import com.nspl.app.domain.FileTemplateLines;
import com.nspl.app.domain.LookUpCode;
import com.nspl.app.domain.ReportDefination;
import com.nspl.app.domain.ReportParameters;
import com.nspl.app.domain.ReportType;
import com.nspl.app.domain.Reports;
import com.nspl.app.repository.BucketDetailsRepository;
import com.nspl.app.repository.BucketListRepository;
import com.nspl.app.repository.DataViewsColumnsRepository;
import com.nspl.app.repository.DataViewsRepository;
import com.nspl.app.repository.FileTemplateLinesRepository;
import com.nspl.app.repository.LookUpCodeRepository;
import com.nspl.app.repository.ReportDefinationRepository;
import com.nspl.app.repository.ReportParametersRepository;
import com.nspl.app.repository.ReportTypeRepository;
import com.nspl.app.repository.ReportsRepository;
import com.nspl.app.security.IDORUtils;
import com.nspl.app.service.ReportsService;
import com.nspl.app.service.UserJdbcService;
import com.nspl.app.web.rest.util.HeaderUtil;
import com.nspl.app.web.rest.util.PaginationUtil;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

/**
 * REST controller for managing ReportDefination.
 */
@RestController
@RequestMapping("/api")
public class ReportDefinationResource {

	private final Logger log = LoggerFactory
			.getLogger(ReportDefinationResource.class);

	private static final String ENTITY_NAME = "reportDefination";

	private final ReportDefinationRepository reportDefinationRepository;

	@Inject
	ReportsRepository reportsRepository;

	@Inject
	DataViewsColumnsRepository dataViewsColumnsRepository;

	@Inject
	ReportParametersRepository reportParametersRepository;

	@Inject
	LookUpCodeRepository lookUpCodeRepository;

	@Inject
	ReportTypeRepository reportTypeRepository;

	@Inject
	FileTemplateLinesRepository fileTemplateLinesRepository;

	@Inject
	BucketListRepository bucketListRepository;

	@Inject
	BucketDetailsRepository bucketDetailsRepository;

	@Inject
	ReportsService reportsService;

	@Inject
	UserJdbcService userJdbcService;
	
	@Inject
	DataViewsRepository dataViewsRepository;

	public ReportDefinationResource(
			ReportDefinationRepository reportDefinationRepository) {
		this.reportDefinationRepository = reportDefinationRepository;
	}

	/**
	 * POST /report-definations : Create a new reportDefination.
	 *
	 * @param reportDefination
	 *            the reportDefination to create
	 * @return the ResponseEntity with status 201 (Created) and with body the
	 *         new reportDefination, or with status 400 (Bad Request) if the
	 *         reportDefination has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/report-definations")
	@Timed
	public ResponseEntity<ReportDefination> createReportDefination(
			@RequestBody ReportDefination reportDefination)
			throws URISyntaxException {
		log.debug("REST request to save ReportDefination : {}",
				reportDefination);
		if (reportDefination.getId() != null) {
			return ResponseEntity
					.badRequest()
					.headers(
							HeaderUtil
									.createFailureAlert(ENTITY_NAME,
											"idexists",
											"A new reportDefination cannot already have an ID"))
					.body(null);
		}
		ReportDefination result = reportDefinationRepository
				.save(reportDefination);
		return ResponseEntity
				.created(new URI("/api/report-definations/" + result.getId()))
				.headers(
						HeaderUtil.createEntityCreationAlert(ENTITY_NAME,
								result.getId().toString())).body(result);
	}

	/**
	 * PUT /report-definations : Updates an existing reportDefination.
	 *
	 * @param reportDefination
	 *            the reportDefination to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         reportDefination, or with status 400 (Bad Request) if the
	 *         reportDefination is not valid, or with status 500 (Internal
	 *         Server Error) if the reportDefination couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/report-definations")
	@Timed
	public ResponseEntity<ReportDefination> updateReportDefination(
			@RequestBody ReportDefination reportDefination)
			throws URISyntaxException {
		log.debug("REST request to update ReportDefination : {}",
				reportDefination);
		if (reportDefination.getId() == null) {
			return createReportDefination(reportDefination);
		}
		ReportDefination result = reportDefinationRepository
				.save(reportDefination);
		return ResponseEntity
				.ok()
				.headers(
						HeaderUtil.createEntityUpdateAlert(ENTITY_NAME,
								reportDefination.getId().toString()))
				.body(result);
	}

	/**
	 * GET /report-definations : get all the reportDefinations.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         reportDefinations in body
	 */
	@GetMapping("/report-definations")
	@Timed
	public ResponseEntity<List<ReportDefination>> getAllReportDefinations(
			@ApiParam Pageable pageable) {
		log.debug("REST request to get a page of ReportDefinations");
		Page<ReportDefination> page = reportDefinationRepository
				.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
				page, "/api/report-definations");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /report-definations/:id : get the "id" reportDefination.
	 *
	 * @param id
	 *            the id of the reportDefination to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         reportDefination, or with status 404 (Not Found)
	 */
	@GetMapping("/report-definations/{id}")
	@Timed
	public ResponseEntity<ReportDefination> getReportDefination(
			@PathVariable Long id) {
		log.debug("REST request to get ReportDefination : {}", id);
		ReportDefination reportDefination = reportDefinationRepository
				.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional
				.ofNullable(reportDefination));
	}
	
	/**
	 * GET report layout columns by report id.
	 *
	 * @param id
	 *            the id of the reportDefination to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         reportDefination, or with status 404 (Not Found)
	 */
	@GetMapping("/reportLayoutCols")
	@Timed
	public JSONObject reportLayoutCols(HttpServletRequest request,@RequestParam String id) {
		log.debug("REST request to get ReportDefination : {}", id);
		HashMap map1=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map1.get("tenantId").toString());
		Reports report=reportsRepository.findByTenantIdAndIdForDisplay(tenantId,id);
		List<ReportDefination> reportCols=reportDefinationRepository.getColNames(report.getId());
        List<HashMap> varcharList=new ArrayList<HashMap>();
        for(ReportDefination eachCol:reportCols) {
        	HashMap oneCol=new HashMap();
        	oneCol.put("id", eachCol.getId());
        	oneCol.put("itemName", eachCol.getDisplayName());
        	oneCol.put("dataName", eachCol.getDisplayName());
        	varcharList.add(oneCol);
        }
        
        List<ReportDefination> reportDecimalCols=reportDefinationRepository.getDecimalColNames(report.getId());
        List<HashMap> decimalList=new ArrayList<HashMap>();
        for(ReportDefination eachCol:reportDecimalCols) {
        	HashMap oneCol=new HashMap();
        	oneCol.put("id", eachCol.getId());
        	oneCol.put("itemName", eachCol.getDisplayName());
        	oneCol.put("dataName", eachCol.getDisplayName());
        	decimalList.add(oneCol);
        }
        
        JSONObject finalJson=new JSONObject();
        finalJson.put("varCols", varcharList);
        finalJson.put("decimalCols", decimalList);
        
		return finalJson;
	}

	/**
	 * DELETE /report-definations/:id : delete the "id" reportDefination.
	 *
	 * @param id
	 *            the id of the reportDefination to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/report-definations/{id}")
	@Timed
	public ResponseEntity<Void> deleteReportDefination(@PathVariable Long id) {
		log.debug("REST request to delete ReportDefination : {}", id);
		reportDefinationRepository.delete(id);
		return ResponseEntity
				.ok()
				.headers(
						HeaderUtil.createEntityDeletionAlert(ENTITY_NAME,
								id.toString())).build();
	}

	/**
	 * Author: Swetha Api to Post Reports Defination
	 * 
	 * @param reportDefinationMap
	 * @param tenantId
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	@PostMapping("/reportDefination")
	@Timed
	public void reportDefination(HttpServletRequest request,
			@RequestBody HashMap reportDefinationMap)
			throws ClassNotFoundException, SQLException {

		HashMap map = userJdbcService.getuserInfoFromToken(request);
		Long tenantId = Long.parseLong(map.get("tenantId").toString());
		log.info("Rest Request for post reportDefination");
		log.info("reportDefinationMap: " + reportDefinationMap);
		Long reportId = 0L;
		 Reports reports;
		if (reportDefinationMap != null) {
			
			 if(reportDefinationMap.get("id")!=null &&
			 !(reportDefinationMap.get("id").toString().isEmpty())){
			 log.info("Updating existing Report");
			 reports=reportsRepository.findByTenantIdAndIdForDisplay(tenantId, reportDefinationMap.get("id").toString());
			 }
			 else {
				 log.info("creating new report"); 
				 reports=new Reports(); 
			 }
			 
			if (reportDefinationMap.get("sourceViewId") != null)
			{
				DataViews dv=dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, reportDefinationMap.get("sourceViewId").toString());
				reportDefinationMap.put("sourceViewId", dv.getId());
			}
			if (reportDefinationMap.get("trgtViewId") != null)
			{
				DataViews dv=dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, reportDefinationMap.get("trgtViewId").toString());
				reportDefinationMap.put("trgtViewId", dv.getId());
			}

			Long userId = 0L;
			ZonedDateTime curTymStamp = ZonedDateTime.now();
			String reportTyypeName = "";
			if (reportDefinationMap.get("reportTypeId") != null) {
				Long repTypeId = Long.parseLong(reportDefinationMap.get(
						"reportTypeId").toString());
				reports.setReportTypeId(repTypeId);
				ReportType repType = reportTypeRepository.findOne(repTypeId);
				if (repType != null)
					reportTyypeName = repType.getType();
			}
			if (reportDefinationMap.get("reportName") != null)
				reports.setReportName(reportDefinationMap.get("reportName")
						.toString());
			if (reportDefinationMap.get("reportDesc") != null)
				reports.setDescription(reportDefinationMap.get("reportDesc")
						.toString());
			if (reportDefinationMap.get("sourceViewId") != null)
				reports.setSourceViewId(Long.parseLong(reportDefinationMap.get(
						"sourceViewId").toString()));
			if (reportDefinationMap.get("trgtViewId") != null)
				reports.setReconViewId(Long.parseLong(reportDefinationMap.get(
						"trgtViewId").toString()));
			if (reportDefinationMap.get("enableFlag") != null)
				reports.setEnableFlag(Boolean.valueOf(reportDefinationMap.get(
						"enableFlag").toString()));
			if (reportDefinationMap.get("startDate") != null) {
				String stDate = reportDefinationMap.get("startDate").toString();
				ZonedDateTime date = ZonedDateTime.parse(stDate);
				reports.setStartDate(date);
			}
			if (reportDefinationMap.get("endDate") != null) {
				String endDate = reportDefinationMap.get("endDate").toString();
				ZonedDateTime date = ZonedDateTime.parse(endDate);
				reports.setEndDate(date);
			}
			if (reportDefinationMap.get("reportMode") != null)
				reports.setReportMode(reportDefinationMap.get("reportMode")
						.toString());
			if (reportDefinationMap.get("coa") != null)
				reports.setReportVal01(reportDefinationMap.get("coa")
						.toString());
			if (reportDefinationMap.get("allowDrilldown") != null
					&& !(reportDefinationMap.get("allowDrilldown").toString()
							.isEmpty()))
				reports.setAllowDrillDown(Boolean.valueOf(reportDefinationMap
						.get("allowDrilldown").toString()));
			if (reportDefinationMap.get("show") != null)
				reports.setAccVal(reportDefinationMap.get("show").toString());
			reports.setCreationDate(curTymStamp);
			reports.setLastUpdatedDate(curTymStamp);
			if (reportDefinationMap.get("createdBy") != null
					&& !(reportDefinationMap.get("createdBy").toString()
							.isEmpty())) {
				userId = Long.parseLong(reportDefinationMap.get("createdBy")
						.toString());
				reports.setCreatedBy(userId);
				reports.setLastUpdatedBy(userId);
			}
			reports.setTenantId(tenantId);
			reports.setReportViewType(reportDefinationMap.get("reportViewType")
					.toString());
			if (reportDefinationMap.get("reportViewType") != null
					&& !(reportDefinationMap.get("reportViewType").toString()
							.isEmpty())) {
				if (reportDefinationMap.get("reportViewType").toString()
						.equalsIgnoreCase("CHART")) {
					reports.setReportVal02(reportDefinationMap.get("chartType")
							.toString());
				}
			}
			if (reportDefinationMap.get("agingBucketId") != null
					&& !(reportDefinationMap.get("agingBucketId").toString()
							.isEmpty()))
				reports.setReportVal01(reportDefinationMap.get("agingBucketId")
						.toString());
			if (reportDefinationMap.get("roleBackType") != null
					&& !(reportDefinationMap.get("roleBackType").toString()
							.isEmpty()))
				reports.setReportVal01(reportDefinationMap.get("roleBackType")
						.toString());
			if (reportDefinationMap.get("roleBackCount") != null
					&& !(reportDefinationMap.get("roleBackCount").toString()
							.isEmpty()))
				reports.setReportVal02(reportDefinationMap.get("roleBackCount")
						.toString());
			if (reportDefinationMap.get("reconcile") != null
					&& !(reportDefinationMap.get("reconcile").toString()
							.isEmpty()))
				reports.setRecVal(reportDefinationMap.get("reconcile")
						.toString());
			if (reportTyypeName.equalsIgnoreCase("ROLL_BACK_REPORT")) {
				if (reportDefinationMap.get("rollBackType") != null
						&& !(reportDefinationMap.get("rollBackType").toString()
								.isEmpty()))
					reports.setReportVal01(reportDefinationMap.get(
							"rollBackType").toString());
				if (reportDefinationMap.get("rollBackCount") != null
						&& !(reportDefinationMap.get("rollBackCount")
								.toString().isEmpty()))
					reports.setReportVal02(reportDefinationMap.get(
							"rollBackCount").toString());
			}
			Reports newReports = reportsRepository.save(reports);
			
			String idForDisplay = IDORUtils.computeFrontEndIdentifier(newReports.getId().toString());
			newReports.setIdForDisplay(idForDisplay);
			newReports = reportsRepository.save(newReports);
    		log.info("job :"+newReports);
			Long repId = newReports.getId();
			reportId = repId;
			log.info("Report has been saved with repId: " + repId);

			if(reportDefinationMap.containsKey("id") && reportDefinationMap.get("id")!=null){
				
				//Report already exists and need to be updated
				List<ReportDefination> existingRepDefList=reportDefinationRepository.findByReportId(reportId);
				reportDefinationRepository.deleteInBatch(existingRepDefList);
				log.info("Existing Report Defination for ReportId: "+reportId+" has been deleted");
				
				List<ReportParameters> existingReportParamList=reportParametersRepository.findByReportId(reportId);
				reportParametersRepository.deleteInBatch(existingReportParamList);
				log.info("Existing Report Parameters List for ReportId: "+reportId+" has been deleted");
			}
			if (reportDefinationMap.containsKey("columnsDefinition")) { // columnsDefinition
				List<ReportDefination> newReportDefinationList = new ArrayList<ReportDefination>();
				List<ReportParameters> reportParametersList = new ArrayList<ReportParameters>();
				List<HashMap> reportDefinationList = (List<HashMap>) reportDefinationMap
						.get("columnsDefinition");
				if (reportDefinationMap.get("reportType") != null
						&& (reportDefinationMap.get("reportType").toString()
								.equalsIgnoreCase("AGING_REPORT") || reportDefinationMap
								.get("reportType").toString()
								.equalsIgnoreCase("ROLL_BACK_REPORT"))) {

					String datatype = "";
					HashMap aggMap = new HashMap();
					HashMap dateQualMap = new HashMap();
					if (reportDefinationMap.get("agregator") != null) {
						log.info("in aggregator");
						datatype = "AGGREGATOR";
						aggMap = (HashMap) reportDefinationMap.get("agregator");
						ReportDefination newReportDef = new ReportDefination();
						newReportDef.setDataType(datatype);
						newReportDef.setCreatedBy(userId);
						newReportDef.setCreationDate(curTymStamp);
						if (aggMap.get("layoutDisplayName") != null)
							newReportDef.setDisplayName(aggMap.get(
									"layoutDisplayName").toString());
						if (aggMap.get("ColumnId") != null)
							newReportDef.setRefColId(Long.parseLong(aggMap.get(
									"ColumnId").toString()));
						if (aggMap.get("columnType") != null
								&& !(aggMap.get("columnType").toString()
										.isEmpty())) {
							String colType = aggMap.get("columnType")
									.toString();
							newReportDef.setRefTypeId(colType);
							if (colType.equalsIgnoreCase("DATA_VIEW")) {
								if (reportDefinationMap.get("sourceViewId") != null) {
									Long srcId = Long
											.parseLong(reportDefinationMap.get(
													"sourceViewId").toString());
									newReportDef.setRefSrcId(srcId);
								} else if (colType
										.equalsIgnoreCase("FIN_FUNCTION")) {
									LookUpCode lookupCode = lookUpCodeRepository
											.findByLookUpTypeAndLookUpCodeAndTenantId(
													"COLUMN_REF_TYPE", colType,
													tenantId);
									newReportDef
											.setRefSrcId(lookupCode.getId());
								}
							}
						}
						newReportDef.setReportId(reportId);
						ReportDefination newmap = reportDefinationRepository
								.save(newReportDef);
						log.info(datatype
								+ " aggregator Defination saved with id: "
								+ newmap.getId());
					}
					if (reportDefinationMap.get("dateQualifier") != null) {
						log.info("in DATE_QUALIFIER");
						datatype = "DATE_QUALIFIER";
						dateQualMap = (HashMap) reportDefinationMap
								.get("dateQualifier");
						ReportDefination newReportDef = new ReportDefination();
						newReportDef.setDataType(datatype);
						newReportDef.setCreatedBy(userId);
						newReportDef.setCreationDate(curTymStamp);
						if (dateQualMap.get("layoutDisplayName") != null)
							newReportDef.setDisplayName(dateQualMap.get(
									"layoutDisplayName").toString());
						if (dateQualMap.get("ColumnId") != null)
							newReportDef.setRefColId(Long.parseLong(dateQualMap
									.get("ColumnId").toString()));
						if (dateQualMap.get("columnType") != null
								&& !(dateQualMap.get("columnType").toString()
										.isEmpty())) {
							String colType = dateQualMap.get("columnType")
									.toString();
							newReportDef.setRefTypeId(colType);
							if (colType.equalsIgnoreCase("DATA_VIEW")) {
								if (reportDefinationMap.get("sourceViewId") != null) {
									Long srcId = Long
											.parseLong(reportDefinationMap.get(
													"sourceViewId").toString());
									newReportDef.setRefSrcId(srcId);
								} else if (colType
										.equalsIgnoreCase("FIN_FUNCTION")) {
									LookUpCode lookupCode = lookUpCodeRepository
											.findByLookUpTypeAndLookUpCodeAndTenantId(
													"COLUMN_REF_TYPE", colType,
													tenantId);
									newReportDef
											.setRefSrcId(lookupCode.getId());
								}
							}
						}
						newReportDef.setReportId(reportId);
						ReportDefination newmap = reportDefinationRepository
								.save(newReportDef);
						log.info(datatype
								+ " date qualifier Defination saved with id: "
								+ newmap.getId());
					}

				}
				for (int i = 0; i < reportDefinationList.size(); i++) {

					HashMap repDef = reportDefinationList.get(i);
					ReportDefination newReportDef = new ReportDefination();
					ReportParameters rParam = new ReportParameters();
					if (repDef.get("groupBy") != null)
						newReportDef.setGoupBy(Boolean.valueOf(repDef.get(
								"groupBy").toString()));
					if (repId != null)
						newReportDef.setReportId(repId);
					if (repDef.get("columnType") != null
							&& !(repDef.get("columnType").toString().isEmpty())) {
						String colType = repDef.get("columnType").toString();
						newReportDef.setRefTypeId(colType);
						rParam.setRefTypeid(colType);

						if (colType.equalsIgnoreCase("DATA_VIEW")) {
							if (reportDefinationMap.get("sourceViewId") != null) {
								Long srcId = Long.parseLong(reportDefinationMap
										.get("sourceViewId").toString());
								newReportDef.setRefSrcId(srcId);
								rParam.setRefSrcId(srcId);
							}
						} else if (colType.equalsIgnoreCase("FIN_FUNCTION")) {
							LookUpCode lookupCode = lookUpCodeRepository
									.findByLookUpTypeAndLookUpCodeAndTenantId(
											"COLUMN_REF_TYPE", colType,
											tenantId);
							newReportDef.setRefSrcId(lookupCode.getId());
							rParam.setRefSrcId(lookupCode.getId());
							rParam.setDataType(lookupCode.getDescription());
						} else if (colType.equalsIgnoreCase("AGING")) {
							if (reportDefinationMap.get("agingBucketId") != null
									&& !(reportDefinationMap.get(
											"agingBucketId").toString()
											.isEmpty()))
								newReportDef.setRefSrcId(Long
										.parseLong(reportDefinationMap.get(
												"agingBucketId").toString()));
							Long bucketId=Long.parseLong(reportDefinationMap.get(
									"agingBucketId").toString());
							BucketList bucketData=bucketListRepository.findOne(bucketId);
							if(bucketData.getType()!=null && bucketData.getType().equalsIgnoreCase("Age"))
								newReportDef.setDataType("DECIMAL");
							else newReportDef.setDataType("INTEGER");
						}
					}
					if (repDef.get("ColumnId") != null
							&& repDef.get("columnType").toString()
									.equalsIgnoreCase("DATA_VIEW")) {
						newReportDef.setRefColId(Long.parseLong(repDef.get(
								"ColumnId").toString()));
						DataViewsColumns dvc = dataViewsColumnsRepository
								.findOne(Long.parseLong(repDef.get("ColumnId")
										.toString()));
						if (dvc != null && dvc.getColDataType() != null)
							newReportDef.setDataType(dvc.getColDataType());
						rParam.setDataType(dvc.getColDataType());
					}
					if (repDef.get("columnType").toString()
							.equalsIgnoreCase("FIN_FUNCTION")) {
						String colDpName = repDef.get("viewColName").toString();
						LookUpCode lookupCode = lookUpCodeRepository
								.findByLookUpTypeAndLookUpCodeAndTenantId(
										reportTyypeName, colDpName, tenantId);
						newReportDef.setRefColId(lookupCode.getId());
						newReportDef.setDataType(lookupCode.getDescription());
					}
					if (repDef.get("columnType").toString()
							.equalsIgnoreCase("AGING")) {
						if (repDef.get("ColumnId") != null
								&& !(repDef.get("ColumnId").toString()
										.isEmpty()))
							newReportDef.setRefColId(Long.parseLong(repDef.get(
									"ColumnId").toString()));
					}
					if (repDef.get("columnType").toString()
							.equalsIgnoreCase("ROLL_BACK")) {
						newReportDef.setDataType("DECIMAL");
					}
					newReportDef.setCreationDate(curTymStamp);
					if (reportDefinationMap.get("createdBy") != null)
						newReportDef.setCreatedBy(userId);
					if (repDef.get("conditionalOperator") != null)
						newReportDef.setConditionalOperator(repDef.get(
								"conditionalOperator").toString());
					if (repDef.get("conditionalValue") != null)
						newReportDef.setConditionalVal(repDef.get(
								"conditionalValue").toString());
					if (repDef.get("layoutDisplayName") != null)
						newReportDef.setDisplayName(repDef.get(
								"layoutDisplayName").toString());
					if (repDef.get("layoutValue") != null)
						newReportDef.setLayoutVal(repDef.get("layoutValue")
								.toString());
					if ((repDef.get("groupBy") != null && !(repDef.get(
							"groupBy").toString().isEmpty()))
							|| ((repDef.get("conditionalOperator") != null && !(repDef
									.get("conditionalOperator").toString()
									.isEmpty())) && (repDef
									.get("conditionalValue") != null && !(repDef
									.get("conditionalValue").toString()
									.isEmpty())))
							|| (repDef.get("layoutValue") != null && !(repDef
									.get("layoutValue").toString().isEmpty()))) {
						ReportDefination reportDef = reportDefinationRepository
								.save(newReportDef);
					} else {
						log.info("rep def is not required to save");
					}

					rParam.setReportId(repId);
					if (repDef.get("parameterName") != null
							&& !(repDef.get("parameterName").toString()
									.isEmpty()))
						rParam.setDisplayName(repDef.get("parameterName")
								.toString());
					if (repDef.get("parameterType") != null
							&& !(repDef.get("parameterType").toString()
									.isEmpty())) {
						String lCode = repDef.get("parameterType").toString();
						rParam.setSelectionType(lCode);
					}
					rParam.setCreatedBy(userId);
					rParam.setCreationDate(curTymStamp);
					if (repDef.get("ColumnId") != null
							&& !(repDef.get("ColumnId").toString().isEmpty()))
						rParam.setRefColId(Long.parseLong(repDef
								.get("ColumnId").toString()));
					if (repDef.containsKey("isMandatory")
							&& repDef.get("isMandatory") != null)
						rParam.setMandatory((Boolean) repDef.get("isMandatory"));
					if (rParam.getSelectionType() != null
							&& !(rParam.getSelectionType().isEmpty())) {
						ReportParameters repParam = reportParametersRepository
								.save(rParam);
						log.info("repParam saved with id: " + repParam.getId());
					}
				}

			}

			/**
			 * Purpose: It creates a physical report view Replaced with on the
			 * fly dynamic spark sql query
			 */
			// reportsService.createReportView(reportId, tenantId);

		}
	}

	/**
	 * Author: Swetha Api to retrieve complete Report Definition
	 * 
	 * @param reportId
	 * @return
	 */
	@GetMapping("/getReportDefinations")
	@Timed
	public HashMap getReportDefinations(@RequestParam String reportId,HttpServletRequest request) {

		log.info("Rest Request to getReportDefinations for reportId: "
				+ reportId);
		HashMap map1 = userJdbcService.getuserInfoFromToken(request);
		Long tenantId = Long.parseLong(map1.get("tenantId").toString());
		HashMap map = new HashMap();
		HashMap aggMap = new HashMap();
		HashMap dateQualMap = new HashMap();
		Reports reports = reportsRepository.findByTenantIdAndIdForDisplay(tenantId,reportId);
		map.put("id", reports.getIdForDisplay());
		map.put("reportTypeId", reports.getReportTypeId());
		Long repTypeId = reports.getReportTypeId();
		ReportType repType = new ReportType();
		String reportType = "";
		if (repTypeId != null) {
			repType = reportTypeRepository.findOne(repTypeId);
			reportType = repType.getType();
			map.put("reportType", reportType);
			if (repType.getDisplayName() != null)
				map.put("reportTypeDisplayName", repType.getDisplayName());
		}
		map.put("reportName", reports.getReportName());
		map.put("reportDesc", reports.getDescription());
		DataViews dv=dataViewsRepository.findOne(reports.getSourceViewId());
		log.info("dv id for display :"+dv.getIdForDisplay());
		if(reports.getReconViewId()!=null){
			DataViews dv2=dataViewsRepository.findOne(reports.getReconViewId());
			if(dv2!=null)
				map.put("trgtViewId", dv2.getIdForDisplay());
		}
		map.put("sourceViewId", dv.getIdForDisplay());
		map.put("enableFlag", reports.isEnableFlag());
		map.put("startDate", reports.getStartDate());
		map.put("endDate", reports.getEndDate());
		map.put("creationDate", reports.getCreationDate());
		map.put("createdBy", reports.getCreatedBy());
		map.put("lastUpdatedDate", reports.getLastUpdatedDate());
		map.put("lastUpdatedBy", reports.getLastUpdatedBy());
		map.put("reportMode", reports.getReportMode());
		map.put("reportViewType", reports.getReportViewType());
		map.put("coa", reports.getReportVal01());
		map.put("reconcile", reports.getRecVal());
		map.put("show", reports.getAccVal());
		map.put("allowDrilldown", reports.getAllowDrillDown());

		if (repType.getType().equalsIgnoreCase("ROLL_BACK_REPORT")) {
			map.put("rollBackType", reports.getReportVal01());
			map.put("rollBackCount", Integer.parseInt(reports.getReportVal02()));
		}
		if (reports.getReportViewType() != null
				&& !(reports.getReportViewType().isEmpty())) {
			if (reports.getReportViewType().equalsIgnoreCase("CHART")) {
				map.put("chartType", reports.getReportVal02());
			}
		}

		/* Fetching Aggregator or Date Qualifier for Aging and Rollback reports */

		if (repType.getType() != null
				&& (repType.getType().equalsIgnoreCase("AGING_REPORT") || repType
						.getType().equalsIgnoreCase("ROLL_BACK_REPORT"))) {
			ReportDefination aggRepDef = reportDefinationRepository
					.findByReportIdAndDataType(reports.getId(), "AGGREGATOR");
			ReportDefination dateQualDef = reportDefinationRepository
					.findByReportIdAndDataType(reports.getId(), "DATE_QUALIFIER");

			if (aggRepDef != null) {
				if (aggRepDef.getRefColId() != null) {
					aggMap.put("ColumnId", aggRepDef.getRefColId());

				}
				String colType = "";
				String colTypeMeaning = "";
				if (aggRepDef.getRefTypeId() != null) {
					colType = aggRepDef.getRefTypeId();
					LookUpCode lookupCode = lookUpCodeRepository
							.findByLookUpTypeAndLookUpCodeAndTenantId(
									"COLUMN_REF_TYPE", colType, tenantId);
					if (lookupCode.getLookUpCode() != null)
						aggMap.put("columnType", lookupCode.getLookUpCode());
					if (lookupCode.getMeaning() != null)
						colTypeMeaning = lookupCode.getMeaning();

					if (colTypeMeaning != null
							&& colTypeMeaning.equalsIgnoreCase("Data View")) {
						DataViewsColumns dvc = dataViewsColumnsRepository
								.findOne(aggRepDef.getRefColId());
						if (dvc.getRefDvColumn() != null) {
							FileTemplateLines ftl = fileTemplateLinesRepository
									.findOne(Long.parseLong(dvc
											.getRefDvColumn()));
							aggMap.put("viewColName", ftl.getColumnHeader());
						} else {
							aggMap.put("viewColName", dvc.getColumnName());
						}
						aggMap.put("viewColDisplayName", dvc.getColumnName());
						// }
					} else if (colTypeMeaning != null
							&& colTypeMeaning.equalsIgnoreCase("Fin Function")) {
						LookUpCode lookupCode1 = lookUpCodeRepository
								.findOne(aggRepDef.getRefColId());
						aggMap.put("viewColName", lookupCode1.getLookUpCode());
						aggMap.put("viewColDisplayName",
								lookupCode1.getMeaning());
					}
				}
				if (aggRepDef.getDisplayName() != null) {
					aggMap.put("userDisplayColName", aggRepDef.getDisplayName());
					aggMap.put("layoutDisplayName", aggRepDef.getDisplayName());
				}
				log.info("aggMap: " + aggMap);
				map.put("agregator", aggMap);
			}
			if (dateQualDef != null) {
				if (dateQualDef.getRefColId() != null) {
					dateQualMap.put("ColumnId", dateQualDef.getRefColId());

				}
				String colType = "";
				String colTypeMeaning = "";
				if (dateQualDef.getRefTypeId() != null) {
					colType = dateQualDef.getRefTypeId();
					LookUpCode lookupCode = lookUpCodeRepository
							.findByLookUpTypeAndLookUpCodeAndTenantId(
									"COLUMN_REF_TYPE", colType, tenantId);
					if (lookupCode.getLookUpCode() != null)
						dateQualMap.put("columnType",
								lookupCode.getLookUpCode());
					if (lookupCode.getMeaning() != null)
						colTypeMeaning = lookupCode.getMeaning();

					if (colTypeMeaning != null
							&& colTypeMeaning.equalsIgnoreCase("Data View")) {
						DataViewsColumns dvc = dataViewsColumnsRepository
								.findOne(dateQualDef.getRefColId());
						if(dvc.getRefDvColumn()!=null){
							FileTemplateLines ftl = fileTemplateLinesRepository
									.findOne(Long.parseLong(dvc.getRefDvColumn()));
							dateQualMap.put("viewColName", ftl.getColumnHeader());
						}
						else dateQualMap.put("viewColName",dvc.getColumnName());
						dateQualMap.put("viewColDisplayName",
								dvc.getColumnName());
					} else if (colTypeMeaning != null
							&& colTypeMeaning.equalsIgnoreCase("Fin Function")) {
						LookUpCode lookupCode1 = lookUpCodeRepository
								.findOne(dateQualDef.getRefColId());
						dateQualMap.put("viewColName",
								lookupCode1.getLookUpCode());
						dateQualMap.put("viewColDisplayName",
								lookupCode1.getMeaning());
					}
				}
				if (dateQualDef.getDisplayName() != null) {
					dateQualMap.put("userDisplayColName",
							dateQualDef.getDisplayName());
					dateQualMap.put("layoutDisplayName",
							dateQualDef.getDisplayName());
				}
				log.info("dateQualMap: " + dateQualMap);
				map.put("dateQualifier", dateQualMap);
			}
		}

		List<ReportDefination> repDefList = reportDefinationRepository
				.fetchByReportIdOrderByLayoutVal(reports.getId());
		// Checking report condition existence
		List<ReportDefination> repCondList = reportDefinationRepository
				.fetchReportConditions(reports.getId());
		ReportDefination repDefCond = new ReportDefination();
		if (repCondList != null && !(repCondList.isEmpty())) {
			repDefCond = repCondList.get(0);
		}
		if (repDefCond.getLayoutVal() != null) {
			log.info("Report Condition Column already exists in layout");
		} else {
			if (repDefCond != null && repDefCond.getReportId() != null) {
				repDefList.add(repDefCond);
			}
		}
		List<HashMap> columnsDefinition = new ArrayList<HashMap>();
		for (int i = 0; i < repDefList.size(); i++) {
			ReportDefination repDef = repDefList.get(i);
			HashMap repDefMap = new HashMap();
			repDefMap.put("ColumnId", repDef.getRefColId());
			repDefMap.put("userDisplayColName", repDef.getDisplayName());
			repDefMap.put("layoutDisplayName", repDef.getDisplayName());
			if(repDef.getDataType()!=null)
				repDefMap.put("dataType",
						repDef.getDataType());
			String colTypeId = repDef.getRefTypeId();
			if(colTypeId != null && colTypeId.equalsIgnoreCase("NATURAL_ACCOUNT")){				// For Trail balance report   @Rk
				repDefMap.put("columnDisplayName", repDef.getDisplayName());
				repDefMap.put("columnType", "NATURAL_ACCOUNT");
				repDefMap.put("viewColName", repDef.getDisplayName());
				repDefMap.put("viewColDisplayName", repDef.getDisplayName());
			}
			else if (colTypeId != null) {
				LookUpCode lookupCode = lookUpCodeRepository
						.findByLookUpTypeAndLookUpCodeAndTenantId(
								"COLUMN_REF_TYPE", colTypeId, tenantId);
				if (lookupCode.getMeaning() != null)
					repDefMap.put("columnType", lookupCode.getLookUpCode());
				if (repDef.getRefColId() != null
						&& lookupCode.getMeaning()
						.equalsIgnoreCase("Data View")) {
					DataViewsColumns dvc = dataViewsColumnsRepository
							.findOne(repDef.getRefColId());
					repDefMap.put("columnDisplayName", dvc.getColumnName());
					if (dvc.getRefDvColumn() != null) {
						FileTemplateLines ftl = fileTemplateLinesRepository
								.findOne(Long.parseLong(dvc.getRefDvColumn()));
						repDefMap.put("viewColName", ftl.getColumnHeader());
					} else {
						repDefMap.put("viewColName", dvc.getColumnName());
					}
					repDefMap.put("viewColDisplayName", dvc.getColumnName());
				} else if (repDef.getRefColId() != null
						&& lookupCode.getMeaning().equalsIgnoreCase(
								"Fin Function")) {
					LookUpCode lookupCode1 = lookUpCodeRepository
							.findOne(repDef.getRefColId());
					repDefMap.put("viewColName", lookupCode1.getLookUpCode());
					repDefMap.put("viewColDisplayName",
							lookupCode1.getMeaning());
				} else if (repDef.getRefColId() != null
						&& lookupCode.getLookUpCode().equalsIgnoreCase("AGING")) {
					String bucketDetDispName = "";
					BucketDetails buckDet = bucketDetailsRepository
							.findOne(repDef.getRefColId());
					BucketList bucketData = bucketListRepository.findOne(repDef
							.getRefSrcId());
					if (bucketData != null && bucketData.getType() != null)
						bucketDetDispName = bucketData.getType();
					bucketDetDispName = bucketDetDispName + "(";
					if (buckDet != null && buckDet.getFromValue() != null) {
						bucketDetDispName = bucketDetDispName
								+ buckDet.getFromValue();
					}
					bucketDetDispName = bucketDetDispName + "-";
					if (buckDet != null && buckDet.getToValue() != null)
						bucketDetDispName = bucketDetDispName
						+ buckDet.getToValue();
					bucketDetDispName = bucketDetDispName + ")";
					repDefMap.put("viewColDisplayName", bucketDetDispName);
					if (repDef.getDisplayName() != null)
						repDefMap.put("layoutDisplayName",
								repDef.getDisplayName());
					if (repDef.getRefColId() != null)
						repDefMap.put("ColumnId", repDef.getRefColId());
					map.put("agingBucketId",
							Integer.parseInt(reports.getReportVal01()));
					BucketList bucketListData = bucketListRepository
							.findOne(Long.parseLong(reports.getReportVal01()));
					if (bucketListData != null)
						map.put("bucketType", bucketListData.getType());
				} else if (lookupCode.getLookUpCode().equalsIgnoreCase(
						"ROLL_BACK")) {
					repDefMap
					.put("viewColDisplayName", repDef.getDisplayName());
				}
			}
			if (repDef.getConditionalOperator() != null
					&& !(repDef.getConditionalOperator().isEmpty())) {
				String opCode = repDef.getConditionalOperator();
				LookUpCode lCode = lookUpCodeRepository
						.findByLookUpTypeAndLookUpCodeAndTenantId(
								repDef.getDataType(), opCode, tenantId);
				repDefMap.put("conditionalOperator", lCode.getLookUpCode());
				repDefMap.put("conditionalOperatorDisplay", lCode.getMeaning());
			}
			if (repDef.getConditionalVal() != null
					&& !(repDef.getConditionalVal().isEmpty())) {
				repDefMap.put("conditionalValue", repDef.getConditionalVal());
			}
			if (repDef.getLayoutVal() != null
					&& !(repDef.getLayoutVal().isEmpty())) {
				repDefMap.put("layoutValue", repDef.getLayoutVal());
			}
			repDefMap.put("groupBy", repDef.isGoupBy());
			ReportParameters repParam = new ReportParameters();
			if (repType != null && repType.getType() != null
					&& repDef.getRefColId() != null) {
				repParam = reportParametersRepository
						.findByReportIdAndRefTypeidAndRefSrcIdAndRefColId(
								reports.getId(), repDef.getRefTypeId(),
								repDef.getRefSrcId(), repDef.getRefColId());
			} else {
				repParam = reportParametersRepository
						.findByReportIdAndRefTypeidAndRefSrcId(reports.getId(),
								repDef.getRefTypeId(), repDef.getRefSrcId());
			}
			if (repParam != null && repParam.getSelectionType() != null
					&& !(repParam.getSelectionType().isEmpty())) {
				String lCode = repParam.getSelectionType();
				LookUpCode lookupCode4 = lookUpCodeRepository
						.findByLookUpTypeAndLookUpCodeAndTenantId(
								"FORM_CONTROLS", lCode, tenantId);
				repDefMap.put("parameterType", lookupCode4.getLookUpCode());
				repDefMap.put("parameterTypeDisplay", lookupCode4.getMeaning());
			}
			if (repParam != null && repParam != null
					&& repParam.getDisplayName() != null
					&& !(repParam.getDisplayName().isEmpty()))
				repDefMap.put("parameterName", repParam.getDisplayName());
			if (repParam != null && repParam.getMandatory() != null)
				repDefMap.put("isMandatory", repParam.getMandatory());
			log.info("repParam: " + repParam);
			columnsDefinition.add(repDefMap);
		}
		log.info("columnsDefinition till repDef data: " + columnsDefinition);
		List<ReportParameters> repParamList = reportParametersRepository
				.findByReportId(reports.getId());
		for (int i = 0; i < repParamList.size(); i++) {
			ReportParameters repParam = repParamList.get(i);
			Long refSrcId = repParam.getRefSrcId();
			Long refColId = repParam.getRefColId();
			String refTypeId = repParam.getRefTypeid();
			ReportDefination repDef = reportDefinationRepository
					.findByReportIdAndRefTypeIdAndRefSrcIdAndRefColId(reports.getId(),
							refTypeId, refSrcId, refColId);
			if (repDef == null) {
				HashMap repDefMap1 = new HashMap();
				repDefMap1.put("ColumnId", repParam.getRefColId());
				repDefMap1.put("parameterName", repParam.getDisplayName());
				String colTypeId = repParam.getRefTypeid();
				if (colTypeId != null) {
					LookUpCode lookupCode = lookUpCodeRepository
							.findByLookUpTypeAndLookUpCodeAndTenantId(
									"COLUMN_REF_TYPE", colTypeId, tenantId);
					if (lookupCode.getMeaning() != null)
						repDefMap1
						.put("columnType", lookupCode.getLookUpCode());
					if (repParam.getRefColId() != null
							&& lookupCode.getMeaning().equalsIgnoreCase(
									"Data View")) {
						DataViewsColumns dvc = dataViewsColumnsRepository
								.findOne(repParam.getRefColId());
						repDefMap1
						.put("columnDisplayName", dvc.getColumnName());

						FileTemplateLines ftl = fileTemplateLinesRepository
								.findOne(Long.parseLong(dvc.getRefDvColumn()));
						repDefMap1.put("viewColName", ftl.getColumnHeader());
						repDefMap1.put("viewColDisplayName",
								dvc.getColumnName());
					} else if (repParam.getRefColId() != null
							&& lookupCode.getMeaning().equalsIgnoreCase(
									"Fin Function")) {
						LookUpCode lookupCode1 = lookUpCodeRepository
								.findOne(repParam.getRefColId());
						repDefMap1.put("viewColName",
								lookupCode1.getLookUpCode());
						repDefMap1.put("columnDisplayName",
								lookupCode1.getLookUpCode());
						repDefMap1.put("viewColDisplayName",
								lookupCode1.getMeaning());
					}
					if (repParam != null && repParam.getSelectionType() != null
							&& !(repParam.getSelectionType().isEmpty())) {
						String lCode = repParam.getSelectionType();
						LookUpCode lookupCode4 = lookUpCodeRepository
								.findByLookUpTypeAndLookUpCodeAndTenantId(
										"FORM_CONTROLS", lCode, tenantId);
						repDefMap1.put("parameterType", lookupCode4.getLookUpCode());
						repDefMap1.put("parameterTypeDisplay", lookupCode4.getMeaning());
					}
					if (repParam.getMandatory() != null)
						repDefMap1.put("isMandatory", repParam.getMandatory());
					else if (repParam.getRefColId() != null
							&& lookupCode.getLookUpCode().equalsIgnoreCase(
									"AGING")) {
						String bucketDetDispName = "";
						BucketDetails buckDet = bucketDetailsRepository
								.findOne(repParam.getRefColId());
						BucketList bucketData = bucketListRepository
								.findOne(repParam.getRefSrcId());
						if (bucketData != null && bucketData.getType() != null)
							bucketDetDispName = bucketData.getType();
						bucketDetDispName = bucketDetDispName + "(";
						if (buckDet != null && buckDet.getFromValue() != null) {
							bucketDetDispName = bucketDetDispName
									+ buckDet.getFromValue();
						}
						bucketDetDispName = bucketDetDispName + "-";
						if (buckDet != null && buckDet.getToValue() != null)
							bucketDetDispName = bucketDetDispName
							+ buckDet.getToValue();
						bucketDetDispName = bucketDetDispName + ")";
						repDefMap1.put("viewColDisplayName", bucketDetDispName);
						if (repParam.getDisplayName() != null)
							repDefMap1.put("parameterName",
									repParam.getDisplayName());
						if (repParam.getRefColId() != null)
							repDefMap1.put("ColumnId", repParam.getRefColId());
					}
				}
				log.info("repDefMap1: " + repDefMap1);
				columnsDefinition.add(repDefMap1);
			} else {
				log.info("repDef is not null");
			}

		}
		log.info("columnsDefinition till repParam data: " + columnsDefinition);
		map.put("columnsDefinition", columnsDefinition);
		return map;

	}

}
