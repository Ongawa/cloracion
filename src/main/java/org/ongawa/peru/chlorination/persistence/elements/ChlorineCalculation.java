package org.ongawa.peru.chlorination.persistence.elements;

import java.sql.Timestamp;

/**
 * @author Kiko
 */
public class ChlorineCalculation {
	
	//INPUTS
	private Timestamp date;
	private int familiesNum;
	private int population;
	private double tankVolume;
	private double endowment;
	private double chlorinePureness;
	private double naturalFlow;
	private double chlorinatedFlow;
	private String chlorineType;
	private double drippingHoursPerDay;
	private double chlorineDemand;
	private double chlorinePrice;
	private double reloadTime;
	private WaterSystem waterSystem;
	
	//RESULTS
	private double chlorineDosePerFortnight;
	private double chlorineDosePerMonth;
	private double drippingFlowInMl;
	private double drippingFlowInDrops;
	private double chlorinationCostPeriod;
	private double chlorinationCostMonth;
	
	public ChlorineCalculation(Timestamp date, WaterSystem waterSystem) {
		super();
		this.date = date;
		this.waterSystem = waterSystem;
	}

	public ChlorineCalculation(Timestamp date, int familiesNum, int population, double tankVolume, double endowment,
			double chlorinePureness, double naturalFlow, double chlorinatedFlow, String chlorineType,
			double drippingHoursPerDay, double chlorineDemand, double chlorinePrice, double reloadTime,
			WaterSystem waterSystem, double chlorineDosePerFortnight, double chlorineDosePerMonth,
			double drippingFlowInMl, double drippingFlowInDrops, double chlorinationCostPeriod , double chlorinationCostMonth) {
		super();
		this.date = date;
		this.familiesNum = familiesNum;
		this.population = population;
		this.tankVolume = tankVolume;
		this.endowment = endowment;
		this.chlorinePureness = chlorinePureness;
		this.naturalFlow = naturalFlow;
		this.chlorinatedFlow = chlorinatedFlow;
		this.chlorineType = chlorineType;
		this.drippingHoursPerDay = drippingHoursPerDay;
		this.chlorineDemand = chlorineDemand;
		this.chlorinePrice = chlorinePrice;
		this.reloadTime = reloadTime;
		this.waterSystem = waterSystem;
		this.chlorineDosePerFortnight = chlorineDosePerFortnight;
		this.chlorineDosePerMonth = chlorineDosePerMonth;
		this.drippingFlowInMl = drippingFlowInMl;
		this.drippingFlowInDrops = drippingFlowInDrops;
		this.chlorinationCostPeriod = chlorinationCostPeriod;
		this.chlorinationCostMonth = chlorinationCostMonth;
	}
	
	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}
	
	public int getFamiliesNum(){
		return familiesNum;
	}
	
	public void setFamiliesNum(int familiesNum){
		this.familiesNum = familiesNum;
	}

	public int getPopulation() {
		return population;
	}

	public void setPopulation(int population) {
		this.population = population;
	}

	public double getTankVolume() {
		return tankVolume;
	}

	public void setTankVolume(double tankVolume) {
		this.tankVolume = tankVolume;
	}

	public double getEndowment() {
		return endowment;
	}

	public void setEndowment(double endowment) {
		this.endowment = endowment;
	}

	public double getChlorinePureness() {
		return chlorinePureness;
	}

	public void setChlorinePureness(double chlorinePureness) {
		this.chlorinePureness = chlorinePureness;
	}

	public double getNaturalFlow() {
		return naturalFlow;
	}

	public void setNaturalFlow(double naturalFlow) {
		this.naturalFlow = naturalFlow;
	}

	public double getChlorinatedFlow() {
		return chlorinatedFlow;
	}

	public void setChlorinatedFlow(double chlorinatedFlow) {
		this.chlorinatedFlow = chlorinatedFlow;
	}

	public String getChlorineType() {
		return chlorineType;
	}

	public void setChlorineType(String chlorineType) {
		this.chlorineType = chlorineType;
	}

	public double getDrippingHoursPerDay() {
		return drippingHoursPerDay;
	}

	public void setDrippingHoursPerDay(double drippingHoursPerDay) {
		this.drippingHoursPerDay = drippingHoursPerDay;
	}

	public double getChlorineDemand() {
		return chlorineDemand;
	}

	public void setChlorineDemand(double chlorineDemand) {
		this.chlorineDemand = chlorineDemand;
	}

	public double getChlorinePrice() {
		return chlorinePrice;
	}

	public void setChlorinePrice(double chlorinePrice) {
		this.chlorinePrice = chlorinePrice;
	}

	public double getReloadTime() {
		return reloadTime;
	}

	public void setReloadTime(double reloadTime) {
		this.reloadTime = reloadTime;
	}

	public double getChlorineDosePerFortnight() {
		return chlorineDosePerFortnight;
	}

	public void setChlorineDosePerFortnight(double chlorineDosePerFortnight) {
		this.chlorineDosePerFortnight = chlorineDosePerFortnight;
	}

	public double getChlorineDosePerMonth() {
		return chlorineDosePerMonth;
	}

	public void setChlorineDosePerMonth(double chlorineDosePerMonth) {
		this.chlorineDosePerMonth = chlorineDosePerMonth;
	}

	public double getDrippingFlowInMl() {
		return drippingFlowInMl;
	}

	public void setDrippingFlowInMl(double drippingFlowInMl) {
		this.drippingFlowInMl = drippingFlowInMl;
	}

	public double getDrippingFlowInDrops() {
		return drippingFlowInDrops;
	}

	public void setDrippingFlowInDrops(double drippingFlowInDrops) {
		this.drippingFlowInDrops = drippingFlowInDrops;
	}

	public double getChlorinationCostPeriod() {
		return chlorinationCostPeriod;
	}
	
    public double getChlorinationCostMonth() {
        return chlorinationCostMonth;
    }

	public void setChlorinationCostPeriod(double chlorinationCost) {
		this.chlorinationCostPeriod = chlorinationCost;
	}

    public void setChlorinationCostMonth(double chlorinationCost) {
        this.chlorinationCostMonth = chlorinationCost;
    }

	public WaterSystem getWaterSystem() {
		return waterSystem;
	}

	public String toString(){
		return "WaterSystem: "+this.getWaterSystem().getName()+" Date: "+this.getDate();
	}
}
