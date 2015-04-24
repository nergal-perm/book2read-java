package ru.terekhov.book2read.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CatalogDownloaderFile extends CatalogDownloaderAbstract {

	public CatalogDownloaderFile() {
		super.initialize();

	}

	@Override
	protected InputStream getInputStream() throws IOException {
		FileInputStream retVal = new FileInputStream("catalog.zip");  
		if (size == -1) {
			size = retVal.available();
			System.out.println("Размер файла: " + size + " байт.");
		}
		return retVal;
	}

}
