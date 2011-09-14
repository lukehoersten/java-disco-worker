package com.allstontrading.disco.lib;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * @author Luke Hoersten <lhoersten@allstontrading.com>
 * 
 */
public interface DiscoMapFunction {

	List<File> map(InputStream input, final String[] args);

}
