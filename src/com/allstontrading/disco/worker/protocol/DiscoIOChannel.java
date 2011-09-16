package com.allstontrading.disco.worker.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import com.allstontrading.disco.worker.protocol.decode.DiscoWorkerDecoder;

/**
 * @author Luke Hoersten <lhoersten@allstontrading.com>
 * 
 */
public class DiscoIOChannel {

	private static final int BUFFER_SIZE = 1024;

	private final ByteBuffer readBuffer;
	private final DiscoWorkerDecoder decoder;
	private final InputStream inputStream;
	private final OutputStream outputStream;

	public DiscoIOChannel(final InputStream inputStream, final OutputStream outputStream, final DiscoWorkerDecoder decoder) {
		this.readBuffer = ByteBuffer.allocate(BUFFER_SIZE);
		this.decoder = decoder;
		this.inputStream = inputStream;
		this.outputStream = outputStream;
	}

	public void send(final Object msg) throws IOException {
		outMsg(msg.toString());
		outputStream.write(msg.toString().getBytes());
		outputStream.flush();
		System.out.println("sent");
		receive();
	}

	private void receive() throws IOException {
		int numBytesRead = 0;
		readBuffer.clear();

		System.out.println("receiving");

		while ((numBytesRead = inputStream.read(readBuffer.array(), readBuffer.position(), BUFFER_SIZE - readBuffer.position())) > 0) {
			flipReadBuffer(numBytesRead);
			inMsg(new String(readBuffer.array(), readBuffer.position(), readBuffer.limit()));
			final boolean readFullMsg = decoder.decode(readBuffer);
			readBuffer.compact();

			if (readFullMsg) {
				break;
			}
		}
	}

	private void outMsg(final String msg) {
		System.out.println("OUT: " + msg);
	}

	private void inMsg(final String msg) {
		System.out.println("IN: " + msg);
	}

	/**
	 * Synthetically flip because we read into the buffer array directly instead of using the ByteBuffer.
	 * 
	 * @param numBytesRead
	 */
	private void flipReadBuffer(final int numBytesRead) {
		readBuffer.limit(readBuffer.position() + numBytesRead);
		readBuffer.position(0);
	}
}
