package com.allstontrading.disco.worker.protocol.encode;

import com.allstontrading.disco.worker.protocol.encode.types.RequestMessageName;

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
