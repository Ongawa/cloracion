package org.ongawa.peru.chlorination.modules.reports;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.ongawa.peru.chlorination.persistence.elements.ChlorineCalculation;
import org.ongawa.peru.chlorination.persistence.elements.Community;
import org.ongawa.peru.chlorination.persistence.elements.CubicReservoir;
import org.ongawa.peru.chlorination.persistence.elements.MeasuredFlow;
import org.ongawa.peru.chlorination.persistence.elements.MeasuringPoint;
import org.ongawa.peru.chlorination.persistence.elements.Pipe;
import org.ongawa.peru.chlorination.persistence.elements.ReliefValve;
import org.ongawa.peru.chlorination.persistence.elements.SubBasin;
import org.ongawa.peru.chlorination.persistence.elements.WaterSpring;
import org.ongawa.peru.chlorination.persistence.elements.WaterSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * @author Kiko
 */
public class FullReport {
	
	private static Logger log;
	static{
		log = LoggerFactory.getLogger(FullReport.class);
	}
	
	private static final float LEFT_IDENTATION = 10f;
	
	private Properties properties;
	private IDataSource ds;
	private ResourceBundle messages;
	
	private Font bodyFont;
	private BaseColor headerColor;
	private BaseColor row1Color;
	private BaseColor row2Color;
	
	public FullReport(String language, String country) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		if(language == null)
			throw new NullArgumentException("language");
		if(country == null)
			throw new NullArgumentException("country");
		
		this.bodyFont = new Font(FontFamily.HELVETICA, 12);
		this.headerColor = new BaseColor(106, 180, 189);
		this.row1Color = BaseColor.WHITE;
		this.row2Color = new BaseColor(229, 244, 246);

		this.properties = ApplicationProperties.getInstance().getProperties();
		this.ds = DataSourceFactory.getInstance().getDefaultDataSource();
		Locale currentLocale = new Locale(language, country);
		this.messages = ResourceBundle.getBundle(this.properties.getProperty(KEYS.REPORT_BUNDLE_NAME), currentLocale);
		log.info("Loaded i18n for reports: "+language.toLowerCase()+"_"+country.toUpperCase());
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
	
	public void addSubBasinInfo(Document document, SubBasin subBasin, List<Community> communities, boolean listCommunities) throws DocumentException{
		Font subBasinTitleFont = new Font(FontFamily.HELVETICA, 19);
		subBasinTitleFont.setStyle(Font.BOLD);
        Chunk chSubBasinTitle = new Chunk(this.messages.getString(KEYS.REPORT_REFERRING_SUBBASIN).replaceAll("&subbasinName", subBasin.getName()), subBasinTitleFont);
        Paragraph phSubBasinTitle = new Paragraph();
        phSubBasinTitle.setSpacingAfter(20);
        phSubBasinTitle.add(new Phrase(chSubBasinTitle));
        document.add(phSubBasinTitle);
        
        if(communities == null || communities.size()==0){
        	Paragraph phNoComs = new Paragraph();
        	phNoComs.setFirstLineIndent(LEFT_IDENTATION);
            Chunk chNoComs = new Chunk(this.messages.getString(KEYS.REPORT_SUBBASIN_WITHOUT_COMMUNITIES).replaceFirst("&subbasinName", subBasin.getName()), this.bodyFont);
            phNoComs.add(chNoComs);
            document.add(phNoComs);
        }
        else if(listCommunities){
	        Paragraph phIntroducingComs = new Paragraph();
	        phIntroducingComs.setFirstLineIndent(LEFT_IDENTATION);
	        Chunk chIntroducingComs = new Chunk(this.messages.getString(KEYS.REPORT_INDTRODUCING_COMMUNITIES).replaceFirst("&subbasinName", subBasin.getName()), this.bodyFont);
	        phIntroducingComs.add(chIntroducingComs);
	        document.add(phIntroducingComs);
	        
	        com.itextpdf.text.List listCom = new com.itextpdf.text.List();
	        listCom.setListSymbol("\u2022");
	        listCom.setIndentationLeft(60);
	        Chunk ch;
		    for(Community community : communities){
		    	ch = new Chunk(community.getName());
		    	ch.setFont(this.bodyFont);
		    	listCom.add(new ListItem(ch));
		    }
		    Paragraph phListCom = new Paragraph();
		    phListCom.setSpacingAfter(10);
		    phListCom.add(listCom);
		    
		    document.add(phListCom);
        }
	}
	
