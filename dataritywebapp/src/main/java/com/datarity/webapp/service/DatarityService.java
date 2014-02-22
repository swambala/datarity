package com.datarity.webapp.service;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.datarity.webapp.model.DatarityScanResponse;

@Controller
public class DatarityService {
	
	private static final Logger logger = Logger.getLogger(DatarityService.class);
	
	@RequestMapping(value = "/scan", method = RequestMethod.GET)
	public @ResponseBody String scan() {
		logger.debug("Request to Scan");
		DatarityScanResponse drScanResponse = new DatarityScanResponse();
		drScanResponse.setId(1);
		return getJson(drScanResponse);
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
