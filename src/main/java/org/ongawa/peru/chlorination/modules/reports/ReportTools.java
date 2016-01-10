package org.ongawa.peru.chlorination.modules.reports;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
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
import org.ongawa.peru.chlorination.persistence.elements.ChlorineCalculation;
import org.ongawa.peru.chlorination.persistence.elements.Community;
import org.ongawa.peru.chlorination.persistence.elements.CubicReservoir;
import org.ongawa.peru.chlorination.persistence.elements.CubicReservoirDesinfection;
import org.ongawa.peru.chlorination.persistence.elements.MeasuredFlow;
import org.ongawa.peru.chlorination.persistence.elements.Pipe;
import org.ongawa.peru.chlorination.persistence.elements.PipeDesinfection;
import org.ongawa.peru.chlorination.persistence.elements.ReliefValve;
import org.ongawa.peru.chlorination.persistence.elements.SubBasin;
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
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

/**
 * @author Kiko
 */
public class ReportTools {
	
	private static Logger log;
	static{
		log = LoggerFactory.getLogger(ReportTools.class);
	}
	
	private static final float LEFT_IDENTATION = 10f;
	
	private Properties properties;
	private ResourceBundle messages;
	private DecimalFormat df;
	private SimpleDateFormat sdf;
	
	private Font bodyFont;
	private BaseColor headerColor;
	private BaseColor row1Color;
	private BaseColor row2Color;
	
	public ReportTools(Locale locale) throws IOException{
		if(locale == null)
			throw new NullArgumentException("locale");
		
		this.bodyFont = new Font(FontFamily.HELVETICA, 12);
		this.headerColor = new BaseColor(106, 180, 189);
		this.row1Color = BaseColor.WHITE;
		this.row2Color = new BaseColor(229, 244, 246);

		this.properties = ApplicationProperties.getInstance().getProperties();
		this.messages = ResourceBundle.getBundle(this.properties.getProperty(KEYS.REPORT_BUNDLE_NAME), locale);
		log.info("Loaded i18n for reports: "+locale.getLanguage()+"_"+locale.getCountry());
		this.df = new DecimalFormat("#.####");
		this.sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
	}

