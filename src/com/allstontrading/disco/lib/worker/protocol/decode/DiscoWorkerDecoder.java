package com.allstontrading.disco.lib.worker.protocol.decode;

import java.nio.ByteBuffer;

/**
 * @author Luke Hoersten <lhoersten@allstontrading.com>
 * 
 */
public class DiscoWorkerDecoder {

	private final HeaderDecoder headerDecoder;
	private final TaskDecoder taskDecoder;
	private final InputDecoder inputDecoder;
	private final FailDecoder failDecoder;
	private final OkDecoder okDecoder;
	private final RetryDecoder retryDecoder;
	private final WaitDecoder waitDecoder;

	private DiscoWorkerListener listener;

	public DiscoWorkerDecoder() {
		this.headerDecoder = new HeaderDecoder();
		this.taskDecoder = new TaskDecoder();
		this.inputDecoder = new InputDecoder();
		this.failDecoder = new FailDecoder();
		this.retryDecoder = new RetryDecoder();
		this.waitDecoder = new WaitDecoder();
		this.okDecoder = new OkDecoder();
		this.listener = null;
	}

	public boolean decode(final ByteBuffer buffer) {
		final int start = buffer.position();

		if (!headerDecoder.isFullHeader(buffer)) {
			buffer.position(start);
			return false;
		}

		headerDecoder.parse(buffer);
		final int payloadLength = headerDecoder.getPayloadLength();

		if (!isFullPayload(buffer, payloadLength)) { // including terminating newline
			buffer.position(start);
			return false;
		}

		switch (headerDecoder.getMessageName()) {
			case TASK:
				taskDecoder.parse(buffer, payloadLength);
				notifyTask();
				break;
			case INPUT:
				inputDecoder.parse(buffer, payloadLength);
				notifyInput();
				break;
			case OK:
				okDecoder.parse(buffer, payloadLength);
				listener.ok();
				break;
			case FAIL:
				failDecoder.parse(buffer, payloadLength);
				notifyFail();
				break;
			case RETRY:
				retryDecoder.parse(buffer, payloadLength);
				notifyRetry();
				break;
			case WAIT:
				waitDecoder.parse(buffer, payloadLength);
				notifyPause();
				break;
		}

		buffer.position(buffer.position() + 1); // eat ending newline
		return true;
	}

	public DiscoWorkerDecoder setListener(final DiscoWorkerListener listener) {
		this.listener = listener;
		return this;
	}

	private void notifyPause() {
		if (listener != null) {
			listener.pause(waitDecoder.getPauseSeconds());
		}
	}

	private void notifyRetry() {
		if (listener != null) {
			listener.retry(retryDecoder.getReplicas());
		}
	}

	private void notifyFail() {
		if (listener != null) {
			listener.fail(failDecoder.getInputId(), failDecoder.getReplicaIds());
		}
	}

	private void notifyInput() {
		if (listener != null) {
			listener.input(inputDecoder.isDone(), inputDecoder.getInputs());
		}
	}

	private void notifyTask() {
		if (listener != null) {
			listener.task(taskDecoder.getTaskHost(), taskDecoder.getMasterHost(), taskDecoder.getJobName(), taskDecoder.getTaskId(),
			        taskDecoder.getMode(), taskDecoder.getDiscoPort(), taskDecoder.getPutPort(), taskDecoder.getDiscoData(),
			        taskDecoder.getDDFSData(), taskDecoder.getJobFile());
		}
	}

	private boolean isFullPayload(final ByteBuffer buffer, final int payloadLength) {
		return buffer.remaining() >= payloadLength + 1;
	}

}
