package org.discoproject.worker.protocol.decoder;

import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

import org.discoproject.utils.JsonUtils;
import org.discoproject.worker.protocol.decoder.types.DiscoInput;
import org.discoproject.worker.protocol.decoder.types.DiscoInputFlag;
import org.discoproject.worker.protocol.decoder.types.DiscoInputReplica;
import org.discoproject.worker.protocol.decoder.types.DiscoInputStatus;
import org.discoproject.worker.protocol.decoder.types.JsonDiscoInputReplicaDecoder;

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

	private List<String> flagInputArray;

	public void parse(final ByteBuffer buffer, final int payloadLength) {
		final String jsonString = new String(buffer.array(), buffer.position(), payloadLength);
		buffer.position(buffer.position() + payloadLength);
		flagInputArray = JsonUtils.asArray(jsonString);
	}

	public boolean isDone() {
		return DiscoInputFlag.valueOf(JsonUtils.asString(FLAG_INDEX, flagInputArray)) == DiscoInputFlag.done;
	}

	public List<DiscoInput> getInputs() {
		try {
			return toInputList(JsonUtils.asArray(flagInputArray.get(INPUTS_INDEX)));
		}
		catch (final URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	private List<DiscoInput> toInputList(final List<String> inputList) throws URISyntaxException {
		final LinkedList<DiscoInput> inputs = new LinkedList<DiscoInput>();

		for (int i = 0; i < inputList.size(); i++) {
			inputs.add(toInput(JsonUtils.asArray(inputList.get(i))));
		}
		return inputs;
	}

	private DiscoInput toInput(final List<String> inputTuple) throws URISyntaxException {
		final int inputId = JsonUtils.asInteger(INPUT_ID_INDEX, inputTuple);
		final String statusString = JsonUtils.asString(INPUT_STATUS_INDEX, inputTuple);
		final DiscoInputStatus status = DiscoInputStatus.valueOf(statusString);
		final List<DiscoInputReplica> replicas = JsonDiscoInputReplicaDecoder.toReplicaList(JsonUtils.asArray(inputTuple
		        .get(REPLICAS_INDEX)));

		return new DiscoInput(inputId, status, replicas);
	}

}
