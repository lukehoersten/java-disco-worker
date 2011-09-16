package com.allstontrading.disco.worker.protocol.encode;

import com.allstontrading.disco.worker.protocol.encode.types.RequestMessageName;

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
