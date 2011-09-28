package org.discoproject.worker.protocol.encoder;

import org.discoproject.worker.protocol.encoder.types.RequestMessageName;

/**
 * @author Luke Hoersten <lhoersten@allstontrading.com>
 * 
 */
public class PingEncoder extends AbstractDiscoWorkerEncoder {

	public PingEncoder() {
		super(RequestMessageName.PING);
		setPayload("\"\""); // Empty payload actually requires an empty quoted string
	}

}
