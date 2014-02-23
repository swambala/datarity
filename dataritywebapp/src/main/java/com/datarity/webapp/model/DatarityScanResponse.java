package com.datarity.webapp.model;

import java.util.ArrayList;
import java.util.List;

public class DatarityScanResponse {

	private int id;
	private List<DatarityScanResult> results = new ArrayList<DatarityScanResult>();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public List<DatarityScanResult> getResults() {
		return results;
	}
	public void setResults(List<DatarityScanResult> results) {
		this.results = results;
	}
}
