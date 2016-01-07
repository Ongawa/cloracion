package org.ongawa.peru.chlorination.modules.reports;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * 
 * @author kiko
 *
 */
public class ReportFactory {
	
	private static ReportFactory instance;
	private static Logger log;
	
	static{
		instance = null;
		log = LoggerFactory.getLogger(ReportFactory.class);
	}
	public static ReportFactory getInstance(){
		if(instance == null)
			instance = new ReportFactory();
		
		return instance;
	}
	
	private ReportFactory(){

	}
	
	private List<MeasuredFlow> getMeasuredFlows(WaterSystem waterSystem, IDataSource ds){
		List<MeasuredFlow> measuredFlows = new ArrayList<MeasuredFlow>();
		List<WaterSpring> waterSprings = ds.getWaterSpringsInWaterSystem(waterSystem);
		for(WaterSpring waterSpring : waterSprings){
			List<MeasuringPoint> measuringPoints = ds.getMeasuringPoints(waterSystem, waterSpring);
			for(MeasuringPoint measuringPoint : measuringPoints)
				measuredFlows.addAll(ds.getMeasuredFlows(measuringPoint));
		}
		
		return measuredFlows;
	}
	
	public void createFullReport(File destFile, Locale locale) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, DocumentException{
		ReportTools tools = new ReportTools(locale);
		IDataSource ds = DataSourceFactory.getInstance().getDefaultDataSource();
		
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(destFile));
        //Set document orientation to landscape
        Rectangle rectangle = new Rectangle(document.getPageSize().getHeight(), document.getPageSize().getWidth());
        document.setPageSize(rectangle);
        document.open();
        
        tools.addFrontpage(document);
        List<SubBasin> subBasins = ds.getSubBasins();
        for(SubBasin subBasin: subBasins){
        	log.debug("Adding subBasin info: "+subBasin.getName());
        	List<Community> communities = ds.getCommunities(subBasin);
        	tools.addSubBasinInfo(document, subBasin, communities, true);
        	for(Community community : communities){
        		List<WaterSystem> waterSystems = ds.getWaterSystems(community); 
        		tools.addCommunityInfo(document, community, waterSystems, false);
        		//if(waterSystems.size()>0) this.addWaterSystemsTable(document, waterSystems);
        		for(WaterSystem waterSystem : waterSystems){
        			tools.addWaterSystemInfo(document, waterSystem);
        			tools.addWaterSystemTable(document, waterSystem, false);
        			List<MeasuredFlow> measuredFlows = this.getMeasuredFlows(waterSystem, ds);
        			tools.addMeasuredFlows(document, measuredFlows);
        			if(!measuredFlows.isEmpty()) tools.addMeasuredFlowsTable(document, measuredFlows);
        			List<ChlorineCalculation> chlorineCalculations = ds.getChlorineCalculations(waterSystem);
        			tools.addChlorineCalculations(document, chlorineCalculations);
        			if(!chlorineCalculations.isEmpty()) tools.addChlorineCalculationsTable(document, chlorineCalculations);
        			List<CubicReservoir> cubicReservoirs = ds.getCubicReservoirs(waterSystem);
        			List<Pipe> pipes = ds.getPipes(waterSystem);
        			List<ReliefValve> reliefValves = ds.getReliefValves(waterSystem);
        			boolean infoAvailable = !(cubicReservoirs.isEmpty() && pipes.isEmpty() && reliefValves.isEmpty());
        			tools.addWaterSystemDesign(document, infoAvailable);
        			if(infoAvailable) tools.addWaterSystemDesignTable(document, cubicReservoirs, pipes, reliefValves);
        		}
        		document.newPage();
        	}
        }        
        document.close();
        log.info("Created Full report ("+locale.toString()+"): "+destFile);
	}
}
