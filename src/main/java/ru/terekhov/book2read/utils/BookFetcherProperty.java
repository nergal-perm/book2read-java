package ru.terekhov.book2read.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;


@Singleton
public class BookFetcherProperty implements IBookFetcher {
	
	private Properties properties; // Ссылка на свойства, впоследствии нужно заменить на БД
	private int currentLibBookNumber; // Номер текущей книги библиотеки 
	
	@PostConstruct
	public void initialize() {
		InputStream in = BookParser.class.getResourceAsStream("/resources/application.properties");
		try {
			properties = new Properties();
			properties.load(in);
			in.close();
			currentLibBookNumber = 1;
		} catch (IOException e) {
			System.out.println(e.getMessage());
			properties = new Properties();
		}
	}
	
	@Override
	public String getNextBookInfo() {
		if (properties.containsKey("libBook"+currentLibBookNumber)) {
			String retVal = properties.getProperty("libBook"+currentLibBookNumber);
			currentLibBookNumber++;
			return retVal;
		} else {
			return "";
		}		
	}
	
	public String getUserName() {
		if (properties.containsKey("proxyUserName")) {
			return properties.getProperty("proxyUserName");
		} else {
			return "";
		}
	}
	
	public String getPassword() {
		if (properties.containsKey("proxyPassword")) {
			return properties.getProperty("proxyPassword");
		} else {
			return "";
		}
			
	}
}
