package com.allstontrading.fx.rd.disco.worker;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.discoproject.worker.protocol.encoder.DoneEncoder;
import org.discoproject.worker.protocol.encoder.ErrorEncoder;
import org.discoproject.worker.protocol.encoder.FatalEncoder;
import org.discoproject.worker.protocol.encoder.InputErrorEncoder;
import org.discoproject.worker.protocol.encoder.MessageEncoder;
import org.discoproject.worker.protocol.encoder.OutputEncoder;
import org.discoproject.worker.protocol.encoder.PingEncoder;
import org.discoproject.worker.protocol.encoder.RequestInputsEncoder;
import org.discoproject.worker.protocol.encoder.RequestTaskEncoder;
import org.discoproject.worker.protocol.encoder.WorkerAnnounceEncoder;
import org.discoproject.worker.protocol.encoder.types.OutputType;
import org.junit.Test;

public class EncodersTest {

	@Test
	public void testDoneEncoder() {
		final DoneEncoder encoder = new DoneEncoder();
		final String encodedString = encoder.toString();

		assertEquals("DONE 2 \"\"\n", encodedString);
	}

	@Test
	public void testErrorEncoder() {
		final ErrorEncoder encoder = new ErrorEncoder();
		final String errorMessage = "The error message";
		encoder.set(errorMessage);
		final String encodedString = encoder.toString();

		assertEquals("ERROR 19 \"The error message\"\n", encodedString);
	}

	@Test
	public void testFatalEncoder() {
		final FatalEncoder encoder = new FatalEncoder();
		final String errorMessage = "The fatal message";
		encoder.set(errorMessage);
		final String encodedString = encoder.toString();

		assertEquals("FATAL 19 \"The fatal message\"\n", encodedString);
	}

	@Test
	public void testInputErrorEncoder() {
		final InputErrorEncoder encoder = new InputErrorEncoder();
		final String inputId = "inputId";
		final List<String> repIds = new ArrayList<String>();
		repIds.add("1");
		repIds.add("2");
		repIds.add("3");
		repIds.add("4");
		repIds.add("5");
		encoder.set(inputId, repIds);
		final String encodedString = encoder.toString();
		assertEquals("INPUT_ERR 33 [\"inputId\",[\"1\",\"2\",\"3\",\"4\",\"5\"]]\n", encodedString);
	}

	@Test
	public void testMessageEncoder() {
		final MessageEncoder encoder = new MessageEncoder();
		final String message = "The message";
		encoder.set(message);
		final String encodedString = encoder.toString();
		assertEquals("MSG 13 \"The message\"\n", encodedString);
	}

	@Test
	public void testOutputEncoder() {
		final OutputEncoder encoder = new OutputEncoder();
		final File jobHome = new File("dir/jobHome/");
		final File outputLocation = new File("dir/outputLocation/");
		final OutputType outputType = OutputType.disco;
		final String label = "thelabel";
		encoder.set(jobHome, outputLocation, outputType, label);
		final String encodedString = encoder.toString();
		assertEquals("OUTPUT 41 [\"dir/outputLocation\",\"disco\",\"thelabel\"]\n", encodedString);
	}

	@Test
	public void testPingEncoder() {
		final PingEncoder encoder = new PingEncoder();
		final String encodedString = encoder.toString();

		assertEquals("PING 2 \"\"\n", encodedString);
	}

	@Test
	public void testRequestInputsEncoder() {
		final RequestInputsEncoder encoder = new RequestInputsEncoder();
		final String encodedString = encoder.toString();

		assertEquals("INPUT 2 \"\"\n", encodedString);
	}

	@Test
	public void testRequestTaskEncoder() {
		final RequestTaskEncoder encoder = new RequestTaskEncoder();
		final String encodedString = encoder.toString();

		assertEquals("TASK 2 \"\"\n", encodedString);
	}

	@Test
	public void testWorkerAnnounceEncoder() {
		final WorkerAnnounceEncoder encoder = new WorkerAnnounceEncoder();
		encoder.set("version", 12345);
		final String encodedString = encoder.toString();
		assertEquals("WORKER 33 {\"pid\":12345,\"version\":\"version\"}\n", encodedString);
	}

}
