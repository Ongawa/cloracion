package org.ongawa.peru.chlorination.modules.reports;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

import org.ongawa.peru.chlorination.ApplicationProperties;
import org.ongawa.peru.chlorination.KEYS;
import org.ongawa.peru.chlorination.persistence.DataSourceFactory;
import org.ongawa.peru.chlorination.persistence.IDataSource;
import org.ongawa.peru.chlorination.persistence.elements.Community;
import org.ongawa.peru.chlorination.persistence.elements.SubBasin;

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

	public static void main(String[] args) throws IOException, DocumentException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Properties properties = ApplicationProperties.getInstance().getProperties();
        Path path  = FileSystems.getDefault().getPath(properties.getProperty(KEYS.RESOURCES_PATH), "report.pdf");
        new Report().createPdf(path.toFile());
    }
 
    public void createPdf(File file) throws IOException, DocumentException, ClassNotFoundException, InstantiationException, IllegalAccessException {
    	Properties properties = ApplicationProperties.getInstance().getProperties();
    	IDataSource ds = DataSourceFactory.getInstance().getDefaultDataSource();
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();
 
        SubBasin subBasin = ds.getSubBasin(1);
        
        Font fontHeader = new Font(FontFamily.HELVETICA, 22);
        fontHeader.setStyle(Font.BOLD);
        Paragraph phHeader = new Paragraph();
        
        Image imageHeader = Image.getInstance(properties.getProperty(KEYS.RESOURCES_PATH)+File.separator+"ongawa.png");
        imageHeader.scalePercent(30);
        phHeader.add(imageHeader);
        
        Chunk chHeader = new Chunk("Informe sobre el sistema de cloración");
        chHeader.setFont(fontHeader);
        phHeader.add(chHeader);
        document.add(phHeader);
        
        Font subHeaderFont = new Font(FontFamily.HELVETICA, 18);
        Chunk chSubHeader = new Chunk("Datos referentes a la subcuenca: "+subBasin.getName());
        chSubHeader.setFont(subHeaderFont);
        document.add(new Paragraph(new Phrase(chSubHeader)));
        
        Font bodyFont = new Font(FontFamily.HELVETICA, 14);
        List<Community> communities = ds.getCommunities(subBasin);
        com.itextpdf.text.List listCom = new com.itextpdf.text.List();
        listCom.setListSymbol("\u2022");
        
        Chunk ch;
        for(int i=0;i<1;i++)
	        for(Community community : communities){
	        	ch = new Chunk(community.getName());
	        	ch.setFont(bodyFont);
	        	listCom.add(new ListItem(ch));
	        }
        document.add(listCom);

        document.close();
    }
}