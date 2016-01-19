package org.ongawa.peru.chlorination.persistence.elements;

import org.ongawa.peru.chlorination.logic.DataCalculator;

/**
 * 
 * @author kiko
 *
 */
public abstract class Reservoir {
	
	private int reservoirId;
	private String name;
	private double width;
	private double length;
	private double height;
	private int count;
	private WaterSystem waterSystem;
	
	public Reservoir(int reservoirId, double width, double length, double height, WaterSystem waterSystem) {
		this.reservoirId = reservoirId;
		this.width = width;
		this.length = length;
		this.height = height;
		this.count = 1;
		this.waterSystem = waterSystem;
	}

	public Reservoir(double width, double length, double height, WaterSystem waterSystem) {
		this.reservoirId = -1;
		this.width = width;
		this.length = length;
		this.height = height;
		this.count = 1;
		this.waterSystem = waterSystem;
	}

	public String getElementName() {
		return name;
	}

	public void setElementName(String name) {
		this.name = name;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getReservoirId() {
		return reservoirId;
	}
	
	public void setReservoirId(int reservoirId){
		this.reservoirId = reservoirId;
	}

	public double getVolume() {
		return DataCalculator.volTanTam(this.getWidth(), this.getLength(), this.getHeight());
	}

	public double getCombinedVolume() {
		return this.getVolume()*this.getCount();
	}
	
	public WaterSystem getWaterSystem(){
		return this.waterSystem;
	}
}