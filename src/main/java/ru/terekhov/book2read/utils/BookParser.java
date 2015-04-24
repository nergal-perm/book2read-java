package ru.terekhov.book2read.utils;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import ru.terekhov.book2read.model.LibraryBook;

import java.io.Serializable;
import java.util.Random;

@SessionScoped
public class BookParser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private IBookFetcher bookDB;

	public LibraryBook getNextBook() {
		try {
			return parseBookInfo(bookDB.getNextBookInfo());
		} catch (Exception e) {
			return null;
		}

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
