package com.nspl.app.jbpm.util;

import java.util.Iterator;

import org.kie.api.task.model.Group;
import org.kie.api.task.model.OrganizationalEntity;
import org.kie.internal.task.api.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomUserInfo implements UserInfo {
	
	private final Logger log = LoggerFactory
			.getLogger(CustomUserInfo.class);

	@Override
	public String getDisplayName(OrganizationalEntity arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEmailForEntity(OrganizationalEntity arg0) {
		// TODO Auto-generated method stub
		log.debug("OrganizationalEntity"+arg0.getId());
		return "srinivas.veedepu@gmail.com";
	}

	@Override
	public String getLanguageForEntity(OrganizationalEntity arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<OrganizationalEntity> getMembersForGroup(Group arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasEmail(Group arg0) {
		// TODO Auto-generated method stub
		return false;
	}

}
