package org.ongawa.peru.chlorination.modules.reports;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.NullArgumentException;
import org.ongawa.peru.chlorination.KEYS;
import org.ongawa.peru.chlorination.persistence.elements.Community;
import org.ongawa.peru.chlorination.persistence.elements.SubBasin;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;

public class SubBasinSection extends ReportSection {
	
	private boolean addCommunitiesList;
	private SubBasin subBasin;

	public SubBasinSection(SubBasin subBasin, Locale locale) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
		super(locale);
		if(subBasin == null)
			throw new NullArgumentException("subBasin");
		this.subBasin = subBasin;
		this.addCommunitiesList = false;
	}

	@Override
	public boolean addSectionInfo(Document document) throws DocumentException {
		Font subBasinTitleFont = new Font(FontFamily.HELVETICA, 19);
		subBasinTitleFont.setStyle(Font.BOLD);
        Chunk chSubBasinTitle = new Chunk(this.messages.getString(KEYS.REPORT_REFERRING_SUBBASIN).replaceAll("&subbasinName", subBasin.getName()), subBasinTitleFont);
        Paragraph phSubBasinTitle = new Paragraph();
        phSubBasinTitle.setSpacingAfter(20);
        phSubBasinTitle.add(new Phrase(chSubBasinTitle));
        document.add(phSubBasinTitle);
        
        List<Community> communities = this.ds.getCommunities(subBasin);
    	if(communities == null || communities.isEmpty()){
    		Paragraph phNoComs = new Paragraph();
        	phNoComs.setFirstLineIndent(LEFT_IDENTATION);
            Chunk chNoComs = new Chunk(this.messages.getString(KEYS.REPORT_SUBBASIN_WITHOUT_COMMUNITIES).replaceFirst("&subbasinName", subBasin.getName()), this.bodyFont);
            phNoComs.add(chNoComs);
            document.add(phNoComs);
    	}
    	else if(this.addCommunitiesList && !communities.isEmpty()){
    		Paragraph phIntroducingComs = new Paragraph();
	        phIntroducingComs.setFirstLineIndent(LEFT_IDENTATION);
	        Chunk chIntroducingComs = new Chunk(this.messages.getString(KEYS.REPORT_INDTRODUCING_COMMUNITIES).replaceFirst("&subbasinName", subBasin.getName()), this.bodyFont);
	        phIntroducingComs.add(chIntroducingComs);
	        document.add(phIntroducingComs);
	        
	        com.itextpdf.text.List listCom = new com.itextpdf.text.List();
	        listCom.setListSymbol("\u2022");
	        listCom.setIndentationLeft(LEFT_IDENTATION*3);
	        Chunk ch;
		    for(Community community : communities){
		    	ch = new Chunk(community.getName());
		    	ch.setFont(this.bodyFont);
		    	listCom.add(new ListItem(ch));
		    }
		    Paragraph phListCom = new Paragraph();
		    phListCom.setSpacingAfter(DEFAULT_SPACING_AFTER);
		    phListCom.add(listCom);
		    
		    document.add(phListCom);
        }
        
        return true;
	}
	
	public SubBasin getSubBasin(){
		return this.subBasin;
	}
	
	public void setShowCommunitiesList(boolean condition){
		this.addCommunitiesList = condition;
	}
	
	public boolean getShowCommunitiesList(){
		return this.addCommunitiesList;
	}

}
