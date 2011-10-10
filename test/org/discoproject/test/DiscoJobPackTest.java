package org.discoproject.test;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

import org.discoproject.job.jobpack.DiscoJobPack;
import org.discoproject.utils.JsonUtils;
import org.junit.Test;

public class DiscoJobPackTest {

	@Test
	public void testDiscoJobPack() throws URISyntaxException {

		final DiscoJobPack jobPack = new DiscoJobPack("jobName", new File("jobPackFile.txt"), true, true, new File("jobHome"), new byte[] {
		        1, 2, 3, 4 });

		jobPack.addEnvVar("env1", "value1");
		jobPack.addEnvVar("env2", "value2");

		jobPack.addInput(new URI("raw://therawuriinput"));
		jobPack.addInput(new URI("raw://therawuriinput2"));

		final ByteBuffer buffer = ByteBuffer.allocate(1024);
		jobPack.write(buffer);
		buffer.flip();

		buffer.getInt(); // magic!
		final int jobDictOffset = buffer.getInt();
		final int jobEnvsOffset = buffer.getInt();
		final int jobHomeOffset = buffer.getInt();

		buffer.position(128);
		final String dictJson = new String(buffer.array(), jobDictOffset, jobEnvsOffset - jobDictOffset);
		final String envsJson = new String(buffer.array(), jobEnvsOffset, jobHomeOffset - jobEnvsOffset);

		final Map<String, String> dictObj = JsonUtils.asObject(dictJson);
		assertEquals("jobPackFile.txt", JsonUtils.asString("worker", dictObj));
		assertEquals("true", JsonUtils.asString("map?", dictObj));
		assertEquals("true", JsonUtils.asString("reduce?", dictObj));
		assertEquals("1", JsonUtils.asString("nr_reduces", dictObj));
		assertEquals("jobName", JsonUtils.asString("prefix", dictObj));
		assertEquals("max_cores", JsonUtils.asString("scheduler", dictObj));

		final List<String> inputs = JsonUtils.asArray(dictObj.get("input"));
		assertEquals(2, inputs.size());
		assertEquals("raw://therawuriinput", JsonUtils.asString(0, inputs));
		assertEquals("raw://therawuriinput2", JsonUtils.asString(1, inputs));
		final Map<String, String> envObj = JsonUtils.asObject(envsJson);
		assertEquals("value1", JsonUtils.asString("env1", envObj));
		assertEquals("value2", JsonUtils.asString("env2", envObj));
	}

}
