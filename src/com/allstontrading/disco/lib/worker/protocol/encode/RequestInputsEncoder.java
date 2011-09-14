package com.allstontrading.disco.lib.worker.protocol.encode;

import com.allstontrading.disco.lib.worker.protocol.encode.types.RequestMessageName;

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
