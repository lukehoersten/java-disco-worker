package com.allstontrading.disco.worker.protocol.decode;

import java.nio.ByteBuffer;

/**
 * @author Luke Hoersten <lhoersten@allstontrading.com>
 * 
 */
public class WaitDecoder {

	private int pauseSeconds;

	public void parse(final ByteBuffer buffer, final int payloadLength) {
		pauseSeconds = Integer.parseInt(new String(buffer.array(), buffer.position(), payloadLength));
		buffer.position(buffer.position() + payloadLength);
	}

	public int getPauseSeconds() {
		return pauseSeconds;
	}

}
