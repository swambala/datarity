package com.datarity.webapp.model;

import java.util.HashMap;
import java.util.Map;

public class DatarityScanResponse {

	private int id;
	
	private long totalSecurityHoles;
	
	private long totalCreditCardNum;
	private long totalPhoneNum;
	private long totalEmailId;
	private long totalSsnNum;
	
	private Map<String, Long> fileNameToSecurityHoles = new HashMap<String, Long>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	} 
}
