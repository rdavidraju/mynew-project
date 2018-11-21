package com.nspl.app.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.PrivilegedAction;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.oozie.client.OozieClientException;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.DataFrameReader;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.RelationalGroupedDataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SparkSession;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.codahale.metrics.annotation.Timed;
import com.google.gson.Gson;
import com.nspl.app.config.ApplicationContextProvider;
import com.nspl.app.domain.ApplicationPrograms;
import com.nspl.app.domain.BucketDetails;
import com.nspl.app.domain.BucketList;
import com.nspl.app.domain.DataViews;
import com.nspl.app.domain.DataViewsColumns;
import com.nspl.app.domain.FileTemplateLines;
import com.nspl.app.domain.JobActions;
import com.nspl.app.domain.LookUpCode;
import com.nspl.app.domain.Notifications;
import com.nspl.app.domain.ReportDefination;
import com.nspl.app.domain.ReportParameters;
import com.nspl.app.domain.ReportRequests;
import com.nspl.app.domain.ReportType;
import com.nspl.app.domain.Reports;
import com.nspl.app.domain.SchedulerDetails;
import com.nspl.app.domain.TenantConfig;
import com.nspl.app.repository.ApplicationProgramsRepository;
import com.nspl.app.repository.BalanceTypeRepository;
import com.nspl.app.repository.BucketDetailsRepository;
import com.nspl.app.repository.BucketListRepository;
import com.nspl.app.repository.DataViewsColumnsRepository;
import com.nspl.app.repository.DataViewsRepository;
import com.nspl.app.repository.FileTemplateLinesRepository;
import com.nspl.app.repository.JobActionsRepository;
import com.nspl.app.repository.LookUpCodeRepository;
import com.nspl.app.repository.NotificationsRepository;
import com.nspl.app.repository.ReportDefinationRepository;
import com.nspl.app.repository.ReportParametersRepository;
import com.nspl.app.repository.ReportRequestsRepository;
import com.nspl.app.repository.ReportTypeRepository;
import com.nspl.app.repository.ReportsRepository;
import com.nspl.app.repository.SchedulerDetailsRepository;
import com.nspl.app.repository.TenantConfigRepository;
import com.nspl.app.security.IDORUtils;

@Service
@Transactional
public class ReportsService {

	private final Logger log = LoggerFactory.getLogger(ReportsService.class);
	
	@Inject
	ReportsRepository reportsRepository;
	
	@Inject
	DataViewsRepository dataViewsRepository;
	
	@Inject
	ReportParametersRepository reportParametersRepository;
	
	@Inject
	ReportDefinationRepository reportDefinationRepository;
	
	@Inject
	DataViewsColumnsRepository dataViewsColumnsRepository;
	
	@Inject
	FileTemplateLinesRepository fileTemplateLinesRepository;
	
	@Inject
    private Environment env;
	
	@Inject
	LookUpCodeRepository lookUpCodeRepository;
	
	@Inject
	ReportTypeRepository ReportTypeRepository;
	
	@Inject
	BucketListRepository bucketListRepository;
	
	@Inject
	BucketDetailsRepository bucketDetailsRepository;
	
	@Inject
	JobActionsRepository jobActionsRepository;
	
	@Inject
	BalanceTypeRepository balanceTypeRepository;
	
	
	 @Inject
	    ReportRequestsRepository reportRequestsRepository;
	 
	   @Inject
	    OozieService oozieService;
	   
	   @Inject
	    SchedulerDetailsRepository schedulerDetailsRepository;
	   
	   
	   @Inject
	    NotificationsRepository notificationsRepository;
	   
	   
	   @Inject
	   ApplicationProgramsRepository applicationProgramsRepository;
	   
	   
	   @Inject
	   UserJdbcService userJdbcService;
	   
	   
	   @Inject
	   TenantConfigRepository tenantConfigRepository;

