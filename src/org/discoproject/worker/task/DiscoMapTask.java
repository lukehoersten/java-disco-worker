package org.discoproject.worker.task;

import java.nio.channels.ReadableByteChannel;
import java.util.List;

import org.discoproject.worker.protocol.DiscoIOChannel;

/**
 * @author Luke Hoersten <lhoersten@allstontrading.com>
 * 
 */
public class DiscoMapTask extends DiscoTask {

	public DiscoMapTask(final DiscoIOChannel discoIOChannel, final String jobName, final int taskId, final int discoPort) {
		super(discoIOChannel, jobName, taskId, discoPort);
	}

	@Override
	protected String getTaskTypeName() {
		return "map";
	}

	public ReadableByteChannel getMapInput() {
		final List<ReadableByteChannel> inputs = getInputFetcher().getInputs();
		if (inputs.size() != 1) {
			throw new RuntimeException("Map task can only have one input");
		}
		return inputs.get(0);
	}

}
