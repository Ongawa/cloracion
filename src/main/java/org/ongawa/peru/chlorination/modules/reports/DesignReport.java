package org.ongawa.peru.chlorination.modules.reports;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
import org.ongawa.peru.chlorination.persistence.elements.ChlorineCalculation;
import org.ongawa.peru.chlorination.persistence.elements.ConductionPipe;
import org.ongawa.peru.chlorination.persistence.elements.CubicReservoir;
import org.ongawa.peru.chlorination.persistence.elements.DistributionPipe;
import org.ongawa.peru.chlorination.persistence.elements.ReliefValve;
import org.ongawa.peru.chlorination.persistence.elements.WaterSystem;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
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
public class DesignReport extends Report {
	
	private IDataSource ds;
	private Properties properties;
	private ResourceBundle messages;
	private WaterSystem waterSystem;

	public DesignReport(WaterSystem waterSystem, File destFile, Locale locale, String author) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
		super(destFile, locale, author);
		if(waterSystem == null)
			throw new NullArgumentException("waterSystem");
		
		this.ds = DataSourceFactory.getInstance().getDefaultDataSource();
		this.properties = ApplicationProperties.getInstance().getProperties();
		this.messages = ResourceBundle.getBundle(this.properties.getProperty(KEYS.REPORT_BUNDLE_NAME), locale);
		
