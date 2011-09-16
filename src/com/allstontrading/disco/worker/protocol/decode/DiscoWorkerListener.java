package com.allstontrading.disco.worker.protocol.decode;

import java.io.File;
import java.util.List;

import com.allstontrading.disco.worker.protocol.decode.types.DiscoInput;
import com.allstontrading.disco.worker.protocol.decode.types.DiscoInputReplica;
import com.allstontrading.disco.worker.protocol.decode.types.DiscoTaskMode;

/**
 * @author Luke Hoersten <lhoersten@allstontrading.com>
 * 
 */
public interface DiscoWorkerListener {

	public void task(String taskHost, String masterHost, String jobName, int taskId, DiscoTaskMode taskMode, int discoPort, int putPort,
	        File discoData, File ddfsData, File jobFile);

	public void ok();

	public void input(boolean isDone, List<DiscoInput> inputs);

	public void fail(int inputId, List<Integer> replicaIds);

	public void retry(List<DiscoInputReplica> replicas);

	public void pause(int seconds);

}
