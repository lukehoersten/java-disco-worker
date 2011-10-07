package org.discoproject.worker.protocol.decoder;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.Map;

import org.discoproject.utils.JsonUtils;
import org.discoproject.worker.protocol.decoder.types.DiscoTaskMode;

/**
 * The Class TaskDecoder.
 * 
 * @author Luke Hoersten <lhoersten@allstontrading.com>
 */
public class TaskDecoder {

	/** The Constant TASK_HOST_KEY. */
	private static final String TASK_HOST_KEY = "host";

	/** The Constant MASTER_HOST_KEY. */
	private static final String MASTER_HOST_KEY = "master";

	/** The Constant JOB_NAME_KEY. */
	private static final String JOB_NAME_KEY = "jobname";

	/** The Constant TASK_ID_KEY. */
	private static final String TASK_ID_KEY = "taskid";

	/** The Constant MODE_KEY. */
	private static final String MODE_KEY = "mode";

	/** The Constant DISCO_PORT_KEY. */
	private static final String DISCO_PORT_KEY = "disco_port";

	/** The Constant PUT_PORT_KEY. */
	private static final String PUT_PORT_KEY = "put_port";

	/** The Constant DISCO_DATA_KEY. */
	private static final String DISCO_DATA_KEY = "disco_data";

	/** The Constant DDFS_DATA_KEY. */
	private static final String DDFS_DATA_KEY = "ddfs_data";

	/** The Constant JOB_FILE_KEY. */
	private static final String JOB_FILE_KEY = "jobfile";

	/** The json object. */
	private Map<String, String> jsonObject = null;

	/**
	 * Parses the.
	 * 
	 * @param buffer the buffer
	 * @param payloadLength the payload length
	 */
	public void parse(final ByteBuffer buffer, final int payloadLength) {
		final String jsonString = new String(buffer.array(), buffer.position(), payloadLength);
		buffer.position(buffer.position() + payloadLength);

		jsonObject = JsonUtils.asObject(jsonString);
	}

	/**
	 * Gets the task host.
	 * 
	 * @return the task host
	 */
	public String getTaskHost() {
		return JsonUtils.asString(TASK_HOST_KEY, jsonObject);
	}

	/**
	 * Gets the master host.
	 * 
	 * @return the master host
	 */
	public String getMasterHost() {
		return JsonUtils.asString(MASTER_HOST_KEY, jsonObject);
	}

	/**
	 * Gets the job name.
	 * 
	 * @return the job name
	 */
	public String getJobName() {
		return JsonUtils.asString(JOB_NAME_KEY, jsonObject);
	}

	/**
	 * Gets the task id.
	 * 
	 * @return the task id
	 */
	public int getTaskId() {
		return JsonUtils.asInteger(TASK_ID_KEY, jsonObject);
	}

	/**
	 * Gets the mode.
	 * 
	 * @return the mode
	 */
	public DiscoTaskMode getMode() {
		return DiscoTaskMode.valueOf(JsonUtils.asString(MODE_KEY, jsonObject));
	}

	/**
	 * Gets the disco port.
	 * 
	 * @return the disco port
	 */
	public int getDiscoPort() {
		return JsonUtils.asInteger(DISCO_PORT_KEY, jsonObject);
	}

	/**
	 * Gets the put port.
	 * 
	 * @return the put port
	 */
	public int getPutPort() {
		return JsonUtils.asInteger(PUT_PORT_KEY, jsonObject);
	}

	/**
	 * Gets the disco data.
	 * 
	 * @return the disco data
	 */
	public File getDiscoData() {
		return new File(JsonUtils.asString(DISCO_DATA_KEY, jsonObject));
	}

	/**
	 * Gets the dDFS data.
	 * 
	 * @return the dDFS data
	 */
	public File getDDFSData() {
		return new File(JsonUtils.asString(DDFS_DATA_KEY, jsonObject));
	}

	/**
	 * Gets the job file.
	 * 
	 * @return the job file
	 */
	public File getJobFile() {
		return new File(JsonUtils.asString(JOB_FILE_KEY, jsonObject));
	}

}
