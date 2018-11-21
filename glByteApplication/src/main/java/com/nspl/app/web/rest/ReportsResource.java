package com.nspl.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.ApplicationPrograms;
import com.nspl.app.domain.DataViews;
import com.nspl.app.domain.ReportParameters;
import com.nspl.app.domain.ReportRequests;
import com.nspl.app.domain.ReportType;
import com.nspl.app.domain.Reports;
import com.nspl.app.repository.ApplicationProgramsRepository;
import com.nspl.app.repository.DataViewsColumnsRepository;
import com.nspl.app.repository.DataViewsRepository;
import com.nspl.app.repository.FavouriteReportsRepository;
import com.nspl.app.repository.FileTemplateLinesRepository;
import com.nspl.app.repository.LookUpCodeRepository;
import com.nspl.app.repository.ReportDefinationRepository;
import com.nspl.app.repository.ReportParametersRepository;
import com.nspl.app.repository.ReportRequestsRepository;
import com.nspl.app.repository.ReportTypeRepository;
import com.nspl.app.repository.ReportsRepository;
import com.nspl.app.repository.TenantConfigRepository;
import com.nspl.app.service.ActiveStatusService;
import com.nspl.app.service.DataViewsService;
import com.nspl.app.service.FileExportService;
import com.nspl.app.service.PropertiesUtilService;
import com.nspl.app.service.ReportsService;
import com.nspl.app.service.SFTPUtilService;
import com.nspl.app.service.UserJdbcService;
import com.nspl.app.web.rest.util.ByteArrayDataSource;
import com.nspl.app.web.rest.util.HeaderUtil;
import com.nspl.app.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiParam;
import io.github.jhipster.config.JHipsterProperties;
import io.github.jhipster.web.util.ResponseUtil;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.CharEncoding;
import org.dom4j.DocumentException;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.Month;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import javax.activation.DataSource;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * REST controller for managing Reports.
 */
@RestController
@RequestMapping("/api")
public class ReportsResource {

	private final Logger log = LoggerFactory.getLogger(ReportsResource.class);

	private static final String ENTITY_NAME = "reports";

	private final ReportsRepository reportsRepository;

	private final FavouriteReportsRepository favouriteReportsRepository;

	@Inject
	ReportDefinationRepository reportDefinationRepository;

	@Inject
	DataViewsRepository dataViewsRepository;

	@Inject
	PropertiesUtilService propertiesUtilService;

	@Inject
	DataViewsService dataViewsService;

	@Inject
	FileTemplateLinesRepository fileTemplateLinesRepository;

	@Inject
	DataViewsColumnsRepository dataViewsColumnsRepository;

	@Inject
	ReportParametersRepository reportParametersRepository;

	@Inject
	ReportTypeRepository reportTypeRepository;

	@Inject
	LookUpCodeRepository lookUpCodeRepository;

	@Inject
	private Environment env;

	@Inject
	ReportsService reportsService;

	@Inject
	SFTPUtilService sftpService;

	@Autowired
	org.apache.hadoop.conf.Configuration hadoopConfiguration;

	@Autowired
	ApplicationProgramsRepository applicationProgramsRepository;

	@Inject
	ReportRequestsRepository reportRequestsRepository;

	@Inject
	UserJdbcService userJdbcService;

	@Inject
	FileExportService fileExportService;

	@Inject
	TenantConfigRepository tenantConfigRepository;

	@Inject
	ActiveStatusService activeStatusService;

	private final JHipsterProperties jHipsterProperties;

	private final SpringTemplateEngine templateEngine;

	private final MessageSource messageSource;

	private final JavaMailSender javaMailSender;

	public ReportsResource(ReportsRepository reportsRepository,FavouriteReportsRepository favouriteReportsRepository,JHipsterProperties jHipsterProperties, JavaMailSender javaMailSender,
			MessageSource messageSource, SpringTemplateEngine templateEngine) {
		this.reportsRepository = reportsRepository;
		this.favouriteReportsRepository = favouriteReportsRepository;
		this.jHipsterProperties = jHipsterProperties;
		this.javaMailSender = javaMailSender;
		this.messageSource = messageSource;
		this.templateEngine = templateEngine;
	}

	/**
	 * POST  /reports : Create a new reports.
	 *
	 * @param reports the reports to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new reports, or with status 400 (Bad Request) if the reports has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/reports")
	@Timed
	public ResponseEntity<Reports> createReports(@RequestBody Reports reports) throws URISyntaxException {
		log.debug("REST request to save Reports : {}", reports);
		if (reports.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new reports cannot already have an ID")).body(null);
		}
		Reports result = reportsRepository.save(reports);
		return ResponseEntity.created(new URI("/api/reports/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /reports : Updates an existing reports.
	 *
	 * @param reports the reports to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated reports,
	 * or with status 400 (Bad Request) if the reports is not valid,
	 * or with status 500 (Internal Server Error) if the reports couldn't be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/reports")
	@Timed
	public ResponseEntity<Reports> updateReports(@RequestBody Reports reports) throws URISyntaxException {
		log.debug("REST request to update Reports : {}", reports);
		if (reports.getId() == null) {
			return createReports(reports);
		}
		Reports result = reportsRepository.save(reports);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, reports.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /reports : get all the reports.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of reports in body
	 */
	@GetMapping("/getAllReportTemplates")
	@Timed
	public List<HashMap> getAllReportTemplates(HttpServletRequest req) {
		log.debug("REST request to get all Report templates");
		HashMap map1=userJdbcService.getuserInfoFromToken(req);
		Long tenantId=Long.parseLong(map1.get("tenantId").toString());
		List<Reports> reportList=reportsRepository.findByTenantIdOrderByIdDesc(tenantId);
		List<HashMap> finalList=new ArrayList<HashMap>();
		for(Reports eachReport:reportList) {
			HashMap oneReport=new HashMap();
			oneReport.put("id", eachReport.getId());
			oneReport.put("reportName", eachReport.getReportName());
			oneReport.put("idForDisplay", eachReport.getIdForDisplay());
			finalList.add(oneReport);
		}
		return finalList;
	}

