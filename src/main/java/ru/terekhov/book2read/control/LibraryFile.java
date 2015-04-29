package ru.terekhov.book2read.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.inject.Named;
import javax.persistence.criteria.CriteriaQuery;

import ru.terekhov.book2read.model.LibraryBook;
import ru.terekhov.book2read.utils.CatalogDownloaderFile;
import ru.terekhov.book2read.utils.CatalogDownloaderFlibusta;

@Named
@Singleton
public class LibraryFile extends LibraryAbstract {
	
	@PostConstruct
	protected void initialize() {
		this.setCd(new CatalogDownloaderFile());
		this.setAllBooks(new HashMap<String, LibraryBook>());
		for (LibraryBook book : getAllDbBooks()) {
			this.getAllBooks().put(book.getId(), book);
		}
	}

	@Override
	public LibraryBook getRandomBook() {
		int randomNum = new Random().nextInt(getBookCount());
		Object[] books = this.getAllBooks().values().toArray();
		return (LibraryBook)books[randomNum];
	}
	
	private List<LibraryBook> getAllDbBooks() {
		if (this.entityManager != null) {
			CriteriaQuery<LibraryBook> criteria = this.entityManager.getCriteriaBuilder().createQuery(
					LibraryBook.class);
			return this.entityManager.createQuery(criteria.select(criteria.from(LibraryBook.class)))
					.getResultList();			
		}
		
		return new ArrayList<LibraryBook>();
		
	}
}
