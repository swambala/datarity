package com.datarity.dataritymapreducer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

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

	public static class Map extends
			Mapper<LongWritable, Text, Text, Text> {
//		private final static IntWritable one = new IntWritable(1);
		private String word;
		private boolean isCCNumber;
		
		private static int numOfTimeRun = 0;//TODO REMOVE

		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			if (numOfTimeRun != 0){
				return;
			}
			
//			Path ptWrite = new Path("/Users/barath/b/barath/hackathon/s/outputfiles/TestNew.txt");
			Path ptWrite = new Path("/Users/barath/b/barath/hackathon/s/files/TestNew.txt");
            FileSystem fs = FileSystem.get(new Configuration());
            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(fs.create(ptWrite,true)));
            // TO append data to a file, use fs.append(Path f)
            
			Path pt=new Path("/Users/barath/b/barath/hackathon/s/files/Test.txt");
            BufferedReader br=new BufferedReader(new InputStreamReader(fs.open(pt)));
            String line;
            line=br.readLine();
            
            while (line != null){
            	System.out.println(line);
    			StringTokenizer tokenizer = new StringTokenizer(line, ",");
    			
    			StringBuffer newLine = new StringBuffer();
    			while (tokenizer.hasMoreTokens()) {
    				word = tokenizer.nextToken();
    				isCCNumber = word.matches("^([0-9]{16})$");
    				
    				if (isCCNumber) {
    					newLine.append("XXXX-XXXX-XXXX-XXXX");
    				} else {
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


	public static void main(String[] args) throws Exception {
		
//		File f = new File("/Users/barath/b/barath/hackathon/s/outputfiles");
		delete(new File("/Users/barath/b/barath/hackathon/s/outputfiles"));
		
		Configuration conf = new Configuration();

		Job job = new Job(conf, "creditcardmasker");

		job.setJarByClass(MaskerMapper.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		job.setMapperClass(Map.class);
//		job.setReducerClass(Reduce.class);
		job.setNumReduceTasks(0);

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