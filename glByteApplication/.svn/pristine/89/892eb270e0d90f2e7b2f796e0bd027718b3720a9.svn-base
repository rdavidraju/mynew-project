package com.nspl.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.JournalApprovalInfo;
import com.nspl.app.repository.JournalApprovalInfoRepository;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

/**
 * REST controller for managing JournalApprovalInfo.
 */
@RestController
@RequestMapping("/api")
public class JournalApprovalInfoResource {

	private final Logger log = LoggerFactory.getLogger(JournalApprovalInfoResource.class);

	private static final String ENTITY_NAME = "journalApprovalInfo";

	private final JournalApprovalInfoRepository journalApprovalInfoRepository;

	public JournalApprovalInfoResource(JournalApprovalInfoRepository journalApprovalInfoRepository) {
		this.journalApprovalInfoRepository = journalApprovalInfoRepository;
	}

	/**
	 * POST  /journal-approval-infos : Create a new journalApprovalInfo.
	 *
	 * @param journalApprovalInfo the journalApprovalInfo to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new journalApprovalInfo, or with status 400 (Bad Request) if the journalApprovalInfo has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/journal-approval-infos")
	@Timed
	public ResponseEntity<JournalApprovalInfo> createJournalApprovalInfo(@RequestBody JournalApprovalInfo journalApprovalInfo) throws URISyntaxException {
		log.debug("REST request to save JournalApprovalInfo : {}", journalApprovalInfo);
		if (journalApprovalInfo.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new journalApprovalInfo cannot already have an ID")).body(null);
		}
		JournalApprovalInfo result = journalApprovalInfoRepository.save(journalApprovalInfo);
		return ResponseEntity.created(new URI("/api/journal-approval-infos/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /journal-approval-infos : Updates an existing journalApprovalInfo.
	 *
	 * @param journalApprovalInfo the journalApprovalInfo to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated journalApprovalInfo,
	 * or with status 400 (Bad Request) if the journalApprovalInfo is not valid,
	 * or with status 500 (Internal Server Error) if the journalApprovalInfo couldn't be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/journal-approval-infos")
	@Timed
	public ResponseEntity<JournalApprovalInfo> updateJournalApprovalInfo(@RequestBody JournalApprovalInfo journalApprovalInfo) throws URISyntaxException {
		log.debug("REST request to update JournalApprovalInfo : {}", journalApprovalInfo);
		if (journalApprovalInfo.getId() == null) {
			return createJournalApprovalInfo(journalApprovalInfo);
		}
		JournalApprovalInfo result = journalApprovalInfoRepository.save(journalApprovalInfo);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, journalApprovalInfo.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /journal-approval-infos : get all the journalApprovalInfos.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of journalApprovalInfos in body
	 */
	@GetMapping("/journal-approval-infos")
	@Timed
	public ResponseEntity<List<JournalApprovalInfo>> getAllJournalApprovalInfos(@ApiParam Pageable pageable) {
		log.debug("REST request to get a page of JournalApprovalInfos");
		Page<JournalApprovalInfo> page = journalApprovalInfoRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/journal-approval-infos");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET  /journal-approval-infos/:id : get the "id" journalApprovalInfo.
	 *
	 * @param id the id of the journalApprovalInfo to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the journalApprovalInfo, or with status 404 (Not Found)
	 */
	@GetMapping("/journal-approval-infos/{id}")
	@Timed
	public ResponseEntity<JournalApprovalInfo> getJournalApprovalInfo(@PathVariable Long id) {
		log.debug("REST request to get JournalApprovalInfo : {}", id);
		JournalApprovalInfo journalApprovalInfo = journalApprovalInfoRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(journalApprovalInfo));
	}

	/**
	 * DELETE  /journal-approval-infos/:id : delete the "id" journalApprovalInfo.
	 *
	 * @param id the id of the journalApprovalInfo to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/journal-approval-infos/{id}")
	@Timed
	public ResponseEntity<Void> deleteJournalApprovalInfo(@PathVariable Long id) {
		log.debug("REST request to delete JournalApprovalInfo : {}", id);
		journalApprovalInfoRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}




	@Transactional
	@RequestMapping(value = "/insertingRecoirdsForJournalApprovalWhileInitiating",
	method = RequestMethod.POST,
	produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public void insertingRecoirdsForJournalApprovalWhileInitiating( @RequestParam String jeHeadersFinDataListSz,@RequestParam String curApp,
			@RequestParam String lastRecId,@RequestParam String ruleId,@RequestParam String rulGrpId) throws SQLException, ClassNotFoundException
	{
		log.info("in API insertingRecoirdsForJournalApprovalWhileInitiating for jeHeadersFinDataListSz and curApp:"+curApp+" and lastRecId :"+lastRecId );

		List<Long> jeHeaderIdList = Arrays.asList(jeHeadersFinDataListSz.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
		log.info("jeHeaderIdList :"+jeHeaderIdList.size());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String currentDateTime = dateFormat.format(date);



		//  SimpleDateFormat sdFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String apprRefOne=curApp+"|"+lastRecId+"|InProcess|"+currentDateTime;
		List<JournalApprovalInfo> jeApprovalInfo=new ArrayList<JournalApprovalInfo>();

		for(Long jeHeaderId:jeHeaderIdList)
		{
			JournalApprovalInfo jeAppInfo=new JournalApprovalInfo();
			jeAppInfo.setJeHeaderId(jeHeaderId);
			jeAppInfo.setApprovalBatchId(Long.valueOf(lastRecId));
			jeAppInfo.setApprovalGroupId(Long.valueOf(rulGrpId));
			jeAppInfo.setApprovalRuleId(Long.valueOf(ruleId));
			jeAppInfo.setApprovalInitiationDate(ZonedDateTime.now());
			jeAppInfo.setApprRef01(apprRefOne);
			jeAppInfo.setFinalActionDate(ZonedDateTime.now());
			jeAppInfo.setFinalStatus("IN_PROCESS");
			jeApprovalInfo.add(jeAppInfo);
		}
		jeApprovalInfo=journalApprovalInfoRepository.save(jeApprovalInfo);

		log.info("************* end of inserting journal approval data :"+ZonedDateTime.now());

	}



}
