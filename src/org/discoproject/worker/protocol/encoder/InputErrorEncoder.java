package org.discoproject.worker.protocol.encoder;

import java.util.List;

import org.discoproject.worker.protocol.encoder.types.RequestMessageName;
import org.json.JSONArray;


/**
 * @author Luke Hoersten <lhoersten@allstontrading.com>
 * 
 */
public class InputErrorEncoder extends AbstractDiscoWorkerEncoder {

	public InputErrorEncoder() {
		super(RequestMessageName.INPUT_ERR);
	}

	// TODO user {@link Replica} type here
	public InputErrorEncoder set(final String inputId, final List<String> repIds) {
		final JSONArray jsonArray = new JSONArray();
		jsonArray.put(inputId);
		jsonArray.put(repIds);
		setPayload(jsonArray.toString());
		return this;
	}

}
