package com.allstontrading.disco.lib.worker.protocol.encode;

import java.io.File;

import org.json.JSONArray;

import com.allstontrading.disco.lib.worker.protocol.encode.types.OutputType;
import com.allstontrading.disco.lib.worker.protocol.encode.types.RequestMessageName;

/**
 * @author Luke Hoersten <lhoersten@allstontrading.com>
 * 
 */
public class OutputEncoder extends AbstractDiscoWorkerEncoder {

	public OutputEncoder() {
		super(RequestMessageName.OUTPUT);
	}

	public OutputEncoder set(final File jobHome, final File outputLocation, final OutputType outputType, final String label) {
		// TODO Local outputs have locations that are paths relative to jobhome.

		final JSONArray jsonArray = new JSONArray();
		jsonArray.put(outputLocation);
		jsonArray.put(outputType);
		jsonArray.put(label);
		setPayload(jsonArray.toString());
		return this;
	}

}
