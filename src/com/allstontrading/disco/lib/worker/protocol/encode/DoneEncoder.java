package com.allstontrading.disco.lib.worker.protocol.encode;

import com.allstontrading.disco.lib.worker.protocol.encode.types.RequestMessageName;

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
