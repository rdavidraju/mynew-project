package com.nspl.app.service;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;

import com.nspl.app.domain.Notifications;
import com.nspl.app.repository.NotificationsRepository;


@Service
public class NotificationService {
	 private final Logger log = LoggerFactory.getLogger(DataViewsService.class);
	 
	 @Inject
	 NotificationsRepository notificationsRepository;
	
	 
	 @SubscribeMapping("/topic/notification")
	    @SendTo("/topic/notificationTracker")
	 public HashMap getNotificationsDataByUserId(HashMap userDetaisls,StompHeaderAccessor stompHeaderAccessor){
	    	log.info("Rest api for getting notifications by user id tracker:");
	    	HashMap finalMap = new HashMap();
	    	Long tenantId=0l;
	    	Long userId=0l;
	    	List<Notifications> notifications = notificationsRepository.findByTenantIdAndUserIdOrderByIdDesc(tenantId, userId);
	    	log.info("No of records fetched: "+ notifications.size());
	    	List<HashMap> data = new ArrayList<HashMap>();
	    	if(notifications.size()>0)
	    	{
	    		for(Notifications not : notifications)
	    		{
	    			HashMap hm = new HashMap();
	    			hm.put("id", not.getId());
	    			hm.put("message", not.getMessage());
	    			hm.put("module", not.getModule());
	    			hm.put("actionType", not.getActionType());
	    			hm.put("actionValue", not.getActionValue());
	    			hm.put("creationDate", not.getCreationDate());
	    			hm.put("isViewed", not.isIsViewed());
	    			//Getting time difference in Seconds, Minuites, Hours
	    			String time = "";
	    			ZonedDateTime start = not.getCreationDate();
	    			ZonedDateTime stop = ZonedDateTime.now();
	    			Duration dur = Duration.between(start, stop);
	    			long hours = dur.toHours();
	    			long minutes = dur.toMinutes();
	    			long days = dur.toDays();
	    			long seconds = dur.getSeconds();
	    			if(days>0)
	    			{
	    				time = time + days+" days ago";
	    			}
	    			else if(hours>0)
	    			{
	    				time = time +hours + " hrs ago";
	    			}
	    			else if(minutes>0)
	    			{
	    				time = time+minutes + " mins ago";
	    			}
	    			else if(seconds>0)
	    			{
	    				time = time + seconds + " secs ago";
	    			}
	    			hm.put("time", time);		
	    			data.add(hm);
	    		}
	    	}
	    	finalMap.put("data", data);
	    	finalMap.put("count", notifications.size());
	    	List<Notifications> unRead = notificationsRepository.findByTenantIdAndUserIdAndIsViewed(tenantId, userId, false);
	    	log.info("Un Read: "+ unRead.size());
	    	finalMap.put("unRead", unRead.size());
	    	return finalMap;
	    }

}
