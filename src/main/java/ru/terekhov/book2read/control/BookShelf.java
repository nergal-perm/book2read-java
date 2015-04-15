package ru.terekhov.book2read.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.logging.Logger;

import ru.terekhov.book2read.model.Book;
import ru.terekhov.book2read.utils.BookParser;

@SessionScoped
@Named
public class BookShelf implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private Logger logger;
	@Inject
	private Library bookLibrary;

	/**
	 * Список книг, находящихся в данный момент в процессе чтения
	 */
	private List<Book> bookShelf;

	/**
	 * @return the bookShelf
	 */
	public List<Book> getBookShelf() {
		return bookShelf;
	}

	/**
	 * @param bookShelf
	 *            the bookShelf to set
	 */
	public void setBookShelf(List<Book> bookShelf) {
		this.bookShelf = bookShelf;
	}

	private SortedMap<Date, Book> readBooks;
	
	/**
	 * @return the readBooks
	 */
	public Map<Date, Book> getReadBooks() {
		return readBooks;
	}
	
	@PostConstruct
	public void initializeBookShelf() {
		bookToReadQuantity = 1;

		this.bookShelf = new ArrayList<Book>();
		getBooksToRead(3);
		
		Book nextBook = bookDb.getNextBook();

		while (nextBook != null) {
			getBookList().add(nextBook);
			if (nextBook.isRead()) {
				getReadBooks().add(nextBook);
			}
			nextBook = bookDb.getNextBook();
		}		

		logger.info("Book shelf constructed successfully");

	}

	private Book selectedBook;

	/**
	 * @return the selectedBook
	 */
	public Book getSelectedBook() {
		return selectedBook;
	}

	/**
	 * @param selectedBook
	 *            the selectedBook to set
	 */
	public void setSelectedBook(Book selectedBook) {
		this.selectedBook = selectedBook;
	}

	public void readBook() {
		selectedBook.setRead(true);
		bookShelf.remove(selectedBook);
		readBooks.put(new Date(), selectedBook);
	}

	/*
	 * Поля и методы, относящиеся к операции получения новой порции рандомных
	 * книг для чтения.
	 */
	private int bookToReadQuantity;

	/**
	 * @return the bookToReadQuantity
	 */
	public int getBookToReadQuantity() {
		return bookToReadQuantity;
	}

	/**
	 * @param bookToReadQuantity
	 *            the bookToReadQuantity to set
	 */
	public void setBookToReadQuantity(int bookToReadQuantity) {
		this.bookToReadQuantity = bookToReadQuantity;
	}

	public void getBooksToRead(int bookNum) {
		for (int i = 0; i < bookNum; i++) {
			bookShelf.add(bookLibrary.getNextBook());
		}
	}



	private List<Book> bookList = new ArrayList<Book>();
	@Inject private BookParser bookDb;
	
	@PostConstruct
	public void initialize() {

		
		
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
		
	
}