	/**
	 * GET  /reports : get all the reports.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of reports in body
	 */
	@GetMapping("/reports")
	@Timed
	public ResponseEntity<List<Reports>> getAllReports(@ApiParam Pageable pageable) {
		log.debug("REST request to get a page of Reports");
		Page<Reports> page = reportsRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/reports");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET  /reports/:id : get the "id" reports.
	 *
	 * @param id the id of the reports to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the reports, or with status 404 (Not Found)
	 */
	@GetMapping("/reports/{id}")
	@Timed
	public ResponseEntity<Reports> getReports(@PathVariable String id, HttpServletRequest request) {
		log.debug("REST request to get Reports : {}", id);
		HashMap map1=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map1.get("tenantId").toString());
		Reports reports = reportsRepository.findByTenantIdAndIdForDisplay(tenantId, id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(reports));
	}

	/**
	 * DELETE  /reports/:id : delete the "id" reports.
	 *
	 * @param id the id of the reports to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/reports/{id}")
	@Timed
	public ResponseEntity<Void> deleteReports(@PathVariable Long id) {
		log.debug("REST request to delete Reports : {}", id);
		reportsRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * Author: Swetha
	 * GET: /getReportsByTenant - Api to fetch list of Reports tagged for a Tenant
	 * @param tenantId
	 * @return
	 * @throws URISyntaxException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	/**Added condition for datetime while sorting**/

	@GetMapping("/getReportsByTenant")
	@Timed	
	public List<JSONObject>  getReportsByTenant(HttpServletRequest request,HttpServletResponse response,@RequestParam(value = "page" , required = false) Integer pageNumber,
			@RequestParam(value = "per_page", required = false) Integer pageSize, @RequestParam(required=false) String reportType,
			@RequestParam(required = false) String sortDirection,@RequestParam(required = false) String sortCol,
			@RequestParam(required = false) Boolean favRpts,@RequestParam(required = false) Boolean recentRpts, @RequestParam(required = false) String searchKeyword) 
					throws URISyntaxException, ClassNotFoundException, SQLException, ParseException{

		HashMap map1=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map1.get("tenantId").toString());
		Long userId=Long.parseLong(map1.get("userId").toString());
		log.debug("REST request to getReportsByTenant by tenantId: "+tenantId+"reportType: "+reportType+"sortCol: "+sortCol+"favList: "+favRpts+"recentRpts: "+recentRpts);
		List<JSONObject> repDtoList=new ArrayList<JSONObject>();
		HashMap<Long, ReportType> reportTypeList=reportsService.getReportTypes(tenantId);
		HashMap<Long, String> reportTypeNameList=reportsService.getReportTypesLivy(tenantId);
		HashMap<String,Long> reportTypeRevList=(HashMap<String, Long>) MapUtils.invertMap(reportTypeNameList);
		Long reportTypeId=reportTypeRevList.get(reportType);
		log.info("reportTypeId: "+reportTypeId);
		List<BigInteger> reportIdList=new ArrayList<BigInteger>();
		List<Long> reportIdLongList=new ArrayList<Long>();
		int xCount=0;
		if(sortDirection==null)
			sortDirection="Descending";
		if(sortCol==null || sortCol.isEmpty() || sortCol.length()<1)
			sortCol="Id";

		if(reportTypeId!=null){
			reportIdList=reportsRepository.fectchActiveReportsByTenantIdAndReportTypeId(tenantId,reportTypeId);
		}
		else{
			reportIdList=reportsRepository.fectchActiveReportsByTenantId(tenantId);
		}

		// ********* To filter favourite reports only		Update by @Rk
		List<BigInteger> favRptIdsList = favouriteReportsRepository.fectchCurrentUserFavouriteReportIds(userId,tenantId);
		if(favRpts){
			for(int i=0;i<reportIdList.size();i++){
				BigInteger id=reportIdList.get(i);
				if(favRptIdsList.contains(id)){
					reportIdLongList.add(id.longValue());
				}
			}
		}else if(recentRpts){
			List<BigInteger> recentRequests=reportRequestsRepository.fectchCurrentUserRecentRanReportIds(userId,tenantId);
			for(int i=0;i<recentRequests.size();i++){
				BigInteger id=recentRequests.get(i);
				if(reportIdList.contains(id)){
					reportIdLongList.add(id.longValue());
				}
			}
		}
		else{
			for(int i=0;i<reportIdList.size();i++)
				reportIdLongList.add(reportIdList.get(i).longValue());
		}
		// ***** END OF UPDATE **********
		int totReportsCnt=reportIdLongList.size();
		log.info("totReportsCnt: "+totReportsCnt);

