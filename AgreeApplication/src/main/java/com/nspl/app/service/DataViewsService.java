package com.nspl.app.service;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.apache.commons.collections.MapUtils;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import com.nspl.app.config.ApplicationContextProvider;
import com.nspl.app.domain.DataViewConditions;
import com.nspl.app.domain.DataViewFilters;
import com.nspl.app.domain.DataViewUnion;
import com.nspl.app.domain.DataViews;
import com.nspl.app.domain.DataViewsColumns;
import com.nspl.app.domain.DataViewsSrcMappings;
import com.nspl.app.domain.FileTemplateLines;
import com.nspl.app.domain.FileTemplates;
import com.nspl.app.domain.IntermediateTable;
import com.nspl.app.domain.LookUpCode;
import com.nspl.app.domain.ReportDefination;
import com.nspl.app.domain.ReportParameters;
import com.nspl.app.domain.Reports;
import com.nspl.app.repository.DataMasterRepository;
import com.nspl.app.repository.DataViewConditionsRepository;
import com.nspl.app.repository.DataViewFiltersRepository;
import com.nspl.app.repository.DataViewUnionRepository;
import com.nspl.app.repository.DataViewsColumnsRepository;
import com.nspl.app.repository.DataViewsRepository;
import com.nspl.app.repository.DataViewsSrcMappingsRepository;
import com.nspl.app.repository.FileTemplateLinesRepository;
import com.nspl.app.repository.FileTemplatesRepository;
import com.nspl.app.repository.IntermediateTableRepository;
import com.nspl.app.repository.LookUpCodeRepository;
import com.nspl.app.repository.ReportDefinationRepository;
import com.nspl.app.repository.ReportParametersRepository;
import com.nspl.app.repository.ReportsRepository;
import com.nspl.app.web.rest.dto.ErrorReport;
import com.nspl.app.web.rest.dto.StatusStringDTO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DataViewsService {

	private final Logger log = LoggerFactory.getLogger(DataViewsService.class);

	@Inject
	DataViewsRepository dataViewsRepository;

	@Inject
	DataViewsColumnsRepository dataViewsColumnsRepository;

	@Inject
	DataViewFiltersRepository dataViewFiltersRepository;

	@Inject
	FileTemplatesRepository fileTemplatesRepository;

	@Inject
	FileTemplateLinesRepository fileTemplateLinesRepository;

	@Inject
	DataViewConditionsRepository dataViewConditionsRepository;

	@Inject
	DataMasterRepository dataMasterRepository;

	@Inject
	ExcelFunctionsService excelFunctionsService;

	@Inject
	SourceConnectionDetailsService sourceConnectionDetailsService;

	@Inject
	DataViewsSrcMappingsRepository dataViewsSrcMappingsRepository;

	@Inject
	ReportDefinationRepository reportDefinationRepository;

	@Inject
	ReportsRepository reportsRepository;

	@Inject
	ReportParametersRepository reportParametersRepository;

	@Inject
	DataViewUnionRepository dataViewUnionRepository;

	@Inject
	private Environment env;

	@Inject
	IntermediateTableRepository intermediateTableRepository;

	@Inject
	LookUpCodeRepository lookUpCodeRepository;

	/**
	 * Author: Swetha Description: Function to frame data view query
	 * 
	 * @param viewId
	 * @return
	 * @throws ClassNotFoundException
	 */
	public String frameQuery(Long viewId, Long tenanatId)
			throws ClassNotFoundException {

		log.info("Request frameQuery for viewId: " + viewId
				+ " and tenanatId: " + tenanatId);
		DataViews dv = dataViewsRepository.findOne(viewId);
		log.info("dv: " + dv);
		List<String> tempIdList = new ArrayList<String>();

		String viewQuery = "(select ";
		tempIdList = dataViewsColumnsRepository.fetchDistinctTemplateId(viewId);
		log.info("tempIdList sz for viewId: " + viewId + " are: "
				+ tempIdList.size());

		/* Part 1 */
		String part1 = "";
		HashMap map = new HashMap();
		List<DataViewsColumns> dvColDataList = new ArrayList<DataViewsColumns>();
		List<DataViewsColumns> dvColDataListEmp = new ArrayList<DataViewsColumns>();
		List<DataViewsColumns> dvColDataList3 = new ArrayList<DataViewsColumns>();
		String srcIds = "";
		for (int j = 0; j < tempIdList.size(); j++) {

			String tempId = tempIdList.get(j);
			log.info("tempId at j: " + j + " is: " + tempId);
			String tabAlias = "";
			if (tempId != null) {
				tabAlias = "ds" + j;
				map.put(tempId, tabAlias);
				log.info("map: " + map);
			} else {
				log.info("tempId is null: " + tempId);
				log.info("map: " + map);
			}

			if (tempId != null) {
				dvColDataList = dataViewsColumnsRepository
						.findByRefDvNameAndDataViewId(tempId, viewId);
				log.info("dvColDataList for tempId: " + tempId
						+ " and viewId: " + viewId + " is: " + dvColDataList);
			} else {
				log.info("tempId is null: " + tempId);
				dvColDataListEmp = dataViewsColumnsRepository
						.findByDataViewIdAndRefDvType(viewId, "Data View");
				for (int z = 0; z < dvColDataListEmp.size(); z++) {
					DataViewsColumns dvc1 = dvColDataListEmp.get(z);
					log.info("dvc1 at" + z + ":" + dvc1);
					if (dvc1.getRefDvName() == null) {
						log.info("dvf for dvc1.getId(): " + dvc1.getId()
								+ " viewId: " + viewId);
						dvColDataList3.add(dvc1);
					}
				}
				dvColDataList = dvColDataList3;
			}
			for (int i = 0; i < dvColDataList.size(); i++) {
				DataViewsColumns DvColData = dvColDataList.get(i);
				String dType = DvColData.getColDataType();
				Boolean grpByFlag=DvColData.getGroupBy();
				log.info("dType: " + dType+" grpByFlag: "+grpByFlag);
				String qualifier = DvColData.getQualifier();
				log.info("qualifier: " + qualifier);
				String formulaQuery = "";

				if (DvColData.getRefDvColumn() != null) {
					FileTemplateLines ftl = fileTemplateLinesRepository
							.findOne(Long.parseLong(DvColData.getRefDvColumn()
									.toString()));
					log.info("ftl: " + ftl);
					log.info("ftl colalias: " + ftl.getColumnAlias());
					log.info("tabAlias: " + tabAlias);
					if (i < dvColDataList.size() - 1 && tabAlias != null
							&& !(tabAlias.isEmpty())) {
						log.info("in if tabAlias");
						if (dType != null && !(dType.isEmpty())
								&& dType.equalsIgnoreCase("INTEGER")) {
							part1 = part1
									+ " cast(trim(replace("
									+ tabAlias
									+ "."
									+ ftl.getMasterTableReferenceColumn()
									.toLowerCase()
									+ ", ',', '') ) as  signed)" + " as `"
									+ ftl.getColumnAlias() + "`";
						} else if (dType != null && !(dType.isEmpty())
								&& dType.equalsIgnoreCase("DECIMAL")) {
							if (qualifier != null && !(qualifier.isEmpty())
									&& qualifier.equalsIgnoreCase("AMOUNT")) {
								String qualifierQry = " (CASE WHEN ISNULL("
										+ tabAlias
										+ ".`vid`) THEN if("+tabAlias
										+ "."
										+ ftl.getMasterTableReferenceColumn()
										.toLowerCase()+"= '',null, cast(trim(replace("
										+ tabAlias
										+ "."
										+ ftl.getMasterTableReferenceColumn()
										.toLowerCase()
										+ ", ',', '') ) as  decimal(38,5)))"
										+ " ELSE if("+tabAlias
										+ ".`v_amount`='',null,CAST(TRIM(REPLACE("
										+ tabAlias
										+ ".`v_amount`, ',', '')) AS DECIMAL (38 , 5 ))) END) "
										+ " as " + "`" + ftl.getColumnAlias()
										+ "`";
								part1 = part1 + qualifierQry;
								log.info("amount qualifier query: "
										+ qualifierQry);
							} else
								part1 = part1
								+ " if("+tabAlias
								+ "."
								+ ftl.getMasterTableReferenceColumn()
								.toLowerCase()+"='',null,cast(trim(replace("
								+ tabAlias
								+ "."
								+ ftl.getMasterTableReferenceColumn()
								.toLowerCase()
								+ ", ',', '') ) as  decimal(38,5)))"
								+ " as " + "`" + ftl.getColumnAlias()
								+ "`";
						} else if (dType != null && !(dType.isEmpty())
								&& dType.equalsIgnoreCase("BOOLEAN")) {
							part1 = part1
									+ " (CASE WHEN ("
									+ tabAlias
									+ "."
									+ ftl.getMasterTableReferenceColumn()
									.toLowerCase()
									+ ") <> 0 THEN 'True' ELSE 'False' END) as "
									+ "`" + ftl.getColumnAlias() + "`";
						}
						else if (dType != null && !(dType.isEmpty())
								&& (dType.equalsIgnoreCase("DATE") || dType.equalsIgnoreCase("DATETIME"))) {
							part1 = part1+"if("+tabAlias
									+ "."
									+ ftl.getMasterTableReferenceColumn()
									.toLowerCase()+" is null,null,CAST("+tabAlias
									+ "."
									+ ftl.getMasterTableReferenceColumn()
									.toLowerCase()+" AS DATE )) as "+ "`" + ftl.getColumnAlias() + "`";
						}
						else if (dType != null && !(dType.isEmpty())
								&& dType.equalsIgnoreCase("VARCHAR") && grpByFlag!=null && grpByFlag.compareTo(true)==0 ) {
							part1 = part1+"if("+tabAlias
									+ "."
									+ ftl.getMasterTableReferenceColumn()
									.toLowerCase()+" is null,null,CAST("+tabAlias
									+ "."
									+ ftl.getMasterTableReferenceColumn()
									.toLowerCase()+" AS CHAR(255) )) as "+ "`" + ftl.getColumnAlias() + "`";
						}
						else
							part1 = part1
							+ " trim("
							+ tabAlias
							+ "."
							+ ftl.getMasterTableReferenceColumn()
							.toLowerCase() + " )" + " as `"
							+ ftl.getColumnAlias() + "`";

						log.info("Template columns part1 at i: " + i + " is: "
								+ part1);
					} else if (i == dvColDataList.size() - 1
							&& tabAlias != null && !(tabAlias.isEmpty())) {
						log.info("in else tabAlias");
						if (dType != null && !(dType.isEmpty())
								&& dType.equalsIgnoreCase("INTEGER")) {
							part1 = part1
									+ " cast(trim(replace("
									+ tabAlias
									+ "."
									+ ftl.getMasterTableReferenceColumn()
									.toLowerCase()
									+ ", ',', '') ) as  signed)" + " as `"
									+ ftl.getColumnAlias() + "`";
						} else if (dType != null && !(dType.isEmpty())
								&& dType.equalsIgnoreCase("DECIMAL")) {
							if (qualifier != null && !(qualifier.isEmpty())
									&& qualifier.equalsIgnoreCase("AMOUNT")) {
								String qualifierQry = " (CASE WHEN ISNULL("
										+ tabAlias
										+ ".`vid`) THEN if("+tabAlias
										+ "."
										+ ftl.getMasterTableReferenceColumn()
										.toLowerCase()+"='',null,cast(trim(replace("
										+ tabAlias
										+ "."
										+ ftl.getMasterTableReferenceColumn()
										.toLowerCase()
										+ ", ',', '') ) as  decimal(38,5)))"
										+ " ELSE if("+tabAlias
										+ ".`v_amount`='',null,CAST(TRIM(REPLACE("
										+ tabAlias
										+ ".`v_amount`, ',', '')) AS DECIMAL (38 , 5 ))) END) "
										+ " as " + "`" + ftl.getColumnAlias()
										+ "`";
								part1 = part1 + qualifierQry;
								log.info("amount qualifier query: "
										+ qualifierQry);
							} else
								part1 = part1
								+ " if("+tabAlias
								+ "."
								+ ftl.getMasterTableReferenceColumn()
								.toLowerCase()+"='',null,cast(trim(replace("
								+ tabAlias
								+ "."
								+ ftl.getMasterTableReferenceColumn()
								.toLowerCase()
								+ ", ',', '') ) as  decimal(38,5)))"
								+ " as " + "`" + ftl.getColumnAlias()
								+ "`";
						} else if (dType != null && !(dType.isEmpty())
								&& dType.equalsIgnoreCase("BOOLEAN")) {
							part1 = part1
									+ " (CASE WHEN ("
									+ tabAlias
									+ "."
									+ ftl.getMasterTableReferenceColumn()
									.toLowerCase()
									+ ") <> 0 THEN 'True' ELSE 'False' END) as "
									+ "`" + ftl.getColumnAlias() + "`";
						}else if (dType != null && !(dType.isEmpty())
								&& (dType.equalsIgnoreCase("DATE") || dType.equalsIgnoreCase("DATETIME"))) {
							part1 = part1+"if("+tabAlias
									+ "."
									+ ftl.getMasterTableReferenceColumn()
									.toLowerCase()+" is null,null,CAST("+tabAlias
									+ "."
									+ ftl.getMasterTableReferenceColumn()
									.toLowerCase()+" AS DATE)) as "+ "`" + ftl.getColumnAlias() + "`";
						}else if(dType != null && !(dType.isEmpty())
								&& dType.equalsIgnoreCase("VARCHAR") && grpByFlag!=null && grpByFlag){
							part1 = part1+"if("+tabAlias
									+ "."
									+ ftl.getMasterTableReferenceColumn()
									.toLowerCase()+" is null,null,CAST("+tabAlias
									+ "."
									+ ftl.getMasterTableReferenceColumn()
									.toLowerCase()+" AS CHAR(255) )) as "+ "`" + ftl.getColumnAlias() + "`";
						}
						else
							part1 = part1
							+ " trim("
							+ tabAlias
							+ "."
							+ ftl.getMasterTableReferenceColumn()
							.toLowerCase() + " )" + " as `"
							+ ftl.getColumnAlias() + "`";

						log.info("Template columns part1.1 at i: " + i
								+ " is: " + part1);
					}
					log.info("Final part1 after block 1: " + part1);

					if (i >= 0 && dvColDataList.size() > 1
							&& i != dvColDataList.size() - 1) {
						part1 = part1 + ",";
						log.info("in part1 if in block 1: " + part1);
					} else {
						log.info("in part1 else in block1: " + part1);
					}

				} else {
					log.info("New Column accessing in part1");
					formulaQuery = "";
					String fourmulaAlias = "";
					if (tabAlias == null || tabAlias.isEmpty()
							|| tabAlias.equalsIgnoreCase("")) {
						tabAlias = "ds0";
					}
					if (DvColData.getFormula() != null) {

						if (DvColData.getFormulaAlias() != null) {
							fourmulaAlias = DvColData.getFormulaAlias();
							log.info("fourmulaAlias: " + fourmulaAlias);
						}
					}

					if (dType != null && !(dType.isEmpty())
							&& dType.equalsIgnoreCase("INTEGER")) {
						formulaQuery = " cast(trim(replace(" + fourmulaAlias
								+ ", ',', '') ) as  signed) as `"
								+ DvColData.getColumnName() + "`";
					} else if (dType != null && !(dType.isEmpty())
							&& dType.equalsIgnoreCase("DECIMAL")) {
						if (qualifier != null && !(qualifier.isEmpty())
								&& qualifier.equalsIgnoreCase("AMOUNT")) {
							formulaQuery = " (CASE WHEN ISNULL("
									+ tabAlias
									+ ".`vid`) THEN if("+fourmulaAlias+"='',null,cast(trim(replace("
									+ fourmulaAlias
									+ ", ',', '') ) as  decimal(38,5))) "
									+ "ELSE if("+tabAlias
									+ ".`v_amount`='',null,CAST(TRIM(REPLACE("
									+ tabAlias
									+ ".`v_amount`, ',', '')) AS DECIMAL (38 , 5 ))) END)  as `"
									+ DvColData.getColumnName() + "`";
						} else
							formulaQuery = " if("+fourmulaAlias+"='',null,cast(trim(replace("
									+ fourmulaAlias
									+ ", ',', '') ) as  decimal(38,5))) as `"
									+ DvColData.getColumnName() + "`";
					} else if (dType != null && !(dType.isEmpty())
							&& dType.equalsIgnoreCase("BOOLEAN")) {
						formulaQuery = " (CASE WHEN (" + fourmulaAlias
								+ ") <> 0 THEN 'True' ELSE 'False' END) as `"
								+ DvColData.getColumnName() + "`";
					} else if (dType != null && !(dType.isEmpty())
							&& (dType.equalsIgnoreCase("DATE") || dType.equalsIgnoreCase("DATETIME"))) {
						formulaQuery = " if("+fourmulaAlias+" is null,null,cast("
								+ fourmulaAlias+ " AS DATE)) as `"
								+ DvColData.getColumnName() + "`";
					}else if(dType != null && !(dType.isEmpty())
							&& dType.equalsIgnoreCase("VARCHAR") && grpByFlag!=null && grpByFlag){
						formulaQuery = " if("+fourmulaAlias+" is null,null,cast("
								+ fourmulaAlias+ " AS CHAR(255))) as `"
								+ DvColData.getColumnName() + "`";
					}
					else {
						formulaQuery = fourmulaAlias + " as `"
								+ DvColData.getColumnName() + "`";
					}
					log.info("final formula query framed :" + formulaQuery);
					log.info("i: " + i + " dvColDataList.size(): "
							+ dvColDataList.size());

					part1 = part1 + formulaQuery;
					log.info("part1 after appending formulaQuery: " + part1);
					if (i >= 0 && dvColDataList.size() > 1
							&& i != dvColDataList.size() - 1) {
						part1 = part1 + ",";
						log.info("in part1 if part1 in custom column: " + part1);
					} else {
						log.info("in part1 else part1 in custom column: "
								+ part1);
					}
				}

			}

			if (j < tempIdList.size() - 1) {
				part1 = part1 + ",";
			} else {
				part1 = part1;
			}

			log.info("part1 at: " + j + "is: " + part1);
		}
		log.info("srcIds: " + srcIds);
		DataViewsSrcMappings dvcSrcMapngs = null;
		List<DataViewsSrcMappings> dvcSrcMapngsList = dataViewsSrcMappingsRepository
				.findByDataViewId(viewId);
		if (dvcSrcMapngsList != null && dvcSrcMapngsList.size() == 1) {
			dvcSrcMapngs = dvcSrcMapngsList.get(0);
		} else {
			dvcSrcMapngs = dataViewsSrcMappingsRepository
					.findByDataViewIdAndBase(viewId, "Primary");
		}
		if (dvcSrcMapngs != null) {
			log.info("map.get(dvcSrcMapngs.getTemplateId()): "
					+ map.get(dvcSrcMapngs.getTemplateId().toString()));
			String idSrcId = " distinct (CASE WHEN ISNULL("
					+ map.get(dvcSrcMapngs.getTemplateId().toString())
					+ ".`vid`) THEN "
					+ map.get(dvcSrcMapngs.getTemplateId().toString())
					+ ".`id` ELSE "
					+ map.get(dvcSrcMapngs.getTemplateId().toString())
					+ ".`vid` END) AS `scrIds`,";
			idSrcId = idSrcId
					+ map.get(dvcSrcMapngs.getTemplateId().toString())
					+ ".`rowDescription` as `rowDescription`, "
					+ map.get(dvcSrcMapngs.getTemplateId().toString())
					+ ".`adjustmentType` as `adjustmentType`, ";
			srcIds = srcIds + idSrcId
					+ map.get(dvcSrcMapngs.getTemplateId().toString())
					+ ".src_file_inb_id as srcFileInbId, "
					+ " TIMESTAMP("+map.get(dvcSrcMapngs.getTemplateId().toString())
					+ ".file_date) as fileDate, " 
					+ viewId + " as dv_id, "
					+ dvcSrcMapngs.getTemplateId() + " as template_id ,";
			log.info("srcIds: " + srcIds);
			part1 = srcIds + part1;
			log.info("part1 aftr adding srcIds: " + srcIds);
			log.info("final map: " + map);
		}

		viewQuery = viewQuery + part1 + " from ";
		log.info("viewQuery after part1: " + viewQuery);

		/* Part2 */

		String part2 = "";
		List<DataViewFilters> dataViewFiltersList2 = new ArrayList<DataViewFilters>();
		int sz = tempIdList.size();
		for (int k = 0; k < sz; k++) {

			String tempId = tempIdList.get(k);
			String dataChildUnionQuery = "";
			if (tempId != null) {
				log.info("map.get(tempId) for tempId: " + tempId + " is: "
						+ map.get(tempId));
				String part2SubQuery = " (select NULL AS `vid`, NULL AS `v_amount`,  NULL AS `rowDescription`, NULL AS `adjustmentType`, tdm.* from t_data_master `tdm` ";
				log.info("part2 b4 appending at loop k: " + k + " is: " + part2);
				part2 = part2 + part2SubQuery;
				log.info("part2 aftr appending at loop k: " + k + " is: "
						+ part2);
				/* Data View Filters */

				String whereCond = " where ";
				List<DataViewFilters> dataViewFiltersList = new ArrayList<DataViewFilters>();
				if (tempId != null) {
					dataViewFiltersList = dataViewFiltersRepository
							.findByDataViewIdAndRefSrcTypeAndRefSrcId(viewId,
									"File Template", Long.parseLong(tempId));
					log.info("dataViewFiltersList in if: "
							+ dataViewFiltersList);
				}

				if (dataViewFiltersList != null
						&& dataViewFiltersList.size() > 0) {
					for (int h = 0; h < dataViewFiltersList.size(); h++) {
						DataViewFilters dvf = dataViewFiltersList.get(h);
						log.info("dataViewFiltersList for viewId: " + viewId
								+ " tempId: " + tempId + " are: " + dvf);

						if (dvf != null) {
							Long colId = dvf.getRefSrcColId();
							DataViewsColumns dvcFil = dataViewsColumnsRepository
									.findOne(colId);
							// log.info("dvcFil: "+dvcFil);
							Long templateLineId = Long.parseLong(dvcFil
									.getRefDvColumn());
							log.info("templateLineId: " + templateLineId);
							FileTemplateLines ftl = fileTemplateLinesRepository
									.findOne(templateLineId);
							log.info("ftl : " + ftl);
							if (dvcFil.getColDataType().equalsIgnoreCase(
									"INTEGER")
									&& (dvf.getFilterOperator()
											.equalsIgnoreCase("=")
											|| dvf.getFilterOperator()
											.equalsIgnoreCase(">")
											|| dvf.getFilterOperator()
											.equalsIgnoreCase("<")
											|| dvf.getFilterOperator()
											.equalsIgnoreCase(">=")
											|| dvf.getFilterOperator()
											.equalsIgnoreCase("<=") || dvf
											.getFilterOperator()
											.equalsIgnoreCase("!="))) {
								whereCond = whereCond
										+ "cast(trim(replace("
										+ ftl.getMasterTableReferenceColumn()
										.toLowerCase()
										+ ",',','')) as  signed) "
										+ dvf.getFilterOperator() + "'"
										+ dvf.getFilterValue() + "'";
							} else if (dvcFil.getColDataType()
									.equalsIgnoreCase("DECIMAL")
									&& (dvf.getFilterOperator()
											.equalsIgnoreCase("=")
											|| dvf.getFilterOperator()
											.equalsIgnoreCase(">")
											|| dvf.getFilterOperator()
											.equalsIgnoreCase("<")
											|| dvf.getFilterOperator()
											.equalsIgnoreCase(">=")
											|| dvf.getFilterOperator()
											.equalsIgnoreCase("<=") || dvf
											.getFilterOperator()
											.equalsIgnoreCase("!="))) {
								whereCond = whereCond
										+ "cast(trim(replace("
										+ ftl.getMasterTableReferenceColumn()
										.toLowerCase()
										+ ",',','')) as  decimal(38,5)) "
										+ dvf.getFilterOperator() + "'"
										+ dvf.getFilterValue() + "'";
							} else if (dvcFil.getColDataType()
									.equalsIgnoreCase("DATE")
									&& (dvf.getFilterOperator()
											.equalsIgnoreCase("=")
											|| dvf.getFilterOperator()
											.equalsIgnoreCase(">")
											|| dvf.getFilterOperator()
											.equalsIgnoreCase("<")
											|| dvf.getFilterOperator()
											.equalsIgnoreCase(">=")
											|| dvf.getFilterOperator()
											.equalsIgnoreCase("<=") || dvf
											.getFilterOperator()
											.equalsIgnoreCase("!="))) {
								whereCond = whereCond
										+ ftl.getMasterTableReferenceColumn()
										.toLowerCase()
										+ dvf.getFilterOperator() + "'"
										+ dvf.getFilterValue() + "'";
							} else if (dvcFil.getColDataType()
									.equalsIgnoreCase("VARCHAR")
									&& (dvf.getFilterOperator()
											.equalsIgnoreCase("=")
											|| dvf.getFilterOperator()
											.equalsIgnoreCase(">")
											|| dvf.getFilterOperator()
											.equalsIgnoreCase("<")
											|| dvf.getFilterOperator()
											.equalsIgnoreCase(">=")
											|| dvf.getFilterOperator()
											.equalsIgnoreCase("<=") || dvf
											.getFilterOperator()
											.equalsIgnoreCase("!="))) {
								whereCond = whereCond
										+ ftl.getMasterTableReferenceColumn()
										.toLowerCase()
										+ dvf.getFilterOperator() + "'"
										+ dvf.getFilterValue() + "'";
							} else if (dvcFil.getColDataType()
									.equalsIgnoreCase("VARCHAR")
									&& (dvf.getFilterOperator()
											.equalsIgnoreCase("in"))) {
								String valList = dvf.getFilterValue();
								String[] valArr = valList.split(",");
								String val = Stream.of(valArr).collect(
										Collectors.joining("','", "'", "'"));
								whereCond = whereCond
										+ ftl.getMasterTableReferenceColumn()
										.toLowerCase() + " "
										+ dvf.getFilterOperator() + "(" + val
										+ ")";
							} else if (dvcFil.getColDataType()
									.equalsIgnoreCase("INTEGER")
									&& (dvf.getFilterOperator()
											.equalsIgnoreCase("in"))) {
								String valList = dvf.getFilterValue();
								String[] valArr = valList.split(",");
								String val = Stream.of(valArr).collect(
										Collectors.joining(","));
								whereCond = whereCond
										+ ftl.getMasterTableReferenceColumn()
										.toLowerCase() + " "
										+ dvf.getFilterOperator() + "(" + val
										+ ")";
							} else if (dvcFil.getColDataType()
									.equalsIgnoreCase("DECIMAL")
									&& dvf.getFilterOperator()
									.equalsIgnoreCase("BETWEEN")) {
								String arr[] = dvf.getFilterValue()
										.split("\\,");
								whereCond = whereCond
										+ "cast(trim(replace("
										+ ftl.getMasterTableReferenceColumn()
										.toLowerCase()
										+ ",',','')) as  decimal(38,5)) " + " "
										+ dvf.getFilterOperator() + " '"
										+ arr[0] + "' and '" + arr[1] + "'";
							} else if (dvcFil.getColDataType()
									.equalsIgnoreCase("INTEGER")
									&& dvf.getFilterOperator()
									.equalsIgnoreCase("BETWEEN")) {
								String arr[] = dvf.getFilterValue()
										.split("\\,");
								whereCond = whereCond
										+ "cast(trim(replace("
										+ ftl.getMasterTableReferenceColumn()
										.toLowerCase()
										+ ",',','')) as  signed) " + " "
										+ dvf.getFilterOperator() + " '"
										+ arr[0] + "' and '" + arr[1] + "'";
							} else if (dvcFil.getColDataType()
									.equalsIgnoreCase("DATE")
									&& dvf.getFilterOperator()
									.equalsIgnoreCase("BETWEEN")) {
								String arr[] = dvf.getFilterValue()
										.split("\\,");
								whereCond = whereCond
										+ ftl.getMasterTableReferenceColumn()
										.toLowerCase() + " "
										+ dvf.getFilterOperator() + " '"
										+ arr[0] + "' and '" + arr[1] + "'";
							} else if (dvf.getFilterOperator()
									.equalsIgnoreCase("EQUALS")
									|| dvf.getFilterOperator()
									.equalsIgnoreCase("=")) {
								whereCond = whereCond
										+ ftl.getMasterTableReferenceColumn()
										.toLowerCase() + "='"
										+ dvf.getFilterValue() + "'";
							} else if (dvf.getFilterOperator()
									.equalsIgnoreCase("BEGINS_WITH")) {
								whereCond = whereCond
										+ ftl.getMasterTableReferenceColumn()
										.toLowerCase() + " like " + "'"
										+ dvf.getFilterValue() + "%'";
							} else if (dvf.getFilterOperator()
									.equalsIgnoreCase("ENDS_WITH")) {
								whereCond = whereCond
										+ ftl.getMasterTableReferenceColumn()
										.toLowerCase() + " like "
										+ "'%" + dvf.getFilterValue() + "'";
							} else if (dvf.getFilterOperator()
									.equalsIgnoreCase("NOT_EQUALS")
									|| dvf.getFilterOperator()
									.equalsIgnoreCase("!=")) {
								whereCond = whereCond
										+ ftl.getMasterTableReferenceColumn()
										.toLowerCase() + "!='"
										+ dvf.getFilterValue() + "'";
							} else if (dvf.getFilterOperator()
									.equalsIgnoreCase("CONTAINS")) {
								whereCond = whereCond
										+ ftl.getMasterTableReferenceColumn()
										.toLowerCase() + " like "
										+ "'%" + dvf.getFilterValue() + "%'";
							} else {
								log.info("Filter Operator not matched");
							}
						}
						if (h > 0 && h != dataViewFiltersList.size() - 1) {
							whereCond = whereCond + " and ";
						} else if (h == 0 && dataViewFiltersList.size() > 1) {
							whereCond = whereCond + " and ";
						} else if (h == dataViewFiltersList.size() - 1) {
							whereCond = whereCond;
						}
					}

					if (whereCond.equals(" where ")) {
						whereCond = whereCond + " template_id= " + tempId;
					} else {
						whereCond = whereCond + " and template_id= " + tempId;
					}
					log.info("whereCond after for at k:" + k + " is: "
							+ whereCond);
				} else {
					log.info("No filters exist for DataViewId: " + viewId);
					whereCond = whereCond + " template_id= " + tempId;
				}
				log.info("k: " + k + " tempIdList sz minus 1: "
						+ tempIdList.size());
				log.info("whereCond:" + whereCond + "enddddddd");

				whereCond = whereCond
						+ " AND (NOT (`tdm`.`id` IN (SELECT `v_data_child`.`master_row_id` FROM `v_data_child` "
						+ " WHERE ((`v_data_child`.`data_view_id` = "
						+ viewId
						+ ") AND (`v_data_child`.`adjustment_type` = 'Split'))))) ";

				log.info("part2:" + part2 + "enddddd");
				part2 = part2 + " " + whereCond;
				log.info("part2 at k in dvfilterslist: " + k + " is: " + part2);

				dataChildUnionQuery = " union (SELECT `vdc`.`vid` AS `vid`,`vdc`.`amount` AS `v_amount`,`vdc`.`row_description` as `rowDescription`,`vdc`.`adjustment_type` as `adjustment_type`, `tdm`.* from `t_data_master` `tdm` join ( select * from `v_data_child` "
						+ " WHERE `data_view_id` = "
						+ viewId
						+ ") `vdc`"
						+ " WHERE (`tdm`.`id` = `vdc`.`master_row_id`))) "
						+ map.get(tempId);

				if (k >= 0 && k < sz - 1) {
					dataChildUnionQuery = dataChildUnionQuery + ",";
				} else if (k == sz - 1) {
				}
			}

			else {
				log.info("tempId is null after part2: " + tempId);
			}
			log.info("after tempId loop end whereCond: " + part2);

			part2 = part2 + dataChildUnionQuery;
		}

		log.info("part2: " + part2);
		if (part2.endsWith(",")) {
			int lastComma = part2.lastIndexOf(",");
			log.info("lastIndex of comma: " + lastComma);

			StringBuilder sb = new StringBuilder(part2);
			sb.setCharAt(lastComma, ' ');
			part2 = sb.toString();
			log.info("part2 after replacing comma: " + part2);
		}
		viewQuery = viewQuery + part2;
		log.info("viewQuery aftr part2: " + viewQuery);

		/* Part 3 */

		String part3 = "";

		/* Data View Conditions */

		List<DataViewConditions> dataViewConsList = dataViewConditionsRepository
				.findByDataViewId(viewId);
		log.info("dataViewConsList for viewId: " + viewId + " are: "
				+ dataViewConsList + " dataViewConsList.size(): "
				+ dataViewConsList.size());

		String joinCond = " where ";
		if (dataViewConsList != null && dataViewConsList.size() != 0) {
			DataViewConditions dvc = dataViewConsList.get(0);

			log.info("dvc.getRefSrcId(): " + dvc.getRefSrcId());
			log.info("Long.parseLong(dvc.getRefSrcId().toString(): "
					+ Long.parseLong(dvc.getRefSrcId().toString()));

			FileTemplateLines ftl = fileTemplateLinesRepository.findOne(dvc
					.getRefSrcColId());
			FileTemplateLines ftl2 = fileTemplateLinesRepository.findOne(dvc
					.getRefSrcColId2());
			joinCond = joinCond + " " + map.get(dvc.getRefSrcId().toString())
					+ "." + ftl.getMasterTableReferenceColumn().toLowerCase()
					+ dvc.getFilterOperator()
					+ map.get(dvc.getRefSrcId2().toString()) + "."
					+ ftl2.getMasterTableReferenceColumn().toLowerCase();

			part3 = part3 + joinCond;
		}
		log.info("part3: " + part3);

		viewQuery = viewQuery + part3 + ")";

		log.info("viewQuery finall: " + viewQuery);
		return viewQuery;
	}

	/**
	 * Author: Swetha Function for Data Views drill down info
	 * 
	 * @param dataViewId
	 * @return
	 */
	public List<HashMap> fetchDataViewAndColumnsByDvId(Long dataViewId) {
		log.info("in fetchDataViewAndColumnsByDvId with dataViewId: "
				+ dataViewId);
		List<HashMap> templateInfoList = new ArrayList<HashMap>();
		List<Object[]> dataviewUnionsUniqueComb = dataViewUnionRepository
				.fetchUniqueTemplateCombination(dataViewId);
		String relation = "";
		List<HashMap> dvTodvcDtoLIst = new ArrayList<HashMap>();
		DataViews dv = dataViewsRepository.findOne(dataViewId);
		if (dv != null) {
			HashMap dvToDvcDto = new HashMap();
			dvToDvcDto.put("id", dv.getIdForDisplay());
			if (dv.getDataViewName() != null)
				dvToDvcDto.put("dataViewName", dv.getDataViewName());
			if (dv.getDataViewDispName() != null) {
				dvToDvcDto.put("dataViewDispName", dv.getDataViewDispName());
			}
			dvToDvcDto.put("createdBy", dv.getCreatedBy());
			dvToDvcDto.put("creationDate", dv.getCreationDate());
			if (dv.isEnabledFlag() != null)
				dvToDvcDto.put("enabledFlag", dv.isEnabledFlag());
			dvToDvcDto.put("lastUpdatedBy", dv.getLastUpdatedBy());
			dvToDvcDto.put("lastUpdatedDate", dv.getLastUpdatedDate());
			dvToDvcDto.put("tenantId", dv.getTenantId());
			dvToDvcDto.put("startDate", dv.getStartDate());
			dvToDvcDto.put("endDate", dv.getEndDate());
			if (dv.getDescription() != null)
				dvToDvcDto.put("description", dv.getDescription());
			List<DataViewsColumns> dvcList = dataViewsColumnsRepository
					.findByDataViewId(dv.getId());
			List<HashMap> dvcdtoLIst = new ArrayList<HashMap>();
			log.info("dvc List sz for data view Id: " + dv.getId() + " is: "
					+ dvcList.size());

			List<DataViewsSrcMappings> dataViewSrcMapList = dataViewsSrcMappingsRepository
					.findByDataViewId(dataViewId);
			DataViewsSrcMappings dvSrcMapng = new DataViewsSrcMappings();
			if (dataViewSrcMapList != null && dataViewSrcMapList.size() == 1) {
				dvSrcMapng = dataViewSrcMapList.get(0);
				relation = dvSrcMapng.getRelation();
			} else {
				for (int k = 0; k < dataViewSrcMapList.size(); k++) {
					dvSrcMapng = dataViewSrcMapList.get(k);
					if (dvSrcMapng.getRelation() != null) {
						dvToDvcDto
						.put("viewRelation", dvSrcMapng.getRelation());
						relation = dvSrcMapng.getRelation();
					}
				}
			}

			for (int j = 0; j < dvcList.size(); j++) {

				DataViewsColumns dvcdto = dvcList.get(j);
				HashMap dvc = new HashMap();
				dvc.put("id", dvcdto.getId());
				dvc.put("sViewColumn", dvcdto.getId());
				if (dvcdto.getColDataType() != null){
					//dvc.put("colDataType", dvcdto.getColDataType());
					String dataTypeCode=dvcdto.getColDataType();
					LookUpCode dTypeCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("DATA_TYPE",dataTypeCode, dv.getTenantId());
					if(dTypeCode!=null){
						dvc.put("colDataType",dataTypeCode);
						dvc.put("colDataTypeMeaning",dTypeCode.getMeaning());
					}
				}
				if (dvcdto.getFormula() != null)
					dvc.put("formula", dvcdto.getFormula());
				if (dvcdto.getColumnName() != null) {
					dvc.put("columnName", dvcdto.getColumnName());
				}
				if (dvcdto.getQualifier() != null) {
					//dvc.put("qualifier", dvcdto.getQualifier());
					String qualifierCode=dvcdto.getQualifier();
					LookUpCode qualCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("RECON_QUALIFIERS",qualifierCode, dv.getTenantId());
					if(qualCode!=null){
						dvc.put("qualifier",qualifierCode);
						dvc.put("qualifierMeaning",qualCode.getMeaning());
					}
				}
				if (dvcdto.getGroupBy() != null) {
					dvc.put("groupBy", dvcdto.getGroupBy());
				}
				if (dvcdto.getRefDvColumn() != null) {
					FileTemplateLines ftl = fileTemplateLinesRepository
							.findOne(Long.parseLong(dvcdto.getRefDvColumn()));
					if (ftl != null) {
						dvc.put("colName", ftl.getColumnHeader());
						dvc.put("columnHeader", ftl.getColumnHeader());
						dvc.put("intermediateId", ftl.getIntermediateId());
						dvc.put("refDvColumn", dvcdto.getRefDvColumn());
					}
				}
				dvc.put("createdBy", dvcdto.getCreatedBy());
				dvc.put("creationDate", dvcdto.getCreationDate());
				if (dvcdto.getDataViewId() != null)
					dvc.put("dataViewId", dv.getIdForDisplay());
				dvc.put("lastUpdatedBy", dvcdto.getLastUpdatedBy());
				dvc.put("lastUpdatedDate", dvcdto.getLastUpdatedDate());
				if (dvcdto.getRefDvName() != null) {
					String refDvName = dvcdto.getRefDvName();
					// log.info("refDvName: " + refDvName);
					FileTemplates ftemp = fileTemplatesRepository.findOne(Long
							.parseLong(refDvName));
					String ftName = "";
					String rowIdentifier = "";
					if (ftemp != null && ftemp.getTemplateName() != null) {
						ftName = ftemp.getTemplateName();
						if (ftemp.getMultipleIdentifier() != null
								&& ftemp.getMultipleIdentifier() == true) {
							if (dvcdto.getIntermediateId() != null) {
								IntermediateTable intTable = intermediateTableRepository
										.findOne(dvcdto.getIntermediateId());
								rowIdentifier = intTable.getRowIdentifier();
								ftName = ftName + "-" + rowIdentifier;
							}
						}
						dvc.put("sourceName", ftName);
					}
					dvc.put("refDvName", ftemp.getIdForDisplay());
				}
				if (dvcdto.getRefDvType() != null)
					dvc.put("refDvType", dvcdto.getRefDvType());

				/* Filters */
				if (dvSrcMapng.getRelation() != null
						&& dvSrcMapng.getRelation().equalsIgnoreCase("UNION")) {
					List<HashMap> dvUList = new ArrayList<HashMap>();
					List<DataViewUnion> dvunionList = dataViewUnionRepository
							.findByDataViewLineId(dvcdto.getId());
					for (DataViewUnion dvunion : dvunionList) {
						HashMap dvU = new HashMap();
						dvU.put("id", dvunion.getId());
						if (dvunion.getDataViewLineId() != null) {
							dvU.put("dataViewLineId",
									dvunion.getDataViewLineId());
							dvc.put("sViewColumn", dvunion.getDataViewLineId());
						}
						if (dvunion.getRefDvType() != null)
							dvU.put("refDvType", dvunion.getRefDvType());
						if (dvunion.getRefDvName() != null) {
							FileTemplates ftData = fileTemplatesRepository
									.findOne(dvunion.getRefDvName());
							dvU.put("refDvName", ftData.getIdForDisplay());
							String tempName = "";
							tempName = ftData.getTemplateName();
							if (dvunion.getIntermediateId() != null) {
								IntermediateTable intTable = intermediateTableRepository
										.findOne(dvunion.getIntermediateId());
								String rowIdentifier = intTable
										.getRowIdentifier();
								tempName = tempName + "-" + rowIdentifier;
							}
							dvU.put("dataName", tempName);
						}
						if (dvunion.getRefDvColumn() != null) {

							Long refDvColId = Long.parseLong(dvunion
									.getRefDvColumn().toString());
							String refDvColName = "";
							FileTemplateLines ftl = fileTemplateLinesRepository
									.findOne(refDvColId);
							refDvColName = ftl.getColumnHeader();
							dvU.put("refDvColumn", refDvColId);
							dvU.put("columnName", refDvColName);
							dvU.put("columnHeader", refDvColName);
						} else {
							if (dvunion.getRefDvColumn() == null
									&& dvunion.getFormula() == null) {
								dvU.put("refDvColumn", "none");
							}
						}
						dvU.put("createdBy", dvunion.getCreatedBy());
						dvU.put("creationDate", dvunion.getCreationDate());
						dvU.put("lastUpdatedBy", dvunion.getLastUpdatedBy());
						dvU.put("lastUpdatedDate", dvunion.getLastUpdatedDate());
						dvU.put("intermediateId", dvunion.getIntermediateId());
						if (dvunion.getFormula() != null) {
							dvU.put("excelexpressioninputUnion",
									dvunion.getFormula());
							dvU.put("refDvColumn", "customFunction");
						}
						dvUList.add(dvU);
					}
					dvc.put("src", dvUList);
					dvcdtoLIst.add(dvc);
					dvToDvcDto.put("dataViewsUnionColumnsList", dvcdtoLIst);

					// to get list irrespective of union and join
					dvToDvcDto.put("dvColumnsList", dvcdtoLIst);
				} else {
					DataViewFilters dvf;
					// log.info("dvcdto.getRefDvName(): "+dvcdto.getRefDvName()+" dvcdto.getRefDvColumn(): "+dvcdto.getRefDvColumn());
					if (dvcdto.getRefDvName() != null
							&& dvcdto.getRefDvColumn() != null) {
						/*
						 * log.info("dataViewId: " + dataViewId +
						 * "  dvcdto.getRefDvType(): " + dvcdto.getRefDvType() +
						 * " dvcdto.getRefDvName(): " + dvcdto.getRefDvName() +
						 * "dvcdto.getId(): " + dvcdto.getId());
						 */
						dvf = dataViewFiltersRepository
								.findByDataViewIdAndRefSrcTypeAndRefSrcIdAndRefSrcColId(
										dataViewId, dvcdto.getRefDvType(), Long
										.parseLong(dvcdto
												.getRefDvName()
												.toString()), dvcdto
												.getId());
						//log.info("dvf: " + dvf);
					} else {
						//log.info("filter need to added for a newly added column");
						dvf = dataViewFiltersRepository
								.findByDataViewIdAndRefSrcTypeAndRefSrcColId(
										dataViewId, dvcdto.getRefDvType(),
										dvcdto.getId());
						//log.info("dvf: " + dvf);
					}
					if (dvf != null) {
						dvc.put("operator", dvf.getFilterOperator());
						dvc.put("colValue", dvf.getFilterValue());
					}
					dvcdtoLIst.add(dvc);
				}
			}

			if (dvSrcMapng.getRelation() == null
					|| dvSrcMapng.getRelation().equalsIgnoreCase("JOIN")) {
				List<DataViewConditions> dvCondList = dataViewConditionsRepository
						.findByDataViewId(dataViewId);
				log.info("dvCondList sz for dataViewId: " + dataViewId
						+ " is: " + dvCondList.size());
				List<HashMap> dvConMapList = new ArrayList<HashMap>();
				for (int h = 0; h < dvCondList.size(); h++) {

					DataViewConditions dvCond = dvCondList.get(h);
					HashMap dvConMap = new HashMap();
					if (dvCond.getRefSrcType() != null)
						dvConMap.put("srcType1", dvCond.getRefSrcType());
					if (dvCond.getRefSrcType2() != null)
						dvConMap.put("srcType2", dvCond.getRefSrcType2());
					if (dvCond.getRefSrcId() != null) {
						FileTemplates ftData = fileTemplatesRepository
								.findOne(dvCond.getRefSrcId());
						dvConMap.put("scr1", ftData.getIdForDisplay());
					}
					if (dvCond.getRefSrcId2() != null) {
						FileTemplates ftData = fileTemplatesRepository
								.findOne(dvCond.getRefSrcId2());
						dvConMap.put("scr2", ftData.getIdForDisplay());
					}
					if (dvCond.getRefSrcColId() != null)
						dvConMap.put("srcCol1", dvCond.getRefSrcColId());
					if (dvCond.getRefSrcColId2() != null)
						dvConMap.put("srcCol2", dvCond.getRefSrcColId2());
					if (dvCond.getDataViewId() != null)
						dvConMap.put("dvId", dvCond.getDataViewId());
					if (dvCond.getId() != null)
						dvConMap.put("id", dvCond.getId());
					dvConMapList.add(dvConMap);
				}

				dvToDvcDto.put("dataViewsColumnsList", dvcdtoLIst);

				// to get list irrespective of union and join
				dvToDvcDto.put("dvColumnsList", dvcdtoLIst);

				dvToDvcDto.put("conditions", dvConMapList);

				List<Object[]> tempIdsAndIntIdListFromJoin = dataViewsSrcMappingsRepository
						.fetchDVUniqueCombinations(dataViewId);
				for (int g = 0; g < tempIdsAndIntIdListFromJoin.size(); g++) {

					Object[] objArr = tempIdsAndIntIdListFromJoin.get(g);
					Long tempId = null;
					Long intId = null;
					String tempName = "";
					String rowIdentifier = "";
					if (objArr[0] != null)
						tempId = Long.parseLong(objArr[0].toString());
					if (objArr[1] != null)
						intId = Long.parseLong(objArr[1].toString());
					// log.info("tempId: " + tempId + " intId: " + intId);
					HashMap tempMap = new HashMap();
					tempMap.put("type", "File Template");
					FileTemplates ftData = fileTemplatesRepository
							.findOne(tempId);
					tempMap.put("typeId", ftData.getIdForDisplay());
					tempMap.put("intermediateId", intId);
					String dataName = "";
					if (tempId != null && intId != null) {
						FileTemplates ft = fileTemplatesRepository
								.findOne(tempId);
						tempName = ft.getTemplateName();
						IntermediateTable it = intermediateTableRepository
								.findOne(intId);
						rowIdentifier = it.getRowIdentifier();
					} else if (tempId != null) {
						FileTemplates ft = fileTemplatesRepository
								.findOne(tempId);
						tempName = ft.getTemplateName();
					}
					// log.info("tempName: " + tempName + " rowIdentifier: " +
					// rowIdentifier);
					if (tempName != null) {
						dataName = dataName + tempName;
					}
					// log.info("rowIdentifier: " + rowIdentifier +
					// " dataName: "+dataName);
					if (rowIdentifier != null && !(rowIdentifier.isEmpty())) {
						dataName = dataName + "-" + rowIdentifier;
					}
					tempMap.put("dataName", dataName);
					log.info("tempMap: " + tempMap);
					templateInfoList.add(tempMap);
				}
				dvToDvcDto.put("templateInfo", templateInfoList);
			}

			if (relation != null && relation.equalsIgnoreCase("UNION")) {
				for (int g = 0; g < dataviewUnionsUniqueComb.size(); g++) {
					Object[] objArr = dataviewUnionsUniqueComb.get(g);
					// log.info("objArr[0]: " + objArr[0] + " objArr[1]: "+
					// objArr[1]);
					Long tempId = null;
					Long intId = null;
					String tempName = "";
					String rowIdentifier = "";
					if (objArr[0] != null)
						tempId = Long.parseLong(objArr[0].toString());
					if (objArr[1] != null)
						intId = Long.parseLong(objArr[1].toString());
					// log.info("tempId: " + tempId + " intId: " + intId);
					HashMap tempMap = new HashMap();
					tempMap.put("type", "File Template");
					FileTemplates ftData = fileTemplatesRepository
							.findOne(tempId);
					tempMap.put("typeId", ftData.getIdForDisplay());
					tempMap.put("intermediateId", intId);
					String dataName = "";
					if (tempId != null && intId != null) {
						FileTemplates ft = fileTemplatesRepository
								.findOne(tempId);
						tempName = ft.getTemplateName();
						IntermediateTable it = intermediateTableRepository
								.findOne(intId);
						rowIdentifier = it.getRowIdentifier();
					} else if (tempId != null) {
						FileTemplates ft = fileTemplatesRepository
								.findOne(tempId);
						tempName = ft.getTemplateName();
					}
					// log.info("tempName: " + tempName + " rowIdentifier: "+
					// rowIdentifier);
					if (tempName != null) {
						dataName = dataName + tempName;
					}
					// log.info("rowIdentifier: " + rowIdentifier +
					// " dataName: "+ dataName);
					if (rowIdentifier != null && !(rowIdentifier.isEmpty())) {
						dataName = dataName + "-" + rowIdentifier;
					}
					tempMap.put("dataName", dataName);
					log.info("tempMap: " + tempMap);
					templateInfoList.add(tempMap);
				}
				dvToDvcDto.put("templateInfo", templateInfoList);
			}
			// }

			dvTodvcDtoLIst.add(dvToDvcDto);
		}
		return dvTodvcDtoLIst;
	}

	/**
	 * Author: Swetha
	 * @param reportId
	 * @param tenanatId
	 * @param filtersMap
	 * @return
	 * @throws ClassNotFoundException
	 */
	public String frameReportsQuery(Long reportId, Long tenanatId,
			HashMap filtersMap) throws ClassNotFoundException {

		log.info("Request frameQuery for reportId: " + reportId
				+ " and tenanatId: " + tenanatId);
		Reports reports = reportsRepository.findOne(reportId);
		List<ReportDefination> reportDefList = reportDefinationRepository
				.findByReportId(reportId);
		ReportDefination reportDef = reportDefList.get(0);
		Long viewId = reportDef.getRefSrcId();
		DataViews dv = dataViewsRepository.findById(viewId);
		String tableName = dv.getDataViewName();
		log.info("dv: " + dv);

		String query = "select ";
		String colsQuery = "";
		for (int i = 0; i < reportDefList.size(); i++) {
			ReportDefination rdef = reportDefList.get(i);
			Long colId = rdef.getRefColId();
			FileTemplateLines ftl = fileTemplateLinesRepository.findOne(colId);
			String col = ftl.getColumnAlias();
			colsQuery = colsQuery + col;
			if (i >= 0 && i < reportDefList.size() - 1) {
				colsQuery = colsQuery + ",";
			}
		}
		log.info("colsQuery: " + colsQuery);
		colsQuery = colsQuery + " from " + tableName;

		String whereQry = " where ";

		String filterQuery = "";
		if (filtersMap.containsKey("fields")) {
			List<HashMap> colFilterMapList = (List<HashMap>) filtersMap
					.get("fields");
			String subQuery = "";
			for (int j = 0; j < colFilterMapList.size(); j++) {

				HashMap colFilterMap = colFilterMapList.get(j);

				String colName = colFilterMap.get("fieldName").toString();
				String selType = colFilterMap.get("fieldType").toString();
				colFilterMap.get("selectedValues").toString();
				String operator = "";
				if (selType.equalsIgnoreCase("MULTI_SELECTION")) {
					operator = "in";
					List<String> valList = (List<String>) colFilterMap
							.get("selectedValues");
					String finVal = "";
					for (int f = 0; f < valList.size(); f++) {
						String val = valList.get(f);
						if (f >= 0 && f < valList.size() - 1) {
							finVal = finVal + "'" + val + "',";
						}
						finVal = finVal + "'" + val + "'";
					}
					log.info("finVal: " + finVal);
					subQuery = filterQuery + colName + " " + operator + " ( "
							+ finVal + " ) ";
				} else if (selType.equalsIgnoreCase("DATE_RANGE_SELECTION")) {
					operator = "between";

				} else if (selType.equalsIgnoreCase("BOOLEAN_SELECTION")) {
					operator = "is";
					subQuery = filterQuery + colName + " " + operator
							+ colFilterMap.get("selectedValues").toString();
				} else if (selType.equalsIgnoreCase("TEXT")) {
					operator = "=";
					subQuery = filterQuery + colName + " " + operator
							+ colFilterMap.get("selectedValues").toString();
				}
			}
			filterQuery = filterQuery + subQuery;
			log.info("filterQuery: " + filterQuery);
		}

		query = query + colsQuery + whereQry + filterQuery;
		log.info("query: " + query);
		return query;
	}

	/**
	 * Author: Swetha Function to map field references from reporting data
	 * tables to column names
	 * @param reportId
	 * @param tableName
	 * @return
	 */
	public LinkedHashMap getFieldRef(Long reportId, String tableName) {
		log.info("in getFieldRef with reportId: " + reportId + " tableName: "
				+ tableName);
		List<String> colNameList = reportParametersRepository
				.fetchTableColumns(tableName);
		log.info("colNameList: " + colNameList);
		List<ReportDefination> repDefList = reportDefinationRepository
				.findByReportId(reportId);
		LinkedHashMap map = new LinkedHashMap();
		int j = 4;
		for (int i = 0; i < repDefList.size(); i++) {
			ReportDefination rDef = repDefList.get(i);
			String disName = rDef.getDisplayName();
			if (rDef.getRefTypeId().equalsIgnoreCase("DATA_VIEW")) {
				for (; j < colNameList.size();) {
					String refColName = colNameList.get(j);
					map.put(disName, refColName);
					j++;
					break;
				}
			} else {

			}
		}
		log.info("map: " + map);
		return map;

	}

	/**
	 * Author: Swetha
	 * @param reportId
	 * @return
	 */
	public LinkedHashMap getFieldRefNew(Long reportId) {
		log.info("in getFieldRef with reportId: " + reportId);
		List<ReportDefination> repDefList = reportDefinationRepository
				.findByReportId(reportId);
		LinkedHashMap map = new LinkedHashMap();
		for (int i = 0; i < repDefList.size(); i++) {
			ReportDefination rDef = repDefList.get(i);
			String disName = rDef.getDisplayName();
			if (rDef.getRefTypeId().equalsIgnoreCase("DATA_VIEW")) {
				Long refColId = rDef.getRefColId();
				DataViewsColumns dvc = dataViewsColumnsRepository
						.findOne(refColId);
				String ftlId = dvc.getRefDvColumn();
				FileTemplateLines ftl = fileTemplateLinesRepository
						.findOne(Long.parseLong(ftlId));
				String colAlias = ftl.getColumnAlias();
				map.put(disName, colAlias);
			} else {

			}
		}

		List<ReportParameters> repParamList = reportParametersRepository
				.findByReportId(reportId);
		for (int i = 0; i < repParamList.size(); i++) {
			ReportParameters rParam = repParamList.get(i);
			String disName = rParam.getDisplayName();
			if (rParam.getRefTypeid().equalsIgnoreCase("DATA_VIEW")) {
				Long refColId = rParam.getRefColId();
				DataViewsColumns dvc = dataViewsColumnsRepository
						.findOne(refColId);
				String ftlId = dvc.getRefDvColumn();
				FileTemplateLines ftl = fileTemplateLinesRepository
						.findOne(Long.parseLong(ftlId));
				String colAlias = ftl.getColumnAlias();
				map.put(disName, colAlias);
			} else {

			}
		}

		log.info("map: " + map);
		return map;

	}

	/**
	 * Author: Swetha Framing Query for Unions
	 * @param viewId
	 * @param tenanatId
	 * @return
	 * @throws ClassNotFoundException
	 */
	public String frameUnionsQuery(Long viewId, Long tenanatId)
			throws ClassNotFoundException {

		log.info("Request frameUnionsQuery2 for viewId: " + viewId
				+ " and tenanatId: " + tenanatId);
		DataViews dv = dataViewsRepository.findOne(viewId);
		log.info("dv: " + dv);
		List<Long> tempIdList = new ArrayList<Long>();
		List<Object[]> dvUnionDataList = new ArrayList<Object[]>();
		String viewQuery = " (select ";
		dvUnionDataList = dataViewUnionRepository
				.fetchUniqueTemplateCombination(viewId);

		List<HashMap> tempToIntMapList = new ArrayList<HashMap>();
		for (int y = 0; y < dvUnionDataList.size(); y++) {
			Object[] object = dvUnionDataList.get(y);
			log.info("object: " + object);
			Long temId = null;
			if (object != null) {
				if (object[0] != null) {
					temId = Long.parseLong(object[0].toString());
					tempIdList.add(temId);
				}
				Long intermediateId = null;
				if (object[1] != null)
					intermediateId = Long.parseLong(object[1].toString());
				HashMap tempToIntMap = new HashMap();
				if (temId != null) {
					tempToIntMap.put(temId, intermediateId);
					tempToIntMapList.add(tempToIntMap);
				}
			}
		}
		log.info("tempToIntMapList: " + tempToIntMapList);
		HashMap finMap = new HashMap();
		for (int i = 0; i < tempToIntMapList.size(); i++) {
			HashMap tempIdRefMap = tempToIntMapList.get(i);
			log.info("tempIdRefMap: " + tempIdRefMap);
			String tempAlias = "ds";
			tempAlias = tempAlias + i;
			HashMap tempIdIntIdMap = new HashMap();
			finMap.put(tempAlias, tempIdRefMap);
		}
		log.info("finMap: " + finMap);

		String partQuery = " select * from (";
		log.info("partQuery final: " + partQuery);

		String finQuery = "";
		Set aliasKeySet = finMap.keySet();
		int aliasSetSz = aliasKeySet.size();
		Iterator entries = finMap.entrySet().iterator();
		Iterator entries2 = finMap.entrySet().iterator();
		String mainAliasQuery = "";
		int count = 0;
		while (entries2.hasNext()) {
			Map.Entry entry = (Map.Entry) entries2.next();
			String tempAlias = (String) entry.getKey();
			HashMap valueMap = (HashMap) entry.getValue();
			System.out.println("tempAlias = " + tempAlias + ", valueMap = "
					+ valueMap);

			Iterator valueEntries = valueMap.entrySet().iterator();
			Long tempId = 0L;
			Long intId = 0L;
			while (valueEntries.hasNext()) {
				Map.Entry ent = (Map.Entry) valueEntries.next();
				tempId = Long.parseLong(ent.getKey().toString());
				if (ent.getValue() != null) {
					intId = Long.parseLong(ent.getValue().toString());
				}
				System.out.println("tempId = " + tempId + ", intId = " + intId);
			}
			String mainQuery = " select ";
			log.info("tempId: " + tempId);

			String srcIds = "";
			srcIds = srcIds + "distinct CASE WHEN ISNULL(" + tempAlias
					+ ".`vid`) THEN " + tempAlias + ".id ELSE " + tempAlias
					+ ".`vid` END " + " as scrIds, " + tempAlias
					+ ".src_file_inb_id as srcFileInbId, " 
					+ " TIMESTAMP("+tempAlias+ ".file_date) as fileDate, ";
			srcIds = srcIds + tempAlias
					+ ".`rowDescription` as `rowDescription`, " + tempAlias
					+ ".`adjustmentType` as `adjustmentType`, " + viewId
					+ " as dv_id, " + tempId + " as template_id, " + intId
					+ " as intermediate_id, ";
			log.info("srcIds: " + srcIds);

			mainQuery = mainQuery + srcIds;
			log.info("mainQuery after srcIds: " + mainQuery);
			List<DataViewUnion> dvUnionList = new ArrayList<DataViewUnion>();
			if (intId != null && intId > 0) {
				dvUnionList = dataViewUnionRepository
						.fetchByRefDvNameAndIntermediateId(tempId, intId,
								viewId);
			} else {
				dvUnionList = dataViewUnionRepository
						.fetchByRefDvNameAndDataviewId(tempId, viewId);
			}
			int sz = dvUnionList.size();
			for (int k = 0; k < sz; k++) {
				String subQuery = "";
				DataViewUnion dvu = dvUnionList.get(k);
				log.info("dvu: " + dvu);
				Long refDvName = dvu.getRefDvName();
				Long refDvCol = dvu.getRefDvColumn();
				Long dvLineId = dvu.getDataViewLineId();
				DataViewsColumns dvc = dataViewsColumnsRepository
						.findOne(dvLineId);
				String colAlias = dvc.getColumnName();
				String dType = dvc.getColDataType();
				Boolean grpByFlag=dvc.getGroupBy();
				String qualifier = dvc.getQualifier();
				if (refDvCol != null) {
					if (refDvCol.toString().equalsIgnoreCase("none")) {
						subQuery = subQuery + " null as `" + colAlias + "`";
					} else {

						FileTemplateLines ftl = fileTemplateLinesRepository
								.findOne(refDvCol);
						if (dType != null && !(dType.isEmpty())
								&& dType.equalsIgnoreCase("INTEGER")) {
							subQuery = subQuery
									+ " cast(trim(replace("
									+ tempAlias
									+ "."
									+ ftl.getMasterTableReferenceColumn()
									.toLowerCase()
									+ ", ',', '') ) as  signed) " + " as `"
									+ colAlias + "`";
						} else if (dType != null && !(dType.isEmpty())
								&& dType.equalsIgnoreCase("DECIMAL")) {
							if (qualifier != null && !(qualifier.isEmpty())
									&& qualifier.equalsIgnoreCase("AMOUNT")) {
								subQuery = subQuery
										+ " (CASE WHEN ISNULL("
										+ tempAlias
										+ ".`vid`) THEN if("+tempAlias
										+ "."
										+ ftl.getMasterTableReferenceColumn()
										.toLowerCase()+"='',null,cast(trim(replace("
										+ tempAlias
										+ "."
										+ ftl.getMasterTableReferenceColumn()
										.toLowerCase()
										+ ", ',', '') ) as  decimal(38,5)))"
										+ "ELSE if("+tempAlias
										+ ".`v_amount`='',null,CAST(TRIM(REPLACE("
										+ tempAlias
										+ ".`v_amount`, ',', '')) AS DECIMAL (38 , 5 ))) END) "
										+ " as " + "`" + colAlias + "`";
							} else
								subQuery = subQuery
								+ "if("+tempAlias
								+ "."
								+ ftl.getMasterTableReferenceColumn()
								.toLowerCase()+"='',null, cast(trim(replace("
								+ tempAlias
								+ "."
								+ ftl.getMasterTableReferenceColumn()
								.toLowerCase()
								+ ", ',', '') ) as  decimal(38,5)))"
								+ " as " + "`" + colAlias + "`";
						} else if (dType != null && !(dType.isEmpty())
								&& dType.equalsIgnoreCase("BOOLEAN")) {
							subQuery = subQuery
									+ " (CASE WHEN ("
									+ tempAlias
									+ "."
									+ ftl.getMasterTableReferenceColumn()
									.toLowerCase()
									+ ") <> 0 THEN 'True' ELSE 'False' END) as "
									+ "`" + colAlias + "`";
						} else if (dType != null && !(dType.isEmpty())
								&& (dType.equalsIgnoreCase("DATE") || dType.equalsIgnoreCase("DATETIME"))) {
							subQuery = subQuery+" if("+tempAlias
									+ "."
									+ ftl.getMasterTableReferenceColumn()
									.toLowerCase()+" is null,null,cast("+tempAlias
									+ "."
									+ ftl.getMasterTableReferenceColumn()
									.toLowerCase()+" as DATE)) as "+"`" + colAlias + "`";
						}
						else if (dType != null && !(dType.isEmpty())
								&& dType.equalsIgnoreCase("VARCHAR") && grpByFlag!=null && grpByFlag) {
							subQuery = subQuery+" if("+tempAlias
									+ "."
									+ ftl.getMasterTableReferenceColumn()
									.toLowerCase()+" is null,null,cast("+tempAlias
									+ "."
									+ ftl.getMasterTableReferenceColumn()
									.toLowerCase()+" as CHAR(255))) as "+"`" + colAlias + "`";
						}
						else {
							subQuery = subQuery
									+ " trim("
									+ tempAlias
									+ "."
									+ ftl.getMasterTableReferenceColumn()
									.toLowerCase() + " )" + " as `"
									+ colAlias + "`";
						}
					}
				} else {
					log.info("New Column accessing");
					String formulaQuery = "";
					String fourmulaAlias = dvu.getFormulaAlias();
					if (dvu.getFormula() != null) {

						if (dvu.getFormulaAlias() != null)
							formulaQuery = dvu.getFormulaAlias();
						log.info("framed query is: " + formulaQuery);
					} else {
						formulaQuery = null;
					}

					log.info("formulaQuery: " + formulaQuery);
					if (dType != null && !(dType.isEmpty())
							&& dType.equalsIgnoreCase("INTEGER")) {
						formulaQuery = " cast(trim(replace(" + fourmulaAlias
								+ ", ',', '') ) as  signed) " + " as `"
								+ colAlias + "`";
					} else if (dType != null && !(dType.isEmpty())
							&& dType.equalsIgnoreCase("DECIMAL")) {
						if (qualifier != null && !(qualifier.isEmpty())
								&& qualifier.equalsIgnoreCase("AMOUNT")) {
							formulaQuery = " (CASE WHEN ISNULL("
									+ tempAlias
									+ ".`vid`) THEN if("+fourmulaAlias+"='',null,cast(trim(replace("
									+ fourmulaAlias
									+ ", ',', '') ) as  decimal(38,5)))"
									+ "ELSE if("+tempAlias
									+ ".`v_amount`='',null,CAST(TRIM(REPLACE("
									+ tempAlias
									+ ".`v_amount`, ',', '')) AS DECIMAL (38 , 5 ))) END) "
									+ " as " + "`" + colAlias + "`";
						} else
							formulaQuery = " if("+fourmulaAlias+"='',null,cast(trim(replace("
									+ fourmulaAlias
									+ ", ',', '') ) as  decimal(38,5)))"
									+ " as " + "`" + colAlias + "`";
					} else if (dType != null && !(dType.isEmpty())
							&& dType.equalsIgnoreCase("BOOLEAN")) {
						formulaQuery = " (CASE WHEN (" + fourmulaAlias
								+ ") <> 0 THEN 'True' ELSE 'False' END) as "
								+ "`" + colAlias + "`";
					}else if (dType != null && !(dType.isEmpty())
							&& (dType.equalsIgnoreCase("DATE") || dType.equalsIgnoreCase("DATETIME"))) {
						formulaQuery=" if("+fourmulaAlias+" is null,null,cast("+fourmulaAlias+" AS DATE)) as "+ "`" + colAlias + "`";
					}
					else if (dType != null && !(dType.isEmpty())
							&& dType.equalsIgnoreCase("VARCHAR") && grpByFlag!=null && grpByFlag) {
						formulaQuery=" if("+fourmulaAlias+" is null,null,cast("+fourmulaAlias+" AS CHAR(255))) as "+ "`" + colAlias + "`";
					}
					else {
						formulaQuery = fourmulaAlias + " as " + "`" + colAlias
								+ "`";
					}
					subQuery = subQuery + formulaQuery;
				}
				if (k >= 0 && k < sz - 1) {
					subQuery = subQuery + ",";
				}
				mainQuery = mainQuery + subQuery;
				log.info("mainQuery at k: " + k + " is: " + mainQuery);
			}
			log.info("final mainQuery : " + mainQuery);
			String inteQryCond = "";
			mainQuery = mainQuery
					+ " from  (select NULL AS `vid`, NULL AS `v_amount`,  NULL AS `rowDescription`, NULL AS `adjustmentType`, tdm.* from t_data_master `tdm`  where  template_id="
					+ tempId;
			if (intId != null && intId != 0L) {
				mainQuery = mainQuery + " and intermediate_id=" + intId;
			} else {
				mainQuery = mainQuery;
			}

			log.info("mainQuery b4 joining data child: " + mainQuery);

			mainQuery = mainQuery
					+ " AND (NOT (`tdm`.`id` IN (SELECT `v_data_child`.`master_row_id` FROM `v_data_child` "
					+ " WHERE ((`v_data_child`.`data_view_id` = " + viewId
					+ ") AND (`v_data_child`.`adjustment_type` = 'Split'))))) ";

			mainQuery = mainQuery
					+ " UNION ( SELECT `vdc`.`vid` AS `vid`, `vdc`.`amount` AS `v_amount`, `vdc`.`row_description` as `rowDescription`, `vdc`.`adjustment_type` as `adjustment_type`, `tdm`.* "
					+ " FROM   `t_data_master` `tdm` JOIN ( SELECT * FROM   `v_data_child`"
					+ " WHERE  `data_view_id` = "
					+ viewId
					+ ") `vdc` WHERE  (`tdm`.`id` = `vdc`.`master_row_id` and template_id="
					+ tempId;
			if (intId != null) {
				mainQuery = mainQuery + " and intermediate_id=" + intId;
			}
			mainQuery = mainQuery + ")))" + tempAlias;

			log.info("mainQuery after joining data child: " + mainQuery);
			aliasSetSz--;
			if (aliasSetSz > 0) {
				mainQuery = mainQuery + " union all ";
			}

			finQuery = finQuery + mainQuery;
			log.info("finQuery in loop: " + finQuery);
		}
		log.info("finQuery b4 adding partQuery: " + finQuery);
		finQuery = partQuery + finQuery + ") as temptable";
		log.info("finQuery aftr partQuery: " + finQuery);
		return finQuery;
	}

	/**
	 * Author: Swetha Function to frame data views custom function
	 * @param formula
	 * @param tenanatId
	 * @param dataViewId
	 * @param relation
	 * @return
	 */
	public String excelFormulasUpd(String formula, Long tenanatId,
			Long dataViewId, String relation) {

		log.info("in excelFormulas with dataViewId: " + dataViewId
				+ " relation: " + relation);
		log.info("formula: " + formula);
		String formulaQuery = "";
		String finalQuery = "";
		String tabAlias = "ds";
		HashMap aliasMap = new HashMap();
		List<HashMap> tempIdToIntMap = new ArrayList<HashMap>();
		List<Object[]> dvUnionsUniqueList = null;
		if (relation != null && relation.equalsIgnoreCase("UNION")) {

			dvUnionsUniqueList = dataViewsSrcMappingsRepository
					.fetchDVUniqueCombinations(dataViewId);
		}
		if (relation != null && relation.equalsIgnoreCase("JOIN")) {
			dvUnionsUniqueList = dataViewsSrcMappingsRepository
					.fetchDVUniqueCombinations(dataViewId);
		}
		log.info("dvUnionsUniqueList: " + dvUnionsUniqueList);
		for (int i = 0; i < dvUnionsUniqueList.size(); i++) {
			tabAlias = "ds";
			Object[] objArr = dvUnionsUniqueList.get(i);
			Long tempId = null;
			Long intId = null;
			if (objArr[0] != null) {
				tempId = Long.parseLong(objArr[0].toString());
			}
			if (objArr[1] != null) {
				intId = Long.parseLong(objArr[1].toString());
			}
			log.info("tempId: " + tempId + " intId: " + intId);
			tabAlias = tabAlias + i;
			HashMap map = new HashMap();
			map.put(tempId, intId);
			tempIdToIntMap.add(map);
			aliasMap.put(tabAlias, map);

		}
		// }
		log.info("aliasMap: " + aliasMap);
		HashMap reversedHashMap = (HashMap) MapUtils.invertMap(aliasMap);
		log.info("reversedHashMap: " + reversedHashMap);
		log.info("tempIdToIntMap: " + tempIdToIntMap);
		log.info("formula: " + formula);
		if (formula != null) {

			Pattern p = Pattern.compile("\\[([^\\]]+)\\]");
			// Matcher m =
			// p.matcher("[FT/DV.templateName.column1],[FT/DV.templateName.column1]");
			log.info("DvColData.getFormula() :" + formula);
			log.info("DvColData.getFormula() split :" + formula.split("\\(")[0]);
			Matcher m = p.matcher(formula);
			List<String> strList = new ArrayList<String>();

			while (m.find()) {
				System.out.println("We found: " + m.group(1));
				log.info("tabAlias :" + tabAlias);
				strList.add(m.group(1));
				log.info("strList :" + strList);

				String formulaSubQuery = "";
				String str = m.group(1);
				String[] temp = str.split("\\.");
				String fd = temp[0];
				String tempName = temp[1].trim();
				String origTempName = "";
				String rowIdentifier = null;
				Long tempId = null;
				Long intId = null;
				log.info("tempName:" + tempName + " fd:" + fd + " temp[2]:"
						+ temp[2].trim());
				if (tempName.contains("-")) {
					String[] tempIntArr = tempName.split("-");
					if (tempIntArr != null) {
						if (tempIntArr.length > 0)
							origTempName = tempIntArr[0];
						log.info("origTempName: " + origTempName);
						FileTemplates tempData = fileTemplatesRepository
								.findByTenantIdAndTemplateName(tenanatId,
										origTempName);
						tempId = tempData.getId();
						if (tempIntArr.length > 1) {
							rowIdentifier = tempIntArr[1].toString();
						}
						IntermediateTable intTable = intermediateTableRepository
								.findByTemplateIdAndRowIdentifier(tempId,
										rowIdentifier);
						intId = intTable.getId();
					}
				}

				else {
					origTempName = tempName;
					FileTemplates tempData = fileTemplatesRepository
							.findByTenantIdAndTemplateName(tenanatId,
									origTempName);
					tempId = tempData.getId();
				}
				log.info("tempId: " + tempId + " intId: " + intId);
				String column = temp[2].trim();
				log.info("column:" + column);
				if (temp[0].equalsIgnoreCase("ft")) {
					FileTemplates tempData = fileTemplatesRepository
							.findByTenantIdAndTemplateName(tenanatId,
									origTempName);
					log.info("tempData for tempName:" + tempName + " is: "
							+ tempData);
					if (tempData != null) {
						tempId = tempData.getId();
					}
					log.info("tempId: " + tempId);
					HashMap reqMap = new HashMap();
					reqMap.put(tempId, intId);
					if (reversedHashMap.containsKey(reqMap)) {
						tabAlias = reversedHashMap.get(reqMap).toString();
						log.info("key: " + reqMap + " tabAlias: " + tabAlias);
						FileTemplateLines ftlines = new FileTemplateLines();
						if (intId != null) {
							ftlines = fileTemplateLinesRepository
									.findByTemplateIdAndIntermediateIdAndColumnHeader(
											tempData.getId(), intId, column);
						} else
							ftlines = fileTemplateLinesRepository
							.findByTemplateIdAndColumnHeader(
									tempData.getId(), column);
						formulaSubQuery = formulaSubQuery + tabAlias + "."
								+ ftlines.getMasterTableReferenceColumn();
						log.info("formulaSubQuery: " + formulaSubQuery);
						formula = formula.replace(m.group(1), formulaSubQuery);
						log.info("formula :" + formula);
						formulaQuery = formulaQuery + formulaSubQuery;
						log.info("formulaQuery: " + formulaQuery);
					}
				}
			}

		}
		// }
		String finalFormula = formula.replaceAll("]", "").replace("[", "");
		log.info("finalFormula :" + finalFormula);
		return finalFormula;
	}

	/**
	 * Author: Swetha Description: Api to fetch the dataView data
	 * @param viewName
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public HashMap getDataViewsData(Long viewId, Long pageStartIndx,
			Long pageSize) throws ClassNotFoundException, SQLException {

		log.info("getDataViewsData with viewId: " + viewId + " limit: "
				+ pageStartIndx + " pageSize: " + pageSize);
		HashMap map = new HashMap();

		String dbUrl = env.getProperty("spring.datasource.url");
		String[] parts = dbUrl.split("[\\s@&?$+-]+");
		String schemaName = parts[0].split("/")[3];
		log.info("schemaName: " + schemaName);

		Connection conn = null;
		Statement stmt = null;
		Statement stmt2 = null;
		Statement stmt3 = null;
		ResultSet result = null;
		ResultSet result2 = null;
		ResultSet result3 = null;
		ResultSet rs = null;
		List<HashMap> mapList = new ArrayList<HashMap>();
		String count = null;
		String totDataCount = null;
		try {
			DataSource ds = (DataSource) ApplicationContextProvider
					.getApplicationContext().getBean("dataSource");
			conn = ds.getConnection();

			log.info("Connected database successfully...");
			stmt = conn.createStatement();
			stmt2 = conn.createStatement();
			stmt3 = conn.createStatement();

			DataViews dv = dataViewsRepository.findOne(viewId);
			String viewName = dv.getDataViewName().toLowerCase();
			log.info("viewName: " + viewName);
			result2 = stmt
					.executeQuery("SELECT count(*) FROM information_schema.columns WHERE table_schema = '"
							+ schemaName
							+ "' AND table_name = '"
							+ viewName
							+ "'");
			result3 = stmt3.executeQuery("SELECT count(*) FROM " + schemaName
					+ ".`" + viewName + "`");

			while (result3.next()) {
				totDataCount = result3.getString(1);
			}

			while (result2.next()) {
				count = result2.getString(1);
			}

			log.info("count: " + count + " totDataCount: " + totDataCount);
			String viewsQuery = frameViewColumnQuery(viewId);
			String finQuery = viewsQuery + " from " + schemaName + ".`"
					+ viewName + "` limit " + pageStartIndx + ", " + pageSize;
			log.info("finQuery: " + finQuery);
			result = stmt2.executeQuery(finQuery);
			rs = stmt2.getResultSet();

			int rsSz = rs.getFetchSize();
			log.info("rsSz: " + rsSz);

			ResultSetMetaData rsmd2 = rs.getMetaData();
			int columnsNumber = rsmd2.getColumnCount();
			int columnCount = rsmd2.getColumnCount();

			while (rs.next()) {
				HashMap<String, String> map2 = new HashMap<String, String>();
				for (int i = 1; i <= columnCount; i++) {
					String name = rsmd2.getColumnLabel(i);
					for (int t = 0, num = 1; t < columnsNumber; t++, num++) {
						String Val = rs.getString(num);
					}
					map2.put(name, rs.getString(i));

				}
				mapList.add(map2);
			}
		} catch (SQLException se) {
			log.info("se: " + se);
		} finally {
			if (result3 != null)
				result3.close();
			if (result2 != null)
				result2.close();
			if (result != null)
				result.close();
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (stmt2 != null)
				stmt2.close();
			if (stmt3 != null)
				stmt3.close();
			if (conn != null)
				conn.close();
		}

		map.put("mapList", mapList);
		map.put("count", totDataCount);
		return map;
	}

	/**
	 * Author: Swetha
	 * @param viewId
	 * @return
	 */
	public String frameViewColumnQuery(Long viewId) {

		log.info("In frameViewColumnQuery with viewId: " + viewId);
		String finQuery = "select ";
		List<DataViewsColumns> dvcList = dataViewsColumnsRepository
				.findByDataViewId(viewId);
		for (int k = 0; k < dvcList.size(); k++) {

			String subQuery = "";
			DataViewsColumns dvc = dvcList.get(k);
			String dType = dvc.getColDataType();
			String refDvCol = dvc.getRefDvColumn();
			String colName = dvc.getColumnName();
			String dataType = dvc.getColDataType();
			if (refDvCol != null && !(refDvCol.isEmpty())) {
				FileTemplateLines fileTemplateLines = fileTemplateLinesRepository
						.findOne(Long.parseLong(refDvCol));
				String dvColName = fileTemplateLines.getColumnAlias();
				if (dataType.equalsIgnoreCase("DECIMAL"))
					subQuery = "FORMAT(" + dvColName + ",2) as `" + colName
					+ "`";
				else
					subQuery = dvColName + " as `" + colName + "`";
			} else {
				if (dataType.equalsIgnoreCase("DECIMAL"))
					subQuery = "FORMAT(`" + colName + "`,2)" + " as `"
							+ colName + "`";
				else
					subQuery = "`" + colName + "`" + " as `" + colName + "`";
			}
			if (k >= 0 && k <= dvcList.size()) {
				finQuery = finQuery + subQuery + ",";
			} else {
				finQuery = finQuery + subQuery;
			}

		}
		int len = finQuery.length();

		if (finQuery.endsWith(",")) {
			System.out.println("removing comma");
			int commaIndex = finQuery.lastIndexOf(",");
			// log.info("commaIndex: "+commaIndex);
			finQuery = finQuery.substring(0, commaIndex);
		}
		log.info("finQuery aftr removing comma: " + finQuery);
		return finQuery;
	}

	/**
	 * Author: Swetha
	 * @param formula
	 * @param tenanatId
	 * @return
	 */
	public ErrorReport distinctTemplates(String formula, Long tenanatId,
			List<String> currentTemplateList) {

		log.info("Fetching distinctTemplates with formula: " + formula);
		log.info("currentTemplateList: " + currentTemplateList);
		ErrorReport errorReport = new ErrorReport();
		errorReport.setTaskName("Custom Function Validation");
		StatusStringDTO dto = new StatusStringDTO();
		List<String> tempNames = new ArrayList<String>();
		Pattern p = Pattern.compile("\\[([^\\]]+)\\]");
		log.info("DvColData.getFormula() :" + formula);
		log.info("DvColData.getFormula() split :" + formula.split("\\(")[0]);
		Matcher m = p.matcher(formula);
		List<String> strList = new ArrayList<String>();
		Boolean val = false;
		int count = 0;

		/*List<Object[]> functList=dataViewsRepository.fetchStringFunctions();
		List<String> funList=new ArrayList<>();
		for(int i=0;i<functList.size();i++){
			Object[] function=functList.get(i);
			funList.add(function[1].toString());
		}

		log.info("funList: "+funList);*/

		while (m.find()) {
			System.out.println("We found: " + m.group(1));
			count++;
			strList.add(m.group(1));
			log.info("strList :" + strList);

			String formulaSubQuery = "";
			String str = m.group(1);
			log.info("str: " + str);
			String[] temp = str.split("\\.");
			String fd = temp[0];
			String tempName = temp[1].trim();
			String origTempName = "";
			String rowIdentifier = "";
			Long tempId = null;
			if (tempName.contains("-")) {
				String[] tempIntArr = tempName.split("-");
				if (tempIntArr != null) {
					if (tempIntArr.length > 0)
						origTempName = tempIntArr[0];
					if (tempIntArr.length > 1) {
						rowIdentifier = tempIntArr[1];

					}
				}

			} else {
				origTempName = tempName;
			}
			log.info("origTempName: " + origTempName + " rowIdentifier: "
					+ rowIdentifier);
			String column = temp[2].trim();
			log.info("column:" + column + " temp[0]: " + temp[0]);
			String identifier = null;
			Long intId = 0L;
			if (temp[0].equalsIgnoreCase("ft")) {
				if (tempName.contains("-")) {
					String[] tempArr = tempName.split("-");
					log.info("tempArr[0]: " + tempArr[0] + " tempArr[1]: "
							+ tempArr[1]);
					tempName = tempArr[0];
					identifier = tempArr[1];
					if (identifier.startsWith("H")) {
						identifier = identifier.replaceFirst("H", "");
					} else if (identifier.startsWith("R")) {
						identifier = identifier.replaceFirst("R", "");
					}
					tempName = tempName + "-" + identifier;
					log.info("final tempName: " + tempName + " identifier: "
							+ identifier);
				}
				if (currentTemplateList.contains(tempName)) {
					log.info("template: " + tempName + " exists");
					FileTemplates ft = fileTemplatesRepository
							.findByTenantIdAndTemplateName(tenanatId,
									origTempName);
					log.info("ft: " + ft);
					FileTemplateLines ftl;
					if ((identifier != null) && !(identifier.isEmpty())) {

						IntermediateTable intData = intermediateTableRepository
								.findByTemplateIdAndRowIdentifier(ft.getId(),
										identifier);
						intId = intData.getId();
						log.info("intId: " + intId);
						ftl = fileTemplateLinesRepository
								.findByTemplateIdAndIntermediateIdAndColumnHeader(
										ft.getId(), intId, column);
					} else
						ftl = fileTemplateLinesRepository
						.findByTemplateIdAndColumnHeader(ft.getId(),
								column);
					if (ftl != null) {
						log.info("column: " + column
								+ " exists for file template: " + origTempName);
					} else {
						log.info("column: " + column
								+ " doesnt exists for file template: "
								+ origTempName);
						val = true;
						errorReport.setTaskStatus("Column Validation Failed");
						errorReport.setDetails("column: " + column
								+ " doesnt exists for file template: "
								+ tempName);
						break;
					}
				} else {
					log.info("template: " + tempName + " doesnt exists");
					val = true;
					errorReport
					.setTaskStatus("File Template Validation Failed");
					errorReport.setDetails("File Template: " + tempName
							+ " doesnt exists");
					break;
				}
			} else if (temp[0].equalsIgnoreCase("Dv")) {
				val = true;
				log.info("File Type short name should be FT for File Templates");
				errorReport
				.setTaskStatus("File Type short name Validation Failed");
				errorReport
				.setDetails("File Type short name should be FT for File Templates");
				break;
			} else if (temp[0].equalsIgnoreCase("Ft/Dv")) {
				val = true;
				log.info("File Type short name is ambiguous , It has be either FT or DV");
				errorReport
				.setTaskStatus("File Type short name Validation Failed");
				errorReport
				.setDetails("File Type short name is ambiguous , It has be either FT or DV");
				break;
			}
		}

		log.info("val: " + val);
		log.info("errorReport: " + errorReport);
		if (count == 0) {
			errorReport.setTaskStatus("Function Syntax Mismatched");
			errorReport.setDetails("Function Syntax Mismatched");
		}

		return errorReport;
	}

	public LinkedHashMap getDataViewsDataWithoutPagination(Long viewId)
			throws ClassNotFoundException, SQLException {

		log.info("Request to getDataViewsData with viewId: " + viewId);
		LinkedHashMap map = new LinkedHashMap();
		Connection conn = null;
		Statement stmt = null;
		Statement stmt2 = null;
		Statement stmt3 = null;
		ResultSet result = null;
		ResultSet result2 = null;
		ResultSet result3 = null;
		ResultSet rs = null;
		List<HashMap> mapList = new ArrayList<HashMap>();
		String count = null;
		String totDataCount = null;
		try {
			DataSource ds = (DataSource) ApplicationContextProvider
					.getApplicationContext().getBean("dataSource");
			conn = ds.getConnection();
			String dbUrl = env.getProperty("spring.datasource.url");
			String[] parts = dbUrl.split("[\\s@&?$+-]+");
			String schemaName = parts[0].split("/")[3];
			log.info("schemaName: " + schemaName);
			stmt = conn.createStatement();
			log.info("Connected database successfully...");
			stmt = conn.createStatement();
			stmt2 = conn.createStatement();
			stmt3 = conn.createStatement();

			DataViews dv = dataViewsRepository.findOne(viewId);
			String viewName = dv.getDataViewName().toLowerCase();
			result2 = stmt
					.executeQuery("SELECT count(*) FROM information_schema.columns WHERE table_schema = '"
							+ schemaName
							+ "' AND table_name = '"
							+ viewName
							+ "'");
			result3 = stmt3.executeQuery("SELECT count(*) FROM " + schemaName
					+ ".`" + viewName + "`");

			while (result3.next()) {
				totDataCount = result3.getString(1);
			}

			while (result2.next()) {
				count = result2.getString(1);
			}

			log.info("count: " + count + " totDataCount: " + totDataCount);
			String viewsQuery = frameViewColumnQuery(viewId);
			String finQuery = viewsQuery + " from " + schemaName + ".`"
					+ viewName + "`";
			log.info("finQuery: " + finQuery);
			result = stmt2.executeQuery(finQuery);
			rs = stmt2.getResultSet();

			int rsSz = rs.getFetchSize();
			log.info("rsSz: " + rsSz);

			ResultSetMetaData rsmd2 = rs.getMetaData();
			int columnsNumber = rsmd2.getColumnCount();
			int columnCount = rsmd2.getColumnCount();

			while (rs.next()) {
				LinkedHashMap<String, String> map2 = new LinkedHashMap<String, String>();
				for (int i = 1; i <= columnCount; i++) {
					String name = rsmd2.getColumnLabel(i);
					for (int t = 0, num = 1; t < columnsNumber; t++, num++) {
						String Val = rs.getString(num);
					}
					map2.put(name, rs.getString(i));

				}
				mapList.add(map2);
			}
		} catch (SQLException se) {
			log.info("se: " + se);
		} finally {
			if (result3 != null)
				result3.close();
			if (result2 != null)
				result2.close();
			if (result != null)
				result.close();
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (stmt2 != null)
				stmt2.close();
			if (stmt3 != null)
				stmt3.close();
			if (conn != null)
				conn.close();
		}

		map.put("mapList", mapList);
		map.put("count", totDataCount);
		return map;
	}

	/**
	 * Function for global search Integrated for Data Views
	 * 
	 * @param jsonValueList
	 * @param headList
	 * @param serachString
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 * @throws ParseException
	 * @throws URISyntaxException
	 * @throws org.json.simple.parser.ParseException
	 */
	public List<JSONObject> globalSearch(List<JSONObject> jsonValueList,
			List<String> headList, String serachString) throws IOException,
			JSONException, ParseException, URISyntaxException,
			org.json.simple.parser.ParseException {
		List<JSONObject> filteredObjList = new ArrayList<JSONObject>();
		for (int i = 0; i < jsonValueList.size(); i++) {
			for (String head : headList) {
				if (jsonValueList.get(i).containsKey(head)) {
					if (!(jsonValueList.get(i).get(head).toString().isEmpty())) {
						if (jsonValueList.get(i).get(head).toString()
								.toLowerCase()
								.contains(serachString.toLowerCase())) {
							filteredObjList.add(jsonValueList.get(i));
							break;
						}
					}
				}
			}
		}
		return filteredObjList;
	}

	/**
	 * Author: Shiva
	 * Purpose: Fetching Date Qualifiers List
	 * **/
	public List<HashMap> getDateQualifiers(Long viewId)
	{
		log.info("Rest API for fetching date qualifiers for the view id: "+viewId);
		List<HashMap> finalList = new ArrayList<HashMap>();
		List<DataViewsColumns> dvCols = dataViewsColumnsRepository.fetchDateQualifiers(viewId);
		log.info("Date Qualifiers List: "+ dvCols.size());
		if(dvCols.size()>0)
		{
			for(DataViewsColumns dvc : dvCols)
			{
				HashMap columMap = new HashMap();
				if("File Template".equalsIgnoreCase(dvc.getRefDvType()))
				{
					FileTemplateLines ftl = fileTemplateLinesRepository.findOne(Long.parseLong(dvc.getRefDvColumn().toString()));
					if(ftl != null)
					{
						columMap.put("columnName", ftl.getColumnAlias());
						columMap.put("displayName", dvc.getColumnName());
					}
				}
				else if("Data View".equalsIgnoreCase(dvc.getRefDvType()) || dvc.getRefDvType() == null)
				{
					columMap.put("columnName", dvc.getColumnName());
					columMap.put("displayName", dvc.getColumnName());
				}   
				// Type of Qualifier
				if("TRANSDATE".equalsIgnoreCase(dvc.getQualifier()))
				{
					columMap.put("dateQualifierType", dvc.getQualifier());
				}
				else if("FILEDATE".equalsIgnoreCase(dvc.getQualifier()))
				{
					columMap.put("dateQualifierType", dvc.getQualifier());    				
				}
				finalList.add(columMap);
			}
		}
		return finalList;
	}
}
