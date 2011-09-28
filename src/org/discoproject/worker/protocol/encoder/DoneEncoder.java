package org.discoproject.worker.protocol.encoder;

import org.discoproject.worker.protocol.encoder.types.RequestMessageName;

/**
 * @author Luke Hoersten <lhoersten@allstontrading.com>
 * 
 */
public class DoneEncoder extends AbstractDiscoWorkerEncoder {

	public DoneEncoder() {
		super(RequestMessageName.DONE);
		setPayload("\"\""); // Empty payload actually requires an empty quoted string
	}

}
