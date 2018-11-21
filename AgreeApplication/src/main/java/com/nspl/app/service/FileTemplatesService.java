package com.nspl.app.service;

import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.RequestParam;

import com.nspl.app.domain.FileTemplateLines;
import com.nspl.app.domain.FileTemplates;
import com.nspl.app.domain.IntermediateTable;
import com.nspl.app.domain.LookUpCode;
import com.nspl.app.domain.RowConditions;
import com.nspl.app.domain.SourceProfileFileAssignments;
import com.nspl.app.domain.TenantConfig;
import com.nspl.app.repository.FileTemplateLinesRepository;
import com.nspl.app.repository.FileTemplatesRepository;
import com.nspl.app.repository.IntermediateTableRepository;
import com.nspl.app.repository.LookUpCodeRepository;
import com.nspl.app.repository.RowConditionsRepository;
import com.nspl.app.repository.SourceProfileFileAssignmentsRepository;
import com.nspl.app.repository.TenantConfigRepository;
import com.nspl.app.repository.search.FileTemplateLinesSearchRepository;
import com.nspl.app.repository.search.FileTemplatesSearchRepository;
import com.nspl.app.security.IDORUtils;
import com.nspl.app.web.rest.dto.ErrorReport;
import com.nspl.app.web.rest.dto.FileTempDTO;
import com.nspl.app.web.rest.dto.FileTemplateLinesDTO;
import com.nspl.app.web.rest.dto.FileTemplatesPostingDTO;
import com.nspl.app.web.rest.dto.FileTmpDTO;
import com.nspl.app.web.rest.dto.MultipleIdentifiersDTO;
import com.nspl.app.web.rest.dto.RowConditionsDTO;
import com.nspl.app.web.rest.dto.SourceProfileFileAssignmentDTO;
import com.nspl.app.web.rest.util.PaginationUtil;

@Service
public class FileTemplatesService {

	private final Logger log = LoggerFactory.getLogger(FileTemplatesService.class);

	@Inject
	UserJdbcService userJdbcService;

	@Inject
	FileTemplatesRepository fileTemplatesRepository;

	@Inject
	LookUpCodeRepository lookUpCodeRepository;

	@Inject
	IntermediateTableRepository intermediateTableRepository;

	@Inject
	FileTemplateLinesRepository fileTemplateLinesRepository;

	@Inject 
	SourceProfileFileAssignmentsRepository srcProfFileAssignRepository;

	@Inject
	RowConditionsRepository rowConditionsRepository;

	@Inject
	FileTemplateLinesSearchRepository fileTemplateLinesSearchRepository;

	private FileTemplatesSearchRepository fileTemplatesSearchRepository;

	@Inject
	TenantConfigRepository tenantConfigRepository;

	private  SourceProfileFileAssignmentsRepository sourceProfileFileAssignmentsRepository;

	private PlatformTransactionManager transactionManager;

	@PersistenceContext(unitName="default")
	private EntityManager entityManager;

	public HashMap getAllFileTemplates(Integer offset,
			Integer limit, String formType, Long tenantId, String sortColName, String sortOrder) throws URISyntaxException {
		log.debug("REST request to getAllFileTemplates with formType: "+formType+" offset: "+offset+" limit: "+limit);

		List<FileTemplates> fileTemplateList=new ArrayList<FileTemplates>();
		List<Long> ftIds=new ArrayList<Long>();
		List<Long> multIdentifierIds=new ArrayList<Long>();
		HashMap finMap=new HashMap();
		HashMap indexMap=new HashMap();
		HashMap MultIdMap=new HashMap();

		Page<FileTemplates> page = null;
		HttpHeaders headers = null;

		int count = 0 ;
		count = fileTemplatesRepository.findByTenantId(tenantId).size();

		if(limit==null || limit == 0){
			limit=count;
		}
		if(offset == null || offset == 0)
		{
			offset = 0;
		}
		log.info("offset :"+offset+"limit"+limit);
		page = fileTemplatesRepository.findByTenantId(tenantId,PaginationUtil.generatePageRequestWithSortColumn(offset, limit, sortOrder, sortColName));
		List<FileTmpDTO> fileTmpList=new ArrayList<FileTmpDTO>();

		for(int i=0;i<page.getContent().size();i++)
		{
			FileTmpDTO fileTmpDTO=new FileTmpDTO();
			fileTmpDTO.setTotalCount(count);
			fileTmpDTO.setIndex(i);
			Long ftId=page.getContent().get(i).getId();
			fileTmpDTO.setId(ftId.toString());
			ftIds.add(ftId);
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
			{
				LookUpCode code = new LookUpCode();
					code = lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("SOURCE_TYPES",page.getContent().get(i).getSource(), tenantId);
					if(code != null && code.getMeaning() != null ){
						fileTmpDTO.setSourceMeaning(code.getMeaning() );
						fileTmpDTO.setSource(page.getContent().get(i).getSource());
					}
				}
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
				if(page.getContent().get(i).getMultipleIdentifier()==true){
					multIdentifierIds.add(page.getContent().get(i).getId());
					MultIdMap.put(ftId, fileTmpDTO);
				}
			}
			int taggedProfCnt = 0;
			taggedProfCnt = srcProfFileAssignRepository.findByTemplateId(page.getContent().get(i).getId()).size();
			fileTmpDTO.setTaggedToProfCnt(taggedProfCnt);
			fileTmpList.add(fileTmpDTO);
			indexMap.put(ftId, i);
		}

		finMap.put("fileTmpList", fileTmpList);
		finMap.put("ftIds", ftIds);
		finMap.put("multIdentifierIds", multIdentifierIds);
		finMap.put("indexMap", indexMap);
		finMap.put("MultIdMap", MultIdMap);

