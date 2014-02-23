package com.datarity.webapp.service;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.datarity.webapp.model.DatarityScanResponse;
import com.datarity.webapp.model.DatarityScanResult;
import com.datarity.webapp.processor.CommandExecutor;

@Controller
public class DatarityService {
	
	private static final Logger logger = Logger.getLogger(DatarityService.class);
	private static final String DEFAULT_PATH_OUTPUT = "/b/d/";
	
	@RequestMapping(value = "/scan", method = RequestMethod.GET)
	public @ResponseBody String scan() {
		logger.debug("Request to Scan");
		
//		if(false) {
//			InputStream fs = DatarityService.class.getResourceAsStream("sampledata.txt");
//			try {
//				List<String> readLines = IOUtils.readLines(fs);
//				StringBuffer sb = new StringBuffer();
//				for (String line : readLines) {
//					sb.append(line+"\n");
//					
//				}
//				return sb.toString();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
		DatarityScanResponse drRes = new DatarityScanResponse();
		drRes.setId(1);
		CommandExecutor executor = new CommandExecutor();
		executor.executeCommand(CommandExecutor.CMD_SCAN);
		executor.executeCommand(CommandExecutor.CMD_CPY_LOCAL);
		
		String[] allFolderNames = getAllFolderNames(DEFAULT_PATH_OUTPUT);
		DatarityScanResult drScanResult;
		
//		long previousId = -1;
		
		for (int i = 0; i < allFolderNames.length; i++) {
			drScanResult = getDRScanResult(DEFAULT_PATH_OUTPUT + allFolderNames[i]);
			if (drScanResult == null) {
				continue;
			}
			drScanResult.setId(allFolderNames[i]);
//			
//			if (previousId < Long.parseLong(drScanResult.getId())) {
//				drRes.getResults().clear();
//				drRes.getResults().add(drScanResult);	
//			}
		}
		
		return getJson(drRes);
	}
	
	@RequestMapping(value = "/scanhistory", method = RequestMethod.GET)
	public @ResponseBody String scanHistory() {
		logger.debug("Request to Scan History");
		DatarityScanResponse drRes = new DatarityScanResponse();
		drRes.setId(2);
		
		String[] allFolderNames = getAllFolderNames(DEFAULT_PATH_OUTPUT);
		DatarityScanResult drScanResult;
		long previousId = -1;
		for (int i = 0; i < allFolderNames.length; i++) {
			drScanResult = getDRScanResult(DEFAULT_PATH_OUTPUT + allFolderNames[i]);
			if (drScanResult == null) {
				continue;
			}
			drScanResult.setId(allFolderNames[i]);
			
			if (previousId < Long.parseLong(drScanResult.getId())) {
				drRes.getResults().clear();
				drRes.getResults().add(drScanResult);	
				previousId = Long.parseLong(drScanResult.getId());
			}
		}
		
		return getJson(drRes);
	}

	@RequestMapping(value = "/scandetail/{id}", method = RequestMethod.GET)
	public @ResponseBody String scanDetail(@PathVariable("id") String id) {
		logger.debug("Request to Scan Detail id : " + id);
		
		DatarityScanResponse drRes = new DatarityScanResponse();
//		drRes.setId(0);

		DatarityScanResult scanResult = getDRScanResult(DEFAULT_PATH_OUTPUT + id);
		scanResult.setId(id);
		List<DatarityScanResult> results = new ArrayList<DatarityScanResult>();
		results.add(scanResult);
		drRes.setResults(results );
		
		return getJson(drRes);
	}

	private DatarityScanResult getDRScanResult(String filePath) {
		DatarityScanResult scanResult = new DatarityScanResult();
		try {
//			List<String> readLines = IOUtils.readLines(DatarityService.class.getResourceAsStream(filePath));
			List<String> readLines = getFileLines(filePath);
			
			if (readLines.isEmpty()) {
				return null;
			}
			
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

		CommandExecutor executor = new CommandExecutor();
		String maskFolder = CommandExecutor.CMD_MASK.replaceAll("KEYKEY", id);
		logger.debug("Request to Mask Folder: " + maskFolder);
		executor.executeCommand(maskFolder);
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
	
	private static String[] getAllFolderNames(String dirPath) {
		File file = new File(dirPath);
		String[] directories = file.list(new FilenameFilter() {
		  @Override
		  public boolean accept(File current, String name) {
		    return new File(current, name).isDirectory();
		  }
		});
		return directories;
	}
	
	private static List<String> getFileLines(String dirPath) {
		try {
			String fileName = dirPath + "/" + "part-r-00000";
			logger.info("File Name: " + fileName);
			return FileUtils.readLines(new File(fileName));
		} catch (IOException e) {
//			e.printStackTrace();
			logger.info("NO File found: " + dirPath);
		}
		return new ArrayList<String>();
	}
	
	public static void main(String[] args) throws Exception {
		DatarityService self = new DatarityService();
		System.out.println(self.scanHistory());
	}
	/*private static void getFile() throws Exception {
		Configuration configuration = new Configuration();
//		configuration.set("fs.defaultFS", "hdfs://192.168.255.182:54310/");
		configuration.set("fs.defaultFS", "hdfs://sandbox.hortonworks.com:8020");
//		configuration.set("hadoop.job.ugi", "hbase");
		
		FileSystem fs = FileSystem.get(configuration);

		Path ptRead=new Path("user/datarity/0.txt");
        FSDataInputStream open = fs.open(ptRead);
		InputStreamReader inputStreamReader = new InputStreamReader(open);
		BufferedReader br=new BufferedReader(inputStreamReader);

        String line = br.readLine();
        
        int lineCount = 0;
        while (line != null){
			line=br.readLine();
			System.out.println(line);
			if ( ++lineCount == 10) {
				break;
			}
        }
	}
	
	private static void writefi() {
		try {
	        Configuration conf = new Configuration();
//	        conf.set("fs.defaultFS", "hdfs://localhost:54310/user/hadoop/");
//	        conf.set("fs.defaultFS", "hdfs://192.168.255.182:54310/user");
//	        conf.set("fs.defaultFS", "hdfs://sandbox.hortonworks.com:8020");
	        conf.set("fs.defaultFS", "hdfs://192.168.255.182:8020");
	        FileSystem fs = FileSystem.get(conf);
//	        FileStatus[] status = fs.listStatus(new Path("/users/barath/b/barath/hackathon/s/dump"));
//	        for(int i=0;i<status.length;i++){
//	            System.out.println(status[i].getPath());
//	            fs.copyFromLocalFile(false, status[i].getPath(), new Path("/datarity/"));
//	        }
	        
//	        fs.createNewFile(new Path("/users/barath/b/barath/hackathon/s/dump/abc.txt"));
	        fs.createNewFile(new Path("/user/datarity/abc.txt"));
	        fs.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		System.out.println("done");
	}*/
	
	
}
