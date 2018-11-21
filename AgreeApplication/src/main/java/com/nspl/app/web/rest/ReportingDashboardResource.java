package com.nspl.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.ReportingDashboard;
import com.nspl.app.domain.ReportingDashboardUsecases;
import com.nspl.app.repository.ReportingDashboardRepository;
import com.nspl.app.repository.ReportingDashboardUsecasesRepository;
import com.nspl.app.repository.search.ReportingDashboardSearchRepository;
import com.nspl.app.repository.search.ReportingDashboardUsecasesSearchRepository;
import com.nspl.app.service.UserJdbcService;
import com.nspl.app.web.rest.dto.ReportingDashboardDTO;
import com.nspl.app.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ReportingDashboard.
 */
@RestController
@RequestMapping("/api")
public class ReportingDashboardResource {

    private final Logger log = LoggerFactory.getLogger(ReportingDashboardResource.class);

    private static final String ENTITY_NAME = "reportingDashboard";

    private final ReportingDashboardRepository reportingDashboardRepository;
    
    @Inject
    ReportingDashboardUsecasesRepository reportingDashboardUsecasesRepository;
    
    @Inject
    ReportingDashboardUsecasesSearchRepository reportingDashboardUsecasesSearchRepository;
    
    @Inject
    UserJdbcService userJdbcService;

    private final ReportingDashboardSearchRepository reportingDashboardSearchRepository;

    public ReportingDashboardResource(ReportingDashboardRepository reportingDashboardRepository, ReportingDashboardSearchRepository reportingDashboardSearchRepository) {
        this.reportingDashboardRepository = reportingDashboardRepository;
        this.reportingDashboardSearchRepository = reportingDashboardSearchRepository;
    }
    
    // *********************  Custom APIs Start  ****************************************
    
    /**
     * Author: @Rk
     * POST  /saveDashboardDefinitions : Create a new reportingDashboard and usecases.
     */
    @PostMapping("/saveDashboardDefinitions")
    @Timed
    public ResponseEntity<Boolean> saveDashboardDefinitions(HttpServletRequest request,@RequestBody ReportingDashboardDTO reportingDashboardDTO)throws URISyntaxException{
    	log.info("REST request to save ReportingDashboard : "+reportingDashboardDTO);
    	HashMap map1=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map1.get("tenantId").toString());
    	Long userId=Long.parseLong(map1.get("userId").toString());
    	log.info("User Id : "+userId+"  Tenant Id : "+tenantId);
    	if(reportingDashboardDTO.getReportingDashboard().getId()==null) {
    		log.info("New dashboard with name : "+reportingDashboardDTO.getReportingDashboard().getName());
    		reportingDashboardDTO.getReportingDashboard().setUserId(userId);
    		reportingDashboardDTO.getReportingDashboard().setTenantId(tenantId);
    		reportingDashboardDTO.getReportingDashboard().setCreatedBy(userId);
    		reportingDashboardDTO.getReportingDashboard().setCreatedDate(LocalDate.now());
    	}else {
    		log.info("Update dashboard with name : "+reportingDashboardDTO.getReportingDashboard().getName()+" And Id : "+reportingDashboardDTO.getReportingDashboard().getId());
    		reportingDashboardDTO.getReportingDashboard().setUpdatedBy(userId);
    		reportingDashboardDTO.getReportingDashboard().setUdatedDate(LocalDate.now());
    	}
    	ArrayList<ReportingDashboard> defaultDashBoards=reportingDashboardRepository.getDefaultDashboard(userId);
    	if(reportingDashboardDTO.getReportingDashboard().isDefaultFlag() && defaultDashBoards.size()>0) {
    		for(ReportingDashboard eachDashBoard:defaultDashBoards) {
    			eachDashBoard.setDefaultFlag(false);
    		}
    		reportingDashboardRepository.save(defaultDashBoards);
    	}
    	log.info(">>>>>Saving dashboard");
    	ReportingDashboard result = reportingDashboardRepository.save(reportingDashboardDTO.getReportingDashboard());
        for(ReportingDashboardUsecases item:reportingDashboardDTO.getReportingDashboardUsecasesList()) {
        	item.setDashboardId(result.getId());
        }
    	if (reportingDashboardDTO.getReportingDashboard().getId() != null) {
    		ArrayList<Long> oldIds=new ArrayList<Long>();
    		for(ReportingDashboardUsecases item:reportingDashboardDTO.getReportingDashboardUsecasesList()) {
    			if(item.getId()!=null) {
    				oldIds.add(item.getId());
    			}
    		}
    		if(oldIds.size()>0) {
    			ArrayList<ReportingDashboardUsecases> usecaseList=reportingDashboardUsecasesRepository.fetchUsecasesByNotInIds(result.getId(),oldIds);
    			if(usecaseList.size()>0) {
	    			reportingDashboardUsecasesRepository.delete(usecaseList);
    			}
    		}
        }
    	if(reportingDashboardDTO.getReportingDashboardUsecasesList().size()>0) {
    		log.info("Posting usecases");
    		List<ReportingDashboardUsecases> usecaseResult = reportingDashboardUsecasesRepository.save(reportingDashboardDTO.getReportingDashboardUsecasesList());
    	}
    	
