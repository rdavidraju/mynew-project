package com.nspl.app.service;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import com.nspl.app.domain.DataViews;
import com.nspl.app.domain.DataViewsColumns;
import com.nspl.app.domain.LookUpCode;
import com.nspl.app.domain.RuleConditions;
import com.nspl.app.domain.RuleGroup;
import com.nspl.app.domain.RuleGroupDetails;
import com.nspl.app.domain.Rules;
import com.nspl.app.repository.DataViewsColumnsRepository;
import com.nspl.app.repository.DataViewsRepository;
import com.nspl.app.repository.LookUpCodeRepository;
import com.nspl.app.repository.RuleConditionsRepository;
import com.nspl.app.repository.RuleGroupDetailsRepository;
import com.nspl.app.repository.RulesRepository;
import com.nspl.app.web.rest.AppRuleCondDto;
import com.nspl.app.web.rest.dto.ErrorReport;
import com.nspl.app.web.rest.dto.RuleConditionsDTO;
import com.nspl.app.web.rest.dto.RulesDTO;

@Service
public class RuleService {

	private final Logger log = LoggerFactory.getLogger(RuleService.class);

	@Inject
	RuleConditionsRepository ruleConditionsRepository;

	@Inject
	RuleGroupDetailsRepository ruleGroupDetailsRepository;

	@Inject
	RulesRepository rulesRepository;

	@Inject
	DataViewsColumnsRepository dataViewsColumnsRepository;

	@Inject
	DataViewsRepository dataViewsRepository;

	@Inject 
	RuleGroupService ruleGroupService;
	
	@Inject
	LookUpCodeRepository lookUpCodeRepository;

