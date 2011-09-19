package com.allstontrading.disco;

import java.io.File;
import java.nio.channels.ReadableByteChannel;
import java.util.List;

/**
 * @author Luke Hoersten <lhoersten@allstontrading.com>
 * 
 */
public interface DiscoReduceFunction {

	List<File> reduce(List<ReadableByteChannel> inputs, final String[] args);

}
