package com.allstontrading.disco.worker;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author Luke Hoersten <lhoersten@allstontrading.com>
 * 
 */
public class DiscoUtils {

	public static String stacktraceToString(final Exception e) {
		final StringWriter stringWriter = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(stringWriter);
		e.printStackTrace(printWriter);
		final String str = stringWriter.toString();
		return str;
	}

}
