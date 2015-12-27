package org.ongawa.peru.chlorination.persistence.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.NullArgumentException;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record2;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.ongawa.peru.chlorination.persistence.IDataSource;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Chlorinecalculation;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Measuredflow;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Measuringpoint;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Subbasin;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Waterspring;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Watersystem;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.WatersystemHasWaterspring;
import org.ongawa.peru.chlorination.persistence.elements.ChlorineCalculation;
import org.ongawa.peru.chlorination.persistence.elements.Community;
import org.ongawa.peru.chlorination.persistence.elements.MeasuredFlow;
import org.ongawa.peru.chlorination.persistence.elements.MeasuringPoint;
import org.ongawa.peru.chlorination.persistence.elements.SubBasin;
import org.ongawa.peru.chlorination.persistence.elements.WaterSpring;
import org.ongawa.peru.chlorination.persistence.elements.WaterSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataSource implements IDataSource {
	/**
	 * @author Kiko
	 */

	private static IDataSource datasource;
	private static Logger log;
	static{
		datasource = null;
		log = LoggerFactory.getLogger(DataSource.class);
	}
	
	public static IDataSource getInstance(){
		if(datasource == null)
			datasource = new DataSource();
		
		return datasource;
	}
	
	private Connection connection;
	
	private DataSource(){
	}
	
	private DSLContext prepareDSLContext(Connection connection){
		return DSL.using(connection, SQLDialect.H2);
	}
	
	private void closeConnection(){
		try {
			this.connection.close();
		} catch (SQLException e) {
			log.warn(e.toString());
		}
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
	
	/* (non-Javadoc)
	 * @see org.ongawa.peru.chlorination.persistence.db.IDataSource#getSubBasins()
	 */
	@Override
	public List<SubBasin> getSubBasins(){
		List<SubBasin> subBasins = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			subBasins = new ArrayList<SubBasin>();
			
			DSLContext create = this.prepareDSLContext(this.connection);
			Result<Record> result = create.select().from(Subbasin.SUBBASIN).fetch();
			SubBasin subBasin;
			
			for(Record record : result){
				subBasin = this.readSubBasin(record);
				subBasins.add(subBasin);
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.toString());
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
			DSLContext create = this.prepareDSLContext(this.connection);
			Result<Record> result = create.select().from(Subbasin.SUBBASIN).where(Subbasin.SUBBASIN.IDSUBBASIN.eq(idSubBasin)).limit(1).fetch();
			for(Record record : result){
				subBasin = this.readSubBasin(record);
				break;
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.toString());
		}
		
		return subBasin;
	}
	
	/* (non-Javadoc)
	 * @see org.ongawa.peru.chlorination.persistence.db.IDataSource#getSubBasin(java.lang.String)
	 */
	@Override
	public SubBasin getSubBasin(String name){
		SubBasin subBasin = null;
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext create = this.prepareDSLContext(this.connection);
			Result<Record> result = create.select().from(Subbasin.SUBBASIN).where(Subbasin.SUBBASIN.NAME.eq(name)).limit(1).fetch();
			for(Record record : result){
				subBasin = this.readSubBasin(record);
				break;
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.toString());
		}
		
		return subBasin;
	}
	
	/* (non-Javadoc)
	 * @see org.ongawa.peru.chlorination.persistence.db.IDataSource#getCommunities(org.ongawa.peru.chlorination.persistence.elements.SubBasin)
	 */
	@Override
	public List<Community> getCommunities(SubBasin subBasin){
		List<Community> communities = null;
		if(subBasin==null)
			throw new NullArgumentException("subBasin");
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			communities = new ArrayList<Community>();
			
			DSLContext create = this.prepareDSLContext(this.connection);
			Result<Record> result = create.select()
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
			log.warn(e.toString());
		}
		
		return communities;
	}
	
	/* (non-Javadoc)
	 * @see org.ongawa.peru.chlorination.persistence.db.IDataSource#getCommunity(org.ongawa.peru.chlorination.persistence.elements.SubBasin, int)
	 */
	@Override
	public Community getCommunity(SubBasin subBasin, int idCommunity){
		Community community = null;
		try {
			if(subBasin==null)
				throw new NullArgumentException("subBasin");
			
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext create = this.prepareDSLContext(this.connection);
			Result<Record> result = create.select().from(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Community.COMMUNITY)
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
			log.warn(e.toString());
		}
		return community;
	}
	
	/* (non-Javadoc)
	 * @see org.ongawa.peru.chlorination.persistence.db.IDataSource#getCommunity(org.ongawa.peru.chlorination.persistence.elements.SubBasin, java.lang.String)
	 */
	@Override
	public Community getCommunity(SubBasin subBasin, String name){
		Community community = null;
		try {
			if(subBasin==null)
				throw new NullArgumentException("subBasin");
			
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext create = this.prepareDSLContext(this.connection);
			Result<Record> result = create.select().from(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Community.COMMUNITY)
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
			log.warn(e.toString());
		}
		return community;
	}
	
	/* (non-Javadoc)
	 * @see org.ongawa.peru.chlorination.persistence.db.IDataSource#getWaterSystems(org.ongawa.peru.chlorination.persistence.elements.Community)
	 */
	@Override
	public List<WaterSystem> getWaterSystems(Community community){
		List<WaterSystem> waterSystems = null;
		if(community == null)
			throw new NullArgumentException("community");
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			waterSystems = new ArrayList<WaterSystem>();
			
			DSLContext create = this.prepareDSLContext(this.connection);
			Result<Record> result = create.select().from(Watersystem.WATERSYSTEM)
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
			log.warn(e.toString());
		}
		return waterSystems;
	}
	
	/* (non-Javadoc)
	 * @see org.ongawa.peru.chlorination.persistence.db.IDataSource#getWaterSystem(org.ongawa.peru.chlorination.persistence.elements.Community, int)
	 */
	@Override
	public WaterSystem getWaterSystem(Community community, int idWaterSystem){
		WaterSystem waterSystem = null;
		if(community == null)
			throw new NullArgumentException("community");
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext create = this.prepareDSLContext(this.connection);
			Result<Record> result = create.select().from(Watersystem.WATERSYSTEM)
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
			log.warn(e.toString());
		}
		
		return waterSystem;
	}
	
	/* (non-Javadoc)
	 * @see org.ongawa.peru.chlorination.persistence.db.IDataSource#getWaterSystem(org.ongawa.peru.chlorination.persistence.elements.Community, java.lang.String)
	 */
	@Override
	public WaterSystem getWaterSystem(Community community, String name){
		WaterSystem waterSystem = null;
		if(community == null)
			throw new NullArgumentException("community");
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext create = this.prepareDSLContext(this.connection);
			Result<Record> result = create.select().from(Watersystem.WATERSYSTEM)
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
			log.warn(e.toString());
		}
		
		return waterSystem;
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
			DSLContext create = this.prepareDSLContext(this.connection);
			Result<Record> result = create.select().from(Waterspring.WATERSPRING).fetch();
			
			WaterSpring waterSpring;
			for(Record record : result){
				waterSpring = this.readWaterSpring(record);
				waterSprings.add(waterSpring);
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.toString());
		}
		
		return waterSprings;
	}
	
	/* (non-Javadoc)
	 * @see org.ongawa.peru.chlorination.persistence.db.IDataSource#getWaterSprings(int)
	 */
	@Override
	public WaterSpring getWaterSprings(int idWaterSpring){
		WaterSpring waterSpring = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext create = this.prepareDSLContext(this.connection);
			Result<Record> result = create.select().from(Waterspring.WATERSPRING)
					.where(Waterspring.WATERSPRING.IDWATERSPRING.eq(idWaterSpring))
					.limit(1)
					.fetch();
			
			for(Record record : result){
				waterSpring = this.readWaterSpring(record);
				break;
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.toString());
		}
		
		return waterSpring;
	}
	
	/* (non-Javadoc)
	 * @see org.ongawa.peru.chlorination.persistence.db.IDataSource#getWaterSprings(java.lang.String)
	 */
	@Override
	public WaterSpring getWaterSprings(String name){
		WaterSpring waterSpring = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext create = this.prepareDSLContext(this.connection);
			Result<Record> result = create.select().from(Waterspring.WATERSPRING)
					.where(Waterspring.WATERSPRING.NAME.eq(name))
					.limit(1)
					.fetch();
			
			for(Record record : result){
				waterSpring = this.readWaterSpring(record);
				break;
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.toString());
		}
		
		return waterSpring;
	}
	
	/* (non-Javadoc)
	 * @see org.ongawa.peru.chlorination.persistence.db.IDataSource#getWaterSprings(org.ongawa.peru.chlorination.persistence.elements.WaterSystem)
	 */
	@Override
	public List<WaterSpring> getWaterSprings(WaterSystem waterSystem){
		List<WaterSpring> waterSprings = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			waterSprings = new ArrayList<WaterSpring>();
			DSLContext create = this.prepareDSLContext(this.connection);
			Result<Record2<Integer, String>> result = create.select(Waterspring.WATERSPRING.IDWATERSPRING, Waterspring.WATERSPRING.NAME)
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
			log.warn(e.toString());
		}
		
		return waterSprings;
	}
	
	/* (non-Javadoc)
	 * @see org.ongawa.peru.chlorination.persistence.db.IDataSource#getMeasuringPoints(org.ongawa.peru.chlorination.persistence.elements.WaterSystem, org.ongawa.peru.chlorination.persistence.elements.WaterSpring)
	 */
	@Override
	public List<MeasuringPoint> getMeasuringPoints(WaterSystem waterSystem, WaterSpring waterSpring){
		List<MeasuringPoint> measuringPoints = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			measuringPoints = new ArrayList<MeasuringPoint>();
			DSLContext create = this.prepareDSLContext(this.connection);
			Result<Record> result = create.select().from(Measuringpoint.MEASURINGPOINT)
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
			log.warn(e.toString());
		}
		
		return measuringPoints;
	}
	
	/* (non-Javadoc)
	 * @see org.ongawa.peru.chlorination.persistence.db.IDataSource#getMeasuringPoint(org.ongawa.peru.chlorination.persistence.elements.WaterSystem, org.ongawa.peru.chlorination.persistence.elements.WaterSpring, int)
	 */
	@Override
	public MeasuringPoint getMeasuringPoint(WaterSystem waterSystem, WaterSpring waterSpring, int idMeasuringPoint){
		MeasuringPoint measuringPoint = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext create  = this.prepareDSLContext(this.connection);
			Result<Record> result = create.select().from(Measuringpoint.MEASURINGPOINT)
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
			log.warn(e.toString());
		}
		
		return measuringPoint;
	}
	
	/* (non-Javadoc)
	 * @see org.ongawa.peru.chlorination.persistence.db.IDataSource#getMeasuredFlows(org.ongawa.peru.chlorination.persistence.elements.MeasuringPoint)
	 */
	@Override
	public List<MeasuredFlow> getMeasuredFlows(MeasuringPoint measuringPoint){
		List<MeasuredFlow> measuredFlows = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			measuredFlows = new ArrayList<MeasuredFlow>();
			DSLContext create = this.prepareDSLContext(this.connection);
			Result<Record> result = create.select().from(Measuredflow.MEASUREDFLOW)
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
			log.warn(e.toString());
		}
		
		return measuredFlows;
	}
	
	/* (non-Javadoc)
	 * @see org.ongawa.peru.chlorination.persistence.db.IDataSource#getMeasuredFlows(java.sql.Timestamp, java.sql.Timestamp, org.ongawa.peru.chlorination.persistence.elements.MeasuringPoint)
	 */
	@Override
	public List<MeasuredFlow> getMeasuredFlows(Timestamp beginDate, Timestamp endDate, MeasuringPoint measuringPoint){
		List<MeasuredFlow> measuredFlows = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			measuredFlows = new ArrayList<MeasuredFlow>();
			DSLContext create = this.prepareDSLContext(this.connection);
			Result<Record> result = create.select().from(Measuredflow.MEASUREDFLOW)
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
			log.warn(e.toString());
		}
		
		return measuredFlows;
	}
	
	/* (non-Javadoc)
	 * @see org.ongawa.peru.chlorination.persistence.db.IDataSource#getMeasuredFlow(java.sql.Timestamp, org.ongawa.peru.chlorination.persistence.elements.MeasuringPoint)
	 */
	@Override
	public MeasuredFlow getMeasuredFlow(Timestamp date, MeasuringPoint measuringPoint){
		MeasuredFlow measuredFlow = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext create = this.prepareDSLContext(this.connection);
			Result<Record> result = create.select().from(Measuredflow.MEASUREDFLOW)
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
			log.warn(e.toString());
		}
		
		return measuredFlow;
	}
	
	/* (non-Javadoc)
	 * @see org.ongawa.peru.chlorination.persistence.db.IDataSource#getChlorineCalculations(org.ongawa.peru.chlorination.persistence.elements.WaterSystem)
	 */
	@Override
	public List<ChlorineCalculation> getChlorineCalculations(WaterSystem waterSystem){
		List<ChlorineCalculation> chlorineCalculations = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			chlorineCalculations = new ArrayList<ChlorineCalculation>();
			DSLContext create = this.prepareDSLContext(this.connection);
			Result<Record> result = create.select().from(Chlorinecalculation.CHLORINECALCULATION)
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
			log.warn(e.toString());
		}
		
		return chlorineCalculations;
	}
	
	/* (non-Javadoc)
	 * @see org.ongawa.peru.chlorination.persistence.db.IDataSource#getChlorineCalculations(java.sql.Timestamp, java.sql.Timestamp, org.ongawa.peru.chlorination.persistence.elements.WaterSystem)
	 */
	@Override
	public List<ChlorineCalculation> getChlorineCalculations(Timestamp beginDate, Timestamp endDate, WaterSystem waterSystem){
		List<ChlorineCalculation> chlorineCalculations = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			chlorineCalculations = new ArrayList<ChlorineCalculation>();
			DSLContext create = this.prepareDSLContext(this.connection);
			Result<Record> result = create.select().from(Chlorinecalculation.CHLORINECALCULATION)
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
			log.warn(e.toString());
		}
		
		return chlorineCalculations;
	}
	
	/* (non-Javadoc)
	 * @see org.ongawa.peru.chlorination.persistence.db.IDataSource#getChlorineCalculation(java.sql.Timestamp, org.ongawa.peru.chlorination.persistence.elements.WaterSystem)
	 */
	@Override
	public ChlorineCalculation getChlorineCalculation(Timestamp date, WaterSystem waterSystem){
		ChlorineCalculation chlorineCalculation = null;
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext create = this.prepareDSLContext(this.connection);
			Result<Record> result = create.select().from(Chlorinecalculation.CHLORINECALCULATION)
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
			log.warn(e.toString());
		}
		
		return chlorineCalculation;
	}
}
