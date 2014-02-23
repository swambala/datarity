package com.datarity.webapp.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.datarity.webapp.model.DatarityScanRequest;
import com.datarity.webapp.model.DatarityScanResponse;
import com.datarity.webapp.model.DatarityScanResult;

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

		DatarityScanResult scanResult = getDRScanResult("sampledata.txt");
		List<DatarityScanResult> results = new ArrayList<DatarityScanResult>();
		results.add(scanResult);
		drRes.setResults(results );
		
		return getJson(drRes);
	}

	private DatarityScanResult getDRScanResult(String filePath) {
		DatarityScanResult scanResult = new DatarityScanResult();
		try {
			List<String> readLines = IOUtils.readLines(DatarityService.class.getResourceAsStream(filePath));
			
			String[] words;
			String firstWord;
			long count;
			for (String line : readLines) {
				words = line.split("\t");
				if (words.length != 2) {
					continue;
				}
				firstWord = words[0];
				count = Long.parseLong(words[1]);
				
				if (firstWord.equals("CREDIT_CARD")) {
					scanResult.setTotalCreditCardNum(count);
				} else if (firstWord.equals("EMAIL")) {
					scanResult.setTotalEmailId(count);
				} else if (firstWord.equals("PHONE")) {
					scanResult.setTotalPhoneNum(count);
				} else if (firstWord.equals("SSN")) {
					scanResult.setTotalSsnNum(count);
				} else if (firstWord.contains("TOTAL_DATARITY_COUNT")) {
					scanResult.setTotalSecurityEscape(count);
				} else if (firstWord.startsWith("SCANED_FILENAME_")) {
					firstWord = firstWord.substring("SCANED_FILENAME_".length(), firstWord.length());
					scanResult.getFileNameToSecurityEscape().put(firstWord, count);
				} else if (firstWord.endsWith("_CREDITCARD")) {
					firstWord = addFileIndividualResults(scanResult, firstWord,count,"_CREDITCARD");
				} else if (firstWord.endsWith("_EMAIL")) {
					firstWord = addFileIndividualResults(scanResult, firstWord,count,"_EMAIL");
				} else if (firstWord.endsWith("_PHONE")) {
					firstWord = addFileIndividualResults(scanResult, firstWord,count,"_PHONE");
				} else if (firstWord.endsWith("_SSN")) {
					firstWord = addFileIndividualResults(scanResult, firstWord,count,"_SSN");
				}
			}
		} catch (Exception e) {
			logger.error("ISSUE: ", e);
		}
		return scanResult;
	}
	private String addFileIndividualResults(DatarityScanResult scanResult,
			String firstWord, long count, String type) {
		firstWord = firstWord.substring(0, firstWord.length() - type.length());
		Map<String, Long> map = scanResult.getFileNameToSecurityTypeToEscape().get(firstWord);
		if (map==null) {
			map = new HashMap<String, Long>();
			scanResult.getFileNameToSecurityTypeToEscape().put(firstWord, map);
		}
		map.put(type, count);
		return firstWord;
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