		//log.info("reportIdLongList: "+reportIdLongList);
		log.info("pageNumber: "+pageNumber+" pageSize: "+pageSize+" sortDirection: "+sortDirection+" sortCol: "+sortCol);
		if(reportIdLongList.size()>0)
		{
			List<Reports> reportsList=reportsRepository.findByTenantIdAndIdInOrderByIdDesc(tenantId, reportIdLongList);
			//List<LinkedHashMap> maps = new ArrayList<LinkedHashMap>();


			Iterator itr=reportsList.iterator();
			while(itr.hasNext()){
				Reports reports= (Reports) itr.next();
				LinkedHashMap reportsDto=new LinkedHashMap();
				reportsDto.put("id",reports.getIdForDisplay());
				reportsDto.put("accVal",reports.getAccVal());
				if(reports.getAllowDrillDown()!=null)
					reportsDto.put("allowDrillDown",reports.getAllowDrillDown().toString());
				else
					reportsDto.put("allowDrillDown",null);
				if(reports.getCreatedBy()!=null)
					reportsDto.put("createdBy",reports.getCreatedBy().toString());
				else
					reportsDto.put("createdBy","");
				if(reports.getCreationDate()!=null)
					reportsDto.put("creationDate",reports.getCreationDate());
				else
					reportsDto.put("creationDate",null);
				reportsDto.put("description",reports.getDescription());
				/*if(reports.isEnableFlag()!=null)
    				reportsDto.put("enableFlag",reports.isEnableFlag().toString());
    			else
    				reportsDto.put("enableFlag",null);*/
				Boolean activeStatus=activeStatusService.activeStatus(reports.getStartDate(), reports.getEndDate(), reports.isEnableFlag());
				reportsDto.put("enableFlag",activeStatus);
				if(reports.getEndDate()!=null)
					reportsDto.put("endDate",reports.getEndDate().toString());
				else
					reportsDto.put("endDate",null);
				if(reports.getLastUpdatedBy()!=null)
					reportsDto.put("lastUpdatedBy",reports.getLastUpdatedBy().toString());
				else
					reportsDto.put("lastUpdatedBy",null);
				if(reports.getLastUpdatedDate()!=null)
					reportsDto.put("lastUpdatedDate",reports.getLastUpdatedDate());
				else
					reportsDto.put("lastUpdatedDate",null);
				reportsDto.put("recVal",reports.getRecVal());
				reportsDto.put("reportMode",reports.getReportMode());
				reportsDto.put("reportName",reports.getReportName());
				Long repTypeId=reports.getReportTypeId();
				ReportType repType=reportTypeList.get(repTypeId);
				if(repTypeId!=null)
					reportsDto.put("reportTypeId",repTypeId.toString());
				else
					reportsDto.put("reportTypeId",null);
				if(repType!=null){
					reportsDto.put("reportTypeCode",repType.getType());
					reportsDto.put("reportTypeName",repType.getDisplayName());
				}
				reportsDto.put("reportVal01",reports.getReportVal01());
				reportsDto.put("reportVal02",reports.getReportVal02());
				reportsDto.put("reportViewType",reports.getReportViewType());
				if(reports.getSourceViewId()!=null){
					reportsDto.put("sourceViewId",reports.getSourceViewId().toString());
					DataViews dv=dataViewsRepository.findOne(reports.getSourceViewId());
					reportsDto.put("sourceView",dv.getDataViewDispName());
				}
				else
					reportsDto.put("sourceViewId",null);
				if(reports.getStartDate()!=null)
					reportsDto.put("startDate",reports.getStartDate().toString());
				else
					reportsDto.put("startDate",null);
				if(reports.getTenantId()!=null)
					reportsDto.put("tenantId",reports.getTenantId().toString());
				else
					reportsDto.put("tenantId",null);
				if(favRptIdsList.contains(new BigInteger(reports.getId().toString())))
					reportsDto.put("isFavourite",true);
				else
					reportsDto.put("isFavourite",false);

				//Adding first parameter info
				List<ReportParameters> repParamsList=reportParametersRepository.findByReportId(reports.getId());
				String firstParam=repParamsList.get(0).getDisplayName();
				reportsDto.put("firstParam", firstParam+"...");


				//Added Request Information
				List<ReportRequests> repRequests=reportRequestsRepository.findByReportIdAndStatusOrderByGeneratedTimeDesc(reports.getId(), "SUCCEEDED");
				if(repRequests!=null && repRequests.size()>0){
					ReportRequests req=repRequests.get(0);
					reportsDto.put("requestId",req.getIdForDisplay());
					reportsDto.put("requestName",req.getReqName());
					reportsDto.put("lastRun",req.getGeneratedTime());
					HashMap map=new HashMap();
					if(req.getCreatedBy()!=null)
						map=userJdbcService.jdbcConnc(req.getCreatedBy(),tenantId);
					if(map!=null){
						if(map.containsKey("assigneeName")){
							if(map.get("assigneeName")!=null && !(map.get("assigneeName").toString().isEmpty()))
								reportsDto.put("lastRunBy",map.get("assigneeName").toString());
						}
					}
				}
				JSONObject jsonValue =new JSONObject();
				JSONObject obj=new JSONObject();
				obj.putAll(reportsDto);

				repDtoList.add(obj);
			}

			if(repDtoList!=null && !(repDtoList.isEmpty()))
				log.info("repDtoList sz: "+repDtoList.size());
		}

