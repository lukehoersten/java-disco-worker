package org.discoproject.worker.protocol.encoder;

import org.discoproject.worker.protocol.encoder.types.RequestMessageName;

/**
 * @author Luke Hoersten <lhoersten@allstontrading.com>
 * 
 */
public class RequestTaskEncoder extends AbstractDiscoWorkerEncoder {

	public RequestTaskEncoder() {
		super(RequestMessageName.TASK);
		setPayload("\"\""); // Empty payload actually requires an empty quoted string
	}

}
