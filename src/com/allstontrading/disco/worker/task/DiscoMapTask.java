package com.allstontrading.disco.worker.task;

import java.io.InputStream;
import java.util.List;

import com.allstontrading.disco.worker.protocol.DiscoIOChannel;

/**
 * @author Luke Hoersten <lhoersten@allstontrading.com>
 * 
 */
public class DiscoMapTask extends DiscoTask {

	public DiscoMapTask(final DiscoIOChannel discoIOChannel, final int taskId, final int discoPort) {
		super(discoIOChannel, taskId, discoPort);
	}

	@Override
	protected String getTaskTypeName() {
		return "map";
	}

	public InputStream getMapInput() {
		final List<InputStream> inputs = getInputFetcher().getInputs();
		if (inputs.size() != 1) {
			throw new RuntimeException("Map task can only have one input");
		}
		return inputs.get(0);
	}

}
