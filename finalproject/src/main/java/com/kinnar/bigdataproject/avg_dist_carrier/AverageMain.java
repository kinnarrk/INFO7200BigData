package com.kinnar.bigdataproject.avg_dist_carrier;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;
import java.net.URI;

public class AverageMain {

	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {

		Configuration conf = new Configuration();

		FileSystem hdfs = FileSystem.get(URI.create("hdfs://localhost:9000"), conf);

		Path output = new Path(args[1]);
		// delete existing directory
		if (hdfs.exists(output)) {
			hdfs.delete(output, true);
		}

		// Create a new Job
		Job job = Job.getInstance(conf, "wordcount");
		job.setJarByClass(AverageMain.class);

		// Specify various job-specific parameters
		job.setJobName("myjob");

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, output);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(AverageCountTuple.class);

		job.setMapperClass(AverageMapper.class);
		job.setCombinerClass(AverageCombiner.class);
		job.setReducerClass(AverageReducer.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(AverageCountTuple.class);

		// Submit the job, then poll for progress until the job is complete
		System.exit(job.waitForCompletion(true) ? 0 : 1);

	}
}
