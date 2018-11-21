package com.nspl.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.google.common.util.concurrent.ListenableFuture;
import com.nspl.app.domain.AccountedSummary;
import com.nspl.app.service.AccountedSummaryService;
import com.nspl.app.service.WebSocketService;
import com.nspl.app.web.rest.dto.SampleMessageDTO;
import com.nspl.app.web.rest.util.HeaderUtil;
import com.nspl.app.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.util.SerializationUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.web.socket.sockjs.frame.Jackson2SockJsMessageCodec;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing AccountedSummary.
 */
@RestController
@RequestMapping("/api")
public class AccountedSummaryResource {

    private final Logger log = LoggerFactory.getLogger(AccountedSummaryResource.class);
    
    private static String URL = "ws://localhost:8082/websocket/tracker";
    private final static WebSocketHttpHeaders headers = new WebSocketHttpHeaders();

    private static final String ENTITY_NAME = "accountedSummary";
        
    private final AccountedSummaryService accountedSummaryService;
    
    @Inject
    WebSocketService webSocketService;

    public AccountedSummaryResource(AccountedSummaryService accountedSummaryService) {
        this.accountedSummaryService = accountedSummaryService;
    }

    /**
     * POST  /accounted-summaries : Create a new accountedSummary.
     *
     * @param accountedSummary the accountedSummary to create
     * @return the ResponseEntity with status 201 (Created) and with body the new accountedSummary, or with status 400 (Bad Request) if the accountedSummary has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/accounted-summaries")
    @Timed
    public ResponseEntity<AccountedSummary> createAccountedSummary(@RequestBody AccountedSummary accountedSummary) throws URISyntaxException {
        log.debug("REST request to save AccountedSummary : {}", accountedSummary);
        if (accountedSummary.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new accountedSummary cannot already have an ID")).body(null);
        }
        AccountedSummary result = accountedSummaryService.save(accountedSummary);
        return ResponseEntity.created(new URI("/api/accounted-summaries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /accounted-summaries : Updates an existing accountedSummary.
     *
     * @param accountedSummary the accountedSummary to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated accountedSummary,
     * or with status 400 (Bad Request) if the accountedSummary is not valid,
     * or with status 500 (Internal Server Error) if the accountedSummary couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/accounted-summaries")
    @Timed
    public ResponseEntity<AccountedSummary> updateAccountedSummary(@RequestBody AccountedSummary accountedSummary) throws URISyntaxException {
        log.debug("REST request to update AccountedSummary : {}", accountedSummary);
        if (accountedSummary.getId() == null) {
            return createAccountedSummary(accountedSummary);
        }
        AccountedSummary result = accountedSummaryService.save(accountedSummary);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, accountedSummary.getId().toString()))
            .body(result);
    }

    /**
     * GET  /accounted-summaries : get all the accountedSummaries.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of accountedSummaries in body
     */
    @GetMapping("/accounted-summaries")
    @Timed
    public ResponseEntity<List<AccountedSummary>> getAllAccountedSummaries(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of AccountedSummaries");
        Page<AccountedSummary> page = accountedSummaryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/accounted-summaries");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /accounted-summaries/:id : get the "id" accountedSummary.
     *
     * @param id the id of the accountedSummary to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the accountedSummary, or with status 404 (Not Found)
     */
    @GetMapping("/accounted-summaries/{id}")
    @Timed
    public ResponseEntity<AccountedSummary> getAccountedSummary(@PathVariable Long id) {
        log.debug("REST request to get AccountedSummary : {}", id);
        AccountedSummary accountedSummary = accountedSummaryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(accountedSummary));
    }

    /**
     * DELETE  /accounted-summaries/:id : delete the "id" accountedSummary.
     *
     * @param id the id of the accountedSummary to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/accounted-summaries/{id}")
    @Timed
    public ResponseEntity<Void> deleteAccountedSummary(@PathVariable Long id) {
        log.debug("REST request to delete AccountedSummary : {}", id);
        accountedSummaryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/accounted-summaries?query=:query : search for the accountedSummary corresponding
     * to the query.
     *
     * @param query the query of the accountedSummary search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/accounted-summaries")
    @Timed
    public ResponseEntity<List<AccountedSummary>> searchAccountedSummaries(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of AccountedSummaries for query {}", query);
        Page<AccountedSummary> page = accountedSummaryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/accounted-summaries");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /** POC's for sending messages through websocket**/
    
    @PostMapping("/webSocketConnection")
    @Timed
    public void webSocketConnection(@RequestBody SampleMessageDTO message) {
    	
    	Transport webSocketTransport = new WebSocketTransport(new StandardWebSocketClient());
    	List<Transport> transports = Collections.singletonList(webSocketTransport);
    	SockJsClient sockJsClient = new SockJsClient(transports);
    	
    	sockJsClient.setMessageCodec(new Jackson2SockJsMessageCodec());
    	WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
    	org.springframework.util.concurrent.ListenableFuture<StompSession> session=stompClient.connect(URL, headers, new MyStompSessionHandler());
    	try {
			StompSession liveSession=session.get();
			
			
	    //	byte[] data=SerializationUtils.serialize((Object) message);
	    	//Message msg=Message
	    	String msg="{\"image\": \"123abd\",\"message\": \"hello\",\"sendtime\": \"now\",\"username\": \"ricky\",\"who\": \"rahul\"}";
			liveSession.send("/websocket/hello", msg.getBytes());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
		
	}
    
    @PostMapping("/webSocketConnectionsss")
    @Timed
    public void webSocketConnectionsss(@RequestBody LinkedHashMap message) {
    	
    		webSocketService.webSocketConnectionService(message,"hello");
    	
    }
    
    


}
