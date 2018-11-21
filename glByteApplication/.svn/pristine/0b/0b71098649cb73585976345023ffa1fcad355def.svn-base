package com.nspl.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.ApprovalRuleAssignment;

import com.nspl.app.repository.ApprovalRuleAssignmentRepository;
import com.nspl.app.web.rest.util.HeaderUtil;
import com.nspl.app.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ApprovalRuleAssignment.
 */
@RestController
@RequestMapping("/api")
public class ApprovalRuleAssignmentResource {

    private final Logger log = LoggerFactory.getLogger(ApprovalRuleAssignmentResource.class);

    private static final String ENTITY_NAME = "approvalRuleAssignment";
        
    private final ApprovalRuleAssignmentRepository approvalRuleAssignmentRepository;

    public ApprovalRuleAssignmentResource(ApprovalRuleAssignmentRepository approvalRuleAssignmentRepository) {
        this.approvalRuleAssignmentRepository = approvalRuleAssignmentRepository;
    }

    /**
     * POST  /approval-rule-assignments : Create a new approvalRuleAssignment.
     *
     * @param approvalRuleAssignment the approvalRuleAssignment to create
     * @return the ResponseEntity with status 201 (Created) and with body the new approvalRuleAssignment, or with status 400 (Bad Request) if the approvalRuleAssignment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/approval-rule-assignments")
    @Timed
    public ResponseEntity<ApprovalRuleAssignment> createApprovalRuleAssignment(@RequestBody ApprovalRuleAssignment approvalRuleAssignment) throws URISyntaxException {
        log.debug("REST request to save ApprovalRuleAssignment : {}", approvalRuleAssignment);
        if (approvalRuleAssignment.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new approvalRuleAssignment cannot already have an ID")).body(null);
        }
        ApprovalRuleAssignment result = approvalRuleAssignmentRepository.save(approvalRuleAssignment);
        return ResponseEntity.created(new URI("/api/approval-rule-assignments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /approval-rule-assignments : Updates an existing approvalRuleAssignment.
     *
     * @param approvalRuleAssignment the approvalRuleAssignment to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated approvalRuleAssignment,
     * or with status 400 (Bad Request) if the approvalRuleAssignment is not valid,
     * or with status 500 (Internal Server Error) if the approvalRuleAssignment couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/approval-rule-assignments")
    @Timed
    public ResponseEntity<ApprovalRuleAssignment> updateApprovalRuleAssignment(@RequestBody ApprovalRuleAssignment approvalRuleAssignment) throws URISyntaxException {
        log.debug("REST request to update ApprovalRuleAssignment : {}", approvalRuleAssignment);
        if (approvalRuleAssignment.getId() == null) {
            return createApprovalRuleAssignment(approvalRuleAssignment);
        }
        ApprovalRuleAssignment result = approvalRuleAssignmentRepository.save(approvalRuleAssignment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, approvalRuleAssignment.getId().toString()))
            .body(result);
    }

    /**
     * GET  /approval-rule-assignments : get all the approvalRuleAssignments.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of approvalRuleAssignments in body
     */
    @GetMapping("/approval-rule-assignments")
    @Timed
    public ResponseEntity<List<ApprovalRuleAssignment>> getAllApprovalRuleAssignments(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of ApprovalRuleAssignments");
        Page<ApprovalRuleAssignment> page = approvalRuleAssignmentRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/approval-rule-assignments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /approval-rule-assignments/:id : get the "id" approvalRuleAssignment.
     *
     * @param id the id of the approvalRuleAssignment to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the approvalRuleAssignment, or with status 404 (Not Found)
     */
    @GetMapping("/approval-rule-assignments/{id}")
    @Timed
    public ResponseEntity<ApprovalRuleAssignment> getApprovalRuleAssignment(@PathVariable Long id) {
        log.debug("REST request to get ApprovalRuleAssignment : {}", id);
        ApprovalRuleAssignment approvalRuleAssignment = approvalRuleAssignmentRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(approvalRuleAssignment));
    }

    /**
     * DELETE  /approval-rule-assignments/:id : delete the "id" approvalRuleAssignment.
     *
     * @param id the id of the approvalRuleAssignment to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/approval-rule-assignments/{id}")
    @Timed
    public ResponseEntity<Void> deleteApprovalRuleAssignment(@PathVariable Long id) {
        log.debug("REST request to delete ApprovalRuleAssignment : {}", id);
        approvalRuleAssignmentRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
