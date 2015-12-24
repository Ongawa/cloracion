package org.ongawa.peru.chlorination.persistence.elements;

public class MeasuringPoint {
	/**
	 * @author Kiko
	 */

	private int measuringPointId;
	private String name;
	
	public MeasuringPoint(int measuringPointId, String name){
		super();
		this.measuringPointId = measuringPointId;
		this.name = name;
	}
	
	public MeasuringPoint(String name){
		super();
		this.measuringPointId = -1;
		this.name = name;
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
}
