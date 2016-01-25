package org.ongawa.peru.chlorination.modules.reports;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.commons.lang.NullArgumentException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;

public abstract class Report {
	
	protected static final float LEFT_IDENTATION = 20f;
	protected static final float DEFAULT_SPACING = 10f;
	private static final int colorCode = 220;
	protected static final BaseColor LIGHT_GRAY = new BaseColor(colorCode, colorCode, colorCode);
	
	protected File file;
	protected Locale locale;
	protected String author;
	protected Font headerFont;
	protected Font bodyFont;
	protected Font redBodyFont;
	protected Font bodyBoldFont;
	protected DecimalFormat df;
	protected SimpleDateFormat sdf;
	
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
		
		this.headerFont = new Font(FontFamily.HELVETICA, 18);
		this.bodyFont = new Font(FontFamily.HELVETICA, 12);
		this.redBodyFont = new Font(this.bodyFont.getFamily(), this.bodyFont.getSize());
		this.redBodyFont.setColor(BaseColor.RED);
		this.bodyBoldFont = new Font(this.bodyFont.getFamily(), this.bodyFont.getSize());
		this.bodyBoldFont.setStyle(Font.BOLD);
		this.df = new DecimalFormat("#.####");
		this.sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
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
