package org.discoproject.worker.protocol.encoder;

import java.io.File;

import org.discoproject.worker.protocol.encoder.types.OutputType;
import org.discoproject.worker.protocol.encoder.types.RequestMessageName;
import org.json.JSONArray;


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
