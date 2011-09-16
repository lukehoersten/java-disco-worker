package com.allstontrading.disco;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * @author Luke Hoersten <lhoersten@allstontrading.com>
 * 
 */
public interface DiscoReduceFunction {

	List<File> reduce(List<InputStream> inputs, final String[] args);

}
