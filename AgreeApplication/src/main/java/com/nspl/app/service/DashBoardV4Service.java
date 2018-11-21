package com.nspl.app.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.nspl.app.config.ApplicationContextProvider;
import com.nspl.app.domain.BucketDetails;
import com.nspl.app.domain.DataViews;
import com.nspl.app.domain.DataViewsColumns;
import com.nspl.app.domain.FileTemplateLines;
import com.nspl.app.domain.LookUpCode;
import com.nspl.app.domain.TenantConfig;
import com.nspl.app.repository.AccountedSummaryRepository;
import com.nspl.app.repository.AccountingDataRepository;
import com.nspl.app.repository.AppModuleSummaryRepository;
import com.nspl.app.repository.ApplicationProgramsRepository;
import com.nspl.app.repository.BucketDetailsRepository;
import com.nspl.app.repository.DataViewsColumnsRepository;
import com.nspl.app.repository.DataViewsRepository;
import com.nspl.app.repository.FileTemplateLinesRepository;
import com.nspl.app.repository.LookUpCodeRepository;
import com.nspl.app.repository.ProcessDetailsRepository;
import com.nspl.app.repository.ProgParametersSetsRepository;
import com.nspl.app.repository.ReconciliationResultRepository;
import com.nspl.app.repository.SchedulerDetailsRepository;
import com.nspl.app.repository.TenantConfigRepository;

@Service
public class DashBoardV4Service {


	private final Logger log = LoggerFactory.getLogger(DashBoardV4Service.class);

	@Inject
	UserJdbcService userJdbcService;


	@Inject
	AppModuleSummaryRepository appModuleSummaryRepository;


	@Inject
	ProcessDetailsRepository processDetailsRepository;

	@Inject
	DashBoardV2Service dashBoardV2Service;

	@Inject
	SchedulerDetailsRepository schedulerDetailsRepository;


	@Inject
	ApplicationProgramsRepository applicationProgramsRepository;

	@Inject
	ProgParametersSetsRepository progParametersSetsRepository;

	@Inject
	LookUpCodeRepository lookUpCodeRepository;

	@Inject
	ReconciliationResultRepository reconciliationResultRepository;

	@Inject
	DataViewsRepository dataViewsRepository;
	
	
	@Inject
	ReconciliationResultService reconciliationResultService;
	
	@Inject
	AccountedSummaryRepository accountedSummaryRepository;
	
	
	@Inject
	DataViewsColumnsRepository dataViewsColumnsRepository;
	
	
	@Inject
	FileTemplateLinesRepository fileTemplateLinesRepository;
	
	@Inject
	BucketDetailsRepository bucketDetailsRepository;
	
	@Inject
	AccountingDataRepository accountingDataRepository;
	
	@Inject
	TenantConfigRepository tenantConfigRepository;
	
	private DecimalFormat dform = new DecimalFormat("####0.00");


	public LinkedHashMap accountingRuleGroupSpecificInformationV4Service(String startDate,String endDate,HttpServletRequest request,Long ruleGrpId,Long viewId)
	{


		HashMap map0=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map0.get("tenantId").toString());


		LocalDate fDate=LocalDate.parse(startDate);

		LocalDate tDate=LocalDate.parse(endDate);

		LinkedHashMap ruleGrpInfoMap=new LinkedHashMap();
		Double notAccountedCt=0d;
		Double notAccountedCtPer=0d;
		Double notAccountedAmt=0d;
		Double notAccountedAmtPer=0d;
		List<LookUpCode> lookUpcodes=lookUpCodeRepository.findByTenantIdAndLookUpTypeAndModule(tenantId, "ACCOUNTING_STATUS", "REPORTING");
		for(LookUpCode actLC:lookUpcodes)
		{
		//	log.info("ruleGrpId :"+ruleGrpId);
		//	log.info("actLC :"+actLC);
			List<Object[]> acct1WSummary=new ArrayList<Object[]>();
			if(viewId!=null)
			{
				BigDecimal totalDvCount=appModuleSummaryRepository.fetchTotalDvcountForReconGroupByViewIdAndType(ruleGrpId, fDate, tDate,viewId,actLC.getLookUpCode());
				BigDecimal totalDvAmt=appModuleSummaryRepository.fetchTotalDvAmtForReconGroupByViewIdAndType(ruleGrpId, fDate, tDate,viewId,actLC.getLookUpCode());
				acct1WSummary=appModuleSummaryRepository.fetchAccountingInfoFromAndToDateAndTypeAndViewId
						(ruleGrpId,fDate,tDate,actLC.getLookUpCode(),viewId,totalDvCount,totalDvAmt);
			}
			else
			{
				
				BigDecimal totalDvCount=appModuleSummaryRepository.fetchTotalDvcountForActGroupByType(ruleGrpId, fDate, tDate,actLC.getLookUpCode());
				BigDecimal totalDvAmt=appModuleSummaryRepository.fetchTotalDvAmtForActGroupByType(ruleGrpId, fDate, tDate,actLC.getLookUpCode());
				log.info("ruleGrpId before:"+ruleGrpId+" and totalDvCount:"+totalDvCount+" and totalDvAmt:"+totalDvAmt +"for type "+actLC.getLookUpCode());
				acct1WSummary=appModuleSummaryRepository.fetchAccountingInfoFromAndToDateAndType
						(ruleGrpId,fDate,tDate,actLC.getLookUpCode(),totalDvCount,totalDvAmt);
			}

			if(acct1WSummary!=null && acct1WSummary.size()>0)
			{



				if(actLC.getLookUpCode().equalsIgnoreCase("Accounting inprocess") || actLC.getLookUpCode().equalsIgnoreCase("IN_PROCESS"))
				{

					notAccountedCt=notAccountedCt+Double.valueOf(acct1WSummary.get(0)[2].toString());
					notAccountedAmt=notAccountedAmt+Double.valueOf(acct1WSummary.get(0)[4].toString());
					notAccountedCtPer=notAccountedCtPer+Double.valueOf(acct1WSummary.get(0)[5].toString());
					notAccountedAmtPer=notAccountedAmtPer+Double.valueOf(acct1WSummary.get(0)[6].toString());

				}	
				else if(actLC.getLookUpCode().equalsIgnoreCase("Not accounted") || actLC.getLookUpCode().equalsIgnoreCase("UN_ACCOUNTED"))
				{

					notAccountedCt=notAccountedCt+Double.valueOf(acct1WSummary.get(0)[2].toString());
					notAccountedAmt=notAccountedAmt+Double.valueOf(acct1WSummary.get(0)[4].toString());
					notAccountedCtPer=notAccountedCtPer+Double.valueOf(acct1WSummary.get(0)[5].toString());
					notAccountedAmtPer=notAccountedAmtPer+Double.valueOf(acct1WSummary.get(0)[6].toString());

				}
				else
				{
					LinkedHashMap acctMap=new LinkedHashMap();
					acctMap.put("amount", Double.valueOf(acct1WSummary.get(0)[4].toString()));
					acctMap.put("count", Double.valueOf(acct1WSummary.get(0)[2].toString()));
					acctMap.put("amountPer",  Double.valueOf(acct1WSummary.get(0)[6].toString()));
					acctMap.put("countPer", Double.valueOf(acct1WSummary.get(0)[5].toString()));

					if(actLC.getLookUpCode().equalsIgnoreCase("ACCOUNTED"))
						ruleGrpInfoMap.put("accounted", acctMap);
					if(actLC.getLookUpCode().equalsIgnoreCase("JOURNALS_ENTERED"))
						ruleGrpInfoMap.put("finalAccounted", acctMap);

				}


			}
			else
			{
				LinkedHashMap acctMap=new LinkedHashMap();
				acctMap.put("amount", 0d);
				acctMap.put("count", 0d);
				acctMap.put("amountPer",  0d);
				acctMap.put("countPer", 0d);
				if(actLC.getLookUpCode().equalsIgnoreCase("ACCOUNTED"))
					ruleGrpInfoMap.put("accounted", acctMap);
				if(actLC.getLookUpCode().equalsIgnoreCase("JOURNALS_ENTERED"))
					ruleGrpInfoMap.put("finalAccounted", acctMap);

			}


		}

		LinkedHashMap notActMap=new LinkedHashMap();
		notActMap.put("amount", Double.valueOf(dform.format(notAccountedAmt)));
		notActMap.put("count", Double.valueOf(dform.format(notAccountedCt)));
		notActMap.put("amountPer",  Double.valueOf(dform.format(notAccountedAmtPer)));
		notActMap.put("countPer", Double.valueOf(dform.format(notAccountedCtPer)));
		ruleGrpInfoMap.put("notAccounted", notActMap);


