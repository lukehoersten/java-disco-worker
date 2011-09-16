package com.allstontrading.disco.worker.protocol.encode;

import com.allstontrading.disco.worker.protocol.encode.types.RequestMessageName;

/**
 * @author Luke Hoersten <lhoersten@allstontrading.com>
 * 
 */
public class ErrorEncoder extends AbstractDiscoWorkerEncoder {

	public ErrorEncoder() {
		super(RequestMessageName.ERROR);
	}

	public ErrorEncoder set(final String message) {
		setPayload('"' + message + '"');
		return this;
	}

}