		this.waterSystem = waterSystem;
	}

	@Override
	public void createReport() throws FileNotFoundException, DocumentException {
		Document document = new Document();
		document.addAuthor(author);
		document.addCreationDate();
		document.addLanguage(locale.getLanguage().toLowerCase()+"_"+locale.getCountry().toUpperCase());
        PdfWriter.getInstance(document, new FileOutputStream(this.file));
        document.open();
		
		ChlorineCalculation chlorineCalculation = this.ds.getLastChlorineCalculation(waterSystem);
    	if(chlorineCalculation !=null){
	        Chunk chTitle1 = new Chunk(this.messages.getString(KEYS.REPORT_GENERIC_TITLE_FIRST_CHUNK), this.headerFont);
	        Font headerUnderline = new Font(this.headerFont.getFamily(), this.headerFont.getSize());
	        headerUnderline.setStyle(Font.UNDERLINE);
	        Chunk chTitle2 = new Chunk(this.messages.getString(KEYS.REPORT_DESIGN_TITLE_CHUNK), headerUnderline);
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
			inputTable.addCell(this.messages.getString(KEYS.REPORT_GENERIC_ENDOWMENT)+" "+df.format(waterSystem.getEndowment()));
			inputTable.addCell(this.messages.getString(KEYS.REPORT_GENERIC_COMMUNITY)+" "+waterSystem.getCommunity().getName());
			inputTable.addCell(this.messages.getString(KEYS.REPORT_GENERIC_CHLORINETYPE)+" "+chlorineCalculation.getChlorineType());
			inputTable.addCell(this.messages.getString(KEYS.REPORT_GENERIC_WATERSYSTEM)+" "+waterSystem.getName());
			inputTable.addCell(this.messages.getString(KEYS.REPORT_GENERIC_CHLORINEPURENESS)+" "+df.format(chlorineCalculation.getChlorinePureness()));
			inputTable.addCell(this.messages.getString(KEYS.REPORT_GENERIC_FAMILIESNUM)+" "+chlorineCalculation.getFamiliesNum());
			inputTable.addCell(this.messages.getString(KEYS.REPORT_GENERIC_RELOADTIME)+" "+df.format(chlorineCalculation.getReloadTime()));
			inputTable.addCell(this.messages.getString(KEYS.REPORT_GENERIC_POPULATION)+" "+chlorineCalculation.getPopulation());
			inputTable.addCell(this.messages.getString(KEYS.REPORT_GENERIC_DRIPPINGPERDAY)+" "+df.format(chlorineCalculation.getDrippingHoursPerDay()));
			inputTable.addCell(this.messages.getString(KEYS.REPORT_GENERIC_GROWINGINDEX)+" "+df.format(waterSystem.getGrowingIndex()));
			inputTable.addCell(this.messages.getString(KEYS.REPORT_GENERIC_CHLORINEDEMAND)+" "+df.format(chlorineCalculation.getChlorineDemand()));
			inputTable.addCell(this.messages.getString(KEYS.REPORT_GENERIC_NATURALFLOW)+" "+df.format(chlorineCalculation.getNaturalFlow()));
			inputTable.addCell(this.messages.getString(KEYS.REPORT_GENERIC_CLIMATE)+" XXXXXXXXXXXXXXXXXXX");
			inputTable.getDefaultCell().setColspan(2);
			inputTable.addCell(this.messages.getString(KEYS.REPORT_GENERIC_CURRENTPOPULATIONNEEDEDFLOW)+" XXXXXXXXXXXXXXX");
			inputTable.addCell(this.messages.getString(KEYS.REPORT_GENERIC_FUTUREPOPULATIONNEEDEDFLOW)+" XXXXXXXXXXXXXX");
			document.add(inputTable);
			
			Paragraph paResults = new Paragraph(new Chunk(this.messages.getString(KEYS.REPORT_GENERIC_RESULTS), subHeaderFont));
	        document.add(paResults);
	        BaseColor oldColor = this.bodyFont.getColor();
	        this.bodyFont.setColor(BaseColor.RED);
	        Paragraph pa = new Paragraph(new Chunk(this.messages.getString(KEYS.REPORT_RESULTS_DESIGN_CHLORINEAMOUNTNEEDED).replaceFirst("&result1", "XXXXXXXXX").replaceFirst("&result2", "XXXXXXXXXXXXX"), bodyFont));
	        pa.setFirstLineIndent(LEFT_IDENTATION);
	        document.add(pa);
	        pa = new Paragraph(new Chunk(this.messages.getString(KEYS.REPORT_RESULTS_DESIGN_TANKVOLUME).replaceFirst("&result1", "XXXXXXXXXXXXXX"), bodyFont));
	        pa.setFirstLineIndent(LEFT_IDENTATION);
	        pa.setSpacingAfter(DEFAULT_SPACING);
	        document.add(pa);
	        document.newPage();
	        this.bodyFont.setColor(oldColor);
			
	        pa = new Paragraph(new Chunk(this.messages.getString(KEYS.REPORT_DESIGN_WATERSYSTEMDETAILS_TITLE), subHeaderFont));
	        pa.setSpacingAfter(DEFAULT_SPACING);
	        document.add(pa);
		        
	        List<CubicReservoir> cubicReservoirs = ds.getCubicReservoirs(waterSystem);
	        List<Catchment> catchments = ds.getCatchments(waterSystem);
	        List<DistributionPipe> distributionPipes = ds.getDistributionPipes(waterSystem);
	        List<ConductionPipe> conductionPipes = ds.getConductionPipes(waterSystem);
	        List<ReliefValve> reliefValves = ds.getReliefValves(waterSystem);
	        
	        if(cubicReservoirs.isEmpty() && catchments.isEmpty() && distributionPipes.isEmpty() && conductionPipes.isEmpty() && reliefValves.isEmpty()){
	        	pa = new Paragraph(new Chunk(this.messages.getString(KEYS.REPORT_DESIGN_NODESIGNDATA), bodyFont));
		        pa.setSpacingAfter(DEFAULT_SPACING);
		        document.add(pa);
	        }
	        else{
		        //Font headerTableFont = new Font(this.bodyFont.getFamily(), this.bodyFont.getSize());
		        //headerTableFont.setStyle(Font.BOLD);
				int tableSize = 7;
				PdfPTable wsTable = new PdfPTable(tableSize);
				wsTable.setWidthPercentage(100);
				wsTable.getDefaultCell().setUseAscender(true);
				wsTable.getDefaultCell().setUseDescender(true);
				wsTable.getDefaultCell().setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				wsTable.getDefaultCell().setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
				wsTable.getDefaultCell().setBackgroundColor(LIGHT_GRAY);
					
				wsTable.addCell(this.messages.getString(KEYS.REPORT_DESIGN_WATERSYSTEMDETAILS_NUM));
				wsTable.addCell(this.messages.getString(KEYS.REPORT_DESIGN_WATERSYSTEMDETAILS_NAME));
				wsTable.addCell(this.messages.getString(KEYS.REPORT_DESIGN_WATERSYSTEMDETAILS_ELEMENT));
				wsTable.addCell(this.messages.getString(KEYS.REPORT_DESIGN_WATERSYSTEMDETAILS_ELEMENTSNUM));
				wsTable.addCell(this.messages.getString(KEYS.REPORT_DESIGN_WATERSYSTEMDETAILS_CHLORINEPERELEMENT));
				wsTable.addCell(this.messages.getString(KEYS.REPORT_DESIGN_WATERSYSTEMDETAILS_SPOONSPERELEMENT));
				wsTable.addCell(this.messages.getString(KEYS.REPORT_DESIGN_WATERSYSTEMDETAILS_RETENTIONTIME));
				
				wsTable.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
				for(CubicReservoir cubicReservoir : cubicReservoirs){
					wsTable.addCell(this.df.format(cubicReservoir.getReservoirId()));
					wsTable.addCell(cubicReservoir.getElementName());
					wsTable.addCell(this.messages.getString(KEYS.REPORT_DESIGN_WATERSYSTEMDETAILS_CUBICRESERVOIR));
					wsTable.addCell(this.df.format(cubicReservoir.getCount()));
					wsTable.addCell("XXXXXX");
					wsTable.addCell("XXXXXX");
					wsTable.addCell("XXXXXX");
				}
				
				for(Catchment catchment : catchments){
					wsTable.addCell(this.df.format(catchment.getReservoirId()));
					wsTable.addCell(catchment.getElementName());
					wsTable.addCell(this.messages.getString(KEYS.REPORT_DESIGN_WATERSYSTEMDETAILS_CATCHMENT));
					wsTable.addCell(this.df.format(catchment.getCount()));
					wsTable.addCell("XXXXXX");
					wsTable.addCell("XXXXXX");
					wsTable.addCell("XXXXXX");					
				}
				
				for(DistributionPipe distributionPipe : distributionPipes){
					wsTable.addCell(this.df.format(distributionPipe.getPipeId()));
					wsTable.addCell(distributionPipe.getElementName());
					wsTable.addCell(this.messages.getString(KEYS.REPORT_DESIGN_WATERSYSTEMDETAILS_DISTRIBUTIONPIPE));
					wsTable.addCell(this.df.format(distributionPipe.getCount()));
					wsTable.addCell("XXXXXX");
					wsTable.addCell("XXXXXX");
					wsTable.addCell("XXXXXX");
				}
				
				for(ConductionPipe conducitonPipe : conductionPipes){
					wsTable.addCell(this.df.format(conducitonPipe.getPipeId()));
					wsTable.addCell(conducitonPipe.getElementName());
					wsTable.addCell(this.messages.getString(KEYS.REPORT_DESIGN_WATERSYSTEMDETAILS_CONDUCTIONPIPE));
					wsTable.addCell(this.df.format(conducitonPipe.getCount()));
					wsTable.addCell("XXXXXX");
					wsTable.addCell("XXXXXX");
					wsTable.addCell("XXXXXX");
				}
				
				for(ReliefValve reliefValve : reliefValves){
					wsTable.addCell(this.df.format(reliefValve.getReliefValveId()));
					wsTable.addCell(reliefValve.getElementName());
					wsTable.addCell(this.messages.getString(KEYS.REPORT_DESIGN_WATERSYSTEMDETAILS_RELIEFVALVE));
					wsTable.addCell(this.df.format(reliefValve.getCount()));
					wsTable.addCell("XXXXXX");
					wsTable.addCell("XXXXXX");
					wsTable.addCell("XXXXXX");
				}
				
				wsTable.setSpacingAfter(DEFAULT_SPACING);
				document.add(wsTable);
	        }
	        
	        oldColor = this.bodyFont.getColor();
	        this.bodyFont.setColor(BaseColor.RED);
	        pa = new Paragraph(new Chunk(this.messages.getString(KEYS.REPORT_DESIGN_CHLORINETOTALAMOUNT_RESULTS).replaceFirst("&result1", "XXXXXXXXX"), bodyFont));
	        pa.setFirstLineIndent(LEFT_IDENTATION);
	        document.add(pa);
	        this.bodyFont.setColor(oldColor);
	        
	        pa = new Paragraph(new Chunk(this.messages.getString(KEYS.REPORT_DESIGN_DESINFECTIONSNUMPERYEAR_RESULTS).replaceFirst("&result1", "XXXXXXXXX"), bodyFont));
	        pa.setFirstLineIndent(LEFT_IDENTATION);
	        document.add(pa);
	        document.newPage();
	        
	        pa = new Paragraph(new Chunk(this.messages.getString(KEYS.REPORT_DESIGN_FAMILYFEECALCULATION_TITLE), subHeaderFont));
	        pa.setSpacingAfter(DEFAULT_SPACING*2);
	        document.add(pa);
	        
	        int tableSize = 2;
			PdfPTable feeTable = new PdfPTable(tableSize);
			feeTable.setComplete(false);
			feeTable.setWidthPercentage(50);
			feeTable.setHorizontalAlignment(Element.ALIGN_LEFT);
			feeTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			feeTable.getDefaultCell().setUseAscender(true);
			feeTable.getDefaultCell().setUseDescender(true);
			feeTable.getDefaultCell().setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
			feeTable.getDefaultCell().setFixedHeight(40f);
			
			feeTable.getDefaultCell().setColspan(2);
			feeTable.getDefaultCell().setBackgroundColor(LIGHT_GRAY);
			feeTable.addCell(this.messages.getString(KEYS.REPORT_DESIGN_FEECALCULATION_TABLE_HEADER));
			
			feeTable.getDefaultCell().setColspan(1);
			feeTable.addCell(this.messages.getString(KEYS.REPORT_DESIGN_FEECALCULATION_INPUT_COLUMNS));
			feeTable.addCell(this.messages.getString(KEYS.REPORT_DESIGN_FEECALCULATION_UNIT_COLUMN));
			feeTable.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
			
			feeTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			feeTable.addCell(this.messages.getString(KEYS.REPORT_DESIGN_FEECALCULATION_CHLORINEFORCHLORINATION_ROW));
			feeTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			feeTable.addCell("XXXXXXX");
			
			feeTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			feeTable.addCell(this.messages.getString(KEYS.REPORT_DESIGN_FEECALCULATION_CHLORINEFORDESINFECTION_ROW));
			feeTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			feeTable.addCell("XXXXXXX");
			
			feeTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			feeTable.addCell(this.messages.getString(KEYS.REPORT_DESIGN_FEECALCULATION_SAPSPARES_ROW));
			feeTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			feeTable.addCell("XXXXXXX");
			
			feeTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			feeTable.addCell(this.messages.getString(KEYS.REPORT_DESIGN_FEECALCULATION_JASSMANAGEMENT_ROW));
			feeTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			feeTable.addCell("XXXXXXX");
			
			feeTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			feeTable.addCell(this.messages.getString(KEYS.REPORT_DESIGN_FEECALCULATION_OPERATORPAYMENT_ROW));
			feeTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			feeTable.addCell("XXXXXXX");
			
			feeTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			feeTable.addCell(new Phrase(new Chunk(this.messages.getString(KEYS.REPORT_DESIGN_FEECALCULATION_TOTAL_ROW), this.bodyBoldFont)));
			feeTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			feeTable.addCell(new Phrase(new Chunk("XXXXXXX", this.bodyBoldFont)));
			
			float oldHeight = feeTable.getDefaultCell().getFixedHeight();
			feeTable.getDefaultCell().setFixedHeight(10f);
			feeTable.getDefaultCell().setColspan(2);
			feeTable.addCell("");
			feeTable.getDefaultCell().setFixedHeight(oldHeight);
			feeTable.getDefaultCell().setColspan(1);
			
			feeTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			feeTable.addCell(new Phrase(new Chunk(this.messages.getString(KEYS.REPORT_DESIGN_FEECALCULATION_ONLYCHLORINE), this.bodyFont)));
			feeTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			feeTable.addCell(new Phrase(new Chunk("XXXXXXX", this.bodyFont)));
			document.add(feeTable);
			
			oldColor = this.bodyFont.getColor();
			this.bodyFont.setColor(BaseColor.RED);
			feeTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			feeTable.addCell(new Phrase(new Chunk(this.messages.getString(KEYS.REPORT_DESIGN_FEECALCULATION_FAMILYFEE), this.bodyFont)));
			feeTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			feeTable.addCell(new Phrase(new Chunk("XXXXXXX", this.bodyFont)));
			feeTable.setSpacingAfter(DEFAULT_SPACING*3);
			document.add(feeTable);
			feeTable.setComplete(true);
			this.bodyFont.setColor(oldColor);
	        
			pa = new Paragraph(new Chunk(this.messages.getString(KEYS.REPORT_DESIGN_WARNINGS_TITLE), subHeaderFont));
	        pa.setSpacingAfter(DEFAULT_SPACING);
	        document.add(pa);
	        
	        pa = new Paragraph(new Chunk(this.messages.getString(KEYS.REPORT_DESIGN_WARNINGS_CONTENT), this.bodyFont));
	        pa.setIndentationLeft(LEFT_IDENTATION);
	        document.add(pa);
			
			document.close();
	   	}
	   	else{
	  		throw new DocumentException("There's no info about any chlorination");
	   	}
    }
	
	public WaterSystem getWaterSystem(){
		return this.waterSystem;
	}
}