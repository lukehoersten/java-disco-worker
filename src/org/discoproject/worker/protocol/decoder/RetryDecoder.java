package org.discoproject.worker.protocol.decoder;

import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.List;

import org.discoproject.utils.JsonUtils;
import org.discoproject.worker.protocol.decoder.types.DiscoInputReplica;
import org.discoproject.worker.protocol.decoder.types.JsonDiscoInputReplicaDecoder;

/**
 * @author Luke Hoersten <lhoersten@allstontrading.com>
 * 
 */
public class RetryDecoder {

	private List<String> replicaJsonList = null;

	public void parse(final ByteBuffer buffer, final int payloadLength) {
		final String jsonString = new String(buffer.array(), buffer.position(), payloadLength);
		buffer.position(buffer.position() + payloadLength);
		replicaJsonList = JsonUtils.asArray(jsonString);
	}

	public List<DiscoInputReplica> getReplicas() {
		try {
			return JsonDiscoInputReplicaDecoder.toReplicaList(replicaJsonList);
		}
		catch (final URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

}
