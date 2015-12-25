package org.ongawa.peru.chlorination.persistence.elements;

public class SubBasin {
	/**
	 * @author Kiko
	 */
	
	private int subBasinId;
	private String name;
		
	public SubBasin(int subBasinId, String name) {
		super();
		this.subBasinId = subBasinId;
		this.name = name;
	}
	
	public SubBasin(String name) {
		super();
		this.subBasinId = -1;
		this.name = name;
	}
	
	public int getSubBasinId() {
		return subBasinId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String toString(){
		return this.getSubBasinId()+" "+this.getName();
	}
}
