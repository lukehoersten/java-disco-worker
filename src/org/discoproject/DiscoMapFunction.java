package org.discoproject;

import java.io.File;
import java.nio.channels.ReadableByteChannel;
import java.util.List;

/**
 * @author Luke Hoersten <lhoersten@allstontrading.com>
 * 
 */
public interface DiscoMapFunction {

	List<File> map(ReadableByteChannel input, File workingDir, final String[] args);

}
