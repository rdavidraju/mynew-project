package com.nspl.app.service;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.nspl.app.domain.DataViewsColumns;
import com.nspl.app.domain.FormConfig;
import com.nspl.app.repository.DataViewsColumnsRepository;
import com.nspl.app.repository.FormConfigRepository;
import com.nspl.app.web.rest.dto.RWQFormConfigDTO;

@Service
public class FormConfigService {
	
    private final Logger log = LoggerFactory.getLogger(FormConfigService.class);
    
    @Inject
    DataViewsColumnsRepository dataViewsColumnsRepository;
    
    @Inject
    FormConfigRepository formConfigRepository;

	
	public JSONArray getFormConfigValue(Long sViewId, Long tViewId, JSONArray jsonValue)
	{
		JSONArray finalValue = new JSONArray();
    	DataViewsColumns sAmountQualifier = dataViewsColumnsRepository.findByDataViewIdAndQualifier(sViewId, "AMOUNT");
    	DataViewsColumns tAmountQualifier = dataViewsColumnsRepository.findByDataViewIdAndQualifier(tViewId, "AMOUNT");
    	
    	DataViewsColumns sCurrencyQualifier = dataViewsColumnsRepository.findByDataViewIdAndQualifier(sViewId, "CURRENCYCODE");
    	DataViewsColumns tCurrencyQualifier = dataViewsColumnsRepository.findByDataViewIdAndQualifier(tViewId, "CURRENCYCODE");
    	
    	DataViewsColumns sDateQualifier = dataViewsColumnsRepository.findByDataViewIdAndQualifier(sViewId, "TRANSDATE");
    	DataViewsColumns tDateQualifier = dataViewsColumnsRepository.findByDataViewIdAndQualifier(tViewId, "TRANSDATE");
    	
    	DataViewsColumns sKCQualifier = dataViewsColumnsRepository.findByDataViewIdAndQualifier(sViewId, "GROUPBY_COLUMN");
    	DataViewsColumns tKCQualifier = dataViewsColumnsRepository.findByDataViewIdAndQualifier(tViewId, "GROUPBY_COLUMN");
		
		for(int i=0; i<jsonValue.size(); i++)
		{
			JSONObject obj = (JSONObject) jsonValue.get(i);
			log.info("Paramter Name: "+obj.get("parameter").toString());
			if("AMOUNT".equalsIgnoreCase(obj.get("parameter").toString()))
			{
				if(sAmountQualifier != null && tAmountQualifier != null)
				{
					obj.put("sColId", sAmountQualifier.getId());
					obj.put("tColId", tAmountQualifier.getId());
					obj.put("sColName", sAmountQualifier.getColumnName());
					obj.put("tColName", tAmountQualifier.getColumnName());
				}
				else
				{
					obj.put("sColId", "");
					obj.put("tColId", "");
					obj.put("sColName", "");
					obj.put("tColName", "");
				}
			}
			else if("TRANSDATE".equalsIgnoreCase(obj.get("parameter").toString()))
			{
				if(sDateQualifier != null && tDateQualifier != null)
				{
					obj.put("sColId", sDateQualifier.getId());
					obj.put("tColId", tDateQualifier.getId());
					obj.put("sColName", sDateQualifier.getColumnName());
					obj.put("tColName", tDateQualifier.getColumnName());
				}
				else
				{
					obj.put("sColId", "");
					obj.put("tColId", "");
					obj.put("sColName", "");
					obj.put("tColName", "");
				}
			}
			else if("CURRENCYCODE".equalsIgnoreCase(obj.get("parameter").toString()))
			{
				if(sCurrencyQualifier != null && tCurrencyQualifier != null)
				{
					obj.put("sColId", sCurrencyQualifier.getId());
					obj.put("tColId", tCurrencyQualifier.getId());
					obj.put("sColName", sCurrencyQualifier.getColumnName());
					obj.put("tColName", tCurrencyQualifier.getColumnName());
				}
				else
				{
					obj.put("sColId", "");
					obj.put("tColId", "");
					obj.put("sColName", "");
					obj.put("tColName", "");
				}
			}
			else if("KEY COMPONENT".equalsIgnoreCase(obj.get("parameter").toString()))
			{
				if(sKCQualifier != null && tKCQualifier != null)
				{
					obj.put("sColId", sKCQualifier.getId());
					obj.put("tColId", tKCQualifier.getId());
					obj.put("sColName", sKCQualifier.getColumnName());
					obj.put("tColName", tKCQualifier.getColumnName());
				}
				else
				{
					obj.put("sColId", "");
					obj.put("tColId", "");
					obj.put("sColName", "");
					obj.put("tColName", "");

				}
			}
			finalValue.add(obj);
		}
		return finalValue;
	}
	
	public FormConfig postDefaultFormConf(Long tenantId, Long userId, Long sViewId) throws JsonGenerationException, JsonMappingException, IOException
	{
		String finalString = "";
		ObjectMapper mapperObj = new ObjectMapper();
		// Posting default form config params
		List<RWQFormConfigDTO> values = new ArrayList<RWQFormConfigDTO>();
		RWQFormConfigDTO amountDTO = new RWQFormConfigDTO();
		amountDTO.setParameter("AMOUNT");
		amountDTO.setOperator("<=");
		amountDTO.setValue("");
		values.add(amountDTO);
		
		RWQFormConfigDTO dateDTO = new RWQFormConfigDTO();
		dateDTO.setParameter("TRANSDATE");
		dateDTO.setOperator("BETWEEN");
		dateDTO.setValue("-5,+10");
		values.add(dateDTO);
		
		RWQFormConfigDTO currencyDTO = new RWQFormConfigDTO();
		currencyDTO.setParameter("CURRENCYCODE");
		currencyDTO.setOperator("=");
		currencyDTO.setValue("");
		values.add(currencyDTO);
		
		DataViewsColumns sKCQualifier = dataViewsColumnsRepository.findByDataViewIdAndQualifier(sViewId, "GROUPBY_COLUMN");
		if(sKCQualifier != null)
		{
			RWQFormConfigDTO kcDTO = new RWQFormConfigDTO();
			kcDTO.setParameter("KEY COMPONENT");
			kcDTO.setOperator("=");
			kcDTO.setValue("");
			values.add(kcDTO);
		}
		
		FormConfig newFC = new FormConfig();
		newFC.setTenantId(tenantId);
		newFC.setFormConfig("reconcilewq");
		newFC.setFormLevel("customfilters");
		newFC.setCreatedBy(userId);
		newFC.setCreatedDate(ZonedDateTime.now());
		newFC.setLastUpdatedBy(userId);
		newFC.setLastUpdatedDate(ZonedDateTime.now());
		for(RWQFormConfigDTO value : values)
		{
            String jsonStr = mapperObj.writeValueAsString(value);
            finalString = finalString + jsonStr+", ";
		}
		finalString = finalString.substring(0, finalString.length()-2);
		finalString = "["+finalString+"]";
		newFC.setValue(finalString);
		
		FormConfig saveFormConfig = formConfigRepository.save(newFC);
		return saveFormConfig;
	}
}
