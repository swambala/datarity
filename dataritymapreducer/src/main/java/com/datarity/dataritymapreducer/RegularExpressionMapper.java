package com.datarity.dataritymapreducer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
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
	
	//bin/hadoop jar /usr/joe/dataritymapreducer-0.0.1-SNAPSHOT.jar com.datarity.dataritymapreducer.RegularExpressionMapper /usr/joe/wordcount/input /usr/joe/wordcount/output

	public static class Map extends
			Mapper<LongWritable, Text, Text, Text> {
//		private final static IntWritable one = new IntWritable(1);
		private String word;
		private boolean isCCNumber;

		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			String line = value.toString();
			StringTokenizer tokenizer = new StringTokenizer(line, ",");

			FileSplit fileSplit = (FileSplit)context.getInputSplit();
			String filename = fileSplit.getPath().getName();
			
			while (tokenizer.hasMoreTokens()) {
				word = tokenizer.nextToken();
				//^(?:4[0-9]{12}(?:[0-9]{3})?|5[1-5][0-9]{14}|6(?:011|5[0-9][0-9])[0-9]{12}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|(?:2131|1800|35\d{3})\d{11})$
//				isCCNumber = word.matches("^(?:4[0-9]{12}(?:[0-9]{3})?|5[1-5][0-9]{14}|6(?:011|5[0-9][0-9])[0-9]{12}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|(?:2131|1800|35\\d{3})\\d{11})$");
				isCCNumber = word.matches("^([0-9]{16})$");
				
				if (!isCCNumber) {
					continue;
				}
				
				
				context.write(new Text(word), new Text(filename));
			}
		}
	}

	public static class Reduce extends
			Reducer<Text, Text, Text, Text> {

		public void reduce(Text key, Iterable<Text> values,
				Context context) throws IOException, InterruptedException {
			int sum = 0;
			Set<String> fileNames = new TreeSet<String>();
			for (Text val : values) {
				sum ++;
				
				fileNames.add(val.toString());
			}
			context.write(key, new Text(fileNames.toString() + " : "+sum));
		}
	}

	public static void main(String[] args) throws Exception {
		
		boolean isCCNumber = "4532445227652580".matches("^([0-9]{16})$");
		System.out.println(isCCNumber);
		
		if(false) {
			return;
		}
		
//		File f = new File("/Users/barath/b/barath/hackathon/s/outputfiles");
//		delete(new File("/Users/barath/b/barath/hackathon/s/outputfiles"));
		
		Configuration conf = new Configuration();

		Job job = new Job(conf, "creditcardmatcher");
		job.setJarByClass(RegularExpressionMapper.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		job.waitForCompletion(true);
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