package com.allstontrading.disco.lib.worker.protocol.encode;

import com.allstontrading.disco.lib.worker.protocol.encode.types.RequestMessageName;

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
