package com.datarity.dataritymapreducer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class MaskerMapper {
	
	//bin/hadoop jar /usr/joe/dataritymapreducer-0.0.1-SNAPSHOT.jar com.datarity.dataritymapreducer.RegularExpressionMapper /usr/joe/wordcount/input /usr/joe/wordcount/output

	private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	private static final String PHONE_PATTERN = "[1-9]\\d{2}-[1-9]\\d{2}-\\d{4}";
	private static Pattern p = Pattern.compile(PHONE_PATTERN);
	private static Pattern p1 = Pattern.compile(EMAIL_PATTERN);
	
	public static class Map extends Mapper<LongWritable, Text, Text, Text> {

		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String fileName = value.toString();
			if (!fileName.startsWith("SCANED_FILENAME_")) {
				return;
			}
			String[] fileNameSplit = fileName.split("\t");
			if (fileNameSplit[1] == "0") {
				return;
			}
			fileName = fileNameSplit[0];
			
			Configuration config  = context.getConfiguration();
			String delimter = config.get("delmiter");
			String maskedFileDirPath = config.get("maskedFileDirPath");
			
			fileName = fileName.substring("SCANED_FILENAME_".length(), fileName.length());
			
			FileSystem fs = FileSystem.get(new Configuration());

			Path ptRead=new Path(fileName);
            BufferedReader br=new BufferedReader(new InputStreamReader(fs.open(ptRead)));

            Path ptWrite = new Path(maskedFileDirPath + "/" + fileName);
            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(fs.create(ptWrite,true)));
            
            String line = br.readLine(), word;
            
            while (line != null){
    			StringTokenizer tokenizer = new StringTokenizer(line, delimter);
    			
    			StringBuffer newLine = new StringBuffer();
    			while (tokenizer.hasMoreTokens()) {
    				word = tokenizer.nextToken();
    				if(isEmailId(word)){
    					newLine.append("XXXX@XXX.com");
    				}else if(isPhoneNumber(word)){
    					newLine.append("XXX-XXX-XXXX");
    				}else if(isCreditCard(stripSpecialCharacters(word))){
    					newLine.append("XXXX-XXXX-XXXX-XXXX");
    				}else if(isSSN(stripSpecialCharacters(word))){
    					newLine.append("XXX-XX-XXXX");
    				}
    				else {
    					newLine.append(word);
    				}
    				newLine.append(",");
    			}
    			newLine.append("\n");
    			
    			bw.write(newLine.toString());
    			line=br.readLine();
            }
            br.close();
            bw.close();
		}
	}

	public static String stripSpecialCharacters(String str){
		return  str.replaceAll("[-+.^:,\\s_]","");
	}
	
	public static boolean isSSN(String ssn){
		return ssn.matches("^([0-9]{9})$");
	}
	
	public static boolean isCreditCard(String cc){
		return cc.matches("^([0-9]{16})$");
	}
	
	public static boolean isPhoneNumber(String phoneNumber){
        Matcher m = p.matcher(phoneNumber);
        return m.matches();
	}
	
	public static boolean  isEmailId(String emailId){
		Matcher m = p1.matcher(emailId);
		return m.matches();
	}

	private static void runJob(String delimiter, String inputFile, String outputPath, String maskedFileDirPath) throws Exception {
		Configuration conf = new Configuration();
		conf.setStrings("delmiter", delimiter);
		conf.setStrings("maskedFileDirPath", maskedFileDirPath);
		
		Job job = new Job(conf, "dataritymasker");

		job.setJarByClass(MaskerMapper.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		job.setMapperClass(Map.class);
//		job.setReducerClass(Reduce.class);
		job.setNumReduceTasks(0);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		Date date = new Date();
		FileInputFormat.addInputPath(job, new Path(inputFile));
		FileOutputFormat.setOutputPath(job, new Path(outputPath + "/" + date.getTime()));

		job.waitForCompletion(true);

	}
	public static void main(String[] args) throws Exception {
		runJob(args[2], args[0], args[1], args[3]);
	}
	
	static void delete(File f) throws IOException {
		  if (f.isDirectory()) {
		    for (File c : f.listFiles())
		      delete(c);
		  }
		  if (!f.delete())
		    throw new FileNotFoundException("Failed to delete file: " + f);
		}

}