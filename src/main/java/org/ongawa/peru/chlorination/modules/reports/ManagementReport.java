package org.ongawa.peru.chlorination.modules.reports;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.commons.lang.NullArgumentException;
import org.ongawa.peru.chlorination.ApplicationProperties;
import org.ongawa.peru.chlorination.KEYS;
import org.ongawa.peru.chlorination.persistence.DataSourceFactory;
import org.ongawa.peru.chlorination.persistence.IDataSource;
import org.ongawa.peru.chlorination.persistence.elements.ChlorineCalculation;
import org.ongawa.peru.chlorination.persistence.elements.WaterSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class ManagementReport extends Report {
	
	protected static Logger log;
	
	static{
		LoggerFactory.getLogger(ManagementReport.class);
	}
	
	private IDataSource ds;
	private Properties properties;
	private ResourceBundle messages;
	private WaterSystem waterSystem;

	public ManagementReport(WaterSystem waterSystem, File destFile, Locale locale, String author) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
		super(destFile, locale, author);
		if(waterSystem == null)
			throw new NullArgumentException("waterSystem");
		
		this.waterSystem = waterSystem;
		this.ds = DataSourceFactory.getInstance().getDefaultDataSource();
		this.properties = ApplicationProperties.getInstance().getProperties();
		this.messages = ResourceBundle.getBundle(this.properties.getProperty(KEYS.REPORT_BUNDLE_NAME), locale);
	}

	@Override
	public void createReport() throws FileNotFoundException, DocumentException {
		Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(this.file));
        document.open();
        
    	ChlorineCalculation chlorineCalculation = this.ds.getLastChlorineCalculation(waterSystem);
    	if(chlorineCalculation !=null){
	        Chunk chTitle1 = new Chunk(this.messages.getString(KEYS.REPORT_GENERIC_TITLE_FIRST_CHUNK), this.headerFont);
	        Font headerUnderline = new Font(this.headerFont.getFamily(), this.headerFont.getSize());
	        headerUnderline.setStyle(Font.UNDERLINE);
	        Chunk chTitle2 = new Chunk(this.messages.getString(KEYS.REPORT_MANAGEMENT_TITLE_CHUNK), headerUnderline);
	        this.headerFont.setStyle(Font.NORMAL);
	        Chunk chTitle3 = new Chunk(this.messages.getString(KEYS.REPORT_GENERIC_TITLE_LAST_CHUNK), this.headerFont);
	        Phrase phTitle = new Phrase();
	        phTitle.add(chTitle1);
	        phTitle.add(chTitle2);
	        phTitle.add(chTitle3);
	        Paragraph paTitle = new Paragraph(phTitle);
	        paTitle.setAlignment(Paragraph.ALIGN_CENTER);
	        paTitle.setSpacingAfter(DEFAULT_SPACING*5);
	        document.add(paTitle);
	        
	        document.add(new Paragraph(new Chunk(this.sdf.format(Calendar.getInstance().getTime()), this.bodyFont)));
	        document.add(new Paragraph(this.author, this.bodyFont));
	        
	        Font subHeaderFont = new Font(this.headerFont.getFamily(), this.headerFont.getSize()-2);
	        subHeaderFont.setStyle(Font.BOLD);
	        Paragraph paSysDataTitle = new Paragraph(new Chunk(this.messages.getString(KEYS.REPORT_GENERIC_SYSTEMDATA), subHeaderFont));
	        paSysDataTitle.setSpacingBefore(DEFAULT_SPACING*3);
	        paSysDataTitle.setSpacingAfter(DEFAULT_SPACING);
	        document.add(paSysDataTitle);
	        
	        PdfPTable inputTable = new PdfPTable(2);
			inputTable.setWidthPercentage(90);
			inputTable.getDefaultCell().setUseAscender(true);
			inputTable.getDefaultCell().setUseDescender(true);
			inputTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
			inputTable.getDefaultCell().setFixedHeight(40);
	        
			inputTable.addCell(this.messages.getString(KEYS.REPORT_GENERIC_SUBBASIN)+" "+waterSystem.getCommunity().getSubBasin().getName());
			inputTable.addCell(this.messages.getString(KEYS.REPORT_GENERIC_CHLORINETYPE)+" "+chlorineCalculation.getChlorineType());
			inputTable.addCell(this.messages.getString(KEYS.REPORT_GENERIC_COMMUNITY)+" "+waterSystem.getCommunity().getName());
			inputTable.addCell(this.messages.getString(KEYS.REPORT_GENERIC_CHLORINEPURENESS)+" "+df.format(chlorineCalculation.getChlorinePureness()));
			inputTable.addCell(this.messages.getString(KEYS.REPORT_GENERIC_WATERSYSTEM)+" "+waterSystem.getName());
			inputTable.addCell(this.messages.getString(KEYS.REPORT_GENERIC_TANKVOLUME)+" "+df.format(chlorineCalculation.getTankVolume()));
			inputTable.addCell(this.messages.getString(KEYS.REPORT_GENERIC_FAMILIESNUM)+" "+chlorineCalculation.getFamiliesNum());
			inputTable.addCell(this.messages.getString(KEYS.REPORT_GENERIC_RELOADTIME)+" "+df.format(chlorineCalculation.getReloadTime()));
			inputTable.addCell(this.messages.getString(KEYS.REPORT_GENERIC_POPULATION)+" "+chlorineCalculation.getPopulation());
			inputTable.addCell(this.messages.getString(KEYS.REPORT_GENERIC_DRIPPINGPERDAY)+" "+df.format(chlorineCalculation.getDrippingHoursPerDay()));
			inputTable.addCell(this.messages.getString(KEYS.REPORT_GENERIC_NATURALFLOW)+" "+df.format(chlorineCalculation.getNaturalFlow()));
			inputTable.addCell(this.messages.getString(KEYS.REPORT_GENERIC_CHLORINEDEMAND)+" "+df.format(chlorineCalculation.getChlorineDemand()));
			inputTable.addCell(this.messages.getString(KEYS.REPORT_GENERIC_FLOWTOCHLORINE)+" "+df.format(chlorineCalculation.getChlorinatedFlow()));
			inputTable.addCell(this.messages.getString(KEYS.REPORT_GENERIC_CHLORINEPRICE)+" "+df.format(chlorineCalculation.getChlorinePrice()));				
			document.add(inputTable);
			
			Paragraph paResults = new Paragraph(new Chunk(this.messages.getString(KEYS.REPORT_GENERIC_RESULTS), subHeaderFont));
	        document.add(paResults);

	        Paragraph pa = new Paragraph(new Chunk(this.messages.getString(KEYS.REPORT_RESULTS_MANAGEMENT_DOSE).replaceFirst("&result1", df.format(chlorineCalculation.getChlorineDosePerFortnight())).replaceFirst("&result2", df.format(chlorineCalculation.getChlorineDosePerMonth())), redBodyFont));
	        pa.setFirstLineIndent(LEFT_IDENTATION);
	        document.add(pa);
	        pa = new Paragraph(new Chunk(this.messages.getString(KEYS.REPORT_RESULTS_MANAGEMENT_DRIPPINGFLOW).replaceFirst("&result1", df.format(chlorineCalculation.getDrippingFlowInMl())).replaceFirst("&result2", df.format(chlorineCalculation.getDrippingFlowInDrops())), redBodyFont));
	        pa.setFirstLineIndent(LEFT_IDENTATION);
	        document.add(pa);
	        pa = new Paragraph(new Chunk(this.messages.getString(KEYS.REPORT_RESULTS_MANAGEMENT_CHLORINATIONCOST)+" "+df.format(chlorineCalculation.getChlorinationCost())));
	        pa.setFirstLineIndent(LEFT_IDENTATION);
	        document.add(pa);
	        
	        Paragraph paWarnings = new Paragraph(new Chunk(this.messages.getString(KEYS.REPORT_RESULTS_GENERIC_WARNINGS_TITLE), subHeaderFont));
	        paWarnings.setSpacingBefore(DEFAULT_SPACING*2);
	        document.add(paWarnings);
	        
	        paWarnings = new Paragraph(new Chunk(this.messages.getString(KEYS.REPORT_RESULTS_MANAGEMENT_WARNINGS), bodyFont));
	        paWarnings.setIndentationLeft(LEFT_IDENTATION);
	        document.add(paWarnings);
	        
	        document.newPage();
    	}
    	else{
    		Paragraph pa = new Paragraph(new Chunk(this.messages.getString(KEYS.REPORT_MANAGEMENT_NOCHLORINECALCULATION), bodyFont));
	        pa.setFirstLineIndent(LEFT_IDENTATION);
	        document.add(pa);
    	}
        
        document.close();
	}
	
	public WaterSystem getWaterSystem(){
		return this.waterSystem;
	}
}