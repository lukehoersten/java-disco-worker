package org.discoproject.worker.protocol.decoder;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

import org.discoproject.utils.JsonUtils;

/**
 * @author Luke Hoersten <lhoersten@allstontrading.com>
 * 
 */
public class FailDecoder {

	private static final int INPUT_ID_INDEX = 0;
	private static final int REPLICA_IDS_INDEX = 1;

	private List<String> jsonTuple = null;

	public void parse(final ByteBuffer buffer, final int payloadLength) {
		final String jsonString = new String(buffer.array(), buffer.position(), payloadLength);
		buffer.position(buffer.position() + payloadLength);
		jsonTuple = JsonUtils.asArray(jsonString);
	}

	public int getInputId() {
		return JsonUtils.asInteger(INPUT_ID_INDEX, jsonTuple);
	}

	public List<Integer> getReplicaIds() {
		return toReplicaList(JsonUtils.asArray(jsonTuple.get(REPLICA_IDS_INDEX)));
	}

	private List<Integer> toReplicaList(final List<String> replicaIdList) {
		final List<Integer> replicaIds = new LinkedList<Integer>();

		for (int i = 0; i < replicaIdList.size(); i++) {
			replicaIds.add(JsonUtils.asInteger(i, replicaIdList));
		}
		return replicaIds;
	}
}
