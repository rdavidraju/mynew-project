package com.nspl.app.service;

import io.github.jhipster.config.JHipsterProperties;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.nspl.app.config.ApplicationContextProvider;
import com.nspl.app.domain.TenantConfig;
import com.nspl.app.repository.TenantConfigRepository;
import com.nspl.app.web.rest.ApprovalGroupMembersResource;

@Service
public class UserJdbcService {
	  private final Logger log = LoggerFactory.getLogger(UserJdbcService.class);
	
	@Inject
	private Environment env;
	
	@Inject
    JHipsterProperties jHipsterProperties;
	
	@Inject
	TenantConfigRepository tenantConfigRepository;
	
	public HashMap jdbcConnc(Long userId,Long tenantId) throws SQLException, ClassNotFoundException {
		
		//log.info("********user jdbc******* with userId :"+userId);
		
	/*	List<TenantConfig> tntConfig=tenantConfigRepository.findByTenantIdAndMeaning(tenantId, "GatewayProperties");
		log.info("tntConfig :"+tntConfig.size());
		LinkedHashMap lhm=new LinkedHashMap();
		for(TenantConfig tntCfg:tntConfig)
		{
			lhm.put(tntCfg.getKey(), tntCfg.getValue());
			
		}*/
		/*String schemaName= env.getProperty("spring.datasource.gatewayschema").toString();
		String schemaName= env.getProperty("spring.datasource.gatewayschema").toString();
		String userName =env.getProperty("spring.datasource.gatewayUserName").toString();
		String password =env.getProperty("spring.datasource.gatewayPassword").toString();
		String jdbcDriver = env.getProperty("spring.datasource.jdbcdriver").toString();
		String dbUrl=env.getProperty("spring.datasource.gatewayUrl").toString();*/
		String schemaName= env.getProperty("spring.secondDatasource.databaseName").toString();
		HashMap map=new HashMap();
		Connection conn = null;
		Statement stmt = null;
		ResultSet result2=null;
		try{ 
		//	Class.forName(jdbcDriver);
		//	conn = DriverManager.getConnection(dbUrl, userName, password);
			DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("secondaryDataSource");
			
			
			conn = ds.getConnection();
			conn.setSchema(schemaName);
	
		//	log.info("Connected database successfully...");
		//	log.info("schemaName :"+schemaName);
			stmt = conn.createStatement();
		//	result2=stmt.executeQuery("select * from jhi_user where id="+userId);
			result2=stmt.executeQuery("select * from "+schemaName+".jhi_user where id="+userId);

			String loginName="";
			String assigneeName="";

			while(result2.next())
			{
				if(result2.getString("login")!=null)
					loginName=result2.getString("login");
				if(result2.getString("first_name")!=null)
					assigneeName=assigneeName+result2.getString("first_name");
				if(result2.getString("last_name")!=null)
					assigneeName=assigneeName+" "+result2.getString("last_name");
				if(result2.getString("business_title")!=null)
					map.put("businessTitle", result2.getString("business_title"));
				if(result2.getString("image")!=null)
					map.put("imgUrl", result2.getString("image"));

			}
			map.put("loginName", loginName);
			map.put("assigneeName", assigneeName);
			
		}
		catch(SQLException se){

		} 
		finally{
			if(conn!=null)
				conn.close();
			if(stmt!=null)
				stmt.close();
			if(result2!=null)
				result2.close();

		}
		return map;
	}
	
	/**
	 * Author: Swetha
	 * Function to retrieve UserId and TenantId from jwt token
	 * @param request
	 * @return
	 */
	public HashMap getuserInfoFromToken(HttpServletRequest request){
		
		HashMap userInfo=new HashMap();
		
		Map<String, String> reqHdrInfo = new HashMap<String, String>();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            reqHdrInfo.put(key, value);
        }
        
        //log.info("reqHdrInfo: "+reqHdrInfo);
        