		return ruleGrpInfoMap;

	}

	


	public List<LinkedHashMap> agiganalysisForUnReconciledDataOrUnAccounted(String startDate,String endDate,Long viewId,Long ruleGroupId,String type,Long bucketId,String fileDateOrQualifier,String viewType)
	{
		
		Boolean checkConnection = false;
		List<LinkedHashMap> dataMapList=new ArrayList<LinkedHashMap>();
		String amountQualifier=reconciliationResultService.getViewColumnQualifier(BigInteger.valueOf(viewId), "AMOUNT");
		log.info("amountQualifier :"+amountQualifier);
		//	LinkedHashMap am=dashBoardV2Service.groupByColumnsOfView(viewId);
		Connection conn = null;
		Statement stmt = null;
		ResultSet result = null;


		DataViews dv=dataViewsRepository.findOne(viewId);

		List<BigInteger> reconciledOrAccountedIds=new ArrayList<BigInteger>();
		if(type.equalsIgnoreCase("unreconciled"))
		{
			if(viewType!=null && viewType.equalsIgnoreCase("source"))
			{
			List<BigInteger> orginalRowIds=reconciliationResultRepository.fetchOrginalRowIdsByRuleGroupId(ruleGroupId,viewId);
			log.info("orginalRowIds :"+orginalRowIds.size());
			reconciledOrAccountedIds.addAll(orginalRowIds);
			}

			else if(viewType!=null && viewType.equalsIgnoreCase("target"))
			{
			List<BigInteger> targetRowIds=reconciliationResultRepository.fetchTargetRowIdsByRuleGroupId(ruleGroupId,viewId);
			log.info("targetRowIds :"+targetRowIds.size());
			reconciledOrAccountedIds.addAll(targetRowIds);
			}

			log.info("reconciledIds size for view "+viewId+" is "+reconciledOrAccountedIds.size());
		}
		else if (type.equalsIgnoreCase("unaccounted"))
		{
			List<BigInteger> accountedRowIds=accountedSummaryRepository.fetchAccountingIdsByStatusNGroupIdNViewId(ruleGroupId, viewId,"ACCOUNTED");
			reconciledOrAccountedIds.addAll(accountedRowIds);
		}
		log.info("size of reconciledOrAccountedIds :"+reconciledOrAccountedIds.size());
		String reconciledOrAccountedIdList=reconciledOrAccountedIds.toString().replaceAll("\\[", "").replaceAll("\\]", "");
		//log.info("orginalRows :"+reconciledOrAccountedIdList);

		String queryUnRec="";


		String subQuery="";

		List<BucketDetails> buckDetails=bucketDetailsRepository.findByBucketId(bucketId);

		for(int j=0;j<buckDetails.size();j++){
			int buckVal=j+1;
			BucketDetails bucketDet=buckDetails.get(j);
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

				if(from!=null && to!=null){
					if(from>to){

						frmVal=to;
						toVal=from;
					}
					else{
						frmVal=from;
						toVal=to;
					}


					String ruleAge=frmVal+"-"+toVal;
					subQuery=subQuery+" when DATEDIFF(CURDATE(),`"+fileDateOrQualifier+"`) between "+frmVal+" and "+toVal+" then '"+ruleAge+"'";



				}
				else{

					log.info("any one of the bucket limits are null");

					if(to==null){
						String ruleAge=">="+from;
						subQuery=subQuery+" when DATEDIFF(CURDATE(),`"+fileDateOrQualifier+"`) > "+from+" then '"+ruleAge+"'";
					}
					if(from==null){
						String ruleAge="<="+to;
						subQuery=subQuery+" when DATEDIFF(CURDATE(),`"+fileDateOrQualifier+"`) < "+to+" then '"+ruleAge+"'";
					}


				}
			}
		}
		log.info("bucket query b4 others : "+subQuery);
		subQuery=subQuery+" else 'Others' end as bucket";





		if(reconciledOrAccountedIds.size()>0)
		{
			queryUnRec="select case "+subQuery+", count(`scrIds`),ROUND(sum(`"+amountQualifier+"`),2) as amount from `"+dv.getDataViewName().toLowerCase()+"` where `"+fileDateOrQualifier+"`>='"+startDate+"' and `"+fileDateOrQualifier+"`<='"+endDate+"' and scrIds not in ("+reconciledOrAccountedIdList+") group by bucket";
			//log.info("queryUnRec****size>0:"+queryUnRec);
		}
		else
		{
			queryUnRec="select case "+subQuery+", count(`scrIds`),ROUND(sum(`"+amountQualifier+"`),2) as amount from `"+dv.getDataViewName().toLowerCase()+"` where `"+fileDateOrQualifier+"`>='"+startDate+"' and `"+fileDateOrQualifier+"`<='"+endDate+"' group by bucket";
			//log.info("queryUnRec***:"+queryUnRec);
		}

		try
		{
			DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
			conn=ds.getConnection();
			checkConnection = true;
			log.info("Checking Connection Open in agiganalysisForUnReconciledDataOrUnAccounted:"+checkConnection);
			log.info("Connected database successfully...");
			stmt = conn.createStatement();
			//log.info("at dv id :"+dv.getId());
			//log.info("queryUnRec :"+queryUnRec+" at time :"+ZonedDateTime.now());
			result=stmt.executeQuery(queryUnRec);
			//	log.info("after executing query at time :"+ZonedDateTime.now());

			while(result.next())
			{

				LinkedHashMap data=new LinkedHashMap();
				LinkedHashMap eachAgeObj=new LinkedHashMap();


				data.put("age",result.getString("bucket"));
				data.put("count", Integer.valueOf(result.getString("count(`scrIds`)").toString()));
				if(result.getString("amount")!=null)
				data.put("amount", Double.valueOf(result.getString("amount").toString()));
				else
				data.put("amount", 0d);
				//eachAgeObj.put(result.getString("bucket"),data);
				dataMapList.add(data);

			}
		}
		catch(Exception ex)
		{
			log.info("Exception while fetching data in agiganalysisForUnReconciledDataOrUnAccounted:"+ex);
		}
		finally
		{
			try{
				if(result!=null)
				{
					result.close();
					checkConnection = false;
				}
				if(stmt!=null)
				{
					stmt.close();
					checkConnection = false;
				}
				if(conn!=null)
				{
					conn.close();
					checkConnection = false;
				}
			}
			catch(Exception ex)
			{
				log.info("Exception while closing statements :"+ex);
			}
		}

		log.info("Checking Connection Close in agiganalysisForUnReconciledDataOrUnAccounted:"+checkConnection);
		return dataMapList;

	}
	
	public LinkedHashMap getDVGroupByColumns(Long viewId,Boolean attributeName)
	{

		LinkedHashMap finalMap=new LinkedHashMap();
		List<LinkedHashMap> groupByList=new ArrayList<LinkedHashMap>();
		//	List<DataViewsColumns> dvcList=dataViewsColumnsRepository.findByDataViewIdAndRefDvTypeAndGroupByIsTrue(viewId,"File Template");

		List<DataViewsColumns> dvcList=dataViewsColumnsRepository.findByDataViewIdAndRefDvTypeAndGroupByIsTrue(viewId,"File Template");
		log.info("dvcList1 :"+dvcList.size());
		if(dvcList.isEmpty() || dvcList.size()<=0)
		{
			log.info("if size zero");
			dvcList=dataViewsColumnsRepository.findByDataViewIdAndRefDvTypeIsNullAndGroupByIsTrue(viewId);
			for(DataViewsColumns dvc:dvcList)
			{
				LinkedHashMap groupBy=new LinkedHashMap();
				groupBy.put("columnName", dvc.getColumnName());
				//	FileTemplateLines ftl=fileTemplateLinesRepository.findOne(Long.valueOf(dvc.getRefDvColumn()));
				groupBy.put("columnAliasName",  dvc.getColumnName());
				groupBy.put("dataType", dvc.getColDataType());
				groupByList.add(groupBy);
			}

		}
		else
		{
			for(DataViewsColumns dvc:dvcList)
			{
				LinkedHashMap groupBy=new LinkedHashMap();
				groupBy.put("columnName", dvc.getColumnName());
				FileTemplateLines ftl=fileTemplateLinesRepository.findOne(Long.valueOf(dvc.getRefDvColumn()));
				groupBy.put("columnAliasName", ftl.getColumnAlias());
				groupBy.put("dataType", dvc.getColDataType());
				groupByList.add(groupBy);
			}
		}
		DataViewsColumns dvcAmtQualitifier=dataViewsColumnsRepository.findByDataViewIdAndQualifier(viewId,"AMOUNT");

		//log.info("dvcList :"+dvcList);


		LinkedHashMap amtQualifierList=new LinkedHashMap();
		LinkedHashMap amtQualifier=new LinkedHashMap();
		if(dvcAmtQualitifier!=null)
		{
			if(dvcAmtQualitifier.getRefDvColumn()!=null)
			{
				FileTemplateLines ftl=fileTemplateLinesRepository.findOne(Long.valueOf(dvcAmtQualitifier.getRefDvColumn()));
				if(ftl!=null)
				{
					if(attributeName)
					{
						amtQualifierList.put("field", dvcAmtQualitifier.getColumnName());
						amtQualifierList.put("header", ftl.getColumnAlias());
						amtQualifierList.put("dataType",dvcAmtQualitifier.getColDataType());
						amtQualifierList.put("align", "right");
						amtQualifierList.put("width", "150px");
					}
					else
					{
						amtQualifierList.put("amtQualifier", dvcAmtQualitifier.getColumnName());
						amtQualifierList.put("amtQualifierAliasName", ftl.getColumnAlias());
						amtQualifierList.put("dataType",dvcAmtQualitifier.getColDataType());
						amtQualifierList.put("align", "right");
						amtQualifierList.put("width", "150px");
					}
				}
			}
			else
			{
				if(attributeName)
				{
					
					amtQualifierList.put("field", dvcAmtQualitifier.getColumnName());
					amtQualifierList.put("header", dvcAmtQualitifier.getColumnName());
					amtQualifierList.put("dataType", dvcAmtQualitifier.getColDataType());
					amtQualifierList.put("align", "right");
					amtQualifierList.put("width", "150px");
					
					
				}
				else
				{
					amtQualifierList.put("amtQualifier", dvcAmtQualitifier.getColumnName());
					amtQualifierList.put("amtQualifierAliasName", dvcAmtQualitifier.getColumnName());
					amtQualifierList.put("dataType", dvcAmtQualitifier.getColDataType());
					amtQualifierList.put("align", "right");
					amtQualifierList.put("width", "150px");
				}
			}
		}


		DataViewsColumns dvcCurrencyCodeQualitifier=dataViewsColumnsRepository.findByDataViewIdAndQualifier(viewId,"CURRENCYCODE");

		//log.info("dvcList :"+dvcList);


		LinkedHashMap currencyCodeQualifierList=new LinkedHashMap();
		LinkedHashMap currencyCodeQualifier=new LinkedHashMap();
		if(dvcCurrencyCodeQualitifier!=null)
		{
			if(dvcCurrencyCodeQualitifier.getRefDvColumn()!=null)
			{
				FileTemplateLines ftl=fileTemplateLinesRepository.findOne(Long.valueOf(dvcCurrencyCodeQualitifier.getRefDvColumn()));
				if(ftl!=null)
				{
					currencyCodeQualifier.put("currencyCodeQualifier", dvcCurrencyCodeQualitifier.getColumnName());
					currencyCodeQualifier.put("currencyCodeQualifierAliasName", ftl.getColumnAlias());
					currencyCodeQualifier.put("dataType", dvcCurrencyCodeQualitifier.getColDataType());
					currencyCodeQualifier.put("align", "right");
					currencyCodeQualifier.put("width", "150px");
				}
			}
			else
			{
				currencyCodeQualifier.put("currencyCodeQualifier", dvcCurrencyCodeQualitifier.getColumnName());
				currencyCodeQualifier.put("currencyCodeQualifierAliasName", dvcCurrencyCodeQualitifier.getColumnName());
				currencyCodeQualifier.put("dataType", dvcCurrencyCodeQualitifier.getColDataType());
				currencyCodeQualifier.put("align", "right");
				currencyCodeQualifier.put("width", "150px");
			}
		}


		DataViewsColumns dvcTransDateQualitifier=dataViewsColumnsRepository.findByDataViewIdAndQualifier(viewId,"TRANSDATE");

		//log.info("dvcList :"+dvcList);


		LinkedHashMap transDateQualifierList=new LinkedHashMap();
		LinkedHashMap transDateQualifier=new LinkedHashMap();
		if(dvcTransDateQualitifier!=null)
		{
			if(dvcTransDateQualitifier.getRefDvColumn()!=null)
			{
				FileTemplateLines ftl=fileTemplateLinesRepository.findOne(Long.valueOf(dvcTransDateQualitifier.getRefDvColumn()));
				if(ftl!=null)
				{
					transDateQualifier.put("field", dvcTransDateQualitifier.getColumnName());
					transDateQualifier.put("header", ftl.getColumnAlias());
					transDateQualifier.put("dataType", dvcTransDateQualitifier.getColDataType());
					transDateQualifier.put("align", "left");
					transDateQualifier.put("width", "150px");
				}
			}
			else
			{
				transDateQualifier.put("field", dvcTransDateQualitifier.getColumnName());
				transDateQualifier.put("header", dvcTransDateQualitifier.getColumnName());
				transDateQualifier.put("dataType", dvcTransDateQualitifier.getColDataType());
				transDateQualifier.put("align", "left");
				transDateQualifier.put("width", "150px");
			}
		}


		finalMap.put("currencyCodeQualifier", currencyCodeQualifier);
		finalMap.put("transDateQualifier", transDateQualifier);
		finalMap.put("amtQualifier", amtQualifierList);
		finalMap.put("columnsList", groupByList);

		// groupByList.add(amtQualifier);
		return finalMap;


	}
	
	
	
	
	
	
	
	public LinkedHashMap getSrcTargetCombinationViewsByRuleGrpofUnReconciledForViolationAndAgingService
	(LocalDate fDate,LocalDate tDate,BigInteger srcViewId,Long trgViewId,Long ruleGroupId,String age,Integer violation,Long bucketId)
	{

		Boolean checkConnection = false;
		LinkedHashMap combination=new LinkedHashMap();

		Connection conn = null;
		Statement stmtSrcTcAndAmtDv = null;
		ResultSet resultSrcTcAndAmtDv = null;

		Statement stmtTrgTcAndAmtDv = null;
		ResultSet resultTrgTcAndAmtDv = null;

		Double totalSrcCount=0d;
		Double totalSrcAmount=0d;

		Double totalTrgCount=0d;
		Double totalTrgAmount=0d;


		Statement stmtSrcNRAndAmtDv=null;
		ResultSet resultSrcNRAndAmtDv=null;

		Double unRecSrcCount=0d;
		Double UnRecSrcAmount=0d;

		Statement stmtTrgNRAndAmtDv=null;
		ResultSet resultTrgNRAndAmtDv=null;

		Double unRecTrgCount=0d;
		Double UnRecTrgAmount=0d;
		//Src dataview
		try
		{


			/** 
			 * to get src and trg reconciled Ids List
			 */
			List<BigInteger> reconciledSrcIds=new ArrayList<BigInteger>();
			List<BigInteger> reconciledTrgIds=new ArrayList<BigInteger>();

			List<BigInteger> orginalRowIds=reconciliationResultRepository.fetchOrginalRowIdsByRuleGroupId(ruleGroupId,srcViewId.longValue());
			log.info("orginalRowIds :"+orginalRowIds.size());
			reconciledSrcIds.addAll(orginalRowIds);

			List<BigInteger> targetRowIds=reconciliationResultRepository.fetchTargetRowIdsByRuleGroupId(ruleGroupId,trgViewId);
			log.info("targetRowIds :"+targetRowIds.size());
			reconciledTrgIds.addAll(targetRowIds);



			String reconciledSrcIdList=reconciledSrcIds.toString().replaceAll("\\[", "").replaceAll("\\]", "");
			String reconciledTrgIdList=reconciledTrgIds.toString().replaceAll("\\[", "").replaceAll("\\]", "");

			DataViews dv = dataViewsRepository.findOne(srcViewId.longValue());
			DataViews innerTargetDv = dataViewsRepository.findOne(trgViewId);

			String srcAmountQualifier=reconciliationResultService.getViewColumnQualifier(srcViewId, "AMOUNT");
			log.info("srcAmountQualifier :"+srcAmountQualifier);

			String trgAmountQualifier=reconciliationResultService.getViewColumnQualifier(BigInteger.valueOf(trgViewId), "AMOUNAT");
			log.info("trgAmountQualifier :"+trgAmountQualifier);




			String srcTotalQuery="select COUNT(scrIds) as count ,case when ROUND(SUM(`"+srcAmountQualifier+"`), 2) is null then 0 else ROUND(SUM(`"+srcAmountQualifier+"`), 2) end AS amount from `"+dv.getDataViewName().toLowerCase()+"` "
					+ "where scrIds not in ("+reconciledSrcIdList+") and DATE(fileDate)>='"+fDate+"' and DATE(fileDate) <='"+tDate+"'";
			//log.info("srcTotalQuery in getSrcTargetCombinationViewsByRuleGrpOfUnReconForViolationAndAging sevice: "+srcTotalQuery);

			String trgTotalQuery="select COUNT(scrIds) as count ,case when ROUND(SUM(`"+trgAmountQualifier+"`), 2) is null then 0 else ROUND(SUM(`"+trgAmountQualifier+"`), 2) end AS amount from `"+innerTargetDv.getDataViewName().toLowerCase()+"` "
					+ "where scrIds not in ("+reconciledTrgIdList+") and DATE(fileDate)>='"+fDate+"' and DATE(fileDate) <='"+tDate+"'";
			//log.info("trgTotalQuery in getSrcTargetCombinationViewsByRuleGrpOfUnReconForViolationAndAging service :"+trgTotalQuery);

			DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
			conn = ds.getConnection();	
			checkConnection = true;
			log.info("Checking Connection Open in getSrcTargetCombinationViewsByRuleGrpofUnReconciledForViolationAndAgingService:"+checkConnection);
			stmtSrcTcAndAmtDv = conn.createStatement();
			resultSrcTcAndAmtDv = stmtSrcTcAndAmtDv.executeQuery(srcTotalQuery);

			while(resultSrcTcAndAmtDv.next())
			{
				totalSrcCount=totalSrcCount+Double.valueOf(resultSrcTcAndAmtDv.getString("count"));
				totalSrcAmount=totalSrcAmount+Double.valueOf(resultSrcTcAndAmtDv.getString("amount"));
			}
			log.info("totalSrcCount: "+totalSrcCount);
			log.info("totalSrcAmount: "+totalSrcAmount);


			stmtTrgTcAndAmtDv = conn.createStatement();
			resultTrgTcAndAmtDv = stmtTrgTcAndAmtDv.executeQuery(trgTotalQuery);
			while(resultTrgTcAndAmtDv.next())
			{
				totalTrgCount=totalTrgCount+Double.valueOf(resultTrgTcAndAmtDv.getString("count"));
				totalTrgAmount=totalTrgAmount+Double.valueOf(resultTrgTcAndAmtDv.getString("amount"));
			}

			log.info("totalTrgCount at viewId :"+innerTargetDv.getId()+" is :"+totalTrgCount);
			log.info("totalTrgAmount at viewId :"+innerTargetDv.getId()+" is :"+totalTrgAmount);


			String srcUnRecQuery="";
			String trgUnRecQuery="";




			if(age==null && violation!=null)
			{
				srcUnRecQuery=	"select if( DATEDIFF(CURDATE(), `fileDate`) >="+violation+" ,'yes','no') as age ,COUNT(scrIds) as count ,ROUND(SUM(`"+srcAmountQualifier+"`), 2) AS amount from `"+dv.getDataViewName().toLowerCase()+"` "
						+ "where scrIds not in ("+reconciledSrcIdList+") and DATE(fileDate)>='"+fDate+"' and DATE(fileDate) <='"+tDate+"' group by age ORDER BY age";

				//log.info("srcUnRecQuery :"+srcUnRecQuery);

				stmtSrcNRAndAmtDv = conn.createStatement();
				resultSrcNRAndAmtDv = stmtSrcNRAndAmtDv.executeQuery(srcUnRecQuery);

				while(resultSrcNRAndAmtDv.next())
				{
					if(resultSrcNRAndAmtDv.getString("age").equalsIgnoreCase("yes"))
					{
						unRecSrcCount=unRecSrcCount+Double.valueOf(resultSrcNRAndAmtDv.getString("count"));
						UnRecSrcAmount=UnRecSrcAmount+Double.valueOf(resultSrcNRAndAmtDv.getString("amount"));
					}
				}

				log.info("unRecSrcCount at viewId :"+dv.getId()+" is :"+unRecSrcCount);
				log.info("UnRecSrcAmount at viewId :"+dv.getId()+" is :"+UnRecSrcAmount);


				trgUnRecQuery=	"select if( DATEDIFF(CURDATE(), `fileDate`) >="+violation+" ,'yes','no') as age ,COUNT(scrIds) as count ,ROUND(SUM(`"+trgAmountQualifier+"`), 2) AS amount from `"+innerTargetDv.getDataViewName().toLowerCase()+"` "
						+ "where scrIds not in ("+reconciledTrgIdList+") and DATE(fileDate)>='"+fDate+"' and DATE(fileDate) <='"+tDate+"' group by age ORDER BY age";

				//	log.info("trgUnRecQuery :"+trgUnRecQuery);

				stmtTrgNRAndAmtDv = conn.createStatement();
				resultTrgNRAndAmtDv = stmtSrcNRAndAmtDv.executeQuery(trgUnRecQuery);

				while(resultTrgNRAndAmtDv.next())
				{
					if(resultTrgNRAndAmtDv.getString("age").equalsIgnoreCase("yes"))
					{
						unRecTrgCount=unRecTrgCount+Double.valueOf(resultTrgNRAndAmtDv.getString("count"));
						UnRecTrgAmount=UnRecTrgAmount+Double.valueOf(resultTrgNRAndAmtDv.getString("amount"));
					}
				}

				log.info("unRecTrgCount at viewId :"+innerTargetDv.getId()+" is :"+unRecTrgCount);
				log.info("UnRecTrgAmount at viewId :"+innerTargetDv.getId()+" is :"+UnRecTrgAmount);

			}


			if(age!=null && violation==null)
			{


				if(age.contains("-"))
				{
					log.info("age contains -");
					String[] str=age.split("-");
					log.info("str at 0:"+str[0]);
					log.info("str at 1:"+str[1]);

					srcUnRecQuery=	"select if( DATEDIFF(CURDATE(), `fileDate`) >="+str[0]+" and DATEDIFF(CURDATE(), `fileDate`)<="+str[1]+" ,'yes','no') as age ,COUNT(scrIds) as count ,ROUND(SUM(`"+srcAmountQualifier+"`), 2) AS amount from `"+dv.getDataViewName().toLowerCase()+"` "
							+ "where scrIds not in ("+reconciledSrcIdList+") and DATE(fileDate)>='"+fDate+"' and DATE(fileDate) <='"+tDate+"' group by age ORDER BY age";

					trgUnRecQuery=	"select if(  DATEDIFF(CURDATE(), `fileDate`) >="+str[0]+" and DATEDIFF(CURDATE(), `fileDate`)<="+str[1]+" ,'yes','no') as age ,COUNT(scrIds) as count ,ROUND(SUM(`"+trgAmountQualifier+"`), 2) AS amount from `"+innerTargetDv.getDataViewName().toLowerCase()+"` "
							+ "where scrIds not in ("+reconciledTrgIdList+") and DATE(fileDate)>='"+fDate+"' and DATE(fileDate) <='"+tDate+"' group by age ORDER BY age";


				}
				else if(age.contains(">") || (age.contains("<")))
				{
					String[] str=age.split("=");

					if(age.contains(">") )
					{
						srcUnRecQuery=	"select if( DATEDIFF(CURDATE(), `fileDate`) >="+str[1]+" ,'yes','no') as age ,COUNT(scrIds) as count ,ROUND(SUM(`"+srcAmountQualifier+"`), 2) AS amount from `"+dv.getDataViewName().toLowerCase()+"` "
								+ "where scrIds not in ("+reconciledSrcIdList+") and DATE(fileDate)>='"+fDate+"' and DATE(fileDate) <='"+tDate+"' group by age ORDER BY age";

						trgUnRecQuery=	"select if( DATEDIFF(CURDATE(), `fileDate`) >="+str[1]+" ,'yes','no') as age ,COUNT(scrIds) as count ,ROUND(SUM(`"+trgAmountQualifier+"`), 2) AS amount from `"+innerTargetDv.getDataViewName().toLowerCase()+"` "
								+ "where scrIds not in ("+reconciledTrgIdList+") and DATE(fileDate)>='"+fDate+"' and DATE(fileDate) <='"+tDate+"' group by age ORDER BY age";
					}
					else if(age.contains("<"))
					{
						srcUnRecQuery=	"select if( DATEDIFF(CURDATE(), `fileDate`) <="+str[1]+" ,'yes','no') as age ,COUNT(scrIds) as count ,ROUND(SUM(`"+srcAmountQualifier+"`), 2) AS amount from `"+dv.getDataViewName().toLowerCase()+"` "
								+ "where scrIds not in ("+reconciledSrcIdList+") and DATE(fileDate)>='"+fDate+"' and DATE(fileDate) <='"+tDate+"' group by age ORDER BY age";

						trgUnRecQuery=	"select if( DATEDIFF(CURDATE(), `fileDate`) <="+str[1]+" ,'yes','no')<="+str[1]+" ,'yes','no') as age ,COUNT(scrIds) as count ,ROUND(SUM(`"+trgAmountQualifier+"`), 2) AS amount from `"+innerTargetDv.getDataViewName().toLowerCase()+"` "
								+ "where scrIds not in ("+reconciledTrgIdList+") and DATE(fileDate)>='"+fDate+"' and DATE(fileDate) <='"+tDate+"' group by age ORDER BY age";
					}
				}
				else if(age.equalsIgnoreCase("others"))
				{
					log.info("in others group");
					String subQuery="";
					List<BucketDetails> buckDetails=bucketDetailsRepository.findByBucketId(bucketId);

					for(int j=0;j<buckDetails.size();j++){
						int buckVal=j+1;
						BucketDetails bucketDet=buckDetails.get(j);
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

							if(from!=null && to!=null){
								if(from>to){

									frmVal=to;
									toVal=from;
								}
								else{
									frmVal=from;
									toVal=to;
								}


								String ruleAge=frmVal+"-"+toVal;
								subQuery=subQuery+"(DATEDIFF(CURDATE(),`fileDate`) not between "+frmVal+" and "+toVal+") and";



							}
							else{

								log.info("any one of the bucket limits are null");

								if(to==null){
									String ruleAge=">="+from;
									subQuery=subQuery+"(DATEDIFF(CURDATE(),`fileDate`) > "+from+") and ";
								}
								if(from==null){
									String ruleAge="<="+to;
									subQuery=subQuery+"(DATEDIFF(CURDATE(),`fileDate`) < "+to+") and ";
								}


							}
						}
					}


					log.info("in other subquery :"+ subQuery);
					String finalSubQuery = subQuery.substring(0,subQuery.length()-3);
					log.info("in other finalSubQuery :"+ finalSubQuery);
					finalSubQuery =finalSubQuery+",'Yes','No') as age";

					srcUnRecQuery=	"select if("+finalSubQuery+" ,COUNT(scrIds) as count ,ROUND(SUM(`"+srcAmountQualifier+"`), 2) AS amount from `"+dv.getDataViewName().toLowerCase()+"` "
							+ "where scrIds not in ("+reconciledSrcIdList+") and DATE(fileDate)>='"+fDate+"' and DATE(fileDate) <='"+tDate+"' group by age ORDER BY age";

					trgUnRecQuery=	"select  if("+finalSubQuery+",COUNT(scrIds) as count ,ROUND(SUM(`"+trgAmountQualifier+"`), 2) AS amount from `"+innerTargetDv.getDataViewName().toLowerCase()+"` "
							+ "where scrIds not in ("+reconciledTrgIdList+") and DATE(fileDate)>='"+fDate+"' and DATE(fileDate) <='"+tDate+"' group by age ORDER BY age";





				}
				//log.info("srcUnRecQuery after applying aging in getSrcTargetCombinationViewsByRuleGrpofUnReconciledForViolationAndAgingService:"+srcUnRecQuery);
				//log.info("trgUnRecQuery after applying aging in getSrcTargetCombinationViewsByRuleGrpofUnReconciledForViolationAndAgingService:"+trgUnRecQuery);

				stmtSrcNRAndAmtDv = conn.createStatement();
				resultSrcNRAndAmtDv = stmtSrcNRAndAmtDv.executeQuery(srcUnRecQuery);

				while(resultSrcNRAndAmtDv.next())
				{
					if(resultSrcNRAndAmtDv.getString("age").equalsIgnoreCase("yes"))
					{
						unRecSrcCount=unRecSrcCount+Double.valueOf(resultSrcNRAndAmtDv.getString("count"));
						UnRecSrcAmount=UnRecSrcAmount+Double.valueOf(resultSrcNRAndAmtDv.getString("amount"));
					}
				}

				log.info("unRecSrcCount at viewId :"+dv.getId()+" is :"+unRecSrcCount);
				log.info("UnRecSrcAmount at viewId :"+dv.getId()+" is :"+UnRecSrcAmount);



				//log.info("trgUnRecQuery :"+trgUnRecQuery);

				stmtTrgNRAndAmtDv = conn.createStatement();
				resultTrgNRAndAmtDv = stmtTrgNRAndAmtDv.executeQuery(trgUnRecQuery);

				while(resultTrgNRAndAmtDv.next())
				{
					if(resultTrgNRAndAmtDv.getString("age").equalsIgnoreCase("yes"))
					{
						unRecTrgCount=unRecTrgCount+Double.valueOf(resultTrgNRAndAmtDv.getString("count"));
						UnRecTrgAmount=UnRecTrgAmount+Double.valueOf(resultTrgNRAndAmtDv.getString("amount"));
					}
				}

				log.info("unRecTrgCount at viewId :"+innerTargetDv.getId()+" is :"+unRecTrgCount);
				log.info("UnRecTrgAmount at viewId :"+innerTargetDv.getId()+" is :"+UnRecTrgAmount);

			}



			Double unRecSrcCountPer=0d;
			Double unRecSrcAmtPer=0d;

			Double unRecTrgCountPer=0d;
			Double unRecTrgAmtPer=0d;

			if(totalSrcCount>0)
				unRecSrcCountPer=(unRecSrcCount/totalSrcCount)*100;
			if(totalSrcAmount>0)
				unRecSrcAmtPer=(UnRecSrcAmount/totalSrcAmount)*100;

			if(totalTrgCount>0)
				unRecTrgCountPer=(unRecTrgCount/totalTrgCount)*100;
			if(totalTrgAmount>0)
				unRecTrgAmtPer=(UnRecTrgAmount/totalTrgAmount)*100;


			List<LinkedHashMap> srcTrgComMapList=new ArrayList<LinkedHashMap>();



			/**Src map**/
			LinkedHashMap eachSrcMap=new LinkedHashMap();
			eachSrcMap.put("dvCount", totalSrcCount);
			eachSrcMap.put("dvAmt", totalSrcAmount);
			eachSrcMap.put("viewId", dv.getId());
			eachSrcMap.put("viewIdDisplay", dv.getIdForDisplay());
			eachSrcMap.put("viewName", dv.getDataViewDispName());
			eachSrcMap.put("viewType", "Source");
			eachSrcMap.put("amountPer",unRecSrcAmtPer);
			eachSrcMap.put("countPer",unRecSrcCountPer);
			srcTrgComMapList.add(eachSrcMap);


			/**target Map**/
			LinkedHashMap eachTrgMap=new LinkedHashMap();
			eachTrgMap.put("dvCount", totalTrgCount);
			eachTrgMap.put("dvAmt", totalTrgAmount);
			eachTrgMap.put("viewId", innerTargetDv.getId());
			eachTrgMap.put("viewIdDisplay", innerTargetDv.getIdForDisplay());
			eachTrgMap.put("viewName", innerTargetDv.getDataViewDispName());
			eachTrgMap.put("viewType", "Target");
			eachTrgMap.put("amountPer",unRecTrgAmtPer);
			eachTrgMap.put("countPer",unRecTrgCountPer);
			srcTrgComMapList.add(eachTrgMap);


			combination.put("combination", srcTrgComMapList);

		}
		catch(Exception e)
		{
			log.info("Exception while fetching data: "+e);
		}
		finally
		{
			try{
				if(resultSrcTcAndAmtDv != null)
				{
					resultSrcTcAndAmtDv.close();
					checkConnection = false;
				}
				if(resultTrgTcAndAmtDv != null)
				{
					resultTrgTcAndAmtDv.close();
					checkConnection = false;
				}
				if(resultSrcNRAndAmtDv!=null)
				{
					resultSrcNRAndAmtDv.close();
					checkConnection = false;
				}
				if(resultTrgNRAndAmtDv!=null)
				{
					resultTrgNRAndAmtDv.close();
					checkConnection = false;
				}
				if(stmtSrcTcAndAmtDv != null)
				{
					stmtSrcTcAndAmtDv.close();
					checkConnection = false;
				}
				if(stmtTrgTcAndAmtDv != null)
				{
					stmtTrgTcAndAmtDv.close();
					checkConnection = false;
				}
				if(stmtSrcNRAndAmtDv!=null)
				{
					stmtSrcNRAndAmtDv.close();
					checkConnection = false;
				}
				if(stmtTrgNRAndAmtDv!=null)
				{
					stmtTrgNRAndAmtDv.close();
					checkConnection = false;
				}
				if(conn != null)
				{
					conn.close();
					checkConnection = false;
				}
			}
			catch(Exception ex)

			{
				log.info("Exception while closing statements:"+ex);
			}
		}
		
		log.info("Checking Connection Close in getSrcTargetCombinationViewsByRuleGrpofUnReconciledForViolationAndAgingService:"+checkConnection);

		return combination;

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	public List<LinkedHashMap> getSrcTargetCombinationViewsByRuleGrpofUnAccountedForViolationAndAgingService(LocalDate fDate,LocalDate tDate,BigInteger srcViewId,
			Long ruleGroupId,String age,Integer violation,String type,Long bucketId,String fileDateOrQualifier) 
	{
		Boolean checkConnection = false;
		Connection conn = null;
		Statement stmtSrcTcAndAmtDv = null;
		ResultSet resultSrcTcAndAmtDv = null;

		Double totalSrcCount=0d;
		Double totalSrcAmount=0d;

		Statement stmtSrcNRAndAmtDv=null;
		ResultSet resultSrcNRAndAmtDv=null;

		Double unRecSrcCount=0d;
		Double UnRecSrcAmount=0d;


		List<LinkedHashMap> srcTrgComMapList=new ArrayList<LinkedHashMap>();

		//Src dataview
		try
		{



			List<BigInteger> accountedIds=new ArrayList<BigInteger>();


			List<BigInteger> accountedRowIds=accountedSummaryRepository.fetchAccountingIdsByStatusNGroupIdNViewId(ruleGroupId, srcViewId.longValue(),"ACCOUNTED");
			accountedIds.addAll(accountedRowIds);

			log.info("accountedRowIds size :"+accountedRowIds.size());

			String accountedIdsList=accountedIds.toString().replaceAll("\\[", "").replaceAll("\\]", "");


			DataViews dv = dataViewsRepository.findOne(srcViewId.longValue());


			String srcAmountQualifier=reconciliationResultService.getViewColumnQualifier(srcViewId, "AMOUNT");
			log.info("srcAmountQualifier :"+srcAmountQualifier);





			String srcTotalQuery="select COUNT(scrIds) as count ,ROUND(SUM(`"+srcAmountQualifier+"`), 2) AS amount from `"+dv.getDataViewName().toLowerCase()+"` "
					+ "where DATE("+fileDateOrQualifier+")>='"+fDate+"' and DATE("+fileDateOrQualifier+") <='"+tDate+"'";


			DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
			conn = ds.getConnection();
			checkConnection = true;
			log.info("Checking Connection Open in getSrcTargetCombinationViewsByRuleGrpofUnAccountedForViolationAndAgingService:"+checkConnection);

			stmtSrcTcAndAmtDv = conn.createStatement();
			resultSrcTcAndAmtDv = stmtSrcTcAndAmtDv.executeQuery(srcTotalQuery);

			while(resultSrcTcAndAmtDv.next())
			{
				totalSrcCount=totalSrcCount+Double.valueOf(resultSrcTcAndAmtDv.getString("count"));
				totalSrcAmount=totalSrcAmount+Double.valueOf(resultSrcTcAndAmtDv.getString("amount"));
			}
			log.info("totalSrcCount: "+totalSrcCount);
			log.info("totalSrcAmount: "+totalSrcAmount);





			String srcUnRecQuery="";
			String trgUnRecQuery="";




			if(age==null && violation!=null)
			{
				srcUnRecQuery=	"select if( DATEDIFF(CURDATE(), `"+fileDateOrQualifier+"`) >="+violation+" ,'yes','no') as age ,COUNT(scrIds) as count ,ROUND(SUM(`"+srcAmountQualifier+"`), 2) AS amount from `"+dv.getDataViewName().toLowerCase()+"` "
						+ "where scrIds not in ("+accountedIdsList+") and DATE("+fileDateOrQualifier+")>='"+fDate+"' and DATE("+fileDateOrQualifier+") <='"+tDate+"' group by age ORDER BY age";

				//log.info("srcUnRecQuery :"+srcUnRecQuery);

				stmtSrcNRAndAmtDv = conn.createStatement();
				resultSrcNRAndAmtDv = stmtSrcNRAndAmtDv.executeQuery(srcUnRecQuery);

				while(resultSrcNRAndAmtDv.next())
				{
					if(resultSrcNRAndAmtDv.getString("age").equalsIgnoreCase("yes"))
					{
						unRecSrcCount=unRecSrcCount+Double.valueOf(resultSrcNRAndAmtDv.getString("count"));
						UnRecSrcAmount=UnRecSrcAmount+Double.valueOf(resultSrcNRAndAmtDv.getString("amount"));
					}
				}

				log.info("unRecSrcCount at viewId :"+dv.getId()+" is :"+unRecSrcCount);
				log.info("UnRecSrcAmount at viewId :"+dv.getId()+" is :"+UnRecSrcAmount);



			}


			if(age!=null && violation==null)
			{


				if(age.contains("-"))
				{
					log.info("age contains -");
					String[] str=age.split("-");
					log.info("str at 0:"+str[0]);
					log.info("str at 1:"+str[1]);

					if(accountedRowIds.size()>0)
					{
						srcUnRecQuery=	"select if( DATEDIFF(CURDATE(), `"+fileDateOrQualifier+"`) >="+str[0]+" and DATEDIFF(CURDATE(), `fileDate`)<="+str[1]+" ,'yes','no') as age ,COUNT(scrIds) as count ,ROUND(SUM(`"+srcAmountQualifier+"`), 2) AS amount from `"+dv.getDataViewName().toLowerCase()+"` "
								+ "where scrIds not in ("+accountedIdsList+") and DATE("+fileDateOrQualifier+")>='"+fDate+"' and DATE("+fileDateOrQualifier+") <='"+tDate+"' group by age ORDER BY age";
					}
					else
					{
						srcUnRecQuery=	"select if( DATEDIFF(CURDATE(), `"+fileDateOrQualifier+"`) >="+str[0]+" and DATEDIFF(CURDATE(), `fileDate`)<="+str[1]+" ,'yes','no') as age ,COUNT(scrIds) as count ,ROUND(SUM(`"+srcAmountQualifier+"`), 2) AS amount from `"+dv.getDataViewName().toLowerCase()+"` "
								+ "where DATE("+fileDateOrQualifier+")>='"+fDate+"' and DATE("+fileDateOrQualifier+") <='"+tDate+"' group by age ORDER BY age";
					}

					//log.info("srcUnRecQuery :"+srcUnRecQuery);


				}
				else if(age.contains(">") || (age.contains("<")))
				{
					String[] str=age.split("=");

					if(age.contains(">") )
					{
						if(accountedRowIds.size()>0)
						{
							srcUnRecQuery=	"select if( DATEDIFF(CURDATE(), `"+fileDateOrQualifier+"`) >="+str[1]+" ,'yes','no') as age ,COUNT(scrIds) as count ,ROUND(SUM(`"+srcAmountQualifier+"`), 2) AS amount from `"+dv.getDataViewName().toLowerCase()+"` "
									+ "where scrIds not in ("+accountedIdsList+") and DATE("+fileDateOrQualifier+")>='"+fDate+"' and DATE("+fileDateOrQualifier+") <='"+tDate+"' group by age ORDER BY age";
						}
						else
						{
							srcUnRecQuery=	"select if( DATEDIFF(CURDATE(), `"+fileDateOrQualifier+"`) >="+str[1]+" ,'yes','no') as age ,COUNT(scrIds) as count ,ROUND(SUM(`"+srcAmountQualifier+"`), 2) AS amount from `"+dv.getDataViewName().toLowerCase()+"` "
									+ "where DATE("+fileDateOrQualifier+")>='"+fDate+"' and DATE("+fileDateOrQualifier+") <='"+tDate+"' group by age ORDER BY age";
						}

					}
					else if(age.contains("<"))
					{
						if(accountedRowIds.size()>0)
						{
							srcUnRecQuery=	"select if( DATEDIFF(CURDATE(), `"+fileDateOrQualifier+"`) <="+str[1]+" ,'yes','no') as age ,COUNT(scrIds) as count ,ROUND(SUM(`"+srcAmountQualifier+"`), 2) AS amount from `"+dv.getDataViewName().toLowerCase()+"` "
									+ "where scrIds not in ("+accountedIdsList+") and DATE("+fileDateOrQualifier+")>='"+fDate+"' and DATE("+fileDateOrQualifier+") <='"+tDate+"' group by age ORDER BY age";
						}
						else
						{
							srcUnRecQuery=	"select if( DATEDIFF(CURDATE(), `"+fileDateOrQualifier+"`) <="+str[1]+" ,'yes','no') as age ,COUNT(scrIds) as count ,ROUND(SUM(`"+srcAmountQualifier+"`), 2) AS amount from `"+dv.getDataViewName().toLowerCase()+"` "
									+ "where DATE("+fileDateOrQualifier+")>='"+fDate+"' and DATE("+fileDateOrQualifier+") <='"+tDate+"' group by age ORDER BY age";
						}

					}

				}
				else if(age.equalsIgnoreCase("others"))
				{
					log.info("in others group");


					log.info("in others group");
					String subQuery="";
					List<BucketDetails> buckDetails=bucketDetailsRepository.findByBucketId(bucketId);

					for(int j=0;j<buckDetails.size();j++){
						int buckVal=j+1;
						BucketDetails bucketDet=buckDetails.get(j);
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

							if(from!=null && to!=null){
								if(from>to){

									frmVal=to;
									toVal=from;
								}
								else{
									frmVal=from;
									toVal=to;
								}


								String ruleAge=frmVal+"-"+toVal;
								subQuery=subQuery+"(DATEDIFF(CURDATE(),`"+fileDateOrQualifier+"`) not between "+frmVal+" and "+toVal+") and";



							}
							else{

								log.info("any one of the bucket limits are null");

								if(to==null){
									String ruleAge=">="+from;
									subQuery=subQuery+"(DATEDIFF(CURDATE(),`"+fileDateOrQualifier+"`) > "+from+") and ";
								}
								if(from==null){
									String ruleAge="<="+to;
									subQuery=subQuery+"(DATEDIFF(CURDATE(),`"+fileDateOrQualifier+"`) < "+to+") and ";
								}


							}
						}
					}


					log.info("in other subquery :"+ subQuery);
					String finalSubQuery = subQuery.substring(0,subQuery.length()-3);
					log.info("in other finalSubQuery :"+ finalSubQuery);
					finalSubQuery =finalSubQuery+",'Yes','No') as age";

					if(accountedRowIds.size()>0)
					{
						srcUnRecQuery=	"select if("+finalSubQuery+" ,COUNT(scrIds) as count ,ROUND(SUM(`"+srcAmountQualifier+"`), 2) AS amount from `"+dv.getDataViewName().toLowerCase()+"` "
								+ "where scrIds not in ("+accountedIdsList+") and DATE("+fileDateOrQualifier+")>='"+fDate+"' and DATE("+fileDateOrQualifier+") <='"+tDate+"' group by age ORDER BY age";
					}
					else
					{
						srcUnRecQuery=	"select if("+finalSubQuery+" ,COUNT(scrIds) as count ,ROUND(SUM(`"+srcAmountQualifier+"`), 2) AS amount from `"+dv.getDataViewName().toLowerCase()+"` "
								+ "where DATE("+fileDateOrQualifier+")>='"+fDate+"' and DATE("+fileDateOrQualifier+") <='"+tDate+"' group by age ORDER BY age";
					}




				}





				//	log.info("srcUnRecQuery :"+srcUnRecQuery);

				stmtSrcNRAndAmtDv = conn.createStatement();
				resultSrcNRAndAmtDv = stmtSrcNRAndAmtDv.executeQuery(srcUnRecQuery);

				while(resultSrcNRAndAmtDv.next())
				{
					if(resultSrcNRAndAmtDv.getString("age").equalsIgnoreCase("yes"))
					{
						unRecSrcCount=unRecSrcCount+Double.valueOf(resultSrcNRAndAmtDv.getString("count"));
						UnRecSrcAmount=UnRecSrcAmount+Double.valueOf(resultSrcNRAndAmtDv.getString("amount"));
					}
				}

				log.info("unRecSrcCount at viewId :"+dv.getId()+" is :"+unRecSrcCount);
				log.info("UnRecSrcAmount at viewId :"+dv.getId()+" is :"+UnRecSrcAmount);

			}



			Double unRecSrcCountPer=0d;
			Double unRecSrcAmtPer=0d;



			if(totalSrcCount>0)
				unRecSrcCountPer=(unRecSrcCount/totalSrcCount)*100;
			if(totalSrcAmount>0)
				unRecSrcAmtPer=(UnRecSrcAmount/totalSrcAmount)*100;





			LinkedHashMap combination=new LinkedHashMap();


			/**Src map**/
			LinkedHashMap eachSrcMap=new LinkedHashMap();
			eachSrcMap.put("dvCount", totalSrcCount);
			eachSrcMap.put("dvAmt", totalSrcAmount);
			eachSrcMap.put("viewId", dv.getId());
			eachSrcMap.put("viewIdDisplay", dv.getIdForDisplay());
			eachSrcMap.put("viewName", dv.getDataViewDispName());
			eachSrcMap.put("viewType", "Source");
			eachSrcMap.put("amountPer",unRecSrcAmtPer);
			eachSrcMap.put("countPer",unRecSrcCountPer);
			srcTrgComMapList.add(eachSrcMap);





		}
		catch(Exception e)
		{
			log.info("Exception while fetching data: "+e);
		}
		finally
		{

			try
			{
				if(resultSrcTcAndAmtDv != null)
				{
					resultSrcTcAndAmtDv.close();
					checkConnection = false;
				}
				if(resultSrcNRAndAmtDv!=null)
				{
					resultSrcNRAndAmtDv.close();
					checkConnection = false;
				}
				if(stmtSrcNRAndAmtDv!=null)
				{
					stmtSrcNRAndAmtDv.close();
					checkConnection = false;
				}
				if(stmtSrcTcAndAmtDv!=null)
				{
					stmtSrcTcAndAmtDv.close();
					checkConnection = false;
				}
				if(conn != null)
				{
					conn.close();
					checkConnection = false;
				}
			}
			catch(Exception ex)
			{
				log.info("Exception while closing statements :"+ex);
			}
		}

		log.info("Checking Connection Close in getSrcTargetCombinationViewsByRuleGrpofUnAccountedForViolationAndAgingService:"+checkConnection);

		return srcTrgComMapList;

	}
	
	
	
	
	public List<LinkedHashMap> getSrcTargetCombinationViewsByRuleGrpofUnAccountedOrAccountedOrJeCreated(LocalDate fDate,LocalDate tDate,BigInteger srcViewId,Long ruleGroupId,String type,String fileDateOrQualifier) 
	{

		Boolean checkConnection = false;
		Connection conn = null;
		Statement stmtSrcTcAndAmtDv = null;
		ResultSet resultSrcTcAndAmtDv = null;

		Double totalSrcCount=0d;
		Double totalSrcAmount=0d;

		Statement stmtSrcNRAndAmtDv=null;
		ResultSet resultSrcNRAndAmtDv=null;

		Double unRecSrcCount=0d;
		Double UnRecSrcAmount=0d;


		List<LinkedHashMap> srcTrgComMapList=new ArrayList<LinkedHashMap>();

		//Src dataview
		try
		{



			List<BigInteger> finalSrcIds=new ArrayList<BigInteger>();

			if(type.equalsIgnoreCase("un-accounted") || type.equalsIgnoreCase("Je pending"))
			{
				List<BigInteger> accountedRowIds=accountedSummaryRepository.fetchAccountingIdsByStatusNGroupIdNViewId(ruleGroupId, srcViewId.longValue(),"ACCOUNTED");
				finalSrcIds.addAll(accountedRowIds);
			}
			else if(type.equalsIgnoreCase("Je Created"))
			{
				List<BigInteger> accountedRowIds=accountedSummaryRepository.fetchPostedRowIdsByRuleGrpIdAndViewId(ruleGroupId, srcViewId.longValue());
				finalSrcIds.addAll(accountedRowIds);
			}
			else if(type.equalsIgnoreCase("approvals"))
			{
				List<BigInteger> appInprocRowIds=accountingDataRepository.fetchDistinctApprovedInProcessRowIds(ruleGroupId, srcViewId.longValue(),"IN_PROCESS");
				finalSrcIds.addAll(appInprocRowIds);
			}
			log.info("finalSrcIds.size() :"+finalSrcIds.size());
			String accountedIdsList=finalSrcIds.toString().replaceAll("\\[", "").replaceAll("\\]", "");


			DataViews dv = dataViewsRepository.findOne(srcViewId.longValue());


			String srcAmountQualifier=reconciliationResultService.getViewColumnQualifier(srcViewId, "AMOUNT");
			log.info("srcAmountQualifier :"+srcAmountQualifier);





			String srcTotalQuery="select COUNT(scrIds) as count ,case when ROUND(SUM(`"+srcAmountQualifier+"`), 2) is null then 0 else ROUND(SUM(`"+srcAmountQualifier+"`), 2) end AS amount from `"+dv.getDataViewName().toLowerCase()+"` "
					+ "where DATE("+fileDateOrQualifier+")>='"+fDate+"' and DATE("+fileDateOrQualifier+") <='"+tDate+"'";
			//log.info("srcTotalQuery: "+srcTotalQuery);


			DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
			conn = ds.getConnection();	
			checkConnection = true;
			log.info("Checking Connection Open in getSrcTargetCombinationViewsByRuleGrpofUnAccountedOrAccountedOrJeCreated:"+checkConnection);
			stmtSrcTcAndAmtDv = conn.createStatement();
			resultSrcTcAndAmtDv = stmtSrcTcAndAmtDv.executeQuery(srcTotalQuery);

			while(resultSrcTcAndAmtDv.next())
			{
				totalSrcCount=totalSrcCount+Double.valueOf(resultSrcTcAndAmtDv.getString("count"));
				totalSrcAmount=totalSrcAmount+Double.valueOf(resultSrcTcAndAmtDv.getString("amount"));
			}
			log.info("totalSrcCount: "+totalSrcCount);
			log.info("totalSrcAmount: "+totalSrcAmount);





			String srcUnRecQuery="";
			String trgUnRecQuery="";



			if(type.equalsIgnoreCase("un-accounted"))
			{
				if(finalSrcIds.size()>0)
				{
					srcUnRecQuery=	"select COUNT(scrIds) as count ,ROUND(SUM(`"+srcAmountQualifier+"`), 2) AS amount from `"+dv.getDataViewName().toLowerCase()+"` "
							+ "where scrIds not in ("+accountedIdsList+") and DATE("+fileDateOrQualifier+")>='"+fDate+"' and DATE("+fileDateOrQualifier+") <='"+tDate+"'";
				}
				else
				{
					srcUnRecQuery=	"select COUNT(scrIds) as count ,ROUND(SUM(`"+srcAmountQualifier+"`), 2) AS amount from `"+dv.getDataViewName().toLowerCase()+"` "
							+ "where DATE("+fileDateOrQualifier+")>='"+fDate+"' and DATE("+fileDateOrQualifier+") <='"+tDate+"'";
				}
			}
			else if(type.equalsIgnoreCase("Je pending") || type.equalsIgnoreCase("Je Created") || type.equalsIgnoreCase("approvals"))
			{
				srcUnRecQuery=	"select COUNT(scrIds) as count ,ROUND(SUM(`"+srcAmountQualifier+"`), 2) AS amount from `"+dv.getDataViewName().toLowerCase()+"` "
						+ "where scrIds in ("+accountedIdsList+") and DATE("+fileDateOrQualifier+")>='"+fDate+"' and DATE("+fileDateOrQualifier+") <='"+tDate+"'";
			}


			//log.info("srcUnRecQuery :"+srcUnRecQuery);

			stmtSrcNRAndAmtDv = conn.createStatement();
			resultSrcNRAndAmtDv = stmtSrcNRAndAmtDv.executeQuery(srcUnRecQuery);

			while(resultSrcNRAndAmtDv.next())
			{
				unRecSrcCount=unRecSrcCount+Double.valueOf(resultSrcNRAndAmtDv.getString("count"));
				if(resultSrcNRAndAmtDv.getString("amount")!=null)
					UnRecSrcAmount=UnRecSrcAmount+Double.valueOf(resultSrcNRAndAmtDv.getString("amount"));
			}

			log.info("unRecSrcCount at viewId :"+dv.getId()+" is :"+unRecSrcCount);
			log.info("UnRecSrcAmount at viewId :"+dv.getId()+" is :"+UnRecSrcAmount);









			Double unRecSrcCountPer=0d;
			Double unRecSrcAmtPer=0d;



			if(totalSrcCount>0)
				unRecSrcCountPer=(unRecSrcCount/totalSrcCount)*100;
			if(totalSrcAmount>0)
				unRecSrcAmtPer=(UnRecSrcAmount/totalSrcAmount)*100;





			LinkedHashMap combination=new LinkedHashMap();


			/**Src map**/
			LinkedHashMap eachSrcMap=new LinkedHashMap();
			eachSrcMap.put("dvCount", totalSrcCount);
			eachSrcMap.put("dvAmt", totalSrcAmount);
			eachSrcMap.put("viewId", dv.getId());
			eachSrcMap.put("viewIdDisplay", dv.getIdForDisplay());
			eachSrcMap.put("viewName", dv.getDataViewDispName());
			eachSrcMap.put("viewType", "Source");
			eachSrcMap.put("amountPer",unRecSrcAmtPer);
			eachSrcMap.put("countPer",unRecSrcCountPer);
			srcTrgComMapList.add(eachSrcMap);





		}
		catch(Exception e)
		{
			log.info("Exception while fetching data: "+e);
		}
		finally
		{
			try
			{
				if(resultSrcTcAndAmtDv != null)
				{
					resultSrcTcAndAmtDv.close();
					checkConnection = false;
				}
				if(resultSrcNRAndAmtDv!=null)
				{
					resultSrcNRAndAmtDv.close();
					checkConnection = false;
				}
				if(stmtSrcNRAndAmtDv!=null)
				{
					stmtSrcNRAndAmtDv.close();
					checkConnection = false;
				}
				if(stmtSrcTcAndAmtDv != null)
				{
					stmtSrcTcAndAmtDv.close();
					checkConnection = false;
				}
				if(conn != null)
				{
					conn.close();
					checkConnection = false;
				}
			}

			catch(Exception ex)
			{
				log.info("Exception while clossing Statements :"+ex);
			}
		}
		log.info("Checking Connection Open in getSrcTargetCombinationViewsByRuleGrpofUnAccountedOrAccountedOrJeCreated:"+checkConnection);

		return srcTrgComMapList;

	}
	
	
	/**service to check multicurrencies for views of a rule group**/
	public LinkedHashMap checkMultiCurrency(Long ruleGroupId,Long tenantId, HashMap<String, List<BigInteger>> distinctViewIdMap/*, String fDate, String tDate*/)
	{
		Boolean checkConnection = false;
		LinkedHashMap multiCurrencyMap=new LinkedHashMap();
		Connection conn = null;
		Statement stmtDv = null;
		ResultSet resultDv=null;
		try
		{
			DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
			conn = ds.getConnection();
			checkConnection = true;
			stmtDv = conn.createStatement();
			List<BigInteger> viewIds=new ArrayList<BigInteger>();
			viewIds.addAll(distinctViewIdMap.get("sourceViewIds"));
			viewIds.addAll(distinctViewIdMap.get("targeViewIds"));
   			log.info("viewIds :"+viewIds+" at rule group id :"+ruleGroupId);
			String query="";
			for(BigInteger viewId : viewIds)
			{
				DataViews dv=dataViewsRepository.findOne(viewId.longValue());
				String currencyCode = reconciliationResultService.getViewColumnQualifier(viewId, "CURRENCYCODE");
				query="select distinct(`"+currencyCode+"`) from `"+dv.getDataViewName().toLowerCase()+"`";/*+"` v where Date(fileDate) between '"+fDate+"' and '"+tDate+"'"*/;
				log.info("Query to check multi currency:"+query);
				resultDv=stmtDv.executeQuery(query);
				List<String> currencyCodeList=new ArrayList<String>();
				while(resultDv.next())
				{
					currencyCodeList.add(resultDv.getString(currencyCode));
				}
				log.info("currencyCodeList size :"+currencyCodeList.size());
				if(currencyCodeList.size()>1)
				{
					multiCurrencyMap.put("multiCurrency", "true");
					multiCurrencyMap.put("currencyCode", "*");
					break;
				}
				else
				{
					multiCurrencyMap.put("multiCurrency", "false");
					multiCurrencyMap.put("currencyCode", currencyCodeList.get(0).toString());
				}
			}
		}
		catch(Exception ex)
		{
			log.info("Exception while fetching multi currencies :"+ex);
		}
		finally
		{
			try
			{
				if(stmtDv!=null)
				{
					stmtDv.close();
					checkConnection = false;
				}
				if(resultDv!=null)
				{
					resultDv.close();
					checkConnection = false;
				}
				if(conn!=null)
				{
					conn.close();
					checkConnection = false;
				}
			}
			catch(Exception ex)
			{
				log.info("Exception while closing Statements :"+ex);
			}
		}
		log.info("Checking Connection Close in checkMultiCurrency:"+checkConnection);
		return multiCurrencyMap;
	}
	

	public LinkedHashMap getCurrencyCodeStatusAndValueFromAppModuleSummary(Long ruleGroupId,LocalDate fDate,LocalDate tDate,Long tenantId)
	{
		LinkedHashMap currencyMap=new LinkedHashMap();

		HashMap<String, List<BigInteger>> distinctViewIdMap = reconciliationResultService.getDistinctDVIdsforRuleGrp(ruleGroupId, tenantId);
		List<BigInteger> viewIds=new ArrayList<BigInteger>();
		viewIds.addAll(distinctViewIdMap.get("sourceViewIds"));
		viewIds.addAll(distinctViewIdMap.get("targeViewIds"));
		log.info("viewIds :"+viewIds+" at rule group id :"+ruleGroupId);
		String query="";
		for(int i=0;i<viewIds.size();i++)
		{
			List<String> currencyCode=appModuleSummaryRepository.findByRuleGroupIdAndViewId(ruleGroupId,fDate,tDate,viewIds.get(i).longValue());
			log.info("currencyCodeList size :"+currencyCode.size());
			if(currencyCode.size()==0)
			{
				currencyMap.put("multiCurrency", "true");
				break;
			}
			if(currencyCode.size()>1)
			{
				currencyMap.put("multiCurrency", "true");
				break;
			}
			else if (currencyCode.size()==1)
			{
				currencyMap.put("multiCurrency", "false");
				//currencyMap.put("currencyCode", currencyCode);
			}
		}

		return currencyMap;

	}
	
	public String getFileDateOrQualifier(Long viewId,Long tenantId)
	{
		TenantConfig viewByDate=tenantConfigRepository.findByTenantIdAndKey(tenantId, "SUMMARY_VIEW_BY");
		String date="";
		if(viewByDate.getValue().equalsIgnoreCase("FILE_DATE"))
		{
			//date="fileDate";	
			String transDateQuailfier=reconciliationResultService.getViewColumnQualifier(BigInteger.valueOf(viewId), "FILEDATE");
			date=transDateQuailfier;
		}
		else if(viewByDate.getValue().equalsIgnoreCase("QUALIFIER_DATE"))
		{
			String transDateQuailfier=reconciliationResultService.getViewColumnQualifier(BigInteger.valueOf(viewId), "TRANSDATE");
			date=transDateQuailfier;
		}
		log.info("date in getFileDateOrQualifier :"+date);
		return date;
		
	}
	public static HashMap updateMultiCurrency(LinkedHashMap multiValues, Long groupId) throws SQLException
	{
		Boolean checkConnection = false;
		HashMap finalMap = new HashMap();
		Connection conn = null;
    	Statement multiCurrencyStmt = null;
    	Statement updateExistedRecordStmt = null;
    	ResultSet multiCurrencyRS = null;
    	List<Long> groupIds = new ArrayList<Long>();
		try
		{
			
			DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
			conn = ds.getConnection();
			checkConnection = true;
			//log.info("Checking Connection Open in updateMultiCurrency:"+checkConnection);
			
			String currencyCode = multiValues.get("currencyCode").toString();
			String multiCurrency = multiValues.get("multiCurrency").toString();
			String multiCurrencyQuery = "select id from t_app_module_summary where rule_group_id = "+groupId;
			multiCurrencyStmt = conn.createStatement();
			multiCurrencyRS = multiCurrencyStmt.executeQuery(multiCurrencyQuery);
			while(multiCurrencyRS.next())
			{
				groupIds.add(Long.parseLong(multiCurrencyRS.getString(1)));
			}
			updateExistedRecordStmt = conn.createStatement();
			if(groupIds.size()>0)
			{
				for(Long id : groupIds)
				{
					String updateQuery = "update t_app_module_summary set currency_code = '"+currencyCode+"', multi_currency = "+multiCurrency+" where id = "+id+"";
					updateExistedRecordStmt.addBatch(updateQuery);
		    		//updateExistedRecordStmt.executeUpdate(updateQuery);
					// updateExistedRecordStmt.addBatch(updateQuery);
				}
/*				int[] updated = updateExistedRecordStmt.executeBatch();
				System.out.println(updated+" records has been updated with multi currency status");*/
			}
			updateExistedRecordStmt.executeBatch();
		}
		catch(Exception e)
		{
			System.out.println("Exception while updating multi currency records: "+e);
		}
		finally
		{
			try
			{
				if(multiCurrencyRS != null)
				{
					multiCurrencyRS.close();
					checkConnection = false;
					}
				if(updateExistedRecordStmt != null)
				{
					updateExistedRecordStmt.close();
					checkConnection = false;
				}
				if(multiCurrencyStmt != null)
				{
					multiCurrencyStmt.close();
					checkConnection = false;
				}
				if(conn != null)
				{
					conn.close();
					checkConnection = false;
				}
			}
			catch(Exception e)
			{
				System.out.println("Exception while closing statements: "+e);
			}
		}
		//log.info("Checking Connection Close in updateMultiCurrency:"+checkConnection);
		return finalMap;
	}
	
	
	public String getFileDateOrQualifierAndGetColumnName(Long viewId,Long tenantId)
	{
		TenantConfig viewByDate=tenantConfigRepository.findByTenantIdAndKey(tenantId, "SUMMARY_VIEW_BY");
		log.info("viewByDate :"+viewByDate);
		String date="";
		if(viewByDate.getValue().equalsIgnoreCase("FILE_DATE"))
		{
			date="fileDate";	
		}
		else if(viewByDate.getValue().equalsIgnoreCase("QUALIFIER_DATE"))
		{
			
			DataViewsColumns dvc = dataViewsColumnsRepository.findByDataViewIdAndQualifier(viewId,"TRANSDATE");
			log.info("dvc :"+dvc);
			
			FileTemplateLines ftl=fileTemplateLinesRepository.findOne(Long.valueOf(dvc.getRefDvColumn()));
			log.info("ftl :"+ftl);
			String strField=ftl.getMasterTableReferenceColumn().toLowerCase().replaceAll("_", "");
			
			date=strField;
		}
		return date;
		
	}

}