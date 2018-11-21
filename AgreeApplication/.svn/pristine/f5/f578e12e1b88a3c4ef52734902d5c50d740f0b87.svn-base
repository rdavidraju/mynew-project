package com.nspl.app.web.rest;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.FxRates;
import com.nspl.app.domain.FxRatesDetails;
import com.nspl.app.repository.FxRatesDetailsRepository;
import com.nspl.app.repository.FxRatesRepository;
import com.nspl.app.service.UserJdbcService;
import com.nspl.app.web.rest.dto.FxRatesDetailsDTO;
import com.nspl.app.web.rest.util.HeaderUtil;
import com.nspl.app.web.rest.util.PaginationUtil;

/**
 * REST controller for managing FxRatesDetails.
 */
@RestController
@RequestMapping("/api")
public class FxRatesDetailsResource {

    private final Logger log = LoggerFactory.getLogger(FxRatesDetailsResource.class);

    private static final String ENTITY_NAME = "fxRatesDetails";
        
    private final FxRatesDetailsRepository fxRatesDetailsRepository;
    
    @Inject
    UserJdbcService userJdbcService;
    
    @Inject
    FxRatesRepository fxRatesRepository; 

    public FxRatesDetailsResource(FxRatesDetailsRepository fxRatesDetailsRepository) {
        this.fxRatesDetailsRepository = fxRatesDetailsRepository;
    }