		//Search Functionality
		List<JSONObject> searchFilteredObjList=new ArrayList<JSONObject>();
		if(searchKeyword!=null){
			List<String> keyList=new ArrayList<String>();
			keyList.add("reportName");
			keyList.add("lastRunBy");
			keyList.add("createdBy");
			keyList.add("reportTypeName");
			keyList.add("description");
			keyList.add("sourceView");

			for (int i = 0; i < repDtoList.size(); i++) {

				for (String  keys:keyList)
				{
					if(repDtoList.get(i).get(keys)!=null)
						if(repDtoList.get(i).get(keys).toString().toLowerCase().contains(searchKeyword.toLowerCase())){
							searchFilteredObjList.add(repDtoList.get(i));
							break;
						}
				}

			}
			xCount=searchFilteredObjList.size();
			log.info("xCount after searching : "+xCount);
		}
		else{
			searchFilteredObjList=repDtoList;
			xCount=totReportsCnt;
		}
		log.info("xCount: "+xCount);
		String sortColumn=sortCol;
		String sortDir=sortDirection;
		if(sortColumn.equalsIgnoreCase("lastRun"))
		{
			Collections.sort(searchFilteredObjList, new Comparator<JSONObject>() {
				public int compare(JSONObject a, JSONObject b) {
					if (a.get("lastRun") == null || b.get("lastRun") == null)
						return 0;

					ZonedDateTime aDate=ZonedDateTime.parse(a.get("lastRun").toString());
					ZonedDateTime bDate=ZonedDateTime.parse(b.get("lastRun").toString());

					return aDate.compareTo(bDate);
				}
			});
		}
		else if(!sortColumn.equalsIgnoreCase("lastRun"))
		{
			Collections.sort( searchFilteredObjList, new Comparator<JSONObject>() { 
				@Override
				public int compare(JSONObject a, JSONObject b) {
					String valA = new String();
					String valB = new String();

					valA = (String) a.get(sortColumn);
					valB = (String) b.get(sortColumn);
					if(valA!=null && valB!=null)
						return 0;
					if (valA == null)
						return 1;
					else if (valB == null)
						return -1;
					if(sortDir!=null && sortDir.equalsIgnoreCase("ascending"))
						return valA.compareTo(valB);
					else
						return -valA.compareTo(valB);

				}
			});
		}
		response.addIntHeader("X-COUNT", xCount);
		int limit = 0;
		if(pageNumber == null || pageNumber == 0)
		{
			pageNumber = 0;
		}
		if(pageSize == null || pageSize == 0)
		{
			pageSize = xCount;
		}
		limit = ((pageNumber+1) * pageSize + 1)-1;
		int startIndex=pageNumber*pageSize; 

