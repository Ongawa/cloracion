package org.ongawa.peru.chlorination.persistence.elements;

/**
 * @author Kiko
 */
public abstract class Pipe {
	
	public static final int DISTRIBUTION_PIPE_TYPE = 1;
	public static final int CONDUCTION_PIPE_TYPE = 2;

	private int pipeId;
	private String name;
	private double diameter;
	private double length;
	private int count;
	private WaterSystem waterSystem;
	
	public Pipe(int pipeId, double diameter, double length, WaterSystem waterSystem) {
		super();
		this.pipeId = pipeId;
		this.diameter = diameter;
		this.length = length;
		this.waterSystem = waterSystem;
		this.count = 1;
	}

	public Pipe(double diameter, double length, WaterSystem waterSystem) {
		super();
		this.pipeId = -1;
		this.diameter = diameter;
		this.length = length;
		this.waterSystem = waterSystem;
	}

	public int getPipeId() {
		return pipeId;
	}

	public void setPipeId(int pipeId) {
		this.pipeId = pipeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getDiameter() {
		return diameter;
	}

	public void setDiameter(double diameter) {
		this.diameter = diameter;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public double getVolume() {
		return this.getLength()*Math.pow(this.getDiameter()/2, 2)*Math.PI;
	}

	public double getCombinedVolume() {
		return this.getVolume()*this.getCount();
	}
	
	public WaterSystem getWaterSystem() {
		return waterSystem;
	}
}
