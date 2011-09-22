package com.allstontrading.disco;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @author Luke Hoersten <lhoersten@allstontrading.com>
 * 
 */
public class DiscoUtils {

	private static final char NEWLINE = '\n';

	/**
	 * @param e
	 * @return string of the stack trace
	 */
	public static String stacktraceToString(final Exception e) {
		final StringWriter stringWriter = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(stringWriter);
		e.printStackTrace(printWriter);
		final String str = stringWriter.toString();
		return str;
	}

	/**
	 * Build up a string of all the contents currently available in the channel up until a newline character. Don't dump channels with a lot
	 * of data (like a file) because it will fill up memory.
	 * 
	 * @param readChannel
	 * @return string containing channel contents
	 * @throws IOException
	 */
	public static String channelLineToString(final ReadableByteChannel readChannel) throws IOException {
		final StringBuilder sb = new StringBuilder();
		final ByteBuffer readBuffer = ByteBuffer.allocate(1024 * 2);

		while (readChannel.read(readBuffer) > 0) {
			readBuffer.flip();
			final String readStr = new String(readBuffer.array(), readBuffer.position(), readBuffer.remaining());
			sb.append(readStr);
			readBuffer.clear();

			if (readStr.lastIndexOf(NEWLINE) != -1) {
				break;
			}
		}

		return sb.toString();
	}

	public static String encodeRaw(final String str) {
		return removeNewlines((new BASE64Encoder()).encode(str.getBytes()));
	}

	public static String decodeRaw(final ReadableByteChannel channel) throws IOException {
		return new String(new BASE64Decoder().decodeBuffer(Channels.newInputStream(channel)));
	}

	public static String getPathRelativeToCwd(final File file) {
		final File cwd = new File(".");
		return getPathRelativeTo(file, cwd);
	}

	public static String getPathRelativeTo(final File file, final File relativeTo) {
		return relativeTo.toURI().relativize(file.toURI()).getPath();
	}

	/**
	 * This method is needed because for some reason, BASE64Encoder inserts a newline character every 76 bytes.
	 * 
	 * @param encode
	 * @return
	 */
	private static String removeNewlines(final String encode) {
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < encode.length(); i++) {
			final char c = encode.charAt(i);
			if (c != NEWLINE) {
				sb.append(c);
			}
		}
		return sb.toString();
	}

}
