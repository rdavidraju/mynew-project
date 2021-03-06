package com.nspl.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.config.ApplicationContextProvider;
import com.nspl.app.domain.BucketList;
import com.nspl.app.domain.DataViews;
import com.nspl.app.domain.DataViewsColumns;
import com.nspl.app.domain.FileTemplateLines;
import com.nspl.app.domain.LookUpCode;
import com.nspl.app.domain.ReportDefination;
import com.nspl.app.domain.ReportParameters;
import com.nspl.app.domain.ReportType;
import com.nspl.app.domain.Reports;
import com.nspl.app.repository.AccountingDataRepository;
import com.nspl.app.repository.BucketListRepository;
import com.nspl.app.repository.DataViewsColumnsRepository;
import com.nspl.app.repository.DataViewsRepository;
import com.nspl.app.repository.FileTemplateLinesRepository;
import com.nspl.app.repository.LookUpCodeRepository;
import com.nspl.app.repository.ReportDefinationRepository;
import com.nspl.app.repository.ReportParametersRepository;
import com.nspl.app.repository.ReportTypeRepository;
import com.nspl.app.repository.ReportsRepository;
import com.nspl.app.repository.SegmentsRepository;
import com.nspl.app.repository.TemplateDetailsRepository;
import com.nspl.app.service.DataViewsService;
import com.nspl.app.service.PropertiesUtilService;
import com.nspl.app.service.ReportsService;
import com.nspl.app.service.UserJdbcService;
import com.nspl.app.web.rest.util.HeaderUtil;
import com.nspl.app.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

/**
 * REST controller for managing ReportParameters.
 */
@RestController
@RequestMapping("/api")
public class ReportParametersResource {

	private final Logger log = LoggerFactory
			.getLogger(ReportParametersResource.class);

	private static final String ENTITY_NAME = "reportParameters";

	private final ReportParametersRepository reportParametersRepository;

	@Inject
	ReportDefinationRepository reportDefinationRepository;

	@Inject
	DataViewsRepository dataViewsRepository;

	@Inject
	DataViewsColumnsRepository dataViewsColumnsRepository;

	@Inject
	FileTemplateLinesRepository fileTemplateLinesRepository;

	@Inject
	PropertiesUtilService propertiesUtilService;

	@Inject
	ReportParametersRepository reportPrametersRepository;

	@Inject
	LookUpCodeRepository lookUpCodeRepository;

	@Inject
	ReportsRepository reportsRepository;

	@Inject
	ReportTypeRepository reportTypeRepository;

	@Inject
	DataViewsService dataViewsService;

	@Inject
	private Environment env;

	@Inject
	BucketListRepository bucketListRepository;

	@Inject
	ReportsService reportsService;

	@Inject
	AccountingDataRepository accountingDataRepository;
	
	@Inject
	SegmentsRepository segmentsRepository;
	
	@Inject
    UserJdbcService userJdbcService;
	
	
	@Inject
	TemplateDetailsRepository templateDetailsRepository;
	
	@PersistenceContext(unitName="default")
	private EntityManager em;


	public ReportParametersResource(
			ReportParametersRepository reportParametersRepository) {
		this.reportParametersRepository = reportParametersRepository;
	}

