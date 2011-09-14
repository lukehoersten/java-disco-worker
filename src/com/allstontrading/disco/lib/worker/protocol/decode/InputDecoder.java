package com.allstontrading.disco.lib.worker.protocol.decode;

import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.allstontrading.disco.lib.worker.protocol.decode.types.DiscoInput;
import com.allstontrading.disco.lib.worker.protocol.decode.types.DiscoInputFlag;
import com.allstontrading.disco.lib.worker.protocol.decode.types.DiscoInputReplica;
import com.allstontrading.disco.lib.worker.protocol.decode.types.JsonDiscoInputReplicaDecoder;
import com.allstontrading.disco.lib.worker.protocol.decode.types.DiscoInputStatus;

/**
 * @author Luke Hoersten <lhoersten@allstontrading.com>
 * 
 */
public class InputDecoder {

	private static final int FLAG_INDEX = 0;
	private static final int INPUTS_INDEX = 1;

	private static final int INPUT_ID_INDEX = 0;
	private static final int INPUT_STATUS_INDEX = 1;
	private static final int REPLICAS_INDEX = 2;

	private JSONArray flagInputTuple = null;

	public void parse(final ByteBuffer buffer, final int payloadLength) {
		final String jsonString = new String(buffer.array(), buffer.position(), payloadLength);
		buffer.position(buffer.position() + payloadLength);

		try {
			flagInputTuple = new JSONArray(jsonString);
		}
		catch (final JSONException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean isDone() {
		try {
			return DiscoInputFlag.valueOf(flagInputTuple.getString(FLAG_INDEX)) == DiscoInputFlag.done;
		}
		catch (final JSONException e) {
			throw new RuntimeException(e);
		}
	}

	public List<DiscoInput> getInputs() {
		try {
			return toInputList(flagInputTuple.getJSONArray(INPUTS_INDEX));
		}
		catch (final JSONException e) {
			throw new RuntimeException(e);
		}
		catch (final URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	private List<DiscoInput> toInputList(final JSONArray inputList) throws JSONException, URISyntaxException {
		final LinkedList<DiscoInput> inputs = new LinkedList<DiscoInput>();

		for (int i = 0; i < inputList.length(); i++) {
			inputs.add(toInput(inputList.getJSONArray(i)));
		}
		return inputs;
	}

	private DiscoInput toInput(final JSONArray inputTuple) throws JSONException, URISyntaxException {
		final int inputId = inputTuple.getInt(INPUT_ID_INDEX);
		final DiscoInputStatus status = DiscoInputStatus.valueOf(inputTuple.getString(INPUT_STATUS_INDEX));
		final List<DiscoInputReplica> replicas = JsonDiscoInputReplicaDecoder.toReplicaList(inputTuple.getJSONArray(REPLICAS_INDEX));

		return new DiscoInput(inputId, status, replicas);
	}

}
