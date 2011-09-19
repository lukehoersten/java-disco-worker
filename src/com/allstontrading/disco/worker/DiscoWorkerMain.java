package com.allstontrading.disco.worker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.channels.Channels;
import java.util.Arrays;
import java.util.List;

import com.allstontrading.disco.DiscoMapFunction;
import com.allstontrading.disco.DiscoUtils;

/**
 * @author Luke Hoersten <lhoersten@allstontrading.com>
 * 
 */
public class DiscoWorkerMain {

	private static final String STDOUT_LOG = "stdout.log";
	private static final String STDERR_LOG = "stderr.log";

	public static void main(final String[] args) throws IOException {
		final DiscoWorker discoWorker = new DiscoWorker(Channels.newChannel(System.in), Channels.newChannel(System.err));
		redirectStdIOToFile();

		final String functionName = args[0];
		final String reduceFunctionName = args[1];
		final String[] slicedArgs = Arrays.copyOfRange(args, 2, args.length);

		try {
			discoWorker.requestTask();

			if (discoWorker.hasMapTask()) {
				final DiscoMapFunction mapFunction = DiscoWorkerMain.<DiscoMapFunction> instansiateFunction(functionName);
				final List<File> outputFiles = mapFunction.map(discoWorker.getMapInput(), slicedArgs);
				discoWorker.reportOutputs(outputFiles);
			}

			// if (discoWorker.hasReduceTask()) {
			// final DiscoReduceFunction reduceFunction = DiscoWorkerMain.<DiscoReduceFunction> instansiateFunction(reduceFunctionName);
			// final List<File> outputFile = reduceFunction.reduce(discoWorker.getReduceInputs(), slicedArgs);
			// discoWorker.reportOutputs(outputFile);
			// }

			discoWorker.doneReportingOutput();
		}
		catch (final Exception e) {
			discoWorker.reportFatalError(DiscoUtils.stacktraceToString(e));
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> T instansiateFunction(final String clazz) throws IllegalArgumentException, SecurityException,
	        InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException {
		return (T) Class.forName(clazz).getConstructor().newInstance();
	}

	public static void redirectStdIOToFile() throws FileNotFoundException {
		System.setErr(new PrintStream(new FileOutputStream(STDERR_LOG)));
		System.setOut(new PrintStream(new FileOutputStream(STDOUT_LOG)));
	}

}
