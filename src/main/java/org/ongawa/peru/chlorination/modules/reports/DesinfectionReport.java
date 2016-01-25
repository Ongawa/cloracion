package org.ongawa.peru.chlorination.modules.reports;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.commons.lang.NullArgumentException;
import org.ongawa.peru.chlorination.ApplicationProperties;
import org.ongawa.peru.chlorination.KEYS;
import org.ongawa.peru.chlorination.persistence.DataSourceFactory;
import org.ongawa.peru.chlorination.persistence.IDataSource;
import org.ongawa.peru.chlorination.persistence.elements.Catchment;
import org.ongawa.peru.chlorination.persistence.elements.CatchmentDesinfection;
import org.ongawa.peru.chlorination.persistence.elements.ConductionPipe;
import org.ongawa.peru.chlorination.persistence.elements.CubicReservoir;
import org.ongawa.peru.chlorination.persistence.elements.CubicReservoirDesinfection;
import org.ongawa.peru.chlorination.persistence.elements.Desinfection;
import org.ongawa.peru.chlorination.persistence.elements.DistributionPipe;
import org.ongawa.peru.chlorination.persistence.elements.PipeDesinfection;
import org.ongawa.peru.chlorination.persistence.elements.ReliefValve;
import org.ongawa.peru.chlorination.persistence.elements.ReliefValveDesinfection;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * 
 * @author kiko
 *
 */
public class DesinfectionReport extends Report {
	
	private IDataSource ds;
	private Properties properties;
	private ResourceBundle messages;
	private Desinfection desinfection;
	
	public DesinfectionReport(Desinfection desinfection, File destFile, Locale locale, String author) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
		super(destFile, locale, author);
		if(desinfection == null)
			throw new NullArgumentException("desinfection");
		
