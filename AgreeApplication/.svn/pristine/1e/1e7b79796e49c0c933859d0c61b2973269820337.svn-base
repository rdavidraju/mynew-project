package com.nspl.app.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ExcelFunctionsService {
	
	@PersistenceContext(unitName="default")
	private EntityManager em;
	
	private final Logger log = LoggerFactory.getLogger(ExcelFunctionsService.class);

	public List<String> vlookup(String searchColumn,String tableName, String indexColumn,List<String> columnData) 
	{
		log.info("To Fetch the VlookUp data");
		log.info("Column Name to search the data:"+searchColumn);
		List<String> result1=new ArrayList<String>();
		for(int k=0;k<columnData.size();k++)
		{
			String keyword=columnData.get(k);
			String buildString = indexColumn+" = '"+keyword+"'";
			log.info("select tr."+searchColumn+" from SampleTable tr where "+buildString);
			//tableName="SampleTable";
			TypedQuery<String> query = em.createQuery("select tr."+searchColumn+" from "+tableName+" tr where "+buildString, String.class);
			List<String> result = query.getResultList();
			String res=keyword+"-"+result;
			result1.add(res);
			log.info("Return:====>>"+res);
		}
		return result1;
	}
}
