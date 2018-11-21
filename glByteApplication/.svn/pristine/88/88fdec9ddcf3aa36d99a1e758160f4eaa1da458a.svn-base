package com.nspl.app.service;


import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.nspl.app.web.rest.ChartOfAccountResource;


@Service
public class ActiveStatusService {

	private final Logger log = LoggerFactory.getLogger(ChartOfAccountResource.class);

	public Boolean activeStatus(ZonedDateTime startDate,ZonedDateTime endDate,Boolean enableFlag)
	{
		ZonedDateTime now = ZonedDateTime.now();
		//log.info("now :"+now);
		
		ZonedDateTime endDt=now.plusDays(1);
		if(endDate!=null)
			endDt=endDate;
		
		if(enableFlag!=null)
			enableFlag=enableFlag;
		else
			enableFlag=false;

		if((startDate.equals(now) || startDate.isBefore(now)) && (endDt.equals(now) || endDt.isAfter(now)) && enableFlag)
			return true;
		else
			return false;
	}

}
