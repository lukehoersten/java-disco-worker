package org.discoproject.job.jobpack;

import java.io.File;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.discoproject.utils.JsonUtils;
import org.discoproject.worker.protocol.decoder.types.DiscoInputReplica;

/**
 * @author Luke Hoersten <lhoersten@allstontrading.com>
 * 
 */
public class DiscoJobPack {

	private static final int HEADER_SIZE = 128;
	private static final int MAGIC_NUM = (0xd5c0 << 16) + 0x0001;

	private static final String INPUT_KEY = "input";
	private static final String WORKER_KEY = "worker";
	private static final String HAS_MAP_KEY = "map?";
	private static final String HAS_REDUCE_KEY = "reduce?";
	private static final String NUM_REDUCES_KEY = "nr_reduces";
	private static final String PREFIX_KEY = "prefix";
	private static final String SCHEDULER_KEY = "scheduler";
	private static final String OWNER_KEY = "owner";

	private final Map<String, Object> jobDict;
	private final Map<String, Object> jobEnvs;

	private final File jobHome;
	private final byte[] jobData;

	/**
	 * Instantiates a new disco job pack.
	 * 
	 * @param jobName the job name
	 * @param worker the worker
	 * @param hasMapPhase the has map phase
	 * @param hasReducePhase the has reduce phase
	 * @param jobHome the job home
	 * @param jobData the job data
	 */
	public DiscoJobPack(final String jobName, final File worker, final boolean hasMapPhase, final boolean hasReducePhase,
	        final File jobHome, final byte[] jobData) {
		this.jobDict = new LinkedHashMap<String, Object>();
		this.jobEnvs = new LinkedHashMap<String, Object>();
		this.jobHome = jobHome;
		this.jobData = jobData;

		this.jobDict.put(INPUT_KEY, new ArrayList<Object>());
		this.jobDict.put(WORKER_KEY, worker.getName());
		this.jobDict.put(HAS_MAP_KEY, hasMapPhase);
		this.jobDict.put(HAS_REDUCE_KEY, hasReducePhase);
		this.jobDict.put(NUM_REDUCES_KEY, 1);
		this.jobDict.put(PREFIX_KEY, jobName);
		this.jobDict.put(SCHEDULER_KEY, DiscoScheduler.max_cores.toString());
		this.jobDict.put(OWNER_KEY, System.getProperty("user.name"));
	}

	/**
	 * TODO: Add support for {@link DiscoInputReplica}s.
	 * 
	 * @param input
	 */
	@SuppressWarnings("unchecked")
	public void addInput(final URI input) {
		((List<Object>) jobDict.get(INPUT_KEY)).add(input.toASCIIString());
	}

	/**
	 * Adds an env var.
	 * 
	 * @param key the key
	 * @param value the value
	 */
	public void addEnvVar(final String key, final String value) {
		jobEnvs.put(key, value);
	}

	/**
	 * Write the job pack to a buffer
	 * 
	 * @param buffer the buffer
	 * @return number of bytes written to buffer
	 */
	public int write(final ByteBuffer buffer) {
		final byte[] jobDictBytes = JsonUtils.toJsonString(jobDict).getBytes();
		final byte[] jobEnvsBytes = JsonUtils.toJsonString(jobEnvs).getBytes();
		final byte[] jobHomeBytes = DiscoJobPackUtils.getJobHomeZip(jobHome);

		final int jobDictOffset = HEADER_SIZE;
		final int jobEnvsOffset = jobDictOffset + jobDictBytes.length;
		final int jobHomeOffset = jobEnvsOffset + jobEnvsBytes.length;
		final int jobDataOffset = jobHomeOffset + jobHomeBytes.length;
		final int packLength = jobDataOffset + jobData.length;

		if (buffer.capacity() < packLength) {
			throw new RuntimeException("Buffer not large enough for JobPack. Buffer length: " + buffer.capacity() + "; Job Pack length: "
			        + packLength);
		}

		buffer.putInt(MAGIC_NUM);
		buffer.putInt(jobDictOffset);
		buffer.putInt(jobEnvsOffset);
		buffer.putInt(jobHomeOffset);
		buffer.putInt(jobDataOffset);

		buffer.position(HEADER_SIZE);

		buffer.put(jobDictBytes);
		buffer.put(jobEnvsBytes);
		buffer.put(jobHomeBytes);
		buffer.put(jobData);

		return packLength;
	}

}
