package com.kinnar.bigdataproject.carrier_delay_cancel;

import java.net.URI;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import com.kinnar.bigdataproject.unique_carrier_names.CarrierInfoMapper;
import com.kinnar.bigdataproject.unique_carrier_names.CarriersApp2;
import com.kinnar.bigdataproject.unique_carrier_names.FlightDetailsMapper;
import com.kinnar.bigdataproject.unique_carrier_names.FlightDetailsReducer;

public class CarrierDelayCancelApp {
	public static void main(String[] args) throws Exception {

		Configuration conf1 = new Configuration();

		FileSystem hdfs = FileSystem.get(URI.create("hdfs://localhost:9000"), conf1);

		Path temp = new Path("/project/temp/carriersdelaycancelintermediate");
		// delete existing directory
		if (hdfs.exists(temp)) {
			hdfs.delete(temp, true);
		}
		Path output = new Path(args[2]);
		// delete existing directory
		if (hdfs.exists(output)) {
			hdfs.delete(output, true);
		}

		Job job1 = Job.getInstance(conf1);
		job1.setJarByClass(CarrierDelayCancelApp.class);
		job1.setJobName("Carrier delay ratio with delay > 15 minutes and cancelled flights ratio");

		FileInputFormat.setInputPaths(job1, new Path(args[0]));
		FileOutputFormat.setOutputPath(job1, temp);

		job1.setMapperClass(CarrierDelayCancelMapper.class);
		job1.setCombinerClass(CarrierDelayCancelReducer.class);
		job1.setReducerClass(CarrierDelayCancelReducer.class);

		job1.setInputFormatClass(TextInputFormat.class);
		job1.setOutputFormatClass(TextOutputFormat.class);

		job1.setMapOutputKeyClass(Text.class);
		job1.setMapOutputValueClass(DelayRatioTuple.class);

		job1.setOutputKeyClass(Text.class);
		job1.setOutputValueClass(DelayRatioTuple.class);

		if (!job1.waitForCompletion(true)) {
			System.exit(1);
		}

		Configuration conf2 = new Configuration();

		Job job2 = Job.getInstance(conf2);
		job2.setJarByClass(CarriersApp2.class);
		job2.setJobName("Reducer Side Inner Join: Get carrier info");

		MultipleInputs.addInputPath(job2, new Path(args[1]), TextInputFormat.class, CarrierInfoMapper.class);
		MultipleInputs.addInputPath(job2, temp, TextInputFormat.class, FlightDetailsMapper.class);

		job2.setReducerClass(FlightDetailsReducer.class);
		job2.setNumReduceTasks(1);

		job2.setOutputKeyClass(Text.class);
		job2.setOutputValueClass(Text.class);

		TextOutputFormat.setOutputPath(job2, output);
		System.exit(job2.waitForCompletion(true) ? 0 : 1);
	}

}
