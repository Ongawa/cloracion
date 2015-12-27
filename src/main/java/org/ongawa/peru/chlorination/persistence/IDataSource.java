package org.ongawa.peru.chlorination.persistence;

import java.sql.Timestamp;
import java.util.List;

import org.ongawa.peru.chlorination.persistence.elements.ChlorineCalculation;
import org.ongawa.peru.chlorination.persistence.elements.Community;
import org.ongawa.peru.chlorination.persistence.elements.MeasuredFlow;
import org.ongawa.peru.chlorination.persistence.elements.MeasuringPoint;
import org.ongawa.peru.chlorination.persistence.elements.SubBasin;
import org.ongawa.peru.chlorination.persistence.elements.WaterSpring;
import org.ongawa.peru.chlorination.persistence.elements.WaterSystem;

public interface IDataSource {

	List<SubBasin> getSubBasins();

	SubBasin getSubBasin(int idSubBasin);

	SubBasin getSubBasin(String name);

	List<Community> getCommunities(SubBasin subBasin);

	Community getCommunity(SubBasin subBasin, int idCommunity);

	Community getCommunity(SubBasin subBasin, String name);

	List<WaterSystem> getWaterSystems(Community community);

	WaterSystem getWaterSystem(Community community, int idWaterSystem);

	WaterSystem getWaterSystem(Community community, String name);

	List<WaterSpring> getWaterSprings();

	WaterSpring getWaterSprings(int idWaterSpring);

	WaterSpring getWaterSprings(String name);

	List<WaterSpring> getWaterSprings(WaterSystem waterSystem);

	List<MeasuringPoint> getMeasuringPoints(WaterSystem waterSystem, WaterSpring waterSpring);

	MeasuringPoint getMeasuringPoint(WaterSystem waterSystem, WaterSpring waterSpring, int idMeasuringPoint);

	List<MeasuredFlow> getMeasuredFlows(MeasuringPoint measuringPoint);

	List<MeasuredFlow> getMeasuredFlows(Timestamp beginDate, Timestamp endDate, MeasuringPoint measuringPoint);

	MeasuredFlow getMeasuredFlow(Timestamp date, MeasuringPoint measuringPoint);

	List<ChlorineCalculation> getChlorineCalculations(WaterSystem waterSystem);

	List<ChlorineCalculation> getChlorineCalculations(Timestamp beginDate, Timestamp endDate, WaterSystem waterSystem);

	ChlorineCalculation getChlorineCalculation(Timestamp date, WaterSystem waterSystem);

}