package com.nspl.app.jbpm.service;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.Task;
import org.kie.api.task.model.TaskSummary;
import org.kie.internal.task.api.InternalTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;


@Service
public class ApprovalTaskService {
	
	private final Logger log = LoggerFactory.getLogger(ApprovalTaskService.class);
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	RuntimeManager manager;
	
	/**
	 * Author: Swetha
	 * Function to retrieve taskId based on ProcessInstanceId and UserId
	 * @param processId
	 * @param userId
	 * @return
	 * @throws ParseException
	 */
	public Long getTasksByProcessId(@RequestParam Long processId,@RequestParam Long userId)
			throws ParseException {
		log.debug("REST request to getTasksByProcessId: " + processId+" and userId: "+userId);
		RuntimeEngine engine = manager.getRuntimeEngine(null);
		KieSession ksession = engine.getKieSession();
		TaskService taskService = engine.getTaskService();
		List<Task> taskList = new ArrayList<Task>();
		try{
			
		List<Long> taskIdList = taskService.getTasksByProcessInstanceId(processId);
		int TasksSize = taskIdList.size();
		log.info("TasksSize: " + TasksSize);
		Long taskId=0L;
		int i = 0;
		while (TasksSize != 0) {
			taskId = taskIdList.get(i);
			log.info("taskId: " + taskId);
			Task task = taskService.getTaskById(taskId);
			if (task != null) {
				Map<String, Object> content = taskService
						.getTaskContent(taskId);
				if (task.getTaskData().getActualOwner() != null) {
					if (task.getTaskData().getActualOwner().getId() != null){
						if(task.getTaskData().getActualOwner().getId().equalsIgnoreCase(String.valueOf(userId))){
							
							if (task.getTaskData().getStatus() != null){
								if(task.getTaskData().getStatus().toString().equalsIgnoreCase("Reserved")){
							return taskId;
								}
							}
						}
				}
				}
					i++;
					TasksSize--;
				} else
					log.info("No Task found for TaskId: " + taskId);
			}
		}catch(Exception e){
			log.info("error retrirbing task: "+e);
		}
		if (engine != null) {
			manager.disposeRuntimeEngine(engine);
		}
		return null;
	}
	
}
