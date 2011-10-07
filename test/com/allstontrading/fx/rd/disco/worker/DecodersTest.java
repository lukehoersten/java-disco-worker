package com.allstontrading.fx.rd.disco.worker;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import java.util.List;

import org.discoproject.worker.protocol.decoder.FailDecoder;
import org.discoproject.worker.protocol.decoder.HeaderDecoder;
import org.discoproject.worker.protocol.decoder.InputDecoder;
import org.discoproject.worker.protocol.decoder.RetryDecoder;
import org.discoproject.worker.protocol.decoder.WaitDecoder;
import org.discoproject.worker.protocol.decoder.types.DiscoInput;
import org.discoproject.worker.protocol.decoder.types.DiscoInputReplica;
import org.discoproject.worker.protocol.decoder.types.DiscoInputReplicaProtocol;
import org.discoproject.worker.protocol.decoder.types.DiscoInputStatus;
import org.junit.Test;

public class DecodersTest {

	@Test
	public void testFailDecoder() {
		final HeaderDecoder headerDecoder = new HeaderDecoder();
		final FailDecoder decoder = new FailDecoder();

		final String inputStr = "FAIL 16 [1234,[5,6,7,8]]\n";
		final ByteBuffer bb = ByteBuffer.wrap(inputStr.getBytes());

		decoder.parse(bb, headerDecoder.parse(bb).getPayloadLength());

		assertEquals(1234, decoder.getInputId());
		final List<Integer> replicaIds = decoder.getReplicaIds();
		for (int i = 0; i < 4; i++) {
			assertEquals((i + 5), replicaIds.get(i).intValue());
		}
	}

	@Test
	public void testRetryDecoder() {
		final HeaderDecoder headerDecoder = new HeaderDecoder();
		final RetryDecoder decoder = new RetryDecoder();

		final String inputStr = "RETRY 33 [[0,\"location0\"],[1,\"location1\"]]\n";
		final ByteBuffer bb = ByteBuffer.wrap(inputStr.getBytes());

		decoder.parse(bb, headerDecoder.parse(bb).getPayloadLength());
		final List<DiscoInputReplica> replicas = decoder.getReplicas();
	}

	@Test
	public void testWaitDecoder() {
		final HeaderDecoder headerDecoder = new HeaderDecoder();
		final WaitDecoder decoder = new WaitDecoder();

		final String inputStr = "WAIT 3 100\n";
		final ByteBuffer bb = ByteBuffer.wrap(inputStr.getBytes());

		decoder.parse(bb, headerDecoder.parse(bb).getPayloadLength());
		assertEquals(100, decoder.getPauseSeconds());
	}

	@Test
	public void testInputDecoder() {
		final HeaderDecoder headerDecoder = new HeaderDecoder();
		final InputDecoder inputDecoder = new InputDecoder();

		final String inputStr = "INPUT 110 [\"done\",[[0,\"ok\",[[0,\"raw://eyJlbnRyeWV4aXRsZXZlbCI6WyJERUMiLDEuNV0sInFyMiI6WyJERUMiLDFdLCJxcjEiOlsiREVD\"]]]]]\n";
		final ByteBuffer bb = ByteBuffer.wrap(inputStr.getBytes());

		inputDecoder.parse(bb, headerDecoder.parse(bb).getPayloadLength());

		assertTrue(inputDecoder.isDone());

		final List<DiscoInput> inputs = inputDecoder.getInputs();
		final DiscoInput input = inputs.get(0);
		assertEquals(0, input.getId());
		assertEquals(DiscoInputStatus.ok, input.getStatus());

		final List<DiscoInputReplica> replicas = input.getReplicas();
		final DiscoInputReplica replica = replicas.get(0);

		assertEquals(0, replica.getId());
		assertEquals("raw://eyJlbnRyeWV4aXRsZXZlbCI6WyJERUMiLDEuNV0sInFyMiI6WyJERUMiLDFdLCJxcjEiOlsiREVD", replica.getURI().toString());
		assertEquals(DiscoInputReplicaProtocol.raw, replica.getScheme());
	}

}
