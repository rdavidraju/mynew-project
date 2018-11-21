package com.nspl.app.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nspl.app.config.ApplicationContextProvider;
import com.nspl.app.domain.DataViews;
import com.nspl.app.repository.DataViewsRepository;

@Service
@Transactional
public class DataChildService 
{	
	private final Logger log = LoggerFactory.getLogger(ReconciliationResultService.class);
	
	@Inject
	DataViewsRepository dataViewsRepository;
	
	@Inject
	ReconciliationResultService reconciliationResultService;
	
	public HashMap insertSplitAddRowsIntoViewTable(String adjustmentType, Long rowId, Long viewId, Long tenantId)
	{
		HashMap finalMap = new HashMap();
		Connection conn = null;
		Statement stmt = null;
		ResultSet result = null;
		
		PreparedStatement insertStmt = null;
		// ResultSet insertRS = null;
		
		PreparedStatement deleteStmt = null;
		// ResultSet deleteRS = null;
		
		try
		{
			DataViews dv = dataViewsRepository.findOne(viewId);
			DataSource ds = (DataSource) ApplicationContextProvider.getApplicationContext().getBean("dataSource");
			conn = ds.getConnection();
			stmt = conn.createStatement();
			String query = "select vid from v_data_child where data_view_id = "+viewId+" and master_row_id = "+rowId+"";
			result = stmt.executeQuery(query);
			List<Long> rowIds = new ArrayList<Long>();
			while(result.next()) 
			{
				rowIds.add(Long.parseLong(result.getString("vid")));
			}
			if(rowIds.size()>0)
			{
				// Inserting splitting rows / adding rows into the view table
				String rowIdsString = reconciliationResultService.getStringWithNumbers(rowIds);
				String insertQuery = "INSERT INTO `"+dv.getDataViewName().toLowerCase()+"` SELECT * FROM `"+dv.getDataViewName().toLowerCase()+"_v` where scrIds in("+rowIdsString+")";
				insertStmt = conn.prepareStatement(insertQuery);
				int inserted = insertStmt.executeUpdate();
				log.info("Inserted Records Size: "+ inserted);
				if("Split".equalsIgnoreCase(adjustmentType))
				{
					String deleteQuery = "DELETE FROM `"+dv.getDataViewName().toLowerCase()+"` where scrIds = "+ rowId;
					deleteStmt = conn.prepareStatement(deleteQuery);
					int updated = deleteStmt.executeUpdate();
					log.info("Deleted Records: "+ updated);
				}
			} 
		}
		catch(Exception e)
		{
			log.info("Exception while inserting split/adding rows in data view table: "+e);
		}
		finally
		{
			try {
				if(result != null)
					result.close();
				if (stmt != null)
					stmt.close();
				if(insertStmt != null)
					insertStmt.close();
				if(deleteStmt != null)
					deleteStmt.close();
				if (conn != null)
					conn.close();
			}catch (Exception e1)
			{
				log.info("Exception while closing jdbc statements."+ e1);
			}
		}
		return finalMap;
	}
}
