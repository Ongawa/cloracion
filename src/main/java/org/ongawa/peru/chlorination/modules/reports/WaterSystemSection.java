package org.ongawa.peru.chlorination.modules.reports;

import java.io.IOException;
import java.util.Locale;

import org.apache.commons.lang.NullArgumentException;
import org.ongawa.peru.chlorination.KEYS;
import org.ongawa.peru.chlorination.persistence.elements.WaterSystem;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

/**
 * 
 * @author kiko
 *
 */
public class WaterSystemSection extends ReportSection {

	private WaterSystem waterSystem;
	private boolean addDesignInfo;
	private boolean addChlorineCalculationInfo;
	private boolean addMeasuredFlowInfo;
	private boolean addDesinfectionInfo;
	
	public WaterSystemSection(WaterSystem waterSystem, Locale locale) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
		super(locale);
		if(waterSystem == null)
			throw new NullArgumentException("waterSystem");
		
		this.waterSystem = waterSystem;
		this.addChlorineCalculationInfo = this.addDesignInfo = this.addDesinfectionInfo = this.addMeasuredFlowInfo = false;
	}
	
	private boolean addWaterSystemInfo(Document document) throws DocumentException{
		int tableSize = 11;
		PdfPTable wsTable = new PdfPTable(tableSize);
		wsTable.setWidthPercentage(100);
		wsTable.getDefaultCell().setUseAscender(true);
		wsTable.getDefaultCell().setUseDescender(true);

		//Header
		String[] titles = new String[]{
				this.messages.getString(KEYS.REPORT_WATERSYSTEM_ROW_TITLE_NAME),
				this.messages.getString(KEYS.REPORT_WATERSYSYEM_ROW_TITLE_FAMILIESNUM),
				this.messages.getString(KEYS.REPORT_WATERSYSTEM_ROW_TITLE_POPULATION),
				this.messages.getString(KEYS.REPORT_WATERSYSTEM_ROW_TITLE_POPULATIONFORECAST),
				this.messages.getString(KEYS.REPORT_WATERSYSTEM_ROW_TITLE_GROWINGINDEX),
				this.messages.getString(KEYS.REPORT_WATERSYSTEM_ROW_TITLE_ENDOWMENT),
				this.messages.getString(KEYS.REPORT_WATERSYSTEM_ROW_TITLE_JASSNUM),
				this.messages.getString(KEYS.REPORT_WATERSYSTEM_ROW_TITLE_FUTURENEEDEDFLOW),
				this.messages.getString(KEYS.REPORT_WATERSYSTEM_ROW_TITLE_RESERVOIRVOLUME),
				this.messages.getString(KEYS.REPORT_WATERSYSTEM_ROW_TITLE_SYSTEMELEVATION)
		};
		Chunk ch;
		PdfPCell cell;
		for(int i=0;i<titles.length;i++){
			ch = new Chunk(titles[i], headerFont);
			cell = new PdfPCell(new Phrase(ch));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setBackgroundColor(this.headerColor);
			wsTable.addCell(cell);
		}
		//End header
		
		String[] fields = new String[]{
				waterSystem.getName().replaceAll("null", ""),
				String.valueOf(waterSystem.getFamiliesNum()),
				String.valueOf(waterSystem.getPopulation()),
				this.df.format(waterSystem.getPopulationForecast()),
				this.df.format(waterSystem.getGrowingIndex())+"%",
				this.df.format(waterSystem.getEndowment())+" "+this.messages.getString(KEYS.REPORT_WATERSYSTEM_ROW_UOM_LPPPD),
				String.valueOf(waterSystem.getJASSNum()),
				this.df.format(waterSystem.getFutureNeededFlow())+" lps",
				this.df.format(waterSystem.getReservoirVolume())+" m^3",
				this.df.format(waterSystem.getSystemElevation())+" "+this.messages.getString(KEYS.REPORT_WATERSYSTEM_ROW_UOM_MAMSL)
		};
			
		for(int i=0;i<fields.length;i++){
			ch = new Chunk(fields[i], cellFont);
			cell = new PdfPCell(new Phrase(ch));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			wsTable.addCell(cell);
		}

		wsTable.setSpacingAfter(DEFAULT_SPACING_AFTER);
		document.add(wsTable);
		
		return true;
	}

	@Override
	public boolean addSectionInfo(Document document) throws DocumentException {
		Font wsTitleFont = new Font(FontFamily.HELVETICA, 13);
		wsTitleFont.setStyle(Font.ITALIC|Font.UNDERLINE);
		Chunk chWSTitle = new Chunk(this.messages.getString(KEYS.REPORT_REFERRING_WATERSYSTEM).replaceAll("&watersystemName", waterSystem.getName()), wsTitleFont);
		Paragraph phWSTitle = new Paragraph();
		phWSTitle.setIndentationLeft(LEFT_IDENTATION*2);
		phWSTitle.setSpacingAfter(DEFAULT_SPACING_AFTER);
		phWSTitle.add(new Phrase(chWSTitle));
		document.add(phWSTitle);
		
		this.addWaterSystemInfo(document);
		
		return true;
	}
	
	public WaterSystem getWaterSystem(){
		return this.waterSystem;
	}

	public boolean getAddDesignInfo() {
		return addDesignInfo;
	}

	public void setAddDesignInfo(boolean addDesignInfo) {
		this.addDesignInfo = addDesignInfo;
	}

	public boolean getAddChlorineCalculationInfo() {
		return addChlorineCalculationInfo;
	}

	public void setAddChlorineCalculationInfo(boolean addChlorineCalculationInfo) {
		this.addChlorineCalculationInfo = addChlorineCalculationInfo;
	}

	public boolean getAddMeasuredFlowInfo() {
		return addMeasuredFlowInfo;
	}

	public void setAddMeasuredFlowInfo(boolean addMeasuredFlowInfo) {
		this.addMeasuredFlowInfo = addMeasuredFlowInfo;
	}

	public boolean getAddDesinfectionInfo() {
		return addDesinfectionInfo;
	}

	public void setAddDesinfectionInfo(boolean addDesinfectionInfo) {
		this.addDesinfectionInfo = addDesinfectionInfo;
	}
}
