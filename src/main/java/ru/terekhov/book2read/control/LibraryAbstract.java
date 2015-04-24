package ru.terekhov.book2read.control;

import ru.terekhov.book2read.model.AbstractBook;

public abstract class LibraryAbstract {

	private int bookCount;
	private String catalogSourceURL;
	
	
	public AbstractBook getRandomBook() {
		return null;
	}
	
	public void update() {
		
	}

	/**
	 * @return the bookCount
	 */
	public int getBookCount() {
		return bookCount;
	}

	/**
	 * @return the catalogSourceURL
	 */
	protected String getCatalogSourceURL() {
		return catalogSourceURL;
	}

	/**
	 * @param catalogSourceURL the catalogSourceURL to set
	 */
	protected void setCatalogSourceURL(String catalogSourceURL) {
		this.catalogSourceURL = catalogSourceURL;
	}

}