	/**
	 * POST /report-parameters : Create a new reportParameters.
	 *
	 * @param reportParameters
	 *            the reportParameters to create
	 * @return the ResponseEntity with status 201 (Created) and with body the
	 *         new reportParameters, or with status 400 (Bad Request) if the
	 *         reportParameters has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/report-parameters")
	@Timed
	public ResponseEntity<ReportParameters> createReportParameters(
			@RequestBody ReportParameters reportParameters)
			throws URISyntaxException {
		log.debug("REST request to save ReportParameters : {}",
				reportParameters);
		if (reportParameters.getId() != null) {
			return ResponseEntity
					.badRequest()
					.headers(
							HeaderUtil
									.createFailureAlert(ENTITY_NAME,
											"idexists",
											"A new reportParameters cannot already have an ID"))
					.body(null);
		}
		ReportParameters result = reportParametersRepository
				.save(reportParameters);
		return ResponseEntity
				.created(new URI("/api/report-parameters/" + result.getId()))
				.headers(
						HeaderUtil.createEntityCreationAlert(ENTITY_NAME,
								result.getId().toString())).body(result);
	}

	/**
	 * PUT /report-parameters : Updates an existing reportParameters.
	 *
	 * @param reportParameters
	 *            the reportParameters to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         reportParameters, or with status 400 (Bad Request) if the
	 *         reportParameters is not valid, or with status 500 (Internal
	 *         Server Error) if the reportParameters couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/report-parameters")
	@Timed
	public ResponseEntity<ReportParameters> updateReportParameters(
			@RequestBody ReportParameters reportParameters)
			throws URISyntaxException {
		log.debug("REST request to update ReportParameters : {}",
				reportParameters);
		if (reportParameters.getId() == null) {
			return createReportParameters(reportParameters);
		}
		ReportParameters result = reportParametersRepository
				.save(reportParameters);
		return ResponseEntity
				.ok()
				.headers(
						HeaderUtil.createEntityUpdateAlert(ENTITY_NAME,
								reportParameters.getId().toString()))
				.body(result);
	}

	/**
	 * GET /report-parameters : get all the reportParameters.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         reportParameters in body
	 */
	@GetMapping("/report-parameters")
	@Timed
	public ResponseEntity<List<ReportParameters>> getAllReportParameters(
			@ApiParam Pageable pageable) {
		log.debug("REST request to get a page of ReportParameters");
		Page<ReportParameters> page = reportParametersRepository
				.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
				page, "/api/report-parameters");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /report-parameters/:id : get the "id" reportParameters.
	 *
	 * @param id
	 *            the id of the reportParameters to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         reportParameters, or with status 404 (Not Found)
	 */
	@GetMapping("/report-parameters/{id}")
	@Timed
	public ResponseEntity<ReportParameters> getReportParameters(
			@PathVariable Long id) {
		log.debug("REST request to get ReportParameters : {}", id);
		ReportParameters reportParameters = reportParametersRepository
				.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional
				.ofNullable(reportParameters));
	}

	/**
	 * DELETE /report-parameters/:id : delete the "id" reportParameters.
	 *
	 * @param id
	 *            the id of the reportParameters to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/report-parameters/{id}")
	@Timed
	public ResponseEntity<Void> deleteReportParameters(@PathVariable Long id) {
		log.debug("REST request to delete ReportParameters : {}", id);
		reportParametersRepository.delete(id);
		return ResponseEntity
				.ok()
				.headers(
						HeaderUtil.createEntityDeletionAlert(ENTITY_NAME,
								id.toString())).build();
	}

	/**
	 * Author: Swetha GET: Api to retrieve distinct values of all the columns of
	 * DataView Tagged to Report
	 * @param tableName
	 * @param columnList
	 * @return
	 * @throws AnalysisException
	 */
	@GetMapping("/getReportFieldsInfo")
	@Timed
	public HashMap getReportFieldsInfo(HttpServletRequest request,
			@RequestParam String reportId) {

		HashMap map0=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map0.get("tenantId").toString());
		log.info("Rest Request to getReportFieldsInfo [Report Parameters] with tenantId: "
				+ tenantId + " reportId: " + reportId);

		/* Report Definition Info */
		Reports reportsData = reportsRepository.findByTenantIdAndIdForDisplay(tenantId, reportId);
		Reports reports = reportsRepository.findOne(reportsData.getId());
		List<ReportParameters> rparamList = reportPrametersRepository
				.findByReportId(reportsData.getId());
		Long repTypeId = reports.getReportTypeId();
		log.info("repTypeId: " + repTypeId);

		/* Src View Info */
		Long srcViewId = reports.getSourceViewId();
		DataViews dv = dataViewsRepository.findById(srcViewId);

		List<String> coaIds=accountingDataRepository.findCoaForViewId(srcViewId);
		String coaId="";
		if(coaIds!=null && !(coaIds.isEmpty()))
			coaId=coaIds.get(0).toString();

		DataViewsColumns dvcQualifier = dataViewsColumnsRepository
				.findByDataViewIdAndQualifier(srcViewId, "DATE");
		String tableName = dv.getDataViewName();
		tableName = tableName.toLowerCase();
		log.info("tableName: " + tableName);

		ReportType repType = reportTypeRepository.findOne(repTypeId);
		HashMap finalMap = new HashMap();
		String coaCode = reports.getReportVal01();
		finalMap.put("reportMode", reports.getReportMode());
		finalMap.put("coa", coaCode);
		finalMap.put("allowDrilldown", reports.getAllowDrillDown());
		finalMap.put("accVal", reports.getAccVal());
		finalMap.put("reconcile", reports.getRecVal());
		if(reports.getReconViewId()!=null)
		{
			DataViews dvData=dataViewsRepository.findOne(reports.getReconViewId());
			finalMap.put("trgtViewId", dvData.getIdForDisplay());
		}
		String repTypeNm=repType.getType();
		finalMap.put("reportTypeCode", repTypeNm);

		List<HashMap> mapList = new ArrayList<HashMap>();
		String columnName = "";
		Long ftlId = null;
		List<String> colList = new ArrayList<String>();
		List<HashMap> colMapList = new ArrayList<HashMap>();

		for (int i = 0; i < rparamList.size(); i++) {

			ReportParameters rparam = rparamList.get(i);
			HashMap colMap = new HashMap();
			log.info("rparam: " + rparam);
			Long col = rparam.getRefColId();
			if (rparam.getRefTypeid().equalsIgnoreCase("DATA_VIEW")) {
				DataViewsColumns dvc = dataViewsColumnsRepository.findOne(col);
				if (dvc.getRefDvName() != null && dvc.getRefDvColumn() != null) {
					FileTemplateLines ftl = fileTemplateLinesRepository
							.findOne(Long.parseLong(dvc.getRefDvColumn()));
					columnName = ftl.getColumnAlias();
				} else {
					columnName = dvc.getColumnName();
				}
			} else if (rparam.getRefTypeid().equalsIgnoreCase("FIN_FUNCTION")) {
				columnName = rparam.getDisplayName();
			}

			colMap.put(rparam.getId(), columnName);
			colMapList.add(colMap);
		}

		log.info("final colMap: " + colMapList);
		String flag = "false";
		Dataset<Row> values = null;
		List<Object[]> segmentsList=segmentsRepository.fetchSegmentsListforViewCoa(srcViewId);
		int segmentsCount=segmentsList.size();
		for (int k = 0; k < colMapList.size(); k++) {
			List<String> valList = new ArrayList<String>();
			HashMap mapCol = colMapList.get(k);
			// log.info("mapCol: "+mapCol);
			Long rParamId = (Long) mapCol.keySet().iterator().next();
			log.info("rParamId: " + rParamId);
			columnName = mapCol.get(rParamId).toString();
			// log.info("columnName: "+columnName);
			ReportParameters rparam = reportParametersRepository
					.findOne(rParamId);
			if (rparam != null) {
				HashMap map = new HashMap();
				map.put("rParamSrcId", rparam.getRefSrcId());
				map.put("rParamId", rParamId);
				map.put("refType", rparam.getRefTypeid());
				map.put("fieldName", rparam.getDisplayName());
				map.put("refColId", rparam.getRefColId());
				map.put("selOperator", "");
				map.put("isMandatory", rparam.getMandatory());
				String repTypeName = repType.getType();
				map.put("repType", repTypeName);
				if (repTypeName.equalsIgnoreCase("AGING_REPORT")) {
					String bucketId = reports.getReportVal01();
					map.put("bucketId", bucketId);
					if (bucketId != null && !(bucketId.isEmpty())) {
						BucketList bucketListData = bucketListRepository
								.findOne(Long.parseLong(bucketId));
						if (bucketListData != null)
							map.put("bucketType", bucketListData.getType());
					}
				}
				String selectionCode = rparam.getSelectionType();
				// log.info("selectionCode: "+selectionCode);
				LookUpCode lookupCode = lookUpCodeRepository
						.findByLookUpTypeAndLookUpCodeAndTenantId(
								"FORM_CONTROLS", selectionCode, tenantId);
				// log.info("lookupCode: "+lookupCode);
				if (lookupCode != null && lookupCode.getMeaning() != null)
					map.put("fieldType", selectionCode);
				if (lookupCode.getMeaning()
						.equalsIgnoreCase("Multi Select LOV")) {

				}
				map.put("fieldValuesList", valList);
				map.put("selectedValue", "");
				mapList.add(map);
			}
		}
		/* To Enable or Disable Period Filters */
		if (dvcQualifier != null) {

		}

		finalMap.put("fields", mapList);

		/* Report Definition Columns for Pivot and Chart View */
		List<ReportDefination> reportDefinitionList = reportDefinationRepository
				.fetchByReportIdOrderByLayoutVal(reportsData.getId());
		List<HashMap> defMapList = new ArrayList<HashMap>();

		for (int k = 0; k < reportDefinitionList.size(); k++) {

			ReportDefination repDef = reportDefinitionList.get(k);
			HashMap defMap = new HashMap();
			defMap.put("ColumnId", repDef.getId());
			defMap.put("layoutDisplayName", repDef.getDisplayName());
			defMap.put("columnType", repDef.getRefTypeId());
			defMap.put("dataType", repDef.getDataType());
			defMapList.add(defMap);

		}

		ReportDefination aggDef = reportDefinationRepository
				.findByReportIdAndDataType(reportsData.getId(), "AGGREGATOR");
		HashMap aggDefMap = new HashMap();
		if (aggDef != null) {
			aggDefMap.put("ColumnId", aggDef.getId());
			aggDefMap.put("layoutDisplayName", aggDef.getDisplayName());
			aggDefMap.put("columnType", aggDef.getRefTypeId());
			aggDefMap.put("dataType", aggDef.getDataType());
		}
		finalMap.put("aggregator", aggDefMap);

		finalMap.put("outputCols", defMapList);

		finalMap.put("coaId", coaId);
		finalMap.put("segmentsCount", segmentsCount);


		/* Segment Filters */
		if(repType.getType()!=null && (repType.getType().equalsIgnoreCase("ACCOUNT_ANALYSIS_REPORT") || repType.getType().equalsIgnoreCase("ACCOUNT_BALANCE_REPORT"))){
			List<HashMap> segmentInfoList=new ArrayList<HashMap>();
			List<String> segList=new ArrayList<String>();
			for(int i=0;i<segmentsList.size();i++){
				Object[] segment=segmentsList.get(i);
				segList.add(segment[1].toString());
				HashMap segmentInfo=new HashMap();
				segmentInfo.put("segId", segment[0]);
				segmentInfo.put("sequence", segment[2]);
				segmentInfo.put("segName", segment[1]);
				List flexValuesList=new ArrayList();

				List<Object[]> segValueList=segmentsRepository.fetchSegmentsValueSet(srcViewId, segment[1].toString());
				for(int k=0;k<segValueList.size();k++){
					Object[] obj=segValueList.get(k);
					String srcVal=obj[1].toString();
					String targetVal=obj[2].toString();
					String segVal=srcVal+"-"+targetVal;
					flexValuesList.add(segVal);
				}
				segmentInfo.put("flexValues",flexValuesList);
				segmentInfo.put("selFlexValues", new ArrayList<HashMap>());
				segmentInfoList.add(segmentInfo);
				finalMap.put("segmentInfo", segmentInfoList);
			}
			log.info("segList: "+segList);
		}

		return finalMap;
	}

	/**
	 * author: ravali
	 * @param tenantId
	 * @param reportId
	 * @param rParamId
	 * @return
	 */
	@GetMapping("/getFieldValuesList")
	@Timed
	public HashMap getFieldValuesListRemovedSpark(HttpServletRequest request,
			@RequestParam String reportId, @RequestParam Long rParamId) throws SQLException, ClassNotFoundException {

		HashMap map0=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map0.get("tenantId").toString());
		log.info("Rest Request to getFieldValuesList [Value Set for Report Parameters] with tenantId: "
				+ tenantId + " reportId: " + reportId+" for rParamId: "+rParamId);

		/* Report Definition Info */
		Reports reports = reportsRepository.findByTenantIdAndIdForDisplay(tenantId, reportId);
		Long repId=reports.getId();
		Long repTypeId = reports.getReportTypeId();
		log.info("repTypeId: " + repTypeId);

		/* Src View Info */
		Long srcViewId = reports.getSourceViewId();
		DataViews dv = dataViewsRepository.findById(srcViewId);
		DataViewsColumns dvcQualifier = dataViewsColumnsRepository
				.findByDataViewIdAndQualifier(srcViewId, "DATE");
		String tableName = dv.getDataViewName();
		tableName = tableName.toLowerCase();
		log.info("tableName: " + tableName);

		ReportType repType = reportTypeRepository.findOne(repTypeId);

		Connection conn = null;
		Statement stmt = null;
		ResultSet result = null;
		DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
		conn=ds.getConnection();
		String dbUrl=env.getProperty("spring.datasource.url");
		String[] parts=dbUrl.split("[\\s@&?$+-]+");
		String schemaName=parts[0].split("/")[3];
		log.info("schemaName: "+schemaName);
		log.info("Connected database successfully...");
		stmt = conn.createStatement();


		String flag = "false";

		List<String> values =new ArrayList<String>();

		List<String> valList = new ArrayList<String>();

		log.info("rParamId: " + rParamId);
		String columnName = "";

		ReportParameters rparam = reportParametersRepository.findOne(rParamId);

		log.info("rparam: " + rparam);
		Long col = rparam.getRefColId();
		if (rparam.getRefTypeid().equalsIgnoreCase("DATA_VIEW")) {
			DataViewsColumns dvc = dataViewsColumnsRepository.findOne(col);
			if (dvc.getRefDvName() != null && dvc.getRefDvColumn() != null) {
				FileTemplateLines ftl = fileTemplateLinesRepository
						.findOne(Long.parseLong(dvc.getRefDvColumn()));
				columnName = ftl.getColumnAlias();
			} else {
				columnName = dvc.getColumnName();
			}
		} else if (rparam.getRefTypeid().equalsIgnoreCase("FIN_FUNCTION")) {
			columnName = rparam.getDisplayName();
		}

		if (rparam.getRefTypeid().equalsIgnoreCase("FIN_FUNCTION")) {
			log.info("**********in if **********");
			if (rparam.getSelectionType().equalsIgnoreCase("AMOUNT_RANGE")) {
				flag = "false";
				values = null;
			} else {
				flag = "true";
				LookUpCode funLookup = lookUpCodeRepository.findOne(rparam
						.getRefColId());
				// log.info("funLookup: "+funLookup);
				String funName = funLookup.getSecureAttribute1();
				LookUpCode functionRec = lookUpCodeRepository
						.findByLookUpTypeAndLookUpCodeAndTenantId(
								"FIN_FUNCTION", funName, tenantId);
				// log.info("functionRec: "+functionRec);
				if (functionRec.getDescription() != null
						&& functionRec.getDescription().equalsIgnoreCase(
								"TABLE")) {
					String tabName = functionRec.getSecureAttribute1();
					String colName = functionRec.getSecureAttribute2();

					log.info("tabName: "+tabName+" colName: "+colName);

					Dataset<Row> accLedgerDataCmplt = null;
					Dataset<Row> colData = null;
					List<String> ledIds = new ArrayList<String>();
					if (tabName.equalsIgnoreCase("t_accounting_data")
							&& colName.equalsIgnoreCase("ledgername")) {

						/**
						 * Data sets for ledger_ref_type in
						 * ('CONSTANT','VIEW_COLUMN')
						 **/
						colName="ledgerRef";
						List<String> accLedgerDataCmpltList=em.createQuery("select distinct"+"("+colName+")"+ " FROM AccountingData where originalViewId="+srcViewId+" and ledgerRefType in ('CONSTANT','VIEW_COLUMN','MAPPING_SET')").getResultList();

						log.info("accLedgerDataCmpltList :"+accLedgerDataCmpltList);
						for (int g = 0; g < accLedgerDataCmpltList.size(); g++) {
							String val = accLedgerDataCmpltList.get(g);
							if (val != null && !(val.isEmpty())
									&& !(val.equalsIgnoreCase(""))
									&& !(val.equalsIgnoreCase("null"))) {
								// log.info("in val null: "+val);
							} else
								val = "(Blank)";
							ledIds.add(val);
						}

						String valListIds = StringUtils.join(ledIds, ',');
						log.info("valListIds: " + valListIds);

						if (valListIds != null && !(valListIds.isEmpty())) {
							values=em.createQuery("select distinct(name) FROM LedgerDefinition where id in ("+valListIds+") order by name asc").getResultList();

							log.info("ledger names: " + values.size());
						}

						/** Data set for ledger_ref_type as 'MAPPING_SET' **/

						/*List<String> accMappingsetDataCmplt=em.createQuery("select distinct"+"("+colName+")"+ " FROM AccountingData where originalViewId="+srcViewId+" and ledgerRefType in ('MAPPING_SET')").getResultList();

						if (accMappingsetDataCmplt != null && values != null) {
							for(String val:accMappingsetDataCmplt)
							{
								values.add(val);
							}
						} else if (accMappingsetDataCmplt != null
								&& values == null) {
							values = accMappingsetDataCmplt;
						} else if (values != null
								&& accMappingsetDataCmplt == null) {
							values = values;
						}*/
						if (values != null)
							log.info("final values.count: " + values.size());
						log.info("values :"+values);

						log.info("final values.count: " + values.size());
						log.info("final values " + values);

					} 
					else if (tabName.equalsIgnoreCase("t_accounting_data")
							&& colName.equalsIgnoreCase("ledgercurrency")) {

						/**
						 * Data sets for ledger_ref_type in
						 * ('CONSTANT','VIEW_COLUMN')
						 **/
						List<String> accLedgerDataCmpltList=em.createQuery("select distinct"+"(ledgerCurrency)"+ " FROM AccountingData where originalViewId="+srcViewId).getResultList();

						for (int g = 0; g < accLedgerDataCmpltList.size(); g++) {
							String val = accLedgerDataCmpltList.get(g);
							if (val != null && !(val.isEmpty())
									&& !(val.equalsIgnoreCase(""))
									&& !(val.equalsIgnoreCase("null"))) {
								// log.info("in val null: "+val);
							} else
								val = "(Blank)";
							values.add(val);
						}

						log.info("final ledger currency:"+values);


					}

					else if (tableName
							.equalsIgnoreCase("t_journals_header_data")
							&& colName.equalsIgnoreCase("period")) {
						List<Long> jeTemplateDetCmpltData=em.createQuery("select id FROM TemplateDetails where view_id="+srcViewId).getResultList();
						log.info("previewing t_template_details data");
						log.info("jeTemplateDetCmpltData :"+jeTemplateDetCmpltData);

						String tempIds = StringUtils.join(jeTemplateDetCmpltData, ',');
						log.info("tempIds: " + tempIds);

						values=em.createQuery("select period FROM JournalHeaderData where je_temp_id in ("+tempIds+")").getResultList();
						log.info("previewing jrnlHdr periods data");
						log.info("previewing final jrnl data :"+values);
					}

					else if (tableName
							.equalsIgnoreCase("t_journals_header_data")
							&& colName.equalsIgnoreCase("je_batch_name")) {
						List<Long> jeTemplateDetCmpltData=em.createQuery("select id FROM TemplateDetails where view_id="+srcViewId).getResultList();
						log.info("previewing t_template_details data");
						log.info("jeTemplateDetCmpltData :"+jeTemplateDetCmpltData);

						String tempIds = StringUtils.join(jeTemplateDetCmpltData, ',');
						log.info("tempIds: " + tempIds);
						values=em.createQuery("select je_batch_name FROM JournalHeaderData where je_temp_id in ("+tempIds+")").getResultList();

						log.info("previewing final je batch data");
						log.info("values :"+values);
					}
					else {

						String whereClause="";
						log.info("in else tabName: "+tabName+" colName: "+colName);
						if(tabName.equalsIgnoreCase("t_accounting_data")){
							whereClause=" where original_view_id="+srcViewId;
						}
						String baseQry="select distinct("+colName+") from "+schemaName+"."+tabName;
						String finalQuery="";
						String baseWhereQry="";
						if(whereClause!=null && !(whereClause.equals("")) && !(whereClause.isEmpty())){
							baseWhereQry=baseQry+whereClause;
						}
						else baseWhereQry=baseQry;
						log.info("baseWhereQry: "+baseWhereQry);
						finalQuery=baseWhereQry+" order by "+colName+" asc";
						log.info("finalQuery :"+finalQuery);
						result=stmt.executeQuery(finalQuery);
						while(result.next())
						{
							if(result.getString(colName)!=null)
								values.add(result.getString(colName));
						}
					}

					log.info("previewing final values");
					log.info("values :"+values);
				} else {
					values = null;
				}

				if(!(values.contains("(Blank)"))){
					values.add("(Blank)");
				}
			}
		} else {
			log.info("**********in else dataview **********");
			log.info("tableName :"+tableName +" columnName :"+columnName);
			String viewQuery="select distinct(`"+columnName+"`) from "+schemaName+".`"+tableName+"`";
			log.info("viewQuery :"+viewQuery);
			result=stmt.executeQuery(viewQuery);
			List<String> valuesList=new ArrayList<String>();
			while(result.next())
			{

				if(result.getString(columnName)!=null)
				{
					//log.info("result.getString(columnName) :"+result.getString(columnName));
					String str=result.getString(columnName);
					valuesList.add(str);
				}
			}
			log.info("valuesList :"+valuesList);
			values = valuesList;
			log.info("values :"+values);
		}

		if (values != null) {
			valList=values;
		}

		if(!(values.contains("(Blank)"))){
			values.add("(Blank)");
		}

		log.info("valList aftr looping: " + valList);
		HashMap map = new HashMap();
		if (rparam != null) {
			List<ReportParameters> rparamList = reportParametersRepository
					.findByReportIdOrderByIdAsc(repId);
			for (int i = 0; i < rparamList.size(); i++) {
				if (rparamList.get(i).getId().equals(rParamId))
					map.put("index", i);
			}
			map.put("fieldValuesList", valList);

		}
		log.info("***** end time ***********");
		if(result!=null)
			result.close();
		if(stmt!=null)
			stmt.close();
		if(conn!=null)
			conn.close();
		return map;
	}

}
