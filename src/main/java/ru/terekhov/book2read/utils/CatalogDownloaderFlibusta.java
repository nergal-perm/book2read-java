package ru.terekhov.book2read.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;

public class CatalogDownloaderFlibusta extends CatalogDownloaderAbstract {

	private URL url;	
	

	
	public CatalogDownloaderFlibusta() {
		try {
			super.initialize();
			this.url = new URL("http://flibusta.net/catalog/catalog.zip");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	protected InputStream getInputStream() throws IOException {
		final BookFetcherProperty prop = new BookFetcherProperty(); 
		prop.initialize();

			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("pmzproxy", 3128));
			Authenticator authenticator = new Authenticator() {

				public PasswordAuthentication getPasswordAuthentication() {
					return (new PasswordAuthentication(prop.getUserName(), prop.getPassword()
							.toCharArray()));
				}
			};
			Authenticator.setDefault(authenticator);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);

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
			return connection.getInputStream();
	}

}
