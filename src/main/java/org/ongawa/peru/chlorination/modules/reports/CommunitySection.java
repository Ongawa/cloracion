package org.ongawa.peru.chlorination.modules.reports;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.NullArgumentException;
import org.ongawa.peru.chlorination.KEYS;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Subbasin;
import org.ongawa.peru.chlorination.persistence.elements.Community;
import org.ongawa.peru.chlorination.persistence.elements.WaterSystem;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;

/**
 * 
 * @author kiko
 *
 */
public class CommunitySection extends ReportSection {
	
	private boolean addWaterSystemList;
	private Community community;
	
	public CommunitySection(Community community, Locale locale) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
		super(locale);
		if(community == null)
			throw new NullArgumentException("community");
		this.community = community;
		this.addWaterSystemList = false;
	}

	@Override
	public boolean addSectionInfo(Document document) throws DocumentException {
		if(document == null)
			throw new NullArgumentException("document");
		
		Font communityTitleFont = new Font(FontFamily.HELVETICA, 15);
		communityTitleFont.setStyle(Font.UNDERLINE|Font.ITALIC);
		Chunk chCommunityTitle = new Chunk(this.messages.getString(KEYS.REPORT_REFERRING_COMMUNITY).replaceAll("&communityName", community.getName()), communityTitleFont);
		Paragraph phCommunityTitle = new Paragraph();
		phCommunityTitle.setSpacingAfter(DEFAULT_SPACING_AFTER);
		phCommunityTitle.add(new Phrase(chCommunityTitle));
		document.add(phCommunityTitle);
		
		List<WaterSystem> waterSystems = this.ds.getWaterSystems(community);		
		if(waterSystems == null || waterSystems.isEmpty()){
			Paragraph phNoWS = new Paragraph();
			phNoWS.setFirstLineIndent(LEFT_IDENTATION);
	        Chunk chNoWS = new Chunk(this.messages.getString(KEYS.REPORT_COMMUNITY_WITHOUT_WATERSYSTEM).replaceFirst("&communityName", community.getName()), this.bodyFont);
	        phNoWS.add(chNoWS);
	        phNoWS.setSpacingAfter(10);
	        document.add(phNoWS);
		}
		else if(this.addWaterSystemList && !waterSystems.isEmpty()){
			Paragraph phIntroducingWS = new Paragraph();
			phIntroducingWS.setFirstLineIndent(LEFT_IDENTATION);
	        Chunk chIntroducingWS = new Chunk(this.messages.getString(KEYS.REPORT_INTRODUCING_WATERSYSTEMS).replaceFirst("&communityName", community.getName()), this.bodyFont);
	        phIntroducingWS.add(chIntroducingWS);
	        phIntroducingWS.setSpacingAfter(DEFAULT_SPACING_AFTER);
	        document.add(phIntroducingWS);
	        com.itextpdf.text.List listWS = new com.itextpdf.text.List();
	        listWS.setListSymbol("\u2022");
	        listWS.setIndentationLeft(LEFT_IDENTATION);
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
		
		return true;
	}
	
	public boolean getAddWaterSystemList(){
		return this.addWaterSystemList;
	}
	
	public void setAddWaterSystemList(boolean condition){
		this.addWaterSystemList = condition;
	}
	
	public Community getCommunity(){
		return this.community;
	}
}
