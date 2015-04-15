package ru.terekhov.book2read.utils;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import ru.terekhov.book2read.model.Author;
import ru.terekhov.book2read.model.Book;
import java.io.Serializable;

@SessionScoped
public class BookParser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject private IBookFetcher bookDB;
	
	public Book getNextBook() {
		try {
			return parseBookInfo(bookDB.getNextBookInfo());
		} catch (Exception e) {
			return null;
		}
	}
	
	private Book parseBookInfo(String bookRec) {
		String[] bookInfoArray = bookRec.split(";");
		
		Author author = new Author((bookInfoArray[0] + " " + bookInfoArray[1] + " " + bookInfoArray[2]).trim());
		
		return new Book(bookInfoArray[bookInfoArray.length - 1],bookInfoArray[3], author);
	}
}