    /**
     * POST  /fx-rates-details : Create a new fxRatesDetails.
     *
     * @param fxRatesDetails the fxRatesDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fxRatesDetails, or with status 400 (Bad Request) if the fxRatesDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/fx-rates-details")
    @Timed
    public ResponseEntity<FxRatesDetails> createFxRatesDetails(HttpServletRequest request, @RequestBody FxRatesDetails fxRatesDetails) throws URISyntaxException {
        log.debug("REST request to save FxRatesDetails : {}", fxRatesDetails);
        if (fxRatesDetails.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new fxRatesDetails cannot already have an ID")).body(null);
        }
        HashMap map=userJdbcService.getuserInfoFromToken(request);
      	Long userId=Long.parseLong(map.get("userId").toString());
        fxRatesDetails.setCreatedBy(userId);
        fxRatesDetails.setCreatedDate(ZonedDateTime.now());
        fxRatesDetails.setLastUpdatedBy(userId);
        fxRatesDetails.setLastUpdatedDate(ZonedDateTime.now());
        FxRatesDetails result = fxRatesDetailsRepository.save(fxRatesDetails);
        return ResponseEntity.created(new URI("/api/fx-rates-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /fx-rates-details : Updates an existing fxRatesDetails.
     *
     * @param fxRatesDetails the fxRatesDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fxRatesDetails,
     * or with status 400 (Bad Request) if the fxRatesDetails is not valid,
     * or with status 500 (Internal Server Error) if the fxRatesDetails couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/fx-rates-details")
    @Timed
    public FxRatesDetails updateFxRatesDetails(@RequestBody FxRatesDetailsDTO fxRatesDetailsDTO, HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to update FxRatesDetails : {}", fxRatesDetailsDTO);
        HashMap map=userJdbcService.getuserInfoFromToken(request);
      	Long userId=Long.parseLong(map.get("userId").toString());
      	Long tenantId=Long.parseLong(map.get("tenantId").toString());
      	HashMap finalMap = new HashMap();
  		
  		FxRates fr = fxRatesRepository.findByTenantIdAndIdForDisplay(tenantId, fxRatesDetailsDTO.getFxRateId());
      	if(fxRatesDetailsDTO.getId() == null)
      	{
      		FxRatesDetails frdNew = new FxRatesDetails();
      		frdNew.setFxRateId(fr.getId());
      		if(fxRatesDetailsDTO.getFromCurrency() != null)
      			frdNew.setFromCurrency(fxRatesDetailsDTO.getFromCurrency());
      		if(fxRatesDetailsDTO.getToCurrency() != null)
      			frdNew.setToCurrency(fxRatesDetailsDTO.getToCurrency());
      		if(fxRatesDetailsDTO.getFromDate() != null)
      			frdNew.setFromDate(fxRatesDetailsDTO.getFromDate());
      		if(fxRatesDetailsDTO.getToDate() != null)
      			frdNew.setToDate(fxRatesDetailsDTO.getToDate());
      		if(fxRatesDetailsDTO.getConversionRate() != null)
      			frdNew.setConversionRate(fxRatesDetailsDTO.getConversionRate());
      		if(fxRatesDetailsDTO.getInverseRate() != null)
      			frdNew.setInverseRate(fxRatesDetailsDTO.getInverseRate());
      		if(fxRatesDetailsDTO.getSource() != null)
      			frdNew.setSource(fxRatesDetailsDTO.getSource());
      		if(fxRatesDetailsDTO.getEnabledFlag() != null)
      			frdNew.setEnabledFlag(fxRatesDetailsDTO.getEnabledFlag());
      		frdNew.setCreatedBy(userId);
      		frdNew.setCreatedDate(ZonedDateTime.now());
      		frdNew.setLastUpdatedBy(userId);
      		frdNew.setLastUpdatedDate(ZonedDateTime.now());
      		FxRatesDetails frSave = fxRatesDetailsRepository.save(frdNew);
      		return frSave;
      	}
      	else
      	{
      		FxRatesDetails frd = fxRatesDetailsRepository.findOne(fxRatesDetailsDTO.getId());
      		// frd.setFxRateId(fr.getId());
      		if(fxRatesDetailsDTO.getFromCurrency() != null)
      			frd.setFromCurrency(fxRatesDetailsDTO.getFromCurrency());
      		if(fxRatesDetailsDTO.getToCurrency() != null)
      			frd.setToCurrency(fxRatesDetailsDTO.getToCurrency());
      		if(fxRatesDetailsDTO.getFromDate() != null)
      			frd.setFromDate(fxRatesDetailsDTO.getFromDate());
      		if(fxRatesDetailsDTO.getToDate() != null)
      			frd.setToDate(fxRatesDetailsDTO.getToDate());
      		if(fxRatesDetailsDTO.getConversionRate() != null)
      			frd.setConversionRate(fxRatesDetailsDTO.getConversionRate());
      		if(fxRatesDetailsDTO.getInverseRate() != null)
      			frd.setInverseRate(fxRatesDetailsDTO.getInverseRate());
      		if(fxRatesDetailsDTO.getEnabledFlag() != null)
      			frd.setEnabledFlag(fxRatesDetailsDTO.getEnabledFlag());
      		frd.setLastUpdatedBy(userId);
      		frd.setLastUpdatedDate(ZonedDateTime.now());
      		FxRatesDetails frdUpdate = fxRatesDetailsRepository.save(frd);
      		return frdUpdate;
      	}
      	
/*        fxRatesDetails.setFromDate(fxRatesDetails.getFromDate());
        fxRatesDetails.setToDate(fxRatesDetails.getToDate());
        if (fxRatesDetails.getId() == null) 
        {
            return createFxRatesDetails(request, fxRatesDetails);
        }
        fxRatesDetails.setLastUpdatedBy(userId);
        fxRatesDetails.setLastUpdatedDate(ZonedDateTime.now());
        FxRatesDetails result = fxRatesDetailsRepository.save(fxRatesDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, fxRatesDetails.getId().toString()))
            .body(result);*/
    }

    /**
     * GET  /fx-rates-details : get all the fxRatesDetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of fxRatesDetails in body
     */
    @GetMapping("/fx-rates-details")
    @Timed
    public ResponseEntity<List<FxRatesDetails>> getAllFxRatesDetails(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of FxRatesDetails");
        Page<FxRatesDetails> page = fxRatesDetailsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/fx-rates-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /fx-rates-details/:id : get the "id" fxRatesDetails.
     *
     * @param id the id of the fxRatesDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fxRatesDetails, or with status 404 (Not Found)
     */
    @GetMapping("/fx-rates-details/{id}")
    @Timed
    public ResponseEntity<FxRatesDetails> getFxRatesDetails(@PathVariable Long id) {
        log.debug("REST request to get FxRatesDetails : {}", id);
        FxRatesDetails fxRatesDetails = fxRatesDetailsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(fxRatesDetails));
    }

    /**
     * DELETE  /fx-rates-details/:id : delete the "id" fxRatesDetails.
     *
     * @param id the id of the fxRatesDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/fx-rates-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteFxRatesDetails(@PathVariable Long id) {
        log.debug("REST request to delete FxRatesDetails : {}", id);
        fxRatesDetailsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
