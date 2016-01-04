package org.ongawa.peru.chlorination.modules.reports;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.commons.lang.NullArgumentException;
import org.ongawa.peru.chlorination.ApplicationProperties;
import org.ongawa.peru.chlorination.KEYS;
import org.ongawa.peru.chlorination.persistence.DataSourceFactory;
import org.ongawa.peru.chlorination.persistence.IDataSource;
import org.ongawa.peru.chlorination.persistence.elements.Community;
import org.ongawa.peru.chlorination.persistence.elements.SubBasin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;


public class Report {
	
	private static Logger log;
	static{
		log = LoggerFactory.getLogger(Report.class);
	}
	
	private Properties properties;
	private IDataSource ds;
	private ResourceBundle messages;
	
	public Report(String language, String country) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException{
		if(language == null)
			throw new NullArgumentException("language");
		if(country == null)
			throw new NullArgumentException("country");

		this.properties = ApplicationProperties.getInstance().getProperties();
		this.ds = DataSourceFactory.getInstance().getDefaultDataSource();
		Locale currentLocale = new Locale(language, country);
		this.messages = ResourceBundle.getBundle(this.properties.getProperty(KEYS.REPORT_BUNDLE_NAME), currentLocale);
		log.info("Loaded i18n for reports: "+language.toLowerCase()+"_"+country.toUpperCase());
	}

	public static void main(String[] args) throws IOException, DocumentException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Properties properties = ApplicationProperties.getInstance().getProperties();
        Path path  = FileSystems.getDefault().getPath(properties.getProperty(KEYS.RESOURCES_PATH), "report.pdf");
        new Report("es", "es").createPdf(path.toFile());
    }
	
	public void addFrontpage(Document document) throws DocumentException, MalformedURLException, IOException{
		Image imageHeader = Image.getInstance(this.properties.getProperty(KEYS.RESOURCES_PATH)+File.separator+"ongawa.png");
        imageHeader.scalePercent(25);
        imageHeader.setAlignment(Image.ALIGN_CENTER);
        imageHeader.setSpacingBefore((document.getPageSize().getHeight()-document.getPageSize().getBorderWidthTop())/2);
        document.add(imageHeader);
        
        Font fontHeader = new Font(FontFamily.HELVETICA, 22);
        fontHeader.setStyle(Font.BOLD);
        Chunk chHeader = new Chunk(messages.getString(KEYS.REPORT_FRONTPAGE_MAIN_TEXT));
        chHeader.setCharacterSpacing(0);
        chHeader.setFont(fontHeader);
        Paragraph phHeader = new Paragraph();
        phHeader.setAlignment(Paragraph.ALIGN_CENTER);
        phHeader.add(chHeader);
        phHeader.setSpacingBefore((document.getPageSize().getHeight()-document.getPageSize().getBorderWidthTop()-250)/2);
        document.add(phHeader);
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String date = sdf.format(Calendar.getInstance().getTime());
        Font fontPublished = new Font(FontFamily.HELVETICA, 19);
        Chunk publishedDate = new Chunk(this.messages.getString(KEYS.REPORT_PUBLISHED_DATE)+date, fontPublished);
        Paragraph publishedDatePh = new Paragraph();
        publishedDatePh.setAlignment(Paragraph.ALIGN_CENTER);
        publishedDatePh.setSpacingBefore(10);
        publishedDatePh.add(new Phrase(publishedDate));
        document.add(publishedDatePh);
        
        document.newPage();
	}
	
	public void addSubBasinInfo(Document document, SubBasin subBasin) throws DocumentException{
		Font subBasinTitleFont = new Font(FontFamily.HELVETICA, 19);
		subBasinTitleFont.setStyle(Font.BOLD);
        Chunk chSubBasinTitle = new Chunk(this.messages.getString(KEYS.REPORT_REFERING_SUBBASIN).replaceAll("&subbasinName", subBasin.getName()), subBasinTitleFont);
        Paragraph phSubBasinTitle = new Paragraph();
        phSubBasinTitle.setSpacingAfter(20);
        phSubBasinTitle.add(new Phrase(chSubBasinTitle));
        document.add(phSubBasinTitle);
        
        Font bodyFont = new Font(FontFamily.HELVETICA, 14);
        Paragraph phIntroducingComs = new Paragraph();
        Chunk chIntroducingComs = new Chunk(this.messages.getString(KEYS.REPORT_INDTRODUCING_COMMUNITIES).replaceFirst("&subbasinName", subBasin.getName()), bodyFont);
        phIntroducingComs.add(chIntroducingComs);
        document.add(phIntroducingComs);
        
        List<Community> communities = ds.getCommunities(subBasin);
        com.itextpdf.text.List listCom = new com.itextpdf.text.List();
        listCom.setListSymbol("\u2022");
        listCom.setIndentationLeft(60);
        Chunk ch;
	    for(Community community : communities){
	    	ch = new Chunk(community.getName());
	    	ch.setFont(bodyFont);
	    	listCom.add(new ListItem(ch));
	    }
	    Paragraph phListCom = new Paragraph();
	    phListCom.setSpacingAfter(60);
	    phListCom.add(listCom);
	    
	    document.add(phListCom);
	}
	
	public void addCommunityInfo(Document document, Community community) throws DocumentException{
		Font communityTitleFont = new Font(FontFamily.HELVETICA, 15);
		communityTitleFont.setStyle(Font.UNDERLINE|Font.ITALIC);
		Chunk chCommunityTitle = new Chunk(this.messages.getString(KEYS.REPORT_REFERING_COMMUNITY).replaceAll("&communityName", community.getName()), communityTitleFont);
		Paragraph phCommunityTitle = new Paragraph();
		phCommunityTitle.setSpacingAfter(60);
		phCommunityTitle.add(new Phrase(chCommunityTitle));
		document.add(phCommunityTitle);
	}
 
    public void createPdf(File file) throws IOException, DocumentException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();
        
        this.addFrontpage(document);
 
        SubBasin subBasin = ds.getSubBasin(1);
        this.addSubBasinInfo(document, subBasin);
        
        List<Community> communities = this.ds.getCommunities(subBasin);
        for(Community community : communities)
        	this.addCommunityInfo(document, community);

        document.close();
    }
}