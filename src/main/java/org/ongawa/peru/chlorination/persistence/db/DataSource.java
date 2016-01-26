package org.ongawa.peru.chlorination.persistence.db;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.Date;
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
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.ongawa.peru.chlorination.ApplicationProperties;
import org.ongawa.peru.chlorination.KEYS;
import org.ongawa.peru.chlorination.persistence.IDataSource;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchmentdesinfection;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Chlorinecalculation;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Cubicreservoir;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Cubicreservoirdesinfection;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Measuredflow;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Measuringpoint;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipedesinfection;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Reliefvalve;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Reliefvalvedesinfection;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Subbasin;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Waterspring;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Watersystem;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.WatersystemHasWaterspring;
import org.ongawa.peru.chlorination.persistence.elements.Backup;
import org.ongawa.peru.chlorination.persistence.elements.Catchment;
import org.ongawa.peru.chlorination.persistence.elements.CatchmentDesinfection;
import org.ongawa.peru.chlorination.persistence.elements.ChlorineCalculation;
import org.ongawa.peru.chlorination.persistence.elements.Community;
import org.ongawa.peru.chlorination.persistence.elements.ConductionPipe;
import org.ongawa.peru.chlorination.persistence.elements.CubicReservoir;
import org.ongawa.peru.chlorination.persistence.elements.CubicReservoirDesinfection;
import org.ongawa.peru.chlorination.persistence.elements.Desinfection;
import org.ongawa.peru.chlorination.persistence.elements.DistributionPipe;
import org.ongawa.peru.chlorination.persistence.elements.MeasuredFlow;
import org.ongawa.peru.chlorination.persistence.elements.MeasuringPoint;
import org.ongawa.peru.chlorination.persistence.elements.Pipe;
import org.ongawa.peru.chlorination.persistence.elements.PipeDesinfection;
import org.ongawa.peru.chlorination.persistence.elements.ReliefValve;
import org.ongawa.peru.chlorination.persistence.elements.ReliefValveDesinfection;
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
		if((dodo=record.getValue(Watersystem.WATERSYSTEM.POPULATIONFORECAST))!=null) waterSystem.setPopulationForecast(integer);
		if((dodo=record.getValue(Watersystem.WATERSYSTEM.GROWINGINDEX))!=null) waterSystem.setGrowingIndex(dodo);
		if((dodo=record.getValue(Watersystem.WATERSYSTEM.ENDOWMENT))!=null) waterSystem.setEndowment(dodo);
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
				record.getValue(Chlorinecalculation.CHLORINECALCULATION.DATE), waterSystem);
		
		Integer integer; Double dodo; String string;
		if((integer=record.getValue(Chlorinecalculation.CHLORINECALCULATION.FAMILIESNUM))!=null) chlorineCalculation.setFamiliesNum(integer);
		if((integer=record.getValue(Chlorinecalculation.CHLORINECALCULATION.POPULATION))!=null) chlorineCalculation.setPopulation(integer);
		if((dodo=record.getValue(Chlorinecalculation.CHLORINECALCULATION.TANKVOLUME))!=null) chlorineCalculation.setTankVolume(dodo);
		if((dodo=record.getValue(Chlorinecalculation.CHLORINECALCULATION.ENDOWMENT))!=null) chlorineCalculation.setEndowment(dodo);
		if((dodo=record.getValue(Chlorinecalculation.CHLORINECALCULATION.CHLORINEPURENESS))!=null) chlorineCalculation.setChlorinePureness(dodo);
		if((dodo=record.getValue(Chlorinecalculation.CHLORINECALCULATION.NATURALFLOW))!=null) chlorineCalculation.setNaturalFlow(dodo);
		if((dodo=record.getValue(Chlorinecalculation.CHLORINECALCULATION.CHLORINATEDFLOW))!=null) chlorineCalculation.setChlorinatedFlow(dodo);
		if((string=record.getValue(Chlorinecalculation.CHLORINECALCULATION.CHLORINETYPE))!=null) chlorineCalculation.setChlorineType(string);
		if((dodo=record.getValue(Chlorinecalculation.CHLORINECALCULATION.DRIPPINGHOURSPERDAY))!=null) chlorineCalculation.setDrippingHoursPerDay(dodo);
		if((dodo=record.getValue(Chlorinecalculation.CHLORINECALCULATION.CHLORINEDEMAND))!=null) chlorineCalculation.setChlorineDemand(dodo);
		if((dodo=record.getValue(Chlorinecalculation.CHLORINECALCULATION.CHLORINEPRICE))!=null) chlorineCalculation.setChlorinePrice(dodo);
		if((dodo=record.getValue(Chlorinecalculation.CHLORINECALCULATION.RELOADTIME))!=null) chlorineCalculation.setReloadTime(dodo);
		if((dodo=record.getValue(Chlorinecalculation.CHLORINECALCULATION.CHLORINEDOSEPERFORTNIGHT))!=null) chlorineCalculation.setChlorineDosePerFortnight(dodo);
		if((dodo=record.getValue(Chlorinecalculation.CHLORINECALCULATION.CHLORINEDOSEPERMONTH))!=null) chlorineCalculation.setChlorineDosePerMonth(dodo);
		if((dodo=record.getValue(Chlorinecalculation.CHLORINECALCULATION.DRIPPINGFLOWINML))!=null) chlorineCalculation.setDrippingFlowInMl(dodo);
		if((dodo=record.getValue(Chlorinecalculation.CHLORINECALCULATION.DRIPPINGFLOWINDROPS))!=null) chlorineCalculation.setDrippingFlowInDrops(dodo);
		if((dodo=record.getValue(Chlorinecalculation.CHLORINECALCULATION.CHLORINATIONCOST))!=null) chlorineCalculation.setChlorinationCost(dodo);
				
		return chlorineCalculation;
	}
	
	private CubicReservoir readCubicReservoir(Record record, WaterSystem waterSystem){
		CubicReservoir cubicReservoir = new CubicReservoir(
				record.getValue(Cubicreservoir.CUBICRESERVOIR.IDCUBICRESERVOIR),
				record.getValue(Cubicreservoir.CUBICRESERVOIR.WIDTH),
				record.getValue(Cubicreservoir.CUBICRESERVOIR.LENGTH),
				record.getValue(Cubicreservoir.CUBICRESERVOIR.HEIGHT),
				waterSystem);
		
		String string; Integer integer;
		if((string=record.getValue(Cubicreservoir.CUBICRESERVOIR.NAME))!=null) cubicReservoir.setElementName(string);
		if((integer=record.getValue(Cubicreservoir.CUBICRESERVOIR.COUNT))!=null) cubicReservoir.setCount(integer);
		
		return cubicReservoir;
	}
	
	private Catchment readCatchment(Record record, WaterSystem waterSystem){
		Catchment catchment = new Catchment(
				record.getValue(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchment.CATCHMENT.IDCATCHMENT),
				record.getValue(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchment.CATCHMENT.WIDTH),
				record.getValue(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchment.CATCHMENT.LENGTH),
				record.getValue(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchment.CATCHMENT.HEIGHT),
				waterSystem);
		
		String string; Integer integer;
		if((string=record.getValue(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchment.CATCHMENT.NAME))!=null) catchment.setElementName(string);
		if((integer=record.getValue(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchment.CATCHMENT.COUNT))!=null) catchment.setCount(integer);
		
		return catchment;
	}
	
	private DistributionPipe readDistributionPipe(Record record, WaterSystem waterSystem){
		DistributionPipe pipe = new DistributionPipe(
				record.getValue(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.IDPIPE),
				record.getValue(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.DIAMETER),
				record.getValue(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.LENGTH),
				waterSystem);
		
		String string; Integer integer;
		if((string=record.getValue(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.NAME))!=null) pipe.setElementName(string);
		if((integer=record.getValue(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.COUNT))!=null) pipe.setCount(integer);
		
		return pipe;
	}
	
	private ConductionPipe readConductionPipe(Record record, WaterSystem waterSystem){
		ConductionPipe pipe = new ConductionPipe(
				record.getValue(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.IDPIPE),
				record.getValue(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.DIAMETER),
				record.getValue(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.LENGTH),
				waterSystem);
		
		String string; Integer integer;
		if((string=record.getValue(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.NAME))!=null) pipe.setElementName(string);
		if((integer=record.getValue(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.COUNT))!=null) pipe.setCount(integer);
		
		return pipe;
	}
	
	private ReliefValve readReliefValve(Record record, WaterSystem waterSystem){
		ReliefValve reliefValve = new ReliefValve(
				record.getValue(Reliefvalve.RELIEFVALVE.IDRELIEFVALVE),
				record.getValue(Reliefvalve.RELIEFVALVE.WIDTH),
				record.getValue(Reliefvalve.RELIEFVALVE.LENGTH),
				record.getValue(Reliefvalve.RELIEFVALVE.HEIGHT),
				waterSystem);
		
		Integer integer; String string;
		if((string=record.getValue(Reliefvalve.RELIEFVALVE.NAME))!=null) reliefValve.setElementName(string);
		if((integer=record.getValue(Reliefvalve.RELIEFVALVE.COUNT))!=null) reliefValve.setCount(integer);
		
		return reliefValve;
	}
	
	private Desinfection readDesinfection(Record record, WaterSystem waterSystem){
		return new Desinfection(
				waterSystem,
				record.getValue(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Desinfection.DESINFECTION.DATE),
				record.getValue(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Desinfection.DESINFECTION.CHLORINETYPE),
				record.getValue(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Desinfection.DESINFECTION.CHLORINEPURENESS),
				record.getValue(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Desinfection.DESINFECTION.CHLORINEPRICE));
	}
	
	private CubicReservoirDesinfection readCubicReservoirDesinfection(Record record, CubicReservoir cubicReservoir, Desinfection desinfection){
		return new CubicReservoirDesinfection(
				cubicReservoir,
				desinfection,
				record.getValue(Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.COUNT),
				record.getValue(Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.CHLORINEQTY),
				record.getValue(Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.DEMANDSPOONS),
				record.getValue(Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.RETENTIONTIME));
	}
	
	private CatchmentDesinfection readCatchmentDesinfection(Record record, Catchment catchment, Desinfection desinfection){
		return new CatchmentDesinfection(
				catchment,
				desinfection,
				record.getValue(Catchmentdesinfection.CATCHMENTDESINFECTION.COUNT),
				record.getValue(Catchmentdesinfection.CATCHMENTDESINFECTION.CHLORINEQTY),
				record.getValue(Catchmentdesinfection.CATCHMENTDESINFECTION.DEMANDSPOONS),
				record.getValue(Catchmentdesinfection.CATCHMENTDESINFECTION.RETENTIONTIME));
	}
	
	private PipeDesinfection readPipeDesinfection(Record record, Pipe pipe, Desinfection desinfection){
		return new PipeDesinfection(
				pipe,
				desinfection,
				record.getValue(Pipedesinfection.PIPEDESINFECTION.COUNT),
				record.getValue(Pipedesinfection.PIPEDESINFECTION.CHLORINEQTY),
				record.getValue(Pipedesinfection.PIPEDESINFECTION.DEMANDSPOONS),
				record.getValue(Pipedesinfection.PIPEDESINFECTION.RETENTIONTIME));
	}
	
	private ReliefValveDesinfection readReliefValveDesinfection(Record record, ReliefValve reliefValve, Desinfection desinfection){
		return new ReliefValveDesinfection(
				reliefValve,
				desinfection,
				record.getValue(Reliefvalvedesinfection.RELIEFVALVEDESINFECTION.COUNT),
				record.getValue(Reliefvalvedesinfection.RELIEFVALVEDESINFECTION.CHLORINEQTY),
				record.getValue(Reliefvalvedesinfection.RELIEFVALVEDESINFECTION.DEMANDSPOONS),
				record.getValue(Reliefvalvedesinfection.RELIEFVALVEDESINFECTION.RETENTIONTIME));
	}
	
	private Backup readBackup(Record record){
		Backup backup = new Backup(
				record.getValue(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Backup.BACKUP.IDBACKUP),
				record.getValue(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Backup.BACKUP.FILEPATH),
				record.getValue(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Backup.BACKUP.FILENAME),
				record.getValue(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Backup.BACKUP.SERVICENAME));
		
		Timestamp date = null;
		if((date=record.getValue(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Backup.BACKUP.LASTATTEMPT))!=null) backup.setLastAttempt(new Date(date.getTime()));
		
		return backup;
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
			this.closeConnection();
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
							Watersystem.WATERSYSTEM.ENDOWMENT,
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
							waterSystem.getEndowment(),
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
	public boolean addWaterSpringToWaterSystem(WaterSystem waterSystem, WaterSpring waterSpring) {
		if(waterSystem == null)
			throw new NullArgumentException("waterSystem");
		if(waterSpring == null)
			throw new NullArgumentException("waterSpring");
		int result = 0;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext insert = this.prepareDSLContext(this.connection);
			result = insert.insertInto(WatersystemHasWaterspring.WATERSYSTEM_HAS_WATERSPRING,
					WatersystemHasWaterspring.WATERSYSTEM_HAS_WATERSPRING.WATERSYSTEM_IDWATERSYSTEM,
					WatersystemHasWaterspring.WATERSYSTEM_HAS_WATERSPRING.WATERSYSTEM_COMMUNITY_IDCOMMUNITY,
					WatersystemHasWaterspring.WATERSYSTEM_HAS_WATERSPRING.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN,
					WatersystemHasWaterspring.WATERSYSTEM_HAS_WATERSPRING.WATERSPRING_IDWATERSPRING)
					.values(waterSystem.getWaterSystemId(),
							waterSystem.getCommunity().getCommunityId(),
							waterSystem.getCommunity().getSubBasin().getSubBasinId(),
							waterSpring.getWaterSpringId())
					.execute();
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.toString());
		}
		
		return result>0;
	}

	@Override
	public List<WaterSpring> getWaterSpringsInWaterSystem(WaterSystem waterSystem) {
		if(waterSystem == null)
			throw new NullArgumentException("waterSystem");
		List<WaterSpring> waterSprings = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext select = this.prepareDSLContext(this.connection);
			waterSprings = new ArrayList<WaterSpring>();
			List<Record2<Integer, String>> result = select.select(Waterspring.WATERSPRING.IDWATERSPRING, Waterspring.WATERSPRING.NAME)
					.from(Waterspring.WATERSPRING, WatersystemHasWaterspring.WATERSYSTEM_HAS_WATERSPRING)
					.where(Waterspring.WATERSPRING.IDWATERSPRING.eq(WatersystemHasWaterspring.WATERSYSTEM_HAS_WATERSPRING.WATERSPRING_IDWATERSPRING))
					.and(WatersystemHasWaterspring.WATERSYSTEM_HAS_WATERSPRING.WATERSYSTEM_IDWATERSYSTEM.eq(waterSystem.getWaterSystemId()))
					.and(WatersystemHasWaterspring.WATERSYSTEM_HAS_WATERSPRING.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(waterSystem.getCommunity().getCommunityId()))
					.and(WatersystemHasWaterspring.WATERSYSTEM_HAS_WATERSPRING.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(waterSystem.getCommunity().getSubBasin().getSubBasinId()))
					.fetch();
			WaterSpring waterSpring = null;
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

	@Override
	public boolean removeWaterSpringOfWaterSystem(WaterSystem waterSystem, WaterSpring waterSpring) {
		if(waterSystem == null)
			throw new NullArgumentException("waterSystem");
		if(waterSpring == null)
			throw new NullArgumentException("waterSpring");
		int result = 0;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext delete = this.prepareDSLContext(this.connection);
			result = delete.delete(WatersystemHasWaterspring.WATERSYSTEM_HAS_WATERSPRING)
					.where(WatersystemHasWaterspring.WATERSYSTEM_HAS_WATERSPRING.WATERSPRING_IDWATERSPRING.eq(waterSpring.getWaterSpringId()))
					.and(WatersystemHasWaterspring.WATERSYSTEM_HAS_WATERSPRING.WATERSYSTEM_IDWATERSYSTEM.eq(waterSystem.getWaterSystemId()))
					.and(WatersystemHasWaterspring.WATERSYSTEM_HAS_WATERSPRING.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(waterSystem.getCommunity().getCommunityId()))
					.and(WatersystemHasWaterspring.WATERSYSTEM_HAS_WATERSPRING.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(waterSystem.getCommunity().getCommunityId()))
					.execute();
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.toString());
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
	public MeasuredFlow getLastMeasuredFlow(MeasuringPoint measuringPoint) {
		if(measuringPoint == null)
			throw new NullArgumentException("measuringPoint");
		MeasuredFlow measuredFlow = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext select = this.prepareDSLContext(this.connection);
			Result<Record> result = select.select().from(Measuredflow.MEASUREDFLOW)
					.where(Measuredflow.MEASUREDFLOW.MEASURINGPOINT_IDMEASURINGPOINT.eq(measuringPoint.getMeasuringPointId()))
					.and(Measuredflow.MEASUREDFLOW.MEASURINGPOINT_WATERSYSTEM_HAS_WATERSPRING_WATERSPRING_IDWATERSPRING.eq(measuringPoint.getWaterSpring().getWaterSpringId()))
					.and(Measuredflow.MEASUREDFLOW.MEASURINGPOINT_WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_IDWATERSYSTEM.eq(measuringPoint.getWaterSystem().getWaterSystemId()))
					.and(Measuredflow.MEASUREDFLOW.MEASURINGPOINT_WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(measuringPoint.getWaterSystem().getCommunity().getCommunityId()))
					.and(Measuredflow.MEASUREDFLOW.MEASURINGPOINT_WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(measuringPoint.getWaterSystem().getCommunity().getSubBasin().getSubBasinId()))
					.and(Measuredflow.MEASUREDFLOW.DATE.eq(
							select.select(DSL.max(Measuredflow.MEASUREDFLOW.DATE))
							.from(Measuredflow.MEASUREDFLOW)
							.where(Measuredflow.MEASUREDFLOW.MEASURINGPOINT_IDMEASURINGPOINT.eq(measuringPoint.getMeasuringPointId()))
							.and(Measuredflow.MEASUREDFLOW.MEASURINGPOINT_WATERSYSTEM_HAS_WATERSPRING_WATERSPRING_IDWATERSPRING.eq(measuringPoint.getWaterSpring().getWaterSpringId()))
							.and(Measuredflow.MEASUREDFLOW.MEASURINGPOINT_WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_IDWATERSYSTEM.eq(measuringPoint.getWaterSystem().getWaterSystemId()))
							.and(Measuredflow.MEASUREDFLOW.MEASURINGPOINT_WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(measuringPoint.getWaterSystem().getCommunity().getCommunityId()))
							.and(Measuredflow.MEASUREDFLOW.MEASURINGPOINT_WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(measuringPoint.getWaterSystem().getCommunity().getSubBasin().getSubBasinId()))))
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
					Chlorinecalculation.CHLORINECALCULATION.FAMILIESNUM,
					Chlorinecalculation.CHLORINECALCULATION.POPULATION,
					Chlorinecalculation.CHLORINECALCULATION.TANKVOLUME,
					Chlorinecalculation.CHLORINECALCULATION.ENDOWMENT,
					Chlorinecalculation.CHLORINECALCULATION.CHLORINEPURENESS,
					Chlorinecalculation.CHLORINECALCULATION.NATURALFLOW,
					Chlorinecalculation.CHLORINECALCULATION.CHLORINATEDFLOW,
					Chlorinecalculation.CHLORINECALCULATION.CHLORINETYPE,
					Chlorinecalculation.CHLORINECALCULATION.DRIPPINGHOURSPERDAY,
					Chlorinecalculation.CHLORINECALCULATION.CHLORINEDEMAND,
					Chlorinecalculation.CHLORINECALCULATION.CHLORINEPRICE,
					Chlorinecalculation.CHLORINECALCULATION.RELOADTIME,
					Chlorinecalculation.CHLORINECALCULATION.CHLORINEDOSEPERFORTNIGHT,
					Chlorinecalculation.CHLORINECALCULATION.CHLORINEDOSEPERMONTH,
					Chlorinecalculation.CHLORINECALCULATION.DRIPPINGFLOWINML,
					Chlorinecalculation.CHLORINECALCULATION.DRIPPINGFLOWINDROPS,
					Chlorinecalculation.CHLORINECALCULATION.CHLORINATIONCOST,
					Chlorinecalculation.CHLORINECALCULATION.WATERSYSTEM_IDWATERSYSTEM,
					Chlorinecalculation.CHLORINECALCULATION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY,
					Chlorinecalculation.CHLORINECALCULATION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN)
					.values(
							chlorineCalculation.getDate(),
							chlorineCalculation.getFamiliesNum(),
							chlorineCalculation.getPopulation(),
							chlorineCalculation.getTankVolume(),
							chlorineCalculation.getEndowment(),
							chlorineCalculation.getChlorinePureness(),
							chlorineCalculation.getNaturalFlow(),
							chlorineCalculation.getChlorinatedFlow(),
							chlorineCalculation.getChlorineType(),
							chlorineCalculation.getDrippingHoursPerDay(),
							chlorineCalculation.getChlorineDemand(),
							chlorineCalculation.getChlorinePrice(),
							chlorineCalculation.getReloadTime(),
							chlorineCalculation.getChlorineDosePerFortnight(),
							chlorineCalculation.getChlorineDosePerMonth(),
							chlorineCalculation.getDrippingFlowInMl(),
							chlorineCalculation.getDrippingFlowInDrops(),
							chlorineCalculation.getChlorinationCost(),
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
	public ChlorineCalculation getLastChlorineCalculation(WaterSystem waterSystem) {
		if(waterSystem == null)
			throw new NullArgumentException("waterSystem");
		ChlorineCalculation chlorineCalculation = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext select = this.prepareDSLContext(this.connection);
			Result<Record> result = select.select().from(Chlorinecalculation.CHLORINECALCULATION)
					.where(Chlorinecalculation.CHLORINECALCULATION.WATERSYSTEM_IDWATERSYSTEM.eq(waterSystem.getWaterSystemId()))
					.and(Chlorinecalculation.CHLORINECALCULATION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(waterSystem.getCommunity().getCommunityId()))
					.and(Chlorinecalculation.CHLORINECALCULATION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(waterSystem.getCommunity().getSubBasin().getSubBasinId()))
					.and(Chlorinecalculation.CHLORINECALCULATION.DATE.eq(select.select(DSL.max(Chlorinecalculation.CHLORINECALCULATION.DATE))
							.from(Chlorinecalculation.CHLORINECALCULATION)
							.where(Chlorinecalculation.CHLORINECALCULATION.WATERSYSTEM_IDWATERSYSTEM.eq(waterSystem.getWaterSystemId()))
							.and(Chlorinecalculation.CHLORINECALCULATION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(waterSystem.getCommunity().getCommunityId()))
							.and(Chlorinecalculation.CHLORINECALCULATION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(waterSystem.getCommunity().getSubBasin().getSubBasinId()))))
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
					.set(Chlorinecalculation.CHLORINECALCULATION.FAMILIESNUM, newChlorineCalculation.getFamiliesNum())
					.set(Chlorinecalculation.CHLORINECALCULATION.POPULATION, newChlorineCalculation.getPopulation())
					.set(Chlorinecalculation.CHLORINECALCULATION.TANKVOLUME, newChlorineCalculation.getTankVolume())
					.set(Chlorinecalculation.CHLORINECALCULATION.ENDOWMENT, newChlorineCalculation.getEndowment())
					.set(Chlorinecalculation.CHLORINECALCULATION.CHLORINEPURENESS, newChlorineCalculation.getChlorinePureness())
					.set(Chlorinecalculation.CHLORINECALCULATION.NATURALFLOW, newChlorineCalculation.getNaturalFlow())
					.set(Chlorinecalculation.CHLORINECALCULATION.CHLORINATEDFLOW, newChlorineCalculation.getChlorinatedFlow())
					.set(Chlorinecalculation.CHLORINECALCULATION.CHLORINETYPE, newChlorineCalculation.getChlorineType())
					.set(Chlorinecalculation.CHLORINECALCULATION.DRIPPINGHOURSPERDAY, newChlorineCalculation.getDrippingHoursPerDay())
					.set(Chlorinecalculation.CHLORINECALCULATION.CHLORINEDEMAND, newChlorineCalculation.getChlorineDemand())
					.set(Chlorinecalculation.CHLORINECALCULATION.CHLORINEPRICE, newChlorineCalculation.getChlorinePrice())
					.set(Chlorinecalculation.CHLORINECALCULATION.RELOADTIME, newChlorineCalculation.getReloadTime())
					.set(Chlorinecalculation.CHLORINECALCULATION.CHLORINEDOSEPERFORTNIGHT, newChlorineCalculation.getChlorineDosePerFortnight())
					.set(Chlorinecalculation.CHLORINECALCULATION.CHLORINEDOSEPERMONTH, newChlorineCalculation.getChlorineDosePerMonth())
					.set(Chlorinecalculation.CHLORINECALCULATION.DRIPPINGFLOWINML, newChlorineCalculation.getDrippingFlowInMl())
					.set(Chlorinecalculation.CHLORINECALCULATION.DRIPPINGFLOWINDROPS, newChlorineCalculation.getDrippingFlowInDrops())
					.set(Chlorinecalculation.CHLORINECALCULATION.CHLORINATIONCOST, newChlorineCalculation.getChlorinationCost())
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
		            Cubicreservoir.CUBICRESERVOIR.WATERSYSTEM_IDWATERSYSTEM,
		            Cubicreservoir.CUBICRESERVOIR.WATERSYSTEM_COMMUNITY_IDCOMMUNITY,
		            Cubicreservoir.CUBICRESERVOIR.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN)
					.values(cubicReservoir.getElementName(),
							cubicReservoir.getWidth(),
							cubicReservoir.getLength(),
							cubicReservoir.getHeight(),
							cubicReservoir.getCount(),
							cubicReservoir.getWaterSystem().getWaterSystemId(),
							cubicReservoir.getWaterSystem().getCommunity().getCommunityId(),
							cubicReservoir.getWaterSystem().getCommunity().getSubBasin().getSubBasinId())
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
					.set(Cubicreservoir.CUBICRESERVOIR.NAME, cubicReservoir.getElementName())
					.set(Cubicreservoir.CUBICRESERVOIR.WIDTH, cubicReservoir.getWidth())
					.set(Cubicreservoir.CUBICRESERVOIR.LENGTH, cubicReservoir.getLength())
					.set(Cubicreservoir.CUBICRESERVOIR.HEIGHT, cubicReservoir.getHeight())
					.set(Cubicreservoir.CUBICRESERVOIR.COUNT, cubicReservoir.getCount())
					.where(Cubicreservoir.CUBICRESERVOIR.IDCUBICRESERVOIR.eq(cubicReservoir.getReservoirId()))
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
					.where(Cubicreservoir.CUBICRESERVOIR.IDCUBICRESERVOIR.eq(cubicReservoir.getReservoirId()))
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
	public Catchment addCatchment(Catchment catchment) {
		if(catchment == null)
			throw new NullArgumentException("catchment");
		Catchment newCatchment = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext insert = this.prepareDSLContext(this.connection);
			int result = insert.insertInto(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchment.CATCHMENT,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchment.CATCHMENT.NAME,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchment.CATCHMENT.WIDTH,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchment.CATCHMENT.LENGTH,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchment.CATCHMENT.HEIGHT,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchment.CATCHMENT.COUNT,
		            org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchment.CATCHMENT.WATERSYSTEM_IDWATERSYSTEM,
		            org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchment.CATCHMENT.WATERSYSTEM_COMMUNITY_IDCOMMUNITY,
		            org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchment.CATCHMENT.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN)
					.values(catchment.getElementName(),
							catchment.getWidth(),
							catchment.getLength(),
							catchment.getHeight(),
							catchment.getCount(),
							catchment.getWaterSystem().getWaterSystemId(),
							catchment.getWaterSystem().getCommunity().getCommunityId(),
							catchment.getWaterSystem().getCommunity().getSubBasin().getSubBasinId())
					.execute();
			this.closeConnection();
			
			if(result>0)
				newCatchment = this.getCatchment(catchment.getWaterSystem(), this.getMaxValue(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchment.CATCHMENT, org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchment.CATCHMENT.IDCATCHMENT));
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return newCatchment;
	}

	@Override
	public List<Catchment> getCatchments(WaterSystem waterSystem) {
		if(waterSystem == null)
			throw new NullArgumentException("waterSystem");
		List<Catchment> catchments = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			catchments = new ArrayList<Catchment>();
			DSLContext select = this.prepareDSLContext(this.connection);
			Result<Record> result = select.select()
					.from(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchment.CATCHMENT)
					.where(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchment.CATCHMENT.WATERSYSTEM_IDWATERSYSTEM.eq(waterSystem.getWaterSystemId()))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchment.CATCHMENT.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(waterSystem.getCommunity().getCommunityId()))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchment.CATCHMENT.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(waterSystem.getCommunity().getSubBasin().getSubBasinId()))
					.fetch();
			
			Catchment cubicReservoir = null;
			for(Record record : result){
				cubicReservoir = this.readCatchment(record, waterSystem);
				catchments.add(cubicReservoir);
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return catchments;
	}

	@Override
	public Catchment getCatchment(WaterSystem waterSystem, int catchmentId) {
		if(waterSystem == null)
			throw new NullArgumentException("waterSystem");
		Catchment catchment = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext select = this.prepareDSLContext(this.connection);
			Result<Record> result = select.select()
					.from(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchment.CATCHMENT)
					.where(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchment.CATCHMENT.IDCATCHMENT.eq(catchmentId))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchment.CATCHMENT.WATERSYSTEM_IDWATERSYSTEM.eq(waterSystem.getWaterSystemId()))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchment.CATCHMENT.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(waterSystem.getCommunity().getCommunityId()))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchment.CATCHMENT.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(waterSystem.getCommunity().getSubBasin().getSubBasinId()))
					.limit(1)
					.fetch();
			
			for(Record record : result){
				catchment = this.readCatchment(record, waterSystem);
				break;
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return catchment;
	}

	@Override
	public boolean editCatchment(Catchment catchment) {
		if(catchment == null)
			throw new NullArgumentException("catchment");
		int result = 0;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext update = this.prepareDSLContext(this.connection);
			result = update.update(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchment.CATCHMENT)
					.set(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchment.CATCHMENT.NAME, catchment.getElementName())
					.set(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchment.CATCHMENT.WIDTH, catchment.getWidth())
					.set(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchment.CATCHMENT.LENGTH, catchment.getLength())
					.set(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchment.CATCHMENT.HEIGHT, catchment.getHeight())
					.set(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchment.CATCHMENT.COUNT, catchment.getCount())
					.where(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchment.CATCHMENT.IDCATCHMENT.eq(catchment.getReservoirId()))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchment.CATCHMENT.WATERSYSTEM_IDWATERSYSTEM.eq(catchment.getWaterSystem().getWaterSystemId()))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchment.CATCHMENT.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(catchment.getWaterSystem().getCommunity().getCommunityId()))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchment.CATCHMENT.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(catchment.getWaterSystem().getCommunity().getSubBasin().getSubBasinId()))
					.execute();
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return result>0;
	}

	@Override
	public boolean removeCatchment(Catchment catchment) {
		if(catchment == null)
			throw new NullArgumentException("catchment");
		int result = 0;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext delete = this.prepareDSLContext(this.connection);
			result = delete.delete(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchment.CATCHMENT)
					.where(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchment.CATCHMENT.IDCATCHMENT.eq(catchment.getReservoirId()))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchment.CATCHMENT.WATERSYSTEM_IDWATERSYSTEM.eq(catchment.getWaterSystem().getWaterSystemId()))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchment.CATCHMENT.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(catchment.getWaterSystem().getCommunity().getCommunityId()))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchment.CATCHMENT.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(catchment.getWaterSystem().getCommunity().getSubBasin().getSubBasinId()))
					.execute();
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return result>0;
	}

	@Override
	public DistributionPipe addDistributionPipe(DistributionPipe pipe) {
		if(pipe == null)
			throw new NullArgumentException("pipe");
		DistributionPipe newPipe = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext insert = this.prepareDSLContext(this.connection);
			int result = insert.insertInto(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.NAME,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.DIAMETER,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.LENGTH,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.COUNT,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.TYPE,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.WATERSYSTEM_IDWATERSYSTEM,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.WATERSYSTEM_COMMUNITY_IDCOMMUNITY,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN)
					.values(pipe.getElementName(),
							pipe.getDiameter(),
							pipe.getLength(),
							pipe.getCount(),
							Pipe.DISTRIBUTION_PIPE_TYPE,
							pipe.getWaterSystem().getWaterSystemId(),
                            pipe.getWaterSystem().getCommunity().getCommunityId(),
                            pipe.getWaterSystem().getCommunity().getSubBasin().getSubBasinId())
					.execute();
			this.closeConnection();
			if(result>0)
				newPipe = this.getDistributionPipe(pipe.getWaterSystem(), this.getMaxValue(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE, org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.IDPIPE));
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return newPipe;
	}

	@Override
	public List<DistributionPipe> getDistributionPipes(WaterSystem waterSystem) {
		if(waterSystem == null)
			throw new NullArgumentException("waterSystem");
		List<DistributionPipe> pipes = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			pipes = new ArrayList<DistributionPipe>();
			DSLContext select = this.prepareDSLContext(this.connection);
			Result<Record> result = select.select().from(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE)
					.where(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.TYPE.eq(Pipe.DISTRIBUTION_PIPE_TYPE))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.WATERSYSTEM_IDWATERSYSTEM.eq(waterSystem.getWaterSystemId()))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(waterSystem.getCommunity().getCommunityId()))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(waterSystem.getCommunity().getSubBasin().getSubBasinId()))
					.fetch();
			
			DistributionPipe pipe = null;
			for(Record record : result){
				pipe = this.readDistributionPipe(record, waterSystem);
				pipes.add(pipe);
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return pipes;
	}

	@Override
	public DistributionPipe getDistributionPipe(WaterSystem waterSystem, int pipeId) {
		if(waterSystem == null)
			throw new NullArgumentException("waterSystem");
		DistributionPipe pipe = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext select = this.prepareDSLContext(this.connection);
			Result<Record> result = select.select().from(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE)
					.where(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.TYPE.eq(Pipe.DISTRIBUTION_PIPE_TYPE))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.IDPIPE.eq(pipeId))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.WATERSYSTEM_IDWATERSYSTEM.eq(waterSystem.getWaterSystemId()))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(waterSystem.getCommunity().getCommunityId()))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(waterSystem.getCommunity().getSubBasin().getSubBasinId()))
					.limit(1)
					.fetch();
			
			for(Record record : result){
				pipe = this.readDistributionPipe(record, waterSystem);
				break;
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return pipe;
	}

	@Override
	public ConductionPipe addConductionPipe(ConductionPipe pipe) {
		if(pipe == null)
			throw new NullArgumentException("pipe");
		ConductionPipe newPipe = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext insert = this.prepareDSLContext(this.connection);
			int result = insert.insertInto(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.NAME,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.DIAMETER,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.LENGTH,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.COUNT,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.TYPE,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.WATERSYSTEM_IDWATERSYSTEM,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.WATERSYSTEM_COMMUNITY_IDCOMMUNITY,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN)
					.values(pipe.getElementName(),
							pipe.getDiameter(),
							pipe.getLength(),
							pipe.getCount(),
							Pipe.CONDUCTION_PIPE_TYPE,
							pipe.getWaterSystem().getWaterSystemId(),
                            pipe.getWaterSystem().getCommunity().getCommunityId(),
                            pipe.getWaterSystem().getCommunity().getSubBasin().getSubBasinId())
					.execute();
			this.closeConnection();
			if(result>0)
				newPipe = this.getConductionPipe(pipe.getWaterSystem(), this.getMaxValue(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE, org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.IDPIPE));
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return newPipe;
	}

	@Override
	public List<ConductionPipe> getConductionPipes(WaterSystem waterSystem) {
		if(waterSystem == null)
			throw new NullArgumentException("waterSystem");
		List<ConductionPipe> pipes = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			pipes = new ArrayList<ConductionPipe>();
			DSLContext select = this.prepareDSLContext(this.connection);
			Result<Record> result = select.select().from(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE)
					.where(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.TYPE.eq(Pipe.CONDUCTION_PIPE_TYPE))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.WATERSYSTEM_IDWATERSYSTEM.eq(waterSystem.getWaterSystemId()))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(waterSystem.getCommunity().getCommunityId()))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(waterSystem.getCommunity().getSubBasin().getSubBasinId()))
					.fetch();
			
			ConductionPipe pipe = null;
			for(Record record : result){
				pipe = this.readConductionPipe(record, waterSystem);
				pipes.add(pipe);
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.getMessage());
		}
		
		return pipes;
	}

	@Override
	public ConductionPipe getConductionPipe(WaterSystem waterSystem, int pipeId) {
		if(waterSystem == null)
			throw new NullArgumentException("waterSystem");
		ConductionPipe pipe = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext select = this.prepareDSLContext(this.connection);
			Result<Record> result = select.select().from(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE)
					.where(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.TYPE.eq(Pipe.CONDUCTION_PIPE_TYPE))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.IDPIPE.eq(pipeId))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.WATERSYSTEM_IDWATERSYSTEM.eq(waterSystem.getWaterSystemId()))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(waterSystem.getCommunity().getCommunityId()))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(waterSystem.getCommunity().getSubBasin().getSubBasinId()))
					.limit(1)
					.fetch();
			
			for(Record record : result){
				pipe = this.readConductionPipe(record, waterSystem);
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
					.set(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.NAME, pipe.getElementName())
					.set(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.DIAMETER, pipe.getDiameter())
					.set(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.LENGTH, pipe.getLength())
					.set(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe.PIPE.COUNT, pipe.getCount())
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
					Reliefvalve.RELIEFVALVE.WATERSYSTEM_IDWATERSYSTEM,
					Reliefvalve.RELIEFVALVE.WATERSYSTEM_COMMUNITY_IDCOMMUNITY,
					Reliefvalve.RELIEFVALVE.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN)
					.values(reliefValve.getElementName(),
							reliefValve.getWidth(),
							reliefValve.getLength(),
							reliefValve.getHeight(),
							reliefValve.getCount(),
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
					.set(Reliefvalve.RELIEFVALVE.NAME, reliefValve.getElementName())
					.set(Reliefvalve.RELIEFVALVE.WIDTH, reliefValve.getWidth())
					.set(Reliefvalve.RELIEFVALVE.LENGTH, reliefValve.getLength())
					.set(Reliefvalve.RELIEFVALVE.HEIGHT, reliefValve.getHeight())
					.set(Reliefvalve.RELIEFVALVE.COUNT, reliefValve.getCount())
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
	public Desinfection addDesinfection(Desinfection desinfection) {
		if(desinfection == null)
			throw new NullArgumentException("desinfection");
		Desinfection newDesinfection = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext insert = this.prepareDSLContext(this.connection);
			int result = insert.insertInto(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Desinfection.DESINFECTION,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Desinfection.DESINFECTION.DATE,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Desinfection.DESINFECTION.CHLORINEPRICE,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Desinfection.DESINFECTION.CHLORINEPURENESS,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Desinfection.DESINFECTION.CHLORINETYPE,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Desinfection.DESINFECTION.WATERSYSTEM_IDWATERSYSTEM,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Desinfection.DESINFECTION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Desinfection.DESINFECTION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN)
					.values(
							desinfection.getDate(),
							desinfection.getChlorinePrice(),
							desinfection.getChlorinePureness(),
							desinfection.getChlorineType(),
							desinfection.getWaterSystem().getWaterSystemId(),
							desinfection.getWaterSystem().getCommunity().getCommunityId(),
							desinfection.getWaterSystem().getCommunity().getSubBasin().getSubBasinId())
					.execute();
			this.closeConnection();
			if(result>0)
				newDesinfection = this.getDesinfection(desinfection.getDate(), desinfection.getWaterSystem());
		} catch (SQLException e) {
			log.warn(e.toString());
		}
		
		return newDesinfection;
	}

	@Override
	public List<Desinfection> getDesinfections(WaterSystem waterSystem) {
		if(waterSystem == null)
			throw new NullArgumentException("waterSystem");
		List<Desinfection> desinfections = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			desinfections = new ArrayList<Desinfection>();
			DSLContext select = this.prepareDSLContext(this.connection);
			List<Record> records = select.select()
					.from(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Desinfection.DESINFECTION)
					.where(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Desinfection.DESINFECTION.WATERSYSTEM_IDWATERSYSTEM.eq(waterSystem.getWaterSystemId()))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Desinfection.DESINFECTION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(waterSystem.getCommunity().getCommunityId()))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Desinfection.DESINFECTION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(waterSystem.getCommunity().getSubBasin().getSubBasinId()))
					.orderBy(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Desinfection.DESINFECTION.DATE)
					.fetch();
			
			for(Record record : records){
				desinfections.add(this.readDesinfection(record, waterSystem));
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.toString());
		}
		
		return desinfections;
	}

	@Override
	public Desinfection getDesinfection(Timestamp date, WaterSystem waterSystem) {
		if(waterSystem == null)
			throw new NullArgumentException("waterSystem");
		if(date == null)
			throw new NullArgumentException("date");
		Desinfection desinfection = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext select = this.prepareDSLContext(this.connection);
			List<Record> records = select.select()
					.from(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Desinfection.DESINFECTION)
					.where(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Desinfection.DESINFECTION.DATE.eq(date))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Desinfection.DESINFECTION.WATERSYSTEM_IDWATERSYSTEM.eq(waterSystem.getWaterSystemId()))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Desinfection.DESINFECTION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(waterSystem.getCommunity().getCommunityId()))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Desinfection.DESINFECTION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(waterSystem.getCommunity().getSubBasin().getSubBasinId()))
					.limit(1)
					.fetch();
			
			for(Record record : records){
				desinfection = this.readDesinfection(record, waterSystem);
				break;
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.toString());
		}
		
		return desinfection;
	}

	@Override
	public Desinfection getLastDesinfection(WaterSystem waterSystem) {
		if(waterSystem == null)
			throw new NullArgumentException("waterSystem");
		Desinfection desinfection = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext select = this.prepareDSLContext(this.connection);
			List<Record> records = select.select()
					.from(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Desinfection.DESINFECTION)
					.where(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Desinfection.DESINFECTION.WATERSYSTEM_IDWATERSYSTEM.eq(waterSystem.getWaterSystemId()))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Desinfection.DESINFECTION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(waterSystem.getCommunity().getCommunityId()))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Desinfection.DESINFECTION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(waterSystem.getCommunity().getSubBasin().getSubBasinId()))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Desinfection.DESINFECTION.DATE.eq(
							select.select(DSL.max(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Desinfection.DESINFECTION.DATE))
							.from(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Desinfection.DESINFECTION)
							.where(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Desinfection.DESINFECTION.WATERSYSTEM_IDWATERSYSTEM.eq(waterSystem.getWaterSystemId()))
							.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Desinfection.DESINFECTION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(waterSystem.getCommunity().getCommunityId()))
							.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Desinfection.DESINFECTION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(waterSystem.getCommunity().getSubBasin().getSubBasinId()))))
					.limit(1)
					.fetch();
			
			for(Record record : records){
				desinfection = this.readDesinfection(record, waterSystem);
				break;
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.toString());
		}
		
		return desinfection;
	}

	@Override
	public boolean editDesinfection(Desinfection desinfection) {
		if(desinfection == null)
			throw new NullArgumentException("desinfection");
		int result = 0;
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext update = this.prepareDSLContext(this.connection);
			result = update.update(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Desinfection.DESINFECTION)
					.set(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Desinfection.DESINFECTION.CHLORINETYPE, desinfection.getChlorineType())
					.set(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Desinfection.DESINFECTION.CHLORINEPURENESS, desinfection.getChlorinePureness())
					.set(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Desinfection.DESINFECTION.CHLORINEPRICE, desinfection.getChlorinePrice())
					.where(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Desinfection.DESINFECTION.DATE.eq(desinfection.getDate()))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Desinfection.DESINFECTION.WATERSYSTEM_IDWATERSYSTEM.eq(desinfection.getWaterSystem().getWaterSystemId()))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Desinfection.DESINFECTION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(desinfection.getWaterSystem().getCommunity().getCommunityId()))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Desinfection.DESINFECTION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(desinfection.getWaterSystem().getCommunity().getSubBasin().getSubBasinId()))
					.execute();
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.toString());
		}
		
		return result>0;
	}

	@Override
	public boolean removeDesinfection(Desinfection desinfection) {
		if(desinfection == null)
			throw new NullArgumentException("desinfection");
		int result = 0;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext remove = this.prepareDSLContext(this.connection);
			result = remove.delete(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Desinfection.DESINFECTION)
					.where(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Desinfection.DESINFECTION.DATE.eq(desinfection.getDate()))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Desinfection.DESINFECTION.WATERSYSTEM_IDWATERSYSTEM.eq(desinfection.getWaterSystem().getWaterSystemId()))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Desinfection.DESINFECTION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(desinfection.getWaterSystem().getCommunity().getCommunityId()))
					.and(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Desinfection.DESINFECTION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(desinfection.getWaterSystem().getCommunity().getSubBasin().getSubBasinId()))
					.execute();
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.toString());
		}
		
		return result>0;
	}
	
	@Override
	public CubicReservoirDesinfection addCubicReservoirDesinfection(CubicReservoirDesinfection cubicReservoirDesinfection) {
		if(cubicReservoirDesinfection == null)
			throw new NullArgumentException("cubicReservoirDesinfection");
		CubicReservoirDesinfection newCubicReservoirDesinfection = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext insert = this.prepareDSLContext(this.connection);
			int result = insert.insertInto(Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION,
					Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.DESINFECTION_DATE,
					Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.COUNT,
					Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.CHLORINEQTY,
					Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.DEMANDSPOONS,
					Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.RETENTIONTIME,
					Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.CUBICRESERVOIR_IDCUBICRESERVOIR,
					Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.WATERSYSTEM_IDWATERSYSTEM,
					Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY,
					Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN)
					.values(cubicReservoirDesinfection.getDesinfection().getDate(),
							cubicReservoirDesinfection.getCount(),
							cubicReservoirDesinfection.getChlorineQty(),
							cubicReservoirDesinfection.getDemandSpoons(),
							cubicReservoirDesinfection.getRetentionTime(),
							cubicReservoirDesinfection.getCubicReservoir().getReservoirId(),
							cubicReservoirDesinfection.getCubicReservoir().getWaterSystem().getWaterSystemId(),
							cubicReservoirDesinfection.getCubicReservoir().getWaterSystem().getCommunity().getCommunityId(),
							cubicReservoirDesinfection.getCubicReservoir().getWaterSystem().getCommunity().getSubBasin().getSubBasinId())
					.execute();
			this.closeConnection();
			
			if(result>0)
				newCubicReservoirDesinfection = this.getCubicReservoirDesinfection(cubicReservoirDesinfection.getCubicReservoir(), cubicReservoirDesinfection.getDesinfection());
		} catch (SQLException e) {
			log.warn(e.toString());
		}
		
		return newCubicReservoirDesinfection;
	}

	@Override
	public CubicReservoirDesinfection getCubicReservoirDesinfection(CubicReservoir cubicReservoir, Desinfection desinfection) {
		if(cubicReservoir == null)
			throw new NullArgumentException("cubicReservoir");
		if(desinfection == null)
			throw new NullArgumentException("desinfection");
		CubicReservoirDesinfection cubicReservoirDesinfection = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext select = this.prepareDSLContext(this.connection);
			List<Record> result = select.select()
					.from(Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION)
					.where(Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.DESINFECTION_DATE.eq(desinfection.getDate()))
					.and(Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.CUBICRESERVOIR_IDCUBICRESERVOIR.eq(cubicReservoir.getReservoirId()))
					.and(Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.WATERSYSTEM_IDWATERSYSTEM.eq(cubicReservoir.getWaterSystem().getWaterSystemId()))
					.and(Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(cubicReservoir.getWaterSystem().getCommunity().getCommunityId()))
					.and(Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(cubicReservoir.getWaterSystem().getCommunity().getSubBasin().getSubBasinId()))
					.limit(1)
					.fetch();
			
			for(Record record:result){
				cubicReservoirDesinfection = this.readCubicReservoirDesinfection(record, cubicReservoir, desinfection);
				break;
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.toString());
		}
		
		return cubicReservoirDesinfection;
	}

	@Override
	public boolean editCubicReservoirDesinfection(CubicReservoirDesinfection newCubicReservoirDesinfection) {
		if(newCubicReservoirDesinfection == null)
			throw new NullArgumentException("newCubicReservoirDesinfection");
		int result = 0;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext update = this.prepareDSLContext(this.connection);
			result = update.update(Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION)
					.set(Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.COUNT, newCubicReservoirDesinfection.getCount())
					.set(Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.CHLORINEQTY, newCubicReservoirDesinfection.getChlorineQty())
					.set(Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.DEMANDSPOONS, newCubicReservoirDesinfection.getDemandSpoons())
					.set(Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.RETENTIONTIME, newCubicReservoirDesinfection.getRetentionTime())
					.where(Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.DESINFECTION_DATE.eq(newCubicReservoirDesinfection.getDesinfection().getDate()))
					.and(Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.CUBICRESERVOIR_IDCUBICRESERVOIR.eq(newCubicReservoirDesinfection.getCubicReservoir().getReservoirId()))
					.and(Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.WATERSYSTEM_IDWATERSYSTEM.eq(newCubicReservoirDesinfection.getCubicReservoir().getWaterSystem().getWaterSystemId()))
					.and(Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(newCubicReservoirDesinfection.getCubicReservoir().getWaterSystem().getCommunity().getCommunityId()))
					.and(Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(newCubicReservoirDesinfection.getCubicReservoir().getWaterSystem().getCommunity().getSubBasin().getSubBasinId()))
					.execute();
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.toString());
		}
		
		return result>0;
	}

	@Override
	public boolean removeCubicReservoirDesinfection(CubicReservoirDesinfection cubicReservoirDesinfection) {
		if(cubicReservoirDesinfection == null)
			throw new NullArgumentException("cubicReservoirDesinfection");
		int result = 0;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext remove = this.prepareDSLContext(this.connection);
			result = remove.delete(Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION)
					.where(Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.DESINFECTION_DATE.eq(cubicReservoirDesinfection.getDesinfection().getDate()))
					.and(Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.CUBICRESERVOIR_IDCUBICRESERVOIR.eq(cubicReservoirDesinfection.getCubicReservoir().getReservoirId()))
					.and(Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.WATERSYSTEM_IDWATERSYSTEM.eq(cubicReservoirDesinfection.getCubicReservoir().getWaterSystem().getWaterSystemId()))
					.and(Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(cubicReservoirDesinfection.getCubicReservoir().getWaterSystem().getCommunity().getCommunityId()))
					.and(Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(cubicReservoirDesinfection.getCubicReservoir().getWaterSystem().getCommunity().getSubBasin().getSubBasinId()))
					.execute();
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.toString());
		}
		
		return result>0;
	}

	@Override
	public CatchmentDesinfection addCatchmentDesinfection(CatchmentDesinfection catchmentDesinfection) {
		if(catchmentDesinfection == null)
			throw new NullArgumentException("catchmentDesinfection");
		CatchmentDesinfection newCatchmentDesinfection = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext insert = this.prepareDSLContext(this.connection);
			int result = insert.insertInto(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchmentdesinfection.CATCHMENTDESINFECTION,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchmentdesinfection.CATCHMENTDESINFECTION.DESINFECTION_DATE,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchmentdesinfection.CATCHMENTDESINFECTION.COUNT,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchmentdesinfection.CATCHMENTDESINFECTION.CHLORINEQTY,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchmentdesinfection.CATCHMENTDESINFECTION.DEMANDSPOONS,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchmentdesinfection.CATCHMENTDESINFECTION.RETENTIONTIME,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchmentdesinfection.CATCHMENTDESINFECTION.CATCHMENT_IDCATCHMENT,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchmentdesinfection.CATCHMENTDESINFECTION.WATERSYSTEM_IDWATERSYSTEM,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchmentdesinfection.CATCHMENTDESINFECTION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchmentdesinfection.CATCHMENTDESINFECTION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN)
					.values(catchmentDesinfection.getDesinfection().getDate(),
							catchmentDesinfection.getCount(),
							catchmentDesinfection.getChlorineQty(),
							catchmentDesinfection.getDemandSpoons(),
							catchmentDesinfection.getRetentionTime(),
							catchmentDesinfection.getCatchment().getReservoirId(),
							catchmentDesinfection.getCatchment().getWaterSystem().getWaterSystemId(),
							catchmentDesinfection.getCatchment().getWaterSystem().getCommunity().getCommunityId(),
							catchmentDesinfection.getCatchment().getWaterSystem().getCommunity().getSubBasin().getSubBasinId())
					.execute();
			this.closeConnection();
			
			if(result>0)
				newCatchmentDesinfection = this.getCatchmentDesinfection(catchmentDesinfection.getCatchment(), catchmentDesinfection.getDesinfection());
		} catch (SQLException e) {
			log.warn(e.toString());
		}
		
		return newCatchmentDesinfection;
	}

	@Override
	public CatchmentDesinfection getCatchmentDesinfection(Catchment catchment, Desinfection desinfection) {
		if(catchment == null)
			throw new NullArgumentException("catchment");
		CatchmentDesinfection catchmentDesinfection = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext select = this.prepareDSLContext(this.connection);
			List<Record> result = select.select()
					.from(Catchmentdesinfection.CATCHMENTDESINFECTION)
					.where(Catchmentdesinfection.CATCHMENTDESINFECTION.DESINFECTION_DATE.eq(desinfection.getDate()))
					.and(Catchmentdesinfection.CATCHMENTDESINFECTION.CATCHMENT_IDCATCHMENT.eq(catchment.getReservoirId()))
					.and(Catchmentdesinfection.CATCHMENTDESINFECTION.WATERSYSTEM_IDWATERSYSTEM.eq(catchment.getWaterSystem().getWaterSystemId()))
					.and(Catchmentdesinfection.CATCHMENTDESINFECTION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(catchment.getWaterSystem().getCommunity().getCommunityId()))
					.and(Catchmentdesinfection.CATCHMENTDESINFECTION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(catchment.getWaterSystem().getCommunity().getSubBasin().getSubBasinId()))
					.limit(1)
					.fetch();
			
			for(Record record:result){
				catchmentDesinfection = this.readCatchmentDesinfection(record, catchment, desinfection);
				break;
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.toString());
		}
		
		return catchmentDesinfection;
	}

	@Override
	public boolean editCatchmentDesinfection(CatchmentDesinfection newCatchmentDesinfection) {
		if(newCatchmentDesinfection == null)
			throw new NullArgumentException("newCatchmentDesinfection");
		int result = 0;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext update = this.prepareDSLContext(this.connection);
			result = update.update(Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION)
					.set(Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.COUNT, newCatchmentDesinfection.getCount())
					.set(Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.CHLORINEQTY, newCatchmentDesinfection.getChlorineQty())
					.set(Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.DEMANDSPOONS, newCatchmentDesinfection.getDemandSpoons())
					.set(Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.RETENTIONTIME, newCatchmentDesinfection.getRetentionTime())
					.where(Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.DESINFECTION_DATE.eq(newCatchmentDesinfection.getDesinfection().getDate()))
					.and(Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.CUBICRESERVOIR_IDCUBICRESERVOIR.eq(newCatchmentDesinfection.getCatchment().getReservoirId()))
					.and(Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.WATERSYSTEM_IDWATERSYSTEM.eq(newCatchmentDesinfection.getCatchment().getWaterSystem().getWaterSystemId()))
					.and(Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(newCatchmentDesinfection.getCatchment().getWaterSystem().getCommunity().getCommunityId()))
					.and(Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(newCatchmentDesinfection.getCatchment().getWaterSystem().getCommunity().getSubBasin().getSubBasinId()))
					.execute();
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.toString());
		}
		
		return result>0;
	}

	@Override
	public boolean removeCatchmentDesinfection(CatchmentDesinfection catchmentDesinfection) {
		if(catchmentDesinfection == null)
			throw new NullArgumentException("catchmentDesinfection");
		int result = 0;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext remove = this.prepareDSLContext(this.connection);
			result = remove.delete(Catchmentdesinfection.CATCHMENTDESINFECTION)
					.where(Catchmentdesinfection.CATCHMENTDESINFECTION.DESINFECTION_DATE.eq(catchmentDesinfection.getDesinfection().getDate()))
					.and(Catchmentdesinfection.CATCHMENTDESINFECTION.CATCHMENT_IDCATCHMENT.eq(catchmentDesinfection.getCatchment().getReservoirId()))
					.and(Catchmentdesinfection.CATCHMENTDESINFECTION.WATERSYSTEM_IDWATERSYSTEM.eq(catchmentDesinfection.getCatchment().getWaterSystem().getWaterSystemId()))
					.and(Catchmentdesinfection.CATCHMENTDESINFECTION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(catchmentDesinfection.getCatchment().getWaterSystem().getCommunity().getCommunityId()))
					.and(Catchmentdesinfection.CATCHMENTDESINFECTION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(catchmentDesinfection.getCatchment().getWaterSystem().getCommunity().getSubBasin().getSubBasinId()))
					.execute();
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.toString());
		}
		
		return result>0;
	}

	@Override
	public PipeDesinfection addPipeDesinfection(PipeDesinfection pipeDesinfection) {
		if(pipeDesinfection == null)
			throw new NullArgumentException("pipeDesinfection");
		PipeDesinfection newPipeDesinfection = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext insert = this.prepareDSLContext(this.connection);
			int result = insert.insertInto(Pipedesinfection.PIPEDESINFECTION,
					Pipedesinfection.PIPEDESINFECTION.DESINFECTION_DATE,
					Pipedesinfection.PIPEDESINFECTION.COUNT,
					Pipedesinfection.PIPEDESINFECTION.CHLORINEQTY,
					Pipedesinfection.PIPEDESINFECTION.DEMANDSPOONS,
					Pipedesinfection.PIPEDESINFECTION.RETENTIONTIME,
					Pipedesinfection.PIPEDESINFECTION.PIPE_IDPIPE,
					Pipedesinfection.PIPEDESINFECTION.WATERSYSTEM_IDWATERSYSTEM,
					Pipedesinfection.PIPEDESINFECTION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY,
					Pipedesinfection.PIPEDESINFECTION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN)
					.values(pipeDesinfection.getDesinfection().getDate(),
							pipeDesinfection.getCount(),
							pipeDesinfection.getChlorineQty(),
							pipeDesinfection.getDemandSpoons(),
							pipeDesinfection.getRetentionTime(),
							pipeDesinfection.getPipe().getPipeId(),
							pipeDesinfection.getPipe().getWaterSystem().getWaterSystemId(),
							pipeDesinfection.getPipe().getWaterSystem().getCommunity().getCommunityId(),
							pipeDesinfection.getPipe().getWaterSystem().getCommunity().getSubBasin().getSubBasinId())
					.execute();
			this.closeConnection();
			
			if(result>0)
				newPipeDesinfection = this.getPipeDesinfection(pipeDesinfection.getPipe(), pipeDesinfection.getDesinfection());
		} catch (SQLException e) {
			log.warn(e.toString());
		}
		
		return newPipeDesinfection;
	}

	@Override
	public PipeDesinfection getPipeDesinfection(Pipe pipe, Desinfection desinfection) {
		if(pipe == null)
			throw new NullArgumentException("pipe");
		if(desinfection == null)
			throw new NullArgumentException("desinfection");
		PipeDesinfection pipeDesinfection = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext select = this.prepareDSLContext(this.connection);
			List<Record> result = select.select()
					.from(Pipedesinfection.PIPEDESINFECTION)
					.where(Pipedesinfection.PIPEDESINFECTION.DESINFECTION_DATE.eq(desinfection.getDate()))
					.and(Pipedesinfection.PIPEDESINFECTION.PIPE_IDPIPE.eq(pipe.getPipeId()))
					.and(Pipedesinfection.PIPEDESINFECTION.WATERSYSTEM_IDWATERSYSTEM.eq(pipe.getWaterSystem().getWaterSystemId()))
					.and(Pipedesinfection.PIPEDESINFECTION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(pipe.getWaterSystem().getCommunity().getCommunityId()))
					.and(Pipedesinfection.PIPEDESINFECTION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(pipe.getWaterSystem().getCommunity().getSubBasin().getSubBasinId()))
					.limit(1)
					.fetch();
			
			for(Record record:result){
				pipeDesinfection = this.readPipeDesinfection(record, pipe, desinfection);
				break;
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.toString());
		}
		
		return pipeDesinfection;
	}

	@Override
	public boolean editPipeDesinfection(PipeDesinfection newPipeDesinfection) {
		if(newPipeDesinfection == null)
			throw new NullArgumentException("newPipeDesinfection");
		int result = 0;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext update = this.prepareDSLContext(this.connection);
			result = update.update(Pipedesinfection.PIPEDESINFECTION)
					.set(Pipedesinfection.PIPEDESINFECTION.COUNT, newPipeDesinfection.getCount())
					.set(Pipedesinfection.PIPEDESINFECTION.CHLORINEQTY, newPipeDesinfection.getChlorineQty())
					.set(Pipedesinfection.PIPEDESINFECTION.DEMANDSPOONS, newPipeDesinfection.getDemandSpoons())
					.set(Pipedesinfection.PIPEDESINFECTION.RETENTIONTIME, newPipeDesinfection.getRetentionTime())
					.where(Pipedesinfection.PIPEDESINFECTION.DESINFECTION_DATE.eq(newPipeDesinfection.getDesinfection().getDate()))
					.and(Pipedesinfection.PIPEDESINFECTION.PIPE_IDPIPE.eq(newPipeDesinfection.getPipe().getPipeId()))
					.and(Pipedesinfection.PIPEDESINFECTION.WATERSYSTEM_IDWATERSYSTEM.eq(newPipeDesinfection.getPipe().getWaterSystem().getWaterSystemId()))
					.and(Pipedesinfection.PIPEDESINFECTION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(newPipeDesinfection.getPipe().getWaterSystem().getCommunity().getCommunityId()))
					.and(Pipedesinfection.PIPEDESINFECTION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(newPipeDesinfection.getPipe().getWaterSystem().getCommunity().getSubBasin().getSubBasinId()))
					.execute();
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.toString());
		}
		
		return result>0;
	}

	@Override
	public boolean removePipeDesinfection(PipeDesinfection pipeDesinfection) {
		if(pipeDesinfection == null)
			throw new NullArgumentException("pipeDesinfection");
		int result = 0;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext remove = this.prepareDSLContext(this.connection);
			result = remove.delete(Pipedesinfection.PIPEDESINFECTION)
					.where(Pipedesinfection.PIPEDESINFECTION.DESINFECTION_DATE.eq(pipeDesinfection.getDesinfection().getDate()))
					.and(Pipedesinfection.PIPEDESINFECTION.PIPE_IDPIPE.eq(pipeDesinfection.getPipe().getPipeId()))
					.and(Pipedesinfection.PIPEDESINFECTION.WATERSYSTEM_IDWATERSYSTEM.eq(pipeDesinfection.getPipe().getWaterSystem().getWaterSystemId()))
					.and(Pipedesinfection.PIPEDESINFECTION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(pipeDesinfection.getPipe().getWaterSystem().getCommunity().getCommunityId()))
					.and(Pipedesinfection.PIPEDESINFECTION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(pipeDesinfection.getPipe().getWaterSystem().getCommunity().getSubBasin().getSubBasinId()))
					.execute();
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.toString());
		}
		
		return result>0;
	}

	@Override
	public ReliefValveDesinfection addReliefValveDesinfection(ReliefValveDesinfection reliefValveDesinfection) {
		if(reliefValveDesinfection == null)
			throw new NullArgumentException("reliefValveDesinfection");
		ReliefValveDesinfection newReliefValveDesinfection = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			int result = 0;
			DSLContext insert = this.prepareDSLContext(this.connection);
			result = insert.insertInto(Reliefvalvedesinfection.RELIEFVALVEDESINFECTION,
					Reliefvalvedesinfection.RELIEFVALVEDESINFECTION.DESINFECTION_DATE,
					Reliefvalvedesinfection.RELIEFVALVEDESINFECTION.COUNT,
					Reliefvalvedesinfection.RELIEFVALVEDESINFECTION.CHLORINEQTY,
					Reliefvalvedesinfection.RELIEFVALVEDESINFECTION.DEMANDSPOONS,
					Reliefvalvedesinfection.RELIEFVALVEDESINFECTION.RETENTIONTIME,
					Reliefvalvedesinfection.RELIEFVALVEDESINFECTION.RELIEFVALVE_IDRELIEFVALVE,
					Reliefvalvedesinfection.RELIEFVALVEDESINFECTION.WATERSYSTEM_IDWATERSYSTEM,
					Reliefvalvedesinfection.RELIEFVALVEDESINFECTION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY,
					Reliefvalvedesinfection.RELIEFVALVEDESINFECTION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN)
					.values(reliefValveDesinfection.getDesinfection().getDate(),
							reliefValveDesinfection.getCount(),
							reliefValveDesinfection.getChlorineQty(),
							reliefValveDesinfection.getDemandSpoons(),
							reliefValveDesinfection.getRetentionTime(),
							reliefValveDesinfection.getReliefValve().getReliefValveId(),
							reliefValveDesinfection.getReliefValve().getWaterSystem().getWaterSystemId(),
							reliefValveDesinfection.getReliefValve().getWaterSystem().getCommunity().getCommunityId(),
							reliefValveDesinfection.getReliefValve().getWaterSystem().getCommunity().getSubBasin().getSubBasinId())
					.execute();
			this.closeConnection();
			
			if(result>0)
				newReliefValveDesinfection = this.getReliefValveDesinfection(reliefValveDesinfection.getReliefValve(), reliefValveDesinfection.getDesinfection());
					
		} catch (SQLException e) {
			log.warn(e.toString());
		}
		return newReliefValveDesinfection;
	}

	@Override
	public ReliefValveDesinfection getReliefValveDesinfection(ReliefValve reliefValve, Desinfection desinfection){
		if(reliefValve == null)
			throw new NullArgumentException("reliefValve");
		if(desinfection == null)
			throw new NullArgumentException("desinfection");
		ReliefValveDesinfection reliefValveDesinfection = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext select = this.prepareDSLContext(this.connection);
			List<Record> result = select.select()
					.from(Reliefvalvedesinfection.RELIEFVALVEDESINFECTION)
					.where(Reliefvalvedesinfection.RELIEFVALVEDESINFECTION.DESINFECTION_DATE.eq(desinfection.getDate()))
					.and(Reliefvalvedesinfection.RELIEFVALVEDESINFECTION.RELIEFVALVE_IDRELIEFVALVE.eq(reliefValve.getReliefValveId()))
					.and(Reliefvalvedesinfection.RELIEFVALVEDESINFECTION.WATERSYSTEM_IDWATERSYSTEM.eq(reliefValve.getWaterSystem().getWaterSystemId()))
					.and(Reliefvalvedesinfection.RELIEFVALVEDESINFECTION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(reliefValve.getWaterSystem().getCommunity().getCommunityId()))
					.and(Reliefvalvedesinfection.RELIEFVALVEDESINFECTION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(reliefValve.getWaterSystem().getCommunity().getSubBasin().getSubBasinId()))
					.limit(1)
					.fetch();
			
			for(Record record : result){
				reliefValveDesinfection = this.readReliefValveDesinfection(record, reliefValve, desinfection);
				break;
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.toString());
		}
		
		return reliefValveDesinfection;
	}

	@Override
	public boolean editReliefValveDesinfection(ReliefValveDesinfection newReliefValveDesinfection) {
		if(newReliefValveDesinfection == null)
			throw new NullArgumentException("newReliefValveDesinfection");
		int result = 0;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext update = this.prepareDSLContext(this.connection);
			result = update.update(Reliefvalvedesinfection.RELIEFVALVEDESINFECTION)
					.set(Reliefvalvedesinfection.RELIEFVALVEDESINFECTION.COUNT, newReliefValveDesinfection.getCount())
					.set(Reliefvalvedesinfection.RELIEFVALVEDESINFECTION.CHLORINEQTY, newReliefValveDesinfection.getChlorineQty())
					.set(Reliefvalvedesinfection.RELIEFVALVEDESINFECTION.DEMANDSPOONS, newReliefValveDesinfection.getDemandSpoons())
					.set(Reliefvalvedesinfection.RELIEFVALVEDESINFECTION.RETENTIONTIME, newReliefValveDesinfection.getRetentionTime())
					.where(Reliefvalvedesinfection.RELIEFVALVEDESINFECTION.DESINFECTION_DATE.eq(newReliefValveDesinfection.getDesinfection().getDate()))
					.and(Reliefvalvedesinfection.RELIEFVALVEDESINFECTION.RELIEFVALVE_IDRELIEFVALVE.eq(newReliefValveDesinfection.getReliefValve().getReliefValveId()))
					.and(Reliefvalvedesinfection.RELIEFVALVEDESINFECTION.WATERSYSTEM_IDWATERSYSTEM.eq(newReliefValveDesinfection.getReliefValve().getWaterSystem().getWaterSystemId()))
					.and(Reliefvalvedesinfection.RELIEFVALVEDESINFECTION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(newReliefValveDesinfection.getReliefValve().getWaterSystem().getCommunity().getCommunityId()))
					.and(Reliefvalvedesinfection.RELIEFVALVEDESINFECTION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(newReliefValveDesinfection.getReliefValve().getWaterSystem().getCommunity().getSubBasin().getSubBasinId()))
					.execute();
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.toString());
		}
		
		return result>0;
	}

	@Override
	public boolean removeReliefValveDesinfection(ReliefValveDesinfection reliefValveDesinfection) {
		if(reliefValveDesinfection == null)
			throw new NullArgumentException("reliefValveDesinfection");
		int result = 0;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext remove = this.prepareDSLContext(this.connection);
			result = remove.delete(Reliefvalvedesinfection.RELIEFVALVEDESINFECTION)
					.where(Reliefvalvedesinfection.RELIEFVALVEDESINFECTION.DESINFECTION_DATE.eq(reliefValveDesinfection.getDesinfection().getDate()))
					.and(Reliefvalvedesinfection.RELIEFVALVEDESINFECTION.RELIEFVALVE_IDRELIEFVALVE.eq(reliefValveDesinfection.getReliefValve().getReliefValveId()))
					.and(Reliefvalvedesinfection.RELIEFVALVEDESINFECTION.WATERSYSTEM_IDWATERSYSTEM.eq(reliefValveDesinfection.getReliefValve().getWaterSystem().getWaterSystemId()))
					.and(Reliefvalvedesinfection.RELIEFVALVEDESINFECTION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY.eq(reliefValveDesinfection.getReliefValve().getWaterSystem().getCommunity().getCommunityId()))
					.and(Reliefvalvedesinfection.RELIEFVALVEDESINFECTION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN.eq(reliefValveDesinfection.getReliefValve().getWaterSystem().getCommunity().getSubBasin().getSubBasinId()))
					.execute();
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.toString());
		}
		
		return result>0;
	}

	private void insertData(){
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext insert = this.prepareDSLContext(this.connection);
			Properties properties = ApplicationProperties.getInstance().getProperties();
			Path path  = FileSystems.getDefault().getPath(properties.getProperty(KEYS.RESOURCES_PATH), "Cloracion_INSERT_real_data.sql");
			String script = StringUtils.join(Files.readAllLines(path), "\n");
			insert.execute(script);
			this.closeConnection();
			log.debug("Loaded TEST DATA");
		} catch (SQLException | IOException e) {
			e.toString();
		}
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
			
			//TEMPORARY
			this.insertData();
		} catch (SQLException | IOException e) {
			log.warn(e.toString());
		}
		
		return true;
	}

	@Override
	public Backup addBackup(Backup backup) {
		if(backup == null)
			throw new NullArgumentException("backup");
		Backup newBackup = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext insert = this.prepareDSLContext(this.connection);
			int result = insert.insertInto(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Backup.BACKUP,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Backup.BACKUP.FILENAME,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Backup.BACKUP.FILEPATH,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Backup.BACKUP.SERVICENAME,
					org.ongawa.peru.chlorination.persistence.db.jooq.tables.Backup.BACKUP.LASTATTEMPT)
					.values(backup.getFileName(),
							backup.getFilePath(),
							backup.getService(),
							(backup.getLastAttempt()!=null)?new Timestamp(backup.getLastAttempt().getTime()):null)
					.execute();
			this.closeConnection();
			if(result>0)
				newBackup = this.getBackup(this.getMaxValue(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Backup.BACKUP, org.ongawa.peru.chlorination.persistence.db.jooq.tables.Backup.BACKUP.IDBACKUP));

		} catch (SQLException | DataAccessException e) {
			log.warn(e.toString());
		}
		
		return newBackup;
	}
	
	@Override
	public Backup getBackup(int idBackup) {
		Backup backup = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext select = this.prepareDSLContext(this.connection);
			Result<Record> result = select.select()
					.from(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Backup.BACKUP)
					.where(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Backup.BACKUP.IDBACKUP.eq(idBackup))
					.limit(1)
					.fetch();
			for(Record record : result){
				backup = this.readBackup(record);
				break;
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.toString());
		}
		
		return backup;
	}

	@Override
	public List<Backup> getBackups(String service) {
		if(service == null)
			throw new NullArgumentException("service");
		List<Backup> backups = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			backups = new ArrayList<Backup>();
			DSLContext select = this.prepareDSLContext(this.connection);
			Result<Record> result = select.select()
					.from(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Backup.BACKUP)
					.where(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Backup.BACKUP.SERVICENAME.eq(service))
					.fetch();
			for(Record record : result){
				backups.add(this.readBackup(record));
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.toString());
		}
		
		return backups;
	}

	@Override
	public List<Backup> getAllBackups() {
		List<Backup> backups = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			backups = new ArrayList<Backup>();
			DSLContext select = this.prepareDSLContext(this.connection);
			Result<Record> result = select.select()
					.from(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Backup.BACKUP)
					.fetch();
			for(Record record : result){
				backups.add(this.readBackup(record));
			}
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.toString());
		}
		
		return backups;
	}

	@Override
	public boolean editBackup(Backup backup) {
		if(backup == null)
			throw new NullArgumentException("backup");
		int result = 0;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext update = this.prepareDSLContext(this.connection);
			result = update.update(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Backup.BACKUP)
					.set(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Backup.BACKUP.LASTATTEMPT, (backup.getLastAttempt()!=null)?new Timestamp(backup.getLastAttempt().getTime()):null)
					.execute();
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.toString());
		}
		
		return result>0;
	}

	@Override
	public boolean removeBackup(Backup backup) {
		if(backup == null)
			throw new NullArgumentException("backup");
		int result = 0;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext remove = this.prepareDSLContext(this.connection);
			result = remove.delete(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Backup.BACKUP)
					.where(org.ongawa.peru.chlorination.persistence.db.jooq.tables.Backup.BACKUP.IDBACKUP.eq(backup.getId()))
					.execute();
			this.closeConnection();
		} catch (SQLException e) {
			log.warn(e.toString());
		}
		
		return result>0;
	}
}
