package org.discoproject.worker.protocol.encoder;

import org.discoproject.worker.protocol.encoder.types.RequestMessageName;

/**
 * @author Luke Hoersten <lhoersten@allstontrading.com>
 * 
 */
public class WorkerAnnounceEncoder extends AbstractDiscoWorkerEncoder {

	private static final String VERSION_KEY = "version";
	private static final String PID_KEY = "pid";

	public WorkerAnnounceEncoder() {
		super(RequestMessageName.WORKER);
	}

	public WorkerAnnounceEncoder set(final String version, final int pid) {
		final StringBuilder sb = new StringBuilder("{");

		sb.append(QUOTE);
		sb.append(PID_KEY);
		sb.append(QUOTE);
		sb.append(":");
		sb.append(pid);
		sb.append(",");
		sb.append(QUOTE);
		sb.append(VERSION_KEY);
		sb.append(QUOTE);
		sb.append(":");
		sb.append(QUOTE);
		sb.append(version);
		sb.append(QUOTE);
		sb.append("}");
		setPayload(sb.toString());
		return this;
	}

}
