package org.ongawa.peru.chlorination.persistence.elements;

public class MeasuringPoint {
	/**
	 * @author Kiko
	 */

	private int measuringPointId;
	private String name;
	private WaterSystem waterSystem;
	private WaterSpring waterSpring;
	
	public MeasuringPoint(int measuringPointId, String name, WaterSystem waterSystem, WaterSpring waterSpring){
		super();
		this.measuringPointId = measuringPointId;
		this.name = name;
		this.waterSystem = waterSystem;
		this.waterSpring = waterSpring;
	}
	
	public MeasuringPoint(String name, WaterSystem waterSystem, WaterSpring waterSpring){
		super();
		this.measuringPointId = -1;
		this.name = name;
		this.waterSystem = waterSystem;
		this.waterSpring = waterSpring;
	}
	
	public int getMeasuringPointId(){
		return this.measuringPointId;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public WaterSystem getWaterSystem(){
		return this.waterSystem;
	}
	
	public void setWaterSystem(WaterSystem waterSystem){
		this.waterSystem = waterSystem;
	}
	
	public WaterSpring getWaterSpring(){
		return this.waterSpring;
	}
	
	public void setWaterSpring(WaterSpring waterSpring){
		this.waterSpring = waterSpring;
	}
	
	public String toString(){
		return this.getMeasuringPointId()+" "+this.getName()+" from Water System: "+this.getWaterSystem().getName()+" and WaterSpring "+this.waterSpring.getName();
	}
}