	public void addFrontpage(Document document) throws DocumentException, MalformedURLException, IOException{
		Image imageHeader = Image.getInstance(this.properties.getProperty(KEYS.RESOURCES_PATH)+File.separator+"img"+File.separator+"ongawa.png");
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
        
        String date = this.sdf.format(Calendar.getInstance().getTime());
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
	
	public void addMeasuredFlowsTable(Document document, List<MeasuredFlow> measuredFlows) throws DocumentException{
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
		String comments;
		for(MeasuredFlow measuredFlow : measuredFlows){
			Font cellFont = new Font(this.bodyFont.getFamily(), 10);

			String[] fields = new String[]{
					measuredFlow.getMeasuringPoint().getWaterSpring().getName(),
					measuredFlow.getMeasuringPoint().getName(),
					this.sdf.format(measuredFlow.getDate()),
					this.df.format(measuredFlow.getFlow())+" "+this.messages.getString(KEYS.REPORT_MEASUREDFLOW_ROW_UOM_LPS)
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
	
	public void addChlorineCalculationsTable(Document document, List<ChlorineCalculation> chlorineCalculations) throws DocumentException{
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
		
		for(ChlorineCalculation chlorineCalculation : chlorineCalculations){
			Font cellFont = new Font(this.bodyFont.getFamily(), 10);
			String[] fields = new String[]{
					this.sdf.format(chlorineCalculation.getDate()),
					String.valueOf(chlorineCalculation.getPopulation()),
					this.df.format(chlorineCalculation.getTankVolume())+" "+this.messages.getString(KEYS.REPORT_CHLORINECALCULATION_ROW_UOM_L),
					this.df.format(chlorineCalculation.getTankUsefulVolume())+" "+this.messages.getString(KEYS.REPORT_CHLORINECALCULATION_ROW_UOM_L),
					this.df.format(chlorineCalculation.getEndowment())+" "+this.messages.getString(KEYS.REPORT_CHLORINECALCULATION_ROW_UOM_LPPPD),
					this.df.format(chlorineCalculation.getChlorinePureness())+"%",
					this.df.format(chlorineCalculation.getInputFlow())+" "+this.messages.getString(KEYS.REPORT_CHLORINECALCULATION_ROW_UOM_LPS),
					this.df.format(chlorineCalculation.getReloadTime())+" "+this.messages.getString(KEYS.REPORT_CHLORINECALCULATION_ROW_UOM_DAYS),
					this.df.format(chlorineCalculation.getDemandCLR())+" "+this.messages.getString(KEYS.REPORT_CHLORINECALCULATION_ROW_UOM_MGPL),
					this.df.format(chlorineCalculation.getDemandActiveChlorine())+" "+this.messages.getString(KEYS.REPORT_CHLORINECALCULATION_ROW_UOM_MGPL),
					this.df.format(chlorineCalculation.getDemandCommonProduct())+" "+this.messages.getString(KEYS.REPORT_CHLORINECALCULATION_ROW_UOM_MGPL)
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
					this.df.format(cubicReservoir.getWidth())+" "+this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_CUBICRESERVOIR_UOM_METERS),
					this.df.format(cubicReservoir.getLength())+" "+this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_CUBICRESERVOIR_UOM_METERS),
					this.df.format(cubicReservoir.getHeight())+" "+this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_CUBICRESERVOIR_UOM_METERS),
					this.df.format(cubicReservoir.getVolume())+" "+this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_CUBICRESERVOIR_UOM_CUBICMETERS),
					String.valueOf(cubicReservoir.getCount()),
					this.df.format(cubicReservoir.getCombinedVolume())+" "+this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_CUBICRESERVOIR_UOM_CUBICMETERS)
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
					this.df.format(pipe.getDiameter())+" "+this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_PIPE_UOM_METERS),
					this.df.format(pipe.getLength())+" "+this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_PIPE_UOM_METERS),
					this.df.format(pipe.getVolume())+" "+this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_PIPE_UOM_CUBICMETERS),
					String.valueOf(pipe.getCount()),
					this.df.format(pipe.getCombinedVolume())+" "+this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_PIPE_UOM_CUBICMETERS)
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
					this.df.format(reliefValve.getWidth())+" "+this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_RELIEFVALVE_UOM_METERS),
					this.df.format(reliefValve.getLength())+" "+this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_RELIEFVALVE_UOM_METERS),
					this.df.format(reliefValve.getHeight())+" "+this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_RELIEFVALVE_UOM_METERS),
					this.df.format(reliefValve.getVolume())+" "+this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_RELIEFVALVE_UOM_CUBICMETERS),
					String.valueOf(reliefValve.getCount()),
					this.df.format(reliefValve.getCombinedVolume())+" "+this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_RELIEFVALVE_UOM_CUBICMETERS)
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
	
	public void addWaterSystemDesignTable(Document document, List<CubicReservoir> cubicReservoirs, List<Pipe> pipes, List<ReliefValve> reliefValves) throws DocumentException{
		Font headerFont = new Font(this.bodyFont.getFamily(), 10);
		int tableColumns = 7;
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
					this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_CUBICRESERVOIR_ROW_COMBINEDVOLUME)
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
					this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_PIPE_ROW_COMBINEDVOLUME)
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
					this.messages.getString(KEYS.REPORT_WATERSYSTEM_DESIGN_RELIEFVALVE_ROW_COMBINEDVOLUME)
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
	
	public void addDesinfectionInformation(Document document, WaterSystem waterSystem){
		Font headerFont = new Font(this.bodyFont.getFamily(), 10);
		int tableColumns = 9;
		PdfPTable dfTable = new PdfPTable(tableColumns);
		dfTable.setWidthPercentage(100);
		dfTable.getDefaultCell().setUseAscender(true);
		dfTable.getDefaultCell().setUseDescender(true);
		
		double partialAmount = 0, totalAmount = 0;
		Chunk ch; PdfPCell cell;
		ch = new Chunk(this.messages.getString(KEYS.REPORT_DESINFECTION_CUBICRESERVOIR_TABLE_NAME), headerFont);
		cell = new PdfPCell(new Phrase(ch));
		cell.setColspan(tableColumns);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setBackgroundColor(this.headerColor);
		dfTable.addCell(cell);
		List<String> values = new ArrayList<String>();
		
		
		String[] titles = new String[]{
				this.messages.getString(KEYS.REPORT_DESINFECTION_CUBICRESERVOIR_ROW_NAME),
				this.messages.getString(KEYS.REPORT_DESINFECTION_CUBICRESERVOIR_ROW_COUNT),
				this.messages.getString(KEYS.REPORT_DESINFECTION_CUBICRESERVOIR_ROW_WATERHEIGHT),
				this.messages.getString(KEYS.REPORT_DESINFECTION_CUBICRESERVOIR_ROW_VOLUME),
				this.messages.getString(KEYS.REPORT_DESINFECTION_CUBICRESERVOIR_ROW_CHLORINECONCENTRATION),
				this.messages.getString(KEYS.REPORT_DESINFECTION_CUBICRESERVOIR_ROW_DEMANDACTIVECHLORINE),
				this.messages.getString(KEYS.REPORT_DESINFECTION_CUBICRESERVOIR_ROW_DEMAND70CHLORINE),
				this.messages.getString(KEYS.REPORT_DESINFECTION_CUBICRESERVOIR_ROW_DEMANDSPOONS),
				this.messages.getString(KEYS.REPORT_DESINFECTION_CUBICRESERVOIR_ROW_RETENTIONTIME)
		};
		for(int i=0;i<titles.length;i++){
			ch = new Chunk(titles[i], headerFont);
			cell = new PdfPCell(new Phrase(ch));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setBackgroundColor(this.headerColor);
			dfTable.addCell(cell);
		}
	}
}