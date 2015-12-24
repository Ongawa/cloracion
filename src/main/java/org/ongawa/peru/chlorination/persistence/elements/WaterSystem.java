package org.ongawa.peru.chlorination.persistence.elements;

public class WaterSystem {
	
	/**
	 * @author Kiko
	 */

	private int waterSystemId;
	private String name;
	private int familiesNum;
	private int population; //familiesNum * 5
	private int populationForecast;
	private double growingIndex;
	private int JASSNum;
	private double futureNeededFlow;
	private double reservoirVolume;
	private double tankASide;
	private double tankBSide;
	private double tankHeight;
	private double tankVolume;
	private double tankUsefulVolume;
	private double systemElevation;
	private Community community;
	
	public WaterSystem(int waterSystemId, String name, Community community) {
		super();
		this.waterSystemId = waterSystemId;
		this.name = name;
		this.community = community;
	}
	
	public WaterSystem(String name, Community community) {
		super();
		this.waterSystemId = -1;
		this.name = name;
		this.community = community;
	}
	
	public int getWaterSystemId() {
		return waterSystemId;
	}
	
	public void setWaterSystemId(int waterSystemId) {
		this.waterSystemId = waterSystemId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getFamiliesNum() {
		return familiesNum;
	}
	
	public void setFamiliesNum(int familiesNum) {
		this.familiesNum = familiesNum;
	}
	
	public int getPopulation() {
		return population;
	}
	
	public void setPopulation(int population) {
		this.population = population;
	}
	
	public int getPopulationForecast() {
		return populationForecast;
	}
	
	public void setPopulationForecast(int populationForecast) {
		this.populationForecast = populationForecast;
	}
	
	public double getGrowingIndex() {
		return growingIndex;
	}
	
	public void setGrowingIndex(double growingIndex) {
		this.growingIndex = growingIndex;
	}
	
	public int getJASSNum() {
		return JASSNum;
	}
	
	public void setJASSNum(int jASSNum) {
		JASSNum = jASSNum;
	}
	
	public double getFutureNeededFlow() {
		return futureNeededFlow;
	}
	
	public void setFutureNeededFlow(double futureNeededFlow) {
		this.futureNeededFlow = futureNeededFlow;
	}
	
	public double getReservoirVolume() {
		return reservoirVolume;
	}
	
	public void setReservoirVolume(double reservoirVolume) {
		this.reservoirVolume = reservoirVolume;
	}
	
	public double getTankASide() {
		return tankASide;
	}
	
	public void setTankASide(double tankASide) {
		this.tankASide = tankASide;
	}
	
	public double getTankBSide() {
		return tankBSide;
	}
	
	public void setTankBSide(double tankBSide) {
		this.tankBSide = tankBSide;
	}
	
	public double getTankHeight() {
		return tankHeight;
	}
	
	public void setTankHeight(double tankHeight) {
		this.tankHeight = tankHeight;
	}
	
	public double getTankVolume() {
		return tankVolume;
	}
	
	public void setTankVolume(double tankVolume) {
		this.tankVolume = tankVolume;
	}
	
	public double getTankUsefulVolume() {
		return tankUsefulVolume;
	}
	
	public void setTankUsefulVolume(double tankUsefulVolume) {
		this.tankUsefulVolume = tankUsefulVolume;
	}
	
	public double getSystemElevation() {
		return systemElevation;
	}
	
	public void setSystemElevation(double systemElevation) {
		this.systemElevation = systemElevation;
	}
	
	public Community getCommunity() {
		return community;
	}
	
	public void setCommunity(Community community) {
		this.community = community;
	}
}