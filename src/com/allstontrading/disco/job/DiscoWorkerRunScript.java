package com.allstontrading.disco.job;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;

import com.allstontrading.disco.DiscoMapFunction;
import com.allstontrading.disco.DiscoReduceFunction;
import com.allstontrading.disco.DiscoUtils;
import com.allstontrading.disco.worker.DiscoWorkerMain;

public class DiscoWorkerRunScript {

	private static final String RUN_SCRIPT_FORMAT = "#!/bin/bash\njava -cp {0} {1} {2} {3} {4}\n";

	public static void generateRunScript(final File scriptFile, final Class<? extends DiscoMapFunction> mapFunctionClass,
	        final Class<? extends DiscoReduceFunction> reduceFunctionClass, final String args) throws IOException {

		scriptFile.setExecutable(true);
		final FileWriter fileWriter = new FileWriter(scriptFile);

		final String classpathRelativeToCwd = getClassPathRelativeToCwd();
		final String discoWorkerMain = DiscoWorkerMain.class.getName();

		final String scriptContents = MessageFormat.format(RUN_SCRIPT_FORMAT, classpathRelativeToCwd, discoWorkerMain,
		        getFunctionName(mapFunctionClass), getFunctionName(reduceFunctionClass), args);

		fileWriter.write(scriptContents);
		fileWriter.close();
	}

	private static String getFunctionName(final Class<?> function) {
		return (function != null) ? function.getName() : "none";
	}

	private static String getClassPathRelativeToCwd() {
		final StringBuilder sb = new StringBuilder();
		final String[] classpaths = getClassPath().split(":");
		for (int i = 0; i < classpaths.length; i++) {
			if (i != 0) {
				sb.append(':');
			}
			sb.append(DiscoUtils.getPathRelativeToCwd(new File(classpaths[i])));
		}
		return sb.toString();
	}

	private static String getClassPath() {
		return System.getProperties().getProperty("java.class.path", null);
	}

}