		this.desinfection = desinfection;
		this.ds = DataSourceFactory.getInstance().getDefaultDataSource();
		this.properties = ApplicationProperties.getInstance().getProperties();
		this.messages = ResourceBundle.getBundle(this.properties.getProperty(KEYS.REPORT_BUNDLE_NAME), locale);
	}

	@Override
	public void createReport() throws FileNotFoundException, DocumentException {
		Document document = new Document();
		document.addAuthor(author);
		document.addCreationDate();
		document.addLanguage(locale.getLanguage().toLowerCase()+"_"+locale.getCountry().toUpperCase());
        PdfWriter.getInstance(document, new FileOutputStream(this.file));
        document.open();
        
        Chunk chTitle1 = new Chunk(this.messages.getString(KEYS.REPORT_GENERIC_TITLE_FIRST_CHUNK), this.headerFont);
        Font headerUnderline = new Font(this.headerFont.getFamily(), this.headerFont.getSize());
        headerUnderline.setStyle(Font.UNDERLINE);
        Chunk chTitle2 = new Chunk(this.messages.getString(KEYS.REPORT_DESINFECTION_TITLE_CHUNK), headerUnderline);
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
		
		inputTable.addCell(this.messages.getString(KEYS.REPORT_GENERIC_SUBBASIN)+" "+this.desinfection.getWaterSystem().getCommunity().getSubBasin().getName());
		inputTable.addCell(this.messages.getString(KEYS.REPORT_GENERIC_CHLORINETYPE)+" "+this.desinfection.getChlorineType());
		inputTable.addCell(this.messages.getString(KEYS.REPORT_GENERIC_COMMUNITY)+" "+this.desinfection.getWaterSystem().getCommunity().getName());
		inputTable.addCell(this.messages.getString(KEYS.REPORT_GENERIC_CHLORINEPURENESS)+" "+this.df.format(this.desinfection.getChlorinePureness()));
		inputTable.addCell(this.messages.getString(KEYS.REPORT_GENERIC_WATERSYSTEM)+" "+this.desinfection.getWaterSystem().getName());
		inputTable.addCell(this.messages.getString(KEYS.REPORT_GENERIC_CHLORINEPRICE)+" "+this.df.format(this.desinfection.getChlorinePrice()));
		document.add(inputTable);
		
		Paragraph pa = new Paragraph(new Chunk(this.messages.getString(KEYS.REPORT_DESINFECTION_WATERSYSTEMDETAILS_TITLE), subHeaderFont));
        pa.setSpacingAfter(DEFAULT_SPACING);
        document.add(pa);
        
        int tableSize = 7;
		PdfPTable wsTable = new PdfPTable(tableSize);
		wsTable.setWidthPercentage(100);
		wsTable.getDefaultCell().setUseAscender(true);
		wsTable.getDefaultCell().setUseDescender(true);
		wsTable.getDefaultCell().setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		wsTable.getDefaultCell().setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		wsTable.getDefaultCell().setBackgroundColor(LIGHT_GRAY);
		
		wsTable.addCell(this.messages.getString(KEYS.REPORT_DESINFECTION_WATERSYSTEM_TABLE_NUM));
		wsTable.addCell(this.messages.getString(KEYS.REPORT_DESINFECTION_WATERSYSTEM_TABLE_NAME));
		wsTable.addCell(this.messages.getString(KEYS.REPORT_DESINFECTION_WATERSYSTEM_TABLE_ELEMENT));
		wsTable.addCell(this.messages.getString(KEYS.REPORT_DESINFECTION_WATERSYSTEM_TABLE_ELEMENTSNUM));
		wsTable.addCell(new Phrase(new Chunk(this.messages.getString(KEYS.REPORT_DESINFECTION_WATERSYSTEM_TABLE_CHLORINEPERELEMENT), redBodyFont)));
		wsTable.addCell(new Phrase(new Chunk(this.messages.getString(KEYS.REPORT_DESINFECTION_WATERSYSTEM_TABLE_SPOONSPERELEMENT), redBodyFont)));
		wsTable.addCell(this.messages.getString(KEYS.REPORT_DESINFECTION_WATERSYSTEM_TABLE_RETENTIONTIME));
		wsTable.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
		
		List<CubicReservoir> cubicReservoirs = this.ds.getCubicReservoirs(this.desinfection.getWaterSystem());
		CubicReservoirDesinfection cubicReservoirDesinfection = null;
		List<Catchment> catchments = this.ds.getCatchments(this.desinfection.getWaterSystem());
		CatchmentDesinfection catchmentDesinfection = null;
		List<DistributionPipe> distributionPipes = this.ds.getDistributionPipes(this.desinfection.getWaterSystem());
		List<ConductionPipe> conductionPipes = this.ds.getConductionPipes(this.desinfection.getWaterSystem());
		PipeDesinfection pipeDesinfection = null;
		List<ReliefValve> reliefValves = this.ds.getReliefValves(this.desinfection.getWaterSystem());
		ReliefValveDesinfection reliefValveDesinfection = null;
		int num = 1, tempCount = 0;
		double totalChlorineAmount = 0, tempAmount = 0;
		
		for(CubicReservoir cubicReservoir : cubicReservoirs){
			cubicReservoirDesinfection = this.ds.getCubicReservoirDesinfection(cubicReservoir, this.desinfection);
			wsTable.addCell(String.valueOf(num));
			wsTable.addCell(cubicReservoir.getElementName());
			wsTable.addCell(this.messages.getString(KEYS.REPORT_DESINFECTION_WATERSYSTEM_TABLE_ELEMENT_CUBICRESERVOIR));
			tempCount = cubicReservoirDesinfection.getCount();
			wsTable.addCell(String.valueOf(tempCount));
			tempAmount = cubicReservoirDesinfection.getChlorineQty();
			totalChlorineAmount += (tempAmount*tempCount);
			wsTable.addCell(new Phrase(new Chunk(this.df.format(tempAmount), redBodyFont)));
			wsTable.addCell(new Phrase(new Chunk(this.df.format(cubicReservoirDesinfection.getDemandSpoons()), redBodyFont)));
			wsTable.addCell(this.df.format(cubicReservoirDesinfection.getRetentionTime()));
			num++;
		}
		
		for(Catchment catchment : catchments){
			catchmentDesinfection = this.ds.getCatchmentDesinfection(catchment, desinfection);
			wsTable.addCell(String.valueOf(num));
			wsTable.addCell(catchment.getElementName());
			wsTable.addCell(this.messages.getString(KEYS.REPORT_DESINFECTION_WATERSYSTEM_TABLE_ELEMENT_CATCHMENT));
			tempCount = catchmentDesinfection.getCount();
			wsTable.addCell(String.valueOf(tempCount));
			tempAmount = catchmentDesinfection.getChlorineQty();
			totalChlorineAmount += tempAmount;
			wsTable.addCell(new Phrase(new Chunk(this.df.format(tempAmount), redBodyFont)));
			wsTable.addCell(new Phrase(new Chunk(this.df.format(catchmentDesinfection.getDemandSpoons()), redBodyFont)));
			wsTable.addCell(this.df.format(catchmentDesinfection.getRetentionTime()));
			num++;
		}
		
		for(DistributionPipe pipe : distributionPipes){
			pipeDesinfection = this.ds.getPipeDesinfection(pipe, desinfection);
			wsTable.addCell(String.valueOf(num));
			wsTable.addCell(pipe.getElementName());
			wsTable.addCell(this.messages.getString(KEYS.REPORT_DESINFECTION_WATERSYSTEM_TABLE_ELEMENT_DISTRIBUTIONPIPE));
			tempCount = pipeDesinfection.getCount();
			wsTable.addCell(String.valueOf(tempCount));
			tempAmount = pipeDesinfection.getChlorineQty();
			totalChlorineAmount += tempAmount;
			wsTable.addCell(new Phrase(new Chunk(this.df.format(tempAmount), redBodyFont)));
			wsTable.addCell(new Phrase(new Chunk(this.df.format(pipeDesinfection.getDemandSpoons()), redBodyFont)));
			wsTable.addCell(this.df.format(pipeDesinfection.getRetentionTime()));
			num++;
		}
		
		for(ConductionPipe pipe : conductionPipes){
			pipeDesinfection = this.ds.getPipeDesinfection(pipe, desinfection);
			wsTable.addCell(String.valueOf(num));
			wsTable.addCell(pipe.getElementName());
			wsTable.addCell(this.messages.getString(KEYS.REPORT_DESINFECTION_WATERSYSTEM_TABLE_ELEMENT_CONDUCTIONPIPE));
			tempCount = pipeDesinfection.getCount();
			wsTable.addCell(String.valueOf(tempCount));
			tempAmount = pipeDesinfection.getChlorineQty();
			totalChlorineAmount += tempAmount;
			wsTable.addCell(new Phrase(new Chunk(this.df.format(tempAmount), redBodyFont)));
			wsTable.addCell(new Phrase(new Chunk(this.df.format(pipeDesinfection.getDemandSpoons()), redBodyFont)));
			wsTable.addCell(this.df.format(pipeDesinfection.getRetentionTime()));
			num++;
		}
		
		for(ReliefValve reliefValve : reliefValves){
			reliefValveDesinfection = this.ds.getReliefValveDesinfection(reliefValve, desinfection);
			wsTable.addCell(String.valueOf(num));
			wsTable.addCell(reliefValve.getElementName());
			wsTable.addCell(this.messages.getString(KEYS.REPORT_DESINFECTION_WATERSYSTEM_TABLE_ELEMENT_RELIEFVALVE));
			tempCount = reliefValveDesinfection.getCount();
			wsTable.addCell(String.valueOf(tempCount));
			tempAmount = reliefValveDesinfection.getChlorineQty();
			totalChlorineAmount += tempAmount;
			wsTable.addCell(new Phrase(new Chunk(this.df.format(tempAmount), redBodyFont)));
			wsTable.addCell(new Phrase(new Chunk(this.df.format(reliefValveDesinfection.getDemandSpoons()), redBodyFont)));
			wsTable.addCell(this.df.format(reliefValveDesinfection.getRetentionTime()));
			num++;
		}
		wsTable.setSpacingAfter(DEFAULT_SPACING);
		document.add(wsTable);
		
		pa = new Paragraph(new Chunk(this.messages.getString(KEYS.REPORT_GENERIC_RESULTS), subHeaderFont));
		pa.setSpacingAfter(DEFAULT_SPACING);
        document.add(pa);
        
        pa = new Paragraph(new Chunk(this.messages.getString(KEYS.REPORT_DESINFECTION_RESULTS_TOTALCHLORINEAMOUNT).replaceAll("&result1", df.format(totalChlorineAmount/1000)), redBodyFont));
        pa.setFirstLineIndent(LEFT_IDENTATION);
        document.add(pa);
        df = new DecimalFormat("#.##");
        pa = new Paragraph(new Chunk(this.messages.getString(KEYS.REPORT_DESINFECTION_RESULTS_DESINFECTIONCOST).replaceAll("&result1",  df.format((totalChlorineAmount/1000)*this.desinfection.getChlorinePrice()))));
        pa.setFirstLineIndent(LEFT_IDENTATION);
        pa.setSpacingAfter(DEFAULT_SPACING*2);
        document.add(pa);
        
        pa = new Paragraph(new Chunk(this.messages.getString(KEYS.REPORT_DESIGN_WARNINGS_TITLE), subHeaderFont));
        pa.setSpacingAfter(DEFAULT_SPACING);
        document.add(pa);
        
        pa = new Paragraph(new Chunk(this.messages.getString(KEYS.REPORT_DESINFECTION_WARNINGS_CONTENT), this.bodyFont));
        pa.setIndentationLeft(LEFT_IDENTATION);
        document.add(pa);
		
		document.close();
	}
	
	public Desinfection getDesinfection(){
		return this.desinfection;
	}
}