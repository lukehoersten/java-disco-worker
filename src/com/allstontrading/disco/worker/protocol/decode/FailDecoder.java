package com.allstontrading.disco.worker.protocol.decode;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * @author Luke Hoersten <lhoersten@allstontrading.com>
 * 
 */
public class FailDecoder {

	private static final int INPUT_ID_INDEX = 0;
	private static final int REPLICA_IDS_INDEX = 1;

	private JSONArray jsonTuple = null;

	public void parse(final ByteBuffer buffer, final int payloadLength) {
		final String jsonString = new String(buffer.array(), buffer.position(), payloadLength);
		buffer.position(buffer.position() + payloadLength);

		try {
			jsonTuple = new JSONArray(jsonString);
		}
		catch (final JSONException e) {
			throw new RuntimeException(e);
		}
	}

	public int getInputId() {
		try {
			return jsonTuple.getInt(INPUT_ID_INDEX);
		}
		catch (final JSONException e) {
			throw new RuntimeException(e);
		}
	}

	public List<Integer> getReplicaIds() {
		try {
			return toReplicaList(jsonTuple.getJSONArray(REPLICA_IDS_INDEX));
		}
		catch (final JSONException e) {
			throw new RuntimeException(e);
		}
	}

	private List<Integer> toReplicaList(final JSONArray replicaIdList) throws JSONException {
		final List<Integer> replicaIds = new LinkedList<Integer>();

		for (int i = 0; i < replicaIdList.length(); i++) {
			replicaIds.add(replicaIdList.getInt(i));
		}
		return replicaIds;
	}

}
