package ru.terekhov.book2read.utils;

import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;


public class CatalogDownloader {
	
	private static final int MAX_BUFFER_SIZE = 1024;
	
	public static final String STATUSES[]={"Downloading","Paused","Complete","Cancelled","Error"};
	
	public static final int DOWNLOADING = 0;
	public static final int PAUSED = 1;
	public static final int COMPLETE = 2;
	public static final int CANCELLED = 3;
	public static final int ERROR = 4;
	
	private URL url;
	private int size;
	private int downloaded;
	private int status;
	
	private BookFetcherProperty prop;
	
	public CatalogDownloader() {}
	
	public void initialize() {
		setSize(-1);
		downloaded = 0;
		setStatus(DOWNLOADING);		
	}
	

	/**
	 * @return the url
	 */
	public URL getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(URL url) {
		this.url = url;
	}

	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	
	public float getProgress() {
		return ((float) downloaded / size) * 100;
	}
	
	private void error(String e) {
		status = ERROR;
		System.out.println(e);
	}
	
	private String getFileName(URL url) {
		String fileName = url.getFile();
		return fileName.substring(fileName.lastIndexOf('/') + 1);
	}
	
	
	public void startDownload() {
		RandomAccessFile file = null;
		InputStream stream = null;
		
		prop = new BookFetcherProperty();
		prop.initialize();
		
		try {
			// TODO: Возможно, нужно создать и настроить proxy-объект
			
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("pmzproxy", 3128));
		    Authenticator authenticator = new Authenticator() {

		        public PasswordAuthentication getPasswordAuthentication() {
		            return (new PasswordAuthentication(prop.getUserName(),
		                    prop.getPassword().toCharArray()));
		        }
		    };
		    Authenticator.setDefault(authenticator);			
			HttpURLConnection connection = (HttpURLConnection)url.openConnection(proxy);
			
			connection.setRequestProperty("Range", "bytes=" + downloaded + "-");
			connection.connect();
			
			if (connection.getResponseCode() / 100 != 2) {
				error("Код ответа не равен 200");
			}
			
			int contentLength = connection.getContentLength();
			if (contentLength < 1) {
				error("Длина содержимого страницы неверна (<1)");
			}
			
			if (size == -1) {
				size = contentLength;
			}
			
			file = new RandomAccessFile(getFileName(url), "rw");
			file.seek(downloaded);
			
			stream = connection.getInputStream();
			while (status == DOWNLOADING) {
				byte buffer[];
				if (size - downloaded > MAX_BUFFER_SIZE) {
					buffer = new byte[MAX_BUFFER_SIZE];
				} else {
					buffer = new byte[size - downloaded];
				}
				
				int read = stream.read(buffer);
				if (read == -1) {
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
					file.close();
				} catch (Exception e) {}
				
				if (stream != null) {
					try {
						stream.close();
					} catch (Exception e) {}
				}
			}
		}
	}
}
