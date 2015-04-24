package ru.terekhov.book2read.control;

import java.util.HashMap;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.inject.Named;

import ru.terekhov.book2read.model.AbstractBook;
import ru.terekhov.book2read.model.LibraryBook;
import ru.terekhov.book2read.utils.CatalogDownloaderFile;

@Named
@Singleton
public class LibraryFile extends LibraryAbstract {
	
	@PostConstruct
	protected void initialize() {
		this.setCd(new CatalogDownloaderFile());
		this.setAllBooks(new HashMap<String, LibraryBook>());
	}

	@Override
	public AbstractBook getRandomBook() {
		int randomNum = new Random().nextInt(getBookCount());
		Object[] books = this.getAllBooks().values().toArray();
		return (LibraryBook)books[randomNum];
	}
}
