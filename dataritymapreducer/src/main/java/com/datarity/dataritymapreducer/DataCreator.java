package com.datarity.dataritymapreducer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class DataCreator {

	private long cc1 = 4024007126047030L;
	
	private long ssn = 100000000L;
	
	private long phone = 4000000000L;
	
	final String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";

	final java.util.Random rand = new java.util.Random();

	// consider using a Map<String,Boolean> to say whether the identifier is being used or not 
	final Set<String> identifiers = new HashSet<String>();

	public DataCreator() {

	}

	public void createData() throws Exception {
		for(int j=0;j<5;j++){
			File f = new File("/Users/bytes4brains/git/log"+j+".txt");
			BufferedWriter writer = new BufferedWriter(new FileWriter(f));
			Random randomGenerator = new Random();
			int number = 0;
			StringBuffer buffer = new StringBuffer();
			for(int k=0;k<100000;k++){
				buffer.append(new Date().toString());
				for (int i = 0; i < 5; i++) {
					number = randomGenerator.nextInt(5);
					if (number == 0) {
						String temp = ""+ (++cc1);
						temp = temp.substring(0, 4)+"-"+temp.substring(4, 8)+"-"+
						temp.substring(8, 12)+"-"+temp.substring(12);
						buffer.append(","+temp);
					} else if (number == 1) {
						String temp = ""+ (++ssn);
						temp = temp.substring(0, 3)+"-"+temp.substring(3, 5)+"-"+
						temp.substring(5);
						buffer.append(","+temp);
					} else if (number == 2) {
						String temp = ""+ (++phone);
						temp = temp.substring(0, 3)+"-"+temp.substring(3, 6)+"-"+
						temp.substring(6);
						buffer.append(","+temp);
					} else if (number == 3) {
						buffer.append(","+randomIdentifier()+".com");
					} else if (number == 4) {
		
					}
				}
				writer.write(buffer.toString()+"\n");
				buffer.delete(0, buffer.length());
			}
			writer.flush();
			writer.close();
		}
	}

	
	
	public String randomIdentifier() {
	    StringBuilder builder = new StringBuilder();
	    while(builder.toString().length() == 0) {
	        int length = rand.nextInt(5)+5;
	        for(int i = 0; i < length; i++)
	            builder.append(lexicon.charAt(rand.nextInt(lexicon.length())));
	        if(identifiers.contains(builder.toString()))
	            builder = new StringBuilder();
	    }
	    return builder.toString();
	}
	public static void main(String ar[]) {
		DataCreator creator = new DataCreator();
		try {
			creator.createData();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
