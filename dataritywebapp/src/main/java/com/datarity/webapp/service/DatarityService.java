package com.datarity.webapp.service;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.datarity.webapp.model.DatarityScanRequest;
import com.datarity.webapp.model.DatarityScanResponse;

@Controller
public class DatarityService {
	
	private static final Logger logger = Logger.getLogger(DatarityService.class);
	
	@RequestMapping(value = "/scan", method = RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String scan(@RequestBody DatarityScanRequest drReq) {
		logger.debug("Request to Scan");
		drReq.setId(1);
		DatarityScanResponse drRes = new DatarityScanResponse();
		drRes.setId(1);
		return getJson(drRes);
	}
	
	@RequestMapping(value = "/scanhistory", method = RequestMethod.GET)
	public @ResponseBody String scanHistory() {
		logger.debug("Request to Scan History");
		DatarityScanResponse drRes = new DatarityScanResponse();
		drRes.setId(2);
		return getJson(drRes);
	}

	@RequestMapping(value = "/scandetail/{id}", method = RequestMethod.GET)
	public @ResponseBody String scanDetail(@PathVariable("id") String id) {
		logger.debug("Request to Scan Detail id : " + id);
		DatarityScanResponse drRes = new DatarityScanResponse();
		drRes.setId(3);
		return getJson(drRes);
	}
	
	@RequestMapping(value = "/mask/{id}", method = RequestMethod.GET)
	public @ResponseBody String mask(@PathVariable("id") String id) {
		logger.debug("Request to Mask id: " + id);
		DatarityScanResponse drRes = new DatarityScanResponse();
		drRes.setId(1);
		return getJson(drRes);
	}
	
	private String getJson(Object obj) {
		ObjectWriter objWriter = new ObjectMapper().writer();
		try {
			return objWriter.writeValueAsString(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "{status:\"-1\", error:\"Exception\"}";
	}
}
