package org.ongawa.peru.chlorination.persistence.elements;

import java.sql.Timestamp;

/**
 * @author Kiko
 */
public class ChlorineCalculation {
	
	private Timestamp date;
	private int population;
	private double tankVolume;
	private double tankUsefulVolume;
	private double endowment;
	private double chlorinePureness;
	private double inputFlow;
	private int reloadTime;
	private double demandCLR;
	private double demandActiveChlorine;
	private double demandCommonProduct;
	private WaterSystem waterSystem;
	
	public ChlorineCalculation(Timestamp date, double demandCLR, double demandActiveChlorine, double demandCommonProduct, WaterSystem waterSystem){
		super();
		this.date = date;
		this.demandCLR = demandCLR;
		this.demandActiveChlorine = demandActiveChlorine;
		this.demandCommonProduct = demandCommonProduct;
		this.waterSystem = waterSystem;
	}
	
	public ChlorineCalculation(Timestamp date, int population, double tankVolume, double tankUsefulVolume,
			double endowment, double chlorinePureness, double inputFlow, int reloadTime, double demandCLR,
			double demandActiveChlorine, double demandCommonProduct, WaterSystem waterSystem) {
		super();
		this.date = date;
		this.population = population;
		this.tankVolume = tankVolume;
		this.tankUsefulVolume = tankUsefulVolume;
		this.endowment = endowment;
		this.chlorinePureness = chlorinePureness;
		this.inputFlow = inputFlow;
		this.reloadTime = reloadTime;
		this.demandCLR = demandCLR;
		this.demandActiveChlorine = demandActiveChlorine;
		this.demandCommonProduct = demandCommonProduct;
		this.waterSystem = waterSystem;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
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

	public double getTankUsefulVolume() {
		return tankUsefulVolume;
	}

	public void setTankUsefulVolume(double tankUsefulVolume) {
		this.tankUsefulVolume = tankUsefulVolume;
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

	public double getInputFlow() {
		return inputFlow;
	}

	public void setInputFlow(double inputFlow) {
		this.inputFlow = inputFlow;
	}

	public int getReloadTime() {
		return reloadTime;
	}

	public void setReloadTime(int reloadTime) {
		this.reloadTime = reloadTime;
	}

	public double getDemandCLR() {
		return demandCLR;
	}

	public void setDemandCLR(double demandCLR) {
		this.demandCLR = demandCLR;
	}

	public double getDemandActiveChlorine() {
		return demandActiveChlorine;
	}

	public void setDemandActiveChlorine(double demandActiveChlorine) {
		this.demandActiveChlorine = demandActiveChlorine;
	}

	public double getDemandCommonProduct() {
		return demandCommonProduct;
	}

	public void setDemandCommonProduct(double demandCommonProduct) {
		this.demandCommonProduct = demandCommonProduct;
	}

	public WaterSystem getWaterSystem() {
		return waterSystem;
	}

	public void setWaterSystem(WaterSystem waterSystem) {
		this.waterSystem = waterSystem;
	}
	
	public String toString(){
		return "WaterSystem: "+this.getWaterSystem().getName()+" Date: "+this.getDate()+" CLR: "+this.getDemandCLR()+" Active: "+this.getDemandActiveChlorine()+" Common: "+this.getDemandCommonProduct();
	}
}
