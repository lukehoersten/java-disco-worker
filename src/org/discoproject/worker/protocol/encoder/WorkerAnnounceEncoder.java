package org.discoproject.worker.protocol.encoder;

import org.discoproject.worker.protocol.encoder.types.RequestMessageName;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * @author Luke Hoersten <lhoersten@allstontrading.com>
 * 
 */
public class WorkerAnnounceEncoder extends AbstractDiscoWorkerEncoder {

	private static final String VERSION_KEY = "version";
	private static final String PID_KEY = "pid";

	private final JSONObject jsonObject;

	public WorkerAnnounceEncoder() {
		super(RequestMessageName.WORKER);
		this.jsonObject = new JSONObject();
	}

	public WorkerAnnounceEncoder set(final String version, final int pid) {
		try {
			jsonObject.put(VERSION_KEY, version);
			jsonObject.put(PID_KEY, pid);
			setPayload(jsonObject.toString());
		}
		catch (final JSONException e) {
			throw new RuntimeException(e);
		}
		return this;
	}

}
