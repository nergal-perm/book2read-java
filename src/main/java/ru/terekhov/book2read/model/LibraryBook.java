package ru.terekhov.book2read.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class LibraryBook implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// Fieds
	// --------------------------------
	private String title;
	@Id
	private String id;
	private String author;
	private String series;
	private String year;
	private String language;
	private boolean isValid;
	private boolean isInShortList;
	private boolean isInReadingQueue;
	private boolean isRead;
	private Date dateReaded;
	private int pagesCount;
	private BookFormat format;
	private int rating; 

	// Getters
	// --------------------------------
	public String getTitle() {
		return title;
	}
	public String getAuthor() {
		return author;
	}
	public String getId() {
		return id;
	}
	public String getSeries() {
		return series;
	}	
	public String getYear() {
		return year;
	}
	public String getLanguage() {
		return language;
	}
	public int getPagesCount() {
		return pagesCount;
	}
	public boolean isInShortList() {
		return isInShortList;
	}
	public boolean isInReadingQueue() {
		return isInReadingQueue;
	}	
	public boolean isValid() {
		return isValid;
	}	
	public boolean isRead() {
		return isRead;
	}
	public Date getDateReaded() {
		return dateReaded;
	}
	public BookFormat getFormat() {
		return format;
	}	
	public int getRating() {
		return this.rating;
	}
	
	// Setters
	// --------------------------------
	public void setTitle(String title) {
		this.title = title;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setSeries(String series) {
		this.series = series;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public void setPagesCount(int pagesCount) {
		this.pagesCount = pagesCount;
	}	
	public void setInShortList(boolean isInShortList) {
		this.isInShortList = isInShortList;
	}
	public void setIsValid(boolean isValid) {
		this.isValid = isValid;
	}
	public void setInReadingQueue(boolean isInReadingQueue) {
		this.isInReadingQueue = isInReadingQueue;
	}
	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}
	public void setDateReaded(Date dateReaded) {
		this.dateReaded = dateReaded;
	}
	public void setFormat(BookFormat format) {
		this.format = format;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	
	
	
	public LibraryBook(String title, String author) {
		setTitle(title);
		setAuthor(author);
	}
	
	public LibraryBook(String id, String title, String author) {
		setId(id);
		setTitle(title);
		setAuthor(author);
	}
	
	public LibraryBook() {
		
	}




	
}
