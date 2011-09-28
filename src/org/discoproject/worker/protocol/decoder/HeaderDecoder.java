package org.discoproject.worker.protocol.decoder;

import java.nio.ByteBuffer;

import org.discoproject.worker.protocol.decoder.types.ResponseMessageName;


/**
 * @author Luke Hoersten <lhoersten@allstontrading.com>
 * 
 */
public class HeaderDecoder {

	private static final byte SPACE = ' ';
	private static final int NUM_SPACES = 2;

	private ResponseMessageName name = null;
	private int payloadLength = 0;

	public HeaderDecoder parse(final ByteBuffer buffer) {
		final String bufStr = new String(buffer.array(), buffer.position(), findSecondSpace(buffer));
		final String[] splitBuffer = bufStr.split(" ", NUM_SPACES + 1);
		name = ResponseMessageName.valueOf(splitBuffer[0]);
		payloadLength = Integer.parseInt(splitBuffer[1]);

		buffer.position(buffer.position() + splitBuffer[0].length() + splitBuffer[1].length() + NUM_SPACES);
		return this;
	}

	public ResponseMessageName getMessageName() {
		return name;
	}

	public int getPayloadLength() {
		return payloadLength;
	}

	private int findSecondSpace(final ByteBuffer buffer) {
		final byte[] bytes = buffer.array();
		int spaceCount = 0;

		for (int i = 0; i < buffer.remaining(); i++) {
			if (bytes[i + buffer.position()] == SPACE) {
				if (++spaceCount == 2) {
					return i;
				}
			}
		}

		return -1;
	}

	/**
	 * Look for two spaces without iterating the position counter.
	 * 
	 * @param buffer
	 * @return position of second space or <code>-1</code> if not found.
	 */
	public boolean isFullHeader(final ByteBuffer buffer) {
		return findSecondSpace(buffer) != -1;
	}

}
