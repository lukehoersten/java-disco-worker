package org.discoproject.worker.protocol.encoder;

import java.util.List;

import org.discoproject.worker.protocol.encoder.types.RequestMessageName;

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
		final StringBuilder sb = new StringBuilder("[");
		sb.append(QUOTE);
		sb.append(inputId);
		sb.append(QUOTE);
		sb.append(",");
		sb.append("[");

		for (int i = 0; i < repIds.size(); i++) {
			if (i > 0) {
				sb.append(",");
			}
			sb.append(QUOTE);
			sb.append(repIds.get(i));
			sb.append(QUOTE);
		}

		sb.append("]]");
		setPayload(sb.toString());
		return this;
	}

}
