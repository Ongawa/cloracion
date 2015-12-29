package org.ongawa.peru.chlorination.persistence;

import java.sql.Timestamp;
import java.util.List;

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

/**
 * @author Kiko
 */
public interface IDataSource {

	SubBasin addSubBasin(SubBasin subBasin);
	
	List<SubBasin> getSubBasins();

	SubBasin getSubBasin(int idSubBasin);

	SubBasin getSubBasin(String name);
	
	boolean removeSubBasin(SubBasin subBasin);
	
	Community addCommunity(Community community);

	List<Community> getCommunities(SubBasin subBasin);

	Community getCommunity(SubBasin subBasin, int idCommunity);

	Community getCommunity(SubBasin subBasin, String name);
	
	boolean removeCommunity(Community community);
	
	WaterSystem addWaterSystem(WaterSystem waterSystem);

	List<WaterSystem> getWaterSystems(Community community);

	WaterSystem getWaterSystem(Community community, int idWaterSystem);

	WaterSystem getWaterSystem(Community community, String name);
	
	boolean removeWaterSystem(WaterSystem waterSystem);
	
	WaterSpring addWaterSpring(WaterSpring waterSpring);

	List<WaterSpring> getWaterSprings();

	WaterSpring getWaterSpring(int idWaterSpring);

	WaterSpring getWaterSpring(String name);

	List<WaterSpring> getWaterSprings(WaterSystem waterSystem);
	
	boolean removeWaterSpring(WaterSpring waterSpring);
	
	MeasuringPoint addMeasuringPoint(MeasuringPoint measuringPoint);

	List<MeasuringPoint> getMeasuringPoints(WaterSystem waterSystem, WaterSpring waterSpring);

	MeasuringPoint getMeasuringPoint(WaterSystem waterSystem, WaterSpring waterSpring, int idMeasuringPoint);
	
	MeasuringPoint getMeasuringPoint(WaterSystem waterSystem, WaterSpring waterSpring, String name);
	
	boolean removeMeasuringPoint(MeasuringPoint measuringPoint);
	
	MeasuredFlow addMeasuredFlow(MeasuredFlow measuredFlow);

	List<MeasuredFlow> getMeasuredFlows(MeasuringPoint measuringPoint);

	List<MeasuredFlow> getMeasuredFlows(Timestamp beginDate, Timestamp endDate, MeasuringPoint measuringPoint);

	MeasuredFlow getMeasuredFlow(Timestamp date, MeasuringPoint measuringPoint);
	
	boolean removeMeasuredFlow(MeasuredFlow measuredFlow);
	
	ChlorineCalculation addChlorineCalculation(ChlorineCalculation chlorineCalculation);

	List<ChlorineCalculation> getChlorineCalculations(WaterSystem waterSystem);

	List<ChlorineCalculation> getChlorineCalculations(Timestamp beginDate, Timestamp endDate, WaterSystem waterSystem);

	ChlorineCalculation getChlorineCalculation(Timestamp date, WaterSystem waterSystem);
	
	boolean removeChlorineCalculation(ChlorineCalculation chlorineCalculation);
	
	CubicReservoir addCubicReservoir(CubicReservoir cubicReservoir);
	
	List<CubicReservoir> getCubicReservoirs(WaterSystem waterSystem);
	
	CubicReservoir getCubicReservoir(WaterSystem waterSystem, int cubicReservoirId);
	
	boolean removeCubicReservoir(CubicReservoir cubicReservoir);
	
	Pipe addPipe(Pipe pipe);
	
	List<Pipe> getPipes(WaterSystem waterSystem);
	
	Pipe getPipe(WaterSystem waterSystem, int pipeId);
	
	boolean removePipe(Pipe pipe);
	
	ReliefValve addReliefValve(ReliefValve reliefValve);
	
	List<ReliefValve> getReliefValves(WaterSystem waterSystem);
	
	ReliefValve getReliefValve(WaterSystem waterSystem, int reliefValveId);
	
	boolean removeReliefValve(ReliefValve reliefValve);
}