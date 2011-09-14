package com.allstontrading.disco.lib.worker.task;

import java.io.InputStream;
import java.util.List;

import com.allstontrading.disco.lib.worker.protocol.DiscoIOChannel;

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

	public List<InputStream> getReduceInputs() {
		return getInputFetcher().getInputs();
	}

}