	public void addCommunityInfo(Document document, Community community, List<WaterSystem> waterSystems, boolean listWaterSystems) throws DocumentException{
		Font communityTitleFont = new Font(FontFamily.HELVETICA, 15);
		communityTitleFont.setStyle(Font.UNDERLINE|Font.ITALIC);
		Chunk chCommunityTitle = new Chunk(this.messages.getString(KEYS.REPORT_REFERRING_COMMUNITY).replaceAll("&communityName", community.getName()), communityTitleFont);
		Paragraph phCommunityTitle = new Paragraph();
		phCommunityTitle.setSpacingBefore(30);
		phCommunityTitle.setSpacingAfter(5);
		phCommunityTitle.add(new Phrase(chCommunityTitle));
		document.add(phCommunityTitle);
		
		if(waterSystems == null || waterSystems.size()==0){
			Paragraph phNoWS = new Paragraph();
			phNoWS.setFirstLineIndent(LEFT_IDENTATION);
	        Chunk chNoWS = new Chunk(this.messages.getString(KEYS.REPORT_COMMUNITY_WITHOUT_WATERSYSTEM).replaceFirst("&communityName", community.getName()), this.bodyFont);
	        phNoWS.add(chNoWS);
	        phNoWS.setSpacingAfter(10);
	        document.add(phNoWS);
		}
		else{
			Paragraph phIntroducingWS = new Paragraph();
			phIntroducingWS.setFirstLineIndent(LEFT_IDENTATION);
	        Chunk chIntroducingWS = new Chunk(this.messages.getString(KEYS.REPORT_INTRODUCING_WATERSYSTEMS).replaceFirst("&communityName", community.getName()), this.bodyFont);
	        phIntroducingWS.add(chIntroducingWS);
	        phIntroducingWS.setSpacingAfter(10);
	        document.add(phIntroducingWS);
	        if(listWaterSystems){
		          com.itextpdf.text.List listWS = new com.itextpdf.text.List();
		          listWS.setListSymbol("\u2022");
		          listWS.setIndentationLeft(5);
		          Chunk ch;
		  	    for(WaterSystem waterSystem : waterSystems){
		  	    	ch = new Chunk(waterSystem.getName());
		  	    	ch.setFont(this.bodyFont);
		  	    	listWS.add(new ListItem(ch));
		  	    }
		  	    Paragraph phListWS = new Paragraph();
		  	    phListWS.setSpacingAfter(10);
		  	    phListWS.add(listWS);
		  	    document.add(phListWS);
	        }
		}
	}
	
	public void addWaterSystemsTable(Document document, List<WaterSystem> waterSystems) throws DocumentException{
		Font headerFont = new Font(this.bodyFont.getFamily(), 10);
		PdfPTable wsTable = new PdfPTable(11);
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
			if(i==0)
				cell.setColspan(2);
			wsTable.addCell(cell);
		}
		//End header
		
