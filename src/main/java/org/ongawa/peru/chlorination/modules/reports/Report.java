package org.ongawa.peru.chlorination.modules.reports;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;

import org.apache.commons.lang.NullArgumentException;

import com.itextpdf.text.DocumentException;

public abstract class Report {
	
	protected File file;
	protected Locale locale;
	protected String author;
	
	public Report(File destFile, Locale locale, String author){
		if(destFile == null)
			throw new NullArgumentException("destFile");
		if(locale == null)
			throw new NullArgumentException("locale");
		if(author == null)
			throw new NullArgumentException("author");
		
		this.file = destFile;
		this.locale = locale;
		this.author = author;
	}
	
	public File getFile(){
		return this.file;
	}
	
	public Locale getLocale(){
		return this.locale;
	}
	
	public String getAuthor(){
		return this.author;
	}
	
	public abstract void createReport() throws FileNotFoundException, DocumentException;
}
