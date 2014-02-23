package com.datarity.webapp.model;

public class DatarityScanRequest {

	private int id;
	private boolean isScan;
	private boolean isMask;
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
	public boolean isScan() {
		return isScan;
	}
	public void setScan(boolean isScan) {
		this.isScan = isScan;
	}
	public boolean isMask() {
		return isMask;
	}
	public void setMask(boolean isMask) {
		this.isMask = isMask;
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
