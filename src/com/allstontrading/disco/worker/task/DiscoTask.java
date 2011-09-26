package com.allstontrading.disco.worker.task;

import java.io.File;
import java.util.List;

import com.allstontrading.disco.DiscoUtils;
import com.allstontrading.disco.worker.protocol.DiscoIOChannel;
import com.allstontrading.disco.worker.protocol.decode.types.DiscoInput;
import com.allstontrading.disco.worker.protocol.decode.types.DiscoInputReplica;

/**
 * @author Luke Hoersten <lhoersten@allstontrading.com>
 * 
 */
public abstract class DiscoTask {

	private static final int HEX = 16;

	private final int taskId;
	private final File workingDir;
	private final DiscoTaskInputFetcher inputFetcher;

	public DiscoTask(final DiscoIOChannel discoIOChannel, final int taskId, final int discoPort) {
		this.taskId = taskId;
		this.inputFetcher = new DiscoTaskInputFetcher(discoIOChannel, discoPort);

		this.workingDir = new File(getWorkingDirName());
		this.workingDir.mkdirs();
	}

	public File getWorkingDir() {
		return workingDir;
	}

	private String getWorkingDirName() {
		final String timehash = Long.toString(System.currentTimeMillis(), HEX);
		return taskId + "_" + getTaskTypeName() + "_pid" + DiscoUtils.getPid() + "_time" + timehash;
	}

	protected abstract String getTaskTypeName();

	protected DiscoTaskInputFetcher getInputFetcher() {
		return inputFetcher;
	}

	public void ok() {}

	public void input(final boolean isDone, final List<DiscoInput> inputs) {
		inputFetcher.input(isDone, inputs);
	}

	public void fail(final int inputId, final List<Integer> replicaIds) {
		inputFetcher.fail(inputId, replicaIds);
	}

	public void retry(final List<DiscoInputReplica> replicas) {
		inputFetcher.retry(replicas);
	}

	public void pause(final int seconds) {
		inputFetcher.pause(seconds);
	}

}
