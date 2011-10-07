package org.discoproject.job.jobpack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Luke Hoersten <lhoersten@allstontrading.com>
 * 
 */
public class DiscoJobPackUtils {

	public static byte[] getJobHomeZip(final File jobHome) {
		if (!jobHome.isDirectory()) {
			return new byte[0];
		}
		File tmpZipFile;
		try {
			tmpZipFile = File.createTempFile("tmpjobhome", "zip");
			DiscoJobPackUtils.zipDirToFile(jobHome, tmpZipFile);
		}
		catch (final IOException e) {
			throw new RuntimeException(e);
		}

		if (tmpZipFile.length() > Integer.MAX_VALUE) {
			throw new RuntimeException("Job home is too big. Must be less than 2 Gigs.");
		}
		final byte[] zipBytes = new byte[(int) tmpZipFile.length()];
		return zipBytes;
	}

	private static void zipDirToFile(final File inputDir, final File outputZipFile) throws IOException {
		final ZipOutputStream outStream = new ZipOutputStream(new FileOutputStream(outputZipFile));
		zipDirToStream(inputDir, outStream);
		outStream.close();
	}

	private static void zipDirToStream(final File inputDir, final ZipOutputStream outStream) throws IOException {
		final File[] files = inputDir.listFiles();
		final byte[] tmpBuf = new byte[1024];

		for (final File file : files) {
			if (file.isDirectory()) {
				zipDirToStream(file, outStream);
				continue;
			}
			final FileInputStream inStream = new FileInputStream(file.getAbsolutePath());
			outStream.putNextEntry(new ZipEntry(file.getAbsolutePath()));
			int len;
			while ((len = inStream.read(tmpBuf)) > 0) {
				outStream.write(tmpBuf, 0, len);
			}
			outStream.closeEntry();
			inStream.close();
		}
	}

}
