package ru.terekhov.book2read.model;

public class Author {

	private String fullName;

	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}


	/**
	 * @param fullName the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	
	
	public Author(String fullName) {
		setFullName(fullName);
	}

	
}
