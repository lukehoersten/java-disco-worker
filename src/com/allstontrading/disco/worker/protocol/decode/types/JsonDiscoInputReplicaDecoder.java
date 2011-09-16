package com.allstontrading.disco.worker.protocol.decode.types;

import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

public class JsonDiscoInputReplicaDecoder {

	private static final int REPLICA_ID_INDEX = 0;
	private static final int REPLICA_LOCATION_INDEX = 1;

	public static List<DiscoInputReplica> toReplicaList(final JSONArray replicaList) throws JSONException, URISyntaxException {
		final LinkedList<DiscoInputReplica> replicas = new LinkedList<DiscoInputReplica>();

		for (int i = 0; i < replicaList.length(); i++) {
			replicas.add(toReplica(replicaList.getJSONArray(i)));
		}
		return replicas;
	}

	private static DiscoInputReplica toReplica(final JSONArray replicaTuple) throws JSONException, URISyntaxException {
		final int replicaId = replicaTuple.getInt(REPLICA_ID_INDEX);
		final String location = replicaTuple.getString(REPLICA_LOCATION_INDEX);

		return new DiscoInputReplica(replicaId, location);
	}

}
