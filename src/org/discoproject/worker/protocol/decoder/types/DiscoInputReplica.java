package org.discoproject.worker.protocol.decoder.types;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Luke Hoersten <lhoersten@allstontrading.com>
 * 
 */
public class DiscoInputReplica {

	private final int id;
	private final URI uri;

	public DiscoInputReplica(final int id, final URI uri) {
		this.id = id;
		this.uri = uri;
	}

	public DiscoInputReplica(final int id, final String uri) throws URISyntaxException {
		this(id, new URI(uri));
	}

	public int getId() {
		return id;
	}

	public URI getURI() {
		return uri;
	}

	public DiscoInputReplicaProtocol getScheme() {
		return DiscoInputReplicaProtocol.valueOf(uri.getScheme());
	}

}