	/**
	 * Author: Swetha
	 * Function to frame report view creation query
	 * @param tenantId
	 * @param reportId
	 * @return
	 */
	 public String frameReportViewCreationQuery(Long tenantId, Long reportId){
		 
		log.info("In ReportViewCreation service with tenantId: "+tenantId+" reportId: "+reportId);
		
		Reports reports=reportsRepository.findOne(reportId);
		
		String reportName=reports.getReportName();
		Long dataViewId=reports.getSourceViewId();
		
		DataViews dv=dataViewsRepository.findOne(dataViewId);
		
		String dataViewName=dv.getDataViewName().toLowerCase();
		
		//List<ReportParameters> repParamList=reportParametersRepository.findByReportIdAndRefSrcIdAndRefTypeid(reportId, dataViewId, "DATA_VIEW");
				
		List<ReportDefination> repDefList=reportDefinationRepository.findByReportIdAndRefTypeId(reportId,"DATA_VIEW");
		
		List<ReportDefination> repDefCondList=reportDefinationRepository.fetchReportConditions(reportId);
		
		String condQry="";
		for(int i=0;i<repDefCondList.size();i++){
			
			ReportDefination repDef=repDefCondList.get(i);
			String condSubQry="";
			Long colId=repDef.getRefColId();
			String condOper=repDef.getConditionalOperator();
			String condVal=repDef.getConditionalVal();
			
			DataViewsColumns dvc=dataViewsColumnsRepository.findOne(colId);
			String dvcDataType=dvc.getColDataType();
			String dvcColId=dvc.getRefDvColumn();
			String dvcSrcId=dvc.getRefDvName();
			
			FileTemplateLines ftl=fileTemplateLinesRepository.findOne(Long.parseLong(dvcColId));
			String colAlias=ftl.getColumnAlias();
			
			if(dvcDataType.equalsIgnoreCase("VARCHAR")){
				 if(condOper.equalsIgnoreCase("EQUALS") || condOper.equalsIgnoreCase("NOT EQUALS")){
					 condOper="=";
					 condSubQry=condSubQry+colAlias+condOper+"'"+condVal+"'";
				 }
				 if(condOper.equalsIgnoreCase("CONTAINS")){
					 condSubQry=condSubQry+colAlias+condOper+"'%"+condVal+"%'";
					 }
				 if(condOper.equalsIgnoreCase("BEGINS_WITH")){
					 condSubQry=condSubQry+colAlias+condOper+"'%"+condVal+"'";
					 }
				 if(condOper.equalsIgnoreCase("ENDS_WITH")){
					 condSubQry=condSubQry+colAlias+condOper+"'"+condVal+"%'";
					 }
			 }else if(dvcDataType.equalsIgnoreCase("INTEGER")){
				 //handle between
			 }else if(dvcDataType.equalsIgnoreCase("DATE")){
				//handle between
				 condSubQry=condSubQry+colAlias+condOper+"'"+condVal+"'";
			 }
			 else if(dvcDataType.equalsIgnoreCase("DATETIME")){
				 //handle between
				 condSubQry=condSubQry+colAlias+condOper+"'"+condVal+"'";
			 }else if(dvcDataType.equalsIgnoreCase("DECIMAL")){
				 //handle between
			 }
			
			log.info("condSubQry at i: "+i+" is: "+condSubQry);
			 condQry=condQry+condSubQry;
			 if(i>=0 && i<repDefCondList.size()-1 && i!=repDefCondList.size()){
				 condQry=condQry+",";
			 }
			
		}
		 
		 log.info("final condQry: "+condQry);
		 
		 String dvAliasName="v";
		 String viewColsQuery="";
		 
		 viewColsQuery="select "+dvAliasName+".scrIds as masterid, "+dvAliasName+".fileDate as fileDate, ";
		 
		 String subQry="";
		 for(int j=0;j<repDefList.size();j++){
			 
			 ReportDefination reportDef=repDefList.get(j);
			 String dpName=reportDef.getDisplayName();
			 Long  refColId=reportDef.getRefColId();
			 
			 DataViewsColumns dvcData=dataViewsColumnsRepository.findOne(refColId);
			 String dataType=dvcData.getColDataType();
			 String dvColId=dvcData.getRefDvColumn();
			 String dvSrcId=dvcData.getRefDvName();
			 
			 FileTemplateLines ftl=fileTemplateLinesRepository.findOne(Long.parseLong(dvColId));
			 String colAlias=ftl.getColumnAlias();
			 
			 subQry=dvAliasName+"."+colAlias+" as "+dpName;
			 
			 if(j>=0 && j<repDefList.size()-1 && j!=repDefList.size()){
				 subQry=subQry+" , ";
			 }
			 
			 viewColsQuery=viewColsQuery+subQry;
			 log.info("viewColsQuery at r: "+j+" is: "+viewColsQuery);
			 
		 }
		 
		 log.info("final viewColsQuery: "+viewColsQuery);
		 
		 String reportViewName="r_"+reportName+"_"+tenantId;
		 String viewQry="create or replace view "+reportViewName+" as ( ";
		 
		 String updQuery="";
		 String finalQry="";
		 
		 String query="`r`.`original_view_id` AS `viewid`, `r`.`reconciliation_rule_group_id` AS `rec_rule_group`, "
			        + " `r`.`reconciled_date` AS `reconciled_date`,  IF(`r`.`reconciliation_rule_group_id` is not null, 'reconciled', 'Un-Reconciled') AS `recon_status`, "
			       + " `a`.`status` AS `acc_status`, "
			       + " a.acct_group_id AS acc_rule_Group, jcr.credit_amount As entered_amt, jcr.accounted_credit As acc_cr_amt, "
			       +"jcr.code_combination As cr_code_combination, jcr.created_date As journal_date,"
			       +" jdr.debit_amount AS debit_amt,  jdr.accounted_debit AS acc_dr_amt, jdr.code_combination AS dr_code_combination,"
			      /* +"IF(`r`.`reconciliation_rule_group_id` is not null, 'reconciled', 'Un-Reconciled') AS `recon_status`, "*/
			       +" jdr.je_batch_id AS batch_id"
			       + " FROM "
			       + "(select * from  "
			       +dataViewName +") `v` "
			       +" LEFT OUTER JOIN ( SELECT * FROM `t_reconciliation_result` WHERE tenant_id="+tenantId+" AND original_view_id="+dataViewId+" ) `r`  on ( `r`.`original_row_id` = `v`.`scrIds` ) "
			      +"  LEFT OUTER JOIN (select distinct tenant_id,original_row_id,acct_group_id,acct_rule_id,original_view_id,ad.status "
			      +"  from t_accounting_data ad where status = 'Accounted' and original_view_id="+dataViewId+") `a` "
			     +"   on (`a`.`original_row_id` = `v`.`scrIds`)"
			      +" LEFT OUTER JOIN (select distinct row_id,credit_amount, accounted_credit "
			      +" , code_combination, created_date "
			      +" from t_je_lines where credit_amount is not null) jcr "
			       +" on (`v`.`scrIds` = `jcr`.`row_id`) "
			      +" LEFT OUTER JOIN (select distinct row_id,currency, debit_amount, accounted_debit, code_combination, je_batch_id "
			       +" from t_je_lines where debit_amount is not null) jdr on (`v`.`scrIds` = `jdr`.`row_id`)";
		 
		 updQuery=viewColsQuery+","+query;
			
		 log.info("updQuery: "+updQuery); 
		 
		 finalQry=viewQry+updQuery+" )";
		 
		return finalQry; 
			
	 }
	 
	 
	 /**
	  * Author: Swetha
	  * Function to create Report View
	  * @param reportId
	  * @param tenantId
	  * @throws ClassNotFoundException
	  * @throws SQLException
	  */
	 public void createReportView(@RequestParam Long reportId, @RequestParam Long tenantId) throws ClassNotFoundException, SQLException {	
		 
			/*String dbUrl=env.getProperty("spring.datasource.url");
			String[] parts=dbUrl.split("[\\s@&?$+-]+");
			String host = parts[0].split("/")[2].split(":")[0];
			log.info("host :"+host);
			String schemaName=parts[0].split("/")[3];
			String userName = env.getProperty("spring.datasource.username");
			String password = env.getProperty("spring.datasource.password");
			String jdbcDriver = env.getProperty("spring.datasource.jdbcdriver");*/
		   
		   Connection conn = null;
		   Statement stmt = null;
		   try{
		     /* Class.forName(jdbcDriver);
		      conn = DriverManager.getConnection(dbUrl, userName, password);*/
			   DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
    		   conn=ds.getConnection();
		      log.info("Connected database successfully...");
		      stmt = conn.createStatement();
		      
		      String viewQuery=frameReportViewCreationQuery(tenantId, reportId);
		      log.info("viewQuery in createDataView: "+viewQuery);
		      int out=stmt.executeUpdate(viewQuery);
		      log.info("query executed");
		   }catch(SQLException se){
			   log.info("se: "+se);
	      }
		   finally{
				stmt.close();
				conn.close();
			}}
	 
	 
	 /**
	  * Author: Swetha
	  * Generic service to prepare filtered data set for Report views generation
	  * @param tenantId
	  * @param reportId
	  * @param filtersMap
	  * @return
	  * @throws AnalysisException
	  * @throws IOException
	 * @throws ParseException 
	  */
	 public LinkedHashMap reportDataSetCreation(
				Long tenantId, Long reportId,
				HashMap filtersMap, SparkSession spark) throws AnalysisException,
				IOException, ParseException {
	    	

			log.info("in reportDataSetCreation with tenantId: "
					+ tenantId + " reportId: " + reportId+ " at: " + ZonedDateTime.now());
			log.info("filtersMap: " + filtersMap);
			
	    	LinkedHashMap finalMap=new LinkedHashMap();

	    	List<String> layoutColList=new ArrayList<String>();
	    	
	    	List<String> grpColList=new ArrayList<String>();

			/*JavaSparkContext sContext = new JavaSparkContext(spark.sparkContext());
			SQLContext sqlCont = new SQLContext(sContext);*/

			log.info("Spark Configuration end at: " + ZonedDateTime.now());
			
			HashMap<Long,ReportType> repTypesData=getReportTypes(tenantId);

			String dataViewName = "";

			String dbUrl = env.getProperty("spring.datasource.url");
			String[] parts = dbUrl.split("[\\s@&?$+-]+");
			String host = parts[0].split("/")[2].split(":")[0];
			String schemaName = parts[0].split("/")[3];
			String userName = env.getProperty("spring.datasource.username");
			String password = env.getProperty("spring.datasource.password");
			String jdbcDriver = env.getProperty("spring.datasource.jdbcdriver");

			Dataset<Row> reportsData = spark.read().format("jdbc")
					.option("url", dbUrl).option("user", userName)
					.option("password", password).option("dbtable", "t_reports")
					.load().where("id=" + reportId).select("source_view_id","report_val_01","report_type_id","report_val_02");

			// reportsData.show();

			Row report = reportsData.collectAsList().get(0);
			Long dataViewId = report.getLong(0);
			Long repTypeId=report.getLong(2);
			ReportType repType=repTypesData.get(repTypeId);
			String reportTypeName=repType.getType();
			int num=0;
			Long bucketId= 0L;
			
			if(reportTypeName.equalsIgnoreCase("AGING_REPORT")){
	        if(report.getString(1)!=null && !(report.getString(1).isEmpty())){
	            bucketId= Long.parseLong(report.getString(1));
	        }
			}
			
			if(reportTypeName.equalsIgnoreCase("ROLL_BACK_REPORT")){
			num=Integer.parseInt(report.getString(3));
			}

			Dataset<Row> datViewData = spark.read().format("jdbc")
					.option("url", dbUrl).option("user", userName)
					.option("password", password).option("dbtable", "t_data_views")
					.load().where("id=" + dataViewId).select("data_view_name");

			Row dv = datViewData.collectAsList().get(0);

			String dvatViewName = dv.getString(0);
			dvatViewName = dvatViewName.toLowerCase();

			log.info("dvatViewName: " + dvatViewName);

			String dvFilterStr = "";
			/*String recFilterStr = "tenant_id=" + tenantId
					+ " and original_view_id=" + dataViewId;*/
			/*String accFilterStr = "tenant_id=" + tenantId
					+ " and original_view_id=" + dataViewId;*/
			String jrnlFilterStr = "";
			//String jrnlHeaderFilterStr = "tenant_id=" + tenantId;

			Dataset<Row> repData = spark
					.read()
					.format("jdbc")
					.option("url", dbUrl)
					.option("user", userName)
					.option("password", password)
					.option("dbtable", "t_report_parameters")
					.load()
					.where("report_id = " + reportId + " and ref_src_id="
							+ dataViewId + " and ref_typeid='DATA_VIEW'");

			List<Row> repParamsList = repData.collectAsList();

			Dataset<Row> recon_data_ds = spark.read().format("jdbc")
					.option("url", dbUrl).option("user", userName)
					.option("password", password)
					.option("dbtable", "t_reconciliation_result").load().where("tenant_id=" + tenantId
							+ " and original_view_id=" + dataViewId);

			//Dataset<Row> recon_data_ds = recon_data.load();

			log.info("recon_data_ds.count(): " + recon_data_ds.count());

			//recon_data_ds = recon_data_ds.filter(recFilterStr);
			recon_data_ds.createTempView("recon_data_ds_v");

			recon_data_ds.registerTempTable("recon_data_ds_v");

			log.info("recon_data_ds_v.count(): " + recon_data_ds.count());

			/* Report Conditions */

			Dataset<Row> repDefConddata = spark
					.read()
					.format("jdbc")
					.option("url", dbUrl)
					.option("user", userName)
					.option("password", password)
					.option("dbtable", "t_report_defination")
					.load()
					.where("report_id="
							+ reportId
							+ " and ref_type_id='DATA_VIEW' and conditional_operator is not null and conditional_val is not null")
					.select("ref_col_id", "conditional_operator", "conditional_val");

			// repDefConddata.show();
			List<Row> dvCondList = repDefConddata.collectAsList();

			String condQry = "";

			for (int k = 0; k < dvCondList.size(); k++) {

				Row dvCond = dvCondList.get(k);

				String conSubQry = "";

				Long refColId = dvCond.getLong(0);
				String condOperOrg = dvCond.getString(1);
				String condOp = "";
				String condVal = dvCond.getString(2);

				Dataset<Row> dataViewColdata = spark
						.read()
						.format("jdbc")
						.option("url", dbUrl)
						.option("user", userName)
						.option("password", password)
						.option("dbtable", "t_data_views_columns")
						.load()
						.where("id=" + refColId)
						.select("col_data_type", "column_name", "ref_dv_column",
								"ref_dv_name");

				List<Row> dvcoldataList = dataViewColdata.collectAsList();

				Row dvcolData = dvcoldataList.get(0);
				String colDtType = dvcolData.getString(0);
				// String dvColDispName=dvcolData.getString(1);
				String dvColId = dvcolData.getString(2); // ftl id
				String refDvId = dvcolData.getString(3); // ft id

				Dataset<Row> ftlDataList = spark.read().format("jdbc")
						.option("url", dbUrl).option("user", userName)
						.option("password", password)
						.option("dbtable", "t_file_template_lines").load()
						.where("id=" + dvColId).select("column_alias");

				List<Row> ftlDataValList = ftlDataList.collectAsList();
				Row ftlData = ftlDataValList.get(0);
				String colAlias = ftlData.getString(0);

				if (colDtType.equalsIgnoreCase("VARCHAR")) {
					if (condOperOrg.equalsIgnoreCase("EQUALS")
							|| condOperOrg.equalsIgnoreCase("NOT EQUALS")) {
						condOp = "=";
						conSubQry = conSubQry + colAlias + condOp + "'" + condVal
								+ "'";
					}
					if (condOperOrg.equalsIgnoreCase("CONTAINS")) {
						condOp = "like";
						conSubQry = conSubQry + colAlias + condOp + "'%" + condVal
								+ "%'";
					}
					if (condOperOrg.equalsIgnoreCase("BEGINS_WITH")) {
						condOp = "like";
						conSubQry = conSubQry + colAlias + condOp + "'%" + condVal
								+ "'";
					}
					if (condOperOrg.equalsIgnoreCase("ENDS_WITH")) {
						condOp = "like";
						conSubQry = conSubQry + colAlias + condOp + "'" + condVal
								+ "%'";
					}
				} else if (colDtType.equalsIgnoreCase("INTEGER")) {
					conSubQry = conSubQry + colAlias + condOperOrg + condVal;
					// handle between
				} else if (colDtType.equalsIgnoreCase("DATE")) {
					// handle between
					conSubQry = conSubQry + colAlias + condOperOrg + "'" + condVal
							+ "'";
				} else if (colDtType.equalsIgnoreCase("DATETIME")) {
					// handle between
					conSubQry = conSubQry + colAlias + condOperOrg + "'" + condVal
							+ "'";
				} else if (colDtType.equalsIgnoreCase("DECIMAL")) {
					// handle between
					conSubQry = conSubQry + colAlias + condOperOrg + condVal;
				}

				log.info("conSubQry at k: " + k + " is: " + conSubQry);
				condQry = condQry + conSubQry;
				if (k >= 0 && k < dvCondList.size() - 1 && k != dvCondList.size()) {
					condQry = condQry + ",";
				}

			}

			log.info("final condQry: " + condQry);
			dvFilterStr = condQry;
			
			DataFrameReader dv_data = spark.read().format("jdbc")
					.option("url", dbUrl).option("user", userName)
					.option("password", password).option("dbtable", dvatViewName);

			Dataset<Row> dv_data_ds_load = dv_data.load();
			//log.info("dv_data_ds.count(): " + dv_data_ds_load.count());
			
			
			log.info("previewing cmplt data view data with count : "+dv_data_ds_load.count());
	       // dv_data_ds_load.show();
	        Dataset<Row> dv_data_ds = null;
			
			/* Finding Date Limits to Filter data view data for Roll back report */
	        String dateTymVal=DateTime.now().toString();
	        if(filtersMap!=null && filtersMap.containsKey("dateTimeVal")){
	            dateTymVal=filtersMap.get("dateTimeVal").toString();
	        }
	        
	        String dtQualAlias="";
	        
	        if(reportTypeName.equalsIgnoreCase("ROLL_BACK_REPORT")){
	        //Formatting it to date format
	                String time=dateTymVal;
	                DateFormat inputFormat = new SimpleDateFormat(
	                        "E MMM dd yyyy HH:mm:ss 'GMT'z", Locale.getDefault());
	                Date date = inputFormat.parse(time);
	                log.info("date: "+date);
	                SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
	                String dateInString=formatter.format(date);
	                log.info("Formatted Date: "+dateInString);
	                
	                DateTimeFormatter localDateFormatter =
	                        DateTimeFormatter.ofPattern("dd-MMM-yyyy");
	                LocalDate startDate=LocalDate.parse(dateInString, localDateFormatter);
	                log.info("startDate: "+startDate);
	                
	                LocalDate dt1=startDate.plusMonths(num);
	                log.info("dt1: "+dt1);
	                LocalDate localDate = LocalDate.parse(dt1.toString(),DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	                LocalDate lastDayOfMonth = localDate.with(TemporalAdjusters.lastDayOfMonth());
	                log.info("lastDayOfMonth: "+lastDayOfMonth);
	        //}

	        /* Date Qualifier at Report Definition */
	        
	        ReportDefination dateQualDef=reportDefinationRepository.findByReportIdAndDataType(reportId, "DATE_QUALIFIER");
	        log.info("dateQualDef: "+dateQualDef);
	        
	        //Dataset<Row> dv_data_ds = null;
	        //String dtQualAlias="";
	        String dateQualColName="";
	        String dateDiffQry=""; //dtQualAlias+" between '"+startDate+"' and '"+lastDayOfMonth+"'"
	        
	        log.info("dateDiffQry: "+dateDiffQry);
	        
	        if(dateQualDef!=null && dateQualDef.getRefTypeId()!=null && dateQualDef.getRefTypeId().equalsIgnoreCase("DATA_VIEW")){
	            dateQualColName=dateQualDef.getDisplayName();
	            Long dateQualId=dateQualDef.getRefColId();
	            DataViewsColumns dvc=dataViewsColumnsRepository.findOne(dateQualId);
	            Long ftlId=Long.parseLong(dvc.getRefDvColumn());
	            FileTemplateLines ftl=fileTemplateLinesRepository.findOne(ftlId);
	            dtQualAlias=ftl.getColumnAlias();
	            
	            long daysBetween = ChronoUnit.DAYS.between(startDate, lastDayOfMonth);
	            log.info("daysBetween: "+daysBetween);
	            
	            if(daysBetween>0){
	                log.info("startdate is greater than end date");
	                dateDiffQry=dtQualAlias+" between '"+startDate+"' and '"+lastDayOfMonth+"'";
	            }else if(daysBetween<0){
	                log.info("enddate is greater than startdate");
	                dateDiffQry=dtQualAlias+" between '"+lastDayOfMonth+"' and '"+startDate+"'";
	            }
	            
	            dv_data_ds=dv_data_ds_load.where(dateDiffQry);
	            
	            log.info("previewing data view data aftr filtering based on date qualifier with count: "+dv_data_ds.count());
	            //dv_data_ds.show();
	        }

			log.info("dvFilterStr: " + dvFilterStr);
			if (dvFilterStr != null && !(dvFilterStr.isEmpty())) {
				dv_data_ds = dv_data_ds.filter(dvFilterStr);
			}
	 }
	        
	        else{
	        	dv_data_ds=dv_data_ds_load;
	        }
			//dv_data_ds.createTempView("dv_data_ds_v");

			dv_data_ds.registerTempTable("dv_data_ds_v");

			log.info("dv_data_ds.count(): " + dv_data_ds.count());

			Dataset<Row> acc_data_ds = spark.read().format("jdbc")
                    .option("url", dbUrl).option("user", userName)
                    .option("password", password)
                    .option("dbtable", "t_accounting_data").load().where("tenant_id=" + tenantId+ " and original_view_id=" + dataViewId);
 
            //Dataset<Row> acc_data_ds = acc_data.load();
            //log.info("acc_data_ds.count(): " + acc_data_ds.count());
 
            //acc_data_ds = acc_data_ds.filter(accFilterStr);
            acc_data_ds.createTempView("acc_data_ds_v");
 
            acc_data_ds.registerTempTable("acc_data_ds_v");
 
            log.info("acc_data_ds_v.count(): " + acc_data_ds.count());
 
            Dataset<Row> jrnlHeader_data_ds = spark.read().format("jdbc")
                    .option("url", dbUrl).option("user", userName)
                    .option("password", password)
                    .option("dbtable", "t_journals_header_data").load().where("tenant_id=" + tenantId);
 
            //Dataset<Row> jrnlHeader_data_ds = jrnlHeader_data.load();
 
            /*if (jrnlHeaderFilterStr != null && !(jrnlHeaderFilterStr.isEmpty())) {
                jrnlHeader_data_ds = jrnlHeader_data_ds.filter(jrnlHeaderFilterStr);
            }*/
            jrnlHeader_data_ds.createTempView("jrnlHeader_data_ds_v");
 
            jrnlHeader_data_ds.registerTempTable("jrnlHeader_data_ds_v");
 
            //log.info("jrnlHeader_data_ds_v.count(): " + jrnlHeader_data_ds.count());

			DataFrameReader jrnl_data = spark.read().format("jdbc")
					.option("url", dbUrl).option("user", userName)
					.option("password", password).option("dbtable", "t_je_lines");

			Dataset<Row> jrnl_data_ds = jrnl_data.load();

			if (jrnlFilterStr != null && !(jrnlFilterStr.isEmpty())) {
				jrnl_data_ds = jrnl_data_ds.filter(jrnlFilterStr);
			}
			jrnl_data_ds.createTempView("jrnl_data_ds_v");

			jrnl_data_ds.registerTempTable("jrnl_data_ds_v");

			log.info("jrnl_data_ds_v.count(): " + jrnl_data_ds.count());

			String dvAliasName = "v";
			String viewColsQuery = "";

			 //String qualifierQry="";
		        //String qualAlias="";
		        String aggr="";
		        String aggregator="";
		        String buckAgg="";
		        String aggQulaCol="";
		        
		        DataViewsColumns dvcqulaData=dataViewsColumnsRepository.findByDataViewIdAndQualifier(dataViewId,"AMOUNT");
		        String amoutQualifierCol=dvcqulaData.getRefDvColumn();
		        DataViewsColumns dvcQualData=dataViewsColumnsRepository.findByDataViewIdAndRefDvColumn(dataViewId, amoutQualifierCol);
		        Long ftlId=Long.parseLong(dvcQualData.getRefDvColumn());
		        String dvcDpName=dvcQualData.getColumnName();
		        FileTemplateLines ftl=fileTemplateLinesRepository.findOne(ftlId);
		        String qulaCol=ftl.getColumnAlias();
		        String aggRefType="";
		        
		        /*ReportDefination amtQualDef=reportDefinationRepository.findByReportIdAndRefTypeIdAndRefSrcIdAndRefColId(reportId, "DATA_VIEW", dataViewId, Long.parseLong(amoutQualifierCol));
		        if(amtQualDef!=null && amtQualDef.getLayoutVal()!=null){
		            //As the amount qualifier exists in layout data, consider its respective alias name
		            if(amtQualDef.getDisplayName()!=null){
		                qualAlias=amtQualDef.getDisplayName();
		                aggr=qualAlias;
		            }
		        }
		        else{
		            //Amount qualifier column doesnt exists in layout columns
		            qualAlias=dvcDpName;
		            aggr=aggregator;
		        }
		        qualAlias=qualAlias+"_amtQual";
		        qualifierQry=dvAliasName+"."+qulaCol+" as "+qualAlias;
		        log.info("qualifierQry: "+qualifierQry);*/
		        
		        if(reportTypeName.equalsIgnoreCase("AGING_REPORT") || reportTypeName.equalsIgnoreCase("ROLL_BACK_REPORT") ){
		        ReportDefination aggrgatorDef=reportDefinationRepository.findByReportIdAndDataType(reportId, "AGGREGATOR");
		        log.info("aggrgatorDef:"+aggrgatorDef+"@@@@@");
		        if(aggrgatorDef!=null && !(aggrgatorDef.equals("")))
		        aggregator=aggrgatorDef.getDisplayName();
		        log.info("aggregator: "+aggregator);
		        aggRefType=aggrgatorDef.getRefTypeId();
		        
		        if(aggregator!=null && !(aggregator.isEmpty()) && !(aggregator.equalsIgnoreCase(""))){
		            Long aggColId=aggrgatorDef.getRefColId();
		            String srcType=aggrgatorDef.getRefTypeId();
		            if(srcType.equalsIgnoreCase("DATA_VIEW")){
		            Long aggSrcId=aggrgatorDef.getRefSrcId();
		            DataViewsColumns aggreDvcQualData=dataViewsColumnsRepository.findByDataViewIdAndRefDvColumn(aggSrcId, aggColId.toString());
		            Long aggrFtlId=Long.parseLong(dvcQualData.getRefDvColumn());
		            String aggrDvcDpName=dvcQualData.getColumnName();
		            FileTemplateLines aggreFtl=fileTemplateLinesRepository.findOne(aggrFtlId);
		            aggQulaCol=ftl.getColumnAlias();
		           // buckAgg=aggQulaCol; 
		            }
		        }
		        
		        /*if(aggregator!=null && !(aggregator.isEmpty()) && !(aggregator.equalsIgnoreCase(""))){
		            Long aggColId=aggrgatorDef.getRefColId();
		            String srcType=aggrgatorDef.getRefTypeId();
		            if(srcType.equalsIgnoreCase("DATA_VIEW")){
		            Long aggSrcId=aggrgatorDef.getRefSrcId();
		            DataViewsColumns aggreDvcQualData=dataViewsColumnsRepository.findByDataViewIdAndRefDvColumn(aggSrcId, aggColId.toString());
		            Long aggrFtlId=Long.parseLong(dvcQualData.getRefDvColumn());
		            String aggrDvcDpName=dvcQualData.getColumnName();
		            FileTemplateLines aggreFtl=fileTemplateLinesRepository.findOne(aggrFtlId);
		            String aggQulaCol=ftl.getColumnAlias();
		            buckAgg=aggQulaCol; 
		            }
		            else if(srcType.equalsIgnoreCase("FIN_FUNCTION")){
		                LookUpCode lookupCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("COLUMN_REF_TYPE", srcType, tenantId);
		                Long refSrcId=lookupCode.getId();
		                buckAgg=aggrgatorDef.getDisplayName();
		            }
		        }
		        else buckAgg=qulaCol; */
		        }
		        buckAgg=aggregator;
		        log.info("buckAgg: "+buckAgg);
		        log.info("aggQulaCol: "+aggQulaCol);
		        
		        /*viewColsQuery = "select " + dvAliasName + ".scrIds as masterid, "+qualifierQry+" , "
		                + dvAliasName + ".fileDate as fileDate, ";*/
		        
		       /* if(aggRefType.equalsIgnoreCase("DATA_VIEW")){
		        	viewColsQuery = "select " + dvAliasName + ".scrIds as masterid , "
		        			+dvAliasName+" ."+aggQulaCol+" as aggregator"
			                + dvAliasName + ".fileDate as fileDate, ";
		        }
		        else{*/
		        	viewColsQuery = "select " + dvAliasName + ".scrIds as masterid , "
		                + dvAliasName + ".fileDate as fileDate, ";
		       // }
		        
		        log.info("viewColsQuery with qualifierQry: "+viewColsQuery);

			List<String> groupByColList=new ArrayList<String>();
			
			Dataset<Row> repDefdata = spark
					.read()
					.format("jdbc")
					.option("url", dbUrl)
					.option("user", userName)
					.option("password", password)
					.option("dbtable", "t_report_defination")
					.load()
					.where("report_id=" + reportId + " and ref_type_id='DATA_VIEW' and layout_val is not null ")
					.select("display_name", "ref_col_id","goup_by","layout_val").orderBy("layout_val");

			List<Row> repDefColList = repDefdata.collectAsList();
			String subQry = "";
			for (int r = 0; r < repDefColList.size(); r++) {
				Row refDef = repDefColList.get(r);
				String dispName = refDef.getString(0);
				log.info("dispName: "+dispName);
				/*String sqlDpName="";
				sqlDpName=reportsService.getAliasNameForQry(dispName,reportId);*/
				Long refColId = refDef.getLong(1);
				//Boolean grpBy=false;
				//if(refDef.get(2)!=null)
				//grpBy=refDef.getBoolean(2);

				Dataset<Row> dataViewColdata = spark
						.read()
						.format("jdbc")
						.option("url", dbUrl)
						.option("user", userName)
						.option("password", password)
						.option("dbtable", "t_data_views_columns")
						.load()
						.where("id=" + refColId)
						.select("col_data_type", "column_name", "ref_dv_column",
								"ref_dv_name");

				List<Row> dvcoldataList = dataViewColdata.collectAsList();

				Row dvcolData = dvcoldataList.get(0);
				String colDtType = dvcolData.getString(0);
				String dvColId = dvcolData.getString(2);
				String refDvId = dvcolData.getString(3);

				Dataset<Row> ftlDataList = spark.read().format("jdbc")
						.option("url", dbUrl).option("user", userName)
						.option("password", password)
						.option("dbtable", "t_file_template_lines").load()
						.where("id=" + dvColId).select("column_alias");

				Row ftlData = ftlDataList.collectAsList().get(0);
				String colAlias = ftlData.getString(0);
				 if(!(colDtType.equalsIgnoreCase("DECIMAL"))){
					 grpColList.add(dispName);
				 }
				layoutColList.add(dispName);
				//log.info("grpColList: "+grpColList);
				//log.info("layoutColList: "+layoutColList);
				/*if(grpBy!=null && grpBy==true){
					groupByColList.add(dispName);
				}*/
				subQry = dvAliasName + "." + colAlias + " as " +"`"+ dispName+"`";

				if (r >= 0 && r < repDefColList.size() - 1
						&& r != repDefColList.size()) {
					subQry = subQry + " , ";
				}
				viewColsQuery = viewColsQuery + subQry;
				//log.info("viewColsQuery at r: " + r + " is: " + viewColsQuery);
			}

			log.info("viewColsQuery after repor defination columns: "
					+ viewColsQuery);

			Dataset<Row> repParamdata = spark.read().format("jdbc")
					.option("url", dbUrl).option("user", userName)
					.option("password", password)
					.option("dbtable", "t_report_parameters").load()
					.where("report_id=" + reportId + " and ref_typeid='DATA_VIEW'")
					.select("display_name", "ref_col_id");

			List<Row> repParamColList = repParamdata.collectAsList();

			if (viewColsQuery != null && !(viewColsQuery.endsWith("fileDate, "))) {
				log.info("in if for existance of report def");
				viewColsQuery = viewColsQuery + " , ";
			}

			for (int r = 0; r < repParamColList.size(); r++) {
				Row refParam = repParamColList.get(r);
				String repParamQry = "";
				String dispName = refParam.getString(0);
				Long refColId = refParam.getLong(1);

				ReportDefination repDefData = reportDefinationRepository
						.findByReportIdAndRefTypeIdAndRefColId(reportId,
								"DATA_VIEW", refColId);

				if (repDefData != null) {
					//log.info("Report Defination already exists for refColId: "+refColId);
				} else {
					//log.info("REport definaition doesnt exist for refColId: "+refColId);
					Dataset<Row> dataViewColdata = spark
							.read()
							.format("jdbc")
							.option("url", dbUrl)
							.option("user", userName)
							.option("password", password)
							.option("dbtable", "t_data_views_columns")
							.load()
							.where("id=" + refColId)
							.select("col_data_type", "column_name",
									"ref_dv_column", "ref_dv_name");

					List<Row> dvcoldataList = dataViewColdata.collectAsList();

					Row dvcolData = dvcoldataList.get(0);
					String colDtType = dvcolData.getString(0);
					String dvColId = dvcolData.getString(2); // ftl id
					String refDvId = dvcolData.getString(3); // ft id

					Dataset<Row> ftlDataList = spark.read().format("jdbc")
							.option("url", dbUrl).option("user", userName)
							.option("password", password)
							.option("dbtable", "t_file_template_lines").load()
							.where("id=" + dvColId).select("column_alias");

					Row ftlData = ftlDataList.collectAsList().get(0);
					String colAlias = ftlData.getString(0);
					//layoutColList.add(dispName);
					repParamQry = dvAliasName + "." + colAlias + " as " + "`"+dispName+"`";

					log.info("repParamQry b4 at r: "+r+" is: "+repParamQry);
					if (r >= 0 && r < repParamColList.size() - 1
							&& r != repParamColList.size()-1) {
						repParamQry = repParamQry + " , ";
					}
					log.info("repParamQry aftr at r: "+r+" is: "+repParamQry);
					log.info("viewColsQuery b4 at r: "+r+" is: "+viewColsQuery);
					viewColsQuery = viewColsQuery + repParamQry;
					log.info("viewColsQuery aftr at r: " + r + " is: " + viewColsQuery);
				}
			}

			log.info("viewColsQuery after repor param columns: " + viewColsQuery);
			
			 if(viewColsQuery.endsWith(" , ")){
		            log.info("removing comma");
		            int commaIndex=viewColsQuery.lastIndexOf(",");
		            viewColsQuery=viewColsQuery.substring(0,commaIndex-1);
		        }

			log.info("final viewColsQuery: " + viewColsQuery);

			String updQuery = "";
			String sysColQry = "";
			String tabAlias = "";
			String tabName = "";
			String origColName = "";

			List<ReportDefination> repDefQuryDataList = reportDefinationRepository
					.findByReportIdAndRefTypeIdOrderByLayoutVal(reportId, "FIN_FUNCTION");
			for (int h = 0; h < repDefQuryDataList.size(); h++) {
				String sysColSubQry = "";
				ReportDefination repDef = repDefQuryDataList.get(h);
				Long refColId = repDef.getRefColId();
				String dpName = repDef.getDisplayName();
				log.info("dpName: "+dpName);
				//Boolean grpByCol=repDef.isGoupBy();
				String dataType=repDef.getDataType();

				LookUpCode lCode = lookUpCodeRepository.findOne(refColId);
				String funName = lCode.getSecureAttribute1();
				LookUpCode funCode = lookUpCodeRepository
						.findByLookUpTypeAndLookUpCodeAndTenantId("FIN_FUNCTION",
								funName, tenantId);
				if(funCode.getDescription()!=null && !(funCode.getDescription().isEmpty())){
					log.info("funCode.getDescription(): "+funCode.getDescription());
					if(funCode.getDescription().equalsIgnoreCase("TABLE")){
				tabName = funCode.getSecureAttribute1();
				origColName = funCode.getSecureAttribute2();

				if (tabName.equalsIgnoreCase("t_accounting_data"))
					tabAlias = "a";
				else if (tabName.equalsIgnoreCase("t_journals_header_data"))
					tabAlias = "jhd";
				else if (tabName.equalsIgnoreCase("t_je_lines")
						&& origColName.contains("credit"))
					tabAlias = "jcr";
				else if (tabName.equalsIgnoreCase("t_je_lines")
						&& origColName.contains("debit"))
					tabAlias = "jdr";
				else if (tabName.equalsIgnoreCase("t_reconciliation_result"))
					tabAlias = "r";
				if(!(dataType.equalsIgnoreCase("DECIMAL"))){
					grpColList.add(dpName);
				}
				//}
				layoutColList.add(dpName);
				//log.info("grpColList: "+grpColList);
				//log.info("layoutColList: "+layoutColList);
				/*if(grpByCol!=null && grpByCol==true){
					groupByColList.add(dpName);
				}*/
				sysColSubQry = tabAlias + "." + origColName + " as " + "`"+dpName+"`";

				sysColQry = sysColQry + sysColSubQry;

				if (h >= 0 && h != repDefQuryDataList.size() - 1) {
					sysColQry = sysColQry + " , ";
				}
			//}
				}
				else if(funCode.getDescription().equalsIgnoreCase("FUNCTION")){
				
					if(funCode.getMeaning()!=null && !(funCode.getMeaning().isEmpty())){
						if(funCode.getLookUpCode().equalsIgnoreCase("getReconStatus")){
							grpColList.add("as_of_recon_status");
							layoutColList.add("as_of_recon_status");
						}
						if(funCode.getLookUpCode().equalsIgnoreCase("getAccountingStatus")){
							grpColList.add("as_of_acc_status");
							layoutColList.add("as_of_acc_status");
						}
					}
				}
				}
			}

			log.info("layoutColList aftr repDef: "+layoutColList);
			log.info("sysColQry aftr repDef loop: "+sysColQry);
			List<ReportParameters> repParamQuryDataList = reportParametersRepository
					.findByReportIdAndRefTypeid(reportId, "FIN_FUNCTION");
			for (int h = 0; h < repParamQuryDataList.size(); h++) {
				String sysColSubQry = "";
				ReportParameters repPrm = repParamQuryDataList.get(h);
				Long refColId = repPrm.getRefColId();
				String dpName = repPrm.getDisplayName();

				ReportDefination repDefData = reportDefinationRepository
						.findByReportIdAndRefTypeIdAndRefColId(reportId,
								"FIN_FUNCTION", refColId);

				if (repDefData == null) {

					if (h == 0 && sysColQry != null && !(sysColQry.isEmpty()))
						sysColQry = sysColQry + " , ";

					LookUpCode lCode = lookUpCodeRepository.findOne(refColId);
					String funName = lCode.getSecureAttribute1();
					LookUpCode funCode = lookUpCodeRepository
							.findByLookUpTypeAndLookUpCodeAndTenantId(
									"FIN_FUNCTION", funName, tenantId);
					tabName = funCode.getSecureAttribute1();
					origColName = funCode.getSecureAttribute2();

					if (tabName.equalsIgnoreCase("t_accounting_data"))
						tabAlias = "a";
					else if (tabName.equalsIgnoreCase("t_journals_header_data"))
						tabAlias = "jhd";
					else if (tabName.equalsIgnoreCase("t_je_lines")
							&& origColName.contains("credit"))
						tabAlias = "jcr";
					else if (tabName.equalsIgnoreCase("t_je_lines")
							&& origColName.contains("debit"))
						tabAlias = "jdr";
					else if (tabName.equalsIgnoreCase("t_reconciliation_result"))
						tabAlias = "r";
					//layoutColList.add(dpName);
					sysColSubQry = tabAlias + "." + origColName + " as " + "`"+dpName+"`";

					sysColQry = sysColQry + sysColSubQry;

					if (h >= 0 && h != repDefQuryDataList.size() - 1) {
						sysColQry = sysColQry + " , ";
					}
				}
			}

			log.info("final sysColQry: " + sysColQry);
			log.info("grpColList: "+grpColList);
			log.info("grpColList: "+grpColList.size());
			
			String grpByColQry="";
			if(groupByColList!=null && !(groupByColList.isEmpty())){
				//grpByColQry=groupByColList.toString();
				grpByColQry = groupByColList.stream().map(Object::toString)
	                    .collect(Collectors.joining(", "));
			}
			
			log.info("grpByColQry: "+grpByColQry);
			
			/*String dateTymVal=DateTime.now().toString();
			if(filtersMap!=null && filtersMap.containsKey("dateTymVal")){
				dateTymVal= filtersMap.get("dateTymVal").toString();
			}*/
			
			 Column[] grpColArr = new Column[grpColList.size()];
			for(int h=0;h<grpColList.size();h++){
				String grpCol=grpColList.get(h);
				Column colarryVal=new Column(grpCol);
				grpColArr[h]=colarryVal;
			}
			
			
			 /* Applying Buckets */
			String bucketsQry="";
			Boolean othersFlag=true; 
			if(reportTypeName.equalsIgnoreCase("AGING_REPORT")){
	        String bucketType="";
	        if(bucketId!=null && bucketId>0){
	            BucketList bucketListData=bucketListRepository.findOne(bucketId);
	            bucketType=bucketListData.getType();
	            List<BucketDetails> bucketDetailsList=bucketDetailsRepository.findByBucketId(bucketId);
	            if(bucketDetailsList!=null){
	                bucketsQry=" ";
	            }
	            for(int j=0;j<bucketDetailsList.size();j++){
	                int buckVal=j+1;
	                BucketDetails bucketDet=bucketDetailsList.get(j);
	                Long bucketDetId =bucketDet.getId();
	                if(bucketDet!=null)
	                {
	                    Integer frmVal=0;
	                    Integer toVal=0;
	                    Integer from=0;
	                    Integer to=0;
	                    from=bucketDet.getFromValue();
	                    to=bucketDet.getToValue();
	                    log.info("from: "+from+" to: "+to);
	                    String bucketName=getBucketName(reportId, "AGING", bucketId, bucketDetId);
	                    if(from!=null && to!=null){
	                    if(from>to){
	                        
	                        frmVal=to;
	                        toVal=from;
	                    }
	                    else{
	                        frmVal=from;
	                        toVal=to;
	                    }
	                    
	                    
	                    if(bucketType.equalsIgnoreCase("AGE")){
	                    bucketsQry=bucketsQry+" when  DATEDIFF( '"+dateTymVal+"' ,`v`.`fileDate`) between "+frmVal+" and "+toVal+" then '"+bucketName+"'";
	                    }
	                    else if(bucketType.equalsIgnoreCase("VALUE")){
	                        bucketsQry=bucketsQry+" when "+aggQulaCol+" between "+frmVal+" and "+toVal+" then '"+bucketName+"'";
	                    }
	                    
	                }
	                    else{
	                    	othersFlag=false;
	                    	log.info("any one of the bucket limits are null");
	                    	//othersFlag=true;
	                    	if(bucketType.equalsIgnoreCase("AGE")){
	                    		if(to==null){
	                    			bucketsQry=bucketsQry+" when  DATEDIFF( '"+dateTymVal+"' ,`v`.`fileDate`) > "+from+" then '"+bucketName+"'";
	                    		}
	                    		if(from==null){
	                    			bucketsQry=bucketsQry+" when  DATEDIFF( '"+dateTymVal+"' ,`v`.`fileDate`) < "+to+" then '"+bucketName+"'";
	                    		}
	                    	}
	                    	if(bucketType.equalsIgnoreCase("VALUE")){
	                    	if(to==null){
	                    		bucketsQry=bucketsQry+" when "+aggQulaCol+"> "+from+" then '"+bucketName+"'";
	                    	}
	                    	if(from==null){
	                    		bucketsQry=bucketsQry+" when "+aggQulaCol+"< "+to+" then '"+bucketName+"'";
	                    	}
	                    	}
	                    }
	                }
	            }
	            log.info("bucket query b4 others : "+bucketsQry);
	            bucketsQry=bucketsQry+" else 'Others' end as bucket";
	        }
	        
	        if(bucketType.equalsIgnoreCase("AGE")){
	        bucketsQry="case when  DATEDIFF( '"+dateTymVal+"', `v`.`fileDate`) is null then 'Others' "+bucketsQry;
	        }
	        else if(bucketType.equalsIgnoreCase("VALUE")){
	            bucketsQry="case when "+aggQulaCol+" is null then 'Others' "+bucketsQry;
	        }
	        log.info("final bucket query: "+bucketsQry);
			}
			else if(reportTypeName.equalsIgnoreCase("ROLL_BACK_REPORT")){
				
				/*bucketsQry=" MONTH( `v`.`"+dtQualAlias+"`) as dateQualMnth,"
			                +"YEAR( `v`.`"+dtQualAlias+"`) as dateQualYear,"
			                +"concat (  case when month("+dtQualAlias+") = '01' then 'JAN' "
			                +" when  month("+dtQualAlias+") = '02' then 'FEB' "
			                +" when  month("+dtQualAlias+") = '03' then 'MAR' "
			                +" when  month("+dtQualAlias+") = '04' then 'APR' "
			                +" when  month("+dtQualAlias+") = '05' then 'MAY' "
			                +" when  month("+dtQualAlias+") = '06' then 'JUN'"
			                +" when  month("+dtQualAlias+") = '07' then 'JUL'"
			                +" when  month("+dtQualAlias+")= '08' then 'AUG'"
			                +" when  month("+dtQualAlias+") = '09' then 'SEP'"
			                +" when month("+dtQualAlias+") = '10' then 'OCT'"
			                +" when  month("+dtQualAlias+") = '11' then 'NOV'"
			                +" when  month("+dtQualAlias+") = '12' then 'DEC'" 
			                +" END,"+"\""+"-"+"\", year("+dtQualAlias+")) AS recon_month";*/
				
				bucketsQry=" MONTH( `v`.`"+dtQualAlias+"`) as dateQualMnth,"
		                +"YEAR( `v`.`"+dtQualAlias+"`) as dateQualYear,"
		                +"concat (  case when month("+dtQualAlias+") = '01' then 'JAN' "
		                +" when  month("+dtQualAlias+") = '02' then 'FEB' "
		                +" when  month("+dtQualAlias+") = '03' then 'MAR' "
		                +" when  month("+dtQualAlias+") = '04' then 'APR' "
		                +" when  month("+dtQualAlias+") = '05' then 'MAY' "
		                +" when  month("+dtQualAlias+") = '06' then 'JUN'"
		                +" when  month("+dtQualAlias+") = '07' then 'JUL'"
		                +" when  month("+dtQualAlias+")= '08' then 'AUG'"
		                +" when  month("+dtQualAlias+") = '09' then 'SEP'"
		                +" when month("+dtQualAlias+") = '10' then 'OCT'"
		                +" when  month("+dtQualAlias+") = '11' then 'NOV'"
		                +" when  month("+dtQualAlias+") = '12' then 'DEC'" 
		                +" END,"+"\""+"-"+"\", year("+dtQualAlias+")) AS recon_month";
				
			}
			
			if(bucketsQry!=null && !(bucketsQry.isEmpty()) && !(bucketsQry.equalsIgnoreCase(""))){
	            bucketsQry=bucketsQry+" , ";
	        }
			
			if(sysColQry!=null && !(sysColQry.isEmpty()) && !(sysColQry.equalsIgnoreCase(""))){
				sysColQry=sysColQry+" , ";
			}
			
			log.info("bucketsQry b4 updQuery: "+bucketsQry);
	        log.info("sysColQry b4 updQuery: "+sysColQry);

			String query = "`r`.`original_view_id` AS `viewid`, `r`.`reconciliation_rule_group_id` AS `rec_rule_group`, "
					+ " `r`.`reconciled_date` AS `reconciled_date`,  IF(`r`.`reconciliation_rule_group_id` is not null, 'Reconciled', 'Un-Reconciled') AS `recon_status`, "
					+ " DATEDIFF( `v`.`fileDate`, '"+ dateTymVal+"' ) as `rule_age`,"
					+ " `a`.`status` AS `acc_status`, "
					+ " a.acct_group_id AS acc_rule_Group, "
					+bucketsQry
					+ sysColQry
					+ " jcr.credit_amount As entered_cr_amt, jcr.accounted_credit As acc_cr_amt, "
					+" jdr.debit_amount AS entered_dr_amt,  jdr.accounted_debit AS acc_dr_amt, jdr.code_combination AS dr_code_combination,"
					+ "jcr.code_combination As cr_code_combination, jcr.created_date As journal_date,"
					//+ " jdr.debit_amount AS debit_amt,  jdr.accounted_debit AS acc_dr_amt, jdr.code_combination AS dr_code_combination,"
					+ " IF(`r`.`reconciled_date` is not null and `r`.`reconciled_date` <= '"
					+ dateTymVal
					+ "' , 'Reconciled', 'Un-Reconciled') AS `as_of_recon_status`, "
					+ " IF(jcr.created_date is not null and jcr.created_date <= '"
					+ dateTymVal
					+ "' , 'Accounted', 'Un-Accounted') AS `as_of_acc_status`, "
					+ " jdr.je_batch_id AS batch_id"
					+ " FROM "
					+ "(select * from  "
					+ " dv_data_ds_v) `v` "
					+ " LEFT OUTER JOIN recon_data_ds_v `r`  on ( `r`.`original_row_id` = `v`.`scrIds` ) "
					+ "  LEFT OUTER JOIN (select distinct tenant_id,original_row_id,acct_group_id,acct_rule_id,original_view_id,ad.status "
					+ " ,source_ref,ledger_ref,category_ref,currency_ref,amount_col_id,line_type_id,line_type_detail " // added
					+ "  from acc_data_ds_v ad where status = 'Accounted' and original_view_id="
					+ dataViewId
					+ ") `a` "
					+ "   on (`a`.`original_row_id` = `v`.`scrIds`)"
					+ " LEFT OUTER JOIN "
					
					/*+ "(select distinct row_id,credit_amount, accounted_credit  , code_combination, created_date from jrnl_data_ds_v where credit_amount is not null) jcr  on (`v`.`scrIds` = `jcr`.`row_id`) "*/
					
					+" (select distinct je.row_id,je.credit_amount, je.accounted_credit  , je.code_combination, je.created_date "
					+" from jrnl_data_ds_v je JOIN jrnlHeader_data_ds_v jh  where je.je_batch_id = jh.id and credit_amount is not null) jcr  on (`v`.`scrIds` = `jcr`.`row_id`) "	
					
					/*+ " LEFT OUTER JOIN (select distinct row_id,currency, debit_amount, accounted_debit, code_combination, je_batch_id "
					+ " from jrnl_data_ds_v where debit_amount is not null ) jdr "*/
					
					+" LEFT OUTER JOIN (select distinct je.row_id,je.currency, je.debit_amount, je.accounted_debit, je.code_combination, je.je_batch_id, jh.je_batch_name "
					+" from jrnl_data_ds_v je JOIN jrnlHeader_data_ds_v jh  where je.je_batch_id = jh.id AND debit_amount is not null ) jdr "
					
					+ "on (`v`.`scrIds` = `jdr`.`row_id`)";

			updQuery = viewColsQuery + "," + query;

			log.info("updQuery: " + updQuery);
			
			Dataset<Row> data = spark.sql(updQuery);

			log.info("Previewing data b4 applying filters with count : "+data.count());
			//data.show();
			//data.show((int) data.count());
			int cnt = data.collectAsList().size();
			log.info("cnt: " + cnt);

			log.info("Data Preview before applying filters with count: "+data.count());
			//data.show();

			String finQuery = "";
			String reconSatus="";
			String accSatus="";
			if (filtersMap != null && !(filtersMap.isEmpty())) {

				String conQuery = " where ";
				String conSubQueryFin = "";
					log.info("filters exists");
					if (filtersMap.containsKey("fields")) {
						List<HashMap> filtersList = (List<HashMap>) filtersMap
								.get("fields");
						log.info("fields exists with sz: " + filtersList.size());
						for (int i = 0; i < filtersList.size(); i++) {
							HashMap filterMap = filtersList.get(i);
							log.info("filterMap: " + filterMap);
							String conSubQuery = "";
							//Long paramRefColId=Long.parseLong(filterMap.get("refColId").toString());
							String colName="";
							Long refColId = Long.parseLong(filterMap.get(
									"refColId").toString());
							Long refSrcId=Long.parseLong(filterMap.get(
									"rParamSrcId").toString());
							String refType=filterMap.get(
									"refType").toString();
							log.info("refColId: "+refColId+" refSrcId: "+refSrcId+" refType: "+refType);
							ReportDefination repDefParamData=reportDefinationRepository.findByReportIdAndRefTypeIdAndRefSrcIdAndRefColId(reportId,refType,refSrcId,refColId);
							log.info("repDefParamData: "+repDefParamData);
							ReportParameters repParamData=reportParametersRepository.findByReportIdAndRefTypeidAndRefSrcIdAndRefColId(reportId, refType, refSrcId, refColId);
							log.info("repParamData: "+repParamData);
							
							/*if (filterMap.get("refType").toString()
									.equalsIgnoreCase("FIN_FUNCTION")) {
								LookUpCode finFunCode = lookUpCodeRepository
										.findOne(refColId);
								log.info("finFunCode: " + finFunCode);
								colName = finFunCode.getLookUpCode().toLowerCase();
							} else {*/
								//ReportDefination repDefParamData=reportDefinationRepository.findByReportIdAndRefTypeIdAndRefSrcIdAndRefColId(reportId,"DATA_VIEW",refSrcId,paramRefColId);
								if(repDefParamData!=null){
									colName=repDefParamData.getDisplayName();
									log.info("colName from dataview: "+colName);
								}
								else{
									//ReportParameters repParamData=reportParametersRepository.findByReportIdAndRefTypeidAndRefSrcIdAndRefColId(reportId, "DATA_VIEW", refSrcId, refColId);
									colName=repParamData.getDisplayName();
									log.info("colName rep param: "+colName);
								}
							//}
							log.info("colName: "+colName);
							String selType = filterMap.get("fieldType").toString();
							log.info("selType: " + selType);
							if (selType.equalsIgnoreCase("MULTI_SELECT_LOV")
									|| selType
											.equalsIgnoreCase("SINGLE_SELECT_LOV")
									|| selType.equalsIgnoreCase("SINGLE_SELECTION")
									|| selType.equalsIgnoreCase("TEXT")) {
								String blankQuery="";
								String selValData = "";
								String selValVar = filterMap.get("selectedValue")
										.toString();
								log.info("selValVar: " + selValVar);
								if(selValVar!=null && !(selValVar.isEmpty()) && !(selValVar.equalsIgnoreCase("[]"))){
								selValVar = selValVar.replace("[", "");
								selValVar = selValVar.replace("]", "");
								List<String> selValList = new ArrayList<String>(
										Arrays.asList(selValVar.split(",")));
								for (int k = 0; k < selValList.size(); k++) {
									String selVal = selValList.get(k);
									//log.info("selVal: "+selVal);
									selVal = selVal.trim();
									if(selVal.equalsIgnoreCase("(Blank)")){
										//log.info("matched blank: "+selVal);
										blankQuery="`"+colName+"` is null";
										log.info("blankQuery: "+blankQuery);
									}
									else selValData = selValData + "'" + selVal + "'";
									//log.info("selValData: "+selValData);
									if (k >= 0 && k < selValList.size() - 1) {
										if(selValData!=null && !(selValData.isEmpty()))
										selValData = selValData + ",";
									}
								}
								if (selValData != null && !(selValData.isEmpty())) {
									if (selValData.endsWith(",")) {
										int indx = selValData.lastIndexOf(",");
										selValData = selValData.substring(0, indx);
										log.info("selValData after removing comma: "
												+ selValData);
									}
									conSubQuery = conSubQuery + "`"+colName +"`" + " in ("
											+ selValData + ") ";
									//log.info("conSubQuery:"+conSubQuery+"@@@@");
								}
								else{
									
								}
								//if(selValList.size()>1 && selValList.contains("(Blank)")){
								if(selValList.size()>1 && blankQuery!=null && !(blankQuery.isEmpty())){
									conSubQuery=" ("+conSubQuery+" or "+blankQuery+") ";//(goup_by in (1) or goup_by is null)
								}
								if(conSubQuery==null || (conSubQuery!=null && conSubQuery.isEmpty()) || conSubQuery.equalsIgnoreCase("")){
									if(blankQuery!=null && !(blankQuery.isEmpty()))
									conSubQuery=blankQuery;
								}
								log.info("final conSubQuery: "+conSubQuery);
								}
							} else if (selType.equalsIgnoreCase("AUTO_COMPLETE")) {
								String selVal = filterMap.get("selectedValue")
										.toString();
								if (selVal != null && !(selVal.isEmpty())) {
									conSubQuery = conSubQuery + "`"+colName +"`" + " in ('"
											+ selVal + "') ";
								}
							} else if (selType
									.equalsIgnoreCase("BOOLEAN_SELECTION")) {
								String selVal = filterMap.get("selectedValue")
										.toString();
								if (selVal != null && !(selVal.isEmpty())) {
									if (conSubQuery.equalsIgnoreCase(" where ")) {

									} else
										conSubQuery = " and ";
									conSubQuery = conSubQuery + "`"+colName +"`" + " is "
											+ selVal;
								}
							} else if (selType.equalsIgnoreCase("AMOUNT_RANGE")) {
								String map = filterMap.get("selectedValue")
										.toString();
								if (map != null && !(map.isEmpty())) {
									log.info("map: " + map);
									map = map.replace("{fromValue=", "");
									map = map.replace("toValue=", "");
									log.info("map aftr replace: " + map);
									String arr[] = map.split(",");
									log.info(" arr[0]: " + arr[0]);
									log.info("arr[1]: " + arr[1]);
									arr[1] = arr[1].replace("}", "");
									log.info("arr[1]: " + arr[1]
											+ " arr[0]: " + arr[0]);
									String fromValue = arr[0].trim();
									String toValue = arr[1].trim();
									conSubQuery = conSubQuery + "`"+colName +"`"
											+ " between " + fromValue + " and "
											+ toValue;
								}
							}
							log.info("conSubQuery: " + conSubQuery);
							if (conSubQuery.length() > 1) {
								conSubQuery = conSubQuery + " and ";
							}
							conSubQueryFin = conSubQuery;
							if (conSubQuery.equalsIgnoreCase(" where ")) {

							} else {
								finQuery = finQuery + conSubQuery;
							}
						}

					}
					
					log.info("finQuery after conQuery: " + finQuery);
					String a = "";
					if (finQuery.endsWith(" and ")) {
						int lastAndVal = finQuery.lastIndexOf(" and ");
						log.info("lastAndVal :" + lastAndVal);

						StringBuilder sb = new StringBuilder(finQuery);
						finQuery = sb.substring(0, lastAndVal);
						log.info("part2 after replacing : " + finQuery);
					}
					log.info("finQuery after framing filters: " + finQuery);

					log.info("Applying filters on data set start");
					
					String statusFilters="";
					
					if(filtersMap.get("reconStatus")!=null && !(filtersMap.get("reconStatus").toString().isEmpty())){
						reconSatus=filtersMap.get("reconStatus").toString();
						if(reconSatus.equalsIgnoreCase("Both"))
							statusFilters=statusFilters+" as_of_recon_status in ('Reconciled','Un-Reconciled')";
						else statusFilters=statusFilters+" as_of_recon_status='"+reconSatus+"'";
					}
					log.info("statusFilters after recon status: "+statusFilters);
					if(filtersMap.get("accStatus")!=null && !(filtersMap.get("accStatus").toString().isEmpty())){
						accSatus=filtersMap.get("accStatus").toString();
						if(statusFilters!=null && !(statusFilters.isEmpty()) && !(statusFilters.equalsIgnoreCase("where"))){
							statusFilters=statusFilters+" and ";
						}
						if(accSatus.equalsIgnoreCase("Both"))
							statusFilters=statusFilters+" as_of_acc_status in ('Accounted', 'Un-Accounted') ";
						else statusFilters=statusFilters+" as_of_acc_status='"+accSatus+"'";
					}
					log.info("final statusFilters: "+statusFilters);
					if(statusFilters!=null && !(statusFilters.isEmpty()))
					{
						finQuery=finQuery+" and "+statusFilters;
					}

					log.info("finQuery after applying status filters: " + finQuery);
					data = data.filter(finQuery);
			}else {
				log.info("filters doesnt exist");
			}

			log.info("Previewing filtered data with sz: "+data.count());
			//data.show();
			//data.show((int) data.count());
			
			finalMap.put("data", data);
			finalMap.put("layoutColList", layoutColList);
			finalMap.put("buckAgg", buckAgg);
			finalMap.put("grpColArr", grpColArr);
			finalMap.put("reportTypeName", reportTypeName);
			finalMap.put("as_of_recon_status",reconSatus );
			finalMap.put("as_of_acc_status",accSatus );
			finalMap.put("othersFlag",othersFlag );
			
			
			return finalMap;

					}
	 
	 /**
	  * Author: Swetha
	  * Function to fetch ReportTypes based on Id
	  * @param tenantId
	  * @return
	  */
	 public HashMap<Long,ReportType> getReportTypes(Long tenantId){
		 List<ReportType> repTypeList=ReportTypeRepository.fetchActiveReportTypesByTenant(tenantId);
		 HashMap<Long, ReportType> map=new HashMap<Long, ReportType>();
		 for(int i=0;i<repTypeList.size();i++){
			 ReportType reportType=repTypeList.get(i);
			 map.put(reportType.getId(), reportType);
		 }
		 log.info("ReportTypeListMap: "+map);
		return map;
	 }
	 
	 /**
	  * Author: Swetha
	  * Function to get sql standard and unique alias name for sql query
	  * @param dpName
	  * @param reportId
	  */
	 public void getAliasNameForQry(String dpName,Long reportId){
		//Tested Format eg: String s="as%-???^&df_#S";
		 log.info("dpName to getAliasNameForQry is: "+dpName);
		 dpName= dpName.replaceAll(" ", "_").toLowerCase();
		Pattern pattern = Pattern.compile("[^a-z A-Z 0-9]");
		Matcher matcher = pattern.matcher(dpName);
		String number = matcher.replaceAll("");
		//log.info("number: "+number);

		String finalCol="";
		String b[]=number.split("\\s+");
		for(int l=0;l<b.length;l++){
			b[l]=b[l].replace(b[l].charAt(0), Character.toTitleCase(b[l].charAt(0)));
			finalCol=finalCol.concat(b[l]);
		}
		finalCol=finalCol+"_"+"reportColumn_"+reportId;
		//log.info("finalCol: "+finalCol);
		//fileTempLines.setColumnAlias(finalCol+"_"+filetempRecord.getId());
	 }
	 
	 /**
	  * Author: Swetha
	  * Function to return HeaderData for Data Table
	  * @param map
	  * @return
	  */
	 public List<LinkedHashMap> tabuleHeaderData(LinkedHashMap map,Long reportId){
		
		 log.info("Request to get HeaderMap with map: "+map);
		 Set keyset=map.keySet();
		 log.info("keyset: "+keyset);
		 Iterator itr=keyset.iterator();
		 LinkedHashMap findata=new LinkedHashMap();
		 List<LinkedHashMap> columns=new ArrayList<LinkedHashMap>();
		 HashMap<String, String> dataTypes=getDataType(reportId);
		 while(itr.hasNext()){
			 String key=(String) itr.next();
			 LinkedHashMap data=new LinkedHashMap();
			 String dataType=dataTypes.get(key.toLowerCase());
			 data.put("field", key);
			 data.put("width", "150px");
			 data.put("header", key);
			 log.info("key: "+key+" dataType: "+dataType);
			 if((dataType!=null && !(dataType.isEmpty()) && dataType.equalsIgnoreCase("DECIMAL")) || (dataType==null || dataType.isEmpty() ) ){
				 data.put("align", "right"); 
			 }
			 else data.put("align", "left");
			 columns.add(data);
		 }
		// findata.put("columns",columns);
		return columns;
	 }
	 
	 /**
	  * Author: Swetha
	  * Function to get Data Type for Report Definition Columns
	  * @param reportId
	 * @return 
	  */
	 public HashMap<String, String> getDataType(Long reportId){
		 
		 log.info("Request to getDataType for ReportId: "+reportId);
		 List<ReportDefination> reportdefList=reportDefinationRepository.findByReportId(reportId);
		 HashMap<String,String> map=new HashMap<String,String>();
		 for(int i=0;i<reportdefList.size();i++){
			 ReportDefination repDef=reportdefList.get(i);
			 String dpName=repDef.getDisplayName().toLowerCase();
			 String dtType=repDef.getDataType();
			 map.put(dpName, dtType);
		 }
		 log.info("datatTypes map: "+map);
		return map;
	 }
	 
	 /**
	  * Author: Swetha
	  * Function to get buckets display names
	  * @param reportId
	  * @param type
	  * @param bucketId
	  * @param bucketDetId
	  * @return
	  */
	 public String getBucketName(Long reportId,String type,Long bucketId, Long bucketDetId){
		 
		 log.info("Request to getBucketName for reportId: "+reportId+" bucketId: "+bucketId+" bucketDetId: "+bucketDetId);
		 ReportDefination repDef=reportDefinationRepository.findByReportIdAndRefTypeIdAndRefSrcIdAndRefColId(reportId, type, bucketId, bucketDetId);
		 String buckName=repDef.getDisplayName();
		 return buckName;
	 }
	 
	 /**
	  * Author: Swetha
	  * Get or Create Spark Session
	  * @return
	  */
	 public SparkSession getSparkSession()
		{
			SparkSession spark = SparkSession.builder()
					.appName("agree")
//					 .config("spark.master",
//					 "yarn-cluster").config("spark.rpc.askTimeout",
//					 "600s").getOrCreate();
					.config("spark.master", "local[*]")
					//.config("spark.executor.memory","3g")
					.getOrCreate();
			spark.sparkContext().setLogLevel("ERROR");
			
			return spark;
		}
	 
	 
	 /**
	  * POC for Accounting Work Queue
	  * @param tenantId
	  * @param reportId
	  * @param filtersMap
	  * @param spark
	  * @return
	  * @throws AnalysisException
	  * @throws IOException
	  * @throws ParseException
	  * Notes: hardcoded db credentials and query
	  */
	 public Dataset<Row> pocAWQ(
				@RequestParam Long tenantId, 
				@RequestBody(required = false) HashMap filtersMap, SparkSession spark,Long dataViewId, Long ruleGrpId) throws AnalysisException,
				IOException, ParseException {
	    	
	    	LinkedHashMap finalMap=new LinkedHashMap();

	    	List<String> layoutColList=new ArrayList<String>();
	    	
	    	List<String> grpColList=new ArrayList<String>();

			log.info("Rest Request to ReportGenerationNew with tenantId: "
					+ tenantId + " at: " + ZonedDateTime.now());
			log.info("filtersMap: " + filtersMap);

			JavaSparkContext sContext = new JavaSparkContext(spark.sparkContext());
			SQLContext sqlCont = new SQLContext(sContext);

			log.info("Spark Configuration end at: " + ZonedDateTime.now());
			
			String dvatViewName = "";

			String dbUrl = env.getProperty("spring.datasource.url");
			String[] parts = dbUrl.split("[\\s@&?$+-]+");
			String host = parts[0].split("/")[2].split(":")[0];
			String schemaName = parts[0].split("/")[3];
			String userName = env.getProperty("spring.datasource.username");
			String password = env.getProperty("spring.datasource.password");
			String jdbcDriver = env.getProperty("spring.datasource.jdbcdriver");
			
			/*String dbUrl = "jdbc:mysql://192.168.0.44:3306/agree_application_2712";
			//String[] parts = dbUrl.split("[\\s@&?$+-]+");
			String host = "192.168.0.44";
			String schemaName = "agree_application_2712";
			String userName = "recon_dev";
			String password = "Welcome321$";
			//String jdbcDriver = "";
*/
			Dataset<Row> datViewData = sqlCont.read().format("jdbc")
					.option("url", dbUrl).option("user", userName)
					.option("password", password).option("dbtable", "t_data_views")
					.load().where("id=" + dataViewId).select("data_view_name");

			Row dv = datViewData.collectAsList().get(0);

			dvatViewName = dv.getString(0);
			dvatViewName = dvatViewName.toLowerCase();

			log.info("dvatViewName: " + dvatViewName);

			String dvFilterStr = "";
			String recFilterStr = "tenant_id=" + tenantId
					+ " and original_view_id=" + dataViewId;
			/*String accFilterStr = "tenant_id=" + tenantId
					+ " and original_view_id=" + dataViewId;*/
			String accFilterStr = "tenant_id=" + tenantId;
			String jrnlFilterStr = "";
			String accSummaryFilterStr = "view_id="+dataViewId;
			String jrnlHeaderFilterStr = "tenant_id=" + tenantId;
			String rulesDataFilterStr="tenant_id=" + tenantId;

			DataFrameReader recon_data = sqlCont.read().format("jdbc")
					.option("url", dbUrl).option("user", userName)
					.option("password", password)
					.option("dbtable", "t_reconciliation_result");

			Dataset<Row> recon_data_ds = recon_data.load();

			log.info("recon_data_ds.count(): " + recon_data_ds.count());

			recon_data_ds = recon_data_ds.filter(recFilterStr);
			recon_data_ds.createTempView("recon_data_ds_v");

			recon_data_ds.registerTempTable("recon_data_ds_v");

			log.info("recon_data_ds_v.count(): " + recon_data_ds.count());

			DataFrameReader dv_data = sqlCont.read().format("jdbc")
					.option("url", dbUrl).option("user", userName)
					.option("password", password).option("dbtable", dvatViewName);

			Dataset<Row> dv_data_ds_load = dv_data.load();
			log.info("dv_data_ds.count(): " + dv_data_ds_load.count());
			
			
			log.info("previewing cmplt data view data");
	       // dv_data_ds_load.show();
	        Dataset<Row> dv_data_ds = null;
			
			/* Finding Date Limits to Filter data view data for Roll back report */
	        String dateTymVal=DateTime.now().toString();
	        if(filtersMap!=null && filtersMap.containsKey("dateTimeVal")){
	            dateTymVal=filtersMap.get("dateTimeVal").toString();
	        }
	        
	        String dtQualAlias="";
	        
	        
	        	dv_data_ds=dv_data_ds_load;
			//dv_data_ds.createTempView("dv_data_ds_v");

			dv_data_ds.registerTempTable("dv_data_ds_v");

			log.info("dv_data_ds.count(): " + dv_data_ds.count());

			DataFrameReader acc_data = sqlCont.read().format("jdbc")
					.option("url", dbUrl).option("user", userName)
					.option("password", password)
					.option("dbtable", "t_accounting_data");

			Dataset<Row> acc_data_ds = acc_data.load();
			log.info("acc_data_ds.count(): " + acc_data_ds.count());

			acc_data_ds = acc_data_ds.filter(accFilterStr);
			acc_data_ds.createTempView("acc_data_ds_v");

			acc_data_ds.registerTempTable("acc_data_ds_v");

			log.info("acc_data_ds_v.count(): " + acc_data_ds.count());

			DataFrameReader jrnlHeader_data = sqlCont.read().format("jdbc")
					.option("url", dbUrl).option("user", userName)
					.option("password", password)
					.option("dbtable", "t_journals_header_data");

			Dataset<Row> jrnlHeader_data_ds = jrnlHeader_data.load();

			if (jrnlHeaderFilterStr != null && !(jrnlHeaderFilterStr.isEmpty())) {
				jrnlHeader_data_ds = jrnlHeader_data_ds.filter(jrnlHeaderFilterStr);
			}
			jrnlHeader_data_ds.createTempView("jrnlHeader_data_ds_v");

			jrnlHeader_data_ds.registerTempTable("jrnlHeader_data_ds_v");

			log.info("jrnlHeader_data_ds_v.count(): " + jrnlHeader_data_ds.count());

			DataFrameReader jrnl_data = sqlCont.read().format("jdbc")
					.option("url", dbUrl).option("user", userName)
					.option("password", password).option("dbtable", "t_je_lines");

			Dataset<Row> jrnl_data_ds = jrnl_data.load();

			if (jrnlFilterStr != null && !(jrnlFilterStr.isEmpty())) {
				jrnl_data_ds = jrnl_data_ds.filter(jrnlFilterStr);
			}
			jrnl_data_ds.createTempView("jrnl_data_ds_v");

			jrnl_data_ds.registerTempTable("jrnl_data_ds_v");

			log.info("jrnl_data_ds_v.count(): " + jrnl_data_ds.count());
			
			DataFrameReader acc_summary_data = sqlCont.read().format("jdbc")
					.option("url", dbUrl).option("user", userName)
					.option("password", password).option("dbtable", "t_accounted_summary");

			Dataset<Row> acc_summary_data_ds = acc_summary_data.load();

			if (accSummaryFilterStr != null && !(accSummaryFilterStr.isEmpty())) {
				acc_summary_data_ds = acc_summary_data_ds.filter(accSummaryFilterStr);
			}
			acc_summary_data_ds.createTempView("acc_summary_data_ds_v");

			acc_summary_data_ds.registerTempTable("acc_summary_data_ds_v");

			log.info("acc_summary_data_ds_v.count(): " + acc_summary_data_ds.count());
			
			DataFrameReader rules_data = sqlCont.read().format("jdbc")
					.option("url", dbUrl).option("user", userName)
					.option("password", password).option("dbtable", "t_rules");

			Dataset<Row> rules_data_ds = rules_data.load();

			if (rulesDataFilterStr != null && !(rulesDataFilterStr.isEmpty())) {
				rules_data_ds = rules_data_ds.filter(rulesDataFilterStr);
			}
			rules_data_ds.createTempView("rules_data_ds_v");

			rules_data_ds.registerTempTable("rules_data_ds_v");

			log.info("rules_data_ds_v.count(): " + rules_data_ds.count());

			String dvAliasName = "v";
			String viewColsQuery = " ";
			viewColsQuery = "select " + dvAliasName + ".scrIds as masterid , "
	                + dvAliasName + ".fileDate as fileDate, ";
			//String accSummAlias="as";

		        String aggregator="";
		        String buckAgg="";
		        
		        Dataset<Row> dataViewColdata = sqlCont
						.read()
						.format("jdbc")
						.option("url", dbUrl)
						.option("user", userName)
						.option("password", password)
						.option("dbtable", "t_data_views_columns")
						.load();
		        
		        Dataset<Row> dvcData=dataViewColdata.where("data_view_id="+dataViewId)
		        		.select("ref_dv_name","ref_dv_column","column_name");
		        
		        List<Row> dvcList=dvcData.collectAsList();
		        
		        Dataset<Row> ftlData = sqlCont.read().format("jdbc")
						.option("url", dbUrl).option("user", userName)
						.option("password", password)
						.option("dbtable", "t_file_template_lines").load();
		        
		        for(int i=0;i<dvcList.size();i++){
		        	 String subQry="";
		        	Row dvc=dvcList.get(i);
		        	String refDvName=dvc.getString(0);
		        	String refDvCol=dvc.getString(1);
		        	String colName=dvc.getString(2);
		        	
		        	Dataset<Row> ftlDataSet=ftlData.where("id="+Long.parseLong(refDvCol))
			        		.select("column_alias");
		        	
		        	List<Row> ftlList=ftlDataSet.collectAsList();
		        	Row ftl=ftlList.get(0);
		        	String colAlias=ftl.getString(0);
		        	
		        	subQry=subQry+dvAliasName+"."+colAlias+" as `"+colName+"`";
		        	
		        	if (i >= 0 && i < dvcList.size() - 1
							&& i != dvcList.size()) {
						subQry = subQry + " , ";
					}
		        	
		        	log.info("subQry at i: "+i+" is: "+subQry);
					viewColsQuery = viewColsQuery + subQry;
					log.info("viewColsQuery at i: "+i+" is: "+viewColsQuery);
					
		        	
		        }
		        
		        log.info("viewColsQuery: "+viewColsQuery);
		        
			 if(viewColsQuery.endsWith(" , ")){
		            log.info("removing comma");
		            int commaIndex=viewColsQuery.lastIndexOf(",");
		            viewColsQuery=viewColsQuery.substring(0,commaIndex-1);
		        }

			log.info("final viewColsQuery: " + viewColsQuery);

			String updQuery = "";
	       /* updQuery=viewColsQuery+", a.ledger_ref, a.line_type_detail, a.category_ref,"
	        		+ " a.source_ref, a.currency_ref, a.line_type, a.acct_group_id fROM "
		+"(select * from acc_data_ds_v where acct_group_id = "+ruleGrpId+" ) `a` "
        +" LEFT OUTER JOIN dv_data_ds_v `v` " 
		+" on ( `v`.`scrIds` = `a`.`original_row_id` )";*/
			
			 updQuery=viewColsQuery+",ac.row_id,  ac.job_reference, ac.created_date, ac.rule_group_id, ac.rule_code, "
                +" IF(ac.rule_group_id is not null, 'Accounted', 'Not Accounted') AS `acc_status` "
                + " fROM dv_data_ds_v v LEFT OUTER JOIN "
                +" (SELECT a.row_id, r.rule_code, a.job_reference, a.created_date, a.rule_group_id from (acc_summary_data_ds_v a "
                +" Join rules_data_ds_v r) Where ((a.rule_id = r.id) and (a.rule_group_id ="+ruleGrpId+" )) ) ac "
                +" on ( v.scrIds = ac.row_id)";
			
			Dataset<Row> data = sqlCont.sql(updQuery);

			int cnt = data.collectAsList().size();
			log.info("cnt: " + cnt);

			log.info("Data Preview before applying filters");
			//data.show();

			String finQuery = "";
			if (filtersMap != null && !(filtersMap.isEmpty())) {

				String conQuery = " where ";
				String conSubQueryFin = "";
					log.info("filters exists");
					if (filtersMap.containsKey("fields")) {
						log.info("finQuery after conQuery: " + finQuery);
						String a = "";
						if (finQuery.endsWith(" and ")) {
							int lastAndVal = finQuery.lastIndexOf(" and ");
							log.info("lastAndVal :" + lastAndVal);

							StringBuilder sb = new StringBuilder(finQuery);
							finQuery = sb.substring(0, lastAndVal);
							log.info("part2 after replacing : " + finQuery);
						}
						log.info("finQuery after framing filters: " + finQuery);

						log.info("Applying filters on data set start");

						data = data.filter(finQuery);
						
					}
					
			}else {
				log.info("filters doesnt exist");
			}

			log.info("Previewing filtered data with sz: "+data.count());

			//data.show();
			
			sContext.close();
			return data;

					}
	 
	 
	 /**
	  * Author: Swetha
	  * Function to retune tabular view json for RollBack and Account Analysis
	  * @param reportId
	  * @param reportTypeName
	  * @param updLayoutDataList
	  * @param startIndex
	  * @param limit
	  * @param finalColList
	  * @return
	  */
	 public List<LinkedHashMap> retuneTabularReportJSON(Long reportId, String reportTypeName,List<Row> updLayoutDataList,int startIndex,int limit,String[] finalColList){
		 
	 log.info("in retuneTabularReportJSON for Report Id: "+reportId);
	 List<LinkedHashMap> maps=new ArrayList<LinkedHashMap>();
	 HashMap<String, String> dataTypes=getDataType(reportId);
	 for(int j=startIndex;j<limit;j++){
         LinkedHashMap map = new LinkedHashMap();
         
         for (int s = 0; s < (finalColList.length); s++) {
             String tabColName = finalColList[s];
             //log.info("tabColName: "+tabColName);
             if(tabColName==null){
                 //log.info("in null");
                 tabColName="Others";
             }
             String dataType=dataTypes.get(tabColName.toLowerCase());
             //log.info("dataType: "+dataType);
             String val = "";
             String finVal="";
             //log.info("tabData.get(j): "+tabData.get(j));
             //log.info("tabData.get(j).get(s): "+tabData.get(j).get(s));
             //val = updLayoutDataList.get(j).get(s).toString();
             //log.info("updLayoutDataList.get(j).get(s): "+updLayoutDataList.get(j).get(s));
             if (updLayoutDataList.get(j).get(s) != null
                     && !(updLayoutDataList.get(j).get(s).toString().isEmpty())) {
                 finVal = updLayoutDataList.get(j).get(s).toString();
                 //log.info("finVal: "+finVal);
                 //log.info("tabColName: "+tabColName+" dataType: "+dataType );
                 if(dataType!=null && dataType.equalsIgnoreCase("DECIMAL") || tabColName.equalsIgnoreCase("Others")){

                     double value=Double.valueOf(finVal);
                     DecimalFormat formatter1;

                     if(value - (int)value > 0.0)
                         formatter1 = new DecimalFormat("0.00"); // Here you can also deal with rounding if you wish..
                     else
                         formatter1 = new DecimalFormat("0");
                     
                     finVal=formatter1.format(value);
                     //log.info("finVal in decimal format: "+finVal);
                     finVal=String.format("%,.2f", Double.parseDouble(finVal));
                     //log.info("finVal aftr fomatting amount: "+finVal);
                     map.put(tabColName, finVal);
                     //log.info("finVal in decimal format: "+finVal);
                     //val=finVal.toString();
                     //log.info("assigned to val [decimal non empty]: "+val);
                    // log.info("finVal to string: "+finVal.toString());
                    // log.info("val b4 format: "+val);
                     //val=String.format("%,.2f", Double.parseDouble(val));
                     //log.info("val after amount format: "+val);
                 }
                 else if(tabColName.equalsIgnoreCase("as_of_recon_status") || tabColName.equalsIgnoreCase("as_of_acc_status")){
                	 map.put(tabColName, finVal);
                 }
                 else if(dataType==null){
                 	//log.info("setting for null data type values");
                 	double value=Double.valueOf(finVal);
                     DecimalFormat formatter1;

                     if(value - (int)value > 0.0)
                         formatter1 = new DecimalFormat("0.00"); // Here you can also deal with rounding if you wish..
                     else
                         formatter1 = new DecimalFormat("0");
                     
                     finVal=formatter1.format(value);
                    // log.info("finVal in decimal format: "+finVal);
                     finVal=String.format("%,.2f", Double.parseDouble(finVal));
                     //log.info("finVal aftr fomatting amount: "+finVal);
                     map.put(tabColName, finVal);
                 }
                 else if(dataType!=null && !(dataType.equalsIgnoreCase("DECIMAL")) && !(tabColName.equalsIgnoreCase("Others"))){
                     //log.info("in setting non decimal non empty val");
                     //val=finVal;
                      map.put(tabColName, finVal);
                 }
             }
             //else if(dataType!=null && dataType.equalsIgnoreCase("DECIMAL") && (updLayoutDataList.get(j).get(s)==null || (updLayoutDataList.get(j).get(s)!=null && updLayoutDataList.get(j).get(s).toString().isEmpty()))){
             else if(updLayoutDataList.get(j).get(s)==null || (updLayoutDataList.get(j).get(s)!=null && updLayoutDataList.get(j).get(s).toString().isEmpty())){
                 if(dataType!=null && dataType.equalsIgnoreCase("DECIMAL")){
                     //log.info("in if decimal and null value");
                     //val=String.valueOf(0);
                     map.put(tabColName, "0.00");
                     //log.info("val: "+val);
                 }else{
                     //log.info("in empty non decimal null values");
                     val="";
                     map.put(tabColName, val);
                 }
                 
             }
             else{
                 //log.info("in empty or null values");
                 val = " ";
             }
             //map.put(tabColName, val);

         }
		if(map!=null && !(map.isEmpty()) )
         maps.add(map);
	 }
	return maps;
     }
	 
	 /**
	  * Author: Swetha
	  * Function to retrieve grouped data from DataView
	  * @param dataViewId
	  * @param spark
	  * @param tenantId
	  * @return
	  */
	 public HashMap getGroupedDataViewData(Long dataViewId, SparkSession spark, Long tenantId){
		 
		JavaSparkContext sContext = new JavaSparkContext(spark.sparkContext());
		SQLContext sqlCont = new SQLContext(sContext);
		
		HashMap finMap=new HashMap();
		
		String dbUrl = env.getProperty("spring.datasource.url");
		String[] parts = dbUrl.split("[\\s@&?$+-]+");
		String host = parts[0].split("/")[2].split(":")[0];
		String schemaName = parts[0].split("/")[3];
		String userName = env.getProperty("spring.datasource.username");
		String password = env.getProperty("spring.datasource.password");
		String jdbcDriver = env.getProperty("spring.datasource.jdbcdriver");
		
		Dataset<Row> datViewCmpltData = sqlCont.read().format("jdbc")
				.option("url", dbUrl).option("user", userName)
				.option("password", password).option("dbtable", "t_data_views")
				.load().where("id=" + dataViewId);

		Dataset<Row> datViewData=datViewCmpltData.select("data_view_name");
		Row dv = datViewData.collectAsList().get(0);
		String dataViewName = dv.getString(0);
		dataViewName = dataViewName.toLowerCase();
		log.info("dvatViewName: " + dataViewName);
		
		DataFrameReader dv_data = sqlCont.read().format("jdbc")
				.option("url", dbUrl).option("user", userName)
				.option("password", password).option("dbtable", dataViewName);

		Dataset<Row> dv_data_ds_load = dv_data.load();
		
		log.info("dv_data_ds_load count: "+dv_data_ds_load.count());
		
		//Dataset<Row> ds_data_ds_datespecific=dv_data_ds_load.where("fileDate='2014-01-04 00:00:00'");
		
		Dataset<Row> ds_data_ds_datespecific=dv_data_ds_load;
		
		DataFrameReader datViewColumnsCmpltData = sqlCont.read().format("jdbc")
				.option("url", dbUrl).option("user", userName)
				.option("password", password).option("dbtable", "t_data_views_columns");
				
		
		Dataset<Row> dvGrpData=datViewColumnsCmpltData.load().where("data_view_id=" + dataViewId+" and group_by=true").select("ref_dv_name","ref_dv_column");
		
		List<Row> groupingColList=dvGrpData.collectAsList();
		
		DataFrameReader ftlData = sqlCont.read().format("jdbc")
				.option("url", dbUrl).option("user", userName)
				.option("password", password).option("dbtable", "t_file_template_lines");
		
		List<String> colAliasList=new ArrayList<String>();
		
		for(int i=0;i<groupingColList.size();i++){
			
			Row groupColData=groupingColList.get(i);
			String refDvName=groupColData.getString(0);
			String refDvCol=groupColData.getString(1);
			
			Dataset<Row> ftl=ftlData.load().where("id="+refDvCol).select("column_alias");
			String colAlias=ftl.collectAsList().get(0).getString(0);
			colAliasList.add(colAlias);
		}
		log.info("colAliasList for grouping: "+colAliasList);
		
		Column[] grpColArr=new Column[colAliasList.size()];
		for(int j=0;j<colAliasList.size();j++){
			String column=colAliasList.get(j);
			Column col=new Column(column);
			grpColArr[j]=col;
		}
		
		RelationalGroupedDataset grpData=ds_data_ds_datespecific.groupBy(grpColArr); 
		
		Dataset<Row> dvcAmtQualData=datViewColumnsCmpltData.load().where("data_view_id="+dataViewId+" and qualifier='AMOUNT'").select("ref_dv_column");
		String dvcAmtQual=dvcAmtQualData.collectAsList().get(0).getString(0);
		Dataset<Row> amtAliasData=ftlData.load().where("id="+Long.parseLong(dvcAmtQual)).select("column_alias");
		String amtQualAlias=amtAliasData.collectAsList().get(0).getString(0);
		
		Dataset<Row> aggData=grpData.sum(amtQualAlias);
		
		log.info("Previewing grouped data WITH COUNT:"+aggData.count());
		//aggData.show((int) aggData.count());
		 
		List<Row> aggDataList=aggData.collectAsList();
		
		String[] aggDataCols=aggData.columns();
		
		List<LinkedHashMap> grpDataMaps = new ArrayList<LinkedHashMap>();
		for (int j = 0; j < aggDataList.size(); j++) {
			LinkedHashMap map = new LinkedHashMap();
			for (int s = 0; s < (aggDataCols.length); s++) {
				String tabColName = aggDataCols[s];
				String val = "";
				if (aggDataList.get(j).get(s) != null
						&& !(aggDataList.get(j).get(s).toString().isEmpty())) {
					val = aggDataList.get(j).get(s).toString();
				} else
					val = " ";
				map.put(tabColName, val);
			}
			grpDataMaps.add(map);
		}
		
		//log.info("grpDataMaps: "+grpDataMaps);
		
		finMap.put("grpDataMaps", grpDataMaps);
		finMap.put("dataViewName", dataViewName);
		finMap.put("aggDataCols", aggDataCols);
		
		sContext.close();
		
		return finMap;
	 }
	 
	 
	 /**
	  * Author: Swetha
	  * Function to Create a JSON File, Write Object to it and Move it to HDFS 
	  * @param reportId
	  * @param obj
	  * @return
	  * @throws IOException
	  */
	 public String FileWriteHDFS(Long reportId,JSONObject obj,String type,Long tenantId) throws IOException {
			log.info("Inside file write hdfs");
			String fileName="";
			String serverCpmltPath="";
			try {
				Long startDate = System.currentTimeMillis() ;
	        	Long startNanoseconds = System.nanoTime() ;
	        	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS") ;
	        	Long microSeconds = (System.nanoTime() - startNanoseconds) / 1000 ;
	        	Long date = startDate + (microSeconds/1000) ;
	  		    String res= dateFormat.format(date) + String.format("%03d", microSeconds % 1000);
	  		    String updRes=res.replace(' ', '_');
	  		    updRes=updRes.replace(':', '-');
	  		    updRes=updRes.replace('.', '-');
	  		    if(type.equalsIgnoreCase("Output"))
	  		    	fileName="Output_"+reportId+"_"+updRes+".json";
	  		    else
	  		    	fileName="Params_"+reportId+"_"+updRes+".json";
				
	        	BufferedWriter bw = null;
				FileWriter fw = null;
				ApplicationPrograms prog=applicationProgramsRepository.findByPrgmNameAndTenantIdAndEnableIsTrue("Reporting",tenantId);
				String hadoopRootUser=env.getProperty("oozie.hadoopRootUser");
				String hdfsHostPath=env.getProperty("spring.hadoop.config.fs.defaultFS");
				String hadoopBaseDir=env.getProperty("baseDirectories.hadoopBaseDir");
				String genPath=prog.getGeneratedPath();
				String linuxBaseDir=env.getProperty("baseDirectories.linuxBaseDir");
				log.info("genPath b4: "+genPath+" linuxBaseDir: "+linuxBaseDir);
				genPath=genPath.replace("XML", "reportsTempPath");
				log.info("genPath aftr: "+genPath);
				String pgmParametersPth=hadoopBaseDir+genPath;
				log.info("pgmParametersPth: "+pgmParametersPth);
				String FILENAME = linuxBaseDir+"/Reporting/"+fileName;
				//String FILENAME = "/home/nspl/Reporting/"+fileName;
				log.info("File Name is --> "+FILENAME);
				
				fw = new FileWriter(FILENAME);
				bw = new BufferedWriter(fw);
				bw.write(obj.toJSONString());
				log.info("File created successfully");
					bw.flush();
						if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();
				
				UserGroupInformation ugi
		        = UserGroupInformation.createRemoteUser(hadoopRootUser);
				
				String serverPath=hdfsHostPath+hadoopBaseDir+genPath;
				log.info("serverPath: "+serverPath);
				
				serverCpmltPath=serverPath+fileName;
				
				 ugi.doAs(new PrivilegedAction<Void>() {

					@Override
					public Void run(){
						// TODO Auto-generated method stub
						Configuration conf = new Configuration();
						//@@@
						conf.set("fs.default.name", serverPath);
						conf.set("hadoop.job.ugi", hadoopRootUser);
					
						FileSystem file = null;
						try {
							file = FileSystem.get(conf);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						File f=new File(FILENAME);
								//we can filter files if needed here
								 try {
									file.copyFromLocalFile(true, true, new Path(f.getPath()), new Path(pgmParametersPth));
								} catch (IllegalArgumentException | IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
						
						log.info("copied to HDFS");
						try {
							file.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return null;
					}
				 });
	        	
			} catch (IOException e) {
				log.info("e: "+e);
			}
			
			return serverCpmltPath;
	 }	
	 
	 /**
	  * Author: Swetha
	  * Function to read file from HDFS
	  * @param filePath
	  * @return
	  * @throws IOException
	  * @throws URISyntaxException
	  * @throws org.json.simple.parser.ParseException
	  */
	 public JSONObject testFileReading(String filePath,Long userId,String jobId,Long schedulerId,Long tenantId,Long reportId) throws IOException, URISyntaxException, org.json.simple.parser.ParseException 
	 {
	  Configuration configuration = new Configuration();
	  URI uri = new URI(filePath); //hdfs://192.168.0.155:9000/user/hdsingle/examples/apps/dev/reporting/report_parameters/Params_9_2018-01-25_12-28-21.834072.json
	  FileSystem hdfs = FileSystem.get(uri, configuration);
	  InputStream inputStream = null;
	  JSONObject jsonObject=new JSONObject();
	  try{
	   Path path = new Path(uri);
	   inputStream = hdfs.open(path);
	   String fileData=inputStream.toString();
	   //log.info("fileData: "+fileData);
	   
	   JSONParser jsonParser = new JSONParser();
	   InputStreamReader isr=new InputStreamReader(inputStream, "UTF-8");
	   jsonObject = (JSONObject)jsonParser.parse(isr);
	   
	   //log.info("jsonObject: "+jsonObject);
	   
	   if(jsonObject!=null && !(jsonObject.isEmpty())){
		   JobActions jobActions = new JobActions();
		   jobActions.setActionName("Reading Report Output from HDFS for ReportId: "+reportId);
		   jobActions.setCreatedBy(userId);
		   jobActions.setCreatedDate(ZonedDateTime.now());
		   jobActions.setJobId(jobId);
		   jobActions.setSchedulerId(schedulerId);
		   jobActions.setStatus("Success");
		   jobActions.setTenantId(tenantId);
		   jobActionsRepository.save(jobActions);
		   
	   }
	   isr.close();
	  }finally{
	   IOUtils.closeStream(inputStream);
	   hdfs.close();
	  }
	return jsonObject;

	 }
	 
	 /**
	  * Author: Swetha
	  * Function to retrieve job status
	  * @param tenantId
	  * @param val
	  * @param userId
	  * @param reportId
	  * @return
	  * @throws IOException
	  * @throws URISyntaxException
	  * @throws org.json.simple.parser.ParseException
	  */
	 public LinkedHashMap getStatus(Long tenantId,String val,Long userId,Long reportId) throws IOException, URISyntaxException, org.json.simple.parser.ParseException{
		 log.info("in getStatus with val: "+val+" and tenantId:"+tenantId);
		 Boolean flag=false;
		 LinkedHashMap map=new LinkedHashMap();
		 JSONObject output=new JSONObject();
		 JSONObject pivotOutput=new JSONObject();
		 //if(status.equalsIgnoreCase("SUCCEEDED")){
				/* On Job success, Access action logs to retrieve report output path */
				//Report output path for reportId: 12 is: hdfs://192.168.0.155:9000/user/hdsingle/examples/apps/dev/reporting/report_parameters/TabularOutput12_2018-01-27_15-42-21.388049.json
		List<JobActions> jobActionsList=jobActionsRepository.findAll();
		log.info("jobActionsList sz: "+jobActionsList.size());
		
		List<JobActions> jobactList=jobActionsRepository.findByJobId(val);
		log.info("jobactList sz: "+jobactList.size());
		
		 JobActions jobAction=jobActionsRepository.findReportOutputPath(val, tenantId);
				log.info("jobAction: "+jobAction);
				String outputPath="";
				if(jobAction!=null){
				log.info("jobAction: "+jobAction);
				String actionName=jobAction.getActionName();
				String[] actionNamesArr=actionName.split("is: ");
				log.info("actionNamesArr -0: "+actionNamesArr[0]+" actionNamesArr-1: "+actionNamesArr[1]);
				outputPath=actionNamesArr[1];
				Long schedulerId=jobAction.getSchedulerId();
				output=testFileReading(outputPath,userId,val,schedulerId,tenantId,reportId);
				//flag=true;
				map.put("output", output);
				map.put("outputPath", outputPath);
				}
				else{
					//output=null;
				}
				/*}
				else{
					//log.info("progra is still running");
				}*/
				JobActions pivotPathData=jobActionsRepository.findReportPivoutOutputPath(val, tenantId);
				String pivotPath="";
				if(pivotPathData!=null){
					log.info("pivotPathData: "+pivotPathData);
					String actionName=pivotPathData.getActionName();
					String[] actionNamesArr=actionName.split("is: ");
					log.info("actionNamesArr -0: "+actionNamesArr[0]+" actionNamesArr-1: "+actionNamesArr[1]);
					pivotPath=actionNamesArr[1];
					Long schedulerId=pivotPathData.getSchedulerId();
					pivotOutput=testFileReading(pivotPath,userId,val,schedulerId,tenantId,reportId);
					//flag=true;
					map.put("pivotOutput", pivotOutput);
					map.put("pivotOutputPath", pivotPath);
					}
					else{
						//output=null;
					}
					/*}
					else{
						//log.info("progra is still running");
					}*/
						
		 return map;
		 
	 }
	 
	 /**
	  * Author: Swetha
	  * Function to get balances column
	  * @param dataViewId
	  * @return
	  */
	 public HashMap getBalancesColumn(Long dataViewId){
		 
		 log.info("in getBalancesColumn with dataViewId: "+dataViewId);
		 HashMap map=new HashMap();
		 
		 List<DataViewsColumns> groupingColList=dataViewsColumnsRepository.findByDataViewIdAndGroupByIsTrue(dataViewId);
			
			for(int i=0;i<groupingColList.size();i++){
				DataViewsColumns row=groupingColList.get(i);
				Long dvcId=row.getId();
				log.info("dvcId: "+dvcId);
				map.put(dvcId, "");
				
			}
			log.info("Final map: "+map);
			return map;
	 }
	 
	 /**
	  * Author: Swetha
	  * Function to frame balances query
	  * @param dataViewId
	  * @param reportId
	  * @return
	  */
	 public HashMap getBalancesQuery(Long dataViewId, Long reportId){
		 
		 List<String> layoutColList=new ArrayList<String>();
		 HashMap map=getBalancesColumn(dataViewId);
		 log.info("Final empty map: "+map);
		 List<String> balancesColums= balanceTypeRepository.fetchBalancesTableColumns("t_balance_type");
			
			List<String> balancesCols=new ArrayList<String>();
			for(int j=0;j<balancesColums.size();j++){
				String balColName=balancesColums.get(j).toLowerCase();
				//log.info("balColName: "+balColName);
				//if(balColName.startsWith("field_")){
					balancesCols.add(balColName);
				//}
			}
			
			log.info("Final balancesCols: "+balancesCols);
			
		Set<String> keys = map.keySet();
		 int keySz=keys.size();
		 Iterator entries = keys.iterator();
			int count=0;
			String query=" ";
			while (entries.hasNext()){
				Long entry=(Long) entries.next();
				log.info("entry: "+entry+" with count: "+count);
				String val=balancesCols.get(count);
				//log.info("val: "+val);
				ReportDefination repDefRow=reportDefinationRepository.findByReportIdAndRefTypeIdAndRefColId(reportId, "DATA_VIEW", entry);
				if(repDefRow!=null){
				String repDefColName=repDefRow.getDisplayName();
				query=query+val+" as '"+repDefColName+"'";
				layoutColList.add(repDefColName);
				if(count>=0 && count<keySz-1){
					query=query+" , ";
				}
			}
				log.info("query at count: "+count+" is: "+query);
				count=count+1;
			}
			
			log.info("Final Balances Query [DV Cols]: "+query);
			
			String sysColQry="";
			/*if(query!=null && !(query.isEmpty()) && query.length()>2){
				sysColQry=" , ";
			}*/
			
			List<ReportDefination> repDefSysCols=reportDefinationRepository.findByReportIdAndRefTypeId(reportId, "FIN_FUNCTION");
			
			log.info("repDefSysCols.size(): "+repDefSysCols.size() );
			for(int i=0;i<repDefSysCols.size();i++){
				ReportDefination row=repDefSysCols.get(i);
				Long refSrcId=row.getRefSrcId();
				Long refColId=row.getRefColId();
				String colName=row.getDisplayName();
				
				LookUpCode lCodeData=lookUpCodeRepository.findOne(refColId);
				String lookupCode=lCodeData.getLookUpCode().toLowerCase();
				log.info("lookupCode: "+lookupCode+" colName: "+colName);
				sysColQry=sysColQry+lookupCode+" as '"+colName+"'";
				log.info("sysColQry at i: "+i+" is: "+sysColQry);
				layoutColList.add(colName);
				if(i>=0 && i<repDefSysCols.size()-1){
					sysColQry=sysColQry+" , ";
				}
			}
		log.info("Sys Col Balances Qry: "+sysColQry);	
		query=query+sysColQry;
		log.info("Final Balances query: "+query);
		log.info("Final layoutColList for Balances: "+layoutColList);	
		
		
		HashMap finMap=new HashMap();
		finMap.put("layoutColList", layoutColList);
		finMap.put("query", query);
		
		return finMap;
		 
	 }
	 
	 /**
	  * Author: Swetha
	  * Reading hdfs file from qat
	  * @param filePath
	  * @return
	  * @throws IOException
	  * @throws URISyntaxException
	  * @throws org.json.simple.parser.ParseException
	  */
	 public LinkedHashMap hdfsFileReading(String filePath) throws IOException, URISyntaxException, org.json.simple.parser.ParseException 
	 {
		  log.info("In hdfsFileReading with filePath: "+filePath);
		  Configuration configuration = new Configuration();
		  URI uri = new URI(filePath); //hdfs://192.168.0.155:9000/user/hdsingle/examples/apps/dev/reporting/report_parameters/Params_9_2018-01-25_12-28-21.834072.json
		  FileSystem hdfs = FileSystem.get(uri, configuration);
		  InputStream inputStream = null;
		  JSONObject jsonObject=new JSONObject();
		  try{
		   Path path = new Path(uri);
		   inputStream = hdfs.open(path);
		  // String fileData=inputStream.toString();
		   //log.info("fileData: "+fileData);
		   
		   JSONParser jsonParser = new JSONParser();
		   InputStreamReader isr=new InputStreamReader(inputStream, "UTF-8");
		   jsonObject = (JSONObject)jsonParser.parse(isr);
		   
		   //log.info("jsonObject: "+jsonObject);
		   isr.close();
		  }finally{
		   IOUtils.closeStream(inputStream);
		   hdfs.close();
		  }
		  
		  LinkedHashMap map=new LinkedHashMap();
		  map.putAll(jsonObject);
		  
		return map;

	 }
	 
	 /**
	  * Author: Swetha
	  * POC on Livy Integration: Framing Dynamic data set query
	  * @param tenantId
	  * @param reportId
	  * @param filtersMap
	  * @return
	  * @throws AnalysisException
	  * @throws IOException
	  * @throws ParseException
	  */
	 public LinkedHashMap<String, Object> reportDataSetCreationLivy(
				Long tenantId, Long reportId,
				HashMap filtersMap) throws AnalysisException,
				IOException, ParseException {

			log.info("in reportDataSetCreation with tenantId: "
					+ tenantId + " reportId: " + reportId+ " at: " + new Date());
	    	LinkedHashMap<String, Object> finalMap=new LinkedHashMap<String, Object>();
	    	List<String> layoutColList=new ArrayList<String>();
	    	List<String> grpColList=new ArrayList<String>();
			HashMap<Long, String> repTypesData=getReportTypesLivy(tenantId);
			log.info("repTypesData: "+repTypesData);
			Boolean decimalCol=false;

			/*Dataset<Row> reportsData=spark.read().format("jdbc")
					.option("url", DbDetails.getMySqlUrl()).option("user", DbDetails.getUsername())
					.option("password", DbDetails.getPassword()).option("dbtable", "t_reports").load().where("id="+reportId)
					.select("source_view_id","report_val_01","report_type_id","report_val_02").cache();
			Row report = reportsData.collectAsList().get(0);*/
			Reports report=reportsRepository.findOne(reportId);
			Long dataViewId = report.getSourceViewId();
			Long repTypeId=report.getReportTypeId();
			String reportTypeName=repTypesData.get(repTypeId).toString(); 
			
			int num=0;
			Long bucketId= 0L;
			HashMap balancesMap=new HashMap();
			
			if(reportTypeName.equalsIgnoreCase("AGING_REPORT")){
	        if(report.getReportVal01()!=null && !(report.getReportVal01().toString().isEmpty())){
	            bucketId= Long.parseLong(report.getReportVal01());
	        }
			}
			if(reportTypeName.equalsIgnoreCase("ROLL_BACK_REPORT")){
			num=Integer.parseInt(report.getReportVal02());
			}
			if(reportTypeName.equalsIgnoreCase("ACCOUNT_BALANCE_REPORT")){
				 balancesMap=getBalancesColumn(dataViewId);
				}

			/*Dataset<Row> datViewData = spark.read().format("jdbc")
					.option("url", dbUrl).option("user", userName)
					.option("password", password).option("dbtable", "t_data_views")
					.load().where("id=" + dataViewId).select("data_view_name").cache();

			Row dv = datViewData.collectAsList().get(0);*/
			
			DataViews dv=dataViewsRepository.findOne(dataViewId);
			String dvatViewName = dv.getDataViewName();
			dvatViewName = dvatViewName.toLowerCase();

			log.info("dvatViewName: " + dvatViewName);

			String dvFilterStr = "";
			String jrnlFilterStr = "";
			/*DataFrameReader repDataDF = spark
					.read()
					.format("jdbc")
					.option("url", dbUrl)
					.option("user", userName)
					.option("password", password)
					.option("dbtable", "t_report_parameters");
			
					Dataset<Row> repData=repDataDF.load()
					.where("report_id = " + reportId + " and ref_src_id="
							+ dataViewId + " and ref_typeid='DATA_VIEW'").cache();

			List<Row> repParamsList = repData.collectAsList();*/

			/*Dataset<Row> recon_data_ds = spark.read().format("jdbc")
					.option("url", dbUrl).option("user", userName)
					.option("password", password)
					.option("dbtable", "t_reconciliation_result").load().where("tenant_id=" + tenantId
							+ " and original_view_id=" + dataViewId).cache();
			recon_data_ds.createTempView("recon_data_ds_v");
			recon_data_ds.registerTempTable("recon_data_ds_v");*/
			
			 String dateTymVal=null;
		        if(filtersMap!=null && filtersMap.containsKey("dateTimeVal")){
		            dateTymVal=filtersMap.get("dateTimeVal").toString();
		            
		        }
			
			/* Report Conditions */
			/*DataFrameReader repDefConddataDF = spark
					.read()
					.format("jdbc")
					.option("url", dbUrl)
					.option("user", userName)
					.option("password", password)
					.option("dbtable", "t_report_defination");
			
			DataFrameReader dataViewColDF = spark
					.read()
					.format("jdbc")
					.option("url", dbUrl)
					.option("user", userName)
					.option("password", password)
					.option("dbtable", "t_data_views_columns");
			
			DataFrameReader ftlDataDF = spark.read().format("jdbc")
					.option("url", dbUrl).option("user", userName)
					.option("password", password)
					.option("dbtable", "t_file_template_lines");
			
			DataFrameReader lookupCodeDF= spark.read().format("jdbc")
					.option("url", dbUrl).option("user", userName)
					.option("password", password)
					.option("dbtable", "look_up_code");
			Dataset<Row> lookupCodeDS=lookupCodeDF.load().where("tenant_id="+tenantId).cache();
			
			Dataset<Row> repDefConddata =repDefConddataDF.load()
					.where("report_id="
							+ reportId
							+ " and ref_type_id='DATA_VIEW' and conditional_operator is not null and conditional_val is not null")
					.select("ref_col_id", "conditional_operator", "conditional_val").cache();*/

		   List<ReportDefination> repDefConddata=reportDefinationRepository.fetchReportConditions(reportId);
		        
			//if(repDefConddata!=null){
			//List<Row> dvCondList = repDefConddata.collectAsList();
			if(repDefConddata.size()>0){
			String condQry = "";
			
			for (int k = 0; k < repDefConddata.size(); k++) {
				ReportDefination dvCond = repDefConddata.get(k);
				String conSubQry = "";
				Long refColId = dvCond.getRefColId();
				String condOperOrg = dvCond.getConditionalOperator();
				String condOp = "";
				String condVal = dvCond.getConditionalVal();

				/*Dataset<Row> dataViewColdata = dataViewColDF
						.load()
						.where("id=" + refColId)
						.select("col_data_type", "column_name", "ref_dv_column",
								"ref_dv_name").cache();
				List<Row> dvcoldataList = dataViewColdata.collectAsList();*/
				
				DataViewsColumns  dvcolData=dataViewsColumnsRepository.findOne(refColId);

				//Row dvcolData = dvcoldataList.get(0);
				String colDtType = dvcolData.getColDataType();
				String dvColId = dvcolData.getRefDvColumn();
				String refDvId = dvcolData.getRefDvName();
				
				/*Dataset<Row> ftlDataList=ftlDataDF.load().where("id=" + dvColId).select("column_alias").cache();
				List<Row> ftlDataValList = ftlDataList.collectAsList();
				Row ftlData = ftlDataValList.get(0);*/
				FileTemplateLines ftlData=fileTemplateLinesRepository.findOne(Long.parseLong(dvColId));
				String colAlias = ftlData.getColumnAlias();

				if (colDtType.equalsIgnoreCase("VARCHAR")) {
					if (condOperOrg.equalsIgnoreCase("EQUALS")
							|| condOperOrg.equalsIgnoreCase("NOT EQUALS")) {
						condOp = "=";
						conSubQry = conSubQry + colAlias + condOp + "'" + condVal
								+ "'";
					}
					if (condOperOrg.equalsIgnoreCase("CONTAINS")) {
						condOp = "like";
						conSubQry = conSubQry + colAlias + condOp + "'%" + condVal
								+ "%'";
					}
					if (condOperOrg.equalsIgnoreCase("BEGINS_WITH")) {
						condOp = "like";
						conSubQry = conSubQry + colAlias + condOp + "'%" + condVal
								+ "'";
					}
					if (condOperOrg.equalsIgnoreCase("ENDS_WITH")) {
						condOp = "like";
						conSubQry = conSubQry + colAlias + condOp + "'" + condVal
								+ "%'";
					}
				} else if (colDtType.equalsIgnoreCase("INTEGER")) {
					conSubQry = conSubQry + colAlias + condOperOrg + condVal;
					// handle between
				} else if (colDtType.equalsIgnoreCase("DATE")) {
					// handle between
					conSubQry = conSubQry + colAlias + condOperOrg + "'" + condVal
							+ "'";
				} else if (colDtType.equalsIgnoreCase("DATETIME")) {
					// handle between
					conSubQry = conSubQry + colAlias + condOperOrg + "'" + condVal
							+ "'";
				} else if (colDtType.equalsIgnoreCase("DECIMAL")) {
					// handle between
					conSubQry = conSubQry + colAlias + condOperOrg + condVal;
				}

				//log.info("conSubQry at k: " + k + " is: " + conSubQry);
				condQry = condQry + conSubQry;
				if (k >= 0 && k < repDefConddata.size() - 1 && k != repDefConddata.size()) {
					condQry = condQry + ",";
				}

			}
			log.info("final condQry: " + condQry);
			dvFilterStr = condQry;
			}
			else{
			}
			/*}
			else{
			}*/

			/*DataFrameReader dv_data = spark.read().format("jdbc")
					.option("url", dbUrl).option("user", userName)
					.option("password", password).option("dbtable", dvatViewName);

			Dataset<Row> dv_data_ds_load = dv_data.load().cache();*/
			//log.info("previewing cmplt data view data with count : "+dv_data_ds_load.count());
			Dataset<Row> dv_data_ds = null;
			
			/* Finding Date Limits to Filter data view data for Roll back report */
	        //String dateTymVal=DateTime.now().toString();
	        String dtQualAlias="";
	        if(reportTypeName.equalsIgnoreCase("ROLL_BACK_REPORT")){
	        //Formatting it to date format
	                String time=dateTymVal;
	                DateFormat inputFormat = new SimpleDateFormat(
	                        "E MMM dd yyyy HH:mm:ss 'GMT'z", Locale.getDefault());
	                Date date = inputFormat.parse(time);
	                log.info("date: "+date);
	                SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
	                String dateInString=formatter.format(date);
	                log.info("Formatted Date: "+dateInString);
	                
	                DateTimeFormatter localDateFormatter =
	                        DateTimeFormatter.ofPattern("dd-MMM-yyyy");
	                LocalDate startDate=LocalDate.parse(dateInString, localDateFormatter);
	                log.info("startDate: "+startDate);
	                
	                LocalDate dt1=startDate.plusMonths(num);
	                log.info("dt1: "+dt1);
	                LocalDate localDate = LocalDate.parse(dt1.toString(),DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	                LocalDate lastDayOfMonth = localDate.with(TemporalAdjusters.lastDayOfMonth());
	                log.info("lastDayOfMonth: "+lastDayOfMonth);

	       /*  Date Qualifier at Report Definition */
	        /*Dataset<Row> dateQualDefDataSet = spark
	        				.read()
	        				.format("jdbc")
	        				.option("url", dbUrl)
	        				.option("user", userName)
	        				.option("password", password)
	        				.option("dbtable", "t_report_defination")
	        				.load().where("report_id="+reportId+" and data_type='DATE_QUALIFIER'")
	        				.select("ref_type_id","ref_src_id","ref_col_id","data_type","display_name").cache(); 
	                
	        List<Row> dateQualDefList=dateQualDefDataSet.collectAsList();
	        Row dateQualDef=dateQualDefList.get(0);*/
	        ReportDefination dateQualDef=reportDefinationRepository.findByReportIdAndDataType(reportId, "DATE_QUALIFIER");        
	        String refTypeId=dateQualDef.getRefTypeId();
	        Long refSrcId=dateQualDef.getRefSrcId();
	        Long refColId=dateQualDef.getRefColId();
	        String dataType=dateQualDef.getDataType();
	        String dpName=dateQualDef.getDisplayName();
	        String dateQualColName="";
	        String dateDiffQry="";
	        
	        if(dateQualDef!=null && refTypeId!=null && refTypeId.equalsIgnoreCase("DATA_VIEW")){
	            dateQualColName=dpName;
	            Long dateQualId=refColId;
	            /*Dataset<Row> dateQualDvcDS=dataViewColDF.load().where("id="+dateQualId)
	            		.select("ref_dv_name","ref_dv_column","column_name","col_data_type")
	            		.cache();
	            List<Row> dateQualDvcList=dateQualDvcDS.collectAsList();
	            Row dvc=dateQualDvcList.get(0);*/
	            
	            DataViewsColumns dvc=dataViewsColumnsRepository.findOne(dateQualId);
	            
	            String refDv=dvc.getRefDvName();
	            String refDvCol=dvc.getRefDvColumn();
	            String colName=dvc.getColumnName();
	            String colDtType=dvc.getColDataType();
	            
	            if(refDv!=null && refDvCol!=null){
	            Long ftlId=Long.parseLong(refDvCol);
	            
	            /*Dataset<Row> ftlDS=ftlDataDF.load().where("id="+ftlId).select("column_alias").cache();
	            List<Row> ftlList=ftlDS.collectAsList();
	            Row ftl=ftlList.get(0);*/
	            FileTemplateLines ftl=fileTemplateLinesRepository.findOne(ftlId);
	            dtQualAlias=ftl.getColumnAlias();
	            }
	            else{
	            	dtQualAlias=colName;
	            }
	            
	            long daysBetween = ChronoUnit.DAYS.between(startDate, lastDayOfMonth);
	            log.info("daysBetween: "+daysBetween);
	            
	            if(daysBetween>0){
	                log.info("startdate is greater than end date");
	                dateDiffQry=dtQualAlias+" between '"+startDate+"' and '"+lastDayOfMonth+"'";
	            }else if(daysBetween<0){
	                log.info("enddate is greater than startdate");
	                dateDiffQry=dtQualAlias+" between '"+lastDayOfMonth+"' and '"+startDate+"'";
	            }
	            
	           // dv_data_ds=dv_data_ds_load.where(dateDiffQry);
	        }

			log.info("dvFilterStr: " + dvFilterStr);
			/*if (dvFilterStr != null && !(dvFilterStr.isEmpty())) {
				dv_data_ds = dv_data_ds.filter(dvFilterStr);
			}*/
	 }
	        
	        else{
	        	//dv_data_ds=dv_data_ds_load;
	        }

			//dv_data_ds.registerTempTable("dv_data_ds_v");

			/*Dataset<Row> acc_data_ds = spark.read().format("jdbc")
	                .option("url", dbUrl).option("user", userName)
	                .option("password", password)
	                .option("dbtable", "t_accounting_data").load()
	                .where("tenant_id=" + tenantId+ " and original_view_id=" + dataViewId).cache();

	        acc_data_ds.createTempView("acc_data_ds_v");
	        acc_data_ds.registerTempTable("acc_data_ds_v");
	        
	        Dataset<Row> acc_sum_data_ds = spark.read().format("jdbc")
	                .option("url", dbUrl).option("user", userName)
	                .option("password", password)
	                .option("dbtable", "t_accounted_summary").load()
	                .where("tenant_id=" + tenantId+ " and original_view_id=" + dataViewId).cache();

	        acc_sum_data_ds.createTempView("acc_sum_data_ds_v");
	        acc_sum_data_ds.registerTempTable("acc_sum_data_ds_v");


	        Dataset<Row> jrnlHeader_data_ds = spark.read().format("jdbc")
	                .option("url", dbUrl).option("user", userName)
	                .option("password", password)
	                .option("dbtable", "t_journals_header_data").load().where("tenant_id=" + tenantId).cache();

	        jrnlHeader_data_ds.createTempView("jrnlHeader_data_ds_v");
	        jrnlHeader_data_ds.registerTempTable("jrnlHeader_data_ds_v");
			DataFrameReader jrnl_data = spark.read().format("jdbc")
					.option("url", dbUrl).option("user", userName)
					.option("password", password).option("dbtable", "t_je_lines");
			Dataset<Row> jrnl_data_ds = jrnl_data.load().cache();

			if (jrnlFilterStr != null && !(jrnlFilterStr.isEmpty())) {
				jrnl_data_ds = jrnl_data_ds.filter(jrnlFilterStr);
			}
			jrnl_data_ds.createTempView("jrnl_data_ds_v");
			jrnl_data_ds.registerTempTable("jrnl_data_ds_v");*/

			String dvAliasName = "v";
			String viewColsQuery = "";

		        String aggr="";
		        String aggregator="";
		        String buckAgg="";
		        String aggQulaCol="";
		        
		        /*Dataset<Row> dvcQualDS=dataViewColDF.load().where("data_view_id="+dataViewId+" and qualifier='AMOUNT'")
		        		.select("ref_dv_column").cache();
		        List<Row> dvcQualList=dvcQualDS.collectAsList();
		        Row dvcqulaData=dvcQualList.get(0);*/
		        DataViewsColumns dvcqulaData=dataViewsColumnsRepository.findByDataViewIdAndQualifier(dataViewId, "AMOUNT");
		        log.info("dvcqulaData: "+dvcqulaData);
		        String amoutQualifierCol=dvcqulaData.getRefDvColumn();
		        log.info("amoutQualifierCol: "+amoutQualifierCol);
		        /*Dataset<Row> dvcQualifierDS=dataViewColDF.load().where("data_view_id="+dataViewId+" and ref_dv_column='"+amoutQualifierCol+"'")
		        		.select("ref_dv_column","column_name").cache();*/
		        
		        log.info("PREVIEWING dvcQualifierDS:");
		        //dvcQualifierDS.show();
		        DataViewsColumns dvcQualData=dataViewsColumnsRepository.findByDataViewIdAndRefDvColumn(dataViewId, amoutQualifierCol);
		        log.info("dvcQualData: "+dvcQualData);
		        /*List<Row> dvcQualifierList=dvcQualifierDS.collectAsList();
		        Row dvcQualData=dvcQualifierList.get(0);*/
		        /*log.info("dvcQualData: "+dvcQualData);
		        log.info("dvcQualData.getString(0): "+dvcQualData.getString(0));
		        log.info("dvcQualData.getString(1): "+dvcQualData.getString(1));*/
		        String dvcDpName=dvcQualData.getColumnName();
		        log.info("dvcDpName: "+dvcDpName);
		        String qulaCol="";
		        if(dvcQualData.getRefDvColumn()!=null){
		        Long ftlId=Long.parseLong(dvcQualData.getRefDvColumn());
		        /*Dataset<Row> ftlDataList=ftlDataDF.load().where("id=" + ftlId).select("column_alias").cache();
				List<Row> ftlDataValList = ftlDataList.collectAsList();
				Row ftl = ftlDataValList.get(0);*/
		        FileTemplateLines ftl=fileTemplateLinesRepository.findOne(ftlId);
		        qulaCol=ftl.getColumnAlias();
		        }
		        else{
		        	qulaCol=dvcDpName;
		        }
		        log.info("qulaCol: "+qulaCol);
		        String aggRefType="";
		        
		        if(reportTypeName.equalsIgnoreCase("AGING_REPORT") || reportTypeName.equalsIgnoreCase("ROLL_BACK_REPORT") ){
		        	
		        /*Dataset<Row> repDefDataDS=repDefConddataDF.load().where("report_id="+reportId+" and data_type='AGGREGATOR'")
		        		.select("display_name","ref_type_id","ref_col_id","ref_src_id").cache();
		        List<Row> repDefDataList=repDefDataDS.collectAsList();
		        Row aggrgatorDef=repDefDataList.get(0);*/
		        ReportDefination aggrgatorDef=reportDefinationRepository.findByReportIdAndDataType(reportId,"AGGREGATOR");	
		        log.info("aggrgatorDef:"+aggrgatorDef+"@@@@@");
		        if(aggrgatorDef!=null && !(aggrgatorDef.equals("")))
		        aggregator=aggrgatorDef.getDisplayName();
		        log.info("aggregator: "+aggregator);
		        aggRefType=aggrgatorDef.getRefTypeId();
		        
		        if(aggregator!=null && !(aggregator.isEmpty()) && !(aggregator.equalsIgnoreCase(""))){
		            Long aggColId=aggrgatorDef.getRefColId();
		            String srcType=aggrgatorDef.getRefTypeId();
		            if(srcType.equalsIgnoreCase("DATA_VIEW")){
		            Long aggSrcId=aggrgatorDef.getRefSrcId();
		            log.info("srcType: "+srcType+" aggColId: "+aggColId+" aggSrcId: "+aggSrcId);
		            
		           /* Dataset<Row> aggDvcDS=dataViewColDF.load().where("data_view_id="+aggSrcId+" and id='"+aggColId+"'")
		            		.select("ref_dv_column","column_name").cache();
		            //aggDvcDS.show();
			        List<Row> aggDvList=aggDvcDS.collectAsList();
			        Row aggreDvcQualData=aggDvList.get(0);*/
		            DataViewsColumns aggreDvcQualData=dataViewsColumnsRepository.findOne(aggColId);
		            log.info("aggreDvcQualData: "+aggreDvcQualData);
		            Long aggrFtlId=Long.parseLong(aggreDvcQualData.getRefDvColumn());
		            String aggrDvcDpName=aggreDvcQualData.getColumnName();
		            
		            /*Dataset<Row> aggreFtlDS=ftlDataDF.load().where("id=" + aggrFtlId).select("column_alias").cache();
					List<Row> aggreFtlList = aggreFtlDS.collectAsList();
					Row aggreFtl = aggreFtlList.get(0);*/
		            FileTemplateLines aggreFtl=fileTemplateLinesRepository.findOne(aggrFtlId);
		            aggQulaCol=aggreFtl.getColumnAlias();
		            }
		        }
		        
		        }
		        buckAgg=aggregator;
		        log.info("buckAgg: "+buckAgg);
		        log.info("aggQulaCol: "+aggQulaCol);
		        
		        	viewColsQuery = "select " + dvAliasName + ".scrIds as masterid , "
		                + dvAliasName + ".fileDate as fileDate, ";
		        
		        log.info("viewColsQuery "+viewColsQuery);

			List<String> groupByColList=new ArrayList<String>();
			
			/*Dataset<Row> repDefdata = spark
					.read()
					.format("jdbc")
					.option("url", dbUrl)
					.option("user", userName)
					.option("password", password)
					.option("dbtable", "t_report_defination")
					.load()
					.where("report_id=" + reportId + " and ref_type_id='DATA_VIEW' and layout_val is not null ")
					.select("display_name", "ref_col_id","goup_by","layout_val").orderBy("layout_val").cache();

			List<Row> repDefColList = repDefdata.collectAsList();*/
			//List<ReportDefination> repDefColList=reportDefinationRepository.fetchByReportIdOrderByLayoutVal(reportId);
			List<ReportDefination> repDefColList=reportDefinationRepository.fetchRepDefByRefTypeAndLayoutVal(reportId, "DATA_VIEW");
			String subQry = "";
			for (int r = 0; r < repDefColList.size(); r++) {
				ReportDefination refDef = repDefColList.get(r);
				log.info("refDef: "+refDef);
				String dispName = refDef.getDisplayName();
				log.info("dispName: "+dispName);
				Long refColId = refDef.getRefColId();
				log.info("refColId: "+refColId);
				/*Dataset<Row> dataViewColdata = spark
						.read()
						.format("jdbc")
						.option("url", dbUrl)
						.option("user", userName)
						.option("password", password)
						.option("dbtable", "t_data_views_columns")
						.load()
						.where("id=" + refColId)
						.select("col_data_type", "column_name", "ref_dv_column",
								"ref_dv_name").cache();

				List<Row> dvcoldataList = dataViewColdata.collectAsList();

				Row dvcolData = dvcoldataList.get(0);*/
				DataViewsColumns dvcolData=dataViewsColumnsRepository.findOne(refColId);
				log.info("dvcolData: "+dvcolData);
				String colDtType = dvcolData.getColDataType();
				String dvColId = dvcolData.getRefDvColumn();
				String refDvId = dvcolData.getRefDvName();
				String colAlias="";
				
				if(dvColId!=null && refDvId!=null){
				/*Dataset<Row> ftlDataListDS =ftlDataDF.load()
						.where("id=" + dvColId).select("column_alias").cache();

				Row ftlData = ftlDataListDS.collectAsList().get(0);*/
				
				FileTemplateLines ftlData=fileTemplateLinesRepository.findOne(Long.parseLong(dvColId));	
				 colAlias = ftlData.getColumnAlias();
				}
				else{
					colAlias=dvcolData.getColumnName();
				}
				
				 if(!(colDtType.equalsIgnoreCase("DECIMAL"))){
					 decimalCol=true;
					 grpColList.add(dispName);
				 }
				layoutColList.add(dispName);
				subQry = dvAliasName + "." + colAlias + " as " +"`"+ dispName+"`";

				if (r >= 0 && r < repDefColList.size() - 1
						&& r != repDefColList.size()) {
					subQry = subQry + " , ";
				}
				viewColsQuery = viewColsQuery + subQry;
			}

			log.info("viewColsQuery after repor defination columns: "
					+ viewColsQuery);
			
			/*Dataset<Row> repParamdata = spark.read().format("jdbc")
					.option("url", dbUrl).option("user", userName)
					.option("password", password)
					.option("dbtable", "t_report_parameters").load()
					.where("report_id=" + reportId + " and ref_typeid='DATA_VIEW'")
					.select("display_name", "ref_col_id").cache();
			List<Row> repParamColList = repParamdata.collectAsList();*/
			
			List<ReportParameters> repParamColList=reportParametersRepository.findByReportIdAndRefTypeid(reportId, "DATA_VIEW");

			if (viewColsQuery != null && !(viewColsQuery.endsWith("fileDate, "))) {
				log.info("in if for existance of report def");
				viewColsQuery = viewColsQuery + " , ";
			}

			for (int r = 0; r < repParamColList.size(); r++) {
				ReportParameters refParam = repParamColList.get(r);
				String repParamQry = "";
				String dispName = refParam.getDisplayName();
				Long refColId = refParam.getRefColId();
				log.info("dispName: "+dispName+" refColId: "+refColId);

				/*Dataset<Row> repDefDataDS=repDefConddataDF.load()
						.where("report_id="+reportId+" and ref_type_id='DATA_VIEW' and ref_col_id="+refColId).cache();
				List<Row> repDefList=repDefDataDS.collectAsList();
				Row repDefData=null;*/
				
				ReportDefination repDefData=reportDefinationRepository.findByReportIdAndRefTypeIdAndRefColId(reportId, "DATA_VIEW", refColId);
				//ReportDefination repDefData=repDefList.get(0);
				/*if(repDefList!=null && repDefList.size()>0)
				repDefData=repDefList.get(0);*/

				if (repDefData != null) {
				} else {
					/*Dataset<Row> dataViewColdata = spark
							.read()
							.format("jdbc")
							.option("url", dbUrl)
							.option("user", userName)
							.option("password", password)
							.option("dbtable", "t_data_views_columns")
							.load()
							.where("id=" + refColId)
							.select("col_data_type", "column_name",
									"ref_dv_column", "ref_dv_name").cache();

					List<Row> dvcoldataList = dataViewColdata.collectAsList();*/
					
					DataViewsColumns dvcolData=dataViewsColumnsRepository.findOne(refColId);

					//Row dvcolData = dvcoldataList.get(0);
					String colDtType = dvcolData.getColDataType();
					String dvColId = dvcolData.getRefDvColumn(); // ftl id
					String refDvId = dvcolData.getRefDvName(); // ft id

					/*Dataset<Row> ftlDataDS = ftlDataDF.load()
							.where("id=" + dvColId).select("column_alias").cache();

					Row ftlData = ftlDataDS.collectAsList().get(0);*/
					FileTemplateLines ftlData=fileTemplateLinesRepository.findOne(Long.parseLong(dvColId));
					String colAlias = ftlData.getColumnAlias();
					repParamQry = dvAliasName + "." + colAlias + " as " + "`"+dispName+"`";

					log.info("repParamQry b4 at r: "+r+" is: "+repParamQry);
					if (r >= 0 && r < repParamColList.size() - 1
							&& r != repParamColList.size()-1) {
						repParamQry = repParamQry + " , ";
					}
					log.info("repParamQry aftr at r: "+r+" is: "+repParamQry);
					log.info("viewColsQuery b4 at r: "+r+" is: "+viewColsQuery);
					viewColsQuery = viewColsQuery + repParamQry;
					log.info("viewColsQuery aftr at r: " + r + " is: " + viewColsQuery);
				}
			}

			log.info("viewColsQuery after repor param columns: " + viewColsQuery);
			
			 if(viewColsQuery.endsWith(" , ")){
		            log.info("removing comma");
		            int commaIndex=viewColsQuery.lastIndexOf(",");
		            viewColsQuery=viewColsQuery.substring(0,commaIndex-1);
		        }

			log.info("final viewColsQuery: " + viewColsQuery);

			String updQuery = "";
			String sysColQry = "";
			String tabAlias = "";
			String tabName = "";
			String origColName = "";

			/*Dataset<Row> repDefQryDS=repDefConddataDF.load().where("report_id="+reportId+" and ref_type_id='FIN_FUNCTION'")
					.select("ref_col_id","display_name","data_type","layout_val").sort("layout_val").cache();
			//repDefQryDS.show();
			List<Row> repDefQuryDataList=repDefQryDS.collectAsList();*/
			List<ReportDefination> repDefQuryDataList=reportDefinationRepository.findByReportIdAndRefTypeId(reportId, "FIN_FUNCTION");
			for (int h = 0; h < repDefQuryDataList.size(); h++) {
				String sysColSubQry = "";
				ReportDefination repDef = repDefQuryDataList.get(h);
				log.info("repDef: "+repDef);
				Long refColId = repDef.getRefColId();
				String dpName = repDef.getDisplayName();
				log.info("dpName: "+dpName);
				String dataType=repDef.getDataType();
				log.info("refColId: "+refColId+" dataType: "+dataType);

				/*Dataset<Row> lCodeDS=lookupCodeDS.where("id="+refColId).select("secure_attribute_1").cache();
				//lCodeDS.show();
				List<Row> lCodeList=lCodeDS.collectAsList();
				Row lCode=lCodeList.get(0);*/
				LookUpCode lCode=lookUpCodeRepository.findOne(refColId);
				log.info("lCode: "+lCode);
				String funName = lCode.getSecureAttribute1();
				log.info("funName: "+funName);
				/*Dataset<Row> funCodeDS=lookupCodeDS.where("look_up_type='FIN_FUNCTION' and look_up_code='"+funName+"'")
						.select("description","secure_attribute_1","secure_attribute_2","meaning","look_up_code").cache();
				List<Row> funCodeList=funCodeDS.collectAsList();
				Row funCode=funCodeList.get(0);*/
				LookUpCode funCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("FIN_FUNCTION", funName, tenantId);
				log.info("funCode: "+funCode);
				if(funCode.getDescription()!=null && !(funCode.getDescription().isEmpty())){
					log.info("funCode.getDescription(): "+funCode.getDescription());
					if(funCode.getDescription().equalsIgnoreCase("TABLE")){
				tabName = funCode.getSecureAttribute1();
				origColName = funCode.getSecureAttribute2();
				log.info("tabName: "+tabName+" origColName: "+origColName);

				if (tabName.equalsIgnoreCase("t_accounting_data"))
					tabAlias = "a";
				else if (tabName.equalsIgnoreCase("t_journals_header_data"))
					tabAlias = "jhd";
				else if (tabName.equalsIgnoreCase("t_je_lines")
						&& origColName.contains("credit"))
					tabAlias = "jcr";
				else if (tabName.equalsIgnoreCase("t_je_lines")
						&& origColName.contains("debit"))
					tabAlias = "jdr";
				else if (tabName.equalsIgnoreCase("t_reconciliation_result"))
					tabAlias = "r";
				if(!(dataType.equalsIgnoreCase("DECIMAL"))){
					decimalCol=true;
					grpColList.add(dpName);
				}
				layoutColList.add(dpName);
				sysColSubQry = tabAlias + "." + origColName + " as " + "`"+dpName+"`";

				sysColQry = sysColQry + sysColSubQry;

				if (h >= 0 && h != repDefQuryDataList.size() - 1) {
					sysColQry = sysColQry + " , ";
				}
				}
				else if(funCode.getDescription().equalsIgnoreCase("FUNCTION")){
				
					if(funCode.getMeaning()!=null && !(funCode.getMeaning().isEmpty())){
						if(funCode.getLookUpCode().equalsIgnoreCase("getReconStatus")){
							grpColList.add("as_of_recon_status");
							layoutColList.add("as_of_recon_status");
						}
						if(funCode.getLookUpCode().equalsIgnoreCase("getAccountingStatus")){
							grpColList.add("as_of_acc_status");
							layoutColList.add("as_of_acc_status");
						}
					}
				}
				}
			}

			log.info("layoutColList aftr repDef: "+layoutColList);
			log.info("sysColQry aftr repDef loop: "+sysColQry);
			
			/*Dataset<Row> repParamDataDS=repDataDF.load().where("report_id="+reportId+" and ref_typeid='FIN_FUNCTION'")
					.select("ref_col_id","display_name").cache();
			List<Row> repParamQuryDataList=repParamDataDS.collectAsList();*/
			List<ReportParameters> repParamQuryDataList=reportParametersRepository.findByReportIdAndRefTypeid(reportId, "FIN_FUNCTION");
			for (int h = 0; h < repParamQuryDataList.size(); h++) {
				String sysColSubQry = "";
				ReportParameters repPrm = repParamQuryDataList.get(h);
				Long refColId = repPrm.getRefColId();
				String dpName = repPrm.getDisplayName();

				/*Dataset<Row> repDefDataDS=repDefConddataDF.load().where("report_id="+reportId+" and ref_type_id='FIN_FUNCTION'")
						.select("display_name","ref_type_id","ref_col_id","ref_src_id").cache();
		        List<Row> repDefDataList=repDefDataDS.collectAsList();
		        Row repDefData=repDefDataList.get(0);*/
				List<ReportDefination> repDefData=reportDefinationRepository.findByReportIdAndRefTypeId(reportId, "FIN_FUNCTION");
				if (repDefData == null) {

					if (h == 0 && sysColQry != null && !(sysColQry.isEmpty()))
						sysColQry = sysColQry + " , ";
					/*Dataset<Row> lCodeDS=lookupCodeDS.where("id="+refColId).select("secure_attribute_1").cache();
					List<Row> lCodeList=lCodeDS.collectAsList();
					Row lCode=lCodeList.get(0);*/
					
					LookUpCode lCode=lookUpCodeRepository.findOne(refColId);
					String funName = lCode.getSecureAttribute1();
					/*Dataset<Row> funCodeDS=lookupCodeDS.where("look_up_type='FIN_FUNCTION' and look_up_code='"+funName+"'")
							.select("secure_attribute_1","secure_attribute_2").cache();
					List<Row> funCodeList=funCodeDS.collectAsList();
					Row funCode=funCodeList.get(0);*/
					LookUpCode funCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("FIN_FUNCTION", funName, tenantId);
					tabName = funCode.getSecureAttribute1();
					origColName = funCode.getSecureAttribute2();

					if (tabName.equalsIgnoreCase("t_accounting_data"))
						tabAlias = "a";
					else if (tabName.equalsIgnoreCase("t_journals_header_data"))
						tabAlias = "jhd";
					else if (tabName.equalsIgnoreCase("t_je_lines")
							&& origColName.contains("credit"))
						tabAlias = "jcr";
					else if (tabName.equalsIgnoreCase("t_je_lines")
							&& origColName.contains("debit"))
						tabAlias = "jdr";
					else if (tabName.equalsIgnoreCase("t_reconciliation_result"))
						tabAlias = "r";
					sysColSubQry = tabAlias + "." + origColName + " as " + "`"+dpName+"`";

					sysColQry = sysColQry + sysColSubQry;

					if (h >= 0 && h != repDefQuryDataList.size() - 1) {
						sysColQry = sysColQry + " , ";
					}
				}
			}

			log.info("final sysColQry: " + sysColQry);
			log.info("grpColList: "+grpColList);
			log.info("grpColList: "+grpColList.size());
			
			String grpByColQry="";
			if(groupByColList!=null && !(groupByColList.isEmpty())){
				grpByColQry = groupByColList.stream().map(Object::toString)
	                    .collect(Collectors.joining(", "));
			}
			
			log.info("grpByColQry: "+grpByColQry);
			
			 Column[] grpColArr = new Column[grpColList.size()];
			for(int h=0;h<grpColList.size();h++){
				String grpCol=grpColList.get(h);
				Column colarryVal=new Column(grpCol);
				grpColArr[h]=colarryVal;
			}
			
			
			/*  Applying Buckets */
			String bucketsQry="";
			Boolean othersFlag=true; 
			if(reportTypeName.equalsIgnoreCase("AGING_REPORT")){
	        String bucketType="";
	        if(bucketId!=null && bucketId>0){
	        	/*DataFrameReader bucketDF= spark.read().format("jdbc")
	    				.option("url", dbUrl).option("user", userName)
	    				.option("password", password)
	    				.option("dbtable", "t_bucket_list");
	        	Dataset<Row> bucketListDS=bucketDF.load().where("id="+bucketId).select("jhi_type").cache();
	        	List<Row> bucketList=bucketListDS.collectAsList();
	        	Row bucketListData=bucketList.get(0);*/
	        	BucketList bucketListData=bucketListRepository.findOne(bucketId);
	            bucketType=bucketListData.getType();
	            
	            /*DataFrameReader bucketDetDF= spark.read().format("jdbc")
	    				.option("url", dbUrl).option("user", userName)
	    				.option("password", password)
	    				.option("dbtable", "t_bucket_details");
	            Dataset<Row> bucketDetDS=bucketDetDF.load().where("bucket_id="+bucketId).select("id","from_value","to_value").cache();
	            List<Row> bucketDetailsList=bucketDetDS.collectAsList();*/
	            List<BucketDetails> bucketDetailsList=bucketDetailsRepository.findByBucketId(bucketId);
	            if(bucketDetailsList!=null){
	                bucketsQry=" ";
	            }
	            for(int j=0;j<bucketDetailsList.size();j++){
	                int buckVal=j+1;
	                BucketDetails bucketDet=bucketDetailsList.get(j);
	                Long bucketDetId =bucketDet.getId();
	                if(bucketDet!=null)
	                {
	                    Integer frmVal=0;
	                    Integer toVal=0;
	                    Integer from=null;
	                    Integer to=null;
	                    if(bucketDet.getFromValue()!=null)
	                    from=bucketDet.getFromValue();
	                    if(bucketDet.getToValue()!=null)
	                    to=bucketDet.getToValue();
	                    log.info("from: "+from+" to: "+to);
	                    String bucketName=getBucketName(reportId, "AGING", bucketId, bucketDetId);
	                    if(from!=null && to!=null){
	                    if(from>to){
	                        
	                        frmVal=to;
	                        toVal=from;
	                    }
	                    else{
	                        frmVal=from;
	                        toVal=to;
	                    }
	                    
	                    
	                    if(bucketType.equalsIgnoreCase("AGE")){
	                    bucketsQry=bucketsQry+" when  DATEDIFF( '"+dateTymVal+"' ,`v`.`fileDate`) between "+frmVal+" and "+toVal+" then '"+bucketName+"'";
	                    }
	                    else if(bucketType.equalsIgnoreCase("VALUE")){
	                        bucketsQry=bucketsQry+" when "+aggQulaCol+" between "+frmVal+" and "+toVal+" then '"+bucketName+"'";
	                    }
	                    
	                }
	                    else{
	                    	othersFlag=false;
	                    	log.info("any one of the bucket limits are null");
	                    	if(bucketType.equalsIgnoreCase("AGE")){
	                    		if(to==null){
	                    			bucketsQry=bucketsQry+" when  DATEDIFF( '"+dateTymVal+"' ,`v`.`fileDate`) > "+from+" then '"+bucketName+"'";
	                    		}
	                    		if(from==null){
	                    			bucketsQry=bucketsQry+" when  DATEDIFF( '"+dateTymVal+"' ,`v`.`fileDate`) < "+to+" then '"+bucketName+"'";
	                    		}
	                    	}
	                    	if(bucketType.equalsIgnoreCase("VALUE")){
	                    	if(to==null){
	                    		bucketsQry=bucketsQry+" when "+aggQulaCol+"> "+from+" then '"+bucketName+"'";
	                    	}
	                    	if(from==null){
	                    		bucketsQry=bucketsQry+" when "+aggQulaCol+"< "+to+" then '"+bucketName+"'";
	                    	}
	                    	}
	                    }
	                }
	            }
	            log.info("bucket query b4 others : "+bucketsQry);
	            bucketsQry=bucketsQry+" else 'Others' end as bucket";
	        }
	        
	        if(bucketType.equalsIgnoreCase("AGE")){
	        bucketsQry="case when  DATEDIFF( '"+dateTymVal+"', `v`.`fileDate`) is null then 'Others' "+bucketsQry;
	        }
	        else if(bucketType.equalsIgnoreCase("VALUE")){
	            bucketsQry="case when "+aggQulaCol+" is null then 'Others' "+bucketsQry;
	        }
	        log.info("final bucket query: "+bucketsQry);
			}
			else if(reportTypeName.equalsIgnoreCase("ROLL_BACK_REPORT")){
				
				bucketsQry=" MONTH( `v`.`"+dtQualAlias+"`) as dateQualMnth,"
		                +"YEAR( `v`.`"+dtQualAlias+"`) as dateQualYear,"
		                +"concat (  case when month("+dtQualAlias+") = '01' then 'JAN' "
		                +" when  month("+dtQualAlias+") = '02' then 'FEB' "
		                +" when  month("+dtQualAlias+") = '03' then 'MAR' "
		                +" when  month("+dtQualAlias+") = '04' then 'APR' "
		                +" when  month("+dtQualAlias+") = '05' then 'MAY' "
		                +" when  month("+dtQualAlias+") = '06' then 'JUN'"
		                +" when  month("+dtQualAlias+") = '07' then 'JUL'"
		                +" when  month("+dtQualAlias+")= '08' then 'AUG'"
		                +" when  month("+dtQualAlias+") = '09' then 'SEP'"
		                +" when month("+dtQualAlias+") = '10' then 'OCT'"
		                +" when  month("+dtQualAlias+") = '11' then 'NOV'"
		                +" when  month("+dtQualAlias+") = '12' then 'DEC'" 
		                +" END,"+"\""+"-"+"\", year("+dtQualAlias+")) AS recon_month";
				
			}
			
			if(bucketsQry!=null && !(bucketsQry.isEmpty()) && !(bucketsQry.equalsIgnoreCase(""))){
	            bucketsQry=bucketsQry+" , ";
	        }
			
			if(sysColQry!=null && !(sysColQry.isEmpty()) && !(sysColQry.equalsIgnoreCase(""))){
				sysColQry=sysColQry+" , ";
			}
			
			log.info("sysColQry b4 comma check: "+sysColQry);
			if(sysColQry.endsWith(",  , ")){
		 		int lastComma=sysColQry.lastIndexOf(",");
		 		log.info("lastIndex of comma: "+lastComma);
		 		
		 		StringBuilder sb = new StringBuilder(sysColQry);
		 		sb.setCharAt(lastComma, ' ');
		 		sysColQry = sb.toString();
		 		log.info("part2 after replacing comma: "+sysColQry);
		 	}
			else{
				log.info("sysColQry doesnt end with commas");
			}
			
			log.info("bucketsQry b4 updQuery: "+bucketsQry);
	        log.info("sysColQry b4 updQuery: "+sysColQry);
	        
	        String query =" ";
	        Dataset<Row> data=null;

	        /*if(reportTypeName.equalsIgnoreCase("ACCOUNT_BALANCE_REPORT")){
	        	//updQuery="select  from balances_data_ds_v ";@@@@@@@@@
	        	HashMap balancesInfo=ReportsService.getBalancesQuery(spark, dataViewId,reportId);
	        	query=balancesInfo.get("query").toString();
	        	updQuery=" select "+query+" from balances_data_ds_v";
	        	log.info("updQuery: "+updQuery);
	        	layoutColList=(List<String>) balancesInfo.get("layoutColList");
	        	log.info("layoutColList in balances report: "+layoutColList);
	        	
	        }*/
	        //else{
			query = "`r`.`original_view_id` AS `viewid`, `r`.`reconciliation_rule_group_id` AS `rec_rule_group`, "
					+ " `r`.`reconciled_date` AS `reconciled_date`,  IF(`r`.`reconciliation_rule_group_id` is not null, 'Reconciled', 'Un-Reconciled') AS `recon_status`, "
					+ " DATEDIFF( `v`.`fileDate`, '"+ dateTymVal+"' ) as `rule_age`,"
					+ " `a`.`status` AS `acc_status`, "
					+ " a.acct_group_id AS acc_rule_Group, "
					+bucketsQry
					+ sysColQry
					+ " jcr.credit_amount As entered_cr_amt, jcr.accounted_credit As acc_cr_amt, "
					+" jdr.debit_amount AS entered_dr_amt,  jdr.accounted_debit AS acc_dr_amt, jdr.code_combination AS dr_code_combination,"
					+ "jcr.code_combination As cr_code_combination, jcr.created_date As journal_date,"
					+ " IF(`r`.`reconciled_date` is not null and `r`.`reconciled_date` <= '"
					+ dateTymVal
					+ "' , 'Reconciled', 'Un-Reconciled') AS `as_of_recon_status`, "
					+ " IF(jcr.created_date is not null and jcr.created_date <= '"
					+ dateTymVal
					+ "' , 'Accounted', 'Un-Accounted') AS `as_of_acc_status`, "
					+ " jdr.je_batch_id AS batch_id"
					+ " FROM "
					+ "(select * from  "
					+ " dv_data_ds_v) `v` "
					+ " LEFT OUTER JOIN recon_data_ds_v `r`  on ( `r`.`original_row_id` = `v`.`scrIds` ) "
					+ "  LEFT OUTER JOIN (select distinct tenant_id,original_row_id,acct_group_id,acct_rule_id,original_view_id,ad.status "
					+ " ,source_ref,ledger_ref,category_ref,currency_ref,amount_col_id,line_type_id,line_type_detail " // added
					+ "  from acc_data_ds_v ad where status = 'Accounted' and original_view_id="
					+ dataViewId
					+ ") `a` "
					+ "   on (`a`.`original_row_id` = `v`.`scrIds`)"
					+ " LEFT OUTER JOIN "
					
					+" (select distinct je.row_id,je.credit_amount, je.accounted_credit  , je.code_combination, je.created_date "
					+" from jrnl_data_ds_v je JOIN jrnlHeader_data_ds_v jh  where je.je_batch_id = jh.id and credit_amount is not null) jcr  on (`v`.`scrIds` = `jcr`.`row_id`) "	
					
					+" LEFT OUTER JOIN (select distinct je.row_id,je.currency, je.debit_amount, je.accounted_debit, je.code_combination, je.je_batch_id, jh.je_batch_name "
					+" from jrnl_data_ds_v je JOIN jrnlHeader_data_ds_v jh  where je.je_batch_id = jh.id AND debit_amount is not null ) jdr "
					
					+ "on (`v`.`scrIds` = `jdr`.`row_id`)";
	        	
	        	/*query = "`r`.`original_view_id` AS `viewid`, `r`.`reconciliation_rule_group_id` AS `rec_rule_group`, "
	    				+ " `r`.`reconciled_date` AS `reconciled_date`,  IF(`r`.`reconciliation_rule_group_id` is not null, 'Reconciled', 'Un-Reconciled') AS `recon_status`, "
	    				+ " DATEDIFF( `v`.`fileDate`, '"+ dateTymVal+"' ) as `rule_age`,"
	    				+ " `a`.`status` AS `acc_status`, "
	    				+ " a.acct_group_id AS acc_rule_Group, "
	    				+bucketsQry
	    				+ sysColQry
	    				+ " jcr.credit_amount As entered_cr_amt, jcr.accounted_credit As acc_cr_amt, "
	    				+" jdr.debit_amount AS entered_dr_amt,  jdr.accounted_debit AS acc_dr_amt, jdr.code_combination AS dr_code_combination,"
	    				+ "jcr.code_combination As cr_code_combination, jcr.created_date As journal_date,"
	    				+ " IF(`r`.`reconciled_date` is not null and `r`.`reconciled_date` <= '"
	    				+ dateTymVal
	    				+ "' , 'Reconciled', 'Un-Reconciled') AS `as_of_recon_status`, "
	    				+ " IF(jcr.created_date is not null and jcr.created_date <= '"
	    				+ dateTymVal
	    				+ "' , 'Accounted', 'Un-Accounted') AS `as_of_acc_status`, "
	    				+ " jdr.je_batch_id AS batch_id"
	    				+ " FROM "
	    				+ "(select * from  "
	    				+ " dv_data_ds_v) `v` "
	    				+ " LEFT OUTER JOIN recon_data_ds_v `r`  on ( `r`.`original_row_id` = `v`.`scrIds` ) "
	    				+ "  LEFT OUTER JOIN "
	    				
	    				
	    				+ " (select distinct tenant_id,original_row_id,acct_group_id,acct_rule_id,original_view_id,ad.status "
	    				+ " ,source_ref,ledger_ref,category_ref,currency_ref,amount_col_id,line_type_id,line_type_detail " // added
	    				+ "  from acc_data_ds_v ad where status = 'Accounted' and original_view_id="
	    				+ dataViewId
	    				+ ") `a` "
	    				
	    				+" (select distinct tenant_id,original_row_id,acct_group_id,acct_rule_id,original_view_id,ad.status  ,source_ref,ledger_ref,category_ref,"
	    				+" currency_ref,amount_col_id,line_type_id,line_type_detail   from acc_data_ds_v ad where status = 'Success' and original_view_id= "+dataViewId
	    				+" and original_row_id in (SELECT row_id FROM acc_sum_data_ds_v where view_id="+dataViewId+" and status='ACCOUNTED'))  a"
	    				
	    				+ "   on (`a`.`original_row_id` = `v`.`scrIds`)"
	    				+ " LEFT OUTER JOIN "
	    				
	    				+" (select distinct je.row_id,je.credit_amount, je.accounted_credit  , je.code_combination, je.created_date "
	    				+" from jrnl_data_ds_v je JOIN jrnlHeader_data_ds_v jh  where je.je_batch_id = jh.id and credit_amount is not null) jcr  on (`v`.`scrIds` = `jcr`.`row_id`) "	
	    				
	    				+" LEFT OUTER JOIN (select distinct je.row_id,je.currency, je.debit_amount, je.accounted_debit, je.code_combination, je.je_batch_id, jh.je_batch_name "
	    				+" from jrnl_data_ds_v je JOIN jrnlHeader_data_ds_v jh  where je.je_batch_id = jh.id AND debit_amount is not null ) jdr "
	    				
	    				+ "on (`v`.`scrIds` = `jdr`.`row_id`)";*/
	        
			updQuery = viewColsQuery + "," + query;
			
			log.info("updQuery: " + updQuery);
			
			String mysqlQuery=updQuery;
	        mysqlQuery=mysqlQuery.replace("dv_data_ds_v", dvatViewName);
	        mysqlQuery=mysqlQuery.replace("recon_data_ds_v", "t_reconciliation_result");
	        mysqlQuery=mysqlQuery.replace("acc_data_ds_v", "t_accounting_data");
	        mysqlQuery=mysqlQuery.replace("jrnl_data_ds_v", "t_je_lines");
	        mysqlQuery=mysqlQuery.replace("jrnlHeader_data_ds_v", "t_journals_header_data");
	        log.info("mysqlQuery: "+mysqlQuery);
	        
	        //}
			 //data = spark.sql(updQuery);
			/*int cnt = data.collectAsList().size();
			log.info("cnt: " + cnt);*/
			//data.show();

			String finQuery = "";
			String reconSatus="";
			String accSatus="";
			
			if (filtersMap != null && !(filtersMap.isEmpty())) {
				
				String conQuery = " where ";
				String conSubQueryFin = "";
					log.info("filters exists");
					if (filtersMap.containsKey("fields")) {
						List<HashMap> filtersList = (List<HashMap>) filtersMap
								.get("fields");
						log.info("fields exists with sz: " + filtersList.size());
						for (int i = 0; i < filtersList.size(); i++) {
							HashMap filterMap = filtersList.get(i);
							log.info("filterMap: " + filterMap);
							String conSubQuery = "";
							String colName="";
							Long refColId = Long.parseLong(filterMap.get(
									"refColId").toString());
							Long refSrcId=Long.parseLong(filterMap.get(
									"rParamSrcId").toString());
							String refType=filterMap.get(
									"refType").toString();
							log.info("refColId: "+refColId+" refSrcId: "+refSrcId+" refType: "+refType);
							/*Dataset<Row> repDefParamdataDS =repDefConddataDF.load()
									.where("report_id="
											+ reportId
											+ " and ref_type_id='"+refType+"' and ref_src_id="+refSrcId+" and ref_col_id="+refColId)
											.select("display_name").cache();
							//repDefParamdataDS.show();
							List<Row> repDefParamdataList;
							Row repDefParamData=null;*/
							/*if(repDefParamdataDS!=null){
							repDefParamdataList=repDefParamdataDS.collectAsList();
							if(repDefParamdataList!=null && repDefParamdataList.size()>0){
							repDefParamData=repDefParamdataList.get(0);
							log.info("repDefParamData: "+repDefParamData);
							}
							}*/
							ReportDefination repDefParamData=null;
							ReportParameters reportPar=null;
							/*Dataset<Row> repParamDS=repDataDF.load()
									.where("report_id = " + reportId + " and ref_src_id="
											+ refSrcId + " and ref_typeid='"+refType+"' and ref_col_id="+refColId)
											.select("display_name","ref_col_id").cache();
							List<Row> repParamDSList=repParamDS.collectAsList();
							Row repParamData=repParamDSList.get(0);*/
							//log.info("repParamData: "+repParamData);
							/*if(repDefParamData!=null){
								
							}
							else{
								repDefParamData=reportParametersRepository.findByReportIdAndRefTypeidAndRefSrcIdAndRefColId(reportId, refType, refSrcId, refColId);
							}*/
							repDefParamData=reportDefinationRepository.findByReportIdAndRefTypeIdAndRefSrcIdAndRefColId(reportId, refType, refSrcId, refColId);
							if(repDefParamData!=null){
								colName=repDefParamData.getDisplayName();
							}
							if(repDefParamData==null){
								reportPar=reportParametersRepository.findByReportIdAndRefTypeidAndRefSrcIdAndRefColId(reportId, refType, refSrcId, refColId);
								colName=reportPar.getDisplayName();
							}
							//repDefParamData=reportParametersRepository.findByReportIdAndRefTypeidAndRefSrcIdAndRefColId(reportId, refType, refSrcId, refColId);
								/*if(repDefParamData!=null){
									colName=repDefParamData.getString(0);
									//log.info("colName from dataview: "+colName);
								}
								else{
									colName=repParamData.getString(0);
									//log.info("colName rep param: "+colName);
								}*/
							//log.info("colName: "+colName);
							String selType = filterMap.get("fieldType").toString();
							//log.info("selType: " + selType);
							if (selType.equalsIgnoreCase("MULTI_SELECT_LOV")
									|| selType
											.equalsIgnoreCase("SINGLE_SELECT_LOV")
									|| selType.equalsIgnoreCase("SINGLE_SELECTION")
									|| selType.equalsIgnoreCase("TEXT")) {
								String blankQuery="";
								String selValData = "";
								String selValVar = filterMap.get("selectedValue")
										.toString();
								log.info("selValVar: " + selValVar);
								if(selValVar!=null && !(selValVar.isEmpty()) && !(selValVar.equalsIgnoreCase("[]"))){
								selValVar = selValVar.replace("[", "");
								selValVar = selValVar.replace("]", "");
								List<String> selValList = new ArrayList<String>(
										Arrays.asList(selValVar.split(",")));
								for (int k = 0; k < selValList.size(); k++) {
									String selVal = selValList.get(k);
									selVal = selVal.trim();
									if(selVal.equalsIgnoreCase("(Blank)")){
										blankQuery="`"+colName+"` is null";
										log.info("blankQuery: "+blankQuery);
									}
									else selValData = selValData + "'" + selVal + "'";
									if (k >= 0 && k < selValList.size() - 1) {
										if(selValData!=null && !(selValData.isEmpty()))
										selValData = selValData + ",";
									}
								}
								if (selValData != null && !(selValData.isEmpty())) {
									if (selValData.endsWith(",")) {
										int indx = selValData.lastIndexOf(",");
										selValData = selValData.substring(0, indx);
										log.info("selValData after removing comma: "
												+ selValData);
									}
									conSubQuery = conSubQuery + "`"+colName +"`" + " in ("
											+ selValData + ") ";
								}
								else{
									
								}
								if(selValList.size()>1 && blankQuery!=null && !(blankQuery.isEmpty())){
									conSubQuery=" ("+conSubQuery+" or "+blankQuery+") ";//(goup_by in (1) or goup_by is null)
								}
								if(conSubQuery==null || (conSubQuery!=null && conSubQuery.isEmpty()) || conSubQuery.equalsIgnoreCase("")){
									if(blankQuery!=null && !(blankQuery.isEmpty()))
									conSubQuery=blankQuery;
								}
								log.info("final conSubQuery: "+conSubQuery);
								}
							} else if (selType.equalsIgnoreCase("AUTO_COMPLETE")) {
								String selVal = filterMap.get("selectedValue")
										.toString();
								if (selVal != null && !(selVal.isEmpty())) {
									conSubQuery = conSubQuery + "`"+colName +"`" + " in ('"
											+ selVal + "') ";
								}
							} else if (selType
									.equalsIgnoreCase("BOOLEAN_SELECTION")) {
								String selVal = filterMap.get("selectedValue")
										.toString();
								if (selVal != null && !(selVal.isEmpty())) {
									if (conSubQuery.equalsIgnoreCase(" where ")) {

									} else
										conSubQuery = " and ";
									conSubQuery = conSubQuery + "`"+colName +"`" + " is "
											+ selVal;
								}
							} else if (selType.equalsIgnoreCase("AMOUNT_RANGE")) {
								String map = filterMap.get("selectedValue")
										.toString();
								if (map != null && !(map.isEmpty())) {
									log.info("map: " + map);
									map = map.replace("{fromValue=", "");
									map = map.replace("toValue=", "");
									log.info("map aftr replace: " + map);
									String arr[] = map.split(",");
									log.info(" arr[0]: " + arr[0]);
									log.info("arr[1]: " + arr[1]);
									arr[1] = arr[1].replace("}", "");
									log.info("arr[1]: " + arr[1]
											+ " arr[0]: " + arr[0]);
									String fromValue = arr[0].trim();
									String toValue = arr[1].trim();
									conSubQuery = conSubQuery + "`"+colName +"`"
											+ " between " + fromValue + " and "
											+ toValue;
								}
							}
							log.info("conSubQuery: " + conSubQuery);
							if (conSubQuery.length() > 1) {
								conSubQuery = conSubQuery + " and ";
							}
							conSubQueryFin = conSubQuery;
							if (conSubQuery.equalsIgnoreCase(" where ")) {

							} else {
								finQuery = finQuery + conSubQuery;
							}
						}

					}
					
					log.info("finQuery after conQuery: " + finQuery);
					String a = "";
					if (finQuery.endsWith(" and ")) {
						int lastAndVal = finQuery.lastIndexOf(" and ");
						log.info("lastAndVal :" + lastAndVal);

						StringBuilder sb = new StringBuilder(finQuery);
						finQuery = sb.substring(0, lastAndVal);
						log.info("part2 after replacing : " + finQuery);
					}
					log.info("finQuery after framing filters: " + finQuery);

					log.info("Applying filters on data set start");
					
					String statusFilters="";
					
					if(filtersMap.get("reconStatus")!=null && !(filtersMap.get("reconStatus").toString().isEmpty())){
						reconSatus=filtersMap.get("reconStatus").toString();
						if(reconSatus.equalsIgnoreCase("Both"))
							statusFilters=statusFilters+" as_of_recon_status in ('Reconciled','Un-Reconciled')";
						else statusFilters=statusFilters+" as_of_recon_status='"+reconSatus+"'";
					}
					log.info("statusFilters after recon status: "+statusFilters);
					if(filtersMap.get("accStatus")!=null && !(filtersMap.get("accStatus").toString().isEmpty())){
						accSatus=filtersMap.get("accStatus").toString();
						if(statusFilters!=null && !(statusFilters.isEmpty()) && !(statusFilters.equalsIgnoreCase("where"))){
							statusFilters=statusFilters+" and ";
						}
						if(accSatus.equalsIgnoreCase("Both"))
							statusFilters=statusFilters+" as_of_acc_status in ('Accounted', 'Un-Accounted') ";
						else statusFilters=statusFilters+" as_of_acc_status='"+accSatus+"'";
					}
					log.info("final statusFilters: "+statusFilters);
					if(statusFilters!=null && !(statusFilters.isEmpty()))
					{
						finQuery=finQuery+" and "+statusFilters;
					}

					log.info("finQuery after applying status filters: " + finQuery);
					data = data.filter(finQuery);
					//log.info("Previewing filtered data with sz: "+data.count());
			}else {
				log.info("filters doesnt exist");
			}

			if(decimalCol==true){
				layoutColList.add(qulaCol);
			}
			
			/*finalMap.put("data", data);
			finalMap.put("layoutColList", layoutColList);
			finalMap.put("buckAgg", buckAgg);
			finalMap.put("grpColArr", grpColArr);
			finalMap.put("reportTypeName", reportTypeName);
			finalMap.put("as_of_recon_status",reconSatus );
			finalMap.put("as_of_acc_status",accSatus );
			finalMap.put("othersFlag",othersFlag );*/

			log.info("Query framing end time at: "+new Date());
			
			return finalMap;

					
		}
	 
	 /**
	  * Author: Swetha
	  * Function to get report types with jpa queries
	  * @param tenantId
	  * @return
	  */
	 public HashMap<Long,String> getReportTypesLivy(Long tenantId){
		 List<ReportType> repTypeList=ReportTypeRepository.fetchActiveReportTypesByTenant(tenantId);
		 HashMap<Long, String> map=new HashMap<Long, String>();
		 for(int i=0;i<repTypeList.size();i++){
			 ReportType reportType=repTypeList.get(i);
			 String repTypeName=reportType.getType();
			 map.put(reportType.getId(), repTypeName);
		 }
		 log.info("ReportTypeListMap: "+map);
		return map;
	 }
	 
	 
	    /**
		  * author Rk
		  * @param jsonValueList
		  * @param sortColumn
		  * @param sortOrder
		  * @param sortColDataType
		  * @return jsonValueList
		  * @throws IOException
		  * @throws JSONException
		  * @throws ParseException
		  * @throws URISyntaxException
		  * @throws org.json.simple.parser.ParseException
		  * Desc : Sorting json arraylist
		  */

		    public List<JSONObject> sortJsonArrayList(List<JSONObject> jsonValueList,String sortColumn,String sortOrder, String sortColDataType) 
		    		throws IOException, JSONException, ParseException, URISyntaxException, org.json.simple.parser.ParseException
		    {
		    	final String sortColDataType1;
		    	sortColDataType1=sortColDataType;
		    	if(sortColDataType!=null) {
			    	Collections.sort( jsonValueList, new Comparator<JSONObject>() { 
		
			    		@Override
			    		public int compare(JSONObject a, JSONObject b) {
			    			String strValA = new String(), strValB = new String();
			    			Boolean boolValA, boolValB;
			    			BigDecimal longValA,longValB;
			    			Integer intA,intB;
			    			String className=null;
			    			switch(sortColDataType1) {
								case "DECIMAL":
									if(a.containsKey(sortColumn)) {
										className=a.get(sortColumn).getClass().getSimpleName();
										switch(className) {
											case "BigDecimal":
												longValA = (BigDecimal) a.get(sortColumn);
												break;
											case "Long":
												longValA = new BigDecimal((Long) a.get(sortColumn));
												break;
											case "Integer":
												longValA = new BigDecimal((Integer)a.get(sortColumn));
												break;
											case "Float":
												longValA = new BigDecimal((Float)a.get(sortColumn));
												break;
											case "Double":
												longValA = new BigDecimal((Double)a.get(sortColumn));
												break;
											default:
												longValA = new BigDecimal((String)a.get(sortColumn));
												break;
										}
									}
					    			else
					    				longValA=BigDecimal.ZERO;
					    			
					    			if(b.containsKey(sortColumn)) {
					    				className=b.get(sortColumn).getClass().getSimpleName();
										switch(className) {
											case "BigDecimal":
												longValB = (BigDecimal) b.get(sortColumn);
												break;
											case "Long":
												longValB = new BigDecimal((Long) b.get(sortColumn));
												break;
											case "Integer":
												longValB = new BigDecimal((Integer)b.get(sortColumn));
												break;
											case "Float":
												longValB = new BigDecimal((Float)b.get(sortColumn));
												break;
											case "Double":
												longValB = new BigDecimal((Double)b.get(sortColumn));
												break;
											default:
												longValB = new BigDecimal((String)b.get(sortColumn));
												break;
										}
					    			}
					    			else 
					    				longValB=BigDecimal.ZERO;
				
					    			if(sortOrder!=null && sortOrder.equalsIgnoreCase("ascending"))
					    				return longValA.compareTo(longValB);
					    			else
					    				return -longValA.compareTo(longValB);				
									
								case "INTEGER":
									if(a.containsKey(sortColumn))
										intA = (Integer) a.get(sortColumn);
					    			else
					    				intA=0;
					    			
					    			if(b.containsKey(sortColumn))
					    				intB = (Integer) b.get(sortColumn);
					    			else 
					    				intB=0;
				
					    			if(sortOrder!=null && sortOrder.equalsIgnoreCase("ascending"))
					    				return intA.compareTo(intB);
					    			else
					    				return -intA.compareTo(intB);
								case "BOOLEAN":
									if(a.containsKey(sortColumn))
										boolValA = (Boolean) a.get(sortColumn);
					    			else
					    				boolValA=null;
					    			
					    			if(b.containsKey(sortColumn))
					    				boolValB = (Boolean) b.get(sortColumn);
					    			else 
					    				boolValB=null;
				
					    			if(sortOrder!=null && sortOrder.equalsIgnoreCase("ascending"))
					    				return boolValA.compareTo(boolValB);
					    			else
					    				return -boolValA.compareTo(boolValB);
				    			default:
				    				if(a.containsKey(sortColumn))
				    					strValA = a.get(sortColumn).toString();
					    			else
					    				strValA=null;
					    			
					    			if(b.containsKey(sortColumn))
					    				strValB = b.get(sortColumn).toString();
					    			else 
					    				strValB=null;
				
					    			if(sortOrder!=null && sortOrder.equalsIgnoreCase("ascending"))
					    				return strValA.compareTo(strValB);
					    			else
					    				return -strValA.compareTo(strValB);
			    			}
			    			
			    		}
			    	});
		    	}
		    	return jsonValueList;
		    }
		    
		    /**
		     * Author: Rk
		     * @param jsonValueList
		     * @param headList
		     * @param serachString
		     * @return filteredObjList
		     * @throws IOException
		     * @throws JSONException
		     * @throws ParseException
		     * @throws URISyntaxException
		     * @throws org.json.simple.parser.ParseException
		     */
		    public List<JSONObject> jsonArrayListGlobalSearch(List<JSONObject> jsonValueList,List<JSONObject> headList,String serachString) 
		    		throws IOException, JSONException, ParseException, URISyntaxException, org.json.simple.parser.ParseException
		    {
		    	List<JSONObject> filteredObjList=new ArrayList<JSONObject>();
		    	serachString=serachString.toLowerCase();
		    	String serachStringDecimal=serachString.replaceAll(",", "");
		    	if(serachStringDecimal.matches("^[0-9]\\d*(\\.00)?$")) {
		    		serachStringDecimal=serachStringDecimal.substring(0, serachStringDecimal.length()-3);
		    	}else if(serachStringDecimal.matches("^[0-9]\\d*(\\.\\d0)?$")) {
		    		serachStringDecimal=serachStringDecimal.substring(0, serachStringDecimal.length()-1);
		    	}
		    	
		    	for (int i = 0; i < jsonValueList.size(); i++) {
		    		for(JSONObject eachCol:headList) {
		    			if (jsonValueList.get(i).containsKey(eachCol.get("field").toString()) && !(jsonValueList.get(i).get(eachCol.get("field").toString()).toString().isEmpty())) {
		    				if(eachCol.get("dataType").toString().equals("DECIMAL")) {
		    					if (jsonValueList.get(i).get(eachCol.get("field").toString()).toString().toLowerCase().contains(serachStringDecimal)) {
									filteredObjList.add(jsonValueList.get(i));
									break;
								}
		    				}
			    			else {
			    				if (jsonValueList.get(i).get(eachCol.get("field").toString()).toString().toLowerCase().contains(serachString)) {
									filteredObjList.add(jsonValueList.get(i));
									break;
								}
			    			}
		    			}
		        	}
				}
		    	
		    	
//		    	try {																// To check is search string is number or not
//		    		Double db=Double.parseDouble(serachString.replaceAll(",", ""));
//		    		serachString=db.toString();
//		    	}catch(NumberFormatException exp) {
//		    		
//		    	}
//		    	
//				for (int i = 0; i < jsonValueList.size(); i++) {
//					for (String head : headList) {
//						if (jsonValueList.get(i).containsKey(head)) {
//							if (!(jsonValueList.get(i).get(head).toString().isEmpty())) {
//								if (jsonValueList.get(i).get(head).toString().toLowerCase().contains(serachString.toLowerCase())) {
//									filteredObjList.add(jsonValueList.get(i));
//									break;
//								}
//							}
//						} 
//					}
//				}
				return filteredObjList;
		    } 
		    
		    private String removeTrailingZeros(String val){
		    	
		    	return null;
		    }
		    
		    /**
		     * Author: Rk
		     * @param jsonValueList
		     * @param headList
		     * @param serachString
		     * @return filteredObjList
		     * @throws IOException
		     * @throws JSONException
		     * @throws ParseException
		     * @throws URISyntaxException
		     * @throws org.json.simple.parser.ParseException
		     */
		    public List<JSONObject> jsonArrayListKeyLevelSearch(List<JSONObject> jsonValueList,List<HashMap> searchMap) 
		    		throws IOException, JSONException, ParseException, URISyntaxException, org.json.simple.parser.ParseException
		    {
		    	List<JSONObject> filteredObjList=new ArrayList<JSONObject>();
		    	String searchKey, searchValue, dataType;
		    	int temp=0;
				for (int i = 0; i < jsonValueList.size(); i++) {
					temp=0;
					for (HashMap eachCol : searchMap) {
						searchKey=eachCol.get("searchColumn").toString();
		    			dataType=eachCol.get("dataType").toString();
		    			if(dataType.equals("DECIMAL"))
		    				searchValue=eachCol.get("searchString").toString().replaceAll(",", "");
		    			else
		    				searchValue=eachCol.get("searchString").toString();
						if (jsonValueList.get(i).containsKey(searchKey)) {
							if (!(jsonValueList.get(i).get(searchKey).toString().isEmpty())) {
								if(dataType.equals("DECIMAL")) {
									if(new BigDecimal(jsonValueList.get(i).get(searchKey).toString()).compareTo(new BigDecimal(searchValue))==0) {
										temp++;
									}
									else
										break;
								}
								else if (jsonValueList.get(i).get(searchKey).toString().toLowerCase().contains(searchValue.toLowerCase())) {
									temp++;
								}else
									break;
							}else
								break;
						}else
							break;
					}
					if(temp==searchMap.size())
						filteredObjList.add(jsonValueList.get(i));
				}
				return filteredObjList;
		    } 
	    
	    
	   /* @PostMapping("/TabularViewReportGeneration")
	    @Timed*/
	    /*public JSONObject reportingPOC(@RequestParam Long tenantId,@RequestParam Long userId, @RequestParam Long reportId, @RequestBody(required = false) HashMap filtersMap, 
	    		@RequestParam(required=false) Integer pageNumber, @RequestParam(required=false) Integer pageSize,HttpServletResponse response) throws org.json.simple.parser.ParseException, IOException, OozieClientException, URISyntaxException{

	    	//Long userId=9L;
	    	log.info("Rest Request to reportingPOC with reportId: "+reportId+" @"+DateTime.now());
	    	log.info("filtersMap: "+filtersMap);
	    	Reports report=reportsRepository.findOne(reportId);
	    	*//**inserting record in reports requests table once the request is raised**//*
	    	
	        List<ReportDefination> reportDefinitionList=reportDefinationRepository.fetchByReportIdOrderByLayoutVal(reportId);
		    List<HashMap> defMapList=new ArrayList<HashMap>();
		    
		    for(int k=0;k<reportDefinitionList.size();k++){
		    	
		    	ReportDefination repDef=reportDefinitionList.get(k);
		    	HashMap defMap=new HashMap();
		    	defMap.put("ColumnId", repDef.getId());
		    	defMap.put("layoutDisplayName", repDef.getDisplayName());
		    	defMap.put("columnType", repDef.getRefTypeId());
		    	defMap.put("dataType", repDef.getDataType());
		    	defMapList.add(defMap);
		    	
		    }
		    
		    filtersMap.put("outputCols", defMapList);
			ReportRequests repReq=new ReportRequests();
			String requestName=report.getReportName()+ZonedDateTime.now();
			repReq.setReqName(requestName);
			repReq.setReportId(reportId);
			repReq.setTenantId(tenantId);
			repReq.setStatus("RUNNING");
			if(filtersMap!=null)
			{
				JSONObject obj=new JSONObject();
				obj.putAll(filtersMap);
				String map=obj.toJSONString();
				log.info("map: "+map);
			repReq.setFilterMap(map);
			}
			repReq.setSubmittedTime(ZonedDateTime.now());
			
			repReq.setCreatedBy(userId);
			repReq.setLastUpdatedBy(userId);
			repReq.setCreatedDate(ZonedDateTime.now());
			repReq.setLastUpdatedDate(ZonedDateTime.now());
			repReq.setRequestType("Request");
			repReq=reportRequestsRepository.save(repReq);
	    	log.info("repReq :"+repReq);
	    	
	    	JSONObject obj = new JSONObject();
	    	obj.putAll(filtersMap);
	    	log.info("obj: "+obj);
	    	
	    	String cmpltFilePath=FileWriteHDFS(reportId, obj,"params");
	    	log.info("done writing file to hdfs");
	    	
	    	HashMap parameterSet = new HashMap();
			parameterSet.put("param1", reportId);
			parameterSet.put("param2", cmpltFilePath);
			parameterSet.put("param3", pageNumber);
			parameterSet.put("param4", pageSize);
			parameterSet.put("param5", "Table");

			log.info("Api call to Intiate Report generation with job parameters: "+parameterSet);
			ResponseEntity jobStatus=oozieService.jobIntiateForAcctAndRec(tenantId, userId, "Reporting", parameterSet,null);
			log.info("jobStatus: "+jobStatus);
			HashMap map=(HashMap) jobStatus.getBody();
			log.info("map: "+map);
			String val=map.get("status").toString();
			log.info("val: "+val);
			JSONObject output=new JSONObject();
			String outputPath="";
			LinkedHashMap dataMap=new LinkedHashMap();
			
			if(val.equalsIgnoreCase("Failed to intiate job")){
				log.info("Reporting Program Failed");
				repReq.setStatus("FAILED");
				repReq.setLastUpdatedDate(ZonedDateTime.now());
				repReq=reportRequestsRepository.save(repReq);
				log.info("updating repReq if it is failed :"+repReq);
			}
			else{
				String status=oozieService.getStatusOfOozieJobId(val);
				log.info("status: "+status);
				repReq.setJobId(val);
				repReq.setLastUpdatedDate(ZonedDateTime.now());
				repReq=reportRequestsRepository.save(repReq);
				for(int i=0;outputPath.length()<1;i++){
					dataMap=getStatus(tenantId, val,userId, reportId);
					output=(JSONObject) dataMap.get("output");//output
					outputPath=dataMap.get("outputPath").toString();
					//log.info("outputPath at i: "+i+" is: "+outputPath);
				if(outputPath.length()>1)
				{
					
					
					log.info("outputPath :"+outputPath);
					String[] bits = outputPath.split("/");
					String lastOne = bits[bits.length-1];
					log.info("file name :"+lastOne);
					status=oozieService.getStatusOfOozieJobId(val);
					log.info("status after processess is completed :");
					repReq.setStatus(status);
					repReq.setGeneratedTime(ZonedDateTime.now());
					repReq.setOutputPath(outputPath);
					repReq.setFileName(lastOne);
					repReq.setLastUpdatedDate(ZonedDateTime.now());
					repReq=reportRequestsRepository.save(repReq);
					log.info(" final repReq :"+repReq);
					Notifications notification=new Notifications();
					notification.setModule("REPORTING");
					
					notification.setMessage("Requested "+report.getReportName()+" has been generated report");
					notification.setUserId(userId);
					notification.setIsViewed(false);
					notification.setActionType("REQUEST,REPORT");
					notification.setActionValue(repReq.getId().toString()+","+repReq.getReportId());
					notification.setTenantId(tenantId);
					notification.setCreatedBy(userId);
					notification.setCreationDate(ZonedDateTime.now());
					notification.setLastUpdatedBy(userId);
					notification.setLastUpdatedDate(ZonedDateTime.now());
					notification=notificationsRepository.save(notification);
					log.info("notification :"+notification);
					
					break;
				}
					
				}
			}
			int totDataCnt=Integer.parseInt(output.get("X-COUNT").toString());
			log.info("totDataCnt: "+totDataCnt);
			response.addIntHeader("X-COUNT", totDataCnt);
			
			List<LinkedHashMap> maps=(List<LinkedHashMap>) output.get("data");
			log.info("maps final count: "+maps.size());
			List<LinkedHashMap> subMaps=new ArrayList<LinkedHashMap>();
			if(maps.size()>0){
				if(maps.size()>=25){
				subMaps=maps.subList(0, 25);
				}
				else if(maps.size()<=25)
					subMaps=maps.subList(0, (maps.size()));
			}
			log.info("submaps final count: "+subMaps.size());
			
			
			output.put("data", subMaps);
			JSONObject requestInfo=(JSONObject) output.get("requestInfo");
			log.info("requestInfo from output: "+requestInfo);
			output.put("requestInfo", requestInfo);
			
			String path=dataMap.get("outputPath").toString();//outputPath
			log.info("path: "+path);
			
			output.put("outputPath", path);
			log.info("**end of tabular API** "+ZonedDateTime.now());
			return output;
			
	    }*/
	    
	    /**
	     * Author: Swetha
	     * @param tenantId
	     * @param userId
	     * @param reportId
	     * @param filtersMap
	     * @param pageNumber
	     * @param pageSize
	     * @param response
	     * @return
	     * @throws org.json.simple.parser.ParseException
	     * @throws IOException
	     * @throws OozieClientException
	     * @throws URISyntaxException
	     */
	    public LinkedHashMap reportingPOC(@RequestParam Long tenantId,@RequestParam Long userId, @RequestParam Long reportId, @RequestParam String requestName, @RequestBody(required = false) HashMap filtersMap, 
	    		@RequestParam(required=false) Integer pageNumber, @RequestParam(required=false) Integer pageSize,@RequestParam String sysRequestName,
	    		HttpServletResponse response,HttpServletRequest request) throws org.json.simple.parser.ParseException, IOException, OozieClientException, URISyntaxException{

	    	log.info("Rest Request to reportingPOC with reportId: "+reportId+" @"+DateTime.now());
	    	log.info("filtersMap: "+filtersMap);
	    	Reports report=reportsRepository.findOne(reportId);
	    	Long repId=report.getId();
	    	String repIdForDisplay=report.getIdForDisplay();
	    	String reportName="";
	    	reportName=report.getReportName();
	    	
	    	/**inserting record in reports requests table once the request is raised**/
	    	
	        List<ReportDefination> reportDefinitionList=reportDefinationRepository.fetchByReportIdOrderByLayoutVal(repId);
		    List<HashMap> defMapList=new ArrayList<HashMap>();
		    
		    for(int k=0;k<reportDefinitionList.size();k++){
		    	
		    	ReportDefination repDef=reportDefinitionList.get(k);
		    	HashMap defMap=new HashMap();
		    	defMap.put("ColumnId", repDef.getId());
		    	defMap.put("layoutDisplayName", repDef.getDisplayName());
		    	defMap.put("columnType", repDef.getRefTypeId());
		    	defMap.put("dataType", repDef.getDataType());
		    	defMapList.add(defMap);
		    	
		    }
		    
		    filtersMap.put("outputCols", defMapList);
			/*ReportRequests repReq=new ReportRequests();
			repReq.setReqName(requestName);
			repReq.setSysReqName(sysRequestName);
			repReq.setReportId(repId);
			repReq.setTenantId(tenantId);
			repReq.setStatus("RUNNING");
			if(filtersMap!=null)
			{
				JSONObject obj=new JSONObject();
				obj.putAll(filtersMap);
				String map=obj.toJSONString();
				//log.info("map: "+map);
			repReq.setFilterMap(map);
			}
			repReq.setSubmittedTime(ZonedDateTime.now());
			
			repReq.setCreatedBy(userId);
			repReq.setLastUpdatedBy(userId);
			repReq.setCreatedDate(ZonedDateTime.now());
			repReq.setLastUpdatedDate(ZonedDateTime.now());
			repReq.setRequestType("Run");
			repReq=reportRequestsRepository.save(repReq);
			String idForDisplay = IDORUtils.computeFrontEndIdentifier(repReq.getId().toString());
			repReq.setIdForDisplay(idForDisplay);
			repReq=reportRequestsRepository.save(repReq);
	    	log.info("repReq :"+repReq);*/
	    	
	    	JSONObject obj = new JSONObject();
	    	obj.putAll(filtersMap);
	    	//log.info("obj: "+obj);
	    	
	    	String cmpltFilePath=FileWriteHDFS(repId, obj,"params",report.getTenantId());
	    	log.info("done writing file to hdfs");
	    	
	    	HashMap parameterSet = new HashMap();
			parameterSet.put("param1", repIdForDisplay);
	    	//parameterSet.put("param1", reportId);
	    	parameterSet.put("param2", cmpltFilePath);
			parameterSet.put("param3", pageNumber);
			parameterSet.put("param4", pageSize);
			parameterSet.put("param5", "Table");
			parameterSet.put("param6", requestName);
			parameterSet.put("param7", sysRequestName);

			log.info("Api call to Intiate Report generation with job parameters: "+parameterSet);
			ResponseEntity jobStatus=oozieService.jobIntiateForAcctAndRec(tenantId, userId, "Reporting", parameterSet,null,request);
			log.info("jobStatus: "+jobStatus);
			HashMap map=(HashMap) jobStatus.getBody();
			log.info("map: "+map);
			String val=map.get("status").toString();
			log.info("val: "+val);
			//JSONObject output=new JSONObject();
			String outputPath="";
			String status="";
	    	String lastOne="";
			String pivotOutputPath="";
			LinkedHashMap dataMap=new LinkedHashMap();
			
			if(val.equalsIgnoreCase("Failed to intiate job")){
				log.info("Reporting Program Failed");
				/*repReq.setStatus("FAILED");
				repReq.setLastUpdatedDate(ZonedDateTime.now());
				repReq=reportRequestsRepository.save(repReq);
				log.info("updating repReq if it is failed :"+repReq);*/
			}
			else{
				log.info("Job has been initiated succesfully");
				SchedulerDetails schData=schedulerDetailsRepository.findByOozieJobIdAndTenantID(tenantId, val);
				log.info("schData: "+schData);
				status=oozieService.getStatusOfOozieJobId(val);
				log.info("status: "+status);
				/*repReq.setJobId(val);
				repReq.setLastUpdatedDate(ZonedDateTime.now());
				repReq=reportRequestsRepository.save(repReq);
				log.info("updating request with jobId: "+repReq);*/
				for(int i=0;;i++){
					
					status=oozieService.getStatusOfOozieJobId(val);
					
					if(!(status.equalsIgnoreCase("RUNNING"))){
						log.info("status: "+status);
						
					break;
						}
					else{
					}
					
					try {
						Thread.sleep(9000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				log.info("request to get success job status");
			}
			
			LinkedHashMap jobDataMap=new LinkedHashMap();
			jobDataMap.put("jobId", val);
			jobDataMap.put("lastOne", lastOne);
			//jobDataMap.put("repReq", repReq);
			jobDataMap.put("reportName",reportName );
			return jobDataMap;
	    }
	  
	    
	    
	    
	    
	    
	/**
	 * Author: Swetha
	 * PivotViewReport
	 * @param tenantId
	 * @param reportId
	 * @param filtersMap
	 * @return
	 * @throws org.json.simple.parser.ParseException
	 * @throws IOException
	 * @throws OozieClientException
	 * @throws URISyntaxException
	 */
	    public List<LinkedHashMap> PivotViewReport(@RequestParam Long tenantId, @RequestParam Long reportId, @RequestBody(required = false) HashMap filtersMap,HttpServletRequest request) 
	    		throws org.json.simple.parser.ParseException, IOException, OozieClientException, URISyntaxException{
	// public List<LinkedHashMap> pivotViewReport1234(@RequestParam Long tenantId, @RequestParam Long reportId, @RequestBody(required=false) HashMap filtersMap) throws AnalysisException, IOException, ParseException{
	    	Long userId=9L;
	    	log.info("Rest Request to reportingPOC with reportId: "+reportId+" @"+DateTime.now());
	    	log.info("filtersMap: "+filtersMap);
	    	JSONObject obj = new JSONObject();
	    	//obj=(JSONObject) filtersMap;
	    	obj.putAll(filtersMap);
	    	log.info("obj: "+obj);
	    	
	    	String cmpltFilePath=FileWriteHDFS(reportId, obj,"params",tenantId);
	    	log.info("done writing file to hdfs");
	    	
	    	HashMap parameterSet = new HashMap();
			parameterSet.put("param1", reportId);
			parameterSet.put("param2", cmpltFilePath);
			//parameterSet.put("param3", null);
			//parameterSet.put("param4", null);
			parameterSet.put("param5", "Pivot");
			

			log.info("Api call to Intiate Job for Data Transformation process: "+parameterSet);
			ResponseEntity jobStatus=oozieService.jobIntiateForAcctAndRec(tenantId, userId, "Reporting", parameterSet,null,request);
			log.info("jobStatus: "+jobStatus);
			HashMap map=(HashMap) jobStatus.getBody();
			log.info("map: "+map);
			String val=map.get("status").toString();
			log.info("val: "+val);
			JSONObject output=new JSONObject();
			LinkedHashMap dataMap=new LinkedHashMap();
			if(val.equalsIgnoreCase("Failed to intiate job")){
				log.info("Reporting Program Failed");
			}
			else{
				String status=oozieService.getStatusOfOozieJobId(val);
				log.info("status: "+status);
				
				for(int i=0;output.isEmpty();i++){
					dataMap=getStatus(tenantId, val,userId, reportId);
					output=(JSONObject) dataMap.get("output");
				if(!(output.isEmpty()))
					break;
				}
			}
			//int totDataCnt=Integer.parseInt(output.get("X-COUNT").toString());
			//log.info("totDataCnt: "+totDataCnt);
			List<LinkedHashMap> finOutput=(List<LinkedHashMap>) output.get("pivotOutput");
			log.info("finOutput: "+finOutput);
			return finOutput;
			
	    }
	  
	    /**
	     * Author: Swetha
	     * @param tenantId
	     * @param val
	     * @param userId
	     * @param reportId
	     * @return
	     * @throws IOException
	     * @throws URISyntaxException
	     * @throws org.json.simple.parser.ParseException
	     */
	    public LinkedHashMap getStatus123(@RequestParam Long tenantId,@RequestParam String val,@RequestParam Long userId,@RequestParam Long reportId) throws IOException, URISyntaxException, org.json.simple.parser.ParseException{
			 log.info("in getStatus with val: "+val+" and tenantId:"+tenantId);
			// val="0000989-180313083847588-oozie-hdsi-W";
			 Boolean flag=false;
			 LinkedHashMap map=new LinkedHashMap();
			 JSONObject output=new JSONObject();
			 JSONObject pivotOutput=new JSONObject();
					JobActions jobAction=jobActionsRepository.findReportOutputPath(val, tenantId);
					log.info("jobAction: "+jobAction);
					String outputPath="";
					if(jobAction!=null){
					log.info("jobAction: "+jobAction);
					String actionName=jobAction.getActionName();
					String[] actionNamesArr=actionName.split("is: ");
					log.info("actionNamesArr -0: "+actionNamesArr[0]+" actionNamesArr-1: "+actionNamesArr[1]);
					outputPath=actionNamesArr[1];
					map.put("output", output);
					map.put("outputPath", outputPath);
					}
					else{
						System.out.println("in else");
					}
					/*}
					else{
						//log.info("progra is still running");
					}*/
					JobActions pivotPathData=jobActionsRepository.findReportPivoutOutputPath(val, tenantId);
					String pivotPath="";
					if(pivotPathData!=null){
						log.info("pivotPathData: "+pivotPathData);
						String actionName=pivotPathData.getActionName();
						String[] actionNamesArr=actionName.split("is: ");
						log.info("actionNamesArr -0: "+actionNamesArr[0]+" actionNamesArr-1: "+actionNamesArr[1]);
						pivotPath=actionNamesArr[1];
						Long schedulerId=pivotPathData.getSchedulerId();
						//flag=true;
						map.put("pivotOutput", pivotOutput);
						map.put("pivotOutputPath", pivotPath);
						}
						else{
							System.out.println("in pivot else");
						}
						/*}
						else{
							//log.info("progra is still running");
						}*/
							
			 return map;
			 
		 }
	    
	    /**
		  * Author: Swetha
		  * Function to re create a JSON File, Write Object to it and Move it to HDFS 
		  * @param reportId
		  * @param obj
		  * @return
		  * @throws IOException
		  */
		 public String FileReWriteHDFS(Long reportId,JSONObject obj,String type,String existingFileName,String existingPath) throws IOException {
				log.info("Inside FileReWriteHDFS with reportId: "+reportId+" existingFileName: "+existingFileName+"type: "+type);
				log.info("existingPath: "+existingPath);
				
				String fileName="";
				String serverCpmltPath="";
				try {
					Long startDate = System.currentTimeMillis() ;
		        	Long startNanoseconds = System.nanoTime() ;
		        	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS") ;
		        	Long microSeconds = (System.nanoTime() - startNanoseconds) / 1000 ;
		        	Long date = startDate + (microSeconds/1000) ;
		  		    String res= dateFormat.format(date) + String.format("%03d", microSeconds % 1000);
		  		    String updRes=res.replace(' ', '_');
		  		    updRes=updRes.replace(':', '-');
		  		    updRes=updRes.replace('.', '-');
		        	fileName=existingFileName;
					
		        	BufferedWriter bw = null;
					FileWriter fw = null;
					String dir="/home/nspl/Reporting/";
					String FILENAME = dir+fileName;
					log.info("File Name is --> "+FILENAME);
					
					fw = new FileWriter(FILENAME);
					bw = new BufferedWriter(fw);
					//log.info("obj.toJSONString(): "+obj.toJSONString());
					bw.write(obj.toJSONString());
					log.info(bw.toString());
					log.info("File created successfully");
						bw.flush();
							if (bw != null)
						bw.close();

					if (fw != null)
						fw.close();
					
					UserGroupInformation ugi
			        = UserGroupInformation.createRemoteUser("hdsingle");
					
					String serverPath="/user/hdsingle/examples/apps/dev/reporting/report_parameters";
					serverCpmltPath="hdfs://192.168.0.155:9000"+serverPath+"/"+fileName;
					 
					 ugi.doAs(new PrivilegedAction<Void>() {

						@Override
						public Void run(){
							// TODO Auto-generated method stub
							Configuration conf = new Configuration();
							conf.set("fs.default.name", "hdfs://192.168.0.155:9000/user/hdsingle/examples/apps/dev/reporting/report_parameters");
							conf.set("hadoop.job.ugi", "hdsingle");
						
							FileSystem file = null;
							try {
								file = FileSystem.get(conf);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							File f=new File(FILENAME);
									//we can filter files if needed here
									 try {
										 file.delete(new Path("existingPath"));
										 log.info("Existing file has been deleted");
										file.copyFromLocalFile(true, true, new Path(f.getPath()), new Path("/user/hdsingle/examples/apps/dev/reporting/report_parameters"));
									} catch (IllegalArgumentException | IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
							
							log.info("copied to HDFS");
							return null;
						}
					 });
		        	
				} catch (IOException e) {
					log.info("e: "+e);
				}
				log.info("serverCpmltPath: "+serverCpmltPath);
				return serverCpmltPath;
		 }
	
}