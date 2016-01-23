package org.ongawa.peru.chlorination.persistence.elements;

import java.sql.Timestamp;

/**
 * 
 * @author kiko
 *
 */
public class Desinfection {
	
	private WaterSystem waterSystem;
	private Timestamp date;
	private String chlorineType;
	private double chlorinePureness;
	private double chlorinePrice;
	
	public Desinfection(WaterSystem waterSystem, Timestamp date, String chlorineType, double chlorinePureness,
			double chlorinePrice) {
		super();
		this.waterSystem = waterSystem;
		this.date = date;
		this.chlorineType = chlorineType;
		this.chlorinePureness = chlorinePureness;
		this.chlorinePrice = chlorinePrice;
	}

	public String getChlorineType() {
		return chlorineType;
	}

	public void setChlorineType(String chlorineType) {
		this.chlorineType = chlorineType;
	}

	public double getChlorinePureness() {
		return chlorinePureness;
	}

	public void setChlorinePureness(double chlorinePureness) {
		this.chlorinePureness = chlorinePureness;
	}

	public double getChlorinePrice() {
		return chlorinePrice;
	}

	public void setChlorinePrice(double chlorinePrice) {
		this.chlorinePrice = chlorinePrice;
	}

	public WaterSystem getWaterSystem() {
		return waterSystem;
	}

	public Timestamp getDate() {
		return date;
	}
}
