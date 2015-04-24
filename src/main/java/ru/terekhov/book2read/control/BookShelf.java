package ru.terekhov.book2read.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.logging.Logger;

import ru.terekhov.book2read.model.LibraryBook;
import ru.terekhov.book2read.utils.BookParser;
import ru.terekhov.book2read.utils.CatalogDownloaderAbstract;
import ru.terekhov.book2read.utils.CatalogDownloaderFile;
import ru.terekhov.book2read.utils.CatalogDownloaderFlibusta;

@SessionScoped
@Named
public class BookShelf implements Serializable {

	private CatalogDownloaderAbstract cd;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private Logger logger;

	/**
	 * Список книг, находящихся в данный момент в процессе чтения
	 */
	private List<LibraryBook> bookShelf;

	/**
	 * @return the bookShelf
	 */
	public List<LibraryBook> getBookShelf() {
		return bookShelf;
	}

	/**
	 * @param bookShelf
	 *            the bookShelf to set
	 */
	public void setBookShelf(List<LibraryBook> bookShelf) {
		this.bookShelf = bookShelf;
	}

	private SortedMap<Date, LibraryBook> readBooks;
	
	/**
	 * @return the readBooks
	 */
	public Map<Date, LibraryBook> getReadBooks() {
		return readBooks;
	}
	
	@PostConstruct
	public void initializeBookShelf() {
		bookToReadQuantity = 1;

		LibraryBook nextBook = bookDb.getNextBook();

		while (nextBook != null) {
			getBookList().add(nextBook);
			nextBook = bookDb.getNextBook();
		}		

		this.bookShelf = new ArrayList<LibraryBook>();
		getBooksToRead(3);		
		
		readBooks = new TreeMap<Date, LibraryBook>();
		
		logger.info("LibraryBook shelf constructed successfully");

	}

	private LibraryBook selectedBook;

	/**
	 * @return the selectedBook
	 */
	public LibraryBook getSelectedBook() {
		return selectedBook;
	}

	/**
	 * @param selectedBook
	 *            the selectedBook to set
	 */
	public void setSelectedBook(LibraryBook selectedBook) {
		this.selectedBook = selectedBook;
	}

	public void readBook() {
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
			LibraryBook newBook = getRandomBook();
			bookShelf.add(newBook);
			bookList.remove(newBook);
			
		}
	}

	private LibraryBook getRandomBook() {
		int randomInt = new Random().nextInt(bookList.size());
		return bookList.get(randomInt);
	}

	private List<LibraryBook> bookList = new ArrayList<LibraryBook>();
	@Inject private BookParser bookDb;
	
	/**
	 * @return the bookList
	 */
	public List<LibraryBook> getBookList() {
		return bookList;
	}

	/**
	 * @param bookList the bookList to set
	 */
	public void setBookList(List<LibraryBook> bookList) {
		this.bookList = bookList;
	}
		

	public int getBooksReadIn30Days() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, -30);
		return readBooks.subMap(c.getTime(), new Date()).size();
	}
	
	public int getPagesReadIn30Days() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, -30);
		int pagesRead = 0;
		for (LibraryBook b : readBooks.subMap(c.getTime(), new Date()).values()) {
			pagesRead += b.getPagesCount();
		}
		return pagesRead;
	}

	public void updateLibrary() {
		try {
			cd = new CatalogDownloaderFile();
			byte[] retVal = cd.getCatalog();
			System.out.println("Прочитано: " + retVal.length + " байт.");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}

