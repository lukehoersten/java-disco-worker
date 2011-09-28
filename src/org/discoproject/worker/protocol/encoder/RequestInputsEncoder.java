package org.discoproject.worker.protocol.encoder;

import org.discoproject.worker.protocol.encoder.types.RequestMessageName;

/**
 * @author Luke Hoersten <lhoersten@allstontrading.com>
 * 
 */
public class RequestInputsEncoder extends AbstractDiscoWorkerEncoder {

	public RequestInputsEncoder() {
		super(RequestMessageName.INPUT);
		setPayload("\"\""); // Empty payload actually requires an empty quoted string
	}

}
