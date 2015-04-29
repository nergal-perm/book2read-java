package ru.terekhov.book2read.model;

public enum BookFormat {
	Electronic("Электронная"), Paper("Бумажная"), Audio("Аудио");

	private final String label;

	private BookFormat(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return this.label;
}
}
