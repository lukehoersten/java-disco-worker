package com.allstontrading.disco.lib.worker.protocol.decode;

import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.allstontrading.disco.lib.worker.protocol.decode.types.DiscoInputReplica;
import com.allstontrading.disco.lib.worker.protocol.decode.types.JsonDiscoInputReplicaDecoder;

/**
 * @author Luke Hoersten <lhoersten@allstontrading.com>
 * 
 */
public class RetryDecoder {

	private JSONArray replicaJsonList = null;

	public void parse(final ByteBuffer buffer, final int payloadLength) {
		final String jsonString = new String(buffer.array(), buffer.position(), payloadLength);
		buffer.position(buffer.position() + payloadLength);

		try {
			replicaJsonList = new JSONArray(jsonString);
		}
		catch (final JSONException e) {
			throw new RuntimeException(e);
		}
	}

	public List<DiscoInputReplica> getReplicas() {
		try {
			return JsonDiscoInputReplicaDecoder.toReplicaList(replicaJsonList);
		}
		catch (final JSONException e) {
			throw new RuntimeException(e);
		}
		catch (final URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

}
