package com.datarity.webapp.model;

import java.util.HashMap;
import java.util.Map;

public class DatarityScanResult {

	private int id;
	
	private long totalSecurityEscape;
	
	private long totalCreditCardNum;
	private long totalPhoneNum;
	private long totalEmailId;
	private long totalSsnNum;
	
	private Map<String, Long> fileNameToSecurityEscape = new HashMap<String, Long>();
	private Map<String, Map<String, Long>> fileNameToSecurityTypeToEscape = new HashMap<String, Map<String,Long>>();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getTotalSecurityEscape() {
		return totalSecurityEscape;
	}
	public void setTotalSecurityEscape(long totalSecurityEscape) {
		this.totalSecurityEscape = totalSecurityEscape;
	}
	public long getTotalCreditCardNum() {
		return totalCreditCardNum;
	}
	public void setTotalCreditCardNum(long totalCreditCardNum) {
		this.totalCreditCardNum = totalCreditCardNum;
	}
	public long getTotalPhoneNum() {
		return totalPhoneNum;
	}
	public void setTotalPhoneNum(long totalPhoneNum) {
		this.totalPhoneNum = totalPhoneNum;
	}
	public long getTotalEmailId() {
		return totalEmailId;
	}
	public void setTotalEmailId(long totalEmailId) {
		this.totalEmailId = totalEmailId;
	}
	public long getTotalSsnNum() {
		return totalSsnNum;
	}
	public void setTotalSsnNum(long totalSsnNum) {
		this.totalSsnNum = totalSsnNum;
	}
	public Map<String, Long> getFileNameToSecurityEscape() {
		return fileNameToSecurityEscape;
	}
	public void setFileNameToSecurityEscape(
			Map<String, Long> fileNameToSecurityEscape) {
		this.fileNameToSecurityEscape = fileNameToSecurityEscape;
	}
	public Map<String, Map<String, Long>> getFileNameToSecurityTypeToEscape() {
		return fileNameToSecurityTypeToEscape;
	}
	public void setFileNameToSecurityTypeToEscape(
			Map<String, Map<String, Long>> fileNameToSecurityTypeToEscape) {
		this.fileNameToSecurityTypeToEscape = fileNameToSecurityTypeToEscape;
	}
}
