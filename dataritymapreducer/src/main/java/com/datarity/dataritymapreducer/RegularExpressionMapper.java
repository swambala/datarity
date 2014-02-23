package com.datarity.dataritymapreducer;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class RegularExpressionMapper {

	private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	private static final String PHONE_PATTERN = "[1-9]\\d{2}-[1-9]\\d{2}-\\d{4}";
	private static Pattern p = Pattern.compile(PHONE_PATTERN);
	private static Pattern p1 = Pattern.compile(EMAIL_PATTERN);

	
	public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {
	
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			
			String line = value.toString();
			Configuration config  = context.getConfiguration();
			String delimter = config.get("delmiter");
			StringTokenizer tokenizer = new StringTokenizer(line, delimter);

			FileSplit fileSplit = (FileSplit) context.getInputSplit();
			String fileName = fileSplit.getPath().getName();
			String fullFileName = fileSplit.getPath().toUri().getPath();
			int ccCount=0, phCount=0, ssnCount=0, emailCount=0;
			String word;
			
			while (tokenizer.hasMoreTokens()) {
				word = tokenizer.nextToken();
				if(isEmailId(word)){
					emailCount++;
				}else if(isPhoneNumber(word)){
					phCount++;
				}else if(isCreditCard(stripSpecialCharacters(word))){
					ccCount++;
				}else if(isSSN(stripSpecialCharacters(word))){
					ssnCount++;
				}
				else {
					continue;
				}
			}
			context.write(new Text(fileName+"_CREDITCARD"), new IntWritable(ccCount));
			context.write(new Text(fileName+"_EMAIL"), new IntWritable(emailCount));
			context.write(new Text(fileName+"_PHONE"), new IntWritable(phCount));
			context.write(new Text(fileName+"_SSN"), new IntWritable(ssnCount));
			
			context.write(new Text(MaskEnum.CREDIT_CARD.toString()), new IntWritable(ccCount));
			context.write(new Text(MaskEnum.EMAIL.toString()), new IntWritable(emailCount));
			context.write(new Text(MaskEnum.PHONE.toString()), new IntWritable(phCount));
			context.write(new Text(MaskEnum.SSN.toString()), new IntWritable(ssnCount));
			
			context.write(new Text("SCANED_FILENAME_" + fullFileName), new IntWritable(ccCount+emailCount+phCount+ssnCount));
			context.write(new Text("TOTAL_DATARITY_COUNT"), new IntWritable(ccCount+emailCount+phCount+ssnCount));
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

	public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {

		public void reduce(Text key, Iterable<IntWritable> values, Context context)
				throws IOException, InterruptedException {
			
			Iterator<IntWritable> itrValues = values.iterator();

			int total = 0;
			while(itrValues.hasNext()){
				total += itrValues.next().get();
			}
			
			context.write(key, new IntWritable(total));
		}
	}

	public static void runJob(String delimiter, String inputPath, String outputPath) throws Exception {
		Configuration conf = new Configuration();
		conf.setStrings("delmiter", delimiter);
		
		Job job = new Job(conf, "datarityscan");

		job.setJarByClass(MaskerMapper.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);
	//	job.setNumReduceTasks(0);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
	//	MultipleOutputs.addNamedOutput(job, "text", TextOutputFormat.class,
	//		    LongWritable.class, Text.class);	
		
		Date date = new Date();
		FileInputFormat.addInputPath(job, new Path(inputPath));
		FileOutputFormat.setOutputPath(job, new Path(outputPath + "/" + date.getTime()));

		job.waitForCompletion(true);
	}
	public static void main(String args[]) throws Exception {
//		delete(new File("/Users/barath/b/barath/hackathon/s/outputfiles"));
		runJob(args[2], args[0], args[1]);
		
	}

	static void delete(File f) throws IOException {
		if (f.isDirectory()) {
			for (File c : f.listFiles())
				delete(c);
		}
		f.delete();
	}
}