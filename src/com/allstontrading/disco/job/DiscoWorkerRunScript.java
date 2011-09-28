package com.allstontrading.disco.job;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.text.MessageFormat;
import java.util.LinkedList;

import com.allstontrading.disco.DiscoMapFunction;
import com.allstontrading.disco.DiscoReduceFunction;
import com.allstontrading.disco.DiscoUtils;
import com.allstontrading.disco.worker.DiscoWorkerMain;

/**
 * @author Luke Hoersten <lhoersten@allstontrading.com>
 * 
 */
public class DiscoWorkerRunScript {

	private static final String COLON = ":";
	private static final String COMMA = ",";
	private static final String SPACE = " ";

	private static final String JAVA_CLASS_PATH = "java.class.path";
	private static final String NO_FUNCTION = "none";
	private static final String RUN_SCRIPT_FORMAT = "#!/bin/bash\n{0} {1} -cp {2} {3} {4} {5} {6}\n";
	private static final String JAVA_BIN = "java";

	public static void generateRunScript(final File scriptFile, final Class<? extends DiscoMapFunction> mapFunctionClass,
	        final Class<? extends DiscoReduceFunction> reduceFunctionClass, final String[] args) throws IOException {

		scriptFile.setExecutable(true);
		final FileWriter fileWriter = new FileWriter(scriptFile);

		final String vmArgs = joinWith(splitBy(getVMArgs(), COMMA), SPACE);
		final String classpath = getClassPathRelativeToCwd();
		final String discoWorkerMain = DiscoWorkerMain.class.getName();
		final String workerArgs = joinWith(args, SPACE);

		final String scriptContents = MessageFormat.format(RUN_SCRIPT_FORMAT, JAVA_BIN, vmArgs, classpath, discoWorkerMain,
		        getFunctionName(mapFunctionClass), getFunctionName(reduceFunctionClass), workerArgs);

		fileWriter.write(scriptContents);
		fileWriter.close();
	}

	private static String joinWith(final String[] args, final String joinWith) {
		final StringBuilder sb = new StringBuilder();
		for (final String arg : args) {
			sb.append(joinWith);
			sb.append(arg);
		}
		return sb.toString();
	}

	private static String[] splitBy(final String[] args, final String splitBy) {
		final LinkedList<String> list = new LinkedList<String>();
		for (final String arg : args) {
			for (final String sarg : arg.split(splitBy)) {
				list.add(sarg);
			}
		}
		return list.toArray(new String[0]);
	}

	private static String getFunctionName(final Class<?> function) {
		return (function != null) ? function.getName() : NO_FUNCTION;
	}

	private static String getClassPathRelativeToCwd() {
		final StringBuilder sb = new StringBuilder();
		final String[] classpaths = getClassPath().split(COLON);
		for (int i = 0; i < classpaths.length; i++) {
			if (i != 0) {
				sb.append(COLON);
			}
			sb.append(DiscoUtils.getPathRelativeToCwd(new File(classpaths[i])));
		}
		return sb.toString();
	}

	private static String getClassPath() {
		return System.getProperties().getProperty(JAVA_CLASS_PATH, null);
	}

	private static String[] getVMArgs() {
		return ManagementFactory.getRuntimeMXBean().getInputArguments().toArray(new String[0]);
	}

}
