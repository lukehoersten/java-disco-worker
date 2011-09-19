package com.allstontrading.disco.worker.task;

import java.nio.channels.ReadableByteChannel;
import java.util.List;

import com.allstontrading.disco.worker.protocol.DiscoIOChannel;

/**
 * @author Luke Hoersten <lhoersten@allstontrading.com>
 * 
 */
public class DiscoReduceTask extends DiscoTask {

	public DiscoReduceTask(final DiscoIOChannel discoIOChannel, final int taskId, final int discoPort) {
		super(discoIOChannel, taskId, discoPort);
	}

	@Override
	protected String getTaskTypeName() {
		return "reduce";
	}

	public List<ReadableByteChannel> getReduceInputs() {
		return getInputFetcher().getInputs();
	}

}
