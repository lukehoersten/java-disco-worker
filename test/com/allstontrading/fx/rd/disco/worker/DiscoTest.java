package com.allstontrading.fx.rd.disco.worker;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.nio.channels.Pipe.SinkChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.List;

import org.discoproject.DiscoUtils;
import org.discoproject.worker.DiscoWorker;
import org.discoproject.worker.protocol.DiscoIOChannel;
import org.discoproject.worker.protocol.decoder.DiscoWorkerDecoder;
import org.discoproject.worker.protocol.decoder.DiscoWorkerListener;
import org.discoproject.worker.protocol.decoder.HeaderDecoder;
import org.discoproject.worker.protocol.decoder.InputDecoder;
import org.discoproject.worker.protocol.decoder.types.DiscoInput;
import org.discoproject.worker.protocol.decoder.types.DiscoInputReplica;
import org.discoproject.worker.protocol.decoder.types.DiscoInputReplicaProtocol;
import org.discoproject.worker.protocol.decoder.types.DiscoInputStatus;
import org.discoproject.worker.protocol.encoder.WorkerAnnounceEncoder;
import org.junit.Test;
import org.mockito.InOrder;


/**
 * @author Luke Hoersten <lhoersten@allstontrading.com>
 * 
 */
public class DiscoTest {

	@Test
	public void testWorkerAnnounceEncode() {
		final String expected = "WORKER 29 {\"pid\":25094,\"version\":\"1.0\"}\n";
		assertEquals(expected, new WorkerAnnounceEncoder().set("1.0", 25094).toString());
	}

	@Test
	public void testWorkerAnnounce() throws IOException {
		final DiscoWorkerListener listener = mock(DiscoWorkerListener.class);
		final InOrder inOrder = inOrder(listener);

		final Pipe pipeFromDisco = Pipe.open();
		final DiscoIOChannel ioChannel = new DiscoIOChannel(pipeFromDisco.source(), new NullByteChannel(),
		        new DiscoWorkerDecoder().setListener(listener));

		final String okStr = "OK 4 \"ok\"\n";
		pipeFromDisco.sink().write(ByteBuffer.wrap(okStr.getBytes()));
		ioChannel.write(new WorkerAnnounceEncoder().set("1.0", 555));

		inOrder.verify(listener).ok();
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

	@Test
	public void testInputFlow() throws IOException {
		final Pipe pipeFromDisco = Pipe.open();

		final DiscoWorker discoWorker = new DiscoWorker(pipeFromDisco.source(), new NullByteChannel());
		final SinkChannel disco = pipeFromDisco.sink();

		// Put messages in the input pipeline (from the disco master)
		final String okStr = "OK 4 \"ok\"\n";
		disco.write(ByteBuffer.wrap(okStr.getBytes()));
		final String taskStr = "TASK 327 {\"taskid\":0,\"master\":\"http://lhoersten-66113:8989\",\"disco_port\":8989,\"put_port\":8990,\"ddfs_data\":\"/srv/disco/ddfs\",\"disco_data\":\"/srv/disco/data\",\"mode\":\"map\",\"jobfile\":\"/srv/disco/data/localhost/5a/lhoersten-FunshineSimulator@524:7310e:1cb44/jobfile\",\"jobname\":\"lhoersten-FunshineSimulator@524:7310e:1cb44\",\"host\":\"localhost\"}\n";
		disco.write(ByteBuffer.wrap(taskStr.getBytes()));
		final String inputStr = "INPUT 110 [\"done\",[[0,\"ok\",[[0,\"raw://eyJlbnRyeWV4aXRsZXZlbCI6WyJERUMiLDEuNV0sInFyMiI6WyJERUMiLDFdLCJxcjEiOlsiREVD\"]]]]]\n";
		disco.write(ByteBuffer.wrap(inputStr.getBytes()));

		discoWorker.requestTask();
		final ReadableByteChannel mapInput = discoWorker.getMapInput();

		final String input = DiscoUtils.channelLineToString(mapInput);

		assertEquals("eyJlbnRyeWV4aXRsZXZlbCI6WyJERUMiLDEuNV0sInFyMiI6WyJERUMiLDFdLCJxcjEiOlsiREVD", input);
	}

	@Test
	public void testChannelToString() throws IOException {
		final Pipe pipe = Pipe.open();

		final SinkChannel sink = pipe.sink();
		final String expected = "12345678\n";
		sink.write(ByteBuffer.wrap(expected.getBytes()));

		final String actual = DiscoUtils.channelLineToString(pipe.source());

		assertEquals(expected, actual);
	}

	@Test
	public void testB64Encoding() {
		final String json = "{\"entryexitlevel\":[\"DEC\",1.5],\"qr2\":[\"DEC\",1],\"qr1\":[\"DEC\",1.0E-5],\"enterexitwindow\":[\"STR\",\"'2 hours'\"],\"VarientNumber\":1}";
		final String expected = "eyJlbnRyeWV4aXRsZXZlbCI6WyJERUMiLDEuNV0sInFyMiI6WyJERUMiLDFdLCJxcjEiOlsiREVDIiwxLjBFLTVdLCJlbnRlcmV4aXR3aW5kb3ciOlsiU1RSIiwiJzIgaG91cnMnIl0sIlZhcmllbnROdW1iZXIiOjF9";

		final String actual = DiscoUtils.encodeRaw(json);

		assertEquals(expected, actual);
	}

	private class NullByteChannel implements WritableByteChannel {
		@Override
		public boolean isOpen() {
			return true;
		}

		@Override
		public void close() throws IOException {}

		@Override
		public int write(final ByteBuffer src) throws IOException {
			final int remaining = src.remaining();
			src.clear();
			return remaining;
		}
	}

}
