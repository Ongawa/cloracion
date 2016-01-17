package org.ongawa.peru.chlorination.persistence;

import java.sql.Timestamp;
import java.util.List;

import org.ongawa.peru.chlorination.persistence.elements.Catchment;
import org.ongawa.peru.chlorination.persistence.elements.ChlorineCalculation;
import org.ongawa.peru.chlorination.persistence.elements.Community;
import org.ongawa.peru.chlorination.persistence.elements.ConductionPipe;
import org.ongawa.peru.chlorination.persistence.elements.CubicReservoir;
import org.ongawa.peru.chlorination.persistence.elements.CubicReservoirDesinfection;
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

/**
 * @author Kiko
 */
public interface IDataSource {

	SubBasin addSubBasin(SubBasin subBasin);
	
	List<SubBasin> getSubBasins();

	SubBasin getSubBasin(int idSubBasin);

	SubBasin getSubBasin(String name);
	
	boolean editSubBasin(SubBasin subBasin);
	
	boolean removeSubBasin(SubBasin subBasin);
	
	Community addCommunity(Community community);

	List<Community> getCommunities(SubBasin subBasin);

	Community getCommunity(SubBasin subBasin, int idCommunity);

	Community getCommunity(SubBasin subBasin, String name);
	
	boolean editCommunity(Community community);
	
	boolean removeCommunity(Community community);
	
	WaterSystem addWaterSystem(WaterSystem waterSystem);

	List<WaterSystem> getWaterSystems(Community community);

	WaterSystem getWaterSystem(Community community, int idWaterSystem);

	WaterSystem getWaterSystem(Community community, String name);
	
	boolean editWaterSystem(WaterSystem waterSystem);
	
	boolean removeWaterSystem(WaterSystem waterSystem);
	
	WaterSpring addWaterSpring(WaterSpring waterSpring);

	List<WaterSpring> getWaterSprings();

	WaterSpring getWaterSpring(int idWaterSpring);

	WaterSpring getWaterSpring(String name);

	List<WaterSpring> getWaterSprings(WaterSystem waterSystem);
	
	boolean editWaterSpring(WaterSpring waterSpring);
	
	boolean removeWaterSpring(WaterSpring waterSpring);
	
	boolean addWaterSpringToWaterSystem(WaterSystem waterSystem, WaterSpring waterSpring);
	
	List<WaterSpring> getWaterSpringsInWaterSystem(WaterSystem waterSystem);
	
	boolean removeWaterSpringOfWaterSystem(WaterSystem waterSystem, WaterSpring waterSpring);
	
	MeasuringPoint addMeasuringPoint(MeasuringPoint measuringPoint);

	List<MeasuringPoint> getMeasuringPoints(WaterSystem waterSystem, WaterSpring waterSpring);

	MeasuringPoint getMeasuringPoint(WaterSystem waterSystem, WaterSpring waterSpring, int idMeasuringPoint);
	
	MeasuringPoint getMeasuringPoint(WaterSystem waterSystem, WaterSpring waterSpring, String name);
	
	boolean editMeasuringPoint(MeasuringPoint measuringPoint);
	
	boolean removeMeasuringPoint(MeasuringPoint measuringPoint);
	
	MeasuredFlow addMeasuredFlow(MeasuredFlow measuredFlow);

	List<MeasuredFlow> getMeasuredFlows(MeasuringPoint measuringPoint);

	List<MeasuredFlow> getMeasuredFlows(Timestamp beginDate, Timestamp endDate, MeasuringPoint measuringPoint);

	MeasuredFlow getMeasuredFlow(Timestamp date, MeasuringPoint measuringPoint);
	
	MeasuredFlow getLastMeasuredFlow(MeasuringPoint measuringPoint);
	
	boolean editMeasuredFlow(Timestamp oldMeasuredFlowDate, MeasuredFlow newMeasuredFlow);
	
	boolean removeMeasuredFlow(MeasuredFlow measuredFlow);
	
	ChlorineCalculation addChlorineCalculation(ChlorineCalculation chlorineCalculation);

	List<ChlorineCalculation> getChlorineCalculations(WaterSystem waterSystem);

	List<ChlorineCalculation> getChlorineCalculations(Timestamp beginDate, Timestamp endDate, WaterSystem waterSystem);

	ChlorineCalculation getChlorineCalculation(Timestamp date, WaterSystem waterSystem);
	
	ChlorineCalculation getLastChlorineCalculation(WaterSystem waterSystem); 
	
