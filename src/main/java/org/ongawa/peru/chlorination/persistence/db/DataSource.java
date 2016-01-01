package org.ongawa.peru.chlorination.persistence.db;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record2;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.ongawa.peru.chlorination.ApplicationProperties;
import org.ongawa.peru.chlorination.KEYS;
import org.ongawa.peru.chlorination.persistence.IDataSource;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Chlorinecalculation;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Cubicreservoir;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Measuredflow;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Measuringpoint;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Reliefvalve;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Subbasin;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Waterspring;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Watersystem;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.WatersystemHasWaterspring;
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

/**
 * @author Kiko
 */
public class DataSource implements IDataSource {

	private static Logger log;
	static{
		log = LoggerFactory.getLogger(DataSource.class);
	}
		
	private Connection connection;
	
	public DataSource(){
	}
	
	private DSLContext prepareDSLContext(Connection connection){
		return DSL.using(connection, SQLDialect.H2);
	}
	
	private void closeConnection(){
		try {
			this.connection.close();
			log.debug("Closed connection: "+connection);
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
	}
	
	private int getMaxValue(Table<?> table, Field<?> field){
		int value = -1;
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext select = this.prepareDSLContext(this.connection);
			Result<?> result = select.select(DSL.max(field).as("MAX")).from(table).limit(1).fetch();
			
			for(Record record : result){
				value = (int)record.getValue("MAX");
				break;
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return value;
	}
	
	private SubBasin readSubBasin(Record record){
		return new SubBasin(record.getValue(Subbasin.SUBBASIN.IDSUBBASIN), record.getValue(Subbasin.SUBBASIN.NAME));
	}
	
	private Community readCommunity(Record record, SubBasin subBasin){
		return new Community(record.getValue(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Community.COMMUNITY.IDCOMMUNITY), 
				record.getValue(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Community.COMMUNITY.NAME),
				subBasin);
	}
	
	private WaterSystem readWaterSystem(Record record, Community community){
		WaterSystem waterSystem = new WaterSystem(record.getValue(Watersystem.WATERSYSTEM.IDWATERSYSTEM), record.getValue(Watersystem.WATERSYSTEM.NAME), community);
		waterSystem.setFamiliesNum(record.getValue(Watersystem.WATERSYSTEM.FAMILIESNUM));
		
		Integer integer; Double dodo;
		if((integer=record.getValue(Watersystem.WATERSYSTEM.POPULATION))!=null) waterSystem.setPopulation(integer);
		else waterSystem.setPopulation(waterSystem.getFamiliesNum()*5);
		if((integer=record.getValue(Watersystem.WATERSYSTEM.POPULATIONFORECAST))!=null) waterSystem.setPopulationForecast(integer);
		if((dodo=record.getValue(Watersystem.WATERSYSTEM.GROWINGINDEX))!=null) waterSystem.setGrowingIndex(dodo);
		if((integer=record.getValue(Watersystem.WATERSYSTEM.JASSNUM))!=null) waterSystem.setJASSNum(integer);
		if((dodo=record.getValue(Watersystem.WATERSYSTEM.FUTURENEEDEDFLOW))!=null) waterSystem.setFutureNeededFlow(dodo);
		if((dodo=record.getValue(Watersystem.WATERSYSTEM.RESERVOIRVOLUME))!=null) waterSystem.setReservoirVolume(dodo);
		if((integer=record.getValue(Watersystem.WATERSYSTEM.SYSTEMELEVATION))!=null) waterSystem.setSystemElevation(integer);
		
		return waterSystem;
	}
	
	private WaterSpring readWaterSpring(Record record){
		return new WaterSpring(record.getValue(Waterspring.WATERSPRING.IDWATERSPRING), record.getValue(Waterspring.WATERSPRING.NAME));
	}
	
	private MeasuringPoint readMeasuringPoint(Record record, WaterSystem waterSystem, WaterSpring waterSpring){
		return new MeasuringPoint(record.getValue(Measuringpoint.MEASURINGPOINT.IDMEASURINGPOINT), record.getValue(Measuringpoint.MEASURINGPOINT.NAME), waterSystem, waterSpring);
	}
	
	private MeasuredFlow readMeasuredFlow(Record record, MeasuringPoint measuringPoint){
		MeasuredFlow measuredFlow = new MeasuredFlow(record.getValue(Measuredflow.MEASUREDFLOW.DATE), record.getValue(Measuredflow.MEASUREDFLOW.FLOW), measuringPoint);
		String comments;
		if((comments=record.getValue(Measuredflow.MEASUREDFLOW.COMMENTS))!=null) measuredFlow.setComments(comments);
		
		return measuredFlow;
	}
	
	private ChlorineCalculation readChlorineCalculation(Record record, WaterSystem waterSystem){
		ChlorineCalculation chlorineCalculation = new ChlorineCalculation(
				record.getValue(Chlorinecalculation.CHLORINECALCULATION.DATE),
				record.getValue(Chlorinecalculation.CHLORINECALCULATION.DEMANDCLR),
				record.getValue(Chlorinecalculation.CHLORINECALCULATION.DEMANDACTIVECHLORINE),
				record.getValue(Chlorinecalculation.CHLORINECALCULATION.DEMANDCOMMONPRODUCT),
				waterSystem);
		
		Integer integer; Double dodo;
		if((integer=record.getValue(Chlorinecalculation.CHLORINECALCULATION.POPULATION))!=null) chlorineCalculation.setPopulation(integer);
		if((dodo=record.getValue(Chlorinecalculation.CHLORINECALCULATION.TANKVOLUME))!=null) chlorineCalculation.setTankVolume(dodo);
		if((dodo=record.getValue(Chlorinecalculation.CHLORINECALCULATION.TANKUSEFULVOLUME))!=null) chlorineCalculation.setTankUsefulVolume(dodo);
		if((dodo=record.getValue(Chlorinecalculation.CHLORINECALCULATION.ENDOWMENT))!=null) chlorineCalculation.setEndowment(dodo);
		if((dodo=record.getValue(Chlorinecalculation.CHLORINECALCULATION.CHLORINEPURENESS))!=null) chlorineCalculation.setChlorinePureness(dodo);
		if((dodo=record.getValue(Chlorinecalculation.CHLORINECALCULATION.INPUTFLOW))!=null) chlorineCalculation.setInputFlow(dodo);
		if((integer=record.getValue(Chlorinecalculation.CHLORINECALCULATION.RELOADTIME))!=null) chlorineCalculation.setReloadTime(integer);
		
		return chlorineCalculation;
	}
	
	private CubicReservoir readCubicReservoir(Record record, WaterSystem waterSystem){
		CubicReservoir cubicReservoir = new CubicReservoir(
				record.getValue(Cubicreservoir.CUBICRESERVOIR.IDCUBICRESERVOIR),
				record.getValue(Cubicreservoir.CUBICRESERVOIR.WIDTH),
				record.getValue(Cubicreservoir.CUBICRESERVOIR.LENGTH),
				record.getValue(Cubicreservoir.CUBICRESERVOIR.HEIGHT),
				waterSystem);
		
		String string; Integer integer; Double dodo;
		if((string=record.getValue(Cubicreservoir.CUBICRESERVOIR.NAME))!=null) cubicReservoir.setName(string);
		if((integer=record.getValue(Cubicreservoir.CUBICRESERVOIR.COUNT))!=null) cubicReservoir.setCount(integer);
		if((dodo=record.getValue(Cubicreservoir.CUBICRESERVOIR.REQUIREDCONCENTRATION))!=null) cubicReservoir.setRequiredConcentration(dodo);
		
		return cubicReservoir;
	}
	
	private Pipe readPipe(Record record, WaterSystem waterSystem){
		Pipe pipe = new Pipe(
				record.getValue(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.IDPIPE),
				record.getValue(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.DIAMETER),
				record.getValue(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.LENGTH),
				waterSystem);
		
		String string; Integer integer; Double dodo;
		if((string=record.getValue(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.NAME))!=null) pipe.setName(string);
		if((integer=record.getValue(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.COUNT))!=null) pipe.setCount(integer);
		if((dodo=record.getValue(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.REQUIREDCONCENTRATION))!=null) pipe.setRequiredConcentration(dodo);
		
		return pipe;
	}
	
	private ReliefValve readReliefValve(Record record, WaterSystem waterSystem){
		ReliefValve reliefValve = new ReliefValve(
				record.getValue(Reliefvalve.RELIEFVALVE.IDRELIEFVALVE),
				record.getValue(Reliefvalve.RELIEFVALVE.WIDTH),
				record.getValue(Reliefvalve.RELIEFVALVE.LENGTH),
				record.getValue(Reliefvalve.RELIEFVALVE.HEIGHT),
				waterSystem);
		
		Integer integer; String string; Double dodo;
		if((string=record.getValue(Reliefvalve.RELIEFVALVE.NAME))!=null) reliefValve.setName(string);
		if((integer=record.getValue(Reliefvalve.RELIEFVALVE.COUNT))!=null) reliefValve.setCount(integer);
		if((dodo=record.getValue(Reliefvalve.RELIEFVALVE.REQUIREDCONCENTRATION))!=null) reliefValve.setRequiredConcentration(dodo);
		
		return reliefValve;
	}
	
	@Override
	public SubBasin addSubBasin(SubBasin subBasin) {
		if(subBasin == null)
			throw new NullArgumentException("subBasin");
		SubBasin newSubBasin = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext insert = this.prepareDSLContext(this.connection);
			int result = insert.insertInto(Subbasin.SUBBASIN, Subbasin.SUBBASIN.NAME)
					.values(subBasin.getName())
					.execute();
			this.closeConnection();
			
			if(result>0)
				newSubBasin = this.getSubBasin(subBasin.getName());
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return newSubBasin;
	}
	
	/* (non-Javadoc)
	 * @see org.ongawa.peru.chlorination.persistence.db.IDataSource#getSubBasins()
	 */
	@Override
	public List<SubBasin> getSubBasins(){
		List<SubBasin> subBasins = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			subBasins = new ArrayList<SubBasin>();
			
			DSLContext select = this.prepareDSLContext(this.connection);
			Result<Record> result = select.select().from(Subbasin.SUBBASIN).fetch();
			SubBasin subBasin;
			
			for(Record record : result){
				subBasin = this.readSubBasin(record);
				subBasins.add(subBasin);
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}	
		
		return subBasins;
	}
	
	/* (non-Javadoc)
	 * @see org.ongawa.peru.chlorination.persistence.db.IDataSource#getSubBasin(int)
	 */
	@Override
	public SubBasin getSubBasin(int idSubBasin){
		SubBasin subBasin = null;
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext select = this.prepareDSLContext(this.connection);
			Result<Record> result = select.select().from(Subbasin.SUBBASIN).where(Subbasin.SUBBASIN.IDSUBBASIN.eq(idSubBasin)).limit(1).fetch();
			for(Record record : result){
				subBasin = this.readSubBasin(record);
				break;
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return subBasin;
	}
	
	/* (non-Javadoc)
	 * @see org.ongawa.peru.chlorination.persistence.db.IDataSource#getSubBasin(java.lang.String)
	 */
	@Override
	public SubBasin getSubBasin(String name){
		if(name == null)
			throw new NullArgumentException("name");
		SubBasin subBasin = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext select = this.prepareDSLContext(this.connection);
			Result<Record> result = select.select().from(Subbasin.SUBBASIN).where(Subbasin.SUBBASIN.NAME.eq(name)).limit(1).fetch();
			for(Record record : result){
				subBasin = this.readSubBasin(record);
				break;
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return subBasin;
	}
	
	@Override
	public boolean editSubBasin(SubBasin subBasin) {
		if(subBasin == null)
			throw new NullArgumentException("subBasin");
		int result = 0;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext update = this.prepareDSLContext(this.connection);
			result = update.update(Subbasin.SUBBASIN)
					.set(Subbasin.SUBBASIN.NAME, subBasin.getName())
					.where(Subbasin.SUBBASIN.IDSUBBASIN.eq(subBasin.getSubBasinId()))
					.execute();
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return result>0;
	}
	
	@Override
	public boolean removeSubBasin(SubBasin subBasin) {
		if(subBasin == null)
			throw new NullArgumentException("subBasin");
		
		int result = 0;
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext delete = this.prepareDSLContext(this.connection);
			result = delete.delete(Subbasin.SUBBASIN).where(Subbasin.SUBBASIN.IDSUBBASIN.eq(subBasin.getSubBasinId())).execute();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return result>0;
	}
	
	@Override
	public Community addCommunity(Community community) {
		if(community == null)
			throw new NullArgumentException("community");
		Community newCommunity = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext insert = this.prepareDSLContext(this.connection);
			int result = insert
					.insertInto(
							org.ongawa.peru.chlorination.persistence.db.jooq.tables.Community.COMMUNITY,
							org.ongawa.peru.chlorination.persistence.db.jooq.tables.Community.COMMUNITY.NAME,
							org.ongawa.peru.chlorination.persistence.db.jooq.tables.Community.COMMUNITY.SUBBASIN_IDSUBBASIN)
					.values(
							community.getName(),
							community.getSubBasin().getSubBasinId())
					.execute();
			this.closeConnection();
			
			if(result>0)
				newCommunity = this.getCommunity(community.getSubBasin(), community.getName());
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return newCommunity;
	}

	/* (non-Javadoc)
	 * @see org.ongawa.peru.chlorination.persistence.db.IDataSource#getCommunities(org.ongawa.peru.chlorination.persistence.elements.SubBasin)
	 */
	@Override
	public List<Community> getCommunities(SubBasin subBasin){
		if(subBasin == null)
			throw new NullArgumentException("subBasin");
		List<Community> communities = null;
				
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			communities = new ArrayList<Community>();
			
			DSLContext select = this.prepareDSLContext(this.connection);
			Result<Record> result = select.select()
					.from(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Community.COMMUNITY)
					.where(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Community.COMMUNITY.SUBBASIN_IDSUBBASIN
							.eq(subBasin.getSubBasinId()))
					.fetch();
			
			Community community;
			for(Record record : result){
				community = this.readCommunity(record, subBasin);
				communities.add(community);
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return communities;
	}
	
	/* (non-Javadoc)
	 * @see org.ongawa.peru.chlorination.persistence.db.IDataSource#getCommunity(org.ongawa.peru.chlorination.persistence.elements.SubBasin, int)
	 */
	@Override
	public Community getCommunity(SubBasin subBasin, int idCommunity){
		if(subBasin==null)
			throw new NullArgumentException("subBasin");
		Community community = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext select = this.prepareDSLContext(this.connection);
			Result<Record> result = select.select().from(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Community.COMMUNITY)
					.where(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Community.COMMUNITY.IDCOMMUNITY.eq(idCommunity))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Community.COMMUNITY.SUBBASIN_IDSUBBASIN.eq(subBasin.getSubBasinId()))
					.limit(1)
					.fetch();
			
			for(Record record : result){
				community = this.readCommunity(record, subBasin);
				break;
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		return community;
	}
	
	/* (non-Javadoc)
	 * @see org.ongawa.peru.chlorination.persistence.db.IDataSource#getCommunity(org.ongawa.peru.chlorination.persistence.elements.SubBasin, java.lang.String)
	 */
	@Override
	public Community getCommunity(SubBasin subBasin, String name){
		if(subBasin==null)
			throw new NullArgumentException("subBasin");
		if(name==null)
			throw new NullArgumentException("name");
		Community community = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext select = this.prepareDSLContext(this.connection);
			Result<Record> result = select.select().from(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Community.COMMUNITY)
					.where(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Community.COMMUNITY.NAME.eq(name))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Community.COMMUNITY.SUBBASIN_IDSUBBASIN.eq(subBasin.getSubBasinId()))
					.limit(1)
					.fetch();
			
			for(Record record : result){
				community = this.readCommunity(record, subBasin);
				break;
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		return community;
	}
	
	@Override
	public boolean editCommunity(Community community) {
		if(community == null)
			throw new NullArgumentException("community");
		int result = 0;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext update = this.prepareDSLContext(this.connection);
			result = update.update(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Community.COMMUNITY)
					.set(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Community.COMMUNITY.NAME, community.getName())
					.where(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Community.COMMUNITY.IDCOMMUNITY.eq(community.getCommunityId()))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Community.COMMUNITY.SUBBASIN_IDSUBBASIN.eq(community.getSubBasin().getSubBasinId()))
					.execute();
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return result>0;
	}
	
	@Override
	public boolean removeCommunity(Community community) {
		if(community == null)
			throw new NullArgumentException("community");
		
		int result = 0;
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext delete = this.prepareDSLContext(this.connection);
			result = delete
					.delete(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Community.COMMUNITY)
					.where(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Community.COMMUNITY.IDCOMMUNITY.eq(community.getCommunityId()))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Community.COMMUNITY.SUBBASIN_IDSUBBASIN.eq(community.getSubBasin().getSubBasinId()))
					.execute();
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return result>0;
	}
	
	@Override
	public WaterSystem addWaterSystem(WaterSystem waterSystem) {
		if(waterSystem == null)
			throw new NullArgumentException("waterSystem");
		WaterSystem newWaterSystem = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext insert = this.prepareDSLContext(this.connection);
			int result = insert
					.insertInto(
							Watersystem.WATERSYSTEM,
							Watersystem.WATERSYSTEM.COMMUNITY_IDCOMMUNITY,
							Watersystem.WATERSYSTEM.COMMUNITY_SUBBASIN_IDSUBBASIN,
							Watersystem.WATERSYSTEM.NAME,
							Watersystem.WATERSYSTEM.FAMILIESNUM,
							Watersystem.WATERSYSTEM.POPULATION,
							Watersystem.WATERSYSTEM.POPULATIONFORECAST,
							Watersystem.WATERSYSTEM.GROWINGINDEX,
							Watersystem.WATERSYSTEM.JASSNUM,
							Watersystem.WATERSYSTEM.FUTURENEEDEDFLOW,
							Watersystem.WATERSYSTEM.RESERVOIRVOLUME,
							Watersystem.WATERSYSTEM.SYSTEMELEVATION)
					.values(waterSystem.getCommunity().getCommunityId(),
							waterSystem.getCommunity().getSubBasin().getSubBasinId(),
							waterSystem.getName(),
							waterSystem.getFamiliesNum(),
							waterSystem.getPopulation(),
							waterSystem.getPopulationForecast(),
							waterSystem.getGrowingIndex(),
							waterSystem.getJASSNum(),
							waterSystem.getFutureNeededFlow(),
							waterSystem.getReservoirVolume(),
							waterSystem.getSystemElevation())
					.execute();
			this.closeConnection();
			
			if(result > 0)
				newWaterSystem = this.getWaterSystem(waterSystem.getCommunity(), waterSystem.getName());
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return newWaterSystem;
	}

	/* (non-Javadoc)
	 * @see org.ongawa.peru.chlorination.persistence.db.IDataSource#getWaterSystems(org.ongawa.peru.chlorination.persistence.elements.Community)
	 */
	@Override
	public List<WaterSystem> getWaterSystems(Community community){
		if(community == null)
			throw new NullArgumentException("community");
		List<WaterSystem> waterSystems = null;
				
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			waterSystems = new ArrayList<WaterSystem>();
			
			DSLContext select = this.prepareDSLContext(this.connection);
			Result<Record> result = select.select().from(Watersystem.WATERSYSTEM)
					.where(Watersystem.WATERSYSTEM.COMMUNITY_IDCOMMUNITY.eq(community.getCommunityId()))
					.and(Watersystem.WATERSYSTEM.COMMUNITY_SUBBASIN_IDSUBBASIN.eq(community.getSubBasin().getSubBasinId()))
					.fetch();
			
			WaterSystem waterSystem = null;
			for(Record record : result){
				waterSystem = this.readWaterSystem(record, community);
				waterSystems.add(waterSystem);
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		return waterSystems;
	}
	
	/* (non-Javadoc)
	 * @see org.ongawa.peru.chlorination.persistence.db.IDataSource#getWaterSystem(org.ongawa.peru.chlorination.persistence.elements.Community, int)
	 */
	@Override
	public WaterSystem getWaterSystem(Community community, int idWaterSystem){
		if(community == null)
			throw new NullArgumentException("community");
		WaterSystem waterSystem = null;
			
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext select = this.prepareDSLContext(this.connection);
			Result<Record> result = select.select().from(Watersystem.WATERSYSTEM)
					.where(Watersystem.WATERSYSTEM.IDWATERSYSTEM.eq(idWaterSystem))
					.and(Watersystem.WATERSYSTEM.COMMUNITY_IDCOMMUNITY.eq(community.getCommunityId()))
					.and(Watersystem.WATERSYSTEM.COMMUNITY_SUBBASIN_IDSUBBASIN.eq(community.getSubBasin().getSubBasinId()))
					.limit(1)
					.fetch();
			
			for(Record record : result){
				waterSystem = this.readWaterSystem(record, community);
				break;
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return waterSystem;
	}
	
	/* (non-Javadoc)
	 * @see org.ongawa.peru.chlorination.persistence.db.IDataSource#getWaterSystem(org.ongawa.peru.chlorination.persistence.elements.Community, java.lang.String)
	 */
	@Override
	public WaterSystem getWaterSystem(Community community, String name){
		if(community == null)
			throw new NullArgumentException("community");
		if(name == null)
			throw new NullArgumentException("name");
		WaterSystem waterSystem = null;
				
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext select = this.prepareDSLContext(this.connection);
			Result<Record> result = select.select().from(Watersystem.WATERSYSTEM)
					.where(Watersystem.WATERSYSTEM.NAME.eq(name))
					.and(Watersystem.WATERSYSTEM.COMMUNITY_IDCOMMUNITY.eq(community.getCommunityId()))
					.and(Watersystem.WATERSYSTEM.COMMUNITY_SUBBASIN_IDSUBBASIN.eq(community.getSubBasin().getSubBasinId()))
					.limit(1)
					.fetch();
			
			for(Record record : result){
				waterSystem = this.readWaterSystem(record, community);
				break;
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return waterSystem;
	}
	
	@Override
	public boolean editWaterSystem(WaterSystem waterSystem) {
		if(waterSystem == null)
			throw new NullArgumentException("waterSystem");
		int result = 0;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext update = this.prepareDSLContext(this.connection);
			result = update.update(Watersystem.WATERSYSTEM)
					.set(Watersystem.WATERSYSTEM.NAME, waterSystem.getName())
					.set(Watersystem.WATERSYSTEM.FAMILIESNUM, waterSystem.getFamiliesNum())
					.set(Watersystem.WATERSYSTEM.POPULATION, waterSystem.getPopulation())
					.set(Watersystem.WATERSYSTEM.POPULATIONFORECAST, waterSystem.getPopulationForecast())
					.set(Watersystem.WATERSYSTEM.GROWINGINDEX, waterSystem.getGrowingIndex())
					.set(Watersystem.WATERSYSTEM.JASSNUM, waterSystem.getJASSNum())
					.set(Watersystem.WATERSYSTEM.FUTURENEEDEDFLOW, waterSystem.getFutureNeededFlow())
					.set(Watersystem.WATERSYSTEM.RESERVOIRVOLUME, waterSystem.getReservoirVolume())
					.set(Watersystem.WATERSYSTEM.SYSTEMELEVATION, waterSystem.getSystemElevation())
					.where(Watersystem.WATERSYSTEM.IDWATERSYSTEM.eq(waterSystem.getWaterSystemId()))
					.and(Watersystem.WATERSYSTEM.COMMUNITY_IDCOMMUNITY.eq(waterSystem.getCommunity().getCommunityId()))
					.and(Watersystem.WATERSYSTEM.COMMUNITY_SUBBASIN_IDSUBBASIN.eq(waterSystem.getCommunity().getSubBasin().getSubBasinId()))
					.execute();
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return result>0;
	}
	
	@Override
	public boolean removeWaterSystem(WaterSystem waterSystem) {
		if(waterSystem == null)
			throw new NullArgumentException("waterSystem");
		int result = 0;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext delete = this.prepareDSLContext(this.connection);
			result = delete.delete(Watersystem.WATERSYSTEM)
					.where(Watersystem.WATERSYSTEM.IDWATERSYSTEM.eq(waterSystem.getWaterSystemId()))
					.and(Watersystem.WATERSYSTEM.COMMUNITY_IDCOMMUNITY.eq(waterSystem.getCommunity().getCommunityId()))
					.and(Watersystem.WATERSYSTEM.COMMUNITY_SUBBASIN_IDSUBBASIN.eq(waterSystem.getCommunity().getSubBasin().getSubBasinId()))
					.execute();
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return result>0;
	}
	
	@Override
	public WaterSpring addWaterSpring(WaterSpring waterSpring) {
		if(waterSpring == null)
			throw new NullArgumentException("waterSpring");
		WaterSpring newWaterSpring = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext insert = this.prepareDSLContext(this.connection);
			int result = insert.insertInto(Waterspring.WATERSPRING,
					Waterspring.WATERSPRING.NAME)
					.values(waterSpring.getName())
					.execute();
			this.closeConnection();
			
			if(result > 0)
				newWaterSpring = this.getWaterSpring(waterSpring.getName());
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return newWaterSpring;
	}

	/* (non-Javadoc)
	 * @see org.ongawa.peru.chlorination.persistence.db.IDataSource#getWaterSprings()
	 */
	@Override
	public List<WaterSpring> getWaterSprings(){
		List<WaterSpring> waterSprings = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			waterSprings = new ArrayList<WaterSpring>();
			DSLContext select = this.prepareDSLContext(this.connection);
			Result<Record> result = select.select().from(Waterspring.WATERSPRING).fetch();
			
			WaterSpring waterSpring;
			for(Record record : result){
				waterSpring = this.readWaterSpring(record);
				waterSprings.add(waterSpring);
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return waterSprings;
	}
	
	/* (non-Javadoc)
	 * @see org.ongawa.peru.chlorination.persistence.db.IDataSource#getWaterSprings(int)
	 */
	@Override
	public WaterSpring getWaterSpring(int idWaterSpring){
		WaterSpring waterSpring = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext select = this.prepareDSLContext(this.connection);
			Result<Record> result = select.select().from(Waterspring.WATERSPRING)
					.where(Waterspring.WATERSPRING.IDWATERSPRING.eq(idWaterSpring))
					.limit(1)
					.fetch();
			
			for(Record record : result){
				waterSpring = this.readWaterSpring(record);
				break;
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return waterSpring;
	}
	
	/* (non-Javadoc)
	 * @see org.ongawa.peru.chlorination.persistence.db.IDataSource#getWaterSprings(java.lang.String)
	 */
	@Override
	public WaterSpring getWaterSpring(String name){
		if(name == null)
			throw new NullArgumentException("name");
		WaterSpring waterSpring = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext select = this.prepareDSLContext(this.connection);
			Result<Record> result = select.select().from(Waterspring.WATERSPRING)
					.where(Waterspring.WATERSPRING.NAME.eq(name))
					.limit(1)
					.fetch();
			
			for(Record record : result){
				waterSpring = this.readWaterSpring(record);
				break;
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return waterSpring;
	}
	
	/* (non-Javadoc)
	 * @see org.ongawa.peru.chlorination.persistence.db.IDataSource#getWaterSprings(org.ongawa.peru.chlorination.persistence.elements.WaterSystem)
	 */
	@Override
	public List<WaterSpring> getWaterSprings(WaterSystem waterSystem){
		if(waterSystem == null)
			throw new NullArgumentException("waterSystem");
		List<WaterSpring> waterSprings = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			waterSprings = new ArrayList<WaterSpring>();
			DSLContext select = this.prepareDSLContext(this.connection);
			Result<Record2<Integer, String>> result = select.select(Waterspring.WATERSPRING.IDWATERSPRING, Waterspring.WATERSPRING.NAME)
					.from(Waterspring.WATERSPRING, WatersystemHasWaterspring.WATERSYSTEM_HAS_WATERSPRING)
					.where(Waterspring.WATERSPRING.IDWATERSPRING.eq(WatersystemHasWaterspring.WATERSYSTEM_HAS_WATERSPRING.WATERSPRING_IDWATERSPRING))
					.and(WatersystemHasWaterspring.WATERSYSTEM_HAS_WATERSPRING.WATERSYSTEM_IDWATERSYSTEM.eq(waterSystem.getWaterSystemId()))
					.and(WatersystemHasWaterspring.WATERSYSTEM_HAS_WATERSPRING.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(waterSystem.getCommunity().getCommunityId()))
					.and(WatersystemHasWaterspring.WATERSYSTEM_HAS_WATERSPRING.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(waterSystem.getCommunity().getSubBasin().getSubBasinId()))
					.fetch();
			
			WaterSpring waterSpring = null;
			for(Record2<Integer, String> record : result){
				waterSpring = this.readWaterSpring(record);
				waterSprings.add(waterSpring);
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return waterSprings;
	}
	
	@Override
	public boolean editWaterSpring(WaterSpring waterSpring) {
		if(waterSpring == null)
			throw new NullArgumentException("waterSpring");
		int result = 0;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext update = this.prepareDSLContext(this.connection);
			result = update.update(Waterspring.WATERSPRING)
					.set(Waterspring.WATERSPRING.NAME, waterSpring.getName())
					.where(Waterspring.WATERSPRING.IDWATERSPRING.eq(waterSpring.getWaterSpringId()))
					.execute();
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return result>0;
	}
	
	@Override
	public boolean removeWaterSpring(WaterSpring waterSpring) {
		if(waterSpring == null)
			throw new NullArgumentException("waterSpring");
		int result = 0;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext delete = this.prepareDSLContext(this.connection);
			result = delete.delete(Waterspring.WATERSPRING).where(Waterspring.WATERSPRING.IDWATERSPRING.eq(waterSpring.getWaterSpringId())).execute();
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return result>0;
	}
	
	@Override
	public MeasuringPoint addMeasuringPoint(MeasuringPoint measuringPoint) {
		if(measuringPoint == null)
			throw new NullArgumentException("measuringPoint");
		MeasuringPoint newMeasuringPoint = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext insert = this.prepareDSLContext(this.connection);
			int result = insert.insertInto(Measuringpoint.MEASURINGPOINT,
					Measuringpoint.MEASURINGPOINT.NAME,
					Measuringpoint.MEASURINGPOINT.WATERSYSTEM_HAS_WATERSPRING_WATERSPRING_IDWATERSPRING,
					Measuringpoint.MEASURINGPOINT.WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_IDWATERSYSTEM,
					Measuringpoint.MEASURINGPOINT.WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_COMMUNITY_IDCOMMUNITY,
					Measuringpoint.MEASURINGPOINT.WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN)
					.values(measuringPoint.getName(),
							measuringPoint.getWaterSpring().getWaterSpringId(),
							measuringPoint.getWaterSystem().getWaterSystemId(),
							measuringPoint.getWaterSystem().getCommunity().getCommunityId(),
							measuringPoint.getWaterSystem().getCommunity().getSubBasin().getSubBasinId())
					.execute();
			this.closeConnection();
			
			if(result>0)
				newMeasuringPoint = this.getMeasuringPoint(measuringPoint.getWaterSystem(), measuringPoint.getWaterSpring(), measuringPoint.getName());
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return newMeasuringPoint;
	}

	/* (non-Javadoc)
	 * @see org.ongawa.peru.chlorination.persistence.db.IDataSource#getMeasuringPoints(org.ongawa.peru.chlorination.persistence.elements.WaterSystem, org.ongawa.peru.chlorination.persistence.elements.WaterSpring)
	 */
	@Override
	public List<MeasuringPoint> getMeasuringPoints(WaterSystem waterSystem, WaterSpring waterSpring){
		if(waterSpring == null)
			throw new NullArgumentException("waterSpring");
		if(waterSystem == null)
			throw new NullArgumentException("waterSystem");
		List<MeasuringPoint> measuringPoints = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			measuringPoints = new ArrayList<MeasuringPoint>();
			DSLContext select = this.prepareDSLContext(this.connection);
			Result<Record> result = select.select().from(Measuringpoint.MEASURINGPOINT)
					.where(Measuringpoint.MEASURINGPOINT.WATERSYSTEM_HAS_WATERSPRING_WATERSPRING_IDWATERSPRING.eq(waterSpring.getWaterSpringId()))
					.and(Measuringpoint.MEASURINGPOINT.WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_IDWATERSYSTEM.eq(waterSystem.getWaterSystemId()))
					.and(Measuringpoint.MEASURINGPOINT.WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(waterSystem.getCommunity().getCommunityId()))
					.and(Measuringpoint.MEASURINGPOINT.WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(waterSystem.getCommunity().getSubBasin().getSubBasinId()))
					.fetch();
			
			MeasuringPoint measuringPoint = null;
			for(Record record : result){
				measuringPoint = this.readMeasuringPoint(record, waterSystem, waterSpring);
				measuringPoints.add(measuringPoint);
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return measuringPoints;
	}
	
	/* (non-Javadoc)
	 * @see org.ongawa.peru.chlorination.persistence.db.IDataSource#getMeasuringPoint(org.ongawa.peru.chlorination.persistence.elements.WaterSystem, org.ongawa.peru.chlorination.persistence.elements.WaterSpring, int)
	 */
	@Override
	public MeasuringPoint getMeasuringPoint(WaterSystem waterSystem, WaterSpring waterSpring, int idMeasuringPoint){
		if(waterSpring == null)
			throw new NullArgumentException("waterSpring");
		if(waterSystem == null)
			throw new NullArgumentException("waterSystem");
		MeasuringPoint measuringPoint = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext select  = this.prepareDSLContext(this.connection);
			Result<Record> result = select.select().from(Measuringpoint.MEASURINGPOINT)
					.where(Measuringpoint.MEASURINGPOINT.IDMEASURINGPOINT.eq(idMeasuringPoint))
					.and(Measuringpoint.MEASURINGPOINT.WATERSYSTEM_HAS_WATERSPRING_WATERSPRING_IDWATERSPRING.eq(waterSpring.getWaterSpringId()))
					.and(Measuringpoint.MEASURINGPOINT.WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_IDWATERSYSTEM.eq(waterSystem.getWaterSystemId()))
					.and(Measuringpoint.MEASURINGPOINT.WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(waterSystem.getCommunity().getCommunityId()))
					.and(Measuringpoint.MEASURINGPOINT.WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(waterSystem.getCommunity().getSubBasin().getSubBasinId()))
					.limit(1)
					.fetch();
			
			for(Record record : result){
				measuringPoint = this.readMeasuringPoint(record, waterSystem, waterSpring);
				break;
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return measuringPoint;
	}
	
	@Override
	public MeasuringPoint getMeasuringPoint(WaterSystem waterSystem, WaterSpring waterSpring, String name) {
		if(waterSpring == null)
			throw new NullArgumentException("waterSpring");
		if(waterSystem == null)
			throw new NullArgumentException("waterSystem");
		if(name == null)
			throw new NullArgumentException("name");
		MeasuringPoint measuringPoint = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext select  = this.prepareDSLContext(this.connection);
			Result<Record> result = select.select().from(Measuringpoint.MEASURINGPOINT)
					.where(Measuringpoint.MEASURINGPOINT.NAME.eq(name))
					.and(Measuringpoint.MEASURINGPOINT.WATERSYSTEM_HAS_WATERSPRING_WATERSPRING_IDWATERSPRING.eq(waterSpring.getWaterSpringId()))
					.and(Measuringpoint.MEASURINGPOINT.WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_IDWATERSYSTEM.eq(waterSystem.getWaterSystemId()))
					.and(Measuringpoint.MEASURINGPOINT.WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(waterSystem.getCommunity().getCommunityId()))
					.and(Measuringpoint.MEASURINGPOINT.WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(waterSystem.getCommunity().getSubBasin().getSubBasinId()))
					.limit(1)
					.fetch();
			
			for(Record record : result){
				measuringPoint = this.readMeasuringPoint(record, waterSystem, waterSpring);
				break;
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return measuringPoint;
	}
	
	@Override
	public boolean editMeasuringPoint(MeasuringPoint measuringPoint) {
		if(measuringPoint == null)
			throw new NullArgumentException("measuringPoint");
		int result = 0;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext update = this.prepareDSLContext(this.connection);
			result = update.update(Measuringpoint.MEASURINGPOINT)
					.set(Measuringpoint.MEASURINGPOINT.NAME, measuringPoint.getName())
					.where(Measuringpoint.MEASURINGPOINT.IDMEASURINGPOINT.eq(measuringPoint.getMeasuringPointId()))
					.and(Measuringpoint.MEASURINGPOINT.WATERSYSTEM_HAS_WATERSPRING_WATERSPRING_IDWATERSPRING.eq(measuringPoint.getWaterSpring().getWaterSpringId()))
					.and(Measuringpoint.MEASURINGPOINT.WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_IDWATERSYSTEM.eq(measuringPoint.getWaterSystem().getWaterSystemId()))
					.and(Measuringpoint.MEASURINGPOINT.WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(measuringPoint.getWaterSystem().getCommunity().getCommunityId()))
					.and(Measuringpoint.MEASURINGPOINT.WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(measuringPoint.getWaterSystem().getCommunity().getSubBasin().getSubBasinId()))
					.execute();
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return result>0;
	}
	
	@Override
	public boolean removeMeasuringPoint(MeasuringPoint measuringPoint) {
		if(measuringPoint == null)
			throw new NullArgumentException("measuringPoint");
		int result = 0;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext delete = this.prepareDSLContext(this.connection);
			result = delete.delete(Measuringpoint.MEASURINGPOINT)
					.where(Measuringpoint.MEASURINGPOINT.IDMEASURINGPOINT.eq(measuringPoint.getMeasuringPointId()))
					.and(Measuringpoint.MEASURINGPOINT.WATERSYSTEM_HAS_WATERSPRING_WATERSPRING_IDWATERSPRING.eq(measuringPoint.getWaterSpring().getWaterSpringId()))
					.and(Measuringpoint.MEASURINGPOINT.WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_IDWATERSYSTEM.eq(measuringPoint.getWaterSystem().getWaterSystemId()))
					.and(Measuringpoint.MEASURINGPOINT.WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(measuringPoint.getWaterSystem().getCommunity().getCommunityId()))
					.and(Measuringpoint.MEASURINGPOINT.WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(measuringPoint.getWaterSystem().getCommunity().getSubBasin().getSubBasinId()))
					.execute();
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return result>0;
	}
	
	@Override
	public MeasuredFlow addMeasuredFlow(MeasuredFlow measuredFlow) {
		if(measuredFlow == null)
			throw new NullArgumentException("measuredFlow");
		MeasuredFlow newMeasuredFlow = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext insert = this.prepareDSLContext(this.connection);
			int result = insert.insertInto(Measuredflow.MEASUREDFLOW,
					Measuredflow.MEASUREDFLOW.DATE,
					Measuredflow.MEASUREDFLOW.FLOW,
					Measuredflow.MEASUREDFLOW.COMMENTS,
					Measuredflow.MEASUREDFLOW.MEASURINGPOINT_IDMEASURINGPOINT,
					Measuredflow.MEASUREDFLOW.MEASURINGPOINT_WATERSYSTEM_HAS_WATERSPRING_WATERSPRING_IDWATERSPRING,
					Measuredflow.MEASUREDFLOW.MEASURINGPOINT_WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_IDWATERSYSTEM,
					Measuredflow.MEASUREDFLOW.MEASURINGPOINT_WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_COMMUNITY_IDCOMMUNITY,
					Measuredflow.MEASUREDFLOW.MEASURINGPOINT_WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN)
					.values(measuredFlow.getDate(),
							measuredFlow.getFlow(),
							measuredFlow.getComments(),
							measuredFlow.getMeasuringPoint().getMeasuringPointId(),
							measuredFlow.getMeasuringPoint().getWaterSpring().getWaterSpringId(),
							measuredFlow.getMeasuringPoint().getWaterSystem().getWaterSystemId(),
							measuredFlow.getMeasuringPoint().getWaterSystem().getCommunity().getCommunityId(),
							measuredFlow.getMeasuringPoint().getWaterSystem().getCommunity().getSubBasin().getSubBasinId())
					.execute();
			this.closeConnection();
			
			if(result>0)
				this.getMeasuredFlow(measuredFlow.getDate(), measuredFlow.getMeasuringPoint());
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return newMeasuredFlow;
	}

	/* (non-Javadoc)
	 * @see org.ongawa.peru.chlorination.persistence.db.IDataSource#getMeasuredFlows(org.ongawa.peru.chlorination.persistence.elements.MeasuringPoint)
	 */
	@Override
	public List<MeasuredFlow> getMeasuredFlows(MeasuringPoint measuringPoint){
		if(measuringPoint == null)
			throw new NullArgumentException("measuringPoint");
		List<MeasuredFlow> measuredFlows = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			measuredFlows = new ArrayList<MeasuredFlow>();
			DSLContext select = this.prepareDSLContext(this.connection);
			Result<Record> result = select.select().from(Measuredflow.MEASUREDFLOW)
					.where(Measuredflow.MEASUREDFLOW.MEASURINGPOINT_IDMEASURINGPOINT.eq(measuringPoint.getMeasuringPointId()))
					.and(Measuredflow.MEASUREDFLOW.MEASURINGPOINT_WATERSYSTEM_HAS_WATERSPRING_WATERSPRING_IDWATERSPRING.eq(measuringPoint.getWaterSpring().getWaterSpringId()))
					.and(Measuredflow.MEASUREDFLOW.MEASURINGPOINT_WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_IDWATERSYSTEM.eq(measuringPoint.getWaterSystem().getWaterSystemId()))
					.and(Measuredflow.MEASUREDFLOW.MEASURINGPOINT_WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(measuringPoint.getWaterSystem().getCommunity().getCommunityId()))
					.and(Measuredflow.MEASUREDFLOW.MEASURINGPOINT_WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(measuringPoint.getWaterSystem().getCommunity().getSubBasin().getSubBasinId()))
					.fetch();
			
			MeasuredFlow measuredFlow = null;
			for(Record record : result){
				measuredFlow = this.readMeasuredFlow(record, measuringPoint);
				measuredFlows.add(measuredFlow);
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return measuredFlows;
	}
	
	/* (non-Javadoc)
	 * @see org.ongawa.peru.chlorination.persistence.db.IDataSource#getMeasuredFlows(java.sql.Timestamp, java.sql.Timestamp, org.ongawa.peru.chlorination.persistence.elements.MeasuringPoint)
	 */
	@Override
	public List<MeasuredFlow> getMeasuredFlows(Timestamp beginDate, Timestamp endDate, MeasuringPoint measuringPoint){
		if(beginDate == null)
			throw new NullArgumentException("beginDate");
		if(endDate == null)
			throw new NullArgumentException("endDate");
		if(measuringPoint == null)
			throw new NullArgumentException("measuringPoint");
		List<MeasuredFlow> measuredFlows = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			measuredFlows = new ArrayList<MeasuredFlow>();
			DSLContext select = this.prepareDSLContext(this.connection);
			Result<Record> result = select.select().from(Measuredflow.MEASUREDFLOW)
					.where(Measuredflow.MEASUREDFLOW.DATE.between(beginDate, endDate))
					.and(Measuredflow.MEASUREDFLOW.MEASURINGPOINT_IDMEASURINGPOINT.eq(measuringPoint.getMeasuringPointId()))
					.and(Measuredflow.MEASUREDFLOW.MEASURINGPOINT_WATERSYSTEM_HAS_WATERSPRING_WATERSPRING_IDWATERSPRING.eq(measuringPoint.getWaterSpring().getWaterSpringId()))
					.and(Measuredflow.MEASUREDFLOW.MEASURINGPOINT_WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_IDWATERSYSTEM.eq(measuringPoint.getWaterSystem().getWaterSystemId()))
					.and(Measuredflow.MEASUREDFLOW.MEASURINGPOINT_WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(measuringPoint.getWaterSystem().getCommunity().getCommunityId()))
					.and(Measuredflow.MEASUREDFLOW.MEASURINGPOINT_WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(measuringPoint.getWaterSystem().getCommunity().getSubBasin().getSubBasinId()))
					.fetch();
			
			MeasuredFlow measuredFlow = null;
			for(Record record : result){
				measuredFlow = this.readMeasuredFlow(record, measuringPoint);
				measuredFlows.add(measuredFlow);
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return measuredFlows;
	}
	
	/* (non-Javadoc)
	 * @see org.ongawa.peru.chlorination.persistence.db.IDataSource#getMeasuredFlow(java.sql.Timestamp, org.ongawa.peru.chlorination.persistence.elements.MeasuringPoint)
	 */
	@Override
	public MeasuredFlow getMeasuredFlow(Timestamp date, MeasuringPoint measuringPoint){
		if(date == null)
			throw new NullArgumentException("date");
		if(measuringPoint == null)
			throw new NullArgumentException("measuringPoint");
		MeasuredFlow measuredFlow = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext select = this.prepareDSLContext(this.connection);
			Result<Record> result = select.select().from(Measuredflow.MEASUREDFLOW)
					.where(Measuredflow.MEASUREDFLOW.DATE.eq(date))
					.and(Measuredflow.MEASUREDFLOW.MEASURINGPOINT_IDMEASURINGPOINT.eq(measuringPoint.getMeasuringPointId()))
					.and(Measuredflow.MEASUREDFLOW.MEASURINGPOINT_WATERSYSTEM_HAS_WATERSPRING_WATERSPRING_IDWATERSPRING.eq(measuringPoint.getWaterSpring().getWaterSpringId()))
					.and(Measuredflow.MEASUREDFLOW.MEASURINGPOINT_WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_IDWATERSYSTEM.eq(measuringPoint.getWaterSystem().getWaterSystemId()))
					.and(Measuredflow.MEASUREDFLOW.MEASURINGPOINT_WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(measuringPoint.getWaterSystem().getCommunity().getCommunityId()))
					.and(Measuredflow.MEASUREDFLOW.MEASURINGPOINT_WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(measuringPoint.getWaterSystem().getCommunity().getSubBasin().getSubBasinId()))
					.limit(1)
					.fetch();
			
			for(Record record : result){
				measuredFlow = this.readMeasuredFlow(record, measuringPoint);
				break;
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return measuredFlow;
	}
	
	@Override
	public boolean editMeasuredFlow(Timestamp oldMeasuredFlowDate, MeasuredFlow newMeasuredFlow) {
		if(oldMeasuredFlowDate == null)
			throw new NullArgumentException("olgMeasuredFlowDate");
		if(newMeasuredFlow == null)
			throw new NullArgumentException("newMeasuredFlow");
		int result = 0;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext update = this.prepareDSLContext(this.connection);
			result = update.update(Measuredflow.MEASUREDFLOW)
					.set(Measuredflow.MEASUREDFLOW.DATE, newMeasuredFlow.getDate())
					.set(Measuredflow.MEASUREDFLOW.FLOW, newMeasuredFlow.getFlow())
					.set(Measuredflow.MEASUREDFLOW.COMMENTS, newMeasuredFlow.getComments())
					.where(Measuredflow.MEASUREDFLOW.DATE.eq(oldMeasuredFlowDate))
					.and(Measuredflow.MEASUREDFLOW.MEASURINGPOINT_IDMEASURINGPOINT.eq(newMeasuredFlow.getMeasuringPoint().getMeasuringPointId()))
					.and(Measuredflow.MEASUREDFLOW.MEASURINGPOINT_WATERSYSTEM_HAS_WATERSPRING_WATERSPRING_IDWATERSPRING.eq(newMeasuredFlow.getMeasuringPoint().getWaterSpring().getWaterSpringId()))
					.and(Measuredflow.MEASUREDFLOW.MEASURINGPOINT_WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_IDWATERSYSTEM.eq(newMeasuredFlow.getMeasuringPoint().getWaterSystem().getWaterSystemId()))
					.and(Measuredflow.MEASUREDFLOW.MEASURINGPOINT_WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(newMeasuredFlow.getMeasuringPoint().getWaterSystem().getCommunity().getCommunityId()))
					.and(Measuredflow.MEASUREDFLOW.MEASURINGPOINT_WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(newMeasuredFlow.getMeasuringPoint().getWaterSystem().getCommunity().getSubBasin().getSubBasinId()))
					.execute();
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return result>0;
	}
	
	@Override
	public boolean removeMeasuredFlow(MeasuredFlow measuredFlow) {
		if(measuredFlow == null)
			throw new NullArgumentException("measuredFlow");
		int result = 0;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext delete = this.prepareDSLContext(this.connection);
			result = delete.delete(Measuredflow.MEASUREDFLOW).where(Measuredflow.MEASUREDFLOW.DATE.eq(measuredFlow.getDate()))
					.and(Measuredflow.MEASUREDFLOW.MEASURINGPOINT_IDMEASURINGPOINT.eq(measuredFlow.getMeasuringPoint().getMeasuringPointId()))
					.and(Measuredflow.MEASUREDFLOW.MEASURINGPOINT_WATERSYSTEM_HAS_WATERSPRING_WATERSPRING_IDWATERSPRING.eq(measuredFlow.getMeasuringPoint().getWaterSpring().getWaterSpringId()))
					.and(Measuredflow.MEASUREDFLOW.MEASURINGPOINT_WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_IDWATERSYSTEM.eq(measuredFlow.getMeasuringPoint().getWaterSystem().getWaterSystemId()))
					.and(Measuredflow.MEASUREDFLOW.MEASURINGPOINT_WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(measuredFlow.getMeasuringPoint().getWaterSystem().getCommunity().getCommunityId()))
					.and(Measuredflow.MEASUREDFLOW.MEASURINGPOINT_WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(measuredFlow.getMeasuringPoint().getWaterSystem().getCommunity().getSubBasin().getSubBasinId()))
					.execute();
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return result>0;
	}
	
	@Override
	public ChlorineCalculation addChlorineCalculation(ChlorineCalculation chlorineCalculation) {
		if(chlorineCalculation == null)
			throw new NullArgumentException("chlorineCalculation");
		ChlorineCalculation newChlorineCalculation = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext insert = this.prepareDSLContext(this.connection);
			int result = insert.insertInto(Chlorinecalculation.CHLORINECALCULATION,
					Chlorinecalculation.CHLORINECALCULATION.DATE,
					Chlorinecalculation.CHLORINECALCULATION.POPULATION,
					Chlorinecalculation.CHLORINECALCULATION.TANKVOLUME,
					Chlorinecalculation.CHLORINECALCULATION.TANKUSEFULVOLUME,
					Chlorinecalculation.CHLORINECALCULATION.ENDOWMENT,
					Chlorinecalculation.CHLORINECALCULATION.CHLORINEPURENESS,
					Chlorinecalculation.CHLORINECALCULATION.INPUTFLOW,
					Chlorinecalculation.CHLORINECALCULATION.RELOADTIME,
					Chlorinecalculation.CHLORINECALCULATION.DEMANDCLR,
					Chlorinecalculation.CHLORINECALCULATION.DEMANDACTIVECHLORINE,
					Chlorinecalculation.CHLORINECALCULATION.DEMANDCOMMONPRODUCT,
					Chlorinecalculation.CHLORINECALCULATION.WATERSYSTEM_IDWATERSYSTEM,
					Chlorinecalculation.CHLORINECALCULATION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY,
					Chlorinecalculation.CHLORINECALCULATION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN)
					.values(chlorineCalculation.getDate(),
							chlorineCalculation.getPopulation(),
							chlorineCalculation.getTankVolume(),
							chlorineCalculation.getTankUsefulVolume(),
							chlorineCalculation.getEndowment(),
							chlorineCalculation.getChlorinePureness(),
							chlorineCalculation.getInputFlow(),
							chlorineCalculation.getReloadTime(),
							chlorineCalculation.getDemandCLR(),
							chlorineCalculation.getDemandActiveChlorine(),
							chlorineCalculation.getDemandCommonProduct(),
							chlorineCalculation.getWaterSystem().getWaterSystemId(),
							chlorineCalculation.getWaterSystem().getCommunity().getCommunityId(),
							chlorineCalculation.getWaterSystem().getCommunity().getSubBasin().getSubBasinId())
					.execute();
			this.closeConnection();
			
			if(result>0)
				newChlorineCalculation = this.getChlorineCalculation(chlorineCalculation.getDate(), chlorineCalculation.getWaterSystem());
					
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return newChlorineCalculation;
	}

	/* (non-Javadoc)
	 * @see org.ongawa.peru.chlorination.persistence.db.IDataSource#getChlorineCalculations(org.ongawa.peru.chlorination.persistence.elements.WaterSystem)
	 */
	@Override
	public List<ChlorineCalculation> getChlorineCalculations(WaterSystem waterSystem){
		if(waterSystem == null)
			throw new NullArgumentException("waterSystem");
		List<ChlorineCalculation> chlorineCalculations = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			chlorineCalculations = new ArrayList<ChlorineCalculation>();
			DSLContext select = this.prepareDSLContext(this.connection);
			Result<Record> result = select.select().from(Chlorinecalculation.CHLORINECALCULATION)
					.where(Chlorinecalculation.CHLORINECALCULATION.WATERSYSTEM_IDWATERSYSTEM.eq(waterSystem.getWaterSystemId()))
					.and(Chlorinecalculation.CHLORINECALCULATION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(waterSystem.getCommunity().getCommunityId()))
					.and(Chlorinecalculation.CHLORINECALCULATION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(waterSystem.getCommunity().getSubBasin().getSubBasinId()))
					.fetch();
			
			ChlorineCalculation chlorineCalculation = null;
			for(Record record : result){
				chlorineCalculation = this.readChlorineCalculation(record, waterSystem);
				chlorineCalculations.add(chlorineCalculation);
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return chlorineCalculations;
	}
	
	/* (non-Javadoc)
	 * @see org.ongawa.peru.chlorination.persistence.db.IDataSource#getChlorineCalculations(java.sql.Timestamp, java.sql.Timestamp, org.ongawa.peru.chlorination.persistence.elements.WaterSystem)
	 */
	@Override
	public List<ChlorineCalculation> getChlorineCalculations(Timestamp beginDate, Timestamp endDate, WaterSystem waterSystem){
		if(beginDate == null)
			throw new NullArgumentException("beginDate");
		if(endDate == null)
			throw new NullArgumentException("endDate");
		if(waterSystem == null)
			throw new NullArgumentException("waterSystem");
		List<ChlorineCalculation> chlorineCalculations = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			chlorineCalculations = new ArrayList<ChlorineCalculation>();
			DSLContext select = this.prepareDSLContext(this.connection);
			Result<Record> result = select.select().from(Chlorinecalculation.CHLORINECALCULATION)
					.where(Chlorinecalculation.CHLORINECALCULATION.DATE.between(beginDate, endDate))
					.and(Chlorinecalculation.CHLORINECALCULATION.WATERSYSTEM_IDWATERSYSTEM.eq(waterSystem.getWaterSystemId()))
					.and(Chlorinecalculation.CHLORINECALCULATION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(waterSystem.getCommunity().getCommunityId()))
					.and(Chlorinecalculation.CHLORINECALCULATION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(waterSystem.getCommunity().getSubBasin().getSubBasinId()))
					.fetch();
			
			ChlorineCalculation chlorineCalculation = null;
			for(Record record : result){
				chlorineCalculation = this.readChlorineCalculation(record, waterSystem);
				chlorineCalculations.add(chlorineCalculation);
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return chlorineCalculations;
	}
	
	/* (non-Javadoc)
	 * @see org.ongawa.peru.chlorination.persistence.db.IDataSource#getChlorineCalculation(java.sql.Timestamp, org.ongawa.peru.chlorination.persistence.elements.WaterSystem)
	 */
	@Override
	public ChlorineCalculation getChlorineCalculation(Timestamp date, WaterSystem waterSystem){
		if(date == null)
			throw new NullArgumentException("date");
		if(waterSystem == null)
			throw new NullArgumentException("waterSystem");
		ChlorineCalculation chlorineCalculation = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext select = this.prepareDSLContext(this.connection);
			Result<Record> result = select.select().from(Chlorinecalculation.CHLORINECALCULATION)
					.where(Chlorinecalculation.CHLORINECALCULATION.DATE.eq(date))
					.and(Chlorinecalculation.CHLORINECALCULATION.WATERSYSTEM_IDWATERSYSTEM.eq(waterSystem.getWaterSystemId()))
					.and(Chlorinecalculation.CHLORINECALCULATION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(waterSystem.getCommunity().getCommunityId()))
					.and(Chlorinecalculation.CHLORINECALCULATION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(waterSystem.getCommunity().getSubBasin().getSubBasinId()))
					.limit(1)
					.fetch();
			
			for(Record record : result){
				chlorineCalculation = this.readChlorineCalculation(record, waterSystem);
				break;
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return chlorineCalculation;
	}
	
	@Override
	public boolean editChlorineCalcuilation(Timestamp oldChlorineCalculationDate, ChlorineCalculation newChlorineCalculation) {
		if(oldChlorineCalculationDate == null)
			throw new NullArgumentException("oldChlorineCalcularionDate");
		if(newChlorineCalculation == null)
			throw new NullArgumentException("newChlorineCalculation");
		int result = 0;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext update = this.prepareDSLContext(this.connection);
			result = update.update(Chlorinecalculation.CHLORINECALCULATION)
					.set(Chlorinecalculation.CHLORINECALCULATION.DATE, newChlorineCalculation.getDate())
					.set(Chlorinecalculation.CHLORINECALCULATION.POPULATION, newChlorineCalculation.getPopulation())
					.set(Chlorinecalculation.CHLORINECALCULATION.TANKVOLUME, newChlorineCalculation.getTankVolume())
					.set(Chlorinecalculation.CHLORINECALCULATION.TANKUSEFULVOLUME, newChlorineCalculation.getTankUsefulVolume())
					.set(Chlorinecalculation.CHLORINECALCULATION.ENDOWMENT, newChlorineCalculation.getEndowment())
					.set(Chlorinecalculation.CHLORINECALCULATION.CHLORINEPURENESS, newChlorineCalculation.getChlorinePureness())
					.set(Chlorinecalculation.CHLORINECALCULATION.INPUTFLOW, newChlorineCalculation.getInputFlow())
					.set(Chlorinecalculation.CHLORINECALCULATION.RELOADTIME, newChlorineCalculation.getReloadTime())
					.set(Chlorinecalculation.CHLORINECALCULATION.DEMANDCLR, newChlorineCalculation.getDemandCLR())
					.set(Chlorinecalculation.CHLORINECALCULATION.DEMANDACTIVECHLORINE, newChlorineCalculation.getDemandActiveChlorine())
					.set(Chlorinecalculation.CHLORINECALCULATION.DEMANDCOMMONPRODUCT, newChlorineCalculation.getDemandCommonProduct())
					.where(Chlorinecalculation.CHLORINECALCULATION.DATE.eq(oldChlorineCalculationDate))
					.and(Chlorinecalculation.CHLORINECALCULATION.WATERSYSTEM_IDWATERSYSTEM.eq(newChlorineCalculation.getWaterSystem().getWaterSystemId()))
					.and(Chlorinecalculation.CHLORINECALCULATION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(newChlorineCalculation.getWaterSystem().getCommunity().getCommunityId()))
					.and(Chlorinecalculation.CHLORINECALCULATION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(newChlorineCalculation.getWaterSystem().getCommunity().getSubBasin().getSubBasinId()))
					.execute();
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return result>0;
	}
	
	@Override
	public boolean removeChlorineCalculation(ChlorineCalculation chlorineCalculation) {
		if(chlorineCalculation == null)
			throw new NullArgumentException("chlorineCalculation");
		int result = 0;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext delete = this.prepareDSLContext(this.connection);
			result = delete.delete(Chlorinecalculation.CHLORINECALCULATION)
					.where(Chlorinecalculation.CHLORINECALCULATION.DATE.eq(chlorineCalculation.getDate()))
					.and(Chlorinecalculation.CHLORINECALCULATION.WATERSYSTEM_IDWATERSYSTEM.eq(chlorineCalculation.getWaterSystem().getWaterSystemId()))
					.and(Chlorinecalculation.CHLORINECALCULATION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(chlorineCalculation.getWaterSystem().getCommunity().getCommunityId()))
					.and(Chlorinecalculation.CHLORINECALCULATION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(chlorineCalculation.getWaterSystem().getCommunity().getSubBasin().getSubBasinId()))
					.execute();
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return result>0;
	}

	@Override
	public CubicReservoir addCubicReservoir(CubicReservoir cubicReservoir) {
		if(cubicReservoir == null)
			throw new NullArgumentException("cubicReservoir");
		CubicReservoir newCubicReservoir = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext insert = this.prepareDSLContext(this.connection);
			int result = insert.insertInto(Cubicreservoir.CUBICRESERVOIR,
					Cubicreservoir.CUBICRESERVOIR.NAME,
					Cubicreservoir.CUBICRESERVOIR.WIDTH,
					Cubicreservoir.CUBICRESERVOIR.LENGTH,
					Cubicreservoir.CUBICRESERVOIR.HEIGHT,
					Cubicreservoir.CUBICRESERVOIR.COUNT,
					Cubicreservoir.CUBICRESERVOIR.REQUIREDCONCENTRATION)
					.values(cubicReservoir.getName(),
							cubicReservoir.getWidth(),
							cubicReservoir.getLength(),
							cubicReservoir.getHeight(),
							cubicReservoir.getCount(),
							cubicReservoir.getRequiredConcentration())
					.execute();
			this.closeConnection();
			
			if(result>0)
				newCubicReservoir = this.getCubicReservoir(cubicReservoir.getWaterSystem(), this.getMaxValue(Cubicreservoir.CUBICRESERVOIR, Cubicreservoir.CUBICRESERVOIR.IDCUBICRESERVOIR));
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return newCubicReservoir;
	}

	@Override
	public List<CubicReservoir> getCubicReservoirs(WaterSystem waterSystem) {
		if(waterSystem == null)
			throw new NullArgumentException("waterSystem");
		List<CubicReservoir> cubicReservoirs = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			cubicReservoirs = new ArrayList<CubicReservoir>();
			DSLContext select = this.prepareDSLContext(this.connection);
			Result<Record> result = select.select()
					.from(Cubicreservoir.CUBICRESERVOIR)
					.where(Cubicreservoir.CUBICRESERVOIR.WATERSYSTEM_IDWATERSYSTEM.eq(waterSystem.getWaterSystemId()))
					.and(Cubicreservoir.CUBICRESERVOIR.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(waterSystem.getCommunity().getCommunityId()))
					.and(Cubicreservoir.CUBICRESERVOIR.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(waterSystem.getCommunity().getSubBasin().getSubBasinId()))
					.fetch();
			
			CubicReservoir cubicReservoir = null;
			for(Record record : result){
				cubicReservoir = this.readCubicReservoir(record, waterSystem);
				cubicReservoirs.add(cubicReservoir);
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return cubicReservoirs;
	}

	@Override
	public CubicReservoir getCubicReservoir(WaterSystem waterSystem, int cubicReservoirId) {
		if(waterSystem == null)
			throw new NullArgumentException("waterSystem");
		CubicReservoir cubicReservoir = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext select = this.prepareDSLContext(this.connection);
			Result<Record> result = select.select()
					.from(Cubicreservoir.CUBICRESERVOIR)
					.where(Cubicreservoir.CUBICRESERVOIR.IDCUBICRESERVOIR.eq(cubicReservoirId))
					.and(Cubicreservoir.CUBICRESERVOIR.WATERSYSTEM_IDWATERSYSTEM.eq(waterSystem.getWaterSystemId()))
					.and(Cubicreservoir.CUBICRESERVOIR.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(waterSystem.getCommunity().getCommunityId()))
					.and(Cubicreservoir.CUBICRESERVOIR.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(waterSystem.getCommunity().getSubBasin().getSubBasinId()))
					.limit(1)
					.fetch();
			
			for(Record record : result){
				cubicReservoir = this.readCubicReservoir(record, waterSystem);
				break;
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return cubicReservoir;
	}
	
	@Override
	public boolean editCubicReservoir(CubicReservoir cubicReservoir) {
		if(cubicReservoir == null)
			throw new NullArgumentException("cubicReservoir");
		int result = 0;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext update = this.prepareDSLContext(this.connection);
			result = update.update(Cubicreservoir.CUBICRESERVOIR)
					.set(Cubicreservoir.CUBICRESERVOIR.NAME, cubicReservoir.getName())
					.set(Cubicreservoir.CUBICRESERVOIR.WIDTH, cubicReservoir.getWidth())
					.set(Cubicreservoir.CUBICRESERVOIR.LENGTH, cubicReservoir.getLength())
					.set(Cubicreservoir.CUBICRESERVOIR.HEIGHT, cubicReservoir.getHeight())
					.set(Cubicreservoir.CUBICRESERVOIR.COUNT, cubicReservoir.getCount())
					.set(Cubicreservoir.CUBICRESERVOIR.REQUIREDCONCENTRATION, cubicReservoir.getRequiredConcentration())
					.where(Cubicreservoir.CUBICRESERVOIR.IDCUBICRESERVOIR.eq(cubicReservoir.getCubbicReservoirId()))
					.and(Cubicreservoir.CUBICRESERVOIR.WATERSYSTEM_IDWATERSYSTEM.eq(cubicReservoir.getWaterSystem().getWaterSystemId()))
					.and(Cubicreservoir.CUBICRESERVOIR.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(cubicReservoir.getWaterSystem().getCommunity().getCommunityId()))
					.and(Cubicreservoir.CUBICRESERVOIR.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(cubicReservoir.getWaterSystem().getCommunity().getSubBasin().getSubBasinId()))
					.execute();
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return result>0;
	}

	@Override
	public boolean removeCubicReservoir(CubicReservoir cubicReservoir) {
		if(cubicReservoir == null)
			throw new NullArgumentException("cubicReservoir");
		int result = 0;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext delete = this.prepareDSLContext(this.connection);
			result = delete.delete(Cubicreservoir.CUBICRESERVOIR)
					.where(Cubicreservoir.CUBICRESERVOIR.IDCUBICRESERVOIR.eq(cubicReservoir.getCubbicReservoirId()))
					.and(Cubicreservoir.CUBICRESERVOIR.WATERSYSTEM_IDWATERSYSTEM.eq(cubicReservoir.getWaterSystem().getWaterSystemId()))
					.and(Cubicreservoir.CUBICRESERVOIR.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(cubicReservoir.getWaterSystem().getCommunity().getCommunityId()))
					.and(Cubicreservoir.CUBICRESERVOIR.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(cubicReservoir.getWaterSystem().getCommunity().getSubBasin().getSubBasinId()))
					.execute();
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return result>0;
	}

	@Override
	public Pipe addPipe(Pipe pipe) {
		if(pipe == null)
			throw new NullArgumentException("pipe");
		Pipe newPipe = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext insert = this.prepareDSLContext(this.connection);
			int result = insert.insertInto(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.NAME,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.DIAMETER,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.LENGTH,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.COUNT,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.REQUIREDCONCENTRATION,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.WATERSYSTEM_IDWATERSYSTEM,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.WATERSYSTEM_COMMUNITY_IDCOMMUNITY,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN)
					.values(pipe.getName(),
							pipe.getDiameter(),
							pipe.getLength(),
							pipe.getCount(),
							pipe.getRequiredConcentration(),
							pipe.getWaterSystem().getWaterSystemId(),
							pipe.getWaterSystem().getCommunity().getCommunityId(),
							pipe.getWaterSystem().getCommunity().getSubBasin().getSubBasinId())
					.execute();
			this.closeConnection();
			if(result>0)
				newPipe = this.getPipe(pipe.getWaterSystem(), this.getMaxValue(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE, org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.IDPIPE));
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return newPipe;
	}

	@Override
	public List<Pipe> getPipes(WaterSystem waterSystem) {
		if(waterSystem == null)
			throw new NullArgumentException("waterSystem");
		List<Pipe> pipes = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			pipes = new ArrayList<Pipe>();
			DSLContext select = this.prepareDSLContext(this.connection);
			Result<Record> result = select.select().from(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE)
					.where(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.WATERSYSTEM_IDWATERSYSTEM.eq(waterSystem.getWaterSystemId()))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(waterSystem.getCommunity().getCommunityId()))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(waterSystem.getCommunity().getSubBasin().getSubBasinId()))
					.fetch();
			
			Pipe pipe = null;
			for(Record record : result){
				pipe = this.readPipe(record, waterSystem);
				pipes.add(pipe);
			}
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return pipes;
	}

	@Override
	public Pipe getPipe(WaterSystem waterSystem, int pipeId) {
		if(waterSystem == null)
			throw new NullArgumentException("waterSystem");
		Pipe pipe = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext select = this.prepareDSLContext(this.connection);
			Result<Record> result = select.select().from(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE)
					.where(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.IDPIPE.eq(pipeId))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.WATERSYSTEM_IDWATERSYSTEM.eq(waterSystem.getWaterSystemId()))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(waterSystem.getCommunity().getCommunityId()))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(waterSystem.getCommunity().getSubBasin().getSubBasinId()))
					.limit(1)
					.fetch();
			
			for(Record record : result){
				pipe = this.readPipe(record, waterSystem);
				break;
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return pipe;
	}
	
	@Override
	public boolean editPipe(Pipe pipe) {
		if(pipe == null)
			throw new NullArgumentException("pipe");
		int result = 0;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext update = this.prepareDSLContext(this.connection);
			result = update.update(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE)
					.set(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.NAME, pipe.getName())
					.set(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.DIAMETER, pipe.getDiameter())
					.set(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.LENGTH, pipe.getLength())
					.set(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.COUNT, pipe.getCount())
					.set(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.REQUIREDCONCENTRATION, pipe.getRequiredConcentration())
					.where(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.IDPIPE.eq(pipe.getPipeId()))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.WATERSYSTEM_IDWATERSYSTEM.eq(pipe.getWaterSystem().getWaterSystemId()))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(pipe.getWaterSystem().getCommunity().getCommunityId()))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(pipe.getWaterSystem().getCommunity().getSubBasin().getSubBasinId()))
					.execute();
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return result>0;
	}

	@Override
	public boolean removePipe(Pipe pipe) {
		if(pipe == null)
			throw new NullArgumentException("pipe");
		int result = 0;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext delete = this.prepareDSLContext(this.connection);
			result = delete.delete(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE)
					.where(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.IDPIPE.eq(pipe.getPipeId()))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.WATERSYSTEM_IDWATERSYSTEM.eq(pipe.getWaterSystem().getWaterSystemId()))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(pipe.getWaterSystem().getCommunity().getCommunityId()))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(pipe.getWaterSystem().getCommunity().getSubBasin().getSubBasinId()))
					.execute();
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return result>0;
	}

	@Override
	public ReliefValve addReliefValve(ReliefValve reliefValve) {
		if(reliefValve == null)
			throw new NullArgumentException("reliefValve");
		ReliefValve newReliefValve = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext insert = this.prepareDSLContext(this.connection);
			int result = insert.insertInto(Reliefvalve.RELIEFVALVE,
					Reliefvalve.RELIEFVALVE.NAME,
					Reliefvalve.RELIEFVALVE.WIDTH,
					Reliefvalve.RELIEFVALVE.LENGTH,
					Reliefvalve.RELIEFVALVE.HEIGHT,
					Reliefvalve.RELIEFVALVE.COUNT,
					Reliefvalve.RELIEFVALVE.REQUIREDCONCENTRATION,
					Reliefvalve.RELIEFVALVE.WATERSYSTEM_IDWATERSYSTEM,
					Reliefvalve.RELIEFVALVE.WATERSYSTEM_COMMUNITY_IDCOMMUNITY,
					Reliefvalve.RELIEFVALVE.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN)
					.values(reliefValve.getName(),
							reliefValve.getWidth(),
							reliefValve.getLength(),
							reliefValve.getHeight(),
							reliefValve.getCount(),
							reliefValve.getRequiredConcentration(),
							reliefValve.getWaterSystem().getWaterSystemId(),
							reliefValve.getWaterSystem().getCommunity().getCommunityId(),
							reliefValve.getWaterSystem().getCommunity().getSubBasin().getSubBasinId())
					.execute();
			this.closeConnection();
			
			if(result>0)
				newReliefValve = this.getReliefValve(reliefValve.getWaterSystem(), this.getMaxValue(Reliefvalve.RELIEFVALVE, Reliefvalve.RELIEFVALVE.IDRELIEFVALVE));
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return newReliefValve;
	}

	@Override
	public List<ReliefValve> getReliefValves(WaterSystem waterSystem) {
		if(waterSystem == null)
			throw new NullArgumentException("waterSystem");
		List<ReliefValve> reliefValves = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			reliefValves = new ArrayList<ReliefValve>();
			DSLContext select = this.prepareDSLContext(this.connection);
			Result<Record> result = select.select().from(Reliefvalve.RELIEFVALVE)
					.where(Reliefvalve.RELIEFVALVE.WATERSYSTEM_IDWATERSYSTEM.eq(waterSystem.getWaterSystemId()))
					.and(Reliefvalve.RELIEFVALVE.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(waterSystem.getCommunity().getCommunityId()))
					.and(Reliefvalve.RELIEFVALVE.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(waterSystem.getCommunity().getSubBasin().getSubBasinId()))
					.fetch();
			
			ReliefValve reliefValve = null;
			for(Record record : result){
				reliefValve = this.readReliefValve(record, waterSystem);
				reliefValves.add(reliefValve);
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return reliefValves;
	}

	@Override
	public ReliefValve getReliefValve(WaterSystem waterSystem, int reliefValveId) {
		if(waterSystem == null)
			throw new NullArgumentException("waterSystem");
		ReliefValve reliefValve = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext select = this.prepareDSLContext(this.connection);
			Result<Record> result = select.select().from(Reliefvalve.RELIEFVALVE)
					.where(Reliefvalve.RELIEFVALVE.IDRELIEFVALVE.eq(reliefValveId))
					.and(Reliefvalve.RELIEFVALVE.WATERSYSTEM_IDWATERSYSTEM.eq(waterSystem.getWaterSystemId()))
					.and(Reliefvalve.RELIEFVALVE.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(waterSystem.getCommunity().getCommunityId()))
					.and(Reliefvalve.RELIEFVALVE.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(waterSystem.getCommunity().getSubBasin().getSubBasinId()))
					.limit(1)
					.fetch();
			
			for(Record record : result){
				reliefValve = this.readReliefValve(record, waterSystem);
				break;
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return reliefValve;
	}
	
	@Override
	public boolean editReliefValve(ReliefValve reliefValve) {
		if(reliefValve == null)
			throw new NullArgumentException("reliefValve");
		int result = 0;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext update = this.prepareDSLContext(this.connection);
			result = update.update(Reliefvalve.RELIEFVALVE)
					.set(Reliefvalve.RELIEFVALVE.NAME, reliefValve.getName())
					.set(Reliefvalve.RELIEFVALVE.WIDTH, reliefValve.getWidth())
					.set(Reliefvalve.RELIEFVALVE.LENGTH, reliefValve.getLength())
					.set(Reliefvalve.RELIEFVALVE.HEIGHT, reliefValve.getHeight())
					.set(Reliefvalve.RELIEFVALVE.COUNT, reliefValve.getCount())
					.set(Reliefvalve.RELIEFVALVE.REQUIREDCONCENTRATION, reliefValve.getRequiredConcentration())
					.where(Reliefvalve.RELIEFVALVE.IDRELIEFVALVE.eq(reliefValve.getReliefValveId()))
					.and(Reliefvalve.RELIEFVALVE.WATERSYSTEM_IDWATERSYSTEM.eq(reliefValve.getWaterSystem().getWaterSystemId()))
					.and(Reliefvalve.RELIEFVALVE.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(reliefValve.getWaterSystem().getCommunity().getCommunityId()))
					.and(Reliefvalve.RELIEFVALVE.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(reliefValve.getWaterSystem().getCommunity().getSubBasin().getSubBasinId()))
					.execute();
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return result>0;
	}

	@Override
	public boolean removeReliefValve(ReliefValve reliefValve) {
		if(reliefValve == null)
			throw new NullArgumentException("reliefValve");
		int result = 0;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext delete = this.prepareDSLContext(this.connection);
			result = delete.delete(Reliefvalve.RELIEFVALVE)
					.where(Reliefvalve.RELIEFVALVE.IDRELIEFVALVE.eq(reliefValve.getReliefValveId()))
					.and(Reliefvalve.RELIEFVALVE.WATERSYSTEM_IDWATERSYSTEM.eq(reliefValve.getWaterSystem().getWaterSystemId()))
					.and(Reliefvalve.RELIEFVALVE.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(reliefValve.getWaterSystem().getCommunity().getCommunityId()))
					.and(Reliefvalve.RELIEFVALVE.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(reliefValve.getWaterSystem().getCommunity().getSubBasin().getSubBasinId()))
					.execute();
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return result>0;
	}

	@Override
	public boolean createInitialEnvironment() {
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext insert = this.prepareDSLContext(this.connection);
			Properties properties = ApplicationProperties.getInstance().getProperties();
			Path path  = FileSystems.getDefault().getPath(properties.getProperty(KEYS.RESOURCES_PATH), properties.getProperty(KEYS.DATABASE_CREATION_SCRIPT));
			String script = StringUtils.join(Files.readAllLines(path), "\n");
			insert.execute(script);
			log.info("Database created");
			properties.setProperty(KEYS.APP_FIRST_RUN, "false");
			ApplicationProperties.getInstance().storeProperties();
			properties = ApplicationProperties.getInstance().getProperties();
		} catch (SQLException | IOException e) {
			log.warn(e.toString());
		}
		
		return true;
	}
}
