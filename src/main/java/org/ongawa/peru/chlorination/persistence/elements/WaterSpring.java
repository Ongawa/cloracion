package org.ongawa.peru.chlorination.persistence.elements;

public class WaterSpring {
	/**
	 * @author Kiko
	 */
	
	private int waterSpringId;
	private String name;
	
	public WaterSpring(int waterSpringId, String name){
		super();
		this.waterSpringId = waterSpringId;
		this.name = name;
	}
	
	public WaterSpring(String name){
		super();
		this.waterSpringId = -1;
		this.name = name;
	}
	
	public int getWaterSpringId(){
		return this.waterSpringId;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
}
