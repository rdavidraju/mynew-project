package com.nspl.app.web.rest.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Utility class for handling pagination.
 *
 * <p>
 * Pagination uses the same principles as the <a href="https://developer.github.com/v3/#pagination">Github API</a>,
 * and follow <a href="http://tools.ietf.org/html/rfc5988">RFC 5988 (Link header)</a>.
 */
public class PaginationUtil {

    public PaginationUtil() {
    }
    
    
    public static final int DEFAULT_OFFSET = 1;

    public static final int MIN_OFFSET = 1;

    public static final int DEFAULT_LIMIT = 20;

    public static final int MAX_LIMIT = 100;

    public static Pageable generatePageRequest(Integer offset, Integer limit) {
        if (offset == null || offset < MIN_OFFSET) {
            offset = DEFAULT_OFFSET;
        }
        if (limit == null || limit > MAX_LIMIT) {
            limit = DEFAULT_LIMIT;
        }
        return new PageRequest(offset - 1, limit);
    }
    
    public static Pageable generatePageRequest2(Integer offset, Integer limit) {
        /*if (offset == null || offset < MIN_OFFSET) {
            offset = limit;
        }
        if (limit == null) {
            limit = limit;
        }*/
        return new PageRequest(offset - 1, limit);
    }
    
    public static Pageable generatePageRequestWithSort(Integer offset, Integer limit, String column,String column1) {
        if (offset == null || offset < MIN_OFFSET) {
            offset = DEFAULT_OFFSET;
        }
        if (limit == null || limit > MAX_LIMIT) {
            limit = DEFAULT_LIMIT;
        }
        return new PageRequest(offset - 1, limit,Sort.Direction.DESC,column,column1);
    }
    
    public static Pageable generatePageRequest2WithSort(Integer offset, Integer limit, String column,String column1) {
        /*if (offset == null || offset < MIN_OFFSET) {
            offset = limit;
        }
        if (limit == null) {
            limit = limit;
        }*/
        return new PageRequest(offset - 1, limit,Sort.Direction.DESC,column,column1);
    }
    
   /* public static Pageable generatePageRequestWithSortColumnList(Integer offset, Integer limit,String orderDirection,List<String> sortColumns) {
    	String sortColstr=sortColumns.stream().map(Object::toString)
                .collect(Collectors.joining(","));
    	Direction sortDir=Sort.Direction.DESC;
    	if(orderDirection.equalsIgnoreCase("Ascending")){
    		 sortDir=Sort.Direction.ASC;
    	}
        return new PageRequest(offset - 1, limit,sortDir,sortColstr);
    }*/
    