	@Transactional(propagation= Propagation.REQUIRED)
	public void saveRule()
	{
		Rules rules = new Rules();
		rules.setRuleCode("test xxx");
		rules.setRuleType("ssdd");
		//try {
		//rulesRepository.save(rules);
		throw new RuntimeException("Rollback this transaction!");
		//				} catch (Exception e) {
		//					// TODO Auto-generated catch block
		//					e.printStackTrace();
		//				}
	}
	@SuppressWarnings("finally")
	@Transactional(propagation= Propagation.REQUIRED)
	public List<ErrorReport> postRules(List<RulesDTO> rules,RuleGroup newGrp,String bulkUpload) throws Exception
	{
		List<ErrorReport> subtasksListForGroupSave = new ArrayList<ErrorReport>();
		log.info("newGrp.getId() has value...."+newGrp.getId());
		List<BigInteger> ruleIdsList=ruleGroupDetailsRepository.fetchRuleIdsByGroupAndTenantId(newGrp.getId(),newGrp.getTenantId());
		log.info("ruleIdsList before:"+ruleIdsList);
		String errorMessage = "";
		List<RulesDTO> ruleDTO = new ArrayList<RulesDTO>();
		//log.info("ruleGroupDTO.getRules()"+ruleGroupDTO.getRules().size());
		ruleDTO = rules;
		for(int i=0;i<ruleDTO.size();i++)
		{
			Long ruleId = 0L;
			Rules newRule=new Rules();
			Rules rule=new Rules();
			try
			{
				errorMessage ="Rule save failed because rule code of rule-"+(i+1)+" is null";
				log.info("ruleDTO.get(i).getRule().getRuleCode()"+ruleDTO.get(i).getRule().getRuleCode());
				String ruleCode = ruleDTO.get(i).getRule().getRuleCode();
				if(ruleCode == null || ruleCode.equals("") || ruleCode.isEmpty())
				{
					throw new RuntimeException((i+1)+"rule`s Rule code is null");
				}
				else
				{
					if(ruleDTO.get(i).getRule().getId() != null)
					{
						//update assignment status 
						RuleGroupDetails ruleGroupDetails = new RuleGroupDetails();
						ruleGroupDetails = ruleGroupDetailsRepository.findByRuleGroupIdAndRuleId(newGrp.getId(), ruleDTO.get(i).getRule().getId());
						if(ruleGroupDetails != null)
						{
							log.info("update assignment status of "+ruleDTO.get(i).getRule().getId());
							if(ruleDTO.get(i).getRule().getPriority()>0)
							{
								ruleGroupDetails.setPriority(ruleDTO.get(i).getRule().getPriority());
							}
							else
							{
								int lastPriority = ruleGroupDetailsRepository.getLatestPriority(newGrp.getId());
								ruleGroupDetails.setPriority(lastPriority+1);
							}
							ruleGroupDetails.setPriority(ruleDTO.get(i).getRule().getPriority());
							ruleGroupDetails.setEnabledFlag(ruleDTO.get(i).getRule().getAssignmentFlag());
							ruleGroupDetails.setSuggestionRule(ruleDTO.get(i).getRule().getSuggestionFlag());
							ruleGroupDetailsRepository.save(ruleGroupDetails);
						}

						rule.setId(ruleDTO.get(i).getRule().getId());


						if(ruleIdsList.size()>0)
						{
							for(int j=0;j<ruleDTO.size();j++)
							{
								if(ruleDTO.get(i).getRule().getId() != null && ruleDTO.get(i).getRule().getId()!=0)
								{
									for(int id=0;id<ruleIdsList.size();id++)
									{
										log.info("ruleIdsList.get(id) :"+ruleIdsList.get(id));
										log.info("ruleDTO.get(j).getRule().getId() :"+ruleDTO.get(j).getRule().getId());
										if(ruleIdsList.get(id).longValue()==ruleDTO.get(j).getRule().getId())
										{
											log.info("same");
											ruleIdsList.remove(ruleIdsList.get(id));
										}
									}
								}
							}
						}
						log.info("ruleIdsList after:"+ruleIdsList);

						
					}
					//else
					//{

					//}
					for(int j=0;j<ruleIdsList.size();j++)
					{
						log.info("ruleIdsList.get(j).longValue() :"+ruleIdsList.get(j).longValue());
						log.info("newGrp.getId() :"+newGrp);
						RuleGroupDetails ruleGrpId=ruleGroupDetailsRepository.findByRuleGroupIdAndRuleId(newGrp.getId(),ruleIdsList.get(j).longValue());
						log.info("ruleGrpId :"+ruleGrpId);
						log.info("");
						if(ruleGrpId!=null)
							ruleGroupDetailsRepository.delete(ruleGrpId);
					}
					log.info("ruleDTO.get(i).getRule() :"+ruleDTO.get(i).getRule());
					log.info("rule id is null please save");
					//				if(ruleDTO.get(i).getRule().getId() == null || ruleDTO.get(i).getRule().getId()==0)
					//				{
					//					rule.setId(ruleDTO.get(i).getId());
					if(ruleDTO.get(i).getRule().getRuleCode()!=null && !ruleDTO.get(i).getRule().getRuleCode().isEmpty())
						rule.setRuleCode(ruleDTO.get(i).getRule().getRuleCode());
					//if(ruleDTO.get(i).getRule().getRuleName()!=null && !ruleDTO.get(i).getRule().getRuleName().isEmpty())
					//	rule.setRuleName(ruleDTO.get(i).getRule().getRuleName());
					if(bulkUpload != null && bulkUpload.equalsIgnoreCase("true"))
					{
						rule.setStartDate(ZonedDateTime.now());
						rule.setEnabledFlag(true);
					}
					else
					{
						if(ruleDTO.get(i).getRule().getStartDate()!=null)
							rule.setStartDate(ruleDTO.get(i).getRule().getStartDate());
						if(ruleDTO.get(i).getRule().getEndDate()!=null)
							rule.setEndDate(ruleDTO.get(i).getRule().getEndDate());
						rule.setEnabledFlag(ruleDTO.get(i).getRule().getEnabledFlag());
					}
					try{
						errorMessage ="Rule type for rule-"+(i+1) +" because rule type is null";
						String ruleType = ruleDTO.get(i).getRule().getRuleType();
						if(ruleType==null || ruleType.equals("") || ruleType == "")
						{
							throw new Exception(errorMessage);
						}
						else
						{
							if(bulkUpload != null && bulkUpload.equalsIgnoreCase("true"))
							{
								LookUpCode ruleTypeCode = new LookUpCode();
								log.info("check for existance");
								errorMessage ="Rule type for rule-"+(i+1) +" while validating the existance rule type for the tenant";
								ruleTypeCode = lookUpCodeRepository.findByMeaningAndLookUpTypeAndTenantId(ruleType, "RULE_TYPE", newGrp.getTenantId());
								log.info(ruleType+"not found"+ruleTypeCode.getLookUpCode());
									rule.setRuleType(ruleTypeCode.getLookUpCode());
							}
							else
							rule.setRuleType(ruleDTO.get(i).getRule().getRuleType());
						}
						
						if(ruleDTO.get(i).getRule().getCategory()!=null && !ruleDTO.get(i).getRule().getCategory().isEmpty())
							rule.setCategory(ruleDTO.get(i).getRule().getCategory());

						//if(ruleDTO.get(i).getRule().getRulePurpose()!=null && !ruleDTO.get(i).getRule().getRulePurpose().isEmpty())
						//	rule.setRulePurpose(ruleDTO.get(i).getRule().getRulePurpose());
						
						
						//try{
						errorMessage="Rule save failed because Source data view of rule-"+(i+1)+" is empty/doesnot exist";
						//log.info("ruleDTO.get(i).getRule().getSourceDataViewId()"+ruleDTO.get(i).getRule().getSourceDataViewId());
						HashMap<String,DataViewsColumns> sourceColumnsMap = new HashMap<String,DataViewsColumns>();
						
						if(bulkUpload != null && bulkUpload.equalsIgnoreCase("true"))
						{
							log.info("bulk upload to set source data view");
							errorMessage ="Rule save failed because Source data view of rule-"+(i+1)+" is empty/doesnot exist";
							String sourceDataViewName = ruleDTO.get(i).getRule().getsDataViewDisplayName();
							if(sourceDataViewName == null || sourceDataViewName.equals("") || sourceDataViewName == "")
							{
								throw new Exception(errorMessage);
							}
							else
							{
								List<DataViews> sdv = new ArrayList<DataViews>();
								sdv = dataViewsRepository.findByTenantIdAndDataViewDispName(newGrp.getTenantId(), sourceDataViewName);
								if(sdv.size()>0)
								{
									if(sdv.size() == 1)
									{
										errorMessage ="Rule -"+(i+1)+" save failed while accessing source data view";
										if(sdv.get(0) != null && sdv.get(0).getId() != null)
										rule.setSourceDataViewId(sdv.get(0).getId());
										
										List<DataViewsColumns> columns = new ArrayList<DataViewsColumns>();
										columns = dataViewsColumnsRepository.findByDataViewId(sdv.get(0).getId());
										for(DataViewsColumns dvCol : columns)
										{
											sourceColumnsMap.put( dvCol.getColumnName(),dvCol);
											
										}
										log.info("sourceColumnsMap size:"+sourceColumnsMap.size());
										log.info("sourceColumnsMap"+sourceColumnsMap.toString());
									}
									else
									{
										errorMessage = "Rule-"+(i+1)+" saving failed because, mutiple data views exists with source side data view - "+sourceDataViewName;
										throw new Exception(errorMessage);
									}	
								}
								else
								{
									errorMessage ="Rule save failed because Source data view of rule-"+(i+1)+" is doesnot exist for this tenant";
									throw new Exception(errorMessage);
								}
								
							}
						}
						else
						{
							log.info("form save to set source data view");
							String srcDataViewId = null;
							errorMessage ="Rule save failed because Source data view of rule-"+(i+1)+" is empty/doesnot exist";
							srcDataViewId = ruleDTO.get(i).getRule().getSourceDataViewId();
							DataViews srcDV=dataViewsRepository.findByTenantIdAndIdForDisplay(newGrp.getTenantId(), srcDataViewId);
							log.info("srcDV has"+srcDV.getId());
							rule.setSourceDataViewId(srcDV.getId());
						}
						//try{
						HashMap<String,DataViewsColumns> targetColumnsMap = new HashMap<String,DataViewsColumns>();
						if(bulkUpload != null && bulkUpload.equalsIgnoreCase("true"))
						{
							log.info("bulk upload to set target data view");
							errorMessage ="Rule save failed because Target data view of rule-"+(i+1)+" is empty/doesnot exist";
							String targetDataViewName = ruleDTO.get(i).getRule().gettDataViewDisplayName();
							List<DataViews> tdv = new ArrayList<DataViews>();
							tdv = dataViewsRepository.findByTenantIdAndDataViewDispName(newGrp.getTenantId(), targetDataViewName);
							
							if(tdv.size()>0)
							{
								log.info("targets are"+tdv.size());
								if(tdv.size() == 1)
								{
									errorMessage ="Rule -"+(i+1)+" save failed while accessing target data view";
									if(tdv.get(0) != null && tdv.get(0).getId() != null)
									rule.setTargetDataViewId(tdv.get(0).getId());
									
									List<DataViewsColumns> columns = new ArrayList<DataViewsColumns>();
									columns = dataViewsColumnsRepository.findByDataViewId(tdv.get(0).getId());
									log.info("tdv.get(0).getId()"+tdv.get(0).getId());
									for(DataViewsColumns dvCol : columns)
									{
										targetColumnsMap.put(dvCol.getColumnName(),dvCol);
									}
								}
								else
								{
									log.info("multiple target views found");
									throw new Exception("Rule-"+(i+1)+" saving failed because, mutiple data views exists with target side data view - "+targetDataViewName);
								}
							}
							else
							{
								errorMessage ="Rule save failed because target data view of rule-"+(i+1)+" is doesnot exist for this tenant";
								throw new Exception(errorMessage);
							}
						}
						else
						{
							log.info("form save to set target data view");
							String trgtDataViewId = null;
							errorMessage ="Rule save failed because Target data view of rule-"+(i+1)+" is empty/doesnot exist";
							trgtDataViewId =ruleDTO.get(i).getRule().getTargetDataViewId();
							DataViews trgtDV=dataViewsRepository.findByTenantIdAndIdForDisplay(newGrp.getTenantId(), trgtDataViewId);
							rule.setTargetDataViewId(trgtDV.getId());
						}
						if(ruleDTO.get(i).getRule().getId() == null || ruleDTO.get(i).getRule().getId() == 0 || ruleDTO.get(i).getRule().getId().equals(""))
						{
							rule.setCreationDate(ZonedDateTime.now());
						}
						else
							rule.setCreationDate(ruleDTO.get(i).getRule().getCreationDate());
						rule.setCreatedBy(newGrp.getCreatedBy());
						rule.setLastUpdatedBy(newGrp.getLastUpdatedBy());
						rule.setLastUpdatedDate(ZonedDateTime.now());
						rule.setTenantId(newGrp.getTenantId());
						ErrorReport rulesave =new ErrorReport();
						//try{
						errorMessage="Rule save failed";
						newRule=rulesRepository.save(rule);

						List<ErrorReport> subTasksForConditions = new ArrayList<ErrorReport>();
						if(newRule.getId() != null)
						{
							rulesave.setTaskName("Save Rule "+ rule.getRuleCode());
							rulesave.setTaskStatus("Success");
							rulesave.setDetails(newRule.getId()+"");
							if(ruleDTO.get(i).getRule().getId()==null || ruleDTO.get(i).getRule().getId()==0)
								subtasksListForGroupSave.add(rulesave);
						}
						if( newRule.getId()!=null)
						{
							ruleId = newRule.getId();
						}
						if(ruleId !=null && ruleId !=0 )
						{
							log.info("newGrp.getId() :"+newGrp.getId());
							log.info("ruleId :"+ruleId);
							RuleGroupDetails ruleGrpDetails=new RuleGroupDetails();
							RuleGroupDetails ruleGrpDet=ruleGroupDetailsRepository.findByRuleGroupIdAndRuleId(newGrp.getId(),ruleId);
							if(ruleGrpDet!=null)
							{
								errorMessage="Failed while saving priority of rule-"+(i+1);
								ruleGrpDet.setPriority(ruleDTO.get(i).getRule().getPriority());
								errorMessage="Failed while saving assignment flag of rule-"+(i+1);
								ruleGrpDet.setEnabledFlag(ruleDTO.get(i).getRule().getAssignmentFlag());
								errorMessage="Rule-"+(i+1)+ " to recon process tagging updation failed";
								ruleGrpDetails= ruleGroupDetailsRepository.save(ruleGrpDet);
							}

							else
							{
								RuleGroupDetails rulegroupDet=new RuleGroupDetails();
								rulegroupDet.setRuleGroupId(newGrp.getId());
								rulegroupDet.setRuleId(ruleId);
								if(bulkUpload!= null && bulkUpload.equalsIgnoreCase("true")){
									rulegroupDet.setPriority(i+1);
									rulegroupDet.setEnabledFlag(true);
								}
								else
								{
									rulegroupDet.setEnabledFlag(ruleDTO.get(i).getRule().getAssignmentFlag());
									rulegroupDet.setPriority(ruleDTO.get(i).getRule().getPriority());
								}
								rulegroupDet.setCreatedBy(newGrp.getCreatedBy());
								rulegroupDet.setLastUpdatedBy(newGrp.getLastUpdatedBy());
								rulegroupDet.setCreationDate(ZonedDateTime.now());
								rulegroupDet.setLastUpdatedDate(ZonedDateTime.now());
								rulegroupDet.setTenantId(newGrp.getTenantId());
								log.info("ruleDTO.get(i).getRule().getAssignmentFlag()"+ruleDTO.get(i).getRule().getAssignmentFlag());
							
								rulegroupDet.setSuggestionRule(ruleDTO.get(i).getRule().getSuggestionFlag());
								errorMessage = "Rule-"+(i+1)+ " to recon process assignment failed";
								ruleGrpDetails= ruleGroupDetailsRepository.save(rulegroupDet);
							}
							//
							errorMessage="Rule "+(i+1)+" saving failed as conditions doesnot exist";
							if(ruleDTO.get(i).getRuleConditions() != null )
							{
								log.info("rc-step-1");
								List<RuleConditionsDTO> ruleCondList=ruleDTO.get(i).getRuleConditions();
								log.info("rc-step-2");
								int j=0;
								String amountColConditions = "";
								if(ruleDTO.get(i).getRule().getId() != null )
								{
									//delete existing conditions
									List<RuleConditions> existingRuleConditionsList = new ArrayList<RuleConditions>();
									existingRuleConditionsList = ruleConditionsRepository.findByRuleId(ruleDTO.get(i).getRule().getId());
									List<Long> alreadyExistingRCIds = new ArrayList<Long>();
									List<Long> currentInputRCIds = new ArrayList<Long>();
									for(RuleConditionsDTO ruleCond : ruleCondList)
									{
										if(ruleCond.getId() != null)
										currentInputRCIds.add(ruleCond.getId());
									}
									for(RuleConditions ruleCond : existingRuleConditionsList)
									{
										alreadyExistingRCIds.add(ruleCond.getId());
									}
									for(Long id : alreadyExistingRCIds)
									{
										errorMessage ="Rule"+(i+1)+ " failed to save, when clearing the deleted rule conditions";
										if(currentInputRCIds.contains(id))
										{
											
										}
										else
										{
											try{
												ruleConditionsRepository.delete(id);
											}
											catch(Exception e)
											{
												throw new Exception(errorMessage);
											}
											
										}
									}
								}
								//Build expression and balance parenthesis
								//buildWhereClause();
								for(RuleConditionsDTO ruleCond : ruleCondList)
								{
									log.info("rc-step-3");
									boolean isSDecimal =false;
									boolean isTDecimal =false;
									log.info("rc-step-4");
									if(ruleCond.getDataType() != null && ruleCond.getDataType().toLowerCase().contains("decimal"))
									{
										log.info("rc-step-5");
										isSDecimal = true;
										amountColConditions = amountColConditions + (j+1)+",";
										log.info("rc-step-6");
									}
									if(ruleCond.gettDataType() != null && ruleCond.gettDataType().toLowerCase().contains("decimal"))
									{
										log.info("rc-step-7");
										isTDecimal = true;
									}
									log.info("rc-step-8");
									if((isSDecimal || isTDecimal) && (j==ruleCondList.size()-1))
									{
										log.info("rc-step-9");
										if(ruleCond.issReconAmountMatch() && ruleCond.istReconAmountMatch() )
										{
											log.info("rc-step-10");
										}
										else 
										{
											log.info("rc-step-11");
											errorMessage ="Rule"+(i+1)+ " failed to save, as recon amount match is missing for amount columns in condition "+amountColConditions;
											throw new Exception(errorMessage);
											
										}
										log.info("rc-step-12");
									}
									boolean hasSColumn =  false;
									boolean hasOperator =  false;
									boolean hasTColumn =  false;
									log.info("rule-"+(i+1)+" rule condition-"+(j+1));
									errorMessage = "Failed while saving rule condition";
									RuleConditions ruleCondition = new RuleConditions();
									//ruleCondition.setAggregationMethod(ruleCond.getAggregationMethod());
									//}
									if(ruleCond.getId() != null)
										ruleCondition.setId(ruleCond.getId());

									if(ruleCond.getOpenBracket()!=null && !ruleCond.getOpenBracket().isEmpty())
										ruleCondition.setOpenBracket(ruleCond.getOpenBracket());

									//check if column belongs to source data view
									if(bulkUpload != null && bulkUpload.equalsIgnoreCase("true"))
									{
										errorMessage="Rule"+(i+1)+ " failed to save because, source column is null in rule condition "+(j+1);
										String sourceColumn = ruleCond.getsColumnName();
										log.info("sourceColumn"+sourceColumn);
										if(sourceColumn == null)
											throw new Exception(errorMessage);
										errorMessage="Rule"+(i+1)+ " failed to save because, given source column in rule condition "+(j+1)+" is not tagged to "+ruleDTO.get(i).getRule().getsDataViewDisplayName();
										Long sourceColumnId = sourceColumnsMap.get(sourceColumn).getId();
										if(sourceColumnId == null)
											throw new Exception(errorMessage);
										log.info(" sourceColumnsMap.get(sourceColumn)"+ sourceColumnsMap.get(sourceColumn));
										log.info("sourceColumnId"+sourceColumnId);
										ruleCondition.setsColumnId(sourceColumnId);
										hasSColumn = true;
									}
									else
									{
										errorMessage = "Rule"+(i+1)+ " failed to save because, given source column in rule condition "+(j+1)+" is null";
										if(ruleCond.getsColumnId() == null)
										{
											log.info("source column is null");
										}
										else
										{
											ruleCondition.setsColumnId(ruleCond.getsColumnId());
											hasSColumn = true;
										}
											
											
									}
									
									
									if(ruleId!=null)
									{
										log.info("newRule.getRuleType() :"+newRule.getRuleType());
										if(newRule.getRuleType() != null)
										{
											
											if(newRule.getRuleType().equalsIgnoreCase("ONE_TO_MANY"))
											{
												log.info("one2one :");
												if(ruleCond.gettColumnId()!=null)
												{

													DataViewsColumns dvc=dataViewsColumnsRepository.findOne(ruleCond.gettColumnId());
													log.info("dvc :"+dvc);
													if(dvc!=null && dvc.getQualifier()!=null && dvc.getQualifier().equalsIgnoreCase("Amount"))
														ruleCondition.settMany(true);
													else
														if(ruleCond.gettMany()!=null)
															ruleCondition.settMany(ruleCond.gettMany());
												}
											}
											else if(newRule.getRuleType().equalsIgnoreCase("MANY_TO_ONE"))
											{
												log.info("one2M :");
												if(ruleCond.getsColumnId()!=null)
												{
													DataViewsColumns dvc=dataViewsColumnsRepository.findOne(ruleCond.getsColumnId());
													log.info("dvc :"+dvc);
													if(dvc!=null && dvc.getQualifier()!=null && dvc.getQualifier().equalsIgnoreCase("Amount"))
														ruleCondition.setsMany(true);
													else
														if(ruleCond.getsMany()!=null)
															ruleCondition.setsMany(ruleCond.getsMany());
												}
											}
											else if(newRule.getRuleType().equalsIgnoreCase("MANY_TO_MANY"))
											{
												log.info("m2m :");
												if(ruleCond.getsColumnId()!=null)
												{
													DataViewsColumns dvc=dataViewsColumnsRepository.findOne(ruleCond.getsColumnId());
													log.info("dvc :"+dvc);
													if(dvc!=null && dvc.getQualifier()!=null && dvc.getQualifier().equalsIgnoreCase("Amount"))
														ruleCondition.setsMany(true);
													else
														if(ruleCond.getsMany()!=null)
															ruleCondition.setsMany(ruleCond.getsMany());
												}
												if(ruleCond.gettColumnId()!=null)
												{
													DataViewsColumns dvc=dataViewsColumnsRepository.findOne(ruleCond.gettColumnId());
													log.info("dvc :"+dvc);
													if(dvc!=null && dvc.getQualifier()!=null && dvc.getQualifier().equalsIgnoreCase("Amount"))
														ruleCondition.settMany(true);
													else
														if(ruleCond.gettMany()!=null)
															ruleCondition.settMany(ruleCond.gettMany());
												}
											}
											else
											{
												log.info("else");
												if(ruleCond.gettMany()!=null)
													ruleCondition.settMany(ruleCond.gettMany());
												if(ruleCond.getsMany()!=null)
													ruleCondition.setsMany(ruleCond.getsMany());
											}
										}
										ruleCondition.setRuleId(ruleId);
									}
									if(bulkUpload != null && bulkUpload.equalsIgnoreCase("true"))
									{
										errorMessage="Rule"+(i+1)+ " failed to save because, target column is null in rule condition "+(j+1);
										if(ruleCond.gettColumnName() == null || ruleCond.gettColumnName().equals("") || ruleCond.gettColumnName().isEmpty())
										{
											log.info("target column is null");
										}
										else
										{
											log.info("target column is not null... trying to fetch");
											String targetColumn = ruleCond.gettColumnName();
											log.info("targetColumn"+targetColumn);
											if(targetColumn == null)
												throw new Exception(errorMessage);
											errorMessage="Rule"+(i+1)+ " failed to save because, given target column in rule condition "+(j+1)+" is not tagged to "+ruleDTO.get(i).getRule().gettDataViewDisplayName();
											Long targetColumnId = targetColumnsMap.get(targetColumn).getId();
											if(targetColumnId == null)
												throw new Exception(errorMessage);
											log.info(" targetColumnsMap.get(targetColumn)"+ targetColumnsMap.get(targetColumn));
											log.info("targetColumnId"+targetColumnId);
											ruleCondition.settColumnId(targetColumnId);
											hasTColumn=true;
										}
										
									}
									else
									{
											errorMessage = "Rule"+(i+1)+ " failed to save because, given target column in rule condition "+(j+1)+" is null";
											if(ruleCond.gettColumnId() == null)
											{
												log.info("target column is null");
											}
											else
											{
												ruleCondition.settColumnId(ruleCond.gettColumnId());
												hasTColumn=true;
											}
											
									}
									
//									if(ruleCond.gettColumnId()!=null)
//										ruleCondition.settColumnId(ruleCond.gettColumnId());
									
									if(ruleCond.getOperator()!=null && !ruleCond.getOperator().isEmpty())
									{
										log.info((i+1)+"-ruleCond.getOperator()"+ruleCond.getOperator());
										errorMessage = "Rule"+(i+1)+ " failed to save while validating if given operator in rule condition "+(j+1)+" is specific to selected source column";
										if(bulkUpload != null && bulkUpload.equalsIgnoreCase("true"))
										{
											String operator = ruleCond.getOperator();
											LookUpCode operatorCode = new LookUpCode();
											log.info("inputs for operator code are"+operator+"=>"+"ruleCond.getsColumnName()"+ruleCond.getsColumnName()+"=>data type"+ sourceColumnsMap.get(ruleCond.getsColumnName()).getColDataType()+"=>"+newGrp.getTenantId());
											operatorCode = lookUpCodeRepository.findByMeaningAndLookUpTypeAndTenantId(operator, sourceColumnsMap.get(ruleCond.getsColumnName()).getColDataType(), newGrp.getTenantId());
											ruleCondition.setOperator(operatorCode.getLookUpCode());
											hasOperator= true;
										}
										else
										{
											log.info("while fetching operator in rule condition" +(i+1));
											ruleCondition.setOperator(ruleCond.getOperator());
											log.info("after operator" +(i+1));
											hasOperator= true;
										}
									}
										// if operator then source column
										//if not operator then source filter values
											//else if not operator then target filter values
									if(!hasSColumn && !hasTColumn)
									{
										log.info("07-08-13");
										errorMessage = "Rule-"+(i+1)+" save failed, rule condition-"+(j+1) +" is invalid";
										throw new Exception(errorMessage);
									}
									
									errorMessage = "Rule-"+(i+1)+" save failed, while validating filter values in  rule condition-"+(j+1);
									try{
										ruleCondition = validateSourceFilters(i,j,bulkUpload,ruleCond,sourceColumnsMap,newGrp,ruleCondition,hasSColumn,hasOperator,hasTColumn);
										log.info("step-40");
									}
									catch(Exception e)
									{
										errorMessage = e.getMessage();
										throw new Exception(errorMessage);
									}
									errorMessage = "Rule-"+(i+1)+" save failed, while validating target filter values in rule condition-"+(j+1) ;
									try{
										log.info("07-08-06-4");
										ruleCondition = validateTargetFilters(i,j,bulkUpload,ruleCond,targetColumnsMap,newGrp,ruleCondition,hasSColumn,hasOperator,hasTColumn);
										
										if(hasSColumn && (validSFilter || validSFunction || validSTolerance) && hasOperator && hasTColumn  && (validFilter || validFunction || validTolerance))
										{
											errorMessage = "Rule-"+(i+1)+" save failed, because rule condition-"+(j+1) +" is invalid.  A condition with both sides filter cannot have an operator in between";
											throw new Exception(errorMessage);
										}
										
										if(hasSColumn && (!validSFilter && !validSFunction && !validSTolerance) && !hasOperator && hasTColumn )
										{
											errorMessage = "Rule-"+(i+1)+" save failed, because rule condition-"+(j+1) +" is invalid";
											throw new Exception(errorMessage);
										}
									}
									catch(Exception e)
									{
										log.info("07-08-06-5");
										errorMessage = e.getMessage();
										throw new Exception(errorMessage);
									}
									
										
										
										log.info("step-42");
										if(ruleCond.getCloseBracket()!=null && !ruleCond.getCloseBracket().isEmpty())
											ruleCondition.setCloseBracket(ruleCond.getCloseBracket());
										log.info("step-43----size"+ruleCondList.size());
										if(j <= (ruleCondList.size()-1) )
										{
											log.info("step-44");
											if((j+1) <= (ruleCondList.size()-1))
											{
												log.info("step-44-1");
												errorMessage = "Rule-"+(i+1)+" save failed, logical operator in rule condition-"+(j+1) +" is missing";
												if(bulkUpload != null && bulkUpload.equalsIgnoreCase("true"))
												{
													log.info("step-44-3");
													LookUpCode logOpCode =new LookUpCode();
													errorMessage = "Rule-"+(i+1)+" save failed, because logical operator in rule condition-"+(j+1)+" doesnot exist for tenant";
													log.info("log oPERATOR"+ ruleCond.getLogicalOperator()+"---"+ newGrp.getTenantId());
													logOpCode = lookUpCodeRepository.findByMeaningAndLookUpTypeAndTenantId(ruleCond.getLogicalOperator(), "LOGICAL_OPERATOR", newGrp.getTenantId());
													ruleCondition.setLogicalOperator(logOpCode.getLookUpCode());
												}
												else
												{
													log.info("step-44-4");
													ruleCondition.setLogicalOperator(ruleCond.getLogicalOperator());
												}
											}
											else
											{
												log.info("step-44-2");
												
											}
											
										}
										log.info("step-45");
									//ruleCondition.setsColumnType(ruleCond.getsColumnType());
									//ruleCondition.setsDataViewId(ruleCond.getsDataViewId());

									//ruleCondition.settColumnType(ruleCond.gettColumnType());
									//ruleCondition.settDataViewId(ruleCond.gettDataViewId());
									ruleCondition.setCreatedBy(newRule.getCreatedBy());
									ruleCondition.setsReconAmountMatch(ruleCond.issReconAmountMatch());
									ruleCondition.settReconAmountMatch(ruleCond.istReconAmountMatch());
									log.info("step-46");
									ruleCondition.setCreationDate(ZonedDateTime.now());
									log.info("step-47");
									ruleCondition.setLastUpdatedBy(newRule.getLastUpdatedBy());
									log.info("step-48");
									ruleCondition.setLastUpdatedDate(ZonedDateTime.now());
									log.info("step-49");
									log.info("save condition"+ruleCondition);
									log.info("step-50");
									RuleConditions newRuleCond=new RuleConditions();
									newRuleCond = ruleConditionsRepository.save(ruleCondition);
									log.info("newRuleCond"+newRuleCond);
									log.info("step-51");
									ErrorReport ruleConditionSave=new ErrorReport();
									log.info("step-52");
									ruleConditionSave.setTaskName("Rule Condition Save");
									if(newRuleCond!=null && newRuleCond.getId()!=null)
									{
										ruleConditionSave.setTaskStatus("Success");
										ruleConditionSave.setDetails(newRuleCond.getId()+"");
									}
									else
									{
										log.info("failed saving condition");
										ruleConditionSave.setTaskStatus("failure");
										//errorReport.add(ruleConditionSave);
									}
									log.info("step-53");
									subTasksForConditions.add(ruleConditionSave);
									j=j+1;
								}
							}
							rulesave.setSubTasksList(subTasksForConditions);
						}

						log.info("next statement after exception");
						//										}
						//										catch(Exception e)
						//										{
						//											ErrorReport ruleSaveReport  = new ErrorReport();
						//											ruleSaveReport.setTaskName("Rule save");
						//											ruleSaveReport.setTaskStatus("Failed");
						//											ruleSaveReport.setDetails(errorMessage);
						//											errorReport.add(ruleSaveReport);
						//											throw new RuntimeException(errorMessage);
						//										}
						//									}
						//									catch(Exception e)
						//									{
						//										log.info("in catch Rule save failed because Target data view empty/doesnot exist");
						//										ErrorReport sourceDvReport  = new ErrorReport();
						//										sourceDvReport.setTaskName("Check for Target data view");
						//										sourceDvReport.setDetails(errorMessage);
						//										errorReport.add(sourceDvReport);
						//										throw new NullPointerException(errorMessage);
						//
						//									}
					}
					catch(Exception e)
					{
						throw new NullPointerException(errorMessage);
					}


					//							}catch(Exception e)
					//							{
					//								log.info("in catch Rule save failed because Source data view empty/doesnot exist");
					//								ErrorReport sourceDvReport  = new ErrorReport();
					//								sourceDvReport.setTaskName("Check for source data view");
					//								sourceDvReport.setDetails(errorMessage);
					//								errorReport.add(sourceDvReport);
					//								throw new Exception(errorMessage);
					//
					//							}

					//						}
					//						catch(Exception e)
					//						{
					//							log.info("in catch Source data view is empty/doesnot exist");
					//							ErrorReport sourceDvReport  = new ErrorReport();
					//							sourceDvReport.setTaskName("Check for source data view");
					//							sourceDvReport.setDetails(errorMessage);
					//							errorReport.add(sourceDvReport);
					//							throw new Exception(errorMessage);
					//						}
					//					}
					//					catch(Exception e)
					//					{
					//						ErrorReport ruleTypeReport  = new ErrorReport();
					//						ruleTypeReport.setTaskName("Rule save");
					//						ruleTypeReport.setTaskStatus("Failed");
					//						ruleTypeReport.setDetails(errorMessage);
					//						errorReport.add(ruleTypeReport);
					//						throw new RuntimeException(errorMessage);
					//					}


				}

			}
			catch(Exception e)
			{
				log.info("catch block of rule code is null");
				ErrorReport ruleCodeReport  = new ErrorReport();
				ruleCodeReport.setTaskName("Rule code in rule");
				ruleCodeReport.setDetails(errorMessage);
				subtasksListForGroupSave.add(ruleCodeReport);
				throw new RuntimeException(errorMessage);
			}
			//			finally{
			//				return errorReport;
			//			}

		}
		return subtasksListForGroupSave;

	}
	Boolean validFilter = false;
	Boolean validFunction = false;
	Boolean validTolerance = false;
	RuleConditions validateTargetFilters(int i,int j, String bulkUpload,RuleConditionsDTO ruleCond,HashMap<String, DataViewsColumns> targetColumnsMap,RuleGroup newGrp,RuleConditions ruleCondition
			,Boolean hassColumn,Boolean hasOperator,Boolean hastColumn) throws Exception
	{
		log.info("07-08-14");
		String errorMessage ="";
		validFilter = false;
		try{
			log.info("07-08-15");
			log.info("hassColumn && hasOperator && hastColumn"+hassColumn +hasOperator + hastColumn);
			errorMessage ="Rule-"+(i+1)+" save failed, invalid condition found. Target filter operator in rule condition-"+(j+1)+" is null";
			boolean mandCheckForFilters = false;
			if(hassColumn && hasOperator && hastColumn)
			{
				log.info("07-08-16");
			}
			else if(!hassColumn && !hasOperator && hastColumn)
			{
				log.info("07-08-17");
				mandCheckForFilters=true;
			}
			else
			{
				log.info("07-08-18");
				if(!hassColumn && hasOperator && hastColumn)
				{
					log.info("07-08-19");
					errorMessage="Rule-"+(i+1)+" save failed, rule condition-"+(j+1)+" is invalid. A condition with operator should definitely have source column tagged to it.";
					throw new Exception(errorMessage);
				}
				log.info("07-08-20");
			}
			if(hastColumn && ruleCond.gettOperator() != null && !ruleCond.gettOperator().equals("") && ruleCond.gettOperator()!="")
			{
				log.info("07-08-21");
				
				if(ruleCond.gettOperator() != null && !ruleCond.gettOperator().equals("") && ruleCond.gettOperator()!="")
				{
					log.info("07-08-22");
					if(bulkUpload != null && bulkUpload.equalsIgnoreCase("true"))
					{
						log.info("07-08-23");
						String tOperator = ruleCond.gettOperator();
						LookUpCode tOpCode = new LookUpCode();
						DataViewsColumns tDVCol = targetColumnsMap.get(ruleCond.gettColumnName());
						log.info("07-08-24");
						errorMessage = "Rule-"+(i+1)+" save failed, because target operator in rule condition-"+(j+1) +" doesnot exist for the selected target column";
						tOpCode  = lookUpCodeRepository.findByMeaningAndLookUpTypeAndTenantId(tOperator,tDVCol.getColDataType(),newGrp.getTenantId());
					}
					else
					{
						log.info("07-08-25");
						ruleCondition.settOperator(ruleCond.gettOperator());
					}
					
					//search for value else validation failed
					errorMessage = "Rule-"+(i+1)+" save failed, invalid condition found. target filter value in rule condition-"+(j+1) +" is null";
					ruleCondition.settValue(ruleCond.gettValue());
					validFilter = true;
					log.info("07-08-26");
					ruleCondition = validateFormula(i, j, bulkUpload, ruleCond, targetColumnsMap, newGrp, ruleCondition, hassColumn, hasOperator, hastColumn);
					log.info("07-08-27");
					if(hastColumn)
					{
						log.info("07-08-28");
						ruleCondition = validateTolerance(i, j, bulkUpload, ruleCond, targetColumnsMap, newGrp, ruleCondition, hassColumn, hasOperator, hastColumn);
					}

					if(mandCheckForFilters )
					{
						log.info("validFilter"+validFilter+" -validFunction"+validFunction+"- validTolerance"+validTolerance);
						if(validFilter || validFunction || validTolerance)
						{
							
						}
						else
						{
							errorMessage="Rule-"+(i+1)+" save failed, invalid condition found. Atleast one filter is not tagged in rule condition-"+(j+1);
							throw new Exception(errorMessage);
						}
					}
					else 
					{
						
					}
				}
				else
				{
					if(mandCheckForFilters)
					{
						throw new Exception(errorMessage);
					}
					else
					{
						
					}
				}
			}
			else
			{
				if(mandCheckForFilters)
				{
					throw new Exception(errorMessage);
				}
				else
				{
					try{
						ruleCondition = validateFormula(i,j,bulkUpload,ruleCond,targetColumnsMap,newGrp,ruleCondition,hassColumn,hasOperator,hastColumn);	
					}
					catch(Exception e)
					{
						throw new Exception(errorMessage);
					}
					try{

						if(hastColumn )
						{
							log.info("tolerance-validate-1");
							ruleCondition = validateTolerance(i, j, bulkUpload, ruleCond, targetColumnsMap, newGrp, ruleCondition, hassColumn, hasOperator, hastColumn);
						}
					}
					catch(Exception e)
					{
						throw new Exception(errorMessage);
					}
				}
			}
		}
		catch(Exception e)
		{
			throw new Exception(errorMessage);
		}
		return ruleCondition;
	}
	
