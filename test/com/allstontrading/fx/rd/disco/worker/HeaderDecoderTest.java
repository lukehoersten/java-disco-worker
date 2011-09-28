package com.allstontrading.fx.rd.disco.worker;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;

import org.discoproject.worker.protocol.decoder.HeaderDecoder;
import org.discoproject.worker.protocol.decoder.types.ResponseMessageName;
import org.junit.Test;


/**
 * @author Luke Hoersten <lhoersten@allstontrading.com>
 * 
 */
public class HeaderDecoderTest {

	@Test
	public void testPartialHeader() {
		final HeaderDecoder headerDecoder = new HeaderDecoder();

		final String msg = "WORKER 2";
		assertFalse(headerDecoder.isFullHeader(ByteBuffer.wrap(msg.getBytes())));

		final String msg1 = "WORK";
		assertFalse(headerDecoder.isFullHeader(ByteBuffer.wrap(msg1.getBytes())));
	}

	@Test
	public void testFullHeader() {
		final String msg = "WORKER 29 {\"pid\":25094,\"version\":\"1.0\"}\n";
		final HeaderDecoder headerDecoder = new HeaderDecoder();
		assertTrue(headerDecoder.isFullHeader(ByteBuffer.wrap(msg.getBytes())));
	}

	@Test
	public void testMessageName() {
		final String msg = "INPUT 29 {\"pid\":25094,\"version\":\"1.0\"}\n";
		final HeaderDecoder headerDecoder = new HeaderDecoder();
		assertEquals(ResponseMessageName.INPUT, headerDecoder.parse(ByteBuffer.wrap(msg.getBytes())).getMessageName());
	}

	@Test
	public void testPayloadLength() {
		final String msg = "INPUT 29 {\"pid\":25094,\"version\":\"1.0\"}\n";
		final HeaderDecoder headerDecoder = new HeaderDecoder();
		assertEquals(29, headerDecoder.parse(ByteBuffer.wrap(msg.getBytes())).getPayloadLength());
	}

}
