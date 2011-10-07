package org.discoproject.worker.protocol.decoder.types;

import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

import org.discoproject.utils.JsonUtils;

public class JsonDiscoInputReplicaDecoder {

	private static final int REPLICA_ID_INDEX = 0;
	private static final int REPLICA_LOCATION_INDEX = 1;

	public static List<DiscoInputReplica> toReplicaList(final List<String> replicaList) throws URISyntaxException {
		final LinkedList<DiscoInputReplica> replicas = new LinkedList<DiscoInputReplica>();

		for (int i = 0; i < replicaList.size(); i++) {
			replicas.add(toReplica(JsonUtils.asArray(replicaList.get(i))));
		}
		return replicas;
	}

	private static DiscoInputReplica toReplica(final List<String> replicaTuple) throws URISyntaxException {
		final int replicaId = JsonUtils.asInteger(REPLICA_ID_INDEX, replicaTuple);
		final String location = JsonUtils.asString(REPLICA_LOCATION_INDEX, replicaTuple);

		return new DiscoInputReplica(replicaId, location);
	}

}