		int cellIndex = 0;
		for(WaterSystem waterSystem : waterSystems){
			Font cellFont = new Font(this.bodyFont.getFamily(), 10);
			
			String[] fields = new String[]{
					waterSystem.getName().replaceAll("null", ""),
					String.valueOf(waterSystem.getFamiliesNum()),
					String.valueOf(waterSystem.getPopulation()),
					String.valueOf(waterSystem.getPopulationForecast()),
					String.valueOf(waterSystem.getGrowingIndex())+"%",
					String.valueOf(waterSystem.getEndowment())+" "+this.messages.getString(KEYS.REPORT_WATERSYSTEM_ROW_UOM_LPPPD),
					String.valueOf(waterSystem.getJASSNum()),
					String.valueOf(waterSystem.getFutureNeededFlow())+" "+this.messages.getString(KEYS.REPORT_WATERSYSTEM_ROW_UOM_LPS),
					String.valueOf(waterSystem.getReservoirVolume())+" m^3",
					String.valueOf(waterSystem.getSystemElevation())+" "+this.messages.getString(KEYS.REPORT_WATERSYSTEM_ROW_UOM_MAMSL)
			};
			
			for(int i=0;i<fields.length;i++){
				ch = new Chunk(fields[i], cellFont);
				cell = new PdfPCell(new Phrase(ch));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				if(i==0)
					cell.setColspan(2);
				if(cellIndex%2==0)
					cell.setBackgroundColor(this.row1Color);
				else
					cell.setBackgroundColor(this.row2Color);
				wsTable.addCell(cell);
			}
			cellIndex++;
		}
		wsTable.setSpacingAfter(20);
		document.add(wsTable);
	}
	
	public void addWaterSystemTable(Document document, WaterSystem waterSystem, boolean addWaterSystemName) throws DocumentException{
		Font headerFont = new Font(this.bodyFont.getFamily(), 10);
		int tableSize = (addWaterSystemName)?11:9;
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
			if(i==0){
				if( addWaterSystemName){
					cell.setColspan(2);
					wsTable.addCell(cell);
				}
			}
			else
				wsTable.addCell(cell);
		}
		//End header
		
		Font cellFont = new Font(this.bodyFont.getFamily(), 10);
		String[] fields = new String[]{
				waterSystem.getName().replaceAll("null", ""),
				String.valueOf(waterSystem.getFamiliesNum()),
				String.valueOf(waterSystem.getPopulation()),
				String.valueOf(waterSystem.getPopulationForecast()),
				String.valueOf(waterSystem.getGrowingIndex())+"%",
				String.valueOf(waterSystem.getEndowment())+" "+this.messages.getString(KEYS.REPORT_WATERSYSTEM_ROW_UOM_LPPPD),
				String.valueOf(waterSystem.getJASSNum()),
				String.valueOf(waterSystem.getFutureNeededFlow())+" lps",
				String.valueOf(waterSystem.getReservoirVolume())+" m^3",
				String.valueOf(waterSystem.getSystemElevation())+" "+this.messages.getString(KEYS.REPORT_WATERSYSTEM_ROW_UOM_MAMSL)
		};
			
		for(int i=0;i<fields.length;i++){
			ch = new Chunk(fields[i], cellFont);
			cell = new PdfPCell(new Phrase(ch));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			if(i==0){
				if( addWaterSystemName){
					cell.setColspan(2);
					wsTable.addCell(cell);
				}
			}
			else
				wsTable.addCell(cell);
		}

		wsTable.setSpacingAfter(20);
		document.add(wsTable);
	}
	
	public void addWaterSystemInfo(Document document, WaterSystem waterSystem) throws DocumentException{
		Font wsTitleFont = new Font(FontFamily.HELVETICA, 13);
		wsTitleFont.setStyle(Font.ITALIC|Font.UNDERLINE);
		Chunk chWSTitle = new Chunk(this.messages.getString(KEYS.REPORT_REFERRING_WATERSYSTEM).replaceAll("&watersystemName", waterSystem.getName()), wsTitleFont);
		Paragraph phWSTitle = new Paragraph();
		phWSTitle.setIndentationLeft(LEFT_IDENTATION*2);
		phWSTitle.setSpacingAfter(10);
		phWSTitle.add(new Phrase(chWSTitle));
		document.add(phWSTitle);
	}
	
	private void addMeasuredFlowsTable(Document document, List<MeasuredFlow> measuredFlows) throws DocumentException{
		Font headerFont = new Font(this.bodyFont.getFamily(), 10);
		PdfPTable mfTable = new PdfPTable(4);
		mfTable.setWidthPercentage(100);
		mfTable.getDefaultCell().setUseAscender(true);
		mfTable.getDefaultCell().setUseDescender(true);
		
		//Header
		String[] titles = new String[]{
				this.messages.getString(KEYS.REPORT_MEASUREDFLOW_ROW_TITLE_WATERSPRING),
				this.messages.getString(KEYS.REPORT_MEASUREDFLOW_ROW_TITLE_MEASURINGPOING),
				this.messages.getString(KEYS.REPORT_MEASUREDFLOW_ROW_TITLE_DATE),
				this.messages.getString(KEYS.REPORT_MEASUREDFLOW_ROW_TITLE_FLOW)
		};
		Chunk ch;
		PdfPCell cell;
		for(int i=0;i<titles.length;i++){
			ch = new Chunk(titles[i], headerFont);
			cell = new PdfPCell(new Phrase(ch));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setBackgroundColor(this.headerColor);
			mfTable.addCell(cell);
		}
		//End header
		
		int cellIndex = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		String comments;
		for(MeasuredFlow measuredFlow : measuredFlows){
			Font cellFont = new Font(this.bodyFont.getFamily(), 10);
			
			String[] fields = new String[]{
					measuredFlow.getMeasuringPoint().getWaterSpring().getName(),
					measuredFlow.getMeasuringPoint().getName(),
					sdf.format(measuredFlow.getDate()),
					String.valueOf(measuredFlow.getFlow()+" "+this.messages.getString(KEYS.REPORT_MEASUREDFLOW_ROW_UOM_LPS))
			};
			BaseColor backgroundColor = (cellIndex%2==0)?this.row1Color:this.row2Color;
			for(int i=0;i<fields.length;i++){
				ch = new Chunk(fields[i], cellFont);
				cell = new PdfPCell(new Phrase(ch));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setBackgroundColor(backgroundColor);
				mfTable.addCell(cell);
			}
			comments = measuredFlow.getComments();
			if(comments != null && !comments.trim().equals("")){
				cell = new PdfPCell(new Phrase(this.messages.getString(KEYS.REPORT_MEASUREDFLOW_ROW_TITLE_COMMENTS), cellFont));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setBackgroundColor(backgroundColor);
				mfTable.addCell(cell);
				
				cell = new PdfPCell(new Phrase(comments, cellFont));
				cell.setColspan(3);
				cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setBackgroundColor(backgroundColor);
				mfTable.addCell(cell);
			}
			cellIndex++;
		}
		
		mfTable.setSpacingAfter(10);
		document.add(mfTable);
	}
	
	public void addMeasuredFlows(Document document, List<MeasuredFlow> measuredFlows) throws DocumentException{
		Font mfTitleFont = new Font(FontFamily.HELVETICA, 12);
		mfTitleFont.setStyle(Font.ITALIC);
		Chunk chMFTitle = new Chunk(this.messages.getString(KEYS.REPORT_REFERRING_MEASUREDFLOWS).replaceAll("&measuredflowsNum", String.valueOf(measuredFlows.size())), mfTitleFont);
		Paragraph phMFTitle = new Paragraph();
		phMFTitle.setAlignment(Paragraph.ALIGN_CENTER);
		phMFTitle.setSpacingAfter(10);
		phMFTitle.add(new Phrase(chMFTitle));
		document.add(phMFTitle);	
	}
	
	private List<MeasuredFlow> getMeasuredFlows(WaterSystem waterSystem){
		List<MeasuredFlow> measuredFlows = new ArrayList<MeasuredFlow>();
		List<WaterSpring> waterSprings = this.ds.getWaterSpringsInWaterSystem(waterSystem);
		for(WaterSpring waterSpring : waterSprings){
			List<MeasuringPoint> measuringPoints = this.ds.getMeasuringPoints(waterSystem, waterSpring);
			for(MeasuringPoint measuringPoint : measuringPoints)
				measuredFlows.addAll(this.ds.getMeasuredFlows(measuringPoint));
		}
		
		return measuredFlows;
	}
	
	private void addChlorineCalculationsTable(Document document, List<ChlorineCalculation> chlorineCalculations) throws DocumentException{
		Font headerFont = new Font(this.bodyFont.getFamily(), 10);
		PdfPTable ccTable = new PdfPTable(11);
		ccTable.setWidthPercentage(100);
		ccTable.getDefaultCell().setUseAscender(true);
		ccTable.getDefaultCell().setUseDescender(true);
		
		//Header
		String[] titles = new String[]{
				this.messages.getString(KEYS.REPORT_CHLORINECALCULATION_ROW_TITLE_DATE),
				this.messages.getString(KEYS.REPORT_CHLORINECALCULATION_ROW_TITLE_POPULATION),
				this.messages.getString(KEYS.REPORT_CHLORINECALCULATION_ROW_TITLE_TANKVOLUME),
				this.messages.getString(KEYS.REPORT_CHLORINECALCULATION_ROW_TITLE_TANKUSEFULVOLUME),
				this.messages.getString(KEYS.REPORT_CHLORINECALCULATION_ROW_TITLE_ENDOWMENT),
				this.messages.getString(KEYS.REPORT_CHLORINECALCULATION_ROW_TITLE_CHLORINEPURENESS),
				this.messages.getString(KEYS.REPORT_CHLORINECALCULATION_ROW_TITLE_INPUTFLOW),
				this.messages.getString(KEYS.REPORT_CHLORINECALCULATION_ROW_TITLE_RELOADTIME),
				this.messages.getString(KEYS.REPORT_CHLORINECALCULATION_ROW_TITLE_DEMANDCLR),
				this.messages.getString(KEYS.REPORT_CHLORINECALCULATION_ROW_TITLE_DEMANDACTIVECHLORINE),
				this.messages.getString(KEYS.REPORT_CHLORINECALCULATION_ROW_TITLE_DEMANDCOMMONPRODUCT)
		};
		Chunk ch;
		PdfPCell cell;
		for(int i=0;i<titles.length;i++){
			ch = new Chunk(titles[i], headerFont);
			cell = new PdfPCell(new Phrase(ch));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setBackgroundColor(this.headerColor);
			ccTable.addCell(cell);
		}
		//End header
		
		int cellIndex = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		for(ChlorineCalculation chlorineCalculation : chlorineCalculations){
			Font cellFont = new Font(this.bodyFont.getFamily(), 10);
			String[] fields = new String[]{
					sdf.format(chlorineCalculation.getDate()),
					String.valueOf(chlorineCalculation.getPopulation()),
					String.valueOf(chlorineCalculation.getTankVolume())+" "+this.messages.getString(KEYS.REPORT_CHLORINECALCULATION_ROW_UOM_L),
					String.valueOf(chlorineCalculation.getTankUsefulVolume())+" "+this.messages.getString(KEYS.REPORT_CHLORINECALCULATION_ROW_UOM_L),
					String.valueOf(chlorineCalculation.getEndowment())+" "+this.messages.getString(KEYS.REPORT_CHLORINECALCULATION_ROW_UOM_LPPPD),
					String.valueOf(chlorineCalculation.getChlorinePureness())+"%",
					String.valueOf(chlorineCalculation.getInputFlow())+" "+this.messages.getString(KEYS.REPORT_CHLORINECALCULATION_ROW_UOM_LPS),
					String.valueOf(chlorineCalculation.getReloadTime())+" "+this.messages.getString(KEYS.REPORT_CHLORINECALCULATION_ROW_UOM_DAYS),
					String.valueOf(chlorineCalculation.getDemandCLR())+" "+this.messages.getString(KEYS.REPORT_CHLORINECALCULATION_ROW_UOM_MGPL),
					String.valueOf(chlorineCalculation.getDemandActiveChlorine())+" "+this.messages.getString(KEYS.REPORT_CHLORINECALCULATION_ROW_UOM_MGPL),
					String.valueOf(chlorineCalculation.getDemandCommonProduct())+" "+this.messages.getString(KEYS.REPORT_CHLORINECALCULATION_ROW_UOM_MGPL)
			};
			BaseColor backgroundColor = (cellIndex%2==0)?this.row1Color:this.row2Color;
			for(int i=0;i<fields.length;i++){
				ch = new Chunk(fields[i], cellFont);
				cell = new PdfPCell(new Phrase(ch));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setBackgroundColor(backgroundColor);
				ccTable.addCell(cell);
			}
			cellIndex++;
		}
		
		ccTable.setSpacingAfter(10);
		document.add(ccTable);
	}
	
	public void addChlorineCalculations(Document document, List<ChlorineCalculation> chlorineCalculations) throws DocumentException{
		Font ccTitleFont = new Font(FontFamily.HELVETICA, 12);
		ccTitleFont.setStyle(Font.ITALIC);
		Chunk chCCTitle = new Chunk(this.messages.getString(KEYS.REPORT_REFERRING_CHLORINECALCULATION).replaceAll("&chlorinecalculationNum", String.valueOf(chlorineCalculations.size())), ccTitleFont);
		Paragraph phCCTitle = new Paragraph();
		phCCTitle.setAlignment(Paragraph.ALIGN_CENTER);
		phCCTitle.setSpacingAfter(10);
		phCCTitle.add(new Phrase(chCCTitle));
		document.add(phCCTitle);
	}
	
	public void addWaterSystemDesign(Document document, boolean isInfoAvailable) throws DocumentException{
		Font wsDSTitleFont = new Font(FontFamily.HELVETICA, 12);
		wsDSTitleFont.setStyle(Font.ITALIC);
		Chunk wsDSCCTitle = new Chunk((isInfoAvailable)?this.messages.getString(KEYS.REPORT_REFERRING_WATERSYSTEM_DESIGN):this.messages.getString(KEYS.REPORT_WATERSYSTEM_WITHOUT_DESIGN), wsDSTitleFont);
		Paragraph phWsDsTitle = new Paragraph();
		phWsDsTitle.setAlignment(Paragraph.ALIGN_CENTER);
		phWsDsTitle.setSpacingAfter(10);
		phWsDsTitle.add(new Phrase(wsDSCCTitle));
		document.add(phWsDsTitle);
	}
	
	private void fillTableWithCubicReservoirs(PdfPTable table, List<CubicReservoir> cubicReservoirs){
		Chunk ch;
		PdfPCell cell;
		int cellIndex = 0;
		for(CubicReservoir cubicReservoir : cubicReservoirs){
			Font cellFont = new Font(this.bodyFont.getFamily(), 10);
			
			String[] fields = new String[]{
					cubicReservoir.getName(),
					cubicReservoir.getWidth()+" "+this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_CUBICRESERVOIR_UOM_METERS),
					cubicReservoir.getLength()+" "+this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_CUBICRESERVOIR_UOM_METERS),
					cubicReservoir.getHeight()+" "+this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_CUBICRESERVOIR_UOM_METERS),
					cubicReservoir.getVolume()+" "+this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_CUBICRESERVOIR_UOM_CUBICMETERS),
					String.valueOf(cubicReservoir.getCount()),
					cubicReservoir.getCombinedVolume()+" "+this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_CUBICRESERVOIR_UOM_CUBICMETERS),
					cubicReservoir.getRequiredConcentration()+" "+this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_CUBICRESERVOIR_UOM_GRL),
			};
			BaseColor backgroundColor = (cellIndex%2==0)?this.row1Color:this.row2Color;
			for(int i=0;i<fields.length;i++){
				ch = new Chunk(fields[i], cellFont);
				cell = new PdfPCell(new Phrase(ch));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setBackgroundColor(backgroundColor);
				table.addCell(cell);
			}
			cellIndex++;
		}
	}
	
	private void fillTableWithPipes(PdfPTable table, List<Pipe> pipes){
		Chunk ch;
		PdfPCell cell;
		int cellIndex = 0;
		for(Pipe pipe : pipes){
			Font cellFont = new Font(this.bodyFont.getFamily(), 10);
			
			String[] fields = new String[]{
					pipe.getName(),
					pipe.getDiameter()+" "+this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_PIPE_UOM_METERS),
					pipe.getLength()+" "+this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_PIPE_UOM_METERS),
					pipe.getVolume()+" "+this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_PIPE_UOM_CUBICMETERS),
					String.valueOf(pipe.getCount()),
					pipe.getCombinedVolume()+" "+this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_PIPE_UOM_CUBICMETERS),
					pipe.getRequiredConcentration()+" "+this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_PIPE_UOM_GRL),
			};
			BaseColor backgroundColor = (cellIndex%2==0)?this.row1Color:this.row2Color;
			for(int i=0;i<fields.length;i++){
				ch = new Chunk(fields[i], cellFont);
				cell = new PdfPCell(new Phrase(ch));
				if(i==1)
					cell.setColspan(2);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setBackgroundColor(backgroundColor);
				table.addCell(cell);
			}
			cellIndex++;
		}
	}
	
	private void fillTableWithReliefValves(PdfPTable table, List<ReliefValve> reliefValves){
		Chunk ch;
		PdfPCell cell;
		int cellIndex = 0;
		for(ReliefValve reliefValve : reliefValves){
			Font cellFont = new Font(this.bodyFont.getFamily(), 10);
			
			String[] fields = new String[]{
					reliefValve.getName(),
					reliefValve.getWidth()+" "+this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_RELIEFVALVE_UOM_METERS),
					reliefValve.getLength()+" "+this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_RELIEFVALVE_UOM_METERS),
					reliefValve.getHeight()+" "+this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_RELIEFVALVE_UOM_METERS),
					reliefValve.getVolume()+" "+this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_RELIEFVALVE_UOM_CUBICMETERS),
					String.valueOf(reliefValve.getCount()),
					reliefValve.getCombinedVolume()+" "+this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_RELIEFVALVE_UOM_CUBICMETERS),
					reliefValve.getRequiredConcentration()+" "+this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_RELIEFVALVE_UOM_GRL),
			};
			BaseColor backgroundColor = (cellIndex%2==0)?this.row1Color:this.row2Color;
			for(int i=0;i<fields.length;i++){
				ch = new Chunk(fields[i], cellFont);
				cell = new PdfPCell(new Phrase(ch));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setBackgroundColor(backgroundColor);
				table.addCell(cell);
			}
			cellIndex++;
		}
	}
	
	private void addWaterSystemDesignTable(Document document, List<CubicReservoir> cubicReservoirs, List<Pipe> pipes, List<ReliefValve> reliefValves) throws DocumentException{
		Font headerFont = new Font(this.bodyFont.getFamily(), 10);
		int tableColumns = 8;
		PdfPTable wsDSTable = new PdfPTable(tableColumns);
		wsDSTable.setWidthPercentage(100);
		wsDSTable.getDefaultCell().setUseAscender(true);
		wsDSTable.getDefaultCell().setUseDescender(true);
		
		//CubicReservoir
		Chunk ch;
		PdfPCell cell;
		
		ch = new Chunk(this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_CUBICRESERVOIR), headerFont);
		cell = new PdfPCell(new Phrase(ch));
		cell.setColspan(tableColumns);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setBackgroundColor(this.headerColor);
		wsDSTable.addCell(cell);
		
		if(!cubicReservoirs.isEmpty()){
			String[] titles = new String[]{
					this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_CUBICRESERVOIR_ROW_NAME),
					this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_CUBICRESERVOIR_ROW_WIDTH),
					this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_CUBICRESERVOIR_ROW_LENGTH),
					this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_CUBICRESERVOIR_ROW_HEIGHT),
					this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_CUBICRESERVOIR_ROW_VOLUME),
					this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_CUBICRESERVOIR_ROW_COUNT),
					this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_CUBICRESERVOIR_ROW_COMBINEDVOLUME),
					this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_CUBICRESERVOIR_ROW_REQUIREDCONCENTRATION)
			};
			for(int i=0;i<titles.length;i++){
				ch = new Chunk(titles[i], headerFont);
				cell = new PdfPCell(new Phrase(ch));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setBackgroundColor(this.headerColor);
				wsDSTable.addCell(cell);
			}
			
			this.fillTableWithCubicReservoirs(wsDSTable, cubicReservoirs);
		}
		else{
			ch = new Chunk(this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_NO_CUBICRESERVOIR), headerFont);
			cell = new PdfPCell(new Phrase(ch));
			cell.setColspan(tableColumns);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setBackgroundColor(this.row1Color);
			wsDSTable.addCell(cell);
		}
		//End CubicReservoir
		
		//Pipe
		ch = new Chunk(this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_PIPE), headerFont);
		cell = new PdfPCell(new Phrase(ch));
		cell.setColspan(tableColumns);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setBackgroundColor(this.headerColor);
		wsDSTable.addCell(cell);
		
		if(!pipes.isEmpty()){
			String[] titles = new String[]{
					this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_PIPE_ROW_NAME),
					this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_PIPE_ROW_DIAMETER),
					this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_PIPE_ROW_LENGTH),
					this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_PIPE_ROW_VOLUME),
					this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_PIPE_ROW_COUNT),
					this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_PIPE_ROW_COMBINEDVOLUME),
					this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_PIPE_ROW_REQUIREDCONCENTRATION)
			};
			for(int i=0;i<titles.length;i++){
				ch = new Chunk(titles[i], headerFont);
				cell = new PdfPCell(new Phrase(ch));
				if(i==1)
					cell.setColspan(2);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setBackgroundColor(this.headerColor);
				wsDSTable.addCell(cell);
			}
			
			this.fillTableWithPipes(wsDSTable, pipes);
		}
		else{
			ch = new Chunk(this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_NO_PIPE), headerFont);
			cell = new PdfPCell(new Phrase(ch));
			cell.setColspan(tableColumns);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setBackgroundColor(this.row1Color);
			wsDSTable.addCell(cell);
		}
		//End Pipe
		
		//ReliefValve
		ch = new Chunk(this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_RELIEFVALVE), headerFont);
		cell = new PdfPCell(new Phrase(ch));
		cell.setColspan(tableColumns);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setBackgroundColor(this.headerColor);
		wsDSTable.addCell(cell);
		
		if(!reliefValves.isEmpty()){
			String[] titles = new String[]{
					this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_RELIEFVALVE_ROW_NAME),
					this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_RELIEFVALVE_ROW_WIDTH),
					this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_RELIEFVALVE_ROW_LENGTH),
					this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_RELIEFVALVE_ROW_HEIGHT),
					this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_RELIEFVALVE_ROW_VOLUME),
					this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_RELIEFVALVE_ROW_COUNT),
					this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_RELIEFVALVE_ROW_COMBINEDVOLUME),
					this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_RELIEFVALVE_ROW_REQUIREDCONCENTRATION)
			};
			for(int i=0;i<titles.length;i++){
				ch = new Chunk(titles[i], headerFont);
				cell = new PdfPCell(new Phrase(ch));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setBackgroundColor(this.headerColor);
				wsDSTable.addCell(cell);
			}
			
			this.fillTableWithReliefValves(wsDSTable, reliefValves);
		}
		else{
			ch = new Chunk(this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_NO_RELIEFVALVE), headerFont);
			cell = new PdfPCell(new Phrase(ch));
			cell.setColspan(tableColumns);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setBackgroundColor(this.row1Color);
			wsDSTable.addCell(cell);
		}
		//End ReliefValve

		
		wsDSTable.setSpacingAfter(10);
		document.add(wsDSTable);
	}
	
    public void createPdf(File file) throws IOException, DocumentException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(file));
        //Set document orientation to landscape
        Rectangle rectangle = new Rectangle(document.getPageSize().getHeight(), document.getPageSize().getWidth());
        document.setPageSize(rectangle);
        document.open();
        
        this.addFrontpage(document);
        List<SubBasin> subBasins = this.ds.getSubBasins();
        for(SubBasin subBasin: subBasins){
        	log.debug("Adding subBasin info: "+subBasin.getName());
        	List<Community> communities = this.ds.getCommunities(subBasin);
        	this.addSubBasinInfo(document, subBasin, communities, true);
        	for(Community community : communities){
        		List<WaterSystem> waterSystems = this.ds.getWaterSystems(community); 
        		this.addCommunityInfo(document, community, waterSystems, false);
        		//if(waterSystems.size()>0) this.addWaterSystemsTable(document, waterSystems);
        		for(WaterSystem waterSystem : waterSystems){
        			this.addWaterSystemInfo(document, waterSystem);
        			this.addWaterSystemTable(document, waterSystem, false);
        			List<MeasuredFlow> measuredFlows = this.getMeasuredFlows(waterSystem);
        			this.addMeasuredFlows(document, measuredFlows);
        			if(!measuredFlows.isEmpty()) this.addMeasuredFlowsTable(document, measuredFlows);
        			List<ChlorineCalculation> chlorineCalculations = this.ds.getChlorineCalculations(waterSystem);
        			this.addChlorineCalculations(document, chlorineCalculations);
        			if(!chlorineCalculations.isEmpty()) this.addChlorineCalculationsTable(document, chlorineCalculations);
        			List<CubicReservoir> cubicReservoirs = this.ds.getCubicReservoirs(waterSystem);
        			List<Pipe> pipes = this.ds.getPipes(waterSystem);
        			List<ReliefValve> reliefValves = this.ds.getReliefValves(waterSystem);
        			boolean infoAvailable = !(cubicReservoirs.isEmpty() && pipes.isEmpty() && reliefValves.isEmpty());
        			this.addWaterSystemDesign(document, infoAvailable);
        			if(infoAvailable) this.addWaterSystemDesignTable(document, cubicReservoirs, pipes, reliefValves);
        		}
        		document.newPage();
        	}
        	
        	document.newPage();
        }
        
        document.close();
    }

	public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, DocumentException {
		Properties properties = ApplicationProperties.getInstance().getProperties();
        Path path  = FileSystems.getDefault().getPath(properties.getProperty(KEYS.RESOURCES_PATH), "report.pdf");
        new FullReport("es", "es").createPdf(path.toFile());
	}
}