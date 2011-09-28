package org.discoproject.job;

import java.io.File;
import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.util.Collections;
import java.util.List;

import org.discoproject.DiscoMapFunction;


/**
 * Run like: "java -cp job.jar com.allstontrading.disco.job.ExampleDiscoJob example_input_file"
 * 
 * @author Luke Hoersten <lhoersten@allstontrading.com>
 * 
 */
public class ExampleDiscoJob implements DiscoMapFunction {

	/**
	 * Describe the parameter space, create a job, and submit it to the disco master.
	 * 
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void main(final String[] args) throws IOException, InterruptedException {
		final String configTemplateFile = args[0];

		final String[] inputs = {};
		final String[] jobArgs = { configTemplateFile };

		final DiscoJob discoJob = new DiscoJob("Test", inputs, jobArgs);
		discoJob.setMapFunction(ExampleDiscoJob.class);
		discoJob.submit();
	}

	@Override
	public List<File> map(final ReadableByteChannel input, final File workingDir, final String[] args) {
		final String configTemplateFile = args[0];

		// Run task with input

		// Return results
		return Collections.singletonList(new File(""));
	}

}
