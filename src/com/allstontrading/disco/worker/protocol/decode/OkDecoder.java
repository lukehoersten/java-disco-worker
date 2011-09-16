package com.allstontrading.disco.worker.protocol.decode;

import java.nio.ByteBuffer;

public class OkDecoder {

	public void parse(final ByteBuffer buffer, final int payloadLength) {
		buffer.position(buffer.position() + payloadLength);
	}

}
