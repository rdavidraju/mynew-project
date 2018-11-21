package com.nspl.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.FavouriteReports;
import com.nspl.app.domain.Reports;
import com.nspl.app.repository.DataViewsRepository;
import com.nspl.app.repository.FavouriteReportsRepository;
import com.nspl.app.repository.ReportsRepository;
import com.nspl.app.service.UserJdbcService;
import com.nspl.app.web.rest.util.HeaderUtil;

import io.github.jhipster.web.util.ResponseUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * REST controller for managing FavouriteReports.
 */
@RestController
@RequestMapping("/api")
public class FavouriteReportsResource {

    private final Logger log = LoggerFactory.getLogger(FavouriteReportsResource.class);

    private static final String ENTITY_NAME = "favouriteReports";

    private final FavouriteReportsRepository favouriteReportsRepository;
    
//    private final 
    @Inject
    UserJdbcService userJdbcService;
    
    @Inject
    ReportsRepository reportsRepository;

    public FavouriteReportsResource(FavouriteReportsRepository favouriteReportsRepository,UserJdbcService userJdbcService) {
        this.favouriteReportsRepository = favouriteReportsRepository;
        this.userJdbcService = userJdbcService;
    }

    /**	AUTHOUR @RK
     * POST  /favourite-reports : Create a new favouriteReports.
     *
     * @param favouriteReports the favouriteReports to create
     * @return the ResponseEntity with status 201 (Created) and with body the new favouriteReports, or with status 400 (Bad Request) if the favouriteReports has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/favourite-reports")
    @Timed
    public ResponseEntity<FavouriteReports> createFavouriteReports(HttpServletRequest request,@RequestBody HashMap reqObj) throws URISyntaxException {
        log.debug("REST request to save FavouriteReports : {}", reqObj);
        
        HashMap map=userJdbcService.getuserInfoFromToken(request);
      	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	Long userId=Long.parseLong(map.get("userId").toString());
    	FavouriteReports favouriteReports=new FavouriteReports();
    	favouriteReports.setReportId(reportsRepository.findByTenantIdAndIdForDisplay(tenantId,reqObj.get("reportId").toString()).getId());
    	favouriteReports.setTenantId(tenantId);
    	favouriteReports.setUserId(userId);
        FavouriteReports result = new FavouriteReports();
        result = (favouriteReportsRepository.findByReportIdAndTenantIdAndUserId(favouriteReports.getReportId(),tenantId,userId));
        if(result == null)
        {
	        if (favouriteReports.getId() != null) {
	            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new favouriteReports cannot already have an ID")).body(null);
	        }
	        result = favouriteReportsRepository.save(favouriteReports);
        }
        return ResponseEntity.created(new URI("/api/favourite-reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /favourite-reports : Updates an existing favouriteReports.
     *
     * @param favouriteReports the favouriteReports to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated favouriteReports,
     * or with status 400 (Bad Request) if the favouriteReports is not valid,
     * or with status 500 (Internal Server Error) if the favouriteReports couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
//    @PutMapping("/favourite-reports")
//    @Timed
//    public ResponseEntity<FavouriteReports> updateFavouriteReports(HttpServletRequest request,@RequestBody FavouriteReports favouriteReports) throws URISyntaxException {
//        log.debug("REST request to update FavouriteReports : {}", favouriteReports);
//        if (favouriteReports.getId() == null) {
//            return createFavouriteReports(request,favouriteReports);
//        }
//        FavouriteReports result = favouriteReportsRepository.save(favouriteReports);
//        return ResponseEntity.ok()
//            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, favouriteReports.getId().toString()))
//            .body(result);
//    }

    /**
     * GET  /favourite-reports : get all the favouriteReports.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of favouriteReports in body
     */
    @GetMapping("/favourite-reports")
    @Timed
    public List<FavouriteReports> getAllFavouriteReports() {
        log.debug("REST request to get all FavouriteReports");
        return favouriteReportsRepository.findAll();
    }

    /**
     * GET  /favourite-reports/:id : get the "id" favouriteReports.
     *
     * @param id the id of the favouriteReports to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the favouriteReports, or with status 404 (Not Found)
     */
    @GetMapping("/favourite-reports/{id}")
    @Timed
    public ResponseEntity<FavouriteReports> getFavouriteReports(@PathVariable Long id) {
        log.debug("REST request to get FavouriteReports : {}", id);
        FavouriteReports favouriteReports = favouriteReportsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(favouriteReports));
    }

    /**
     * DELETE  /favourite-reports/:id : delete the "id" favouriteReports.
     *
     * @param id the id of the favouriteReports to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/favourite-reports/{id}")
    @Timed
    public ResponseEntity<Void> deleteFavouriteReports(@PathVariable Long id) {
        log.debug("REST request to delete FavouriteReports : {}", id);
        
        favouriteReportsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    /**	AUTHOUR @RK
     * DELETE  /remove-favourite-report/?rptId&userId&tenantId : 
     *
     * @param id the id of the favouriteReports to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/remove-favourite-report")
    @Timed
    public ResponseEntity<Void> deleteFavReports(HttpServletRequest request,@RequestParam String rptId){//,@RequestParam Long userId,@RequestParam Long tenantId) {
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
      	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	Long userId=Long.parseLong(map.get("userId").toString());
    	Long id = (favouriteReportsRepository.findByReportIdAndTenantIdAndUserId(reportsRepository.findByTenantIdAndIdForDisplay(tenantId,rptId).getId(),tenantId,userId)).getId();
    	log.debug("REST request to delete FavouriteReports : {}", id);
    	if(id != null){
    		favouriteReportsRepository.delete(id);
    	}
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
}
