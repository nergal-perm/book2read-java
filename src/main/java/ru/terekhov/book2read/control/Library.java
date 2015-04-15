package ru.terekhov.book2read.control;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ru.terekhov.book2read.model.Book;
import ru.terekhov.book2read.utils.BookParser;

@ApplicationScoped
@Named
public class Library {
	
	private List<Book> bookList = new ArrayList<Book>();
	private List<Book> readBooks = new ArrayList<Book>();
	@Inject private BookParser bookDb;
	
	@PostConstruct
	public void initialize() {
		Book nextBook = bookDb.getNextBook();
		
		
		
		while (nextBook != null) {
			getBookList().add(nextBook);
			if (nextBook.isRead()) {
				getReadBooks().add(nextBook);
			}
			nextBook = bookDb.getNextBook();
		}
		
		
	}

	/**
	 * @return the bookList
	 */
	public List<Book> getBookList() {
		return bookList;
	}

	/**
	 * @param bookList the bookList to set
	 */
	public void setBookList(List<Book> bookList) {
		this.bookList = bookList;
	}

	/**
	 * @return the readBooks
	 */
	public List<Book> getReadBooks() {
		return readBooks;
	}

	/**
	 * @param readBooks the readBooks to set
	 */
	public void setReadBooks(List<Book> readBooks) {
		this.readBooks = readBooks;
	}
	
	
}
