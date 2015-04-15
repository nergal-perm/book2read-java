package ru.terekhov.book2read.model;

public class Book {
	
	/**
	 * Хранит название книги
	 */
	private String title;

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * Хранит автора книги
	 */
	private Author author;
	
	/**
	 * @return the author
	 */
	public Author getAuthor() {
		return author;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(Author author) {
		this.author = author;
	}

	private String id;
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}	

	private boolean isRead;

	/**
	 * @return the isRead
	 */
	public boolean isRead() {
		return isRead;
	}

	/**
	 * @param isRead the isRead to set
	 */
	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}	
	
	public Book(String title, Author author) {
		setTitle(title);
		setAuthor(author);
	}
	
	public Book(String id, String title, Author author) {
		setId(id);
		setTitle(title);
		setAuthor(author);
	}




	
}
