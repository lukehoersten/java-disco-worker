package com.allstontrading.disco.worker.protocol.decode;

import java.io.File;
import java.nio.ByteBuffer;

import org.json.JSONException;
import org.json.JSONObject;

import com.allstontrading.disco.worker.protocol.decode.types.DiscoTaskMode;

/**
 * @author Luke Hoersten <lhoersten@allstontrading.com>
 * 
 */
public class TaskDecoder {

	private static final String TASK_HOST_KEY = "host";
	private static final String MASTER_HOST_KEY = "master";
	private static final String JOB_NAME_KEY = "jobname";
	private static final String TASK_ID_KEY = "taskid";
	private static final String MODE_KEY = "mode";
	private static final String DISCO_PORT_KEY = "disco_port";
	private static final String PUT_PORT_KEY = "put_port";
	private static final String DISCO_DATA_KEY = "disco_data";
	private static final String DDFS_DATA_KEY = "ddfs_data";
	private static final String JOB_FILE_KEY = "jobfile";

	private JSONObject jsonObject = null;

	public void parse(final ByteBuffer buffer, final int payloadLength) {
		final String jsonString = new String(buffer.array(), buffer.position(), payloadLength);
		buffer.position(buffer.position() + payloadLength);

		try {
			jsonObject = new JSONObject(jsonString);
		}
		catch (final JSONException e) {
			throw new RuntimeException(e);
		}
	}

	public String getTaskHost() {
		try {
			return jsonObject.getString(TASK_HOST_KEY);
		}
		catch (final JSONException e) {
			throw new RuntimeException(e);
		}
	}

	public String getMasterHost() {
		try {
			return jsonObject.getString(MASTER_HOST_KEY);
		}
		catch (final JSONException e) {
			throw new RuntimeException(e);
		}
	}

	public String getJobName() {
		try {
			return jsonObject.getString(JOB_NAME_KEY);
		}
		catch (final JSONException e) {
			throw new RuntimeException(e);
		}
	}

	public int getTaskId() {
		try {
			return jsonObject.getInt(TASK_ID_KEY);
		}
		catch (final JSONException e) {
			throw new RuntimeException(e);
		}
	}

	public DiscoTaskMode getMode() {
		try {
			return DiscoTaskMode.valueOf(jsonObject.getString(MODE_KEY));
		}
		catch (final JSONException e) {
			throw new RuntimeException(e);
		}
	}

	public int getDiscoPort() {
		try {
			return jsonObject.getInt(DISCO_PORT_KEY);
		}
		catch (final JSONException e) {
			throw new RuntimeException(e);
		}
	}

	public int getPutPort() {
		try {
			return jsonObject.getInt(PUT_PORT_KEY);
		}
		catch (final JSONException e) {
			throw new RuntimeException(e);
		}
	}

	public File getDiscoData() {
		try {
			return new File(jsonObject.getString(DISCO_DATA_KEY));
		}
		catch (final JSONException e) {
			throw new RuntimeException(e);
		}
	}

	public File getDDFSData() {
		try {
			return new File(jsonObject.getString(DDFS_DATA_KEY));
		}
		catch (final JSONException e) {
			throw new RuntimeException(e);
		}
	}

	public File getJobFile() {
		try {
			return new File(jsonObject.getString(JOB_FILE_KEY));
		}
		catch (final JSONException e) {
			throw new RuntimeException(e);
		}
	}

}
