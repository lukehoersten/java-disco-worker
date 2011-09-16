package com.allstontrading.disco.worker.protocol.encode;

import com.allstontrading.disco.worker.protocol.encode.types.RequestMessageName;

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