		return finMap;

	}

	/**
	 * Author: Swetha
	 * Service to set file template lines
	 * @param ftempIdList
	 * @param type
	 * @param intermediateIdList
	 * @return
	 */
	public List getFileTempleLines(List<Long> ftempIdList, String type, List<Long> intermediateIdList){

		log.info("in getFileTempleLines with type: "+type);
		log.info("ftempIdList: "+ftempIdList);
		log.info("intermediateIdList: "+intermediateIdList);
		List<FileTemplateLines> ftempList=new ArrayList<FileTemplateLines>();

		if(type.equalsIgnoreCase("nonMultiId")){
			ftempList=fileTemplateLinesRepository.findByTemplateIdIn(ftempIdList);
		}
		else if(type.equalsIgnoreCase("multiId")){
			List<FileTemplateLines> ftlLines=new ArrayList<FileTemplateLines>();
			if(intermediateIdList!=null && intermediateIdList.size()>0){
				ftlLines=fileTemplateLinesRepository.findByTemplateIdInAndIntermediateIdInOrderByLineNumber(ftempIdList, intermediateIdList);
			}
			else{
				ftlLines=fileTemplateLinesRepository.findByTemplateIdIn(ftempIdList);
			}
			log.info("ftlLines sz: "+ftlLines.size());
			ftempList.addAll(ftlLines);
		}
		List finalList=new ArrayList<>();
		for(int k=0;k<ftempList.size();k++){
			FileTemplateLines ftemp=ftempList.get(k);
			HashMap map=new HashMap();
			Long ftId=ftemp.getId();
			String colDataType="";
			map.put("id",ftId );
			if(type.equalsIgnoreCase("multiId")){

			}
			FileTemplates fileTemplate=fileTemplatesRepository.findOne(ftemp.getTemplateId());
			map.put("dataViewId", fileTemplate.getIdForDisplay());
			String rowIdentifier="";
			Long intermediateId=ftemp.getIntermediateId();
			if(intermediateId!=null && intermediateId>0){
				map.put("intermediateId", intermediateId);
				IntermediateTable intData=intermediateTableRepository.findOne(intermediateId);	
				rowIdentifier=intData.getRowIdentifier();
			}
			String fileTempName=fileTemplate.getTemplateName();
			if(rowIdentifier!=null && !(rowIdentifier.isEmpty())){
				fileTempName=fileTempName+"-"+rowIdentifier;
			}
			if(fileTemplate!=null && fileTemplate.getTemplateName()!=null)
			{
				map.put("templateName", fileTempName);
				map.put("dataViewName", fileTempName);
				map.put("dataViewDisplayName", fileTempName);
			}
			map.put("refDvType", "File Template");
			map.put("colDataType", colDataType);
			if(ftemp.getLineNumber()!=null)
				map.put("lineNumber", ftemp.getLineNumber());
			if(ftemp.getColumnHeader()!=null)
				map.put("columnName", ftemp.getColumnHeader());
			if(ftemp.getMasterTableReferenceColumn()!=null)
				map.put("masterTableReferenceColumn", ftemp.getMasterTableReferenceColumn());
			if(ftemp.getRecordTYpe()!=null)
				map.put("recordTYpe", ftemp.getRecordTYpe());
			if(ftemp.getRecordIdentifier()!=null)
				map.put("recordIdentifier",ftemp.getRecordIdentifier());
			if(ftemp.getColumnNumber()!=null)
				map.put("columnNumber", ftemp.getColumnNumber());
			if(ftemp.getEnclosedChar()!=null)
				map.put("enclosedChar", ftemp.getEnclosedChar());
			if(ftemp.getPositionBegin()!=null)
				map.put("positionBegin", ftemp.getPositionBegin());
			if(ftemp.getPositionEnd()!=null)
				map.put("positionEnd", ftemp.getPositionEnd());
			if(ftemp.getColumnHeader()!=null)
				map.put("columnHeader", ftemp.getColumnHeader());
			if(ftemp.getConstantValue()!=null)
				map.put("constantValue", ftemp.getConstantValue());
			if(ftemp.getZeroFill()!=null)
				map.put("zeroFill", ftemp.getZeroFill());
			if(ftemp.getAlign()!=null)
				map.put("align", ftemp.getAlign());
			if(ftemp.getDateFormat()!=null)
				map.put("dateFormat", ftemp.getDateFormat());
			if(ftemp.getTimeFormat()!=null)
				map.put("timeFormat",ftemp.getTimeFormat());
			if(ftemp.getAmountFormat()!=null)
				map.put("amountFormat", ftemp.getAmountFormat());
			if(ftemp.getOverFlow()!=null)
				map.put("overFlow",ftemp.getOverFlow()!=null);
			if(ftemp.getSkipColumn()!=null)
				map.put("skipColumn", ftemp.getSkipColumn());
			if(ftemp.getColumnDelimiter()!=null)
				map.put("columnDelimiter", ftemp.getColumnDelimiter());
			if(ftemp.getCreatedBy()!=null)
				map.put("createdBy", ftemp.getCreatedBy());
			if(ftemp.getCreatedDate()!=null)
				map.put("createdDate", ftemp.getCreatedDate());
			if(ftemp.getLastUpdatedBy()!=null)
				map.put("lastUpdatedBy", ftemp.getLastUpdatedBy());
			if( ftemp.getLastUpdatedDate()!=null)
				map.put("lastUpdatedDate", ftemp.getLastUpdatedDate());
			if(map!=null)
				finalList.add(map);
		}
		log.info("finalList sz: "+finalList.size());
		return finalList;
	}

	/**
	 * Author: Swetha
	 * Service to set file template lines
	 * @param ftempIdList
	 * @param type
	 * @param intermediateIdList
	 * @return
	 */
	public List getFileTempleLinesForTemplate(List<Long> ftempIdList, String type, List<HashMap> ftIdTiIntIdMapList,Long tenantId){

		log.info("in getFileTempleLines with type: "+type);
		log.info("ftIdTiIntIdMapList.size(): "+ftIdTiIntIdMapList.size());
		List finalList=new ArrayList<>();
		for(int n=0;n<ftIdTiIntIdMapList.size();n++)
		{
			HashMap intmap=(HashMap) ftIdTiIntIdMapList.get(n);
			Iterator entries = intmap.entrySet().iterator();
			while (entries.hasNext()) {

				Map.Entry entry = (Map.Entry) entries.next();
				String ftIdForDisplay=entry.getKey().toString();
				Long intermediateId=null;
				if(entry.getValue()!=null){
					intermediateId=Long.parseLong(entry.getValue().toString());
				}
				FileTemplates fileTemplate=fileTemplatesRepository.findByIdForDisplayAndTenantId(ftIdForDisplay, tenantId);
				Long ftId=fileTemplate.getId();
				List<FileTemplateLines> finTempLineList=new ArrayList<FileTemplateLines>();
				List<FileTemplateLines> ftempLines=new ArrayList<FileTemplateLines>();
				if(fileTemplate.getMultipleIdentifier()!=null && fileTemplate.getMultipleIdentifier()==true)
				{
					List<HashMap> ftlMapList=new ArrayList<HashMap>();
					ftempLines=fileTemplateLinesRepository.findByTemplateIdAndIntermediateIdOrderByLineNumber(ftId, intermediateId);
					log.info("ftempLines sz for ftId: "+ftId+" and intermediateId: "+intermediateId+" is: "+ftempLines.size()+" at n: "+n);
					log.info("ftempLines.sz: "+ftempLines.size());

					for(int i=0;i<ftempLines.size();i++){

						FileTemplateLines ftemp=ftempLines.get(i);
						HashMap map=new HashMap();

						String colDataType="";
						map.put("id",ftemp.getId() );
						map.put("refDvName",ftIdForDisplay );
						map.put("refDvColumn",ftemp.getId() );
						map.put("dataViewId", fileTemplate.getIdForDisplay());

						String rowIdentifier="";
						intermediateId=ftemp.getIntermediateId();
						map.put("intermediateId", intermediateId);
						if(intermediateId!=null && intermediateId>0){
							rowIdentifier=ftemp.getRecordIdentifier();
						}
						String fileTempName=fileTemplate.getTemplateName();
						if(rowIdentifier!=null && !(rowIdentifier.isEmpty())){
							fileTempName=fileTempName+"-"+rowIdentifier;
						}
						if(fileTemplate!=null && fileTemplate.getTemplateName()!=null)
						{
							map.put("templateName", fileTempName);
							map.put("dataViewName", fileTempName);
							map.put("dataViewDisplayName", fileTempName);
						}
						map.put("refDvType", "File Template");
						map.put("colDataType", colDataType);
						if(ftemp.getLineNumber()!=null)
							map.put("lineNumber", ftemp.getLineNumber());
						if(ftemp.getColumnHeader()!=null)
							map.put("columnName", ftemp.getColumnHeader());
						if(ftemp.getMasterTableReferenceColumn()!=null)
							map.put("masterTableReferenceColumn", ftemp.getMasterTableReferenceColumn());
						if(ftemp.getRecordTYpe()!=null)
							map.put("recordTYpe", ftemp.getRecordTYpe());
						if(ftemp.getRecordIdentifier()!=null)
							map.put("recordIdentifier",ftemp.getRecordIdentifier());
						if(ftemp.getColumnNumber()!=null)
							map.put("columnNumber", ftemp.getColumnNumber());
						if(ftemp.getEnclosedChar()!=null)
							map.put("enclosedChar", ftemp.getEnclosedChar());
						if(ftemp.getPositionBegin()!=null)
							map.put("positionBegin", ftemp.getPositionBegin());
						if(ftemp.getPositionEnd()!=null)
							map.put("positionEnd", ftemp.getPositionEnd());
						if(ftemp.getColumnHeader()!=null)
							map.put("columnHeader", ftemp.getColumnHeader());
						if(ftemp.getConstantValue()!=null)
							map.put("constantValue", ftemp.getConstantValue());
						if(ftemp.getZeroFill()!=null)
							map.put("zeroFill", ftemp.getZeroFill());
						if(ftemp.getAlign()!=null)
							map.put("align", ftemp.getAlign());
						if(ftemp.getDateFormat()!=null)
							map.put("dateFormat", ftemp.getDateFormat());
						if(ftemp.getTimeFormat()!=null)
							map.put("timeFormat",ftemp.getTimeFormat());
						if(ftemp.getAmountFormat()!=null)
							map.put("amountFormat", ftemp.getAmountFormat());
						if(ftemp.getOverFlow()!=null)
							map.put("overFlow",ftemp.getOverFlow()!=null);
						if(ftemp.getSkipColumn()!=null)
							map.put("skipColumn", ftemp.getSkipColumn());
						if(ftemp.getColumnDelimiter()!=null)
							map.put("columnDelimiter", ftemp.getColumnDelimiter());
						if(ftemp.getCreatedBy()!=null)
							map.put("createdBy", ftemp.getCreatedBy());
						if(ftemp.getCreatedDate()!=null)
							map.put("createdDate", ftemp.getCreatedDate());
						if(ftemp.getLastUpdatedBy()!=null)
							map.put("lastUpdatedBy", ftemp.getLastUpdatedBy());
						if( ftemp.getLastUpdatedDate()!=null)
							map.put("lastUpdatedDate", ftemp.getLastUpdatedDate());
						if(map!=null)
							ftlMapList.add(map);
					}
					finalList.add(ftlMapList);
				}
				else{
					log.info("in non multi row identifier case");
					ftempLines=fileTemplateLinesRepository.findByTemplateId(ftId);
					List<HashMap> ftlMapList=new ArrayList<HashMap>();
					for(int i=0;i<ftempLines.size();i++){

						FileTemplateLines ftemp=ftempLines.get(i);
						HashMap map=new HashMap();

						String colDataType="";
						map.put("id",ftemp.getId() );
						map.put("refDvName",ftIdForDisplay );
						map.put("refDvColumn",ftemp.getId() );
						map.put("dataViewId", fileTemplate.getIdForDisplay());

						String rowIdentifier="";
						map.put("intermediateId", intermediateId);
						if(intermediateId!=null && intermediateId>0){
							rowIdentifier=ftemp.getRecordIdentifier();
						}
						String fileTempName=fileTemplate.getTemplateName();
						if(rowIdentifier!=null && !(rowIdentifier.isEmpty())){
							fileTempName=fileTempName+"-"+rowIdentifier;
						}
						if(fileTemplate!=null && fileTemplate.getTemplateName()!=null)
						{
							map.put("templateName", fileTempName);
							map.put("dataViewName", fileTempName);
							map.put("dataViewDisplayName", fileTempName);
						}
						map.put("refDvType", "File Template");
						map.put("colDataType", colDataType);
						if(ftemp.getLineNumber()!=null)
							map.put("lineNumber", ftemp.getLineNumber());
						if(ftemp.getColumnHeader()!=null)
							map.put("columnName", ftemp.getColumnHeader());
						if(ftemp.getMasterTableReferenceColumn()!=null)
							map.put("masterTableReferenceColumn", ftemp.getMasterTableReferenceColumn());
						if(ftemp.getRecordTYpe()!=null)
							map.put("recordTYpe", ftemp.getRecordTYpe());
						if(ftemp.getRecordIdentifier()!=null)
							map.put("recordIdentifier",ftemp.getRecordIdentifier());
						if(ftemp.getColumnNumber()!=null)
							map.put("columnNumber", ftemp.getColumnNumber());
						if(ftemp.getEnclosedChar()!=null)
							map.put("enclosedChar", ftemp.getEnclosedChar());
						if(ftemp.getPositionBegin()!=null)
							map.put("positionBegin", ftemp.getPositionBegin());
						if(ftemp.getPositionEnd()!=null)
							map.put("positionEnd", ftemp.getPositionEnd());
						if(ftemp.getColumnHeader()!=null)
							map.put("columnHeader", ftemp.getColumnHeader());
						if(ftemp.getConstantValue()!=null)
							map.put("constantValue", ftemp.getConstantValue());
						if(ftemp.getZeroFill()!=null)
							map.put("zeroFill", ftemp.getZeroFill());
						if(ftemp.getAlign()!=null)
							map.put("align", ftemp.getAlign());
						if(ftemp.getDateFormat()!=null)
							map.put("dateFormat", ftemp.getDateFormat());
						if(ftemp.getTimeFormat()!=null)
							map.put("timeFormat",ftemp.getTimeFormat());
						if(ftemp.getAmountFormat()!=null)
							map.put("amountFormat", ftemp.getAmountFormat());
						if(ftemp.getOverFlow()!=null)
							map.put("overFlow",ftemp.getOverFlow()!=null);
						if(ftemp.getSkipColumn()!=null)
							map.put("skipColumn", ftemp.getSkipColumn());
						if(ftemp.getColumnDelimiter()!=null)
							map.put("columnDelimiter", ftemp.getColumnDelimiter());
						if(ftemp.getCreatedBy()!=null)
							map.put("createdBy", ftemp.getCreatedBy());
						if(ftemp.getCreatedDate()!=null)
							map.put("createdDate", ftemp.getCreatedDate());
						if(ftemp.getLastUpdatedBy()!=null)
							map.put("lastUpdatedBy", ftemp.getLastUpdatedBy());
						if( ftemp.getLastUpdatedDate()!=null)
							map.put("lastUpdatedDate", ftemp.getLastUpdatedDate());
						if(map!=null)
							ftlMapList.add(map);
					}
					finalList.add(ftlMapList);
				}
				log.info("ftempLines.size(): "+ftempLines.size());
			}

		}
		log.info("finalList sz: "+finalList.size());
		return finalList;
	}

	/**
	 * Author: Swetha
	 * @param ftIdTiIntIdMapList
	 * @param tenantId
	 * @return
	 */
	public List getFileTempleLinesForTemplateTest( List<HashMap> ftIdTiIntIdMapList,Long tenantId){

		log.info("ftIdTiIntIdMapList.size(): "+ftIdTiIntIdMapList.size());
		List finalList=new ArrayList<>();
		for(int n=0;n<ftIdTiIntIdMapList.size();n++){
			HashMap intmap=(HashMap) ftIdTiIntIdMapList.get(n);
			Iterator entries = intmap.entrySet().iterator();
			while (entries.hasNext()) {

				Map.Entry entry = (Map.Entry) entries.next();
				String ftIdForDisplay=entry.getKey().toString();
				Long intermediateId=null;
				if(entry.getValue()!=null){
					intermediateId=Long.parseLong(entry.getValue().toString());
				}
				//log.info("ftIdForDisplay: "+ftIdForDisplay+" intermediateId: "+intermediateId);
				//FileTemplates fileTemplate=fileTemplatesRepository.findOne(ftId);
				FileTemplates fileTemplate=fileTemplatesRepository.findByIdForDisplayAndTenantId(ftIdForDisplay, tenantId);
				Long ftId=fileTemplate.getId();
				//log.info("fileTemplate: "+fileTemplate);
				List<FileTemplateLines> finTempLineList=new ArrayList<FileTemplateLines>();
				List<FileTemplateLines> ftempLines=new ArrayList<FileTemplateLines>();
				if(fileTemplate.getMultipleIdentifier()!=null && fileTemplate.getMultipleIdentifier()==true){

					//log.info("intermediateId for ftId: "+ftIdForDisplay+" is: "+intermediateId);
					List<HashMap> ftlMapList=new ArrayList<HashMap>();
					ftempLines=fileTemplateLinesRepository.findByTemplateIdAndIntermediateIdOrderByLineNumber(ftId, intermediateId);
					log.info("ftempLines sz for ftId: "+ftIdForDisplay+" and intermediateId: "+intermediateId+" is: "+ftempLines.size()+" at n: "+n);
					log.info("ftempLines.sz: "+ftempLines.size());

					for(int i=0;i<ftempLines.size();i++){

						FileTemplateLines ftemp=ftempLines.get(i);
						HashMap map=new HashMap();

						String colDataType="";
						map.put("id",ftemp.getId() );
						map.put("refDvName",ftIdForDisplay );
						map.put("refDvColumn",ftemp.getId() );
						map.put("dataViewId", fileTemplate.getId());

						String rowIdentifier="";
						intermediateId=ftemp.getIntermediateId();
						map.put("intermediateId", intermediateId);
						if(intermediateId!=null && intermediateId>0){
							rowIdentifier=ftemp.getRecordIdentifier();
						}
						String fileTempName=fileTemplate.getTemplateName();
						if(rowIdentifier!=null && !(rowIdentifier.isEmpty())){
							fileTempName=fileTempName+"-"+rowIdentifier;
						}
						if(fileTemplate!=null && fileTemplate.getTemplateName()!=null)
						{
							map.put("templateName", fileTempName);
							map.put("dataViewName", fileTempName);
							map.put("dataViewDisplayName", fileTempName);
						}
						map.put("refDvType", "File Template");
						map.put("colDataType", colDataType);
						if(ftemp.getLineNumber()!=null)
							map.put("lineNumber", ftemp.getLineNumber());
						if(ftemp.getColumnHeader()!=null)
							map.put("columnName", ftemp.getColumnHeader());
						if(ftemp.getMasterTableReferenceColumn()!=null)
							map.put("masterTableReferenceColumn", ftemp.getMasterTableReferenceColumn());
						if(ftemp.getRecordTYpe()!=null)
							map.put("recordTYpe", ftemp.getRecordTYpe());
						if(ftemp.getRecordIdentifier()!=null)
							map.put("recordIdentifier",ftemp.getRecordIdentifier());
						if(ftemp.getColumnNumber()!=null)
							map.put("columnNumber", ftemp.getColumnNumber());
						if(ftemp.getEnclosedChar()!=null)
							map.put("enclosedChar", ftemp.getEnclosedChar());
						if(ftemp.getPositionBegin()!=null)
							map.put("positionBegin", ftemp.getPositionBegin());
						if(ftemp.getPositionEnd()!=null)
							map.put("positionEnd", ftemp.getPositionEnd());
						if(ftemp.getColumnHeader()!=null)
							map.put("columnHeader", ftemp.getColumnHeader());
						if(ftemp.getConstantValue()!=null)
							map.put("constantValue", ftemp.getConstantValue());
						if(ftemp.getZeroFill()!=null)
							map.put("zeroFill", ftemp.getZeroFill());
						if(ftemp.getAlign()!=null)
							map.put("align", ftemp.getAlign());
						if(ftemp.getDateFormat()!=null)
							map.put("dateFormat", ftemp.getDateFormat());
						if(ftemp.getTimeFormat()!=null)
							map.put("timeFormat",ftemp.getTimeFormat());
						if(ftemp.getAmountFormat()!=null)
							map.put("amountFormat", ftemp.getAmountFormat());
						if(ftemp.getOverFlow()!=null)
							map.put("overFlow",ftemp.getOverFlow()!=null);
						if(ftemp.getSkipColumn()!=null)
							map.put("skipColumn", ftemp.getSkipColumn());
						if(ftemp.getColumnDelimiter()!=null)
							map.put("columnDelimiter", ftemp.getColumnDelimiter());
						if(ftemp.getCreatedBy()!=null)
							map.put("createdBy", ftemp.getCreatedBy());
						if(ftemp.getCreatedDate()!=null)
							map.put("createdDate", ftemp.getCreatedDate());
						if(ftemp.getLastUpdatedBy()!=null)
							map.put("lastUpdatedBy", ftemp.getLastUpdatedBy());
						if( ftemp.getLastUpdatedDate()!=null)
							map.put("lastUpdatedDate", ftemp.getLastUpdatedDate());
						if(map!=null)
							ftlMapList.add(map);
					}
					finalList.add(ftlMapList);
				}
				else{
					//log.info("in non multi row identifier case");
					ftempLines=fileTemplateLinesRepository.findByTemplateId(ftId);
					List<HashMap> ftlMapList=new ArrayList<HashMap>();
					for(int i=0;i<ftempLines.size();i++){

						FileTemplateLines ftemp=ftempLines.get(i);
						HashMap map=new HashMap();

						String colDataType="";
						map.put("id",ftemp.getId() );
						map.put("refDvName",ftIdForDisplay );
						map.put("refDvColumn",ftemp.getId() );
						map.put("dataViewId", fileTemplate.getId());

						String rowIdentifier="";
						map.put("intermediateId", intermediateId);
						if(intermediateId!=null && intermediateId>0){
							rowIdentifier=ftemp.getRecordIdentifier();
						}
						String fileTempName=fileTemplate.getTemplateName();
						if(rowIdentifier!=null && !(rowIdentifier.isEmpty())){
							fileTempName=fileTempName+"-"+rowIdentifier;
						}
						if(fileTemplate!=null && fileTemplate.getTemplateName()!=null)
						{
							map.put("templateName", fileTempName);
							map.put("dataViewName", fileTempName);
							map.put("dataViewDisplayName", fileTempName);
						}
						map.put("refDvType", "File Template");
						map.put("colDataType", colDataType);
						if(ftemp.getLineNumber()!=null)
							map.put("lineNumber", ftemp.getLineNumber());
						if(ftemp.getColumnHeader()!=null)
							map.put("columnName", ftemp.getColumnHeader());
						if(ftemp.getMasterTableReferenceColumn()!=null)
							map.put("masterTableReferenceColumn", ftemp.getMasterTableReferenceColumn());
						if(ftemp.getRecordTYpe()!=null)
							map.put("recordTYpe", ftemp.getRecordTYpe());
						if(ftemp.getRecordIdentifier()!=null)
							map.put("recordIdentifier",ftemp.getRecordIdentifier());
						if(ftemp.getColumnNumber()!=null)
							map.put("columnNumber", ftemp.getColumnNumber());
						if(ftemp.getEnclosedChar()!=null)
							map.put("enclosedChar", ftemp.getEnclosedChar());
						if(ftemp.getPositionBegin()!=null)
							map.put("positionBegin", ftemp.getPositionBegin());
						if(ftemp.getPositionEnd()!=null)
							map.put("positionEnd", ftemp.getPositionEnd());
						if(ftemp.getColumnHeader()!=null)
							map.put("columnHeader", ftemp.getColumnHeader());
						if(ftemp.getConstantValue()!=null)
							map.put("constantValue", ftemp.getConstantValue());
						if(ftemp.getZeroFill()!=null)
							map.put("zeroFill", ftemp.getZeroFill());
						if(ftemp.getAlign()!=null)
							map.put("align", ftemp.getAlign());
						if(ftemp.getDateFormat()!=null)
							map.put("dateFormat", ftemp.getDateFormat());
						if(ftemp.getTimeFormat()!=null)
							map.put("timeFormat",ftemp.getTimeFormat());
						if(ftemp.getAmountFormat()!=null)
							map.put("amountFormat", ftemp.getAmountFormat());
						if(ftemp.getOverFlow()!=null)
							map.put("overFlow",ftemp.getOverFlow()!=null);
						if(ftemp.getSkipColumn()!=null)
							map.put("skipColumn", ftemp.getSkipColumn());
						if(ftemp.getColumnDelimiter()!=null)
							map.put("columnDelimiter", ftemp.getColumnDelimiter());
						if(ftemp.getCreatedBy()!=null)
							map.put("createdBy", ftemp.getCreatedBy());
						if(ftemp.getCreatedDate()!=null)
							map.put("createdDate", ftemp.getCreatedDate());
						if(ftemp.getLastUpdatedBy()!=null)
							map.put("lastUpdatedBy", ftemp.getLastUpdatedBy());
						if( ftemp.getLastUpdatedDate()!=null)
							map.put("lastUpdatedDate", ftemp.getLastUpdatedDate());
						if(map!=null)
							ftlMapList.add(map);
					}
					finalList.add(ftlMapList);
				}
				log.info("ftempLines.size(): "+ftempLines.size());
			}

		}
		log.info("finalList sz: "+finalList.size());
		return finalList;
	}

	@Transactional(readOnly=false,propagation= Propagation.REQUIRES_NEW,rollbackFor=Exception.class)
	public ErrorReport saveFileTemplate(FileTempDTO fileTempDTO,Long userId,Long tenantId,String bulkUpload,FileTemplatesPostingDTO fileTemplatesPostingDTO)
	{
		log.info("save template with name"+fileTempDTO.getTemplateName());
		FileTemplates filetempRecord=new FileTemplates();
		FileTemplates fileTemplate=new FileTemplates();
		if(fileTempDTO.getId()!=null)
		{

			FileTemplates ft= fileTemplatesRepository.findByIdForDisplayAndTenantId(fileTempDTO.getId(), tenantId);
			fileTemplate.setId(ft.getId());
			if(userId != null)
				fileTemplate.setCreatedBy(userId);
		}
		if(userId != null)
			fileTemplate.setLastUpdatedBy(userId);
		if(fileTempDTO.getTemplateName()!=null&&!fileTempDTO.getTemplateName().isEmpty())
			fileTemplate.setTemplateName(fileTempDTO.getTemplateName());
		if(fileTempDTO.getDescription()!=null&&!fileTempDTO.getDescription().isEmpty())
			fileTemplate.setDescription(fileTempDTO.getDescription());

		if(fileTempDTO.getStartDate()!=null)
			fileTemplate.setStartDate(fileTempDTO.getStartDate());
		if(fileTempDTO.getEndDate()!=null)
			fileTemplate.setEndDate(fileTempDTO.getEndDate());
		fileTemplate.setMultipleIdentifier(fileTempDTO.getMultipleRowIdentifiers());
		if(fileTempDTO.getStatus()!=null&&!fileTempDTO.getStatus().isEmpty())
			fileTemplate.setStatus(fileTempDTO.getStatus());
		else
			fileTemplate.setStatus("Active");
		if(fileTempDTO.getFileType()!=null&&!fileTempDTO.getFileType().isEmpty())
			fileTemplate.setFileType(fileTempDTO.getFileType());
		if(fileTempDTO.getFileType()!=null&&!fileTempDTO.getFileType().isEmpty())
			fileTemplate.setDelimiter(fileTempDTO.getDelimiter());
		if(fileTempDTO.getSource()!=null&&!fileTempDTO.getSource().isEmpty())
			fileTemplate.setSource(fileTempDTO.getSource());
		if(fileTempDTO.getSkipRowsStart()!=null)
			fileTemplate.setSkipRowsStart(fileTempDTO.getSkipRowsStart());
		if(fileTempDTO.getSkipRowsEnd()!=null)
			fileTemplate.setSkipRowsEnd(fileTempDTO.getSkipRowsEnd());
		if(fileTempDTO.getNumberOfColumns()!=null)
			fileTemplate.setNumber_of_columns(fileTempDTO.getNumberOfColumns());
		if(fileTempDTO.getHeaderRowNumber()!=null)
			fileTemplate.setHeaderRowNumber(fileTempDTO.getHeaderRowNumber());
		if(fileTempDTO.getRowIdentifier()!=null)
			log.info("fileTempDTO.getRowIdentifier(): "+fileTempDTO.getRowIdentifier());
		fileTemplate.setRowIdentifier(fileTempDTO.getRowIdentifier());
		fileTemplate.setData(fileTempDTO.getData());
		String startRowColumns = "";
		if(fileTempDTO.getStartRowColumns() != null)
		{
			List<String> cols = fileTempDTO.getStartRowColumns();
			if(cols.size() >0)
			{
				for(int c=0;c<cols.size();c++)
				{
					if(c == cols.size()-1)
						startRowColumns = startRowColumns+cols.get(c);
					else
						startRowColumns = startRowColumns + cols.get(c)+ ',' ;
				}
			}
			if(startRowColumns != null)
				fileTemplate.setStartRowColumns(startRowColumns);
		}


		/*if(fileTempDTO.getSampleData().size() > 0)
		{
			String sampleDataConvToBlob = "";
			sampleDataConvToBlob = findDelimiterAndFileExtensionService.savingBlobData(fileTempDTO.getSampleData());
			if(sampleDataConvToBlob != null && !sampleDataConvToBlob.isEmpty() && !sampleDataConvToBlob.equals(""))
			{
				fileTemplate.setData(sampleDataConvToBlob);
			}
		}*/
		fileTemplate.setTenantId(tenantId);
		//	if(fileTempDTO.getTenantId()!=null)
		//fileTemplate.setTenantId(fileTempDTO.getTenantId());
		//if(fileTempDTO.getCreatedBy()!=null)
		//fileTemplate.setCreatedBy(fileTempDTO.getCreatedBy());
		//	if(fileTempDTO.getLastUpdatedBy()!=null)
		//fileTemplate.setLastUpdatedBy(fileTempDTO.getLastUpdatedBy());
		fileTemplate.setCreatedDate(ZonedDateTime.now());
		fileTemplate.setLastUpdatedDate(ZonedDateTime.now());
		//if(fileTemplate!=null)
		//By Kiran to set sample data filename
		if(fileTempDTO.getSdFilename()!=null)
			fileTemplate.setSdFilename(fileTempDTO.getSdFilename());
		filetempRecord=fileTemplatesRepository.save(fileTemplate);

		String idForDisplay = IDORUtils.computeFrontEndIdentifier(filetempRecord.getId().toString());
		filetempRecord.setIdForDisplay(idForDisplay);
		filetempRecord = fileTemplatesRepository.save(filetempRecord);
		ErrorReport fileTempErrorReport=new ErrorReport();
		fileTempErrorReport.setTaskName("File Template Save "+filetempRecord.getTemplateName());
		if(filetempRecord !=null)
		{
			//fileTemplatesSearchRepository.save(filetempRecord);
			log.info("fileTemplate :"+fileTemplate);
			if(filetempRecord.getId()!=null)
			{
				fileTempErrorReport.setTaskStatus("Success");
				fileTempErrorReport.setDetails(filetempRecord.getIdForDisplay()+"");
				List<ErrorReport> linesErrorReports = new ArrayList<ErrorReport>();
				log.info("update lines for existing template");
				if(bulkUpload != null && bulkUpload.equalsIgnoreCase("false"))
				{
					try{
						linesErrorReports = saveIdentifiersAndTemplateLines( filetempRecord, fileTempDTO, tenantId, fileTemplatesPostingDTO);
					}
					catch(Exception e)
					{

					}
				}
			}
			else{
				fileTempErrorReport.setTaskStatus("Failure");
			}
		}
		else
		{
			fileTempErrorReport.setTaskStatus("Failure");
		}

		return fileTempErrorReport;
	}
	public List<ErrorReport> saveTemplateLines(FileTemplates filetempRecord,FileTempDTO fileTempDTO,Long tenantId,FileTemplatesPostingDTO fileTemplatesPostingDTO)
	{

		List<ErrorReport> errorReports = new ArrayList<ErrorReport>();
		HashMap<String,Long> interRecordToIdMap = new HashMap<String,Long>();
		if(filetempRecord != null && filetempRecord.getId() != null && fileTempDTO.getMultipleRowIdentifiers() == true)
		{

			List<MultipleIdentifiersDTO> multipleRIList = new ArrayList<MultipleIdentifiersDTO>();
			multipleRIList = fileTemplatesPostingDTO.getMultipleRIList();
			Long templateId = filetempRecord.getId() ;
			List<ErrorReport> identifiersReports = new ArrayList<ErrorReport>();
			List<IntermediateTable> multipleIdentifiersForTemplate = new ArrayList<IntermediateTable>();
			for(MultipleIdentifiersDTO MRI : multipleRIList)
			{
				ErrorReport identifierRep = new ErrorReport();
				identifierRep.setTaskName("Save Identifier");
				IntermediateTable dupIdentifierRec = new IntermediateTable();
				dupIdentifierRec = intermediateTableRepository.findByTemplateIdAndRowIdentifierAndRowIdentifierCriteria(templateId, MRI.getRowIdentifier(),MRI.getCriteria());
				if(dupIdentifierRec != null)
				{
					identifierRep.setTaskStatus("Identifier with this combination already exists for the template");
				}
				else
				{
					IntermediateTable rowIdentifierRecord = new IntermediateTable();
					if(fileTempDTO.getCreatedBy()!=null)
						rowIdentifierRecord.setCreatedBy(fileTempDTO.getCreatedBy());
					rowIdentifierRecord.setCreationDate(ZonedDateTime.now());
					/*if(MRI.getData().size() >0)
					{
						String sampleDataConvToBlob = "";
						sampleDataConvToBlob = findDelimiterAndFileExtensionService.savingBlobData(MRI.getData());
						if(sampleDataConvToBlob != null && !sampleDataConvToBlob.isEmpty() && !sampleDataConvToBlob.equals(""))
						{
							fileTemplate.setData(sampleDataConvToBlob);
						}
					}*/
					if(MRI.getId() != null)
						rowIdentifierRecord.setId(MRI.getId());
					rowIdentifierRecord.setData(MRI.getData());
					//rowIdentifierRecord.setHeaderInfo(headerInfo);
					rowIdentifierRecord.setLastUpdatedBy(fileTempDTO.getCreatedBy());
					rowIdentifierRecord.setLastUpdatedDate(ZonedDateTime.now());
					rowIdentifierRecord.setPositionEnd(MRI.getPositionEnd());
					rowIdentifierRecord.setPositionStart(MRI.getPositionStart());
					rowIdentifierRecord.setRowIdentifier(MRI.getRowIdentifier());
					rowIdentifierRecord.setRowIdentifierCriteria(MRI.getCriteria());
					//rowIdentifierRecord.setRowInfo(MRI.get);
					if(templateId != null)
						rowIdentifierRecord.setTemplateId(templateId);
					rowIdentifierRecord.setTenantId(tenantId);
					//if(fileTempDTO.getTenantId()!=null)
					//rowIdentifierRecord.setTenantId(fileTempDTO.getTenantId());
					multipleIdentifiersForTemplate.add(rowIdentifierRecord);
					identifierRep.setTaskStatus("Identifier saved successfully");
				}
				identifiersReports.add(identifierRep);
			}
			List<IntermediateTable> savedIdentifiers = intermediateTableRepository.save(multipleIdentifiersForTemplate);

			if(savedIdentifiers != null && savedIdentifiers.size()>0)
			{
				for(IntermediateTable ITR : savedIdentifiers){
					interRecordToIdMap.put(ITR.getRowIdentifier(), ITR.getId());
				}
			}
			log.info("interRecordToIdMap:"+interRecordToIdMap);
		}
		HashMap<String,Long> headerToTempLineIdMap = new HashMap<String,Long>();
		if(filetempRecord.getId()!=null)
		{
			if(fileTemplatesPostingDTO.getFileTemplateLinesListDTO().size() > 0)
			{
				List<ErrorReport> templateLinesListReports = new ArrayList<ErrorReport>();
				for(int j =0;j<fileTemplatesPostingDTO.getFileTemplateLinesListDTO().size();j++)
				{

					List<FileTemplateLinesDTO> FileTemplateLinesList=fileTemplatesPostingDTO.getFileTemplateLinesListDTO().get(j);
					List<ErrorReport> templateLinesReports = new ArrayList<ErrorReport>();
					if(FileTemplateLinesList.size()>0)
					{
						List<FileTemplateLines> fileTempLinesList=new ArrayList<FileTemplateLines>();
						String str="";
						ErrorReport fileTempLinesErrReport=new ErrorReport();
						for(int i=0;i<FileTemplateLinesList.size();i++)
						{
							ErrorReport tempLineReport = new ErrorReport();
							FileTemplateLinesDTO fileTempLinesDTO=FileTemplateLinesList.get(i);
							FileTemplateLines fileTempLines=new FileTemplateLines();
							if(fileTempLinesDTO.getColumnHeader() !=null&&!fileTempLinesDTO.getColumnHeader().isEmpty())
							{
								if(fileTempLinesDTO.getId()!=null)
								{
									fileTempLines.setId(fileTempLinesDTO.getId());
								}
								if(filetempRecord.getId()!=null)
									fileTempLines.setTemplateId(filetempRecord.getId());
								//link intermediate id
								if(fileTempDTO.getMultipleRowIdentifiers() == true)
								{
									log.info("yes its true");
									String MI = "";
									log.info("fileTempLinesDTO.getMI()"+fileTempLinesDTO.getInterRI());
									MI = fileTempLinesDTO.getInterRI();
									log.info("MI"+MI);

									if(interRecordToIdMap != null && interRecordToIdMap.containsKey(MI) && interRecordToIdMap.get(MI)!= null)
									{
										fileTempLines.setIntermediateId(interRecordToIdMap.get(MI));
									}
									else
									{
										log.info("interRecordToIdMap.get(MI) isnull");
									}
								}
								//log.info("fileTempLinesDTO.getLineNumber(): "+fileTempLinesDTO.getLineNumber());
								if(fileTempLinesDTO.getRecordStartRow()!=null)
									fileTempLines.setRecordStartRow(fileTempLinesDTO.getRecordStartRow());

								if(fileTempLinesDTO.getLineNumber()!=null)
									fileTempLines.setLineNumber(fileTempLinesDTO.getLineNumber());
								if(fileTempLinesDTO.getMasterTableReferenceColumn()!=null)
									fileTempLines.setMasterTableReferenceColumn(fileTempLinesDTO.getMasterTableReferenceColumn());
								if(fileTempLinesDTO.getRecordTYpe()!=null&&!fileTempLinesDTO.getRecordTYpe().isEmpty())
								{
									//log.info("fileTempLinesDTO printing2:"+fileTempLinesDTO.getRecordTYpe());
									fileTempLines.setRecordTYpe(fileTempLinesDTO.getRecordTYpe());
								}else
									fileTempLines.setRecordTYpe("Row Data");
								//log.info("fileTempLinesDTO printing3:"+fileTempLines.getRecordTYpe());

								log.info("fileTempLinesDTO.getRecordIdentifier(): "+fileTempLinesDTO.getRecordIdentifier()+" fileTempLinesDTO.getRecordStartRow(): "+fileTempLinesDTO.getRecordStartRow());

								if(fileTempLinesDTO.getRecordIdentifier()!=null)
								{
									fileTempLines.setRecordIdentifier(fileTempLinesDTO.getRecordIdentifier());
								}
								else{
									fileTempLines.setRecordIdentifier(fileTempLinesDTO.getRecordStartRow());}

								if(fileTempLinesDTO.getColumnNumber()!=null)
									fileTempLines.setColumnNumber(fileTempLinesDTO.getColumnNumber());

								if(fileTempLinesDTO.getRecordStartRow()!=null)
									//log.info("fileTempLinesDTO.getRecordStartRow(): "+fileTempLinesDTO.getRecordStartRow());
									//fileTempLines.setRecordStartRow(fileTempLinesDTO.getRecordStartRow());
									if(fileTempLinesDTO.getEnclosedChar()!=null&&!fileTempLinesDTO.getEnclosedChar().isEmpty())
										fileTempLines.setEnclosedChar(fileTempLinesDTO.getEnclosedChar());
								if(fileTempLinesDTO.getEnclosedChar()!=null&&!fileTempLinesDTO.getEnclosedChar().isEmpty())
									fileTempLines.setEnclosedChar(fileTempLinesDTO.getEnclosedChar());
								if(fileTempLinesDTO.getPositionBegin()!=null)
									fileTempLines.setPositionBegin(fileTempLinesDTO.getPositionBegin());
								if(fileTempLinesDTO.getPositionEnd()!=null)
									fileTempLines.setPositionEnd(fileTempLinesDTO.getPositionEnd());
								if(fileTempLinesDTO.getColumnHeader()!=null&&!fileTempLinesDTO.getColumnHeader().isEmpty())
									fileTempLines.setColumnHeader(fileTempLinesDTO.getColumnHeader());
								if(fileTempLinesDTO.getColumnHeader()!=null&&!fileTempLinesDTO.getColumnHeader().isEmpty())
									fileTempLines.setConstantValue(fileTempLinesDTO.getConstantValue());
								if(fileTempLinesDTO.getZeroFill()!=null&&!fileTempLinesDTO.getZeroFill().isEmpty())
									fileTempLines.setZeroFill(fileTempLinesDTO.getZeroFill());
								if(fileTempLinesDTO.getAlign()!=null&&!fileTempLinesDTO.getAlign().isEmpty())
									fileTempLines.setAlign(fileTempLinesDTO.getAlign());
								if(fileTempLinesDTO.getFormula()!=null&&!fileTempLinesDTO.getFormula().isEmpty())
									fileTempLines.setFormula(fileTempLinesDTO.getFormula());
								if(fileTempLinesDTO.getDateFormat()!=null&&!fileTempLinesDTO.getDateFormat().isEmpty())
									fileTempLines.setDateFormat(fileTempLinesDTO.getDateFormat());
								if(fileTempLinesDTO.getTimeFormat()!=null&&!fileTempLinesDTO.getTimeFormat().isEmpty())
									fileTempLines.setTimeFormat(fileTempLinesDTO.getTimeFormat());
								if(fileTempLinesDTO.getAmountFormat()!=null&&!fileTempLinesDTO.getAmountFormat().isEmpty())
									fileTempLines.setAmountFormat(fileTempLinesDTO.getAmountFormat());
								if(fileTempLinesDTO.getAmountFormat()!=null&&!fileTempLinesDTO.getAmountFormat().isEmpty())
									fileTempLines.setOverFlow(fileTempLinesDTO.getOverFlow());
								if(fileTempLinesDTO.getSkipColumn()!=null&&!fileTempLinesDTO.getSkipColumn().isEmpty())
									fileTempLines.setSkipColumn(fileTempLinesDTO.getSkipColumn());
								if(fileTempLinesDTO.getColumnDelimeter()!=null&&!fileTempLinesDTO.getColumnDelimeter().isEmpty())
									fileTempLines.setColumnDelimiter(fileTempLinesDTO.getColumnDelimeter());
								if(fileTempLinesDTO.getCreatedBy()!=null)
									fileTempLines.setCreatedBy(fileTempLinesDTO.getCreatedBy());

								fileTempLines.setCreatedDate(ZonedDateTime.now());
								if(fileTempLinesDTO.getLastUpdatedBy()!=null)
									fileTempLines.setLastUpdatedBy(fileTempLinesDTO.getCreatedBy());
								fileTempLines.setLastUpdatedDate(ZonedDateTime.now());



								//	if(fileTempLinesDTO.getColumnHeader().)
								// Swetha
								/* Logic to process column header and save it in column alias */
								//String s=fileTempLinesDTO.getColumnHeader();
								//String s="% Rate -???^&df_#S";
								String s=fileTempLinesDTO.getColumnHeader();
								Pattern pattern = Pattern.compile("[^a-z A-Z 0-9]");
								Matcher matcher = pattern.matcher(s);
								String number = matcher.replaceAll("");
								//log.info("number: "+number);

								String finalCol="";
								String b[]=number.split("\\s+");
								for(int l=0;l<b.length;l++){
									//log.info("b[l] at l: "+l+" is: "+b[l]);
									if(b[l]!=null && !(b[l].isEmpty())){
										String val1=Character.toString(b[l].charAt(0));
										String val2=Character.toString(Character.toTitleCase(b[l].charAt(0)));
										if(b[l].contains(val1)){
											//log.info("contains");
											b[l]=b[l].replace(val1, val2);
										}
									}
									finalCol=finalCol.concat(b[l]);
								}

								log.info("finalCol: "+finalCol);
								fileTempLines.setColumnAlias(finalCol+"_"+filetempRecord.getId());


								FileTemplateLines fileTempLine=fileTemplateLinesRepository.save(fileTempLines);
								//fileTemplateLinesSearchRepository.save(fileTempLine);
								headerToTempLineIdMap.put(fileTempLine.getColumnHeader(), fileTempLine.getId());
								fileTempLinesList.add(fileTempLine);
								//log.info("fileTempLinesDTO.line: :"+fileTempLinesDTO.getLineNumber());
								if(fileTempLine!=null&&fileTempLine.getId()!=null)
								{
									fileTempLinesErrReport.setTaskName("File Template Lines Save");
									fileTempLinesErrReport.setTaskStatus("Success");

								}
								else
								{
									fileTempLinesErrReport.setTaskName("File Template Lines Save");
									fileTempLinesErrReport.setTaskStatus("Failure");
									str=str+fileTempLinesDTO.getLineNumber()+",";
									//fileTempLinesErrReport.setDetails("Failed to save at line number :"+str);
								}
							}
						}
						//removing "," for last string
						if (str.endsWith(",")) {
							str = str.substring(0, str.length() - 1);
						}
						if(fileTempLinesErrReport.getTaskStatus()!=null&&fileTempLinesErrReport.getTaskStatus().equalsIgnoreCase("failure"))
						{
						}
						if(fileTempLinesErrReport.getTaskStatus()!=null && fileTempLinesErrReport.getTaskStatus().equalsIgnoreCase("Success"))
						{
							ErrorReport fileTempLinesErrorReport=new ErrorReport();
							fileTempLinesErrorReport.setTaskName("File Template Lines Save");
							if(FileTemplateLinesList.size()==fileTempLinesList.size())
							{
								fileTempLinesErrorReport.setTaskStatus("Success");
							}
							else
							{
								fileTempLinesErrorReport.setTaskStatus("Failure");
							}
						}

					}
				}
			}
			//headerToTempLineIdMap
			if(fileTemplatesPostingDTO.getEndRowConditionsList() != null)
			{
				List<RowConditionsDTO> endRowConditions = fileTemplatesPostingDTO.getEndRowConditionsList();
				for(int endRowCondInd=0;endRowCondInd<endRowConditions.size();endRowCondInd++)
				{
					RowConditionsDTO endConditionDto = new RowConditionsDTO();
					endConditionDto = endRowConditions.get(endRowCondInd);

					RowConditions endCondition = new RowConditions();

					Long tempLineId = null;
					if(endConditionDto.getColumnName() != null && headerToTempLineIdMap.get(endConditionDto.getColumnName()) != null)
						tempLineId = headerToTempLineIdMap.get(endConditionDto.getColumnName());
					if(tempLineId != null)
						endCondition.setTemplateLineId(tempLineId);
					endCondition.setType(endConditionDto.getType());
					endCondition.setOperator(endConditionDto.getOperator());
					endCondition.setValue(endConditionDto.getValue());
					endCondition.setLogicalOperator(endConditionDto.getLogicalOperator());
					rowConditionsRepository.save(endCondition);
				}
			}
			if(fileTemplatesPostingDTO.getSkipRowConditionsList() != null)
			{
				List<RowConditionsDTO> skipRowConditions = fileTemplatesPostingDTO.getSkipRowConditionsList();
				for(int endRowCondInd=0;endRowCondInd<skipRowConditions.size();endRowCondInd++)
				{
					RowConditionsDTO skipConditionDto = new RowConditionsDTO();
					skipConditionDto = skipRowConditions.get(endRowCondInd);

					RowConditions skipCondition = new RowConditions();

					Long tempLineId = null;
					if(skipConditionDto.getColumnName() != null && headerToTempLineIdMap.get(skipConditionDto.getColumnName()) != null)
						tempLineId = headerToTempLineIdMap.get(skipConditionDto.getColumnName());
					if(tempLineId != null)
						skipCondition.setTemplateLineId(tempLineId);
					skipCondition.setType(skipConditionDto.getType());
					skipCondition.setOperator(skipConditionDto.getOperator());
					skipCondition.setValue(skipConditionDto.getValue());
					skipCondition.setLogicalOperator(skipConditionDto.getLogicalOperator());
					rowConditionsRepository.save(skipCondition);
				}
			}


			SourceProfileFileAssignments SPA=new SourceProfileFileAssignments();
			if(fileTemplatesPostingDTO.getSourceProfileFileAssignmentDTO()!=null && fileTemplatesPostingDTO.getSourceProfileFileAssignmentDTO().getSourceProfileId() != null)
			{
				TenantConfig  tenantConfig=tenantConfigRepository.findByTenantIdAndKey(tenantId, "LocalDirPath");
				log.info("tenantConfig: "+tenantConfig.toString());
				SourceProfileFileAssignmentDTO SPADTO=fileTemplatesPostingDTO.getSourceProfileFileAssignmentDTO();
				log.info("SPADTO: temp id:"+SPADTO.getTemplateId());
				if(SPADTO.getId()!=null)
				{
					SPA.setId(SPADTO.getId());
				}
				if(SPADTO.getSourceProfileId()!=null)
					SPA.setSourceProfileId(SPADTO.getSourceProfileId());
				if(SPADTO.getFileNameFormat()!=null&&!SPADTO.getFileNameFormat().isEmpty())
					SPA.setFileNameFormat(SPADTO.getFileNameFormat());
				if(SPADTO.getFileDescription()!=null&&!SPADTO.getFileDescription().isEmpty())
					SPA.setFileDescription(SPADTO.getFileDescription());
				if(SPADTO.getFileExtension()!=null&&!SPADTO.getFileExtension().isEmpty())
					SPA.setFileExtension(SPADTO.getFileExtension());
				if(SPADTO.getFrequencyType()!=null&&!SPADTO.getFrequencyType().isEmpty())
					SPA.setFrequencyType(SPADTO.getFrequencyType());
				if(SPADTO.getDueTime()!=null&&!SPADTO.getDueTime().isEmpty())
					SPA.setDueTime(SPADTO.getDueTime());
				if(SPADTO.getDay()!=null)
					SPA.setDay(SPADTO.getDay());
				if(SPADTO.getSourceDirectoryPath()!=null&&!SPADTO.getSourceDirectoryPath().isEmpty())
					SPA.setSourceDirectoryPath(SPADTO.getSourceDirectoryPath());
				if(SPADTO.getLocalDirectoryPath()!=null&&!SPADTO.getLocalDirectoryPath().isEmpty())
					SPA.setLocalDirectoryPath(SPADTO.getLocalDirectoryPath());
				if(filetempRecord.getId()!=null)
					SPA.setTemplateId(filetempRecord.getId());
				SPA.setEnabledFlag(true);
				if(SPADTO.getCreatedBy()!=null)
					SPA.setCreatedBy(SPADTO.getCreatedBy());
				SPA.setCreatedDate(ZonedDateTime.now());
				if(SPADTO.getLastUpdatedBy()!=null)
					SPA.setLastUpdatedBy(SPADTO.getLastUpdatedBy());
				SPA.setLastUpdatedDate(ZonedDateTime.now());

				if(tenantConfig!=null)
					SPA.setLocalDirectoryPath(tenantConfig.getValue());
				SourceProfileFileAssignments sourceProfileAssgn=sourceProfileFileAssignmentsRepository.save(SPA);

				ErrorReport fileTempLinesErrorReport=new ErrorReport();
				fileTempLinesErrorReport.setTaskName("Source Profile Assignment Save");
				if(sourceProfileAssgn.getId()!=null)
				{
					fileTempLinesErrorReport.setTaskStatus("Success");
				}
				else
				{
					fileTempLinesErrorReport.setTaskStatus("Failure");

				}
			}
		}		
		return errorReports;
	}


	/**
	 * Author : Shobha
	 * @param fileTemplatesPostingDTO
	 * @param request
	 * @param bulkUpload
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation= Propagation.REQUIRED)
	public ErrorReport saveTemplate(FileTemplatesPostingDTO fileTemplatesPostingDTO,HttpServletRequest request,String bulkUpload) throws Exception
	{
		log.info("Rest Request to post File template , File Template Lines and source profile assignments for tenant: ");
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		Long userId=Long.parseLong(map.get("userId").toString());

		ErrorReport finalErrorReport=saveFileTemplate(fileTemplatesPostingDTO,tenantId,userId,bulkUpload);
		return finalErrorReport;

/*		FileTemplates filetempRecord=new FileTemplates();
		ErrorReport finalErrorReport=new ErrorReport();
		String errorMessage = ""; 
		String save="Failure";
		int count=0;
		ErrorReport getPostingDTOReport = new ErrorReport();
		getPostingDTOReport.setTaskName("Get File templates content");
		if(fileTemplatesPostingDTO!=null)
		{
			//save FileTemplates

			FileTempDTO fileTempDTO = fileTemplatesPostingDTO.getFileTempDTO();

			if(fileTempDTO != null)
			{
				//log.info("fileTemplatesPostingDTO.getFileTempDTO(): "+fileTemplatesPostingDTO.getFileTempDTO());
				ErrorReport checkForTempNameReport = new ErrorReport();
				checkForTempNameReport.setTaskName("Get File templates content");
				//if id is null, check for duplicates else update template
				if(fileTempDTO.getTemplateName() != null )
				{
					filetempRecord= fileTemplatesRepository.findByTenantIdAndTemplateName(tenantId, fileTempDTO.getTemplateName());
					if(filetempRecord != null)
					{
						if(fileTempDTO.getId() != null )
						{
							//update template
							finalErrorReport = saveFileTemplate(fileTempDTO,userId,tenantId,bulkUpload,fileTemplatesPostingDTO);
						}
						else
						{
							errorMessage = "Template save failed, as template with name "+fileTempDTO.getTemplateName()+" already exists";
							throw new Exception(errorMessage);
						}

					}
					else
					{

						try{
							finalErrorReport = saveFileTemplate(fileTempDTO,userId,tenantId,bulkUpload,fileTemplatesPostingDTO);
						}
						catch(Exception e)
						{
							throw new Exception();
						}

					}
				}else
				{
					checkForTempNameReport.setTaskStatus("file template name doesnot exist");
					finalErrorReport=(checkForTempNameReport);
				}

			}
			else
			{
				getPostingDTOReport.setTaskStatus("Failed to find contents");
				finalErrorReport = (getPostingDTOReport);
			}

		}
		else
		{
			getPostingDTOReport.setTaskStatus("Failed to find contents");
			finalErrorReport = (getPostingDTOReport);
		}
		return finalErrorReport;*/
	}
	
	public ErrorReport saveFileTemplate(FileTemplatesPostingDTO fileTemplatesPostingDTO,Long tenantId, Long userId,String bulkUpload) throws Exception
	{
		FileTemplates filetempRecord=new FileTemplates();
		ErrorReport finalErrorReport=new ErrorReport();
		String errorMessage = ""; 
		String save="Failure";
		int count=0;
		ErrorReport getPostingDTOReport = new ErrorReport();
		getPostingDTOReport.setTaskName("Get File templates content");
		if(fileTemplatesPostingDTO!=null)
		{
			//save FileTemplates

			FileTempDTO fileTempDTO = fileTemplatesPostingDTO.getFileTempDTO();

			if(fileTempDTO != null)
			{
				//log.info("fileTemplatesPostingDTO.getFileTempDTO(): "+fileTemplatesPostingDTO.getFileTempDTO());
				ErrorReport checkForTempNameReport = new ErrorReport();
				checkForTempNameReport.setTaskName("Get File templates content");
				//if id is null, check for duplicates else update template
				if(fileTempDTO.getTemplateName() != null )
				{
					filetempRecord= fileTemplatesRepository.findByTenantIdAndTemplateName(tenantId, fileTempDTO.getTemplateName());
					if(filetempRecord != null)
					{
						if(fileTempDTO.getId() != null )
						{
							//update template
							finalErrorReport = saveFileTemplate(fileTempDTO,userId,tenantId,bulkUpload,fileTemplatesPostingDTO);
						}
						else
						{
							errorMessage = "Template save failed, as template with name "+fileTempDTO.getTemplateName()+" already exists";
							throw new Exception(errorMessage);
						}

					}
					else
					{

						try{
							finalErrorReport = saveFileTemplate(fileTempDTO,userId,tenantId,bulkUpload,fileTemplatesPostingDTO);
						}
						catch(Exception e)
						{
							throw new Exception();
						}

					}
				}else
				{
					checkForTempNameReport.setTaskStatus("file template name doesnot exist");
					finalErrorReport=(checkForTempNameReport);
				}

			}
			else
			{
				getPostingDTOReport.setTaskStatus("Failed to find contents");
				finalErrorReport = (getPostingDTOReport);
			}

		}
		else
		{
			getPostingDTOReport.setTaskStatus("Failed to find contents");
			finalErrorReport = (getPostingDTOReport);
		}
		return finalErrorReport;
	}
	
	/**
	 * Author Kiran
	 * @param fileTemplatesPostingDTO
	 * @param tenantId
	 * @param userId
	 * @param bulkUpload
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation= Propagation.REQUIRED)
	public ErrorReport saveDefaultTemplate(FileTemplatesPostingDTO fileTemplatesPostingDTO,Long tenantId, Long userId,String bulkUpload) throws Exception
	{
		ErrorReport finalErrorReport=saveFileTemplate(fileTemplatesPostingDTO,tenantId,userId,bulkUpload);
		return finalErrorReport;
	}
	
	//@Transactional(readOnly=false,propagation= Propagation.REQUIRES_NEW,rollbackFor=Exception.class)
	public List<ErrorReport> saveIdentifiersAndTemplateLines(FileTemplates filetempRecord,FileTempDTO fileTempDTO,Long tenantId,FileTemplatesPostingDTO fileTemplatesPostingDTO) throws Exception
	{
		log.info("Service to save identifiers and template lines:"+filetempRecord.getId());

		List<ErrorReport> errorReports = new ArrayList<ErrorReport>();
		HashMap<String,Long> interRecordToIdMap = new HashMap<String,Long>();
		IntermediateTable identifierRecord = new IntermediateTable();
		Long templateId = 0L;
		if(filetempRecord != null && filetempRecord.getId() != null)
		{
			templateId = filetempRecord.getId() ;
		}
		if( filetempRecord != null && filetempRecord.getId() != null &&  fileTempDTO.getMultipleRowIdentifiers() == true)
		{
			log.info("Save identifiers as template is with multi data set");
			ErrorReport checkForMultiDataSetReport = new ErrorReport();
			checkForMultiDataSetReport.setTaskName("Check for multiple data sets");
			List<MultipleIdentifiersDTO> multipleRIList = new ArrayList<MultipleIdentifiersDTO>();
			if(fileTemplatesPostingDTO.getMultipleRIList().size()>0)
			{
				multipleRIList = fileTemplatesPostingDTO.getMultipleRIList();
				List<ErrorReport> identifiersReports = new ArrayList<ErrorReport>();

				IntermediateTable multipleIdentifiersForTemplate = new IntermediateTable();
				for(MultipleIdentifiersDTO MRI : multipleRIList)
				{
					ErrorReport identifierRep = new ErrorReport();
					identifierRep.setTaskName("Save Identifier");
					IntermediateTable dupIdentifierRec = new IntermediateTable();
					log.info("parameters are"+templateId+ "-"+MRI.getRowIdentifier()+ "-"+MRI.getCriteria());
					if(MRI.getId() != null)
					{
						
					}else{
						dupIdentifierRec = intermediateTableRepository.findByTemplateIdAndRowIdentifierAndRowIdentifierCriteria(templateId, MRI.getRowIdentifier(),MRI.getCriteria());	
					}
					if(dupIdentifierRec != null && dupIdentifierRec.getId() != null)
					{
						log.info("duplicate identifiers data has"+dupIdentifierRec.toString());
						log.info("identifier already exists");
						identifierRep.setTaskStatus("Identifier with this combination already exists for the template");
					}
					else
					{
						log.info("couldnt identify duplicate");
						IntermediateTable rowIdentifierRecord = new IntermediateTable();
						if(fileTempDTO.getCreatedBy()!=null)
							rowIdentifierRecord.setCreatedBy(fileTempDTO.getCreatedBy());
						rowIdentifierRecord.setCreationDate(ZonedDateTime.now());
						/*if(MRI.getData().size() >0)
						{
							String sampleDataConvToBlob = "";
							sampleDataConvToBlob = findDelimiterAndFileExtensionService.savingBlobData(MRI.getData());
							if(sampleDataConvToBlob != null && !sampleDataConvToBlob.isEmpty() && !sampleDataConvToBlob.equals(""))
							{
								fileTemplate.setData(sampleDataConvToBlob);
							}
						}*/
						if(MRI.getId() != null)
							rowIdentifierRecord.setId(MRI.getId());
						rowIdentifierRecord.setData(MRI.getData());
						//rowIdentifierRecord.setHeaderInfo(headerInfo);
						rowIdentifierRecord.setLastUpdatedBy(fileTempDTO.getCreatedBy());
						rowIdentifierRecord.setLastUpdatedDate(ZonedDateTime.now());
						rowIdentifierRecord.setPositionEnd(MRI.getPositionEnd());
						rowIdentifierRecord.setPositionStart(MRI.getPositionStart());
						rowIdentifierRecord.setRowIdentifier(MRI.getRowIdentifier());
						rowIdentifierRecord.setRowIdentifierCriteria(MRI.getCriteria());
						//rowIdentifierRecord.setRowInfo(MRI.get);
						if(templateId != null)
							rowIdentifierRecord.setTemplateId(templateId);
						rowIdentifierRecord.setTenantId(tenantId);
						//if(fileTempDTO.getTenantId()!=null)
						//rowIdentifierRecord.setTenantId(fileTempDTO.getTenantId());

						IntermediateTable savedIdentifiers = intermediateTableRepository.save(rowIdentifierRecord);
						identifierRecord = savedIdentifiers;
						if(savedIdentifiers != null )
						{
							identifierRep.setTaskStatus("Identifier saved successfully");
							identifiersReports.add(identifierRep);
							interRecordToIdMap.put(identifierRecord.getRowIdentifier(), identifierRecord.getId());
							checkForMultiDataSetReport.setSubTasksList(identifiersReports);
							log.info("interRecordToIdMap:"+interRecordToIdMap);
						}
					}

				}
			}
			else
			{
				checkForMultiDataSetReport.setTaskStatus("Failed to find data sets");
			}
			errorReports.add(checkForMultiDataSetReport);
		}
		HashMap<String,Long> headerToTempLineIdMap = new HashMap<String,Long>();
		if(filetempRecord.getId()!=null)
		{
			log.info("fileTemplatesPostingDTO.getFileTemplateLinesListDTO()"+fileTemplatesPostingDTO.getFileTemplateLinesListDTO().size());
			if(fileTemplatesPostingDTO.getFileTemplateLinesListDTO().size() > 0)
			{
				ErrorReport templateLinesListReports= new ErrorReport();
				templateLinesListReports.setTaskName("Save template lines");
				for(int j =0;j<fileTemplatesPostingDTO.getFileTemplateLinesListDTO().size();j++)
				{
					List<FileTemplateLinesDTO> FileTemplateLinesList=fileTemplatesPostingDTO.getFileTemplateLinesListDTO().get(j);
					List<FileTemplateLines> fileTempLinesList=new ArrayList<FileTemplateLines>();
					List<ErrorReport> templateLinesReports = new ArrayList<ErrorReport>();
					log.info("FileTemplateLinesList"+FileTemplateLinesList.size());
					if(FileTemplateLinesList.size()>0)
					{
						String str="";
						ErrorReport fileTempLinesErrReport = new ErrorReport();

						for(int i=0;i<FileTemplateLinesList.size();i++)
						{
							FileTemplateLinesDTO fileTempLinesDTO=FileTemplateLinesList.get(i);
							FileTemplateLines fileTempLines=new FileTemplateLines();
							String mandatoryFields ="";
							ErrorReport mandatoryCheck = new ErrorReport();
							log.info("fileTempLinesDTO before process"+fileTempLinesDTO.getColumnHeader());
							//log.info("***1***");
							mandatoryCheck.setTaskName("Check for mandatory columns");
							if(fileTempLinesDTO.getColumnHeader() == null || fileTempLinesDTO.getColumnHeader().isEmpty() || fileTempLinesDTO.getColumnHeader().equals(""))
							{
								//log.info("***2***");
								mandatoryCheck.setTaskStatus("Failed");
								mandatoryFields = mandatoryFields +" column name";

							}
							if(mandatoryFields != null && !mandatoryFields.equals(""))
							{
								//log.info("***3***");
								mandatoryCheck.setDetails(mandatoryFields +"---"+new Exception());
								templateLinesReports.add(mandatoryCheck);
								templateLinesListReports.setSubTasksList(templateLinesReports);
								//throw new Exception();
							}
							else
							{
								//log.info("***4***");

							}

							ErrorReport dupTempLineReport = new ErrorReport();
							dupTempLineReport.setTaskName("Check for duplicate line");
							//log.info("***5***");
							if(fileTempLinesDTO.getColumnHeader() != null && !fileTempLinesDTO.getColumnHeader().isEmpty())
							{
								FileTemplateLines dupFTL = new FileTemplateLines();
								if(fileTempDTO.getMultipleRowIdentifiers() == true)
								{
									//check FTL with column header, identifier,templateId
									//log.info("***7***");
									if(fileTempLinesDTO.getInterRI() != null && interRecordToIdMap.containsKey(fileTempLinesDTO.getInterRI()) && 
											interRecordToIdMap.get(fileTempLinesDTO.getInterRI()) != null){
										dupFTL =  fileTemplateLinesRepository.findByTemplateIdAndIntermediateIdAndColumnHeaderAndRecordIdentifier(identifierRecord.getTemplateId(),	Long.valueOf(interRecordToIdMap.get(fileTempLinesDTO.getInterRI()))
												,fileTempLinesDTO.getColumnHeader(),fileTempLinesDTO.getRecordStartRow());
										dupTempLineReport.setTaskStatus("Line already exists with combination "+filetempRecord.getTemplateName()+" - "+ fileTempLinesDTO.getRecordStartRow()+" - "+fileTempLinesDTO.getColumnHeader());
									}
									
								}
								else
								{
									//check FTL with column header, templateId
									//log.info("***8***");
									dupFTL = fileTemplateLinesRepository.findByTemplateIdAndColumnHeader(templateId, fileTempLinesDTO.getColumnHeader());
									dupTempLineReport.setTaskStatus("Line already exists with combination "+filetempRecord.getTemplateName()+" - "+fileTempLinesDTO.getColumnHeader());
								}

								if(dupFTL != null)
								{
									//log.info("***9***");
									templateLinesReports.add(dupTempLineReport);
								}
								else
								{
									//log.info("***10***");
									if(fileTempLinesDTO.getId()!=null)
									{
										fileTempLines.setId(fileTempLinesDTO.getId());
									}
									if(filetempRecord.getId()!=null)
										fileTempLines.setTemplateId(filetempRecord.getId());
									//link intermediate id
									if(fileTempDTO.getMultipleRowIdentifiers() == true)
									{
										log.info("yes its true");
										String MI = "";
										//log.info("fileTempLinesDTO.getMI()"+fileTempLinesDTO.getInterRI());
										MI = fileTempLinesDTO.getInterRI();
										//log.info("MI"+MI);

										if(interRecordToIdMap != null && interRecordToIdMap.containsKey(MI) && interRecordToIdMap.get(MI)!= null)
										{
											fileTempLines.setIntermediateId(interRecordToIdMap.get(MI));
										}
										else
										{
											log.info("interRecordToIdMap.get(MI) isnull");
										}
									}
									else
									{
										//log.info("***11***");
									}
									//log.info("fileTempLinesDTO.getLineNumber(): "+fileTempLinesDTO.getLineNumber());
									if(fileTempLinesDTO.getRecordStartRow()!=null)
										fileTempLines.setRecordStartRow(fileTempLinesDTO.getRecordStartRow());

									if(fileTempLinesDTO.getLineNumber()!=null)
										fileTempLines.setLineNumber(fileTempLinesDTO.getLineNumber());
									if(fileTempLinesDTO.getMasterTableReferenceColumn()!=null)
										fileTempLines.setMasterTableReferenceColumn(fileTempLinesDTO.getMasterTableReferenceColumn());
									if(fileTempLinesDTO.getRecordTYpe()!=null&&!fileTempLinesDTO.getRecordTYpe().isEmpty())
									{
										//log.info("fileTempLinesDTO printing2:"+fileTempLinesDTO.getRecordTYpe());
										fileTempLines.setRecordTYpe(fileTempLinesDTO.getRecordTYpe());
									}else
										fileTempLines.setRecordTYpe("Row Data");
									//log.info("fileTempLinesDTO printing3:"+fileTempLines.getRecordTYpe());

									log.info("fileTempLinesDTO.getRecordIdentifier(): "+fileTempLinesDTO.getRecordIdentifier()+" fileTempLinesDTO.getRecordStartRow(): "+fileTempLinesDTO.getRecordStartRow());

									if(fileTempLinesDTO.getRecordIdentifier()!=null)
									{
										fileTempLines.setRecordIdentifier(fileTempLinesDTO.getRecordIdentifier());
									}
									else{
										fileTempLines.setRecordIdentifier(fileTempLinesDTO.getRecordStartRow());}

									if(fileTempLinesDTO.getColumnNumber()!=null)
										fileTempLines.setColumnNumber(fileTempLinesDTO.getColumnNumber());

									if(fileTempLinesDTO.getRecordStartRow()!=null)
										//log.info("fileTempLinesDTO.getRecordStartRow(): "+fileTempLinesDTO.getRecordStartRow());
										//fileTempLines.setRecordStartRow(fileTempLinesDTO.getRecordStartRow());
										if(fileTempLinesDTO.getEnclosedChar()!=null&&!fileTempLinesDTO.getEnclosedChar().isEmpty())
											fileTempLines.setEnclosedChar(fileTempLinesDTO.getEnclosedChar());
									if(fileTempLinesDTO.getEnclosedChar()!=null&&!fileTempLinesDTO.getEnclosedChar().isEmpty())
										fileTempLines.setEnclosedChar(fileTempLinesDTO.getEnclosedChar());
									if(fileTempLinesDTO.getPositionBegin()!=null)
										fileTempLines.setPositionBegin(fileTempLinesDTO.getPositionBegin());
									if(fileTempLinesDTO.getPositionEnd()!=null)
										fileTempLines.setPositionEnd(fileTempLinesDTO.getPositionEnd());
									if(fileTempLinesDTO.getColumnHeader()!=null&&!fileTempLinesDTO.getColumnHeader().isEmpty())
										fileTempLines.setColumnHeader(fileTempLinesDTO.getColumnHeader());
									if(fileTempLinesDTO.getColumnHeader()!=null&&!fileTempLinesDTO.getColumnHeader().isEmpty())
										fileTempLines.setConstantValue(fileTempLinesDTO.getConstantValue());
									if(fileTempLinesDTO.getZeroFill()!=null&&!fileTempLinesDTO.getZeroFill().isEmpty())
										fileTempLines.setZeroFill(fileTempLinesDTO.getZeroFill());
									if(fileTempLinesDTO.getAlign()!=null&&!fileTempLinesDTO.getAlign().isEmpty())
										fileTempLines.setAlign(fileTempLinesDTO.getAlign());
									if(fileTempLinesDTO.getFormula()!=null&&!fileTempLinesDTO.getFormula().isEmpty())
										fileTempLines.setFormula(fileTempLinesDTO.getFormula());
									if(fileTempLinesDTO.getDateFormat()!=null&&!fileTempLinesDTO.getDateFormat().isEmpty())
										fileTempLines.setDateFormat(fileTempLinesDTO.getDateFormat());
									if(fileTempLinesDTO.getTimeFormat()!=null&&!fileTempLinesDTO.getTimeFormat().isEmpty())
										fileTempLines.setTimeFormat(fileTempLinesDTO.getTimeFormat());
									if(fileTempLinesDTO.getAmountFormat()!=null&&!fileTempLinesDTO.getAmountFormat().isEmpty())
										fileTempLines.setAmountFormat(fileTempLinesDTO.getAmountFormat());
									if(fileTempLinesDTO.getAmountFormat()!=null&&!fileTempLinesDTO.getAmountFormat().isEmpty())
										fileTempLines.setOverFlow(fileTempLinesDTO.getOverFlow());
									if(fileTempLinesDTO.getSkipColumn()!=null&&!fileTempLinesDTO.getSkipColumn().isEmpty())
										fileTempLines.setSkipColumn(fileTempLinesDTO.getSkipColumn());
									if(fileTempLinesDTO.getColumnDelimeter()!=null&&!fileTempLinesDTO.getColumnDelimeter().isEmpty())
										fileTempLines.setColumnDelimiter(fileTempLinesDTO.getColumnDelimeter());
									if(fileTempLinesDTO.getCreatedBy()!=null)
										fileTempLines.setCreatedBy(fileTempLinesDTO.getCreatedBy());

									fileTempLines.setCreatedDate(ZonedDateTime.now());
									if(fileTempLinesDTO.getLastUpdatedBy()!=null)
										fileTempLines.setLastUpdatedBy(fileTempLinesDTO.getCreatedBy());
									fileTempLines.setLastUpdatedDate(ZonedDateTime.now());



									//	if(fileTempLinesDTO.getColumnHeader().)
									// Swetha
									/* Logic to process column header and save it in column alias */
									//String s=fileTempLinesDTO.getColumnHeader();
									//String s="% Rate -???^&df_#S";
									String s=fileTempLinesDTO.getColumnHeader();
									Pattern pattern = Pattern.compile("[^a-z A-Z 0-9]");
									Matcher matcher = pattern.matcher(s);
									String number = matcher.replaceAll("");
									//log.info("number: "+number);

									String finalCol="";
									String b[]=number.split("\\s+");
									for(int l=0;l<b.length;l++){
										//log.info("b[l] at l: "+l+" is: "+b[l]);
										if(b[l]!=null && !(b[l].isEmpty())){
											String val1=Character.toString(b[l].charAt(0));
											String val2=Character.toString(Character.toTitleCase(b[l].charAt(0)));
											if(b[l].contains(val1)){
												//log.info("contains");
												b[l]=b[l].replace(val1, val2);
											}
										}
										finalCol=finalCol.concat(b[l]);
									}

									//log.info("finalCol: "+finalCol);
									fileTempLines.setColumnAlias(finalCol+"_"+filetempRecord.getId());


									FileTemplateLines fileTempLine = new FileTemplateLines();

									//fileTempLine = fileTemplateLinesRepository.save(fileTempLines);
									/*try
									{
										entityManager.persist(fileTempLine);
										if(entityManager.contains(fileTempLine))
										{
											entityManager.getTransaction().commit();
											entityManager.close();
										}
										else
										{
											entityManager.getTransaction().rollback();
										}
									}
									catch(Exception e)
									{
										entityManager.getTransaction().rollback();
									}*/

									//log.info("fileTempLines: " +fileTempLines.toString());
									FileTemplateLines fileDatefileTempLines=new FileTemplateLines();
									if(fileTempDTO.getMultipleRowIdentifiers() == true)
									{
										log.info("yes its true");
										String MI = "";
										//log.info("fileTempLinesDTO.getMI()"+fileTempLinesDTO.getInterRI());
										MI = fileTempLinesDTO.getInterRI();
										//log.info("MI"+MI);

										if(interRecordToIdMap != null && interRecordToIdMap.containsKey(MI) && interRecordToIdMap.get(MI)!= null)
										{
											fileTempLines.setIntermediateId(interRecordToIdMap.get(MI));
										}
										else
										{
											log.info("interRecordToIdMap.get(MI) isnull");
										}
										//check if already exists along with intermediate id.. loop thru all inter id s and save
										if(i == 0){
											log.info("multi -1=>");
											//fileDatefileTempLines = fileTempLines;
											BeanUtils.copyProperties(fileDatefileTempLines, fileTempLines);
											//fetch existing transaction date record
											FileTemplateLines transactionDateRec = new FileTemplateLines();
											transactionDateRec = fileTemplateLinesRepository.findByTemplateIdAndIntermediateIdAndColumnHeader(filetempRecord.getId(),interRecordToIdMap.get(MI),"file_date");
											//log.info("transactionDateRec: "+transactionDateRec);
											//log.info("multi -2=>");
											if(transactionDateRec != null){
												//log.info("multi -3=>");
												fileDatefileTempLines.setId(transactionDateRec.getId());
											}
											else
											{
												fileDatefileTempLines.setId(null);
												//log.info("multi -4=>");
											}
											if(fileDatefileTempLines.getColumnHeader() != null){
												fileDatefileTempLines.setColumnHeader("file_date");
												fileDatefileTempLines.setColumnAlias("file_date");
												fileDatefileTempLines.setMasterTableReferenceColumn("file_date");
												fileDatefileTempLines.setDateFormat("yyyy-MM-dd");
												fileDatefileTempLines.setColumnNumber(null);
												fileDatefileTempLines.setLineNumber(null);
												fileDatefileTempLines.setRecordTYpe(null);
												//log.info("multi -5=>");
												fileDatefileTempLines = fileTemplateLinesRepository.save(fileDatefileTempLines);
											}
										}
									}
									else
									{
										log.info("single -1=>");
										//save the transaction date here
										if(i == 0){
											//log.info("single -2=>");
											//fileDatefileTempLines = (FileTemplateLines) SerializationUtils.clone(fileTempLines);
											BeanUtils.copyProperties(fileDatefileTempLines, fileTempLines);
											//log.info("fileTempLines at copy: "+fileTempLines);
											//log.info("fileDatefileTempLines copied as : "+fileDatefileTempLines);
											//fetch existing transaction date record
											FileTemplateLines transactionDateRec = new FileTemplateLines();
											transactionDateRec = fileTemplateLinesRepository.findByTemplateIdAndColumnHeader(filetempRecord.getId(),"file_date");
											//log.info("transactionDateRec: "+transactionDateRec);
											//log.info("single -3=>");
											if(transactionDateRec != null){
												//log.info("single -4=>");
												fileDatefileTempLines.setId(transactionDateRec.getId());
											}
											else
											{
												fileDatefileTempLines.setId(null);
												//log.info("single -5=>");
											}
											//log.info("fileDatefileTempLines.getColumnHeader()"+fileDatefileTempLines.getColumnHeader());
											if(fileDatefileTempLines.getColumnHeader() != null){
												fileDatefileTempLines.setColumnHeader("file_date");
												fileDatefileTempLines.setColumnAlias("file_date");
												fileDatefileTempLines.setMasterTableReferenceColumn("file_date");
												fileDatefileTempLines.setDateFormat("yyyy-MM-dd");
												fileDatefileTempLines.setColumnNumber(null);
												fileDatefileTempLines.setRecordTYpe(null);
												fileDatefileTempLines.setLineNumber(null);
												//log.info("single -6=>");
												fileDatefileTempLines = fileTemplateLinesRepository.save(fileDatefileTempLines);
											}
											log.info("fileDatefileTempLines after saving: "+fileDatefileTempLines);
										}
									}

									fileTempLine = fileTemplateLinesRepository.save(fileTempLines);
									//log.info("fileTempLine" +fileTempLine.toString());
									//									if(fileTempLine.getColumnHeader() != null && fileTempLine.getColumnHeader().toLowerCase().equalsIgnoreCase("ledger name"))
									//									{
									//										throw new NullPointerException();
									//									}

									headerToTempLineIdMap.put(fileTempLine.getColumnHeader(), fileTempLine.getId());
									fileTempLinesList.add(fileTempLine);
									//log.info("fileTempLinesDTO.line: :"+fileTempLinesDTO.getLineNumber());
									if(fileTempLine!=null&&fileTempLine.getId()!=null)
									{
										fileTempLinesErrReport.setTaskName("File Template Lines Save");
										fileTempLinesErrReport.setTaskStatus("Success");
									}
									else
									{
										fileTempLinesErrReport.setTaskName("File Template Lines Save");
										fileTempLinesErrReport.setTaskStatus("Failure");
										str=str+fileTempLinesDTO.getLineNumber()+",";
										//fileTempLinesErrReport.setDetails("Failed to save at line number :"+str);
									}
									templateLinesReports.add(fileTempLinesErrReport);
								}
							}
							else
							{
								//log.info("***6***");
							}
						}
						//removing "," for last string
						if (str.endsWith(",")) {
							str = str.substring(0, str.length() - 1);
						}
						if(fileTempLinesErrReport.getTaskStatus()!=null&&fileTempLinesErrReport.getTaskStatus().equalsIgnoreCase("failure"))
						{
						}
						if(fileTempLinesErrReport.getTaskStatus()!=null && fileTempLinesErrReport.getTaskStatus().equalsIgnoreCase("Success"))
						{
							ErrorReport fileTempLinesErrorReport=new ErrorReport();
							fileTempLinesErrorReport.setTaskName("File Template Lines Save");
							if(FileTemplateLinesList.size()==fileTempLinesList.size())
							{
								fileTempLinesErrorReport.setTaskStatus("Success");
							}
							else
							{
								fileTempLinesErrorReport.setTaskStatus("Failure");
							}
						}
					}
					templateLinesListReports.setSubTasksList(templateLinesReports);
				}

				errorReports.add(templateLinesListReports);
				//headerToTempLineIdMap
				//save excel conditions if exists
				if(filetempRecord != null && filetempRecord.getFileType() != null && filetempRecord.getFileType().toLowerCase().contains("excel"))
				{
					ErrorReport saveEndRowConditions = new ErrorReport();
					saveEndRowConditions.setTaskName("Save end row conditions");
					if(fileTemplatesPostingDTO.getEndRowConditionsList() != null)
					{
						List<RowConditionsDTO> endRowConditions = fileTemplatesPostingDTO.getEndRowConditionsList();
						List<ErrorReport> endConditionsReportList = new ArrayList<ErrorReport>();
						log.info("endRowConditions.size() "+endRowConditions.size());
						for(int endRowCondInd=0;endRowCondInd<endRowConditions.size();endRowCondInd++)
						{
							if(endRowConditions.get(endRowCondInd).getType().toLowerCase().equalsIgnoreCase("end"))
							{
								ErrorReport endRowCondReport = new ErrorReport();
								endRowCondReport.setTaskName("Save Condition "+(endRowCondInd+1));
								RowConditionsDTO endConditionDto = new RowConditionsDTO();
								endConditionDto = endRowConditions.get(endRowCondInd);
								RowConditions endCondition = new RowConditions();
								Long tempLineId = null;
								if(endConditionDto.getColumnName() != null && headerToTempLineIdMap.get(endConditionDto.getColumnName()) != null)
									tempLineId = headerToTempLineIdMap.get(endConditionDto.getColumnName());
								if(tempLineId != null)
									endCondition.setTemplateLineId(tempLineId);
								endCondition.setType(endConditionDto.getType());
								endCondition.setOperator(endConditionDto.getOperator());
								endCondition.setValue(endConditionDto.getValue());
								endCondition.setLogicalOperator(endConditionDto.getLogicalOperator());
								RowConditions rowCond = rowConditionsRepository.save(endCondition);
								if(rowCond != null)
								{

								}
								else
								{
									endRowCondReport.setTaskStatus("Failed to save");
									endConditionsReportList.add(endRowCondReport);
								}
							}

						}
						errorReports.addAll(endConditionsReportList);
						saveEndRowConditions.setSubTasksList(endConditionsReportList);
					}

					ErrorReport saveSkipRowConditions = new ErrorReport();
					saveSkipRowConditions.setTaskName("Save skip row conditions");
					if(fileTemplatesPostingDTO.getSkipRowConditionsList() != null)
					{

						List<ErrorReport> skipConditionsReportList = new ArrayList<ErrorReport>();
						List<RowConditionsDTO> skipRowConditions = fileTemplatesPostingDTO.getSkipRowConditionsList();
						log.info("skipRowConditions.size() "+skipRowConditions.size());
						for(int endRowCondInd=0;endRowCondInd<skipRowConditions.size();endRowCondInd++)
						{
							if(skipRowConditions.get(endRowCondInd).getType().toLowerCase().equalsIgnoreCase("skip"))
							{
								ErrorReport skipRowCondReport = new ErrorReport();
								RowConditionsDTO skipConditionDto = new RowConditionsDTO();
								skipConditionDto = skipRowConditions.get(endRowCondInd);
								RowConditions skipCondition = new RowConditions();
								Long tempLineId = null;
								if(skipConditionDto.getColumnName() != null && headerToTempLineIdMap.get(skipConditionDto.getColumnName()) != null)
									log.info("skipConditionDto.getColumnName()"+skipConditionDto.getColumnName());
								tempLineId = headerToTempLineIdMap.get(skipConditionDto.getColumnName());
								if(tempLineId != null)
								{
									skipCondition.setTemplateLineId(tempLineId);
									skipCondition.setType(skipConditionDto.getType());
									skipCondition.setOperator(skipConditionDto.getOperator());
									skipCondition.setValue(skipConditionDto.getValue());
									skipCondition.setLogicalOperator(skipConditionDto.getLogicalOperator());
									RowConditions rowCond = rowConditionsRepository.save(skipCondition);
									if(rowCond != null)
									{

									}
									else
									{
										skipRowCondReport.setTaskStatus("Failed to save");
										skipConditionsReportList.add(skipRowCondReport);
									}
								}

							}
						}
						saveSkipRowConditions.setSubTasksList(skipConditionsReportList);
						errorReports.add(saveSkipRowConditions);
					}

				}
				SourceProfileFileAssignments SPA=new SourceProfileFileAssignments();
				if(fileTemplatesPostingDTO.getSourceProfileFileAssignmentDTO()!=null && fileTemplatesPostingDTO.getSourceProfileFileAssignmentDTO().getSourceProfileId() != null)
				{
					TenantConfig  tenantConfig=tenantConfigRepository.findByTenantIdAndKey(tenantId, "LocalDirPath");
					log.info("tenantConfig: "+tenantConfig.toString());
					SourceProfileFileAssignmentDTO SPADTO=fileTemplatesPostingDTO.getSourceProfileFileAssignmentDTO();
					log.info("SPADTO: temp id:"+SPADTO.getTemplateId());
					if(SPADTO.getId()!=null)
					{
						SPA.setId(SPADTO.getId());
					}
					if(SPADTO.getSourceProfileId()!=null)
						SPA.setSourceProfileId(SPADTO.getSourceProfileId());
					if(SPADTO.getFileNameFormat()!=null&&!SPADTO.getFileNameFormat().isEmpty())
						SPA.setFileNameFormat(SPADTO.getFileNameFormat());
					if(SPADTO.getFileDescription()!=null&&!SPADTO.getFileDescription().isEmpty())
						SPA.setFileDescription(SPADTO.getFileDescription());
					if(SPADTO.getFileExtension()!=null&&!SPADTO.getFileExtension().isEmpty())
						SPA.setFileExtension(SPADTO.getFileExtension());
					if(SPADTO.getFrequencyType()!=null&&!SPADTO.getFrequencyType().isEmpty())
						SPA.setFrequencyType(SPADTO.getFrequencyType());
					if(SPADTO.getDueTime()!=null&&!SPADTO.getDueTime().isEmpty())
						SPA.setDueTime(SPADTO.getDueTime());
					if(SPADTO.getDay()!=null)
						SPA.setDay(SPADTO.getDay());
					if(SPADTO.getSourceDirectoryPath()!=null&&!SPADTO.getSourceDirectoryPath().isEmpty())
						SPA.setSourceDirectoryPath(SPADTO.getSourceDirectoryPath());
					if(SPADTO.getLocalDirectoryPath()!=null&&!SPADTO.getLocalDirectoryPath().isEmpty())
						SPA.setLocalDirectoryPath(SPADTO.getLocalDirectoryPath());
					if(filetempRecord.getId()!=null)
						SPA.setTemplateId(filetempRecord.getId());
					SPA.setEnabledFlag(true);
					if(SPADTO.getCreatedBy()!=null)
						SPA.setCreatedBy(SPADTO.getCreatedBy());
					SPA.setCreatedDate(ZonedDateTime.now());
					if(SPADTO.getLastUpdatedBy()!=null)
						SPA.setLastUpdatedBy(SPADTO.getLastUpdatedBy());
					SPA.setLastUpdatedDate(ZonedDateTime.now());

					if(tenantConfig!=null)
						SPA.setLocalDirectoryPath(tenantConfig.getValue());
					SourceProfileFileAssignments sourceProfileAssgn=sourceProfileFileAssignmentsRepository.save(SPA);

					ErrorReport fileTempLinesErrorReport=new ErrorReport();
					fileTempLinesErrorReport.setTaskName("Source Profile Assignment Save");
					if(sourceProfileAssgn.getId()!=null)
					{
						fileTempLinesErrorReport.setTaskStatus("Success");
					}
					else
					{
						fileTempLinesErrorReport.setTaskStatus("Failure");

					}
				}
			}
			else
			{
				log.info("No lines exists");
			}

		}	
		return errorReports;
	}
}
