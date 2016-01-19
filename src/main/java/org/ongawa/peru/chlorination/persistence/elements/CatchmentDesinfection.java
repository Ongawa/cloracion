package org.ongawa.peru.chlorination.persistence.elements;

import java.sql.Timestamp;

public class CatchmentDesinfection {
	private Timestamp date;
	private int count;
	private double waterHeight;
	private double volume;
	private double chlorineConcentration;
	private double demandActiveChlorine;
	private double demand70Chlorine;
	private double demandSpoons;
	private double retentionTime;
	private Catchment catchment;
	
	public CatchmentDesinfection(Timestamp date, Catchment catchment, int count){
		this.date = date;
		this.catchment = catchment;
		this.count = count;
	}
	
	public CatchmentDesinfection(Timestamp date, Catchment catchment){
		this.date = date;
		this.catchment = catchment;
		this.count = 1;
	}

	public int getCount() {
		return this.count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public double getWaterHeight() {
		return this.waterHeight;
	}

	public void setWaterHeight(double waterHeight) {
		this.waterHeight = waterHeight;
	}

	public double getVolume() {
		return this.volume;
	}

	public void setVolume(double volume) {
		this.volume = volume;
	}

	public double getChlorineConcentration() {
		return this.chlorineConcentration;
	}

	public void setChlorineConcentration(double chlorineConcentration) {
		this.chlorineConcentration = chlorineConcentration;
	}

	public double getDemandActiveChlorine() {
		return this.demandActiveChlorine;
	}

	public void setDemandActiveChlorine(double demandActiveChlorine) {
		this.demandActiveChlorine = demandActiveChlorine;
	}

	public double getDemand70Chlorine() {
		return this.demand70Chlorine;
	}

	public void setDemand70Chlorine(double demand70Chlorine) {
		this.demand70Chlorine = demand70Chlorine;
	}

	public double getDemandSpoons() {
		return this.demandSpoons;
	}

	public void setDemandSpoons(double demandSpoons) {
		this.demandSpoons = demandSpoons;
	}

	public double getRetentionTime() {
		return this.retentionTime;
	}

	public void setRetentionTime(double retentionTime) {
		this.retentionTime = retentionTime;
	}

	public Timestamp getDate() {
		return this.date;
	}

	public Catchment getCatchment() {
		return this.catchment;
	}
}
