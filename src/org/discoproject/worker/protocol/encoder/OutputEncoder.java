package org.discoproject.worker.protocol.encoder;

import java.io.File;

import org.discoproject.worker.protocol.encoder.types.OutputType;
import org.discoproject.worker.protocol.encoder.types.RequestMessageName;

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

		final StringBuilder sb = new StringBuilder("[");
		sb.append(QUOTE);
		sb.append(outputLocation);
		sb.append(QUOTE);
		sb.append(",");
		sb.append(QUOTE);
		sb.append(outputType);
		sb.append(QUOTE);
		sb.append(",");
		sb.append(QUOTE);
		sb.append(label);
		sb.append(QUOTE);
		sb.append("]");
		setPayload(sb.toString());

		return this;
	}

}
