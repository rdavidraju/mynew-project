package com.nspl.app.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.nspl.app.domain.NotificationBatch;
import com.nspl.app.repository.NotificationBatchRepository;

@Service
public class NotificationBatchService {
	
	private final Logger log = LoggerFactory.getLogger(NotificationBatchService.class);
	
	@Inject
	NotificationBatchRepository notificationBatchRepository;

	/**
	 * Author: Swetha
	 * Function to get count of Notifications tagged to a User / User and his subordinates
	 * @param tenantId
	 * @param status
	 * @param resourceList
	 * @return
	 */
	public int getApprovalNotificationCount(Long tenantId, String status, List<String> moduleList, List<Long> resourceList){
		
		List<NotificationBatch> notBatchList=new ArrayList<NotificationBatch>();
		
		if(status!=null)
	    	notBatchList = notificationBatchRepository.fetchByTenantIdAndStatusAndModuleAndCurrentApproverInOrderByIdDesc(tenantId,status,resourceList,moduleList);
	    	else
	    		notBatchList = notificationBatchRepository.fetchByTenantIdAndModuleAndCurrentApproverInOrderByIdDesc(tenantId,resourceList,moduleList);
		int sz=notBatchList.size();
	    	log.info("notificationCount.size :"+sz);
			return sz;
	    	
	}
}