    public static Pageable generatePageRequestWithSortColumn(Integer offset, Integer limit,String orderDirection,String sortColumn) {
    	/*String sortColstr=sortColumns.stream().map(Object::toString)
                .collect(Collectors.joining(","));*/
    	Direction sortDir=Sort.Direction.DESC;
    	if(orderDirection.equalsIgnoreCase("Ascending")){
    		 sortDir=Sort.Direction.ASC;
    	}
        return new PageRequest(offset - 1, limit,sortDir,sortColumn);
    }
    
    
    public static HttpHeaders generatePaginationHttpHeaderss(Page page, String baseUrl, Integer offset, Integer limit)
            throws URISyntaxException {

            if (offset == null || offset < MIN_OFFSET) {
                offset = DEFAULT_OFFSET;
            }
            if (limit == null || limit > MAX_LIMIT) {
                limit = DEFAULT_LIMIT;
            }
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Total-Count", "" + page.getTotalElements());
            String link = "";
            if (offset < page.getTotalPages()) {
                link = "<" + (new URI(baseUrl +"?page=" + (offset + 1) + "&per_page=" + limit)).toString()
                    + ">; rel=\"next\",";
            }
            if (offset > 1) {
                link += "<" + (new URI(baseUrl +"?page=" + (offset - 1) + "&per_page=" + limit)).toString()
                    + ">; rel=\"prev\",";
            }
            link += "<" + (new URI(baseUrl +"?page=" + page.getTotalPages() + "&per_page=" + limit)).toString()
                + ">; rel=\"last\"," +
                "<" + (new URI(baseUrl +"?page=" + 1 + "&per_page=" + limit)).toString()
                + ">; rel=\"first\"";
            headers.add(HttpHeaders.LINK, link);
            return headers;
        }
        
    
    public static HttpHeaders generatePaginationHttpHeaders2(Page page, String baseUrl, Integer offset, Integer limit)
            throws URISyntaxException {

            /*if (offset == null || offset < MIN_OFFSET) {
                offset = limit;
            }
            if (limit == null) {
                limit = limit;
            }*/
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Total-Count", "" + page.getTotalElements());
            String link = "";
            if (offset < page.getTotalPages()) {
                link = "<" + (new URI(baseUrl +"?page=" + (offset + 1) + "&per_page=" + limit)).toString()
                    + ">; rel=\"next\",";
            }
            if (offset > 1) {
                link += "<" + (new URI(baseUrl +"?page=" + (offset - 1) + "&per_page=" + limit)).toString()
                    + ">; rel=\"prev\",";
            }
            link += "<" + (new URI(baseUrl +"?page=" + page.getTotalPages() + "&per_page=" + limit)).toString()
                + ">; rel=\"last\"," +
                "<" + (new URI(baseUrl +"?page=" + 1 + "&per_page=" + limit)).toString()
                + ">; rel=\"first\"";
            headers.add(HttpHeaders.LINK, link);
            return headers;
        }
    

    public static HttpHeaders generatePaginationHttpHeaders(Page page, String baseUrl) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", "" + Long.toString(page.getTotalElements()));
        String link = "";
        if ((page.getNumber() + 1) < page.getTotalPages()) {
            link = "<" + generateUri(baseUrl, page.getNumber() + 1, page.getSize()) + ">; rel=\"next\",";
        }
        // prev link
        if ((page.getNumber()) > 0) {
            link += "<" + generateUri(baseUrl, page.getNumber() - 1, page.getSize()) + ">; rel=\"prev\",";
        }
        // last and first link
        int lastPage = 0;
        if (page.getTotalPages() > 0) {
            lastPage = page.getTotalPages() - 1;
        }
        link += "<" + generateUri(baseUrl, lastPage, page.getSize()) + ">; rel=\"last\",";
        link += "<" + generateUri(baseUrl, 0, page.getSize()) + ">; rel=\"first\"";
        headers.add(HttpHeaders.LINK, link);
        return headers;
    }

    private static String generateUri(String baseUrl, int page, int size) {
        return UriComponentsBuilder.fromUriString(baseUrl).queryParam("page", page).queryParam("size", size).toUriString();
    }

    public static HttpHeaders generateSearchPaginationHttpHeaders(String query, Page page, String baseUrl) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", "" + page.getTotalElements());
        String link = "";
        if ((page.getNumber() + 1) < page.getTotalPages()) {
            link = "<" + generateUri(baseUrl, page.getNumber() + 1, page.getSize()) + "&query=" + query + ">; rel=\"next\",";
        }
        // prev link
        if ((page.getNumber()) > 0) {
            link += "<" + generateUri(baseUrl, page.getNumber() - 1, page.getSize()) + "&query=" + query + ">; rel=\"prev\",";
        }
        // last and first link
        int lastPage = 0;
        if (page.getTotalPages() > 0) {
            lastPage = page.getTotalPages() - 1;
        }
        link += "<" + generateUri(baseUrl, lastPage, page.getSize()) + "&query=" + query + ">; rel=\"last\",";
        link += "<" + generateUri(baseUrl, 0, page.getSize()) + "&query=" + query + ">; rel=\"first\"";
        headers.add(HttpHeaders.LINK, link);
        return headers;
    }
}
