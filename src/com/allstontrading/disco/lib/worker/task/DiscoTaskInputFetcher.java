package com.allstontrading.disco.lib.worker.task;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

import com.allstontrading.disco.lib.worker.protocol.DiscoIOChannel;
import com.allstontrading.disco.lib.worker.protocol.decode.types.DiscoInput;
import com.allstontrading.disco.lib.worker.protocol.decode.types.DiscoInputReplica;
import com.allstontrading.disco.lib.worker.protocol.encode.InputErrorEncoder;
import com.allstontrading.disco.lib.worker.protocol.encode.RequestInputsEncoder;

public class DiscoTaskInputFetcher {

	private static final String URL_FORMAT = "{0}://{1}:{2}/{3}?{4}#{5}";

	private final RequestInputsEncoder requestInputEncoder;
	private final InputErrorEncoder inputErrorEncoder;

	private final DiscoIOChannel discoIOChannel;
	private final List<InputStream> inputs;
	private final int discoPort;

	public DiscoTaskInputFetcher(final DiscoIOChannel discoIOChannel, final int discoPort) {
		this.discoIOChannel = discoIOChannel;
		this.inputs = new LinkedList<InputStream>();
		this.discoPort = discoPort;

		this.requestInputEncoder = new RequestInputsEncoder();
		this.inputErrorEncoder = new InputErrorEncoder();
	}

	public List<InputStream> getInputs() {
		if (inputs.isEmpty()) {
			requestInput();
		}
		return inputs;
	}

	public void input(final boolean isDone, final List<DiscoInput> inputs) {
		if (!isDone || inputs.isEmpty()) {
			// TODO payload: "['exclude', [input_id]]"
			requestInput();
		}
		else {
			fetchInputs(inputs);
		}
	}

	public void fail(final int inputId, final List<Integer> replicaIds) {
		// TODO error handling incomplete
	}

	public void retry(final List<DiscoInputReplica> replicas) {
		// TODO error handling incomplete
	}

	public void pause(final int seconds) {
		// TODO error handling incomplete
	}

	private void requestInput() {
		try {
			discoIOChannel.send(requestInputEncoder);
		}
		catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void fetchInputs(final List<DiscoInput> inputs) {
		for (final DiscoInput input : inputs) {
			fetchInput(input);
		}
	}

	private void fetchInput(final DiscoInput input) {
		switch (input.getStatus()) {
			case ok:
				fetchReplicas(input);
				break;
			case busy:
				requestInput();
				break;
			case failed:
				// TODO just fail the task if you get failed status
				// TODO error();
				break;
		}
	}

	private void fetchReplicas(final DiscoInput input) {
		for (final DiscoInputReplica replica : input.getReplicas()) {
			fetchReplica(replica);
		}
	}

	private void fetchReplica(final DiscoInputReplica replica) {
		// TODO One important optimization is to use the local filesystem instead of HTTP for accessing inputs when they are local. This can
		// be determined by comparing the URL hostname with the host specified in the TASK response, and then converting the URL path into a
		// filesystem path using the disco_data or ddfs_data path prefixes for URL paths beginning with disco/ and ddfs/ respectively.

		switch (replica.getScheme()) {
			case disco:
				addDiscoURLInputStream(replica);
				break;
			case http:
				addHttpURLInputStream(replica);
				break;
			case raw:
				addRawURLInputStream(replica);
				break;
		}
	}

	/**
	 * URL itself (minus the scheme) is the data for the task.
	 * 
	 * @param replica
	 */
	private void addRawURLInputStream(final DiscoInputReplica replica) {
		final String dataString = replica.getURI().toString().split("://")[1];
		inputs.add(new ByteArrayInputStream(dataString.getBytes()));
	}

	/**
	 * Access data through HTTP URL as-is.
	 * 
	 * @param replica
	 */
	private void addHttpURLInputStream(final DiscoInputReplica replica) {
		try {
			inputs.add(replica.getURI().toURL().openStream());
		}
		catch (final IOException e) {
			throw new RuntimeException(e);
			// TODO error();
		}
	}

	/**
	 * Accessed using HTTP at the disco_port specified in the TASK response.
	 * 
	 * @param replica
	 */
	private void addDiscoURLInputStream(final DiscoInputReplica replica) {
		try {
			final URL discoLocation = insertDiscoPort(replica.getURI());
			inputs.add(discoLocation.openStream());
		}
		catch (final IOException e) {
			throw new RuntimeException(e);
			// TODO error();
		}
	}

	/**
	 * Insert the disco port after the hostname but before folders
	 * 
	 * @param location
	 * @return
	 * @throws MalformedURLException
	 */
	private URL insertDiscoPort(final URI uri) throws MalformedURLException {
		return new URL(MessageFormat.format(URL_FORMAT, uri.getScheme(), uri.getAuthority(), discoPort, uri.getPath(), uri.getQuery(),
		        uri.getFragment()));
	}

	private void inputError(final String inputId, final List<String> repIds) {
		try {
			discoIOChannel.send(inputErrorEncoder.set(inputId, repIds));
		}
		catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

}