		if(limit>xCount){
			limit=xCount;
		}
		log.info("startIndex: "+startIndex+" limit: "+limit);
		List<JSONObject> limitedOutputList=new ArrayList<JSONObject>();
		if(searchFilteredObjList!=null && searchFilteredObjList.size()>0 && limit<=searchFilteredObjList.size()){
			limitedOutputList=searchFilteredObjList.subList(startIndex, limit);
		}
		else limitedOutputList=searchFilteredObjList;
		log.info("***end of API**** "+ZonedDateTime.now());
		return limitedOutputList;

	}


	/**
	 * Author: Swetha
	 * Api to validate duplication of ReportName
	 * @param tenantId
	 * @param reportName
	 * @return
	 */
	@GetMapping("/validateReportName")
	@Timed
	public String validateReportName(HttpServletRequest request,@RequestParam String reportName){

		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		log.info("Rest Request to validateReportName with tenantId: "+tenantId+" and reportName: "+reportName);
		String result="";
		Long count=reportsRepository.fetchReportNameCount(tenantId, reportName);
		log.info("count for "+reportName+" : "+count);
		if(count>0){
			List<Reports> reports=reportsRepository.findListByTenantIdAndReportName(tenantId, reportName);
			result=reports.get(0).getIdForDisplay();
		}
		return result;
	}

	/**
	 * author :ravali
	 * Swetha: Integrated Email Template with jhipster default mail services
	 *         Physical Attachment without physical file creation
	 * @param email
	 * @param filePath
	 * @param tenantId
	 * @throws AddressException
	 * @throws MessagingException
	 * @throws IOException
	 * @throws DocumentException
	 * @throws URISyntaxException
	 * @throws ParseException
	 * Desc :sharing csv through mail
	 */
	@GetMapping("/shareReportingCSV")
	@Timed
	public LinkedHashMap shareReportingCSV(@RequestParam(required=true) String emailList,@RequestParam String requetId,
			@RequestParam(required=true) String comments,@RequestParam(required=true) String url, HttpServletRequest request, HttpServletResponse response) 
					throws AddressException, MessagingException, IOException, DocumentException, URISyntaxException, ParseException, ClassNotFoundException, SQLException
	{
		log.info("REST API FOR SHARING SampleCSV");
		log.info("email :"+emailList);
		log.info("comments********** :"+comments);

		LinkedHashMap finalMap=new LinkedHashMap();
		finalMap.put("status", "success");

		Long send=0l;
		Long failed=0l;
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());

		ReportRequests reportRequests=reportRequestsRepository.findByTenantIdAndIdForDisplay(tenantId, requetId);
		Long repId=reportRequests.getReportId();
		Reports reports=reportsRepository.findOne(repId);

		LinkedHashMap cmpltOutput=reportsService.hdfsFileReading(reportRequests.getOutputPath());

		List<HashMap> header=(List<HashMap>) cmpltOutput.get("columns");
		List<HashMap> values=(List<HashMap>) cmpltOutput.get("data");
		System.out.println("values sz: "+values.size());


		List<String> head=new ArrayList<String>();
		for(int i=0;i<header.size();i++)
		{
			head.add(header.get(i).get("field").toString());
		}

		log.info("head :"+head);

		List finValList=new ArrayList();

		String output = StringUtils.join(head.toArray(), ",") + "\n";
		for (HashMap<String, String> linkedMap : values) {
			output = output + getCommaSeparatedRow(head, linkedMap) + "\n";
		}
		ByteArrayDataSource dataSource = new ByteArrayDataSource(reportRequests.getReqName(),"application/csv",output.getBytes());
		System.out.println("dataSource name: "+dataSource.getName());
		System.out.println("output.getBytes() length: "+output.getBytes().length);
		String[] emailArr=emailList.split(";");

		for(int i=0;i<emailArr.length;i++){
			String to=emailArr[i];
			log.info("to: "+to);
			try
			{
				System.out.println("reportRequests.getReqName(): "+reportRequests.getReqName());
				sendActivationValidationEmail(to,request,reports, reportRequests,url,comments,reportRequests.getReqName()+".csv",dataSource); //add all the variables to replace in content
				send=send+1l;
				log.info("Sent message successfully to : "+to);
			}catch(Exception e)
			{
				log.info("Message sent failed to : "+to);
				log.info("ERROR >>>>>>"+e.getMessage());
				finalMap.put("status", "success");
				failed=failed+1l;
			}
		}

		log.info("send: "+send+" failed: "+failed);
		finalMap.put("send", send);
		finalMap.put("failed", failed);


		return finalMap;


	}

	@PostMapping("/sortbykey")
	@Timed
	public static void sortbykey(@RequestParam LinkedHashMap output)
	{
		Map<String, Integer> map = new HashMap<>();
		TreeMap<String, Integer> sorted = new TreeMap<>();

		sorted.putAll(map);

		for (Map.Entry<String, Integer> entry : sorted.entrySet()) 
			System.out.println("Key = " + entry.getKey() + 
					", Value = " + entry.getValue());        
	}

	@PostMapping("/sortingValuesInReportOutputJson")
	@Timed
	public JSONObject sortingValuesInJson(@RequestParam Long requestId,@RequestParam String sortColumn,@RequestParam(required=false) String sortOrder,
			@RequestParam(required=false) Integer pageNumber, @RequestParam(required=false) Integer pageSize,HttpServletResponse response) 
					throws IOException, JSONException, ParseException, URISyntaxException
	{
		log.info("requestId: "+requestId);
		log.info("pageNumber: "+pageNumber+" & pageSize: "+pageSize);
		ReportRequests req=reportRequestsRepository.findOne(requestId);
		log.info("req.getOutputPath(): "+req.getOutputPath());
		JSONObject jsonValue =new JSONObject();
		if(req.getOutputPath()!=null)
		{
			LinkedHashMap cmpltOutput=reportsService.hdfsFileReading(req.getOutputPath());
			JSONObject obj=new JSONObject();
			obj.putAll(cmpltOutput);
			String paramVal=obj.toJSONString();
			JSONParser parser = new JSONParser();
			jsonValue = (JSONObject) parser.parse(paramVal);


			List<JSONObject> jsonValueList = (List<JSONObject>) jsonValue.get("data");

			Collections.sort( jsonValueList, new Comparator<JSONObject>() { 


				@Override
				public int compare(JSONObject a, JSONObject b) {
					String valA = new String();
					String valB = new String();

					valA = (String) a.get(sortColumn);
					valB = (String) b.get(sortColumn);


					if(sortOrder!=null && sortOrder.equalsIgnoreCase("ascending"))
						return valA.compareTo(valB);
					else
						return -valA.compareTo(valB);
					//if you want to change the sort order, simply use the following:
					//return -valA.compareTo(valB);
				}
			});


			List<JSONObject> finalObj=new ArrayList<JSONObject>();
			int totDataCnt=Integer.parseInt(cmpltOutput.get("X-COUNT").toString());
			log.info("totDataCnt: "+totDataCnt);
			response.addIntHeader("X-COUNT", totDataCnt);

			int limit = 0;
			if(pageNumber == null || pageNumber == 0)
			{
				pageNumber = 0;
			}
			if(pageSize == null || pageSize == 0)
			{
				pageSize = totDataCnt;
			}
			limit = ((pageNumber+1) * pageSize + 1)-1;
			int startIndex=pageNumber*pageSize; 

			if(limit>totDataCnt){
				limit=totDataCnt;
			}

			log.info("startIndex: "+startIndex+" limit: "+limit);

			for(int j=startIndex;j<limit;j++){

				JSONObject map=jsonValueList.get(j);
				finalObj.add(map);

			}

			log.info("finalObj: "+finalObj);
			jsonValue.put("data", finalObj);
		}
		return jsonValue;
	}

	@PostMapping("/SearchValuesInReportOutputJson")
	@Timed
	public JSONObject SearchValuesInReportOutputJson(@RequestParam Long requestId,@RequestParam String serachString,
			@RequestParam(required=false) Integer pageNumber, @RequestParam(required=false) Integer pageSize,HttpServletResponse response) 
					throws IOException, JSONException, ParseException, URISyntaxException
	{
		log.info("requestId: "+requestId);
		log.info("pageNumber: "+pageNumber+" & pageSize: "+pageSize);
		ReportRequests req=reportRequestsRepository.findOne(requestId);
		log.info("req :"+req);
		JSONObject jsonValue =new JSONObject();
		if(req.getOutputPath()!=null)
		{
			LinkedHashMap cmpltOutput=reportsService.hdfsFileReading(req.getOutputPath());
			JSONObject obj=new JSONObject();
			obj.putAll(cmpltOutput);
			String paramVal=obj.toJSONString();
			JSONParser parser = new JSONParser();
			jsonValue = (JSONObject) parser.parse(paramVal);


			List<JSONObject> jsonValueList = (List<JSONObject>) jsonValue.get("data");
			List<JSONObject> searchObj=new ArrayList<JSONObject>();

			List<JSONObject> finalObj=new ArrayList<JSONObject>();

			List<HashMap> header=(List<HashMap>) cmpltOutput.get("columns");
			List<String> headList=new ArrayList<String>();
			for(int i=0;i<header.size();i++)
			{
				headList.add(header.get(i).get("field").toString());
			}

			log.info("head :"+headList);


			for (int i = 0; i < jsonValueList.size(); i++) {

				for (String  head:headList)
				{


					if(jsonValueList.get(i).get(head).toString().toLowerCase().contains(serachString.toLowerCase()))
						searchObj.add(jsonValueList.get(i));
				}

			}

			int totDataCnt= searchObj.size();
			response.addIntHeader("X-COUNT", searchObj.size());

			int limit = 0;
			if(pageNumber == null || pageNumber == 0)
			{
				pageNumber = 0;
			}
			if(pageSize == null || pageSize == 0)
			{
				pageSize = totDataCnt;
			}
			log.info("pageNumber :"+pageNumber +" pageSize :"+pageSize );
			limit = ((pageNumber+1) * pageSize + 1)-1;
			log.info("limit :"+limit);
			int startIndex=pageNumber*pageSize; 

			if(limit>totDataCnt){
				limit=totDataCnt;
			}

			log.info("startIndex: "+startIndex+" limit: "+limit);
			if(searchObj!=null && !searchObj.isEmpty())
			{
				for(int j=startIndex;j<limit;j++){


					JSONObject map=searchObj.get(j);
					finalObj.add(map);


				}
			}

			log.info("finalObj: "+finalObj);
			jsonValue.put("data", finalObj);
		}
		return jsonValue;
	}


	/**seraching based on key values**/

	@PostMapping("/SearchKeyValuesInReportOutputJson")
	@Timed
	public JSONObject SearchKeyValuesInReportOutputJson(@RequestParam(required=false) LinkedHashMap keyValues,@RequestParam Long requestId,@RequestParam String serachString,
			@RequestParam(required=false) Integer pageNumber, @RequestParam(required=false) Integer pageSize,HttpServletResponse response) 
					throws IOException, JSONException, ParseException, URISyntaxException
	{
		log.info("requestId: "+requestId);
		log.info("pageNumber: "+pageNumber+" & pageSize: "+pageSize);
		ReportRequests req=reportRequestsRepository.findOne(requestId);
		log.info("req :"+req);
		JSONObject jsonValue =new JSONObject();
		if(req.getOutputPath()!=null)
		{
			LinkedHashMap cmpltOutput=reportsService.hdfsFileReading(req.getOutputPath());
			JSONObject obj=new JSONObject();
			obj.putAll(cmpltOutput);
			String paramVal=obj.toJSONString();
			JSONParser parser = new JSONParser();
			jsonValue = (JSONObject) parser.parse(paramVal);


			List<JSONObject> jsonValueList = (List<JSONObject>) jsonValue.get("data");
			List<JSONObject> searchObj=new ArrayList<JSONObject>();

			List<JSONObject> finalObj=new ArrayList<JSONObject>();

			List<HashMap> header=(List<HashMap>) cmpltOutput.get("columns");
			List<String> headList=new ArrayList<String>();
			for(int i=0;i<header.size();i++)
			{
				headList.add(header.get(i).get("field").toString());
			}

			log.info("head :"+headList);


			for (int i = 0; i < jsonValueList.size(); i++) {

				if(keyValues.size()>0 && keyValues !=null)
				{

				}
				else
				{
					for (String  head:headList)
					{


						if(jsonValueList.get(i).get(head).toString().toLowerCase().contains(serachString.toLowerCase()))
							searchObj.add(jsonValueList.get(i));
					}
				}

			}

			int totDataCnt= searchObj.size();
			response.addIntHeader("X-COUNT", searchObj.size());

			int limit = 0;
			if(pageNumber == null || pageNumber == 0)
			{
				pageNumber = 0;
			}
			if(pageSize == null || pageSize == 0)
			{
				pageSize = totDataCnt;
			}
			log.info("pageNumber :"+pageNumber +" pageSize :"+pageSize );
			limit = ((pageNumber+1) * pageSize + 1)-1;
			log.info("limit :"+limit);
			int startIndex=pageNumber*pageSize; 

			if(limit>totDataCnt){
				limit=totDataCnt;
			}

			log.info("startIndex: "+startIndex+" limit: "+limit);
			if(searchObj!=null && !searchObj.isEmpty())
			{
				for(int j=startIndex;j<limit;j++){


					JSONObject map=searchObj.get(j);
					finalObj.add(map);


				}
			}

			log.info("finalObj: "+finalObj);
			jsonValue.put("data", finalObj);
		}
		return jsonValue;
	}


	/**
	 * Author: Swetha
	 * Replicated from Gateway User Activation Mail
	 * @param email
	 * @param request
	 * @param reports
	 * @param reportRequests
	 * @param reporturl
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	@Async
	public void sendActivationValidationEmail(String email,HttpServletRequest request,Reports reports, ReportRequests reportRequests,String reporturl, String comments,String attachmentFilename, DataSource dataSource) throws ClassNotFoundException, SQLException {
		log.debug("Sending Activation Validation email to '{}'", email);
		// System.out.println("dataSource.getContentType(): "+dataSource.getContentType());
		//System.out.println("dataSource.getName(): "+dataSource.getName());
		sendEmailFromTemplate(email, "reportsNotification", "email.reportshare.title",request,reports,reportRequests,reporturl,comments,attachmentFilename,dataSource);
	}

	/**
	 * Author: Swetha
	 * Replicated from Gateway User Activation Mail
	 * Integrated Reports specific info to email content
	 * @param email
	 * @param templateName
	 * @param titleKey
	 * @param request
	 * @param reports
	 * @param reportRequests
	 * @param reporturl
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	@Async
	public void sendEmailFromTemplate(String email, String templateName, String titleKey, HttpServletRequest request,Reports reports, 
			ReportRequests reportRequests,String reporturl,String comments, String attachmentFilename, DataSource dataSource) throws ClassNotFoundException, SQLException {
		Locale locale = Locale.forLanguageTag("en");
		Context context = new Context(locale);
		String portStr = "";
		String applicationUrl=request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
		log.info("applicationUrl", applicationUrl);
		portStr = ":"+request.getServerPort();
		if(templateName.equals("reportsNotification"))
		{
			String sslProtocol = "http";
			if(env.getProperty("server.ssl.enabled")!=null)
			{
				if(env.getProperty("server.ssl.enabled").toString().equals("true"))
				{
					sslProtocol = "https";
				}
			}
			context.setVariable(applicationUrl, sslProtocol+"://"+applicationUrl+portStr);
		}
		else
		{
			context.setVariable(applicationUrl, jHipsterProperties.getMail().getBaseUrl());
		}
		String content = templateEngine.process(templateName, context);


		Long repTypeId=reports.getReportTypeId();
		Long tenantId=reports.getTenantId();
		HashMap<Long,ReportType> reportTypes=reportsService.getReportTypes(tenantId);
		ReportType reportTypeData=reportTypes.get(repTypeId);
		String reportType=reportTypeData.getDisplayName();
		String reportId=reports.getIdForDisplay();
		String reportName=reports.getReportName();
		String reqName=reportRequests.getReqName();
		content = content.replaceAll("ReportName",reports.getReportName());
		content = content.replaceAll("ReportType",reportType);
		content = content.replaceAll("RequestName",reportRequests.getReqName());
		if(comments!=null && comments.length()>0) {
			content = content.replaceAll("comments",comments);
		}
		else {
			content = content.replaceAll("comments","");
		}
		ZonedDateTime generatedTime=reportRequests.getGeneratedTime();
		Month month=generatedTime.getMonth();
		String mnthName=month.name();
		mnthName=mnthName.substring(0,1).toUpperCase() + mnthName.substring(1).toLowerCase();;
		int day=generatedTime.getDayOfMonth();
		int year=generatedTime.getYear();
		String generatedOn=day+" "+mnthName+" "+year+" "+generatedTime.getHour()+":"+generatedTime.getMinute()+":"+generatedTime.getSecond();
		content = content.replaceAll("GeneratedOn",generatedOn);
		HashMap userInfo=userJdbcService.jdbcConnc(reportRequests.getCreatedBy(),tenantId);
		String submittedBy=userInfo.get("assigneeName").toString();
		content = content.replaceAll("SubmittedBy",submittedBy);
		Long srcId=reports.getSourceViewId();
		DataViews dataViews=dataViewsRepository.findOne(srcId);
		String dataSourceTagged=dataViews.getDataViewDispName();
		content = content.replaceAll("DataSourceTagged",dataSourceTagged);
		String loginUrl=""; 
		if(loginUrl!=null){
			loginUrl="#";
		}
		String requestId=reportRequests.getIdForDisplay();
		String reportUrl=request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
		String reportView="<a href="+reporturl+"#/reports/(content:run-reports/"+requestId+"/"+reportId+">Click Here</a>";
		String registerView="<a href="+reporturl+">Register to Application</a>";
		//log.info("reportView: "+reportView);
		HashMap userMap=userJdbcService.getUserByEmail(email, tenantId);
		System.out.println("userMap: "+userMap);
		if(userMap!=null && !(userMap.isEmpty())){
			if(userMap.containsKey("assigneeName")){
				if(userMap.get("assigneeName")!=null && !(userMap.get("assigneeName").toString().isEmpty())){
					content = content.replaceAll("ResourceName", userMap.get("assigneeName").toString());
					content = content.replaceAll("reportView",reportView);
				}
				else{
					content = content.replaceAll("reportView",registerView); 
				}
			}
		}
		String subject = messageSource.getMessage(titleKey, null, locale);
		sendEmail(email, subject, content, true, true, attachmentFilename,dataSource );

	}

	/**
	 * Author: Swetha
	 * Replicated from Gateway User Activation Mail
	 * @param to
	 * @param subject
	 * @param content
	 * @param isMultipart
	 * @param isHtml
	 */
	@Async
	public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml, String attachmentFilename, DataSource dataSource ) {
		log.debug("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
				isMultipart, isHtml, to, subject, content);
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
			helper.setTo(to);
			helper.setFrom(jHipsterProperties.getMail().getFrom());
			helper.setSubject(subject);
			helper.setText(content, isHtml);
			// System.out.println("attachmentFilename: "+attachmentFilename+" dataSource: "+dataSource);
			helper.addAttachment(attachmentFilename, dataSource);
			javaMailSender.send(mimeMessage);
			log.debug("Sent email to User '{}'", to);
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.warn("Email could not be sent to user '{}'", to, e);
			} else {
				log.warn("Email could not be sent to user '{}': {}", to, e.getMessage());
			}
		}
	}

	/**
	 * Author: Ravali
	 * @param requestId
	 * @param fileType
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws ParseException
	 */
	@GetMapping("/exportReport")
	@Timed
	public void exportToExcel(@RequestParam String requestId,@RequestParam String fileType, HttpServletRequest request, HttpServletResponse response) throws IOException, URISyntaxException, ParseException
	{ 
		log.info("Rest Request to Download reportJsonToCSV for requestId: "+requestId);
		HashMap finalMap=new HashMap();
		HashMap map1=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map1.get("tenantId").toString());
		ReportRequests reportRequests=reportRequestsRepository.findByTenantIdAndIdForDisplay(tenantId, requestId);
		Reports rep=reportsRepository.findOne(reportRequests.getReportId());
		String reportName=rep.getReportName();

		ApplicationPrograms prog=applicationProgramsRepository.findByPrgmNameAndTenantIdAndEnableIsTrue("Reporting",tenantId);
		String baseDir=env.getProperty("baseDirectories.linuxBaseDir");
		String localPath=baseDir+"/"+prog.getGeneratedPath();
		log.info("ProgramPath configured for Reports :"+localPath);
		LinkedHashMap cmpltOutput=reportsService.hdfsFileReading(reportRequests.getOutputPath());

		List<HashMap> header=(List<HashMap>) cmpltOutput.get("columns");
		List<HashMap> values=(List<HashMap>) cmpltOutput.get("data");

		List<String> columnsList=new ArrayList<String>();
		for(int i=0;i<header.size();i++)
		{
			columnsList.add(header.get(i).get("field").toString());
		}

		log.info("columnsList :"+columnsList);


		if(fileType.equalsIgnoreCase("excel")){
			response=fileExportService.exportToExcel2(response, columnsList, values);
		}
		else if(fileType.equalsIgnoreCase("csv"))
		{
			response.setContentType ("application/csv");
			fileExportService.jsonToCSVForReports(values,columnsList,response.getWriter());
		}

	}

	/**
	 * Author: Swetha
	 * Framing data for csv format
	 * @param headers
	 * @param map
	 * @return
	 */
	private String getCommaSeparatedRow(List<String> headers, Map map) {
		List<String> items = new ArrayList<String>();
		for (String header : headers) {
			String value = map.get(header) == null ? "" : map.get(header).toString().replace(",", "");
			items.add(value);
		}
		return StringUtils.join(items.toArray(), ",");
	}

}