    	return ResponseEntity.created(new URI("/api/saveDashboardDefinitions/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(true);
    }
    
    /**
     * Author: @Rk
     * Get  /getDashboardDefinitions : get reportingDashboard and usecases.
     */
    @GetMapping("/getDashboardDefinitions")
    @Timed
    public ResponseEntity<ReportingDashboardDTO> getDashboardDefinitions(@RequestParam Long dashBoardId)throws URISyntaxException{
    	log.info("REST request to get ReportingDashboard : "+dashBoardId);
    	ReportingDashboardDTO finalObj=new ReportingDashboardDTO();
    	finalObj.setReportingDashboard(reportingDashboardRepository.findOne(dashBoardId));
    	finalObj.setReportingDashboardUsecasesList(reportingDashboardUsecasesRepository.findByDashboardId(dashBoardId));
    	
    	return ResponseUtil.wrapOrNotFound(Optional.ofNullable(finalObj));
    }
    
    /**
     * Author: @Rk
     * Get  /checkDashboardName : get reportingDashboard and usecases.
     */
    @GetMapping("/checkDashboardName")
    @Timed
    public Long checkDashboardName(@RequestParam String dashBoardName)throws URISyntaxException{
    	log.info("REST request to ReportingDashboard Name : "+dashBoardName);
    	ReportingDashboardDTO finalObj=new ReportingDashboardDTO();
    	ArrayList<ReportingDashboard> dbList = reportingDashboardRepository.fetchDashboardListByName(dashBoardName);
    	
    	if(dbList.size()==1) {
    		return dbList.get(0).getId();
    	}
    	return new Long(dbList.size());
    }
    
    
    // *********************   Custom APIs End	 ****************************************

    /**
     * POST  /reporting-dashboards : Create a new reportingDashboard.
     *
     * @param reportingDashboard the reportingDashboard to create
     * @return the ResponseEntity with status 201 (Created) and with body the new reportingDashboard, or with status 400 (Bad Request) if the reportingDashboard has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/reporting-dashboards")
    @Timed
    public ResponseEntity<ReportingDashboard> createReportingDashboard(@RequestBody ReportingDashboard reportingDashboard) throws URISyntaxException {
        log.debug("REST request to save ReportingDashboard : {}", reportingDashboard);
        if (reportingDashboard.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new reportingDashboard cannot already have an ID")).body(null);
        }
        ReportingDashboard result = reportingDashboardRepository.save(reportingDashboard);
        reportingDashboardSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/reporting-dashboards/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /reporting-dashboards : Updates an existing reportingDashboard.
     *
     * @param reportingDashboard the reportingDashboard to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated reportingDashboard,
     * or with status 400 (Bad Request) if the reportingDashboard is not valid,
     * or with status 500 (Internal Server Error) if the reportingDashboard couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/reporting-dashboards")
    @Timed
    public ResponseEntity<ReportingDashboard> updateReportingDashboard(@RequestBody ReportingDashboard reportingDashboard) throws URISyntaxException {
        log.debug("REST request to update ReportingDashboard : {}", reportingDashboard);
        if (reportingDashboard.getId() == null) {
            return createReportingDashboard(reportingDashboard);
        }
        ReportingDashboard result = reportingDashboardRepository.save(reportingDashboard);
        reportingDashboardSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, reportingDashboard.getId().toString()))
            .body(result);
    }

    /**
     * GET  /reporting-dashboards : get all the reportingDashboards.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of reportingDashboards in body
     */
    @GetMapping("/reporting-dashboards")
    @Timed
    public List<ReportingDashboard> getAllReportingDashboards(HttpServletRequest request) {
        log.debug("REST request to get all ReportingDashboards");
        HashMap map1=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map1.get("tenantId").toString());
    	Long userId=Long.parseLong(map1.get("userId").toString());
        return reportingDashboardRepository.findByTenantIdAndUserId(tenantId,userId);
    }

    /**
     * GET  /reporting-dashboards/:id : get the "id" reportingDashboard.
     *
     * @param id the id of the reportingDashboard to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the reportingDashboard, or with status 404 (Not Found)
     */
    @GetMapping("/reporting-dashboards/{id}")
    @Timed
    public ResponseEntity<ReportingDashboard> getReportingDashboard(@PathVariable Long id) {
        log.debug("REST request to get ReportingDashboard : {}", id);
        ReportingDashboard reportingDashboard = reportingDashboardRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(reportingDashboard));
    }

    /**
     * DELETE  /reporting-dashboards/:id : delete the "id" reportingDashboard.
     *
     * @param id the id of the reportingDashboard to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/reporting-dashboards/{id}")
    @Timed
    public ResponseEntity<Void> deleteReportingDashboard(@PathVariable Long id) {
        log.debug("REST request to delete ReportingDashboard : {}", id);
        reportingDashboardRepository.delete(id);
        reportingDashboardSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/reporting-dashboards?query=:query : search for the reportingDashboard corresponding
     * to the query.
     *
     * @param query the query of the reportingDashboard search
     * @return the result of the search
     */
    @GetMapping("/_search/reporting-dashboards")
    @Timed
    public List<ReportingDashboard> searchReportingDashboards(@RequestParam String query) {
        log.debug("REST request to search ReportingDashboards for query {}", query);
        return StreamSupport
            .stream(reportingDashboardSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
