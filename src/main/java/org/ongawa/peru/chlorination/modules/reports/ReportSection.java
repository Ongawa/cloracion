package org.ongawa.peru.chlorination.modules.reports;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.commons.lang.NullArgumentException;
import org.ongawa.peru.chlorination.ApplicationProperties;
import org.ongawa.peru.chlorination.KEYS;
import org.ongawa.peru.chlorination.persistence.DataSourceFactory;
import org.ongawa.peru.chlorination.persistence.IDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;

/**
 * 
 * @author kiko
 *
 */
public abstract class ReportSection {
	
	protected static Logger log;
	protected static final float LEFT_IDENTATION = 10f;
	protected static final float DEFAULT_SPACING_AFTER = 10f;
	
	static{
		LoggerFactory.getLogger(ReportSection.class);
	}
	
	protected IDataSource ds;
	protected Properties properties;
	protected ResourceBundle messages;
	protected Font headerFont;
	protected Font cellFont;
	protected Font bodyFont;
	protected BaseColor headerColor;
	protected BaseColor row1Color;
	protected BaseColor row2Color;
	protected DecimalFormat df;
	protected SimpleDateFormat sdf;
	protected Locale locale;
	
	public ReportSection(Locale locale) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException{
		if(locale == null)
			throw new NullArgumentException("locale");
		
		this.ds = DataSourceFactory.getInstance().getDefaultDataSource();
		this.properties = ApplicationProperties.getInstance().getProperties();
		this.messages = ResourceBundle.getBundle(this.properties.getProperty(KEYS.REPORT_BUNDLE_NAME), locale);
		this.locale = locale;
		
		this.headerFont = new Font(FontFamily.HELVETICA, 13);
		this.cellFont = new Font(this.bodyFont.getFamily(), 10);
		this.bodyFont = new Font(FontFamily.HELVETICA, 12);
		this.headerColor = new BaseColor(106, 180, 189);
		this.row1Color = BaseColor.WHITE;
		this.row2Color = new BaseColor(229, 244, 246);
		this.df = new DecimalFormat("#.####");
		this.sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
	}
	
	public abstract boolean addSectionInfo(Document document) throws DocumentException;
}