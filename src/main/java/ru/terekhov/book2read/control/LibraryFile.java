package ru.terekhov.book2read.control;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.ejb.Asynchronous;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.logging.Logger;

import ru.terekhov.book2read.facade.DatabaseFacade;
import ru.terekhov.book2read.model.Catalog;
import ru.terekhov.book2read.model.LibraryBook;
import ru.terekhov.book2read.utils.CatalogDownloaderFile;
import ru.terekhov.book2read.utils.CatalogDownloaderFlibusta;

@Named
@Singleton
@Startup
public class LibraryFile extends LibraryAbstract {

	private static final Logger logger = Logger.getLogger(LibraryFile.class);
	private Catalog catalog;
	@Inject
	private DatabaseFacade dbFacade;

	@PostConstruct
	protected void initialize() {
		if (dbFacade == null) {
			logger.error("Database facade wasn't constructed properly");
			dbFacade = new DatabaseFacade();
		}
		
		this.setAllBooks(new HashMap<String, LibraryBook>());
		for (LibraryBook book : getAllDbBooks()) {
			this.getAllBooks().put(book.getId(), book);
		}
	}

	@Override
	public LibraryBook getRandomBook() {
		int randomNum = new Random().nextInt(getBookCount());
		Object[] books = this.getAllBooks().values().toArray();
		return (LibraryBook) books[randomNum];
	}

	@Schedule(hour = "*", minute = "*", second = "*/30", persistent = false)
	@Asynchronous
	protected void update_pseudo() {
		if (needToDownload()) {
			logger.info("We need to redownload a catalog");
			downloadCatalog();
		} else {
			logger.info("We'll use DB-stored catalog");
			this.catalog = getDBCatalog();
			this.setCd(new CatalogDownloaderFile());
		}
		fetchNextBooks(1000);
	}

	private List<LibraryBook> getAllDbBooks() {
		return dbFacade.getBooks();
	}	
	
	private Catalog getDBCatalog() {
		return dbFacade.getCatalogFromDb();
	}

	private void saveToDb(Catalog c) {
		dbFacade.mergeCatalog(c);
	}	
	
	private boolean needToDownload() {
		Calendar threshold = new GregorianCalendar();
		threshold.add(Calendar.DAY_OF_MONTH, -30);
		logger.info("30 days before: " + threshold.getTime());
		return getLastUpdated().before(threshold.getTime());
	}

	private Date getLastUpdated() {
		Date lastUpdated = new GregorianCalendar(2000, 00, 1).getTime();
		try {
			lastUpdated = dbFacade.getLastUpdatedDate();
		} catch (Exception ex) {
			// do Nothing
		}
		logger.info("Last updated: " + lastUpdated);
		return lastUpdated;
	}

	private void downloadCatalog() {
		this.setCd(new CatalogDownloaderFlibusta());
		clearCurrentCatalog();
		this.setCatalog(new Catalog(this.getCd().getZippedBytes()));
		this.getCatalog().setDateUpdated(new Date());
		logger.info("Downloaded file, size: " + this.getCatalog().getContent().length + " bytes.");
		saveToDb(this.getCatalog());
	}
	
	private void clearCurrentCatalog() {
		logger.info("Deleted " + dbFacade.clearCatalog() + " record(s)");
	}

	private void fetchNextBooks(int count) {
		InputStream is = null;
		BufferedReader bfReader = null;
		try {
			is = new ByteArrayInputStream(this.getCd().unzipCatalog(this.getCatalog().getContent()));
			bfReader = new BufferedReader(new InputStreamReader(is));
			String temp = null;
			int i = 0;
			while ((temp = bfReader.readLine()) != null && i < count) {
				LibraryBook book = parseBookInfo(temp);
				if (dbFacade.mergeBook(book)) {
					this.getAllBooks().put(book.getId(), book);
					i++;
				}
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

	private LibraryBook parseBookInfo(String bookRec) {

		String[] bookInfoArray = bookRec.split(";");
		int arrSize = bookInfoArray.length;
		LibraryBook retVal = new LibraryBook();
		if (arrSize < 9) {
			logger.warn("Wrong book row: " + bookRec);
		} else {
			StringBuilder sb = new StringBuilder();

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
		}
		return retVal;
	}

	public Catalog getCatalog() {
		return catalog;
	}

	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}

}
