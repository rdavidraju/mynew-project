package com.nspl.app.web.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

@RestController
@RequestMapping("/api")
public class UtilsResource {

	private final Logger log = LoggerFactory.getLogger(UtilsResource.class);
	
		@GetMapping("/getTimezones")
	    @Timed
	    public ResponseEntity<List<String>> fetchTimeZones()
	    {
			List<String> timeZonesList = new ArrayList<String>();
			
			String[] ids = TimeZone.getAvailableIDs();
			
			for (String id : ids) {
				timeZonesList.add(displayTimeZone(TimeZone.getTimeZone(id)));
			}
			
			log.info("Total TimeZone ID " + ids.length);
			
			return  new ResponseEntity<List<String>>(timeZonesList, HttpStatus.OK);
	    }
		
		private static String displayTimeZone(TimeZone tz) {

			long hours = TimeUnit.MILLISECONDS.toHours(tz.getRawOffset());
			long minutes = TimeUnit.MILLISECONDS.toMinutes(tz.getRawOffset()) 
	                                  - TimeUnit.HOURS.toMinutes(hours);
			// avoid -4:-30 issue
			minutes = Math.abs(minutes);

			String result = "";

			result = String.format("%s (GMT+%d:%02d)", tz.getID(), hours, minutes);

			return result;

		}
		
		@GetMapping("/getLanguagesList")
	    @Timed
	    public HashMap fetchLanguagesList()
	    {
			HashMap finalMap = new HashMap();
	    	
			List<HashMap> lang = new ArrayList<HashMap>();
			String[] languages = Locale.getISOLanguages();
			for (int i = 0; i < languages.length; i++){
			    Locale loc = new Locale(languages[i]);
			    
			    HashMap rec = new HashMap();
			    
			    rec.put("displayValue", loc.getDisplayLanguage());
			    rec.put("keyValue", languages[i]);
			    
			    lang.add(rec);
			    
			}
			finalMap.put("languages", lang);
			return finalMap;
	    }
		
}

