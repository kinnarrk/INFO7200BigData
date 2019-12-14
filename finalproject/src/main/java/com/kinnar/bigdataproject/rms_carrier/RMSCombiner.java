package com.kinnar.bigdataproject.rms_carrier;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;

public class RMSCombiner extends Reducer<Text, RMSCountTuple, Text, RMSCountTuple> {

	private RMSCountTuple res = new RMSCountTuple();

	@Override
	protected void reduce(Text key, Iterable<RMSCountTuple> values, Context context)
			throws IOException, InterruptedException {

		int total = 0;
		int arrDelay = 0;
		int depDelay = 0;

		for (RMSCountTuple tup : values) {
			total += tup.getTotalFlight();
			arrDelay += tup.getArrDelay();
			depDelay += tup.getDepDelay();
		}

		res.setTotalFlight(total);
		res.setArrDelay(arrDelay);
		res.setDepDelay(depDelay);

		context.write(key, res);
	}
}
