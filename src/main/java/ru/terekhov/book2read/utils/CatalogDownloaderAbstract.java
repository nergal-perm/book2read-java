package ru.terekhov.book2read.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.enterprise.inject.Model;

@Model
public abstract class CatalogDownloaderAbstract {

	private static final int MAX_BUFFER_SIZE = 1024;

	protected static final String STATUSES[] = { "Downloading", "Paused", "Complete", "Cancelled",
			"Error" };

	protected static final int DOWNLOADING = 0;
	protected static final int PAUSED = 1;
	protected static final int COMPLETE = 2;
	protected static final int CANCELLED = 3;
	protected static final int ERROR = 4;

	protected int size;
	protected int downloaded;
	protected int status;

	protected void initialize() {
		setSize(-1);
		downloaded = 0;
		setStatus(DOWNLOADING);
	}

	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	protected void setSize(int size) {
		this.size = size;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	protected void setStatus(int status) {
		this.status = status;
	}

	public float getProgress() {
		return ((float) downloaded / size) * 100;
	}

	protected void error(String e) {
		status = ERROR;
		System.out.println(e);
	}

	// Downloading catalog
	//------------------------------------------------------
	public byte[] getCatalog() {
		return unzipCatalog(getZippedBytes());
	}

	public byte[] unzipCatalog(byte[] zippedStream) {
		byte[] buffer = new byte[MAX_BUFFER_SIZE];
		byte[] retVal = new byte[] {};

		try {
			ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zippedStream));
			ZipEntry ze = zis.getNextEntry();

			ByteArrayOutputStream out = new ByteArrayOutputStream();

			while (ze != null) {
				if (!ze.getName().equals("catalog.txt")) {
					ze = zis.getNextEntry();
					continue;
				}

				int len;
				while ((len = zis.read(buffer)) > 0) {
					out.write(buffer, 0, len);
				}

				retVal = out.toByteArray();
				out.close();
				break;
			}

			zis.closeEntry();
			zis.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return retVal;

	}

	public byte[] getZippedBytes() {
		ByteArrayOutputStream file = null;
		InputStream stream = null;
		byte[] retVal = new byte[] {};

		initialize();
		
		try {
			file = new ByteArrayOutputStream();
			stream = getInputStream();
			while (status == DOWNLOADING) {
				byte buffer[];
				if (size - downloaded > MAX_BUFFER_SIZE) {
					buffer = new byte[MAX_BUFFER_SIZE];
				} else {
					buffer = new byte[size - downloaded];
				}

				int read = stream.read(buffer);
				if (read == -1 || buffer.length == 0) {
					break;
				}

				file.write(buffer, 0, read);
				downloaded += read;
			}

			if (status == DOWNLOADING) {
				status = COMPLETE;
			}
		} catch (Exception e) {
			error(e.getMessage());
		} finally {
			if (file != null) {
				try {
					retVal = file.toByteArray();
					file.close();
				} catch (Exception e) {
				}

				if (stream != null) {
					try {
						stream.close();
					} catch (Exception e) {
					}
				}
			}
		}
		return retVal;
	}

	protected abstract InputStream getInputStream() throws IOException;
	
	// Parsing book data
	//------------------------------------------------------
	
}
