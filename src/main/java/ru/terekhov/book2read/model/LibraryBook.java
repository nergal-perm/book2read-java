package ru.terekhov.book2read.model;

public class LibraryBook extends AbstractBook {
	
	private int pagesCount;
	
	/**
	 * @return the pagesCount
	 */
	public int getPagesCount() {
		return pagesCount;
	}

	/**
	 * @param pagesCount the pagesCount to set
	 */
	public void setPagesCount(int pagesCount) {
		this.pagesCount = pagesCount;
	}
	
	public LibraryBook(String title, String author) {
		setTitle(title);
		setAuthor(author);
	}
	
	public LibraryBook(String id, String title, String author) {
		setId(id);
		setTitle(title);
		setAuthor(author);
	}
	
	public LibraryBook() {
		
	}
	
}
