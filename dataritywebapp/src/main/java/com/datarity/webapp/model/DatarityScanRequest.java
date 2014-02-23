package com.datarity.webapp.model;

public class DatarityScanRequest {

	private int id;
	private String delimiter = ",";//default ,
	private String[] inputDirs;
	private String outputDir;
	
	private String maskForScanJobId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDelimiter() {
		return delimiter;
	}
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}
	public String[] getInputDirs() {
		return inputDirs;
	}
	public void setInputDirs(String[] inputDirs) {
		this.inputDirs = inputDirs;
	}
	public String getOutputDir() {
		return outputDir;
	}
	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}
	public String getMaskForScanJobId() {
		return maskForScanJobId;
	}
	public void setMaskForScanJobId(String maskForScanJobId) {
		this.maskForScanJobId = maskForScanJobId;
	}
}
