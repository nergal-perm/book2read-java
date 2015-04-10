package ru.terekhov.book2read.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.logging.Logger;

import ru.terekhov.book2read.model.Author;
import ru.terekhov.book2read.model.Book;

@SessionScoped
@Named
public class BookShelf implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject private Logger logger;
	
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
	 * @param bookShelf the bookShelf to set
	 */
	public void setBookShelf(List<Book> bookShelf) {
		this.bookShelf = bookShelf;
	}
	
	@PostConstruct
	public void initializeBookShelf() {
		this.bookShelf = new ArrayList<Book>();
		
		this.bookShelf.add(new Book("Смерть или престол", new Author("Андре Нортон")));
		this.bookShelf.add(new Book("Плоский мир", new Author("Терри Пратчетт")));
		this.bookShelf.add(new Book("Белые камни Харумбы", new Author("Макс Фрай")));
		
		logger.info("Book shelf constructed successfully");
		
	}
	
	
	
	
}