        String authVal="";
        if(reqHdrInfo.get("authorization")!=null)
        authVal=reqHdrInfo.get("authorization");
        else
        	 authVal=reqHdrInfo.get("Authorization");
        //log.info("authVal: "+authVal);
        
        String authToken=authVal.replaceFirst("Bearer ", "");
        //log.info("authToken: "+authToken);
       
        String secretKey=jHipsterProperties.getSecurity().getAuthentication().getJwt().getSecret();
      //  Header headerMap=Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken).getHeader();
        //System.out.println("headerMap from validateToken: "+headerMap.toString());
        String tenantId=Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken).getHeader().get("TenantId").toString();
        String userId=Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken).getHeader().get("userId").toString();
        //System.out.print("tenantId from validateToken: "+tenantId);
        
      //  log.info("tenantId********:"+tenantId);
        
        userInfo.put("tenantId", tenantId);
        userInfo.put("userId", userId);
		
		return userInfo;
		
		
	}
	
	/*public HashMap getUserInfo(Long userId,Long tenantId) throws SQLException, ClassNotFoundException {
		
	
		String schemaName= env.getProperty("spring.datasource.gatewayschema").toString();
		String userName =env.getProperty("spring.datasource.gatewayUserName").toString();
		String password =env.getProperty("spring.datasource.gatewayPassword").toString();
		String jdbcDriver = env.getProperty("spring.datasource.jdbcdriver").toString();
		String dbUrl=env.getProperty("spring.datasource.gatewayUrl").toString();
		HashMap map=new HashMap();
		Connection conn = null;
		Statement stmt = null;
		ResultSet result2=null;
		try{ 
			Class.forName(jdbcDriver);
			conn = DriverManager.getConnection(dbUrl, userName, password);
			log.info("Connected database successfully...");
			stmt = conn.createStatement();
			result2=stmt.executeQuery("select * from "+schemaName+".jhi_user where id="+userId);

			String loginName="";
			String assigneeName="";

			while(result2.next())
			{
				if(result2.getString("login")!=null)
					loginName=result2.getString("login");
				if(result2.getString("first_name")!=null)
					assigneeName=assigneeName+result2.getString("first_name");
				if(result2.getString("last_name")!=null)
					assigneeName=assigneeName+" "+result2.getString("last_name");
				if(result2.getString("business_title")!=null)
					map.put("businessTitle", result2.getString("business_title"));
				if(result2.getString("image")!=null)
					map.put("imgUrl", result2.getString("image"));

			}
			map.put("loginName", loginName);
			map.put("assigneeName", assigneeName);
		}
		catch(SQLException se){

		} 
		finally{
			if(conn!=null)
				conn.close();
			if(stmt!=null)
				stmt.close();
			if(result2!=null)
				result2.close();

		}
		return map;
	}
	*/
	/**
	 * Author: Swetha
	 * @param email
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public HashMap getUserByEmail(String email,Long tenantId) throws SQLException, ClassNotFoundException {
		
	/*	List<TenantConfig> tntConfig=tenantConfigRepository.findByTenantIdAndMeaning(tenantId, "GatewayProperties");
		log.info("tntConfig :"+tntConfig.size());
		LinkedHashMap lhm=new LinkedHashMap();
		for(TenantConfig tntCfg:tntConfig)
		{
			lhm.put(tntCfg.getKey(), tntCfg.getValue());
			
		}*/
		/*String schemaName= env.getProperty("spring.datasource.gatewayschema").toString();
		String userName =env.getProperty("spring.datasource.gatewayUserName").toString();
		String password =env.getProperty("spring.datasource.gatewayPassword").toString();
		String jdbcDriver = env.getProperty("spring.datasource.jdbcdriver").toString();
		String dbUrl=env.getProperty("spring.datasource.gatewayUrl").toString();*/
		String schemaName= env.getProperty("spring.secondDatasource.databaseName").toString();
		HashMap map=new HashMap();
		Connection conn = null;
		Statement stmt = null;
		ResultSet result2=null;
		try{ 
			DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("secondaryDataSource");
		    conn = ds.getConnection();
			log.info("Connected database successfully...");
			stmt = conn.createStatement();
			result2=stmt.executeQuery("select * from "+schemaName+".jhi_user where email='"+email+"'");

			String loginName="";
			String assigneeName="";

			while(result2.next())
			{
				if(result2.getString("login")!=null)
					loginName=result2.getString("login");
				if(result2.getString("first_name")!=null)
					assigneeName=assigneeName+result2.getString("first_name");
				if(result2.getString("last_name")!=null)
					assigneeName=assigneeName+" "+result2.getString("last_name");
				if(result2.getString("business_title")!=null)
					map.put("businessTitle", result2.getString("business_title"));
				if(result2.getString("image")!=null)
					map.put("imgUrl", result2.getString("image"));

			}
			map.put("loginName", loginName);
			map.put("assigneeName", assigneeName);
		}
		catch(SQLException se){

		} 
		finally{
			if(conn!=null)
				conn.close();
			if(stmt!=null)
				stmt.close();
			if(result2!=null)
				result2.close();

		}
		return map;
	}
	
	
	
	
	
	
	public HashMap getTenantIdForDisplay(String tenantId) throws SQLException, ClassNotFoundException {
	/*	List<TenantConfig> tntConfig=tenantConfigRepository.findByTenantIdAndMeaning(0l, "GatewayProperties");
		log.info("tntConfig :"+tntConfig.size());
		LinkedHashMap lhm=new LinkedHashMap();
		for(TenantConfig tntCfg:tntConfig)
		{
			lhm.put(tntCfg.getKey(), tntCfg.getValue());
			
		}*/
		/*String schemaName= env.getProperty("spring.datasource.gatewayschema").toString();
		String userName =env.getProperty("spring.datasource.gatewayUserName").toString();
		String password =env.getProperty("spring.datasource.gatewayPassword").toString();
		String jdbcDriver = env.getProperty("spring.datasource.jdbcdriver").toString();
		String dbUrl=env.getProperty("spring.datasource.gatewayUrl").toString();*/
		String schemaName= env.getProperty("spring.secondDatasource.databaseName").toString();
		
		HashMap map=new HashMap();
		Connection conn = null;
		Statement stmt = null;
		ResultSet result2=null;
		try{ 
			DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("secondaryDataSource");
			conn = ds.getConnection();
			log.info("Connected database successfully...");
			stmt = conn.createStatement();
			result2=stmt.executeQuery("select id from "+schemaName+".tenant_details where id_for_display='"+tenantId+"'");
	
			while(result2.next())
			{
				if(result2.getString(1)!=null)
					map.put("tenantId", result2.getString(1));

			}
			
			
		}
		catch(SQLException se){
			log.info("SQLException : "+se);
		} 
		finally{
			if(conn!=null)
				conn.close();
			if(stmt!=null)
				stmt.close();
			if(result2!=null)
				result2.close();

		}
		return map;
	}
	
	
	
	public HashMap getTenantIdForDisplayByTenantId(Long tenantId) throws SQLException, ClassNotFoundException {

		String schemaName= env.getProperty("spring.secondDatasource.databaseName").toString();
		
		HashMap map=new HashMap();
		Connection conn = null;
		Statement stmt = null;
		ResultSet result2=null;
		try{ 
			DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("secondaryDataSource");
			conn = ds.getConnection();
			log.info("Connected database successfully...");
			stmt = conn.createStatement();
			result2=stmt.executeQuery("select id_for_display from "+schemaName+".tenant_details where id="+tenantId);
	
			while(result2.next())
			{
				if(result2.getString(1)!=null)
					map.put("tenantIdForDisplay", result2.getString(1));

			}
			
			
		}
		catch(SQLException se){
			log.info("SQLException : "+se);
		} 
		finally{
			if(conn!=null)
				conn.close();
			if(stmt!=null)
				stmt.close();
			if(result2!=null)
				result2.close();

		}
		return map;
	}
	
	
	
	
	

}
