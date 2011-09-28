package org.discoproject.worker.protocol.encoder;

import org.discoproject.worker.protocol.encoder.types.RequestMessageName;

/**
 * @author Luke Hoersten <lhoersten@allstontrading.com>
 * 
 */
public class FatalEncoder extends AbstractDiscoWorkerEncoder {

	public FatalEncoder() {
		super(RequestMessageName.FATAL);
	}

	public FatalEncoder set(final String message) {
		setPayload('"' + message + '"');
		return this;
	}

}
