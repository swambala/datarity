package com.datarity.webapp.processor;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

public class CommandExecutor {

	public static final String CMD_SCAN = "hadoop jar /b/exe/dataritymapreducer-0.0.1-SNAPSHOT.jar com.datarity.dataritymapreducer.RegularExpressionMapper /user/datarity/";
	///Users/barath/b/barath/hackathon/s/outputfiles/1393115319919/part-r-00000 /Users/barath/b/barath/hackathon/s/outputfiles/maskjob , /Users/barath/b/barath/hackathon/s/outputfiles/mask
	public static final String CMD_MASK = "hadoop jar /b/exe/dataritymapreducer-0.0.1-SNAPSHOT.jar com.datarity.dataritymapreducer.MaskerMapper /user/datarityoutputs/KEYKEY /user/dataritymaskoutput/ , /user/dataritymaskeddata/";
	public static final String CMD_CPY_LOCAL = "hadoop fs -copyToLocal /user/datarityoutputs/* /b/d/";
	
	private static final Logger logger = Logger.getLogger(CommandExecutor.class);
	
	public String executeCommand(String command) {

		logger.info("Command to execute: " + command);
		StringBuffer output = new StringBuffer();

		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));

			String line = "";
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}

		} catch (Exception e) {
			logger.error("Exceptin in cmd execution : ", e);
		}

		return output.toString();

	}
}