	boolean editChlorineCalcuilation(Timestamp oldChlorineCalculationDate, ChlorineCalculation newChlorineCalculation);
	
	boolean removeChlorineCalculation(ChlorineCalculation chlorineCalculation);
	
	CubicReservoir addCubicReservoir(CubicReservoir cubicReservoir);
	
	List<CubicReservoir> getCubicReservoirs(WaterSystem waterSystem);
	
	CubicReservoir getCubicReservoir(WaterSystem waterSystem, int cubicReservoirId);
	
	boolean editCubicReservoir(CubicReservoir cubicReservoir);
	
	boolean removeCubicReservoir(CubicReservoir cubicReservoir);
	
	Catchment addCatchment(Catchment catchment);
	
	List<Catchment> getCatchments(WaterSystem waterSystem);
	
	Catchment getCatchment(WaterSystem waterSystem, int catchmentId);
	
	boolean editCatchment(Catchment catchment);
	
	boolean removeCatchment(Catchment catchment);
	
	DistributionPipe addDistributionPipe(DistributionPipe pipe);
	
	List<DistributionPipe> getDistributionPipes(WaterSystem waterSystem);
	
	DistributionPipe getDistributionPipe(WaterSystem waterSystem, int pipeId);
	
	ConductionPipe addConductionPipe(ConductionPipe pipe);
	
	List<ConductionPipe> getConductionPipes(WaterSystem waterSystem);
	
	ConductionPipe getConductionPipe(WaterSystem waterSystem, int pipeId);
	
	boolean editPipe(Pipe pipe);
	
	boolean removePipe(Pipe pipe);
	
	ReliefValve addReliefValve(ReliefValve reliefValve);
	
	List<ReliefValve> getReliefValves(WaterSystem waterSystem);
	
	ReliefValve getReliefValve(WaterSystem waterSystem, int reliefValveId);
	
	boolean editReliefValve(ReliefValve reliefValve);
	
	boolean removeReliefValve(ReliefValve reliefValve);
	
	CubicReservoirDesinfection addCubicReservoirDesinfection(CubicReservoirDesinfection cubicReservoirDesinfection);
	
	List<CubicReservoirDesinfection> getCubicReservoirDesinfections(CubicReservoir cubicReservoir);
	
	List<CubicReservoirDesinfection> getCubicReservoirDesinfections(Timestamp beginDate, Timestamp endDate, CubicReservoir cubicReservoir);
	
	CubicReservoirDesinfection getCubicReservoirDesinfection(Timestamp date, CubicReservoir cubicReservoir);
	
	CubicReservoirDesinfection getLastCubicReservoirDesinfection(CubicReservoir cubicReservoir);
	
	boolean editCubicReservoirDesinfection(Timestamp oldCubicReservoirDesinfection, CubicReservoirDesinfection newCubicReservoirDesinfection);
	
	boolean removeCubicReservoirDesinfection(Timestamp date, CubicReservoir cubicReservoir);
	
	PipeDesinfection addPipeDesinfection(PipeDesinfection pipeDesinfection);
	
	List<PipeDesinfection> getPipeDesinfections(Pipe pipe);
	
	List<PipeDesinfection> getPipeDesinfections(Timestamp beginDate, Timestamp endDate, Pipe pipe);
	
	PipeDesinfection getPipeDesinfection(Timestamp date, Pipe pipe);
	
	PipeDesinfection getLastPipeDesinfection(Pipe pipe);
	
	boolean editPipeDesinfection(Timestamp oldPipeDesinfection, PipeDesinfection newPipeDesinfection);
	
	boolean removePipeDesinfection(Timestamp date, Pipe pipe);
	
	ReliefValveDesinfection addReliefValveDesinfection(ReliefValveDesinfection reliefValveDesinfection);
	
	List<ReliefValveDesinfection> getReliefValveDesinfections(ReliefValve reliefValve);
	
	List<ReliefValveDesinfection> getReliefValveDesinfections(Timestamp beginDate, Timestamp endDate, ReliefValve reliefValve);
	
	ReliefValveDesinfection getReliefValveDesinfection(Timestamp date, ReliefValve reliefValve);
	
	ReliefValveDesinfection getLastReliefValveDesinfection(ReliefValve reliefValve);
	
	boolean editReliefValveDesinfection(Timestamp oldReliefValveDesinfection, ReliefValveDesinfection newReliefValveDesinfection);
	
	boolean removeReliefValveDesinfection(Timestamp date, ReliefValveDesinfection reliefValveDesinfection);
	
	boolean createInitialEnvironment();
}