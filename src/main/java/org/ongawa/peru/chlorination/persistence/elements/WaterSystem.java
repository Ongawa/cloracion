package org.ongawa.peru.chlorination.persistence.elements;

/**
 * @author Kiko
 */
public class WaterSystem {

	private int waterSystemId;
	private String name;
	private int familiesNum;
	private int population; //familiesNum * 5
	private int populationForecast;
	private double growingIndex;
	private double endowment;
	private int JASSNum;
	private double futureNeededFlow;
	private double reservoirVolume;
	private int systemElevation;
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
	
	public double getEndowment(){
		return this.endowment;
	}
	
	public void setEndowment(double endowment){
		this.endowment = endowment;
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
	
	public int getSystemElevation() {
		return systemElevation;
	}
	
	public void setSystemElevation(int systemElevation) {
		this.systemElevation = systemElevation;
	}
	
	public Community getCommunity() {
		return community;
	}
	
	public void setCommunity(Community community) {
		this.community = community;
	}
	
	public String toString(){
		return this.getWaterSystemId()+" "+this.getName()+" from community "+this.getCommunity().getName();
	}
}