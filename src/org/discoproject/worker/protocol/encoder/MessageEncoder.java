package org.discoproject.worker.protocol.encoder;

import org.discoproject.worker.protocol.encoder.types.RequestMessageName;

/**
 * @author Luke Hoersten <lhoersten@allstontrading.com>
 * 
 */
public class MessageEncoder extends AbstractDiscoWorkerEncoder {

	public MessageEncoder() {
		super(RequestMessageName.MSG);
	}

	public MessageEncoder set(final String message) {
		setPayload('"' + message + '"');
		return this;
	}

}
