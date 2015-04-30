package ru.terekhov.book2read.control;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Random;

import javax.ejb.Asynchronous;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import ru.terekhov.book2read.model.LibraryBook;
import ru.terekhov.book2read.utils.CatalogDownloaderAbstract;

public abstract class LibraryAbstract {

	@PersistenceContext(unitName = "Book2Read", type = PersistenceContextType.TRANSACTION)
	protected EntityManager entityManager;

	private Map<String, LibraryBook> allBooks;
	private CatalogDownloaderAbstract cd;

	public LibraryAbstract() {
		initialize();
	}

	protected abstract void initialize();

	public abstract LibraryBook getRandomBook();

	@Asynchronous
	public void update() {

	}

	protected abstract void update_pseudo();	
	
	/**
	 * @return the bookCount
	 */
	public int getBookCount() {
		return allBooks.size();
	}

	/**
	 * @return the cd
	 */
	protected CatalogDownloaderAbstract getCd() {
		return cd;
	}

	/**
	 * @param cd
	 *            the cd to set
	 */
	protected void setCd(CatalogDownloaderAbstract cd) {
		this.cd = cd;
	}

	/**
	 * @return the allBooks
	 */
	protected Map<String, LibraryBook> getAllBooks() {
		return allBooks;
	}

	/**
	 * @param allBooks
	 *            the allBooks to set
	 */
	protected void setAllBooks(Map<String, LibraryBook> allBooks) {
		this.allBooks = allBooks;
	}



}
