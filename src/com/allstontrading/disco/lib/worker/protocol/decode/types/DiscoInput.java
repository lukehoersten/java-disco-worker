package com.allstontrading.disco.lib.worker.protocol.decode.types;

import java.util.List;

/**
 * @author Luke Hoersten <lhoersten@allstontrading.com>
 * 
 */
public class DiscoInput {

	private final int id;
	private final DiscoInputStatus status;
	private final List<DiscoInputReplica> replicas;

	public DiscoInput(final int id, final DiscoInputStatus status, final List<DiscoInputReplica> replicas) {
		this.id = id;
		this.status = status;
		this.replicas = replicas;
	}

	public int getId() {
		return id;
	}

	public DiscoInputStatus getStatus() {
		return status;
	}

	public List<DiscoInputReplica> getReplicas() {
		return replicas;
	}

}
