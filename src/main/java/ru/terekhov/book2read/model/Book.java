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


	public Book(String title, Author author) {
		setTitle(title);
		setAuthor(author);
	}
	
}