	RuleConditions validateFormula(int i,int j, String bulkUpload,RuleConditionsDTO ruleCond,HashMap<String, DataViewsColumns> targetColumnsMap,RuleGroup newGrp,RuleConditions ruleCondition
			,Boolean hassColumn,Boolean hasOperator,Boolean hastColumn)throws Exception
	{
		validFunction=false;
		if(ruleCond.gettFormula()!=null && !ruleCond.gettFormula().isEmpty())
		{
			ruleCondition.settFormula(ruleCond.gettFormula());
			validFunction = true;
		}
		return ruleCondition;
	}
	
	RuleConditions validateTolerance(int i,int j, String bulkUpload,RuleConditionsDTO ruleCond,HashMap<String, DataViewsColumns> targetColumnsMap,RuleGroup newGrp,RuleConditions ruleCondition
			,Boolean hassColumn,Boolean hasOperator,Boolean hastColumn)throws Exception
	{
		String errorMessage ="";
		validTolerance=false;
		log.info("07-08-29");
		boolean mandCheckForTolerance = false;
		try{
			log.info("07-08-30");
			if(hassColumn && hasOperator && hastColumn)
			{
				log.info("07-08-31");
			}
			else if(!hassColumn && !hasOperator && hastColumn)
			{
				log.info("07-08-32");
				mandCheckForTolerance=true;
			}
			log.info("07-08-33");
			if(ruleCond.gettToleranceType()!=null && !ruleCond.gettToleranceType().isEmpty())
			{
				log.info("07-08-34");
				errorMessage = "Rule-"+(i+1)+" save failed, because target tolerance type in rule condition-"+(j+1)+" doesnot exist for tenant";
				if(bulkUpload != null && bulkUpload.equalsIgnoreCase("true"))
				{
					log.info("07-08-35");
					LookUpCode toleranceTypeCode = new LookUpCode();
					toleranceTypeCode = lookUpCodeRepository.findByMeaningAndLookUpTypeAndTenantId(ruleCond.gettToleranceType(), "TOLERANCE_TYPE", newGrp.getTenantId());
					ruleCondition.settToleranceType(toleranceTypeCode.getLookUpCode());
					log.info("07-08-36");
				}
				else
				{
					log.info("07-08-37");
					log.info("tolerance type:"+ruleCond.gettToleranceType());
					ruleCondition.settToleranceType(ruleCond.gettToleranceType());
					log.info("07-08-38");
				}
				log.info("07-08-39");
				if(ruleCond.gettToleranceOperatorFrom()!=null && !ruleCond.gettToleranceOperatorFrom().isEmpty())
				{
					log.info("07-08-40");
					ruleCondition.settToleranceOperatorFrom(ruleCond.gettToleranceOperatorFrom());
				}
				else
				{
					log.info("07-08-41");
					ruleCondition.settToleranceOperatorFrom("+");
				}
				log.info("07-08-42");
				errorMessage = "Rule-"+(i+1)+" save failed, because target tolerance from value in rule condition-"+(j+1)+" is null";
				if(ruleCond.gettToleranceValueFrom() != null && !ruleCond.gettToleranceValueFrom().equals("") && ruleCond.gettToleranceValueFrom() != "" )
				ruleCondition.settToleranceValueFrom(ruleCond.gettToleranceValueFrom());
				else
				{
					//throw new Exception (errorMessage);
				}
				log.info("07-08-43");
				if(ruleCond.gettToleranceOperatorTo()!=null && !ruleCond.gettToleranceOperatorTo().isEmpty())
				{
					log.info("07-08-44");
					ruleCondition.settToleranceOperatorTo(ruleCond.gettToleranceOperatorTo());
				}
				else
				{
					log.info("07-08-45");
					ruleCondition.settToleranceOperatorTo("+");
				}
				errorMessage = "Rule-"+(i+1)+" save failed, because target tolerance to value in rule condition-"+(j+1)+" is null";
				log.info("07-08-46");
				if(ruleCond.gettToleranceValueTo() != null && !ruleCond.gettToleranceValueTo().equals("") && ruleCond.gettToleranceValueTo() != "" )
				ruleCondition.settToleranceValueTo(ruleCond.gettToleranceValueTo());
				else
				{
					//throw new Exception (errorMessage);
				}
				log.info("07-08-47");
				if(ruleCondition.gettToleranceValueFrom()!=null && ruleCondition.gettToleranceValueTo() != null)
				validTolerance=true;
				log.info("ruleCondition after tolerance set"+ruleCondition.toString());
				log.info("validTolerance isss:"+validTolerance);
			}
			else
			{

			}
		}
		catch(Exception e)
		{
			throw new Exception (errorMessage);
		}

		return ruleCondition;
	}
	Boolean validSFilter = false;
	Boolean validSFunction = false;
	Boolean validSTolerance = false;
	RuleConditions validateSourceFilters(int i,int j, String bulkUpload,RuleConditionsDTO ruleCond,HashMap<String, DataViewsColumns> sourceColumnsMap,RuleGroup newGrp,RuleConditions ruleCondition
			,Boolean hassColumn,Boolean hasOperator,Boolean hastColumn) throws Exception
	{
		String errorMessage ="";
		validSFilter=false;
		boolean mandCheckForFilters = false;
		log.info("step-1");
		try{
			errorMessage = "Rule-"+(i+1)+" save failed, because source operator in rule condition-"+(j+1) +" is null. A condition without operator and target column should definitely have atleast one filter.";
			log.info("hassColumn && hasOperator && hastColumn"+hassColumn + hasOperator + hastColumn);
			if(hassColumn && hasOperator && hastColumn)
			{
				log.info("step-2");
			}
			else if(hassColumn && !hasOperator && !hastColumn)
			{
				log.info("step-3");
				mandCheckForFilters=true;
			}
			else
			{
				if((hassColumn && hasOperator && !hastColumn))
				{
					errorMessage="Rule-"+(i+1)+" save failed, rule condition-"+(j+1)+" is invalid. A condition with operator should definitely have target column";
					throw new Exception(errorMessage);	
				}
			}
			if(hassColumn && ruleCond.getsOperator() != null && !ruleCond.getsOperator().equals("") && ruleCond.getsOperator() !="")
			{
				log.info("step-4");
				
				if(ruleCond.getsOperator() != null && !ruleCond.getsOperator().equals("") && ruleCond.getsOperator()!="")
				{
					log.info("step-5");
					LookUpCode sOpCode = new LookUpCode();
					log.info("ruleCondition.getsColumnId()"+ruleCondition.getsColumnId());
					DataViewsColumns sDVCol = new DataViewsColumns(); 
				
					log.info("step-6");
					errorMessage = "Rule-"+(i+1)+" save failed, because source operator in rule condition-"+(j+1) +" doesnot exist for the selected source column";
					if(bulkUpload != null && bulkUpload.equalsIgnoreCase("true"))
					{
						sDVCol = sourceColumnsMap.get(ruleCond.getsColumnName());
						log.info("sDVCol"+sDVCol.toString());
						String sOperator = ruleCond.getsOperator();
						log.info("sOperator,sDVCol.getColDataType(),newGrp.getTenantId()"+sOperator+sDVCol.getColDataType()+newGrp.getTenantId());
						sOpCode  = lookUpCodeRepository.findByMeaningAndLookUpTypeAndTenantId(sOperator,sDVCol.getColDataType(),newGrp.getTenantId());
						log.info("step-7");
						ruleCondition.setsOperator(sOpCode.getLookUpCode());
					}
					else
					{
						ruleCondition.setsOperator(ruleCond.getsOperator());
					}
					errorMessage = "Rule-"+(i+1)+" save failed, because source filter value in rule condition-"+(j+1) +" doesnot exist for the selected source column";
					log.info("step-8");
					ruleCondition.setsValue(ruleCond.getsValue());
					validSFilter=true;
					//search for value else validation failed
					log.info("step-9");
					errorMessage = "Rule-"+(i+1)+" save failed, invalid condition found. source filter value in rule condition-"+(j+1) +" is null";
					try{
					ruleCondition = validateSourceFormula(i, j, bulkUpload, ruleCond, sourceColumnsMap, newGrp, ruleCondition, hassColumn, hasOperator, hastColumn);
					}catch(Exception e)
					{
						errorMessage = e.getMessage();
						throw new Exception(errorMessage);
					}
					log.info("step-10");
					try{
					ruleCondition = validateSTolerance(i, j, bulkUpload, ruleCond, sourceColumnsMap, newGrp, ruleCondition, hassColumn, hasOperator, hastColumn);
					}
					catch(Exception e)
					{
						errorMessage = e.getMessage();
						throw new Exception(errorMessage);
					}
					log.info("step-11");
					if(mandCheckForFilters )
					{
						log.info("step-12");
						log.info("validSFilter"+validSFilter+" -validSFunction"+validSFunction+"- validSTolerance"+validSTolerance);
						if(validSFilter || validSFunction || validSTolerance)
						{
							
						}
						else
						{
							log.info("step-13");
							errorMessage="Rule-"+(i+1)+" save failed, invalid condition found at rule condition-"+(j+1)+". A condition without operator and target column should definitely have atleast one filter.";
							throw new Exception(errorMessage);
						}
					}
					else 
					{
						log.info("step-14");
					}
				}
				else
				{
					if(mandCheckForFilters)
					{
						log.info("step-14");
						throw new Exception(errorMessage);
					}
					else
					{
						log.info("step-15");
						try{
						ruleCondition = validateSourceFormula(i,j,bulkUpload,ruleCond,sourceColumnsMap,newGrp,ruleCondition,hassColumn,hasOperator,hastColumn);
						}catch(Exception e)
						{
							errorMessage = e.getMessage();
							throw new Exception(errorMessage);
						}
						if(hassColumn)
						{
						try{
							ruleCondition = validateSTolerance(i, j, bulkUpload, ruleCond, sourceColumnsMap, newGrp, ruleCondition, hassColumn, hasOperator, hastColumn);
						}
						catch(Exception e)
						{
							errorMessage = e.getMessage();
							throw new Exception(errorMessage);
						}
						}
						
					}
				}
			}
			else
			{
				log.info("step-16");
				if(mandCheckForFilters)
				{
					log.info("step-17");
					throw new Exception(errorMessage);
				}
				else
				{
					log.info("step-18");
					try{
						ruleCondition = validateSourceFormula(i,j,bulkUpload,ruleCond,sourceColumnsMap,newGrp,ruleCondition,hassColumn,hasOperator,hastColumn);
					}
					catch(Exception e)
					{
						errorMessage= e.getMessage();
						throw new Exception(errorMessage);
					}
					try{
						if(hassColumn)
						{
							ruleCondition = validateSTolerance(i, j, bulkUpload, ruleCond, sourceColumnsMap, newGrp, ruleCondition, hassColumn, hasOperator, hastColumn);
							log.info("step-33");
						}
						log.info("step-34");
							
					}
					catch(Exception e)
					{
						log.info("step-35");
						errorMessage= e.getMessage();
						throw new Exception(errorMessage);
					}
					log.info("step-36");
				}
				log.info("step-37");
			}
			log.info("step-38");
		}
		catch(Exception e)
		{
			log.info("step-19");
			throw new Exception(errorMessage);
		}
		log.info("step-39");
		return ruleCondition;
	}
	RuleConditions validateSTolerance(int i,int j, String bulkUpload,RuleConditionsDTO ruleCond,HashMap<String, DataViewsColumns> targetColumnsMap,RuleGroup newGrp,RuleConditions ruleCondition
			,Boolean hassColumn,Boolean hasOperator,Boolean hastColumn)throws Exception
	{
		String errorMessage ="";
		validSTolerance=false;
		log.info("step-23");
		try{
			if(ruleCond.getsToleranceType()!=null && !ruleCond.getsToleranceType().isEmpty())
			{
				log.info("step-24");
				errorMessage = "Rule-"+(i+1)+" save failed, because source tolerance type in rule condition-"+(j+1)+" doesnot exist for tenant";
				if(bulkUpload != null && bulkUpload.equalsIgnoreCase("true"))
				{
					log.info("step-25");
					LookUpCode toleranceTypeCode = new LookUpCode();
					toleranceTypeCode = lookUpCodeRepository.findByMeaningAndLookUpTypeAndTenantId(ruleCond.getsToleranceType(), "TOLERANCE_TYPE", newGrp.getTenantId());
					ruleCondition.setsToleranceType(toleranceTypeCode.getLookUpCode());
				}
				else
					ruleCondition.setsToleranceType(ruleCond.getsToleranceType());
				log.info("step-26");
				if(ruleCond.getsToleranceOperatorFrom()!=null && !ruleCond.getsToleranceOperatorFrom().isEmpty())
					ruleCondition.setsToleranceOperatorFrom(ruleCond.getsToleranceOperatorFrom());
				else
				{
					ruleCondition.setsToleranceOperatorFrom("+");
				}
				log.info("step-27");
				errorMessage = "Rule-"+(i+1)+" save failed, because source tolerance from value in rule condition-"+(j+1)+" is null";
				log.info("ruleCond.getsToleranceValueFrom()"+ruleCond.getsToleranceValueFrom());
				if(ruleCond.getsToleranceValueFrom() != null && !ruleCond.getsToleranceValueFrom().equals("") && ruleCond.getsToleranceValueFrom() != "")
				ruleCondition.setsToleranceValueFrom(ruleCond.getsToleranceValueFrom());
				else
				{
					//throw new Exception (errorMessage);
				}
				log.info("step-28");
				if(ruleCond.getsToleranceOperatorTo()!=null && !ruleCond.getsToleranceOperatorTo().isEmpty())
					ruleCondition.setsToleranceOperatorTo(ruleCond.getsToleranceOperatorTo());
				else
				{
					ruleCondition.setsToleranceOperatorTo("+");
				}
				log.info("step-29");
				errorMessage = "Rule-"+(i+1)+" save failed, because source tolerance to value in rule condition-"+(j+1)+" is null";
				if(ruleCond.getsToleranceValueTo() != null && !ruleCond.getsToleranceValueTo().equals("") && ruleCond.getsToleranceValueTo() != "")
				ruleCondition.setsToleranceValueTo(ruleCond.getsToleranceValueTo());
				else
				{
					//throw new Exception (errorMessage);
				}
				log.info("step-30");
				if(ruleCondition.getsToleranceValueFrom()!=null && ruleCondition.getsToleranceValueTo() != null)
				validSTolerance=true;

			}
			else
			{
				log.info("step-31");
			}
		}catch(Exception e)
		{
			throw new Exception (errorMessage);
		}
		log.info("step-32");
		
		return ruleCondition;
	}
	RuleConditions validateSourceFormula(int i,int j, String bulkUpload,RuleConditionsDTO ruleCond,HashMap<String, DataViewsColumns> targetColumnsMap,RuleGroup newGrp,RuleConditions ruleCondition
			,Boolean hassColumn,Boolean hasOperator,Boolean hastColumn)throws Exception
	{
		validSFunction=false;
		log.info("step-20");
		if(ruleCond.getsFormula()!=null && !ruleCond.getsFormula().isEmpty())
		{
			log.info("step-21");
			ruleCondition.setsFormula(ruleCond.getsFormula());
			log.info("step-22");
			validSFunction = true;
		}
		return ruleCondition;
	}
	public static String buildReconConditionsExpression(List<RuleConditionsDTO> ruleConditions)
	{
		String expression = "";
		for(RuleConditionsDTO ruleCond : ruleConditions)
		{
			
		}
		return expression;
	}
	@Transactional(propagation= Propagation.REQUIRED)
	public  String buildWhereClause(List<AppRuleCondDto> ruleConditions) throws Exception {
		
		
		// TODO Auto-generated method stub
		String whereClause = " ";
		try{
			
		for (AppRuleCondDto rCon : ruleConditions) {

			if (rCon.getOperator() != null) {
				String sourceCol = "";
				String operator = "";
				String targetCol = "";
				String sourceTagetExpression = "";
				String logicalOperator = "";

				if (rCon.getOperator().equals("=")
						|| rCon.getOperator().equalsIgnoreCase("EQUALS")) {
					operator = "=";
				} else if (rCon.getOperator().equals("<")
						|| rCon.getOperator().equalsIgnoreCase("LESS THAN")
						|| rCon.getOperator().equalsIgnoreCase("LESS_THAN")) {
					operator = "<";
				} else if (rCon.getOperator().equals("<=")
						|| rCon.getOperator().equalsIgnoreCase(
								"LESS THAN OR EQUAL")
						|| rCon.getOperator().equalsIgnoreCase(
								"LESS_THAN_OR_EQUAL")		) {
					operator = "<=";
				} else if (rCon.getOperator().equals(">")
						|| rCon.getOperator().equalsIgnoreCase("GREATER THAN")
						|| rCon.getOperator().equalsIgnoreCase("GREATER_THAN")) {
					operator = ">";
				} else if (rCon.getOperator().equals(">=")
						|| rCon.getOperator().equalsIgnoreCase(
								"GREATER THAN OR EQUAL")
						|| rCon.getOperator().equalsIgnoreCase(
								"GREATER_THAN_OR_EQUAL")) {
					operator = ">=";
				} else if (rCon.getOperator().equalsIgnoreCase("CONTAINS")) {
					operator = " like concat('%',";
				} else if (rCon.getOperator().equalsIgnoreCase("BEGINS WITH")
						|| rCon.getOperator().equalsIgnoreCase("BEGINS_WITH")) {
					operator = " like concat(";
				} else if (rCon.getOperator().equalsIgnoreCase("ENDS WITH")
						|| rCon.getOperator().equalsIgnoreCase("ENDS_WITH")) {
					operator = " like concat('%',";
				} else if (rCon.getOperator().equalsIgnoreCase("NOT EQUALS")
						|| rCon.getOperator().equalsIgnoreCase("NOT_EQUALS")
						|| rCon.getOperator().equalsIgnoreCase("!=")) {
					operator = "!=";
				} else if (rCon.getOperator().equalsIgnoreCase("IN")) {
					operator = "IN";
				}

				if (rCon.getsColumnName() != null) {
					sourceCol = "lower("+rCon.getsColumnName()+")";
				}

		

				if(operator.equalsIgnoreCase("IN") && rCon.getValue() != null)
				{
					targetCol = "(";
					String[] valArr = rCon.getValue().split(",");
					System.out.println("valArr "+valArr.toString());
					for(int i=0; i< valArr.length; i++)
					{
						targetCol = targetCol+  "'" + valArr[i].toLowerCase() + "',";
					}
					if(targetCol.endsWith(","))
					{
						targetCol = targetCol.substring(0,targetCol.length()-1);
					}
					targetCol = targetCol + ")";
				}
				else if (rCon.getValue() != null) {
					targetCol = "'" + rCon.getValue().toLowerCase() + "'";
					// targetCol = rCon.getValue();
				}

				if (rCon.getOperator().equalsIgnoreCase("CONTAINS")
						|| rCon.getOperator().equalsIgnoreCase("BEGINS WITH")
						|| rCon.getOperator().equalsIgnoreCase("BEGINS_WITH")) {
					targetCol = targetCol + ",'%')";
				} else if (rCon.getOperator().equalsIgnoreCase("ENDS WITH")
						|| rCon.getOperator().equalsIgnoreCase("ENDS_WITH")) {
					targetCol = targetCol + ")";
				}

				if (sourceTagetExpression.length() == 0) {
					sourceTagetExpression = " " + sourceCol + " " + operator
							+ " " + targetCol + " ";
				}

				if (rCon.getLogicalOperator() != null
						&& rCon.getLogicalOperator().equalsIgnoreCase("AND")) {
					logicalOperator = "AND";
				} else if (rCon.getLogicalOperator() != null
						&& rCon.getLogicalOperator().equalsIgnoreCase("OR")) {
					logicalOperator = "OR";
				}
				else
				{
					logicalOperator = "AND";
				}
				//log.info("sourceTagetExpression in where clause method :"+sourceTagetExpression);
				if(rCon.getOpenBracket()!=null)
				{
					sourceTagetExpression=rCon.getOpenBracket()+sourceTagetExpression;
				}
				if(rCon.getCloseBracket()!=null)
				{
					sourceTagetExpression=sourceTagetExpression+rCon.getCloseBracket()+" ";
				}
				whereClause = whereClause + sourceTagetExpression
						+ logicalOperator + " ";
			}
		}

		whereClause = whereClause.trim();
		if (whereClause.trim().endsWith("OR")) {
			whereClause = whereClause.substring(0, whereClause.length() - 2);
		}
		if (whereClause.trim().endsWith("AND")) {
			whereClause = whereClause.substring(0, whereClause.length() - 3);
		}
		}
		catch(Exception e)
		{
			throw new Exception(e);
		}
		return whereClause;
	}
	
	public Boolean balanceParanthesis (@RequestParam String condition) {
		
		Boolean result = false;
		 Stack<Integer> stk = new Stack<Integer>();
		int len = condition.length();	
        System.out.println("\nMatches and Mismatches:\n");
        for (int i = 0; i < len; i++)
        {    
            char ch = condition.charAt(i);
            if (ch == '(')
                stk.push(i);
            else if (ch == ')')
            {
                try
                {
                    int p = stk.pop() + 1;
                    log.info("')' at index "+(i+1)+" matched with '(' at index "+p);
                    result = true;
                }
                catch(Exception e)
                {
                	log.info("')' at index "+(i+1)+" is unmatched");
                    result = false;
                }
            }            
        }
        while (!stk.isEmpty() )
        {
        	  result = false;
        	  log.info("'(' at index "+(stk.pop() +1)+" is unmatched");
        }
            
		return result;
	}

}
