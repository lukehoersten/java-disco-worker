package com.allstontrading.disco.worker.protocol;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import com.allstontrading.disco.worker.protocol.decode.DiscoWorkerDecoder;

/**
 * @author Luke Hoersten <lhoersten@allstontrading.com>
 * 
 */
public class DiscoIOChannel {

	private static final int BUFFER_SIZE = 1024 * 64; // 64 KB

	private final ByteBuffer readBuffer;
	private final DiscoWorkerDecoder decoder;

	private final ReadableByteChannel readChannel;
	private final WritableByteChannel writeChannel;

	public DiscoIOChannel(final ReadableByteChannel readChannel, final WritableByteChannel writeChannel, final DiscoWorkerDecoder decoder) {
		this.readBuffer = ByteBuffer.allocate(BUFFER_SIZE);
		this.readBuffer.flip();

		this.decoder = decoder;
		this.readChannel = readChannel;
		this.writeChannel = writeChannel;
	}

	public void write(final Object msg) throws IOException {
		final String msgStr = msg.toString();
		outMsg(msgStr);
		writeChannel.write(ByteBuffer.wrap(msgStr.getBytes()));
		read();
	}

	private void read() throws IOException {
		for (;;) {
			final int startPosition = readBuffer.position();
			if (decoder.decode(readBuffer)) {
				inMsg(new String(readBuffer.array(), startPosition, readBuffer.position() - startPosition));
				return;
			}

			readBuffer.compact(); // go to write mode
			final int numBytesRead = readChannel.read(readBuffer);

			if (numBytesRead == 0) {
				throw new IllegalStateException("Ready zero (0) bytes from channel");
			}
			else if (numBytesRead < 0) {
				throw new IllegalStateException("Ready EOF (-1) from channel");
			}

			readBuffer.flip(); // go to read mode
		}
	}

	private void outMsg(final String msg) {
		System.out.println("Disco OUT: " + msg);
	}

	private void inMsg(final String msg) {
		System.out.println("Disco IN: " + msg);
	}

}
