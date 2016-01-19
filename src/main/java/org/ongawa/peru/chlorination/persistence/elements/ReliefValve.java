package org.ongawa.peru.chlorination.persistence.elements;

/**
 * @author kiko
 */
public class ReliefValve {
	private int reliefValveId;
	private String name;
	private double width;
	private double length;
	private double height;
	private int count;
	private WaterSystem waterSystem;
	
	public ReliefValve(int reliefValveId, double width, double length, double height, WaterSystem waterSystem) {
		super();
		this.reliefValveId = reliefValveId;
		this.width = width;
		this.length = length;
		this.height = height;
		this.count = 1;
		this.waterSystem = waterSystem;
	}

	public ReliefValve(double width, double length, double height, WaterSystem waterSystem) {
		super();
		this.reliefValveId = -1;
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

	public int getReliefValveId() {
		return reliefValveId;
	}

	public double getVolume() {
		return this.getWidth()*this.getLength()*this.getHeight();
	}

	public double getCombinedVolume() {
		return this.getVolume()*this.getCount();
	}
	
	public WaterSystem getWaterSystem(){
		return this.waterSystem;
	}
}
