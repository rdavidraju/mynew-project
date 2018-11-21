package com.nspl.app.jbpm.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.kie.api.task.UserGroupCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomUserGroupCallback implements UserGroupCallback {

	private final Logger log = LoggerFactory
			.getLogger(CustomUserGroupCallback.class);

	public boolean existsGroup(String groupId) {
		// TODO Auto-generated method stub
		log.info("In existsGroup "+groupId);
		if (groupId.equals("Administrators")) {
			return true;
		} else {
			return true;
		}
		}
	
	public boolean existsUser(String userId) {
		log.info("User: " + userId);
		log.debug("User: " + userId);
		// return
		// personDetailService.existsPerson(Integer.parseInt(userId))||userId.equals("Administrator");

		if (userId.equals("Administrator")) {
			return true;
		}
		else
		return true;

	}

	public List<String> getGroupsForUser(String userId, List<String> groupIds,
			List<String> allExistingGroupIds) {
		log.info("user group callback. getting groups for user:"+userId);
		List list = new ArrayList();
		ArrayList<String> listS = new ArrayList<String>();
		//User currUser=userRepository.findByLoginAndActivatedIsTrue(userId);
		//User currUser=userRepository.findOne(Long.valueOf(userId));
		//list = hierarchyDetailsRepository.findDistinctHierarchyIdByObjectId(currUser.getId());
		Iterator iter = list.iterator();
		while(iter.hasNext())
		{
			listS.add(iter.next().toString());
		}
		log.info("user group callback. list "+list.size());
		if (list.isEmpty()){
			return null;			
		}else{
			log.info("user group callback. list "+list.get(0));
			return null;
		}
	}

	
}