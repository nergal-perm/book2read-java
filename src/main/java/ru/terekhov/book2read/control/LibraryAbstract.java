package ru.terekhov.book2read.control;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Random;

import ru.terekhov.book2read.model.AbstractBook;
import ru.terekhov.book2read.model.LibraryBook;
import ru.terekhov.book2read.utils.CatalogDownloaderAbstract;

public abstract class LibraryAbstract {

	private Map<String, LibraryBook> allBooks;
	private CatalogDownloaderAbstract cd;
	
	public LibraryAbstract() {
		initialize();
	}
	
	protected abstract void initialize();	
	public abstract AbstractBook getRandomBook(); 
	
	public void update() {
		InputStream is = null;
		BufferedReader bfReader = null;
		try {
			is = new ByteArrayInputStream(cd.getCatalog());
			bfReader = new BufferedReader(new InputStreamReader(is));
			String temp = null;
			while((temp = bfReader.readLine()) != null) {
				LibraryBook book = parseBookInfo(temp);
				allBooks.put(book.getId(), book);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (Exception ex) {
				// do nothing
			}
		}
	}

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
	 * @param cd the cd to set
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
	 * @param allBooks the allBooks to set
	 */
	protected void setAllBooks(Map<String, LibraryBook> allBooks) {
		this.allBooks = allBooks;
	}


	
	
	private LibraryBook parseBookInfo(String bookRec) {
		String[] bookInfoArray = bookRec.split(";");
		int arrSize = bookInfoArray.length;
		StringBuilder sb = new StringBuilder();
		LibraryBook retVal = new LibraryBook();

		retVal.setIsValid(arrSize == 9);
		retVal.setAuthor((bookInfoArray[0] + " " + bookInfoArray[1] + " " + bookInfoArray[2])
				.trim());
		for (int i = 3; i < arrSize - 4; i++) {
			if (bookInfoArray[i].length() != 0) {
				sb.append(bookInfoArray[i]).append(";");
			}
		}
		if (sb.toString().endsWith(";")) {
			retVal.setTitle(sb.toString().substring(0, sb.toString().length() - 1));
		} else {
			retVal.setTitle(sb.toString());
		}

		retVal.setLanguage(bookInfoArray[arrSize - 4]);
		retVal.setYear(bookInfoArray[arrSize - 3]);
		retVal.setSeries(bookInfoArray[arrSize - 2]);
		retVal.setId(bookInfoArray[arrSize - 1]);

		retVal.setPagesCount(new Random().nextInt(1000) + 1);
		return retVal;
	}
	
	
}
